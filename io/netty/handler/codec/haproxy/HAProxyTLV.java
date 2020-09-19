/*     */ package io.netty.handler.codec.haproxy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.DefaultByteBufHolder;
/*     */ import io.netty.util.ReferenceCounted;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HAProxyTLV
/*     */   extends DefaultByteBufHolder
/*     */ {
/*     */   private final Type type;
/*     */   private final byte typeByteValue;
/*     */   
/*     */   public enum Type
/*     */   {
/*  39 */     PP2_TYPE_ALPN,
/*  40 */     PP2_TYPE_AUTHORITY,
/*  41 */     PP2_TYPE_SSL,
/*  42 */     PP2_TYPE_SSL_VERSION,
/*  43 */     PP2_TYPE_SSL_CN,
/*  44 */     PP2_TYPE_NETNS,
/*     */ 
/*     */ 
/*     */     
/*  48 */     OTHER;
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
/*     */     public static Type typeForByteValue(byte byteValue) {
/*  60 */       switch (byteValue) {
/*     */         case 1:
/*  62 */           return PP2_TYPE_ALPN;
/*     */         case 2:
/*  64 */           return PP2_TYPE_AUTHORITY;
/*     */         case 32:
/*  66 */           return PP2_TYPE_SSL;
/*     */         case 33:
/*  68 */           return PP2_TYPE_SSL_VERSION;
/*     */         case 34:
/*  70 */           return PP2_TYPE_SSL_CN;
/*     */         case 48:
/*  72 */           return PP2_TYPE_NETNS;
/*     */       } 
/*  74 */       return OTHER;
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
/*     */   HAProxyTLV(Type type, byte typeByteValue, ByteBuf content) {
/*  87 */     super(content);
/*  88 */     ObjectUtil.checkNotNull(type, "type");
/*     */     
/*  90 */     this.type = type;
/*  91 */     this.typeByteValue = typeByteValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type type() {
/*  98 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte typeByteValue() {
/* 105 */     return this.typeByteValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public HAProxyTLV copy() {
/* 110 */     return replace(content().copy());
/*     */   }
/*     */ 
/*     */   
/*     */   public HAProxyTLV duplicate() {
/* 115 */     return replace(content().duplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public HAProxyTLV retainedDuplicate() {
/* 120 */     return replace(content().retainedDuplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public HAProxyTLV replace(ByteBuf content) {
/* 125 */     return new HAProxyTLV(this.type, this.typeByteValue, content);
/*     */   }
/*     */ 
/*     */   
/*     */   public HAProxyTLV retain() {
/* 130 */     super.retain();
/* 131 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HAProxyTLV retain(int increment) {
/* 136 */     super.retain(increment);
/* 137 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HAProxyTLV touch() {
/* 142 */     super.touch();
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public HAProxyTLV touch(Object hint) {
/* 148 */     super.touch(hint);
/* 149 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\haproxy\HAProxyTLV.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */