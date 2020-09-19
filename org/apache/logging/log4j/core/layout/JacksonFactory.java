/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*     */ import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.ObjectWriter;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
/*     */ import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.apache.logging.log4j.core.impl.Log4jLogEvent;
/*     */ import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;
/*     */ import org.apache.logging.log4j.core.jackson.Log4jXmlObjectMapper;
/*     */ import org.apache.logging.log4j.core.jackson.Log4jYamlObjectMapper;
/*     */ import org.codehaus.stax2.XMLStreamWriter2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class JacksonFactory
/*     */ {
/*     */   protected abstract String getPropertNameForContextMap();
/*     */   
/*     */   protected abstract String getPropertNameForSource();
/*     */   
/*     */   protected abstract String getPropertNameForNanoTime();
/*     */   
/*     */   protected abstract PrettyPrinter newCompactPrinter();
/*     */   
/*     */   protected abstract ObjectMapper newObjectMapper();
/*     */   
/*     */   protected abstract PrettyPrinter newPrettyPrinter();
/*     */   
/*     */   static class JSON
/*     */     extends JacksonFactory
/*     */   {
/*     */     private final boolean encodeThreadContextAsList;
/*     */     private final boolean includeStacktrace;
/*     */     
/*     */     public JSON(boolean encodeThreadContextAsList, boolean includeStacktrace) {
/*  49 */       this.encodeThreadContextAsList = encodeThreadContextAsList;
/*  50 */       this.includeStacktrace = includeStacktrace;
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForContextMap() {
/*  55 */       return "contextMap";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForSource() {
/*  60 */       return "source";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForNanoTime() {
/*  65 */       return "nanoTime";
/*     */     }
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newCompactPrinter() {
/*  70 */       return (PrettyPrinter)new MinimalPrettyPrinter();
/*     */     }
/*     */ 
/*     */     
/*     */     protected ObjectMapper newObjectMapper() {
/*  75 */       return (ObjectMapper)new Log4jJsonObjectMapper(this.encodeThreadContextAsList, this.includeStacktrace);
/*     */     }
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newPrettyPrinter() {
/*  80 */       return (PrettyPrinter)new DefaultPrettyPrinter();
/*     */     }
/*     */   }
/*     */   
/*     */   static class XML
/*     */     extends JacksonFactory
/*     */   {
/*     */     static final int DEFAULT_INDENT = 1;
/*     */     private final boolean includeStacktrace;
/*     */     
/*     */     public XML(boolean includeStacktrace) {
/*  91 */       this.includeStacktrace = includeStacktrace;
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForContextMap() {
/*  96 */       return "ContextMap";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForSource() {
/* 101 */       return "Source";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForNanoTime() {
/* 106 */       return "nanoTime";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newCompactPrinter() {
/* 112 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     protected ObjectMapper newObjectMapper() {
/* 117 */       return (ObjectMapper)new Log4jXmlObjectMapper(this.includeStacktrace);
/*     */     }
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newPrettyPrinter() {
/* 122 */       return (PrettyPrinter)new JacksonFactory.Log4jXmlPrettyPrinter(1);
/*     */     }
/*     */   }
/*     */   
/*     */   static class YAML
/*     */     extends JacksonFactory {
/*     */     private final boolean includeStacktrace;
/*     */     
/*     */     public YAML(boolean includeStacktrace) {
/* 131 */       this.includeStacktrace = includeStacktrace;
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForContextMap() {
/* 136 */       return "contextMap";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForSource() {
/* 141 */       return "source";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPropertNameForNanoTime() {
/* 146 */       return "nanoTime";
/*     */     }
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newCompactPrinter() {
/* 151 */       return (PrettyPrinter)new MinimalPrettyPrinter();
/*     */     }
/*     */ 
/*     */     
/*     */     protected ObjectMapper newObjectMapper() {
/* 156 */       return (ObjectMapper)new Log4jYamlObjectMapper(false, this.includeStacktrace);
/*     */     }
/*     */ 
/*     */     
/*     */     protected PrettyPrinter newPrettyPrinter() {
/* 161 */       return (PrettyPrinter)new DefaultPrettyPrinter();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Log4jXmlPrettyPrinter
/*     */     extends DefaultXmlPrettyPrinter
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Log4jXmlPrettyPrinter(int nesting) {
/* 181 */       this._nesting = nesting;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void writePrologLinefeed(XMLStreamWriter2 sw) throws XMLStreamException {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DefaultXmlPrettyPrinter createInstance() {
/* 194 */       return new Log4jXmlPrettyPrinter(1);
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
/*     */   ObjectWriter newWriter(boolean locationInfo, boolean properties, boolean compact) {
/* 212 */     SimpleFilterProvider filters = new SimpleFilterProvider();
/* 213 */     Set<String> except = new HashSet<>(2);
/* 214 */     if (!locationInfo) {
/* 215 */       except.add(getPropertNameForSource());
/*     */     }
/* 217 */     if (!properties) {
/* 218 */       except.add(getPropertNameForContextMap());
/*     */     }
/* 220 */     except.add(getPropertNameForNanoTime());
/* 221 */     filters.addFilter(Log4jLogEvent.class.getName(), SimpleBeanPropertyFilter.serializeAllExcept(except));
/* 222 */     ObjectWriter writer = newObjectMapper().writer(compact ? newCompactPrinter() : newPrettyPrinter());
/* 223 */     return writer.with((FilterProvider)filters);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\JacksonFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */