/*    */ package io.netty.resolver;
/*    */ 
/*    */ import io.netty.util.concurrent.EventExecutor;
/*    */ import io.netty.util.concurrent.Promise;
/*    */ import io.netty.util.internal.SocketUtils;
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import java.util.Arrays;
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
/*    */ public class DefaultNameResolver
/*    */   extends InetNameResolver
/*    */ {
/*    */   public DefaultNameResolver(EventExecutor executor) {
/* 37 */     super(executor);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doResolve(String inetHost, Promise<InetAddress> promise) throws Exception {
/*    */     try {
/* 43 */       promise.setSuccess(SocketUtils.addressByName(inetHost));
/* 44 */     } catch (UnknownHostException e) {
/* 45 */       promise.setFailure(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doResolveAll(String inetHost, Promise<List<InetAddress>> promise) throws Exception {
/*    */     try {
/* 52 */       promise.setSuccess(Arrays.asList(SocketUtils.allAddressesByName(inetHost)));
/* 53 */     } catch (UnknownHostException e) {
/* 54 */       promise.setFailure(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\DefaultNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */