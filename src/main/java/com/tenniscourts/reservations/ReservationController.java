package com.tenniscourts.reservations;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tenniscourts.config.BaseRestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationController extends BaseRestController {

	
    private final ReservationService reservationService;

	@RequestMapping(value="/create", method=RequestMethod.POST)
    public ResponseEntity<Void> bookReservation(@RequestBody CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }
	
	@RequestMapping(method=RequestMethod.GET, value="/findbyid")
    public ResponseEntity<ReservationDTO> findReservation(@RequestParam("id") Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

	@RequestMapping(value="/cancel", method=RequestMethod.PUT)
    public ResponseEntity<ReservationDTO> cancelReservation(Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

	@RequestMapping(value="/reschedule", method=RequestMethod.PUT)
    public ResponseEntity<ReservationDTO> rescheduleReservation(Long reservationId, Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
