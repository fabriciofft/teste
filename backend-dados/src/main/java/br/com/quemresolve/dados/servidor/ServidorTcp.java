package br.com.quemresolve.dados.servidor;

import br.com.quemresolve.dados.configuracao.Configuracao;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

// Servidor TCP: uma linha JSON por requisição, uma linha JSON por resposta.
public class ServidorTcp {
  private final Configuracao configuracao;
  private final RoteadorRequisicoes roteador;

  public ServidorTcp(Configuracao configuracao, RoteadorRequisicoes roteador) {
    this.configuracao = configuracao;
    this.roteador = roteador;
  }

  public void iniciar() throws Exception {
    ExecutorService executor = Executors.newFixedThreadPool(12);
    try (ServerSocket server = new ServerSocket(
        configuracao.listenPort(), 100, InetAddress.getByName(configuracao.listenHost())
    )) {
      System.out.println("Backend de dados TCP em " + configuracao.listenHost() + ":" + configuracao.listenPort());
      while (true) {
        Socket s = server.accept();
        executor.submit(() -> {
          try (s;
               BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
               PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true)) {
            s.setSoTimeout(10000);
            String linha = in.readLine();
            JsonObject resposta = roteador.executar(JsonParser.parseString(linha).getAsJsonObject());
            out.println(new Gson().toJson(resposta));
          } catch (Exception e) {
            System.err.println("Falha TCP/SQL: " + e.getMessage());
            try { s.close(); } catch (Exception ignored) {}
          }
        });
      }
    }
  }
}
