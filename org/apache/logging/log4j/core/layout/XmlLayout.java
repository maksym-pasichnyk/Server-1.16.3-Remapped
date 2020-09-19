/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
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
/*     */ @Plugin(name = "XmlLayout", category = "Core", elementType = "layout", printObject = true)
/*     */ public final class XmlLayout
/*     */   extends AbstractJacksonLayout
/*     */ {
/*     */   private static final String ROOT_TAG = "Events";
/*     */   
/*     */   protected XmlLayout(boolean locationInfo, boolean properties, boolean complete, boolean compact, Charset charset, boolean includeStacktrace) {
/* 199 */     super((Configuration)null, (new JacksonFactory.XML(includeStacktrace)).newWriter(locationInfo, properties, compact), charset, compact, complete, false, (AbstractStringLayout.Serializer)null, (AbstractStringLayout.Serializer)null);
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
/*     */   public byte[] getHeader() {
/* 214 */     if (!this.complete) {
/* 215 */       return null;
/*     */     }
/* 217 */     StringBuilder buf = new StringBuilder();
/* 218 */     buf.append("<?xml version=\"1.0\" encoding=\"");
/* 219 */     buf.append(getCharset().name());
/* 220 */     buf.append("\"?>");
/* 221 */     buf.append(this.eol);
/*     */     
/* 223 */     buf.append('<');
/* 224 */     buf.append("Events");
/* 225 */     buf.append(" xmlns=\"http://logging.apache.org/log4j/2.0/events\">");
/* 226 */     buf.append(this.eol);
/* 227 */     return buf.toString().getBytes(getCharset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getFooter() {
/* 237 */     if (!this.complete) {
/* 238 */       return null;
/*     */     }
/* 240 */     return getBytes("</Events>" + this.eol);
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
/*     */   public Map<String, String> getContentFormat() {
/* 254 */     Map<String, String> result = new HashMap<>();
/*     */     
/* 256 */     result.put("xsd", "log4j-events.xsd");
/* 257 */     result.put("version", "2.0");
/* 258 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentType() {
/* 266 */     return "text/xml; charset=" + getCharset();
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
/*     */   @PluginFactory
/*     */   public static XmlLayout createLayout(@PluginAttribute("locationInfo") boolean locationInfo, @PluginAttribute("properties") boolean properties, @PluginAttribute("complete") boolean complete, @PluginAttribute("compact") boolean compact, @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset, @PluginAttribute(value = "includeStacktrace", defaultBoolean = true) boolean includeStacktrace) {
/* 292 */     return new XmlLayout(locationInfo, properties, complete, compact, charset, includeStacktrace);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XmlLayout createDefaultLayout() {
/* 301 */     return new XmlLayout(false, false, false, false, StandardCharsets.UTF_8, true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\XmlLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */