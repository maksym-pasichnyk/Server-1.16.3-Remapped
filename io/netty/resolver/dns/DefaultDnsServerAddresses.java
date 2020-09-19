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
/*    */ abstract class DefaultDnsServerAddresses
/*    */   extends DnsServerAddresses
/*    */ {
/*    */   protected final InetSocketAddress[] addresses;
/*    */   private final String strVal;
/*    */   
/*    */   DefaultDnsServerAddresses(String type, InetSocketAddress[] addresses) {
/* 27 */     this.addresses = addresses;
/*    */     
/* 29 */     StringBuilder buf = new StringBuilder(type.length() + 2 + addresses.length * 16);
/* 30 */     buf.append(type).append('(');
/*    */     
/* 32 */     for (InetSocketAddress a : addresses) {
/* 33 */       buf.append(a).append(", ");
/*    */     }
/*    */     
/* 36 */     buf.setLength(buf.length() - 2);
/* 37 */     buf.append(')');
/*    */     
/* 39 */     this.strVal = buf.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 44 */     return this.strVal;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\resolver\dns\DefaultDnsServerAddresses.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */