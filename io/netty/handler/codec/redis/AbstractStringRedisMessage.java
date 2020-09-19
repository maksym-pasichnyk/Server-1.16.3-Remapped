/*    */ package io.netty.handler.codec.redis;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ public abstract class AbstractStringRedisMessage
/*    */   implements RedisMessage
/*    */ {
/*    */   private final String content;
/*    */   
/*    */   AbstractStringRedisMessage(String content) {
/* 31 */     this.content = (String)ObjectUtil.checkNotNull(content, "content");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final String content() {
/* 40 */     return this.content;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 45 */     return StringUtil.simpleClassName(this) + '[' + 
/* 46 */       "content=" + 
/* 47 */       this.content + 
/* 48 */       ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\AbstractStringRedisMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */