/*     */ package org.apache.logging.log4j.core.config.composite;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.config.AbstractConfiguration;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.ConfiguratonFileWatcher;
/*     */ import org.apache.logging.log4j.core.config.Node;
/*     */ import org.apache.logging.log4j.core.config.Reconfigurable;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
/*     */ import org.apache.logging.log4j.core.config.status.StatusConfiguration;
/*     */ import org.apache.logging.log4j.core.util.FileWatcher;
/*     */ import org.apache.logging.log4j.core.util.Patterns;
/*     */ import org.apache.logging.log4j.core.util.WatchManager;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompositeConfiguration
/*     */   extends AbstractConfiguration
/*     */   implements Reconfigurable
/*     */ {
/*     */   public static final String MERGE_STRATEGY_PROPERTY = "log4j.mergeStrategy";
/*  53 */   private static final String[] VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<? extends AbstractConfiguration> configurations;
/*     */ 
/*     */   
/*     */   private MergeStrategy mergeStrategy;
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeConfiguration(List<? extends AbstractConfiguration> configurations) {
/*  65 */     super(((AbstractConfiguration)configurations.get(0)).getLoggerContext(), ConfigurationSource.NULL_SOURCE);
/*  66 */     this.rootNode = ((AbstractConfiguration)configurations.get(0)).getRootNode();
/*  67 */     this.configurations = configurations;
/*  68 */     String mergeStrategyClassName = PropertiesUtil.getProperties().getStringProperty("log4j.mergeStrategy", DefaultMergeStrategy.class.getName());
/*     */     
/*     */     try {
/*  71 */       this.mergeStrategy = (MergeStrategy)LoaderUtil.newInstanceOf(mergeStrategyClassName);
/*  72 */     } catch (ClassNotFoundException|IllegalAccessException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|InstantiationException ex) {
/*     */       
/*  74 */       this.mergeStrategy = new DefaultMergeStrategy();
/*     */     } 
/*  76 */     for (AbstractConfiguration config : configurations) {
/*  77 */       this.mergeStrategy.mergeRootProperties(this.rootNode, config);
/*     */     }
/*  79 */     StatusConfiguration statusConfig = (new StatusConfiguration()).withVerboseClasses(VERBOSE_CLASSES).withStatus(getDefaultStatus());
/*     */     
/*  81 */     for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)this.rootNode.getAttributes().entrySet()) {
/*  82 */       String key = entry.getKey();
/*  83 */       String value = getStrSubstitutor().replace(entry.getValue());
/*  84 */       if ("status".equalsIgnoreCase(key)) {
/*  85 */         statusConfig.withStatus(value.toUpperCase()); continue;
/*  86 */       }  if ("dest".equalsIgnoreCase(key)) {
/*  87 */         statusConfig.withDestination(value); continue;
/*  88 */       }  if ("shutdownHook".equalsIgnoreCase(key)) {
/*  89 */         this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(value); continue;
/*  90 */       }  if ("shutdownTimeout".equalsIgnoreCase(key)) {
/*  91 */         this.shutdownTimeoutMillis = Long.parseLong(value); continue;
/*  92 */       }  if ("verbose".equalsIgnoreCase(key)) {
/*  93 */         statusConfig.withVerbosity(value); continue;
/*  94 */       }  if ("packages".equalsIgnoreCase(key)) {
/*  95 */         this.pluginPackages.addAll(Arrays.asList(value.split(Patterns.COMMA_SEPARATOR))); continue;
/*  96 */       }  if ("name".equalsIgnoreCase(key)) {
/*  97 */         setName(value);
/*     */       }
/*     */     } 
/* 100 */     statusConfig.initialize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setup() {
/* 105 */     AbstractConfiguration targetConfiguration = this.configurations.get(0);
/* 106 */     staffChildConfiguration(targetConfiguration);
/* 107 */     WatchManager watchManager = getWatchManager();
/* 108 */     WatchManager targetWatchManager = targetConfiguration.getWatchManager();
/* 109 */     ConfiguratonFileWatcher configuratonFileWatcher = new ConfiguratonFileWatcher(this, this.listeners);
/* 110 */     if (targetWatchManager.getIntervalSeconds() > 0) {
/* 111 */       watchManager.setIntervalSeconds(targetWatchManager.getIntervalSeconds());
/* 112 */       Map<File, FileWatcher> watchers = targetWatchManager.getWatchers();
/* 113 */       for (Map.Entry<File, FileWatcher> entry : watchers.entrySet()) {
/* 114 */         if (entry.getValue() instanceof ConfiguratonFileWatcher) {
/* 115 */           watchManager.watchFile(entry.getKey(), (FileWatcher)configuratonFileWatcher);
/*     */         }
/*     */       } 
/*     */     } 
/* 119 */     for (AbstractConfiguration sourceConfiguration : this.configurations.subList(1, this.configurations.size())) {
/* 120 */       staffChildConfiguration(sourceConfiguration);
/* 121 */       Node sourceRoot = sourceConfiguration.getRootNode();
/* 122 */       this.mergeStrategy.mergConfigurations(this.rootNode, sourceRoot, getPluginManager());
/* 123 */       if (LOGGER.isEnabled(Level.ALL)) {
/* 124 */         StringBuilder sb = new StringBuilder();
/* 125 */         printNodes("", this.rootNode, sb);
/* 126 */         System.out.println(sb.toString());
/*     */       } 
/* 128 */       int monitorInterval = sourceConfiguration.getWatchManager().getIntervalSeconds();
/* 129 */       if (monitorInterval > 0) {
/* 130 */         int currentInterval = watchManager.getIntervalSeconds();
/* 131 */         if (currentInterval <= 0 || monitorInterval < currentInterval) {
/* 132 */           watchManager.setIntervalSeconds(monitorInterval);
/*     */         }
/* 134 */         WatchManager sourceWatchManager = sourceConfiguration.getWatchManager();
/* 135 */         Map<File, FileWatcher> watchers = sourceWatchManager.getWatchers();
/* 136 */         for (Map.Entry<File, FileWatcher> entry : watchers.entrySet()) {
/* 137 */           if (entry.getValue() instanceof ConfiguratonFileWatcher) {
/* 138 */             watchManager.watchFile(entry.getKey(), (FileWatcher)configuratonFileWatcher);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Configuration reconfigure() {
/* 147 */     LOGGER.debug("Reconfiguring composite configuration");
/* 148 */     List<AbstractConfiguration> configs = new ArrayList<>();
/* 149 */     ConfigurationFactory factory = ConfigurationFactory.getInstance();
/* 150 */     for (AbstractConfiguration config : this.configurations) {
/* 151 */       AbstractConfiguration abstractConfiguration1; ConfigurationSource source = config.getConfigurationSource();
/* 152 */       URI sourceURI = source.getURI();
/*     */       
/* 154 */       if (sourceURI != null) {
/* 155 */         LOGGER.warn("Unable to determine URI for configuration {}, changes to it will be ignored", config.getName());
/*     */         
/* 157 */         Configuration currentConfig = factory.getConfiguration(getLoggerContext(), config.getName(), sourceURI);
/* 158 */         if (currentConfig == null) {
/* 159 */           LOGGER.warn("Unable to reload configuration {}, changes to it will be ignored", config.getName());
/* 160 */           abstractConfiguration1 = config;
/*     */         } 
/*     */       } else {
/* 163 */         abstractConfiguration1 = config;
/*     */       } 
/* 165 */       configs.add(abstractConfiguration1);
/*     */     } 
/*     */ 
/*     */     
/* 169 */     return (Configuration)new CompositeConfiguration(configs);
/*     */   }
/*     */   
/*     */   private void staffChildConfiguration(AbstractConfiguration childConfiguration) {
/* 173 */     childConfiguration.setPluginManager(this.pluginManager);
/* 174 */     childConfiguration.setScriptManager(this.scriptManager);
/* 175 */     childConfiguration.setup();
/*     */   }
/*     */   
/*     */   private void printNodes(String indent, Node node, StringBuilder sb) {
/* 179 */     sb.append(indent).append(node.getName()).append(" type: ").append(node.getType()).append("\n");
/* 180 */     sb.append(indent).append(node.getAttributes().toString()).append("\n");
/* 181 */     for (Node child : node.getChildren())
/* 182 */       printNodes(indent + "  ", child, sb); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\composite\CompositeConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */