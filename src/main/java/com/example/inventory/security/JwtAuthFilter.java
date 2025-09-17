package com.example.inventory.security;

import com.example.inventory.logs.LogEntry;
import com.example.inventory.logs.LogEntryRepository;
import com.example.inventory.util.Correlation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final RevokedTokenRepository revokedTokenRepository;
    private final LogEntryRepository logEntryRepository;

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService userDetailsService,
                         RevokedTokenRepository revokedTokenRepository, LogEntryRepository logEntryRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.revokedTokenRepository = revokedTokenRepository;
        this.logEntryRepository = logEntryRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            if (jwtService.isTokenValid(token) && revokedTokenRepository.findByToken(token).isEmpty()) {
                username = jwtService.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            LogEntry entry = new LogEntry();
            entry.setEndpoint(request.getMethod() + " " + request.getRequestURI());
            entry.setEventType("HTTP_ACCESS");
            entry.setCorrelationId(Correlation.get());
            entry.setUserId(username != null ? 0L : null);
            entry.setDetails("status=" + response.getStatus());
            entry.setTimestamp(Instant.now());
            logEntryRepository.save(entry);
        }
    }
}
