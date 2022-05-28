package com.coldrain.jwt.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@Table(name = "USERS")
public class User {

    @Id @GeneratedValue
    public Long id;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // USER, ADMIN

}
