/*     */ package io.netty.resolver;
/*     */ 
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ 
/*     */ public class RoundRobinInetAddressResolver
/*     */   extends InetNameResolver
/*     */ {
/*     */   private final NameResolver<InetAddress> nameResolver;
/*     */   
/*     */   public RoundRobinInetAddressResolver(EventExecutor executor, NameResolver<InetAddress> nameResolver) {
/*  48 */     super(executor);
/*  49 */     this.nameResolver = nameResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doResolve(final String inetHost, final Promise<InetAddress> promise) throws Exception {
/*  57 */     this.nameResolver.resolveAll(inetHost).addListener((GenericFutureListener)new FutureListener<List<InetAddress>>()
/*     */         {
/*     */           public void operationComplete(Future<List<InetAddress>> future) throws Exception {
/*  60 */             if (future.isSuccess()) {
/*  61 */               List<InetAddress> inetAddresses = (List<InetAddress>)future.getNow();
/*  62 */               int numAddresses = inetAddresses.size();
/*  63 */               if (numAddresses > 0) {
/*     */ 
/*     */                 
/*  66 */                 promise.setSuccess(inetAddresses.get(RoundRobinInetAddressResolver.randomIndex(numAddresses)));
/*     */               } else {
/*  68 */                 promise.setFailure(new UnknownHostException(inetHost));
/*     */               } 
/*     */             } else {
/*  71 */               promise.setFailure(future.cause());
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doResolveAll(String inetHost, final Promise<List<InetAddress>> promise) throws Exception {
/*  79 */     this.nameResolver.resolveAll(inetHost).addListener((GenericFutureListener)new FutureListener<List<InetAddress>>()
/*     */         {
/*     */           public void operationComplete(Future<List<InetAddress>> future) throws Exception {
/*  82 */             if (future.isSuccess()) {
/*  83 */               List<InetAddress> inetAddresses = (List<InetAddress>)future.getNow();
/*  84 */               if (!inetAddresses.isEmpty()) {
/*     */                 
/*  86 */                 List<InetAddress> result = new ArrayList<InetAddress>(inetAddresses);
/*     */                 
/*  88 */                 Collections.rotate(result, RoundRobinInetAddressResolver.randomIndex(inetAddresses.size()));
/*  89 */                 promise.setSuccess(result);
/*     */               } else {
/*  91 */                 promise.setSuccess(inetAddresses);
/*     */               } 
/*     */             } else {
/*  94 */               promise.setFailure(future.cause());
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private static int randomIndex(int numAddresses) {
/* 101 */     return (numAddresses == 1) ? 0 : PlatformDependent.threadLocalRandom().nextInt(numAddresses);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\RoundRobinInetAddressResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */