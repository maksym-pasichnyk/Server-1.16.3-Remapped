/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultChannelPromise;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Http2CodecUtil
/*     */ {
/*     */   public static final int CONNECTION_STREAM_ID = 0;
/*     */   public static final int HTTP_UPGRADE_STREAM_ID = 1;
/*  48 */   public static final CharSequence HTTP_UPGRADE_SETTINGS_HEADER = (CharSequence)AsciiString.cached("HTTP2-Settings");
/*  49 */   public static final CharSequence HTTP_UPGRADE_PROTOCOL_NAME = "h2c";
/*  50 */   public static final CharSequence TLS_UPGRADE_PROTOCOL_NAME = "h2";
/*     */   
/*     */   public static final int PING_FRAME_PAYLOAD_LENGTH = 8;
/*     */   
/*     */   public static final short MAX_UNSIGNED_BYTE = 255;
/*     */   
/*     */   public static final int MAX_PADDING = 256;
/*     */   
/*     */   public static final long MAX_UNSIGNED_INT = 4294967295L;
/*     */   
/*     */   public static final int FRAME_HEADER_LENGTH = 9;
/*     */   
/*     */   public static final int SETTING_ENTRY_LENGTH = 6;
/*     */   
/*     */   public static final int PRIORITY_ENTRY_LENGTH = 5;
/*     */   public static final int INT_FIELD_LENGTH = 4;
/*     */   public static final short MAX_WEIGHT = 256;
/*     */   public static final short MIN_WEIGHT = 1;
/*  68 */   private static final ByteBuf CONNECTION_PREFACE = Unpooled.unreleasableBuffer(Unpooled.directBuffer(24).writeBytes("PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n".getBytes(CharsetUtil.UTF_8)))
/*  69 */     .asReadOnly();
/*     */   
/*  71 */   private static final ByteBuf EMPTY_PING = Unpooled.unreleasableBuffer(Unpooled.directBuffer(8).writeZero(8))
/*  72 */     .asReadOnly();
/*     */   
/*     */   private static final int MAX_PADDING_LENGTH_LENGTH = 1;
/*     */   
/*     */   public static final int DATA_FRAME_HEADER_LENGTH = 10;
/*     */   
/*     */   public static final int HEADERS_FRAME_HEADER_LENGTH = 15;
/*     */   
/*     */   public static final int PRIORITY_FRAME_LENGTH = 14;
/*     */   
/*     */   public static final int RST_STREAM_FRAME_LENGTH = 13;
/*     */   
/*     */   public static final int PUSH_PROMISE_FRAME_HEADER_LENGTH = 14;
/*     */   
/*     */   public static final int GO_AWAY_FRAME_HEADER_LENGTH = 17;
/*     */   
/*     */   public static final int WINDOW_UPDATE_FRAME_LENGTH = 13;
/*     */   
/*     */   public static final int CONTINUATION_FRAME_HEADER_LENGTH = 10;
/*     */   
/*     */   public static final char SETTINGS_HEADER_TABLE_SIZE = '\001';
/*     */   
/*     */   public static final char SETTINGS_ENABLE_PUSH = '\002';
/*     */   
/*     */   public static final char SETTINGS_MAX_CONCURRENT_STREAMS = '\003';
/*     */   
/*     */   public static final char SETTINGS_INITIAL_WINDOW_SIZE = '\004';
/*     */   
/*     */   public static final char SETTINGS_MAX_FRAME_SIZE = '\005';
/*     */   
/*     */   public static final char SETTINGS_MAX_HEADER_LIST_SIZE = '\006';
/*     */   
/*     */   public static final int NUM_STANDARD_SETTINGS = 6;
/*     */   
/*     */   public static final long MAX_HEADER_TABLE_SIZE = 4294967295L;
/*     */   
/*     */   public static final long MAX_CONCURRENT_STREAMS = 4294967295L;
/*     */   
/*     */   public static final int MAX_INITIAL_WINDOW_SIZE = 2147483647;
/*     */   
/*     */   public static final int MAX_FRAME_SIZE_LOWER_BOUND = 16384;
/*     */   
/*     */   public static final int MAX_FRAME_SIZE_UPPER_BOUND = 16777215;
/*     */   
/*     */   public static final long MAX_HEADER_LIST_SIZE = 4294967295L;
/*     */   
/*     */   public static final long MIN_HEADER_TABLE_SIZE = 0L;
/*     */   
/*     */   public static final long MIN_CONCURRENT_STREAMS = 0L;
/*     */   public static final int MIN_INITIAL_WINDOW_SIZE = 0;
/*     */   public static final long MIN_HEADER_LIST_SIZE = 0L;
/*     */   public static final int DEFAULT_WINDOW_SIZE = 65535;
/*     */   public static final short DEFAULT_PRIORITY_WEIGHT = 16;
/*     */   public static final int DEFAULT_HEADER_TABLE_SIZE = 4096;
/*     */   public static final long DEFAULT_HEADER_LIST_SIZE = 8192L;
/*     */   public static final int DEFAULT_MAX_FRAME_SIZE = 16384;
/*     */   public static final int SMALLEST_MAX_CONCURRENT_STREAMS = 100;
/*     */   static final int DEFAULT_MAX_RESERVED_STREAMS = 100;
/*     */   static final int DEFAULT_MIN_ALLOCATION_CHUNK = 1024;
/*     */   static final int DEFAULT_INITIAL_HUFFMAN_DECODE_CAPACITY = 32;
/*     */   
/*     */   public static long calculateMaxHeaderListSizeGoAway(long maxHeaderListSize) {
/* 134 */     return maxHeaderListSize + (maxHeaderListSize >>> 2L);
/*     */   }
/*     */   
/* 137 */   public static final long DEFAULT_GRACEFUL_SHUTDOWN_TIMEOUT_MILLIS = TimeUnit.MILLISECONDS.convert(30L, TimeUnit.SECONDS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isOutboundStream(boolean server, int streamId) {
/* 146 */     boolean even = ((streamId & 0x1) == 0);
/* 147 */     return (streamId > 0 && server == even);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isStreamIdValid(int streamId) {
/* 154 */     return (streamId >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMaxFrameSizeValid(int maxFrameSize) {
/* 161 */     return (maxFrameSize >= 16384 && maxFrameSize <= 16777215);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf connectionPrefaceBuf() {
/* 169 */     return CONNECTION_PREFACE.retainedDuplicate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf emptyPingBuf() {
/* 177 */     return EMPTY_PING.retainedDuplicate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Http2Exception getEmbeddedHttp2Exception(Throwable cause) {
/* 185 */     while (cause != null) {
/* 186 */       if (cause instanceof Http2Exception) {
/* 187 */         return (Http2Exception)cause;
/*     */       }
/* 189 */       cause = cause.getCause();
/*     */     } 
/* 191 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ByteBuf toByteBuf(ChannelHandlerContext ctx, Throwable cause) {
/* 199 */     if (cause == null || cause.getMessage() == null) {
/* 200 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/*     */     
/* 203 */     return ByteBufUtil.writeUtf8(ctx.alloc(), cause.getMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int readUnsignedInt(ByteBuf buf) {
/* 210 */     return buf.readInt() & Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeFrameHeader(ByteBuf out, int payloadLength, byte type, Http2Flags flags, int streamId) {
/* 218 */     out.ensureWritable(9 + payloadLength);
/* 219 */     writeFrameHeaderInternal(out, payloadLength, type, flags, streamId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int streamableBytes(StreamByteDistributor.StreamState state) {
/* 226 */     return Math.max(0, (int)Math.min(state.pendingBytes(), state.windowSize()));
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
/*     */   public static void headerListSizeExceeded(int streamId, long maxHeaderListSize, boolean onDecode) throws Http2Exception {
/* 239 */     throw Http2Exception.headerListSizeError(streamId, Http2Error.PROTOCOL_ERROR, onDecode, "Header size exceeded max allowed size (%d)", new Object[] {
/* 240 */           Long.valueOf(maxHeaderListSize)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void headerListSizeExceeded(long maxHeaderListSize) throws Http2Exception {
/* 251 */     throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header size exceeded max allowed size (%d)", new Object[] {
/* 252 */           Long.valueOf(maxHeaderListSize)
/*     */         });
/*     */   }
/*     */   
/*     */   static void writeFrameHeaderInternal(ByteBuf out, int payloadLength, byte type, Http2Flags flags, int streamId) {
/* 257 */     out.writeMedium(payloadLength);
/* 258 */     out.writeByte(type);
/* 259 */     out.writeByte(flags.value());
/* 260 */     out.writeInt(streamId);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SimpleChannelPromiseAggregator
/*     */     extends DefaultChannelPromise
/*     */   {
/*     */     private final ChannelPromise promise;
/*     */     
/*     */     private int expectedCount;
/*     */     private int doneCount;
/*     */     private Throwable lastFailure;
/*     */     private boolean doneAllocating;
/*     */     
/*     */     SimpleChannelPromiseAggregator(ChannelPromise promise, Channel c, EventExecutor e) {
/* 275 */       super(c, e);
/* 276 */       assert promise != null && !promise.isDone();
/* 277 */       this.promise = promise;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ChannelPromise newPromise() {
/* 286 */       assert !this.doneAllocating : "Done allocating. No more promises can be allocated.";
/* 287 */       this.expectedCount++;
/* 288 */       return (ChannelPromise)this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ChannelPromise doneAllocatingPromises() {
/* 297 */       if (!this.doneAllocating) {
/* 298 */         this.doneAllocating = true;
/* 299 */         if (this.doneCount == this.expectedCount || this.expectedCount == 0) {
/* 300 */           return setPromise();
/*     */         }
/*     */       } 
/* 303 */       return (ChannelPromise)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryFailure(Throwable cause) {
/* 308 */       if (allowFailure()) {
/* 309 */         this.doneCount++;
/* 310 */         this.lastFailure = cause;
/* 311 */         if (allPromisesDone()) {
/* 312 */           return tryPromise();
/*     */         }
/*     */ 
/*     */         
/* 316 */         return true;
/*     */       } 
/* 318 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ChannelPromise setFailure(Throwable cause) {
/* 329 */       if (allowFailure()) {
/* 330 */         this.doneCount++;
/* 331 */         this.lastFailure = cause;
/* 332 */         if (allPromisesDone()) {
/* 333 */           return setPromise();
/*     */         }
/*     */       } 
/* 336 */       return (ChannelPromise)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelPromise setSuccess(Void result) {
/* 341 */       if (awaitingPromises()) {
/* 342 */         this.doneCount++;
/* 343 */         if (allPromisesDone()) {
/* 344 */           setPromise();
/*     */         }
/*     */       } 
/* 347 */       return (ChannelPromise)this;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean trySuccess(Void result) {
/* 352 */       if (awaitingPromises()) {
/* 353 */         this.doneCount++;
/* 354 */         if (allPromisesDone()) {
/* 355 */           return tryPromise();
/*     */         }
/*     */ 
/*     */         
/* 359 */         return true;
/*     */       } 
/* 361 */       return false;
/*     */     }
/*     */     
/*     */     private boolean allowFailure() {
/* 365 */       return (awaitingPromises() || this.expectedCount == 0);
/*     */     }
/*     */     
/*     */     private boolean awaitingPromises() {
/* 369 */       return (this.doneCount < this.expectedCount);
/*     */     }
/*     */     
/*     */     private boolean allPromisesDone() {
/* 373 */       return (this.doneCount == this.expectedCount && this.doneAllocating);
/*     */     }
/*     */     
/*     */     private ChannelPromise setPromise() {
/* 377 */       if (this.lastFailure == null) {
/* 378 */         this.promise.setSuccess();
/* 379 */         return super.setSuccess(null);
/*     */       } 
/* 381 */       this.promise.setFailure(this.lastFailure);
/* 382 */       return super.setFailure(this.lastFailure);
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean tryPromise() {
/* 387 */       if (this.lastFailure == null) {
/* 388 */         this.promise.trySuccess();
/* 389 */         return super.trySuccess(null);
/*     */       } 
/* 391 */       this.promise.tryFailure(this.lastFailure);
/* 392 */       return super.tryFailure(this.lastFailure);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void verifyPadding(int padding) {
/* 398 */     if (padding < 0 || padding > 256)
/* 399 */       throw new IllegalArgumentException(String.format("Invalid padding '%d'. Padding must be between 0 and %d (inclusive).", new Object[] {
/* 400 */               Integer.valueOf(padding), Integer.valueOf(256)
/*     */             })); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2CodecUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */