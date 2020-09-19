/*     */ package io.netty.handler.codec.smtp;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class SmtpCommand
/*     */ {
/*  32 */   public static final SmtpCommand EHLO = new SmtpCommand(AsciiString.cached("EHLO"));
/*  33 */   public static final SmtpCommand HELO = new SmtpCommand(AsciiString.cached("HELO"));
/*  34 */   public static final SmtpCommand MAIL = new SmtpCommand(AsciiString.cached("MAIL"));
/*  35 */   public static final SmtpCommand RCPT = new SmtpCommand(AsciiString.cached("RCPT"));
/*  36 */   public static final SmtpCommand DATA = new SmtpCommand(AsciiString.cached("DATA"));
/*  37 */   public static final SmtpCommand NOOP = new SmtpCommand(AsciiString.cached("NOOP"));
/*  38 */   public static final SmtpCommand RSET = new SmtpCommand(AsciiString.cached("RSET"));
/*  39 */   public static final SmtpCommand EXPN = new SmtpCommand(AsciiString.cached("EXPN"));
/*  40 */   public static final SmtpCommand VRFY = new SmtpCommand(AsciiString.cached("VRFY"));
/*  41 */   public static final SmtpCommand HELP = new SmtpCommand(AsciiString.cached("HELP"));
/*  42 */   public static final SmtpCommand QUIT = new SmtpCommand(AsciiString.cached("QUIT"));
/*     */   
/*  44 */   private static final Map<String, SmtpCommand> COMMANDS = new HashMap<String, SmtpCommand>();
/*     */   static {
/*  46 */     COMMANDS.put(EHLO.name().toString(), EHLO);
/*  47 */     COMMANDS.put(HELO.name().toString(), HELO);
/*  48 */     COMMANDS.put(MAIL.name().toString(), MAIL);
/*  49 */     COMMANDS.put(RCPT.name().toString(), RCPT);
/*  50 */     COMMANDS.put(DATA.name().toString(), DATA);
/*  51 */     COMMANDS.put(NOOP.name().toString(), NOOP);
/*  52 */     COMMANDS.put(RSET.name().toString(), RSET);
/*  53 */     COMMANDS.put(EXPN.name().toString(), EXPN);
/*  54 */     COMMANDS.put(VRFY.name().toString(), VRFY);
/*  55 */     COMMANDS.put(HELP.name().toString(), HELP);
/*  56 */     COMMANDS.put(QUIT.name().toString(), QUIT);
/*     */   }
/*     */ 
/*     */   
/*     */   private final AsciiString name;
/*     */   
/*     */   public static SmtpCommand valueOf(CharSequence commandName) {
/*  63 */     ObjectUtil.checkNotNull(commandName, "commandName");
/*  64 */     SmtpCommand command = COMMANDS.get(commandName.toString());
/*  65 */     return (command != null) ? command : new SmtpCommand(AsciiString.of(commandName));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private SmtpCommand(AsciiString name) {
/*  71 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsciiString name() {
/*  78 */     return this.name;
/*     */   }
/*     */   
/*     */   void encode(ByteBuf buffer) {
/*  82 */     ByteBufUtil.writeAscii(buffer, (CharSequence)this.name);
/*     */   }
/*     */   
/*     */   boolean isContentExpected() {
/*  86 */     return equals(DATA);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  91 */     return this.name.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  96 */     if (obj == this) {
/*  97 */       return true;
/*     */     }
/*  99 */     if (!(obj instanceof SmtpCommand)) {
/* 100 */       return false;
/*     */     }
/* 102 */     return this.name.contentEqualsIgnoreCase((CharSequence)((SmtpCommand)obj).name());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 107 */     return "SmtpCommand{name=" + this.name + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\smtp\SmtpCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */