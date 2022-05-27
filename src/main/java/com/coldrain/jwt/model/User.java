package com.coldrain.jwt.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id @GeneratedValue
    public Long id;
    private String username;
    private String password;
    private String roles;
}
