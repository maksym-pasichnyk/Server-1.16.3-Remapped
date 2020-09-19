/*     */ package org.apache.logging.log4j.core.config.plugins.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAliases;
/*     */ import org.apache.logging.log4j.core.config.plugins.processor.PluginCache;
/*     */ import org.apache.logging.log4j.core.config.plugins.processor.PluginEntry;
/*     */ import org.apache.logging.log4j.core.util.Loader;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.apache.logging.log4j.util.Strings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PluginRegistry
/*     */ {
/*  49 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private static volatile PluginRegistry INSTANCE;
/*  52 */   private static final Object INSTANCE_LOCK = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private final AtomicReference<Map<String, List<PluginType<?>>>> pluginsByCategoryRef = new AtomicReference<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private final ConcurrentMap<Long, Map<String, List<PluginType<?>>>> pluginsByCategoryByBundleId = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   private final ConcurrentMap<String, Map<String, List<PluginType<?>>>> pluginsByCategoryByPackage = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PluginRegistry getInstance() {
/*  82 */     PluginRegistry result = INSTANCE;
/*  83 */     if (result == null) {
/*  84 */       synchronized (INSTANCE_LOCK) {
/*  85 */         result = INSTANCE;
/*  86 */         if (result == null) {
/*  87 */           INSTANCE = result = new PluginRegistry();
/*     */         }
/*     */       } 
/*     */     }
/*  91 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  98 */     this.pluginsByCategoryRef.set(null);
/*  99 */     this.pluginsByCategoryByPackage.clear();
/* 100 */     this.pluginsByCategoryByBundleId.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Long, Map<String, List<PluginType<?>>>> getPluginsByCategoryByBundleId() {
/* 107 */     return this.pluginsByCategoryByBundleId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, List<PluginType<?>>> loadFromMainClassLoader() {
/* 114 */     Map<String, List<PluginType<?>>> existing = this.pluginsByCategoryRef.get();
/* 115 */     if (existing != null)
/*     */     {
/* 117 */       return existing;
/*     */     }
/* 119 */     Map<String, List<PluginType<?>>> newPluginsByCategory = decodeCacheFiles(Loader.getClassLoader());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     if (this.pluginsByCategoryRef.compareAndSet(null, newPluginsByCategory)) {
/* 125 */       return newPluginsByCategory;
/*     */     }
/* 127 */     return this.pluginsByCategoryRef.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearBundlePlugins(long bundleId) {
/* 134 */     this.pluginsByCategoryByBundleId.remove(Long.valueOf(bundleId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, List<PluginType<?>>> loadFromBundle(long bundleId, ClassLoader loader) {
/* 141 */     Map<String, List<PluginType<?>>> existing = this.pluginsByCategoryByBundleId.get(Long.valueOf(bundleId));
/* 142 */     if (existing != null)
/*     */     {
/* 144 */       return existing;
/*     */     }
/* 146 */     Map<String, List<PluginType<?>>> newPluginsByCategory = decodeCacheFiles(loader);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     existing = this.pluginsByCategoryByBundleId.putIfAbsent(Long.valueOf(bundleId), newPluginsByCategory);
/* 152 */     if (existing != null) {
/* 153 */       return existing;
/*     */     }
/* 155 */     return newPluginsByCategory;
/*     */   }
/*     */   
/*     */   private Map<String, List<PluginType<?>>> decodeCacheFiles(ClassLoader loader) {
/* 159 */     long startTime = System.nanoTime();
/* 160 */     PluginCache cache = new PluginCache();
/*     */     try {
/* 162 */       Enumeration<URL> resources = loader.getResources("META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat");
/* 163 */       if (resources == null) {
/* 164 */         LOGGER.info("Plugin preloads not available from class loader {}", loader);
/*     */       } else {
/* 166 */         cache.loadCacheFiles(resources);
/*     */       } 
/* 168 */     } catch (IOException ioe) {
/* 169 */       LOGGER.warn("Unable to preload plugins", ioe);
/*     */     } 
/* 171 */     Map<String, List<PluginType<?>>> newPluginsByCategory = new HashMap<>();
/* 172 */     int pluginCount = 0;
/* 173 */     for (Map.Entry<String, Map<String, PluginEntry>> outer : (Iterable<Map.Entry<String, Map<String, PluginEntry>>>)cache.getAllCategories().entrySet()) {
/* 174 */       String categoryLowerCase = outer.getKey();
/* 175 */       List<PluginType<?>> types = new ArrayList<>(((Map)outer.getValue()).size());
/* 176 */       newPluginsByCategory.put(categoryLowerCase, types);
/* 177 */       for (Map.Entry<String, PluginEntry> inner : (Iterable<Map.Entry<String, PluginEntry>>)((Map)outer.getValue()).entrySet()) {
/* 178 */         PluginEntry entry = inner.getValue();
/* 179 */         String className = entry.getClassName();
/*     */         try {
/* 181 */           Class<?> clazz = loader.loadClass(className);
/* 182 */           PluginType<?> type = new PluginType(entry, clazz, entry.getName());
/* 183 */           types.add(type);
/* 184 */           pluginCount++;
/* 185 */         } catch (ClassNotFoundException e) {
/* 186 */           LOGGER.info("Plugin [{}] could not be loaded due to missing classes.", className, e);
/* 187 */         } catch (VerifyError e) {
/* 188 */           LOGGER.info("Plugin [{}] could not be loaded due to verification error.", className, e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 193 */     long endTime = System.nanoTime();
/* 194 */     DecimalFormat numFormat = new DecimalFormat("#0.000000");
/* 195 */     double seconds = (endTime - startTime) * 1.0E-9D;
/* 196 */     LOGGER.debug("Took {} seconds to load {} plugins from {}", numFormat.format(seconds), Integer.valueOf(pluginCount), loader);
/*     */     
/* 198 */     return newPluginsByCategory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, List<PluginType<?>>> loadFromPackage(String pkg) {
/* 205 */     if (Strings.isBlank(pkg))
/*     */     {
/* 207 */       return Collections.emptyMap();
/*     */     }
/* 209 */     Map<String, List<PluginType<?>>> existing = this.pluginsByCategoryByPackage.get(pkg);
/* 210 */     if (existing != null)
/*     */     {
/* 212 */       return existing;
/*     */     }
/*     */     
/* 215 */     long startTime = System.nanoTime();
/* 216 */     ResolverUtil resolver = new ResolverUtil();
/* 217 */     ClassLoader classLoader = Loader.getClassLoader();
/* 218 */     if (classLoader != null) {
/* 219 */       resolver.setClassLoader(classLoader);
/*     */     }
/* 221 */     resolver.findInPackage(new PluginTest(), pkg);
/*     */     
/* 223 */     Map<String, List<PluginType<?>>> newPluginsByCategory = new HashMap<>();
/* 224 */     for (Class<?> clazz : resolver.getClasses()) {
/* 225 */       Plugin plugin = clazz.<Plugin>getAnnotation(Plugin.class);
/* 226 */       String categoryLowerCase = plugin.category().toLowerCase();
/* 227 */       List<PluginType<?>> list = newPluginsByCategory.get(categoryLowerCase);
/* 228 */       if (list == null) {
/* 229 */         newPluginsByCategory.put(categoryLowerCase, list = new ArrayList<>());
/*     */       }
/* 231 */       PluginEntry mainEntry = new PluginEntry();
/* 232 */       String mainElementName = plugin.elementType().equals("") ? plugin.name() : plugin.elementType();
/*     */       
/* 234 */       mainEntry.setKey(plugin.name().toLowerCase());
/* 235 */       mainEntry.setName(plugin.name());
/* 236 */       mainEntry.setCategory(plugin.category());
/* 237 */       mainEntry.setClassName(clazz.getName());
/* 238 */       mainEntry.setPrintable(plugin.printObject());
/* 239 */       mainEntry.setDefer(plugin.deferChildren());
/* 240 */       PluginType<?> mainType = new PluginType(mainEntry, clazz, mainElementName);
/* 241 */       list.add(mainType);
/* 242 */       PluginAliases pluginAliases = clazz.<PluginAliases>getAnnotation(PluginAliases.class);
/* 243 */       if (pluginAliases != null) {
/* 244 */         for (String alias : pluginAliases.value()) {
/* 245 */           PluginEntry aliasEntry = new PluginEntry();
/* 246 */           String aliasElementName = plugin.elementType().equals("") ? alias.trim() : plugin.elementType();
/*     */           
/* 248 */           aliasEntry.setKey(alias.trim().toLowerCase());
/* 249 */           aliasEntry.setName(plugin.name());
/* 250 */           aliasEntry.setCategory(plugin.category());
/* 251 */           aliasEntry.setClassName(clazz.getName());
/* 252 */           aliasEntry.setPrintable(plugin.printObject());
/* 253 */           aliasEntry.setDefer(plugin.deferChildren());
/* 254 */           PluginType<?> aliasType = new PluginType(aliasEntry, clazz, aliasElementName);
/* 255 */           list.add(aliasType);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 260 */     long endTime = System.nanoTime();
/* 261 */     DecimalFormat numFormat = new DecimalFormat("#0.000000");
/* 262 */     double seconds = (endTime - startTime) * 1.0E-9D;
/* 263 */     LOGGER.debug("Took {} seconds to load {} plugins from package {}", numFormat.format(seconds), Integer.valueOf(resolver.getClasses().size()), pkg);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 269 */     existing = this.pluginsByCategoryByPackage.putIfAbsent(pkg, newPluginsByCategory);
/* 270 */     if (existing != null) {
/* 271 */       return existing;
/*     */     }
/* 273 */     return newPluginsByCategory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class PluginTest
/*     */     implements ResolverUtil.Test
/*     */   {
/*     */     public boolean matches(Class<?> type) {
/* 285 */       return (type != null && type.isAnnotationPresent((Class)Plugin.class));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 290 */       return "annotated with @" + Plugin.class.getSimpleName();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(URI resource) {
/* 295 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean doesMatchClass() {
/* 300 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean doesMatchResource() {
/* 305 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\plugin\\util\PluginRegistry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */