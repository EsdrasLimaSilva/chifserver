package me.edu.chifserver.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class FirebaseAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("Hello Authenticaion");
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // checking if the authetication Object is compatible with this provider
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
        return true;
    }
}
