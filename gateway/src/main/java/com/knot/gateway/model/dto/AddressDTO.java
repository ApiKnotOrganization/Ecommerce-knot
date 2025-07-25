package com.knot.gateway.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDTO {
    private Long id;
    private Long idUser;
    private String line_address_1;
    private String line_address_2;
    private String city;
    private String country;
    private String zip_code;
    private Boolean isPrimary;
}
