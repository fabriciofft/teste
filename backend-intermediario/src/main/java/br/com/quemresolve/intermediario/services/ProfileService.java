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

public class ProfileService {

  private final DbClient db;
  private final SessionService sessions;

  private static final Set<String> CATS = Set.of(
          "Eletricista",
          "Encanador",
          "Limpeza",
          "Pintor",
          "Jardinagem",
          "Montador",
          "Ar-condicionado",
          "Reformas"
  );

  private static final Set<String> UNITS = Set.of(
          "por serviço",
          "por hora",
          "por diária",
          "a combinar"
  );

  public ProfileService(DbClient db, SessionService sessions) {
    this.db = db;
    this.sessions = sessions;
  }

  public JsonObject profile(HttpServerExchange x) throws Exception {

    JsonObject u = sessions.auth(x);

    if (!s(u, "role").equals("provider")) {
      throw new ApiError(
              403,
              "Apenas prestadores podem editar perfis profissionais."
      );
    }

    JsonObject b = body(x);

    String cat = s(b, "category").trim();
    String title = s(b, "title").trim();
    String bio = s(b, "bio").trim();
    String city = s(b, "city").trim();
    String region = s(b, "region").trim();
    String unit = s(b, "unit");
    String phone = s(b, "phone").replaceAll("\\D", "");

    double price = n(b, "price", Double.NaN);
    double radius = n(b, "radius", Double.NaN);

    Double lat = nullable(b, "lat");
    Double lng = nullable(b, "lng");

    boolean cats = !cat.isBlank() &&
            Arrays.stream(cat.split(",", -1))
                    .map(String::trim)
                    .allMatch(CATS::contains);

    if (!cats ||
            title.isBlank() ||
            title.length() > 100 ||
            bio.isBlank() ||
            bio.length() > 2000 ||
            city.isBlank() ||
            city.length() > 100 ||
            region.isBlank() ||
            region.length() > 300 ||
            !Double.isFinite(price) ||
            price < 0 ||
            price > 1_000_000 ||
            !Double.isFinite(radius) ||
            radius < 1 ||
            radius > 500 ||
            !phone.matches("^55\\d{10,11}$") ||
            !UNITS.contains(unit) ||
            (lat != null && (!Double.isFinite(lat) || Math.abs(lat) > 90)) ||
            (lng != null && (!Double.isFinite(lng) || Math.abs(lng) > 180)) ||
            ((lat == null) != (lng == null))) {

      throw new ApiError(
              400,
              "Confira os campos. WhatsApp deve incluir 55 + DDD + número. Defina uma região, preço válido e raio de 1 a 500 km."
      );
    }

    JsonObject d = DbClient.obj(
            "userId", s(u, "id"),
            "category", cat,
            "title", title,
            "bio", bio,
            "city", city,
            "region", region,
            "unit", unit,
            "phone", phone,
            "radius", Double.toString(radius),
            "price", Double.toString(price),
            "updated", Long.toString(System.currentTimeMillis())
    );

    if (lat == null) {
      d.add("lat", JsonNull.INSTANCE);
    } else {
      d.addProperty("lat", lat);
    }

    if (lng == null) {
      d.add("lng", JsonNull.INSTANCE);
    } else {
      d.addProperty("lng", lng);
    }

    db.call("UPSERT_PROFILE", d);

    JsonObject o = new JsonObject();
    o.addProperty("ok", true);

    return o;
  }
}
