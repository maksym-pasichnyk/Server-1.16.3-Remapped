/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ public abstract class ByteSink
/*     */ {
/*     */   public CharSink asCharSink(Charset charset) {
/*  60 */     return new AsCharSink(charset);
/*     */   }
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
/*     */   public abstract OutputStream openStream() throws IOException;
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
/*     */   public OutputStream openBufferedStream() throws IOException {
/*  86 */     OutputStream out = openStream();
/*  87 */     return (out instanceof BufferedOutputStream) ? out : new BufferedOutputStream(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] bytes) throws IOException {
/*  98 */     Preconditions.checkNotNull(bytes);
/*     */     
/* 100 */     Closer closer = Closer.create();
/*     */     try {
/* 102 */       OutputStream out = closer.<OutputStream>register(openStream());
/* 103 */       out.write(bytes);
/* 104 */       out.flush();
/* 105 */     } catch (Throwable e) {
/* 106 */       throw closer.rethrow(e);
/*     */     } finally {
/* 108 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long writeFrom(InputStream input) throws IOException {
/* 122 */     Preconditions.checkNotNull(input);
/*     */     
/* 124 */     Closer closer = Closer.create();
/*     */     try {
/* 126 */       OutputStream out = closer.<OutputStream>register(openStream());
/* 127 */       long written = ByteStreams.copy(input, out);
/* 128 */       out.flush();
/* 129 */       return written;
/* 130 */     } catch (Throwable e) {
/* 131 */       throw closer.rethrow(e);
/*     */     } finally {
/* 133 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class AsCharSink
/*     */     extends CharSink
/*     */   {
/*     */     private final Charset charset;
/*     */ 
/*     */     
/*     */     private AsCharSink(Charset charset) {
/* 146 */       this.charset = (Charset)Preconditions.checkNotNull(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer openStream() throws IOException {
/* 151 */       return new OutputStreamWriter(ByteSink.this.openStream(), this.charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 156 */       return ByteSink.this.toString() + ".asCharSink(" + this.charset + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\ByteSink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */