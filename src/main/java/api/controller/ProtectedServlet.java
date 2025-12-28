package api.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/api/protected/data")
public class ProtectedServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req,
                       HttpServletResponse resp)
      throws IOException {

    String userEmail = (String) req.getAttribute("userEmail");

    resp.setContentType("application/json");
    resp.getWriter().write(
        "{\"message\":\"Acceso autorizado\", \"user\":\"" + userEmail + "\"}"
    );
  }

}
