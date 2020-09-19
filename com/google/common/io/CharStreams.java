/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.ArrayList;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class CharStreams
/*     */ {
/*     */   static CharBuffer createBuffer() {
/*  54 */     return CharBuffer.allocate(2048);
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
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static long copy(Readable from, Appendable to) throws IOException {
/*  70 */     Preconditions.checkNotNull(from);
/*  71 */     Preconditions.checkNotNull(to);
/*  72 */     CharBuffer buf = createBuffer();
/*  73 */     long total = 0L;
/*  74 */     while (from.read(buf) != -1) {
/*  75 */       buf.flip();
/*  76 */       to.append(buf);
/*  77 */       total += buf.remaining();
/*  78 */       buf.clear();
/*     */     } 
/*  80 */     return total;
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
/*     */   public static String toString(Readable r) throws IOException {
/*  92 */     return toStringBuilder(r).toString();
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
/*     */   private static StringBuilder toStringBuilder(Readable r) throws IOException {
/* 104 */     StringBuilder sb = new StringBuilder();
/* 105 */     copy(r, sb);
/* 106 */     return sb;
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
/*     */   
/*     */   public static List<String> readLines(Readable r) throws IOException {
/* 121 */     List<String> result = new ArrayList<>();
/* 122 */     LineReader lineReader = new LineReader(r);
/*     */     String line;
/* 124 */     while ((line = lineReader.readLine()) != null) {
/* 125 */       result.add(line);
/*     */     }
/* 127 */     return result;
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
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readLines(Readable readable, LineProcessor<T> processor) throws IOException {
/* 141 */     Preconditions.checkNotNull(readable);
/* 142 */     Preconditions.checkNotNull(processor);
/*     */     
/* 144 */     LineReader lineReader = new LineReader(readable); String line; do {
/*     */     
/* 146 */     } while ((line = lineReader.readLine()) != null && 
/* 147 */       processor.processLine(line));
/*     */ 
/*     */ 
/*     */     
/* 151 */     return processor.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static long exhaust(Readable readable) throws IOException {
/* 162 */     long total = 0L;
/*     */     
/* 164 */     CharBuffer buf = createBuffer(); long read;
/* 165 */     while ((read = readable.read(buf)) != -1L) {
/* 166 */       total += read;
/* 167 */       buf.clear();
/*     */     } 
/* 169 */     return total;
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
/*     */   public static void skipFully(Reader reader, long n) throws IOException {
/* 182 */     Preconditions.checkNotNull(reader);
/* 183 */     while (n > 0L) {
/* 184 */       long amt = reader.skip(n);
/* 185 */       if (amt == 0L) {
/* 186 */         throw new EOFException();
/*     */       }
/* 188 */       n -= amt;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Writer nullWriter() {
/* 198 */     return NullWriter.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class NullWriter
/*     */     extends Writer {
/* 203 */     private static final NullWriter INSTANCE = new NullWriter();
/*     */ 
/*     */     
/*     */     public void write(int c) {}
/*     */ 
/*     */     
/*     */     public void write(char[] cbuf) {
/* 210 */       Preconditions.checkNotNull(cbuf);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(char[] cbuf, int off, int len) {
/* 215 */       Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(String str) {
/* 220 */       Preconditions.checkNotNull(str);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(String str, int off, int len) {
/* 225 */       Preconditions.checkPositionIndexes(off, off + len, str.length());
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer append(CharSequence csq) {
/* 230 */       Preconditions.checkNotNull(csq);
/* 231 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer append(CharSequence csq, int start, int end) {
/* 236 */       Preconditions.checkPositionIndexes(start, end, csq.length());
/* 237 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer append(char c) {
/* 242 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public void flush() {}
/*     */ 
/*     */     
/*     */     public void close() {}
/*     */ 
/*     */     
/*     */     public String toString() {
/* 253 */       return "CharStreams.nullWriter()";
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
/*     */   public static Writer asWriter(Appendable target) {
/* 266 */     if (target instanceof Writer) {
/* 267 */       return (Writer)target;
/*     */     }
/* 269 */     return new AppendableWriter(target);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\CharStreams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */