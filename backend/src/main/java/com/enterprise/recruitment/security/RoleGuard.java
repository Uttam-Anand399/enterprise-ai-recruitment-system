package com.enterprise.recruitment.security;

import com.enterprise.recruitment.entity.AppUser;
import com.enterprise.recruitment.entity.Role;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class RoleGuard {

    public void requireRole(AppUser user, Role role) {
        if (user == null || user.getRole() != role) {
            throw new AccessDeniedException("Insufficient role");
        }
    }

    public void requireAnyRole(AppUser user, Role... roles) {
        if (user == null) {
            throw new AccessDeniedException("Insufficient role");
        }

        for (Role role : roles) {
            if (user.getRole() == role) {
                return;
            }
        }

        throw new AccessDeniedException("Insufficient role");
    }
}
