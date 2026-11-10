package br.com.quemresolve.dados.server;

import br.com.quemresolve.dados.config.Config;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

/** Servidor TCP: uma linha JSON por requisição, uma linha JSON por resposta. */
public class TcpServer {
  private final Config c;
  private final RequestRouter router;

  public TcpServer(Config c, RequestRouter router) {
    this.c = c;
    this.router = router;
  }

  public void start() throws Exception {
    ExecutorService pool = Executors.newFixedThreadPool(12);
    try (ServerSocket server = new ServerSocket(
        c.listenPort(), 100, InetAddress.getByName(c.listenHost())
    )) {
      System.out.println("Backend de dados TCP em " + c.listenHost() + ":" + c.listenPort());
      while (true) {
        Socket s = server.accept();
        pool.submit(() -> {
          try (s;
               BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
               PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true)) {
            s.setSoTimeout(10000);
            String line = in.readLine();
            JsonObject response = router.execute(JsonParser.parseString(line).getAsJsonObject());
            out.println(new Gson().toJson(response));
          } catch (Exception e) {
            System.err.println("Falha TCP/SQL: " + e.getMessage());
            try { s.close(); } catch (Exception ignored) {}
          }
        });
      }
    }
  }
}
