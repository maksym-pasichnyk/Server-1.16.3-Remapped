/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.channel.ChannelFuture;
/*    */ import io.netty.handler.codec.dns.DnsQuestion;
/*    */ import io.netty.handler.codec.dns.DnsResponseCode;
/*    */ import java.net.InetSocketAddress;
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
/*    */ final class NoopDnsQueryLifecycleObserver
/*    */   implements DnsQueryLifecycleObserver
/*    */ {
/* 26 */   static final NoopDnsQueryLifecycleObserver INSTANCE = new NoopDnsQueryLifecycleObserver();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void queryWritten(InetSocketAddress dnsServerAddress, ChannelFuture future) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void queryCancelled(int queriesRemaining) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public DnsQueryLifecycleObserver queryRedirected(List<InetSocketAddress> nameServers) {
/* 41 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public DnsQueryLifecycleObserver queryCNAMEd(DnsQuestion cnameQuestion) {
/* 46 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public DnsQueryLifecycleObserver queryNoAnswer(DnsResponseCode code) {
/* 51 */     return this;
/*    */   }
/*    */   
/*    */   public void queryFailed(Throwable cause) {}
/*    */   
/*    */   public void querySucceed() {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\NoopDnsQueryLifecycleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */