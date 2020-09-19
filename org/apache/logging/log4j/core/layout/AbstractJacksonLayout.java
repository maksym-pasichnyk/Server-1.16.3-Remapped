/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.ObjectWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.impl.MutableLogEvent;
/*     */ import org.apache.logging.log4j.core.util.StringBuilderWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractJacksonLayout
/*     */   extends AbstractStringLayout
/*     */ {
/*     */   protected static final String DEFAULT_EOL = "\r\n";
/*     */   protected static final String COMPACT_EOL = "";
/*     */   protected final String eol;
/*     */   protected final ObjectWriter objectWriter;
/*     */   protected final boolean compact;
/*     */   protected final boolean complete;
/*     */   
/*     */   public static abstract class Builder<B extends Builder<B>>
/*     */     extends AbstractStringLayout.Builder<B>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private boolean eventEol;
/*     */     @PluginBuilderAttribute
/*     */     private boolean compact;
/*     */     @PluginBuilderAttribute
/*     */     private boolean complete;
/*     */     
/*     */     public boolean getEventEol() {
/*  51 */       return this.eventEol;
/*     */     }
/*     */     
/*     */     public boolean isCompact() {
/*  55 */       return this.compact;
/*     */     }
/*     */     
/*     */     public boolean isComplete() {
/*  59 */       return this.complete;
/*     */     }
/*     */     
/*     */     public B setEventEol(boolean eventEol) {
/*  63 */       this.eventEol = eventEol;
/*  64 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setCompact(boolean compact) {
/*  68 */       this.compact = compact;
/*  69 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setComplete(boolean complete) {
/*  73 */       this.complete = complete;
/*  74 */       return asBuilder();
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
/*     */   protected AbstractJacksonLayout(Configuration config, ObjectWriter objectWriter, Charset charset, boolean compact, boolean complete, boolean eventEol, AbstractStringLayout.Serializer headerSerializer, AbstractStringLayout.Serializer footerSerializer) {
/*  87 */     super(config, charset, headerSerializer, footerSerializer);
/*  88 */     this.objectWriter = objectWriter;
/*  89 */     this.compact = compact;
/*  90 */     this.complete = complete;
/*  91 */     this.eol = (compact && !eventEol) ? "" : "\r\n";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toSerializable(LogEvent event) {
/* 102 */     StringBuilderWriter writer = new StringBuilderWriter();
/*     */     try {
/* 104 */       toSerializable(event, (Writer)writer);
/* 105 */       return writer.toString();
/* 106 */     } catch (IOException e) {
/*     */       
/* 108 */       LOGGER.error(e);
/* 109 */       return "";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static LogEvent convertMutableToLog4jEvent(LogEvent event) {
/* 117 */     return (event instanceof MutableLogEvent) ? (LogEvent)((MutableLogEvent)event).createMemento() : event;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void toSerializable(LogEvent event, Writer writer) throws JsonGenerationException, JsonMappingException, IOException {
/* 124 */     this.objectWriter.writeValue(writer, convertMutableToLog4jEvent(event));
/* 125 */     writer.write(this.eol);
/* 126 */     markEvent();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\AbstractJacksonLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */