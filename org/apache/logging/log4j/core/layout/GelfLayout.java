/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.StringWriter;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.zip.DeflaterOutputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.lookup.StrSubstitutor;
/*     */ import org.apache.logging.log4j.core.net.Severity;
/*     */ import org.apache.logging.log4j.core.util.JsonUtils;
/*     */ import org.apache.logging.log4j.core.util.KeyValuePair;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.message.Message;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.StringBuilderFormattable;
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
/*     */ @Plugin(name = "GelfLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class GelfLayout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   private static final char C = ',';
/*     */   private static final int COMPRESSION_THRESHOLD = 1024;
/*     */   private static final char Q = '"';
/*     */   private static final String QC = "\",";
/*     */   private static final String QU = "\"_";
/*     */   private final KeyValuePair[] additionalFields;
/*     */   private final int compressionThreshold;
/*     */   private final CompressionType compressionType;
/*     */   private final String host;
/*     */   private final boolean includeStacktrace;
/*     */   private final boolean includeThreadContext;
/*     */   
/*     */   public enum CompressionType
/*     */   {
/*  82 */     GZIP
/*     */     {
/*     */       public DeflaterOutputStream createDeflaterOutputStream(OutputStream os) throws IOException {
/*  85 */         return new GZIPOutputStream(os);
/*     */       }
/*     */     },
/*  88 */     ZLIB
/*     */     {
/*     */       public DeflaterOutputStream createDeflaterOutputStream(OutputStream os) throws IOException {
/*  91 */         return new DeflaterOutputStream(os);
/*     */       }
/*     */     },
/*  94 */     OFF
/*     */     {
/*     */       public DeflaterOutputStream createDeflaterOutputStream(OutputStream os) throws IOException {
/*  97 */         return null;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract DeflaterOutputStream createDeflaterOutputStream(OutputStream param1OutputStream) throws IOException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractStringLayout.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<GelfLayout>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private String host;
/*     */ 
/*     */ 
/*     */     
/*     */     @PluginElement("AdditionalField")
/*     */     private KeyValuePair[] additionalFields;
/*     */ 
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/* 126 */     private GelfLayout.CompressionType compressionType = GelfLayout.CompressionType.GZIP;
/*     */     
/*     */     @PluginBuilderAttribute
/* 129 */     private int compressionThreshold = 1024;
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean includeStacktrace = true;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean includeThreadContext = true;
/*     */ 
/*     */     
/*     */     public Builder() {
/* 140 */       setCharset(StandardCharsets.UTF_8);
/*     */     }
/*     */ 
/*     */     
/*     */     public GelfLayout build() {
/* 145 */       return new GelfLayout(getConfiguration(), this.host, this.additionalFields, this.compressionType, this.compressionThreshold, this.includeStacktrace, this.includeThreadContext);
/*     */     }
/*     */     
/*     */     public String getHost() {
/* 149 */       return this.host;
/*     */     }
/*     */     
/*     */     public GelfLayout.CompressionType getCompressionType() {
/* 153 */       return this.compressionType;
/*     */     }
/*     */     
/*     */     public int getCompressionThreshold() {
/* 157 */       return this.compressionThreshold;
/*     */     }
/*     */     
/*     */     public boolean isIncludeStacktrace() {
/* 161 */       return this.includeStacktrace;
/*     */     }
/*     */     
/*     */     public boolean isIncludeThreadContext() {
/* 165 */       return this.includeThreadContext;
/*     */     }
/*     */     
/*     */     public KeyValuePair[] getAdditionalFields() {
/* 169 */       return this.additionalFields;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setHost(String host) {
/* 178 */       this.host = host;
/* 179 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setCompressionType(GelfLayout.CompressionType compressionType) {
/* 188 */       this.compressionType = compressionType;
/* 189 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setCompressionThreshold(int compressionThreshold) {
/* 198 */       this.compressionThreshold = compressionThreshold;
/* 199 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setIncludeStacktrace(boolean includeStacktrace) {
/* 209 */       this.includeStacktrace = includeStacktrace;
/* 210 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setIncludeThreadContext(boolean includeThreadContext) {
/* 219 */       this.includeThreadContext = includeThreadContext;
/* 220 */       return asBuilder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public B setAdditionalFields(KeyValuePair[] additionalFields) {
/* 229 */       this.additionalFields = additionalFields;
/* 230 */       return asBuilder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public GelfLayout(String host, KeyValuePair[] additionalFields, CompressionType compressionType, int compressionThreshold, boolean includeStacktrace) {
/* 240 */     this((Configuration)null, host, additionalFields, compressionType, compressionThreshold, includeStacktrace, true);
/*     */   }
/*     */ 
/*     */   
/*     */   private GelfLayout(Configuration config, String host, KeyValuePair[] additionalFields, CompressionType compressionType, int compressionThreshold, boolean includeStacktrace, boolean includeThreadContext) {
/* 245 */     super(config, StandardCharsets.UTF_8, (AbstractStringLayout.Serializer)null, (AbstractStringLayout.Serializer)null);
/* 246 */     this.host = (host != null) ? host : NetUtils.getLocalHostname();
/* 247 */     this.additionalFields = (additionalFields != null) ? additionalFields : new KeyValuePair[0];
/* 248 */     if (config == null) {
/* 249 */       for (KeyValuePair additionalField : this.additionalFields) {
/* 250 */         if (valueNeedsLookup(additionalField.getValue())) {
/* 251 */           throw new IllegalArgumentException("configuration needs to be set when there are additional fields with variables");
/*     */         }
/*     */       } 
/*     */     }
/* 255 */     this.compressionType = compressionType;
/* 256 */     this.compressionThreshold = compressionThreshold;
/* 257 */     this.includeStacktrace = includeStacktrace;
/* 258 */     this.includeThreadContext = includeThreadContext;
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
/*     */   @Deprecated
/*     */   public static GelfLayout createLayout(@PluginAttribute("host") String host, @PluginElement("AdditionalField") KeyValuePair[] additionalFields, @PluginAttribute(value = "compressionType", defaultString = "GZIP") CompressionType compressionType, @PluginAttribute(value = "compressionThreshold", defaultInt = 1024) int compressionThreshold, @PluginAttribute(value = "includeStacktrace", defaultBoolean = true) boolean includeStacktrace) {
/* 276 */     return new GelfLayout(null, host, additionalFields, compressionType, compressionThreshold, includeStacktrace, true);
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 281 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 286 */     return Collections.emptyMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 291 */     return "application/json; charset=" + getCharset();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] toByteArray(LogEvent event) {
/* 296 */     StringBuilder text = toText(event, getStringBuilder(), false);
/* 297 */     byte[] bytes = getBytes(text.toString());
/* 298 */     return (this.compressionType != CompressionType.OFF && bytes.length > this.compressionThreshold) ? compress(bytes) : bytes;
/*     */   }
/*     */ 
/*     */   
/*     */   public void encode(LogEvent event, ByteBufferDestination destination) {
/* 303 */     if (this.compressionType != CompressionType.OFF) {
/* 304 */       super.encode(event, destination);
/*     */       return;
/*     */     } 
/* 307 */     StringBuilder text = toText(event, getStringBuilder(), true);
/* 308 */     Encoder<StringBuilder> helper = getStringBuilderEncoder();
/* 309 */     helper.encode(text, destination);
/*     */   }
/*     */   
/*     */   private byte[] compress(byte[] bytes) {
/*     */     try {
/* 314 */       ByteArrayOutputStream baos = new ByteArrayOutputStream(this.compressionThreshold / 8);
/* 315 */       try (DeflaterOutputStream stream = this.compressionType.createDeflaterOutputStream(baos)) {
/* 316 */         if (stream == null) {
/* 317 */           return bytes;
/*     */         }
/* 319 */         stream.write(bytes);
/* 320 */         stream.finish();
/*     */       } 
/* 322 */       return baos.toByteArray();
/* 323 */     } catch (IOException e) {
/* 324 */       StatusLogger.getLogger().error(e);
/* 325 */       return bytes;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toSerializable(LogEvent event) {
/* 331 */     StringBuilder text = toText(event, getStringBuilder(), false);
/* 332 */     return text.toString();
/*     */   }
/*     */   
/*     */   private StringBuilder toText(LogEvent event, StringBuilder builder, boolean gcFree) {
/* 336 */     builder.append('{');
/* 337 */     builder.append("\"version\":\"1.1\",");
/* 338 */     builder.append("\"host\":\"");
/* 339 */     JsonUtils.quoteAsString(toNullSafeString(this.host), builder);
/* 340 */     builder.append("\",");
/* 341 */     builder.append("\"timestamp\":").append(formatTimestamp(event.getTimeMillis())).append(',');
/* 342 */     builder.append("\"level\":").append(formatLevel(event.getLevel())).append(',');
/* 343 */     if (event.getThreadName() != null) {
/* 344 */       builder.append("\"_thread\":\"");
/* 345 */       JsonUtils.quoteAsString(event.getThreadName(), builder);
/* 346 */       builder.append("\",");
/*     */     } 
/* 348 */     if (event.getLoggerName() != null) {
/* 349 */       builder.append("\"_logger\":\"");
/* 350 */       JsonUtils.quoteAsString(event.getLoggerName(), builder);
/* 351 */       builder.append("\",");
/*     */     } 
/* 353 */     if (this.additionalFields.length > 0) {
/* 354 */       StrSubstitutor strSubstitutor = getConfiguration().getStrSubstitutor();
/* 355 */       for (KeyValuePair additionalField : this.additionalFields) {
/* 356 */         builder.append("\"_");
/* 357 */         JsonUtils.quoteAsString(additionalField.getKey(), builder);
/* 358 */         builder.append("\":\"");
/* 359 */         String value = valueNeedsLookup(additionalField.getValue()) ? strSubstitutor.replace(event, additionalField.getValue()) : additionalField.getValue();
/*     */ 
/*     */         
/* 362 */         JsonUtils.quoteAsString(toNullSafeString(value), builder);
/* 363 */         builder.append("\",");
/*     */       } 
/*     */     } 
/* 366 */     if (this.includeThreadContext) {
/* 367 */       event.getContextData().forEach(WRITE_KEY_VALUES_INTO, builder);
/*     */     }
/* 369 */     if (event.getThrown() != null) {
/* 370 */       builder.append("\"full_message\":\"");
/* 371 */       if (this.includeStacktrace) {
/* 372 */         JsonUtils.quoteAsString(formatThrowable(event.getThrown()), builder);
/*     */       } else {
/* 374 */         JsonUtils.quoteAsString(event.getThrown().toString(), builder);
/*     */       } 
/* 376 */       builder.append("\",");
/*     */     } 
/*     */     
/* 379 */     builder.append("\"short_message\":\"");
/* 380 */     Message message = event.getMessage();
/* 381 */     if (message instanceof CharSequence) {
/* 382 */       JsonUtils.quoteAsString((CharSequence)message, builder);
/* 383 */     } else if (gcFree && message instanceof StringBuilderFormattable) {
/* 384 */       StringBuilder messageBuffer = getMessageStringBuilder();
/*     */       try {
/* 386 */         ((StringBuilderFormattable)message).formatTo(messageBuffer);
/* 387 */         JsonUtils.quoteAsString(messageBuffer, builder);
/*     */       } finally {
/* 389 */         trimToMaxSize(messageBuffer);
/*     */       } 
/*     */     } else {
/* 392 */       JsonUtils.quoteAsString(toNullSafeString(message.getFormattedMessage()), builder);
/*     */     } 
/* 394 */     builder.append('"');
/* 395 */     builder.append('}');
/* 396 */     return builder;
/*     */   }
/*     */   
/*     */   private static boolean valueNeedsLookup(String value) {
/* 400 */     return (value != null && value.contains("${"));
/*     */   }
/*     */   
/* 403 */   private static final TriConsumer<String, Object, StringBuilder> WRITE_KEY_VALUES_INTO = new TriConsumer<String, Object, StringBuilder>()
/*     */     {
/*     */       public void accept(String key, Object value, StringBuilder stringBuilder) {
/* 406 */         stringBuilder.append("\"_");
/* 407 */         JsonUtils.quoteAsString(key, stringBuilder);
/* 408 */         stringBuilder.append("\":\"");
/* 409 */         JsonUtils.quoteAsString(GelfLayout.toNullSafeString(String.valueOf(value)), stringBuilder);
/* 410 */         stringBuilder.append("\",");
/*     */       }
/*     */     };
/*     */   
/* 414 */   private static final ThreadLocal<StringBuilder> messageStringBuilder = new ThreadLocal<>();
/*     */   
/*     */   private static StringBuilder getMessageStringBuilder() {
/* 417 */     StringBuilder result = messageStringBuilder.get();
/* 418 */     if (result == null) {
/* 419 */       result = new StringBuilder(1024);
/* 420 */       messageStringBuilder.set(result);
/*     */     } 
/* 422 */     result.setLength(0);
/* 423 */     return result;
/*     */   }
/*     */   
/*     */   private static CharSequence toNullSafeString(CharSequence s) {
/* 427 */     return (s == null) ? "" : s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static CharSequence formatTimestamp(long timeMillis) {
/* 434 */     if (timeMillis < 1000L) {
/* 435 */       return "0";
/*     */     }
/* 437 */     StringBuilder builder = getTimestampStringBuilder();
/* 438 */     builder.append(timeMillis);
/* 439 */     builder.insert(builder.length() - 3, '.');
/* 440 */     return builder;
/*     */   }
/*     */   
/* 443 */   private static final ThreadLocal<StringBuilder> timestampStringBuilder = new ThreadLocal<>();
/*     */   
/*     */   private static StringBuilder getTimestampStringBuilder() {
/* 446 */     StringBuilder result = timestampStringBuilder.get();
/* 447 */     if (result == null) {
/* 448 */       result = new StringBuilder(20);
/* 449 */       timestampStringBuilder.set(result);
/*     */     } 
/* 451 */     result.setLength(0);
/* 452 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int formatLevel(Level level) {
/* 459 */     return Severity.getSeverity(level).getCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static CharSequence formatThrowable(Throwable throwable) {
/* 467 */     StringWriter sw = new StringWriter(2048);
/* 468 */     PrintWriter pw = new PrintWriter(sw);
/* 469 */     throwable.printStackTrace(pw);
/* 470 */     pw.flush();
/* 471 */     return sw.getBuffer();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\GelfLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */