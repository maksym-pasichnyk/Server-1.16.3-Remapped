/*     */ package com.mojang.authlib;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.io.Charsets;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public abstract class HttpAuthenticationService
/*     */   extends BaseAuthenticationService {
/*  21 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   private final Proxy proxy;
/*     */   
/*     */   protected HttpAuthenticationService(Proxy proxy) {
/*  26 */     Validate.notNull(proxy);
/*  27 */     this.proxy = proxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Proxy getProxy() {
/*  36 */     return this.proxy;
/*     */   }
/*     */   
/*     */   protected HttpURLConnection createUrlConnection(URL url) throws IOException {
/*  40 */     Validate.notNull(url);
/*  41 */     LOGGER.debug("Opening connection to " + url);
/*  42 */     HttpURLConnection connection = (HttpURLConnection)url.openConnection(this.proxy);
/*  43 */     connection.setConnectTimeout(15000);
/*  44 */     connection.setReadTimeout(15000);
/*  45 */     connection.setUseCaches(false);
/*  46 */     return connection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String performPostRequest(URL url, String post, String contentType) throws IOException {
/*  63 */     Validate.notNull(url);
/*  64 */     Validate.notNull(post);
/*  65 */     Validate.notNull(contentType);
/*  66 */     HttpURLConnection connection = createUrlConnection(url);
/*  67 */     byte[] postAsBytes = post.getBytes(Charsets.UTF_8);
/*     */     
/*  69 */     connection.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
/*  70 */     connection.setRequestProperty("Content-Length", "" + postAsBytes.length);
/*  71 */     connection.setDoOutput(true);
/*     */     
/*  73 */     LOGGER.debug("Writing POST data to " + url + ": " + post);
/*     */     
/*  75 */     OutputStream outputStream = null;
/*     */     try {
/*  77 */       outputStream = connection.getOutputStream();
/*  78 */       IOUtils.write(postAsBytes, outputStream);
/*     */     } finally {
/*  80 */       IOUtils.closeQuietly(outputStream);
/*     */     } 
/*     */     
/*  83 */     LOGGER.debug("Reading data from " + url);
/*     */     
/*  85 */     InputStream inputStream = null;
/*     */     try {
/*  87 */       inputStream = connection.getInputStream();
/*  88 */       String result = IOUtils.toString(inputStream, Charsets.UTF_8);
/*  89 */       LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
/*  90 */       LOGGER.debug("Response: " + result);
/*  91 */       return result;
/*  92 */     } catch (IOException e) {
/*  93 */       IOUtils.closeQuietly(inputStream);
/*  94 */       inputStream = connection.getErrorStream();
/*     */       
/*  96 */       if (inputStream != null) {
/*  97 */         LOGGER.debug("Reading error page from " + url);
/*  98 */         String result = IOUtils.toString(inputStream, Charsets.UTF_8);
/*  99 */         LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
/* 100 */         LOGGER.debug("Response: " + result);
/* 101 */         return result;
/*     */       } 
/* 103 */       LOGGER.debug("Request failed", e);
/* 104 */       throw e;
/*     */     } finally {
/*     */       
/* 107 */       IOUtils.closeQuietly(inputStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String performGetRequest(URL url) throws IOException {
/* 123 */     Validate.notNull(url);
/* 124 */     HttpURLConnection connection = createUrlConnection(url);
/*     */     
/* 126 */     LOGGER.debug("Reading data from " + url);
/*     */     
/* 128 */     InputStream inputStream = null;
/*     */     try {
/* 130 */       inputStream = connection.getInputStream();
/* 131 */       String result = IOUtils.toString(inputStream, Charsets.UTF_8);
/* 132 */       LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
/* 133 */       LOGGER.debug("Response: " + result);
/* 134 */       return result;
/* 135 */     } catch (IOException e) {
/* 136 */       IOUtils.closeQuietly(inputStream);
/* 137 */       inputStream = connection.getErrorStream();
/*     */       
/* 139 */       if (inputStream != null) {
/* 140 */         LOGGER.debug("Reading error page from " + url);
/* 141 */         String result = IOUtils.toString(inputStream, Charsets.UTF_8);
/* 142 */         LOGGER.debug("Successful read, server response was " + connection.getResponseCode());
/* 143 */         LOGGER.debug("Response: " + result);
/* 144 */         return result;
/*     */       } 
/* 146 */       LOGGER.debug("Request failed", e);
/* 147 */       throw e;
/*     */     } finally {
/*     */       
/* 150 */       IOUtils.closeQuietly(inputStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL constantURL(String url) {
/*     */     try {
/* 164 */       return new URL(url);
/* 165 */     } catch (MalformedURLException ex) {
/* 166 */       throw new Error("Couldn't create constant for " + url, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String buildQuery(Map<String, Object> query) {
/* 177 */     if (query == null) {
/* 178 */       return "";
/*     */     }
/* 180 */     StringBuilder builder = new StringBuilder();
/*     */     
/* 182 */     for (Map.Entry<String, Object> entry : query.entrySet()) {
/* 183 */       if (builder.length() > 0) {
/* 184 */         builder.append('&');
/*     */       }
/*     */       
/*     */       try {
/* 188 */         builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
/* 189 */       } catch (UnsupportedEncodingException e) {
/* 190 */         LOGGER.error("Unexpected exception building query", e);
/*     */       } 
/*     */       
/* 193 */       if (entry.getValue() != null) {
/* 194 */         builder.append('=');
/*     */         try {
/* 196 */           builder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
/* 197 */         } catch (UnsupportedEncodingException e) {
/* 198 */           LOGGER.error("Unexpected exception building query", e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 203 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL concatenateURL(URL url, String query) {
/*     */     try {
/* 215 */       if (url.getQuery() != null && url.getQuery().length() > 0) {
/* 216 */         return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + "&" + query);
/*     */       }
/* 218 */       return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + "?" + query);
/*     */     }
/* 220 */     catch (MalformedURLException ex) {
/* 221 */       throw new IllegalArgumentException("Could not concatenate given URL with GET arguments!", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\HttpAuthenticationService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */