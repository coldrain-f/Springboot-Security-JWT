package com.coldrain.jwt.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class User {

    @Id @GeneratedValue
    public Long id;
    private String username;
    private String password;
    private Role role; // USER, ADMIN

}
