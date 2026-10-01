package com.codexdrive.electronic.store.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        // api se pehle chalega jwt header: usko verify karne ke liye

        //Authorization : Bearer 2352345235sdfrsfgsdfssdf

        String requestHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            // Header is present and starts with Bearer
            token = requestHeader.substring(7);
            try {
                username = this.jwtHelper.getUsernameFromToken(token);
                logger.info("Token Username : {}", username);
            } catch (IllegalArgumentException ex) {
                logger.info("Illegal Argument while fetching the username !!" + ex.getMessage());
            } catch (ExpiredJwtException ex) {
                logger.info("Given jwt token is expired !!" + ex.getMessage());
            } catch (MalformedJwtException ex) {
                logger.info("Some changed has done in token !! Invalid Token" + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (requestHeader != null) {
            logger.warn("Invalid Header Value !! Header is not starting with Bearer");
        } else {
            logger.debug("No Authorization header provided (public endpoint request)");
        }

        // agar username null nhi hai to kaam karenge
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //username kuch hai
            //authentication null

            //fetch user detail from username
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            //validate token
            if (username.equals(userDetails.getUsername()) && !jwtHelper.isTokenExpired(token)) {

                // token valid
                // security  context ke andar authentication set karenge
                //set the authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.info("Validation fails !!");
            }
        }
        filterChain.doFilter(request, response);
    }
}