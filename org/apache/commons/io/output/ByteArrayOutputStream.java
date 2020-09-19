/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.SequenceInputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.commons.io.input.ClosedInputStream;
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
/*     */ public class ByteArrayOutputStream
/*     */   extends OutputStream
/*     */ {
/*  60 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*     */ 
/*     */   
/*  63 */   private final List<byte[]> buffers = (List)new ArrayList<byte>();
/*     */ 
/*     */   
/*     */   private int currentBufferIndex;
/*     */ 
/*     */   
/*     */   private int filledBufferSum;
/*     */ 
/*     */   
/*     */   private byte[] currentBuffer;
/*     */   
/*     */   private int count;
/*     */   
/*     */   private boolean reuseBuffers = true;
/*     */ 
/*     */   
/*     */   public ByteArrayOutputStream() {
/*  80 */     this(1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArrayOutputStream(int size) {
/*  91 */     if (size < 0) {
/*  92 */       throw new IllegalArgumentException("Negative initial size: " + size);
/*     */     }
/*     */     
/*  95 */     synchronized (this) {
/*  96 */       needNewBuffer(size);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void needNewBuffer(int newcount) {
/* 107 */     if (this.currentBufferIndex < this.buffers.size() - 1) {
/*     */       
/* 109 */       this.filledBufferSum += this.currentBuffer.length;
/*     */       
/* 111 */       this.currentBufferIndex++;
/* 112 */       this.currentBuffer = this.buffers.get(this.currentBufferIndex);
/*     */     } else {
/*     */       int newBufferSize;
/*     */       
/* 116 */       if (this.currentBuffer == null) {
/* 117 */         newBufferSize = newcount;
/* 118 */         this.filledBufferSum = 0;
/*     */       } else {
/* 120 */         newBufferSize = Math.max(this.currentBuffer.length << 1, newcount - this.filledBufferSum);
/*     */ 
/*     */         
/* 123 */         this.filledBufferSum += this.currentBuffer.length;
/*     */       } 
/*     */       
/* 126 */       this.currentBufferIndex++;
/* 127 */       this.currentBuffer = new byte[newBufferSize];
/* 128 */       this.buffers.add(this.currentBuffer);
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
/*     */   public void write(byte[] b, int off, int len) {
/* 140 */     if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 145 */       throw new IndexOutOfBoundsException(); } 
/* 146 */     if (len == 0) {
/*     */       return;
/*     */     }
/* 149 */     synchronized (this) {
/* 150 */       int newcount = this.count + len;
/* 151 */       int remaining = len;
/* 152 */       int inBufferPos = this.count - this.filledBufferSum;
/* 153 */       while (remaining > 0) {
/* 154 */         int part = Math.min(remaining, this.currentBuffer.length - inBufferPos);
/* 155 */         System.arraycopy(b, off + len - remaining, this.currentBuffer, inBufferPos, part);
/* 156 */         remaining -= part;
/* 157 */         if (remaining > 0) {
/* 158 */           needNewBuffer(newcount);
/* 159 */           inBufferPos = 0;
/*     */         } 
/*     */       } 
/* 162 */       this.count = newcount;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void write(int b) {
/* 172 */     int inBufferPos = this.count - this.filledBufferSum;
/* 173 */     if (inBufferPos == this.currentBuffer.length) {
/* 174 */       needNewBuffer(this.count + 1);
/* 175 */       inBufferPos = 0;
/*     */     } 
/* 177 */     this.currentBuffer[inBufferPos] = (byte)b;
/* 178 */     this.count++;
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
/*     */   public synchronized int write(InputStream in) throws IOException {
/* 193 */     int readCount = 0;
/* 194 */     int inBufferPos = this.count - this.filledBufferSum;
/* 195 */     int n = in.read(this.currentBuffer, inBufferPos, this.currentBuffer.length - inBufferPos);
/* 196 */     while (n != -1) {
/* 197 */       readCount += n;
/* 198 */       inBufferPos += n;
/* 199 */       this.count += n;
/* 200 */       if (inBufferPos == this.currentBuffer.length) {
/* 201 */         needNewBuffer(this.currentBuffer.length);
/* 202 */         inBufferPos = 0;
/*     */       } 
/* 204 */       n = in.read(this.currentBuffer, inBufferPos, this.currentBuffer.length - inBufferPos);
/*     */     } 
/* 206 */     return readCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int size() {
/* 214 */     return this.count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reset() {
/* 234 */     this.count = 0;
/* 235 */     this.filledBufferSum = 0;
/* 236 */     this.currentBufferIndex = 0;
/* 237 */     if (this.reuseBuffers) {
/* 238 */       this.currentBuffer = this.buffers.get(this.currentBufferIndex);
/*     */     } else {
/*     */       
/* 241 */       this.currentBuffer = null;
/* 242 */       int size = ((byte[])this.buffers.get(0)).length;
/* 243 */       this.buffers.clear();
/* 244 */       needNewBuffer(size);
/* 245 */       this.reuseBuffers = true;
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
/*     */   public synchronized void writeTo(OutputStream out) throws IOException {
/* 258 */     int remaining = this.count;
/* 259 */     for (byte[] buf : this.buffers) {
/* 260 */       int c = Math.min(buf.length, remaining);
/* 261 */       out.write(buf, 0, c);
/* 262 */       remaining -= c;
/* 263 */       if (remaining == 0) {
/*     */         break;
/*     */       }
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
/*     */   public static InputStream toBufferedInputStream(InputStream input) throws IOException {
/* 292 */     return toBufferedInputStream(input, 1024);
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
/*     */   public static InputStream toBufferedInputStream(InputStream input, int size) throws IOException {
/* 321 */     ByteArrayOutputStream output = new ByteArrayOutputStream(size);
/* 322 */     output.write(input);
/* 323 */     return output.toInputStream();
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
/*     */   public synchronized InputStream toInputStream() {
/* 337 */     int remaining = this.count;
/* 338 */     if (remaining == 0) {
/* 339 */       return (InputStream)new ClosedInputStream();
/*     */     }
/* 341 */     List<ByteArrayInputStream> list = new ArrayList<ByteArrayInputStream>(this.buffers.size());
/* 342 */     for (byte[] buf : this.buffers) {
/* 343 */       int c = Math.min(buf.length, remaining);
/* 344 */       list.add(new ByteArrayInputStream(buf, 0, c));
/* 345 */       remaining -= c;
/* 346 */       if (remaining == 0) {
/*     */         break;
/*     */       }
/*     */     } 
/* 350 */     this.reuseBuffers = false;
/* 351 */     return new SequenceInputStream(Collections.enumeration((Collection)list));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized byte[] toByteArray() {
/* 362 */     int remaining = this.count;
/* 363 */     if (remaining == 0) {
/* 364 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/* 366 */     byte[] newbuf = new byte[remaining];
/* 367 */     int pos = 0;
/* 368 */     for (byte[] buf : this.buffers) {
/* 369 */       int c = Math.min(buf.length, remaining);
/* 370 */       System.arraycopy(buf, 0, newbuf, pos, c);
/* 371 */       pos += c;
/* 372 */       remaining -= c;
/* 373 */       if (remaining == 0) {
/*     */         break;
/*     */       }
/*     */     } 
/* 377 */     return newbuf;
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
/*     */   @Deprecated
/*     */   public String toString() {
/* 391 */     return new String(toByteArray(), Charset.defaultCharset());
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
/*     */   public String toString(String enc) throws UnsupportedEncodingException {
/* 404 */     return new String(toByteArray(), enc);
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
/*     */   public String toString(Charset charset) {
/* 417 */     return new String(toByteArray(), charset);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\io\output\ByteArrayOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */