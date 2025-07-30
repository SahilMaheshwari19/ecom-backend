package com.wryon.ecomproj1.config;

import com.wryon.ecomproj1.service.JWTService;
import com.wryon.ecomproj1.service.MyUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger LOG = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        LOG.info("Inside shouldNotFilter method - init shouldNotFilter");
        String path = request.getRequestURI();
        return path.equals("/login") || path.equals("/register");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LOG.info("Inside doFilterInternal method - init doFilterInternal");
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        LOG.info("Inside doFilterInternal method - Requesting Cookie");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            LOG.info("Inside doFilterInternal method - Cookie Not Null");
            for (Cookie cookie : cookies) {
                LOG.info("Inside doFilterInternal method - Looping Cookie ");
                if (cookie.getName().equals("jwtToken")) {
                    LOG.info("Inside doFilterInternal method - Found JWT Token ");
                    jwtToken = cookie.getValue();
                    LOG.info("Inside doFilterInternal method - Extracting username ");
                    username = jwtService.extractUserName(jwtToken);
                }
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            LOG.info("Inside doFilterInternal method - Found Username AND no Authentication");
            UserDetails userDetails = applicationContext.getBean(MyUserDetailService.class).loadUserByUsername(username);
            LOG.info("Inside doFilterInternal method - Found UserDetails ");
            if (jwtService.validateToken(jwtToken, userDetails)) {
                LOG.info("Inside doFilterInternal method - Token Validated");
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                LOG.info("Inside doFilterInternal method - Setting Authentication Details");
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        LOG.info("Authorization Header: {}", authHeader);
        LOG.info("JWT Token: {}", jwtToken);
        LOG.info("Username: {}", username);
        filterChain.doFilter(request, response);
    }
}
