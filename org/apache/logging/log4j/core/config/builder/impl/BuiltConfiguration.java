/*     */ package org.apache.logging.log4j.core.config.builder.impl;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.AbstractConfiguration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.ConfiguratonFileWatcher;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.Reconfigurable;
/*     */ import org.apache.logging.log4j.core.config.builder.api.Component;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
/*     */ import org.apache.logging.log4j.core.config.status.StatusConfiguration;
/*     */ import org.apache.logging.log4j.core.util.FileWatcher;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BuiltConfiguration
/*     */   extends AbstractConfiguration
/*     */ {
/*  46 */   private static final String[] VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
/*     */   private final StatusConfiguration statusConfig;
/*     */   protected Component rootComponent;
/*     */   private Component loggersComponent;
/*     */   private Component appendersComponent;
/*     */   private Component filtersComponent;
/*     */   private Component propertiesComponent;
/*     */   private Component customLevelsComponent;
/*     */   private Component scriptsComponent;
/*  55 */   private String contentType = "text";
/*     */   
/*     */   public BuiltConfiguration(LoggerContext loggerContext, ConfigurationSource source, Component rootComponent) {
/*  58 */     super(loggerContext, source);
/*  59 */     this.statusConfig = (new StatusConfiguration()).withVerboseClasses(VERBOSE_CLASSES).withStatus(getDefaultStatus());
/*  60 */     for (Component component : rootComponent.getComponents()) {
/*  61 */       switch (component.getPluginType()) {
/*     */         case "Scripts":
/*  63 */           this.scriptsComponent = component;
/*     */ 
/*     */         
/*     */         case "Loggers":
/*  67 */           this.loggersComponent = component;
/*     */ 
/*     */         
/*     */         case "Appenders":
/*  71 */           this.appendersComponent = component;
/*     */ 
/*     */         
/*     */         case "Filters":
/*  75 */           this.filtersComponent = component;
/*     */ 
/*     */         
/*     */         case "Properties":
/*  79 */           this.propertiesComponent = component;
/*     */ 
/*     */         
/*     */         case "CustomLevels":
/*  83 */           this.customLevelsComponent = component;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/*  88 */     this.rootComponent = rootComponent;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setup() {
/*  93 */     List<Node> children = this.rootNode.getChildren();
/*  94 */     if (this.propertiesComponent.getComponents().size() > 0) {
/*  95 */       children.add(convertToNode(this.rootNode, this.propertiesComponent));
/*     */     }
/*  97 */     if (this.scriptsComponent.getComponents().size() > 0) {
/*  98 */       children.add(convertToNode(this.rootNode, this.scriptsComponent));
/*     */     }
/* 100 */     if (this.customLevelsComponent.getComponents().size() > 0) {
/* 101 */       children.add(convertToNode(this.rootNode, this.customLevelsComponent));
/*     */     }
/* 103 */     children.add(convertToNode(this.rootNode, this.loggersComponent));
/* 104 */     children.add(convertToNode(this.rootNode, this.appendersComponent));
/* 105 */     if (this.filtersComponent.getComponents().size() > 0) {
/* 106 */       if (this.filtersComponent.getComponents().size() == 1) {
/* 107 */         children.add(convertToNode(this.rootNode, this.filtersComponent.getComponents().get(0)));
/*     */       } else {
/* 109 */         children.add(convertToNode(this.rootNode, this.filtersComponent));
/*     */       } 
/*     */     }
/* 112 */     this.rootComponent = null;
/*     */   }
/*     */   
/*     */   public String getContentType() {
/* 116 */     return this.contentType;
/*     */   }
/*     */   
/*     */   public void setContentType(String contentType) {
/* 120 */     this.contentType = contentType;
/*     */   }
/*     */   
/*     */   public void createAdvertiser(String advertiserString, ConfigurationSource configSource) {
/* 124 */     byte[] buffer = null;
/*     */     try {
/* 126 */       if (configSource != null) {
/* 127 */         InputStream is = configSource.getInputStream();
/* 128 */         if (is != null) {
/* 129 */           buffer = toByteArray(is);
/*     */         }
/*     */       } 
/* 132 */     } catch (IOException ioe) {
/* 133 */       LOGGER.warn("Unable to read configuration source " + configSource.toString());
/*     */     } 
/* 135 */     createAdvertiser(advertiserString, configSource, buffer, this.contentType);
/*     */   }
/*     */   
/*     */   public StatusConfiguration getStatusConfiguration() {
/* 139 */     return this.statusConfig;
/*     */   }
/*     */   
/*     */   public void setPluginPackages(String packages) {
/* 143 */     this.pluginPackages.addAll(Arrays.asList(packages.split(Patterns.COMMA_SEPARATOR)));
/*     */   }
/*     */   
/*     */   public void setShutdownHook(String flag) {
/* 147 */     this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(flag);
/*     */   }
/*     */   
/*     */   public void setShutdownTimeoutMillis(long shutdownTimeoutMillis) {
/* 151 */     this.shutdownTimeoutMillis = shutdownTimeoutMillis;
/*     */   }
/*     */   
/*     */   public void setMonitorInterval(int intervalSeconds) {
/* 155 */     if (this instanceof Reconfigurable && intervalSeconds > 0) {
/* 156 */       ConfigurationSource configSource = getConfigurationSource();
/* 157 */       if (configSource != null) {
/* 158 */         File configFile = configSource.getFile();
/* 159 */         if (intervalSeconds > 0) {
/* 160 */           getWatchManager().setIntervalSeconds(intervalSeconds);
/* 161 */           if (configFile != null) {
/* 162 */             ConfiguratonFileWatcher configuratonFileWatcher = new ConfiguratonFileWatcher((Reconfigurable)this, this.listeners);
/* 163 */             getWatchManager().watchFile(configFile, (FileWatcher)configuratonFileWatcher);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PluginManager getPluginManager() {
/* 172 */     return this.pluginManager;
/*     */   }
/*     */   
/*     */   protected Node convertToNode(Node parent, Component component) {
/* 176 */     String name = component.getPluginType();
/* 177 */     PluginType<?> pluginType = this.pluginManager.getPluginType(name);
/* 178 */     Node node = new Node(parent, name, pluginType);
/* 179 */     node.getAttributes().putAll(component.getAttributes());
/* 180 */     node.setValue(component.getValue());
/* 181 */     List<Node> children = node.getChildren();
/* 182 */     for (Component child : component.getComponents()) {
/* 183 */       children.add(convertToNode(node, child));
/*     */     }
/* 185 */     return node;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\builder\impl\BuiltConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */