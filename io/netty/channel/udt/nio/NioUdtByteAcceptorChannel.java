/*    */ package io.netty.channel.udt.nio;
/*    */ 
/*    */ import com.barchart.udt.TypeUDT;
/*    */ import com.barchart.udt.nio.SocketChannelUDT;
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.udt.UdtChannel;
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
/*    */ public class NioUdtByteAcceptorChannel
/*    */   extends NioUdtAcceptorChannel
/*    */ {
/*    */   public NioUdtByteAcceptorChannel() {
/* 31 */     super(TypeUDT.STREAM);
/*    */   }
/*    */ 
/*    */   
/*    */   protected UdtChannel newConnectorChannel(SocketChannelUDT channelUDT) {
/* 36 */     return new NioUdtByteConnectorChannel((Channel)this, channelUDT);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channe\\udt\nio\NioUdtByteAcceptorChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */