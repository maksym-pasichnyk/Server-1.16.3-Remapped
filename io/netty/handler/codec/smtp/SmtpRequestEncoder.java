/*     */ package io.netty.handler.codec.smtp;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.MessageToMessageEncoder;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SmtpRequestEncoder
/*     */   extends MessageToMessageEncoder<Object>
/*     */ {
/*     */   private static final int CRLF_SHORT = 3338;
/*     */   private static final byte SP = 32;
/*  36 */   private static final ByteBuf DOT_CRLF_BUFFER = Unpooled.unreleasableBuffer(
/*  37 */       Unpooled.directBuffer(3).writeByte(46).writeByte(13).writeByte(10));
/*     */   
/*     */   private boolean contentExpected;
/*     */ 
/*     */   
/*     */   public boolean acceptOutboundMessage(Object msg) throws Exception {
/*  43 */     return (msg instanceof SmtpRequest || msg instanceof SmtpContent);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
/*  48 */     if (msg instanceof SmtpRequest) {
/*  49 */       SmtpRequest req = (SmtpRequest)msg;
/*  50 */       if (this.contentExpected) {
/*  51 */         if (req.command().equals(SmtpCommand.RSET)) {
/*  52 */           this.contentExpected = false;
/*     */         } else {
/*  54 */           throw new IllegalStateException("SmtpContent expected");
/*     */         } 
/*     */       }
/*  57 */       boolean release = true;
/*  58 */       ByteBuf buffer = ctx.alloc().buffer();
/*     */       try {
/*  60 */         req.command().encode(buffer);
/*  61 */         writeParameters(req.parameters(), buffer);
/*  62 */         ByteBufUtil.writeShortBE(buffer, 3338);
/*  63 */         out.add(buffer);
/*  64 */         release = false;
/*  65 */         if (req.command().isContentExpected()) {
/*  66 */           this.contentExpected = true;
/*     */         }
/*     */       } finally {
/*  69 */         if (release) {
/*  70 */           buffer.release();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  75 */     if (msg instanceof SmtpContent) {
/*  76 */       if (!this.contentExpected) {
/*  77 */         throw new IllegalStateException("No SmtpContent expected");
/*     */       }
/*  79 */       ByteBuf content = ((SmtpContent)msg).content();
/*  80 */       out.add(content.retain());
/*  81 */       if (msg instanceof LastSmtpContent) {
/*  82 */         out.add(DOT_CRLF_BUFFER.retainedDuplicate());
/*  83 */         this.contentExpected = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void writeParameters(List<CharSequence> parameters, ByteBuf out) {
/*  89 */     if (parameters.isEmpty()) {
/*     */       return;
/*     */     }
/*  92 */     out.writeByte(32);
/*  93 */     if (parameters instanceof java.util.RandomAccess) {
/*  94 */       int sizeMinusOne = parameters.size() - 1;
/*  95 */       for (int i = 0; i < sizeMinusOne; i++) {
/*  96 */         ByteBufUtil.writeAscii(out, parameters.get(i));
/*  97 */         out.writeByte(32);
/*     */       } 
/*  99 */       ByteBufUtil.writeAscii(out, parameters.get(sizeMinusOne));
/*     */     } else {
/* 101 */       Iterator<CharSequence> params = parameters.iterator();
/*     */       while (true) {
/* 103 */         ByteBufUtil.writeAscii(out, params.next());
/* 104 */         if (params.hasNext()) {
/* 105 */           out.writeByte(32);
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\smtp\SmtpRequestEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */