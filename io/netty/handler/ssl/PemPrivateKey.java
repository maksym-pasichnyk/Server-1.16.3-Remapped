/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.AbstractReferenceCounted;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.IllegalReferenceCountException;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.security.PrivateKey;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PemPrivateKey
/*     */   extends AbstractReferenceCounted
/*     */   implements PrivateKey, PemEncoded
/*     */ {
/*     */   private static final long serialVersionUID = 7978017465645018936L;
/*  46 */   private static final byte[] BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n".getBytes(CharsetUtil.US_ASCII);
/*  47 */   private static final byte[] END_PRIVATE_KEY = "\n-----END PRIVATE KEY-----\n".getBytes(CharsetUtil.US_ASCII);
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String PKCS8_FORMAT = "PKCS#8";
/*     */ 
/*     */   
/*     */   private final ByteBuf content;
/*     */ 
/*     */ 
/*     */   
/*     */   static PemEncoded toPEM(ByteBufAllocator allocator, boolean useDirect, PrivateKey key) {
/*  59 */     if (key instanceof PemEncoded) {
/*  60 */       return ((PemEncoded)key).retain();
/*     */     }
/*     */     
/*  63 */     ByteBuf encoded = Unpooled.wrappedBuffer(key.getEncoded());
/*     */     try {
/*  65 */       ByteBuf base64 = SslUtils.toBase64(allocator, encoded);
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
/*     */     }
/*     */     finally {
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
/*  89 */       SslUtils.zerooutAndRelease(encoded);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PemPrivateKey valueOf(byte[] key) {
/* 100 */     return valueOf(Unpooled.wrappedBuffer(key));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PemPrivateKey valueOf(ByteBuf key) {
/* 110 */     return new PemPrivateKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private PemPrivateKey(ByteBuf content) {
/* 116 */     this.content = (ByteBuf)ObjectUtil.checkNotNull(content, "content");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSensitive() {
/* 121 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf content() {
/* 126 */     int count = refCnt();
/* 127 */     if (count <= 0) {
/* 128 */       throw new IllegalReferenceCountException(count);
/*     */     }
/*     */     
/* 131 */     return this.content;
/*     */   }
/*     */ 
/*     */   
/*     */   public PemPrivateKey copy() {
/* 136 */     return replace(this.content.copy());
/*     */   }
/*     */ 
/*     */   
/*     */   public PemPrivateKey duplicate() {
/* 141 */     return replace(this.content.duplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public PemPrivateKey retainedDuplicate() {
/* 146 */     return replace(this.content.retainedDuplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public PemPrivateKey replace(ByteBuf content) {
/* 151 */     return new PemPrivateKey(content);
/*     */   }
/*     */ 
/*     */   
/*     */   public PemPrivateKey touch() {
/* 156 */     this.content.touch();
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public PemPrivateKey touch(Object hint) {
/* 162 */     this.content.touch(hint);
/* 163 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public PemPrivateKey retain() {
/* 168 */     return (PemPrivateKey)super.retain();
/*     */   }
/*     */ 
/*     */   
/*     */   public PemPrivateKey retain(int increment) {
/* 173 */     return (PemPrivateKey)super.retain(increment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void deallocate() {
/* 180 */     SslUtils.zerooutAndRelease(this.content);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getEncoded() {
/* 185 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAlgorithm() {
/* 190 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 195 */     return "PKCS#8";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 206 */     release(refCnt());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDestroyed() {
/* 217 */     return (refCnt() == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\ssl\PemPrivateKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */