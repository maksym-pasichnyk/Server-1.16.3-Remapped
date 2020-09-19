/*     */ package org.apache.logging.log4j.core.config.status;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.core.util.FileUtils;
/*     */ import org.apache.logging.log4j.core.util.NetUtils;
/*     */ import org.apache.logging.log4j.status.StatusConsoleListener;
/*     */ import org.apache.logging.log4j.status.StatusListener;
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
/*     */ public class StatusConfiguration
/*     */ {
/*  43 */   private static final PrintStream DEFAULT_STREAM = System.out;
/*  44 */   private static final Level DEFAULT_STATUS = Level.ERROR;
/*  45 */   private static final Verbosity DEFAULT_VERBOSITY = Verbosity.QUIET;
/*     */   
/*  47 */   private final Collection<String> errorMessages = Collections.synchronizedCollection(new LinkedList<>());
/*  48 */   private final StatusLogger logger = StatusLogger.getLogger();
/*     */   
/*     */   private volatile boolean initialized = false;
/*     */   
/*  52 */   private PrintStream destination = DEFAULT_STREAM;
/*  53 */   private Level status = DEFAULT_STATUS;
/*  54 */   private Verbosity verbosity = DEFAULT_VERBOSITY;
/*     */   
/*     */   private String[] verboseClasses;
/*     */ 
/*     */   
/*     */   public enum Verbosity
/*     */   {
/*  61 */     QUIET, VERBOSE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Verbosity toVerbosity(String value) {
/*  70 */       return Boolean.parseBoolean(value) ? VERBOSE : QUIET;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String message) {
/*  81 */     if (!this.initialized) {
/*  82 */       this.errorMessages.add(message);
/*     */     } else {
/*  84 */       this.logger.error(message);
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
/*     */   public StatusConfiguration withDestination(String destination) {
/*     */     try {
/*  99 */       this.destination = parseStreamName(destination);
/* 100 */     } catch (URISyntaxException e) {
/* 101 */       error("Could not parse URI [" + destination + "]. Falling back to default of stdout.");
/* 102 */       this.destination = DEFAULT_STREAM;
/* 103 */     } catch (FileNotFoundException e) {
/* 104 */       error("File could not be found at [" + destination + "]. Falling back to default of stdout.");
/* 105 */       this.destination = DEFAULT_STREAM;
/*     */     } 
/* 107 */     return this;
/*     */   }
/*     */   
/*     */   private PrintStream parseStreamName(String name) throws URISyntaxException, FileNotFoundException {
/* 111 */     if (name == null || name.equalsIgnoreCase("out")) {
/* 112 */       return DEFAULT_STREAM;
/*     */     }
/* 114 */     if (name.equalsIgnoreCase("err")) {
/* 115 */       return System.err;
/*     */     }
/* 117 */     URI destUri = NetUtils.toURI(name);
/* 118 */     File output = FileUtils.fileFromUri(destUri);
/* 119 */     if (output == null)
/*     */     {
/* 121 */       return DEFAULT_STREAM;
/*     */     }
/* 123 */     FileOutputStream fos = new FileOutputStream(output);
/* 124 */     return new PrintStream(fos, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StatusConfiguration withStatus(String status) {
/* 135 */     this.status = Level.toLevel(status, null);
/* 136 */     if (this.status == null) {
/* 137 */       error("Invalid status level specified: " + status + ". Defaulting to ERROR.");
/* 138 */       this.status = Level.ERROR;
/*     */     } 
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StatusConfiguration withStatus(Level status) {
/* 150 */     this.status = status;
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StatusConfiguration withVerbosity(String verbosity) {
/* 162 */     this.verbosity = Verbosity.toVerbosity(verbosity);
/* 163 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StatusConfiguration withVerboseClasses(String... verboseClasses) {
/* 173 */     this.verboseClasses = verboseClasses;
/* 174 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize() {
/* 181 */     if (!this.initialized) {
/* 182 */       if (this.status == Level.OFF) {
/* 183 */         this.initialized = true;
/*     */       } else {
/* 185 */         boolean configured = configureExistingStatusConsoleListener();
/* 186 */         if (!configured) {
/* 187 */           registerNewStatusConsoleListener();
/*     */         }
/* 189 */         migrateSavedLogMessages();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean configureExistingStatusConsoleListener() {
/* 195 */     boolean configured = false;
/* 196 */     for (StatusListener statusListener : this.logger.getListeners()) {
/* 197 */       if (statusListener instanceof StatusConsoleListener) {
/* 198 */         StatusConsoleListener listener = (StatusConsoleListener)statusListener;
/* 199 */         listener.setLevel(this.status);
/* 200 */         this.logger.updateListenerLevel(this.status);
/* 201 */         if (this.verbosity == Verbosity.QUIET) {
/* 202 */           listener.setFilters(this.verboseClasses);
/*     */         }
/* 204 */         configured = true;
/*     */       } 
/*     */     } 
/* 207 */     return configured;
/*     */   }
/*     */ 
/*     */   
/*     */   private void registerNewStatusConsoleListener() {
/* 212 */     StatusConsoleListener listener = new StatusConsoleListener(this.status, this.destination);
/* 213 */     if (this.verbosity == Verbosity.QUIET) {
/* 214 */       listener.setFilters(this.verboseClasses);
/*     */     }
/* 216 */     this.logger.registerListener((StatusListener)listener);
/*     */   }
/*     */   
/*     */   private void migrateSavedLogMessages() {
/* 220 */     for (String message : this.errorMessages) {
/* 221 */       this.logger.error(message);
/*     */     }
/* 223 */     this.initialized = true;
/* 224 */     this.errorMessages.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\status\StatusConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */