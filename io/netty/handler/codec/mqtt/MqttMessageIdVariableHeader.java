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
/*    */ 
/*    */ public final class MqttMessageIdVariableHeader
/*    */ {
/*    */   private final int messageId;
/*    */   
/*    */   public static MqttMessageIdVariableHeader from(int messageId) {
/* 30 */     if (messageId < 1 || messageId > 65535) {
/* 31 */       throw new IllegalArgumentException("messageId: " + messageId + " (expected: 1 ~ 65535)");
/*    */     }
/* 33 */     return new MqttMessageIdVariableHeader(messageId);
/*    */   }
/*    */   
/*    */   private MqttMessageIdVariableHeader(int messageId) {
/* 37 */     this.messageId = messageId;
/*    */   }
/*    */   
/*    */   public int messageId() {
/* 41 */     return this.messageId;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 46 */     return StringUtil.simpleClassName(this) + '[' + 
/* 47 */       "messageId=" + 
/* 48 */       this.messageId + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttMessageIdVariableHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */