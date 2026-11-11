package br.com.quemresolve.dados.banco;

import br.com.quemresolve.dados.configuracao.Configuracao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Abre conexões JDBC com o banco configurado
public class DB {
  private final Configuracao configuracao;

  public DB(Configuracao configuracao) {
    this.configuracao = configuracao;
  }

  public Connection conectar() throws SQLException {
    return DriverManager.getConnection(
      configuracao.url(true),
      configuracao.user(),
      configuracao.senha()
    );
  }
}
