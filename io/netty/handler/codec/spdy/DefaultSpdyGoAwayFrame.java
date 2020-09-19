/*    */ package io.netty.handler.codec.spdy;
/*    */ 
/*    */ import io.netty.util.internal.StringUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultSpdyGoAwayFrame
/*    */   implements SpdyGoAwayFrame
/*    */ {
/*    */   private int lastGoodStreamId;
/*    */   private SpdySessionStatus status;
/*    */   
/*    */   public DefaultSpdyGoAwayFrame(int lastGoodStreamId) {
/* 34 */     this(lastGoodStreamId, 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultSpdyGoAwayFrame(int lastGoodStreamId, int statusCode) {
/* 44 */     this(lastGoodStreamId, SpdySessionStatus.valueOf(statusCode));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultSpdyGoAwayFrame(int lastGoodStreamId, SpdySessionStatus status) {
/* 54 */     setLastGoodStreamId(lastGoodStreamId);
/* 55 */     setStatus(status);
/*    */   }
/*    */ 
/*    */   
/*    */   public int lastGoodStreamId() {
/* 60 */     return this.lastGoodStreamId;
/*    */   }
/*    */ 
/*    */   
/*    */   public SpdyGoAwayFrame setLastGoodStreamId(int lastGoodStreamId) {
/* 65 */     if (lastGoodStreamId < 0) {
/* 66 */       throw new IllegalArgumentException("Last-good-stream-ID cannot be negative: " + lastGoodStreamId);
/*    */     }
/*    */     
/* 69 */     this.lastGoodStreamId = lastGoodStreamId;
/* 70 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public SpdySessionStatus status() {
/* 75 */     return this.status;
/*    */   }
/*    */ 
/*    */   
/*    */   public SpdyGoAwayFrame setStatus(SpdySessionStatus status) {
/* 80 */     this.status = status;
/* 81 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 86 */     return 
/* 87 */       StringUtil.simpleClassName(this) + StringUtil.NEWLINE + 
/* 88 */       "--> Last-good-stream-ID = " + 
/*    */       
/* 90 */       lastGoodStreamId() + StringUtil.NEWLINE + 
/* 91 */       "--> Status: " + 
/*    */       
/* 93 */       status();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\spdy\DefaultSpdyGoAwayFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */