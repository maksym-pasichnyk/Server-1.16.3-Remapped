/*    */ package io.netty.handler.codec.mqtt;
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
/*    */ public final class MqttPubAckMessage
/*    */   extends MqttMessage
/*    */ {
/*    */   public MqttPubAckMessage(MqttFixedHeader mqttFixedHeader, MqttMessageIdVariableHeader variableHeader) {
/* 25 */     super(mqttFixedHeader, variableHeader);
/*    */   }
/*    */ 
/*    */   
/*    */   public MqttMessageIdVariableHeader variableHeader() {
/* 30 */     return (MqttMessageIdVariableHeader)super.variableHeader();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttPubAckMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */