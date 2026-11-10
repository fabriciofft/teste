package br.com.quemresolve.intermediario.controllers;

import io.undertow.server.HttpServerExchange;

import br.com.quemresolve.intermediario.services.ReviewService;
import static br.com.quemresolve.intermediario.utils.JsonUtils.send;

public class ReviewController {

  private final ReviewService service;

  public ReviewController(ReviewService service) {
    this.service = service;
  }

  public void reviews(HttpServerExchange x) {
    send(x, 200, service.reviews(x));
  }

  public void review(HttpServerExchange x) throws Exception {
    send(x, 200, service.review(x));
  }
}
