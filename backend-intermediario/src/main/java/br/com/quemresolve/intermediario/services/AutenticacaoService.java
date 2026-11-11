package br.com.quemresolve.intermediario.services;

import com.google.gson.*;
import io.undertow.server.*;
import io.undertow.util.*;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import br.com.quemresolve.intermediario.banco.ClienteDados;
import br.com.quemresolve.intermediario.seguranca.Seguranca;
import br.com.quemresolve.intermediario.utilitarios.ErroApi;
import static br.com.quemresolve.intermediario.utilitarios.UtilJson.*;

public class AutenticacaoService {

  private final ClienteDados dados;
  private final SessaoService sessoes;

  public AutenticacaoService(ClienteDados dados, SessaoService sessoes) {
    this.dados = dados;
    this.sessoes = sessoes;
  }

  public JsonObject cadastrar(HttpServerExchange x) throws Exception {

    JsonObject b = corpo(x);

    String nome = s(b, "nome").trim();
    String email = s(b, "email").trim().toLowerCase();
    String senha = s(b, "senha");
    String role = s(b, "role");

    validarLogin(email, senha);

    if (nome.length() < 3 || nome.length() > 80
        || !Set.of("cliente", "prestador").contains(role)) {

      throw new ErroApi(400, "Confira seu nome e tipo de conta."
      );
    }

    if (ClienteDados.encontrado(dados.chamar("USER_EMAIL", ClienteDados.objeto("email", email)))) {

      throw new ErroApi(409, "Este e-mail já está cadastrado.");
    }

    String id = UUID.randomUUID().toString();

    JsonObject objeto = ClienteDados.objeto(
     "id", id,
            "nome", nome,
            "email", email,
            "senha", Seguranca.resumirSenha(senha),
            "role", role
    );

    dados.chamar("INSERT_USER", objeto);

    JsonObject usuario = montarUsuarioPublico(id, nome, email,  role);

    sessoes.abrirSessao(x, usuario);

    JsonObject saida = new JsonObject();
    saida.add("usuario", usuario);

    return saida;
  }

  public JsonObject entrar(HttpServerExchange x) throws Exception {

    JsonObject b = corpo(x);

    String email = s(b, "email").trim().toLowerCase();
    String senha = s(b, "senha");

    validarLogin(email, senha);

    JsonObject r = dados.chamar("USER_EMAIL",ClienteDados.objeto("email", email));

    if (!ClienteDados.encontrado(r)) {

      throw new ErroApi(401, "E-mail ou senha incorretos.");
    }

    JsonObject armazenado = r.getAsJsonObject("value");

    if (!Seguranca.verificarSenha(senha, s(armazenado, "senha"))) {

      throw new ErroApi(401, "E-mail ou senha incorretos.");
    }

    JsonObject usuario = montarUsuarioPublico(
        s(armazenado, "id"),
        s(armazenado, "nome"),
        s(armazenado, "email"),
        s(armazenado, "role")
    );

    sessoes.abrirSessao(x, usuario);

    JsonObject resposta = new JsonObject();
    resposta.add("usuario", usuario);

    return resposta;
  }

  public JsonObject usuarioAtual(HttpServerExchange x) {
    String t = sessoes.obterCookie(x);

    JsonObject o = new JsonObject();

    if (t.isBlank()) {
      o.add("usuario", JsonNull.INSTANCE);
      o.add("perfil", JsonNull.INSTANCE);

      return o;
    }

    JsonObject r = dados.chamar(
            "USER_SESSION",
            ClienteDados.objeto(
                    "token", Seguranca.calcularSha(t),
                    "now", Long.toString(System.currentTimeMillis())
            )
    );

    if (!ClienteDados.encontrado(r)) {

      o.add("user", JsonNull.INSTANCE);
      o.add("profile", JsonNull.INSTANCE);

    } else {

      JsonObject u = r.getAsJsonObject("value");

      // Nunca enviar o objeto completo do banco.
      JsonObject safeUser = montarUsuarioPublico(
              s(u, "id"),
              s(u, "nome"),
              s(u, "email"),
              s(u, "role")
      );

      JsonObject p = dados.chamar("PROFILE",ClienteDados.objeto("userId", s(u, "id")));

      o.add("user", safeUser);

      o.add("profile", ClienteDados.encontrado(p) ? p.get("value"): JsonNull.INSTANCE);
    }

    return o;
  }

  public JsonObject sair(HttpServerExchange x) {
    String t = sessoes.obterCookie(x);

    if (!t.isBlank()) {

      dados.chamar("DELETE_SESSION", ClienteDados.objeto("token", Seguranca.calcularSha(t)));
    }

    x.getResponseHeaders().put(Headers.SET_COOKIE, sessoes.cabecalhoCookie("", 0));

    JsonObject objeto = new JsonObject();
    objeto.addProperty("ok", true);

    return objeto;
  }

  private JsonObject montarUsuarioPublico(
    String id,
    String nome,
    String email,
    String role
  ) {

    return ClienteDados.objeto(
      "id", id,
      "nome", nome,
      "email", email,
      "role", role
    );
  }

  private void validarLogin(String email, String senha) {

    if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
        || senha.length() < 8 || senha.length() > 128) {

      throw new ErroApi(400, "Informe um e-mail válido e senha de 8 a 128 caracteres.");
    }
  }
}
