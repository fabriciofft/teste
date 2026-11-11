package br.com.quemresolve.dados.repository;

import br.com.quemresolve.dados.banco.DB;
import static br.com.quemresolve.dados.utilitarios.ArgumentosJson.*;
import static br.com.quemresolve.dados.utilitarios.Respostas.*;

import com.google.gson.*;
import java.sql.*;

public class ContatoRepository {
  private final DB dados;

  public ContatoRepository(DB dados) {
    this.dados = dados;
  }

  public JsonObject contatarPrestador(JsonObject d) throws SQLException {
    try (Connection c = dados.conectar(); PreparedStatement p = c.prepareStatement("INSERT IGNORE INTO contatos(usuario_id,prestador_id) VALUES(?,?)")) {

      p.setString(1, texto(d, "usuario_id"));
      p.setString(2, texto(d, "prestador_id"));
      p.executeUpdate();

      return sucesso();
    }
  }

  public JsonObject possuiContato(JsonObject d) throws SQLException {
    try (Connection c = dados.conectar(); PreparedStatement p = c.prepareStatement("SELECT 1 FROM contatos WHERE usuario_id=? AND prestador_id=?")) {
      p.setString(1, texto(d, "usuario_id"));
      p.setString(2, texto(d, "prestador_id"));

      try (ResultSet rs = p.executeQuery()) {
        JsonObject o = sucesso();
        o.addProperty("exists", rs.next());
        return o;
      }
    }
  }
}
