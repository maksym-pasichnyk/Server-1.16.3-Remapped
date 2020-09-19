/*     */ package org.apache.commons.lang3;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocaleUtils
/*     */ {
/*  41 */   private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry = new ConcurrentHashMap<String, List<Locale>>();
/*     */ 
/*     */ 
/*     */   
/*  45 */   private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage = new ConcurrentHashMap<String, List<Locale>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Locale toLocale(String str) {
/*  90 */     if (str == null) {
/*  91 */       return null;
/*     */     }
/*  93 */     if (str.isEmpty()) {
/*  94 */       return new Locale("", "");
/*     */     }
/*  96 */     if (str.contains("#")) {
/*  97 */       throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */     }
/*  99 */     int len = str.length();
/* 100 */     if (len < 2) {
/* 101 */       throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */     }
/* 103 */     char ch0 = str.charAt(0);
/* 104 */     if (ch0 == '_') {
/* 105 */       if (len < 3) {
/* 106 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       }
/* 108 */       char ch1 = str.charAt(1);
/* 109 */       char ch2 = str.charAt(2);
/* 110 */       if (!Character.isUpperCase(ch1) || !Character.isUpperCase(ch2)) {
/* 111 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       }
/* 113 */       if (len == 3) {
/* 114 */         return new Locale("", str.substring(1, 3));
/*     */       }
/* 116 */       if (len < 5) {
/* 117 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       }
/* 119 */       if (str.charAt(3) != '_') {
/* 120 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       }
/* 122 */       return new Locale("", str.substring(1, 3), str.substring(4));
/*     */     } 
/*     */     
/* 125 */     String[] split = str.split("_", -1);
/* 126 */     int occurrences = split.length - 1;
/* 127 */     switch (occurrences) {
/*     */       case 0:
/* 129 */         if (StringUtils.isAllLowerCase(str) && (len == 2 || len == 3)) {
/* 130 */           return new Locale(str);
/*     */         }
/* 132 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       
/*     */       case 1:
/* 135 */         if (StringUtils.isAllLowerCase(split[0]) && (split[0]
/* 136 */           .length() == 2 || split[0].length() == 3) && split[1]
/* 137 */           .length() == 2 && StringUtils.isAllUpperCase(split[1])) {
/* 138 */           return new Locale(split[0], split[1]);
/*     */         }
/* 140 */         throw new IllegalArgumentException("Invalid locale format: " + str);
/*     */       
/*     */       case 2:
/* 143 */         if (StringUtils.isAllLowerCase(split[0]) && (split[0]
/* 144 */           .length() == 2 || split[0].length() == 3) && (split[1]
/* 145 */           .length() == 0 || (split[1].length() == 2 && StringUtils.isAllUpperCase(split[1]))) && split[2]
/* 146 */           .length() > 0) {
/* 147 */           return new Locale(split[0], split[1], split[2]);
/*     */         }
/*     */         break;
/*     */     } 
/*     */     
/* 152 */     throw new IllegalArgumentException("Invalid locale format: " + str);
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
/*     */   public static List<Locale> localeLookupList(Locale locale) {
/* 170 */     return localeLookupList(locale, locale);
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
/*     */   public static List<Locale> localeLookupList(Locale locale, Locale defaultLocale) {
/* 192 */     List<Locale> list = new ArrayList<Locale>(4);
/* 193 */     if (locale != null) {
/* 194 */       list.add(locale);
/* 195 */       if (locale.getVariant().length() > 0) {
/* 196 */         list.add(new Locale(locale.getLanguage(), locale.getCountry()));
/*     */       }
/* 198 */       if (locale.getCountry().length() > 0) {
/* 199 */         list.add(new Locale(locale.getLanguage(), ""));
/*     */       }
/* 201 */       if (!list.contains(defaultLocale)) {
/* 202 */         list.add(defaultLocale);
/*     */       }
/*     */     } 
/* 205 */     return Collections.unmodifiableList(list);
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
/*     */   public static List<Locale> availableLocaleList() {
/* 219 */     return SyncAvoid.AVAILABLE_LOCALE_LIST;
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
/*     */   public static Set<Locale> availableLocaleSet() {
/* 233 */     return SyncAvoid.AVAILABLE_LOCALE_SET;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAvailableLocale(Locale locale) {
/* 244 */     return availableLocaleList().contains(locale);
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
/*     */   public static List<Locale> languagesByCountry(String countryCode) {
/* 258 */     if (countryCode == null) {
/* 259 */       return Collections.emptyList();
/*     */     }
/* 261 */     List<Locale> langs = cLanguagesByCountry.get(countryCode);
/* 262 */     if (langs == null) {
/* 263 */       langs = new ArrayList<Locale>();
/* 264 */       List<Locale> locales = availableLocaleList();
/* 265 */       for (int i = 0; i < locales.size(); i++) {
/* 266 */         Locale locale = locales.get(i);
/* 267 */         if (countryCode.equals(locale.getCountry()) && locale
/* 268 */           .getVariant().isEmpty()) {
/* 269 */           langs.add(locale);
/*     */         }
/*     */       } 
/* 272 */       langs = Collections.unmodifiableList(langs);
/* 273 */       cLanguagesByCountry.putIfAbsent(countryCode, langs);
/* 274 */       langs = cLanguagesByCountry.get(countryCode);
/*     */     } 
/* 276 */     return langs;
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
/*     */   public static List<Locale> countriesByLanguage(String languageCode) {
/* 290 */     if (languageCode == null) {
/* 291 */       return Collections.emptyList();
/*     */     }
/* 293 */     List<Locale> countries = cCountriesByLanguage.get(languageCode);
/* 294 */     if (countries == null) {
/* 295 */       countries = new ArrayList<Locale>();
/* 296 */       List<Locale> locales = availableLocaleList();
/* 297 */       for (int i = 0; i < locales.size(); i++) {
/* 298 */         Locale locale = locales.get(i);
/* 299 */         if (languageCode.equals(locale.getLanguage()) && locale
/* 300 */           .getCountry().length() != 0 && locale
/* 301 */           .getVariant().isEmpty()) {
/* 302 */           countries.add(locale);
/*     */         }
/*     */       } 
/* 305 */       countries = Collections.unmodifiableList(countries);
/* 306 */       cCountriesByLanguage.putIfAbsent(languageCode, countries);
/* 307 */       countries = cCountriesByLanguage.get(languageCode);
/*     */     } 
/* 309 */     return countries;
/*     */   }
/*     */ 
/*     */   
/*     */   static class SyncAvoid
/*     */   {
/*     */     private static final List<Locale> AVAILABLE_LOCALE_LIST;
/*     */     
/*     */     private static final Set<Locale> AVAILABLE_LOCALE_SET;
/*     */ 
/*     */     
/*     */     static {
/* 321 */       List<Locale> list = new ArrayList<Locale>(Arrays.asList(Locale.getAvailableLocales()));
/* 322 */       AVAILABLE_LOCALE_LIST = Collections.unmodifiableList(list);
/* 323 */       AVAILABLE_LOCALE_SET = Collections.unmodifiableSet(new HashSet<Locale>(list));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\LocaleUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */