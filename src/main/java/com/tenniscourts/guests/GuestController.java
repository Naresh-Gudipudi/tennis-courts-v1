package com.tenniscourts.guests;

import java.util.List;

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
@RequestMapping("/guest")
public class GuestController extends BaseRestController {
	
	private final GuestService guestService;
	
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public ResponseEntity<Void> addGuest(@RequestBody GuestDTO guestDTO){
        return ResponseEntity.created(locationByEntity(guestService.createGuest(guestDTO).getId())).build();
	}
	
	@RequestMapping(value="/getbyid",method=RequestMethod.GET)
	public ResponseEntity<GuestDTO> FindGuestById(@RequestParam("id") Long guestId){
        return ResponseEntity.ok(guestService.getGuestById(guestId));
	}

	@RequestMapping(value="/getbyname",method=RequestMethod.GET)
	public ResponseEntity<List<GuestDTO>> FindGuestByName(@RequestParam("name") String guestName){
        return ResponseEntity.ok(guestService.getGuestByName(guestName));
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.DELETE)
	public ResponseEntity<GuestDTO> deleteGuestById(@RequestBody GuestDTO guestDTO){
        return ResponseEntity.ok(guestService.deleteGuest(guestDTO));
	}
	
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public ResponseEntity<Void> updateGuest(@RequestBody GuestDTO guestDTO){
        return ResponseEntity.created(locationByEntity(guestService.updateGuest(guestDTO).getId())).build();
	}
	
	@RequestMapping(value="/getall",method=RequestMethod.GET)
	public ResponseEntity<List<GuestDTO>> getAll(){
        return ResponseEntity.ok(guestService.getAllGuests());
	}
}
