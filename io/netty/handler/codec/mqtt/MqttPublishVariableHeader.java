/*    */ package io.netty.handler.codec.mqtt;
/*    */ 
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
/*    */ public final class MqttPublishVariableHeader
/*    */ {
/*    */   private final String topicName;
/*    */   private final int packetId;
/*    */   
/*    */   public MqttPublishVariableHeader(String topicName, int packetId) {
/* 30 */     this.topicName = topicName;
/* 31 */     this.packetId = packetId;
/*    */   }
/*    */   
/*    */   public String topicName() {
/* 35 */     return this.topicName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public int messageId() {
/* 43 */     return this.packetId;
/*    */   }
/*    */   
/*    */   public int packetId() {
/* 47 */     return this.packetId;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 52 */     return StringUtil.simpleClassName(this) + '[' + 
/* 53 */       "topicName=" + 
/* 54 */       this.topicName + ", packetId=" + 
/* 55 */       this.packetId + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttPublishVariableHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */