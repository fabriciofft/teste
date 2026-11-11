package br.com.quemresolve.dados.repository;

import br.com.quemresolve.dados.banco.DB;
import static br.com.quemresolve.dados.utilitarios.Respostas.*;

import com.google.gson.*;
import java.sql.*;

public class PrestadorRepository {
  private final DB dados;

  public PrestadorRepository(DB dados) {
    this.dados = dados;
  }

  public JsonObject listarPrestadores() throws SQLException {
    JsonArray lista = new JsonArray();
    String sql = "SELECT p.usuario_id,u.nome,p.categoria,p.titulo,p.biografia,p.cidade,p.regiao,p.preco,p.unidade,p.celular,p.latitude,p.longitude,p.raio,p.atualizado,COALESCE(AVG(a.nota),0) nota,COUNT(a.nota) quantidade_notas FROM prestadores p JOIN usuarios u ON u.id=p.usuario_id LEFT JOIN avaliacoes a ON a.prestador_id=p.usuario_id GROUP BY p.usuario_id,u.nome,p.categoria,p.titulo,p.biografia,p.cidade,p.regiao,p.preco,p.unidade,p.celular,p.latitude,p.longitude,p.raio,p.atualizado ORDER BY nota DESC,p.atualizado DESC";

    try (Connection c = dados.conectar(); PreparedStatement p = c.prepareStatement(sql); ResultSet rs = p.executeQuery()) {
      while (rs.next()) {
        JsonObject objeto = new JsonObject();
        objeto.addProperty("usuario_id", rs.getString("usuario_id"));
        objeto.addProperty("nome", rs.getString("nome"));
        objeto.addProperty("categoria", rs.getString("categoria"));
        objeto.addProperty("titulo", rs.getString("titulo"));
        objeto.addProperty("biografia", rs.getString("biografia"));
        objeto.addProperty("cidade", rs.getString("cidade"));
        objeto.addProperty("regiao", rs.getString("regiao"));
        objeto.addProperty("preco", rs.getBigDecimal("preco"));
        objeto.addProperty("unidade", rs.getString("unidade"));
        objeto.addProperty("celular", rs.getString("celular"));
        double lat = rs.getDouble("latitude");
        objeto.add("latitude", rs.wasNull() ? JsonNull.INSTANCE : new JsonPrimitive(lat));
        double lng = rs.getDouble("longitude");
        objeto.add("longitude", rs.wasNull() ? JsonNull.INSTANCE : new JsonPrimitive(lng));
        objeto.addProperty("raio", rs.getDouble("raio"));
        objeto.addProperty("atualizado", rs.getLong("atualizado"));
        objeto.addProperty("nota", rs.getDouble("nota"));
        objeto.addProperty("qauntidade_notas", rs.getLong("quantidade_notas"));
        lista.add(objeto);
      }
    }
    JsonObject resposta = sucesso();
    resposta.add("prestadores", lista);
    return resposta;
  }
}
