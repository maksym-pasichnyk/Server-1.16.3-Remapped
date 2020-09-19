/*     */ package org.apache.logging.log4j.core.jmx;
/*     */ 
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import javax.management.InstanceAlreadyExistsException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanRegistrationException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.core.Appender;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.appender.AsyncAppender;
/*     */ import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
/*     */ import org.apache.logging.log4j.core.async.AsyncLoggerContext;
/*     */ import org.apache.logging.log4j.core.config.LoggerConfig;
/*     */ import org.apache.logging.log4j.core.impl.Log4jContextFactory;
/*     */ import org.apache.logging.log4j.core.selector.ContextSelector;
/*     */ import org.apache.logging.log4j.core.util.Constants;
/*     */ import org.apache.logging.log4j.core.util.Log4jThreadFactory;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Server
/*     */ {
/*     */   public static final String DOMAIN = "org.apache.logging.log4j2";
/*     */   private static final String PROPERTY_DISABLE_JMX = "log4j2.disable.jmx";
/*     */   private static final String PROPERTY_ASYNC_NOTIF = "log4j2.jmx.notify.async";
/*     */   private static final String THREAD_NAME_PREFIX = "jmx.notif";
/*  64 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*  65 */   static final Executor executor = isJmxDisabled() ? null : createExecutor();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ExecutorService createExecutor() {
/*  78 */     boolean defaultAsync = !Constants.IS_WEB_APP;
/*  79 */     boolean async = PropertiesUtil.getProperties().getBooleanProperty("log4j2.jmx.notify.async", defaultAsync);
/*  80 */     return async ? Executors.newFixedThreadPool(1, (ThreadFactory)Log4jThreadFactory.createDaemonThreadFactory("jmx.notif")) : null;
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
/*     */   public static String escape(String name) {
/*  92 */     StringBuilder sb = new StringBuilder(name.length() * 2);
/*  93 */     boolean needsQuotes = false;
/*  94 */     for (int i = 0; i < name.length(); i++) {
/*  95 */       char c = name.charAt(i);
/*  96 */       switch (c) {
/*     */         
/*     */         case '"':
/*     */         case '*':
/*     */         case '?':
/*     */         case '\\':
/* 102 */           sb.append('\\');
/* 103 */           needsQuotes = true;
/*     */ 
/*     */         
/*     */         case ',':
/*     */         case ':':
/*     */         case '=':
/* 109 */           needsQuotes = true;
/*     */ 
/*     */         
/*     */         case '\r':
/*     */           break;
/*     */         
/*     */         case '\n':
/* 116 */           sb.append("\\n");
/* 117 */           needsQuotes = true;
/*     */           break;
/*     */         default:
/* 120 */           sb.append(c); break;
/*     */       } 
/* 122 */     }  if (needsQuotes) {
/* 123 */       sb.insert(0, '"');
/* 124 */       sb.append('"');
/*     */     } 
/* 126 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static boolean isJmxDisabled() {
/* 130 */     return PropertiesUtil.getProperties().getBooleanProperty("log4j2.disable.jmx");
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reregisterMBeansAfterReconfigure() {
/* 135 */     if (isJmxDisabled()) {
/* 136 */       LOGGER.debug("JMX disabled for Log4j2. Not registering MBeans.");
/*     */       return;
/*     */     } 
/* 139 */     MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
/* 140 */     reregisterMBeansAfterReconfigure(mbs);
/*     */   }
/*     */   
/*     */   public static void reregisterMBeansAfterReconfigure(MBeanServer mbs) {
/* 144 */     if (isJmxDisabled()) {
/* 145 */       LOGGER.debug("JMX disabled for Log4j2. Not registering MBeans.");
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/* 152 */       ContextSelector selector = getContextSelector();
/* 153 */       if (selector == null) {
/* 154 */         LOGGER.debug("Could not register MBeans: no ContextSelector found.");
/*     */         return;
/*     */       } 
/* 157 */       LOGGER.trace("Reregistering MBeans after reconfigure. Selector={}", selector);
/* 158 */       List<LoggerContext> contexts = selector.getLoggerContexts();
/* 159 */       int i = 0;
/* 160 */       for (LoggerContext ctx : contexts) {
/* 161 */         LOGGER.trace("Reregistering context ({}/{}): '{}' {}", Integer.valueOf(++i), Integer.valueOf(contexts.size()), ctx.getName(), ctx);
/*     */ 
/*     */         
/* 164 */         unregisterLoggerContext(ctx.getName(), mbs);
/*     */         
/* 166 */         LoggerContextAdmin mbean = new LoggerContextAdmin(ctx, executor);
/* 167 */         register(mbs, mbean, mbean.getObjectName());
/*     */         
/* 169 */         if (ctx instanceof AsyncLoggerContext) {
/* 170 */           RingBufferAdmin rbmbean = ((AsyncLoggerContext)ctx).createRingBufferAdmin();
/* 171 */           if (rbmbean.getBufferSize() > 0L)
/*     */           {
/* 173 */             register(mbs, rbmbean, rbmbean.getObjectName());
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 182 */         registerStatusLogger(ctx.getName(), mbs, executor);
/* 183 */         registerContextSelector(ctx.getName(), selector, mbs, executor);
/*     */         
/* 185 */         registerLoggerConfigs(ctx, mbs, executor);
/* 186 */         registerAppenders(ctx, mbs, executor);
/*     */       } 
/* 188 */     } catch (Exception ex) {
/* 189 */       LOGGER.error("Could not register mbeans", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unregisterMBeans() {
/* 197 */     if (isJmxDisabled()) {
/* 198 */       LOGGER.debug("JMX disabled for Log4j2. Not unregistering MBeans.");
/*     */       return;
/*     */     } 
/* 201 */     MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
/* 202 */     unregisterMBeans(mbs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unregisterMBeans(MBeanServer mbs) {
/* 211 */     unregisterStatusLogger("*", mbs);
/* 212 */     unregisterContextSelector("*", mbs);
/* 213 */     unregisterContexts(mbs);
/* 214 */     unregisterLoggerConfigs("*", mbs);
/* 215 */     unregisterAsyncLoggerRingBufferAdmins("*", mbs);
/* 216 */     unregisterAsyncLoggerConfigRingBufferAdmins("*", mbs);
/* 217 */     unregisterAppenders("*", mbs);
/* 218 */     unregisterAsyncAppenders("*", mbs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ContextSelector getContextSelector() {
/* 227 */     LoggerContextFactory factory = LogManager.getFactory();
/* 228 */     if (factory instanceof Log4jContextFactory) {
/* 229 */       ContextSelector selector = ((Log4jContextFactory)factory).getSelector();
/* 230 */       return selector;
/*     */     } 
/* 232 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unregisterLoggerContext(String loggerContextName) {
/* 242 */     if (isJmxDisabled()) {
/* 243 */       LOGGER.debug("JMX disabled for Log4j2. Not unregistering MBeans.");
/*     */       return;
/*     */     } 
/* 246 */     MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
/* 247 */     unregisterLoggerContext(loggerContextName, mbs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void unregisterLoggerContext(String contextName, MBeanServer mbs) {
/* 258 */     String search = String.format("org.apache.logging.log4j2:type=%s", new Object[] { escape(contextName), "*" });
/* 259 */     unregisterAllMatching(search, mbs);
/*     */ 
/*     */     
/* 262 */     unregisterStatusLogger(contextName, mbs);
/* 263 */     unregisterContextSelector(contextName, mbs);
/* 264 */     unregisterLoggerConfigs(contextName, mbs);
/* 265 */     unregisterAppenders(contextName, mbs);
/* 266 */     unregisterAsyncAppenders(contextName, mbs);
/* 267 */     unregisterAsyncLoggerRingBufferAdmins(contextName, mbs);
/* 268 */     unregisterAsyncLoggerConfigRingBufferAdmins(contextName, mbs);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerStatusLogger(String contextName, MBeanServer mbs, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
/* 274 */     StatusLoggerAdmin mbean = new StatusLoggerAdmin(contextName, executor);
/* 275 */     register(mbs, mbean, mbean.getObjectName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerContextSelector(String contextName, ContextSelector selector, MBeanServer mbs, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
/* 282 */     ContextSelectorAdmin mbean = new ContextSelectorAdmin(contextName, selector);
/* 283 */     register(mbs, mbean, mbean.getObjectName());
/*     */   }
/*     */   
/*     */   private static void unregisterStatusLogger(String contextName, MBeanServer mbs) {
/* 287 */     String search = String.format("org.apache.logging.log4j2:type=%s,component=StatusLogger", new Object[] { escape(contextName), "*" });
/* 288 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterContextSelector(String contextName, MBeanServer mbs) {
/* 292 */     String search = String.format("org.apache.logging.log4j2:type=%s,component=ContextSelector", new Object[] { escape(contextName), "*" });
/* 293 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterLoggerConfigs(String contextName, MBeanServer mbs) {
/* 297 */     String pattern = "org.apache.logging.log4j2:type=%s,component=Loggers,name=%s";
/* 298 */     String search = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s", new Object[] { escape(contextName), "*" });
/* 299 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterContexts(MBeanServer mbs) {
/* 303 */     String pattern = "org.apache.logging.log4j2:type=%s";
/* 304 */     String search = String.format("org.apache.logging.log4j2:type=%s", new Object[] { "*" });
/* 305 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterAppenders(String contextName, MBeanServer mbs) {
/* 309 */     String pattern = "org.apache.logging.log4j2:type=%s,component=Appenders,name=%s";
/* 310 */     String search = String.format("org.apache.logging.log4j2:type=%s,component=Appenders,name=%s", new Object[] { escape(contextName), "*" });
/* 311 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterAsyncAppenders(String contextName, MBeanServer mbs) {
/* 315 */     String pattern = "org.apache.logging.log4j2:type=%s,component=AsyncAppenders,name=%s";
/* 316 */     String search = String.format("org.apache.logging.log4j2:type=%s,component=AsyncAppenders,name=%s", new Object[] { escape(contextName), "*" });
/* 317 */     unregisterAllMatching(search, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterAsyncLoggerRingBufferAdmins(String contextName, MBeanServer mbs) {
/* 321 */     String pattern1 = "org.apache.logging.log4j2:type=%s,component=AsyncLoggerRingBuffer";
/* 322 */     String search1 = String.format("org.apache.logging.log4j2:type=%s,component=AsyncLoggerRingBuffer", new Object[] { escape(contextName) });
/* 323 */     unregisterAllMatching(search1, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterAsyncLoggerConfigRingBufferAdmins(String contextName, MBeanServer mbs) {
/* 327 */     String pattern2 = "org.apache.logging.log4j2:type=%s,component=Loggers,name=%s,subtype=RingBuffer";
/* 328 */     String search2 = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s,subtype=RingBuffer", new Object[] { escape(contextName), "*" });
/* 329 */     unregisterAllMatching(search2, mbs);
/*     */   }
/*     */   
/*     */   private static void unregisterAllMatching(String search, MBeanServer mbs) {
/*     */     try {
/* 334 */       ObjectName pattern = new ObjectName(search);
/* 335 */       Set<ObjectName> found = mbs.queryNames(pattern, null);
/* 336 */       if (found.isEmpty()) {
/* 337 */         LOGGER.trace("Unregistering but no MBeans found matching '{}'", search);
/*     */       } else {
/* 339 */         LOGGER.trace("Unregistering {} MBeans: {}", Integer.valueOf(found.size()), found);
/*     */       } 
/* 341 */       for (ObjectName objectName : found) {
/* 342 */         mbs.unregisterMBean(objectName);
/*     */       }
/* 344 */     } catch (InstanceNotFoundException ex) {
/* 345 */       LOGGER.debug("Could not unregister MBeans for " + search + ". Ignoring " + ex);
/* 346 */     } catch (Exception ex) {
/* 347 */       LOGGER.error("Could not unregister MBeans for " + search, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerLoggerConfigs(LoggerContext ctx, MBeanServer mbs, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
/* 354 */     Map<String, LoggerConfig> map = ctx.getConfiguration().getLoggers();
/* 355 */     for (String name : map.keySet()) {
/* 356 */       LoggerConfig cfg = map.get(name);
/* 357 */       LoggerConfigAdmin mbean = new LoggerConfigAdmin(ctx, cfg);
/* 358 */       register(mbs, mbean, mbean.getObjectName());
/*     */       
/* 360 */       if (cfg instanceof AsyncLoggerConfig) {
/* 361 */         AsyncLoggerConfig async = (AsyncLoggerConfig)cfg;
/* 362 */         RingBufferAdmin rbmbean = async.createRingBufferAdmin(ctx.getName());
/* 363 */         register(mbs, rbmbean, rbmbean.getObjectName());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void registerAppenders(LoggerContext ctx, MBeanServer mbs, Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
/* 371 */     Map<String, Appender> map = ctx.getConfiguration().getAppenders();
/* 372 */     for (String name : map.keySet()) {
/* 373 */       Appender appender = map.get(name);
/*     */       
/* 375 */       if (appender instanceof AsyncAppender) {
/* 376 */         AsyncAppender async = (AsyncAppender)appender;
/* 377 */         AsyncAppenderAdmin asyncAppenderAdmin = new AsyncAppenderAdmin(ctx.getName(), async);
/* 378 */         register(mbs, asyncAppenderAdmin, asyncAppenderAdmin.getObjectName()); continue;
/*     */       } 
/* 380 */       AppenderAdmin mbean = new AppenderAdmin(ctx.getName(), appender);
/* 381 */       register(mbs, mbean, mbean.getObjectName());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void register(MBeanServer mbs, Object mbean, ObjectName objectName) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
/* 388 */     LOGGER.debug("Registering MBean {}", objectName);
/* 389 */     mbs.registerMBean(mbean, objectName);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\jmx\Server.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */