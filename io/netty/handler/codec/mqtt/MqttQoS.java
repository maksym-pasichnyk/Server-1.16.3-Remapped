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
/*    */ public enum MqttQoS
/*    */ {
/* 19 */   AT_MOST_ONCE(0),
/* 20 */   AT_LEAST_ONCE(1),
/* 21 */   EXACTLY_ONCE(2),
/* 22 */   FAILURE(128);
/*    */   
/*    */   private final int value;
/*    */   
/*    */   MqttQoS(int value) {
/* 27 */     this.value = value;
/*    */   }
/*    */   
/*    */   public int value() {
/* 31 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttQoS.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */