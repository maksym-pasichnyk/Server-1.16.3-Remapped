/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.handler.codec.dns.DnsQuestion;
/*     */ import io.netty.handler.codec.dns.DnsResponseCode;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BiDnsQueryLifecycleObserver
/*     */   implements DnsQueryLifecycleObserver
/*     */ {
/*     */   private final DnsQueryLifecycleObserver a;
/*     */   private final DnsQueryLifecycleObserver b;
/*     */   
/*     */   public BiDnsQueryLifecycleObserver(DnsQueryLifecycleObserver a, DnsQueryLifecycleObserver b) {
/*  42 */     this.a = (DnsQueryLifecycleObserver)ObjectUtil.checkNotNull(a, "a");
/*  43 */     this.b = (DnsQueryLifecycleObserver)ObjectUtil.checkNotNull(b, "b");
/*     */   }
/*     */ 
/*     */   
/*     */   public void queryWritten(InetSocketAddress dnsServerAddress, ChannelFuture future) {
/*     */     try {
/*  49 */       this.a.queryWritten(dnsServerAddress, future);
/*     */     } finally {
/*  51 */       this.b.queryWritten(dnsServerAddress, future);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void queryCancelled(int queriesRemaining) {
/*     */     try {
/*  58 */       this.a.queryCancelled(queriesRemaining);
/*     */     } finally {
/*  60 */       this.b.queryCancelled(queriesRemaining);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQueryLifecycleObserver queryRedirected(List<InetSocketAddress> nameServers) {
/*     */     try {
/*  67 */       this.a.queryRedirected(nameServers);
/*     */     } finally {
/*  69 */       this.b.queryRedirected(nameServers);
/*     */     } 
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQueryLifecycleObserver queryCNAMEd(DnsQuestion cnameQuestion) {
/*     */     try {
/*  77 */       this.a.queryCNAMEd(cnameQuestion);
/*     */     } finally {
/*  79 */       this.b.queryCNAMEd(cnameQuestion);
/*     */     } 
/*  81 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DnsQueryLifecycleObserver queryNoAnswer(DnsResponseCode code) {
/*     */     try {
/*  87 */       this.a.queryNoAnswer(code);
/*     */     } finally {
/*  89 */       this.b.queryNoAnswer(code);
/*     */     } 
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void queryFailed(Throwable cause) {
/*     */     try {
/*  97 */       this.a.queryFailed(cause);
/*     */     } finally {
/*  99 */       this.b.queryFailed(cause);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void querySucceed() {
/*     */     try {
/* 106 */       this.a.querySucceed();
/*     */     } finally {
/* 108 */       this.b.querySucceed();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\BiDnsQueryLifecycleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */