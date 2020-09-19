/*    */ package io.netty.handler.codec.redis;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class RedisConstants
/*    */ {
/*    */   static final int TYPE_LENGTH = 1;
/*    */   static final int EOL_LENGTH = 2;
/*    */   static final int NULL_LENGTH = 2;
/*    */   static final int NULL_VALUE = -1;
/*    */   static final int REDIS_MESSAGE_MAX_LENGTH = 536870912;
/*    */   static final int REDIS_INLINE_MESSAGE_MAX_LENGTH = 65536;
/*    */   static final int POSITIVE_LONG_MAX_LENGTH = 19;
/*    */   static final int LONG_MAX_LENGTH = 20;
/* 43 */   static final short NULL_SHORT = RedisCodecUtil.makeShort('-', '1');
/*    */   
/* 45 */   static final short EOL_SHORT = RedisCodecUtil.makeShort('\r', '\n');
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\RedisConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */