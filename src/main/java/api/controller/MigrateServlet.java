package api.controller;

import api.config.MigrateTables;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/migrate")
public class MigrateServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("text/plain");
    PrintWriter out = response.getWriter();

    try {
      MigrateTables.migrate();
      out.println("Migración completada correctamente en Railway!");
    } catch (Exception e) {
      e.printStackTrace(out);
      out.println("Error en la migración: " + e.getMessage());
    }
  }
}
