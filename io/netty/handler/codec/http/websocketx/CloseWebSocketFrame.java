/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.ReferenceCounted;
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
/*     */ public class CloseWebSocketFrame
/*     */   extends WebSocketFrame
/*     */ {
/*     */   public CloseWebSocketFrame() {
/*  32 */     super(Unpooled.buffer(0));
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
/*     */   public CloseWebSocketFrame(int statusCode, String reasonText) {
/*  45 */     this(true, 0, statusCode, reasonText);
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
/*     */   public CloseWebSocketFrame(boolean finalFragment, int rsv) {
/*  57 */     this(finalFragment, rsv, Unpooled.buffer(0));
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
/*     */   public CloseWebSocketFrame(boolean finalFragment, int rsv, int statusCode, String reasonText) {
/*  74 */     super(finalFragment, rsv, newBinaryData(statusCode, reasonText));
/*     */   }
/*     */   
/*     */   private static ByteBuf newBinaryData(int statusCode, String reasonText) {
/*  78 */     if (reasonText == null) {
/*  79 */       reasonText = "";
/*     */     }
/*     */     
/*  82 */     ByteBuf binaryData = Unpooled.buffer(2 + reasonText.length());
/*  83 */     binaryData.writeShort(statusCode);
/*  84 */     if (!reasonText.isEmpty()) {
/*  85 */       binaryData.writeCharSequence(reasonText, CharsetUtil.UTF_8);
/*     */     }
/*     */     
/*  88 */     binaryData.readerIndex(0);
/*  89 */     return binaryData;
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
/*     */   public CloseWebSocketFrame(boolean finalFragment, int rsv, ByteBuf binaryData) {
/* 103 */     super(finalFragment, rsv, binaryData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int statusCode() {
/* 111 */     ByteBuf binaryData = content();
/* 112 */     if (binaryData == null || binaryData.capacity() == 0) {
/* 113 */       return -1;
/*     */     }
/*     */     
/* 116 */     binaryData.readerIndex(0);
/* 117 */     int statusCode = binaryData.readShort();
/* 118 */     binaryData.readerIndex(0);
/*     */     
/* 120 */     return statusCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String reasonText() {
/* 128 */     ByteBuf binaryData = content();
/* 129 */     if (binaryData == null || binaryData.capacity() <= 2) {
/* 130 */       return "";
/*     */     }
/*     */     
/* 133 */     binaryData.readerIndex(2);
/* 134 */     String reasonText = binaryData.toString(CharsetUtil.UTF_8);
/* 135 */     binaryData.readerIndex(0);
/*     */     
/* 137 */     return reasonText;
/*     */   }
/*     */ 
/*     */   
/*     */   public CloseWebSocketFrame copy() {
/* 142 */     return (CloseWebSocketFrame)super.copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public CloseWebSocketFrame duplicate() {
/* 147 */     return (CloseWebSocketFrame)super.duplicate();
/*     */   }
/*     */ 
/*     */   
/*     */   public CloseWebSocketFrame retainedDuplicate() {
/* 152 */     return (CloseWebSocketFrame)super.retainedDuplicate();
/*     */   }
/*     */ 
/*     */   
/*     */   public CloseWebSocketFrame replace(ByteBuf content) {
/* 157 */     return new CloseWebSocketFrame(isFinalFragment(), rsv(), content);
/*     */   }
/*     */ 
/*     */   
/*     */   public CloseWebSocketFrame retain() {
/* 162 */     super.retain();
/* 163 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public CloseWebSocketFrame retain(int increment) {
/* 168 */     super.retain(increment);
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public CloseWebSocketFrame touch() {
/* 174 */     super.touch();
/* 175 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public CloseWebSocketFrame touch(Object hint) {
/* 180 */     super.touch(hint);
/* 181 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\CloseWebSocketFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */