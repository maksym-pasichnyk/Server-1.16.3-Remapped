/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Closeable;
/*     */ import java.io.Flushable;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import javax.annotation.Nullable;
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
/*     */ class AppendableWriter
/*     */   extends Writer
/*     */ {
/*     */   private final Appendable target;
/*     */   private boolean closed;
/*     */   
/*     */   AppendableWriter(Appendable target) {
/*  45 */     this.target = (Appendable)Preconditions.checkNotNull(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/*  54 */     checkNotClosed();
/*     */ 
/*     */     
/*  57 */     this.target.append(new String(cbuf, off, len));
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*  62 */     checkNotClosed();
/*  63 */     if (this.target instanceof Flushable) {
/*  64 */       ((Flushable)this.target).flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  70 */     this.closed = true;
/*  71 */     if (this.target instanceof Closeable) {
/*  72 */       ((Closeable)this.target).close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int c) throws IOException {
/*  82 */     checkNotClosed();
/*  83 */     this.target.append((char)c);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(@Nullable String str) throws IOException {
/*  88 */     checkNotClosed();
/*  89 */     this.target.append(str);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(@Nullable String str, int off, int len) throws IOException {
/*  94 */     checkNotClosed();
/*     */     
/*  96 */     this.target.append(str, off, off + len);
/*     */   }
/*     */ 
/*     */   
/*     */   public Writer append(char c) throws IOException {
/* 101 */     checkNotClosed();
/* 102 */     this.target.append(c);
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Writer append(@Nullable CharSequence charSeq) throws IOException {
/* 108 */     checkNotClosed();
/* 109 */     this.target.append(charSeq);
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Writer append(@Nullable CharSequence charSeq, int start, int end) throws IOException {
/* 115 */     checkNotClosed();
/* 116 */     this.target.append(charSeq, start, end);
/* 117 */     return this;
/*     */   }
/*     */   
/*     */   private void checkNotClosed() throws IOException {
/* 121 */     if (this.closed)
/* 122 */       throw new IOException("Cannot write to a closed writer."); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\AppendableWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */