package com.knot.gateway.service;

import com.knot.gateway.mapper.AddressMapper;
import com.knot.gateway.model.Address;
import com.knot.gateway.model.dto.AddressDTO;
import com.knot.gateway.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAddressService {
    @Autowired
    private AddressRepository addressRepository;

    public AddressDTO save(AddressDTO address) {
        Address addressEntity = AddressMapper.INSTANCE.toEntity(address);
        return AddressMapper.INSTANCE
                .toDTO(addressRepository.save(addressEntity));
    }

    public AddressDTO update(Long id, AddressDTO address) {
        Address addressEntity = AddressMapper.INSTANCE.toEntity(address);
        addressEntity.setId(id);
        return AddressMapper.INSTANCE
                .toDTO(addressRepository.save(addressEntity));
    }

    public void delete(Long id) {
        addressRepository.deleteById(id);
    }
}
