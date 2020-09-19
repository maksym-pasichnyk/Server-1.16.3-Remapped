/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UniformStreamByteDistributor
/*     */   implements StreamByteDistributor
/*     */ {
/*     */   private final Http2Connection.PropertyKey stateKey;
/*  39 */   private final Deque<State> queue = new ArrayDeque<State>(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private int minAllocationChunk = 1024;
/*     */   
/*     */   private long totalStreamableBytes;
/*     */   
/*     */   public UniformStreamByteDistributor(Http2Connection connection) {
/*  50 */     this.stateKey = connection.newKey();
/*  51 */     Http2Stream connectionStream = connection.connectionStream();
/*  52 */     connectionStream.setProperty(this.stateKey, new State(connectionStream));
/*     */ 
/*     */     
/*  55 */     connection.addListener(new Http2ConnectionAdapter()
/*     */         {
/*     */           public void onStreamAdded(Http2Stream stream) {
/*  58 */             stream.setProperty(UniformStreamByteDistributor.this.stateKey, new UniformStreamByteDistributor.State(stream));
/*     */           }
/*     */ 
/*     */           
/*     */           public void onStreamClosed(Http2Stream stream) {
/*  63 */             UniformStreamByteDistributor.this.state(stream).close();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void minAllocationChunk(int minAllocationChunk) {
/*  75 */     if (minAllocationChunk <= 0) {
/*  76 */       throw new IllegalArgumentException("minAllocationChunk must be > 0");
/*     */     }
/*  78 */     this.minAllocationChunk = minAllocationChunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateStreamableBytes(StreamByteDistributor.StreamState streamState) {
/*  83 */     state(streamState.stream()).updateStreamableBytes(Http2CodecUtil.streamableBytes(streamState), streamState
/*  84 */         .hasFrame(), streamState
/*  85 */         .windowSize());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateDependencyTree(int childStreamId, int parentStreamId, short weight, boolean exclusive) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean distribute(int maxBytes, StreamByteDistributor.Writer writer) throws Http2Exception {
/*  95 */     int size = this.queue.size();
/*  96 */     if (size == 0) {
/*  97 */       return (this.totalStreamableBytes > 0L);
/*     */     }
/*     */     
/* 100 */     int chunkSize = Math.max(this.minAllocationChunk, maxBytes / size);
/*     */     
/* 102 */     State state = this.queue.pollFirst();
/*     */     do {
/* 104 */       state.enqueued = false;
/* 105 */       if (state.windowNegative) {
/*     */         continue;
/*     */       }
/* 108 */       if (maxBytes == 0 && state.streamableBytes > 0) {
/*     */ 
/*     */ 
/*     */         
/* 112 */         this.queue.addFirst(state);
/* 113 */         state.enqueued = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/* 118 */       int chunk = Math.min(chunkSize, Math.min(maxBytes, state.streamableBytes));
/* 119 */       maxBytes -= chunk;
/*     */ 
/*     */       
/* 122 */       state.write(chunk, writer);
/* 123 */     } while ((state = this.queue.pollFirst()) != null);
/*     */     
/* 125 */     return (this.totalStreamableBytes > 0L);
/*     */   }
/*     */   
/*     */   private State state(Http2Stream stream) {
/* 129 */     return ((Http2Stream)ObjectUtil.checkNotNull(stream, "stream")).<State>getProperty(this.stateKey);
/*     */   }
/*     */ 
/*     */   
/*     */   private final class State
/*     */   {
/*     */     final Http2Stream stream;
/*     */     
/*     */     int streamableBytes;
/*     */     boolean windowNegative;
/*     */     boolean enqueued;
/*     */     boolean writing;
/*     */     
/*     */     State(Http2Stream stream) {
/* 143 */       this.stream = stream;
/*     */     }
/*     */     
/*     */     void updateStreamableBytes(int newStreamableBytes, boolean hasFrame, int windowSize) {
/* 147 */       assert hasFrame || newStreamableBytes == 0 : "hasFrame: " + hasFrame + " newStreamableBytes: " + newStreamableBytes;
/*     */ 
/*     */       
/* 150 */       int delta = newStreamableBytes - this.streamableBytes;
/* 151 */       if (delta != 0) {
/* 152 */         this.streamableBytes = newStreamableBytes;
/* 153 */         UniformStreamByteDistributor.this.totalStreamableBytes = UniformStreamByteDistributor.this.totalStreamableBytes + delta;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 162 */       this.windowNegative = (windowSize < 0);
/* 163 */       if (hasFrame && (windowSize > 0 || (windowSize == 0 && !this.writing))) {
/* 164 */         addToQueue();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void write(int numBytes, StreamByteDistributor.Writer writer) throws Http2Exception {
/* 173 */       this.writing = true;
/*     */       
/*     */       try {
/* 176 */         writer.write(this.stream, numBytes);
/* 177 */       } catch (Throwable t) {
/* 178 */         throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, t, "byte distribution write error", new Object[0]);
/*     */       } finally {
/* 180 */         this.writing = false;
/*     */       } 
/*     */     }
/*     */     
/*     */     void addToQueue() {
/* 185 */       if (!this.enqueued) {
/* 186 */         this.enqueued = true;
/* 187 */         UniformStreamByteDistributor.this.queue.addLast(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     void removeFromQueue() {
/* 192 */       if (this.enqueued) {
/* 193 */         this.enqueued = false;
/* 194 */         UniformStreamByteDistributor.this.queue.remove(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void close() {
/* 200 */       removeFromQueue();
/*     */ 
/*     */       
/* 203 */       updateStreamableBytes(0, false, 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\UniformStreamByteDistributor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */