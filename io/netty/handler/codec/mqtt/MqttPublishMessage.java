/*     */ package io.netty.handler.codec.mqtt;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
/*     */ import io.netty.util.IllegalReferenceCountException;
/*     */ import io.netty.util.ReferenceCounted;
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
/*     */ public class MqttPublishMessage
/*     */   extends MqttMessage
/*     */   implements ByteBufHolder
/*     */ {
/*     */   public MqttPublishMessage(MqttFixedHeader mqttFixedHeader, MqttPublishVariableHeader variableHeader, ByteBuf payload) {
/*  32 */     super(mqttFixedHeader, variableHeader, payload);
/*     */   }
/*     */ 
/*     */   
/*     */   public MqttPublishVariableHeader variableHeader() {
/*  37 */     return (MqttPublishVariableHeader)super.variableHeader();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf payload() {
/*  42 */     return content();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuf content() {
/*  47 */     ByteBuf data = (ByteBuf)super.payload();
/*  48 */     if (data.refCnt() <= 0) {
/*  49 */       throw new IllegalReferenceCountException(data.refCnt());
/*     */     }
/*  51 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   public MqttPublishMessage copy() {
/*  56 */     return replace(content().copy());
/*     */   }
/*     */ 
/*     */   
/*     */   public MqttPublishMessage duplicate() {
/*  61 */     return replace(content().duplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public MqttPublishMessage retainedDuplicate() {
/*  66 */     return replace(content().retainedDuplicate());
/*     */   }
/*     */ 
/*     */   
/*     */   public MqttPublishMessage replace(ByteBuf content) {
/*  71 */     return new MqttPublishMessage(fixedHeader(), variableHeader(), content);
/*     */   }
/*     */ 
/*     */   
/*     */   public int refCnt() {
/*  76 */     return content().refCnt();
/*     */   }
/*     */ 
/*     */   
/*     */   public MqttPublishMessage retain() {
/*  81 */     content().retain();
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public MqttPublishMessage retain(int increment) {
/*  87 */     content().retain(increment);
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public MqttPublishMessage touch() {
/*  93 */     content().touch();
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public MqttPublishMessage touch(Object hint) {
/*  99 */     content().touch(hint);
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release() {
/* 105 */     return content().release();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release(int decrement) {
/* 110 */     return content().release(decrement);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\mqtt\MqttPublishMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */