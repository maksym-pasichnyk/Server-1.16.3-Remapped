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
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
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
/*     */ @Plugin(name = "MemoryMappedFile", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class MemoryMappedFileAppender
/*     */   extends AbstractOutputStreamAppender<MemoryMappedFileManager>
/*     */ {
/*     */   private static final int BIT_POSITION_1GB = 30;
/*     */   private static final int MAX_REGION_LENGTH = 1073741824;
/*     */   private static final int MIN_REGION_LENGTH = 256;
/*     */   private final String fileName;
/*     */   private Object advertisement;
/*     */   private final Advertiser advertiser;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<MemoryMappedFileAppender>
/*     */   {
/*     */     @PluginBuilderAttribute("fileName")
/*     */     private String fileName;
/*     */     @PluginBuilderAttribute("append")
/*     */     private boolean append = true;
/*     */     @PluginBuilderAttribute("regionLength")
/*  60 */     private int regionLength = 33554432;
/*     */ 
/*     */     
/*     */     @PluginBuilderAttribute("advertise")
/*     */     private boolean advertise;
/*     */     
/*     */     @PluginBuilderAttribute("advertiseURI")
/*     */     private String advertiseURI;
/*     */ 
/*     */     
/*     */     public MemoryMappedFileAppender build() {
/*  71 */       String name = getName();
/*  72 */       int actualRegionLength = MemoryMappedFileAppender.determineValidRegionLength(name, this.regionLength);
/*     */       
/*  74 */       if (name == null) {
/*  75 */         MemoryMappedFileAppender.LOGGER.error("No name provided for MemoryMappedFileAppender");
/*  76 */         return null;
/*     */       } 
/*     */       
/*  79 */       if (this.fileName == null) {
/*  80 */         MemoryMappedFileAppender.LOGGER.error("No filename provided for MemoryMappedFileAppender with name " + name);
/*  81 */         return null;
/*     */       } 
/*  83 */       Layout<? extends Serializable> layout = getOrCreateLayout();
/*  84 */       MemoryMappedFileManager manager = MemoryMappedFileManager.getFileManager(this.fileName, this.append, isImmediateFlush(), actualRegionLength, this.advertiseURI, layout);
/*     */       
/*  86 */       if (manager == null) {
/*  87 */         return null;
/*     */       }
/*     */       
/*  90 */       return new MemoryMappedFileAppender(name, layout, getFilter(), manager, this.fileName, isIgnoreExceptions(), false, this.advertise ? getConfiguration().getAdvertiser() : null);
/*     */     }
/*     */ 
/*     */     
/*     */     public B setFileName(String fileName) {
/*  95 */       this.fileName = fileName;
/*  96 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setAppend(boolean append) {
/* 100 */       this.append = append;
/* 101 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setRegionLength(int regionLength) {
/* 105 */       this.regionLength = regionLength;
/* 106 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setAdvertise(boolean advertise) {
/* 110 */       this.advertise = advertise;
/* 111 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setAdvertiseURI(String advertiseURI) {
/* 115 */       this.advertiseURI = advertiseURI;
/* 116 */       return (B)asBuilder();
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
/*     */   private MemoryMappedFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, MemoryMappedFileManager manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
/* 132 */     super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
/* 133 */     if (advertiser != null) {
/* 134 */       Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
/* 135 */       configuration.putAll(manager.getContentFormat());
/* 136 */       configuration.put("contentType", layout.getContentType());
/* 137 */       configuration.put("name", name);
/* 138 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } 
/* 140 */     this.fileName = filename;
/* 141 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 146 */     setStopping();
/* 147 */     stop(timeout, timeUnit, false);
/* 148 */     if (this.advertiser != null) {
/* 149 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/* 151 */     setStopped();
/* 152 */     return true;
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
/*     */   public void append(LogEvent event) {
/* 169 */     getManager().setEndOfBatch(event.isEndOfBatch());
/* 170 */     super.append(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 179 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRegionLength() {
/* 188 */     return getManager().getRegionLength();
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
/*     */   @Deprecated
/*     */   public static <B extends Builder<B>> MemoryMappedFileAppender createAppender(String fileName, String append, String name, String immediateFlush, String regionLengthStr, String ignore, Layout<? extends Serializable> layout, Filter filter, String advertise, String advertiseURI, Configuration config) {
/* 228 */     boolean isAppend = Booleans.parseBoolean(append, true);
/* 229 */     boolean isImmediateFlush = Booleans.parseBoolean(immediateFlush, false);
/* 230 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 231 */     boolean isAdvertise = Boolean.parseBoolean(advertise);
/* 232 */     int regionLength = Integers.parseInt(regionLengthStr, 33554432);
/*     */ 
/*     */     
/* 235 */     return ((Builder<B>)((Builder<Builder<B>>)((Builder<Builder<Builder<B>>>)((Builder<Builder<Builder<Builder<B>>>>)((Builder<Builder<Builder<Builder<Builder<B>>>>>)((Builder<B>)newBuilder().setAdvertise(isAdvertise).setAdvertiseURI(advertiseURI).setAppend(isAppend).setConfiguration(config)).setFileName(fileName).withFilter(filter)).withIgnoreExceptions(ignoreExceptions)).withImmediateFlush(isImmediateFlush)).withLayout(layout)).withName(name)).setRegionLength(regionLength).build();
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
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 253 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int determineValidRegionLength(String name, int regionLength) {
/* 260 */     if (regionLength > 1073741824) {
/* 261 */       LOGGER.info("MemoryMappedAppender[{}] Reduced region length from {} to max length: {}", name, Integer.valueOf(regionLength), Integer.valueOf(1073741824));
/*     */       
/* 263 */       return 1073741824;
/*     */     } 
/* 265 */     if (regionLength < 256) {
/* 266 */       LOGGER.info("MemoryMappedAppender[{}] Expanded region length from {} to min length: {}", name, Integer.valueOf(regionLength), Integer.valueOf(256));
/*     */       
/* 268 */       return 256;
/*     */     } 
/* 270 */     int result = Integers.ceilingNextPowerOfTwo(regionLength);
/* 271 */     if (regionLength != result) {
/* 272 */       LOGGER.info("MemoryMappedAppender[{}] Rounded up region length from {} to next power of two: {}", name, Integer.valueOf(regionLength), Integer.valueOf(result));
/*     */     }
/*     */     
/* 275 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\MemoryMappedFileAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */