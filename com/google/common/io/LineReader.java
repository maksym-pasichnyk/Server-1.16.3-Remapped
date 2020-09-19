/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.nio.CharBuffer;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Queue;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ @GwtIncompatible
/*    */ public final class LineReader
/*    */ {
/*    */   private final Readable readable;
/*    */   private final Reader reader;
/* 42 */   private final CharBuffer cbuf = CharStreams.createBuffer();
/* 43 */   private final char[] buf = this.cbuf.array();
/*    */   
/* 45 */   private final Queue<String> lines = new LinkedList<>();
/* 46 */   private final LineBuffer lineBuf = new LineBuffer()
/*    */     {
/*    */       protected void handleLine(String line, String end)
/*    */       {
/* 50 */         LineReader.this.lines.add(line);
/*    */       }
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LineReader(Readable readable) {
/* 58 */     this.readable = (Readable)Preconditions.checkNotNull(readable);
/* 59 */     this.reader = (readable instanceof Reader) ? (Reader)readable : null;
/*    */   }
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
/*    */   @CanIgnoreReturnValue
/*    */   public String readLine() throws IOException {
/* 73 */     while (this.lines.peek() == null) {
/* 74 */       this.cbuf.clear();
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 79 */       int read = (this.reader != null) ? this.reader.read(this.buf, 0, this.buf.length) : this.readable.read(this.cbuf);
/* 80 */       if (read == -1) {
/* 81 */         this.lineBuf.finish();
/*    */         break;
/*    */       } 
/* 84 */       this.lineBuf.add(this.buf, 0, read);
/*    */     } 
/* 86 */     return this.lines.poll();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\LineReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */