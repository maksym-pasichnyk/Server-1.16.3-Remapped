/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Closeables
/*     */ {
/*     */   @VisibleForTesting
/*  37 */   static final Logger logger = Logger.getLogger(Closeables.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void close(@Nullable Closeable closeable, boolean swallowIOException) throws IOException {
/*  72 */     if (closeable == null) {
/*     */       return;
/*     */     }
/*     */     try {
/*  76 */       closeable.close();
/*  77 */     } catch (IOException e) {
/*  78 */       if (swallowIOException) {
/*  79 */         logger.log(Level.WARNING, "IOException thrown while closing Closeable.", e);
/*     */       } else {
/*  81 */         throw e;
/*     */       } 
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
/*     */   public static void closeQuietly(@Nullable InputStream inputStream) {
/*     */     try {
/* 102 */       close(inputStream, true);
/* 103 */     } catch (IOException impossible) {
/* 104 */       throw new AssertionError(impossible);
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
/*     */   public static void closeQuietly(@Nullable Reader reader) {
/*     */     try {
/* 123 */       close(reader, true);
/* 124 */     } catch (IOException impossible) {
/* 125 */       throw new AssertionError(impossible);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\io\Closeables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */