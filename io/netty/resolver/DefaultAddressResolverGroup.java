/*    */ package io.netty.resolver;
/*    */ 
/*    */ import io.netty.util.concurrent.EventExecutor;
/*    */ import java.net.InetSocketAddress;
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
/*    */ public final class DefaultAddressResolverGroup
/*    */   extends AddressResolverGroup<InetSocketAddress>
/*    */ {
/* 30 */   public static final DefaultAddressResolverGroup INSTANCE = new DefaultAddressResolverGroup();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected AddressResolver<InetSocketAddress> newResolver(EventExecutor executor) throws Exception {
/* 36 */     return (new DefaultNameResolver(executor)).asAddressResolver();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\DefaultAddressResolverGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */