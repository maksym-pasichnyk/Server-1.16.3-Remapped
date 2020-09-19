/*    */ package io.netty.handler.codec.mqtt;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MqttFixedHeader
/*    */ {
/*    */   private final MqttMessageType messageType;
/*    */   private final boolean isDup;
/*    */   private final MqttQoS qosLevel;
/*    */   private final boolean isRetain;
/*    */   private final int remainingLength;
/*    */   
/*    */   public MqttFixedHeader(MqttMessageType messageType, boolean isDup, MqttQoS qosLevel, boolean isRetain, int remainingLength) {
/* 40 */     this.messageType = (MqttMessageType)ObjectUtil.checkNotNull(messageType, "messageType");
/* 41 */     this.isDup = isDup;
/* 42 */     this.qosLevel = (MqttQoS)ObjectUtil.checkNotNull(qosLevel, "qosLevel");
/* 43 */     this.isRetain = isRetain;
/* 44 */     this.remainingLength = remainingLength;
/*    */   }
/*    */   
/*    */   public MqttMessageType messageType() {
/* 48 */     return this.messageType;
/*    */   }
/*    */   
/*    */   public boolean isDup() {
/* 52 */     return this.isDup;
/*    */   }
/*    */   
/*    */   public MqttQoS qosLevel() {
/* 56 */     return this.qosLevel;
/*    */   }
/*    */   
/*    */   public boolean isRetain() {
/* 60 */     return this.isRetain;
/*    */   }
/*    */   
/*    */   public int remainingLength() {
/* 64 */     return this.remainingLength;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 69 */     return StringUtil.simpleClassName(this) + '[' + 
/* 70 */       "messageType=" + 
/* 71 */       this.messageType + ", isDup=" + 
/* 72 */       this.isDup + ", qosLevel=" + 
/* 73 */       this.qosLevel + ", isRetain=" + 
/* 74 */       this.isRetain + ", remainingLength=" + 
/* 75 */       this.remainingLength + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttFixedHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */