/*     */ package io.netty.handler.codec.smtp;
/*     */ 
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public final class SmtpRequests
/*     */ {
/*  31 */   private static final SmtpRequest DATA = new DefaultSmtpRequest(SmtpCommand.DATA);
/*  32 */   private static final SmtpRequest NOOP = new DefaultSmtpRequest(SmtpCommand.NOOP);
/*  33 */   private static final SmtpRequest RSET = new DefaultSmtpRequest(SmtpCommand.RSET);
/*  34 */   private static final SmtpRequest HELP_NO_ARG = new DefaultSmtpRequest(SmtpCommand.HELP);
/*  35 */   private static final SmtpRequest QUIT = new DefaultSmtpRequest(SmtpCommand.QUIT);
/*  36 */   private static final AsciiString FROM_NULL_SENDER = AsciiString.cached("FROM:<>");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SmtpRequest helo(CharSequence hostname) {
/*  42 */     return new DefaultSmtpRequest(SmtpCommand.HELO, new CharSequence[] { hostname });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SmtpRequest ehlo(CharSequence hostname) {
/*  49 */     return new DefaultSmtpRequest(SmtpCommand.EHLO, new CharSequence[] { hostname });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SmtpRequest noop() {
/*  56 */     return NOOP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SmtpRequest data() {
/*  63 */     return DATA;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SmtpRequest rset() {
/*  70 */     return RSET;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SmtpRequest help(String cmd) {
/*  77 */     return (cmd == null) ? HELP_NO_ARG : new DefaultSmtpRequest(SmtpCommand.HELP, new CharSequence[] { cmd });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SmtpRequest quit() {
/*  84 */     return QUIT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SmtpRequest mail(CharSequence sender, CharSequence... mailParameters) {
/*  91 */     if (mailParameters == null || mailParameters.length == 0) {
/*  92 */       return new DefaultSmtpRequest(SmtpCommand.MAIL, new CharSequence[] { (sender != null) ? ("FROM:<" + sender + '>') : (CharSequence)FROM_NULL_SENDER });
/*     */     }
/*     */     
/*  95 */     List<CharSequence> params = new ArrayList<CharSequence>(mailParameters.length + 1);
/*  96 */     params.add((sender != null) ? ("FROM:<" + sender + '>') : (CharSequence)FROM_NULL_SENDER);
/*  97 */     for (CharSequence param : mailParameters) {
/*  98 */       params.add(param);
/*     */     }
/* 100 */     return new DefaultSmtpRequest(SmtpCommand.MAIL, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SmtpRequest rcpt(CharSequence recipient, CharSequence... rcptParameters) {
/* 108 */     ObjectUtil.checkNotNull(recipient, "recipient");
/* 109 */     if (rcptParameters == null || rcptParameters.length == 0) {
/* 110 */       return new DefaultSmtpRequest(SmtpCommand.RCPT, new CharSequence[] { "TO:<" + recipient + '>' });
/*     */     }
/* 112 */     List<CharSequence> params = new ArrayList<CharSequence>(rcptParameters.length + 1);
/* 113 */     params.add("TO:<" + recipient + '>');
/* 114 */     for (CharSequence param : rcptParameters) {
/* 115 */       params.add(param);
/*     */     }
/* 117 */     return new DefaultSmtpRequest(SmtpCommand.RCPT, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SmtpRequest expn(CharSequence mailingList) {
/* 125 */     return new DefaultSmtpRequest(SmtpCommand.EXPN, new CharSequence[] { (CharSequence)ObjectUtil.checkNotNull(mailingList, "mailingList") });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SmtpRequest vrfy(CharSequence user) {
/* 132 */     return new DefaultSmtpRequest(SmtpCommand.VRFY, new CharSequence[] { (CharSequence)ObjectUtil.checkNotNull(user, "user") });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\smtp\SmtpRequests.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */