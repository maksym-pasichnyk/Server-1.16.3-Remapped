/*     */ package org.apache.logging.log4j.core.pattern;
/*     */ 
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.util.PerformanceSensitive;
/*     */ import org.apache.logging.log4j.util.ReadOnlyStringMap;
/*     */ import org.apache.logging.log4j.util.StringBuilders;
/*     */ import org.apache.logging.log4j.util.TriConsumer;
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
/*     */ @Plugin(name = "MdcPatternConverter", category = "Converter")
/*     */ @ConverterKeys({"X", "mdc", "MDC"})
/*     */ @PerformanceSensitive({"allocation"})
/*     */ public final class MdcPatternConverter
/*     */   extends LogEventPatternConverter
/*     */ {
/*  39 */   private static final ThreadLocal<StringBuilder> threadLocal = new ThreadLocal<>();
/*     */   private static final int DEFAULT_STRING_BUILDER_SIZE = 64;
/*  41 */   private static final int MAX_STRING_BUILDER_SIZE = Constants.MAX_REUSABLE_MESSAGE_SIZE;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String key;
/*     */ 
/*     */   
/*     */   private final String[] keys;
/*     */ 
/*     */   
/*     */   private final boolean full;
/*     */ 
/*     */ 
/*     */   
/*     */   private MdcPatternConverter(String[] options) {
/*  56 */     super((options != null && options.length > 0) ? ("MDC{" + options[0] + '}') : "MDC", "mdc");
/*  57 */     if (options != null && options.length > 0) {
/*  58 */       this.full = false;
/*  59 */       if (options[0].indexOf(',') > 0) {
/*  60 */         this.keys = options[0].split(",");
/*  61 */         for (int i = 0; i < this.keys.length; i++) {
/*  62 */           this.keys[i] = this.keys[i].trim();
/*     */         }
/*  64 */         this.key = null;
/*     */       } else {
/*  66 */         this.keys = null;
/*  67 */         this.key = options[0];
/*     */       } 
/*     */     } else {
/*  70 */       this.full = true;
/*  71 */       this.key = null;
/*  72 */       this.keys = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MdcPatternConverter newInstance(String[] options) {
/*  83 */     return new MdcPatternConverter(options);
/*     */   }
/*     */   
/*  86 */   private static final TriConsumer<String, Object, StringBuilder> WRITE_KEY_VALUES_INTO = new TriConsumer<String, Object, StringBuilder>()
/*     */     {
/*     */       public void accept(String key, Object value, StringBuilder sb) {
/*  89 */         if (sb.length() > 1) {
/*  90 */           sb.append(", ");
/*     */         }
/*  92 */         sb.append(key).append('=');
/*  93 */         StringBuilders.appendValue(sb, value);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void format(LogEvent event, StringBuilder toAppendTo) {
/* 102 */     ReadOnlyStringMap contextData = event.getContextData();
/*     */ 
/*     */     
/* 105 */     if (this.full) {
/* 106 */       if (contextData == null || contextData.size() == 0) {
/* 107 */         toAppendTo.append("{}");
/*     */         return;
/*     */       } 
/* 110 */       appendFully(contextData, toAppendTo);
/*     */     }
/* 112 */     else if (this.keys != null) {
/* 113 */       if (contextData == null || contextData.size() == 0) {
/* 114 */         toAppendTo.append("{}");
/*     */         return;
/*     */       } 
/* 117 */       appendSelectedKeys(this.keys, contextData, toAppendTo);
/* 118 */     } else if (contextData != null) {
/*     */       
/* 120 */       Object value = contextData.getValue(this.key);
/* 121 */       if (value != null) {
/* 122 */         StringBuilders.appendValue(toAppendTo, value);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void appendFully(ReadOnlyStringMap contextData, StringBuilder toAppendTo) {
/* 129 */     StringBuilder sb = getStringBuilder();
/* 130 */     sb.append("{");
/* 131 */     contextData.forEach(WRITE_KEY_VALUES_INTO, sb);
/* 132 */     sb.append('}');
/* 133 */     toAppendTo.append(sb);
/* 134 */     trimToMaxSize(sb);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void appendSelectedKeys(String[] keys, ReadOnlyStringMap contextData, StringBuilder toAppendTo) {
/* 139 */     StringBuilder sb = getStringBuilder();
/* 140 */     sb.append("{");
/* 141 */     for (int i = 0; i < keys.length; i++) {
/* 142 */       String theKey = keys[i];
/* 143 */       Object value = contextData.getValue(theKey);
/* 144 */       if (value != null) {
/* 145 */         if (sb.length() > 1) {
/* 146 */           sb.append(", ");
/*     */         }
/* 148 */         sb.append(theKey).append('=');
/* 149 */         StringBuilders.appendValue(sb, value);
/*     */       } 
/*     */     } 
/* 152 */     sb.append('}');
/* 153 */     toAppendTo.append(sb);
/* 154 */     trimToMaxSize(sb);
/*     */   }
/*     */   
/*     */   private static StringBuilder getStringBuilder() {
/* 158 */     StringBuilder result = threadLocal.get();
/* 159 */     if (result == null) {
/* 160 */       result = new StringBuilder(64);
/* 161 */       threadLocal.set(result);
/*     */     } 
/* 163 */     result.setLength(0);
/* 164 */     return result;
/*     */   }
/*     */   
/*     */   private static void trimToMaxSize(StringBuilder stringBuilder) {
/* 168 */     if (stringBuilder.length() > MAX_STRING_BUILDER_SIZE) {
/* 169 */       stringBuilder.setLength(MAX_STRING_BUILDER_SIZE);
/* 170 */       stringBuilder.trimToSize();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\pattern\MdcPatternConverter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */