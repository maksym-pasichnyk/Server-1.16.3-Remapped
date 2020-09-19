/*     */ package io.netty.handler.codec.protobuf;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.handler.codec.CorruptedFrameException;
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
/*     */ public class ProtobufVarint32FrameDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
/*  51 */     in.markReaderIndex();
/*  52 */     int preIndex = in.readerIndex();
/*  53 */     int length = readRawVarint32(in);
/*  54 */     if (preIndex == in.readerIndex()) {
/*     */       return;
/*     */     }
/*  57 */     if (length < 0) {
/*  58 */       throw new CorruptedFrameException("negative length: " + length);
/*     */     }
/*     */     
/*  61 */     if (in.readableBytes() < length) {
/*  62 */       in.resetReaderIndex();
/*     */     } else {
/*  64 */       out.add(in.readRetainedSlice(length));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int readRawVarint32(ByteBuf buffer) {
/*  74 */     if (!buffer.isReadable()) {
/*  75 */       return 0;
/*     */     }
/*  77 */     buffer.markReaderIndex();
/*  78 */     byte tmp = buffer.readByte();
/*  79 */     if (tmp >= 0) {
/*  80 */       return tmp;
/*     */     }
/*  82 */     int result = tmp & Byte.MAX_VALUE;
/*  83 */     if (!buffer.isReadable()) {
/*  84 */       buffer.resetReaderIndex();
/*  85 */       return 0;
/*     */     } 
/*  87 */     if ((tmp = buffer.readByte()) >= 0) {
/*  88 */       result |= tmp << 7;
/*     */     } else {
/*  90 */       result |= (tmp & Byte.MAX_VALUE) << 7;
/*  91 */       if (!buffer.isReadable()) {
/*  92 */         buffer.resetReaderIndex();
/*  93 */         return 0;
/*     */       } 
/*  95 */       if ((tmp = buffer.readByte()) >= 0) {
/*  96 */         result |= tmp << 14;
/*     */       } else {
/*  98 */         result |= (tmp & Byte.MAX_VALUE) << 14;
/*  99 */         if (!buffer.isReadable()) {
/* 100 */           buffer.resetReaderIndex();
/* 101 */           return 0;
/*     */         } 
/* 103 */         if ((tmp = buffer.readByte()) >= 0) {
/* 104 */           result |= tmp << 21;
/*     */         } else {
/* 106 */           result |= (tmp & Byte.MAX_VALUE) << 21;
/* 107 */           if (!buffer.isReadable()) {
/* 108 */             buffer.resetReaderIndex();
/* 109 */             return 0;
/*     */           } 
/* 111 */           result |= (tmp = buffer.readByte()) << 28;
/* 112 */           if (tmp < 0) {
/* 113 */             throw new CorruptedFrameException("malformed varint.");
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 118 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\protobuf\ProtobufVarint32FrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */