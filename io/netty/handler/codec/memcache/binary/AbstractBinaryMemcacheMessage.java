/*     */ package io.netty.handler.codec.memcache.binary;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.handler.codec.memcache.AbstractMemcacheObject;
/*     */ import io.netty.handler.codec.memcache.MemcacheMessage;
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
/*     */ public abstract class AbstractBinaryMemcacheMessage
/*     */   extends AbstractMemcacheObject
/*     */   implements BinaryMemcacheMessage
/*     */ {
/*     */   private ByteBuf key;
/*     */   private ByteBuf extras;
/*     */   private byte magic;
/*     */   private byte opcode;
/*     */   private short keyLength;
/*     */   private byte extrasLength;
/*     */   private byte dataType;
/*     */   private int totalBodyLength;
/*     */   private int opaque;
/*     */   private long cas;
/*     */   
/*     */   protected AbstractBinaryMemcacheMessage(ByteBuf key, ByteBuf extras) {
/*  56 */     this.key = key;
/*  57 */     this.keyLength = (key == null) ? 0 : (short)key.readableBytes();
/*  58 */     this.extras = extras;
/*  59 */     this.extrasLength = (extras == null) ? 0 : (byte)extras.readableBytes();
/*  60 */     this.totalBodyLength = this.keyLength + this.extrasLength;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf key() {
/*  65 */     return this.key;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf extras() {
/*  70 */     return this.extras;
/*     */   }
/*     */ 
/*     */   
/*     */   public BinaryMemcacheMessage setKey(ByteBuf key) {
/*  75 */     if (this.key != null) {
/*  76 */       this.key.release();
/*     */     }
/*  78 */     this.key = key;
/*  79 */     short oldKeyLength = this.keyLength;
/*  80 */     this.keyLength = (key == null) ? 0 : (short)key.readableBytes();
/*  81 */     this.totalBodyLength = this.totalBodyLength + this.keyLength - oldKeyLength;
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BinaryMemcacheMessage setExtras(ByteBuf extras) {
/*  87 */     if (this.extras != null) {
/*  88 */       this.extras.release();
/*     */     }
/*  90 */     this.extras = extras;
/*  91 */     short oldExtrasLength = (short)this.extrasLength;
/*  92 */     this.extrasLength = (extras == null) ? 0 : (byte)extras.readableBytes();
/*  93 */     this.totalBodyLength = this.totalBodyLength + this.extrasLength - oldExtrasLength;
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte magic() {
/*  99 */     return this.magic;
/*     */   }
/*     */ 
/*     */   
/*     */   public BinaryMemcacheMessage setMagic(byte magic) {
/* 104 */     this.magic = magic;
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public long cas() {
/* 110 */     return this.cas;
/*     */   }
/*     */ 
/*     */   
/*     */   public BinaryMemcacheMessage setCas(long cas) {
/* 115 */     this.cas = cas;
/* 116 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int opaque() {
/* 121 */     return this.opaque;
/*     */   }
/*     */ 
/*     */   
/*     */   public BinaryMemcacheMessage setOpaque(int opaque) {
/* 126 */     this.opaque = opaque;
/* 127 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int totalBodyLength() {
/* 132 */     return this.totalBodyLength;
/*     */   }
/*     */ 
/*     */   
/*     */   public BinaryMemcacheMessage setTotalBodyLength(int totalBodyLength) {
/* 137 */     this.totalBodyLength = totalBodyLength;
/* 138 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte dataType() {
/* 143 */     return this.dataType;
/*     */   }
/*     */ 
/*     */   
/*     */   public BinaryMemcacheMessage setDataType(byte dataType) {
/* 148 */     this.dataType = dataType;
/* 149 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte extrasLength() {
/* 154 */     return this.extrasLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BinaryMemcacheMessage setExtrasLength(byte extrasLength) {
/* 165 */     this.extrasLength = extrasLength;
/* 166 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public short keyLength() {
/* 171 */     return this.keyLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BinaryMemcacheMessage setKeyLength(short keyLength) {
/* 182 */     this.keyLength = keyLength;
/* 183 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte opcode() {
/* 188 */     return this.opcode;
/*     */   }
/*     */ 
/*     */   
/*     */   public BinaryMemcacheMessage setOpcode(byte opcode) {
/* 193 */     this.opcode = opcode;
/* 194 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BinaryMemcacheMessage retain() {
/* 199 */     super.retain();
/* 200 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BinaryMemcacheMessage retain(int increment) {
/* 205 */     super.retain(increment);
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void deallocate() {
/* 211 */     if (this.key != null) {
/* 212 */       this.key.release();
/*     */     }
/* 214 */     if (this.extras != null) {
/* 215 */       this.extras.release();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public BinaryMemcacheMessage touch() {
/* 221 */     super.touch();
/* 222 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BinaryMemcacheMessage touch(Object hint) {
/* 227 */     if (this.key != null) {
/* 228 */       this.key.touch(hint);
/*     */     }
/* 230 */     if (this.extras != null) {
/* 231 */       this.extras.touch(hint);
/*     */     }
/* 233 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\binary\AbstractBinaryMemcacheMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */