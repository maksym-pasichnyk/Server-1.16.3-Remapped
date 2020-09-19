/*     */ package io.netty.handler.stream;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChunkedNioStream
/*     */   implements ChunkedInput<ByteBuf>
/*     */ {
/*     */   private final ReadableByteChannel in;
/*     */   private final int chunkSize;
/*     */   private long offset;
/*     */   private final ByteBuffer byteBuffer;
/*     */   
/*     */   public ChunkedNioStream(ReadableByteChannel in) {
/*  46 */     this(in, 8192);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkedNioStream(ReadableByteChannel in, int chunkSize) {
/*  56 */     if (in == null) {
/*  57 */       throw new NullPointerException("in");
/*     */     }
/*  59 */     if (chunkSize <= 0) {
/*  60 */       throw new IllegalArgumentException("chunkSize: " + chunkSize + " (expected: a positive integer)");
/*     */     }
/*     */     
/*  63 */     this.in = in;
/*  64 */     this.offset = 0L;
/*  65 */     this.chunkSize = chunkSize;
/*  66 */     this.byteBuffer = ByteBuffer.allocate(chunkSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long transferredBytes() {
/*  73 */     return this.offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndOfInput() throws Exception {
/*  78 */     if (this.byteBuffer.position() > 0)
/*     */     {
/*  80 */       return false;
/*     */     }
/*  82 */     if (this.in.isOpen()) {
/*     */       
/*  84 */       int b = this.in.read(this.byteBuffer);
/*  85 */       if (b < 0) {
/*  86 */         return true;
/*     */       }
/*  88 */       this.offset += b;
/*  89 */       return false;
/*     */     } 
/*     */     
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws Exception {
/*  97 */     this.in.close();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ByteBuf readChunk(ChannelHandlerContext ctx) throws Exception {
/* 103 */     return readChunk(ctx.alloc());
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf readChunk(ByteBufAllocator allocator) throws Exception {
/* 108 */     if (isEndOfInput()) {
/* 109 */       return null;
/*     */     }
/*     */     
/* 112 */     int readBytes = this.byteBuffer.position();
/*     */     do {
/* 114 */       int localReadBytes = this.in.read(this.byteBuffer);
/* 115 */       if (localReadBytes < 0) {
/*     */         break;
/*     */       }
/* 118 */       readBytes += localReadBytes;
/* 119 */       this.offset += localReadBytes;
/* 120 */     } while (readBytes != this.chunkSize);
/*     */ 
/*     */ 
/*     */     
/* 124 */     this.byteBuffer.flip();
/* 125 */     boolean release = true;
/* 126 */     ByteBuf buffer = allocator.buffer(this.byteBuffer.remaining());
/*     */     try {
/* 128 */       buffer.writeBytes(this.byteBuffer);
/* 129 */       this.byteBuffer.clear();
/* 130 */       release = false;
/* 131 */       return buffer;
/*     */     } finally {
/* 133 */       if (release) {
/* 134 */         buffer.release();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long length() {
/* 141 */     return -1L;
/*     */   }
/*     */ 
/*     */   
/*     */   public long progress() {
/* 146 */     return this.offset;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\stream\ChunkedNioStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */