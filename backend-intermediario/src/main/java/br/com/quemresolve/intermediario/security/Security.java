package br.com.quemresolve.intermediario.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.HexFormat;
import java.util.UUID;

public final class Security {
  private Security(){}
  public static String token() {
    return UUID.randomUUID().toString()
            + UUID.randomUUID().toString();
  }
  public static String sha(String s){try{return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8)));}catch(Exception e){throw new RuntimeException(e);}}
  public static String hashPassword(String password){
    byte[] salt=new byte[16];new SecureRandom().nextBytes(salt);
    String saltHex=HexFormat.of().formatHex(salt);
    return saltHex+":"+derive(password,saltHex);
  }
  public static boolean verify(String password,String stored){
    int i=stored==null?-1:stored.indexOf(':');if(i<1)return false;
    String actual=derive(password,stored.substring(0,i));
    return MessageDigest.isEqual(actual.getBytes(StandardCharsets.US_ASCII),stored.substring(i+1).getBytes(StandardCharsets.US_ASCII));
  }
  private static String derive(String password,String salt){
    try{
      Mac mac=Mac.getInstance("HmacSHA256");mac.init(new SecretKeySpec(password.getBytes(StandardCharsets.UTF_8),"HmacSHA256"));
      byte[] s=salt.getBytes(StandardCharsets.UTF_8);byte[] first=ByteBuffer.allocate(s.length+4).put(s).putInt(1).array();
      byte[] u=mac.doFinal(first),t=u.clone();
      for(int n=1;n<100000;n++){u=mac.doFinal(u);for(int j=0;j<t.length;j++)t[j]^=u[j];}
      return HexFormat.of().formatHex(t);
    }catch(Exception e){throw new RuntimeException(e);}
  }
}
