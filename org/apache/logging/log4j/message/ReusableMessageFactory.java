/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
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
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class ReusableMessageFactory
/*     */   implements MessageFactory2, Serializable
/*     */ {
/*  38 */   public static final ReusableMessageFactory INSTANCE = new ReusableMessageFactory();
/*     */   
/*     */   private static final long serialVersionUID = -8970940216592525651L;
/*  41 */   private static ThreadLocal<ReusableParameterizedMessage> threadLocalParameterized = new ThreadLocal<>();
/*  42 */   private static ThreadLocal<ReusableSimpleMessage> threadLocalSimpleMessage = new ThreadLocal<>();
/*  43 */   private static ThreadLocal<ReusableObjectMessage> threadLocalObjectMessage = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ReusableParameterizedMessage getParameterized() {
/*  53 */     ReusableParameterizedMessage result = threadLocalParameterized.get();
/*  54 */     if (result == null) {
/*  55 */       result = new ReusableParameterizedMessage();
/*  56 */       threadLocalParameterized.set(result);
/*     */     } 
/*  58 */     return result.reserved ? (new ReusableParameterizedMessage()).reserve() : result.reserve();
/*     */   }
/*     */   
/*     */   private static ReusableSimpleMessage getSimple() {
/*  62 */     ReusableSimpleMessage result = threadLocalSimpleMessage.get();
/*  63 */     if (result == null) {
/*  64 */       result = new ReusableSimpleMessage();
/*  65 */       threadLocalSimpleMessage.set(result);
/*     */     } 
/*  67 */     return result;
/*     */   }
/*     */   
/*     */   private static ReusableObjectMessage getObject() {
/*  71 */     ReusableObjectMessage result = threadLocalObjectMessage.get();
/*  72 */     if (result == null) {
/*  73 */       result = new ReusableObjectMessage();
/*  74 */       threadLocalObjectMessage.set(result);
/*     */     } 
/*  76 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void release(Message message) {
/*  87 */     if (message instanceof ReusableParameterizedMessage) {
/*  88 */       ((ReusableParameterizedMessage)message).reserved = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Message newMessage(CharSequence charSequence) {
/*  94 */     ReusableSimpleMessage result = getSimple();
/*  95 */     result.set(charSequence);
/*  96 */     return result;
/*     */   }
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
/*     */   public Message newMessage(String message, Object... params) {
/* 110 */     return getParameterized().set(message, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0) {
/* 115 */     return getParameterized().set(message, p0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1) {
/* 120 */     return getParameterized().set(message, p0, p1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2) {
/* 125 */     return getParameterized().set(message, p0, p1, p2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3) {
/* 131 */     return getParameterized().set(message, p0, p1, p2, p3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
/* 137 */     return getParameterized().set(message, p0, p1, p2, p3, p4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
/* 143 */     return getParameterized().set(message, p0, p1, p2, p3, p4, p5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
/* 149 */     return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
/* 155 */     return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6, p7);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
/* 161 */     return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
/* 167 */     return getParameterized().set(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
/*     */   }
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
/*     */   public Message newMessage(String message) {
/* 180 */     ReusableSimpleMessage result = getSimple();
/* 181 */     result.set(message);
/* 182 */     return result;
/*     */   }
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
/*     */   public Message newMessage(Object message) {
/* 196 */     ReusableObjectMessage result = getObject();
/* 197 */     result.set(message);
/* 198 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\message\ReusableMessageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */