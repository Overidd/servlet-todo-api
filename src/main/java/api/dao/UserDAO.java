package api.dao;

import api.config.DBConnection;
import api.model.User;
import api.util.ErrorUtil;

import java.sql.*;

public class UserDAO {

  public void save(User user) throws Exception {
    if (findByEmail(user.getEmail()) != null) {
      throw new ErrorUtil(401, "El usuario con este correo ya existe");
    }

    String sql = "INSERT INTO users(email, name, password) VALUES (?,?,?)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, user.getEmail());
      ps.setString(2, user.getName());
      ps.setString(3, user.getPassword());
      ps.executeUpdate();
    }
  }

  public User findByEmail(String email) throws Exception {
    String sql = "SELECT * FROM users WHERE email=?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      ps.setString(1, email);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setEmail(rs.getString("email"));
        u.setName(rs.getString("name"));
        u.setPassword(rs.getString("password"));
        return u;
      }
      return null;
    }
  }
}
