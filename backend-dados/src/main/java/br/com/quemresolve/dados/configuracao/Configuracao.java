package br.com.quemresolve.dados.configuracao;

public record Configuracao(String host, int port, String dados, String user, String senha, String listenHost, int listenPort) {

  static String env(String k, String d) {
    String v = System.getenv(k);
    return v == null || v.isBlank() ? d : v;
  }

  public static Configuracao carregar() {
    return new Configuracao(env("DB_HOST", "localhost"), Integer.parseInt(env("DB_PORT", "3306")),
      env("DB_NAME", "quemresolve"), env("DB_USER", "root"), env("DB_PASSWORD", "123456"),
      env("DB_SERVER_HOST", "127.0.0.1"), Integer.parseInt(env("DB_SERVER_PORT", "8081")));
  }

  public String url(boolean withDb) {
    return "jdbc:mysql://" + host + ":" + port + (withDb ? "/" + dados : "/") + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
  }
}
