package me.edu.chifserver.security.filters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class FirebaseFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        // if token was not provided
        if(header == null || !header.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }

        // retrieving the token
        String token = header.substring("Bearer".length());

        // trying to validate the token
        try{
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token);
            if(firebaseToken == null){
                filterChain.doFilter(request, response);
                return;
            }

            // setting id to the request
            request.setAttribute("uid", firebaseToken.getUid());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    firebaseToken.getUid(),
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);


        } catch (FirebaseAuthException e) {
            System.out.println("Firebase Token Validation Failed: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //everything was fine
        filterChain.doFilter(request, response);
    }
}
