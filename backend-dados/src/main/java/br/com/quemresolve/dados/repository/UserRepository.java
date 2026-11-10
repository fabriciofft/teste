package br.com.quemresolve.dados.repository;

import br.com.quemresolve.dados.database.ConnectionFactory;
import static br.com.quemresolve.dados.util.JsonArgs.*;
import static br.com.quemresolve.dados.util.Responses.*;

import com.google.gson.*;
import java.sql.*;

public class UserRepository {
  private final ConnectionFactory db;

  public UserRepository(ConnectionFactory db) {
    this.db = db;
  }

  public JsonObject userEmail(String email) throws SQLException {
    try (Connection c = db.con(); PreparedStatement p = c.prepareStatement("SELECT id,name,email,password,role FROM users WHERE email=?")) {
      p.setString(1, email);
      try (ResultSet rs = p.executeQuery()) {
        if (!rs.next()) return no();
        JsonObject u = new JsonObject();
        u.addProperty("id", rs.getString("id"));
        u.addProperty("name", rs.getString("name"));
        u.addProperty("email", rs.getString("email"));
        u.addProperty("password", rs.getString("password"));
        u.addProperty("role", rs.getString("role"));
        return yes(u);
      }
    }
  }

  public JsonObject insertUser(JsonObject d) throws SQLException {
    try (Connection c = db.con(); PreparedStatement p = c.prepareStatement("INSERT INTO users(id,name,email,password,role) VALUES(?,?,?,?,?)")) {
      p.setString(1, str(d, "id"));
      p.setString(2, str(d, "name"));
      p.setString(3, str(d, "email"));
      p.setString(4, str(d, "password"));
      p.setString(5, str(d, "role"));
      p.executeUpdate();
      return ok();
    }
  }
}
