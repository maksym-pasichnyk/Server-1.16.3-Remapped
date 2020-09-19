/*     */ package org.apache.logging.log4j.message;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class DefaultFlowMessageFactory
/*     */   implements FlowMessageFactory, Serializable
/*     */ {
/*     */   private static final String EXIT_DEFAULT_PREFIX = "Exit";
/*     */   private static final String ENTRY_DEFAULT_PREFIX = "Enter";
/*     */   private static final long serialVersionUID = 8578655591131397576L;
/*     */   private final String entryText;
/*     */   private final String exitText;
/*     */   
/*     */   public DefaultFlowMessageFactory() {
/*  39 */     this("Enter", "Exit");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultFlowMessageFactory(String entryText, String exitText) {
/*  49 */     this.entryText = entryText;
/*  50 */     this.exitText = exitText;
/*     */   }
/*     */   
/*     */   private static class AbstractFlowMessage
/*     */     implements FlowMessage {
/*     */     private static final long serialVersionUID = 1L;
/*     */     private final Message message;
/*     */     private final String text;
/*     */     
/*     */     AbstractFlowMessage(String text, Message message) {
/*  60 */       this.message = message;
/*  61 */       this.text = text;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getFormattedMessage() {
/*  66 */       if (this.message != null) {
/*  67 */         return this.text + " " + this.message.getFormattedMessage();
/*     */       }
/*  69 */       return this.text;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getFormat() {
/*  74 */       if (this.message != null) {
/*  75 */         return this.text + ": " + this.message.getFormat();
/*     */       }
/*  77 */       return this.text;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] getParameters() {
/*  82 */       if (this.message != null) {
/*  83 */         return this.message.getParameters();
/*     */       }
/*  85 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Throwable getThrowable() {
/*  90 */       if (this.message != null) {
/*  91 */         return this.message.getThrowable();
/*     */       }
/*  93 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Message getMessage() {
/*  98 */       return this.message;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getText() {
/* 103 */       return this.text;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SimpleEntryMessage
/*     */     extends AbstractFlowMessage implements EntryMessage {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     SimpleEntryMessage(String entryText, Message message) {
/* 112 */       super(entryText, message);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SimpleExitMessage
/*     */     extends AbstractFlowMessage
/*     */     implements ExitMessage
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     private final Object result;
/*     */     private final boolean isVoid;
/*     */     
/*     */     SimpleExitMessage(String exitText, EntryMessage message) {
/* 125 */       super(exitText, message.getMessage());
/* 126 */       this.result = null;
/* 127 */       this.isVoid = true;
/*     */     }
/*     */     
/*     */     SimpleExitMessage(String exitText, Object result, EntryMessage message) {
/* 131 */       super(exitText, message.getMessage());
/* 132 */       this.result = result;
/* 133 */       this.isVoid = false;
/*     */     }
/*     */     
/*     */     SimpleExitMessage(String exitText, Object result, Message message) {
/* 137 */       super(exitText, message);
/* 138 */       this.result = result;
/* 139 */       this.isVoid = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getFormattedMessage() {
/* 144 */       String formattedMessage = super.getFormattedMessage();
/* 145 */       if (this.isVoid) {
/* 146 */         return formattedMessage;
/*     */       }
/* 148 */       return formattedMessage + ": " + this.result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEntryText() {
/* 157 */     return this.entryText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExitText() {
/* 165 */     return this.exitText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntryMessage newEntryMessage(Message message) {
/* 175 */     return new SimpleEntryMessage(this.entryText, makeImmutable(message));
/*     */   }
/*     */   
/*     */   private Message makeImmutable(Message message) {
/* 179 */     if (!(message instanceof ReusableMessage)) {
/* 180 */       return message;
/*     */     }
/* 182 */     return new SimpleMessage(message.getFormattedMessage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExitMessage newExitMessage(EntryMessage message) {
/* 192 */     return new SimpleExitMessage(this.exitText, message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExitMessage newExitMessage(Object result, EntryMessage message) {
/* 202 */     return new SimpleExitMessage(this.exitText, result, message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExitMessage newExitMessage(Object result, Message message) {
/* 212 */     return new SimpleExitMessage(this.exitText, result, message);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\message\DefaultFlowMessageFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */