/*     */ package org.apache.logging.log4j.core.appender.mom.jeromq;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.core.appender.AbstractManager;
/*     */ import org.apache.logging.log4j.core.appender.ManagerFactory;
/*     */ import org.apache.logging.log4j.core.util.ShutdownCallbackRegistry;
/*     */ import org.apache.logging.log4j.util.PropertiesUtil;
/*     */ import org.zeromq.ZMQ;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JeroMqManager
/*     */   extends AbstractManager
/*     */ {
/*     */   public static final String SYS_PROPERTY_ENABLE_SHUTDOWN_HOOK = "log4j.jeromq.enableShutdownHook";
/*     */   public static final String SYS_PROPERTY_IO_THREADS = "log4j.jeromq.ioThreads";
/*  48 */   private static final JeroMqManagerFactory FACTORY = new JeroMqManagerFactory();
/*     */   private static final ZMQ.Context CONTEXT;
/*     */   
/*     */   static {
/*  52 */     LOGGER.trace("JeroMqManager using ZMQ version {}", ZMQ.getVersionString());
/*     */     
/*  54 */     int ioThreads = PropertiesUtil.getProperties().getIntegerProperty("log4j.jeromq.ioThreads", 1);
/*  55 */     LOGGER.trace("JeroMqManager creating ZMQ context with ioThreads = {}", Integer.valueOf(ioThreads));
/*  56 */     CONTEXT = ZMQ.context(ioThreads);
/*     */     
/*  58 */     boolean enableShutdownHook = PropertiesUtil.getProperties().getBooleanProperty("log4j.jeromq.enableShutdownHook", true);
/*     */     
/*  60 */     if (enableShutdownHook) {
/*  61 */       ((ShutdownCallbackRegistry)LogManager.getFactory()).addShutdownCallback(new Runnable()
/*     */           {
/*     */             public void run() {
/*  64 */               JeroMqManager.CONTEXT.close();
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   private final ZMQ.Socket publisher;
/*     */   
/*     */   private JeroMqManager(String name, JeroMqConfiguration config) {
/*  73 */     super(null, name);
/*  74 */     this.publisher = CONTEXT.socket(1);
/*  75 */     this.publisher.setAffinity(config.affinity);
/*  76 */     this.publisher.setBacklog(config.backlog);
/*  77 */     this.publisher.setDelayAttachOnConnect(config.delayAttachOnConnect);
/*  78 */     if (config.identity != null) {
/*  79 */       this.publisher.setIdentity(config.identity);
/*     */     }
/*  81 */     this.publisher.setIPv4Only(config.ipv4Only);
/*  82 */     this.publisher.setLinger(config.linger);
/*  83 */     this.publisher.setMaxMsgSize(config.maxMsgSize);
/*  84 */     this.publisher.setRcvHWM(config.rcvHwm);
/*  85 */     this.publisher.setReceiveBufferSize(config.receiveBufferSize);
/*  86 */     this.publisher.setReceiveTimeOut(config.receiveTimeOut);
/*  87 */     this.publisher.setReconnectIVL(config.reconnectIVL);
/*  88 */     this.publisher.setReconnectIVLMax(config.reconnectIVLMax);
/*  89 */     this.publisher.setSendBufferSize(config.sendBufferSize);
/*  90 */     this.publisher.setSendTimeOut(config.sendTimeOut);
/*  91 */     this.publisher.setSndHWM(config.sndHwm);
/*  92 */     this.publisher.setTCPKeepAlive(config.tcpKeepAlive);
/*  93 */     this.publisher.setTCPKeepAliveCount(config.tcpKeepAliveCount);
/*  94 */     this.publisher.setTCPKeepAliveIdle(config.tcpKeepAliveIdle);
/*  95 */     this.publisher.setTCPKeepAliveInterval(config.tcpKeepAliveInterval);
/*  96 */     this.publisher.setXpubVerbose(config.xpubVerbose);
/*  97 */     for (String endpoint : config.endpoints) {
/*  98 */       this.publisher.bind(endpoint);
/*     */     }
/* 100 */     LOGGER.debug("Created JeroMqManager with {}", config);
/*     */   }
/*     */   
/*     */   public boolean send(byte[] data) {
/* 104 */     return this.publisher.send(data);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean releaseSub(long timeout, TimeUnit timeUnit) {
/* 109 */     this.publisher.close();
/* 110 */     return true;
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
/*     */   public static JeroMqManager getJeroMqManager(String name, long affinity, long backlog, boolean delayAttachOnConnect, byte[] identity, boolean ipv4Only, long linger, long maxMsgSize, long rcvHwm, long receiveBufferSize, int receiveTimeOut, long reconnectIVL, long reconnectIVLMax, long sendBufferSize, int sendTimeOut, long sndHwm, int tcpKeepAlive, long tcpKeepAliveCount, long tcpKeepAliveIdle, long tcpKeepAliveInterval, boolean xpubVerbose, List<String> endpoints) {
/* 123 */     return (JeroMqManager)getManager(name, FACTORY, new JeroMqConfiguration(affinity, backlog, delayAttachOnConnect, identity, ipv4Only, linger, maxMsgSize, rcvHwm, receiveBufferSize, receiveTimeOut, reconnectIVL, reconnectIVLMax, sendBufferSize, sendTimeOut, sndHwm, tcpKeepAlive, tcpKeepAliveCount, tcpKeepAliveIdle, tcpKeepAliveInterval, xpubVerbose, endpoints));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ZMQ.Context getContext() {
/* 131 */     return CONTEXT;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class JeroMqConfiguration
/*     */   {
/*     */     private final long affinity;
/*     */     
/*     */     private final long backlog;
/*     */     
/*     */     private final boolean delayAttachOnConnect;
/*     */     
/*     */     private final byte[] identity;
/*     */     
/*     */     private final boolean ipv4Only;
/*     */     
/*     */     private final long linger;
/*     */     private final long maxMsgSize;
/*     */     private final long rcvHwm;
/*     */     private final long receiveBufferSize;
/*     */     private final int receiveTimeOut;
/*     */     private final long reconnectIVL;
/*     */     private final long reconnectIVLMax;
/*     */     private final long sendBufferSize;
/*     */     private final int sendTimeOut;
/*     */     private final long sndHwm;
/*     */     private final int tcpKeepAlive;
/*     */     private final long tcpKeepAliveCount;
/*     */     private final long tcpKeepAliveIdle;
/*     */     private final long tcpKeepAliveInterval;
/*     */     private final boolean xpubVerbose;
/*     */     private final List<String> endpoints;
/*     */     
/*     */     private JeroMqConfiguration(long affinity, long backlog, boolean delayAttachOnConnect, byte[] identity, boolean ipv4Only, long linger, long maxMsgSize, long rcvHwm, long receiveBufferSize, int receiveTimeOut, long reconnectIVL, long reconnectIVLMax, long sendBufferSize, int sendTimeOut, long sndHwm, int tcpKeepAlive, long tcpKeepAliveCount, long tcpKeepAliveIdle, long tcpKeepAliveInterval, boolean xpubVerbose, List<String> endpoints) {
/* 165 */       this.affinity = affinity;
/* 166 */       this.backlog = backlog;
/* 167 */       this.delayAttachOnConnect = delayAttachOnConnect;
/* 168 */       this.identity = identity;
/* 169 */       this.ipv4Only = ipv4Only;
/* 170 */       this.linger = linger;
/* 171 */       this.maxMsgSize = maxMsgSize;
/* 172 */       this.rcvHwm = rcvHwm;
/* 173 */       this.receiveBufferSize = receiveBufferSize;
/* 174 */       this.receiveTimeOut = receiveTimeOut;
/* 175 */       this.reconnectIVL = reconnectIVL;
/* 176 */       this.reconnectIVLMax = reconnectIVLMax;
/* 177 */       this.sendBufferSize = sendBufferSize;
/* 178 */       this.sendTimeOut = sendTimeOut;
/* 179 */       this.sndHwm = sndHwm;
/* 180 */       this.tcpKeepAlive = tcpKeepAlive;
/* 181 */       this.tcpKeepAliveCount = tcpKeepAliveCount;
/* 182 */       this.tcpKeepAliveIdle = tcpKeepAliveIdle;
/* 183 */       this.tcpKeepAliveInterval = tcpKeepAliveInterval;
/* 184 */       this.xpubVerbose = xpubVerbose;
/* 185 */       this.endpoints = endpoints;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 190 */       return "JeroMqConfiguration{affinity=" + this.affinity + ", backlog=" + this.backlog + ", delayAttachOnConnect=" + this.delayAttachOnConnect + ", identity=" + Arrays.toString(this.identity) + ", ipv4Only=" + this.ipv4Only + ", linger=" + this.linger + ", maxMsgSize=" + this.maxMsgSize + ", rcvHwm=" + this.rcvHwm + ", receiveBufferSize=" + this.receiveBufferSize + ", receiveTimeOut=" + this.receiveTimeOut + ", reconnectIVL=" + this.reconnectIVL + ", reconnectIVLMax=" + this.reconnectIVLMax + ", sendBufferSize=" + this.sendBufferSize + ", sendTimeOut=" + this.sendTimeOut + ", sndHwm=" + this.sndHwm + ", tcpKeepAlive=" + this.tcpKeepAlive + ", tcpKeepAliveCount=" + this.tcpKeepAliveCount + ", tcpKeepAliveIdle=" + this.tcpKeepAliveIdle + ", tcpKeepAliveInterval=" + this.tcpKeepAliveInterval + ", xpubVerbose=" + this.xpubVerbose + ", endpoints=" + this.endpoints + '}';
/*     */     }
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
/*     */   private static class JeroMqManagerFactory
/*     */     implements ManagerFactory<JeroMqManager, JeroMqConfiguration>
/*     */   {
/*     */     private JeroMqManagerFactory() {}
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
/*     */     public JeroMqManager createManager(String name, JeroMqManager.JeroMqConfiguration data) {
/* 219 */       return new JeroMqManager(name, data);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\mom\jeromq\JeroMqManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */