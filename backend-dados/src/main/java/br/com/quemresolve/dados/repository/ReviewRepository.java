package br.com.quemresolve.dados.repository;

import br.com.quemresolve.dados.database.ConnectionFactory;
import static br.com.quemresolve.dados.util.JsonArgs.*;
import static br.com.quemresolve.dados.util.Responses.*;

import com.google.gson.*;
import java.sql.*;

public class ReviewRepository {
  private final ConnectionFactory db;

  public ReviewRepository(ConnectionFactory db) {
    this.db = db;
  }

  public JsonObject reviews(JsonObject d) throws SQLException {
    JsonArray a = new JsonArray();
    try (Connection c = db.con(); PreparedStatement p = c.prepareStatement("SELECT r.rating,r.comment,r.created,u.name FROM reviews r JOIN users u ON u.id=r.user_id WHERE r.provider_id=? ORDER BY r.created DESC")) {
      p.setString(1, str(d, "providerId"));
      try (ResultSet rs = p.executeQuery()) {
        while (rs.next()) {
          JsonObject o = new JsonObject();
          o.addProperty("rating", rs.getInt("rating"));
          o.addProperty("comment", rs.getString("comment"));
          o.addProperty("created", rs.getLong("created"));
          o.addProperty("name", rs.getString("name"));
          a.add(o);
        }
      }
    }
    boolean contacted = false;
    if (!str(d, "userId").isBlank()) {
      try (Connection c = db.con(); PreparedStatement p = c.prepareStatement("SELECT 1 FROM contacts WHERE user_id=? AND provider_id=?")) {
        p.setString(1, str(d, "userId"));
        p.setString(2, str(d, "providerId"));
        try (ResultSet rs = p.executeQuery()) {
          contacted = rs.next();
        }
      }
    }
    JsonObject out = ok();
    out.add("reviews", a);
    out.addProperty("contacted", contacted);
    return out;
  }

  public JsonObject upsertReview(JsonObject d) throws SQLException {
    String sql = "INSERT INTO reviews(user_id,provider_id,rating,comment,created) VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE rating=VALUES(rating),comment=VALUES(comment),created=VALUES(created)";
    try (Connection c = db.con(); PreparedStatement p = c.prepareStatement(sql)) {
      p.setString(1, str(d, "userId"));
      p.setString(2, str(d, "providerId"));
      p.setInt(3, (int) num(d, "rating"));
      p.setString(4, str(d, "comment"));
      p.setLong(5, num(d, "created"));
      p.executeUpdate();
      return ok();
    }
  }
}
