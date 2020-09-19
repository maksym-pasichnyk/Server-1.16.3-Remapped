/*    */ package io.netty.channel.udt.nio;
/*    */ 
/*    */ import com.barchart.udt.TypeUDT;
/*    */ import com.barchart.udt.nio.SocketChannelUDT;
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
/*    */ public class NioUdtMessageRendezvousChannel
/*    */   extends NioUdtMessageConnectorChannel
/*    */ {
/*    */   public NioUdtMessageRendezvousChannel() {
/* 32 */     super((SocketChannelUDT)NioUdtProvider.newRendezvousChannelUDT(TypeUDT.DATAGRAM));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\udt\nio\NioUdtMessageRendezvousChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */