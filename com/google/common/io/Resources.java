/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class Resources
/*     */ {
/*     */   public static ByteSource asByteSource(URL url) {
/*  56 */     return new UrlByteSource(url);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class UrlByteSource
/*     */     extends ByteSource
/*     */   {
/*     */     private final URL url;
/*     */ 
/*     */     
/*     */     private UrlByteSource(URL url) {
/*  67 */       this.url = (URL)Preconditions.checkNotNull(url);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/*  72 */       return this.url.openStream();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  77 */       return "Resources.asByteSource(" + this.url + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource asCharSource(URL url, Charset charset) {
/*  87 */     return asByteSource(url).asCharSource(charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] toByteArray(URL url) throws IOException {
/*  98 */     return asByteSource(url).read();
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
/*     */   public static String toString(URL url, Charset charset) throws IOException {
/* 111 */     return asCharSource(url, charset).read();
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
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readLines(URL url, Charset charset, LineProcessor<T> callback) throws IOException {
/* 128 */     return asCharSource(url, charset).readLines(callback);
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
/*     */   public static List<String> readLines(URL url, Charset charset) throws IOException {
/* 147 */     return readLines(url, charset, new LineProcessor<List<String>>()
/*     */         {
/*     */ 
/*     */           
/* 151 */           final List<String> result = Lists.newArrayList();
/*     */ 
/*     */           
/*     */           public boolean processLine(String line) {
/* 155 */             this.result.add(line);
/* 156 */             return true;
/*     */           }
/*     */ 
/*     */           
/*     */           public List<String> getResult() {
/* 161 */             return this.result;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copy(URL from, OutputStream to) throws IOException {
/* 174 */     asByteSource(from).copyTo(to);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static URL getResource(String resourceName) {
/* 194 */     ClassLoader loader = (ClassLoader)MoreObjects.firstNonNull(
/* 195 */         Thread.currentThread().getContextClassLoader(), Resources.class.getClassLoader());
/* 196 */     URL url = loader.getResource(resourceName);
/* 197 */     Preconditions.checkArgument((url != null), "resource %s not found.", resourceName);
/* 198 */     return url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL getResource(Class<?> contextClass, String resourceName) {
/* 208 */     URL url = contextClass.getResource(resourceName);
/* 209 */     Preconditions.checkArgument((url != null), "resource %s relative to %s not found.", resourceName, contextClass
/* 210 */         .getName());
/* 211 */     return url;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\Resources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */