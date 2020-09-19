/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ public class StringBuilderEncoder
/*     */   implements Encoder<StringBuilder>
/*     */ {
/*     */   private static final int DEFAULT_BYTE_BUFFER_SIZE = 8192;
/*  35 */   private final ThreadLocal<CharBuffer> charBufferThreadLocal = new ThreadLocal<>();
/*  36 */   private final ThreadLocal<ByteBuffer> byteBufferThreadLocal = new ThreadLocal<>();
/*  37 */   private final ThreadLocal<CharsetEncoder> charsetEncoderThreadLocal = new ThreadLocal<>();
/*     */   private final Charset charset;
/*     */   private final int charBufferSize;
/*     */   private final int byteBufferSize;
/*     */   
/*     */   public StringBuilderEncoder(Charset charset) {
/*  43 */     this(charset, Constants.ENCODER_CHAR_BUFFER_SIZE, 8192);
/*     */   }
/*     */   
/*     */   public StringBuilderEncoder(Charset charset, int charBufferSize, int byteBufferSize) {
/*  47 */     this.charBufferSize = charBufferSize;
/*  48 */     this.byteBufferSize = byteBufferSize;
/*  49 */     this.charset = Objects.<Charset>requireNonNull(charset, "charset");
/*     */   }
/*     */ 
/*     */   
/*     */   public void encode(StringBuilder source, ByteBufferDestination destination) {
/*  54 */     ByteBuffer temp = getByteBuffer();
/*  55 */     temp.clear();
/*  56 */     temp.limit(Math.min(temp.capacity(), destination.getByteBuffer().capacity()));
/*  57 */     CharsetEncoder charsetEncoder = getCharsetEncoder();
/*     */     
/*  59 */     int estimatedBytes = estimateBytes(source.length(), charsetEncoder.maxBytesPerChar());
/*  60 */     if (temp.remaining() < estimatedBytes) {
/*  61 */       encodeSynchronized(getCharsetEncoder(), getCharBuffer(), source, destination);
/*     */     } else {
/*  63 */       encodeWithThreadLocals(charsetEncoder, getCharBuffer(), temp, source, destination);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void encodeWithThreadLocals(CharsetEncoder charsetEncoder, CharBuffer charBuffer, ByteBuffer temp, StringBuilder source, ByteBufferDestination destination) {
/*     */     try {
/*  70 */       TextEncoderHelper.encodeTextWithCopy(charsetEncoder, charBuffer, temp, source, destination);
/*  71 */     } catch (Exception ex) {
/*  72 */       logEncodeTextException(ex, source, destination);
/*  73 */       TextEncoderHelper.encodeTextFallBack(this.charset, source, destination);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int estimateBytes(int charCount, float maxBytesPerChar) {
/*  78 */     return (int)(charCount * maxBytesPerChar);
/*     */   }
/*     */ 
/*     */   
/*     */   private void encodeSynchronized(CharsetEncoder charsetEncoder, CharBuffer charBuffer, StringBuilder source, ByteBufferDestination destination) {
/*  83 */     synchronized (destination) {
/*     */       try {
/*  85 */         TextEncoderHelper.encodeText(charsetEncoder, charBuffer, destination.getByteBuffer(), source, destination);
/*     */       }
/*  87 */       catch (Exception ex) {
/*  88 */         logEncodeTextException(ex, source, destination);
/*  89 */         TextEncoderHelper.encodeTextFallBack(this.charset, source, destination);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private CharsetEncoder getCharsetEncoder() {
/*  95 */     CharsetEncoder result = this.charsetEncoderThreadLocal.get();
/*  96 */     if (result == null) {
/*  97 */       result = this.charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
/*     */       
/*  99 */       this.charsetEncoderThreadLocal.set(result);
/*     */     } 
/* 101 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private CharBuffer getCharBuffer() {
/* 106 */     CharBuffer result = this.charBufferThreadLocal.get();
/* 107 */     if (result == null) {
/* 108 */       result = CharBuffer.wrap(new char[this.charBufferSize]);
/* 109 */       this.charBufferThreadLocal.set(result);
/*     */     } 
/* 111 */     return result;
/*     */   }
/*     */   
/*     */   private ByteBuffer getByteBuffer() {
/* 115 */     ByteBuffer result = this.byteBufferThreadLocal.get();
/* 116 */     if (result == null) {
/* 117 */       result = ByteBuffer.wrap(new byte[this.byteBufferSize]);
/* 118 */       this.byteBufferThreadLocal.set(result);
/*     */     } 
/* 120 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private void logEncodeTextException(Exception ex, StringBuilder text, ByteBufferDestination destination) {
/* 125 */     StatusLogger.getLogger().error("Recovering from StringBuilderEncoder.encode('{}') error: {}", text, ex, ex);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\StringBuilderEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */