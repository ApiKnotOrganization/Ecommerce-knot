package com.knot.gateway.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String role;
    private String first_name;
    private String last_name;
    private String email;
    private String phone_country_code;
    private String phone_number;
}
