package com.kapas.user.mapper;

import com.kapas.user.entity.Role;
import com.kapas.user.entity.User;
import com.kapas.user.model.UserRequest;
import com.kapas.user.model.UserResponse;
import com.kapas.user.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Optional;


@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    private RoleRepository roleRepository;

    @Mapping(target = "userName", source = "userRequest.userName")
    @Mapping(target = "firstName", source = "userRequest.firstName")
    @Mapping(target = "lastName", source = "userRequest.lastName")
    @Mapping(target = "email", source = "userRequest.email")
    @Mapping(target = "mobile", source = "userRequest.mobile")
    @Mapping(target = "password", source = "userRequest.password")
    @Mapping(target = "role", source = "userRequest.roleId")
    @Mapping(target = "isActive", constant = "1")
    @Mapping(target = "description", source = "userRequest.description")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", source = "user.createdBy")
    @Mapping(target = "creationTime", ignore = true)
    @Mapping(target = "modificationTime", ignore = true)
    @Mapping(target = "modifiedBy", source = "user.modifiedBy")
    public abstract User userRequestToUser(UserRequest userRequest, User user);

    public Role mapRole(Integer roleId) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        return optionalRole.orElseThrow(() -> new RuntimeException("Role not valid"));
    }

    public abstract UserResponse userToUserResponse(User user);

    public String mapRole(Role role) {
        return role.getRoleName();
    }

}
