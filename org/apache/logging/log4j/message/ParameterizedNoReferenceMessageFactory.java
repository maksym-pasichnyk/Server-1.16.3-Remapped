/*     */ package org.apache.logging.log4j.message;
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
/*     */ public final class ParameterizedNoReferenceMessageFactory
/*     */   extends AbstractMessageFactory
/*     */ {
/*     */   private static final long serialVersionUID = 5027639245636870500L;
/*     */   
/*     */   static class StatusMessage
/*     */     implements Message
/*     */   {
/*     */     private final String formattedMessage;
/*     */     private final Throwable throwable;
/*     */     
/*     */     public StatusMessage(String formattedMessage, Throwable throwable) {
/*  53 */       this.formattedMessage = formattedMessage;
/*  54 */       this.throwable = throwable;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getFormattedMessage() {
/*  59 */       return this.formattedMessage;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getFormat() {
/*  64 */       return this.formattedMessage;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] getParameters() {
/*  69 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Throwable getThrowable() {
/*  74 */       return this.throwable;
/*     */     }
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
/*  88 */   public static final ParameterizedNoReferenceMessageFactory INSTANCE = new ParameterizedNoReferenceMessageFactory();
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
/* 101 */     if (params == null) {
/* 102 */       return new SimpleMessage(message);
/*     */     }
/* 104 */     ParameterizedMessage msg = new ParameterizedMessage(message, params);
/* 105 */     return new StatusMessage(msg.getFormattedMessage(), msg.getThrowable());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\message\ParameterizedNoReferenceMessageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */