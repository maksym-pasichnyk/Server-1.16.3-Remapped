/*     */ package org.apache.logging.log4j.core.appender.db.jdbc;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.lang.reflect.Method;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.logging.Logger;
/*     */ import javax.sql.DataSource;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginFactory;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ @Plugin(name = "ConnectionFactory", category = "Core", elementType = "connectionSource", printObject = true)
/*     */ public final class FactoryMethodConnectionSource
/*     */   implements ConnectionSource
/*     */ {
/*  41 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*     */   private final DataSource dataSource;
/*     */   
/*     */   private final String description;
/*     */   
/*     */   private FactoryMethodConnectionSource(DataSource dataSource, String className, String methodName, String returnType) {
/*  48 */     this.dataSource = dataSource;
/*  49 */     this.description = "factory{ public static " + returnType + ' ' + className + '.' + methodName + "() }";
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/*  54 */     return this.dataSource.getConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  59 */     return this.description;
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
/*     */   @PluginFactory
/*     */   public static FactoryMethodConnectionSource createConnectionSource(@PluginAttribute("class") String className, @PluginAttribute("method") String methodName) {
/*     */     final Method method;
/*     */     DataSource dataSource;
/*  76 */     if (Strings.isEmpty(className) || Strings.isEmpty(methodName)) {
/*  77 */       LOGGER.error("No class name or method name specified for the connection factory method.");
/*  78 */       return null;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/*  83 */       Class<?> factoryClass = LoaderUtil.loadClass(className);
/*  84 */       method = factoryClass.getMethod(methodName, new Class[0]);
/*  85 */     } catch (Exception e) {
/*  86 */       LOGGER.error(e.toString(), e);
/*  87 */       return null;
/*     */     } 
/*     */     
/*  90 */     Class<?> returnType = method.getReturnType();
/*  91 */     String returnTypeString = returnType.getName();
/*     */     
/*  93 */     if (returnType == DataSource.class) {
/*     */       try {
/*  95 */         dataSource = (DataSource)method.invoke(null, new Object[0]);
/*  96 */         returnTypeString = returnTypeString + "[" + dataSource + ']';
/*  97 */       } catch (Exception e) {
/*  98 */         LOGGER.error(e.toString(), e);
/*  99 */         return null;
/*     */       } 
/* 101 */     } else if (returnType == Connection.class) {
/* 102 */       dataSource = new DataSource()
/*     */         {
/*     */           public Connection getConnection() throws SQLException {
/*     */             try {
/* 106 */               return (Connection)method.invoke(null, new Object[0]);
/* 107 */             } catch (Exception e) {
/* 108 */               throw new SQLException("Failed to obtain connection from factory method.", e);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public Connection getConnection(String username, String password) throws SQLException {
/* 114 */             throw new UnsupportedOperationException();
/*     */           }
/*     */ 
/*     */           
/*     */           public int getLoginTimeout() throws SQLException {
/* 119 */             throw new UnsupportedOperationException();
/*     */           }
/*     */ 
/*     */           
/*     */           public PrintWriter getLogWriter() throws SQLException {
/* 124 */             throw new UnsupportedOperationException();
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public Logger getParentLogger() {
/* 130 */             throw new UnsupportedOperationException();
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 135 */             return false;
/*     */           }
/*     */ 
/*     */           
/*     */           public void setLoginTimeout(int seconds) throws SQLException {
/* 140 */             throw new UnsupportedOperationException();
/*     */           }
/*     */ 
/*     */           
/*     */           public void setLogWriter(PrintWriter out) throws SQLException {
/* 145 */             throw new UnsupportedOperationException();
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> T unwrap(Class<T> iface) throws SQLException {
/* 150 */             return null;
/*     */           }
/*     */         };
/*     */     } else {
/* 154 */       LOGGER.error("Method [{}.{}()] returns unsupported type [{}].", className, methodName, returnType.getName());
/*     */       
/* 156 */       return null;
/*     */     } 
/*     */     
/* 159 */     return new FactoryMethodConnectionSource(dataSource, className, methodName, returnTypeString);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\db\jdbc\FactoryMethodConnectionSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */