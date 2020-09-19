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
/*     */ import org.apache.logging.log4j.core.appender.rolling.DirectWriteRolloverStrategy;
/*     */ import org.apache.logging.log4j.core.appender.rolling.RollingFileManager;
/*     */ import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
/*     */ import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginElement;
/*     */ import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
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
/*     */ @Plugin(name = "RollingFile", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class RollingFileAppender
/*     */   extends AbstractOutputStreamAppender<RollingFileManager>
/*     */ {
/*     */   public static final String PLUGIN_NAME = "RollingFile";
/*     */   private static final int DEFAULT_BUFFER_SIZE = 8192;
/*     */   private final String fileName;
/*     */   private final String filePattern;
/*     */   private Object advertisement;
/*     */   private final Advertiser advertiser;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<RollingFileAppender>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private String fileName;
/*     */     @PluginBuilderAttribute
/*     */     @Required
/*     */     private String filePattern;
/*     */     @PluginBuilderAttribute
/*     */     private boolean append = true;
/*     */     @PluginBuilderAttribute
/*     */     private boolean locking;
/*     */     @PluginElement("Policy")
/*     */     @Required
/*     */     private TriggeringPolicy policy;
/*     */     @PluginElement("Strategy")
/*     */     private RolloverStrategy strategy;
/*     */     @PluginBuilderAttribute
/*     */     private boolean advertise;
/*     */     @PluginBuilderAttribute
/*     */     private String advertiseUri;
/*     */     @PluginBuilderAttribute
/*     */     private boolean createOnDemand;
/*     */     
/*     */     public RollingFileAppender build() {
/*  96 */       boolean isBufferedIo = isBufferedIo();
/*  97 */       int bufferSize = getBufferSize();
/*  98 */       if (getName() == null) {
/*  99 */         RollingFileAppender.LOGGER.error("RollingFileAppender '{}': No name provided.", getName());
/* 100 */         return null;
/*     */       } 
/*     */       
/* 103 */       if (!isBufferedIo && bufferSize > 0) {
/* 104 */         RollingFileAppender.LOGGER.warn("RollingFileAppender '{}': The bufferSize is set to {} but bufferedIO is not true", getName(), Integer.valueOf(bufferSize));
/*     */       }
/*     */       
/* 107 */       if (this.filePattern == null) {
/* 108 */         RollingFileAppender.LOGGER.error("RollingFileAppender '{}': No file name pattern provided.", getName());
/* 109 */         return null;
/*     */       } 
/*     */       
/* 112 */       if (this.policy == null) {
/* 113 */         RollingFileAppender.LOGGER.error("RollingFileAppender '{}': No TriggeringPolicy provided.", getName());
/* 114 */         return null;
/*     */       } 
/*     */       
/* 117 */       if (this.strategy == null) {
/* 118 */         if (this.fileName != null) {
/* 119 */           this.strategy = (RolloverStrategy)DefaultRolloverStrategy.createStrategy(null, null, null, String.valueOf(-1), null, true, getConfiguration());
/*     */         } else {
/*     */           
/* 122 */           this.strategy = (RolloverStrategy)DirectWriteRolloverStrategy.createStrategy(null, String.valueOf(-1), null, true, getConfiguration());
/*     */         }
/*     */       
/* 125 */       } else if (this.fileName == null && !(this.strategy instanceof org.apache.logging.log4j.core.appender.rolling.DirectFileRolloverStrategy)) {
/* 126 */         RollingFileAppender.LOGGER.error("RollingFileAppender '{}': When no file name is provided a DirectFilenameRolloverStrategy must be configured");
/* 127 */         return null;
/*     */       } 
/*     */       
/* 130 */       Layout<? extends Serializable> layout = getOrCreateLayout();
/* 131 */       RollingFileManager manager = RollingFileManager.getFileManager(this.fileName, this.filePattern, this.append, isBufferedIo, this.policy, this.strategy, this.advertiseUri, layout, bufferSize, isImmediateFlush(), this.createOnDemand, getConfiguration());
/*     */ 
/*     */       
/* 134 */       if (manager == null) {
/* 135 */         return null;
/*     */       }
/*     */       
/* 138 */       manager.initialize();
/*     */       
/* 140 */       return new RollingFileAppender(getName(), layout, getFilter(), manager, this.fileName, this.filePattern, isIgnoreExceptions(), isImmediateFlush(), this.advertise ? getConfiguration().getAdvertiser() : null);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getAdvertiseUri() {
/* 145 */       return this.advertiseUri;
/*     */     }
/*     */     
/*     */     public String getFileName() {
/* 149 */       return this.fileName;
/*     */     }
/*     */     
/*     */     public boolean isAdvertise() {
/* 153 */       return this.advertise;
/*     */     }
/*     */     
/*     */     public boolean isAppend() {
/* 157 */       return this.append;
/*     */     }
/*     */     
/*     */     public boolean isCreateOnDemand() {
/* 161 */       return this.createOnDemand;
/*     */     }
/*     */     
/*     */     public boolean isLocking() {
/* 165 */       return this.locking;
/*     */     }
/*     */     
/*     */     public B withAdvertise(boolean advertise) {
/* 169 */       this.advertise = advertise;
/* 170 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAdvertiseUri(String advertiseUri) {
/* 174 */       this.advertiseUri = advertiseUri;
/* 175 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAppend(boolean append) {
/* 179 */       this.append = append;
/* 180 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFileName(String fileName) {
/* 184 */       this.fileName = fileName;
/* 185 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withCreateOnDemand(boolean createOnDemand) {
/* 189 */       this.createOnDemand = createOnDemand;
/* 190 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withLocking(boolean locking) {
/* 194 */       this.locking = locking;
/* 195 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public String getFilePattern() {
/* 199 */       return this.filePattern;
/*     */     }
/*     */     
/*     */     public TriggeringPolicy getPolicy() {
/* 203 */       return this.policy;
/*     */     }
/*     */     
/*     */     public RolloverStrategy getStrategy() {
/* 207 */       return this.strategy;
/*     */     }
/*     */     
/*     */     public B withFilePattern(String filePattern) {
/* 211 */       this.filePattern = filePattern;
/* 212 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withPolicy(TriggeringPolicy policy) {
/* 216 */       this.policy = policy;
/* 217 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withStrategy(RolloverStrategy strategy) {
/* 221 */       this.strategy = strategy;
/* 222 */       return (B)asBuilder();
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
/*     */   private RollingFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, RollingFileManager manager, String fileName, String filePattern, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
/* 237 */     super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
/* 238 */     if (advertiser != null) {
/* 239 */       Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
/* 240 */       configuration.put("contentType", layout.getContentType());
/* 241 */       configuration.put("name", name);
/* 242 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } 
/* 244 */     this.fileName = fileName;
/* 245 */     this.filePattern = filePattern;
/* 246 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 251 */     setStopping();
/* 252 */     boolean stopped = stop(timeout, timeUnit, false);
/* 253 */     if (this.advertiser != null) {
/* 254 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/* 256 */     setStopped();
/* 257 */     return stopped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void append(LogEvent event) {
/* 267 */     getManager().checkRollover(event);
/* 268 */     super.append(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 276 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilePattern() {
/* 284 */     return this.filePattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends TriggeringPolicy> T getTriggeringPolicy() {
/* 293 */     return (T)getManager().getTriggeringPolicy();
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
/*     */   @Deprecated
/*     */   public static <B extends Builder<B>> RollingFileAppender createAppender(String fileName, String filePattern, String append, String name, String bufferedIO, String bufferSizeStr, String immediateFlush, TriggeringPolicy policy, RolloverStrategy strategy, Layout<? extends Serializable> layout, Filter filter, String ignore, String advertise, String advertiseUri, Configuration config) {
/* 337 */     int bufferSize = Integers.parseInt(bufferSizeStr, 8192);
/*     */     
/* 339 */     return ((Builder<B>)((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)((Builder<Builder<Builder<Builder<B>>>>)((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)newBuilder().withAdvertise(Boolean.parseBoolean(advertise)).withAdvertiseUri(advertiseUri).withAppend(Booleans.parseBoolean(append, true)).withBufferedIo(Booleans.parseBoolean(bufferedIO, true))).withBufferSize(bufferSize)).setConfiguration(config)).withFileName(fileName).withFilePattern(filePattern).withFilter(filter)).withIgnoreExceptions(Booleans.parseBoolean(ignore, true))).withImmediateFlush(Booleans.parseBoolean(immediateFlush, true))).withLayout(layout)).withCreateOnDemand(false).withLocking(false).withName(name)).withPolicy(policy).withStrategy(strategy).build();
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
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 363 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\RollingFileAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */