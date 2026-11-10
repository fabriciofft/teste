package br.com.quemresolve.dados.database;

import br.com.quemresolve.dados.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Cria o banco e as tabelas, caso ainda não existam. */
public class SchemaInitializer {
  private final Config cfg;
  private final ConnectionFactory db;

  public SchemaInitializer(Config c, ConnectionFactory db) {
    this.cfg = c;
    this.db = db;
  }

  public void initialize() throws SQLException {
    if (!cfg.db().matches("[A-Za-z0-9_]+")) throw new SQLException("Nome de banco inválido");
    try (Connection c = DriverManager.getConnection(cfg.url(false), cfg.user(), cfg.password()); Statement s = c.createStatement()) {
      s.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + cfg.db() + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
    }
    try (Connection c = db.con(); Statement s = c.createStatement()) {
      s.executeUpdate("CREATE TABLE IF NOT EXISTS users(id VARCHAR(36) PRIMARY KEY,name VARCHAR(80) NOT NULL,email VARCHAR(190) NOT NULL UNIQUE,password VARCHAR(255) NOT NULL,role VARCHAR(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
      s.executeUpdate("CREATE TABLE IF NOT EXISTS sessions(token CHAR(64) PRIMARY KEY,user_id VARCHAR(36) NOT NULL,expires BIGINT NOT NULL,FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
      s.executeUpdate("CREATE TABLE IF NOT EXISTS profiles(user_id VARCHAR(36) PRIMARY KEY,category VARCHAR(255) NOT NULL,title VARCHAR(100) NOT NULL,bio VARCHAR(2000) NOT NULL,city VARCHAR(100) NOT NULL,region VARCHAR(300) NOT NULL,price DECIMAL(12,2) NOT NULL,unit VARCHAR(30) NOT NULL,phone VARCHAR(20) NOT NULL,lat DOUBLE NULL,lng DOUBLE NULL,radius DOUBLE NOT NULL,updated BIGINT NOT NULL,FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
      s.executeUpdate("CREATE TABLE IF NOT EXISTS contacts(user_id VARCHAR(36) NOT NULL,provider_id VARCHAR(36) NOT NULL,PRIMARY KEY(user_id,provider_id),FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,FOREIGN KEY(provider_id) REFERENCES users(id) ON DELETE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
      s.executeUpdate("CREATE TABLE IF NOT EXISTS reviews(user_id VARCHAR(36) NOT NULL,provider_id VARCHAR(36) NOT NULL,rating TINYINT NOT NULL,comment VARCHAR(1000) NOT NULL,created BIGINT NOT NULL,PRIMARY KEY(user_id,provider_id),FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,FOREIGN KEY(provider_id) REFERENCES users(id) ON DELETE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
    }
    System.out.println("Banco e tabelas criados/verificados.");
  }
}
