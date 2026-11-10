package br.com.quemresolve.dados.database;

import br.com.quemresolve.dados.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Abre conexões JDBC com o banco configurado. */
public class ConnectionFactory {
  private final Config cfg;

  public ConnectionFactory(Config c) {
    cfg = c;
  }

  public Connection con() throws SQLException {
    return DriverManager.getConnection(cfg.url(true), cfg.user(), cfg.password());
  }
}
