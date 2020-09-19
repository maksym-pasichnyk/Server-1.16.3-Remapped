/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.internal.tcnative.SSL;
/*     */ import io.netty.internal.tcnative.SSLContext;
/*     */ import io.netty.internal.tcnative.SessionTicketKey;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSessionContext;
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
/*     */ public abstract class OpenSslSessionContext
/*     */   implements SSLSessionContext
/*     */ {
/*  34 */   private static final Enumeration<byte[]> EMPTY = new EmptyEnumeration();
/*     */ 
/*     */   
/*     */   private final OpenSslSessionStats stats;
/*     */ 
/*     */   
/*     */   final ReferenceCountedOpenSslContext context;
/*     */ 
/*     */   
/*     */   OpenSslSessionContext(ReferenceCountedOpenSslContext context) {
/*  44 */     this.context = context;
/*  45 */     this.stats = new OpenSslSessionStats(context);
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSession getSession(byte[] bytes) {
/*  50 */     if (bytes == null) {
/*  51 */       throw new NullPointerException("bytes");
/*     */     }
/*  53 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration<byte[]> getIds() {
/*  58 */     return EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setTicketKeys(byte[] keys) {
/*  67 */     if (keys.length % 48 != 0) {
/*  68 */       throw new IllegalArgumentException("keys.length % 48 != 0");
/*     */     }
/*  70 */     SessionTicketKey[] tickets = new SessionTicketKey[keys.length / 48];
/*  71 */     for (int i = 0, a = 0; i < tickets.length; i++) {
/*  72 */       byte[] name = Arrays.copyOfRange(keys, a, 16);
/*  73 */       a += 16;
/*  74 */       byte[] hmacKey = Arrays.copyOfRange(keys, a, 16);
/*  75 */       i += 16;
/*  76 */       byte[] aesKey = Arrays.copyOfRange(keys, a, 16);
/*  77 */       a += 16;
/*  78 */       tickets[i] = new SessionTicketKey(name, hmacKey, aesKey);
/*     */     } 
/*  80 */     Lock writerLock = this.context.ctxLock.writeLock();
/*  81 */     writerLock.lock();
/*     */     try {
/*  83 */       SSLContext.clearOptions(this.context.ctx, SSL.SSL_OP_NO_TICKET);
/*  84 */       SSLContext.setSessionTicketKeys(this.context.ctx, tickets);
/*     */     } finally {
/*  86 */       writerLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTicketKeys(OpenSslSessionTicketKey... keys) {
/*  94 */     ObjectUtil.checkNotNull(keys, "keys");
/*  95 */     SessionTicketKey[] ticketKeys = new SessionTicketKey[keys.length];
/*  96 */     for (int i = 0; i < ticketKeys.length; i++) {
/*  97 */       ticketKeys[i] = (keys[i]).key;
/*     */     }
/*  99 */     Lock writerLock = this.context.ctxLock.writeLock();
/* 100 */     writerLock.lock();
/*     */     try {
/* 102 */       SSLContext.clearOptions(this.context.ctx, SSL.SSL_OP_NO_TICKET);
/* 103 */       SSLContext.setSessionTicketKeys(this.context.ctx, ticketKeys);
/*     */     } finally {
/* 105 */       writerLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setSessionCacheEnabled(boolean paramBoolean);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isSessionCacheEnabled();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OpenSslSessionStats stats() {
/* 123 */     return this.stats;
/*     */   }
/*     */   
/*     */   private static final class EmptyEnumeration
/*     */     implements Enumeration<byte[]> {
/*     */     public boolean hasMoreElements() {
/* 129 */       return false;
/*     */     }
/*     */     private EmptyEnumeration() {}
/*     */     
/*     */     public byte[] nextElement() {
/* 134 */       throw new NoSuchElementException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\OpenSslSessionContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */