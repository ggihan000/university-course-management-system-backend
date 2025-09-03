package com.example.ucms.authConfigerations;


import com.example.ucms.service.JWTService;
import com.example.ucms.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.context.ApplicationContext;
import java.io.IOException;

//Manage Spring security filters
@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String token = null;
        String email = null;

        System.out.println("Filtering");
        //extract token and then the username from the token
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            email = jwtService.extractUserName(token);
        }

        //bypass the Username Password Authentication if already login
        System.out.println("em"+email);
        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("kkkkk123");
            UserDetails userDetails = applicationContext.getBean(UserService.class).loadUserByUsername(email);
            if(jwtService.validateToken(token, userDetails)){
                System.out.println("kkkkk");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                System.out.println(userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);}
        }

        System.out.println(request.getRequestURI());
        System.out.println(response.getStatus());
        filterChain.doFilter(request, response);

        System.out.println("Final response status: " + response.getStatus());
    }
}
