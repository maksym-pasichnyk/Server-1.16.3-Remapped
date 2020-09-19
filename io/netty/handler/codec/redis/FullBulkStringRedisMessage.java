/*     */ package io.netty.handler.codec.redis;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.buffer.DefaultByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
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
/*     */ public class FullBulkStringRedisMessage
/*     */   extends DefaultByteBufHolder
/*     */   implements LastBulkStringRedisContent
/*     */ {
/*     */   private FullBulkStringRedisMessage() {
/*  31 */     this(Unpooled.EMPTY_BUFFER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FullBulkStringRedisMessage(ByteBuf content) {
/*  42 */     super(content);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNull() {
/*  51 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  56 */     return StringUtil.simpleClassName(this) + '[' + 
/*  57 */       "content=" + 
/*     */       
/*  59 */       content() + ']';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   public static final FullBulkStringRedisMessage NULL_INSTANCE = new FullBulkStringRedisMessage()
/*     */     {
/*     */       public boolean isNull() {
/*  69 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public ByteBuf content() {
/*  74 */         return Unpooled.EMPTY_BUFFER;
/*     */       }
/*     */ 
/*     */       
/*     */       public FullBulkStringRedisMessage copy() {
/*  79 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public FullBulkStringRedisMessage duplicate() {
/*  84 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public FullBulkStringRedisMessage retainedDuplicate() {
/*  89 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public int refCnt() {
/*  94 */         return 1;
/*     */       }
/*     */ 
/*     */       
/*     */       public FullBulkStringRedisMessage retain() {
/*  99 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public FullBulkStringRedisMessage retain(int increment) {
/* 104 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public FullBulkStringRedisMessage touch() {
/* 109 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public FullBulkStringRedisMessage touch(Object hint) {
/* 114 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean release() {
/* 119 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean release(int decrement) {
/* 124 */         return false;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   public static final FullBulkStringRedisMessage EMPTY_INSTANCE = new FullBulkStringRedisMessage()
/*     */     {
/*     */       public ByteBuf content() {
/* 134 */         return Unpooled.EMPTY_BUFFER;
/*     */       }
/*     */ 
/*     */       
/*     */       public FullBulkStringRedisMessage copy() {
/* 139 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public FullBulkStringRedisMessage duplicate() {
/* 144 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public FullBulkStringRedisMessage retainedDuplicate() {
/* 149 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public int refCnt() {
/* 154 */         return 1;
/*     */       }
/*     */ 
/*     */       
/*     */       public FullBulkStringRedisMessage retain() {
/* 159 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public FullBulkStringRedisMessage retain(int increment) {
/* 164 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public FullBulkStringRedisMessage touch() {
/* 169 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public FullBulkStringRedisMessage touch(Object hint) {
/* 174 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean release() {
/* 179 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean release(int decrement) {
/* 184 */         return false;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public FullBulkStringRedisMessage copy() {
/* 190 */     return (FullBulkStringRedisMessage)super.copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public FullBulkStringRedisMessage duplicate() {
/* 195 */     return (FullBulkStringRedisMessage)super.duplicate();
/*     */   }
/*     */ 
/*     */   
/*     */   public FullBulkStringRedisMessage retainedDuplicate() {
/* 200 */     return (FullBulkStringRedisMessage)super.retainedDuplicate();
/*     */   }
/*     */ 
/*     */   
/*     */   public FullBulkStringRedisMessage replace(ByteBuf content) {
/* 205 */     return new FullBulkStringRedisMessage(content);
/*     */   }
/*     */ 
/*     */   
/*     */   public FullBulkStringRedisMessage retain() {
/* 210 */     super.retain();
/* 211 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FullBulkStringRedisMessage retain(int increment) {
/* 216 */     super.retain(increment);
/* 217 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FullBulkStringRedisMessage touch() {
/* 222 */     super.touch();
/* 223 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public FullBulkStringRedisMessage touch(Object hint) {
/* 228 */     super.touch(hint);
/* 229 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\FullBulkStringRedisMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */