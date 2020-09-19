/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.util.ArrayUtils;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.message.MultiformatMessage;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
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
/*     */ @Plugin(name = "MessagePatternConverter", category = "Converter")
/*     */ @ConverterKeys({"m", "msg", "message"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class MessagePatternConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*     */   private static final String NOLOOKUPS = "nolookups";
/*     */   private final String[] formats;
/*     */   private final Configuration config;
/*     */   private final TextRenderer textRenderer;
/*     */   private final boolean noLookups;
/*     */   
/*     */   private MessagePatternConverter(Configuration config, String[] options) {
/*  54 */     super("Message", "message");
/*  55 */     this.formats = options;
/*  56 */     this.config = config;
/*  57 */     int noLookupsIdx = loadNoLookups(options);
/*  58 */     this.noLookups = (noLookupsIdx >= 0);
/*  59 */     this.textRenderer = loadMessageRenderer((noLookupsIdx >= 0) ? (String[])ArrayUtils.remove((Object[])options, noLookupsIdx) : options);
/*     */   }
/*     */   
/*     */   private int loadNoLookups(String[] options) {
/*  63 */     if (options != null) {
/*  64 */       for (int i = 0; i < options.length; i++) {
/*  65 */         String option = options[i];
/*  66 */         if ("nolookups".equalsIgnoreCase(option)) {
/*  67 */           return i;
/*     */         }
/*     */       } 
/*     */     }
/*  71 */     return -1;
/*     */   }
/*     */   
/*     */   private TextRenderer loadMessageRenderer(String[] options) {
/*  75 */     if (options != null) {
/*  76 */       for (String option : options) {
/*  77 */         switch (option.toUpperCase(Locale.ROOT)) {
/*     */           case "ANSI":
/*  79 */             if (Loader.isJansiAvailable()) {
/*  80 */               return new JAnsiTextRenderer(options, JAnsiTextRenderer.DefaultMessageStyleMap);
/*     */             }
/*  82 */             StatusLogger.getLogger().warn("You requested ANSI message rendering but JANSI is not on the classpath.");
/*     */             
/*  84 */             return null;
/*     */           case "HTML":
/*  86 */             return new HtmlTextRenderer(options);
/*     */         } 
/*     */       } 
/*     */     }
/*  90 */     return null;
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
/*     */   public static MessagePatternConverter newInstance(Configuration config, String[] options) {
/* 103 */     return new MessagePatternConverter(config, options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 111 */     Message msg = event.getMessage();
/* 112 */     if (msg instanceof StringBuilderFormattable) {
/*     */       
/* 114 */       boolean doRender = (this.textRenderer != null);
/* 115 */       StringBuilder workingBuilder = doRender ? new StringBuilder(80) : toAppendTo;
/*     */       
/* 117 */       StringBuilderFormattable stringBuilderFormattable = (StringBuilderFormattable)msg;
/* 118 */       int offset = workingBuilder.length();
/* 119 */       stringBuilderFormattable.formatTo(workingBuilder);
/*     */ 
/*     */       
/* 122 */       if (this.config != null && !this.noLookups) {
/* 123 */         for (int i = offset; i < workingBuilder.length() - 1; i++) {
/* 124 */           if (workingBuilder.charAt(i) == '$' && workingBuilder.charAt(i + 1) == '{') {
/* 125 */             String value = workingBuilder.substring(offset, workingBuilder.length());
/* 126 */             workingBuilder.setLength(offset);
/* 127 */             workingBuilder.append(this.config.getStrSubstitutor().replace(event, value));
/*     */           } 
/*     */         } 
/*     */       }
/* 131 */       if (doRender) {
/* 132 */         this.textRenderer.render(workingBuilder, toAppendTo);
/*     */       }
/*     */       return;
/*     */     } 
/* 136 */     if (msg != null) {
/*     */       String result;
/* 138 */       if (msg instanceof MultiformatMessage) {
/* 139 */         result = ((MultiformatMessage)msg).getFormattedMessage(this.formats);
/*     */       } else {
/* 141 */         result = msg.getFormattedMessage();
/*     */       } 
/* 143 */       if (result != null) {
/* 144 */         toAppendTo.append((this.config != null && result.contains("${")) ? this.config.getStrSubstitutor().replace(event, result) : result);
/*     */       } else {
/*     */         
/* 147 */         toAppendTo.append("null");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\MessagePatternConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */