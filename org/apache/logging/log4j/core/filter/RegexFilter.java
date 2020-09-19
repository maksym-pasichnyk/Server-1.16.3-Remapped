/*     */ package org.apache.logging.log4j.core.filter;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.message.Message;
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
/*     */ @Plugin(name = "RegexFilter", category = "Core", elementType = "filter", printObject = true)
/*     */ public final class RegexFilter
/*     */   extends AbstractFilter
/*     */ {
/*     */   private static final int DEFAULT_PATTERN_FLAGS = 0;
/*     */   private final Pattern pattern;
/*     */   private final boolean useRawMessage;
/*     */   
/*     */   private RegexFilter(boolean raw, Pattern pattern, Filter.Result onMatch, Filter.Result onMismatch) {
/*  52 */     super(onMatch, onMismatch);
/*  53 */     this.pattern = pattern;
/*  54 */     this.useRawMessage = raw;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
/*  60 */     return filter(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
/*  66 */     if (msg == null) {
/*  67 */       return this.onMismatch;
/*     */     }
/*  69 */     return filter(msg.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Filter.Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
/*  75 */     if (msg == null) {
/*  76 */       return this.onMismatch;
/*     */     }
/*  78 */     String text = this.useRawMessage ? msg.getFormat() : msg.getFormattedMessage();
/*  79 */     return filter(text);
/*     */   }
/*     */ 
/*     */   
/*     */   public Filter.Result filter(LogEvent event) {
/*  84 */     String text = this.useRawMessage ? event.getMessage().getFormat() : event.getMessage().getFormattedMessage();
/*  85 */     return filter(text);
/*     */   }
/*     */   
/*     */   private Filter.Result filter(String msg) {
/*  89 */     if (msg == null) {
/*  90 */       return this.onMismatch;
/*     */     }
/*  92 */     Matcher m = this.pattern.matcher(msg);
/*  93 */     return m.matches() ? this.onMatch : this.onMismatch;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  98 */     StringBuilder sb = new StringBuilder();
/*  99 */     sb.append("useRaw=").append(this.useRawMessage);
/* 100 */     sb.append(", pattern=").append(this.pattern.toString());
/* 101 */     return sb.toString();
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
/*     */   @PluginFactory
/*     */   public static RegexFilter createFilter(@PluginAttribute("regex") String regex, @PluginElement("PatternFlags") String[] patternFlags, @PluginAttribute("useRawMsg") Boolean useRawMsg, @PluginAttribute("onMatch") Filter.Result match, @PluginAttribute("onMismatch") Filter.Result mismatch) throws IllegalArgumentException, IllegalAccessException {
/* 131 */     if (regex == null) {
/* 132 */       LOGGER.error("A regular expression must be provided for RegexFilter");
/* 133 */       return null;
/*     */     } 
/* 135 */     return new RegexFilter(useRawMsg.booleanValue(), Pattern.compile(regex, toPatternFlags(patternFlags)), match, mismatch);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int toPatternFlags(String[] patternFlags) throws IllegalArgumentException, IllegalAccessException {
/* 140 */     if (patternFlags == null || patternFlags.length == 0) {
/* 141 */       return 0;
/*     */     }
/* 143 */     Field[] fields = Pattern.class.getDeclaredFields();
/* 144 */     Comparator<Field> comparator = new Comparator<Field>()
/*     */       {
/*     */         public int compare(Field f1, Field f2)
/*     */         {
/* 148 */           return f1.getName().compareTo(f2.getName());
/*     */         }
/*     */       };
/* 151 */     Arrays.sort(fields, comparator);
/* 152 */     String[] fieldNames = new String[fields.length];
/* 153 */     for (int i = 0; i < fields.length; i++) {
/* 154 */       fieldNames[i] = fields[i].getName();
/*     */     }
/* 156 */     int flags = 0;
/* 157 */     for (String test : patternFlags) {
/* 158 */       int index = Arrays.binarySearch((Object[])fieldNames, test);
/* 159 */       if (index >= 0) {
/* 160 */         Field field = fields[index];
/* 161 */         flags |= field.getInt(Pattern.class);
/*     */       } 
/*     */     } 
/* 164 */     return flags;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\filter\RegexFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */