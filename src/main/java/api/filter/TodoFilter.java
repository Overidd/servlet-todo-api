package api.filter;

import api.util.JwtUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/api/todo/*")
public class TodoFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    String authHeader = req.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);

      try {
        String email = JwtUtil.validateToken(token);
        if (email != null && !email.isEmpty()) {
          chain.doFilter(request, response);
          return;
        }
      } catch (JWTVerificationException e) {
        e.printStackTrace();
      }
    }

    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    res.getWriter().write("{\"error\": \"Token invalido o no proporcionado\"}");
  }

  @Override
  public void destroy() {
    // Limpieza opcional
  }
}
