/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnumUtils
/*     */ {
/*     */   private static final String NULL_ELEMENTS_NOT_PERMITTED = "null elements not permitted";
/*     */   private static final String CANNOT_STORE_S_S_VALUES_IN_S_BITS = "Cannot store %s %s values in %s bits";
/*     */   private static final String S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE = "%s does not seem to be an Enum type";
/*     */   private static final String ENUM_CLASS_MUST_BE_DEFINED = "EnumClass must be defined.";
/*     */   
/*     */   public static <E extends Enum<E>> Map<String, E> getEnumMap(Class<E> enumClass) {
/*  58 */     Map<String, E> map = new LinkedHashMap<String, E>();
/*  59 */     for (Enum enum_ : (Enum[])enumClass.getEnumConstants()) {
/*  60 */       map.put(enum_.name(), (E)enum_);
/*     */     }
/*  62 */     return map;
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
/*     */   public static <E extends Enum<E>> List<E> getEnumList(Class<E> enumClass) {
/*  75 */     return new ArrayList<E>(Arrays.asList(enumClass.getEnumConstants()));
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
/*     */   public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String enumName) {
/*  90 */     if (enumName == null) {
/*  91 */       return false;
/*     */     }
/*     */     try {
/*  94 */       Enum.valueOf(enumClass, enumName);
/*  95 */       return true;
/*  96 */     } catch (IllegalArgumentException ex) {
/*  97 */       return false;
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
/*     */   public static <E extends Enum<E>> E getEnum(Class<E> enumClass, String enumName) {
/* 113 */     if (enumName == null) {
/* 114 */       return null;
/*     */     }
/*     */     try {
/* 117 */       return Enum.valueOf(enumClass, enumName);
/* 118 */     } catch (IllegalArgumentException ex) {
/* 119 */       return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Enum<E>> long generateBitVector(Class<E> enumClass, Iterable<? extends E> values) {
/* 142 */     checkBitVectorable(enumClass);
/* 143 */     Validate.notNull(values);
/* 144 */     long total = 0L;
/* 145 */     for (Enum enum_ : values) {
/* 146 */       Validate.isTrue((enum_ != null), "null elements not permitted", new Object[0]);
/* 147 */       total |= 1L << enum_.ordinal();
/*     */     } 
/* 149 */     return total;
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
/*     */   public static <E extends Enum<E>> long[] generateBitVectors(Class<E> enumClass, Iterable<? extends E> values) {
/* 169 */     asEnum(enumClass);
/* 170 */     Validate.notNull(values);
/* 171 */     EnumSet<E> condensed = EnumSet.noneOf(enumClass);
/* 172 */     for (Enum enum_ : values) {
/* 173 */       Validate.isTrue((enum_ != null), "null elements not permitted", new Object[0]);
/* 174 */       condensed.add((E)enum_);
/*     */     } 
/* 176 */     long[] result = new long[(((Enum[])enumClass.getEnumConstants()).length - 1) / 64 + 1];
/* 177 */     for (Enum enum_ : condensed) {
/* 178 */       result[enum_.ordinal() / 64] = result[enum_.ordinal() / 64] | 1L << enum_.ordinal() % 64;
/*     */     }
/* 180 */     ArrayUtils.reverse(result);
/* 181 */     return result;
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
/*     */   public static <E extends Enum<E>> long generateBitVector(Class<E> enumClass, E... values) {
/* 202 */     Validate.noNullElements(values);
/* 203 */     return generateBitVector(enumClass, Arrays.asList(values));
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
/*     */   public static <E extends Enum<E>> long[] generateBitVectors(Class<E> enumClass, E... values) {
/* 223 */     asEnum(enumClass);
/* 224 */     Validate.noNullElements(values);
/* 225 */     EnumSet<E> condensed = EnumSet.noneOf(enumClass);
/* 226 */     Collections.addAll(condensed, values);
/* 227 */     long[] result = new long[(((Enum[])enumClass.getEnumConstants()).length - 1) / 64 + 1];
/* 228 */     for (Enum enum_ : condensed) {
/* 229 */       result[enum_.ordinal() / 64] = result[enum_.ordinal() / 64] | 1L << enum_.ordinal() % 64;
/*     */     }
/* 231 */     ArrayUtils.reverse(result);
/* 232 */     return result;
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
/*     */   public static <E extends Enum<E>> EnumSet<E> processBitVector(Class<E> enumClass, long value) {
/* 249 */     checkBitVectorable(enumClass).getEnumConstants();
/* 250 */     return processBitVectors(enumClass, new long[] { value });
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
/*     */   public static <E extends Enum<E>> EnumSet<E> processBitVectors(Class<E> enumClass, long... values) {
/* 267 */     EnumSet<E> results = EnumSet.noneOf(asEnum(enumClass));
/* 268 */     long[] lvalues = ArrayUtils.clone(Validate.<long[]>notNull(values));
/* 269 */     ArrayUtils.reverse(lvalues);
/* 270 */     for (Enum enum_ : (Enum[])enumClass.getEnumConstants()) {
/* 271 */       int block = enum_.ordinal() / 64;
/* 272 */       if (block < lvalues.length && (lvalues[block] & 1L << enum_.ordinal() % 64) != 0L) {
/* 273 */         results.add((E)enum_);
/*     */       }
/*     */     } 
/* 276 */     return results;
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
/*     */   private static <E extends Enum<E>> Class<E> checkBitVectorable(Class<E> enumClass) {
/* 289 */     Enum[] arrayOfEnum = asEnum(enumClass).getEnumConstants();
/* 290 */     Validate.isTrue((arrayOfEnum.length <= 64), "Cannot store %s %s values in %s bits", new Object[] {
/* 291 */           Integer.valueOf(arrayOfEnum.length), enumClass.getSimpleName(), Integer.valueOf(64)
/*     */         });
/* 293 */     return enumClass;
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
/*     */   private static <E extends Enum<E>> Class<E> asEnum(Class<E> enumClass) {
/* 306 */     Validate.notNull(enumClass, "EnumClass must be defined.", new Object[0]);
/* 307 */     Validate.isTrue(enumClass.isEnum(), "%s does not seem to be an Enum type", new Object[] { enumClass });
/* 308 */     return enumClass;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\EnumUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */