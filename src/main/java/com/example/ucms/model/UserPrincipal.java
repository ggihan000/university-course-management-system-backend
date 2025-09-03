package com.example.ucms.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserPrincipal implements UserDetails {

    User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority authority = resolveRoleIdToAuthority(user.getRole_id());
        System.out.println("Resolved authority for user " + user.getUserId() + " → " + authority.getAuthority());
        return Collections.singletonList(authority);
    }

    private GrantedAuthority resolveRoleIdToAuthority(Long roleId) {
        // Map IDs to role names (adjust as needed)
        Map<Long, String> roleMap = Map.of(
                1L, "ADMIN",
                2L, "STUDENT"
        );

        String roleName = roleMap.getOrDefault(roleId, "DEFAULT_ROLE");
        return new SimpleGrantedAuthority("ROLE_" + roleName);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}

