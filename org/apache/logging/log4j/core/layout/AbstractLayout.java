/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.core.Layout;
/*     */ import org.apache.logging.log4j.core.LogEvent;
/*     */ import org.apache.logging.log4j.core.config.Configuration;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractLayout<T extends Serializable>
/*     */   implements Layout<T>
/*     */ {
/*     */   public static abstract class Builder<B extends Builder<B>>
/*     */   {
/*     */     @PluginConfiguration
/*     */     private Configuration configuration;
/*     */     @PluginBuilderAttribute
/*     */     private byte[] footer;
/*     */     @PluginBuilderAttribute
/*     */     private byte[] header;
/*     */     
/*     */     public B asBuilder() {
/*  58 */       return (B)this;
/*     */     }
/*     */     
/*     */     public Configuration getConfiguration() {
/*  62 */       return this.configuration;
/*     */     }
/*     */     
/*     */     public byte[] getFooter() {
/*  66 */       return this.footer;
/*     */     }
/*     */     
/*     */     public byte[] getHeader() {
/*  70 */       return this.header;
/*     */     }
/*     */     
/*     */     public B setConfiguration(Configuration configuration) {
/*  74 */       this.configuration = configuration;
/*  75 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setFooter(byte[] footer) {
/*  79 */       this.footer = footer;
/*  80 */       return asBuilder();
/*     */     }
/*     */     
/*     */     public B setHeader(byte[] header) {
/*  84 */       this.header = header;
/*  85 */       return asBuilder();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   protected static final Logger LOGGER = (Logger)StatusLogger.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Configuration configuration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long eventCount;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final byte[] footer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final byte[] header;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public AbstractLayout(byte[] header, byte[] footer) {
/* 126 */     this(null, header, footer);
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
/*     */   public AbstractLayout(Configuration configuration, byte[] header, byte[] footer) {
/* 141 */     this.configuration = configuration;
/* 142 */     this.header = header;
/* 143 */     this.footer = footer;
/*     */   }
/*     */   
/*     */   public Configuration getConfiguration() {
/* 147 */     return this.configuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getContentFormat() {
/* 152 */     return new HashMap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getFooter() {
/* 162 */     return this.footer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getHeader() {
/* 172 */     return this.header;
/*     */   }
/*     */   
/*     */   protected void markEvent() {
/* 176 */     this.eventCount++;
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
/*     */   public void encode(LogEvent event, ByteBufferDestination destination) {
/* 211 */     byte[] data = toByteArray(event);
/* 212 */     writeTo(data, 0, data.length, destination);
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
/*     */   public static void writeTo(byte[] data, int offset, int length, ByteBufferDestination destination) {
/* 224 */     int chunk = 0;
/* 225 */     synchronized (destination) {
/* 226 */       ByteBuffer buffer = destination.getByteBuffer();
/*     */       while (true) {
/* 228 */         if (length > buffer.remaining()) {
/* 229 */           buffer = destination.drain(buffer);
/*     */         }
/* 231 */         chunk = Math.min(length, buffer.remaining());
/* 232 */         buffer.put(data, offset, chunk);
/* 233 */         offset += chunk;
/* 234 */         length -= chunk;
/* 235 */         if (length <= 0)
/*     */           return; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\AbstractLayout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */