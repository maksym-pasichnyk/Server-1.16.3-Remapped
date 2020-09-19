/*      */ package io.netty.channel.epoll;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.channel.AbstractChannel;
/*      */ import io.netty.channel.Channel;
/*      */ import io.netty.channel.ChannelConfig;
/*      */ import io.netty.channel.ChannelFuture;
/*      */ import io.netty.channel.ChannelFutureListener;
/*      */ import io.netty.channel.ChannelMetadata;
/*      */ import io.netty.channel.ChannelOutboundBuffer;
/*      */ import io.netty.channel.ChannelPipeline;
/*      */ import io.netty.channel.ChannelPromise;
/*      */ import io.netty.channel.DefaultFileRegion;
/*      */ import io.netty.channel.EventLoop;
/*      */ import io.netty.channel.FileRegion;
/*      */ import io.netty.channel.RecvByteBufAllocator;
/*      */ import io.netty.channel.socket.DuplexChannel;
/*      */ import io.netty.channel.unix.FileDescriptor;
/*      */ import io.netty.channel.unix.IovArray;
/*      */ import io.netty.channel.unix.SocketWritableByteChannel;
/*      */ import io.netty.channel.unix.UnixChannelUtil;
/*      */ import io.netty.util.concurrent.Future;
/*      */ import io.netty.util.concurrent.GenericFutureListener;
/*      */ import io.netty.util.internal.ObjectUtil;
/*      */ import io.netty.util.internal.PlatformDependent;
/*      */ import io.netty.util.internal.StringUtil;
/*      */ import io.netty.util.internal.ThrowableUtil;
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import java.io.IOException;
/*      */ import java.net.SocketAddress;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.ClosedChannelException;
/*      */ import java.nio.channels.WritableByteChannel;
/*      */ import java.util.Queue;
/*      */ import java.util.concurrent.Executor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AbstractEpollStreamChannel
/*      */   extends AbstractEpollChannel
/*      */   implements DuplexChannel
/*      */ {
/*   59 */   private static final ChannelMetadata METADATA = new ChannelMetadata(false, 16);
/*   60 */   private static final String EXPECTED_TYPES = " (expected: " + 
/*   61 */     StringUtil.simpleClassName(ByteBuf.class) + ", " + 
/*   62 */     StringUtil.simpleClassName(DefaultFileRegion.class) + ')';
/*   63 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractEpollStreamChannel.class);
/*      */   
/*   65 */   private static final ClosedChannelException CLEAR_SPLICE_QUEUE_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractEpollStreamChannel.class, "clearSpliceQueue()");
/*      */   
/*   67 */   private static final ClosedChannelException SPLICE_TO_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractEpollStreamChannel.class, "spliceTo(...)");
/*      */ 
/*      */ 
/*      */   
/*   71 */   private static final ClosedChannelException FAIL_SPLICE_IF_CLOSED_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), AbstractEpollStreamChannel.class, "failSpliceIfClosed(...)");
/*      */   
/*   73 */   private final Runnable flushTask = new Runnable()
/*      */     {
/*      */       
/*      */       public void run()
/*      */       {
/*   78 */         ((AbstractEpollChannel.AbstractEpollUnsafe)AbstractEpollStreamChannel.this.unsafe()).flush0();
/*      */       }
/*      */     };
/*      */ 
/*      */   
/*      */   private Queue<SpliceInTask> spliceQueue;
/*      */   
/*      */   private FileDescriptor pipeIn;
/*      */   private FileDescriptor pipeOut;
/*      */   private WritableByteChannel byteChannel;
/*      */   
/*      */   protected AbstractEpollStreamChannel(Channel parent, int fd) {
/*   90 */     this(parent, new LinuxSocket(fd));
/*      */   }
/*      */   
/*      */   protected AbstractEpollStreamChannel(int fd) {
/*   94 */     this(new LinuxSocket(fd));
/*      */   }
/*      */   
/*      */   AbstractEpollStreamChannel(LinuxSocket fd) {
/*   98 */     this(fd, isSoErrorZero(fd));
/*      */   }
/*      */   
/*      */   AbstractEpollStreamChannel(Channel parent, LinuxSocket fd) {
/*  102 */     super(parent, fd, Native.EPOLLIN, true);
/*      */     
/*  104 */     this.flags |= Native.EPOLLRDHUP;
/*      */   }
/*      */   
/*      */   AbstractEpollStreamChannel(Channel parent, LinuxSocket fd, SocketAddress remote) {
/*  108 */     super(parent, fd, Native.EPOLLIN, remote);
/*      */     
/*  110 */     this.flags |= Native.EPOLLRDHUP;
/*      */   }
/*      */   
/*      */   protected AbstractEpollStreamChannel(LinuxSocket fd, boolean active) {
/*  114 */     super((Channel)null, fd, Native.EPOLLIN, active);
/*      */     
/*  116 */     this.flags |= Native.EPOLLRDHUP;
/*      */   }
/*      */ 
/*      */   
/*      */   protected AbstractEpollChannel.AbstractEpollUnsafe newUnsafe() {
/*  121 */     return new EpollStreamUnsafe();
/*      */   }
/*      */ 
/*      */   
/*      */   public ChannelMetadata metadata() {
/*  126 */     return METADATA;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ChannelFuture spliceTo(AbstractEpollStreamChannel ch, int len) {
/*  144 */     return spliceTo(ch, len, newPromise());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ChannelFuture spliceTo(AbstractEpollStreamChannel ch, int len, ChannelPromise promise) {
/*  163 */     if (ch.eventLoop() != eventLoop()) {
/*  164 */       throw new IllegalArgumentException("EventLoops are not the same.");
/*      */     }
/*  166 */     if (len < 0) {
/*  167 */       throw new IllegalArgumentException("len: " + len + " (expected: >= 0)");
/*      */     }
/*  169 */     if (ch.config().getEpollMode() != EpollMode.LEVEL_TRIGGERED || 
/*  170 */       config().getEpollMode() != EpollMode.LEVEL_TRIGGERED) {
/*  171 */       throw new IllegalStateException("spliceTo() supported only when using " + EpollMode.LEVEL_TRIGGERED);
/*      */     }
/*  173 */     ObjectUtil.checkNotNull(promise, "promise");
/*  174 */     if (!isOpen()) {
/*  175 */       promise.tryFailure(SPLICE_TO_CLOSED_CHANNEL_EXCEPTION);
/*      */     } else {
/*  177 */       addToSpliceQueue(new SpliceInChannelTask(ch, len, promise));
/*  178 */       failSpliceIfClosed(promise);
/*      */     } 
/*  180 */     return (ChannelFuture)promise;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ChannelFuture spliceTo(FileDescriptor ch, int offset, int len) {
/*  198 */     return spliceTo(ch, offset, len, newPromise());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ChannelFuture spliceTo(FileDescriptor ch, int offset, int len, ChannelPromise promise) {
/*  217 */     if (len < 0) {
/*  218 */       throw new IllegalArgumentException("len: " + len + " (expected: >= 0)");
/*      */     }
/*  220 */     if (offset < 0) {
/*  221 */       throw new IllegalArgumentException("offset must be >= 0 but was " + offset);
/*      */     }
/*  223 */     if (config().getEpollMode() != EpollMode.LEVEL_TRIGGERED) {
/*  224 */       throw new IllegalStateException("spliceTo() supported only when using " + EpollMode.LEVEL_TRIGGERED);
/*      */     }
/*  226 */     ObjectUtil.checkNotNull(promise, "promise");
/*  227 */     if (!isOpen()) {
/*  228 */       promise.tryFailure(SPLICE_TO_CLOSED_CHANNEL_EXCEPTION);
/*      */     } else {
/*  230 */       addToSpliceQueue(new SpliceFdTask(ch, offset, len, promise));
/*  231 */       failSpliceIfClosed(promise);
/*      */     } 
/*  233 */     return (ChannelFuture)promise;
/*      */   }
/*      */   
/*      */   private void failSpliceIfClosed(ChannelPromise promise) {
/*  237 */     if (!isOpen())
/*      */     {
/*      */       
/*  240 */       if (promise.tryFailure(FAIL_SPLICE_IF_CLOSED_CLOSED_CHANNEL_EXCEPTION)) {
/*  241 */         eventLoop().execute(new Runnable()
/*      */             {
/*      */               public void run()
/*      */               {
/*  245 */                 AbstractEpollStreamChannel.this.clearSpliceQueue();
/*      */               }
/*      */             });
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int writeBytes(ChannelOutboundBuffer in, ByteBuf buf) throws Exception {
/*  267 */     int readableBytes = buf.readableBytes();
/*  268 */     if (readableBytes == 0) {
/*  269 */       in.remove();
/*  270 */       return 0;
/*      */     } 
/*      */     
/*  273 */     if (buf.hasMemoryAddress() || buf.nioBufferCount() == 1) {
/*  274 */       return doWriteBytes(in, buf);
/*      */     }
/*  276 */     ByteBuffer[] nioBuffers = buf.nioBuffers();
/*  277 */     return writeBytesMultiple(in, nioBuffers, nioBuffers.length, readableBytes, 
/*  278 */         config().getMaxBytesPerGatheringWrite());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void adjustMaxBytesPerGatheringWrite(long attempted, long written, long oldMaxBytesPerGatheringWrite) {
/*  286 */     if (attempted == written) {
/*  287 */       if (attempted << 1L > oldMaxBytesPerGatheringWrite) {
/*  288 */         config().setMaxBytesPerGatheringWrite(attempted << 1L);
/*      */       }
/*  290 */     } else if (attempted > 4096L && written < attempted >>> 1L) {
/*  291 */       config().setMaxBytesPerGatheringWrite(attempted >>> 1L);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int writeBytesMultiple(ChannelOutboundBuffer in, IovArray array) throws IOException {
/*  311 */     long expectedWrittenBytes = array.size();
/*  312 */     assert expectedWrittenBytes != 0L;
/*  313 */     int cnt = array.count();
/*  314 */     assert cnt != 0;
/*      */     
/*  316 */     long localWrittenBytes = this.socket.writevAddresses(array.memoryAddress(0), cnt);
/*  317 */     if (localWrittenBytes > 0L) {
/*  318 */       adjustMaxBytesPerGatheringWrite(expectedWrittenBytes, localWrittenBytes, array.maxBytes());
/*  319 */       in.removeBytes(localWrittenBytes);
/*  320 */       return 1;
/*      */     } 
/*  322 */     return Integer.MAX_VALUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int writeBytesMultiple(ChannelOutboundBuffer in, ByteBuffer[] nioBuffers, int nioBufferCnt, long expectedWrittenBytes, long maxBytesPerGatheringWrite) throws IOException {
/*  346 */     assert expectedWrittenBytes != 0L;
/*  347 */     if (expectedWrittenBytes > maxBytesPerGatheringWrite) {
/*  348 */       expectedWrittenBytes = maxBytesPerGatheringWrite;
/*      */     }
/*      */     
/*  351 */     long localWrittenBytes = this.socket.writev(nioBuffers, 0, nioBufferCnt, expectedWrittenBytes);
/*  352 */     if (localWrittenBytes > 0L) {
/*  353 */       adjustMaxBytesPerGatheringWrite(expectedWrittenBytes, localWrittenBytes, maxBytesPerGatheringWrite);
/*  354 */       in.removeBytes(localWrittenBytes);
/*  355 */       return 1;
/*      */     } 
/*  357 */     return Integer.MAX_VALUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int writeDefaultFileRegion(ChannelOutboundBuffer in, DefaultFileRegion region) throws Exception {
/*  375 */     long regionCount = region.count();
/*  376 */     if (region.transferred() >= regionCount) {
/*  377 */       in.remove();
/*  378 */       return 0;
/*      */     } 
/*      */     
/*  381 */     long offset = region.transferred();
/*  382 */     long flushedAmount = this.socket.sendFile(region, region.position(), offset, regionCount - offset);
/*  383 */     if (flushedAmount > 0L) {
/*  384 */       in.progress(flushedAmount);
/*  385 */       if (region.transferred() >= regionCount) {
/*  386 */         in.remove();
/*      */       }
/*  388 */       return 1;
/*      */     } 
/*  390 */     return Integer.MAX_VALUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int writeFileRegion(ChannelOutboundBuffer in, FileRegion region) throws Exception {
/*  408 */     if (region.transferred() >= region.count()) {
/*  409 */       in.remove();
/*  410 */       return 0;
/*      */     } 
/*      */     
/*  413 */     if (this.byteChannel == null) {
/*  414 */       this.byteChannel = (WritableByteChannel)new EpollSocketWritableByteChannel();
/*      */     }
/*  416 */     long flushedAmount = region.transferTo(this.byteChannel, region.transferred());
/*  417 */     if (flushedAmount > 0L) {
/*  418 */       in.progress(flushedAmount);
/*  419 */       if (region.transferred() >= region.count()) {
/*  420 */         in.remove();
/*      */       }
/*  422 */       return 1;
/*      */     } 
/*  424 */     return Integer.MAX_VALUE;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void doWrite(ChannelOutboundBuffer in) throws Exception {
/*  429 */     int writeSpinCount = config().getWriteSpinCount();
/*      */     do {
/*  431 */       int msgCount = in.size();
/*      */       
/*  433 */       if (msgCount > 1 && in.current() instanceof ByteBuf)
/*  434 */       { writeSpinCount -= doWriteMultiple(in); }
/*  435 */       else { if (msgCount == 0) {
/*      */           
/*  437 */           clearFlag(Native.EPOLLOUT);
/*      */           
/*      */           return;
/*      */         } 
/*  441 */         writeSpinCount -= doWriteSingle(in);
/*      */          }
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  447 */     while (writeSpinCount > 0);
/*      */     
/*  449 */     if (writeSpinCount == 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  454 */       clearFlag(Native.EPOLLOUT);
/*      */ 
/*      */       
/*  457 */       eventLoop().execute(this.flushTask);
/*      */     }
/*      */     else {
/*      */       
/*  461 */       setFlag(Native.EPOLLOUT);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int doWriteSingle(ChannelOutboundBuffer in) throws Exception {
/*  481 */     Object msg = in.current();
/*  482 */     if (msg instanceof ByteBuf)
/*  483 */       return writeBytes(in, (ByteBuf)msg); 
/*  484 */     if (msg instanceof DefaultFileRegion)
/*  485 */       return writeDefaultFileRegion(in, (DefaultFileRegion)msg); 
/*  486 */     if (msg instanceof FileRegion)
/*  487 */       return writeFileRegion(in, (FileRegion)msg); 
/*  488 */     if (msg instanceof SpliceOutTask) {
/*  489 */       if (!((SpliceOutTask)msg).spliceOut()) {
/*  490 */         return Integer.MAX_VALUE;
/*      */       }
/*  492 */       in.remove();
/*  493 */       return 1;
/*      */     } 
/*      */     
/*  496 */     throw new Error();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int doWriteMultiple(ChannelOutboundBuffer in) throws Exception {
/*  515 */     long maxBytesPerGatheringWrite = config().getMaxBytesPerGatheringWrite();
/*  516 */     if (PlatformDependent.hasUnsafe()) {
/*  517 */       IovArray array = ((EpollEventLoop)eventLoop()).cleanArray();
/*  518 */       array.maxBytes(maxBytesPerGatheringWrite);
/*  519 */       in.forEachFlushedMessage((ChannelOutboundBuffer.MessageProcessor)array);
/*      */       
/*  521 */       if (array.count() >= 1)
/*      */       {
/*  523 */         return writeBytesMultiple(in, array);
/*      */       }
/*      */     } else {
/*  526 */       ByteBuffer[] buffers = in.nioBuffers();
/*  527 */       int cnt = in.nioBufferCount();
/*  528 */       if (cnt >= 1)
/*      */       {
/*  530 */         return writeBytesMultiple(in, buffers, cnt, in.nioBufferSize(), maxBytesPerGatheringWrite);
/*      */       }
/*      */     } 
/*      */     
/*  534 */     in.removeBytes(0L);
/*  535 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Object filterOutboundMessage(Object msg) {
/*  540 */     if (msg instanceof ByteBuf) {
/*  541 */       ByteBuf buf = (ByteBuf)msg;
/*  542 */       return UnixChannelUtil.isBufferCopyNeededForWrite(buf) ? newDirectBuffer(buf) : buf;
/*      */     } 
/*      */     
/*  545 */     if (msg instanceof FileRegion || msg instanceof SpliceOutTask) {
/*  546 */       return msg;
/*      */     }
/*      */     
/*  549 */     throw new UnsupportedOperationException("unsupported message type: " + 
/*  550 */         StringUtil.simpleClassName(msg) + EXPECTED_TYPES);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void doShutdownOutput() throws Exception {
/*  556 */     this.socket.shutdown(false, true);
/*      */   }
/*      */   
/*      */   private void shutdownInput0(ChannelPromise promise) {
/*      */     try {
/*  561 */       this.socket.shutdown(true, false);
/*  562 */       promise.setSuccess();
/*  563 */     } catch (Throwable cause) {
/*  564 */       promise.setFailure(cause);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isOutputShutdown() {
/*  570 */     return this.socket.isOutputShutdown();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInputShutdown() {
/*  575 */     return this.socket.isInputShutdown();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isShutdown() {
/*  580 */     return this.socket.isShutdown();
/*      */   }
/*      */ 
/*      */   
/*      */   public ChannelFuture shutdownOutput() {
/*  585 */     return shutdownOutput(newPromise());
/*      */   }
/*      */ 
/*      */   
/*      */   public ChannelFuture shutdownOutput(final ChannelPromise promise) {
/*  590 */     EventLoop loop = eventLoop();
/*  591 */     if (loop.inEventLoop()) {
/*  592 */       ((AbstractChannel.AbstractUnsafe)unsafe()).shutdownOutput(promise);
/*      */     } else {
/*  594 */       loop.execute(new Runnable()
/*      */           {
/*      */             public void run() {
/*  597 */               ((AbstractChannel.AbstractUnsafe)AbstractEpollStreamChannel.this.unsafe()).shutdownOutput(promise);
/*      */             }
/*      */           });
/*      */     } 
/*      */     
/*  602 */     return (ChannelFuture)promise;
/*      */   }
/*      */ 
/*      */   
/*      */   public ChannelFuture shutdownInput() {
/*  607 */     return shutdownInput(newPromise());
/*      */   }
/*      */ 
/*      */   
/*      */   public ChannelFuture shutdownInput(final ChannelPromise promise) {
/*  612 */     Executor closeExecutor = ((EpollStreamUnsafe)unsafe()).prepareToClose();
/*  613 */     if (closeExecutor != null) {
/*  614 */       closeExecutor.execute(new Runnable()
/*      */           {
/*      */             public void run() {
/*  617 */               AbstractEpollStreamChannel.this.shutdownInput0(promise);
/*      */             }
/*      */           });
/*      */     } else {
/*  621 */       EventLoop loop = eventLoop();
/*  622 */       if (loop.inEventLoop()) {
/*  623 */         shutdownInput0(promise);
/*      */       } else {
/*  625 */         loop.execute(new Runnable()
/*      */             {
/*      */               public void run() {
/*  628 */                 AbstractEpollStreamChannel.this.shutdownInput0(promise);
/*      */               }
/*      */             });
/*      */       } 
/*      */     } 
/*  633 */     return (ChannelFuture)promise;
/*      */   }
/*      */ 
/*      */   
/*      */   public ChannelFuture shutdown() {
/*  638 */     return shutdown(newPromise());
/*      */   }
/*      */ 
/*      */   
/*      */   public ChannelFuture shutdown(final ChannelPromise promise) {
/*  643 */     ChannelFuture shutdownOutputFuture = shutdownOutput();
/*  644 */     if (shutdownOutputFuture.isDone()) {
/*  645 */       shutdownOutputDone(shutdownOutputFuture, promise);
/*      */     } else {
/*  647 */       shutdownOutputFuture.addListener((GenericFutureListener)new ChannelFutureListener()
/*      */           {
/*      */             public void operationComplete(ChannelFuture shutdownOutputFuture) throws Exception {
/*  650 */               AbstractEpollStreamChannel.this.shutdownOutputDone(shutdownOutputFuture, promise);
/*      */             }
/*      */           });
/*      */     } 
/*  654 */     return (ChannelFuture)promise;
/*      */   }
/*      */   
/*      */   private void shutdownOutputDone(final ChannelFuture shutdownOutputFuture, final ChannelPromise promise) {
/*  658 */     ChannelFuture shutdownInputFuture = shutdownInput();
/*  659 */     if (shutdownInputFuture.isDone()) {
/*  660 */       shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
/*      */     } else {
/*  662 */       shutdownInputFuture.addListener((GenericFutureListener)new ChannelFutureListener()
/*      */           {
/*      */             public void operationComplete(ChannelFuture shutdownInputFuture) throws Exception {
/*  665 */               AbstractEpollStreamChannel.shutdownDone(shutdownOutputFuture, shutdownInputFuture, promise);
/*      */             }
/*      */           });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void shutdownDone(ChannelFuture shutdownOutputFuture, ChannelFuture shutdownInputFuture, ChannelPromise promise) {
/*  674 */     Throwable shutdownOutputCause = shutdownOutputFuture.cause();
/*  675 */     Throwable shutdownInputCause = shutdownInputFuture.cause();
/*  676 */     if (shutdownOutputCause != null) {
/*  677 */       if (shutdownInputCause != null) {
/*  678 */         logger.debug("Exception suppressed because a previous exception occurred.", shutdownInputCause);
/*      */       }
/*      */       
/*  681 */       promise.setFailure(shutdownOutputCause);
/*  682 */     } else if (shutdownInputCause != null) {
/*  683 */       promise.setFailure(shutdownInputCause);
/*      */     } else {
/*  685 */       promise.setSuccess();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doClose() throws Exception {
/*      */     try {
/*  693 */       super.doClose();
/*      */     } finally {
/*  695 */       safeClosePipe(this.pipeIn);
/*  696 */       safeClosePipe(this.pipeOut);
/*  697 */       clearSpliceQueue();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void clearSpliceQueue() {
/*  702 */     if (this.spliceQueue == null) {
/*      */       return;
/*      */     }
/*      */     while (true) {
/*  706 */       SpliceInTask task = this.spliceQueue.poll();
/*  707 */       if (task == null) {
/*      */         break;
/*      */       }
/*  710 */       task.promise.tryFailure(CLEAR_SPLICE_QUEUE_CLOSED_CHANNEL_EXCEPTION);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void safeClosePipe(FileDescriptor fd) {
/*  715 */     if (fd != null) {
/*      */       try {
/*  717 */         fd.close();
/*  718 */       } catch (IOException e) {
/*  719 */         if (logger.isWarnEnabled()) {
/*  720 */           logger.warn("Error while closing a pipe", e);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   class EpollStreamUnsafe
/*      */     extends AbstractEpollChannel.AbstractEpollUnsafe
/*      */   {
/*      */     protected Executor prepareToClose() {
/*  730 */       return super.prepareToClose();
/*      */     }
/*      */ 
/*      */     
/*      */     private void handleReadException(ChannelPipeline pipeline, ByteBuf byteBuf, Throwable cause, boolean close, EpollRecvByteAllocatorHandle allocHandle) {
/*  735 */       if (byteBuf != null) {
/*  736 */         if (byteBuf.isReadable()) {
/*  737 */           this.readPending = false;
/*  738 */           pipeline.fireChannelRead(byteBuf);
/*      */         } else {
/*  740 */           byteBuf.release();
/*      */         } 
/*      */       }
/*  743 */       allocHandle.readComplete();
/*  744 */       pipeline.fireChannelReadComplete();
/*  745 */       pipeline.fireExceptionCaught(cause);
/*  746 */       if (close || cause instanceof IOException) {
/*  747 */         shutdownInput(false);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     EpollRecvByteAllocatorHandle newEpollHandle(RecvByteBufAllocator.ExtendedHandle handle) {
/*  753 */       return new EpollRecvByteAllocatorStreamingHandle(handle);
/*      */     }
/*      */ 
/*      */     
/*      */     void epollInReady() {
/*  758 */       EpollChannelConfig epollChannelConfig = AbstractEpollStreamChannel.this.config();
/*  759 */       if (AbstractEpollStreamChannel.this.shouldBreakEpollInReady((ChannelConfig)epollChannelConfig)) {
/*  760 */         clearEpollIn0();
/*      */         return;
/*      */       } 
/*  763 */       EpollRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
/*  764 */       allocHandle.edgeTriggered(AbstractEpollStreamChannel.this.isFlagSet(Native.EPOLLET));
/*      */       
/*  766 */       ChannelPipeline pipeline = AbstractEpollStreamChannel.this.pipeline();
/*  767 */       ByteBufAllocator allocator = epollChannelConfig.getAllocator();
/*  768 */       allocHandle.reset((ChannelConfig)epollChannelConfig);
/*  769 */       epollInBefore();
/*      */       
/*  771 */       ByteBuf byteBuf = null;
/*  772 */       boolean close = false;
/*      */       try {
/*      */         label37: do {
/*  775 */           if (AbstractEpollStreamChannel.this.spliceQueue != null) {
/*  776 */             AbstractEpollStreamChannel.SpliceInTask spliceTask = AbstractEpollStreamChannel.this.spliceQueue.peek();
/*  777 */             if (spliceTask != null) {
/*  778 */               if (spliceTask.spliceIn((RecvByteBufAllocator.Handle)allocHandle)) {
/*      */ 
/*      */                 
/*  781 */                 if (AbstractEpollStreamChannel.this.isActive()) {
/*  782 */                   AbstractEpollStreamChannel.this.spliceQueue.remove();
/*      */                 }
/*      */               } else {
/*      */                 break label37;
/*      */               } 
/*      */ 
/*      */               
/*      */               continue;
/*      */             } 
/*      */           } 
/*      */           
/*  793 */           byteBuf = allocHandle.allocate(allocator);
/*  794 */           allocHandle.lastBytesRead(AbstractEpollStreamChannel.this.doReadBytes(byteBuf));
/*  795 */           if (allocHandle.lastBytesRead() <= 0) {
/*      */             
/*  797 */             byteBuf.release();
/*  798 */             byteBuf = null;
/*  799 */             close = (allocHandle.lastBytesRead() < 0);
/*  800 */             if (close)
/*      */             {
/*  802 */               this.readPending = false;
/*      */             }
/*      */             break label37;
/*      */           } 
/*  806 */           allocHandle.incMessagesRead(1);
/*  807 */           this.readPending = false;
/*  808 */           pipeline.fireChannelRead(byteBuf);
/*  809 */           byteBuf = null;
/*      */           
/*  811 */           if (AbstractEpollStreamChannel.this.shouldBreakEpollInReady((ChannelConfig)epollChannelConfig))
/*      */           {
/*      */             break label37;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*  825 */         while (allocHandle.continueReading());
/*      */         
/*  827 */         allocHandle.readComplete();
/*  828 */         pipeline.fireChannelReadComplete();
/*      */         
/*  830 */         if (close) {
/*  831 */           shutdownInput(false);
/*      */         }
/*  833 */       } catch (Throwable t) {
/*  834 */         handleReadException(pipeline, byteBuf, t, close, allocHandle);
/*      */       } finally {
/*  836 */         epollInFinally((ChannelConfig)epollChannelConfig);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private void addToSpliceQueue(final SpliceInTask task) {
/*  842 */     EventLoop eventLoop = eventLoop();
/*  843 */     if (eventLoop.inEventLoop()) {
/*  844 */       addToSpliceQueue0(task);
/*      */     } else {
/*  846 */       eventLoop.execute(new Runnable()
/*      */           {
/*      */             public void run() {
/*  849 */               AbstractEpollStreamChannel.this.addToSpliceQueue0(task);
/*      */             }
/*      */           });
/*      */     } 
/*      */   }
/*      */   
/*      */   private void addToSpliceQueue0(SpliceInTask task) {
/*  856 */     if (this.spliceQueue == null) {
/*  857 */       this.spliceQueue = PlatformDependent.newMpscQueue();
/*      */     }
/*  859 */     this.spliceQueue.add(task);
/*      */   }
/*      */   
/*      */   protected abstract class SpliceInTask {
/*      */     final ChannelPromise promise;
/*      */     int len;
/*      */     
/*      */     protected SpliceInTask(int len, ChannelPromise promise) {
/*  867 */       this.promise = promise;
/*  868 */       this.len = len;
/*      */     }
/*      */ 
/*      */     
/*      */     abstract boolean spliceIn(RecvByteBufAllocator.Handle param1Handle);
/*      */     
/*      */     protected final int spliceIn(FileDescriptor pipeOut, RecvByteBufAllocator.Handle handle) throws IOException {
/*  875 */       int length = Math.min(handle.guess(), this.len);
/*  876 */       int splicedIn = 0;
/*      */       
/*      */       while (true) {
/*  879 */         int localSplicedIn = Native.splice(AbstractEpollStreamChannel.this.socket.intValue(), -1L, pipeOut.intValue(), -1L, length);
/*  880 */         if (localSplicedIn == 0) {
/*      */           break;
/*      */         }
/*  883 */         splicedIn += localSplicedIn;
/*  884 */         length -= localSplicedIn;
/*      */       } 
/*      */       
/*  887 */       return splicedIn;
/*      */     }
/*      */   }
/*      */   
/*      */   private final class SpliceInChannelTask
/*      */     extends SpliceInTask implements ChannelFutureListener {
/*      */     private final AbstractEpollStreamChannel ch;
/*      */     
/*      */     SpliceInChannelTask(AbstractEpollStreamChannel ch, int len, ChannelPromise promise) {
/*  896 */       super(len, promise);
/*  897 */       this.ch = ch;
/*      */     }
/*      */ 
/*      */     
/*      */     public void operationComplete(ChannelFuture future) throws Exception {
/*  902 */       if (!future.isSuccess()) {
/*  903 */         this.promise.setFailure(future.cause());
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean spliceIn(RecvByteBufAllocator.Handle handle) {
/*  909 */       assert this.ch.eventLoop().inEventLoop();
/*  910 */       if (this.len == 0) {
/*  911 */         this.promise.setSuccess();
/*  912 */         return true;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  918 */         FileDescriptor pipeOut = this.ch.pipeOut;
/*  919 */         if (pipeOut == null) {
/*      */           
/*  921 */           FileDescriptor[] pipe = FileDescriptor.pipe();
/*  922 */           this.ch.pipeIn = pipe[0];
/*  923 */           pipeOut = this.ch.pipeOut = pipe[1];
/*      */         } 
/*      */         
/*  926 */         int splicedIn = spliceIn(pipeOut, handle);
/*  927 */         if (splicedIn > 0) {
/*      */           ChannelPromise splicePromise;
/*  929 */           if (this.len != Integer.MAX_VALUE) {
/*  930 */             this.len -= splicedIn;
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  936 */           if (this.len == 0) {
/*  937 */             splicePromise = this.promise;
/*      */           } else {
/*  939 */             splicePromise = this.ch.newPromise().addListener((GenericFutureListener)this);
/*      */           } 
/*      */           
/*  942 */           boolean autoRead = AbstractEpollStreamChannel.this.config().isAutoRead();
/*      */ 
/*      */ 
/*      */           
/*  946 */           this.ch.unsafe().write(new AbstractEpollStreamChannel.SpliceOutTask(this.ch, splicedIn, autoRead), splicePromise);
/*  947 */           this.ch.unsafe().flush();
/*  948 */           if (autoRead && !splicePromise.isDone())
/*      */           {
/*      */ 
/*      */ 
/*      */             
/*  953 */             AbstractEpollStreamChannel.this.config().setAutoRead(false);
/*      */           }
/*      */         } 
/*      */         
/*  957 */         return (this.len == 0);
/*  958 */       } catch (Throwable cause) {
/*  959 */         this.promise.setFailure(cause);
/*  960 */         return true;
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private final class SpliceOutTask {
/*      */     private final AbstractEpollStreamChannel ch;
/*      */     private final boolean autoRead;
/*      */     private int len;
/*      */     
/*      */     SpliceOutTask(AbstractEpollStreamChannel ch, int len, boolean autoRead) {
/*  971 */       this.ch = ch;
/*  972 */       this.len = len;
/*  973 */       this.autoRead = autoRead;
/*      */     }
/*      */     
/*      */     public boolean spliceOut() throws Exception {
/*  977 */       assert this.ch.eventLoop().inEventLoop();
/*      */       try {
/*  979 */         int splicedOut = Native.splice(this.ch.pipeIn.intValue(), -1L, this.ch.socket.intValue(), -1L, this.len);
/*  980 */         this.len -= splicedOut;
/*  981 */         if (this.len == 0) {
/*  982 */           if (this.autoRead)
/*      */           {
/*  984 */             AbstractEpollStreamChannel.this.config().setAutoRead(true);
/*      */           }
/*  986 */           return true;
/*      */         } 
/*  988 */         return false;
/*  989 */       } catch (IOException e) {
/*  990 */         if (this.autoRead)
/*      */         {
/*  992 */           AbstractEpollStreamChannel.this.config().setAutoRead(true);
/*      */         }
/*  994 */         throw e;
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private final class SpliceFdTask extends SpliceInTask {
/*      */     private final FileDescriptor fd;
/*      */     private final ChannelPromise promise;
/*      */     private final int offset;
/*      */     
/*      */     SpliceFdTask(FileDescriptor fd, int offset, int len, ChannelPromise promise) {
/* 1005 */       super(len, promise);
/* 1006 */       this.fd = fd;
/* 1007 */       this.promise = promise;
/* 1008 */       this.offset = offset;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean spliceIn(RecvByteBufAllocator.Handle handle) {
/* 1013 */       assert AbstractEpollStreamChannel.this.eventLoop().inEventLoop();
/* 1014 */       if (this.len == 0) {
/* 1015 */         this.promise.setSuccess();
/* 1016 */         return true;
/*      */       } 
/*      */       
/*      */       try {
/* 1020 */         FileDescriptor[] pipe = FileDescriptor.pipe();
/* 1021 */         FileDescriptor pipeIn = pipe[0];
/* 1022 */         FileDescriptor pipeOut = pipe[1];
/*      */         try {
/* 1024 */           int splicedIn = spliceIn(pipeOut, handle);
/* 1025 */           if (splicedIn > 0) {
/*      */             
/* 1027 */             if (this.len != Integer.MAX_VALUE) {
/* 1028 */               this.len -= splicedIn;
/*      */             }
/*      */             while (true) {
/* 1031 */               int splicedOut = Native.splice(pipeIn.intValue(), -1L, this.fd.intValue(), this.offset, splicedIn);
/* 1032 */               splicedIn -= splicedOut;
/* 1033 */               if (splicedIn <= 0) {
/* 1034 */                 if (this.len == 0)
/* 1035 */                 { this.promise.setSuccess();
/* 1036 */                   splicedOut = 1; return splicedOut; }  break;
/*      */               } 
/*      */             } 
/* 1039 */           }  return false;
/*      */         } finally {
/* 1041 */           AbstractEpollStreamChannel.safeClosePipe(pipeIn);
/* 1042 */           AbstractEpollStreamChannel.safeClosePipe(pipeOut);
/*      */         } 
/* 1044 */       } catch (Throwable cause) {
/* 1045 */         this.promise.setFailure(cause);
/* 1046 */         return true;
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private final class EpollSocketWritableByteChannel extends SocketWritableByteChannel {
/*      */     EpollSocketWritableByteChannel() {
/* 1053 */       super((FileDescriptor)AbstractEpollStreamChannel.this.socket);
/*      */     }
/*      */ 
/*      */     
/*      */     protected ByteBufAllocator alloc() {
/* 1058 */       return AbstractEpollStreamChannel.this.alloc();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\AbstractEpollStreamChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */