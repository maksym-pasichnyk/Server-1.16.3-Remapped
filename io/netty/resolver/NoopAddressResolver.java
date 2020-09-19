/*    */ package io.netty.resolver;
/*    */ 
/*    */ import io.netty.util.concurrent.EventExecutor;
/*    */ import io.netty.util.concurrent.Promise;
/*    */ import java.net.SocketAddress;
/*    */ import java.util.Collections;
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
/*    */ public class NoopAddressResolver
/*    */   extends AbstractAddressResolver<SocketAddress>
/*    */ {
/*    */   public NoopAddressResolver(EventExecutor executor) {
/* 35 */     super(executor);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean doIsResolved(SocketAddress address) {
/* 40 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doResolve(SocketAddress unresolvedAddress, Promise<SocketAddress> promise) throws Exception {
/* 45 */     promise.setSuccess(unresolvedAddress);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doResolveAll(SocketAddress unresolvedAddress, Promise<List<SocketAddress>> promise) throws Exception {
/* 51 */     promise.setSuccess(Collections.singletonList(unresolvedAddress));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\NoopAddressResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */