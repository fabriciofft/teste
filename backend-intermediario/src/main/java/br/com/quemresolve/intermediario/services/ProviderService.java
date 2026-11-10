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

public class ProviderService {

  private final DbClient db;
  private final SessionService sessions;

  public ProviderService(DbClient db, SessionService sessions) {
    this.db = db;
    this.sessions = sessions;
  }

  public JsonObject providers() {

    JsonObject r = db.call("PROVIDERS", new JsonObject());

    JsonObject o = new JsonObject();
    o.add("providers", r.get("providers"));

    return o;
  }

  public JsonObject contact(HttpServerExchange x) throws Exception {

    JsonObject u = sessions.auth(x);

    if (!s(u, "role").equals("client")) {
      throw new ApiError(
              403,
              "Use uma conta de cliente para solicitar atendimento."
      );
    }

    String id = s(body(x), "id").trim();

    if (id.isBlank()) {
      throw new ApiError(
              400,
              "Identificador do prestador não informado."
      );
    }

    JsonObject p = db.call(
            "PHONE",
            DbClient.obj("providerId", id)
    );

    if (!DbClient.found(p)) {
      throw new ApiError(
              404,
              "Prestador não encontrado."
      );
    }

    db.call(
            "CONTACT",
            DbClient.obj(
                    "userId", s(u, "id"),
                    "providerId", id
            )
    );

    String phone = s(
            p.getAsJsonObject("value"),
            "phone"
    );

    String msg = URLEncoder.encode(
            "Olá! Encontrei seu perfil no Quem Resolve e gostaria de um orçamento.",
            StandardCharsets.UTF_8
    );

    JsonObject o = new JsonObject();

    o.addProperty(
            "url",
            "https://wa.me/" + phone + "?text=" + msg
    );

    return o;
  }
}
