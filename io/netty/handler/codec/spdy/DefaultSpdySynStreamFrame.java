/*     */ package io.netty.handler.codec.spdy;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultSpdySynStreamFrame
/*     */   extends DefaultSpdyHeadersFrame
/*     */   implements SpdySynStreamFrame
/*     */ {
/*     */   private int associatedStreamId;
/*     */   private byte priority;
/*     */   private boolean unidirectional;
/*     */   
/*     */   public DefaultSpdySynStreamFrame(int streamId, int associatedStreamId, byte priority) {
/*  38 */     this(streamId, associatedStreamId, priority, true);
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
/*     */   public DefaultSpdySynStreamFrame(int streamId, int associatedStreamId, byte priority, boolean validateHeaders) {
/*  50 */     super(streamId, validateHeaders);
/*  51 */     setAssociatedStreamId(associatedStreamId);
/*  52 */     setPriority(priority);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdySynStreamFrame setStreamId(int streamId) {
/*  57 */     super.setStreamId(streamId);
/*  58 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdySynStreamFrame setLast(boolean last) {
/*  63 */     super.setLast(last);
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdySynStreamFrame setInvalid() {
/*  69 */     super.setInvalid();
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int associatedStreamId() {
/*  75 */     return this.associatedStreamId;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdySynStreamFrame setAssociatedStreamId(int associatedStreamId) {
/*  80 */     if (associatedStreamId < 0) {
/*  81 */       throw new IllegalArgumentException("Associated-To-Stream-ID cannot be negative: " + associatedStreamId);
/*     */     }
/*     */ 
/*     */     
/*  85 */     this.associatedStreamId = associatedStreamId;
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte priority() {
/*  91 */     return this.priority;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdySynStreamFrame setPriority(byte priority) {
/*  96 */     if (priority < 0 || priority > 7) {
/*  97 */       throw new IllegalArgumentException("Priority must be between 0 and 7 inclusive: " + priority);
/*     */     }
/*     */     
/* 100 */     this.priority = priority;
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnidirectional() {
/* 106 */     return this.unidirectional;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpdySynStreamFrame setUnidirectional(boolean unidirectional) {
/* 111 */     this.unidirectional = unidirectional;
/* 112 */     return this;
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
/*     */   public String toString() {
/* 127 */     StringBuilder buf = (new StringBuilder()).append(StringUtil.simpleClassName(this)).append("(last: ").append(isLast()).append("; unidirectional: ").append(isUnidirectional()).append(')').append(StringUtil.NEWLINE).append("--> Stream-ID = ").append(streamId()).append(StringUtil.NEWLINE);
/* 128 */     if (this.associatedStreamId != 0) {
/* 129 */       buf.append("--> Associated-To-Stream-ID = ")
/* 130 */         .append(associatedStreamId())
/* 131 */         .append(StringUtil.NEWLINE);
/*     */     }
/* 133 */     buf.append("--> Priority = ")
/* 134 */       .append(priority())
/* 135 */       .append(StringUtil.NEWLINE)
/* 136 */       .append("--> Headers:")
/* 137 */       .append(StringUtil.NEWLINE);
/* 138 */     appendHeaders(buf);
/*     */ 
/*     */     
/* 141 */     buf.setLength(buf.length() - StringUtil.NEWLINE.length());
/* 142 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\spdy\DefaultSpdySynStreamFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */