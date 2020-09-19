/*    */ package org.apache.logging.log4j.core.osgi;
/*    */ 
/*    */ import java.lang.ref.WeakReference;
/*    */ import java.net.URI;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ import org.apache.logging.log4j.core.LoggerContext;
/*    */ import org.apache.logging.log4j.core.impl.ContextAnchor;
/*    */ import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;
/*    */ import org.apache.logging.log4j.util.ReflectionUtil;
/*    */ import org.osgi.framework.Bundle;
/*    */ import org.osgi.framework.BundleReference;
/*    */ import org.osgi.framework.FrameworkUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BundleContextSelector
/*    */   extends ClassLoaderContextSelector
/*    */ {
/*    */   public LoggerContext getContext(String fqcn, ClassLoader loader, boolean currentContext, URI configLocation) {
/* 44 */     if (currentContext) {
/* 45 */       LoggerContext ctx = ContextAnchor.THREAD_CONTEXT.get();
/* 46 */       if (ctx != null) {
/* 47 */         return ctx;
/*    */       }
/* 49 */       return getDefault();
/*    */     } 
/*    */     
/* 52 */     if (loader instanceof BundleReference) {
/* 53 */       return locateContext(((BundleReference)loader).getBundle(), configLocation);
/*    */     }
/* 55 */     Class<?> callerClass = ReflectionUtil.getCallerClass(fqcn);
/* 56 */     if (callerClass != null) {
/* 57 */       return locateContext(FrameworkUtil.getBundle(callerClass), configLocation);
/*    */     }
/* 59 */     LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
/* 60 */     return (lc == null) ? getDefault() : lc;
/*    */   }
/*    */   
/*    */   private static LoggerContext locateContext(Bundle bundle, URI configLocation) {
/* 64 */     String name = ((Bundle)Objects.<Bundle>requireNonNull(bundle, "No Bundle provided")).getSymbolicName();
/* 65 */     AtomicReference<WeakReference<LoggerContext>> ref = (AtomicReference<WeakReference<LoggerContext>>)CONTEXT_MAP.get(name);
/* 66 */     if (ref == null) {
/* 67 */       LoggerContext context = new LoggerContext(name, bundle, configLocation);
/* 68 */       CONTEXT_MAP.putIfAbsent(name, new AtomicReference(new WeakReference<>(context)));
/*    */       
/* 70 */       return ((WeakReference<LoggerContext>)((AtomicReference<WeakReference<LoggerContext>>)CONTEXT_MAP.get(name)).get()).get();
/*    */     } 
/* 72 */     WeakReference<LoggerContext> r = ref.get();
/* 73 */     LoggerContext ctx = r.get();
/* 74 */     if (ctx == null) {
/* 75 */       LoggerContext context = new LoggerContext(name, bundle, configLocation);
/* 76 */       ref.compareAndSet(r, new WeakReference<>(context));
/* 77 */       return ((WeakReference<LoggerContext>)ref.get()).get();
/*    */     } 
/* 79 */     URI oldConfigLocation = ctx.getConfigLocation();
/* 80 */     if (oldConfigLocation == null && configLocation != null) {
/* 81 */       LOGGER.debug("Setting bundle ({}) configuration to {}", name, configLocation);
/* 82 */       ctx.setConfigLocation(configLocation);
/* 83 */     } else if (oldConfigLocation != null && configLocation != null && !configLocation.equals(oldConfigLocation)) {
/* 84 */       LOGGER.warn("locateContext called with URI [{}], but existing LoggerContext has URI [{}]", configLocation, oldConfigLocation);
/*    */     } 
/*    */     
/* 87 */     return ctx;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\osgi\BundleContextSelector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */