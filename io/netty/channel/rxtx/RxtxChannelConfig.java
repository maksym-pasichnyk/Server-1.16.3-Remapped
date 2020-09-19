/*     */ package io.netty.channel.rxtx;
/*     */ 
/*     */ @Deprecated
/*     */ public interface RxtxChannelConfig extends ChannelConfig {
/*     */   RxtxChannelConfig setBaudrate(int paramInt);
/*     */   
/*     */   RxtxChannelConfig setStopbits(Stopbits paramStopbits);
/*     */   
/*     */   RxtxChannelConfig setDatabits(Databits paramDatabits);
/*     */   
/*     */   RxtxChannelConfig setParitybit(Paritybit paramParitybit);
/*     */   
/*     */   int getBaudrate();
/*     */   
/*     */   Stopbits getStopbits();
/*     */   
/*     */   Databits getDatabits();
/*     */   
/*     */   Paritybit getParitybit();
/*     */   
/*     */   boolean isDtr();
/*     */   
/*     */   RxtxChannelConfig setDtr(boolean paramBoolean);
/*     */   
/*     */   boolean isRts();
/*     */   
/*     */   RxtxChannelConfig setRts(boolean paramBoolean);
/*     */   
/*     */   int getWaitTimeMillis();
/*     */   
/*     */   RxtxChannelConfig setWaitTimeMillis(int paramInt);
/*     */   
/*     */   RxtxChannelConfig setReadTimeout(int paramInt);
/*     */   
/*     */   int getReadTimeout();
/*     */   
/*     */   RxtxChannelConfig setConnectTimeoutMillis(int paramInt);
/*     */   
/*     */   @Deprecated
/*     */   RxtxChannelConfig setMaxMessagesPerRead(int paramInt);
/*     */   
/*     */   RxtxChannelConfig setWriteSpinCount(int paramInt);
/*     */   
/*     */   RxtxChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);
/*     */   
/*     */   RxtxChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator);
/*     */   
/*     */   RxtxChannelConfig setAutoRead(boolean paramBoolean);
/*     */   
/*     */   RxtxChannelConfig setAutoClose(boolean paramBoolean);
/*     */   
/*     */   RxtxChannelConfig setWriteBufferHighWaterMark(int paramInt);
/*     */   
/*     */   RxtxChannelConfig setWriteBufferLowWaterMark(int paramInt);
/*     */   
/*     */   RxtxChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark paramWriteBufferWaterMark);
/*     */   
/*     */   RxtxChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator);
/*     */   
/*     */   public enum Stopbits {
/*  61 */     STOPBITS_1(1),
/*     */ 
/*     */ 
/*     */     
/*  65 */     STOPBITS_2(2),
/*     */ 
/*     */ 
/*     */     
/*  69 */     STOPBITS_1_5(3);
/*     */     
/*     */     private final int value;
/*     */     
/*     */     Stopbits(int value) {
/*  74 */       this.value = value;
/*     */     }
/*     */     
/*     */     public int value() {
/*  78 */       return this.value;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Databits
/*     */   {
/*  95 */     DATABITS_5(5),
/*     */ 
/*     */ 
/*     */     
/*  99 */     DATABITS_6(6),
/*     */ 
/*     */ 
/*     */     
/* 103 */     DATABITS_7(7),
/*     */ 
/*     */ 
/*     */     
/* 107 */     DATABITS_8(8);
/*     */     
/*     */     private final int value;
/*     */     
/*     */     Databits(int value) {
/* 112 */       this.value = value;
/*     */     }
/*     */     
/*     */     public int value() {
/* 116 */       return this.value;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Paritybit
/*     */   {
/* 133 */     NONE(0),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     ODD(1),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     EVEN(2),
/*     */ 
/*     */ 
/*     */     
/* 147 */     MARK(3),
/*     */ 
/*     */ 
/*     */     
/* 151 */     SPACE(4);
/*     */     
/*     */     private final int value;
/*     */     
/*     */     Paritybit(int value) {
/* 156 */       this.value = value;
/*     */     }
/*     */     
/*     */     public int value() {
/* 160 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\rxtx\RxtxChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */