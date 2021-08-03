package com.tenniscourts.reservations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestRepository;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReservationService {


    private final  ReservationRepository reservationRepository;
	
    private final  ReservationMapper reservationMapper;
    
    private final ScheduleRepository scheduleRepository;
    
    private final GuestRepository guestRepository;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
    	Optional<Schedule> schedule = scheduleRepository.findById(createReservationRequestDTO.getScheduleId());
    	Optional<Guest> guest = guestRepository.findById(createReservationRequestDTO.getGuestId());
    	Reservation reservation= new Reservation();
   	List<Reservation> isSchedulealreadyBooked = reservationRepository.findBySchedule_Id(createReservationRequestDTO.getScheduleId());
   	
    	if(isSchedulealreadyBooked.isEmpty() && schedule.isPresent() && guest.isPresent()) {
    		Schedule scheduleId = schedule.get();
    		scheduleId.addReservation(reservation);
    		scheduleId.setReservations(scheduleId.getReservations());
			reservation.setSchedule(scheduleId);
    		reservation.setGuest(guest.get());
    		reservation.setValue(new BigDecimal(10));
    		reservation.setRefundValue(new BigDecimal(10));
    		reservation.setReservationStatus(ReservationStatus.READY_TO_PLAY);
    		reservation=reservationRepository.save(reservation);    		
    		return reservationMapper.map(reservation);
    	}
        throw new UnsupportedOperationException("The slot for that tennis court is filled");
    }

    public ReservationDTO findReservation(Long reservationId) throws  EntityNotFoundException{
    
    		 return reservationRepository.findById(reservationId).map(reservationMapper::map).<EntityNotFoundException>orElseThrow(() -> {
    	            throw new EntityNotFoundException("Reservation not found.");
    	        });	
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) throws  EntityNotFoundException{
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    	
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (hours >= 24) {
            return reservation.getValue();
        }
        else if(hours >=12) {
        	return reservation.getValue().multiply(new BigDecimal(0.25));
        }
        else if(hours >= 2) {
        	return reservation.getValue().multiply(new BigDecimal(0.5));
        }else {
        	return reservation.getValue().multiply(new BigDecimal(0.75));
        }

    }

    /*TODO: This method actually not fully working, find a way to fix the issue when it's throwing the error:
            "Cannot reschedule to the same slot.*/
    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
        Reservation previousReservation = cancel(previousReservationId);

        if (scheduleId.equals(previousReservation.getSchedule().getId())) {
            throw new IllegalArgumentException("Cannot reschedule to the same slot.");
        }

        previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }
}
