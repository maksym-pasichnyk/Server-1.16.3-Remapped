/*     */ package org.apache.logging.log4j.core.appender;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Filter;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
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
/*     */ @Plugin(name = "File", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class FileAppender
/*     */   extends AbstractOutputStreamAppender<FileManager>
/*     */ {
/*     */   public static final String PLUGIN_NAME = "File";
/*     */   private static final int DEFAULT_BUFFER_SIZE = 8192;
/*     */   private final String fileName;
/*     */   private final Advertiser advertiser;
/*     */   private final Object advertisement;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<FileAppender>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     @Required
/*     */     private String fileName;
/*     */     @PluginBuilderAttribute
/*     */     private boolean append = true;
/*     */     @PluginBuilderAttribute
/*     */     private boolean locking;
/*     */     @PluginBuilderAttribute
/*     */     private boolean advertise;
/*     */     @PluginBuilderAttribute
/*     */     private String advertiseUri;
/*     */     @PluginBuilderAttribute
/*     */     private boolean createOnDemand;
/*     */     
/*     */     public FileAppender build() {
/*  75 */       boolean bufferedIo = isBufferedIo();
/*  76 */       int bufferSize = getBufferSize();
/*  77 */       if (this.locking && bufferedIo) {
/*  78 */         FileAppender.LOGGER.warn("Locking and buffering are mutually exclusive. No buffering will occur for {}", this.fileName);
/*  79 */         bufferedIo = false;
/*     */       } 
/*  81 */       if (!bufferedIo && bufferSize > 0) {
/*  82 */         FileAppender.LOGGER.warn("The bufferSize is set to {} but bufferedIo is false: {}", Integer.valueOf(bufferSize), Boolean.valueOf(bufferedIo));
/*     */       }
/*  84 */       Layout<? extends Serializable> layout = getOrCreateLayout();
/*     */       
/*  86 */       FileManager manager = FileManager.getFileManager(this.fileName, this.append, this.locking, bufferedIo, this.createOnDemand, this.advertiseUri, layout, bufferSize, getConfiguration());
/*     */       
/*  88 */       if (manager == null) {
/*  89 */         return null;
/*     */       }
/*     */       
/*  92 */       return new FileAppender(getName(), layout, getFilter(), manager, this.fileName, isIgnoreExceptions(), (!bufferedIo || isImmediateFlush()), this.advertise ? getConfiguration().getAdvertiser() : null);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getAdvertiseUri() {
/*  97 */       return this.advertiseUri;
/*     */     }
/*     */     
/*     */     public String getFileName() {
/* 101 */       return this.fileName;
/*     */     }
/*     */     
/*     */     public boolean isAdvertise() {
/* 105 */       return this.advertise;
/*     */     }
/*     */     
/*     */     public boolean isAppend() {
/* 109 */       return this.append;
/*     */     }
/*     */     
/*     */     public boolean isCreateOnDemand() {
/* 113 */       return this.createOnDemand;
/*     */     }
/*     */     
/*     */     public boolean isLocking() {
/* 117 */       return this.locking;
/*     */     }
/*     */     
/*     */     public B withAdvertise(boolean advertise) {
/* 121 */       this.advertise = advertise;
/* 122 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAdvertiseUri(String advertiseUri) {
/* 126 */       this.advertiseUri = advertiseUri;
/* 127 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withAppend(boolean append) {
/* 131 */       this.append = append;
/* 132 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withFileName(String fileName) {
/* 136 */       this.fileName = fileName;
/* 137 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withCreateOnDemand(boolean createOnDemand) {
/* 141 */       this.createOnDemand = createOnDemand;
/* 142 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B withLocking(boolean locking) {
/* 146 */       this.locking = locking;
/* 147 */       return (B)asBuilder();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static <B extends Builder<B>> FileAppender createAppender(String fileName, String append, String locking, String name, String immediateFlush, String ignoreExceptions, String bufferedIo, String bufferSizeStr, Layout<? extends Serializable> layout, Filter filter, String advertise, String advertiseUri, Configuration config) {
/* 192 */     return ((Builder)((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)((Builder<Builder<Builder<Builder<B>>>>)((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)newBuilder().withAdvertise(Boolean.parseBoolean(advertise)).withAdvertiseUri(advertiseUri).withAppend(Booleans.parseBoolean(append, true)).withBufferedIo(Booleans.parseBoolean(bufferedIo, true))).withBufferSize(Integers.parseInt(bufferSizeStr, 8192))).setConfiguration(config)).withFileName(fileName).withFilter(filter)).withIgnoreExceptions(Booleans.parseBoolean(ignoreExceptions, true))).withImmediateFlush(Booleans.parseBoolean(immediateFlush, true))).withLayout(layout)).withLocking(Boolean.parseBoolean(locking)).withName(name)).build();
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
/* 212 */     return (B)(new Builder<>()).asBuilder();
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
/*     */   private FileAppender(String name, Layout<? extends Serializable> layout, Filter filter, FileManager manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
/* 225 */     super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
/* 226 */     if (advertiser != null) {
/* 227 */       Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
/* 228 */       configuration.putAll(manager.getContentFormat());
/* 229 */       configuration.put("contentType", layout.getContentType());
/* 230 */       configuration.put("name", name);
/* 231 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } else {
/* 233 */       this.advertisement = null;
/*     */     } 
/* 235 */     this.fileName = filename;
/* 236 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 244 */     return this.fileName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 249 */     setStopping();
/* 250 */     stop(timeout, timeUnit, false);
/* 251 */     if (this.advertiser != null) {
/* 252 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/* 254 */     setStopped();
/* 255 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\FileAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */