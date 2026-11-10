package br.com.quemresolve.intermediario.websocket;

import br.com.quemresolve.intermediario.database.DbClient;

import com.google.gson.*;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.*;
import io.undertow.websockets.spi.WebSocketHttpExchange;

public class LiveSocket implements WebSocketConnectionCallback {
  private final DbClient db; private final Gson gson=new Gson();
  public LiveSocket(DbClient db){this.db=db;}
  public void onConnect(WebSocketHttpExchange exchange,WebSocketChannel channel){
    channel.getReceiveSetter().set(new AbstractReceiveListener(){
      protected void onFullTextMessage(WebSocketChannel ch,BufferedTextMessage msg){
        if(!msg.getData().equals("sync")){JsonObject e=new JsonObject();e.addProperty("error","Mensagem não reconhecida.");WebSockets.sendText(gson.toJson(e),ch,null);return;}
        try{JsonObject r=db.call("PROVIDERS",new JsonObject());JsonObject o=new JsonObject();o.add("providers",r.get("providers"));WebSockets.sendText(gson.toJson(o),ch,null);}
        catch(Exception ex){JsonObject e=new JsonObject();e.addProperty("error","Atualização indisponível");WebSockets.sendText(gson.toJson(e),ch,null);}
      }
    });
    channel.resumeReceives();
  }
}
