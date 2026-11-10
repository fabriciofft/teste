package br.com.quemresolve.intermediario.controllers;

import io.undertow.server.HttpServerExchange;

import br.com.quemresolve.intermediario.services.ProfileService;
import static br.com.quemresolve.intermediario.utils.JsonUtils.send;

public class ProfileController {

  private final ProfileService service;

  public ProfileController(ProfileService service) {
    this.service = service;
  }

  public void profile(HttpServerExchange x) throws Exception {
    send(x, 200, service.profile(x));
  }
}
