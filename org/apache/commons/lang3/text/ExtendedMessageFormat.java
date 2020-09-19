/*     */ package org.apache.commons.lang3.text;
/*     */ 
/*     */ import java.text.Format;
/*     */ import java.text.MessageFormat;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang3.ObjectUtils;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtendedMessageFormat
/*     */   extends MessageFormat
/*     */ {
/*     */   private static final long serialVersionUID = -2362048321261811743L;
/*     */   private static final int HASH_SEED = 31;
/*     */   private static final String DUMMY_PATTERN = "";
/*     */   private static final char START_FMT = ',';
/*     */   private static final char END_FE = '}';
/*     */   private static final char START_FE = '{';
/*     */   private static final char QUOTE = '\'';
/*     */   private String toPattern;
/*     */   private final Map<String, ? extends FormatFactory> registry;
/*     */   
/*     */   public ExtendedMessageFormat(String pattern) {
/*  88 */     this(pattern, Locale.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedMessageFormat(String pattern, Locale locale) {
/*  99 */     this(pattern, locale, (Map<String, ? extends FormatFactory>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExtendedMessageFormat(String pattern, Map<String, ? extends FormatFactory> registry) {
/* 110 */     this(pattern, Locale.getDefault(), registry);
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
/*     */   public ExtendedMessageFormat(String pattern, Locale locale, Map<String, ? extends FormatFactory> registry) {
/* 122 */     super("");
/* 123 */     setLocale(locale);
/* 124 */     this.registry = registry;
/* 125 */     applyPattern(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toPattern() {
/* 133 */     return this.toPattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void applyPattern(String pattern) {
/* 143 */     if (this.registry == null) {
/* 144 */       super.applyPattern(pattern);
/* 145 */       this.toPattern = super.toPattern();
/*     */       return;
/*     */     } 
/* 148 */     ArrayList<Format> foundFormats = new ArrayList<Format>();
/* 149 */     ArrayList<String> foundDescriptions = new ArrayList<String>();
/* 150 */     StringBuilder stripCustom = new StringBuilder(pattern.length());
/*     */     
/* 152 */     ParsePosition pos = new ParsePosition(0);
/* 153 */     char[] c = pattern.toCharArray();
/* 154 */     int fmtCount = 0;
/* 155 */     while (pos.getIndex() < pattern.length()) {
/* 156 */       int start, index; Format format; String formatDescription; switch (c[pos.getIndex()]) {
/*     */         case '\'':
/* 158 */           appendQuotedString(pattern, pos, stripCustom);
/*     */           continue;
/*     */         case '{':
/* 161 */           fmtCount++;
/* 162 */           seekNonWs(pattern, pos);
/* 163 */           start = pos.getIndex();
/* 164 */           index = readArgumentIndex(pattern, next(pos));
/* 165 */           stripCustom.append('{').append(index);
/* 166 */           seekNonWs(pattern, pos);
/* 167 */           format = null;
/* 168 */           formatDescription = null;
/* 169 */           if (c[pos.getIndex()] == ',') {
/* 170 */             formatDescription = parseFormatDescription(pattern, 
/* 171 */                 next(pos));
/* 172 */             format = getFormat(formatDescription);
/* 173 */             if (format == null) {
/* 174 */               stripCustom.append(',').append(formatDescription);
/*     */             }
/*     */           } 
/* 177 */           foundFormats.add(format);
/* 178 */           foundDescriptions.add((format == null) ? null : formatDescription);
/* 179 */           Validate.isTrue((foundFormats.size() == fmtCount));
/* 180 */           Validate.isTrue((foundDescriptions.size() == fmtCount));
/* 181 */           if (c[pos.getIndex()] != '}') {
/* 182 */             throw new IllegalArgumentException("Unreadable format element at position " + start);
/*     */           }
/*     */           break;
/*     */       } 
/*     */       
/* 187 */       stripCustom.append(c[pos.getIndex()]);
/* 188 */       next(pos);
/*     */     } 
/*     */     
/* 191 */     super.applyPattern(stripCustom.toString());
/* 192 */     this.toPattern = insertFormats(super.toPattern(), foundDescriptions);
/* 193 */     if (containsElements(foundFormats)) {
/* 194 */       Format[] origFormats = getFormats();
/*     */ 
/*     */       
/* 197 */       int i = 0;
/* 198 */       for (Iterator<Format> it = foundFormats.iterator(); it.hasNext(); i++) {
/* 199 */         Format f = it.next();
/* 200 */         if (f != null) {
/* 201 */           origFormats[i] = f;
/*     */         }
/*     */       } 
/* 204 */       super.setFormats(origFormats);
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
/*     */   public void setFormat(int formatElementIndex, Format newFormat) {
/* 217 */     throw new UnsupportedOperationException();
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
/*     */   public void setFormatByArgumentIndex(int argumentIndex, Format newFormat) {
/* 229 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFormats(Format[] newFormats) {
/* 240 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFormatsByArgumentIndex(Format[] newFormats) {
/* 251 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 262 */     if (obj == this) {
/* 263 */       return true;
/*     */     }
/* 265 */     if (obj == null) {
/* 266 */       return false;
/*     */     }
/* 268 */     if (!super.equals(obj)) {
/* 269 */       return false;
/*     */     }
/* 271 */     if (ObjectUtils.notEqual(getClass(), obj.getClass())) {
/* 272 */       return false;
/*     */     }
/* 274 */     ExtendedMessageFormat rhs = (ExtendedMessageFormat)obj;
/* 275 */     if (ObjectUtils.notEqual(this.toPattern, rhs.toPattern)) {
/* 276 */       return false;
/*     */     }
/* 278 */     if (ObjectUtils.notEqual(this.registry, rhs.registry)) {
/* 279 */       return false;
/*     */     }
/* 281 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 290 */     int result = super.hashCode();
/* 291 */     result = 31 * result + ObjectUtils.hashCode(this.registry);
/* 292 */     result = 31 * result + ObjectUtils.hashCode(this.toPattern);
/* 293 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Format getFormat(String desc) {
/* 303 */     if (this.registry != null) {
/* 304 */       String name = desc;
/* 305 */       String args = null;
/* 306 */       int i = desc.indexOf(',');
/* 307 */       if (i > 0) {
/* 308 */         name = desc.substring(0, i).trim();
/* 309 */         args = desc.substring(i + 1).trim();
/*     */       } 
/* 311 */       FormatFactory factory = this.registry.get(name);
/* 312 */       if (factory != null) {
/* 313 */         return factory.getFormat(name, args, getLocale());
/*     */       }
/*     */     } 
/* 316 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int readArgumentIndex(String pattern, ParsePosition pos) {
/* 327 */     int start = pos.getIndex();
/* 328 */     seekNonWs(pattern, pos);
/* 329 */     StringBuilder result = new StringBuilder();
/* 330 */     boolean error = false;
/* 331 */     for (; !error && pos.getIndex() < pattern.length(); next(pos)) {
/* 332 */       char c = pattern.charAt(pos.getIndex());
/* 333 */       if (Character.isWhitespace(c)) {
/* 334 */         seekNonWs(pattern, pos);
/* 335 */         c = pattern.charAt(pos.getIndex());
/* 336 */         if (c != ',' && c != '}') {
/* 337 */           error = true;
/*     */           continue;
/*     */         } 
/*     */       } 
/* 341 */       if ((c == ',' || c == '}') && result.length() > 0) {
/*     */         try {
/* 343 */           return Integer.parseInt(result.toString());
/* 344 */         } catch (NumberFormatException numberFormatException) {}
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 349 */       error = !Character.isDigit(c);
/* 350 */       result.append(c); continue;
/*     */     } 
/* 352 */     if (error) {
/* 353 */       throw new IllegalArgumentException("Invalid format argument index at position " + start + ": " + pattern
/*     */           
/* 355 */           .substring(start, pos.getIndex()));
/*     */     }
/* 357 */     throw new IllegalArgumentException("Unterminated format element at position " + start);
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
/*     */   private String parseFormatDescription(String pattern, ParsePosition pos) {
/* 369 */     int start = pos.getIndex();
/* 370 */     seekNonWs(pattern, pos);
/* 371 */     int text = pos.getIndex();
/* 372 */     int depth = 1;
/* 373 */     for (; pos.getIndex() < pattern.length(); next(pos)) {
/* 374 */       switch (pattern.charAt(pos.getIndex())) {
/*     */         case '{':
/* 376 */           depth++;
/*     */           break;
/*     */         case '}':
/* 379 */           depth--;
/* 380 */           if (depth == 0) {
/* 381 */             return pattern.substring(text, pos.getIndex());
/*     */           }
/*     */           break;
/*     */         case '\'':
/* 385 */           getQuotedString(pattern, pos);
/*     */           break;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 391 */     throw new IllegalArgumentException("Unterminated format element at position " + start);
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
/*     */   private String insertFormats(String pattern, ArrayList<String> customPatterns) {
/* 403 */     if (!containsElements(customPatterns)) {
/* 404 */       return pattern;
/*     */     }
/* 406 */     StringBuilder sb = new StringBuilder(pattern.length() * 2);
/* 407 */     ParsePosition pos = new ParsePosition(0);
/* 408 */     int fe = -1;
/* 409 */     int depth = 0;
/* 410 */     while (pos.getIndex() < pattern.length()) {
/* 411 */       char c = pattern.charAt(pos.getIndex());
/* 412 */       switch (c) {
/*     */         case '\'':
/* 414 */           appendQuotedString(pattern, pos, sb);
/*     */           continue;
/*     */         case '{':
/* 417 */           depth++;
/* 418 */           sb.append('{').append(readArgumentIndex(pattern, next(pos)));
/*     */           
/* 420 */           if (depth == 1) {
/* 421 */             fe++;
/* 422 */             String customPattern = customPatterns.get(fe);
/* 423 */             if (customPattern != null) {
/* 424 */               sb.append(',').append(customPattern);
/*     */             }
/*     */           } 
/*     */           continue;
/*     */         case '}':
/* 429 */           depth--;
/*     */           break;
/*     */       } 
/* 432 */       sb.append(c);
/* 433 */       next(pos);
/*     */     } 
/*     */     
/* 436 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void seekNonWs(String pattern, ParsePosition pos) {
/* 446 */     int len = 0;
/* 447 */     char[] buffer = pattern.toCharArray();
/*     */     do {
/* 449 */       len = StrMatcher.splitMatcher().isMatch(buffer, pos.getIndex());
/* 450 */       pos.setIndex(pos.getIndex() + len);
/* 451 */     } while (len > 0 && pos.getIndex() < pattern.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ParsePosition next(ParsePosition pos) {
/* 461 */     pos.setIndex(pos.getIndex() + 1);
/* 462 */     return pos;
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
/*     */   private StringBuilder appendQuotedString(String pattern, ParsePosition pos, StringBuilder appendTo) {
/* 476 */     assert pattern.toCharArray()[pos.getIndex()] == '\'' : "Quoted string must start with quote character";
/*     */ 
/*     */ 
/*     */     
/* 480 */     if (appendTo != null) {
/* 481 */       appendTo.append('\'');
/*     */     }
/* 483 */     next(pos);
/*     */     
/* 485 */     int start = pos.getIndex();
/* 486 */     char[] c = pattern.toCharArray();
/* 487 */     int lastHold = start;
/* 488 */     for (int i = pos.getIndex(); i < pattern.length(); i++) {
/* 489 */       switch (c[pos.getIndex()]) {
/*     */         case '\'':
/* 491 */           next(pos);
/* 492 */           return (appendTo == null) ? null : appendTo.append(c, lastHold, pos
/* 493 */               .getIndex() - lastHold);
/*     */       } 
/* 495 */       next(pos);
/*     */     } 
/*     */     
/* 498 */     throw new IllegalArgumentException("Unterminated quoted string at position " + start);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getQuotedString(String pattern, ParsePosition pos) {
/* 509 */     appendQuotedString(pattern, pos, (StringBuilder)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean containsElements(Collection<?> coll) {
/* 518 */     if (coll == null || coll.isEmpty()) {
/* 519 */       return false;
/*     */     }
/* 521 */     for (Object name : coll) {
/* 522 */       if (name != null) {
/* 523 */         return true;
/*     */       }
/*     */     } 
/* 526 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\text\ExtendedMessageFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */