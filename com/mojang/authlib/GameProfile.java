/*     */ package com.mojang.authlib;
/*     */ 
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import java.util.UUID;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.lang3.builder.ToStringBuilder;
/*     */ 
/*     */ public class GameProfile
/*     */ {
/*     */   private final UUID id;
/*     */   private final String name;
/*  12 */   private final PropertyMap properties = new PropertyMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean legacy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GameProfile(UUID id, String name) {
/*  25 */     if (id == null && StringUtils.isBlank(name)) {
/*  26 */       throw new IllegalArgumentException("Name and ID cannot both be blank");
/*     */     }
/*     */     
/*  29 */     this.id = id;
/*  30 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UUID getId() {
/*  41 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  52 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyMap getProperties() {
/*  61 */     return this.properties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/*  72 */     return (this.id != null && StringUtils.isNotBlank(getName()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  77 */     if (this == o) {
/*  78 */       return true;
/*     */     }
/*  80 */     if (o == null || getClass() != o.getClass()) {
/*  81 */       return false;
/*     */     }
/*     */     
/*  84 */     GameProfile that = (GameProfile)o;
/*     */     
/*  86 */     if ((this.id != null) ? !this.id.equals(that.id) : (that.id != null)) {
/*  87 */       return false;
/*     */     }
/*  89 */     if ((this.name != null) ? !this.name.equals(that.name) : (that.name != null)) {
/*  90 */       return false;
/*     */     }
/*     */     
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  98 */     int result = (this.id != null) ? this.id.hashCode() : 0;
/*  99 */     result = 31 * result + ((this.name != null) ? this.name.hashCode() : 0);
/* 100 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     return (new ToStringBuilder(this))
/* 106 */       .append("id", this.id)
/* 107 */       .append("name", this.name)
/* 108 */       .append("properties", this.properties)
/* 109 */       .append("legacy", this.legacy)
/* 110 */       .toString();
/*     */   }
/*     */   
/*     */   public boolean isLegacy() {
/* 114 */     return this.legacy;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\authlib\GameProfile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */