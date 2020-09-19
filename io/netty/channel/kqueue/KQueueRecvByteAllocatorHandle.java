/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.util.UncheckedBooleanSupplier;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class KQueueRecvByteAllocatorHandle
/*     */   implements RecvByteBufAllocator.ExtendedHandle
/*     */ {
/*     */   private final RecvByteBufAllocator.ExtendedHandle delegate;
/*     */   
/*  30 */   private final UncheckedBooleanSupplier defaultMaybeMoreDataSupplier = new UncheckedBooleanSupplier()
/*     */     {
/*     */       public boolean get() {
/*  33 */         return KQueueRecvByteAllocatorHandle.this.maybeMoreDataToRead();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private boolean overrideGuess;
/*     */   
/*     */   KQueueRecvByteAllocatorHandle(RecvByteBufAllocator.ExtendedHandle handle) {
/*  41 */     this.delegate = (RecvByteBufAllocator.ExtendedHandle)ObjectUtil.checkNotNull(handle, "handle");
/*     */   }
/*     */   private boolean readEOF; private long numberBytesPending;
/*     */   
/*     */   public int guess() {
/*  46 */     return this.overrideGuess ? guess0() : this.delegate.guess();
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset(ChannelConfig config) {
/*  51 */     this.overrideGuess = ((KQueueChannelConfig)config).getRcvAllocTransportProvidesGuess();
/*  52 */     this.delegate.reset(config);
/*     */   }
/*     */ 
/*     */   
/*     */   public void incMessagesRead(int numMessages) {
/*  57 */     this.delegate.incMessagesRead(numMessages);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf allocate(ByteBufAllocator alloc) {
/*  62 */     return this.overrideGuess ? alloc.ioBuffer(guess0()) : this.delegate.allocate(alloc);
/*     */   }
/*     */ 
/*     */   
/*     */   public void lastBytesRead(int bytes) {
/*  67 */     this.numberBytesPending = (bytes < 0) ? 0L : Math.max(0L, this.numberBytesPending - bytes);
/*  68 */     this.delegate.lastBytesRead(bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastBytesRead() {
/*  73 */     return this.delegate.lastBytesRead();
/*     */   }
/*     */ 
/*     */   
/*     */   public void attemptedBytesRead(int bytes) {
/*  78 */     this.delegate.attemptedBytesRead(bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public int attemptedBytesRead() {
/*  83 */     return this.delegate.attemptedBytesRead();
/*     */   }
/*     */ 
/*     */   
/*     */   public void readComplete() {
/*  88 */     this.delegate.readComplete();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean continueReading(UncheckedBooleanSupplier maybeMoreDataSupplier) {
/*  93 */     return this.delegate.continueReading(maybeMoreDataSupplier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueReading() {
/*  99 */     return this.delegate.continueReading(this.defaultMaybeMoreDataSupplier);
/*     */   }
/*     */   
/*     */   void readEOF() {
/* 103 */     this.readEOF = true;
/*     */   }
/*     */   
/*     */   void numberBytesPending(long numberBytesPending) {
/* 107 */     this.numberBytesPending = numberBytesPending;
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
/*     */   boolean maybeMoreDataToRead() {
/* 121 */     return (this.numberBytesPending != 0L || this.readEOF);
/*     */   }
/*     */   
/*     */   private int guess0() {
/* 125 */     return (int)Math.min(this.numberBytesPending, 2147483647L);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueRecvByteAllocatorHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */