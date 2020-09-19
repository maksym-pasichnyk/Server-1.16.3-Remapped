/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StreamBufferingEncoder
/*     */   extends DecoratingHttp2ConnectionEncoder
/*     */ {
/*     */   public static final class Http2ChannelClosedException
/*     */     extends Http2Exception
/*     */   {
/*     */     private static final long serialVersionUID = 4768543442094476971L;
/*     */     
/*     */     public Http2ChannelClosedException() {
/*  68 */       super(Http2Error.REFUSED_STREAM, "Connection closed");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Http2GoAwayException
/*     */     extends Http2Exception
/*     */   {
/*     */     private static final long serialVersionUID = 1326785622777291198L;
/*     */     
/*     */     private final int lastStreamId;
/*     */     private final long errorCode;
/*     */     private final byte[] debugData;
/*     */     
/*     */     public Http2GoAwayException(int lastStreamId, long errorCode, byte[] debugData) {
/*  83 */       super(Http2Error.STREAM_CLOSED);
/*  84 */       this.lastStreamId = lastStreamId;
/*  85 */       this.errorCode = errorCode;
/*  86 */       this.debugData = debugData;
/*     */     }
/*     */     
/*     */     public int lastStreamId() {
/*  90 */       return this.lastStreamId;
/*     */     }
/*     */     
/*     */     public long errorCode() {
/*  94 */       return this.errorCode;
/*     */     }
/*     */     
/*     */     public byte[] debugData() {
/*  98 */       return this.debugData;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   private final TreeMap<Integer, PendingStream> pendingStreams = new TreeMap<Integer, PendingStream>();
/*     */   private int maxConcurrentStreams;
/*     */   private boolean closed;
/*     */   
/*     */   public StreamBufferingEncoder(Http2ConnectionEncoder delegate) {
/* 111 */     this(delegate, 100);
/*     */   }
/*     */   
/*     */   public StreamBufferingEncoder(Http2ConnectionEncoder delegate, int initialMaxConcurrentStreams) {
/* 115 */     super(delegate);
/* 116 */     this.maxConcurrentStreams = initialMaxConcurrentStreams;
/* 117 */     connection().addListener(new Http2ConnectionAdapter()
/*     */         {
/*     */           public void onGoAwayReceived(int lastStreamId, long errorCode, ByteBuf debugData)
/*     */           {
/* 121 */             StreamBufferingEncoder.this.cancelGoAwayStreams(lastStreamId, errorCode, debugData);
/*     */           }
/*     */ 
/*     */           
/*     */           public void onStreamClosed(Http2Stream stream) {
/* 126 */             StreamBufferingEncoder.this.tryCreatePendingStreams();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int numBufferedStreams() {
/* 135 */     return this.pendingStreams.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, ChannelPromise promise) {
/* 141 */     return writeHeaders(ctx, streamId, headers, 0, (short)16, false, padding, endStream, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream, ChannelPromise promise) {
/* 149 */     if (this.closed) {
/* 150 */       return (ChannelFuture)promise.setFailure(new Http2ChannelClosedException());
/*     */     }
/* 152 */     if (isExistingStream(streamId) || connection().goAwayReceived()) {
/* 153 */       return super.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream, promise);
/*     */     }
/*     */     
/* 156 */     if (canCreateStream()) {
/* 157 */       return super.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream, promise);
/*     */     }
/*     */     
/* 160 */     PendingStream pendingStream = this.pendingStreams.get(Integer.valueOf(streamId));
/* 161 */     if (pendingStream == null) {
/* 162 */       pendingStream = new PendingStream(ctx, streamId);
/* 163 */       this.pendingStreams.put(Integer.valueOf(streamId), pendingStream);
/*     */     } 
/* 165 */     pendingStream.frames.add(new HeadersFrame(headers, streamDependency, weight, exclusive, padding, endOfStream, promise));
/*     */     
/* 167 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeRstStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise) {
/* 173 */     if (isExistingStream(streamId)) {
/* 174 */       return super.writeRstStream(ctx, streamId, errorCode, promise);
/*     */     }
/*     */ 
/*     */     
/* 178 */     PendingStream stream = this.pendingStreams.remove(Integer.valueOf(streamId));
/* 179 */     if (stream != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 184 */       stream.close(null);
/* 185 */       promise.setSuccess();
/*     */     } else {
/* 187 */       promise.setFailure(Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream does not exist %d", new Object[] { Integer.valueOf(streamId) }));
/*     */     } 
/* 189 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture writeData(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream, ChannelPromise promise) {
/* 195 */     if (isExistingStream(streamId)) {
/* 196 */       return super.writeData(ctx, streamId, data, padding, endOfStream, promise);
/*     */     }
/* 198 */     PendingStream pendingStream = this.pendingStreams.get(Integer.valueOf(streamId));
/* 199 */     if (pendingStream != null) {
/* 200 */       pendingStream.frames.add(new DataFrame(data, padding, endOfStream, promise));
/*     */     } else {
/* 202 */       ReferenceCountUtil.safeRelease(data);
/* 203 */       promise.setFailure(Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream does not exist %d", new Object[] { Integer.valueOf(streamId) }));
/*     */     } 
/* 205 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remoteSettings(Http2Settings settings) throws Http2Exception {
/* 212 */     super.remoteSettings(settings);
/*     */ 
/*     */     
/* 215 */     this.maxConcurrentStreams = connection().local().maxActiveStreams();
/*     */ 
/*     */     
/* 218 */     tryCreatePendingStreams();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*     */     try {
/* 224 */       if (!this.closed) {
/* 225 */         this.closed = true;
/*     */ 
/*     */         
/* 228 */         Http2ChannelClosedException e = new Http2ChannelClosedException();
/* 229 */         while (!this.pendingStreams.isEmpty()) {
/* 230 */           PendingStream stream = (PendingStream)this.pendingStreams.pollFirstEntry().getValue();
/* 231 */           stream.close(e);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 235 */       super.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tryCreatePendingStreams() {
/* 240 */     while (!this.pendingStreams.isEmpty() && canCreateStream()) {
/* 241 */       Map.Entry<Integer, PendingStream> entry = this.pendingStreams.pollFirstEntry();
/* 242 */       PendingStream pendingStream = entry.getValue();
/*     */       try {
/* 244 */         pendingStream.sendFrames();
/* 245 */       } catch (Throwable t) {
/* 246 */         pendingStream.close(t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cancelGoAwayStreams(int lastStreamId, long errorCode, ByteBuf debugData) {
/* 252 */     Iterator<PendingStream> iter = this.pendingStreams.values().iterator();
/* 253 */     Exception e = new Http2GoAwayException(lastStreamId, errorCode, ByteBufUtil.getBytes(debugData));
/* 254 */     while (iter.hasNext()) {
/* 255 */       PendingStream stream = iter.next();
/* 256 */       if (stream.streamId > lastStreamId) {
/* 257 */         iter.remove();
/* 258 */         stream.close(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canCreateStream() {
/* 267 */     return (connection().local().numActiveStreams() < this.maxConcurrentStreams);
/*     */   }
/*     */   
/*     */   private boolean isExistingStream(int streamId) {
/* 271 */     return (streamId <= connection().local().lastStreamCreated());
/*     */   }
/*     */   
/*     */   private static final class PendingStream {
/*     */     final ChannelHandlerContext ctx;
/*     */     final int streamId;
/* 277 */     final Queue<StreamBufferingEncoder.Frame> frames = new ArrayDeque<StreamBufferingEncoder.Frame>(2);
/*     */     
/*     */     PendingStream(ChannelHandlerContext ctx, int streamId) {
/* 280 */       this.ctx = ctx;
/* 281 */       this.streamId = streamId;
/*     */     }
/*     */     
/*     */     void sendFrames() {
/* 285 */       for (StreamBufferingEncoder.Frame frame : this.frames) {
/* 286 */         frame.send(this.ctx, this.streamId);
/*     */       }
/*     */     }
/*     */     
/*     */     void close(Throwable t) {
/* 291 */       for (StreamBufferingEncoder.Frame frame : this.frames)
/* 292 */         frame.release(t); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class Frame
/*     */   {
/*     */     final ChannelPromise promise;
/*     */     
/*     */     Frame(ChannelPromise promise) {
/* 301 */       this.promise = promise;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void release(Throwable t) {
/* 308 */       if (t == null) {
/* 309 */         this.promise.setSuccess();
/*     */       } else {
/* 311 */         this.promise.setFailure(t);
/*     */       } 
/*     */     }
/*     */     
/*     */     abstract void send(ChannelHandlerContext param1ChannelHandlerContext, int param1Int);
/*     */   }
/*     */   
/*     */   private final class HeadersFrame
/*     */     extends Frame {
/*     */     final Http2Headers headers;
/*     */     final int streamDependency;
/*     */     final short weight;
/*     */     final boolean exclusive;
/*     */     final int padding;
/*     */     final boolean endOfStream;
/*     */     
/*     */     HeadersFrame(Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream, ChannelPromise promise) {
/* 328 */       super(promise);
/* 329 */       this.headers = headers;
/* 330 */       this.streamDependency = streamDependency;
/* 331 */       this.weight = weight;
/* 332 */       this.exclusive = exclusive;
/* 333 */       this.padding = padding;
/* 334 */       this.endOfStream = endOfStream;
/*     */     }
/*     */ 
/*     */     
/*     */     void send(ChannelHandlerContext ctx, int streamId) {
/* 339 */       StreamBufferingEncoder.this.writeHeaders(ctx, streamId, this.headers, this.streamDependency, this.weight, this.exclusive, this.padding, this.endOfStream, this.promise);
/*     */     }
/*     */   }
/*     */   
/*     */   private final class DataFrame extends Frame {
/*     */     final ByteBuf data;
/*     */     final int padding;
/*     */     final boolean endOfStream;
/*     */     
/*     */     DataFrame(ByteBuf data, int padding, boolean endOfStream, ChannelPromise promise) {
/* 349 */       super(promise);
/* 350 */       this.data = data;
/* 351 */       this.padding = padding;
/* 352 */       this.endOfStream = endOfStream;
/*     */     }
/*     */ 
/*     */     
/*     */     void release(Throwable t) {
/* 357 */       super.release(t);
/* 358 */       ReferenceCountUtil.safeRelease(this.data);
/*     */     }
/*     */ 
/*     */     
/*     */     void send(ChannelHandlerContext ctx, int streamId) {
/* 363 */       StreamBufferingEncoder.this.writeData(ctx, streamId, this.data, this.padding, this.endOfStream, this.promise);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\StreamBufferingEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */