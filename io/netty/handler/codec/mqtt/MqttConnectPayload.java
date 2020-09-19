/*     */ package io.netty.handler.codec.mqtt;
/*     */ 
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public final class MqttConnectPayload
/*     */ {
/*     */   private final String clientIdentifier;
/*     */   private final String willTopic;
/*     */   private final byte[] willMessage;
/*     */   private final String userName;
/*     */   private final byte[] password;
/*     */   
/*     */   @Deprecated
/*     */   public MqttConnectPayload(String clientIdentifier, String willTopic, String willMessage, String userName, String password) {
/*  43 */     this(clientIdentifier, willTopic, willMessage
/*     */ 
/*     */         
/*  46 */         .getBytes(CharsetUtil.UTF_8), userName, password
/*     */         
/*  48 */         .getBytes(CharsetUtil.UTF_8));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MqttConnectPayload(String clientIdentifier, String willTopic, byte[] willMessage, String userName, byte[] password) {
/*  57 */     this.clientIdentifier = clientIdentifier;
/*  58 */     this.willTopic = willTopic;
/*  59 */     this.willMessage = willMessage;
/*  60 */     this.userName = userName;
/*  61 */     this.password = password;
/*     */   }
/*     */   
/*     */   public String clientIdentifier() {
/*  65 */     return this.clientIdentifier;
/*     */   }
/*     */   
/*     */   public String willTopic() {
/*  69 */     return this.willTopic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String willMessage() {
/*  77 */     return (this.willMessage == null) ? null : new String(this.willMessage, CharsetUtil.UTF_8);
/*     */   }
/*     */   
/*     */   public byte[] willMessageInBytes() {
/*  81 */     return this.willMessage;
/*     */   }
/*     */   
/*     */   public String userName() {
/*  85 */     return this.userName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String password() {
/*  93 */     return (this.password == null) ? null : new String(this.password, CharsetUtil.UTF_8);
/*     */   }
/*     */   
/*     */   public byte[] passwordInBytes() {
/*  97 */     return this.password;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 102 */     return StringUtil.simpleClassName(this) + '[' + 
/* 103 */       "clientIdentifier=" + 
/* 104 */       this.clientIdentifier + ", willTopic=" + 
/* 105 */       this.willTopic + ", willMessage=" + 
/* 106 */       this.willMessage + ", userName=" + 
/* 107 */       this.userName + ", password=" + 
/* 108 */       this.password + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttConnectPayload.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */