package Project.Order.Management.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token= recoveryToken(request);

        if(token!=null && jwtService.validateToken(token)){

            String username= jwtService.extractUsername(token);

            if(!username.isBlank() && SecurityContextHolder.getContext().getAuthentication()==null){

                UserDetails user= customUserDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoveryToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header==null) return null;
        if(!header.startsWith("Bearer ")) return null;
        return header.replace("Bearer ", "");
    }
}
