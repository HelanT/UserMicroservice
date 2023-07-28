package com.htm.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.htm.service.DefaultUserServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Component
public class JwtGeneratorValidator {
	
	@Autowired
	private DefaultUserServiceImpl userService;
	
    //private final String SECRET = "26478268462346264862864665438765";
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }
    
    public Claims extractUserRole(String token) {

        return extractAllClaims(token);
    }
    public Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, authentication);
    }


    //SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //or HS384 or HS512

    private String createToken(Map<String, Object> claims, Authentication authentication) {
    	String role =authentication.getAuthorities().stream()
  	     .map(r -> r.getAuthority()).collect(Collectors.toSet()).iterator().next();
        return Jwts.builder()
                .claim("role",role)
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)))
                //.signWith(SignatureAlgorithm.HS256, SECRET).compact();
                 .signWith(key).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    public UsernamePasswordAuthenticationToken
    getAuthenticationToken(final String token,
                           final Authentication existingAuth,
                           final UserDetails userDetails) {

         Claims claims = extractAllClaims(token);

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

}
