/*     */ package org.apache.logging.log4j.core.lookup;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationAware;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
/*     */ import org.apache.logging.log4j.core.config.plugins.util.PluginType;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.core.util.ReflectionUtil;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Interpolator
/*     */   extends AbstractConfigurationAwareLookup
/*     */ {
/*     */   private static final String LOOKUP_KEY_WEB = "web";
/*     */   private static final String LOOKUP_KEY_JNDI = "jndi";
/*     */   private static final String LOOKUP_KEY_JVMRUNARGS = "jvmrunargs";
/*  44 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private static final char PREFIX_SEPARATOR = ':';
/*     */   
/*  49 */   private final Map<String, StrLookup> lookups = new HashMap<>();
/*     */   
/*     */   private final StrLookup defaultLookup;
/*     */   
/*     */   public Interpolator(StrLookup defaultLookup) {
/*  54 */     this(defaultLookup, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Interpolator(StrLookup defaultLookup, List<String> pluginPackages) {
/*  65 */     this.defaultLookup = (defaultLookup == null) ? new MapLookup(new HashMap<>()) : defaultLookup;
/*  66 */     PluginManager manager = new PluginManager("Lookup");
/*  67 */     manager.collectPlugins(pluginPackages);
/*  68 */     Map<String, PluginType<?>> plugins = manager.getPlugins();
/*     */     
/*  70 */     for (Map.Entry<String, PluginType<?>> entry : plugins.entrySet()) {
/*     */       try {
/*  72 */         Class<? extends StrLookup> clazz = ((PluginType)entry.getValue()).getPluginClass().asSubclass(StrLookup.class);
/*  73 */         this.lookups.put(entry.getKey(), ReflectionUtil.instantiate(clazz));
/*  74 */       } catch (Throwable t) {
/*  75 */         handleError(entry.getKey(), t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Interpolator() {
/*  84 */     this((Map<String, String>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Interpolator(Map<String, String> properties) {
/*  91 */     this.defaultLookup = new MapLookup((properties == null) ? new HashMap<>() : properties);
/*     */     
/*  93 */     this.lookups.put("log4j", new Log4jLookup());
/*  94 */     this.lookups.put("sys", new SystemPropertiesLookup());
/*  95 */     this.lookups.put("env", new EnvironmentLookup());
/*  96 */     this.lookups.put("main", MainMapLookup.MAIN_SINGLETON);
/*  97 */     this.lookups.put("marker", new MarkerLookup());
/*  98 */     this.lookups.put("java", new JavaLookup());
/*     */ 
/*     */     
/*     */     try {
/* 102 */       this.lookups.put("jndi", Loader.newCheckedInstanceOf("org.apache.logging.log4j.core.lookup.JndiLookup", StrLookup.class));
/*     */     }
/* 104 */     catch (LinkageError|Exception e) {
/* 105 */       handleError("jndi", e);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 110 */       this.lookups.put("jvmrunargs", Loader.newCheckedInstanceOf("org.apache.logging.log4j.core.lookup.JmxRuntimeInputArgumentsLookup", StrLookup.class));
/*     */     
/*     */     }
/* 113 */     catch (LinkageError|Exception e) {
/* 114 */       handleError("jvmrunargs", e);
/*     */     } 
/* 116 */     this.lookups.put("date", new DateLookup());
/* 117 */     this.lookups.put("ctx", new ContextMapLookup());
/* 118 */     if (Loader.isClassAvailable("javax.servlet.ServletContext")) {
/*     */       try {
/* 120 */         this.lookups.put("web", Loader.newCheckedInstanceOf("org.apache.logging.log4j.web.WebLookup", StrLookup.class));
/*     */       }
/* 122 */       catch (Exception ignored) {
/* 123 */         handleError("web", ignored);
/*     */       } 
/*     */     } else {
/* 126 */       LOGGER.debug("Not in a ServletContext environment, thus not loading WebLookup plugin.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleError(String lookupKey, Throwable t) {
/* 131 */     switch (lookupKey) {
/*     */       
/*     */       case "jndi":
/* 134 */         LOGGER.warn("JNDI lookup class is not available because this JRE does not support JNDI. JNDI string lookups will not be available, continuing configuration. Ignoring " + t);
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case "jvmrunargs":
/* 140 */         LOGGER.warn("JMX runtime input lookup class is not available because this JRE does not support JMX. JMX lookups will not be available, continuing configuration. Ignoring " + t);
/*     */         return;
/*     */ 
/*     */       
/*     */       case "web":
/* 145 */         LOGGER.info("Log4j appears to be running in a Servlet environment, but there's no log4j-web module available. If you want better web container support, please add the log4j-web JAR to your web archive or server lib directory.");
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 150 */     LOGGER.error("Unable to create Lookup for {}", lookupKey, t);
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
/*     */   public String lookup(LogEvent event, String var) {
/* 169 */     if (var == null) {
/* 170 */       return null;
/*     */     }
/*     */     
/* 173 */     int prefixPos = var.indexOf(':');
/* 174 */     if (prefixPos >= 0) {
/* 175 */       String prefix = var.substring(0, prefixPos).toLowerCase(Locale.US);
/* 176 */       String name = var.substring(prefixPos + 1);
/* 177 */       StrLookup lookup = this.lookups.get(prefix);
/* 178 */       if (lookup instanceof ConfigurationAware) {
/* 179 */         ((ConfigurationAware)lookup).setConfiguration(this.configuration);
/*     */       }
/* 181 */       String value = null;
/* 182 */       if (lookup != null) {
/* 183 */         value = (event == null) ? lookup.lookup(name) : lookup.lookup(event, name);
/*     */       }
/*     */       
/* 186 */       if (value != null) {
/* 187 */         return value;
/*     */       }
/* 189 */       var = var.substring(prefixPos + 1);
/*     */     } 
/* 191 */     if (this.defaultLookup != null) {
/* 192 */       return (event == null) ? this.defaultLookup.lookup(var) : this.defaultLookup.lookup(event, var);
/*     */     }
/* 194 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 199 */     StringBuilder sb = new StringBuilder();
/* 200 */     for (String name : this.lookups.keySet()) {
/* 201 */       if (sb.length() == 0) {
/* 202 */         sb.append('{');
/*     */       } else {
/* 204 */         sb.append(", ");
/*     */       } 
/*     */       
/* 207 */       sb.append(name);
/*     */     } 
/* 209 */     if (sb.length() > 0) {
/* 210 */       sb.append('}');
/*     */     }
/* 212 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\lookup\Interpolator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */