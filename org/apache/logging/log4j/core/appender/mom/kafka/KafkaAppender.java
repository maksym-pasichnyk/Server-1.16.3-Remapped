/*     */ package org.apache.logging.log4j.core.appender.mom.kafka;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.util.StringEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "Kafka", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class KafkaAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private final KafkaManager manager;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<KafkaAppender>
/*     */   {
/*     */     @PluginAttribute("topic")
/*     */     private String topic;
/*     */     @PluginAttribute(value = "syncSend", defaultBoolean = true)
/*     */     private boolean syncSend;
/*     */     @PluginElement("Properties")
/*     */     private Property[] properties;
/*     */     
/*     */     public KafkaAppender build() {
/*  68 */       KafkaManager kafkaManager = new KafkaManager(getConfiguration().getLoggerContext(), getName(), this.topic, this.syncSend, this.properties);
/*  69 */       return new KafkaAppender(getName(), getLayout(), getFilter(), isIgnoreExceptions(), kafkaManager);
/*     */     }
/*     */     
/*     */     public String getTopic() {
/*  73 */       return this.topic;
/*     */     }
/*     */     
/*     */     public Property[] getProperties() {
/*  77 */       return this.properties;
/*     */     }
/*     */     
/*     */     public B setTopic(String topic) {
/*  81 */       this.topic = topic;
/*  82 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setSyncSend(boolean syncSend) {
/*  86 */       this.syncSend = syncSend;
/*  87 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setProperties(Property[] properties) {
/*  91 */       this.properties = properties;
/*  92 */       return (B)asBuilder();
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
/*     */   @Deprecated
/*     */   public static KafkaAppender createAppender(@PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter, @Required(message = "No name provided for KafkaAppender") @PluginAttribute("name") String name, @PluginAttribute(value = "ignoreExceptions", defaultBoolean = true) boolean ignoreExceptions, @Required(message = "No topic provided for KafkaAppender") @PluginAttribute("topic") String topic, @PluginElement("Properties") Property[] properties, @PluginConfiguration Configuration configuration) {
/* 105 */     KafkaManager kafkaManager = new KafkaManager(configuration.getLoggerContext(), name, topic, true, properties);
/* 106 */     return new KafkaAppender(name, layout, filter, ignoreExceptions, kafkaManager);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 115 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private KafkaAppender(String name, Layout<? extends Serializable> layout, Filter filter, boolean ignoreExceptions, KafkaManager manager) {
/* 122 */     super(name, filter, layout, ignoreExceptions);
/* 123 */     this.manager = Objects.<KafkaManager>requireNonNull(manager, "manager");
/*     */   }
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/* 128 */     if (event.getLoggerName().startsWith("org.apache.kafka")) {
/* 129 */       LOGGER.warn("Recursive logging from [{}] for appender [{}].", event.getLoggerName(), getName());
/*     */     } else {
/*     */       try {
/* 132 */         byte[] data; Layout<? extends Serializable> layout = getLayout();
/*     */         
/* 134 */         if (layout != null) {
/* 135 */           if (layout instanceof org.apache.logging.log4j.core.layout.SerializedLayout) {
/* 136 */             byte[] header = layout.getHeader();
/* 137 */             byte[] body = layout.toByteArray(event);
/* 138 */             data = new byte[header.length + body.length];
/* 139 */             System.arraycopy(header, 0, data, 0, header.length);
/* 140 */             System.arraycopy(body, 0, data, header.length, body.length);
/*     */           } else {
/* 142 */             data = layout.toByteArray(event);
/*     */           } 
/*     */         } else {
/* 145 */           data = StringEncoder.toBytes(event.getMessage().getFormattedMessage(), StandardCharsets.UTF_8);
/*     */         } 
/* 147 */         this.manager.send(data);
/* 148 */       } catch (Exception e) {
/* 149 */         LOGGER.error("Unable to write to Kafka [{}] for appender [{}].", this.manager.getName(), getName(), e);
/* 150 */         throw new AppenderLoggingException("Unable to write to Kafka in appender: " + e.getMessage(), e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 157 */     super.start();
/* 158 */     this.manager.startup();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 163 */     setStopping();
/* 164 */     boolean stopped = stop(timeout, timeUnit, false);
/* 165 */     stopped &= this.manager.stop(timeout, timeUnit);
/* 166 */     setStopped();
/* 167 */     return stopped;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 172 */     return "KafkaAppender{name=" + getName() + ", state=" + getState() + ", topic=" + this.manager.getTopic() + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\mom\kafka\KafkaAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */