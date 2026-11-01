package org.example.applicationaspects;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.appexeptions.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class SecurityAspect {

    // Matches any method that has the @Secured annotation
    @Before("@annotation(org.example.Secured)")
    public void authenticateRequest() {
        // 1. Grab the current HTTP request from Spring's request context holder
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new AuthenticationException("No active HTTP request context found");
        }

        HttpServletRequest request = attributes.getRequest();

        // 2. Extract the Authorization Header
        String authHeader = request.getHeader("Authorization");
        log.info("Security AOP -> Intercepted secured endpoint. Verifying Authorization Header...");

        // 3. Simple Token Validation Logic (Replace with actual JWT or DB validation)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Security AOP -> Missing or malformed Authorization header.");
            throw new AuthenticationException("Missing or invalid Authorization Header");
        }

        String token = authHeader.substring(7); // Extract the token string after "Bearer "

        // Hardcoded check for demo purposes (e.g., secret-token-123)
        if (!"valid-session-token".equals(token)) {
            log.warn("Security AOP -> Unauthorized token attempt: {}", token);
            throw new AuthenticationException("Invalid or expired session token");
        }

        log.info("Security AOP -> Authentication successful.");
    }
}
