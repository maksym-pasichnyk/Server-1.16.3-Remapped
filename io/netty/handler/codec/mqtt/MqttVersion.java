/*    */ package io.netty.handler.codec.mqtt;
/*    */ 
/*    */ import io.netty.util.CharsetUtil;
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ public enum MqttVersion
/*    */ {
/* 26 */   MQTT_3_1("MQIsdp", (byte)3),
/* 27 */   MQTT_3_1_1("MQTT", (byte)4);
/*    */   
/*    */   private final String name;
/*    */   private final byte level;
/*    */   
/*    */   MqttVersion(String protocolName, byte protocolLevel) {
/* 33 */     this.name = (String)ObjectUtil.checkNotNull(protocolName, "protocolName");
/* 34 */     this.level = protocolLevel;
/*    */   }
/*    */   
/*    */   public String protocolName() {
/* 38 */     return this.name;
/*    */   }
/*    */   
/*    */   public byte[] protocolNameBytes() {
/* 42 */     return this.name.getBytes(CharsetUtil.UTF_8);
/*    */   }
/*    */   
/*    */   public byte protocolLevel() {
/* 46 */     return this.level;
/*    */   }
/*    */   
/*    */   public static MqttVersion fromProtocolNameAndLevel(String protocolName, byte protocolLevel) {
/* 50 */     for (MqttVersion mv : values()) {
/* 51 */       if (mv.name.equals(protocolName)) {
/* 52 */         if (mv.level == protocolLevel) {
/* 53 */           return mv;
/*    */         }
/* 55 */         throw new MqttUnacceptableProtocolVersionException(protocolName + " and " + protocolLevel + " are not match");
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 60 */     throw new MqttUnacceptableProtocolVersionException(protocolName + "is unknown protocol name");
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */