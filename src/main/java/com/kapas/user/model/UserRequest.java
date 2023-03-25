package com.kapas.user.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @Size(min = 5, max = 75, message = "Email must be between 5 and 75 characters")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Mobile is mandatory")
    @Size(min = 10, max = 15, message = "Mobile must be between 10 and 15 characters")
    private String mobile;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
    message = "Password must contain minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character (@$!%*?&)")
    private String password;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Role Id is mandatory")
    private Integer roleId;

}
