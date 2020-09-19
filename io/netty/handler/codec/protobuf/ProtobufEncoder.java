/*    */ package io.netty.handler.codec.protobuf;
/*    */ 
/*    */ import com.google.protobuf.MessageLite;
/*    */ import com.google.protobuf.MessageLiteOrBuilder;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
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
/*    */ @Sharable
/*    */ public class ProtobufEncoder
/*    */   extends MessageToMessageEncoder<MessageLiteOrBuilder>
/*    */ {
/*    */   protected void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg, List<Object> out) throws Exception {
/* 66 */     if (msg instanceof MessageLite) {
/* 67 */       out.add(Unpooled.wrappedBuffer(((MessageLite)msg).toByteArray()));
/*    */       return;
/*    */     } 
/* 70 */     if (msg instanceof MessageLite.Builder)
/* 71 */       out.add(Unpooled.wrappedBuffer(((MessageLite.Builder)msg).build().toByteArray())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\protobuf\ProtobufEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */