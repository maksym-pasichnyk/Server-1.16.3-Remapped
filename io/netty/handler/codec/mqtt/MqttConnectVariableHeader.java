/*     */ package io.netty.handler.codec.mqtt;
/*     */ 
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
/*     */ 
/*     */ public final class MqttConnectVariableHeader
/*     */ {
/*     */   private final String name;
/*     */   private final int version;
/*     */   private final boolean hasUserName;
/*     */   private final boolean hasPassword;
/*     */   private final boolean isWillRetain;
/*     */   private final int willQos;
/*     */   private final boolean isWillFlag;
/*     */   private final boolean isCleanSession;
/*     */   private final int keepAliveTimeSeconds;
/*     */   
/*     */   public MqttConnectVariableHeader(String name, int version, boolean hasUserName, boolean hasPassword, boolean isWillRetain, int willQos, boolean isWillFlag, boolean isCleanSession, int keepAliveTimeSeconds) {
/*  46 */     this.name = name;
/*  47 */     this.version = version;
/*  48 */     this.hasUserName = hasUserName;
/*  49 */     this.hasPassword = hasPassword;
/*  50 */     this.isWillRetain = isWillRetain;
/*  51 */     this.willQos = willQos;
/*  52 */     this.isWillFlag = isWillFlag;
/*  53 */     this.isCleanSession = isCleanSession;
/*  54 */     this.keepAliveTimeSeconds = keepAliveTimeSeconds;
/*     */   }
/*     */   
/*     */   public String name() {
/*  58 */     return this.name;
/*     */   }
/*     */   
/*     */   public int version() {
/*  62 */     return this.version;
/*     */   }
/*     */   
/*     */   public boolean hasUserName() {
/*  66 */     return this.hasUserName;
/*     */   }
/*     */   
/*     */   public boolean hasPassword() {
/*  70 */     return this.hasPassword;
/*     */   }
/*     */   
/*     */   public boolean isWillRetain() {
/*  74 */     return this.isWillRetain;
/*     */   }
/*     */   
/*     */   public int willQos() {
/*  78 */     return this.willQos;
/*     */   }
/*     */   
/*     */   public boolean isWillFlag() {
/*  82 */     return this.isWillFlag;
/*     */   }
/*     */   
/*     */   public boolean isCleanSession() {
/*  86 */     return this.isCleanSession;
/*     */   }
/*     */   
/*     */   public int keepAliveTimeSeconds() {
/*  90 */     return this.keepAliveTimeSeconds;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  95 */     return StringUtil.simpleClassName(this) + '[' + 
/*  96 */       "name=" + 
/*  97 */       this.name + ", version=" + 
/*  98 */       this.version + ", hasUserName=" + 
/*  99 */       this.hasUserName + ", hasPassword=" + 
/* 100 */       this.hasPassword + ", isWillRetain=" + 
/* 101 */       this.isWillRetain + ", isWillFlag=" + 
/* 102 */       this.isWillFlag + ", isCleanSession=" + 
/* 103 */       this.isCleanSession + ", keepAliveTimeSeconds=" + 
/* 104 */       this.keepAliveTimeSeconds + ']';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttConnectVariableHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */