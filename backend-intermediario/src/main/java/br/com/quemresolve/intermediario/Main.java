package br.com.quemresolve.intermediario;

import io.undertow.*;
import io.undertow.server.handlers.PathHandler;

import br.com.quemresolve.intermediario.config.CorsConfig;
import br.com.quemresolve.intermediario.config.ServerConfig;
import br.com.quemresolve.intermediario.database.DbClient;
import br.com.quemresolve.intermediario.routes.ApiRoutes;
import br.com.quemresolve.intermediario.websocket.LiveSocket;

public class Main {
  public static void main(String[] args) {
    ServerConfig config=new ServerConfig();
    String dbHost=config.dbHost;
    int dbPort=config.dbPort;
    int port=config.port;
    String host=config.host;
    String origins=config.origins;
    boolean secure=config.secure;
    DbClient db=new DbClient(dbHost,dbPort);
    ApiRoutes api=new ApiRoutes(db,new CorsConfig(origins),secure);
    PathHandler paths=io.undertow.Handlers.path()
      .addExactPath("/api/live",io.undertow.Handlers.websocket(new LiveSocket(db)))
      .addPrefixPath("/api",api);
    Undertow server=Undertow.builder().addHttpListener(port,host).setHandler(paths).build();
    server.start();
    System.out.println("Backend intermediário HTTP/WebSocket em "+host+":"+port);
  }
}
