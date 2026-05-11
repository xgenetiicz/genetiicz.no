package com.example.genetiicz.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


//JWT SERVICE WILL HANDLE THE LOGIC OF THE JWT TOKEN. IT WILL GENERATE THE LOGIC BASED ON VALIDATION.
//THE UserService WILL HANDLE WHO THIS PERSON IS FROM THE DATABASE AND THEN THE JWT WILL GENERATE TOKEN, VALIDATE IT, EXTRACT AND CROSSCHECK
//AUTHREQUESTDTO IF TOKEN IS VALID == USER, AND IN THIS ASPECT THE USER IS ADMIN WHO IS ME.
@Component
public class JwtService {


    //So the idea is to use this with @Value to fetch the String token from application.yaml
    //THE CONSTRUCTOR WILL NOT FETCH THE VALUE FROM APPLICATION.YAML, THAT IS WHY I NEEDED TO USE @Value so Spring
    //AUTOMATICALLY INJECTS IN THE DECLARED String VARIABLE;

    @Value("${JWT_SECRET}") //needed to import springsecurityAnnotation.
    private String JWT_SECRET;


    //we create the token by datatype String
    public String generateToken (String email) { //Use email as username instead
        Map<String, Object> claims = new HashMap<>();
         /*now that we mapped the object to claims with fields in a new hashmap such as claims and email
         we use this map to return the value back as the createToken.
         */
        return createToken(claims, email);
    }

    public String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder() //by this builder I mutate the claims, that is within the generateToken method, when this method gets called upon. it should then create the empty object, and then initialize it here.
                .setClaims(claims) //this is the hash map, and we set the value here
                .setSubject(email) //and the fields that get mutated within the hash map.
                .setIssuedAt(new Date())//and the date from .now
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // EXPLICIT 30 MINUTES EXPIRATION DATE AFTER EACH TOKEN.
                .signWith(getSignKey(), SignatureAlgorithm.HS256) //and we sign in with the method getSignKey()
                .compact(); //serialize the token and send the token back to the client as **header.payload.signature.** JWT token needs to be in a serializable format
    }

    public SecretKey getSignKey() {
        byte [] keyBytes = Decoders.BASE64.decode(this.JWT_SECRET); //the jwtSecret from the application.yaml, gets stored into to keybytes by decoding with base64
        return Keys.hmacShaKeyFor(keyBytes); //and we return this value so we kan retrieve the getSignKey.
    }

    //TODO: extractUsername with this template: https://www.geeksforgeeks.org/springboot/spring-boot-3-0-jwt-authentication-with-spring-security-using-mysql-database/
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); //so for the object claims that has email
    }

    public Date extractExpiration (String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); //HERE we declared this variable as final where Claims claims the map that is mutated = extractAllClaims with the token created.
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims (String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey()) //this is stored as bytes remember in the byte [] keyBytes and we call on the method here so its within our scope.
                .build()
                .parseClaimsJws(token)
                .getBody(); //based on Jws<Claims> the parsed token extract All claims from the body. where this is the .payload. and we sign it.
    }

    private Boolean isTokenExpired (String token) {
        return extractExpiration(token).before(new Date()); //makes sense, it checks token if it is expired, if that is true, it return the value. and if not it doesnt return anything.
        /*on expiry this should create a new token and then set values for:

        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
        */
    }

    public Boolean validateToken (String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }
}
