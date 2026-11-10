package br.com.quemresolve.dados.util;

import com.google.gson.JsonObject;

/** Leitura segura de campos do JSON de entrada. */
public final class JsonArgs {
  private JsonArgs() {}

  public static String str(JsonObject o, String k) {
    return o.has(k) && !o.get(k).isJsonNull() ? o.get(k).getAsString() : "";
  }

  public static long num(JsonObject o, String k) {
    return o.has(k) && !o.get(k).isJsonNull() ? o.get(k).getAsLong() : 0;
  }
}
