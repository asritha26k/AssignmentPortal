// src/main/java/com/example/schoolportal/security/JwtAuthenticationFilter.java

package com.example.schoolportal.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService; // Note: You named it UserDetailsServiceImpl, not CustomUserDetailsService

    @Value("${jwt.cookie.name}")
    private String jwtCookieName;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("\n--- JwtAuthenticationFilter START ---");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Authentication in SecurityContextHolder before filter: " + SecurityContextHolder.getContext().getAuthentication());

        final String jwt;
        String email = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            System.out.println("Found " + cookies.length + " cookies in request.");
            jwt = Arrays.stream(cookies)
                    .filter(cookie -> {
                        boolean isMatch = jwtCookieName.equals(cookie.getName());
                        System.out.println("  Checking cookie: " + cookie.getName() + " (matches '" + jwtCookieName + "': " + isMatch + ")");
                        return isMatch;
                    })
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        } else {
            jwt = null;
            System.out.println("No cookies found in request.");
        }

        if (jwt == null) {
            System.out.println("JWT cookie not found or is null.");
            if (request.getServletPath().startsWith("/api/auth")) {
                System.out.println("Request is to /api/auth endpoint, allowing unauthenticated access.");
            } else {
                System.out.println("Request is to protected endpoint without JWT. Passing to next filter (expecting 401).");
            }
            filterChain.doFilter(request, response);
            System.out.println("--- JwtAuthenticationFilter END (No JWT) ---\n");
            return;
        }

        System.out.println("JWT cookie found. Value starts with: " + jwt.substring(0, Math.min(jwt.length(), 30)) + "...");

        try {
            email = jwtUtil.extractUsername(jwt);
            System.out.println("Extracted email from JWT: " + email);
        } catch (Exception e) {
            System.err.println("JWT Token invalid or expired during username extraction: " + e.getMessage());
            clearJwtCookie(response);
            filterChain.doFilter(request, response);
            System.out.println("--- JwtAuthenticationFilter END (JWT Invalid/Expired) ---\n");
            return;
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Email is not null (" + email + ") and SecurityContextHolder is empty. Attempting to authenticate...");
            UserDetails userDetails;
            try {
                userDetails = userDetailsService.loadUserByUsername(email);
                System.out.println("UserDetails loaded for: " + userDetails.getUsername() + ". Authorities: " + userDetails.getAuthorities());
            } catch (UsernameNotFoundException e) {
                System.err.println("User from JWT not found in database: " + email + ". Clearing cookie.");
                clearJwtCookie(response);
                filterChain.doFilter(request, response);
                System.out.println("--- JwtAuthenticationFilter END (User Not Found) ---\n");
                return;
            }

            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                System.out.println("JWT is VALID for UserDetails: " + userDetails.getUsername());

                List<String> roles = jwtUtil.extractRoles(jwt);
                // --- FIX HERE ---
                // Roles extracted from JWT already have "ROLE_" prefix because of JwtUtil.generateToken change.
                // So, just map them directly to SimpleGrantedAuthority.
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new) // Removed the extra "ROLE_" prefixing here
                        .collect(Collectors.toList());
                System.out.println("Authorities from JWT: " + authorities); // This should now print [ROLE_TEACHER]

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication set in SecurityContextHolder successfully for: " + userDetails.getUsername());
            } else {
                System.err.println("JWT Token validation failed against UserDetails for: " + userDetails.getUsername() + ". Clearing cookie.");
                clearJwtCookie(response);
            }
        } else {
            if (email == null) {
                System.out.println("Email was null after extraction. Cannot authenticate.");
            } else {
                System.out.println("SecurityContextHolder already contains authentication for: " + SecurityContextHolder.getContext().getAuthentication().getName() + ". Skipping new authentication.");
            }
        }

        filterChain.doFilter(request, response);
        System.out.println("--- JwtAuthenticationFilter END ---\n");
    }

    private void clearJwtCookie(HttpServletResponse response) {
        System.out.println("Attempting to clear JWT cookie: " + jwtCookieName);
        ResponseCookie clearedCookie = ResponseCookie.from(jwtCookieName, "")
                .httpOnly(true)
                .secure(false) // TEMPORARY for local development if not using HTTPS
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, clearedCookie.toString());
    }
}