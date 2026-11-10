package br.com.quemresolve.dados;

import br.com.quemresolve.dados.config.Config;
import br.com.quemresolve.dados.database.ConnectionFactory;
import br.com.quemresolve.dados.database.SchemaInitializer;
import br.com.quemresolve.dados.server.RequestRouter;
import br.com.quemresolve.dados.server.TcpServer;

public class Main {
  public static void main(String[] args) throws Exception {
    Config c = Config.load();
    ConnectionFactory db = new ConnectionFactory(c);
    new SchemaInitializer(c, db).initialize();
    new TcpServer(c, new RequestRouter(db)).start();
  }
}
