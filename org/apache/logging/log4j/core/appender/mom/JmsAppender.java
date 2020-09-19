/*     */ package org.apache.logging.log4j.core.appender.mom;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageProducer;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
/*     */ import org.apache.logging.log4j.core.layout.SerializedLayout;
/*     */ import org.apache.logging.log4j.core.net.JndiManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "JMS", category = "Core", elementType = "appender", printObject = true)
/*     */ @PluginAliases({"JMSQueue", "JMSTopic"})
/*     */ public class JmsAppender
/*     */   extends AbstractAppender
/*     */ {
/*     */   private final JmsManager manager;
/*     */   private final MessageProducer producer;
/*     */   
/*     */   protected JmsAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, JmsManager manager) throws JMSException {
/*  57 */     super(name, filter, layout, ignoreExceptions);
/*  58 */     this.manager = manager;
/*  59 */     this.producer = this.manager.createMessageProducer();
/*     */   }
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/*     */     try {
/*  65 */       Message message = this.manager.createMessage(getLayout().toSerializable(event));
/*  66 */       message.setJMSTimestamp(event.getTimeMillis());
/*  67 */       this.producer.send(message);
/*  68 */     } catch (JMSException e) {
/*  69 */       throw new AppenderLoggingException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/*  75 */     setStopping();
/*  76 */     boolean stopped = stop(timeout, timeUnit, false);
/*  77 */     stopped &= this.manager.stop(timeout, timeUnit);
/*  78 */     setStopped();
/*  79 */     return stopped;
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/*  84 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<JmsAppender>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "A name for the JmsAppender must be specified")
/*     */     private String name;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String factoryName;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String providerUrl;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String urlPkgPrefixes;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private String securityPrincipalName;
/*     */     
/*     */     @PluginBuilderAttribute(sensitive = true)
/*     */     private String securityCredentials;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     @Required(message = "A javax.jms.ConnectionFactory JNDI name must be specified")
/*     */     private String factoryBindingName;
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     @PluginAliases({"queueBindingName", "topicBindingName"})
/*     */     @Required(message = "A javax.jms.Destination JNDI name must be specified")
/*     */     private String destinationBindingName;
/*     */     @PluginBuilderAttribute
/*     */     private String username;
/*     */     @PluginBuilderAttribute(sensitive = true)
/*     */     private String password;
/*     */     @PluginElement("Layout")
/* 123 */     private Layout<? extends Serializable> layout = (Layout<? extends Serializable>)SerializedLayout.createLayout();
/*     */ 
/*     */     
/*     */     @PluginElement("Filter")
/*     */     private Filter filter;
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute
/*     */     private boolean ignoreExceptions = true;
/*     */ 
/*     */     
/*     */     private JmsManager jmsManager;
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setName(String name) {
/* 139 */       this.name = name;
/* 140 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setFactoryName(String factoryName) {
/* 144 */       this.factoryName = factoryName;
/* 145 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setProviderUrl(String providerUrl) {
/* 149 */       this.providerUrl = providerUrl;
/* 150 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setUrlPkgPrefixes(String urlPkgPrefixes) {
/* 154 */       this.urlPkgPrefixes = urlPkgPrefixes;
/* 155 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSecurityPrincipalName(String securityPrincipalName) {
/* 159 */       this.securityPrincipalName = securityPrincipalName;
/* 160 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSecurityCredentials(String securityCredentials) {
/* 164 */       this.securityCredentials = securityCredentials;
/* 165 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setFactoryBindingName(String factoryBindingName) {
/* 169 */       this.factoryBindingName = factoryBindingName;
/* 170 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDestinationBindingName(String destinationBindingName) {
/* 174 */       this.destinationBindingName = destinationBindingName;
/* 175 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setUsername(String username) {
/* 179 */       this.username = username;
/* 180 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setPassword(String password) {
/* 184 */       this.password = password;
/* 185 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setLayout(Layout<? extends Serializable> layout) {
/* 189 */       this.layout = layout;
/* 190 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setFilter(Filter filter) {
/* 194 */       this.filter = filter;
/* 195 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setJmsManager(JmsManager jmsManager) {
/* 199 */       this.jmsManager = jmsManager;
/* 200 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setIgnoreExceptions(boolean ignoreExceptions) {
/* 204 */       this.ignoreExceptions = ignoreExceptions;
/* 205 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public JmsAppender build() {
/* 210 */       JmsManager actualJmsManager = this.jmsManager;
/* 211 */       if (actualJmsManager == null) {
/* 212 */         JndiManager jndiManager = JndiManager.getJndiManager(this.factoryName, this.providerUrl, this.urlPkgPrefixes, this.securityPrincipalName, this.securityCredentials, null);
/*     */         
/* 214 */         actualJmsManager = JmsManager.getJmsManager(this.name, jndiManager, this.factoryBindingName, this.destinationBindingName, this.username, this.password);
/*     */       } 
/*     */       
/*     */       try {
/* 218 */         return new JmsAppender(this.name, this.filter, this.layout, this.ignoreExceptions, actualJmsManager);
/* 219 */       } catch (JMSException e) {
/* 220 */         JmsAppender.LOGGER.error("Error creating JmsAppender [{}].", this.name, e);
/* 221 */         return null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private Builder() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\mom\JmsAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */