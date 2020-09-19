/*     */ package org.apache.logging.log4j.core.appender.mom.kafka;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.apache.kafka.clients.producer.Callback;
/*     */ import org.apache.kafka.clients.producer.Producer;
/*     */ import org.apache.kafka.clients.producer.ProducerRecord;
/*     */ import org.apache.kafka.clients.producer.RecordMetadata;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.config.Property;
/*     */ import org.apache.logging.log4j.core.util.Log4jThread;
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
/*     */ public class KafkaManager
/*     */   extends AbstractManager
/*     */ {
/*     */   public static final String DEFAULT_TIMEOUT_MILLIS = "30000";
/*  43 */   static KafkaProducerFactory producerFactory = new DefaultKafkaProducerFactory();
/*     */   
/*  45 */   private final Properties config = new Properties();
/*     */   
/*     */   private Producer<byte[], byte[]> producer;
/*     */   private final int timeoutMillis;
/*     */   private final String topic;
/*     */   private final boolean syncSend;
/*     */   
/*     */   public KafkaManager(LoggerContext loggerContext, String name, String topic, boolean syncSend, Property[] properties) {
/*  53 */     super(loggerContext, name);
/*  54 */     this.topic = Objects.<String>requireNonNull(topic, "topic");
/*  55 */     this.syncSend = syncSend;
/*  56 */     this.config.setProperty("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
/*  57 */     this.config.setProperty("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
/*  58 */     this.config.setProperty("batch.size", "0");
/*  59 */     for (Property property : properties) {
/*  60 */       this.config.setProperty(property.getName(), property.getValue());
/*     */     }
/*  62 */     this.timeoutMillis = Integer.parseInt(this.config.getProperty("timeout.ms", "30000"));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean releaseSub(long timeout, TimeUnit timeUnit) {
/*  67 */     if (timeout > 0L) {
/*  68 */       closeProducer(timeout, timeUnit);
/*     */     } else {
/*  70 */       closeProducer(this.timeoutMillis, TimeUnit.MILLISECONDS);
/*     */     } 
/*  72 */     return true;
/*     */   }
/*     */   
/*     */   private void closeProducer(long timeout, TimeUnit timeUnit) {
/*  76 */     if (this.producer != null) {
/*     */       
/*  78 */       Log4jThread log4jThread = new Log4jThread(new Runnable()
/*     */           {
/*     */             public void run() {
/*  81 */               if (KafkaManager.this.producer != null) {
/*  82 */                 KafkaManager.this.producer.close();
/*     */               }
/*     */             }
/*     */           },  "KafkaManager-CloseThread");
/*  86 */       log4jThread.setDaemon(true);
/*  87 */       log4jThread.start();
/*     */       try {
/*  89 */         log4jThread.join(timeUnit.toMillis(timeout));
/*  90 */       } catch (InterruptedException ignore) {
/*  91 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void send(byte[] msg) throws ExecutionException, InterruptedException, TimeoutException {
/*  98 */     if (this.producer != null) {
/*  99 */       ProducerRecord<byte[], byte[]> newRecord = new ProducerRecord(this.topic, msg);
/* 100 */       if (this.syncSend) {
/* 101 */         Future<RecordMetadata> response = this.producer.send(newRecord);
/* 102 */         response.get(this.timeoutMillis, TimeUnit.MILLISECONDS);
/*     */       } else {
/* 104 */         this.producer.send(newRecord, new Callback() {
/*     */               public void onCompletion(RecordMetadata metadata, Exception e) {
/* 106 */                 if (e != null) {
/* 107 */                   KafkaManager.LOGGER.error("Unable to write to Kafka [" + KafkaManager.this.getName() + "].", e);
/*     */                 }
/*     */               }
/*     */             });
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void startup() {
/* 116 */     this.producer = producerFactory.newKafkaProducer(this.config);
/*     */   }
/*     */   
/*     */   public String getTopic() {
/* 120 */     return this.topic;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\mom\kafka\KafkaManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */