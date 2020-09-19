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
/*    */ public final class MqttUnsubAckMessage
/*    */   extends MqttMessage
/*    */ {
/*    */   public MqttUnsubAckMessage(MqttFixedHeader mqttFixedHeader, MqttMessageIdVariableHeader variableHeader) {
/* 25 */     super(mqttFixedHeader, variableHeader, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public MqttMessageIdVariableHeader variableHeader() {
/* 30 */     return (MqttMessageIdVariableHeader)super.variableHeader();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttUnsubAckMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */