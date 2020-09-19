/*    */ package io.netty.resolver.dns;
/*    */ 
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
/*    */ final class SequentialDnsServerAddressStream
/*    */   implements DnsServerAddressStream
/*    */ {
/*    */   private final InetSocketAddress[] addresses;
/*    */   private int i;
/*    */   
/*    */   SequentialDnsServerAddressStream(InetSocketAddress[] addresses, int startIdx) {
/* 27 */     this.addresses = addresses;
/* 28 */     this.i = startIdx;
/*    */   }
/*    */ 
/*    */   
/*    */   public InetSocketAddress next() {
/* 33 */     int i = this.i;
/* 34 */     InetSocketAddress next = this.addresses[i];
/* 35 */     if (++i < this.addresses.length) {
/* 36 */       this.i = i;
/*    */     } else {
/* 38 */       this.i = 0;
/*    */     } 
/* 40 */     return next;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 45 */     return this.addresses.length;
/*    */   }
/*    */ 
/*    */   
/*    */   public SequentialDnsServerAddressStream duplicate() {
/* 50 */     return new SequentialDnsServerAddressStream(this.addresses, this.i);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return toString("sequential", this.i, this.addresses);
/*    */   }
/*    */   
/*    */   static String toString(String type, int index, InetSocketAddress[] addresses) {
/* 59 */     StringBuilder buf = new StringBuilder(type.length() + 2 + addresses.length * 16);
/* 60 */     buf.append(type).append("(index: ").append(index);
/* 61 */     buf.append(", addrs: (");
/* 62 */     for (InetSocketAddress a : addresses) {
/* 63 */       buf.append(a).append(", ");
/*    */     }
/*    */     
/* 66 */     buf.setLength(buf.length() - 2);
/* 67 */     buf.append("))");
/*    */     
/* 69 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\SequentialDnsServerAddressStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */