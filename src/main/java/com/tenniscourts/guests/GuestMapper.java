package com.tenniscourts.guests;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GuestMapper {
	
	GuestMapper INSTANCE = Mappers.getMapper( GuestMapper.class );


    Guest map(GuestDTO source);

    GuestDTO map(Guest source);

    List<GuestDTO> map(List<Guest> source);
}
