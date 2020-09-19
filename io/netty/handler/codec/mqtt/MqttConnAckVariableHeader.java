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
/*    */ public final class MqttConnAckVariableHeader
/*    */ {
/*    */   private final MqttConnectReturnCode connectReturnCode;
/*    */   private final boolean sessionPresent;
/*    */   
/*    */   public MqttConnAckVariableHeader(MqttConnectReturnCode connectReturnCode, boolean sessionPresent) {
/* 31 */     this.connectReturnCode = connectReturnCode;
/* 32 */     this.sessionPresent = sessionPresent;
/*    */   }
/*    */   
/*    */   public MqttConnectReturnCode connectReturnCode() {
/* 36 */     return this.connectReturnCode;
/*    */   }
/*    */   
/*    */   public boolean isSessionPresent() {
/* 40 */     return this.sessionPresent;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 45 */     return StringUtil.simpleClassName(this) + '[' + 
/* 46 */       "connectReturnCode=" + 
/* 47 */       this.connectReturnCode + ", sessionPresent=" + 
/* 48 */       this.sessionPresent + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttConnAckVariableHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */