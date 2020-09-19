/*    */ package io.netty.handler.codec.mqtt;
/*    */ 
/*    */ import io.netty.handler.codec.DecoderResult;
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ public class MqttMessage
/*    */ {
/*    */   private final MqttFixedHeader mqttFixedHeader;
/*    */   private final Object variableHeader;
/*    */   private final Object payload;
/*    */   private final DecoderResult decoderResult;
/*    */   
/*    */   public MqttMessage(MqttFixedHeader mqttFixedHeader) {
/* 33 */     this(mqttFixedHeader, null, null);
/*    */   }
/*    */   
/*    */   public MqttMessage(MqttFixedHeader mqttFixedHeader, Object variableHeader) {
/* 37 */     this(mqttFixedHeader, variableHeader, null);
/*    */   }
/*    */   
/*    */   public MqttMessage(MqttFixedHeader mqttFixedHeader, Object variableHeader, Object payload) {
/* 41 */     this(mqttFixedHeader, variableHeader, payload, DecoderResult.SUCCESS);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MqttMessage(MqttFixedHeader mqttFixedHeader, Object variableHeader, Object payload, DecoderResult decoderResult) {
/* 49 */     this.mqttFixedHeader = mqttFixedHeader;
/* 50 */     this.variableHeader = variableHeader;
/* 51 */     this.payload = payload;
/* 52 */     this.decoderResult = decoderResult;
/*    */   }
/*    */   
/*    */   public MqttFixedHeader fixedHeader() {
/* 56 */     return this.mqttFixedHeader;
/*    */   }
/*    */   
/*    */   public Object variableHeader() {
/* 60 */     return this.variableHeader;
/*    */   }
/*    */   
/*    */   public Object payload() {
/* 64 */     return this.payload;
/*    */   }
/*    */   
/*    */   public DecoderResult decoderResult() {
/* 68 */     return this.decoderResult;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 73 */     return StringUtil.simpleClassName(this) + '[' + 
/* 74 */       "fixedHeader=" + (
/* 75 */       (fixedHeader() != null) ? fixedHeader().toString() : "") + ", variableHeader=" + (
/* 76 */       (variableHeader() != null) ? this.variableHeader.toString() : "") + ", payload=" + (
/* 77 */       (payload() != null) ? this.payload.toString() : "") + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */