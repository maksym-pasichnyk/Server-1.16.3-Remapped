/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.CharsetUtil;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ public class ByteBufOutputStream
/*     */   extends OutputStream
/*     */   implements DataOutput
/*     */ {
/*     */   private final ByteBuf buffer;
/*     */   private final int startIndex;
/*  42 */   private final DataOutputStream utf8out = new DataOutputStream(this);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBufOutputStream(ByteBuf buffer) {
/*  48 */     if (buffer == null) {
/*  49 */       throw new NullPointerException("buffer");
/*     */     }
/*  51 */     this.buffer = buffer;
/*  52 */     this.startIndex = buffer.writerIndex();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int writtenBytes() {
/*  59 */     return this.buffer.writerIndex() - this.startIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  64 */     if (len == 0) {
/*     */       return;
/*     */     }
/*     */     
/*  68 */     this.buffer.writeBytes(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/*  73 */     this.buffer.writeBytes(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/*  78 */     this.buffer.writeByte(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeBoolean(boolean v) throws IOException {
/*  83 */     this.buffer.writeBoolean(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeByte(int v) throws IOException {
/*  88 */     this.buffer.writeByte(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeBytes(String s) throws IOException {
/*  93 */     this.buffer.writeCharSequence(s, CharsetUtil.US_ASCII);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeChar(int v) throws IOException {
/*  98 */     this.buffer.writeChar(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeChars(String s) throws IOException {
/* 103 */     int len = s.length();
/* 104 */     for (int i = 0; i < len; i++) {
/* 105 */       this.buffer.writeChar(s.charAt(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeDouble(double v) throws IOException {
/* 111 */     this.buffer.writeDouble(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeFloat(float v) throws IOException {
/* 116 */     this.buffer.writeFloat(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeInt(int v) throws IOException {
/* 121 */     this.buffer.writeInt(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeLong(long v) throws IOException {
/* 126 */     this.buffer.writeLong(v);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeShort(int v) throws IOException {
/* 131 */     this.buffer.writeShort((short)v);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeUTF(String s) throws IOException {
/* 136 */     this.utf8out.writeUTF(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuf buffer() {
/* 143 */     return this.buffer;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\ByteBufOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */