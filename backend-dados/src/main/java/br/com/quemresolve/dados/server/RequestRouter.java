package br.com.quemresolve.dados.server;

import br.com.quemresolve.dados.database.ConnectionFactory;
import br.com.quemresolve.dados.repository.*;
import static br.com.quemresolve.dados.util.JsonArgs.*;

import com.google.gson.*;
import java.sql.*;

/** Recebe {"op": "...", "data": {...}} e chama o repositório correspondente. */
public class RequestRouter {
  private final UserRepository users;
  private final SessionRepository sessions;
  private final ProfileRepository profiles;
  private final ProviderRepository providers;
  private final ReviewRepository reviews;
  private final ContactRepository contacts;

  public RequestRouter(ConnectionFactory db) {
    users = new UserRepository(db);
    sessions = new SessionRepository(db);
    profiles = new ProfileRepository(db);
    providers = new ProviderRepository(db);
    reviews = new ReviewRepository(db);
    contacts = new ContactRepository(db);
  }

  public JsonObject execute(JsonObject r) throws SQLException {
    System.out.println(
            "OPERAÇÃO RECEBIDA: " + r.get("op").getAsString()
    );

    JsonObject d = r.has("data") && r.get("data").isJsonObject() ? r.getAsJsonObject("data") : new JsonObject();
    return switch (r.get("op").getAsString()) {
      case "USER_EMAIL" -> users.userEmail(str(d, "email"));
      case "USER_SESSION" -> sessions.userSession(str(d, "token"), num(d, "now"));
      case "INSERT_USER" -> users.insertUser(d);
      case "SAVE_SESSION" -> sessions.saveSession(d);
      case "DELETE_SESSION" -> sessions.deleteSession(str(d, "token"));
      case "PROFILE" -> profiles.profile(str(d, "userId"));
      case "PROVIDERS" -> providers.providers();
      case "UPSERT_PROFILE" -> {
        System.out.println("Executando UPSERT_PROFILE");
        yield profiles.upsertProfile(d);
      }
      case "REVIEWS" -> reviews.reviews(d);
      case "PHONE" -> profiles.phone(str(d, "providerId"));
      case "CONTACT" -> contacts.contact(d);
      case "HAS_CONTACT" -> contacts.hasContact(d);
      case "UPSERT_REVIEW" -> reviews.upsertReview(d);
      default -> throw new SQLException("Operação desconhecida");
    };
  }
}
