/*     */ package org.apache.logging.log4j.core.appender.db.jpa;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import javax.persistence.EntityManager;
/*     */ import javax.persistence.EntityManagerFactory;
/*     */ import javax.persistence.EntityTransaction;
/*     */ import javax.persistence.Persistence;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.AppenderLoggingException;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
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
/*     */ public final class JpaDatabaseManager
/*     */   extends AbstractDatabaseManager
/*     */ {
/*  35 */   private static final JPADatabaseManagerFactory FACTORY = new JPADatabaseManagerFactory();
/*     */   
/*     */   private final String entityClassName;
/*     */   
/*     */   private final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor;
/*     */   
/*     */   private final String persistenceUnitName;
/*     */   
/*     */   private EntityManagerFactory entityManagerFactory;
/*     */   
/*     */   private EntityManager entityManager;
/*     */   
/*     */   private EntityTransaction transaction;
/*     */   
/*     */   private JpaDatabaseManager(String name, int bufferSize, Class<? extends AbstractLogEventWrapperEntity> entityClass, Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor, String persistenceUnitName) {
/*  50 */     super(name, bufferSize);
/*  51 */     this.entityClassName = entityClass.getName();
/*  52 */     this.entityConstructor = entityConstructor;
/*  53 */     this.persistenceUnitName = persistenceUnitName;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void startupInternal() {
/*  58 */     this.entityManagerFactory = Persistence.createEntityManagerFactory(this.persistenceUnitName);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shutdownInternal() {
/*  63 */     boolean closed = true;
/*  64 */     if (this.entityManager != null || this.transaction != null) {
/*  65 */       closed &= commitAndClose();
/*     */     }
/*  67 */     if (this.entityManagerFactory != null && this.entityManagerFactory.isOpen()) {
/*  68 */       this.entityManagerFactory.close();
/*     */     }
/*  70 */     return closed;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void connectAndStart() {
/*     */     try {
/*  76 */       this.entityManager = this.entityManagerFactory.createEntityManager();
/*  77 */       this.transaction = this.entityManager.getTransaction();
/*  78 */       this.transaction.begin();
/*  79 */     } catch (Exception e) {
/*  80 */       throw new AppenderLoggingException("Cannot write logging event or flush buffer; manager cannot create EntityManager or transaction.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(LogEvent event) {
/*     */     AbstractLogEventWrapperEntity entity;
/*  88 */     if (!isRunning() || this.entityManagerFactory == null || this.entityManager == null || this.transaction == null)
/*     */     {
/*  90 */       throw new AppenderLoggingException("Cannot write logging event; JPA manager not connected to the database.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  96 */       entity = this.entityConstructor.newInstance(new Object[] { event });
/*  97 */     } catch (Exception e) {
/*  98 */       throw new AppenderLoggingException("Failed to instantiate entity class [" + this.entityClassName + "].", e);
/*     */     } 
/*     */     
/*     */     try {
/* 102 */       this.entityManager.persist(entity);
/* 103 */     } catch (Exception e) {
/* 104 */       if (this.transaction != null && this.transaction.isActive()) {
/* 105 */         this.transaction.rollback();
/* 106 */         this.transaction = null;
/*     */       } 
/* 108 */       throw new AppenderLoggingException("Failed to insert record for log event in JPA manager: " + e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean commitAndClose() {
/* 115 */     boolean closed = true;
/*     */     try {
/* 117 */       if (this.transaction != null && this.transaction.isActive()) {
/* 118 */         this.transaction.commit();
/*     */       }
/* 120 */     } catch (Exception e) {
/* 121 */       if (this.transaction != null && this.transaction.isActive()) {
/* 122 */         this.transaction.rollback();
/*     */       }
/*     */     } finally {
/* 125 */       this.transaction = null;
/*     */       try {
/* 127 */         if (this.entityManager != null && this.entityManager.isOpen()) {
/* 128 */           this.entityManager.close();
/*     */         }
/* 130 */       } catch (Exception e) {
/* 131 */         logWarn("Failed to close entity manager while logging event or flushing buffer", e);
/* 132 */         closed = false;
/*     */       } finally {
/* 134 */         this.entityManager = null;
/*     */       } 
/*     */     } 
/* 137 */     return closed;
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
/*     */   public static JpaDatabaseManager getJPADatabaseManager(String name, int bufferSize, Class<? extends AbstractLogEventWrapperEntity> entityClass, Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor, String persistenceUnitName) {
/* 158 */     return (JpaDatabaseManager)AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, entityClass, entityConstructor, persistenceUnitName), FACTORY);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class FactoryData
/*     */     extends AbstractDatabaseManager.AbstractFactoryData
/*     */   {
/*     */     private final Class<? extends AbstractLogEventWrapperEntity> entityClass;
/*     */     
/*     */     private final Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor;
/*     */     
/*     */     private final String persistenceUnitName;
/*     */ 
/*     */     
/*     */     protected FactoryData(int bufferSize, Class<? extends AbstractLogEventWrapperEntity> entityClass, Constructor<? extends AbstractLogEventWrapperEntity> entityConstructor, String persistenceUnitName) {
/* 174 */       super(bufferSize);
/*     */       
/* 176 */       this.entityClass = entityClass;
/* 177 */       this.entityConstructor = entityConstructor;
/* 178 */       this.persistenceUnitName = persistenceUnitName;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class JPADatabaseManagerFactory
/*     */     implements ManagerFactory<JpaDatabaseManager, FactoryData>
/*     */   {
/*     */     private JPADatabaseManagerFactory() {}
/*     */     
/*     */     public JpaDatabaseManager createManager(String name, JpaDatabaseManager.FactoryData data) {
/* 188 */       return new JpaDatabaseManager(name, data.getBufferSize(), data.entityClass, data.entityConstructor, data.persistenceUnitName);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\db\jpa\JpaDatabaseManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */