/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
/*     */ import org.apache.logging.log4j.core.appender.rolling.RollingRandomAccessFileManager;
/*     */ import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
/*     */ import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.net.Advertiser;
/*     */ import org.apache.logging.log4j.core.util.Booleans;
/*     */ import org.apache.logging.log4j.core.util.Integers;
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
/*     */ @Plugin(name = "RollingRandomAccessFile", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class RollingRandomAccessFileAppender
/*     */   extends AbstractOutputStreamAppender<RollingRandomAccessFileManager>
/*     */ {
/*     */   private final String fileName;
/*     */   private final String filePattern;
/*     */   private final Object advertisement;
/*     */   private final Advertiser advertiser;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<RollingRandomAccessFileAppender>
/*     */   {
/*     */     @PluginBuilderAttribute("fileName")
/*     */     private String fileName;
/*     */     @PluginBuilderAttribute("filePattern")
/*     */     private String filePattern;
/*     */     @PluginBuilderAttribute("append")
/*     */     private boolean append;
/*     */     @PluginElement("Policy")
/*     */     private TriggeringPolicy policy;
/*     */     @PluginElement("Strategy")
/*     */     private RolloverStrategy strategy;
/*     */     @PluginBuilderAttribute("advertise")
/*     */     private boolean advertise;
/*     */     @PluginBuilderAttribute("advertiseURI")
/*     */     private String advertiseURI;
/*     */     
/*     */     public Builder() {
/*  68 */       this.append = true;
/*     */       withBufferSize(262144);
/*     */       withIgnoreExceptions(true);
/*     */       withImmediateFlush(true);
/*     */     }
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
/*     */     public RollingRandomAccessFileAppender build() {
/*  85 */       String name = getName();
/*  86 */       if (name == null) {
/*  87 */         RollingRandomAccessFileAppender.LOGGER.error("No name provided for FileAppender");
/*  88 */         return null;
/*     */       } 
/*     */       
/*  91 */       if (this.fileName == null) {
/*  92 */         RollingRandomAccessFileAppender.LOGGER.error("No filename was provided for FileAppender with name " + name);
/*  93 */         return null;
/*     */       } 
/*     */       
/*  96 */       if (this.filePattern == null) {
/*  97 */         RollingRandomAccessFileAppender.LOGGER.error("No filename pattern provided for FileAppender with name " + name);
/*  98 */         return null;
/*     */       } 
/*     */       
/* 101 */       if (this.policy == null) {
/* 102 */         RollingRandomAccessFileAppender.LOGGER.error("A TriggeringPolicy must be provided");
/* 103 */         return null;
/*     */       } 
/*     */       
/* 106 */       if (this.strategy == null) {
/* 107 */         this.strategy = (RolloverStrategy)DefaultRolloverStrategy.createStrategy(null, null, null, String.valueOf(-1), null, true, getConfiguration());
/*     */       }
/*     */ 
/*     */       
/* 111 */       Layout<? extends Serializable> layout = getOrCreateLayout();
/*     */       
/* 113 */       boolean immediateFlush = isImmediateFlush();
/* 114 */       int bufferSize = getBufferSize();
/* 115 */       RollingRandomAccessFileManager manager = RollingRandomAccessFileManager.getRollingRandomAccessFileManager(this.fileName, this.filePattern, this.append, immediateFlush, bufferSize, this.policy, this.strategy, this.advertiseURI, layout, getConfiguration());
/*     */ 
/*     */       
/* 118 */       if (manager == null) {
/* 119 */         return null;
/*     */       }
/*     */       
/* 122 */       manager.initialize();
/*     */       
/* 124 */       return new RollingRandomAccessFileAppender(name, layout, getFilter(), manager, this.fileName, this.filePattern, isIgnoreExceptions(), immediateFlush, bufferSize, this.advertise ? getConfiguration().getAdvertiser() : null);
/*     */     }
/*     */ 
/*     */     
/*     */     public B withFileName(String fileName) {
/* 129 */       this.fileName = fileName;
/* 130 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFilePattern(String filePattern) {
/* 134 */       this.filePattern = filePattern;
/* 135 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAppend(boolean append) {
/* 139 */       this.append = append;
/* 140 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withPolicy(TriggeringPolicy policy) {
/* 144 */       this.policy = policy;
/* 145 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withStrategy(RolloverStrategy strategy) {
/* 149 */       this.strategy = strategy;
/* 150 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAdvertise(boolean advertise) {
/* 154 */       this.advertise = advertise;
/* 155 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAdvertiseURI(String advertiseURI) {
/* 159 */       this.advertiseURI = advertiseURI;
/* 160 */       return (B)asBuilder();
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
/*     */   private RollingRandomAccessFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, RollingRandomAccessFileManager manager, String fileName, String filePattern, boolean ignoreExceptions, boolean immediateFlush, int bufferSize, Advertiser advertiser) {
/* 174 */     super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
/* 175 */     if (advertiser != null) {
/* 176 */       Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
/* 177 */       configuration.put("contentType", layout.getContentType());
/* 178 */       configuration.put("name", name);
/* 179 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } else {
/* 181 */       this.advertisement = null;
/*     */     } 
/* 183 */     this.fileName = fileName;
/* 184 */     this.filePattern = filePattern;
/* 185 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 190 */     setStopping();
/* 191 */     stop(timeout, timeUnit, false);
/* 192 */     if (this.advertiser != null) {
/* 193 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/* 195 */     setStopped();
/* 196 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/* 206 */     RollingRandomAccessFileManager manager = getManager();
/* 207 */     manager.checkRollover(event);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     manager.setEndOfBatch(event.isEndOfBatch());
/*     */ 
/*     */     
/* 218 */     super.append(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 227 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilePattern() {
/* 236 */     return this.filePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 244 */     return getManager().getBufferSize();
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
/*     */   @Deprecated
/*     */   public static <B extends Builder<B>> RollingRandomAccessFileAppender createAppender(String fileName, String filePattern, String append, String name, String immediateFlush, String bufferSizeStr, TriggeringPolicy policy, RolloverStrategy strategy, Layout<? extends Serializable> layout, Filter filter, String ignoreExceptions, String advertise, String advertiseURI, Configuration configuration) {
/* 292 */     boolean isAppend = Booleans.parseBoolean(append, true);
/* 293 */     boolean isIgnoreExceptions = Booleans.parseBoolean(ignoreExceptions, true);
/* 294 */     boolean isImmediateFlush = Booleans.parseBoolean(immediateFlush, true);
/* 295 */     boolean isAdvertise = Boolean.parseBoolean(advertise);
/* 296 */     int bufferSize = Integers.parseInt(bufferSizeStr, 262144);
/*     */     
/* 298 */     return ((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)((Builder<Builder<Builder<Builder<B>>>>)((Builder<Builder<Builder<Builder<Builder<B>>>>>)((Builder<B>)((Builder<Builder<B>>)newBuilder().withAdvertise(isAdvertise).withAdvertiseURI(advertiseURI).withAppend(isAppend).withBufferSize(bufferSize)).setConfiguration(configuration)).withFileName(fileName).withFilePattern(filePattern).withFilter(filter)).withIgnoreExceptions(isIgnoreExceptions)).withImmediateFlush(isImmediateFlush)).withLayout(layout)).withName(name)).withPolicy(policy).withStrategy(strategy).build();
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
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 318 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\RollingRandomAccessFileAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */