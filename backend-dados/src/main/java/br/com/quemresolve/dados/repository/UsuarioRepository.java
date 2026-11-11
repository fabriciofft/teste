package br.com.quemresolve.dados.repository;

import br.com.quemresolve.dados.banco.DB;
import static br.com.quemresolve.dados.utilitarios.ArgumentosJson.*;
import static br.com.quemresolve.dados.utilitarios.Respostas.*;
import com.google.gson.*;
import java.sql.*;

public class UsuarioRepository {
  private final DB db;

  public UsuarioRepository(DB db) {
    this.db = db;
  }

  public JsonObject buscarUsuarioPorEmail(String email) throws SQLException {
    try (Connection conexao = db.conectar(); PreparedStatement comando = conexao.prepareStatement("SELECT id,nome,email,senha,role FROM usuarios WHERE email=?")) {
      comando.setString(1, email);

      try (ResultSet resultado = comando.executeQuery()) {
        if (!resultado.next()) return no();

        JsonObject usuario = new JsonObject();

        usuario.addProperty("id", resultado.getString("id"));
        usuario.addProperty("nome", resultado.getString("nome"));
        usuario.addProperty("email", resultado.getString("email"));
        usuario.addProperty("senha", resultado.getString("senha"));
        usuario.addProperty("role", resultado.getString("role"));

        return yes(usuario);
      }
    }
  }

  public JsonObject inserirUsuario(JsonObject dadosUsuario) throws SQLException {
    try (Connection conexao = db.conectar(); PreparedStatement comando = conexao.prepareStatement("INSERT INTO usuarios(id,nome,email,senha,role) VALUES(?,?,?,?,?)")) {
      comando.setString(1, texto(dadosUsuario, "id"));
      comando.setString(2, texto(dadosUsuario, "nome"));
      comando.setString(3, texto(dadosUsuario, "email"));
      comando.setString(4, texto(dadosUsuario, "senha"));
      comando.setString(5, texto(dadosUsuario, "role"));
      comando.executeUpdate();
      return sucesso();
    }
  }
}
