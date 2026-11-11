package br.com.quemresolve.dados.utilitarios;

import com.google.gson.JsonObject;

/** Formatos padrão de resposta: {ok}, {found:false} e {found:true, value}. */
public final class Respostas {
  private Respostas() {}

  public static JsonObject sucesso() {
    JsonObject o = new JsonObject();
    o.addProperty("ok", true);
    return o;
  }

  public static JsonObject no() {
    JsonObject o = new JsonObject();
    o.addProperty("found", false);
    return o;
  }

  public static JsonObject yes(JsonObject v) {
    JsonObject o = new JsonObject();
    o.addProperty("found", true);
    o.add("value", v);
    return o;
  }
}
