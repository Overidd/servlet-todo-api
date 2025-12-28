package api.controller;

import api.dao.TodoDAO;
import api.model.Todo;
import api.util.ErrorUtil;
import api.util.JsonUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/todo/*")
public class TodoServlet extends HttpServlet {

  private final TodoDAO todoDAO = new TodoDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String path = request.getPathInfo(); // /{id} o null
    try {
      if (path == null || path.equals("/")) {
        List<Todo> todos = todoDAO.getAll();
        JsonUtil.toJson(response, todos);
      } else {
        int id = Integer.parseInt(path.substring(1));
        Todo todo = todoDAO.getById(id);
        if (todo != null) {
          JsonUtil.toJson(response, todo);
        } else {
          response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
      }
    } catch (SQLException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      e.printStackTrace();
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      Todo todo = JsonUtil.fromJson(request, Todo.class);
      Todo created = todoDAO.create(todo);
      JsonUtil.toJson(response, created);
    } catch (SQLException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      e.printStackTrace();
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String path = request.getPathInfo();
    if (path == null || path.equals("/")) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }
    int id = Integer.parseInt(path.substring(1));
    try {
      Todo todo = JsonUtil.fromJson(request, Todo.class);
      todo.setId(id);
      boolean updated = todoDAO.update(todo);
      if (updated) {
        JsonUtil.toJson(response, todo);
      } else {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
    } catch (SQLException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      e.printStackTrace();
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("application/json");
    String path = request.getPathInfo();

    try {
      if (path == null || path.equals("/")) {
        int count = todoDAO.deleteAll();
        Map<String, Object> result = new HashMap<>();
        result.put("deleted", count);
        JsonUtil.toJson(response, result);
      } else {
        int id = Integer.parseInt(path.substring(1));
        boolean deleted = todoDAO.delete(id);
        if (deleted) {
          Map<String, Object> result = new HashMap<>();
          result.put("deleted", 1);
          JsonUtil.toJson(response, result);
        } else {
          response.setStatus(HttpServletResponse.SC_NOT_FOUND);
          response.getWriter().write("{\"error\": \"Todo no encontrado\"}");
        }
      }
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("{\"error\": \"ID inválido\"}");
    } catch (SQLException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      e.printStackTrace();
    }
  }


  protected void deleteId(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {
    String path = request.getPathInfo();
    if (path == null || path.equals("/")) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("{\"error\": \"Debe proporcionar un ID para eliminar\"}");
      return;
    }

    try {
      int id = Integer.parseInt(path.substring(1));
      boolean deleted = todoDAO.delete(id);
      if (deleted) {
        Map<String, Object> result = new HashMap<>();
        result.put("deleted", 1);
        JsonUtil.toJson(response, result);
      } else {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("{\"error\": \"Todo no encontrado\"}");
      }
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("{\"error\": \"ID inválido\"}");
    } catch (SQLException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      e.printStackTrace();
    }
  }

  protected void deleteAll(HttpServletRequest request, HttpServletResponse response) throws
      ServletException, IOException {
    String path = request.getPathInfo();

    try {
      if (path == null || path.equals("/")) {
        int count = todoDAO.deleteAll();
        Map<String, Object> result = new HashMap<>();
        result.put("deleted", count);
        JsonUtil.toJson(response, result);
      } else {
        int id = Integer.parseInt(path.substring(1));
        boolean deleted = todoDAO.delete(id);
        if (deleted) {
          Map<String, Object> result = new HashMap<>();
          result.put("deleted", 1);
          JsonUtil.toJson(response, result);
        } else {
          response.setStatus(HttpServletResponse.SC_NOT_FOUND);
          response.getWriter().write("{\"error\": \"Todo no encontrado\"}");
        }
      }
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("{\"error\": \"ID inválido\"}");
    } catch (SQLException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      e.printStackTrace();
    }
  }
}
