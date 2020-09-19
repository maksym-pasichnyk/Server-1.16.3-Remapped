/*     */ package io.netty.buffer;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteBufInputStream
/*     */   extends InputStream
/*     */   implements DataInput
/*     */ {
/*     */   private final ByteBuf buffer;
/*     */   private final int startIndex;
/*     */   private final int endIndex;
/*     */   private boolean closed;
/*     */   private final boolean releaseOnClose;
/*     */   
/*     */   public ByteBufInputStream(ByteBuf buffer) {
/*  62 */     this(buffer, buffer.readableBytes());
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
/*     */   public ByteBufInputStream(ByteBuf buffer, int length) {
/*  76 */     this(buffer, length, false);
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
/*     */   public ByteBufInputStream(ByteBuf buffer, boolean releaseOnClose) {
/*  88 */     this(buffer, buffer.readableBytes(), releaseOnClose);
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
/*     */   public ByteBufInputStream(ByteBuf buffer, int length, boolean releaseOnClose) {
/* 104 */     if (buffer == null) {
/* 105 */       throw new NullPointerException("buffer");
/*     */     }
/* 107 */     if (length < 0) {
/* 108 */       if (releaseOnClose) {
/* 109 */         buffer.release();
/*     */       }
/* 111 */       throw new IllegalArgumentException("length: " + length);
/*     */     } 
/* 113 */     if (length > buffer.readableBytes()) {
/* 114 */       if (releaseOnClose) {
/* 115 */         buffer.release();
/*     */       }
/* 117 */       throw new IndexOutOfBoundsException("Too many bytes to be read - Needs " + length + ", maximum is " + buffer
/* 118 */           .readableBytes());
/*     */     } 
/*     */     
/* 121 */     this.releaseOnClose = releaseOnClose;
/* 122 */     this.buffer = buffer;
/* 123 */     this.startIndex = buffer.readerIndex();
/* 124 */     this.endIndex = this.startIndex + length;
/* 125 */     buffer.markReaderIndex();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int readBytes() {
/* 132 */     return this.buffer.readerIndex() - this.startIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 138 */       super.close();
/*     */     } finally {
/*     */       
/* 141 */       if (this.releaseOnClose && !this.closed) {
/* 142 */         this.closed = true;
/* 143 */         this.buffer.release();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 150 */     return this.endIndex - this.buffer.readerIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public void mark(int readlimit) {
/* 155 */     this.buffer.markReaderIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 160 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 165 */     if (!this.buffer.isReadable()) {
/* 166 */       return -1;
/*     */     }
/* 168 */     return this.buffer.readByte() & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 173 */     int available = available();
/* 174 */     if (available == 0) {
/* 175 */       return -1;
/*     */     }
/*     */     
/* 178 */     len = Math.min(available, len);
/* 179 */     this.buffer.readBytes(b, off, len);
/* 180 */     return len;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() throws IOException {
/* 185 */     this.buffer.resetReaderIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 190 */     if (n > 2147483647L) {
/* 191 */       return skipBytes(2147483647);
/*     */     }
/* 193 */     return skipBytes((int)n);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean readBoolean() throws IOException {
/* 199 */     checkAvailable(1);
/* 200 */     return (read() != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte readByte() throws IOException {
/* 205 */     if (!this.buffer.isReadable()) {
/* 206 */       throw new EOFException();
/*     */     }
/* 208 */     return this.buffer.readByte();
/*     */   }
/*     */ 
/*     */   
/*     */   public char readChar() throws IOException {
/* 213 */     return (char)readShort();
/*     */   }
/*     */ 
/*     */   
/*     */   public double readDouble() throws IOException {
/* 218 */     return Double.longBitsToDouble(readLong());
/*     */   }
/*     */ 
/*     */   
/*     */   public float readFloat() throws IOException {
/* 223 */     return Float.intBitsToFloat(readInt());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFully(byte[] b) throws IOException {
/* 228 */     readFully(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFully(byte[] b, int off, int len) throws IOException {
/* 233 */     checkAvailable(len);
/* 234 */     this.buffer.readBytes(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public int readInt() throws IOException {
/* 239 */     checkAvailable(4);
/* 240 */     return this.buffer.readInt();
/*     */   }
/*     */   
/* 243 */   private final StringBuilder lineBuf = new StringBuilder();
/*     */ 
/*     */   
/*     */   public String readLine() throws IOException {
/* 247 */     this.lineBuf.setLength(0);
/*     */     
/*     */     while (true) {
/* 250 */       if (!this.buffer.isReadable()) {
/* 251 */         return (this.lineBuf.length() > 0) ? this.lineBuf.toString() : null;
/*     */       }
/*     */       
/* 254 */       int c = this.buffer.readUnsignedByte();
/* 255 */       switch (c) {
/*     */         case 10:
/*     */           break;
/*     */         
/*     */         case 13:
/* 260 */           if (this.buffer.isReadable() && (char)this.buffer.getUnsignedByte(this.buffer.readerIndex()) == '\n') {
/* 261 */             this.buffer.skipBytes(1);
/*     */           }
/*     */           break;
/*     */       } 
/*     */       
/* 266 */       this.lineBuf.append((char)c);
/*     */     } 
/*     */ 
/*     */     
/* 270 */     return this.lineBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public long readLong() throws IOException {
/* 275 */     checkAvailable(8);
/* 276 */     return this.buffer.readLong();
/*     */   }
/*     */ 
/*     */   
/*     */   public short readShort() throws IOException {
/* 281 */     checkAvailable(2);
/* 282 */     return this.buffer.readShort();
/*     */   }
/*     */ 
/*     */   
/*     */   public String readUTF() throws IOException {
/* 287 */     return DataInputStream.readUTF(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int readUnsignedByte() throws IOException {
/* 292 */     return readByte() & 0xFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public int readUnsignedShort() throws IOException {
/* 297 */     return readShort() & 0xFFFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public int skipBytes(int n) throws IOException {
/* 302 */     int nBytes = Math.min(available(), n);
/* 303 */     this.buffer.skipBytes(nBytes);
/* 304 */     return nBytes;
/*     */   }
/*     */   
/*     */   private void checkAvailable(int fieldSize) throws IOException {
/* 308 */     if (fieldSize < 0) {
/* 309 */       throw new IndexOutOfBoundsException("fieldSize cannot be a negative number");
/*     */     }
/* 311 */     if (fieldSize > available())
/* 312 */       throw new EOFException("fieldSize is too long! Length is " + fieldSize + ", but maximum is " + 
/* 313 */           available()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\ByteBufInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */