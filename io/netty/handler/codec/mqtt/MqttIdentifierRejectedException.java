/*    */ package io.netty.handler.codec.mqtt;
/*    */ 
/*    */ import io.netty.handler.codec.DecoderException;
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
/*    */ public final class MqttIdentifierRejectedException
/*    */   extends DecoderException
/*    */ {
/*    */   private static final long serialVersionUID = -1323503322689614981L;
/*    */   
/*    */   public MqttIdentifierRejectedException() {}
/*    */   
/*    */   public MqttIdentifierRejectedException(String message, Throwable cause) {
/* 36 */     super(message, cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MqttIdentifierRejectedException(String message) {
/* 43 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MqttIdentifierRejectedException(Throwable cause) {
/* 50 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttIdentifierRejectedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */