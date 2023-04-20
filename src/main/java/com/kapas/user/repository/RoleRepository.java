package com.kapas.user.repository;

import com.kapas.user.entity.Role;
import com.kapas.workorder.entity.Workorder;
import io.hypersistence.utils.spring.repository.BaseJpaRepository;

public interface RoleRepository extends BaseJpaRepository<Role, Integer> {

    Role getRoleByRoleName(String roleName);


}