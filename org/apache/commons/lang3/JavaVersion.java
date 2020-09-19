/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import org.apache.commons.lang3.math.NumberUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum JavaVersion
/*     */ {
/*  33 */   JAVA_0_9(1.5F, "0.9"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   JAVA_1_1(1.1F, "1.1"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  43 */   JAVA_1_2(1.2F, "1.2"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   JAVA_1_3(1.3F, "1.3"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   JAVA_1_4(1.4F, "1.4"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   JAVA_1_5(1.5F, "1.5"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   JAVA_1_6(1.6F, "1.6"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   JAVA_1_7(1.7F, "1.7"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   JAVA_1_8(1.8F, "1.8"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   JAVA_1_9(9.0F, "9"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   JAVA_9(9.0F, "9"),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   JAVA_RECENT(maxVersion(), Float.toString(maxVersion()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final float value;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JavaVersion(float value, String name) {
/* 108 */     this.value = value;
/* 109 */     this.name = name;
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
/*     */   public boolean atLeast(JavaVersion requiredVersion) {
/* 123 */     return (this.value >= requiredVersion.value);
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
/*     */   static JavaVersion getJavaVersion(String nom) {
/* 137 */     return get(nom);
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
/*     */   static JavaVersion get(String nom) {
/* 150 */     if ("0.9".equals(nom))
/* 151 */       return JAVA_0_9; 
/* 152 */     if ("1.1".equals(nom))
/* 153 */       return JAVA_1_1; 
/* 154 */     if ("1.2".equals(nom))
/* 155 */       return JAVA_1_2; 
/* 156 */     if ("1.3".equals(nom))
/* 157 */       return JAVA_1_3; 
/* 158 */     if ("1.4".equals(nom))
/* 159 */       return JAVA_1_4; 
/* 160 */     if ("1.5".equals(nom))
/* 161 */       return JAVA_1_5; 
/* 162 */     if ("1.6".equals(nom))
/* 163 */       return JAVA_1_6; 
/* 164 */     if ("1.7".equals(nom))
/* 165 */       return JAVA_1_7; 
/* 166 */     if ("1.8".equals(nom))
/* 167 */       return JAVA_1_8; 
/* 168 */     if ("9".equals(nom)) {
/* 169 */       return JAVA_9;
/*     */     }
/* 171 */     if (nom == null) {
/* 172 */       return null;
/*     */     }
/* 174 */     float v = toFloatVersion(nom);
/* 175 */     if (v - 1.0D < 1.0D) {
/* 176 */       int firstComma = Math.max(nom.indexOf('.'), nom.indexOf(','));
/* 177 */       int end = Math.max(nom.length(), nom.indexOf(',', firstComma));
/* 178 */       if (Float.parseFloat(nom.substring(firstComma + 1, end)) > 0.9F) {
/* 179 */         return JAVA_RECENT;
/*     */       }
/*     */     } 
/* 182 */     return null;
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
/*     */   public String toString() {
/* 195 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float maxVersion() {
/* 204 */     float v = toFloatVersion(System.getProperty("java.specification.version", "99.0"));
/* 205 */     if (v > 0.0F) {
/* 206 */       return v;
/*     */     }
/* 208 */     return 99.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static float toFloatVersion(String value) {
/* 218 */     int defaultReturnValue = -1;
/* 219 */     if (value.contains(".")) {
/* 220 */       String[] toParse = value.split("\\.");
/* 221 */       if (toParse.length >= 2) {
/* 222 */         return NumberUtils.toFloat(toParse[0] + '.' + toParse[1], -1.0F);
/*     */       }
/*     */     } else {
/* 225 */       return NumberUtils.toFloat(value, -1.0F);
/*     */     } 
/* 227 */     return -1.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\JavaVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */