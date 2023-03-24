package com.kapas.user.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserRequest {
    @NotBlank(message = "User Name is mandatory")
    @Size(min = 5, max = 75, message = "User Name must be between 5 and 75 characters")
    private String userName;

    @NotBlank(message = "First Name is mandatory")
    @Size(min = 5, max = 75, message = "First Name must be between 5 and 75 characters")
    private String firstName;

    @NotBlank(message = "Last Name is mandatory")
    @Size(min = 5, max = 75, message = "Last Name must be between 5 and 75 characters")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Size(min = 5, max = 75, message = "Email must be in the format xxx@xxx.com")
    private String email;

    @NotBlank(message = "Mobile is mandatory")
    @Size(min = 10, max = 15, message = "Mobile must be between 10 and 15 characters")
    private String mobile;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 4, message = "Password must be of minimum 4 characters")
    private String password;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Role Id is mandatory")
    private Integer roleId;

}
