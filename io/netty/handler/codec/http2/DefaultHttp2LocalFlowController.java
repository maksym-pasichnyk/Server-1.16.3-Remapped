/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
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
/*     */ public class DefaultHttp2LocalFlowController
/*     */   implements Http2LocalFlowController
/*     */ {
/*     */   public static final float DEFAULT_WINDOW_UPDATE_RATIO = 0.5F;
/*     */   private final Http2Connection connection;
/*     */   private final Http2Connection.PropertyKey stateKey;
/*     */   private Http2FrameWriter frameWriter;
/*     */   private ChannelHandlerContext ctx;
/*     */   private float windowUpdateRatio;
/*  55 */   private int initialWindowSize = 65535;
/*     */   
/*     */   public DefaultHttp2LocalFlowController(Http2Connection connection) {
/*  58 */     this(connection, 0.5F, false);
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
/*     */   public DefaultHttp2LocalFlowController(Http2Connection connection, float windowUpdateRatio, boolean autoRefillConnectionWindow) {
/*  77 */     this.connection = (Http2Connection)ObjectUtil.checkNotNull(connection, "connection");
/*  78 */     windowUpdateRatio(windowUpdateRatio);
/*     */ 
/*     */     
/*  81 */     this.stateKey = connection.newKey();
/*     */ 
/*     */     
/*  84 */     FlowState connectionState = autoRefillConnectionWindow ? new AutoRefillState(connection.connectionStream(), this.initialWindowSize) : new DefaultState(connection.connectionStream(), this.initialWindowSize);
/*  85 */     connection.connectionStream().setProperty(this.stateKey, connectionState);
/*     */ 
/*     */     
/*  88 */     connection.addListener(new Http2ConnectionAdapter()
/*     */         {
/*     */           
/*     */           public void onStreamAdded(Http2Stream stream)
/*     */           {
/*  93 */             stream.setProperty(DefaultHttp2LocalFlowController.this.stateKey, DefaultHttp2LocalFlowController.REDUCED_FLOW_STATE);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void onStreamActive(Http2Stream stream) {
/* 100 */             stream.setProperty(DefaultHttp2LocalFlowController.this.stateKey, new DefaultHttp2LocalFlowController.DefaultState(stream, DefaultHttp2LocalFlowController.this.initialWindowSize));
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void onStreamClosed(Http2Stream stream) {
/*     */             try {
/* 108 */               DefaultHttp2LocalFlowController.FlowState state = DefaultHttp2LocalFlowController.this.state(stream);
/* 109 */               int unconsumedBytes = state.unconsumedBytes();
/* 110 */               if (DefaultHttp2LocalFlowController.this.ctx != null && unconsumedBytes > 0) {
/* 111 */                 DefaultHttp2LocalFlowController.this.connectionState().consumeBytes(unconsumedBytes);
/* 112 */                 state.consumeBytes(unconsumedBytes);
/*     */               } 
/* 114 */             } catch (Http2Exception e) {
/* 115 */               PlatformDependent.throwException(e);
/*     */             
/*     */             }
/*     */             finally {
/*     */               
/* 120 */               stream.setProperty(DefaultHttp2LocalFlowController.this.stateKey, DefaultHttp2LocalFlowController.REDUCED_FLOW_STATE);
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttp2LocalFlowController frameWriter(Http2FrameWriter frameWriter) {
/* 128 */     this.frameWriter = (Http2FrameWriter)ObjectUtil.checkNotNull(frameWriter, "frameWriter");
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelHandlerContext(ChannelHandlerContext ctx) {
/* 134 */     this.ctx = (ChannelHandlerContext)ObjectUtil.checkNotNull(ctx, "ctx");
/*     */   }
/*     */ 
/*     */   
/*     */   public void initialWindowSize(int newWindowSize) throws Http2Exception {
/* 139 */     assert this.ctx == null || this.ctx.executor().inEventLoop();
/* 140 */     int delta = newWindowSize - this.initialWindowSize;
/* 141 */     this.initialWindowSize = newWindowSize;
/*     */     
/* 143 */     WindowUpdateVisitor visitor = new WindowUpdateVisitor(delta);
/* 144 */     this.connection.forEachActiveStream(visitor);
/* 145 */     visitor.throwIfError();
/*     */   }
/*     */ 
/*     */   
/*     */   public int initialWindowSize() {
/* 150 */     return this.initialWindowSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public int windowSize(Http2Stream stream) {
/* 155 */     return state(stream).windowSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public int initialWindowSize(Http2Stream stream) {
/* 160 */     return state(stream).initialWindowSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void incrementWindowSize(Http2Stream stream, int delta) throws Http2Exception {
/* 165 */     assert this.ctx != null && this.ctx.executor().inEventLoop();
/* 166 */     FlowState state = state(stream);
/*     */ 
/*     */     
/* 169 */     state.incrementInitialStreamWindow(delta);
/* 170 */     state.writeWindowUpdateIfNeeded();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean consumeBytes(Http2Stream stream, int numBytes) throws Http2Exception {
/* 175 */     assert this.ctx != null && this.ctx.executor().inEventLoop();
/* 176 */     if (numBytes < 0) {
/* 177 */       throw new IllegalArgumentException("numBytes must not be negative");
/*     */     }
/* 179 */     if (numBytes == 0) {
/* 180 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 185 */     if (stream != null && !isClosed(stream)) {
/* 186 */       if (stream.id() == 0) {
/* 187 */         throw new UnsupportedOperationException("Returning bytes for the connection window is not supported");
/*     */       }
/*     */       
/* 190 */       boolean windowUpdateSent = connectionState().consumeBytes(numBytes);
/* 191 */       windowUpdateSent |= state(stream).consumeBytes(numBytes);
/* 192 */       return windowUpdateSent;
/*     */     } 
/* 194 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int unconsumedBytes(Http2Stream stream) {
/* 199 */     return state(stream).unconsumedBytes();
/*     */   }
/*     */   
/*     */   private static void checkValidRatio(float ratio) {
/* 203 */     if (Double.compare(ratio, 0.0D) <= 0 || Double.compare(ratio, 1.0D) >= 0) {
/* 204 */       throw new IllegalArgumentException("Invalid ratio: " + ratio);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void windowUpdateRatio(float ratio) {
/* 216 */     assert this.ctx == null || this.ctx.executor().inEventLoop();
/* 217 */     checkValidRatio(ratio);
/* 218 */     this.windowUpdateRatio = ratio;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float windowUpdateRatio() {
/* 227 */     return this.windowUpdateRatio;
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
/*     */   public void windowUpdateRatio(Http2Stream stream, float ratio) throws Http2Exception {
/* 244 */     assert this.ctx != null && this.ctx.executor().inEventLoop();
/* 245 */     checkValidRatio(ratio);
/* 246 */     FlowState state = state(stream);
/* 247 */     state.windowUpdateRatio(ratio);
/* 248 */     state.writeWindowUpdateIfNeeded();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float windowUpdateRatio(Http2Stream stream) throws Http2Exception {
/* 258 */     return state(stream).windowUpdateRatio();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void receiveFlowControlledFrame(Http2Stream stream, ByteBuf data, int padding, boolean endOfStream) throws Http2Exception {
/* 264 */     assert this.ctx != null && this.ctx.executor().inEventLoop();
/* 265 */     int dataLength = data.readableBytes() + padding;
/*     */ 
/*     */     
/* 268 */     FlowState connectionState = connectionState();
/* 269 */     connectionState.receiveFlowControlledFrame(dataLength);
/*     */     
/* 271 */     if (stream != null && !isClosed(stream)) {
/*     */       
/* 273 */       FlowState state = state(stream);
/* 274 */       state.endOfStream(endOfStream);
/* 275 */       state.receiveFlowControlledFrame(dataLength);
/* 276 */     } else if (dataLength > 0) {
/*     */       
/* 278 */       connectionState.consumeBytes(dataLength);
/*     */     } 
/*     */   }
/*     */   
/*     */   private FlowState connectionState() {
/* 283 */     return this.connection.connectionStream().<FlowState>getProperty(this.stateKey);
/*     */   }
/*     */   
/*     */   private FlowState state(Http2Stream stream) {
/* 287 */     return stream.<FlowState>getProperty(this.stateKey);
/*     */   }
/*     */   
/*     */   private static boolean isClosed(Http2Stream stream) {
/* 291 */     return (stream.state() == Http2Stream.State.CLOSED);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class AutoRefillState
/*     */     extends DefaultState
/*     */   {
/*     */     public AutoRefillState(Http2Stream stream, int initialWindowSize) {
/* 300 */       super(stream, initialWindowSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public void receiveFlowControlledFrame(int dataLength) throws Http2Exception {
/* 305 */       super.receiveFlowControlledFrame(dataLength);
/*     */       
/* 307 */       super.consumeBytes(dataLength);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean consumeBytes(int numBytes) throws Http2Exception {
/* 313 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class DefaultState
/*     */     implements FlowState
/*     */   {
/*     */     private final Http2Stream stream;
/*     */ 
/*     */ 
/*     */     
/*     */     private int window;
/*     */ 
/*     */ 
/*     */     
/*     */     private int processedWindow;
/*     */ 
/*     */ 
/*     */     
/*     */     private int initialStreamWindowSize;
/*     */ 
/*     */ 
/*     */     
/*     */     private float streamWindowUpdateRatio;
/*     */ 
/*     */ 
/*     */     
/*     */     private int lowerBound;
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean endOfStream;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DefaultState(Http2Stream stream, int initialWindowSize) {
/* 353 */       this.stream = stream;
/* 354 */       window(initialWindowSize);
/* 355 */       this.streamWindowUpdateRatio = DefaultHttp2LocalFlowController.this.windowUpdateRatio;
/*     */     }
/*     */ 
/*     */     
/*     */     public void window(int initialWindowSize) {
/* 360 */       assert DefaultHttp2LocalFlowController.this.ctx == null || DefaultHttp2LocalFlowController.this.ctx.executor().inEventLoop();
/* 361 */       this.window = this.processedWindow = this.initialStreamWindowSize = initialWindowSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public int windowSize() {
/* 366 */       return this.window;
/*     */     }
/*     */ 
/*     */     
/*     */     public int initialWindowSize() {
/* 371 */       return this.initialStreamWindowSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public void endOfStream(boolean endOfStream) {
/* 376 */       this.endOfStream = endOfStream;
/*     */     }
/*     */ 
/*     */     
/*     */     public float windowUpdateRatio() {
/* 381 */       return this.streamWindowUpdateRatio;
/*     */     }
/*     */ 
/*     */     
/*     */     public void windowUpdateRatio(float ratio) {
/* 386 */       assert DefaultHttp2LocalFlowController.this.ctx == null || DefaultHttp2LocalFlowController.this.ctx.executor().inEventLoop();
/* 387 */       this.streamWindowUpdateRatio = ratio;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void incrementInitialStreamWindow(int delta) {
/* 393 */       int newValue = (int)Math.min(2147483647L, 
/* 394 */           Math.max(0L, this.initialStreamWindowSize + delta));
/* 395 */       delta = newValue - this.initialStreamWindowSize;
/*     */       
/* 397 */       this.initialStreamWindowSize += delta;
/*     */     }
/*     */ 
/*     */     
/*     */     public void incrementFlowControlWindows(int delta) throws Http2Exception {
/* 402 */       if (delta > 0 && this.window > Integer.MAX_VALUE - delta) {
/* 403 */         throw Http2Exception.streamError(this.stream.id(), Http2Error.FLOW_CONTROL_ERROR, "Flow control window overflowed for stream: %d", new Object[] {
/* 404 */               Integer.valueOf(this.stream.id())
/*     */             });
/*     */       }
/* 407 */       this.window += delta;
/* 408 */       this.processedWindow += delta;
/* 409 */       this.lowerBound = (delta < 0) ? delta : 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void receiveFlowControlledFrame(int dataLength) throws Http2Exception {
/* 414 */       assert dataLength >= 0;
/*     */ 
/*     */       
/* 417 */       this.window -= dataLength;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 424 */       if (this.window < this.lowerBound)
/* 425 */         throw Http2Exception.streamError(this.stream.id(), Http2Error.FLOW_CONTROL_ERROR, "Flow control window exceeded for stream: %d", new Object[] {
/* 426 */               Integer.valueOf(this.stream.id())
/*     */             }); 
/*     */     }
/*     */     
/*     */     private void returnProcessedBytes(int delta) throws Http2Exception {
/* 431 */       if (this.processedWindow - delta < this.window)
/* 432 */         throw Http2Exception.streamError(this.stream.id(), Http2Error.INTERNAL_ERROR, "Attempting to return too many bytes for stream %d", new Object[] {
/* 433 */               Integer.valueOf(this.stream.id())
/*     */             }); 
/* 435 */       this.processedWindow -= delta;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean consumeBytes(int numBytes) throws Http2Exception {
/* 441 */       returnProcessedBytes(numBytes);
/* 442 */       return writeWindowUpdateIfNeeded();
/*     */     }
/*     */ 
/*     */     
/*     */     public int unconsumedBytes() {
/* 447 */       return this.processedWindow - this.window;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean writeWindowUpdateIfNeeded() throws Http2Exception {
/* 452 */       if (this.endOfStream || this.initialStreamWindowSize <= 0) {
/* 453 */         return false;
/*     */       }
/*     */       
/* 456 */       int threshold = (int)(this.initialStreamWindowSize * this.streamWindowUpdateRatio);
/* 457 */       if (this.processedWindow <= threshold) {
/* 458 */         writeWindowUpdate();
/* 459 */         return true;
/*     */       } 
/* 461 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void writeWindowUpdate() throws Http2Exception {
/* 470 */       int deltaWindowSize = this.initialStreamWindowSize - this.processedWindow;
/*     */       try {
/* 472 */         incrementFlowControlWindows(deltaWindowSize);
/* 473 */       } catch (Throwable t) {
/* 474 */         throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, t, "Attempting to return too many bytes for stream %d", new Object[] {
/* 475 */               Integer.valueOf(this.stream.id())
/*     */             });
/*     */       } 
/*     */       
/* 479 */       DefaultHttp2LocalFlowController.this.frameWriter.writeWindowUpdate(DefaultHttp2LocalFlowController.this.ctx, this.stream.id(), deltaWindowSize, DefaultHttp2LocalFlowController.this.ctx.newPromise());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 487 */   private static final FlowState REDUCED_FLOW_STATE = new FlowState()
/*     */     {
/*     */       public int windowSize()
/*     */       {
/* 491 */         return 0;
/*     */       }
/*     */ 
/*     */       
/*     */       public int initialWindowSize() {
/* 496 */         return 0;
/*     */       }
/*     */ 
/*     */       
/*     */       public void window(int initialWindowSize) {
/* 501 */         throw new UnsupportedOperationException();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void incrementInitialStreamWindow(int delta) {}
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean writeWindowUpdateIfNeeded() throws Http2Exception {
/* 512 */         throw new UnsupportedOperationException();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean consumeBytes(int numBytes) throws Http2Exception {
/* 517 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public int unconsumedBytes() {
/* 522 */         return 0;
/*     */       }
/*     */ 
/*     */       
/*     */       public float windowUpdateRatio() {
/* 527 */         throw new UnsupportedOperationException();
/*     */       }
/*     */ 
/*     */       
/*     */       public void windowUpdateRatio(float ratio) {
/* 532 */         throw new UnsupportedOperationException();
/*     */       }
/*     */ 
/*     */       
/*     */       public void receiveFlowControlledFrame(int dataLength) throws Http2Exception {
/* 537 */         throw new UnsupportedOperationException();
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void incrementFlowControlWindows(int delta) throws Http2Exception {}
/*     */ 
/*     */ 
/*     */       
/*     */       public void endOfStream(boolean endOfStream) {
/* 548 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
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
/*     */   private final class WindowUpdateVisitor
/*     */     implements Http2StreamVisitor
/*     */   {
/*     */     private Http2Exception.CompositeStreamException compositeException;
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
/*     */     private final int delta;
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
/*     */     public WindowUpdateVisitor(int delta) {
/* 617 */       this.delta = delta;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean visit(Http2Stream stream) throws Http2Exception {
/*     */       try {
/* 624 */         DefaultHttp2LocalFlowController.FlowState state = DefaultHttp2LocalFlowController.this.state(stream);
/* 625 */         state.incrementFlowControlWindows(this.delta);
/* 626 */         state.incrementInitialStreamWindow(this.delta);
/* 627 */       } catch (StreamException e) {
/* 628 */         if (this.compositeException == null) {
/* 629 */           this.compositeException = new Http2Exception.CompositeStreamException(e.error(), 4);
/*     */         }
/* 631 */         this.compositeException.add(e);
/*     */       } 
/* 633 */       return true;
/*     */     }
/*     */     
/*     */     public void throwIfError() throws Http2Exception.CompositeStreamException {
/* 637 */       if (this.compositeException != null)
/* 638 */         throw this.compositeException; 
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface FlowState {
/*     */     int windowSize();
/*     */     
/*     */     int initialWindowSize();
/*     */     
/*     */     void window(int param1Int);
/*     */     
/*     */     void incrementInitialStreamWindow(int param1Int);
/*     */     
/*     */     boolean writeWindowUpdateIfNeeded() throws Http2Exception;
/*     */     
/*     */     boolean consumeBytes(int param1Int) throws Http2Exception;
/*     */     
/*     */     int unconsumedBytes();
/*     */     
/*     */     float windowUpdateRatio();
/*     */     
/*     */     void windowUpdateRatio(float param1Float);
/*     */     
/*     */     void receiveFlowControlledFrame(int param1Int) throws Http2Exception;
/*     */     
/*     */     void incrementFlowControlWindows(int param1Int) throws Http2Exception;
/*     */     
/*     */     void endOfStream(boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\DefaultHttp2LocalFlowController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */