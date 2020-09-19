/*     */ package io.netty.channel.oio;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public abstract class AbstractOioMessageChannel
/*     */   extends AbstractOioChannel
/*     */ {
/*  32 */   private final List<Object> readBuf = new ArrayList();
/*     */   
/*     */   protected AbstractOioMessageChannel(Channel parent) {
/*  35 */     super(parent);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doRead() {
/*  40 */     if (!this.readPending) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  47 */     this.readPending = false;
/*     */     
/*  49 */     ChannelConfig config = config();
/*  50 */     ChannelPipeline pipeline = pipeline();
/*  51 */     RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/*  52 */     allocHandle.reset(config);
/*     */     
/*  54 */     boolean closed = false;
/*  55 */     Throwable exception = null;
/*     */     
/*     */     try {
/*     */       do {
/*  59 */         int localRead = doReadMessages(this.readBuf);
/*  60 */         if (localRead == 0) {
/*     */           break;
/*     */         }
/*  63 */         if (localRead < 0) {
/*  64 */           closed = true;
/*     */           
/*     */           break;
/*     */         } 
/*  68 */         allocHandle.incMessagesRead(localRead);
/*  69 */       } while (allocHandle.continueReading());
/*  70 */     } catch (Throwable t) {
/*  71 */       exception = t;
/*     */     } 
/*     */     
/*  74 */     boolean readData = false;
/*  75 */     int size = this.readBuf.size();
/*  76 */     if (size > 0) {
/*  77 */       readData = true;
/*  78 */       for (int i = 0; i < size; i++) {
/*  79 */         this.readPending = false;
/*  80 */         pipeline.fireChannelRead(this.readBuf.get(i));
/*     */       } 
/*  82 */       this.readBuf.clear();
/*  83 */       allocHandle.readComplete();
/*  84 */       pipeline.fireChannelReadComplete();
/*     */     } 
/*     */     
/*  87 */     if (exception != null) {
/*  88 */       if (exception instanceof java.io.IOException) {
/*  89 */         closed = true;
/*     */       }
/*     */       
/*  92 */       pipeline.fireExceptionCaught(exception);
/*     */     } 
/*     */     
/*  95 */     if (closed) {
/*  96 */       if (isOpen()) {
/*  97 */         unsafe().close(unsafe().voidPromise());
/*     */       }
/*  99 */     } else if (this.readPending || config.isAutoRead() || (!readData && isActive())) {
/*     */ 
/*     */       
/* 102 */       read();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract int doReadMessages(List<Object> paramList) throws Exception;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\oio\AbstractOioMessageChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */