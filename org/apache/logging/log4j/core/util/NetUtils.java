/*    */ package org.apache.logging.log4j.core.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.net.InetAddress;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.NetworkInterface;
/*    */ import java.net.SocketException;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.net.URL;
/*    */ import java.net.UnknownHostException;
/*    */ import java.util.Enumeration;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class NetUtils
/*    */ {
/* 38 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final String UNKNOWN_LOCALHOST = "UNKNOWN_LOCALHOST";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getLocalHostname() {
/*    */     try {
/* 53 */       InetAddress addr = InetAddress.getLocalHost();
/* 54 */       return addr.getHostName();
/* 55 */     } catch (UnknownHostException uhe) {
/*    */       try {
/* 57 */         Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
/* 58 */         while (interfaces.hasMoreElements()) {
/* 59 */           NetworkInterface nic = interfaces.nextElement();
/* 60 */           Enumeration<InetAddress> addresses = nic.getInetAddresses();
/* 61 */           while (addresses.hasMoreElements()) {
/* 62 */             InetAddress address = addresses.nextElement();
/* 63 */             if (!address.isLoopbackAddress()) {
/* 64 */               String hostname = address.getHostName();
/* 65 */               if (hostname != null) {
/* 66 */                 return hostname;
/*    */               }
/*    */             } 
/*    */           } 
/*    */         } 
/* 71 */       } catch (SocketException se) {
/* 72 */         LOGGER.error("Could not determine local host name", uhe);
/* 73 */         return "UNKNOWN_LOCALHOST";
/*    */       } 
/* 75 */       LOGGER.error("Could not determine local host name", uhe);
/* 76 */       return "UNKNOWN_LOCALHOST";
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static URI toURI(String path) {
/*    */     try {
/* 89 */       return new URI(path);
/* 90 */     } catch (URISyntaxException e) {
/*    */ 
/*    */       
/*    */       try {
/* 94 */         URL url = new URL(path);
/* 95 */         return new URI(url.getProtocol(), url.getHost(), url.getPath(), null);
/* 96 */       } catch (MalformedURLException|URISyntaxException nestedEx) {
/* 97 */         return (new File(path)).toURI();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\NetUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */