/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.util.ByteProcessor;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.zip.Adler32;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.Checksum;
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
/*     */ abstract class ByteBufChecksum
/*     */   implements Checksum
/*     */ {
/*  42 */   private static final Method ADLER32_UPDATE_METHOD = updateByteBuffer(new Adler32());
/*  43 */   private static final Method CRC32_UPDATE_METHOD = updateByteBuffer(new CRC32());
/*     */ 
/*     */   
/*  46 */   private final ByteProcessor updateProcessor = new ByteProcessor()
/*     */     {
/*     */       public boolean process(byte value) throws Exception {
/*  49 */         ByteBufChecksum.this.update(value);
/*  50 */         return true;
/*     */       }
/*     */     };
/*     */   
/*     */   private static Method updateByteBuffer(Checksum checksum) {
/*  55 */     if (PlatformDependent.javaVersion() >= 8) {
/*     */       try {
/*  57 */         Method method = checksum.getClass().getDeclaredMethod("update", new Class[] { ByteBuffer.class });
/*  58 */         method.invoke(method, new Object[] { ByteBuffer.allocate(1) });
/*  59 */         return method;
/*  60 */       } catch (Throwable ignore) {
/*  61 */         return null;
/*     */       } 
/*     */     }
/*  64 */     return null;
/*     */   }
/*     */   
/*     */   static ByteBufChecksum wrapChecksum(Checksum checksum) {
/*  68 */     ObjectUtil.checkNotNull(checksum, "checksum");
/*  69 */     if (checksum instanceof Adler32 && ADLER32_UPDATE_METHOD != null) {
/*  70 */       return new ReflectiveByteBufChecksum(checksum, ADLER32_UPDATE_METHOD);
/*     */     }
/*  72 */     if (checksum instanceof CRC32 && CRC32_UPDATE_METHOD != null) {
/*  73 */       return new ReflectiveByteBufChecksum(checksum, CRC32_UPDATE_METHOD);
/*     */     }
/*  75 */     return new SlowByteBufChecksum(checksum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(ByteBuf b, int off, int len) {
/*  82 */     if (b.hasArray()) {
/*  83 */       update(b.array(), b.arrayOffset() + off, len);
/*     */     } else {
/*  85 */       b.forEachByte(off, len, this.updateProcessor);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class ReflectiveByteBufChecksum extends SlowByteBufChecksum {
/*     */     private final Method method;
/*     */     
/*     */     ReflectiveByteBufChecksum(Checksum checksum, Method method) {
/*  93 */       super(checksum);
/*  94 */       this.method = method;
/*     */     }
/*     */ 
/*     */     
/*     */     public void update(ByteBuf b, int off, int len) {
/*  99 */       if (b.hasArray()) {
/* 100 */         update(b.array(), b.arrayOffset() + off, len);
/*     */       } else {
/*     */         try {
/* 103 */           this.method.invoke(this.checksum, new Object[] { CompressionUtil.safeNioBuffer(b) });
/* 104 */         } catch (Throwable cause) {
/* 105 */           throw new Error();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SlowByteBufChecksum
/*     */     extends ByteBufChecksum {
/*     */     protected final Checksum checksum;
/*     */     
/*     */     SlowByteBufChecksum(Checksum checksum) {
/* 116 */       this.checksum = checksum;
/*     */     }
/*     */ 
/*     */     
/*     */     public void update(int b) {
/* 121 */       this.checksum.update(b);
/*     */     }
/*     */ 
/*     */     
/*     */     public void update(byte[] b, int off, int len) {
/* 126 */       this.checksum.update(b, off, len);
/*     */     }
/*     */ 
/*     */     
/*     */     public long getValue() {
/* 131 */       return this.checksum.getValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public void reset() {
/* 136 */       this.checksum.reset();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\compression\ByteBufChecksum.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */