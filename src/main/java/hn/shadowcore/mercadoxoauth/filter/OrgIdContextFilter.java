package hn.shadowcore.mercadoxoauth.filter;

import hn.shadowcore.mercadoxcontext.utils.JwtUtil;
import hn.shadowcore.mercadoxcontext.utils.OrgIdContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OrgIdContextFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            if(jwtUtil.validateToken(jwt)) {
                String orgId = jwtUtil.getOrgIdFromToken(jwt);
                OrgIdContextHolder.setTenantId(orgId);
            }
        }
        try {
            filterChain.doFilter(request, response);
        }
        finally {
            OrgIdContextHolder.clear();
        }

    }
}
