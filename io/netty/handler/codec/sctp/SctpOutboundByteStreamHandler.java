/*    */ package io.netty.handler.codec.sctp;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.sctp.SctpMessage;
/*    */ import io.netty.handler.codec.MessageToMessageEncoder;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SctpOutboundByteStreamHandler
/*    */   extends MessageToMessageEncoder<ByteBuf>
/*    */ {
/*    */   private final int streamIdentifier;
/*    */   private final int protocolIdentifier;
/*    */   private final boolean unordered;
/*    */   
/*    */   public SctpOutboundByteStreamHandler(int streamIdentifier, int protocolIdentifier) {
/* 40 */     this(streamIdentifier, protocolIdentifier, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SctpOutboundByteStreamHandler(int streamIdentifier, int protocolIdentifier, boolean unordered) {
/* 49 */     this.streamIdentifier = streamIdentifier;
/* 50 */     this.protocolIdentifier = protocolIdentifier;
/* 51 */     this.unordered = unordered;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
/* 56 */     out.add(new SctpMessage(this.protocolIdentifier, this.streamIdentifier, this.unordered, msg.retain()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\sctp\SctpOutboundByteStreamHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */