/*    */ package io.netty.handler.codec.sctp;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.sctp.SctpMessage;
/*    */ import io.netty.handler.codec.MessageToMessageDecoder;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ public class SctpMessageCompletionHandler
/*    */   extends MessageToMessageDecoder<SctpMessage>
/*    */ {
/* 36 */   private final Map<Integer, ByteBuf> fragments = new HashMap<Integer, ByteBuf>();
/*    */ 
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, SctpMessage msg, List<Object> out) throws Exception {
/* 40 */     ByteBuf byteBuf = msg.content();
/* 41 */     int protocolIdentifier = msg.protocolIdentifier();
/* 42 */     int streamIdentifier = msg.streamIdentifier();
/* 43 */     boolean isComplete = msg.isComplete();
/* 44 */     boolean isUnordered = msg.isUnordered();
/*    */     
/* 46 */     ByteBuf frag = this.fragments.remove(Integer.valueOf(streamIdentifier));
/* 47 */     if (frag == null) {
/* 48 */       frag = Unpooled.EMPTY_BUFFER;
/*    */     }
/*    */     
/* 51 */     if (isComplete && !frag.isReadable()) {
/*    */       
/* 53 */       out.add(msg);
/* 54 */     } else if (!isComplete && frag.isReadable()) {
/*    */       
/* 56 */       this.fragments.put(Integer.valueOf(streamIdentifier), Unpooled.wrappedBuffer(new ByteBuf[] { frag, byteBuf }));
/* 57 */     } else if (isComplete && frag.isReadable()) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 63 */       SctpMessage assembledMsg = new SctpMessage(protocolIdentifier, streamIdentifier, isUnordered, Unpooled.wrappedBuffer(new ByteBuf[] { frag, byteBuf }));
/* 64 */       out.add(assembledMsg);
/*    */     } else {
/*    */       
/* 67 */       this.fragments.put(Integer.valueOf(streamIdentifier), byteBuf);
/*    */     } 
/* 69 */     byteBuf.retain();
/*    */   }
/*    */ 
/*    */   
/*    */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
/* 74 */     for (ByteBuf buffer : this.fragments.values()) {
/* 75 */       buffer.release();
/*    */     }
/* 77 */     this.fragments.clear();
/* 78 */     super.handlerRemoved(ctx);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\sctp\SctpMessageCompletionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */