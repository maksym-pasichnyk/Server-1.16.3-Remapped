/*    */ package io.netty.handler.codec.protobuf;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
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
/*    */ public class ProtobufVarint32LengthFieldPrepender
/*    */   extends MessageToByteEncoder<ByteBuf>
/*    */ {
/*    */   protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
/* 46 */     int bodyLen = msg.readableBytes();
/* 47 */     int headerLen = computeRawVarint32Size(bodyLen);
/* 48 */     out.ensureWritable(headerLen + bodyLen);
/* 49 */     writeRawVarint32(out, bodyLen);
/* 50 */     out.writeBytes(msg, msg.readerIndex(), bodyLen);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void writeRawVarint32(ByteBuf out, int value) {
/*    */     while (true) {
/* 60 */       if ((value & 0xFFFFFF80) == 0) {
/* 61 */         out.writeByte(value);
/*    */         return;
/*    */       } 
/* 64 */       out.writeByte(value & 0x7F | 0x80);
/* 65 */       value >>>= 7;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static int computeRawVarint32Size(int value) {
/* 76 */     if ((value & 0xFFFFFF80) == 0) {
/* 77 */       return 1;
/*    */     }
/* 79 */     if ((value & 0xFFFFC000) == 0) {
/* 80 */       return 2;
/*    */     }
/* 82 */     if ((value & 0xFFE00000) == 0) {
/* 83 */       return 3;
/*    */     }
/* 85 */     if ((value & 0xF0000000) == 0) {
/* 86 */       return 4;
/*    */     }
/* 88 */     return 5;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\protobuf\ProtobufVarint32LengthFieldPrepender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */