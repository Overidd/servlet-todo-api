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
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    int userId = getUserId(request);
    String path = request.getPathInfo();

    try {
      if (path == null || path.equals("/")) {
        // 🔐 todos del usuario
        List<Todo> todos = todoDAO.getAllByUser(userId);
        JsonUtil.toJson(response, todos);
      } else {
        int todoId = Integer.parseInt(path.substring(1));
        Todo todo = todoDAO.getById(todoId, userId);

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
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    int userId = getUserId(request);

    try {
      Todo todo = JsonUtil.fromJson(request, Todo.class);
      Todo created = todoDAO.create(todo, userId);
      JsonUtil.toJson(response, created);
    } catch (SQLException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      e.printStackTrace();
    }
  }

  @Override
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    int userId = getUserId(request);
    String path = request.getPathInfo();

    if (path == null || path.equals("/")) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    int todoId = Integer.parseInt(path.substring(1));

    try {
      Todo todo = JsonUtil.fromJson(request, Todo.class);
      todo.setId(todoId);

      boolean updated = todoDAO.update(todo, userId);
      if (updated) {
        JsonUtil.toJson(response, todo);
      } else {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 🔐 no pertenece
      }
    } catch (SQLException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      e.printStackTrace();
    }
  }

  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    int userId = getUserId(request);
    String path = request.getPathInfo();

    try {
      if (path == null || path.equals("/")) {
        int count = todoDAO.deleteAllByUser(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("deleted", count);
        JsonUtil.toJson(response, result);
      } else {
        int todoId = Integer.parseInt(path.substring(1));
        boolean deleted = todoDAO.delete(todoId, userId);

        if (deleted) {
          Map<String, Object> result = new HashMap<>();
          result.put("deleted", 1);
          JsonUtil.toJson(response, result);
        } else {
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
      }
    } catch (SQLException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      e.printStackTrace();
    }
  }

  private int getUserId(HttpServletRequest request) {
    return (int) request.getAttribute("userId");
  }
}
