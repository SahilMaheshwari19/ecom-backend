package com.wryon.ecomproj1.config;

import com.wryon.ecomproj1.service.JWTService;
import com.wryon.ecomproj1.service.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/login") || path.equals("/register");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    jwtToken = cookie.getValue();
                    username = jwtService.extractUserName(jwtToken);
                }
            }
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = applicationContext.getBean(MyUserDetailService.class).loadUserByUsername(username);
            System.out.println(userDetails.getUsername());
            if(jwtService.validateToken(jwtToken,userDetails)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        System.out.println("Authorization Header: " + authHeader);
        System.out.println("JWT Token: " + jwtToken);
        System.out.println("Username: " + username);

        filterChain.doFilter(request,response);
    }
}
