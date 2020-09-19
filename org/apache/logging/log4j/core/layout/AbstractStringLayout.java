/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.StringLayout;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.LoggerConfig;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.impl.DefaultLogEventFactory;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.StringEncoder;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
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
/*     */ public abstract class AbstractStringLayout
/*     */   extends AbstractLayout<String>
/*     */   implements StringLayout
/*     */ {
/*     */   protected static final int DEFAULT_STRING_BUILDER_SIZE = 1024;
/*     */   
/*     */   public static interface Serializer2
/*     */   {
/*     */     StringBuilder toSerializable(LogEvent param1LogEvent, StringBuilder param1StringBuilder);
/*     */   }
/*     */   
/*     */   public static interface Serializer
/*     */   {
/*     */     String toSerializable(LogEvent param1LogEvent);
/*     */   }
/*     */   
/*     */   public static abstract class Builder<B extends Builder<B>>
/*     */     extends AbstractLayout.Builder<B>
/*     */   {
/*     */     @PluginBuilderAttribute("charset")
/*     */     private Charset charset;
/*     */     @PluginElement("footerSerializer")
/*     */     private AbstractStringLayout.Serializer footerSerializer;
/*     */     @PluginElement("headerSerializer")
/*     */     private AbstractStringLayout.Serializer headerSerializer;
/*     */     
/*     */     public Charset getCharset() {
/*  60 */       return this.charset;
/*     */     }
/*     */     
/*     */     public AbstractStringLayout.Serializer getFooterSerializer() {
/*  64 */       return this.footerSerializer;
/*     */     }
/*     */     
/*     */     public AbstractStringLayout.Serializer getHeaderSerializer() {
/*  68 */       return this.headerSerializer;
/*     */     }
/*     */     
/*     */     public B setCharset(Charset charset) {
/*  72 */       this.charset = charset;
/*  73 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setFooterSerializer(AbstractStringLayout.Serializer footerSerializer) {
/*  77 */       this.footerSerializer = footerSerializer;
/*  78 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setHeaderSerializer(AbstractStringLayout.Serializer headerSerializer) {
/*  82 */       this.headerSerializer = headerSerializer;
/*  83 */       return asBuilder();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   protected static final int MAX_STRING_BUILDER_SIZE = Math.max(1024, size("log4j.layoutStringBuilder.maxSize", 2048));
/*     */ 
/*     */   
/* 108 */   private static final ThreadLocal<StringBuilder> threadLocal = new ThreadLocal<>();
/*     */   
/*     */   private Encoder<StringBuilder> textEncoder;
/*     */   
/*     */   private transient Charset charset;
/*     */   private final String charsetName;
/*     */   
/*     */   protected static StringBuilder getStringBuilder() {
/* 116 */     StringBuilder result = threadLocal.get();
/* 117 */     if (result == null) {
/* 118 */       result = new StringBuilder(1024);
/* 119 */       threadLocal.set(result);
/*     */     } 
/* 121 */     trimToMaxSize(result);
/* 122 */     result.setLength(0);
/* 123 */     return result;
/*     */   }
/*     */   private final Serializer footerSerializer; private final Serializer headerSerializer; private final boolean useCustomEncoding;
/*     */   
/*     */   private static boolean isPreJava8() {
/* 128 */     String version = System.getProperty("java.version");
/* 129 */     String[] parts = version.split("\\.");
/*     */     try {
/* 131 */       int major = Integer.parseInt(parts[1]);
/* 132 */       return (major < 8);
/* 133 */     } catch (Exception ex) {
/* 134 */       return true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int size(String property, int defaultValue) {
/* 139 */     return PropertiesUtil.getProperties().getIntegerProperty(property, defaultValue);
/*     */   }
/*     */   
/*     */   protected static void trimToMaxSize(StringBuilder stringBuilder) {
/* 143 */     if (stringBuilder.length() > MAX_STRING_BUILDER_SIZE) {
/* 144 */       stringBuilder.setLength(MAX_STRING_BUILDER_SIZE);
/* 145 */       stringBuilder.trimToSize();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractStringLayout(Charset charset) {
/* 165 */     this(charset, (byte[])null, (byte[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractStringLayout(Charset aCharset, byte[] header, byte[] footer) {
/* 176 */     super((Configuration)null, header, footer);
/* 177 */     this.headerSerializer = null;
/* 178 */     this.footerSerializer = null;
/* 179 */     this.charset = (aCharset == null) ? StandardCharsets.UTF_8 : aCharset;
/* 180 */     this.charsetName = this.charset.name();
/* 181 */     this.useCustomEncoding = (isPreJava8() && (StandardCharsets.ISO_8859_1.equals(aCharset) || StandardCharsets.US_ASCII.equals(aCharset)));
/*     */     
/* 183 */     this.textEncoder = Constants.ENABLE_DIRECT_ENCODERS ? new StringBuilderEncoder(this.charset) : null;
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
/*     */   protected AbstractStringLayout(Configuration config, Charset aCharset, Serializer headerSerializer, Serializer footerSerializer) {
/* 196 */     super(config, (byte[])null, (byte[])null);
/* 197 */     this.headerSerializer = headerSerializer;
/* 198 */     this.footerSerializer = footerSerializer;
/* 199 */     this.charset = (aCharset == null) ? StandardCharsets.UTF_8 : aCharset;
/* 200 */     this.charsetName = this.charset.name();
/* 201 */     this.useCustomEncoding = (isPreJava8() && (StandardCharsets.ISO_8859_1.equals(aCharset) || StandardCharsets.US_ASCII.equals(aCharset)));
/*     */     
/* 203 */     this.textEncoder = Constants.ENABLE_DIRECT_ENCODERS ? new StringBuilderEncoder(this.charset) : null;
/*     */   }
/*     */   
/*     */   protected byte[] getBytes(String s) {
/* 207 */     if (this.useCustomEncoding) {
/* 208 */       return StringEncoder.encodeSingleByteChars(s);
/*     */     }
/*     */     try {
/* 211 */       return s.getBytes(this.charsetName);
/* 212 */     } catch (UnsupportedEncodingException e) {
/* 213 */       return s.getBytes(this.charset);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Charset getCharset() {
/* 219 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 227 */     return "text/plain";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getFooter() {
/* 237 */     return serializeToBytes(this.footerSerializer, super.getFooter());
/*     */   }
/*     */   
/*     */   public Serializer getFooterSerializer() {
/* 241 */     return this.footerSerializer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getHeader() {
/* 251 */     return serializeToBytes(this.headerSerializer, super.getHeader());
/*     */   }
/*     */   
/*     */   public Serializer getHeaderSerializer() {
/* 255 */     return this.headerSerializer;
/*     */   }
/*     */   
/*     */   private DefaultLogEventFactory getLogEventFactory() {
/* 259 */     return DefaultLogEventFactory.getInstance();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Encoder<StringBuilder> getStringBuilderEncoder() {
/* 268 */     if (this.textEncoder == null) {
/* 269 */       this.textEncoder = new StringBuilderEncoder(getCharset());
/*     */     }
/* 271 */     return this.textEncoder;
/*     */   }
/*     */   
/*     */   protected byte[] serializeToBytes(Serializer serializer, byte[] defaultValue) {
/* 275 */     String serializable = serializeToString(serializer);
/* 276 */     if (serializer == null) {
/* 277 */       return defaultValue;
/*     */     }
/* 279 */     return StringEncoder.toBytes(serializable, getCharset());
/*     */   }
/*     */   
/*     */   protected String serializeToString(Serializer serializer) {
/* 283 */     if (serializer == null) {
/* 284 */       return null;
/*     */     }
/* 286 */     LoggerConfig rootLogger = getConfiguration().getRootLogger();
/*     */     
/* 288 */     LogEvent logEvent = getLogEventFactory().createEvent(rootLogger.getName(), null, "", rootLogger.getLevel(), null, null, null);
/*     */     
/* 290 */     return serializer.toSerializable(logEvent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] toByteArray(LogEvent event) {
/* 301 */     return getBytes((String)toSerializable(event));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\AbstractStringLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */