/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class WebSocketUtil
/*     */ {
/*  32 */   private static final FastThreadLocal<MessageDigest> MD5 = new FastThreadLocal<MessageDigest>()
/*     */     {
/*     */       protected MessageDigest initialValue() throws Exception
/*     */       {
/*     */         try {
/*  37 */           return MessageDigest.getInstance("MD5");
/*  38 */         } catch (NoSuchAlgorithmException e) {
/*     */           
/*  40 */           throw new InternalError("MD5 not supported on this platform - Outdated?");
/*     */         } 
/*     */       }
/*     */     };
/*     */   
/*  45 */   private static final FastThreadLocal<MessageDigest> SHA1 = new FastThreadLocal<MessageDigest>()
/*     */     {
/*     */       protected MessageDigest initialValue() throws Exception
/*     */       {
/*     */         try {
/*  50 */           return MessageDigest.getInstance("SHA1");
/*  51 */         } catch (NoSuchAlgorithmException e) {
/*     */           
/*  53 */           throw new InternalError("SHA-1 not supported on this platform - Outdated?");
/*     */         } 
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] md5(byte[] data) {
/*  66 */     return digest(MD5, data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] sha1(byte[] data) {
/*  77 */     return digest(SHA1, data);
/*     */   }
/*     */   
/*     */   private static byte[] digest(FastThreadLocal<MessageDigest> digestFastThreadLocal, byte[] data) {
/*  81 */     MessageDigest digest = (MessageDigest)digestFastThreadLocal.get();
/*  82 */     digest.reset();
/*  83 */     return digest.digest(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String base64(byte[] data) {
/*  93 */     ByteBuf encodedData = Unpooled.wrappedBuffer(data);
/*  94 */     ByteBuf encoded = Base64.encode(encodedData);
/*  95 */     String encodedString = encoded.toString(CharsetUtil.UTF_8);
/*  96 */     encoded.release();
/*  97 */     return encodedString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] randomBytes(int size) {
/* 107 */     byte[] bytes = new byte[size];
/*     */     
/* 109 */     for (int index = 0; index < size; index++) {
/* 110 */       bytes[index] = (byte)randomNumber(0, 255);
/*     */     }
/*     */     
/* 113 */     return bytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int randomNumber(int minimum, int maximum) {
/* 124 */     return (int)(Math.random() * maximum + minimum);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\WebSocketUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */