/*    */ package io.netty.handler.codec.smtp;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ public final class DefaultSmtpRequest
/*    */   implements SmtpRequest
/*    */ {
/*    */   private final SmtpCommand command;
/*    */   private final List<CharSequence> parameters;
/*    */   
/*    */   public DefaultSmtpRequest(SmtpCommand command) {
/* 37 */     this.command = (SmtpCommand)ObjectUtil.checkNotNull(command, "command");
/* 38 */     this.parameters = Collections.emptyList();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultSmtpRequest(SmtpCommand command, CharSequence... parameters) {
/* 45 */     this.command = (SmtpCommand)ObjectUtil.checkNotNull(command, "command");
/* 46 */     this.parameters = SmtpUtils.toUnmodifiableList(parameters);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultSmtpRequest(CharSequence command, CharSequence... parameters) {
/* 53 */     this(SmtpCommand.valueOf(command), parameters);
/*    */   }
/*    */   
/*    */   DefaultSmtpRequest(SmtpCommand command, List<CharSequence> parameters) {
/* 57 */     this.command = (SmtpCommand)ObjectUtil.checkNotNull(command, "command");
/* 58 */     this
/* 59 */       .parameters = (parameters != null) ? Collections.<CharSequence>unmodifiableList(parameters) : Collections.<CharSequence>emptyList();
/*    */   }
/*    */ 
/*    */   
/*    */   public SmtpCommand command() {
/* 64 */     return this.command;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<CharSequence> parameters() {
/* 69 */     return this.parameters;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 74 */     return this.command.hashCode() * 31 + this.parameters.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 79 */     if (!(o instanceof DefaultSmtpRequest)) {
/* 80 */       return false;
/*    */     }
/*    */     
/* 83 */     if (o == this) {
/* 84 */       return true;
/*    */     }
/*    */     
/* 87 */     DefaultSmtpRequest other = (DefaultSmtpRequest)o;
/*    */     
/* 89 */     return (command().equals(other.command()) && 
/* 90 */       parameters().equals(other.parameters()));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 95 */     return "DefaultSmtpRequest{command=" + this.command + ", parameters=" + this.parameters + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\smtp\DefaultSmtpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */