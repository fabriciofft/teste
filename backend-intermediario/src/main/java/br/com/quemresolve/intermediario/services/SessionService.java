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

public class SessionService {

  private final DbClient db;
  private final boolean secure;

  public SessionService(DbClient db, boolean secure) {
    this.db = db;
    this.secure = secure;
  }

  public JsonObject auth(HttpServerExchange x) {

    String t = cookie(x);

    if (t.isBlank()) {
      throw new ApiError(
              401,
              "Entre na sua conta para continuar."
      );
    }

    JsonObject r = db.call(
            "USER_SESSION",
            DbClient.obj(
                    "token", Security.sha(t),
                    "now", Long.toString(System.currentTimeMillis())
            )
    );

    if (!DbClient.found(r)) {
      throw new ApiError(
              401,
              "Entre na sua conta para continuar."
      );
    }

    return r.getAsJsonObject("value");
  }

  public void session(HttpServerExchange x, JsonObject u) {

    String raw = Security.token();

    db.call(
            "SAVE_SESSION",
            DbClient.obj(
                    "token", Security.sha(raw),
                    "userId", s(u, "id"),
                    "expires", Long.toString(
                            System.currentTimeMillis() + 604800000L
                    )
            )
    );

    x.getResponseHeaders().put(
            Headers.SET_COOKIE,
            cookieHeader(raw, 604800)
    );
  }

  public String cookieHeader(String value, long maxAge) {

    String sameSite = secure
            ? "SameSite=None; Secure"
            : "SameSite=Lax";

    return "qr_session=" + value +
            "; HttpOnly" +
            "; " + sameSite +
            "; Path=/" +
            "; Max-Age=" + maxAge;
  }

  public String cookie(HttpServerExchange x) {

    String c = x.getRequestHeaders()
            .getFirst(Headers.COOKIE);

    if (c == null) {
      return "";
    }

    for (String p : c.split(";")) {

      String[] a = p.trim().split("=", 2);

      if (a.length == 2 &&
              a[0].equals("qr_session")) {

        return a[1];
      }
    }

    return "";
  }
}
