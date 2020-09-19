/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.util.internal.PlatformDependent;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.util.Random;
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
/*    */ final class ShuffledDnsServerAddressStream
/*    */   implements DnsServerAddressStream
/*    */ {
/*    */   private final InetSocketAddress[] addresses;
/*    */   private int i;
/*    */   
/*    */   ShuffledDnsServerAddressStream(InetSocketAddress[] addresses) {
/* 35 */     this.addresses = addresses;
/*    */     
/* 37 */     shuffle();
/*    */   }
/*    */   
/*    */   private ShuffledDnsServerAddressStream(InetSocketAddress[] addresses, int startIdx) {
/* 41 */     this.addresses = addresses;
/* 42 */     this.i = startIdx;
/*    */   }
/*    */   
/*    */   private void shuffle() {
/* 46 */     InetSocketAddress[] addresses = this.addresses;
/* 47 */     Random r = PlatformDependent.threadLocalRandom();
/*    */     
/* 49 */     for (int i = addresses.length - 1; i >= 0; i--) {
/* 50 */       InetSocketAddress tmp = addresses[i];
/* 51 */       int j = r.nextInt(i + 1);
/* 52 */       addresses[i] = addresses[j];
/* 53 */       addresses[j] = tmp;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public InetSocketAddress next() {
/* 59 */     int i = this.i;
/* 60 */     InetSocketAddress next = this.addresses[i];
/* 61 */     if (++i < this.addresses.length) {
/* 62 */       this.i = i;
/*    */     } else {
/* 64 */       this.i = 0;
/* 65 */       shuffle();
/*    */     } 
/* 67 */     return next;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 72 */     return this.addresses.length;
/*    */   }
/*    */ 
/*    */   
/*    */   public ShuffledDnsServerAddressStream duplicate() {
/* 77 */     return new ShuffledDnsServerAddressStream(this.addresses, this.i);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 82 */     return SequentialDnsServerAddressStream.toString("shuffled", this.i, this.addresses);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\ShuffledDnsServerAddressStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */