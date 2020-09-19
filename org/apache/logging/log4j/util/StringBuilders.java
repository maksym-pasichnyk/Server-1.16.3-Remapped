/*     */ package org.apache.logging.log4j.util;
/*     */ 
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StringBuilders
/*     */ {
/*     */   public static StringBuilder appendDqValue(StringBuilder sb, Object value) {
/*  38 */     return sb.append('"').append(value).append('"');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringBuilder appendKeyDqValue(StringBuilder sb, Map.Entry<String, String> entry) {
/*  49 */     return appendKeyDqValue(sb, entry.getKey(), entry.getValue());
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
/*     */   public static StringBuilder appendKeyDqValue(StringBuilder sb, String key, Object value) {
/*  61 */     return sb.append(key).append('=').append('"').append(value).append('"');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void appendValue(StringBuilder stringBuilder, Object obj) {
/*  72 */     if (obj == null || obj instanceof String) {
/*  73 */       stringBuilder.append((String)obj);
/*  74 */     } else if (obj instanceof StringBuilderFormattable) {
/*  75 */       ((StringBuilderFormattable)obj).formatTo(stringBuilder);
/*  76 */     } else if (obj instanceof CharSequence) {
/*  77 */       stringBuilder.append((CharSequence)obj);
/*  78 */     } else if (obj instanceof Integer) {
/*  79 */       stringBuilder.append(((Integer)obj).intValue());
/*  80 */     } else if (obj instanceof Long) {
/*  81 */       stringBuilder.append(((Long)obj).longValue());
/*  82 */     } else if (obj instanceof Double) {
/*  83 */       stringBuilder.append(((Double)obj).doubleValue());
/*  84 */     } else if (obj instanceof Boolean) {
/*  85 */       stringBuilder.append(((Boolean)obj).booleanValue());
/*  86 */     } else if (obj instanceof Character) {
/*  87 */       stringBuilder.append(((Character)obj).charValue());
/*  88 */     } else if (obj instanceof Short) {
/*  89 */       stringBuilder.append(((Short)obj).shortValue());
/*  90 */     } else if (obj instanceof Float) {
/*  91 */       stringBuilder.append(((Float)obj).floatValue());
/*     */     } else {
/*  93 */       stringBuilder.append(obj);
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
/*     */   public static boolean equals(CharSequence left, int leftOffset, int leftLength, CharSequence right, int rightOffset, int rightLength) {
/* 111 */     if (leftLength == rightLength) {
/* 112 */       for (int i = 0; i < rightLength; i++) {
/* 113 */         if (left.charAt(i + leftOffset) != right.charAt(i + rightOffset)) {
/* 114 */           return false;
/*     */         }
/*     */       } 
/* 117 */       return true;
/*     */     } 
/* 119 */     return false;
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
/*     */   public static boolean equalsIgnoreCase(CharSequence left, int leftOffset, int leftLength, CharSequence right, int rightOffset, int rightLength) {
/* 136 */     if (leftLength == rightLength) {
/* 137 */       for (int i = 0; i < rightLength; i++) {
/* 138 */         if (Character.toLowerCase(left.charAt(i + leftOffset)) != Character.toLowerCase(right.charAt(i + rightOffset))) {
/* 139 */           return false;
/*     */         }
/*     */       } 
/* 142 */       return true;
/*     */     } 
/* 144 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4\\util\StringBuilders.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */