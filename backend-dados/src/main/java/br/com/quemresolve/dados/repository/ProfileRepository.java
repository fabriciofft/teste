package br.com.quemresolve.dados.repository;

import br.com.quemresolve.dados.database.ConnectionFactory;
import static br.com.quemresolve.dados.util.JsonArgs.*;
import static br.com.quemresolve.dados.util.Responses.*;

import com.google.gson.*;
import java.sql.*;

public class ProfileRepository {
  private final ConnectionFactory db;

  public ProfileRepository(ConnectionFactory db) {
    this.db = db;
  }

  public JsonObject profile(String id) throws SQLException {
    try (Connection c = db.con(); PreparedStatement p = c.prepareStatement("SELECT * FROM profiles WHERE user_id=?")) {
      p.setString(1, id);
      try (ResultSet rs = p.executeQuery()) {
        if (!rs.next()) return no();
        JsonObject o = new JsonObject();
        o.addProperty("user_id", rs.getString("user_id"));
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
        return yes(o);
      }
    }
  }

  public JsonObject upsertProfile(JsonObject d) throws SQLException {
    String sql = "INSERT INTO profiles(user_id,category,title,bio,city,region,price,unit,phone,lat,lng,radius,updated) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE category=VALUES(category),title=VALUES(title),bio=VALUES(bio),city=VALUES(city),region=VALUES(region),price=VALUES(price),unit=VALUES(unit),phone=VALUES(phone),lat=VALUES(lat),lng=VALUES(lng),radius=VALUES(radius),updated=VALUES(updated)";
    try (Connection c = db.con(); PreparedStatement p = c.prepareStatement(sql)) {
      p.setString(1, str(d, "userId"));
      p.setString(2, str(d, "category"));
      p.setString(3, str(d, "title"));
      p.setString(4, str(d, "bio"));
      p.setString(5, str(d, "city"));
      p.setString(6, str(d, "region"));
      p.setDouble(7, num(d, "price"));
      p.setString(8, str(d, "unit"));
      p.setString(9, str(d, "phone"));
      setDouble(p, 10, d, "lat");
      setDouble(p, 11, d, "lng");
      p.setDouble(12, num(d, "radius"));
      p.setLong(13, num(d, "updated"));
      p.executeUpdate();
      return ok();
    }
  }

  public JsonObject phone(String id) throws SQLException {
    try (Connection c = db.con(); PreparedStatement p = c.prepareStatement("SELECT phone FROM profiles WHERE user_id=?")) {
      p.setString(1, id);
      try (ResultSet rs = p.executeQuery()) {
        if (!rs.next()) return no();
        JsonObject o = new JsonObject();
        o.addProperty("phone", rs.getString(1));
        return yes(o);
      }
    }
  }

  private void setDouble(PreparedStatement p, int i, JsonObject d, String k) throws SQLException {
    if (!d.has(k) || d.get(k).isJsonNull()) p.setNull(i, Types.DOUBLE);
    else p.setDouble(i, d.get(k).getAsDouble());
  }
}
