/*    */ package io.netty.handler.codec.redis;
/*    */ 
/*    */ import io.netty.handler.codec.CodecException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class RedisCodecException
/*    */   extends CodecException
/*    */ {
/*    */   private static final long serialVersionUID = 5570454251549268063L;
/*    */   
/*    */   public RedisCodecException(String message) {
/* 33 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RedisCodecException(Throwable cause) {
/* 40 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\RedisCodecException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */