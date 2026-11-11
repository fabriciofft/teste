package br.com.quemresolve.dados.utilitarios;

import com.google.gson.JsonObject;

// Leitura segura de campos do JSON de entrada
public final class ArgumentosJson {
  private ArgumentosJson() {}

  public static String texto(JsonObject o, String k) {
    return o.has(k) && !o.get(k).isJsonNull() ? o.get(k).getAsString() : "";
  }

  public static long numero(JsonObject o, String k) {
    return o.has(k) && !o.get(k).isJsonNull()
            ? o.get(k).getAsBigDecimal().longValue()
            : 0L;
  }

  public static double decimal(JsonObject o, String k) {
    return o.has(k) && !o.get(k).isJsonNull()
            ? o.get(k).getAsDouble()
            : 0.0;
  }
}
