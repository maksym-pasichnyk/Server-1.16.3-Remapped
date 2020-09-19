/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.IllegalReferenceCountException;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public class DefaultSpdyDataFrame
/*     */   extends DefaultSpdyStreamFrame
/*     */   implements SpdyDataFrame
/*     */ {
/*     */   private final ByteBuf data;
/*     */   
/*     */   public DefaultSpdyDataFrame(int streamId) {
/*  36 */     this(streamId, Unpooled.buffer(0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultSpdyDataFrame(int streamId, ByteBuf data) {
/*  46 */     super(streamId);
/*  47 */     if (data == null) {
/*  48 */       throw new NullPointerException("data");
/*     */     }
/*  50 */     this.data = validate(data);
/*     */   }
/*     */   
/*     */   private static ByteBuf validate(ByteBuf data) {
/*  54 */     if (data.readableBytes() > 16777215) {
/*  55 */       throw new IllegalArgumentException("data payload cannot exceed 16777215 bytes");
/*     */     }
/*     */     
/*  58 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdyDataFrame setStreamId(int streamId) {
/*  63 */     super.setStreamId(streamId);
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdyDataFrame setLast(boolean last) {
/*  69 */     super.setLast(last);
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf content() {
/*  75 */     if (this.data.refCnt() <= 0) {
/*  76 */       throw new IllegalReferenceCountException(this.data.refCnt());
/*     */     }
/*  78 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdyDataFrame copy() {
/*  83 */     return replace(content().copy());
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdyDataFrame duplicate() {
/*  88 */     return replace(content().duplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdyDataFrame retainedDuplicate() {
/*  93 */     return replace(content().retainedDuplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdyDataFrame replace(ByteBuf content) {
/*  98 */     SpdyDataFrame frame = new DefaultSpdyDataFrame(streamId(), content);
/*  99 */     frame.setLast(isLast());
/* 100 */     return frame;
/*     */   }
/*     */ 
/*     */   
/*     */   public int refCnt() {
/* 105 */     return this.data.refCnt();
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdyDataFrame retain() {
/* 110 */     this.data.retain();
/* 111 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdyDataFrame retain(int increment) {
/* 116 */     this.data.retain(increment);
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdyDataFrame touch() {
/* 122 */     this.data.touch();
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdyDataFrame touch(Object hint) {
/* 128 */     this.data.touch(hint);
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release() {
/* 134 */     return this.data.release();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release(int decrement) {
/* 139 */     return this.data.release(decrement);
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
/*     */   public String toString() {
/* 153 */     StringBuilder buf = (new StringBuilder()).append(StringUtil.simpleClassName(this)).append("(last: ").append(isLast()).append(')').append(StringUtil.NEWLINE).append("--> Stream-ID = ").append(streamId()).append(StringUtil.NEWLINE).append("--> Size = ");
/* 154 */     if (refCnt() == 0) {
/* 155 */       buf.append("(freed)");
/*     */     } else {
/* 157 */       buf.append(content().readableBytes());
/*     */     } 
/* 159 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\spdy\DefaultSpdyDataFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */