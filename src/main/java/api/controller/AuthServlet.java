package api.controller;

import api.dao.UserDAO;
import api.model.User;
import api.util.ErrorUtil;
import api.util.JsonUtil;
import api.util.JwtUtil;
import api.util.PasswordUtil;
import com.google.gson.Gson;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.Map;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {

  private final UserDAO userDAO = new UserDAO();
  private final Gson gson = new Gson();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    resp.setContentType("application/json");
    String path = req.getPathInfo();

    Map<String, String> body = gson.fromJson(req.getReader(), Map.class);

    try {
      switch (path) {
        case "/verify":
          verifyToken(body, resp);
          break;
        case "/logout":
          break;
        case "/register":
          register(body, resp);
          break;
        case "/login":
          login(body, resp);
          break;
        default:
          throw new Exception("Error");
      }


    } catch (ErrorUtil e) {
      resp.setStatus(e.getStatusCode());
      resp.getWriter().write(
          JsonUtil.toJson(Map.of("message", e.getMessage()))
      );

    } catch (Exception e) {
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      resp.getWriter().write(
          JsonUtil.toJson(Map.of("message", "Error interno del servidor"))
      );
    }
  }

  private void register(Map<String, String> body,
                        HttpServletResponse resp) throws Exception {

    User user = new User();
    user.setEmail(body.get("email"));
    user.setName(body.get("name"));
    user.setPassword(PasswordUtil.hash(body.get("password")));
    userDAO.save(user);

    resp.getWriter().write(
        JsonUtil.toJson(Map.of("message", "Usuario registrado"))
    );
  }

  private void login(Map<String, String> body,
                     HttpServletResponse resp) throws Exception {

    User user = userDAO.findByEmail(body.get("email"));

    if (user == null ||
        !PasswordUtil.verify(body.get("password"), user.getPassword())) {

      resp.setStatus(401);
      resp.getWriter().write(
          JsonUtil.toJson(Map.of("error", "Credenciales inválidas"))
      );
      return;
    }

    String token = JwtUtil.generateToken(user.getEmail());

    resp.getWriter().write(
        JsonUtil.toJson(Map.of("message", "Login exitoso", "token", token))
    );
  }

  private void verifyToken(Map<String, String> body, HttpServletResponse resp) throws IOException {
    String token = body.get("token");

    if (token == null || token.isEmpty()) {
      resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      resp.getWriter().write(JsonUtil.toJson(Map.of("error", "Token no proporcionado")));
      return;
    }

    try {
      String email = JwtUtil.validateToken(token);

      resp.getWriter().write(
          JsonUtil.toJson(Map.of("message", "Token válido", "email", email))
      );
    } catch (Exception e) {
      resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      resp.getWriter().write(
          JsonUtil.toJson(Map.of("error", "Token inválido o expirado"))
      );
    }
  }

}
