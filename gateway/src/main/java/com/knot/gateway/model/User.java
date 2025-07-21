package com.knot.gateway.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;
    private Long id_payment;
    private String phone_country_code;
    private String phone_number;
    private String first_name;
    private String last_name;
    private String email;
    private String username;
    private String password;
    private String role;
    private Date created_at;
    private Date updated_at;
}
