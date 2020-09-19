/*     */ package org.apache.logging.log4j.core.appender.db.jpa;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*     */ import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ import org.apache.logging.log4j.util.LoaderUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "JPA", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class JpaAppender
/*     */   extends AbstractDatabaseAppender<JpaDatabaseManager>
/*     */ {
/*     */   private final String description;
/*     */   
/*     */   private JpaAppender(String name, Filter filter, boolean ignoreExceptions, JpaDatabaseManager manager) {
/*  49 */     super(name, filter, ignoreExceptions, manager);
/*  50 */     this.description = getName() + "{ manager=" + getManager() + " }";
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  55 */     return this.description;
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
/*     */ 
/*     */   
/*     */   @PluginFactory
/*     */   public static JpaAppender createAppender(@PluginAttribute("name") String name, @PluginAttribute("ignoreExceptions") String ignore, @PluginElement("Filter") Filter filter, @PluginAttribute("bufferSize") String bufferSize, @PluginAttribute("entityClassName") String entityClassName, @PluginAttribute("persistenceUnitName") String persistenceUnitName) {
/*  80 */     if (Strings.isEmpty(entityClassName) || Strings.isEmpty(persistenceUnitName)) {
/*  81 */       LOGGER.error("Attributes entityClassName and persistenceUnitName are required for JPA Appender.");
/*  82 */       return null;
/*     */     } 
/*     */     
/*  85 */     int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
/*  86 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/*     */     
/*     */     try {
/*  89 */       Class<? extends AbstractLogEventWrapperEntity> entityClass = LoaderUtil.loadClass(entityClassName).asSubclass(AbstractLogEventWrapperEntity.class);
/*     */ 
/*     */       
/*     */       try {
/*  93 */         entityClass.getConstructor(new Class[0]);
/*  94 */       } catch (NoSuchMethodException e) {
/*  95 */         LOGGER.error("Entity class [{}] does not have a no-arg constructor. The JPA provider will reject it.", entityClassName);
/*     */         
/*  97 */         return null;
/*     */       } 
/*     */       
/* 100 */       Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor = entityClass.getConstructor(new Class[] { LogEvent.class });
/*     */ 
/*     */       
/* 103 */       String managerName = "jpaManager{ description=" + name + ", bufferSize=" + bufferSizeInt + ", persistenceUnitName=" + persistenceUnitName + ", entityClass=" + entityClass.getName() + '}';
/*     */ 
/*     */       
/* 106 */       JpaDatabaseManager manager = JpaDatabaseManager.getJPADatabaseManager(managerName, bufferSizeInt, entityClass, entityConstructor, persistenceUnitName);
/*     */ 
/*     */       
/* 109 */       if (manager == null) {
/* 110 */         return null;
/*     */       }
/*     */       
/* 113 */       return new JpaAppender(name, filter, ignoreExceptions, manager);
/* 114 */     } catch (ClassNotFoundException e) {
/* 115 */       LOGGER.error("Could not load entity class [{}].", entityClassName, e);
/* 116 */       return null;
/* 117 */     } catch (NoSuchMethodException e) {
/* 118 */       LOGGER.error("Entity class [{}] does not have a constructor with a single argument of type LogEvent.", entityClassName);
/*     */       
/* 120 */       return null;
/* 121 */     } catch (ClassCastException e) {
/* 122 */       LOGGER.error("Entity class [{}] does not extend AbstractLogEventWrapperEntity.", entityClassName);
/* 123 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\db\jpa\JpaAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */