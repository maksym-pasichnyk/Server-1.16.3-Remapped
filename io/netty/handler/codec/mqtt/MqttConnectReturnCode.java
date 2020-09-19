/*    */ package io.netty.handler.codec.mqtt;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public enum MqttConnectReturnCode
/*    */ {
/* 27 */   CONNECTION_ACCEPTED((byte)0),
/* 28 */   CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION((byte)1),
/* 29 */   CONNECTION_REFUSED_IDENTIFIER_REJECTED((byte)2),
/* 30 */   CONNECTION_REFUSED_SERVER_UNAVAILABLE((byte)3),
/* 31 */   CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD((byte)4),
/* 32 */   CONNECTION_REFUSED_NOT_AUTHORIZED((byte)5);
/*    */   private static final Map<Byte, MqttConnectReturnCode> VALUE_TO_CODE_MAP;
/*    */   private final byte byteValue;
/*    */   
/*    */   static {
/* 37 */     Map<Byte, MqttConnectReturnCode> valueMap = new HashMap<Byte, MqttConnectReturnCode>();
/* 38 */     for (MqttConnectReturnCode code : values()) {
/* 39 */       valueMap.put(Byte.valueOf(code.byteValue), code);
/*    */     }
/* 41 */     VALUE_TO_CODE_MAP = Collections.unmodifiableMap(valueMap);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   MqttConnectReturnCode(byte byteValue) {
/* 47 */     this.byteValue = byteValue;
/*    */   }
/*    */   
/*    */   public byte byteValue() {
/* 51 */     return this.byteValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttConnectReturnCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */