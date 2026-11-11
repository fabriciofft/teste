package br.com.quemresolve.dados.repository;

import br.com.quemresolve.dados.banco.DB;
import static br.com.quemresolve.dados.utilitarios.ArgumentosJson.*;
import static br.com.quemresolve.dados.utilitarios.Respostas.*;

import com.google.gson.*;
import java.sql.*;

public class PerfilRepository {
  private final DB dados;

  public PerfilRepository(DB dados) {
    this.dados = dados;
  }

  public JsonObject buscarPerfil(String id) throws SQLException {
    try (Connection c = dados.conectar(); PreparedStatement p = c.prepareStatement("SELECT * FROM prestadores WHERE usuario_id=?")) {
      p.setString(1, id);
      try (ResultSet rs = p.executeQuery()) {
        if (!rs.next()) return no();
        JsonObject o = new JsonObject();
        o.addProperty("usuario_id", rs.getString("usuario_id"));
        o.addProperty("categoria", rs.getString("categoria"));
        o.addProperty("titulo", rs.getString("titulo"));
        o.addProperty("biografia", rs.getString("biografia"));
        o.addProperty("cidade", rs.getString("cidade"));
        o.addProperty("regiao", rs.getString("regiao"));
        o.addProperty("preco", rs.getBigDecimal("preco"));
        o.addProperty("unidade", rs.getString("unidade"));
        o.addProperty("celular", rs.getString("celular"));
        double lat = rs.getDouble("latitude");
        o.add("latitude", rs.wasNull() ? JsonNull.INSTANCE : new JsonPrimitive(lat));
        double lng = rs.getDouble("longitude");
        o.add("longitude", rs.wasNull() ? JsonNull.INSTANCE : new JsonPrimitive(lng));
        o.addProperty("raio", rs.getDouble("raio"));
        o.addProperty("atualizado", rs.getLong("atualizado"));
        return yes(o);
      }
    }
  }

  public JsonObject gravarPerfil(JsonObject d) throws SQLException {

    String sql = "INSERT INTO prestadores(usuario_id,categoria,titulo,biografia,cidade,regiao,preco,unidade,celular,latitude,longitude,raio,atualizado) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) " +
            "ON DUPLICATE KEY UPDATE " +
            "categoria=VALUES(categoria)," +
            "titulo=VALUES(titulo)," +
            "biografia=VALUES(biografia)," +
            "cidade=VALUES(cidade)," +
            "regiao=VALUES(regiao)," +
            "preco=VALUES(preco)," +
            "unidade=VALUES(unidade)," +
            "celular=VALUES(celular)," +
            "latitude=VALUES(latitude)," +
            "longitude=VALUES(longitude)," +
            "raio=VALUES(raio)," +
            "atualizado=VALUES(atualizado)";

    try (Connection c = dados.conectar();
         PreparedStatement p = c.prepareStatement(sql)) {

      p.setString(1, texto(d, "usuario_Id"));
      p.setString(2, texto(d, "categoria"));
      p.setString(3, texto(d, "titulo"));
      p.setString(4, texto(d, "biografia"));
      p.setString(5, texto(d, "cidade"));
      p.setString(6, texto(d, "regiao"));
      p.setDouble(7, decimal(d, "preco"));
      p.setString(8, texto(d, "unidade"));
      p.setString(9, texto(d, "celular"));
      setDouble(p, 10, d, "latitude");
      setDouble(p, 11, d, "longitude");
      p.setDouble(12, decimal(d, "raio"));
      p.setLong(13, numero(d, "atualizado"));

      p.executeUpdate();

      return sucesso();
    }
  }

  public JsonObject buscarTelefone(String id) throws SQLException {
    try (Connection c = dados.conectar(); PreparedStatement p = c.prepareStatement("SELECT celular FROM prestadores WHERE usuario_id=?")) {
      p.setString(1, id);
      try (ResultSet rs = p.executeQuery()) {
        if (!rs.next()) return no();
        JsonObject o = new JsonObject();
        o.addProperty("celular", rs.getString(1));
        return yes(o);
      }
    }
  }

  private void setDouble(PreparedStatement p, int i, JsonObject d, String k) throws SQLException {
    if (!d.has(k) || d.get(k).isJsonNull()) p.setNull(i, Types.DOUBLE);
    else p.setDouble(i, d.get(k).getAsDouble());
  }
}
