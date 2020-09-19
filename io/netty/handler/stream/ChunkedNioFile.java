/*     */ package io.netty.handler.stream;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.FileChannel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChunkedNioFile
/*     */   implements ChunkedInput<ByteBuf>
/*     */ {
/*     */   private final FileChannel in;
/*     */   private final long startOffset;
/*     */   private final long endOffset;
/*     */   private final int chunkSize;
/*     */   private long offset;
/*     */   
/*     */   public ChunkedNioFile(File in) throws IOException {
/*  48 */     this((new FileInputStream(in)).getChannel());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkedNioFile(File in, int chunkSize) throws IOException {
/*  58 */     this((new FileInputStream(in)).getChannel(), chunkSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkedNioFile(FileChannel in) throws IOException {
/*  65 */     this(in, 8192);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkedNioFile(FileChannel in, int chunkSize) throws IOException {
/*  75 */     this(in, 0L, in.size(), chunkSize);
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
/*     */   public ChunkedNioFile(FileChannel in, long offset, long length, int chunkSize) throws IOException {
/*  88 */     if (in == null) {
/*  89 */       throw new NullPointerException("in");
/*     */     }
/*  91 */     if (offset < 0L) {
/*  92 */       throw new IllegalArgumentException("offset: " + offset + " (expected: 0 or greater)");
/*     */     }
/*     */     
/*  95 */     if (length < 0L) {
/*  96 */       throw new IllegalArgumentException("length: " + length + " (expected: 0 or greater)");
/*     */     }
/*     */     
/*  99 */     if (chunkSize <= 0) {
/* 100 */       throw new IllegalArgumentException("chunkSize: " + chunkSize + " (expected: a positive integer)");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 105 */     if (offset != 0L) {
/* 106 */       in.position(offset);
/*     */     }
/* 108 */     this.in = in;
/* 109 */     this.chunkSize = chunkSize;
/* 110 */     this.offset = this.startOffset = offset;
/* 111 */     this.endOffset = offset + length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long startOffset() {
/* 118 */     return this.startOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long endOffset() {
/* 125 */     return this.endOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long currentOffset() {
/* 132 */     return this.offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndOfInput() throws Exception {
/* 137 */     return (this.offset >= this.endOffset || !this.in.isOpen());
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws Exception {
/* 142 */     this.in.close();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ByteBuf readChunk(ChannelHandlerContext ctx) throws Exception {
/* 148 */     return readChunk(ctx.alloc());
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf readChunk(ByteBufAllocator allocator) throws Exception {
/* 153 */     long offset = this.offset;
/* 154 */     if (offset >= this.endOffset) {
/* 155 */       return null;
/*     */     }
/*     */     
/* 158 */     int chunkSize = (int)Math.min(this.chunkSize, this.endOffset - offset);
/* 159 */     ByteBuf buffer = allocator.buffer(chunkSize);
/* 160 */     boolean release = true;
/*     */     try {
/* 162 */       int readBytes = 0;
/*     */       do {
/* 164 */         int localReadBytes = buffer.writeBytes(this.in, chunkSize - readBytes);
/* 165 */         if (localReadBytes < 0) {
/*     */           break;
/*     */         }
/* 168 */         readBytes += localReadBytes;
/* 169 */       } while (readBytes != chunkSize);
/*     */ 
/*     */ 
/*     */       
/* 173 */       this.offset += readBytes;
/* 174 */       release = false;
/* 175 */       return buffer;
/*     */     } finally {
/* 177 */       if (release) {
/* 178 */         buffer.release();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long length() {
/* 185 */     return this.endOffset - this.startOffset;
/*     */   }
/*     */ 
/*     */   
/*     */   public long progress() {
/* 190 */     return this.offset - this.startOffset;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\stream\ChunkedNioFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */