/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
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
/*     */ public abstract class CharSink
/*     */ {
/*     */   public abstract Writer openStream() throws IOException;
/*     */   
/*     */   public Writer openBufferedStream() throws IOException {
/*  79 */     Writer writer = openStream();
/*  80 */     return (writer instanceof BufferedWriter) ? writer : new BufferedWriter(writer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(CharSequence charSequence) throws IOException {
/*  91 */     Preconditions.checkNotNull(charSequence);
/*     */     
/*  93 */     Closer closer = Closer.create();
/*     */     try {
/*  95 */       Writer out = closer.<Writer>register(openStream());
/*  96 */       out.append(charSequence);
/*  97 */       out.flush();
/*  98 */     } catch (Throwable e) {
/*  99 */       throw closer.rethrow(e);
/*     */     } finally {
/* 101 */       closer.close();
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
/*     */   public void writeLines(Iterable<? extends CharSequence> lines) throws IOException {
/* 113 */     writeLines(lines, System.getProperty("line.separator"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeLines(Iterable<? extends CharSequence> lines, String lineSeparator) throws IOException {
/* 124 */     Preconditions.checkNotNull(lines);
/* 125 */     Preconditions.checkNotNull(lineSeparator);
/*     */     
/* 127 */     Closer closer = Closer.create();
/*     */     try {
/* 129 */       Writer out = closer.<Writer>register(openBufferedStream());
/* 130 */       for (CharSequence line : lines) {
/* 131 */         out.append(line).append(lineSeparator);
/*     */       }
/* 133 */       out.flush();
/* 134 */     } catch (Throwable e) {
/* 135 */       throw closer.rethrow(e);
/*     */     } finally {
/* 137 */       closer.close();
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
/*     */   public long writeFrom(Readable readable) throws IOException {
/* 151 */     Preconditions.checkNotNull(readable);
/*     */     
/* 153 */     Closer closer = Closer.create();
/*     */     try {
/* 155 */       Writer out = closer.<Writer>register(openStream());
/* 156 */       long written = CharStreams.copy(readable, out);
/* 157 */       out.flush();
/* 158 */       return written;
/* 159 */     } catch (Throwable e) {
/* 160 */       throw closer.rethrow(e);
/*     */     } finally {
/* 162 */       closer.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\CharSink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */