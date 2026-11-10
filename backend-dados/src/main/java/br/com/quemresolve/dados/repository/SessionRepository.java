package br.com.quemresolve.dados.repository;

import br.com.quemresolve.dados.database.ConnectionFactory;
import static br.com.quemresolve.dados.util.JsonArgs.*;
import static br.com.quemresolve.dados.util.Responses.*;

import com.google.gson.*;
import java.sql.*;

public class SessionRepository {
  private final ConnectionFactory db;

  public SessionRepository(ConnectionFactory db) {
    this.db = db;
  }

  public JsonObject userSession(String token, long now) throws SQLException {
    try (Connection c = db.con(); PreparedStatement p = c.prepareStatement("SELECT u.id,u.name,u.email,u.role FROM sessions s JOIN users u ON u.id=s.user_id WHERE s.token=? AND s.expires>?")) {
      p.setString(1, token);
      p.setLong(2, now);
      try (ResultSet rs = p.executeQuery()) {
        if (!rs.next()) return no();
        JsonObject u = new JsonObject();
        u.addProperty("id", rs.getString("id"));
        u.addProperty("name", rs.getString("name"));
        u.addProperty("email", rs.getString("email"));
        u.addProperty("role", rs.getString("role"));
        return yes(u);
      }
    }
  }

  public JsonObject saveSession(JsonObject d) throws SQLException {
    try (Connection c = db.con(); PreparedStatement p = c.prepareStatement("INSERT INTO sessions(token,user_id,expires) VALUES(?,?,?)")) {
      p.setString(1, str(d, "token"));
      p.setString(2, str(d, "userId"));
      p.setLong(3, num(d, "expires"));
      p.executeUpdate();
      return ok();
    }
  }

  public JsonObject deleteSession(String token) throws SQLException {
    try (Connection c = db.con(); PreparedStatement p = c.prepareStatement("DELETE FROM sessions WHERE token=?")) {
      p.setString(1, token);
      p.executeUpdate();
      return ok();
    }
  }
}
