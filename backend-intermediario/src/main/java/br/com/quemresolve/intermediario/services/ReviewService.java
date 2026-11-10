package br.com.quemresolve.intermediario.services;

import com.google.gson.*;
import io.undertow.server.*;
import io.undertow.util.*;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import br.com.quemresolve.intermediario.database.DbClient;
import br.com.quemresolve.intermediario.security.Security;
import br.com.quemresolve.intermediario.utils.ApiError;
import static br.com.quemresolve.intermediario.utils.JsonUtils.*;

public class ReviewService {

  private final DbClient db;
  private final SessionService sessions;

  public ReviewService(DbClient db, SessionService sessions) {
    this.db = db;
    this.sessions = sessions;
  }

  public JsonObject reviews(HttpServerExchange x) {

    String id = query(x.getQueryString(), "id").trim();

    if (id.isBlank()) {
      throw new ApiError(
              400,
              "Identificador do prestador não informado."
      );
    }

    String userId = "";

    String t = sessions.cookie(x);

    if (!t.isBlank()) {

      JsonObject r = db.call(
              "USER_SESSION",
              DbClient.obj(
                      "token", Security.sha(t),
                      "now", Long.toString(System.currentTimeMillis())
              )
      );

      if (DbClient.found(r)) {
        userId = s(r.getAsJsonObject("value"), "id");
      }
    }

    JsonObject r = db.call(
            "REVIEWS",
            DbClient.obj(
                    "providerId", id,
                    "userId", userId
            )
    );

    JsonObject o = new JsonObject();

    o.add("reviews", r.get("reviews"));
    o.add("contacted", r.get("contacted"));

    return o;
  }

  public JsonObject review(HttpServerExchange x) throws Exception {

    JsonObject u = sessions.auth(x);

    if (!s(u, "role").equals("client")) {
      throw new ApiError(
              403,
              "Use uma conta de cliente para avaliar."
      );
    }

    JsonObject b = body(x);

    String id = s(b, "id").trim();
    String comment = s(b, "comment").trim();

    double ratingValue = n(b, "rating", -1);

    if (id.isBlank()) {
      throw new ApiError(
              400,
              "Identificador do prestador não informado."
      );
    }

    if (!Double.isFinite(ratingValue) ||
            ratingValue != Math.floor(ratingValue) ||
            ratingValue < 1 ||
            ratingValue > 5) {

      throw new ApiError(
              400,
              "Nota inválida."
      );
    }

    int rating = (int) ratingValue;

    if (!Boolean.parseBoolean(s(b, "confirmed")) ||
            comment.length() < 10 ||
            comment.length() > 1000) {

      throw new ApiError(
              400,
              "Confirme o atendimento, selecione uma nota e escreva pelo menos 10 caracteres."
      );
    }

    JsonObject d = DbClient.obj(
            "userId", s(u, "id"),
            "providerId", id
    );

    JsonObject contact = db.call("HAS_CONTACT", d);

    if (!contact.has("exists") ||
            !contact.get("exists").getAsBoolean()) {

      throw new ApiError(
              403,
              "Você precisa iniciar contato antes de avaliar."
      );
    }

    d.addProperty("rating", rating);
    d.addProperty("comment", comment);
    d.addProperty("created", System.currentTimeMillis());

    db.call("UPSERT_REVIEW", d);

    JsonObject o = new JsonObject();
    o.addProperty("ok", true);

    return o;
  }
}
