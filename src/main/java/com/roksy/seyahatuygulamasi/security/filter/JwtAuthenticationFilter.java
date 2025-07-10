package com.roksy.seyahatuygulamasi.security.filter;

import com.roksy.seyahatuygulamasi.service.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // <-- DEBUG: Adım 1
        System.out.println("1. Filtre çalıştı. Gelen istek: " + request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // <-- DEBUG: Adım 2
            System.out.println("2. Authorization başlığı yok veya 'Bearer' ile başlamıyor. Filtre bu istek için pas geçiliyor.");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        
        try {
            username = jwtService.extractUsername(jwt);
            // <-- DEBUG: Adım 3
            System.out.println("3. Token'dan kullanıcı adı çıkarıldı: " + username);
        } catch (Exception e) {
            System.out.println("3. HATA: Token'dan kullanıcı adı çıkarılamadı: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // <-- DEBUG: Adım 4
                System.out.println("4. Kullanıcı daha önce doğrulanmamış, veritabanından aranıyor.");
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // <-- DEBUG: Adım 5
                System.out.println("5. Token'ın geçerliliği kontrol ediliyor...");
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // <-- DEBUG: Adım 6 (Başarılı)
                    System.out.println("6. BAŞARILI: Token geçerli! Kullanıcı sisteme tanıtılıyor.");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    // <-- DEBUG: Adım 6 (Başarısız)
                    System.out.println("6. HATA: Token geçerli değil!");
                }
            } catch (Exception e) {
                System.out.println("4-6 ARASI HATA: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Kullanıcı adı null veya kullanıcı zaten doğrulanmış.");
        }

        filterChain.doFilter(request, response);
    }
}