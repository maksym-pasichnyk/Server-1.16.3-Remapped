/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.core.LifeCycle;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.AbstractConfiguration;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.composite.CompositeConfiguration;
/*     */ import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;
/*     */ import org.apache.logging.log4j.core.selector.ContextSelector;
/*     */ import org.apache.logging.log4j.core.util.Cancellable;
/*     */ import org.apache.logging.log4j.core.util.DefaultShutdownCallbackRegistry;
/*     */ import org.apache.logging.log4j.core.util.ShutdownCallbackRegistry;
/*     */ import org.apache.logging.log4j.spi.LoggerContext;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ public class Log4jContextFactory
/*     */   implements LoggerContextFactory, ShutdownCallbackRegistry
/*     */ {
/*  47 */   private static final StatusLogger LOGGER = StatusLogger.getLogger();
/*  48 */   private static final boolean SHUTDOWN_HOOK_ENABLED = PropertiesUtil.getProperties().getBooleanProperty("log4j.shutdownHookEnabled", true);
/*     */ 
/*     */   
/*     */   private final ContextSelector selector;
/*     */ 
/*     */   
/*     */   private final ShutdownCallbackRegistry shutdownCallbackRegistry;
/*     */ 
/*     */   
/*     */   public Log4jContextFactory() {
/*  58 */     this(createContextSelector(), createShutdownCallbackRegistry());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Log4jContextFactory(ContextSelector selector) {
/*  66 */     this(selector, createShutdownCallbackRegistry());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Log4jContextFactory(ShutdownCallbackRegistry shutdownCallbackRegistry) {
/*  77 */     this(createContextSelector(), shutdownCallbackRegistry);
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
/*     */   public Log4jContextFactory(ContextSelector selector, ShutdownCallbackRegistry shutdownCallbackRegistry) {
/*  89 */     this.selector = Objects.<ContextSelector>requireNonNull(selector, "No ContextSelector provided");
/*  90 */     this.shutdownCallbackRegistry = Objects.<ShutdownCallbackRegistry>requireNonNull(shutdownCallbackRegistry, "No ShutdownCallbackRegistry provided");
/*  91 */     LOGGER.debug("Using ShutdownCallbackRegistry {}", shutdownCallbackRegistry.getClass());
/*  92 */     initializeShutdownCallbackRegistry();
/*     */   }
/*     */   
/*     */   private static ContextSelector createContextSelector() {
/*     */     try {
/*  97 */       ContextSelector selector = (ContextSelector)LoaderUtil.newCheckedInstanceOfProperty("Log4jContextSelector", ContextSelector.class);
/*     */       
/*  99 */       if (selector != null) {
/* 100 */         return selector;
/*     */       }
/* 102 */     } catch (Exception e) {
/* 103 */       LOGGER.error("Unable to create custom ContextSelector. Falling back to default.", e);
/*     */     } 
/* 105 */     return (ContextSelector)new ClassLoaderContextSelector();
/*     */   }
/*     */   
/*     */   private static ShutdownCallbackRegistry createShutdownCallbackRegistry() {
/*     */     try {
/* 110 */       ShutdownCallbackRegistry registry = (ShutdownCallbackRegistry)LoaderUtil.newCheckedInstanceOfProperty("log4j.shutdownCallbackRegistry", ShutdownCallbackRegistry.class);
/*     */ 
/*     */       
/* 113 */       if (registry != null) {
/* 114 */         return registry;
/*     */       }
/* 116 */     } catch (Exception e) {
/* 117 */       LOGGER.error("Unable to create custom ShutdownCallbackRegistry. Falling back to default.", e);
/*     */     } 
/* 119 */     return (ShutdownCallbackRegistry)new DefaultShutdownCallbackRegistry();
/*     */   }
/*     */   
/*     */   private void initializeShutdownCallbackRegistry() {
/* 123 */     if (SHUTDOWN_HOOK_ENABLED && this.shutdownCallbackRegistry instanceof LifeCycle) {
/*     */       try {
/* 125 */         ((LifeCycle)this.shutdownCallbackRegistry).start();
/* 126 */       } catch (IllegalStateException e) {
/* 127 */         LOGGER.error("Cannot start ShutdownCallbackRegistry, already shutting down.");
/* 128 */         throw e;
/* 129 */       } catch (RuntimeException e) {
/* 130 */         LOGGER.error("There was an error starting the ShutdownCallbackRegistry.", e);
/*     */       } 
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
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext) {
/* 147 */     LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext);
/* 148 */     if (externalContext != null && ctx.getExternalContext() == null) {
/* 149 */       ctx.setExternalContext(externalContext);
/*     */     }
/* 151 */     if (ctx.getState() == LifeCycle.State.INITIALIZED) {
/* 152 */       ctx.start();
/*     */     }
/* 154 */     return ctx;
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
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, ConfigurationSource source) {
/* 169 */     LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, null);
/* 170 */     if (externalContext != null && ctx.getExternalContext() == null) {
/* 171 */       ctx.setExternalContext(externalContext);
/*     */     }
/* 173 */     if (ctx.getState() == LifeCycle.State.INITIALIZED) {
/* 174 */       if (source != null) {
/* 175 */         ContextAnchor.THREAD_CONTEXT.set(ctx);
/* 176 */         Configuration config = ConfigurationFactory.getInstance().getConfiguration(ctx, source);
/* 177 */         LOGGER.debug("Starting LoggerContext[name={}] from configuration {}", ctx.getName(), source);
/* 178 */         ctx.start(config);
/* 179 */         ContextAnchor.THREAD_CONTEXT.remove();
/*     */       } else {
/* 181 */         ctx.start();
/*     */       } 
/*     */     }
/* 184 */     return ctx;
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
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, Configuration configuration) {
/* 199 */     LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, null);
/* 200 */     if (externalContext != null && ctx.getExternalContext() == null) {
/* 201 */       ctx.setExternalContext(externalContext);
/*     */     }
/* 203 */     if (ctx.getState() == LifeCycle.State.INITIALIZED) {
/* 204 */       ContextAnchor.THREAD_CONTEXT.set(ctx);
/*     */       try {
/* 206 */         ctx.start(configuration);
/*     */       } finally {
/* 208 */         ContextAnchor.THREAD_CONTEXT.remove();
/*     */       } 
/*     */     } 
/* 211 */     return ctx;
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
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, URI configLocation, String name) {
/* 227 */     LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, configLocation);
/* 228 */     if (externalContext != null && ctx.getExternalContext() == null) {
/* 229 */       ctx.setExternalContext(externalContext);
/*     */     }
/* 231 */     if (name != null) {
/* 232 */       ctx.setName(name);
/*     */     }
/* 234 */     if (ctx.getState() == LifeCycle.State.INITIALIZED) {
/* 235 */       if (configLocation != null || name != null) {
/* 236 */         ContextAnchor.THREAD_CONTEXT.set(ctx);
/* 237 */         Configuration config = ConfigurationFactory.getInstance().getConfiguration(ctx, name, configLocation);
/* 238 */         LOGGER.debug("Starting LoggerContext[name={}] from configuration at {}", ctx.getName(), configLocation);
/* 239 */         ctx.start(config);
/* 240 */         ContextAnchor.THREAD_CONTEXT.remove();
/*     */       } else {
/* 242 */         ctx.start();
/*     */       } 
/*     */     }
/* 245 */     return ctx;
/*     */   }
/*     */ 
/*     */   
/*     */   public LoggerContext getContext(String fqcn, ClassLoader loader, Object externalContext, boolean currentContext, List<URI> configLocations, String name) {
/* 250 */     LoggerContext ctx = this.selector.getContext(fqcn, loader, currentContext, null);
/*     */     
/* 252 */     if (externalContext != null && ctx.getExternalContext() == null) {
/* 253 */       ctx.setExternalContext(externalContext);
/*     */     }
/* 255 */     if (name != null) {
/* 256 */       ctx.setName(name);
/*     */     }
/* 258 */     if (ctx.getState() == LifeCycle.State.INITIALIZED) {
/* 259 */       if (configLocations != null && !configLocations.isEmpty()) {
/* 260 */         ContextAnchor.THREAD_CONTEXT.set(ctx);
/* 261 */         List<AbstractConfiguration> configurations = new ArrayList<>(configLocations.size());
/* 262 */         for (URI configLocation : configLocations) {
/* 263 */           Configuration currentReadConfiguration = ConfigurationFactory.getInstance().getConfiguration(ctx, name, configLocation);
/*     */           
/* 265 */           if (currentReadConfiguration instanceof AbstractConfiguration) {
/* 266 */             configurations.add((AbstractConfiguration)currentReadConfiguration); continue;
/*     */           } 
/* 268 */           LOGGER.error("Found configuration {}, which is not an AbstractConfiguration and can't be handled by CompositeConfiguration", configLocation);
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 273 */         CompositeConfiguration compositeConfiguration = new CompositeConfiguration(configurations);
/* 274 */         LOGGER.debug("Starting LoggerContext[name={}] from configurations at {}", ctx.getName(), configLocations);
/*     */         
/* 276 */         ctx.start((Configuration)compositeConfiguration);
/* 277 */         ContextAnchor.THREAD_CONTEXT.remove();
/*     */       } else {
/* 279 */         ctx.start();
/*     */       } 
/*     */     }
/* 282 */     return ctx;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContextSelector getSelector() {
/* 290 */     return this.selector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShutdownCallbackRegistry getShutdownCallbackRegistry() {
/* 300 */     return this.shutdownCallbackRegistry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeContext(LoggerContext context) {
/* 310 */     if (context instanceof LoggerContext) {
/* 311 */       this.selector.removeContext((LoggerContext)context);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Cancellable addShutdownCallback(Runnable callback) {
/* 317 */     return SHUTDOWN_HOOK_ENABLED ? this.shutdownCallbackRegistry.addShutdownCallback(callback) : null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\impl\Log4jContextFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */