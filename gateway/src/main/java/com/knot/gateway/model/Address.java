package com.knot.gateway.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_address")
    private Long id;
    @Column(name = "id_user")
    private Long idUser;
    private String line_address_1;
    private String line_address_2;
    private String city;
    private String country;
    private String zip_code;
    @Column(name = "is_primary")
    private Boolean isPrimary;
    private Date created_at;
    private Date updated_at;
}
