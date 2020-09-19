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
/*    */ 
/*    */ public final class MqttUnacceptableProtocolVersionException
/*    */   extends DecoderException
/*    */ {
/*    */   private static final long serialVersionUID = 4914652213232455749L;
/*    */   
/*    */   public MqttUnacceptableProtocolVersionException() {}
/*    */   
/*    */   public MqttUnacceptableProtocolVersionException(String message, Throwable cause) {
/* 37 */     super(message, cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MqttUnacceptableProtocolVersionException(String message) {
/* 44 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MqttUnacceptableProtocolVersionException(Throwable cause) {
/* 51 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttUnacceptableProtocolVersionException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */