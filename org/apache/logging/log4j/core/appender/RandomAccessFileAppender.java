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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "RandomAccessFile", category = "Core", elementType = "appender", printObject = true)
/*     */ public final class RandomAccessFileAppender
/*     */   extends AbstractOutputStreamAppender<RandomAccessFileManager>
/*     */ {
/*     */   private final String fileName;
/*     */   private Object advertisement;
/*     */   private final Advertiser advertiser;
/*     */   
/*     */   public static class Builder<B extends Builder<B>>
/*     */     extends AbstractOutputStreamAppender.Builder<B>
/*     */     implements org.apache.logging.log4j.core.util.Builder<RandomAccessFileAppender>
/*     */   {
/*     */     @PluginBuilderAttribute("fileName")
/*     */     private String fileName;
/*     */     @PluginBuilderAttribute("append")
/*     */     private boolean append;
/*     */     @PluginBuilderAttribute("advertise")
/*     */     private boolean advertise;
/*     */     @PluginBuilderAttribute("advertiseURI")
/*     */     private String advertiseURI;
/*     */     
/*     */     public RandomAccessFileAppender build() {
/*  66 */       String name = getName();
/*  67 */       if (name == null) {
/*  68 */         RandomAccessFileAppender.LOGGER.error("No name provided for FileAppender");
/*  69 */         return null;
/*     */       } 
/*     */       
/*  72 */       if (this.fileName == null) {
/*  73 */         RandomAccessFileAppender.LOGGER.error("No filename provided for FileAppender with name " + name);
/*  74 */         return null;
/*     */       } 
/*  76 */       Layout<? extends Serializable> layout = getOrCreateLayout();
/*  77 */       boolean immediateFlush = isImmediateFlush();
/*  78 */       RandomAccessFileManager manager = RandomAccessFileManager.getFileManager(this.fileName, this.append, immediateFlush, getBufferSize(), this.advertiseURI, layout, null);
/*     */       
/*  80 */       if (manager == null) {
/*  81 */         return null;
/*     */       }
/*     */       
/*  84 */       return new RandomAccessFileAppender(name, layout, getFilter(), manager, this.fileName, isIgnoreExceptions(), immediateFlush, this.advertise ? getConfiguration().getAdvertiser() : null);
/*     */     }
/*     */ 
/*     */     
/*     */     public B setFileName(String fileName) {
/*  89 */       this.fileName = fileName;
/*  90 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setAppend(boolean append) {
/*  94 */       this.append = append;
/*  95 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setAdvertise(boolean advertise) {
/*  99 */       this.advertise = advertise;
/* 100 */       return (B)asBuilder();
/*     */     }
/*     */     
/*     */     public B setAdvertiseURI(String advertiseURI) {
/* 104 */       this.advertiseURI = advertiseURI;
/* 105 */       return (B)asBuilder();
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
/*     */   private RandomAccessFileAppender(String name, Layout<? extends Serializable> layout, Filter filter, RandomAccessFileManager manager, String filename, boolean ignoreExceptions, boolean immediateFlush, Advertiser advertiser) {
/* 118 */     super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
/* 119 */     if (advertiser != null) {
/* 120 */       Map<String, String> configuration = new HashMap<>(layout.getContentFormat());
/*     */       
/* 122 */       configuration.putAll(manager.getContentFormat());
/* 123 */       configuration.put("contentType", layout.getContentType());
/* 124 */       configuration.put("name", name);
/* 125 */       this.advertisement = advertiser.advertise(configuration);
/*     */     } 
/* 127 */     this.fileName = filename;
/* 128 */     this.advertiser = advertiser;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean stop(long timeout, TimeUnit timeUnit) {
/* 133 */     setStopping();
/* 134 */     stop(timeout, timeUnit, false);
/* 135 */     if (this.advertiser != null) {
/* 136 */       this.advertiser.unadvertise(this.advertisement);
/*     */     }
/* 138 */     setStopped();
/* 139 */     return true;
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
/* 156 */     getManager().setEndOfBatch(event.isEndOfBatch());
/*     */ 
/*     */     
/* 159 */     super.append(event);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 168 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 176 */     return getManager().getBufferSize();
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
/*     */   @Deprecated
/*     */   public static <B extends Builder<B>> RandomAccessFileAppender createAppender(String fileName, String append, String name, String immediateFlush, String bufferSizeStr, String ignore, Layout<? extends Serializable> layout, Filter filter, String advertise, String advertiseURI, Configuration configuration) {
/* 218 */     boolean isAppend = Booleans.parseBoolean(append, true);
/* 219 */     boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
/* 220 */     boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
/* 221 */     boolean isAdvertise = Boolean.parseBoolean(advertise);
/* 222 */     int bufferSize = Integers.parseInt(bufferSizeStr, 262144);
/*     */     
/* 224 */     return ((Builder)((Builder<Builder>)((Builder<Builder<Builder>>)((Builder<Builder<Builder<Builder>>>)((Builder<Builder<Builder<Builder<Builder>>>>)((Builder<B>)((Builder<Builder<B>>)newBuilder().setAdvertise(isAdvertise).setAdvertiseURI(advertiseURI).setAppend(isAppend).withBufferSize(bufferSize)).setConfiguration(configuration)).setFileName(fileName).withFilter(filter)).withIgnoreExceptions(ignoreExceptions)).withImmediateFlush(isFlush)).withLayout(layout)).withName(name)).build();
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
/*     */   @PluginBuilderFactory
/*     */   public static <B extends Builder<B>> B newBuilder() {
/* 245 */     return (B)(new Builder<>()).asBuilder();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\appender\RandomAccessFileAppender.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */