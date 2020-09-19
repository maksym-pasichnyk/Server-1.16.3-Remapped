/*     */ package org.apache.logging.log4j.core.impl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.core.pattern.PlainTextRenderer;
/*     */ import org.apache.logging.log4j.core.pattern.TextRenderer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ExtendedClassInfo
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final boolean exact;
/*     */   private final String location;
/*     */   private final String version;
/*     */   
/*     */   public ExtendedClassInfo(boolean exact, String location, String version) {
/*  46 */     this.exact = exact;
/*  47 */     this.location = location;
/*  48 */     this.version = version;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  53 */     if (this == obj) {
/*  54 */       return true;
/*     */     }
/*  56 */     if (obj == null) {
/*  57 */       return false;
/*     */     }
/*  59 */     if (!(obj instanceof ExtendedClassInfo)) {
/*  60 */       return false;
/*     */     }
/*  62 */     ExtendedClassInfo other = (ExtendedClassInfo)obj;
/*  63 */     if (this.exact != other.exact) {
/*  64 */       return false;
/*     */     }
/*  66 */     if (this.location == null) {
/*  67 */       if (other.location != null) {
/*  68 */         return false;
/*     */       }
/*  70 */     } else if (!this.location.equals(other.location)) {
/*  71 */       return false;
/*     */     } 
/*  73 */     if (this.version == null) {
/*  74 */       if (other.version != null) {
/*  75 */         return false;
/*     */       }
/*  77 */     } else if (!this.version.equals(other.version)) {
/*  78 */       return false;
/*     */     } 
/*  80 */     return true;
/*     */   }
/*     */   
/*     */   public boolean getExact() {
/*  84 */     return this.exact;
/*     */   }
/*     */   
/*     */   public String getLocation() {
/*  88 */     return this.location;
/*     */   }
/*     */   
/*     */   public String getVersion() {
/*  92 */     return this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  97 */     int prime = 31;
/*  98 */     int result = 1;
/*  99 */     result = 31 * result + (this.exact ? 1231 : 1237);
/* 100 */     result = 31 * result + ((this.location == null) ? 0 : this.location.hashCode());
/* 101 */     result = 31 * result + ((this.version == null) ? 0 : this.version.hashCode());
/* 102 */     return result;
/*     */   }
/*     */   
/*     */   public void renderOn(StringBuilder output, TextRenderer textRenderer) {
/* 106 */     if (!this.exact) {
/* 107 */       textRenderer.render("~", output, "ExtraClassInfo.Inexact");
/*     */     }
/* 109 */     textRenderer.render("[", output, "ExtraClassInfo.Container");
/* 110 */     textRenderer.render(this.location, output, "ExtraClassInfo.Location");
/* 111 */     textRenderer.render(":", output, "ExtraClassInfo.ContainerSeparator");
/* 112 */     textRenderer.render(this.version, output, "ExtraClassInfo.Version");
/* 113 */     textRenderer.render("]", output, "ExtraClassInfo.Container");
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 118 */     StringBuilder sb = new StringBuilder();
/* 119 */     renderOn(sb, (TextRenderer)PlainTextRenderer.getInstance());
/* 120 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\impl\ExtendedClassInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */