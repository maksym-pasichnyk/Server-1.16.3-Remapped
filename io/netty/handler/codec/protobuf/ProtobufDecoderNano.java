/*    */ package io.netty.handler.codec.protobuf;
/*    */ 
/*    */ import com.google.protobuf.nano.MessageNano;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageDecoder;
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ public class ProtobufDecoderNano
/*    */   extends MessageToMessageDecoder<ByteBuf>
/*    */ {
/*    */   private final Class<? extends MessageNano> clazz;
/*    */   
/*    */   public ProtobufDecoderNano(Class<? extends MessageNano> clazz) {
/* 68 */     this.clazz = (Class<? extends MessageNano>)ObjectUtil.checkNotNull(clazz, "You must provide a Class");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
/*    */     byte[] array;
/* 76 */     int offset, length = msg.readableBytes();
/* 77 */     if (msg.hasArray()) {
/* 78 */       array = msg.array();
/* 79 */       offset = msg.arrayOffset() + msg.readerIndex();
/*    */     } else {
/* 81 */       array = new byte[length];
/* 82 */       msg.getBytes(msg.readerIndex(), array, 0, length);
/* 83 */       offset = 0;
/*    */     } 
/* 85 */     MessageNano prototype = this.clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 86 */     out.add(MessageNano.mergeFrom(prototype, array, offset, length));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\protobuf\ProtobufDecoderNano.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */