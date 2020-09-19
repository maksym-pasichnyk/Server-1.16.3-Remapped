/*    */ package org.apache.logging.log4j.core.osgi;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ import org.apache.logging.log4j.core.config.plugins.util.PluginRegistry;
/*    */ import org.apache.logging.log4j.status.StatusLogger;
/*    */ import org.apache.logging.log4j.util.PropertiesUtil;
/*    */ import org.osgi.framework.Bundle;
/*    */ import org.osgi.framework.BundleActivator;
/*    */ import org.osgi.framework.BundleContext;
/*    */ import org.osgi.framework.BundleEvent;
/*    */ import org.osgi.framework.BundleListener;
/*    */ import org.osgi.framework.SynchronousBundleListener;
/*    */ import org.osgi.framework.wiring.BundleWiring;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Activator
/*    */   implements BundleActivator, SynchronousBundleListener
/*    */ {
/* 40 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*    */   
/* 42 */   private final AtomicReference<BundleContext> contextRef = new AtomicReference<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public void start(BundleContext context) throws Exception {
/* 47 */     if (PropertiesUtil.getProperties().getStringProperty("Log4jContextSelector") == null) {
/* 48 */       System.setProperty("Log4jContextSelector", BundleContextSelector.class.getName());
/*    */     }
/* 50 */     if (this.contextRef.compareAndSet(null, context)) {
/* 51 */       context.addBundleListener((BundleListener)this);
/*    */       
/* 53 */       scanInstalledBundlesForPlugins(context);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void scanInstalledBundlesForPlugins(BundleContext context) {
/* 58 */     Bundle[] bundles = context.getBundles();
/* 59 */     for (Bundle bundle : bundles) {
/*    */       
/* 61 */       if (bundle.getState() == 32 && bundle.getBundleId() != 0L)
/*    */       {
/* 63 */         scanBundleForPlugins(bundle);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void scanBundleForPlugins(Bundle bundle) {
/* 69 */     LOGGER.trace("Scanning bundle [{}] for plugins.", bundle.getSymbolicName());
/* 70 */     PluginRegistry.getInstance().loadFromBundle(bundle.getBundleId(), ((BundleWiring)bundle.adapt(BundleWiring.class)).getClassLoader());
/*    */   }
/*    */ 
/*    */   
/*    */   private static void stopBundlePlugins(Bundle bundle) {
/* 75 */     LOGGER.trace("Stopping bundle [{}] plugins.", bundle.getSymbolicName());
/*    */     
/* 77 */     PluginRegistry.getInstance().clearBundlePlugins(bundle.getBundleId());
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop(BundleContext context) throws Exception {
/* 82 */     this.contextRef.compareAndSet(context, null);
/* 83 */     LogManager.shutdown();
/*    */   }
/*    */ 
/*    */   
/*    */   public void bundleChanged(BundleEvent event) {
/* 88 */     switch (event.getType()) {
/*    */       
/*    */       case 2:
/* 91 */         scanBundleForPlugins(event.getBundle());
/*    */         break;
/*    */       
/*    */       case 256:
/* 95 */         stopBundlePlugins(event.getBundle());
/*    */         break;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\osgi\Activator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */