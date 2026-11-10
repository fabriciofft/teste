package br.com.quemresolve.intermediario.database;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class DbClient {
  private final String host; private final int port; private final Gson gson=new Gson();
  public DbClient(String h,int p){host=h;port=p;}
  public JsonObject call(String op,JsonObject data){
    JsonObject req=new JsonObject();req.addProperty("op",op);req.add("data",data);
    try(Socket s=new Socket()){
      s.connect(new InetSocketAddress(host,port),3000);s.setSoTimeout(7000);
      try(PrintWriter out=new PrintWriter(new OutputStreamWriter(s.getOutputStream(),StandardCharsets.UTF_8),true);
          BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream(),StandardCharsets.UTF_8))){
        out.println(gson.toJson(req));String line=in.readLine();if(line==null)throw new IOException("Resposta vazia");
        JsonObject result=JsonParser.parseString(line).getAsJsonObject();
        if(result.has("error"))throw new IllegalStateException(result.get("error").getAsString());
        return result;
      }
    }catch(Exception e){throw new IllegalStateException("Falha ao chamar backend de dados: "+e.getMessage(),e);}
  }
  public static JsonObject obj(String... pairs){JsonObject o=new JsonObject();for(int i=0;i<pairs.length;i+=2)o.addProperty(pairs[i],pairs[i+1]);return o;}
  public static boolean found(JsonObject o){return o.has("found")&&o.get("found").getAsBoolean();}
}
