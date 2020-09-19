/*    */ package io.netty.handler.codec.stomp;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.AsciiHeadersEncoder;
/*    */ import io.netty.handler.codec.MessageToMessageEncoder;
/*    */ import io.netty.util.CharsetUtil;
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
/*    */ public class StompSubframeEncoder
/*    */   extends MessageToMessageEncoder<StompSubframe>
/*    */ {
/*    */   protected void encode(ChannelHandlerContext ctx, StompSubframe msg, List<Object> out) throws Exception {
/* 36 */     if (msg instanceof StompFrame) {
/* 37 */       StompFrame frame = (StompFrame)msg;
/* 38 */       ByteBuf frameBuf = encodeFrame(frame, ctx);
/* 39 */       out.add(frameBuf);
/* 40 */       ByteBuf contentBuf = encodeContent(frame, ctx);
/* 41 */       out.add(contentBuf);
/* 42 */     } else if (msg instanceof StompHeadersSubframe) {
/* 43 */       StompHeadersSubframe frame = (StompHeadersSubframe)msg;
/* 44 */       ByteBuf buf = encodeFrame(frame, ctx);
/* 45 */       out.add(buf);
/* 46 */     } else if (msg instanceof StompContentSubframe) {
/* 47 */       StompContentSubframe stompContentSubframe = (StompContentSubframe)msg;
/* 48 */       ByteBuf buf = encodeContent(stompContentSubframe, ctx);
/* 49 */       out.add(buf);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static ByteBuf encodeContent(StompContentSubframe content, ChannelHandlerContext ctx) {
/* 54 */     if (content instanceof LastStompContentSubframe) {
/* 55 */       ByteBuf buf = ctx.alloc().buffer(content.content().readableBytes() + 1);
/* 56 */       buf.writeBytes(content.content());
/* 57 */       buf.writeByte(0);
/* 58 */       return buf;
/*    */     } 
/* 60 */     return content.content().retain();
/*    */   }
/*    */ 
/*    */   
/*    */   private static ByteBuf encodeFrame(StompHeadersSubframe frame, ChannelHandlerContext ctx) {
/* 65 */     ByteBuf buf = ctx.alloc().buffer();
/*    */     
/* 67 */     buf.writeCharSequence(frame.command().toString(), CharsetUtil.US_ASCII);
/* 68 */     buf.writeByte(10);
/* 69 */     AsciiHeadersEncoder headersEncoder = new AsciiHeadersEncoder(buf, AsciiHeadersEncoder.SeparatorType.COLON, AsciiHeadersEncoder.NewlineType.LF);
/* 70 */     for (Map.Entry<CharSequence, CharSequence> entry : (Iterable<Map.Entry<CharSequence, CharSequence>>)frame.headers()) {
/* 71 */       headersEncoder.encode(entry);
/*    */     }
/* 73 */     buf.writeByte(10);
/* 74 */     return buf;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\stomp\StompSubframeEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */