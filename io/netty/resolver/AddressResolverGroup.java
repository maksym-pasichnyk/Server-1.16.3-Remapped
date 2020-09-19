/*     */ package io.netty.resolver;
/*     */ 
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.Closeable;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.IdentityHashMap;
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
/*     */ public abstract class AddressResolverGroup<T extends SocketAddress>
/*     */   implements Closeable
/*     */ {
/*  38 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AddressResolverGroup.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   private final Map<EventExecutor, AddressResolver<T>> resolvers = new IdentityHashMap<EventExecutor, AddressResolver<T>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AddressResolver<T> getResolver(final EventExecutor executor) {
/*     */     AddressResolver<T> r;
/*  55 */     if (executor == null) {
/*  56 */       throw new NullPointerException("executor");
/*     */     }
/*     */     
/*  59 */     if (executor.isShuttingDown()) {
/*  60 */       throw new IllegalStateException("executor not accepting a task");
/*     */     }
/*     */ 
/*     */     
/*  64 */     synchronized (this.resolvers) {
/*  65 */       r = this.resolvers.get(executor);
/*  66 */       if (r == null) {
/*     */         final AddressResolver<T> newResolver;
/*     */         try {
/*  69 */           newResolver = newResolver(executor);
/*  70 */         } catch (Exception e) {
/*  71 */           throw new IllegalStateException("failed to create a new resolver", e);
/*     */         } 
/*     */         
/*  74 */         this.resolvers.put(executor, newResolver);
/*  75 */         executor.terminationFuture().addListener((GenericFutureListener)new FutureListener<Object>()
/*     */             {
/*     */               public void operationComplete(Future<Object> future) throws Exception {
/*  78 */                 synchronized (AddressResolverGroup.this.resolvers) {
/*  79 */                   AddressResolverGroup.this.resolvers.remove(executor);
/*     */                 } 
/*  81 */                 newResolver.close();
/*     */               }
/*     */             });
/*     */         
/*  85 */         r = newResolver;
/*     */       } 
/*     */     } 
/*     */     
/*  89 */     return r;
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
/*     */   public void close() {
/*     */     AddressResolver[] arrayOfAddressResolver;
/* 104 */     synchronized (this.resolvers) {
/* 105 */       arrayOfAddressResolver = (AddressResolver[])this.resolvers.values().toArray((Object[])new AddressResolver[this.resolvers.size()]);
/* 106 */       this.resolvers.clear();
/*     */     } 
/*     */     
/* 109 */     for (AddressResolver<T> r : arrayOfAddressResolver) {
/*     */       try {
/* 111 */         r.close();
/* 112 */       } catch (Throwable t) {
/* 113 */         logger.warn("Failed to close a resolver:", t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract AddressResolver<T> newResolver(EventExecutor paramEventExecutor) throws Exception;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\AddressResolverGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */