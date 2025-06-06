// src/main/java/com/example/schoolportal/controller/AuthController.java

package com.example.schoolportal.controller;

import com.example.schoolportal.dto.AuthRequest;
import com.example.schoolportal.dto.RegisterRequest;
import com.example.schoolportal.security.JwtUtil;
import com.example.schoolportal.service.AuthService;
import jakarta.servlet.http.HttpServletResponse; // Add this import
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders; // Add this import
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie; // Add this import
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Add this import
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Or just "/auth" if you prefer as per your SecurityConfig
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil; // Inject JwtUtil here

    @PostMapping("/register/teacher")
    public ResponseEntity<String> registerTeacher(@RequestBody RegisterRequest request) {
        authService.registerTeacher(request);
        return ResponseEntity.ok("Teacher registered successfully!");
    }

    @PostMapping("/register/student")
    public ResponseEntity<String> registerStudent(@RequestBody RegisterRequest request) {
        authService.registerStudent(request);
        return ResponseEntity.ok("Student registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody AuthRequest request, HttpServletResponse response) {
        // 1. Authenticate user credentials
        Authentication authentication = authService.authenticate(request);

        // 2. Generate JWT token based on the authenticated user
        String jwtToken = jwtUtil.generateToken(authentication);

        // 3. Create an HTTP-only cookie
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwtToken) // "jwt" is the cookie name
                .httpOnly(true)       // Prevents client-side JavaScript access to the cookie
                .secure(true)         // Only send over HTTPS (CRUCIAL for production)
                .path("/")            // Makes the cookie available to all paths in the application
                .maxAge(jwtUtil.getExpirationTime() / 1000) // Match JWT expiration time (in seconds)
                .sameSite("Lax")      // CSRF protection: "Lax" for most common use cases, "Strict" for stricter
                .build();

        // 4. Add the cookie to the response header
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return ResponseEntity.ok("Login successful! JWT set in HttpOnly cookie.");
    }
    // In TeacherController.java
    @GetMapping("/test-auth")
    public ResponseEntity<String> testAuthentication(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok("Authenticated as: " + authentication.getName() + " with roles: " + authentication.getAuthorities());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated.");
    }


    // Optional: Logout endpoint to clear the cookie
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", "") // Empty value
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0) // Set max age to 0 to immediately expire the cookie
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        return ResponseEntity.ok("Logout successful!");
    }
}