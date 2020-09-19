/*    */ package io.netty.handler.codec.protobuf;
/*    */ 
/*    */ import com.google.protobuf.nano.CodedOutputByteBufferNano;
/*    */ import com.google.protobuf.nano.MessageNano;
/*    */ import io.netty.buffer.ByteBuf;
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
/*    */ @Sharable
/*    */ public class ProtobufEncoderNano
/*    */   extends MessageToMessageEncoder<MessageNano>
/*    */ {
/*    */   protected void encode(ChannelHandlerContext ctx, MessageNano msg, List<Object> out) throws Exception {
/* 64 */     int size = msg.getSerializedSize();
/* 65 */     ByteBuf buffer = ctx.alloc().heapBuffer(size, size);
/* 66 */     byte[] array = buffer.array();
/* 67 */     CodedOutputByteBufferNano cobbn = CodedOutputByteBufferNano.newInstance(array, buffer
/* 68 */         .arrayOffset(), buffer.capacity());
/* 69 */     msg.writeTo(cobbn);
/* 70 */     buffer.writerIndex(size);
/* 71 */     out.add(buffer);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\protobuf\ProtobufEncoderNano.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */