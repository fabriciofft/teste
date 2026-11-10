package br.com.quemresolve.intermediario.routes;

import com.google.gson.*;
import io.undertow.server.*;
import io.undertow.util.*;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import br.com.quemresolve.intermediario.config.CorsConfig;
import br.com.quemresolve.intermediario.controllers.*;
import br.com.quemresolve.intermediario.database.DbClient;
import br.com.quemresolve.intermediario.services.*;
import br.com.quemresolve.intermediario.utils.ApiError;
import static br.com.quemresolve.intermediario.utils.JsonUtils.*;

public class ApiRoutes implements HttpHandler {

  private final CorsConfig cors;
  private final AuthController authController;
  private final ProfileController profileController;
  private final ProviderController providerController;
  private final ReviewController reviewController;

  public ApiRoutes(DbClient db, CorsConfig cors, boolean secure) {
    this.cors = cors;

    SessionService sessions = new SessionService(db, secure);

    this.authController = new AuthController(new AuthService(db, sessions));
    this.profileController = new ProfileController(new ProfileService(db, sessions));
    this.providerController = new ProviderController(new ProviderService(db, sessions));
    this.reviewController = new ReviewController(new ReviewService(db, sessions));
  }

  @Override
  public void handleRequest(HttpServerExchange x) {
    cors.cors(x);

    // Requisições OPTIONS (preflight do CORS)
    if (Methods.OPTIONS.equals(x.getRequestMethod())) {
      x.setStatusCode(204);
      x.endExchange();
      return;
    }

    // Transfere o processamento para uma Worker Thread
    x.dispatch(() -> {

      try {

        x.startBlocking();

        String origin = x.getRequestHeaders()
                .getFirst(Headers.ORIGIN);

        if (origin != null && !cors.allows(origin)) {
          send(x, 403, err("Origem inválida."));
          return;
        }

        String path = x.getRequestPath();
        String method = x.getRequestMethod().toString();

        if (method.equals("POST") && path.equals("/api/register")) {
          authController.register(x);

        } else if (method.equals("POST") && path.equals("/api/login")) {
          authController.login(x);

        } else if (method.equals("GET") && path.equals("/api/me")) {
          authController.me(x);

        } else if (method.equals("POST") && path.equals("/api/logout")) {
          authController.logout(x);

        } else if (method.equals("GET") && path.equals("/api/providers")) {

          providerController.list(x);

        } else if (method.equals("POST") && path.equals("/api/profile")) {
          profileController.profile(x);

        } else if (method.equals("GET") && path.equals("/api/reviews")) {
          reviewController.reviews(x);

        } else if (method.equals("POST") && path.equals("/api/contact")) {
          providerController.contact(x);

        } else if (method.equals("POST") && path.equals("/api/review")) {
          reviewController.review(x);

        } else {
          send(x, 404, err("Operação não encontrada."));
        }

      } catch (ApiError e) {

        send(x, e.status, err(e.getMessage()));

      } catch (Exception e) {

        System.err.println("Erro API: " + e.getMessage());
        e.printStackTrace();

        send(x, 500, err(
                "Não foi possível concluir. Tente novamente."
        ));
      }
    });
  }
}
