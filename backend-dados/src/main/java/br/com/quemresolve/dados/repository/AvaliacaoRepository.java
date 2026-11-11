package br.com.quemresolve.dados.repository;

import br.com.quemresolve.dados.banco.DB;
import static br.com.quemresolve.dados.utilitarios.ArgumentosJson.*;
import static br.com.quemresolve.dados.utilitarios.Respostas.*;

import com.google.gson.*;
import java.sql.*;

public class AvaliacaoRepository {
  private final DB dados;

  public AvaliacaoRepository(DB dados) {
    this.dados = dados;
  }

  public JsonObject listarAvaliacoes(JsonObject d) throws SQLException {

    JsonArray lista = new JsonArray();

    try (Connection c = dados.conectar();
         PreparedStatement p = c.prepareStatement(
       "SELECT a.nota, a.comentario, a.criado, u.nome " +
           "FROM avaliacoes a " +
           "JOIN usuarios u ON u.id = a.usuario_id " +
           "WHERE a.prestador_id = ? " +
           "ORDER BY a.criado DESC"
         )) {

      p.setString(1, texto(d, "prestador_id"));

      try (ResultSet rs = p.executeQuery()) {

        while (rs.next()) {

          JsonObject objeto = new JsonObject();

          objeto.addProperty("nota", rs.getInt("nota"));
          objeto.addProperty("comentario", rs.getString("comentario"));
          objeto.addProperty("criado", rs.getLong("criado"));
          objeto.addProperty("nome", rs.getString("nome"));

          lista.add(objeto);
        }
      }
    }

    boolean contatado = false;

    if (!texto(d, "usuario_id").isBlank()) {

      try (Connection c = dados.conectar();
           PreparedStatement p = c.prepareStatement(
                   "SELECT 1 FROM contatos WHERE usuario_id=? AND prestador_id=?"
           )) {

        p.setString(1, texto(d, "usuario_id"));
        p.setString(2, texto(d, "prestador_id"));

        try (ResultSet rs = p.executeQuery()) {
          contatado = rs.next();
        }
      }
    }

    JsonObject resposta = sucesso();

    resposta.add("avaliacoes", lista);
    resposta.addProperty("contatado", contatado);

    return resposta;
  }

  public JsonObject gravarAvaliacao(JsonObject d) throws SQLException {
    String sql = "INSERT INTO avaliacoes(usuario_id,prestador_id,nota,comentario,criado) VALUES(?,?,?,?,?) ON DUPLICATE KEY UPDATE nota=VALUES(nota),comentario=VALUES(comentario),criado=VALUES(criado)";
    try (Connection c = dados.conectar(); PreparedStatement p = c.prepareStatement(sql)) {
      p.setString(1, texto(d, "usuario_id"));
      p.setString(2, texto(d, "prestador_id"));
      p.setInt(3, (int) numero(d, "nota"));
      p.setString(4, texto(d, "comentario"));
      p.setLong(5, numero(d, "criado"));
      p.executeUpdate();

      return sucesso();
    }
  }
}
