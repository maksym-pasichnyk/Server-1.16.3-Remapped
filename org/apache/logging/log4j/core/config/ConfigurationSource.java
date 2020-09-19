/*     */ package org.apache.logging.log4j.core.config;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigurationSource
/*     */ {
/*  38 */   public static final ConfigurationSource NULL_SOURCE = new ConfigurationSource(new byte[0]);
/*     */ 
/*     */   
/*     */   private final File file;
/*     */ 
/*     */   
/*     */   private final URL url;
/*     */   
/*     */   private final String location;
/*     */   
/*     */   private final InputStream stream;
/*     */   
/*     */   private final byte[] data;
/*     */ 
/*     */   
/*     */   public ConfigurationSource(InputStream stream, File file) {
/*  54 */     this.stream = Objects.<InputStream>requireNonNull(stream, "stream is null");
/*  55 */     this.file = Objects.<File>requireNonNull(file, "file is null");
/*  56 */     this.location = file.getAbsolutePath();
/*  57 */     this.url = null;
/*  58 */     this.data = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationSource(InputStream stream, URL url) {
/*  69 */     this.stream = Objects.<InputStream>requireNonNull(stream, "stream is null");
/*  70 */     this.url = Objects.<URL>requireNonNull(url, "URL is null");
/*  71 */     this.location = url.toString();
/*  72 */     this.file = null;
/*  73 */     this.data = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationSource(InputStream stream) throws IOException {
/*  84 */     this(toByteArray(stream));
/*     */   }
/*     */   
/*     */   private ConfigurationSource(byte[] data) {
/*  88 */     this.data = Objects.<byte[]>requireNonNull(data, "data is null");
/*  89 */     this.stream = new ByteArrayInputStream(data);
/*  90 */     this.file = null;
/*  91 */     this.url = null;
/*  92 */     this.location = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] toByteArray(InputStream inputStream) throws IOException {
/* 103 */     int buffSize = Math.max(4096, inputStream.available());
/* 104 */     ByteArrayOutputStream contents = new ByteArrayOutputStream(buffSize);
/* 105 */     byte[] buff = new byte[buffSize];
/*     */     
/* 107 */     int length = inputStream.read(buff);
/* 108 */     while (length > 0) {
/* 109 */       contents.write(buff, 0, length);
/* 110 */       length = inputStream.read(buff);
/*     */     } 
/* 112 */     return contents.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 122 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getURL() {
/* 132 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() {
/* 140 */     URI sourceURI = null;
/* 141 */     if (this.url != null) {
/*     */       try {
/* 143 */         sourceURI = this.url.toURI();
/* 144 */       } catch (URISyntaxException uRISyntaxException) {}
/*     */     }
/*     */ 
/*     */     
/* 148 */     if (sourceURI == null && this.file != null) {
/* 149 */       sourceURI = this.file.toURI();
/*     */     }
/* 151 */     if (sourceURI == null && this.location != null) {
/*     */       try {
/* 153 */         sourceURI = new URI(this.location);
/* 154 */       } catch (URISyntaxException ex) {
/*     */         
/*     */         try {
/* 157 */           sourceURI = new URI("file://" + this.location);
/* 158 */         } catch (URISyntaxException uRISyntaxException) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 163 */     return sourceURI;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLocation() {
/* 173 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() {
/* 182 */     return this.stream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationSource resetInputStream() throws IOException {
/* 192 */     if (this.file != null)
/* 193 */       return new ConfigurationSource(new FileInputStream(this.file), this.file); 
/* 194 */     if (this.url != null) {
/* 195 */       return new ConfigurationSource(this.url.openStream(), this.url);
/*     */     }
/* 197 */     return new ConfigurationSource(this.data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 203 */     if (this.location != null) {
/* 204 */       return this.location;
/*     */     }
/* 206 */     if (this == NULL_SOURCE) {
/* 207 */       return "NULL_SOURCE";
/*     */     }
/* 209 */     int length = (this.data == null) ? -1 : this.data.length;
/* 210 */     return "stream (" + length + " bytes, unknown location)";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\ConfigurationSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */