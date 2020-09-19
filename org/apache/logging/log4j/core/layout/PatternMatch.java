/*     */ package org.apache.logging.log4j.core.layout;
/*     */ 
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import org.apache.logging.log4j.core.config.plugins.Plugin;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
/*     */ import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Plugin(name = "PatternMatch", category = "Core", printObject = true)
/*     */ public final class PatternMatch
/*     */ {
/*     */   private final String key;
/*     */   private final String pattern;
/*     */   
/*     */   public PatternMatch(String key, String pattern) {
/*  45 */     this.key = key;
/*  46 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKey() {
/*  54 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern() {
/*  62 */     return this.pattern;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  67 */     return this.key + '=' + this.pattern;
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/*  72 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     implements org.apache.logging.log4j.core.util.Builder<PatternMatch>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     @PluginBuilderAttribute
/*     */     private String key;
/*     */     @PluginBuilderAttribute
/*     */     private String pattern;
/*     */     
/*     */     public Builder setKey(String key) {
/*  86 */       this.key = key;
/*  87 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setPattern(String pattern) {
/*  91 */       this.pattern = pattern;
/*  92 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public PatternMatch build() {
/*  97 */       return new PatternMatch(this.key, this.pattern);
/*     */     }
/*     */     
/*     */     protected Object readResolve() throws ObjectStreamException {
/* 101 */       return new PatternMatch(this.key, this.pattern);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 107 */     int prime = 31;
/* 108 */     int result = 1;
/* 109 */     result = 31 * result + ((this.key == null) ? 0 : this.key.hashCode());
/* 110 */     result = 31 * result + ((this.pattern == null) ? 0 : this.pattern.hashCode());
/* 111 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 116 */     if (this == obj) {
/* 117 */       return true;
/*     */     }
/* 119 */     if (obj == null) {
/* 120 */       return false;
/*     */     }
/* 122 */     if (getClass() != obj.getClass()) {
/* 123 */       return false;
/*     */     }
/* 125 */     PatternMatch other = (PatternMatch)obj;
/* 126 */     if (this.key == null) {
/* 127 */       if (other.key != null) {
/* 128 */         return false;
/*     */       }
/* 130 */     } else if (!this.key.equals(other.key)) {
/* 131 */       return false;
/*     */     } 
/* 133 */     if (this.pattern == null) {
/* 134 */       if (other.pattern != null) {
/* 135 */         return false;
/*     */       }
/* 137 */     } else if (!this.pattern.equals(other.pattern)) {
/* 138 */       return false;
/*     */     } 
/* 140 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\layout\PatternMatch.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */