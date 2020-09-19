/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import com.beust.jcommander.IStringConverter;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InetAddressConverter
/*    */   implements IStringConverter<InetAddress>
/*    */ {
/*    */   public InetAddress convert(String host) {
/*    */     try {
/* 29 */       return InetAddress.getByName(host);
/* 30 */     } catch (UnknownHostException e) {
/* 31 */       throw new IllegalArgumentException(host, e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\InetAddressConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */