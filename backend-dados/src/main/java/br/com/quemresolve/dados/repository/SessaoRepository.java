package br.com.quemresolve.dados.repository;

import br.com.quemresolve.dados.banco.DB;
import static br.com.quemresolve.dados.utilitarios.ArgumentosJson.*;
import static br.com.quemresolve.dados.utilitarios.Respostas.*;

import com.google.gson.*;
import java.sql.*;

public class SessaoRepository {
  private final DB dados;

  public SessaoRepository(DB dados) {
    this.dados = dados;
  }

  public JsonObject buscarSessaoUsuario(String token, long now) throws SQLException {
    try (Connection c = dados.conectar(); PreparedStatement p = c.prepareStatement("SELECT u.id,u.nome,u.email,u.role FROM sessoes s JOIN usuarios u ON u.id=s.usuario_id WHERE s.token=? AND s.expira>?")) {
      p.setString(1, token);
      p.setLong(2, now);

      try (ResultSet rs = p.executeQuery()) {
        if (!rs.next()) return no();
        JsonObject u = new JsonObject();
        u.addProperty("id", rs.getString("id"));
        u.addProperty("nome", rs.getString("nome"));
        u.addProperty("email", rs.getString("email"));
        u.addProperty("role", rs.getString("role"));
        return yes(u);
      }
    }
  }

  public JsonObject salvarSessao(JsonObject d) throws SQLException {
    try (Connection c = dados.conectar(); PreparedStatement p = c.prepareStatement("INSERT INTO sessoes(token,usuario_id,expira) VALUES(?,?,?)")) {
      p.setString(1, texto(d, "token"));
      p.setString(2, texto(d, "usuario_Id"));
      p.setLong(3, numero(d, "expira"));
      p.executeUpdate();
      return sucesso();
    }
  }

  public JsonObject excluirSessao(String token) throws SQLException {
    try (Connection c = dados.conectar(); PreparedStatement p = c.prepareStatement("DELETE FROM sessoes WHERE token=?")) {
      p.setString(1, token);
      p.executeUpdate();
      return sucesso();
    }
  }
}
