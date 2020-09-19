/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import java.net.InetSocketAddress;
/*    */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
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
/*    */ final class RotationalDnsServerAddresses
/*    */   extends DefaultDnsServerAddresses
/*    */ {
/* 25 */   private static final AtomicIntegerFieldUpdater<RotationalDnsServerAddresses> startIdxUpdater = AtomicIntegerFieldUpdater.newUpdater(RotationalDnsServerAddresses.class, "startIdx");
/*    */   
/*    */   private volatile int startIdx;
/*    */ 
/*    */   
/*    */   RotationalDnsServerAddresses(InetSocketAddress[] addresses) {
/* 31 */     super("rotational", addresses);
/*    */   }
/*    */ 
/*    */   
/*    */   public DnsServerAddressStream stream() {
/*    */     while (true) {
/* 37 */       int curStartIdx = this.startIdx;
/* 38 */       int nextStartIdx = curStartIdx + 1;
/* 39 */       if (nextStartIdx >= this.addresses.length) {
/* 40 */         nextStartIdx = 0;
/*    */       }
/* 42 */       if (startIdxUpdater.compareAndSet(this, curStartIdx, nextStartIdx))
/* 43 */         return new SequentialDnsServerAddressStream(this.addresses, curStartIdx); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\RotationalDnsServerAddresses.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */