/*      */ package org.apache.logging.log4j.core.layout;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import org.apache.logging.log4j.core.LogEvent;
/*      */ import org.apache.logging.log4j.core.config.Configuration;
/*      */ import org.apache.logging.log4j.core.config.DefaultConfiguration;
/*      */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*      */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*      */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*      */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*      */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Plugin(name = "JsonLayout", category = "Core", elementType = "layout", printObject = true)
/*      */ public final class JsonLayout
/*      */   extends AbstractJacksonLayout
/*      */ {
/*      */   private static final String DEFAULT_FOOTER = "]";
/*      */   private static final String DEFAULT_HEADER = "[";
/*      */   static final String CONTENT_TYPE = "application/json";
/*      */   
/*      */   public static class Builder<B extends Builder<B>>
/*      */     extends AbstractJacksonLayout.Builder<B>
/*      */     implements org.apache.logging.log4j.core.util.Builder<JsonLayout>
/*      */   {
/*      */     @PluginBuilderAttribute
/*      */     private boolean locationInfo;
/*      */     @PluginBuilderAttribute
/*      */     private boolean properties;
/*      */     @PluginBuilderAttribute
/*      */     private boolean propertiesAsList;
/*      */     @PluginBuilderAttribute
/*      */     private boolean includeStacktrace = true;
/*      */     
/*      */     public Builder() {
/*  830 */       setCharset(StandardCharsets.UTF_8);
/*      */     }
/*      */ 
/*      */     
/*      */     public JsonLayout build() {
/*  835 */       boolean encodeThreadContextAsList = (this.properties && this.propertiesAsList);
/*  836 */       String headerPattern = toStringOrNull(getHeader());
/*  837 */       String footerPattern = toStringOrNull(getFooter());
/*  838 */       return new JsonLayout(getConfiguration(), this.locationInfo, this.properties, encodeThreadContextAsList, isComplete(), isCompact(), getEventEol(), headerPattern, footerPattern, getCharset(), this.includeStacktrace);
/*      */     }
/*      */ 
/*      */     
/*      */     private String toStringOrNull(byte[] header) {
/*  843 */       return (header == null) ? null : new String(header, Charset.defaultCharset());
/*      */     }
/*      */     
/*      */     public boolean isLocationInfo() {
/*  847 */       return this.locationInfo;
/*      */     }
/*      */     
/*      */     public boolean isProperties() {
/*  851 */       return this.properties;
/*      */     }
/*      */     
/*      */     public boolean isPropertiesAsList() {
/*  855 */       return this.propertiesAsList;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isIncludeStacktrace() {
/*  863 */       return this.includeStacktrace;
/*      */     }
/*      */     
/*      */     public B setLocationInfo(boolean locationInfo) {
/*  867 */       this.locationInfo = locationInfo;
/*  868 */       return asBuilder();
/*      */     }
/*      */     
/*      */     public B setProperties(boolean properties) {
/*  872 */       this.properties = properties;
/*  873 */       return asBuilder();
/*      */     }
/*      */     
/*      */     public B setPropertiesAsList(boolean propertiesAsList) {
/*  877 */       this.propertiesAsList = propertiesAsList;
/*  878 */       return asBuilder();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public B setIncludeStacktrace(boolean includeStacktrace) {
/*  887 */       this.includeStacktrace = includeStacktrace;
/*  888 */       return asBuilder();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonLayout(Configuration config, boolean locationInfo, boolean properties, boolean encodeThreadContextAsList, boolean complete, boolean compact, boolean eventEol, String headerPattern, String footerPattern, Charset charset, boolean includeStacktrace) {
/*  896 */     super(config, (new JacksonFactory.JSON(encodeThreadContextAsList, includeStacktrace)).newWriter(locationInfo, properties, compact), charset, compact, complete, eventEol, PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(headerPattern).setDefaultPattern("[").build(), PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footerPattern).setDefaultPattern("]").build());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getHeader() {
/*  910 */     if (!this.complete) {
/*  911 */       return null;
/*      */     }
/*  913 */     StringBuilder buf = new StringBuilder();
/*  914 */     String str = serializeToString(getHeaderSerializer());
/*  915 */     if (str != null) {
/*  916 */       buf.append(str);
/*      */     }
/*  918 */     buf.append(this.eol);
/*  919 */     return getBytes(buf.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getFooter() {
/*  929 */     if (!this.complete) {
/*  930 */       return null;
/*      */     }
/*  932 */     StringBuilder buf = new StringBuilder();
/*  933 */     buf.append(this.eol);
/*  934 */     String str = serializeToString(getFooterSerializer());
/*  935 */     if (str != null) {
/*  936 */       buf.append(str);
/*      */     }
/*  938 */     buf.append(this.eol);
/*  939 */     return getBytes(buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, String> getContentFormat() {
/*  944 */     Map<String, String> result = new HashMap<>();
/*  945 */     result.put("version", "2.0");
/*  946 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getContentType() {
/*  954 */     return "application/json; charset=" + getCharset();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static JsonLayout createLayout(@PluginConfiguration Configuration config, @PluginAttribute("locationInfo") boolean locationInfo, @PluginAttribute("properties") boolean properties, @PluginAttribute("propertiesAsList") boolean propertiesAsList, @PluginAttribute("complete") boolean complete, @PluginAttribute("compact") boolean compact, @PluginAttribute("eventEol") boolean eventEol, @PluginAttribute(value = "header", defaultString = "[") String headerPattern, @PluginAttribute(value = "footer", defaultString = "]") String footerPattern, @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset, @PluginAttribute(value = "includeStacktrace", defaultBoolean = true) boolean includeStacktrace) {
/* 1003 */     boolean encodeThreadContextAsList = (properties && propertiesAsList);
/* 1004 */     return new JsonLayout(config, locationInfo, properties, encodeThreadContextAsList, complete, compact, eventEol, headerPattern, footerPattern, charset, includeStacktrace);
/*      */   }
/*      */ 
/*      */   
/*      */   @PluginBuilderFactory
/*      */   public static <B extends Builder<B>> B newBuilder() {
/* 1010 */     return (B)(new Builder<>()).asBuilder();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static JsonLayout createDefaultLayout() {
/* 1019 */     return new JsonLayout((Configuration)new DefaultConfiguration(), false, false, false, false, false, false, "[", "]", StandardCharsets.UTF_8, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void toSerializable(LogEvent event, Writer writer) throws IOException {
/* 1025 */     if (this.complete && this.eventCount > 0L) {
/* 1026 */       writer.append(", ");
/*      */     }
/* 1028 */     super.toSerializable(event, writer);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\JsonLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */