/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.handler.codec.dns.DnsQuestion;
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
/*    */ public final class NoopDnsQueryLifecycleObserverFactory
/*    */   implements DnsQueryLifecycleObserverFactory
/*    */ {
/* 23 */   public static final NoopDnsQueryLifecycleObserverFactory INSTANCE = new NoopDnsQueryLifecycleObserverFactory();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DnsQueryLifecycleObserver newDnsQueryLifecycleObserver(DnsQuestion question) {
/* 30 */     return NoopDnsQueryLifecycleObserver.INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\NoopDnsQueryLifecycleObserverFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */