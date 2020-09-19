/*    */ package io.netty.handler.codec.redis;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
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
/*    */ public enum RedisMessageType
/*    */ {
/* 27 */   INLINE_COMMAND(null, true),
/* 28 */   SIMPLE_STRING(Byte.valueOf((byte)43), true),
/* 29 */   ERROR(Byte.valueOf((byte)45), true),
/* 30 */   INTEGER(Byte.valueOf((byte)58), true),
/* 31 */   BULK_STRING(Byte.valueOf((byte)36), false),
/* 32 */   ARRAY_HEADER(Byte.valueOf((byte)42), false);
/*    */   
/*    */   private final Byte value;
/*    */   private final boolean inline;
/*    */   
/*    */   RedisMessageType(Byte value, boolean inline) {
/* 38 */     this.value = value;
/* 39 */     this.inline = inline;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int length() {
/* 46 */     return (this.value != null) ? 1 : 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isInline() {
/* 54 */     return this.inline;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static RedisMessageType readFrom(ByteBuf in, boolean decodeInlineCommands) {
/* 61 */     int initialIndex = in.readerIndex();
/* 62 */     RedisMessageType type = valueOf(in.readByte());
/* 63 */     if (type == INLINE_COMMAND) {
/* 64 */       if (!decodeInlineCommands) {
/* 65 */         throw new RedisCodecException("Decoding of inline commands is disabled");
/*    */       }
/*    */       
/* 68 */       in.readerIndex(initialIndex);
/*    */     } 
/* 70 */     return type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeTo(ByteBuf out) {
/* 77 */     if (this.value == null) {
/*    */       return;
/*    */     }
/* 80 */     out.writeByte(this.value.byteValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\RedisMessageType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */