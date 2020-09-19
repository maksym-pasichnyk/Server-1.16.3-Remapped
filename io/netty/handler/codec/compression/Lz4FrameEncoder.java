/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.ChannelPromiseNotifier;
/*     */ import io.netty.handler.codec.EncoderException;
/*     */ import io.netty.handler.codec.MessageToByteEncoder;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.zip.Checksum;
/*     */ import net.jpountz.lz4.LZ4Compressor;
/*     */ import net.jpountz.lz4.LZ4Exception;
/*     */ import net.jpountz.lz4.LZ4Factory;
/*     */ import net.jpountz.xxhash.XXHashFactory;
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
/*     */ 
/*     */ 
/*     */ public class Lz4FrameEncoder
/*     */   extends MessageToByteEncoder<ByteBuf>
/*     */ {
/*  72 */   private static final EncoderException ENCODE_FINSHED_EXCEPTION = (EncoderException)ThrowableUtil.unknownStackTrace((Throwable)new EncoderException(new IllegalStateException("encode finished and not enough space to write remaining data")), Lz4FrameEncoder.class, "encode");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int DEFAULT_MAX_ENCODE_SIZE = 2147483647;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int blockSize;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final LZ4Compressor compressor;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ByteBufChecksum checksum;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int compressionLevel;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteBuf buffer;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int maxEncodeSize;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean finished;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile ChannelHandlerContext ctx;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Lz4FrameEncoder() {
/* 120 */     this(false);
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
/*     */   public Lz4FrameEncoder(boolean highCompressor) {
/* 132 */     this(LZ4Factory.fastestInstance(), highCompressor, 65536, 
/* 133 */         XXHashFactory.fastestInstance().newStreamingHash32(-1756908916).asChecksum());
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
/*     */   public Lz4FrameEncoder(LZ4Factory factory, boolean highCompressor, int blockSize, Checksum checksum) {
/* 149 */     this(factory, highCompressor, blockSize, checksum, 2147483647);
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
/*     */   public Lz4FrameEncoder(LZ4Factory factory, boolean highCompressor, int blockSize, Checksum checksum, int maxEncodeSize) {
/* 167 */     if (factory == null) {
/* 168 */       throw new NullPointerException("factory");
/*     */     }
/* 170 */     if (checksum == null) {
/* 171 */       throw new NullPointerException("checksum");
/*     */     }
/*     */     
/* 174 */     this.compressor = highCompressor ? factory.highCompressor() : factory.fastCompressor();
/* 175 */     this.checksum = ByteBufChecksum.wrapChecksum(checksum);
/*     */     
/* 177 */     this.compressionLevel = compressionLevel(blockSize);
/* 178 */     this.blockSize = blockSize;
/* 179 */     this.maxEncodeSize = ObjectUtil.checkPositive(maxEncodeSize, "maxEncodeSize");
/* 180 */     this.finished = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int compressionLevel(int blockSize) {
/* 187 */     if (blockSize < 64 || blockSize > 33554432)
/* 188 */       throw new IllegalArgumentException(String.format("blockSize: %d (expected: %d-%d)", new Object[] {
/* 189 */               Integer.valueOf(blockSize), Integer.valueOf(64), Integer.valueOf(33554432)
/*     */             })); 
/* 191 */     int compressionLevel = 32 - Integer.numberOfLeadingZeros(blockSize - 1);
/* 192 */     compressionLevel = Math.max(0, compressionLevel - 10);
/* 193 */     return compressionLevel;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, ByteBuf msg, boolean preferDirect) {
/* 198 */     return allocateBuffer(ctx, msg, preferDirect, true);
/*     */   }
/*     */ 
/*     */   
/*     */   private ByteBuf allocateBuffer(ChannelHandlerContext ctx, ByteBuf msg, boolean preferDirect, boolean allowEmptyReturn) {
/* 203 */     int targetBufSize = 0;
/* 204 */     int remaining = msg.readableBytes() + this.buffer.readableBytes();
/*     */ 
/*     */     
/* 207 */     if (remaining < 0) {
/* 208 */       throw new EncoderException("too much data to allocate a buffer for compression");
/*     */     }
/*     */     
/* 211 */     while (remaining > 0) {
/* 212 */       int curSize = Math.min(this.blockSize, remaining);
/* 213 */       remaining -= curSize;
/*     */       
/* 215 */       targetBufSize += this.compressor.maxCompressedLength(curSize) + 21;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 221 */     if (targetBufSize > this.maxEncodeSize || 0 > targetBufSize) {
/* 222 */       throw new EncoderException(String.format("requested encode buffer size (%d bytes) exceeds the maximum allowable size (%d bytes)", new Object[] {
/* 223 */               Integer.valueOf(targetBufSize), Integer.valueOf(this.maxEncodeSize)
/*     */             }));
/*     */     }
/* 226 */     if (allowEmptyReturn && targetBufSize < this.blockSize) {
/* 227 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/*     */     
/* 230 */     if (preferDirect) {
/* 231 */       return ctx.alloc().ioBuffer(targetBufSize, targetBufSize);
/*     */     }
/* 233 */     return ctx.alloc().heapBuffer(targetBufSize, targetBufSize);
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
/*     */   protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
/* 246 */     if (this.finished) {
/* 247 */       if (!out.isWritable(in.readableBytes()))
/*     */       {
/* 249 */         throw ENCODE_FINSHED_EXCEPTION;
/*     */       }
/* 251 */       out.writeBytes(in);
/*     */       
/*     */       return;
/*     */     } 
/* 255 */     ByteBuf buffer = this.buffer;
/*     */     int length;
/* 257 */     while ((length = in.readableBytes()) > 0) {
/* 258 */       int nextChunkSize = Math.min(length, buffer.writableBytes());
/* 259 */       in.readBytes(buffer, nextChunkSize);
/*     */       
/* 261 */       if (!buffer.isWritable()) {
/* 262 */         flushBufferedData(out);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void flushBufferedData(ByteBuf out) {
/* 268 */     int compressedLength, blockType, flushableBytes = this.buffer.readableBytes();
/* 269 */     if (flushableBytes == 0) {
/*     */       return;
/*     */     }
/* 272 */     this.checksum.reset();
/* 273 */     this.checksum.update(this.buffer, this.buffer.readerIndex(), flushableBytes);
/* 274 */     int check = (int)this.checksum.getValue();
/*     */     
/* 276 */     int bufSize = this.compressor.maxCompressedLength(flushableBytes) + 21;
/* 277 */     out.ensureWritable(bufSize);
/* 278 */     int idx = out.writerIndex();
/*     */     
/*     */     try {
/* 281 */       ByteBuffer outNioBuffer = out.internalNioBuffer(idx + 21, out.writableBytes() - 21);
/* 282 */       int pos = outNioBuffer.position();
/*     */       
/* 284 */       this.compressor.compress(this.buffer.internalNioBuffer(this.buffer.readerIndex(), flushableBytes), outNioBuffer);
/* 285 */       compressedLength = outNioBuffer.position() - pos;
/* 286 */     } catch (LZ4Exception e) {
/* 287 */       throw new CompressionException(e);
/*     */     } 
/*     */     
/* 290 */     if (compressedLength >= flushableBytes) {
/* 291 */       blockType = 16;
/* 292 */       compressedLength = flushableBytes;
/* 293 */       out.setBytes(idx + 21, this.buffer, 0, flushableBytes);
/*     */     } else {
/* 295 */       blockType = 32;
/*     */     } 
/*     */     
/* 298 */     out.setLong(idx, 5501767354678207339L);
/* 299 */     out.setByte(idx + 8, (byte)(blockType | this.compressionLevel));
/* 300 */     out.setIntLE(idx + 9, compressedLength);
/* 301 */     out.setIntLE(idx + 13, flushableBytes);
/* 302 */     out.setIntLE(idx + 17, check);
/* 303 */     out.writerIndex(idx + 21 + compressedLength);
/* 304 */     this.buffer.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush(ChannelHandlerContext ctx) throws Exception {
/* 309 */     if (this.buffer != null && this.buffer.isReadable()) {
/* 310 */       ByteBuf buf = allocateBuffer(ctx, Unpooled.EMPTY_BUFFER, isPreferDirect(), false);
/* 311 */       flushBufferedData(buf);
/* 312 */       ctx.write(buf);
/*     */     } 
/* 314 */     ctx.flush();
/*     */   }
/*     */   
/*     */   private ChannelFuture finishEncode(ChannelHandlerContext ctx, ChannelPromise promise) {
/* 318 */     if (this.finished) {
/* 319 */       promise.setSuccess();
/* 320 */       return (ChannelFuture)promise;
/*     */     } 
/* 322 */     this.finished = true;
/*     */     
/* 324 */     ByteBuf footer = ctx.alloc().heapBuffer(this.compressor
/* 325 */         .maxCompressedLength(this.buffer.readableBytes()) + 21);
/* 326 */     flushBufferedData(footer);
/*     */     
/* 328 */     int idx = footer.writerIndex();
/* 329 */     footer.setLong(idx, 5501767354678207339L);
/* 330 */     footer.setByte(idx + 8, (byte)(0x10 | this.compressionLevel));
/* 331 */     footer.setInt(idx + 9, 0);
/* 332 */     footer.setInt(idx + 13, 0);
/* 333 */     footer.setInt(idx + 17, 0);
/*     */     
/* 335 */     footer.writerIndex(idx + 21);
/*     */     
/* 337 */     return ctx.writeAndFlush(footer, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 344 */     return this.finished;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture close() {
/* 353 */     return close(ctx().newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture close(final ChannelPromise promise) {
/* 362 */     ChannelHandlerContext ctx = ctx();
/* 363 */     EventExecutor executor = ctx.executor();
/* 364 */     if (executor.inEventLoop()) {
/* 365 */       return finishEncode(ctx, promise);
/*     */     }
/* 367 */     executor.execute(new Runnable()
/*     */         {
/*     */           public void run() {
/* 370 */             ChannelFuture f = Lz4FrameEncoder.this.finishEncode(Lz4FrameEncoder.this.ctx(), promise);
/* 371 */             f.addListener((GenericFutureListener)new ChannelPromiseNotifier(new ChannelPromise[] { this.val$promise }));
/*     */           }
/*     */         });
/* 374 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
/* 380 */     ChannelFuture f = finishEncode(ctx, ctx.newPromise());
/* 381 */     f.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */         {
/*     */           public void operationComplete(ChannelFuture f) throws Exception {
/* 384 */             ctx.close(promise);
/*     */           }
/*     */         });
/*     */     
/* 388 */     if (!f.isDone())
/*     */     {
/* 390 */       ctx.executor().schedule(new Runnable()
/*     */           {
/*     */             public void run() {
/* 393 */               ctx.close(promise);
/*     */             }
/*     */           },  10L, TimeUnit.SECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */   private ChannelHandlerContext ctx() {
/* 400 */     ChannelHandlerContext ctx = this.ctx;
/* 401 */     if (ctx == null) {
/* 402 */       throw new IllegalStateException("not added to a pipeline");
/*     */     }
/* 404 */     return ctx;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) {
/* 409 */     this.ctx = ctx;
/*     */     
/* 411 */     this.buffer = Unpooled.wrappedBuffer(new byte[this.blockSize]);
/* 412 */     this.buffer.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
/* 417 */     super.handlerRemoved(ctx);
/* 418 */     if (this.buffer != null) {
/* 419 */       this.buffer.release();
/* 420 */       this.buffer = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   final ByteBuf getBackingBuffer() {
/* 425 */     return this.buffer;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\compression\Lz4FrameEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */