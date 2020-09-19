/*     */ package org.apache.logging.log4j.core.appender.mom;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.jms.Connection;
/*     */ import javax.jms.ConnectionFactory;
/*     */ import javax.jms.Destination;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageConsumer;
/*     */ import javax.jms.MessageProducer;
/*     */ import javax.jms.Session;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.net.JndiManager;
/*     */ import org.apache.logging.log4j.status.StatusLogger;
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
/*     */ public class JmsManager
/*     */   extends AbstractManager
/*     */ {
/*  45 */   private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */   
/*  47 */   private static final JmsManagerFactory FACTORY = new JmsManagerFactory();
/*     */   
/*     */   private final JndiManager jndiManager;
/*     */   
/*     */   private final Connection connection;
/*     */   
/*     */   private final Session session;
/*     */   private final Destination destination;
/*     */   
/*     */   private JmsManager(String name, JndiManager jndiManager, String connectionFactoryName, String destinationName, String username, String password) throws NamingException, JMSException {
/*  57 */     super(null, name);
/*  58 */     this.jndiManager = jndiManager;
/*  59 */     ConnectionFactory connectionFactory = (ConnectionFactory)this.jndiManager.lookup(connectionFactoryName);
/*  60 */     if (username != null && password != null) {
/*  61 */       this.connection = connectionFactory.createConnection(username, password);
/*     */     } else {
/*  63 */       this.connection = connectionFactory.createConnection();
/*     */     } 
/*  65 */     this.session = this.connection.createSession(false, 1);
/*  66 */     this.destination = (Destination)this.jndiManager.lookup(destinationName);
/*  67 */     this.connection.start();
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
/*     */   public static JmsManager getJmsManager(String name, JndiManager jndiManager, String connectionFactoryName, String destinationName, String username, String password) {
/*  84 */     JmsConfiguration configuration = new JmsConfiguration(jndiManager, connectionFactoryName, destinationName, username, password);
/*     */     
/*  86 */     return (JmsManager)getManager(name, FACTORY, configuration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageConsumer createMessageConsumer() throws JMSException {
/*  96 */     return this.session.createConsumer(this.destination);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageProducer createMessageProducer() throws JMSException {
/* 106 */     return this.session.createProducer(this.destination);
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
/*     */   public Message createMessage(Serializable object) throws JMSException {
/* 121 */     if (object instanceof String) {
/* 122 */       return (Message)this.session.createTextMessage((String)object);
/*     */     }
/* 124 */     return (Message)this.session.createObjectMessage(object);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean releaseSub(long timeout, TimeUnit timeUnit) {
/* 129 */     boolean closed = true;
/*     */     try {
/* 131 */       this.session.close();
/* 132 */     } catch (JMSException ignored) {
/*     */       
/* 134 */       closed = false;
/*     */     } 
/*     */     try {
/* 137 */       this.connection.close();
/* 138 */     } catch (JMSException ignored) {
/*     */       
/* 140 */       closed = false;
/*     */     } 
/* 142 */     return (closed && this.jndiManager.stop(timeout, timeUnit));
/*     */   }
/*     */   
/*     */   private static class JmsConfiguration
/*     */   {
/*     */     private final JndiManager jndiManager;
/*     */     private final String connectionFactoryName;
/*     */     private final String destinationName;
/*     */     private final String username;
/*     */     private final String password;
/*     */     
/*     */     private JmsConfiguration(JndiManager jndiManager, String connectionFactoryName, String destinationName, String username, String password) {
/* 154 */       this.jndiManager = jndiManager;
/* 155 */       this.connectionFactoryName = connectionFactoryName;
/* 156 */       this.destinationName = destinationName;
/* 157 */       this.username = username;
/* 158 */       this.password = password;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class JmsManagerFactory implements ManagerFactory<JmsManager, JmsConfiguration> {
/*     */     private JmsManagerFactory() {}
/*     */     
/*     */     public JmsManager createManager(String name, JmsManager.JmsConfiguration data) {
/*     */       try {
/* 167 */         return new JmsManager(name, data.jndiManager, data.connectionFactoryName, data.destinationName, data.username, data.password);
/*     */       }
/* 169 */       catch (Exception e) {
/* 170 */         JmsManager.LOGGER.error("Error creating JmsManager using ConnectionFactory [{}] and Destination [{}].", data.connectionFactoryName, data.destinationName, e);
/*     */         
/* 172 */         return null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\mom\JmsManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */