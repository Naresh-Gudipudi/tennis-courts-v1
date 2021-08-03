package com.tenniscourts.guests;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestDTO {

	private Long id;
	private String name;
	private LocalDateTime dateCreate;
	private String ipNumberCreate;
}
