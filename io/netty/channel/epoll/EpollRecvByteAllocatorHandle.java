/*     */ package io.netty.channel.epoll;
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
/*     */ class EpollRecvByteAllocatorHandle
/*     */   implements RecvByteBufAllocator.ExtendedHandle
/*     */ {
/*     */   private final RecvByteBufAllocator.ExtendedHandle delegate;
/*     */   
/*  27 */   private final UncheckedBooleanSupplier defaultMaybeMoreDataSupplier = new UncheckedBooleanSupplier()
/*     */     {
/*     */       public boolean get() {
/*  30 */         return EpollRecvByteAllocatorHandle.this.maybeMoreDataToRead();
/*     */       }
/*     */     };
/*     */   
/*     */   private boolean isEdgeTriggered;
/*     */   
/*     */   EpollRecvByteAllocatorHandle(RecvByteBufAllocator.ExtendedHandle handle) {
/*  37 */     this.delegate = (RecvByteBufAllocator.ExtendedHandle)ObjectUtil.checkNotNull(handle, "handle");
/*     */   }
/*     */   private boolean receivedRdHup;
/*     */   final void receivedRdHup() {
/*  41 */     this.receivedRdHup = true;
/*     */   }
/*     */   
/*     */   final boolean isReceivedRdHup() {
/*  45 */     return this.receivedRdHup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean maybeMoreDataToRead() {
/*  56 */     return ((this.isEdgeTriggered && lastBytesRead() > 0) || (!this.isEdgeTriggered && 
/*  57 */       lastBytesRead() == attemptedBytesRead()) || this.receivedRdHup);
/*     */   }
/*     */ 
/*     */   
/*     */   final void edgeTriggered(boolean edgeTriggered) {
/*  62 */     this.isEdgeTriggered = edgeTriggered;
/*     */   }
/*     */   
/*     */   final boolean isEdgeTriggered() {
/*  66 */     return this.isEdgeTriggered;
/*     */   }
/*     */ 
/*     */   
/*     */   public final ByteBuf allocate(ByteBufAllocator alloc) {
/*  71 */     return this.delegate.allocate(alloc);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int guess() {
/*  76 */     return this.delegate.guess();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void reset(ChannelConfig config) {
/*  81 */     this.delegate.reset(config);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void incMessagesRead(int numMessages) {
/*  86 */     this.delegate.incMessagesRead(numMessages);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void lastBytesRead(int bytes) {
/*  91 */     this.delegate.lastBytesRead(bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int lastBytesRead() {
/*  96 */     return this.delegate.lastBytesRead();
/*     */   }
/*     */ 
/*     */   
/*     */   public final int attemptedBytesRead() {
/* 101 */     return this.delegate.attemptedBytesRead();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void attemptedBytesRead(int bytes) {
/* 106 */     this.delegate.attemptedBytesRead(bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void readComplete() {
/* 111 */     this.delegate.readComplete();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean continueReading(UncheckedBooleanSupplier maybeMoreDataSupplier) {
/* 116 */     return this.delegate.continueReading(maybeMoreDataSupplier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean continueReading() {
/* 122 */     return this.delegate.continueReading(this.defaultMaybeMoreDataSupplier);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\EpollRecvByteAllocatorHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */