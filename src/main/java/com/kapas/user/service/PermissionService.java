package com.kapas.user.service;

import com.kapas.user.entity.Permission;
import com.kapas.user.entity.Role;
import com.kapas.user.entity.Scope;
import com.kapas.user.entity.User;
import com.kapas.user.model.PermissionEnum;
import com.kapas.user.model.ScopeEnum;
import com.kapas.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public void validatePermissionOn(User principal, ScopeEnum scope, PermissionEnum permission) {
        principal.setRole(userRepository.findUserById(principal.getId()).getRole());
        if (!hasScopeAndPermission(principal.getRole(), scope.name(), permission.name())) {
            throw new RuntimeException(String.format("We apologize we cannot honor your request," +
                    " You do not have appropriate permissions to perform this action. Scope: %s. Permission: %s.",
                    scope.name(), permission.name()));
        }
    }

    public boolean hasScopeAndPermission(Role userRole, String scope, String permission) {
        Set<Scope> scopes = userRole.getScopes().stream()
                .filter(s -> s.getName().equals(scope))
                .collect(Collectors.toSet());
        if (scopes.isEmpty()) {
            return false;
        }
        Set<Permission> permissions = scopes.stream()
                .flatMap(s -> s.getPermissions().stream())
                .filter(p -> p.getName().equals(permission))
                .collect(Collectors.toSet());
        return !permissions.isEmpty();
    }

    public boolean hasPermission(Role userRole, String permission) {
        Set<Scope> scopes = userRole.getScopes();
        Set<Permission> permissions = scopes.stream()
                .flatMap(s -> s.getPermissions().stream())
                .filter(p -> p.getName().equals(permission))
                .collect(Collectors.toSet());
        return !permissions.isEmpty();
    }
}
