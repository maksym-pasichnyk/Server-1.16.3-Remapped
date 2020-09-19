/*     */ package org.apache.logging.log4j.core.net.server;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.jms.JMSException;
/*     */ import javax.jms.Message;
/*     */ import javax.jms.MessageConsumer;
/*     */ import javax.jms.MessageListener;
/*     */ import javax.jms.ObjectMessage;
/*     */ import org.apache.logging.log4j.LoggingException;
/*     */ import org.apache.logging.log4j.core.AbstractLifeCycle;
/*     */ import org.apache.logging.log4j.core.LifeCycle;
/*     */ import org.apache.logging.log4j.core.LifeCycle2;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.LogEventListener;
/*     */ import org.apache.logging.log4j.core.appender.mom.JmsManager;
/*     */ import org.apache.logging.log4j.core.net.JndiManager;
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
/*     */ public class JmsServer
/*     */   extends LogEventListener
/*     */   implements MessageListener, LifeCycle2
/*     */ {
/*  47 */   private final AtomicReference<LifeCycle.State> state = new AtomicReference<>(LifeCycle.State.INITIALIZED);
/*     */   
/*     */   private final JmsManager jmsManager;
/*     */   
/*     */   private MessageConsumer messageConsumer;
/*     */ 
/*     */   
/*     */   public JmsServer(String connectionFactoryBindingName, String destinationBindingName, String username, String password) {
/*  55 */     String managerName = JmsServer.class.getName() + '@' + JmsServer.class.hashCode();
/*  56 */     JndiManager jndiManager = JndiManager.getDefaultManager(managerName);
/*  57 */     this.jmsManager = JmsManager.getJmsManager(managerName, jndiManager, connectionFactoryBindingName, destinationBindingName, username, password);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LifeCycle.State getState() {
/*  63 */     return this.state.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMessage(Message message) {
/*     */     try {
/*  69 */       if (message instanceof ObjectMessage) {
/*  70 */         Object body = ((ObjectMessage)message).getObject();
/*  71 */         if (body instanceof LogEvent) {
/*  72 */           log((LogEvent)body);
/*     */         } else {
/*  74 */           LOGGER.warn("Expected ObjectMessage to contain LogEvent. Got type {} instead.", body.getClass());
/*     */         } 
/*     */       } else {
/*  77 */         LOGGER.warn("Received message of type {} and JMSType {} which cannot be handled.", message.getClass(), message.getJMSType());
/*     */       }
/*     */     
/*  80 */     } catch (JMSException e) {
/*  81 */       LOGGER.catching((Throwable)e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize() {}
/*     */ 
/*     */   
/*     */   public void start() {
/*  91 */     if (this.state.compareAndSet(LifeCycle.State.INITIALIZED, LifeCycle.State.STARTING)) {
/*     */       try {
/*  93 */         this.messageConsumer = this.jmsManager.createMessageConsumer();
/*  94 */         this.messageConsumer.setMessageListener(this);
/*  95 */       } catch (JMSException e) {
/*  96 */         throw new LoggingException(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 103 */     stop(0L, AbstractLifeCycle.DEFAULT_STOP_TIMEUNIT);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 108 */     boolean stopped = true;
/*     */     try {
/* 110 */       this.messageConsumer.close();
/* 111 */     } catch (JMSException e) {
/* 112 */       LOGGER.debug("Exception closing {}", this.messageConsumer, e);
/* 113 */       stopped = false;
/*     */     } 
/* 115 */     return (stopped && this.jmsManager.stop(timeout, timeUnit));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStarted() {
/* 120 */     return (this.state.get() == LifeCycle.State.STARTED);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStopped() {
/* 125 */     return (this.state.get() == LifeCycle.State.STOPPED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() throws IOException {
/*     */     String line;
/* 135 */     start();
/* 136 */     System.out.println("Type \"exit\" to quit.");
/* 137 */     BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, Charset.defaultCharset()));
/*     */     do {
/* 139 */       line = stdin.readLine();
/* 140 */     } while (line != null && !line.equalsIgnoreCase("exit"));
/* 141 */     System.out.println("Exiting. Kill the application if it does not exit due to daemon threads.");
/* 142 */     stop();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\server\JmsServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */