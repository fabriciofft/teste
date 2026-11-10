package br.com.quemresolve.dados.repository;

import br.com.quemresolve.dados.database.ConnectionFactory;
import static br.com.quemresolve.dados.util.JsonArgs.*;
import static br.com.quemresolve.dados.util.Responses.*;

import com.google.gson.*;
import java.sql.*;

public class ProviderRepository {
  private final ConnectionFactory db;

  public ProviderRepository(ConnectionFactory db) {
    this.db = db;
  }

  public JsonObject providers() throws SQLException {
    JsonArray a = new JsonArray();
    String sql = "SELECT p.user_id,u.name,p.category,p.title,p.bio,p.city,p.region,p.price,p.unit,p.phone,p.lat,p.lng,p.radius,p.updated,COALESCE(AVG(r.rating),0) rating,COUNT(r.rating) review_count FROM profiles p JOIN users u ON u.id=p.user_id LEFT JOIN reviews r ON r.provider_id=p.user_id GROUP BY p.user_id,u.name,p.category,p.title,p.bio,p.city,p.region,p.price,p.unit,p.phone,p.lat,p.lng,p.radius,p.updated ORDER BY rating DESC,p.updated DESC";
    try (Connection c = db.con(); PreparedStatement p = c.prepareStatement(sql); ResultSet rs = p.executeQuery()) {
      while (rs.next()) {
        JsonObject o = new JsonObject();
        o.addProperty("user_id", rs.getString("user_id"));
        o.addProperty("name", rs.getString("name"));
        o.addProperty("category", rs.getString("category"));
        o.addProperty("title", rs.getString("title"));
        o.addProperty("bio", rs.getString("bio"));
        o.addProperty("city", rs.getString("city"));
        o.addProperty("region", rs.getString("region"));
        o.addProperty("price", rs.getBigDecimal("price"));
        o.addProperty("unit", rs.getString("unit"));
        o.addProperty("phone", rs.getString("phone"));
        double lat = rs.getDouble("lat");
        o.add("lat", rs.wasNull() ? JsonNull.INSTANCE : new JsonPrimitive(lat));
        double lng = rs.getDouble("lng");
        o.add("lng", rs.wasNull() ? JsonNull.INSTANCE : new JsonPrimitive(lng));
        o.addProperty("radius", rs.getDouble("radius"));
        o.addProperty("updated", rs.getLong("updated"));
        o.addProperty("rating", rs.getDouble("rating"));
        o.addProperty("review_count", rs.getLong("review_count"));
        a.add(o);
      }
    }
    JsonObject out = ok();
    out.add("providers", a);
    return out;
  }
}
