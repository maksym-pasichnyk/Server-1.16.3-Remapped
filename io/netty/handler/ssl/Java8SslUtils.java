/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import javax.net.ssl.SNIHostName;
/*    */ import javax.net.ssl.SNIMatcher;
/*    */ import javax.net.ssl.SNIServerName;
/*    */ import javax.net.ssl.SSLParameters;
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
/*    */ final class Java8SslUtils
/*    */ {
/*    */   static List<String> getSniHostNames(SSLParameters sslParameters) {
/* 33 */     List<SNIServerName> names = sslParameters.getServerNames();
/* 34 */     if (names == null || names.isEmpty()) {
/* 35 */       return Collections.emptyList();
/*    */     }
/* 37 */     List<String> strings = new ArrayList<String>(names.size());
/*    */     
/* 39 */     for (SNIServerName serverName : names) {
/* 40 */       if (serverName instanceof SNIHostName) {
/* 41 */         strings.add(((SNIHostName)serverName).getAsciiName()); continue;
/*    */       } 
/* 43 */       throw new IllegalArgumentException("Only " + SNIHostName.class.getName() + " instances are supported, but found: " + serverName);
/*    */     } 
/*    */ 
/*    */     
/* 47 */     return strings;
/*    */   }
/*    */   
/*    */   static void setSniHostNames(SSLParameters sslParameters, List<String> names) {
/* 51 */     List<SNIServerName> sniServerNames = new ArrayList<SNIServerName>(names.size());
/* 52 */     for (String name : names) {
/* 53 */       sniServerNames.add(new SNIHostName(name));
/*    */     }
/* 55 */     sslParameters.setServerNames(sniServerNames);
/*    */   }
/*    */   
/*    */   static boolean getUseCipherSuitesOrder(SSLParameters sslParameters) {
/* 59 */     return sslParameters.getUseCipherSuitesOrder();
/*    */   }
/*    */   
/*    */   static void setUseCipherSuitesOrder(SSLParameters sslParameters, boolean useOrder) {
/* 63 */     sslParameters.setUseCipherSuitesOrder(useOrder);
/*    */   }
/*    */ 
/*    */   
/*    */   static void setSNIMatchers(SSLParameters sslParameters, Collection<?> matchers) {
/* 68 */     sslParameters.setSNIMatchers((Collection)matchers);
/*    */   }
/*    */ 
/*    */   
/*    */   static boolean checkSniHostnameMatch(Collection<?> matchers, String hostname) {
/* 73 */     if (matchers != null && !matchers.isEmpty()) {
/* 74 */       SNIHostName name = new SNIHostName(hostname);
/* 75 */       Iterator<SNIMatcher> matcherIt = (Iterator)matchers.iterator();
/* 76 */       while (matcherIt.hasNext()) {
/* 77 */         SNIMatcher matcher = matcherIt.next();
/*    */         
/* 79 */         if (matcher.getType() == 0 && matcher.matches(name)) {
/* 80 */           return true;
/*    */         }
/*    */       } 
/* 83 */       return false;
/*    */     } 
/* 85 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\Java8SslUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */