package com.knot.gateway.mapper;

import com.knot.gateway.model.Address;
import com.knot.gateway.model.dto.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);
    AddressDTO toDTO(Address address);
    Address toEntity(AddressDTO addressDTO);
}
