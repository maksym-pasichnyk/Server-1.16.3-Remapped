/*     */ package io.netty.handler.codec.mqtt;
/*     */ 
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class MqttCodecUtil
/*     */ {
/*  23 */   private static final char[] TOPIC_WILDCARDS = new char[] { '#', '+' };
/*     */   
/*     */   private static final int MIN_CLIENT_ID_LENGTH = 1;
/*     */   private static final int MAX_CLIENT_ID_LENGTH = 23;
/*     */   
/*     */   static boolean isValidPublishTopicName(String topicName) {
/*  29 */     for (char c : TOPIC_WILDCARDS) {
/*  30 */       if (topicName.indexOf(c) >= 0) {
/*  31 */         return false;
/*     */       }
/*     */     } 
/*  34 */     return true;
/*     */   }
/*     */   
/*     */   static boolean isValidMessageId(int messageId) {
/*  38 */     return (messageId != 0);
/*     */   }
/*     */   
/*     */   static boolean isValidClientId(MqttVersion mqttVersion, String clientId) {
/*  42 */     if (mqttVersion == MqttVersion.MQTT_3_1) {
/*  43 */       return (clientId != null && clientId.length() >= 1 && clientId
/*  44 */         .length() <= 23);
/*     */     }
/*  46 */     if (mqttVersion == MqttVersion.MQTT_3_1_1)
/*     */     {
/*     */       
/*  49 */       return (clientId != null);
/*     */     }
/*  51 */     throw new IllegalArgumentException(mqttVersion + " is unknown mqtt version");
/*     */   }
/*     */   
/*     */   static MqttFixedHeader validateFixedHeader(MqttFixedHeader mqttFixedHeader) {
/*  55 */     switch (mqttFixedHeader.messageType()) {
/*     */       case PUBREL:
/*     */       case SUBSCRIBE:
/*     */       case UNSUBSCRIBE:
/*  59 */         if (mqttFixedHeader.qosLevel() != MqttQoS.AT_LEAST_ONCE)
/*  60 */           throw new DecoderException(mqttFixedHeader.messageType().name() + " message must have QoS 1"); 
/*     */         break;
/*     */     } 
/*  63 */     return mqttFixedHeader;
/*     */   }
/*     */ 
/*     */   
/*     */   static MqttFixedHeader resetUnusedFields(MqttFixedHeader mqttFixedHeader) {
/*  68 */     switch (mqttFixedHeader.messageType()) {
/*     */       case CONNECT:
/*     */       case CONNACK:
/*     */       case PUBACK:
/*     */       case PUBREC:
/*     */       case PUBCOMP:
/*     */       case SUBACK:
/*     */       case UNSUBACK:
/*     */       case PINGREQ:
/*     */       case PINGRESP:
/*     */       case DISCONNECT:
/*  79 */         if (mqttFixedHeader.isDup() || mqttFixedHeader
/*  80 */           .qosLevel() != MqttQoS.AT_MOST_ONCE || mqttFixedHeader
/*  81 */           .isRetain()) {
/*  82 */           return new MqttFixedHeader(mqttFixedHeader
/*  83 */               .messageType(), false, MqttQoS.AT_MOST_ONCE, false, mqttFixedHeader
/*     */ 
/*     */ 
/*     */               
/*  87 */               .remainingLength());
/*     */         }
/*  89 */         return mqttFixedHeader;
/*     */       case PUBREL:
/*     */       case SUBSCRIBE:
/*     */       case UNSUBSCRIBE:
/*  93 */         if (mqttFixedHeader.isRetain()) {
/*  94 */           return new MqttFixedHeader(mqttFixedHeader
/*  95 */               .messageType(), mqttFixedHeader
/*  96 */               .isDup(), mqttFixedHeader
/*  97 */               .qosLevel(), false, mqttFixedHeader
/*     */               
/*  99 */               .remainingLength());
/*     */         }
/* 101 */         return mqttFixedHeader;
/*     */     } 
/* 103 */     return mqttFixedHeader;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttCodecUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */