/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.http.HttpConstants;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
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
/*     */ public abstract class AbstractMemoryHttpData
/*     */   extends AbstractHttpData
/*     */ {
/*     */   private ByteBuf byteBuf;
/*     */   private int chunkPosition;
/*     */   
/*     */   protected AbstractMemoryHttpData(String name, Charset charset, long size) {
/*  45 */     super(name, charset, size);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContent(ByteBuf buffer) throws IOException {
/*  50 */     if (buffer == null) {
/*  51 */       throw new NullPointerException("buffer");
/*     */     }
/*  53 */     long localsize = buffer.readableBytes();
/*  54 */     checkSize(localsize);
/*  55 */     if (this.definedSize > 0L && this.definedSize < localsize) {
/*  56 */       throw new IOException("Out of size: " + localsize + " > " + this.definedSize);
/*     */     }
/*     */     
/*  59 */     if (this.byteBuf != null) {
/*  60 */       this.byteBuf.release();
/*     */     }
/*  62 */     this.byteBuf = buffer;
/*  63 */     this.size = localsize;
/*  64 */     setCompleted();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContent(InputStream inputStream) throws IOException {
/*  69 */     if (inputStream == null) {
/*  70 */       throw new NullPointerException("inputStream");
/*     */     }
/*  72 */     ByteBuf buffer = Unpooled.buffer();
/*  73 */     byte[] bytes = new byte[16384];
/*  74 */     int read = inputStream.read(bytes);
/*  75 */     int written = 0;
/*  76 */     while (read > 0) {
/*  77 */       buffer.writeBytes(bytes, 0, read);
/*  78 */       written += read;
/*  79 */       checkSize(written);
/*  80 */       read = inputStream.read(bytes);
/*     */     } 
/*  82 */     this.size = written;
/*  83 */     if (this.definedSize > 0L && this.definedSize < this.size) {
/*  84 */       throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
/*     */     }
/*  86 */     if (this.byteBuf != null) {
/*  87 */       this.byteBuf.release();
/*     */     }
/*  89 */     this.byteBuf = buffer;
/*  90 */     setCompleted();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContent(ByteBuf buffer, boolean last) throws IOException {
/*  96 */     if (buffer != null) {
/*  97 */       long localsize = buffer.readableBytes();
/*  98 */       checkSize(this.size + localsize);
/*  99 */       if (this.definedSize > 0L && this.definedSize < this.size + localsize) {
/* 100 */         throw new IOException("Out of size: " + (this.size + localsize) + " > " + this.definedSize);
/*     */       }
/*     */       
/* 103 */       this.size += localsize;
/* 104 */       if (this.byteBuf == null) {
/* 105 */         this.byteBuf = buffer;
/* 106 */       } else if (this.byteBuf instanceof CompositeByteBuf) {
/* 107 */         CompositeByteBuf cbb = (CompositeByteBuf)this.byteBuf;
/* 108 */         cbb.addComponent(true, buffer);
/*     */       } else {
/* 110 */         CompositeByteBuf cbb = Unpooled.compositeBuffer(2147483647);
/* 111 */         cbb.addComponents(true, new ByteBuf[] { this.byteBuf, buffer });
/* 112 */         this.byteBuf = (ByteBuf)cbb;
/*     */       } 
/*     */     } 
/* 115 */     if (last) {
/* 116 */       setCompleted();
/*     */     }
/* 118 */     else if (buffer == null) {
/* 119 */       throw new NullPointerException("buffer");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContent(File file) throws IOException {
/* 126 */     if (file == null) {
/* 127 */       throw new NullPointerException("file");
/*     */     }
/* 129 */     long newsize = file.length();
/* 130 */     if (newsize > 2147483647L) {
/* 131 */       throw new IllegalArgumentException("File too big to be loaded in memory");
/*     */     }
/*     */     
/* 134 */     checkSize(newsize);
/* 135 */     FileInputStream inputStream = new FileInputStream(file);
/* 136 */     FileChannel fileChannel = inputStream.getChannel();
/* 137 */     byte[] array = new byte[(int)newsize];
/* 138 */     ByteBuffer byteBuffer = ByteBuffer.wrap(array);
/* 139 */     int read = 0;
/* 140 */     while (read < newsize) {
/* 141 */       read += fileChannel.read(byteBuffer);
/*     */     }
/* 143 */     fileChannel.close();
/* 144 */     inputStream.close();
/* 145 */     byteBuffer.flip();
/* 146 */     if (this.byteBuf != null) {
/* 147 */       this.byteBuf.release();
/*     */     }
/* 149 */     this.byteBuf = Unpooled.wrappedBuffer(2147483647, new ByteBuffer[] { byteBuffer });
/* 150 */     this.size = newsize;
/* 151 */     setCompleted();
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete() {
/* 156 */     if (this.byteBuf != null) {
/* 157 */       this.byteBuf.release();
/* 158 */       this.byteBuf = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] get() {
/* 164 */     if (this.byteBuf == null) {
/* 165 */       return Unpooled.EMPTY_BUFFER.array();
/*     */     }
/* 167 */     byte[] array = new byte[this.byteBuf.readableBytes()];
/* 168 */     this.byteBuf.getBytes(this.byteBuf.readerIndex(), array);
/* 169 */     return array;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString() {
/* 174 */     return getString(HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString(Charset encoding) {
/* 179 */     if (this.byteBuf == null) {
/* 180 */       return "";
/*     */     }
/* 182 */     if (encoding == null) {
/* 183 */       encoding = HttpConstants.DEFAULT_CHARSET;
/*     */     }
/* 185 */     return this.byteBuf.toString(encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuf getByteBuf() {
/* 195 */     return this.byteBuf;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getChunk(int length) throws IOException {
/* 200 */     if (this.byteBuf == null || length == 0 || this.byteBuf.readableBytes() == 0) {
/* 201 */       this.chunkPosition = 0;
/* 202 */       return Unpooled.EMPTY_BUFFER;
/*     */     } 
/* 204 */     int sizeLeft = this.byteBuf.readableBytes() - this.chunkPosition;
/* 205 */     if (sizeLeft == 0) {
/* 206 */       this.chunkPosition = 0;
/* 207 */       return Unpooled.EMPTY_BUFFER;
/*     */     } 
/* 209 */     int sliceLength = length;
/* 210 */     if (sizeLeft < length) {
/* 211 */       sliceLength = sizeLeft;
/*     */     }
/* 213 */     ByteBuf chunk = this.byteBuf.retainedSlice(this.chunkPosition, sliceLength);
/* 214 */     this.chunkPosition += sliceLength;
/* 215 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInMemory() {
/* 220 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean renameTo(File dest) throws IOException {
/* 225 */     if (dest == null) {
/* 226 */       throw new NullPointerException("dest");
/*     */     }
/* 228 */     if (this.byteBuf == null) {
/*     */       
/* 230 */       if (!dest.createNewFile()) {
/* 231 */         throw new IOException("file exists already: " + dest);
/*     */       }
/* 233 */       return true;
/*     */     } 
/* 235 */     int length = this.byteBuf.readableBytes();
/* 236 */     FileOutputStream outputStream = new FileOutputStream(dest);
/* 237 */     FileChannel fileChannel = outputStream.getChannel();
/* 238 */     int written = 0;
/* 239 */     if (this.byteBuf.nioBufferCount() == 1) {
/* 240 */       ByteBuffer byteBuffer = this.byteBuf.nioBuffer();
/* 241 */       while (written < length) {
/* 242 */         written += fileChannel.write(byteBuffer);
/*     */       }
/*     */     } else {
/* 245 */       ByteBuffer[] byteBuffers = this.byteBuf.nioBuffers();
/* 246 */       while (written < length) {
/* 247 */         written = (int)(written + fileChannel.write(byteBuffers));
/*     */       }
/*     */     } 
/*     */     
/* 251 */     fileChannel.force(false);
/* 252 */     fileChannel.close();
/* 253 */     outputStream.close();
/* 254 */     return (written == length);
/*     */   }
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/* 259 */     throw new IOException("Not represented by a file");
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpData touch() {
/* 264 */     return touch((Object)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpData touch(Object hint) {
/* 269 */     if (this.byteBuf != null) {
/* 270 */       this.byteBuf.touch(hint);
/*     */     }
/* 272 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\multipart\AbstractMemoryHttpData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */