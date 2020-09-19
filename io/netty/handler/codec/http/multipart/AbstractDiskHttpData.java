/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.http.HttpConstants;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*     */ public abstract class AbstractDiskHttpData
/*     */   extends AbstractHttpData
/*     */ {
/*  40 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractDiskHttpData.class);
/*     */   
/*     */   private File file;
/*     */   private boolean isRenamed;
/*     */   private FileChannel fileChannel;
/*     */   
/*     */   protected AbstractDiskHttpData(String name, Charset charset, long size) {
/*  47 */     super(name, charset, size);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private File tempFile() throws IOException {
/*     */     String newpostfix;
/*     */     File tmpFile;
/*  81 */     String diskFilename = getDiskFilename();
/*  82 */     if (diskFilename != null) {
/*  83 */       newpostfix = '_' + diskFilename;
/*     */     } else {
/*  85 */       newpostfix = getPostfix();
/*     */     } 
/*     */     
/*  88 */     if (getBaseDirectory() == null) {
/*     */       
/*  90 */       tmpFile = File.createTempFile(getPrefix(), newpostfix);
/*     */     } else {
/*  92 */       tmpFile = File.createTempFile(getPrefix(), newpostfix, new File(
/*  93 */             getBaseDirectory()));
/*     */     } 
/*  95 */     if (deleteOnExit()) {
/*  96 */       tmpFile.deleteOnExit();
/*     */     }
/*  98 */     return tmpFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContent(ByteBuf buffer) throws IOException {
/* 103 */     if (buffer == null) {
/* 104 */       throw new NullPointerException("buffer");
/*     */     }
/*     */     try {
/* 107 */       this.size = buffer.readableBytes();
/* 108 */       checkSize(this.size);
/* 109 */       if (this.definedSize > 0L && this.definedSize < this.size) {
/* 110 */         throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
/*     */       }
/* 112 */       if (this.file == null) {
/* 113 */         this.file = tempFile();
/*     */       }
/* 115 */       if (buffer.readableBytes() == 0) {
/*     */         
/* 117 */         if (!this.file.createNewFile()) {
/* 118 */           if (this.file.length() == 0L) {
/*     */             return;
/*     */           }
/* 121 */           if (!this.file.delete() || !this.file.createNewFile()) {
/* 122 */             throw new IOException("file exists already: " + this.file);
/*     */           }
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/* 128 */       FileOutputStream outputStream = new FileOutputStream(this.file);
/*     */       try {
/* 130 */         FileChannel localfileChannel = outputStream.getChannel();
/* 131 */         ByteBuffer byteBuffer = buffer.nioBuffer();
/* 132 */         int written = 0;
/* 133 */         while (written < this.size) {
/* 134 */           written += localfileChannel.write(byteBuffer);
/*     */         }
/* 136 */         buffer.readerIndex(buffer.readerIndex() + written);
/* 137 */         localfileChannel.force(false);
/*     */       } finally {
/* 139 */         outputStream.close();
/*     */       } 
/* 141 */       setCompleted();
/*     */     }
/*     */     finally {
/*     */       
/* 145 */       buffer.release();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContent(ByteBuf buffer, boolean last) throws IOException {
/* 152 */     if (buffer != null) {
/*     */       try {
/* 154 */         int localsize = buffer.readableBytes();
/* 155 */         checkSize(this.size + localsize);
/* 156 */         if (this.definedSize > 0L && this.definedSize < this.size + localsize) {
/* 157 */           throw new IOException("Out of size: " + (this.size + localsize) + " > " + this.definedSize);
/*     */         }
/*     */         
/* 160 */         ByteBuffer byteBuffer = (buffer.nioBufferCount() == 1) ? buffer.nioBuffer() : buffer.copy().nioBuffer();
/* 161 */         int written = 0;
/* 162 */         if (this.file == null) {
/* 163 */           this.file = tempFile();
/*     */         }
/* 165 */         if (this.fileChannel == null) {
/* 166 */           FileOutputStream outputStream = new FileOutputStream(this.file);
/* 167 */           this.fileChannel = outputStream.getChannel();
/*     */         } 
/* 169 */         while (written < localsize) {
/* 170 */           written += this.fileChannel.write(byteBuffer);
/*     */         }
/* 172 */         this.size += localsize;
/* 173 */         buffer.readerIndex(buffer.readerIndex() + written);
/*     */       }
/*     */       finally {
/*     */         
/* 177 */         buffer.release();
/*     */       } 
/*     */     }
/* 180 */     if (last) {
/* 181 */       if (this.file == null) {
/* 182 */         this.file = tempFile();
/*     */       }
/* 184 */       if (this.fileChannel == null) {
/* 185 */         FileOutputStream outputStream = new FileOutputStream(this.file);
/* 186 */         this.fileChannel = outputStream.getChannel();
/*     */       } 
/* 188 */       this.fileChannel.force(false);
/* 189 */       this.fileChannel.close();
/* 190 */       this.fileChannel = null;
/* 191 */       setCompleted();
/*     */     }
/* 193 */     else if (buffer == null) {
/* 194 */       throw new NullPointerException("buffer");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContent(File file) throws IOException {
/* 201 */     if (this.file != null) {
/* 202 */       delete();
/*     */     }
/* 204 */     this.file = file;
/* 205 */     this.size = file.length();
/* 206 */     checkSize(this.size);
/* 207 */     this.isRenamed = true;
/* 208 */     setCompleted();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContent(InputStream inputStream) throws IOException {
/* 213 */     if (inputStream == null) {
/* 214 */       throw new NullPointerException("inputStream");
/*     */     }
/* 216 */     if (this.file != null) {
/* 217 */       delete();
/*     */     }
/* 219 */     this.file = tempFile();
/* 220 */     FileOutputStream outputStream = new FileOutputStream(this.file);
/* 221 */     int written = 0;
/*     */     try {
/* 223 */       FileChannel localfileChannel = outputStream.getChannel();
/* 224 */       byte[] bytes = new byte[16384];
/* 225 */       ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
/* 226 */       int read = inputStream.read(bytes);
/* 227 */       while (read > 0) {
/* 228 */         byteBuffer.position(read).flip();
/* 229 */         written += localfileChannel.write(byteBuffer);
/* 230 */         checkSize(written);
/* 231 */         read = inputStream.read(bytes);
/*     */       } 
/* 233 */       localfileChannel.force(false);
/*     */     } finally {
/* 235 */       outputStream.close();
/*     */     } 
/* 237 */     this.size = written;
/* 238 */     if (this.definedSize > 0L && this.definedSize < this.size) {
/* 239 */       if (!this.file.delete()) {
/* 240 */         logger.warn("Failed to delete: {}", this.file);
/*     */       }
/* 242 */       this.file = null;
/* 243 */       throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
/*     */     } 
/* 245 */     this.isRenamed = true;
/* 246 */     setCompleted();
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete() {
/* 251 */     if (this.fileChannel != null) {
/*     */       try {
/* 253 */         this.fileChannel.force(false);
/* 254 */         this.fileChannel.close();
/* 255 */       } catch (IOException e) {
/* 256 */         logger.warn("Failed to close a file.", e);
/*     */       } 
/* 258 */       this.fileChannel = null;
/*     */     } 
/* 260 */     if (!this.isRenamed) {
/* 261 */       if (this.file != null && this.file.exists() && 
/* 262 */         !this.file.delete()) {
/* 263 */         logger.warn("Failed to delete: {}", this.file);
/*     */       }
/*     */       
/* 266 */       this.file = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] get() throws IOException {
/* 272 */     if (this.file == null) {
/* 273 */       return EmptyArrays.EMPTY_BYTES;
/*     */     }
/* 275 */     return readFrom(this.file);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getByteBuf() throws IOException {
/* 280 */     if (this.file == null) {
/* 281 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/* 283 */     byte[] array = readFrom(this.file);
/* 284 */     return Unpooled.wrappedBuffer(array);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf getChunk(int length) throws IOException {
/* 289 */     if (this.file == null || length == 0) {
/* 290 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/* 292 */     if (this.fileChannel == null) {
/* 293 */       FileInputStream inputStream = new FileInputStream(this.file);
/* 294 */       this.fileChannel = inputStream.getChannel();
/*     */     } 
/* 296 */     int read = 0;
/* 297 */     ByteBuffer byteBuffer = ByteBuffer.allocate(length);
/* 298 */     while (read < length) {
/* 299 */       int readnow = this.fileChannel.read(byteBuffer);
/* 300 */       if (readnow == -1) {
/* 301 */         this.fileChannel.close();
/* 302 */         this.fileChannel = null;
/*     */         break;
/*     */       } 
/* 305 */       read += readnow;
/*     */     } 
/*     */     
/* 308 */     if (read == 0) {
/* 309 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/* 311 */     byteBuffer.flip();
/* 312 */     ByteBuf buffer = Unpooled.wrappedBuffer(byteBuffer);
/* 313 */     buffer.readerIndex(0);
/* 314 */     buffer.writerIndex(read);
/* 315 */     return buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString() throws IOException {
/* 320 */     return getString(HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString(Charset encoding) throws IOException {
/* 325 */     if (this.file == null) {
/* 326 */       return "";
/*     */     }
/* 328 */     if (encoding == null) {
/* 329 */       byte[] arrayOfByte = readFrom(this.file);
/* 330 */       return new String(arrayOfByte, HttpConstants.DEFAULT_CHARSET.name());
/*     */     } 
/* 332 */     byte[] array = readFrom(this.file);
/* 333 */     return new String(array, encoding.name());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInMemory() {
/* 338 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean renameTo(File dest) throws IOException {
/* 343 */     if (dest == null) {
/* 344 */       throw new NullPointerException("dest");
/*     */     }
/* 346 */     if (this.file == null) {
/* 347 */       throw new IOException("No file defined so cannot be renamed");
/*     */     }
/* 349 */     if (!this.file.renameTo(dest)) {
/*     */       
/* 351 */       IOException exception = null;
/* 352 */       FileInputStream inputStream = null;
/* 353 */       FileOutputStream outputStream = null;
/* 354 */       long chunkSize = 8196L;
/* 355 */       long position = 0L;
/*     */       try {
/* 357 */         inputStream = new FileInputStream(this.file);
/* 358 */         outputStream = new FileOutputStream(dest);
/* 359 */         FileChannel in = inputStream.getChannel();
/* 360 */         FileChannel out = outputStream.getChannel();
/* 361 */         while (position < this.size) {
/* 362 */           if (chunkSize < this.size - position) {
/* 363 */             chunkSize = this.size - position;
/*     */           }
/* 365 */           position += in.transferTo(position, chunkSize, out);
/*     */         } 
/* 367 */       } catch (IOException e) {
/* 368 */         exception = e;
/*     */       } finally {
/* 370 */         if (inputStream != null) {
/*     */           try {
/* 372 */             inputStream.close();
/* 373 */           } catch (IOException e) {
/* 374 */             if (exception == null) {
/* 375 */               exception = e;
/*     */             } else {
/* 377 */               logger.warn("Multiple exceptions detected, the following will be suppressed {}", e);
/*     */             } 
/*     */           } 
/*     */         }
/* 381 */         if (outputStream != null) {
/*     */           try {
/* 383 */             outputStream.close();
/* 384 */           } catch (IOException e) {
/* 385 */             if (exception == null) {
/* 386 */               exception = e;
/*     */             } else {
/* 388 */               logger.warn("Multiple exceptions detected, the following will be suppressed {}", e);
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/* 393 */       if (exception != null) {
/* 394 */         throw exception;
/*     */       }
/* 396 */       if (position == this.size) {
/* 397 */         if (!this.file.delete()) {
/* 398 */           logger.warn("Failed to delete: {}", this.file);
/*     */         }
/* 400 */         this.file = dest;
/* 401 */         this.isRenamed = true;
/* 402 */         return true;
/*     */       } 
/* 404 */       if (!dest.delete()) {
/* 405 */         logger.warn("Failed to delete: {}", dest);
/*     */       }
/* 407 */       return false;
/*     */     } 
/*     */     
/* 410 */     this.file = dest;
/* 411 */     this.isRenamed = true;
/* 412 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] readFrom(File src) throws IOException {
/* 420 */     long srcsize = src.length();
/* 421 */     if (srcsize > 2147483647L) {
/* 422 */       throw new IllegalArgumentException("File too big to be loaded in memory");
/*     */     }
/*     */     
/* 425 */     FileInputStream inputStream = new FileInputStream(src);
/* 426 */     byte[] array = new byte[(int)srcsize];
/*     */     try {
/* 428 */       FileChannel fileChannel = inputStream.getChannel();
/* 429 */       ByteBuffer byteBuffer = ByteBuffer.wrap(array);
/* 430 */       int read = 0;
/* 431 */       while (read < srcsize) {
/* 432 */         read += fileChannel.read(byteBuffer);
/*     */       }
/*     */     } finally {
/* 435 */       inputStream.close();
/*     */     } 
/* 437 */     return array;
/*     */   }
/*     */ 
/*     */   
/*     */   public File getFile() throws IOException {
/* 442 */     return this.file;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpData touch() {
/* 447 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpData touch(Object hint) {
/* 452 */     return this;
/*     */   }
/*     */   
/*     */   protected abstract String getDiskFilename();
/*     */   
/*     */   protected abstract String getPrefix();
/*     */   
/*     */   protected abstract String getBaseDirectory();
/*     */   
/*     */   protected abstract String getPostfix();
/*     */   
/*     */   protected abstract boolean deleteOnExit();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\multipart\AbstractDiskHttpData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */