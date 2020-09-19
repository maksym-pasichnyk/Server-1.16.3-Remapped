/*    */ package org.apache.logging.log4j.core.layout;
/*    */ 
/*    */ import java.nio.CharBuffer;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.CharsetEncoder;
/*    */ import java.nio.charset.CodingErrorAction;
/*    */ import java.util.Objects;
/*    */ import org.apache.logging.log4j.core.util.Constants;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
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
/*    */ public class LockingStringBuilderEncoder
/*    */   implements Encoder<StringBuilder>
/*    */ {
/*    */   private final Charset charset;
/*    */   private final CharsetEncoder charsetEncoder;
/*    */   private final CharBuffer cachedCharBuffer;
/*    */   
/*    */   public LockingStringBuilderEncoder(Charset charset) {
/* 38 */     this(charset, Constants.ENCODER_CHAR_BUFFER_SIZE);
/*    */   }
/*    */   
/*    */   public LockingStringBuilderEncoder(Charset charset, int charBufferSize) {
/* 42 */     this.charset = Objects.<Charset>requireNonNull(charset, "charset");
/* 43 */     this.charsetEncoder = charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
/*    */     
/* 45 */     this.cachedCharBuffer = CharBuffer.wrap(new char[charBufferSize]);
/*    */   }
/*    */   
/*    */   private CharBuffer getCharBuffer() {
/* 49 */     return this.cachedCharBuffer;
/*    */   }
/*    */ 
/*    */   
/*    */   public void encode(StringBuilder source, ByteBufferDestination destination) {
/* 54 */     synchronized (destination) {
/*    */       try {
/* 56 */         TextEncoderHelper.encodeText(this.charsetEncoder, this.cachedCharBuffer, destination.getByteBuffer(), source, destination);
/*    */       }
/* 58 */       catch (Exception ex) {
/* 59 */         logEncodeTextException(ex, source, destination);
/* 60 */         TextEncoderHelper.encodeTextFallBack(this.charset, source, destination);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private void logEncodeTextException(Exception ex, StringBuilder text, ByteBufferDestination destination) {
/* 67 */     StatusLogger.getLogger().error("Recovering from LockingStringBuilderEncoder.encode('{}') error", text, ex);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\LockingStringBuilderEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */