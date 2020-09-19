/*    */ package io.netty.resolver;
/*    */ 
/*    */ import io.netty.util.concurrent.EventExecutor;
/*    */ import java.net.InetAddress;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class InetNameResolver
/*    */   extends SimpleNameResolver<InetAddress>
/*    */ {
/*    */   private volatile AddressResolver<InetSocketAddress> addressResolver;
/*    */   
/*    */   protected InetNameResolver(EventExecutor executor) {
/* 37 */     super(executor);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AddressResolver<InetSocketAddress> asAddressResolver() {
/* 45 */     AddressResolver<InetSocketAddress> result = this.addressResolver;
/* 46 */     if (result == null) {
/* 47 */       synchronized (this) {
/* 48 */         result = this.addressResolver;
/* 49 */         if (result == null) {
/* 50 */           this.addressResolver = result = new InetSocketAddressResolver(executor(), this);
/*    */         }
/*    */       } 
/*    */     }
/* 54 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\InetNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */