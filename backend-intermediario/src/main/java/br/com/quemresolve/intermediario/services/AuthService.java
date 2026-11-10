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

public class AuthService {

  private final DbClient db;
  private final SessionService sessions;

  public AuthService(DbClient db, SessionService sessions) {
    this.db = db;
    this.sessions = sessions;
  }

  public JsonObject register(HttpServerExchange x) throws Exception {

    JsonObject b = body(x);

    String name = s(b, "name").trim();
    String email = s(b, "email").trim().toLowerCase();
    String pass = s(b, "password");
    String role = s(b, "role");

    validateLogin(email, pass);

    if (name.length() < 3 ||
            name.length() > 80 ||
            !Set.of("client", "provider").contains(role)) {

      throw new ApiError(
              400,
              "Confira seu nome e tipo de conta."
      );
    }

    if (DbClient.found(
            db.call("USER_EMAIL", DbClient.obj("email", email))
    )) {
      throw new ApiError(
              409,
              "Este e-mail já está cadastrado."
      );
    }

    String id = UUID.randomUUID().toString();

    JsonObject d = DbClient.obj(
            "id", id,
            "name", name,
            "email", email,
            "password", Security.hashPassword(pass),
            "role", role
    );

    db.call("INSERT_USER", d);

    JsonObject u = publicUser(id, name, email, role);

    sessions.session(x, u);

    JsonObject out = new JsonObject();
    out.add("user", u);

    return out;
  }

  public JsonObject login(HttpServerExchange x) throws Exception {

    JsonObject b = body(x);

    String email = s(b, "email").trim().toLowerCase();
    String pass = s(b, "password");

    validateLogin(email, pass);

    JsonObject r = db.call(
            "USER_EMAIL",
            DbClient.obj("email", email)
    );

    if (!DbClient.found(r)) {
      throw new ApiError(
              401,
              "E-mail ou senha incorretos."
      );
    }

    JsonObject stored = r.getAsJsonObject("value");

    if (!Security.verify(pass, s(stored, "password"))) {
      throw new ApiError(
              401,
              "E-mail ou senha incorretos."
      );
    }

    JsonObject u = publicUser(
            s(stored, "id"),
            s(stored, "name"),
            s(stored, "email"),
            s(stored, "role")
    );

    sessions.session(x, u);

    JsonObject out = new JsonObject();
    out.add("user", u);

    return out;
  }

  public JsonObject me(HttpServerExchange x) {

    String t = sessions.cookie(x);

    JsonObject o = new JsonObject();

    if (t.isBlank()) {
      o.add("user", JsonNull.INSTANCE);
      o.add("profile", JsonNull.INSTANCE);

      return o;
    }

    JsonObject r = db.call(
            "USER_SESSION",
            DbClient.obj(
                    "token", Security.sha(t),
                    "now", Long.toString(System.currentTimeMillis())
            )
    );

    if (!DbClient.found(r)) {

      o.add("user", JsonNull.INSTANCE);
      o.add("profile", JsonNull.INSTANCE);

    } else {

      JsonObject u = r.getAsJsonObject("value");

      // Nunca enviar o objeto completo do banco.
      JsonObject safeUser = publicUser(
              s(u, "id"),
              s(u, "name"),
              s(u, "email"),
              s(u, "role")
      );

      JsonObject p = db.call(
              "PROFILE",
              DbClient.obj("userId", s(u, "id"))
      );

      o.add("user", safeUser);

      o.add(
              "profile",
              DbClient.found(p)
                      ? p.get("value")
                      : JsonNull.INSTANCE
      );
    }

    return o;
  }

  public JsonObject logout(HttpServerExchange x) {

    String t = sessions.cookie(x);

    if (!t.isBlank()) {
      db.call(
              "DELETE_SESSION",
              DbClient.obj("token", Security.sha(t))
      );
    }

    x.getResponseHeaders().put(
            Headers.SET_COOKIE,
            sessions.cookieHeader("", 0)
    );

    JsonObject o = new JsonObject();
    o.addProperty("ok", true);

    return o;
  }

  private JsonObject publicUser(
          String id,
          String name,
          String email,
          String role
  ) {

    return DbClient.obj(
            "id", id,
            "name", name,
            "email", email,
            "role", role
    );
  }

  private void validateLogin(String email, String p) {

    if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$") ||
            p.length() < 8 ||
            p.length() > 128) {

      throw new ApiError(
              400,
              "Informe um e-mail válido e senha de 8 a 128 caracteres."
      );
    }
  }
}
