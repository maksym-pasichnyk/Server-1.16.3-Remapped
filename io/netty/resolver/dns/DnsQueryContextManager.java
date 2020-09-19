/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.util.NetUtil;
/*     */ import io.netty.util.collection.IntObjectHashMap;
/*     */ import io.netty.util.collection.IntObjectMap;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ final class DnsQueryContextManager
/*     */ {
/*  38 */   final Map<InetSocketAddress, IntObjectMap<DnsQueryContext>> map = new HashMap<InetSocketAddress, IntObjectMap<DnsQueryContext>>();
/*     */ 
/*     */   
/*     */   int add(DnsQueryContext qCtx) {
/*  42 */     IntObjectMap<DnsQueryContext> contexts = getOrCreateContextMap(qCtx.nameServerAddr());
/*     */     
/*  44 */     int id = PlatformDependent.threadLocalRandom().nextInt(65535) + 1;
/*  45 */     int maxTries = 131070;
/*  46 */     int tries = 0;
/*     */     
/*  48 */     synchronized (contexts) {
/*     */       while (true) {
/*  50 */         if (!contexts.containsKey(id)) {
/*  51 */           contexts.put(id, qCtx);
/*  52 */           return id;
/*     */         } 
/*     */         
/*  55 */         id = id + 1 & 0xFFFF;
/*     */         
/*  57 */         if (++tries >= 131070)
/*  58 */           throw new IllegalStateException("query ID space exhausted: " + qCtx.question()); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   DnsQueryContext get(InetSocketAddress nameServerAddr, int id) {
/*     */     DnsQueryContext qCtx;
/*  65 */     IntObjectMap<DnsQueryContext> contexts = getContextMap(nameServerAddr);
/*     */     
/*  67 */     if (contexts != null) {
/*  68 */       synchronized (contexts) {
/*  69 */         qCtx = (DnsQueryContext)contexts.get(id);
/*     */       } 
/*     */     } else {
/*  72 */       qCtx = null;
/*     */     } 
/*     */     
/*  75 */     return qCtx;
/*     */   }
/*     */   
/*     */   DnsQueryContext remove(InetSocketAddress nameServerAddr, int id) {
/*  79 */     IntObjectMap<DnsQueryContext> contexts = getContextMap(nameServerAddr);
/*  80 */     if (contexts == null) {
/*  81 */       return null;
/*     */     }
/*     */     
/*  84 */     synchronized (contexts) {
/*  85 */       return (DnsQueryContext)contexts.remove(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   private IntObjectMap<DnsQueryContext> getContextMap(InetSocketAddress nameServerAddr) {
/*  90 */     synchronized (this.map) {
/*  91 */       return this.map.get(nameServerAddr);
/*     */     } 
/*     */   }
/*     */   
/*     */   private IntObjectMap<DnsQueryContext> getOrCreateContextMap(InetSocketAddress nameServerAddr) {
/*  96 */     synchronized (this.map) {
/*  97 */       IntObjectMap<DnsQueryContext> contexts = this.map.get(nameServerAddr);
/*  98 */       if (contexts != null) {
/*  99 */         return contexts;
/*     */       }
/*     */       
/* 102 */       IntObjectHashMap intObjectHashMap = new IntObjectHashMap();
/* 103 */       InetAddress a = nameServerAddr.getAddress();
/* 104 */       int port = nameServerAddr.getPort();
/* 105 */       this.map.put(nameServerAddr, intObjectHashMap);
/*     */       
/* 107 */       if (a instanceof Inet4Address) {
/*     */         
/* 109 */         Inet4Address a4 = (Inet4Address)a;
/* 110 */         if (a4.isLoopbackAddress()) {
/* 111 */           this.map.put(new InetSocketAddress(NetUtil.LOCALHOST6, port), intObjectHashMap);
/*     */         } else {
/* 113 */           this.map.put(new InetSocketAddress(toCompactAddress(a4), port), intObjectHashMap);
/*     */         } 
/* 115 */       } else if (a instanceof Inet6Address) {
/*     */         
/* 117 */         Inet6Address a6 = (Inet6Address)a;
/* 118 */         if (a6.isLoopbackAddress()) {
/* 119 */           this.map.put(new InetSocketAddress(NetUtil.LOCALHOST4, port), intObjectHashMap);
/* 120 */         } else if (a6.isIPv4CompatibleAddress()) {
/* 121 */           this.map.put(new InetSocketAddress(toIPv4Address(a6), port), intObjectHashMap);
/*     */         } 
/*     */       } 
/*     */       
/* 125 */       return (IntObjectMap<DnsQueryContext>)intObjectHashMap;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Inet6Address toCompactAddress(Inet4Address a4) {
/* 130 */     byte[] b4 = a4.getAddress();
/* 131 */     byte[] b6 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, b4[0], b4[1], b4[2], b4[3] };
/*     */     try {
/* 133 */       return (Inet6Address)InetAddress.getByAddress(b6);
/* 134 */     } catch (UnknownHostException e) {
/* 135 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Inet4Address toIPv4Address(Inet6Address a6) {
/* 140 */     byte[] b6 = a6.getAddress();
/* 141 */     byte[] b4 = { b6[12], b6[13], b6[14], b6[15] };
/*     */     try {
/* 143 */       return (Inet4Address)InetAddress.getByAddress(b4);
/* 144 */     } catch (UnknownHostException e) {
/* 145 */       throw new Error(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DnsQueryContextManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */