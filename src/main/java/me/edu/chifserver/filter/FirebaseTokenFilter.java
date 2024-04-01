package me.edu.chifserver.filter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public class FirebaseTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // getting token from request
        String token = request.getHeader("Authentication");

        if(token == null){
            sendResponse(response, HttpStatus.UNAUTHORIZED, "Missing Token!");
            return;
        }

        FirebaseToken decodedToken = null;
        try{
            // verify token in firebase server
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

        } catch (FirebaseAuthException e) {
            sendResponse(response, HttpStatus.UNAUTHORIZED, "Invalid Token!");
            return;
        }

        if(decodedToken == null) {
            sendResponse(response, HttpStatus.UNAUTHORIZED, "Invalid Token!");
            return;
        }

        // extract uid and email
        String uid = decodedToken.getUid();
        String email = decodedToken.getEmail();

        // adding to the request
        request.setAttribute("uid", uid);
        request.setAttribute("email", email);

        filterChain.doFilter(request, response);
    }

    private void sendResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.getWriter().write(message);
        response.getWriter().flush();
    }
}
