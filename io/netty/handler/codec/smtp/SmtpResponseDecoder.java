/*     */ package io.netty.handler.codec.smtp;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.LineBasedFrameDecoder;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SmtpResponseDecoder
/*     */   extends LineBasedFrameDecoder
/*     */ {
/*     */   private List<CharSequence> details;
/*     */   
/*     */   public SmtpResponseDecoder(int maxLineLength) {
/*  41 */     super(maxLineLength);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SmtpResponse decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
/*  46 */     ByteBuf frame = (ByteBuf)super.decode(ctx, buffer);
/*  47 */     if (frame == null)
/*     */     {
/*  49 */       return null;
/*     */     }
/*     */     try {
/*  52 */       int readable = frame.readableBytes();
/*  53 */       int readerIndex = frame.readerIndex();
/*  54 */       if (readable < 3) {
/*  55 */         throw newDecoderException(buffer, readerIndex, readable);
/*     */       }
/*  57 */       int code = parseCode(frame);
/*  58 */       int separator = frame.readByte();
/*  59 */       CharSequence detail = frame.isReadable() ? frame.toString(CharsetUtil.US_ASCII) : null;
/*     */       
/*  61 */       List<CharSequence> details = this.details;
/*     */       
/*  63 */       switch (separator) {
/*     */         
/*     */         case 32:
/*  66 */           this.details = null;
/*  67 */           if (details != null) {
/*  68 */             if (detail != null) {
/*  69 */               details.add(detail);
/*     */             }
/*     */           }
/*  72 */           else if (detail == null) {
/*  73 */             details = Collections.emptyList();
/*     */           } else {
/*  75 */             details = Collections.singletonList(detail);
/*     */           } 
/*     */           
/*  78 */           return new DefaultSmtpResponse(code, details);
/*     */         
/*     */         case 45:
/*  81 */           if (detail != null) {
/*  82 */             if (details == null)
/*     */             {
/*     */               
/*  85 */               this.details = details = new ArrayList<CharSequence>(4);
/*     */             }
/*  87 */             details.add(detail);
/*     */           } 
/*     */           break;
/*     */         default:
/*  91 */           throw newDecoderException(buffer, readerIndex, readable);
/*     */       } 
/*     */     } finally {
/*  94 */       frame.release();
/*     */     } 
/*  96 */     return null;
/*     */   }
/*     */   
/*     */   private static DecoderException newDecoderException(ByteBuf buffer, int readerIndex, int readable) {
/* 100 */     return new DecoderException("Received invalid line: '" + buffer
/* 101 */         .toString(readerIndex, readable, CharsetUtil.US_ASCII) + '\'');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int parseCode(ByteBuf buffer) {
/* 108 */     int first = parseNumber(buffer.readByte()) * 100;
/* 109 */     int second = parseNumber(buffer.readByte()) * 10;
/* 110 */     int third = parseNumber(buffer.readByte());
/* 111 */     return first + second + third;
/*     */   }
/*     */   
/*     */   private static int parseNumber(byte b) {
/* 115 */     return Character.digit((char)b, 10);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\smtp\SmtpResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */