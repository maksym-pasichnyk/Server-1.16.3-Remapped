/*     */ package org.apache.logging.log4j.core.net;
/*     */ 
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.util.JndiCloser;
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
/*     */ public class JndiManager
/*     */   extends AbstractManager
/*     */ {
/*  38 */   private static final JndiManagerFactory FACTORY = new JndiManagerFactory();
/*     */   
/*     */   private final Context context;
/*     */   
/*     */   private JndiManager(String name, Context context) {
/*  43 */     super(null, name);
/*  44 */     this.context = context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JndiManager getDefaultManager() {
/*  53 */     return (JndiManager)getManager(JndiManager.class.getName(), FACTORY, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JndiManager getDefaultManager(String name) {
/*  62 */     return (JndiManager)getManager(name, FACTORY, null);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static JndiManager getJndiManager(String initialContextFactoryName, String providerURL, String urlPkgPrefixes, String securityPrincipal, String securityCredentials, Properties additionalProperties) {
/*  84 */     String name = JndiManager.class.getName() + '@' + JndiManager.class.hashCode();
/*  85 */     if (initialContextFactoryName == null) {
/*  86 */       return (JndiManager)getManager(name, FACTORY, null);
/*     */     }
/*  88 */     Properties properties = new Properties();
/*  89 */     properties.setProperty("java.naming.factory.initial", initialContextFactoryName);
/*  90 */     if (providerURL != null) {
/*  91 */       properties.setProperty("java.naming.provider.url", providerURL);
/*     */     } else {
/*  93 */       LOGGER.warn("The JNDI InitialContextFactory class name [{}] was provided, but there was no associated provider URL. This is likely to cause problems.", initialContextFactoryName);
/*     */     } 
/*     */     
/*  96 */     if (urlPkgPrefixes != null) {
/*  97 */       properties.setProperty("java.naming.factory.url.pkgs", urlPkgPrefixes);
/*     */     }
/*  99 */     if (securityPrincipal != null) {
/* 100 */       properties.setProperty("java.naming.security.principal", securityPrincipal);
/* 101 */       if (securityCredentials != null) {
/* 102 */         properties.setProperty("java.naming.security.credentials", securityCredentials);
/*     */       } else {
/* 104 */         LOGGER.warn("A security principal [{}] was provided, but with no corresponding security credentials.", securityPrincipal);
/*     */       } 
/*     */     } 
/*     */     
/* 108 */     if (additionalProperties != null) {
/* 109 */       properties.putAll(additionalProperties);
/*     */     }
/* 111 */     return (JndiManager)getManager(name, FACTORY, properties);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean releaseSub(long timeout, TimeUnit timeUnit) {
/* 116 */     return JndiCloser.closeSilently(this.context);
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
/*     */   public <T> T lookup(String name) throws NamingException {
/* 129 */     return (T)this.context.lookup(name);
/*     */   }
/*     */   
/*     */   private static class JndiManagerFactory implements ManagerFactory<JndiManager, Properties> {
/*     */     private JndiManagerFactory() {}
/*     */     
/*     */     public JndiManager createManager(String name, Properties data) {
/*     */       try {
/* 137 */         return new JndiManager(name, new InitialContext(data));
/* 138 */       } catch (NamingException e) {
/* 139 */         JndiManager.LOGGER.error("Error creating JNDI InitialContext.", e);
/* 140 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\JndiManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */