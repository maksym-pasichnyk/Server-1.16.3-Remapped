/*     */ package io.netty.handler.codec.redis;
/*     */ 
/*     */ import io.netty.util.AbstractReferenceCounted;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public class ArrayRedisMessage
/*     */   extends AbstractReferenceCounted
/*     */   implements RedisMessage
/*     */ {
/*     */   private final List<RedisMessage> children;
/*     */   
/*     */   private ArrayRedisMessage() {
/*  36 */     this.children = Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayRedisMessage(List<RedisMessage> children) {
/*  46 */     this.children = (List<RedisMessage>)ObjectUtil.checkNotNull(children, "children");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<RedisMessage> children() {
/*  55 */     return this.children;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNull() {
/*  64 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void deallocate() {
/*  69 */     for (RedisMessage msg : this.children) {
/*  70 */       ReferenceCountUtil.release(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayRedisMessage touch(Object hint) {
/*  76 */     for (RedisMessage msg : this.children) {
/*  77 */       ReferenceCountUtil.touch(msg);
/*     */     }
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  84 */     return StringUtil.simpleClassName(this) + '[' + 
/*  85 */       "children=" + 
/*  86 */       this.children
/*  87 */       .size() + ']';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public static final ArrayRedisMessage NULL_INSTANCE = new ArrayRedisMessage()
/*     */     {
/*     */       public boolean isNull() {
/*  97 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public ArrayRedisMessage retain() {
/* 102 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public ArrayRedisMessage retain(int increment) {
/* 107 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public ArrayRedisMessage touch() {
/* 112 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public ArrayRedisMessage touch(Object hint) {
/* 117 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean release() {
/* 122 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean release(int decrement) {
/* 127 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 132 */         return "NullArrayRedisMessage";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 139 */   public static final ArrayRedisMessage EMPTY_INSTANCE = new ArrayRedisMessage()
/*     */     {
/*     */       public ArrayRedisMessage retain()
/*     */       {
/* 143 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public ArrayRedisMessage retain(int increment) {
/* 148 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public ArrayRedisMessage touch() {
/* 153 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public ArrayRedisMessage touch(Object hint) {
/* 158 */         return this;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean release() {
/* 163 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean release(int decrement) {
/* 168 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 173 */         return "EmptyArrayRedisMessage";
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\ArrayRedisMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */