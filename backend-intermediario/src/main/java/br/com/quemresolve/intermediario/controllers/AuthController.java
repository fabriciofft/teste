package br.com.quemresolve.intermediario.controllers;

import io.undertow.server.HttpServerExchange;

import br.com.quemresolve.intermediario.services.AuthService;
import static br.com.quemresolve.intermediario.utils.JsonUtils.send;

public class AuthController {

  private final AuthService service;

  public AuthController(AuthService service) {
    this.service = service;
  }

  public void register(HttpServerExchange x) throws Exception {
    send(x, 200, service.register(x));
  }

  public void login(HttpServerExchange x) throws Exception {
    send(x, 200, service.login(x));
  }

  public void me(HttpServerExchange x) {
    send(x, 200, service.me(x));
  }

  public void logout(HttpServerExchange x) {
    send(x, 200, service.logout(x));
  }
}
