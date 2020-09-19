/*    */ package io.netty.handler.codec.string;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufUtil;
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageEncoder;
/*    */ import io.netty.util.CharsetUtil;
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import java.nio.CharBuffer;
/*    */ import java.nio.charset.Charset;
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
/*    */ @Sharable
/*    */ public class LineEncoder
/*    */   extends MessageToMessageEncoder<CharSequence>
/*    */ {
/*    */   private final Charset charset;
/*    */   private final byte[] lineSeparator;
/*    */   
/*    */   public LineEncoder() {
/* 63 */     this(LineSeparator.DEFAULT, CharsetUtil.UTF_8);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LineEncoder(LineSeparator lineSeparator) {
/* 70 */     this(lineSeparator, CharsetUtil.UTF_8);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LineEncoder(Charset charset) {
/* 77 */     this(LineSeparator.DEFAULT, charset);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LineEncoder(LineSeparator lineSeparator, Charset charset) {
/* 84 */     this.charset = (Charset)ObjectUtil.checkNotNull(charset, "charset");
/* 85 */     this.lineSeparator = ((LineSeparator)ObjectUtil.checkNotNull(lineSeparator, "lineSeparator")).value().getBytes(charset);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {
/* 90 */     ByteBuf buffer = ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), this.charset, this.lineSeparator.length);
/* 91 */     buffer.writeBytes(this.lineSeparator);
/* 92 */     out.add(buffer);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\string\LineEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */