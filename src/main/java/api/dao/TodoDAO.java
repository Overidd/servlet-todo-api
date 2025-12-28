package api.dao;

import api.config.DBConnection;
import api.model.Todo;
import api.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO {

  public List<Todo> getAllByUser(int userId) throws SQLException {
    List<Todo> todos = new ArrayList<>();
    String sql = "SELECT * FROM todo WHERE user_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        Todo t = new Todo();
        t.setId(rs.getInt("id"));
        t.setTitle(rs.getString("title"));
        t.setCompleted(rs.getBoolean("completed"));
        t.setCategory(rs.getString("category"));
        t.setUserId(rs.getInt("user_id"));
        todos.add(t);
      }
    }
    return todos;
  }

  public Todo getById(int todoId, int userId) throws SQLException {
    String sql = "SELECT * FROM todo WHERE id = ? AND user_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, todoId);
      ps.setInt(2, userId);

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        Todo t = new Todo();
        t.setId(rs.getInt("id"));
        t.setTitle(rs.getString("title"));
        t.setCompleted(rs.getBoolean("completed"));
        t.setCategory(rs.getString("category"));
        t.setUserId(rs.getInt("user_id"));
        return t;
      }
    }
    return null;
  }

  public Todo create(Todo todo, int userId) throws SQLException {
    String sql = "INSERT INTO todo(title, completed, category, user_id) VALUES (?, ?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      ps.setString(1, todo.getTitle());
      ps.setBoolean(2, todo.isCompleted());
      ps.setString(3, todo.getCategory());
      ps.setInt(4, userId);

      ps.executeUpdate();

      ResultSet rs = ps.getGeneratedKeys();
      if (rs.next()) {
        todo.setId(rs.getInt(1));
        todo.setUserId(userId);
      }
    }
    return todo;
  }

  public boolean update(Todo todo, int userId) throws SQLException {
    String sql = """
      UPDATE todo
      SET title = ?, completed = ?, category = ?
      WHERE id = ? AND user_id = ?
    """;

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, todo.getTitle());
      ps.setBoolean(2, todo.isCompleted());
      ps.setString(3, todo.getCategory());
      ps.setInt(4, todo.getId());
      ps.setInt(5, userId);

      return ps.executeUpdate() > 0;
    }
  }

  public boolean delete(int todoId, int userId) throws SQLException {
    String sql = "DELETE FROM todo WHERE id = ? AND user_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, todoId);
      ps.setInt(2, userId);
      return ps.executeUpdate() > 0;
    }
  }

  public int deleteAllByUser(int userId) throws SQLException {
    String sql = "DELETE FROM todo WHERE user_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setInt(1, userId);
      return ps.executeUpdate();
    }
  }

  public User getUserByEmail(String email) throws SQLException {
    String sql = "SELECT id, email, name, password FROM users WHERE email = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, email);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        return user;
      }
    }
    return null;
  }
}
