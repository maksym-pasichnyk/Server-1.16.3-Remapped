/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PropertiesUtil
/*     */ {
/*  38 */   private static final PropertiesUtil LOG4J_PROPERTIES = new PropertiesUtil("log4j2.component.properties");
/*     */ 
/*     */ 
/*     */   
/*     */   private final Properties props;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertiesUtil(Properties props) {
/*  48 */     this.props = props;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertiesUtil(String propertiesFileName) {
/*  58 */     Properties properties = new Properties();
/*  59 */     for (URL url : LoaderUtil.findResources(propertiesFileName)) {
/*  60 */       try (InputStream in = url.openStream()) {
/*  61 */         properties.load(in);
/*  62 */       } catch (IOException ioe) {
/*  63 */         LowLevelLogUtil.logException("Unable to read " + url.toString(), ioe);
/*     */       } 
/*     */     } 
/*  66 */     this.props = properties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Properties loadClose(InputStream in, Object source) {
/*  77 */     Properties props = new Properties();
/*  78 */     if (null != in) {
/*     */       try {
/*  80 */         props.load(in);
/*  81 */       } catch (IOException e) {
/*  82 */         LowLevelLogUtil.logException("Unable to read " + source, e);
/*     */       } finally {
/*     */         try {
/*  85 */           in.close();
/*  86 */         } catch (IOException e) {
/*  87 */           LowLevelLogUtil.logException("Unable to close " + source, e);
/*     */         } 
/*     */       } 
/*     */     }
/*  91 */     return props;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PropertiesUtil getProperties() {
/* 100 */     return LOG4J_PROPERTIES;
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
/*     */   public boolean getBooleanProperty(String name) {
/* 112 */     return getBooleanProperty(name, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getBooleanProperty(String name, boolean defaultValue) {
/* 123 */     String prop = getStringProperty(name);
/* 124 */     return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharsetProperty(String name) {
/* 134 */     return getCharsetProperty(name, Charset.defaultCharset());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharsetProperty(String name, Charset defaultValue) {
/* 145 */     String prop = getStringProperty(name);
/* 146 */     return (prop == null) ? defaultValue : Charset.forName(prop);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDoubleProperty(String name, double defaultValue) {
/* 157 */     String prop = getStringProperty(name);
/* 158 */     if (prop != null) {
/*     */       try {
/* 160 */         return Double.parseDouble(prop);
/* 161 */       } catch (Exception ignored) {
/* 162 */         return defaultValue;
/*     */       } 
/*     */     }
/* 165 */     return defaultValue;
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
/*     */   public int getIntegerProperty(String name, int defaultValue) {
/* 177 */     String prop = getStringProperty(name);
/* 178 */     if (prop != null) {
/*     */       try {
/* 180 */         return Integer.parseInt(prop);
/* 181 */       } catch (Exception ignored) {
/* 182 */         return defaultValue;
/*     */       } 
/*     */     }
/* 185 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLongProperty(String name, long defaultValue) {
/* 196 */     String prop = getStringProperty(name);
/* 197 */     if (prop != null) {
/*     */       try {
/* 199 */         return Long.parseLong(prop);
/* 200 */       } catch (Exception ignored) {
/* 201 */         return defaultValue;
/*     */       } 
/*     */     }
/* 204 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStringProperty(String name) {
/* 214 */     String prop = null;
/*     */     try {
/* 216 */       prop = System.getProperty(name);
/* 217 */     } catch (SecurityException securityException) {}
/*     */ 
/*     */     
/* 220 */     return (prop == null) ? this.props.getProperty(name) : prop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStringProperty(String name, String defaultValue) {
/* 231 */     String prop = getStringProperty(name);
/* 232 */     return (prop == null) ? defaultValue : prop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Properties getSystemProperties() {
/*     */     try {
/* 242 */       return new Properties(System.getProperties());
/* 243 */     } catch (SecurityException ex) {
/* 244 */       LowLevelLogUtil.logException("Unable to access system properties.", ex);
/*     */       
/* 246 */       return new Properties();
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
/*     */   public static Properties extractSubset(Properties properties, String prefix) {
/* 259 */     Properties subset = new Properties();
/*     */     
/* 261 */     if (prefix == null || prefix.length() == 0) {
/* 262 */       return subset;
/*     */     }
/*     */     
/* 265 */     String prefixToMatch = (prefix.charAt(prefix.length() - 1) != '.') ? (prefix + '.') : prefix;
/*     */     
/* 267 */     List<String> keys = new ArrayList<>();
/*     */     
/* 269 */     for (String key : properties.stringPropertyNames()) {
/* 270 */       if (key.startsWith(prefixToMatch)) {
/* 271 */         subset.setProperty(key.substring(prefixToMatch.length()), properties.getProperty(key));
/* 272 */         keys.add(key);
/*     */       } 
/*     */     } 
/* 275 */     for (String key : keys) {
/* 276 */       properties.remove(key);
/*     */     }
/*     */     
/* 279 */     return subset;
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
/*     */   public static Map<String, Properties> partitionOnCommonPrefixes(Properties properties) {
/* 291 */     Map<String, Properties> parts = new ConcurrentHashMap<>();
/* 292 */     for (String key : properties.stringPropertyNames()) {
/* 293 */       String prefix = key.substring(0, key.indexOf('.'));
/* 294 */       if (!parts.containsKey(prefix)) {
/* 295 */         parts.put(prefix, new Properties());
/*     */       }
/* 297 */       ((Properties)parts.get(prefix)).setProperty(key.substring(key.indexOf('.') + 1), properties.getProperty(key));
/*     */     } 
/* 299 */     return parts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOsWindows() {
/* 307 */     return getStringProperty("os.name").startsWith("Windows");
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4\\util\PropertiesUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */