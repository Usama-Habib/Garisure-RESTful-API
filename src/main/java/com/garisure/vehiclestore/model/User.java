package com.garisure.vehiclestore.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@ApiModel(description = "User Details")
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"})
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ApiModelProperty(notes = "User email address")
    private String username;
    private String password;
    @ApiModelProperty(notes = "User firebase unique Identifier")
    private String uid;
    @ApiModelProperty(notes = "User role such as ADMIN, SUPER ADMIN")
    private String role;
    @ApiModelProperty(notes = "User approval status")
    private int active;
    @Lob
    @ApiModelProperty(notes = "User JWT token provided by firebase")
    private String idToken;

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getRole() {
        return role;
    }

    public int isActive() {
        return active;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return this.getId() + " " + this.getUsername() + " " + this.getPassword();
    }
}
