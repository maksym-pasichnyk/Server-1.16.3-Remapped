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
/*    */ final class SingletonDnsServerAddresses
/*    */   extends DnsServerAddresses
/*    */ {
/*    */   private final InetSocketAddress address;
/*    */   
/* 25 */   private final DnsServerAddressStream stream = new DnsServerAddressStream()
/*    */     {
/*    */       public InetSocketAddress next() {
/* 28 */         return SingletonDnsServerAddresses.this.address;
/*    */       }
/*    */ 
/*    */       
/*    */       public int size() {
/* 33 */         return 1;
/*    */       }
/*    */ 
/*    */       
/*    */       public DnsServerAddressStream duplicate() {
/* 38 */         return this;
/*    */       }
/*    */ 
/*    */       
/*    */       public String toString() {
/* 43 */         return SingletonDnsServerAddresses.this.toString();
/*    */       }
/*    */     };
/*    */   
/*    */   SingletonDnsServerAddresses(InetSocketAddress address) {
/* 48 */     this.address = address;
/*    */   }
/*    */ 
/*    */   
/*    */   public DnsServerAddressStream stream() {
/* 53 */     return this.stream;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     return "singleton(" + this.address + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\SingletonDnsServerAddresses.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */