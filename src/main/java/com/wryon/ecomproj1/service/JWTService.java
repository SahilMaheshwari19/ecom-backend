package com.wryon.ecomproj1.service;

import com.wryon.ecomproj1.DAO.UserRepo;
import com.wryon.ecomproj1.model.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
public class JWTService {

    Logger LOG = LoggerFactory.getLogger(LoggerFactory.class);
    private String secretKey;

    @Autowired
    private UserRepo userRepo;

    public JWTService() {
        LOG.info("Inside JWTService Service method - init JWTService");
        try {
            LOG.info("Inside TRY BLOCK  -- JWTService Service Class");
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sKey = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sKey.getEncoded());
            LOG.info("secretKey: {}", secretKey);
        }catch (NoSuchAlgorithmException e){
            LOG.error("Inside CATCH BLOCK -- JWTService Service Class = {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String userName) {
        LOG.info("Inside generateToken method -- userName = {}", userName);
        Users user = userRepo.findByUsername(userName);
        Map<String,Object> claims = new HashMap<>();
        claims.put("roles", user.getRole());
        LOG.info("Inside generateToken method -- BUILDING JWT");
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60*60*10000))
                .and()
                .signWith(getKey())
                .compact();
    }


    private SecretKey getKey() {
        LOG.info("Inside getKey method -- building key");
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //extract the username from jwt token
    public String extractUserName(String jwtToken) {
        LOG.info("Inside extractUserName method - jwtToken = {}", jwtToken);
        return extractClaim(jwtToken, Claims::getSubject);
    }

    //extract the username from request
    public String extractUserNameFromRequest(HttpServletRequest request) {
        LOG.info("Inside extractUserNameFromRequest method - request = {}", request);
        return "extractClaim()";
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        LOG.info("Inside extractClaim method - claims = {}", claims);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        LOG.info("Inside extractAllClaims method - jwtToken = {}", jwtToken);
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public boolean validateToken(String jwtToken, UserDetails userDetails) {
        final String username = extractUserName(jwtToken);
        LOG.info("Inside validateToken method - username = {}", username);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
    }

    public boolean isTokenExpired(String jwtToken) {
        LOG.info("Inside isTokenExpired method - jwtToken = {}", jwtToken);
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        LOG.info("Inside extractExpiration method - jwtToken = {}", jwtToken);
        return extractClaim(jwtToken, Claims::getExpiration);
    }
}
