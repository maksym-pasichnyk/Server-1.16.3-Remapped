/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.handler.codec.dns.DnsQuestion;
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ public final class BiDnsQueryLifecycleObserverFactory
/*    */   implements DnsQueryLifecycleObserverFactory
/*    */ {
/*    */   private final DnsQueryLifecycleObserverFactory a;
/*    */   private final DnsQueryLifecycleObserverFactory b;
/*    */   
/*    */   public BiDnsQueryLifecycleObserverFactory(DnsQueryLifecycleObserverFactory a, DnsQueryLifecycleObserverFactory b) {
/* 37 */     this.a = (DnsQueryLifecycleObserverFactory)ObjectUtil.checkNotNull(a, "a");
/* 38 */     this.b = (DnsQueryLifecycleObserverFactory)ObjectUtil.checkNotNull(b, "b");
/*    */   }
/*    */ 
/*    */   
/*    */   public DnsQueryLifecycleObserver newDnsQueryLifecycleObserver(DnsQuestion question) {
/* 43 */     return new BiDnsQueryLifecycleObserver(this.a.newDnsQueryLifecycleObserver(question), this.b
/* 44 */         .newDnsQueryLifecycleObserver(question));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\BiDnsQueryLifecycleObserverFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */