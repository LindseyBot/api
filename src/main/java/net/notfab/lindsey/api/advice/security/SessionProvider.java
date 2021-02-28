package net.notfab.lindsey.api.advice.security;

import net.notfab.lindsey.api.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SessionProvider {

    public boolean hasSession() {
        return SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal() != null;
    }

    public User getUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

}
