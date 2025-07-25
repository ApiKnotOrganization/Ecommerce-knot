package com.knot.gateway.controller;

import com.knot.gateway.model.dto.AddressDTO;
import com.knot.gateway.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/address")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @PostMapping("/add")
    public ResponseEntity<?> addUserAddress(AddressDTO addressDTO) {
        return ResponseEntity.ok(userAddressService.save(addressDTO));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserAddress(AddressDTO addressDTO) {
        return ResponseEntity.ok(userAddressService.update(addressDTO.getId(), addressDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserAddress(@PathVariable("id") Long id) {
        userAddressService.delete(id);
        return ResponseEntity.ok("Address deleted successfully.");
    }
}
