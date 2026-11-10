package br.com.quemresolve.intermediario.config;

import io.undertow.server.*;
import io.undertow.util.*;

import java.util.*;
import java.util.stream.Collectors;

public class CorsConfig {

  private final Set<String> origins;

  public CorsConfig(String origins) {
    this.origins = Arrays.stream(origins.split(","))
            .map(String::trim)
            .filter(s -> !s.isBlank())
            .collect(Collectors.toSet());
  }

  public boolean allows(String origin) {
    return origins.contains(origin);
  }

  // CORS

  public void cors(HttpServerExchange x) {

    String origin = x.getRequestHeaders()
            .getFirst(Headers.ORIGIN);

    x.getResponseHeaders().put(
            Headers.VARY,
            "Origin"
    );

    if (origin != null && origins.contains(origin)) {

      x.getResponseHeaders().put(
              new HttpString("Access-Control-Allow-Origin"),
              origin
      );

      x.getResponseHeaders().put(
              new HttpString("Access-Control-Allow-Credentials"),
              "true"
      );
    }

    x.getResponseHeaders().put(
            new HttpString("Access-Control-Allow-Methods"),
            "GET, POST, OPTIONS"
    );

    x.getResponseHeaders().put(
            new HttpString("Access-Control-Allow-Headers"),
            "Content-Type"
    );
  }
}
