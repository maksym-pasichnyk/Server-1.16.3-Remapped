/*     */ package io.netty.handler.stream;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChunkedFile
/*     */   implements ChunkedInput<ByteBuf>
/*     */ {
/*     */   private final RandomAccessFile file;
/*     */   private final long startOffset;
/*     */   private final long endOffset;
/*     */   private final int chunkSize;
/*     */   private long offset;
/*     */   
/*     */   public ChunkedFile(File file) throws IOException {
/*  46 */     this(file, 8192);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkedFile(File file, int chunkSize) throws IOException {
/*  56 */     this(new RandomAccessFile(file, "r"), chunkSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkedFile(RandomAccessFile file) throws IOException {
/*  63 */     this(file, 8192);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkedFile(RandomAccessFile file, int chunkSize) throws IOException {
/*  73 */     this(file, 0L, file.length(), chunkSize);
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
/*     */   public ChunkedFile(RandomAccessFile file, long offset, long length, int chunkSize) throws IOException {
/*  85 */     if (file == null) {
/*  86 */       throw new NullPointerException("file");
/*     */     }
/*  88 */     if (offset < 0L) {
/*  89 */       throw new IllegalArgumentException("offset: " + offset + " (expected: 0 or greater)");
/*     */     }
/*     */     
/*  92 */     if (length < 0L) {
/*  93 */       throw new IllegalArgumentException("length: " + length + " (expected: 0 or greater)");
/*     */     }
/*     */     
/*  96 */     if (chunkSize <= 0) {
/*  97 */       throw new IllegalArgumentException("chunkSize: " + chunkSize + " (expected: a positive integer)");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 102 */     this.file = file;
/* 103 */     this.offset = this.startOffset = offset;
/* 104 */     this.endOffset = offset + length;
/* 105 */     this.chunkSize = chunkSize;
/*     */     
/* 107 */     file.seek(offset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long startOffset() {
/* 114 */     return this.startOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long endOffset() {
/* 121 */     return this.endOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long currentOffset() {
/* 128 */     return this.offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndOfInput() throws Exception {
/* 133 */     return (this.offset >= this.endOffset || !this.file.getChannel().isOpen());
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws Exception {
/* 138 */     this.file.close();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ByteBuf readChunk(ChannelHandlerContext ctx) throws Exception {
/* 144 */     return readChunk(ctx.alloc());
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf readChunk(ByteBufAllocator allocator) throws Exception {
/* 149 */     long offset = this.offset;
/* 150 */     if (offset >= this.endOffset) {
/* 151 */       return null;
/*     */     }
/*     */     
/* 154 */     int chunkSize = (int)Math.min(this.chunkSize, this.endOffset - offset);
/*     */ 
/*     */     
/* 157 */     ByteBuf buf = allocator.heapBuffer(chunkSize);
/* 158 */     boolean release = true;
/*     */     try {
/* 160 */       this.file.readFully(buf.array(), buf.arrayOffset(), chunkSize);
/* 161 */       buf.writerIndex(chunkSize);
/* 162 */       this.offset = offset + chunkSize;
/* 163 */       release = false;
/* 164 */       return buf;
/*     */     } finally {
/* 166 */       if (release) {
/* 167 */         buf.release();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long length() {
/* 174 */     return this.endOffset - this.startOffset;
/*     */   }
/*     */ 
/*     */   
/*     */   public long progress() {
/* 179 */     return this.offset - this.startOffset;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\stream\ChunkedFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */