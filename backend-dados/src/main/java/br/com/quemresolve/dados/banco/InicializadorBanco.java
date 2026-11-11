package br.com.quemresolve.dados.banco;

import br.com.quemresolve.dados.configuracao.Configuracao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Cria o banco e as tabelas, caso ainda não existam.
public class InicializadorBanco {
  private final Configuracao configuracao;
  private final DB dados;

  public InicializadorBanco(Configuracao configuracao, DB dados) {
    this.configuracao = configuracao;
    this.dados = dados;
  }

  public void inicializar() throws SQLException {
    if (!configuracao.dados().matches("[A-Za-z0-9_]+")){
      throw new SQLException("Nome de banco inválido");
    }
    try (Connection c = DriverManager.getConnection(configuracao.url(false), configuracao.user(), configuracao.senha()); Statement s = c.createStatement()) {
      s.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + configuracao.dados() + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
    }
    try (Connection c = dados.conectar(); Statement s = c.createStatement()) {
      s.executeUpdate("CREATE TABLE IF NOT EXISTS usuarios(id VARCHAR(36) PRIMARY KEY, nome VARCHAR(80) NOT NULL,email VARCHAR(190) NOT NULL UNIQUE,senha VARCHAR(255) NOT NULL,role VARCHAR(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
      s.executeUpdate("CREATE TABLE IF NOT EXISTS sessoes(token CHAR(64) PRIMARY KEY,usuario_id VARCHAR(36) NOT NULL,expira BIGINT NOT NULL,FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
      s.executeUpdate("CREATE TABLE IF NOT EXISTS prestadores(usuario_id VARCHAR(36) PRIMARY KEY,categoria VARCHAR(255) NOT NULL,titulo VARCHAR(100) NOT NULL,biografia VARCHAR(2000) NOT NULL,cidade VARCHAR(100) NOT NULL,regiao VARCHAR(300) NOT NULL,preco DECIMAL(12,2) NOT NULL,unidade VARCHAR(30) NOT NULL,celular VARCHAR(20) NOT NULL,latitude DOUBLE NULL,longitude DOUBLE NULL,raio DOUBLE NOT NULL,atualizado BIGINT NOT NULL,FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
      s.executeUpdate("CREATE TABLE IF NOT EXISTS contatos(usuario_id VARCHAR(36) NOT NULL,prestador_id VARCHAR(36) NOT NULL,PRIMARY KEY(usuario_id,prestador_id),FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,FOREIGN KEY(prestador_id) REFERENCES usuarios(id) ON DELETE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
      s.executeUpdate("CREATE TABLE IF NOT EXISTS avaliacoes(usuario_id VARCHAR(36) NOT NULL,prestador_id VARCHAR(36) NOT NULL,nota TINYINT NOT NULL,comentario VARCHAR(1000) NOT NULL,criado BIGINT NOT NULL,PRIMARY KEY(usuario_id,prestador_id),FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,FOREIGN KEY(prestador_id) REFERENCES usuarios(id) ON DELETE CASCADE) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
    }
    System.out.println("Banco e tabelas criados/verificados.");
  }
}
