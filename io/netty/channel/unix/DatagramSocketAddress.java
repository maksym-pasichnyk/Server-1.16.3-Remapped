/*    */ package io.netty.channel.unix;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DatagramSocketAddress
/*    */   extends InetSocketAddress
/*    */ {
/*    */   private static final long serialVersionUID = 3094819287843178401L;
/*    */   private final int receivedAmount;
/*    */   private final DatagramSocketAddress localAddress;
/*    */   
/*    */   DatagramSocketAddress(String addr, int port, int receivedAmount, DatagramSocketAddress local) {
/* 34 */     super(addr, port);
/* 35 */     this.receivedAmount = receivedAmount;
/* 36 */     this.localAddress = local;
/*    */   }
/*    */   
/*    */   public DatagramSocketAddress localAddress() {
/* 40 */     return this.localAddress;
/*    */   }
/*    */   
/*    */   public int receivedAmount() {
/* 44 */     return this.receivedAmount;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\unix\DatagramSocketAddress.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */