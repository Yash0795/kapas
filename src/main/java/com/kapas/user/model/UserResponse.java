package com.kapas.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Integer id;

    private String userName;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private String isActive;

    private String description;

    private String role;

}
