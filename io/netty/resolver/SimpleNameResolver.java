/*    */ package io.netty.resolver;
/*    */ 
/*    */ import io.netty.util.concurrent.EventExecutor;
/*    */ import io.netty.util.concurrent.Future;
/*    */ import io.netty.util.concurrent.Promise;
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class SimpleNameResolver<T>
/*    */   implements NameResolver<T>
/*    */ {
/*    */   private final EventExecutor executor;
/*    */   
/*    */   protected SimpleNameResolver(EventExecutor executor) {
/* 41 */     this.executor = (EventExecutor)ObjectUtil.checkNotNull(executor, "executor");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected EventExecutor executor() {
/* 49 */     return this.executor;
/*    */   }
/*    */ 
/*    */   
/*    */   public final Future<T> resolve(String inetHost) {
/* 54 */     Promise<T> promise = executor().newPromise();
/* 55 */     return resolve(inetHost, promise);
/*    */   }
/*    */ 
/*    */   
/*    */   public Future<T> resolve(String inetHost, Promise<T> promise) {
/* 60 */     ObjectUtil.checkNotNull(promise, "promise");
/*    */     
/*    */     try {
/* 63 */       doResolve(inetHost, promise);
/* 64 */       return (Future<T>)promise;
/* 65 */     } catch (Exception e) {
/* 66 */       return (Future<T>)promise.setFailure(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public final Future<List<T>> resolveAll(String inetHost) {
/* 72 */     Promise<List<T>> promise = executor().newPromise();
/* 73 */     return resolveAll(inetHost, promise);
/*    */   }
/*    */ 
/*    */   
/*    */   public Future<List<T>> resolveAll(String inetHost, Promise<List<T>> promise) {
/* 78 */     ObjectUtil.checkNotNull(promise, "promise");
/*    */     
/*    */     try {
/* 81 */       doResolveAll(inetHost, promise);
/* 82 */       return (Future<List<T>>)promise;
/* 83 */     } catch (Exception e) {
/* 84 */       return (Future<List<T>>)promise.setFailure(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected abstract void doResolve(String paramString, Promise<T> paramPromise) throws Exception;
/*    */   
/*    */   protected abstract void doResolveAll(String paramString, Promise<List<T>> paramPromise) throws Exception;
/*    */   
/*    */   public void close() {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\SimpleNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */