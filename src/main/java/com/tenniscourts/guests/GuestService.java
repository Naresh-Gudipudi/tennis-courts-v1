package com.tenniscourts.guests;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GuestService {

	private final GuestRepository guestRepository;
	
	private final GuestMapper guestMapper;
	
	public GuestDTO createGuest(GuestDTO guestDTO) {
		Guest guest = guestMapper.map(guestDTO);
		return guestMapper.map(guestRepository.save(guest));
	}
	
	public GuestDTO updateGuest(GuestDTO guestDTO) {
		Optional<Guest> guestFromRepo=guestRepository.findById(guestDTO.getId());
		if(guestFromRepo.isPresent()) {
			guestFromRepo.get().setName(guestDTO.getName());
		return guestMapper.map(guestRepository.save(guestFromRepo.get()));
		}
        throw new UnsupportedOperationException("The Guest not exists");
	}
	
	public GuestDTO deleteGuest(GuestDTO guestDTO) {
		Optional<Guest> guestFromRepo=guestRepository.findById(guestDTO.getId());
		if(guestFromRepo.isPresent()) {
		guestRepository.delete(guestFromRepo.get());
		return guestMapper.map(guestFromRepo.get());
		}
        throw new UnsupportedOperationException("The Guest not exists");
	}
	
	public GuestDTO getGuestById(Long guestId) {
		Optional<Guest> guestFromRepo=guestRepository.findById(guestId);
		if(guestFromRepo.isPresent()) {
			return guestMapper.map(guestFromRepo.get());
		}
        throw new UnsupportedOperationException("The Guest not exists");
	}
	
	public List<GuestDTO> getAllGuests() {
		List<Guest> guestFromRepo=guestRepository.findAll();
		if(!guestFromRepo.isEmpty()) {
			return guestMapper.map(guestFromRepo);
		}
        throw new UnsupportedOperationException("There are no Guest avaliable");
	}
	
	public List<GuestDTO> getGuestByName(String guestName) {
		List<Guest> guestFromRepo=guestRepository.findByName(guestName);
		if(!guestFromRepo.isEmpty()) {
			return guestMapper.map(guestFromRepo);
		}
        throw new UnsupportedOperationException("There are no Guest available");
	}
}
