/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.security.Permission;
/*     */ import java.util.List;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.spi.LoggerContextFactory;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
/*     */ import org.osgi.framework.AdaptPermission;
/*     */ import org.osgi.framework.AdminPermission;
/*     */ import org.osgi.framework.Bundle;
/*     */ import org.osgi.framework.BundleActivator;
/*     */ import org.osgi.framework.BundleContext;
/*     */ import org.osgi.framework.BundleEvent;
/*     */ import org.osgi.framework.BundleListener;
/*     */ import org.osgi.framework.SynchronousBundleListener;
/*     */ import org.osgi.framework.wiring.BundleWire;
/*     */ import org.osgi.framework.wiring.BundleWiring;
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
/*     */ public class Activator
/*     */   implements BundleActivator, SynchronousBundleListener
/*     */ {
/*  45 */   private static final SecurityManager SECURITY_MANAGER = System.getSecurityManager();
/*     */   
/*  47 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */   
/*     */   private boolean lockingProviderUtil;
/*     */ 
/*     */   
/*     */   private static void checkPermission(Permission permission) {
/*  54 */     if (SECURITY_MANAGER != null) {
/*  55 */       SECURITY_MANAGER.checkPermission(permission);
/*     */     }
/*     */   }
/*     */   
/*     */   private void loadProvider(Bundle bundle) {
/*  60 */     if (bundle.getState() == 1) {
/*     */       return;
/*     */     }
/*     */     try {
/*  64 */       checkPermission((Permission)new AdminPermission(bundle, "resource"));
/*  65 */       checkPermission((Permission)new AdaptPermission(BundleWiring.class.getName(), bundle, "adapt"));
/*  66 */       loadProvider((BundleWiring)bundle.adapt(BundleWiring.class));
/*  67 */     } catch (SecurityException e) {
/*  68 */       LOGGER.debug("Cannot access bundle [{}] contents. Ignoring.", bundle.getSymbolicName(), e);
/*  69 */     } catch (Exception e) {
/*  70 */       LOGGER.warn("Problem checking bundle {} for Log4j 2 provider.", bundle.getSymbolicName(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadProvider(BundleWiring provider) {
/*  75 */     List<URL> urls = provider.findEntries("META-INF", "log4j-provider.properties", 0);
/*  76 */     for (URL url : urls) {
/*  77 */       ProviderUtil.loadProvider(url, provider.getClassLoader());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void start(BundleContext context) throws Exception {
/*  83 */     ProviderUtil.STARTUP_LOCK.lock();
/*  84 */     this.lockingProviderUtil = true;
/*  85 */     BundleWiring self = (BundleWiring)context.getBundle().adapt(BundleWiring.class);
/*  86 */     List<BundleWire> required = self.getRequiredWires(LoggerContextFactory.class.getName());
/*  87 */     for (BundleWire wire : required) {
/*  88 */       loadProvider(wire.getProviderWiring());
/*     */     }
/*  90 */     context.addBundleListener((BundleListener)this);
/*  91 */     Bundle[] bundles = context.getBundles();
/*  92 */     for (Bundle bundle : bundles) {
/*  93 */       loadProvider(bundle);
/*     */     }
/*  95 */     unlockIfReady();
/*     */   }
/*     */   
/*     */   private void unlockIfReady() {
/*  99 */     if (this.lockingProviderUtil && !ProviderUtil.PROVIDERS.isEmpty()) {
/* 100 */       ProviderUtil.STARTUP_LOCK.unlock();
/* 101 */       this.lockingProviderUtil = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop(BundleContext context) throws Exception {
/* 107 */     context.removeBundleListener((BundleListener)this);
/* 108 */     unlockIfReady();
/*     */   }
/*     */ 
/*     */   
/*     */   public void bundleChanged(BundleEvent event) {
/* 113 */     switch (event.getType()) {
/*     */       case 2:
/* 115 */         loadProvider(event.getBundle());
/* 116 */         unlockIfReady();
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4\\util\Activator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */