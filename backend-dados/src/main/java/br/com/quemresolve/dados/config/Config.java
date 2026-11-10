package br.com.quemresolve.dados.config;

public record Config(String host, int port, String db, String user, String password, String listenHost, int listenPort) {

  static String env(String k, String d) {
    String v = System.getenv(k);
    return v == null || v.isBlank() ? d : v;
  }

  public static Config load() {
    return new Config(env("DB_HOST", "localhost"), Integer.parseInt(env("DB_PORT", "3306")),
      env("DB_NAME", "quemresolve"), env("DB_USER", "root"), env("DB_PASSWORD", "123456"),
      env("DB_SERVER_HOST", "127.0.0.1"), Integer.parseInt(env("DB_SERVER_PORT", "8081")));
  }

  public String url(boolean withDb) {
    return "jdbc:mysql://" + host + ":" + port + (withDb ? "/" + db : "/") + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
  }
}
