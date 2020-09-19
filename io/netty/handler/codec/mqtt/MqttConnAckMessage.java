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
/*    */ public final class MqttConnAckMessage
/*    */   extends MqttMessage
/*    */ {
/*    */   public MqttConnAckMessage(MqttFixedHeader mqttFixedHeader, MqttConnAckVariableHeader variableHeader) {
/* 25 */     super(mqttFixedHeader, variableHeader);
/*    */   }
/*    */ 
/*    */   
/*    */   public MqttConnAckVariableHeader variableHeader() {
/* 30 */     return (MqttConnAckVariableHeader)super.variableHeader();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttConnAckMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */