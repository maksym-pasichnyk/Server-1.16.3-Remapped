/*    */ package io.netty.channel.rxtx;
/*    */ 
/*    */ import java.net.SocketAddress;
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
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class RxtxDeviceAddress
/*    */   extends SocketAddress
/*    */ {
/*    */   private static final long serialVersionUID = -2907820090993709523L;
/*    */   private final String value;
/*    */   
/*    */   public RxtxDeviceAddress(String value) {
/* 39 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String value() {
/* 46 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\rxtx\RxtxDeviceAddress.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */