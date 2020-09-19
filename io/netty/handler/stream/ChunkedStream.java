/*     */ package io.netty.handler.stream;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import java.io.InputStream;
/*     */ import java.io.PushbackInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChunkedStream
/*     */   implements ChunkedInput<ByteBuf>
/*     */ {
/*     */   static final int DEFAULT_CHUNK_SIZE = 8192;
/*     */   private final PushbackInputStream in;
/*     */   private final int chunkSize;
/*     */   private long offset;
/*     */   private boolean closed;
/*     */   
/*     */   public ChunkedStream(InputStream in) {
/*  48 */     this(in, 8192);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkedStream(InputStream in, int chunkSize) {
/*  58 */     if (in == null) {
/*  59 */       throw new NullPointerException("in");
/*     */     }
/*  61 */     if (chunkSize <= 0) {
/*  62 */       throw new IllegalArgumentException("chunkSize: " + chunkSize + " (expected: a positive integer)");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  67 */     if (in instanceof PushbackInputStream) {
/*  68 */       this.in = (PushbackInputStream)in;
/*     */     } else {
/*  70 */       this.in = new PushbackInputStream(in);
/*     */     } 
/*  72 */     this.chunkSize = chunkSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long transferredBytes() {
/*  79 */     return this.offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndOfInput() throws Exception {
/*  84 */     if (this.closed) {
/*  85 */       return true;
/*     */     }
/*     */     
/*  88 */     int b = this.in.read();
/*  89 */     if (b < 0) {
/*  90 */       return true;
/*     */     }
/*  92 */     this.in.unread(b);
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws Exception {
/*  99 */     this.closed = true;
/* 100 */     this.in.close();
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ByteBuf readChunk(ChannelHandlerContext ctx) throws Exception {
/* 106 */     return readChunk(ctx.alloc());
/*     */   }
/*     */   
/*     */   public ByteBuf readChunk(ByteBufAllocator allocator) throws Exception {
/*     */     int chunkSize;
/* 111 */     if (isEndOfInput()) {
/* 112 */       return null;
/*     */     }
/*     */     
/* 115 */     int availableBytes = this.in.available();
/*     */     
/* 117 */     if (availableBytes <= 0) {
/* 118 */       chunkSize = this.chunkSize;
/*     */     } else {
/* 120 */       chunkSize = Math.min(this.chunkSize, this.in.available());
/*     */     } 
/*     */     
/* 123 */     boolean release = true;
/* 124 */     ByteBuf buffer = allocator.buffer(chunkSize);
/*     */     
/*     */     try {
/* 127 */       this.offset += buffer.writeBytes(this.in, chunkSize);
/* 128 */       release = false;
/* 129 */       return buffer;
/*     */     } finally {
/* 131 */       if (release) {
/* 132 */         buffer.release();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long length() {
/* 139 */     return -1L;
/*     */   }
/*     */ 
/*     */   
/*     */   public long progress() {
/* 144 */     return this.offset;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\stream\ChunkedStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */