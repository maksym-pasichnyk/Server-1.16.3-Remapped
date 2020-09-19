/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
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
/*     */ public class TextEncoderHelper
/*     */ {
/*     */   static void encodeTextFallBack(Charset charset, StringBuilder text, ByteBufferDestination destination) {
/*  38 */     byte[] bytes = text.toString().getBytes(charset);
/*  39 */     synchronized (destination) {
/*  40 */       ByteBuffer buffer = destination.getByteBuffer();
/*  41 */       int offset = 0;
/*     */       while (true) {
/*  43 */         int length = Math.min(bytes.length - offset, buffer.remaining());
/*  44 */         buffer.put(bytes, offset, length);
/*  45 */         offset += length;
/*  46 */         if (offset < bytes.length) {
/*  47 */           buffer = destination.drain(buffer);
/*     */         }
/*  49 */         if (offset >= bytes.length)
/*     */           return; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static void encodeTextWithCopy(CharsetEncoder charsetEncoder, CharBuffer charBuf, ByteBuffer temp, StringBuilder text, ByteBufferDestination destination) {
/*  56 */     encodeText(charsetEncoder, charBuf, temp, text, destination);
/*  57 */     copyDataToDestination(temp, destination);
/*     */   }
/*     */   
/*     */   private static void copyDataToDestination(ByteBuffer temp, ByteBufferDestination destination) {
/*  61 */     synchronized (destination) {
/*  62 */       ByteBuffer destinationBuffer = destination.getByteBuffer();
/*  63 */       if (destinationBuffer != temp) {
/*  64 */         temp.flip();
/*  65 */         if (temp.remaining() > destinationBuffer.remaining()) {
/*  66 */           destinationBuffer = destination.drain(destinationBuffer);
/*     */         }
/*  68 */         destinationBuffer.put(temp);
/*  69 */         temp.clear();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void encodeText(CharsetEncoder charsetEncoder, CharBuffer charBuf, ByteBuffer byteBuf, StringBuilder text, ByteBufferDestination destination) {
/*  76 */     charsetEncoder.reset();
/*  77 */     ByteBuffer temp = byteBuf;
/*  78 */     int start = 0;
/*  79 */     int todoChars = text.length();
/*  80 */     boolean endOfInput = true;
/*     */     do {
/*  82 */       charBuf.clear();
/*  83 */       int copied = copy(text, start, charBuf);
/*  84 */       start += copied;
/*  85 */       todoChars -= copied;
/*  86 */       endOfInput = (todoChars <= 0);
/*     */       
/*  88 */       charBuf.flip();
/*  89 */       temp = encode(charsetEncoder, charBuf, endOfInput, destination, temp);
/*  90 */     } while (!endOfInput);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void encodeText(CharsetEncoder charsetEncoder, CharBuffer charBuf, ByteBufferDestination destination) {
/*  99 */     synchronized (destination) {
/* 100 */       charsetEncoder.reset();
/* 101 */       ByteBuffer byteBuf = destination.getByteBuffer();
/* 102 */       encode(charsetEncoder, charBuf, true, destination, byteBuf);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer encode(CharsetEncoder charsetEncoder, CharBuffer charBuf, boolean endOfInput, ByteBufferDestination destination, ByteBuffer byteBuf) {
/*     */     try {
/* 109 */       byteBuf = encodeAsMuchAsPossible(charsetEncoder, charBuf, endOfInput, destination, byteBuf);
/* 110 */       if (endOfInput) {
/* 111 */         byteBuf = flushRemainingBytes(charsetEncoder, destination, byteBuf);
/*     */       }
/* 113 */     } catch (CharacterCodingException ex) {
/* 114 */       throw new IllegalStateException(ex);
/*     */     } 
/* 116 */     return byteBuf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuffer encodeAsMuchAsPossible(CharsetEncoder charsetEncoder, CharBuffer charBuf, boolean endOfInput, ByteBufferDestination destination, ByteBuffer temp) throws CharacterCodingException {
/*     */     while (true) {
/* 124 */       CoderResult result = charsetEncoder.encode(charBuf, temp, endOfInput);
/* 125 */       temp = drainIfByteBufferFull(destination, temp, result);
/* 126 */       if (!result.isOverflow()) {
/* 127 */         if (!result.isUnderflow()) {
/* 128 */           result.throwException();
/*     */         }
/* 130 */         return temp;
/*     */       } 
/*     */     } 
/*     */   } private static ByteBuffer drainIfByteBufferFull(ByteBufferDestination destination, ByteBuffer temp, CoderResult result) {
/* 134 */     if (result.isOverflow()) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 139 */       ByteBuffer destinationBuffer = destination.getByteBuffer();
/* 140 */       if (destinationBuffer != temp) {
/* 141 */         temp.flip();
/* 142 */         destinationBuffer.put(temp);
/* 143 */         temp.clear();
/*     */       } 
/*     */ 
/*     */       
/* 147 */       destinationBuffer = destination.drain(destinationBuffer);
/* 148 */       temp = destinationBuffer;
/*     */     } 
/* 150 */     return temp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuffer flushRemainingBytes(CharsetEncoder charsetEncoder, ByteBufferDestination destination, ByteBuffer temp) throws CharacterCodingException {
/*     */     while (true) {
/* 159 */       CoderResult result = charsetEncoder.flush(temp);
/* 160 */       temp = drainIfByteBufferFull(destination, temp, result);
/* 161 */       if (!result.isOverflow()) {
/* 162 */         if (!result.isUnderflow()) {
/* 163 */           result.throwException();
/*     */         }
/* 165 */         return temp;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int copy(StringBuilder source, int offset, CharBuffer destination) {
/* 176 */     int length = Math.min(source.length() - offset, destination.remaining());
/* 177 */     char[] array = destination.array();
/* 178 */     int start = destination.position();
/* 179 */     source.getChars(offset, offset + length, array, start);
/* 180 */     destination.position(start + length);
/* 181 */     return length;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\TextEncoderHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */