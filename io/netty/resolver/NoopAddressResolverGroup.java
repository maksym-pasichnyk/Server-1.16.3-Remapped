/*    */ package io.netty.resolver;
/*    */ 
/*    */ import io.netty.util.concurrent.EventExecutor;
/*    */ import java.net.SocketAddress;
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
/*    */ public final class NoopAddressResolverGroup
/*    */   extends AddressResolverGroup<SocketAddress>
/*    */ {
/* 30 */   public static final NoopAddressResolverGroup INSTANCE = new NoopAddressResolverGroup();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected AddressResolver<SocketAddress> newResolver(EventExecutor executor) throws Exception {
/* 36 */     return new NoopAddressResolver(executor);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\NoopAddressResolverGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */