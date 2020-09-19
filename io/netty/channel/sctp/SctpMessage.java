/*     */ package io.netty.channel.sctp;
/*     */ 
/*     */ import com.sun.nio.sctp.MessageInfo;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.DefaultByteBufHolder;
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
/*     */ 
/*     */ 
/*     */ public final class SctpMessage
/*     */   extends DefaultByteBufHolder
/*     */ {
/*     */   private final int streamIdentifier;
/*     */   private final int protocolIdentifier;
/*     */   private final boolean unordered;
/*     */   private final MessageInfo msgInfo;
/*     */   
/*     */   public SctpMessage(int protocolIdentifier, int streamIdentifier, ByteBuf payloadBuffer) {
/*  39 */     this(protocolIdentifier, streamIdentifier, false, payloadBuffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SctpMessage(int protocolIdentifier, int streamIdentifier, boolean unordered, ByteBuf payloadBuffer) {
/*  50 */     super(payloadBuffer);
/*  51 */     this.protocolIdentifier = protocolIdentifier;
/*  52 */     this.streamIdentifier = streamIdentifier;
/*  53 */     this.unordered = unordered;
/*  54 */     this.msgInfo = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SctpMessage(MessageInfo msgInfo, ByteBuf payloadBuffer) {
/*  63 */     super(payloadBuffer);
/*  64 */     if (msgInfo == null) {
/*  65 */       throw new NullPointerException("msgInfo");
/*     */     }
/*  67 */     this.msgInfo = msgInfo;
/*  68 */     this.streamIdentifier = msgInfo.streamNumber();
/*  69 */     this.protocolIdentifier = msgInfo.payloadProtocolID();
/*  70 */     this.unordered = msgInfo.isUnordered();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int streamIdentifier() {
/*  77 */     return this.streamIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int protocolIdentifier() {
/*  84 */     return this.protocolIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnordered() {
/*  91 */     return this.unordered;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageInfo messageInfo() {
/*  99 */     return this.msgInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/* 106 */     if (this.msgInfo != null) {
/* 107 */       return this.msgInfo.isComplete();
/*     */     }
/*     */     
/* 110 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 116 */     if (this == o) {
/* 117 */       return true;
/*     */     }
/*     */     
/* 120 */     if (o == null || getClass() != o.getClass()) {
/* 121 */       return false;
/*     */     }
/*     */     
/* 124 */     SctpMessage sctpFrame = (SctpMessage)o;
/*     */     
/* 126 */     if (this.protocolIdentifier != sctpFrame.protocolIdentifier) {
/* 127 */       return false;
/*     */     }
/*     */     
/* 130 */     if (this.streamIdentifier != sctpFrame.streamIdentifier) {
/* 131 */       return false;
/*     */     }
/*     */     
/* 134 */     if (this.unordered != sctpFrame.unordered) {
/* 135 */       return false;
/*     */     }
/*     */     
/* 138 */     return content().equals(sctpFrame.content());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 143 */     int result = this.streamIdentifier;
/* 144 */     result = 31 * result + this.protocolIdentifier;
/*     */     
/* 146 */     result = 31 * result + (this.unordered ? 1231 : 1237);
/* 147 */     result = 31 * result + content().hashCode();
/* 148 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpMessage copy() {
/* 153 */     return (SctpMessage)super.copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpMessage duplicate() {
/* 158 */     return (SctpMessage)super.duplicate();
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpMessage retainedDuplicate() {
/* 163 */     return (SctpMessage)super.retainedDuplicate();
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpMessage replace(ByteBuf content) {
/* 168 */     if (this.msgInfo == null) {
/* 169 */       return new SctpMessage(this.protocolIdentifier, this.streamIdentifier, this.unordered, content);
/*     */     }
/* 171 */     return new SctpMessage(this.msgInfo, content);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SctpMessage retain() {
/* 177 */     super.retain();
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpMessage retain(int increment) {
/* 183 */     super.retain(increment);
/* 184 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpMessage touch() {
/* 189 */     super.touch();
/* 190 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SctpMessage touch(Object hint) {
/* 195 */     super.touch(hint);
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 201 */     return "SctpFrame{streamIdentifier=" + this.streamIdentifier + ", protocolIdentifier=" + this.protocolIdentifier + ", unordered=" + this.unordered + ", data=" + 
/*     */ 
/*     */       
/* 204 */       contentToString() + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\sctp\SctpMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */