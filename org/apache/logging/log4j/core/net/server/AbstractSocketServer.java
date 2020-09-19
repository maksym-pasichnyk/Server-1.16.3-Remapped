/*     */ package org.apache.logging.log4j.core.net.server;
/*     */ 
/*     */ import com.beust.jcommander.Parameter;
/*     */ import com.beust.jcommander.validators.PositiveInteger;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.InetAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.Objects;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.LogEventListener;
/*     */ import org.apache.logging.log4j.core.LoggerContext;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.ConfigurationSource;
/*     */ import org.apache.logging.log4j.core.config.xml.XmlConfiguration;
/*     */ import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;
/*     */ import org.apache.logging.log4j.core.util.BasicCommandLineArguments;
/*     */ import org.apache.logging.log4j.core.util.InetAddressConverter;
/*     */ import org.apache.logging.log4j.core.util.Log4jThread;
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
/*     */ public abstract class AbstractSocketServer<T extends InputStream>
/*     */   extends LogEventListener
/*     */   implements Runnable
/*     */ {
/*     */   protected static final int MAX_PORT = 65534;
/*     */   
/*     */   protected static class CommandLineArguments
/*     */     extends BasicCommandLineArguments
/*     */   {
/*     */     @Parameter(names = {"--config", "-c"}, description = "Log4j configuration file location (path or URL).")
/*     */     private String configLocation;
/*     */     @Parameter(names = {"--interactive", "-i"}, description = "Accepts commands on standard input (\"exit\" is the only command).")
/*     */     private boolean interactive;
/*     */     @Parameter(names = {"--port", "-p"}, validateWith = PositiveInteger.class, description = "Server socket port.")
/*     */     private int port;
/*     */     @Parameter(names = {"--localbindaddress", "-a"}, converter = InetAddressConverter.class, description = "Server socket local bind address.")
/*     */     private InetAddress localBindAddress;
/*     */     
/*     */     String getConfigLocation() {
/*  74 */       return this.configLocation;
/*     */     }
/*     */     
/*     */     int getPort() {
/*  78 */       return this.port;
/*     */     }
/*     */     
/*     */     protected boolean isInteractive() {
/*  82 */       return this.interactive;
/*     */     }
/*     */     
/*     */     void setConfigLocation(String configLocation) {
/*  86 */       this.configLocation = configLocation;
/*     */     }
/*     */     
/*     */     void setInteractive(boolean interactive) {
/*  90 */       this.interactive = interactive;
/*     */     }
/*     */     
/*     */     void setPort(int port) {
/*  94 */       this.port = port;
/*     */     }
/*     */     
/*     */     InetAddress getLocalBindAddress() {
/*  98 */       return this.localBindAddress;
/*     */     }
/*     */     
/*     */     void setLocalBindAddress(InetAddress localBindAddress) {
/* 102 */       this.localBindAddress = localBindAddress;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected static class ServerConfigurationFactory
/*     */     extends XmlConfigurationFactory
/*     */   {
/*     */     private final String path;
/*     */ 
/*     */     
/*     */     public ServerConfigurationFactory(String path) {
/* 114 */       this.path = path;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Configuration getConfiguration(LoggerContext loggerContext, String name, URI configLocation) {
/* 120 */       if (Strings.isNotEmpty(this.path)) {
/* 121 */         File file = null;
/* 122 */         ConfigurationSource source = null;
/*     */         try {
/* 124 */           file = new File(this.path);
/* 125 */           FileInputStream is = new FileInputStream(file);
/* 126 */           source = new ConfigurationSource(is, file);
/* 127 */         } catch (FileNotFoundException fileNotFoundException) {}
/*     */ 
/*     */         
/* 130 */         if (source == null) {
/*     */           try {
/* 132 */             URL url = new URL(this.path);
/* 133 */             source = new ConfigurationSource(url.openStream(), url);
/* 134 */           } catch (IOException iOException) {}
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 140 */           if (source != null) {
/* 141 */             return (Configuration)new XmlConfiguration(loggerContext, source);
/*     */           }
/* 143 */         } catch (Exception exception) {}
/*     */ 
/*     */         
/* 146 */         System.err.println("Unable to process configuration at " + this.path + ", using default.");
/*     */       } 
/* 148 */       return super.getConfiguration(loggerContext, name, configLocation);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean active = true;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final LogEventBridge<T> logEventInput;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Logger logger;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractSocketServer(int port, LogEventBridge<T> logEventInput) {
/* 169 */     this.logger = LogManager.getLogger(getClass().getName() + '.' + port);
/* 170 */     this.logEventInput = Objects.<LogEventBridge<T>>requireNonNull(logEventInput, "LogEventInput");
/*     */   }
/*     */   
/*     */   protected boolean isActive() {
/* 174 */     return this.active;
/*     */   }
/*     */   
/*     */   protected void setActive(boolean isActive) {
/* 178 */     this.active = isActive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Thread startNewThread() {
/* 187 */     Log4jThread log4jThread = new Log4jThread(this);
/* 188 */     log4jThread.start();
/* 189 */     return (Thread)log4jThread;
/*     */   }
/*     */ 
/*     */   
/*     */   public void awaitTermination(Thread serverThread) throws Exception {
/*     */     String line;
/* 195 */     BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
/*     */     do {
/* 197 */       line = reader.readLine();
/* 198 */     } while (line != null && !line.equalsIgnoreCase("quit") && !line.equalsIgnoreCase("stop") && !line.equalsIgnoreCase("exit"));
/*     */ 
/*     */ 
/*     */     
/* 202 */     shutdown();
/* 203 */     serverThread.join();
/*     */   }
/*     */   
/*     */   public abstract void shutdown() throws Exception;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\net\server\AbstractSocketServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */