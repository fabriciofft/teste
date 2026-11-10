package br.com.quemresolve.dados.repository;

import br.com.quemresolve.dados.database.ConnectionFactory;
import static br.com.quemresolve.dados.util.JsonArgs.*;
import static br.com.quemresolve.dados.util.Responses.*;

import com.google.gson.*;
import java.sql.*;

public class ContactRepository {
  private final ConnectionFactory db;

  public ContactRepository(ConnectionFactory db) {
    this.db = db;
  }

  public JsonObject contact(JsonObject d) throws SQLException {
    try (Connection c = db.con(); PreparedStatement p = c.prepareStatement("INSERT IGNORE INTO contacts(user_id,provider_id) VALUES(?,?)")) {
      p.setString(1, str(d, "userId"));
      p.setString(2, str(d, "providerId"));
      p.executeUpdate();
      return ok();
    }
  }

  public JsonObject hasContact(JsonObject d) throws SQLException {
    try (Connection c = db.con(); PreparedStatement p = c.prepareStatement("SELECT 1 FROM contacts WHERE user_id=? AND provider_id=?")) {
      p.setString(1, str(d, "userId"));
      p.setString(2, str(d, "providerId"));
      try (ResultSet rs = p.executeQuery()) {
        JsonObject o = ok();
        o.addProperty("exists", rs.next());
        return o;
      }
    }
  }
}
