/*     */ package org.apache.logging.log4j.core.util;
/*     */ 
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
/*     */ @Plugin(name = "KeyValuePair", category = "Core", printObject = true)
/*     */ public final class KeyValuePair
/*     */ {
/*     */   private final String key;
/*     */   private final String value;
/*     */   
/*     */   public KeyValuePair(String key, String value) {
/*  42 */     this.key = key;
/*  43 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKey() {
/*  51 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  59 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  64 */     return this.key + '=' + this.value;
/*     */   }
/*     */   
/*     */   @PluginBuilderFactory
/*     */   public static Builder newBuilder() {
/*  69 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */     implements Builder<KeyValuePair>
/*     */   {
/*     */     @PluginBuilderAttribute
/*     */     private String key;
/*     */     @PluginBuilderAttribute
/*     */     private String value;
/*     */     
/*     */     public Builder setKey(String aKey) {
/*  81 */       this.key = aKey;
/*  82 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setValue(String aValue) {
/*  86 */       this.value = aValue;
/*  87 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public KeyValuePair build() {
/*  92 */       return new KeyValuePair(this.key, this.value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  99 */     int prime = 31;
/* 100 */     int result = 1;
/* 101 */     result = 31 * result + ((this.key == null) ? 0 : this.key.hashCode());
/* 102 */     result = 31 * result + ((this.value == null) ? 0 : this.value.hashCode());
/* 103 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 108 */     if (this == obj) {
/* 109 */       return true;
/*     */     }
/* 111 */     if (obj == null) {
/* 112 */       return false;
/*     */     }
/* 114 */     if (getClass() != obj.getClass()) {
/* 115 */       return false;
/*     */     }
/* 117 */     KeyValuePair other = (KeyValuePair)obj;
/* 118 */     if (this.key == null) {
/* 119 */       if (other.key != null) {
/* 120 */         return false;
/*     */       }
/* 122 */     } else if (!this.key.equals(other.key)) {
/* 123 */       return false;
/*     */     } 
/* 125 */     if (this.value == null) {
/* 126 */       if (other.value != null) {
/* 127 */         return false;
/*     */       }
/* 129 */     } else if (!this.value.equals(other.value)) {
/* 130 */       return false;
/*     */     } 
/* 132 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\cor\\util\KeyValuePair.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */