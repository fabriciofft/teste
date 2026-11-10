package br.com.quemresolve.intermediario.controllers;

import io.undertow.server.HttpServerExchange;

import br.com.quemresolve.intermediario.services.ProviderService;
import static br.com.quemresolve.intermediario.utils.JsonUtils.send;

public class ProviderController {

  private final ProviderService service;

  public ProviderController(ProviderService service) {
    this.service = service;
  }

  public void list(HttpServerExchange x) {
    send(x, 200, service.providers());
  }

  public void contact(HttpServerExchange x) throws Exception {
    send(x, 200, service.contact(x));
  }
}
