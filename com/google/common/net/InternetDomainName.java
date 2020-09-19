/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.CharMatcher;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.thirdparty.publicsuffix.PublicSuffixPatterns;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtCompatible
/*     */ public final class InternetDomainName
/*     */ {
/*  72 */   private static final CharMatcher DOTS_MATCHER = CharMatcher.anyOf(".。．｡");
/*  73 */   private static final Splitter DOT_SPLITTER = Splitter.on('.');
/*  74 */   private static final Joiner DOT_JOINER = Joiner.on('.');
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int NO_PUBLIC_SUFFIX_FOUND = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DOT_REGEX = "\\.";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_PARTS = 127;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_LENGTH = 253;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int MAX_DOMAIN_PART_LENGTH = 63;
/*     */ 
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
/*     */   private final ImmutableList<String> parts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int publicSuffixIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InternetDomainName(String name) {
/* 130 */     name = Ascii.toLowerCase(DOTS_MATCHER.replaceFrom(name, '.'));
/*     */     
/* 132 */     if (name.endsWith(".")) {
/* 133 */       name = name.substring(0, name.length() - 1);
/*     */     }
/*     */     
/* 136 */     Preconditions.checkArgument((name.length() <= 253), "Domain name too long: '%s':", name);
/* 137 */     this.name = name;
/*     */     
/* 139 */     this.parts = ImmutableList.copyOf(DOT_SPLITTER.split(name));
/* 140 */     Preconditions.checkArgument((this.parts.size() <= 127), "Domain has too many parts: '%s'", name);
/* 141 */     Preconditions.checkArgument(validateSyntax((List<String>)this.parts), "Not a valid domain name: '%s'", name);
/*     */     
/* 143 */     this.publicSuffixIndex = findPublicSuffix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int findPublicSuffix() {
/* 153 */     int partsSize = this.parts.size();
/*     */     
/* 155 */     for (int i = 0; i < partsSize; i++) {
/* 156 */       String ancestorName = DOT_JOINER.join((Iterable)this.parts.subList(i, partsSize));
/*     */       
/* 158 */       if (PublicSuffixPatterns.EXACT.containsKey(ancestorName)) {
/* 159 */         return i;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 165 */       if (PublicSuffixPatterns.EXCLUDED.containsKey(ancestorName)) {
/* 166 */         return i + 1;
/*     */       }
/*     */       
/* 169 */       if (matchesWildcardPublicSuffix(ancestorName)) {
/* 170 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 174 */     return -1;
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
/*     */   public static InternetDomainName from(String domain) {
/* 196 */     return new InternetDomainName((String)Preconditions.checkNotNull(domain));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validateSyntax(List<String> parts) {
/* 206 */     int lastIndex = parts.size() - 1;
/*     */ 
/*     */ 
/*     */     
/* 210 */     if (!validatePart(parts.get(lastIndex), true)) {
/* 211 */       return false;
/*     */     }
/*     */     
/* 214 */     for (int i = 0; i < lastIndex; i++) {
/* 215 */       String part = parts.get(i);
/* 216 */       if (!validatePart(part, false)) {
/* 217 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 221 */     return true;
/*     */   }
/*     */   
/* 224 */   private static final CharMatcher DASH_MATCHER = CharMatcher.anyOf("-_");
/*     */ 
/*     */   
/* 227 */   private static final CharMatcher PART_CHAR_MATCHER = CharMatcher.javaLetterOrDigit().or(DASH_MATCHER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validatePart(String part, boolean isFinalPart) {
/* 242 */     if (part.length() < 1 || part.length() > 63) {
/* 243 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 256 */     String asciiChars = CharMatcher.ascii().retainFrom(part);
/*     */     
/* 258 */     if (!PART_CHAR_MATCHER.matchesAllOf(asciiChars)) {
/* 259 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 264 */     if (DASH_MATCHER.matches(part.charAt(0)) || DASH_MATCHER
/* 265 */       .matches(part.charAt(part.length() - 1))) {
/* 266 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 276 */     if (isFinalPart && CharMatcher.digit().matches(part.charAt(0))) {
/* 277 */       return false;
/*     */     }
/*     */     
/* 280 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<String> parts() {
/* 289 */     return this.parts;
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
/*     */   public boolean isPublicSuffix() {
/* 303 */     return (this.publicSuffixIndex == 0);
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
/*     */   public boolean hasPublicSuffix() {
/* 316 */     return (this.publicSuffixIndex != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternetDomainName publicSuffix() {
/* 326 */     return hasPublicSuffix() ? ancestor(this.publicSuffixIndex) : null;
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
/*     */   public boolean isUnderPublicSuffix() {
/* 347 */     return (this.publicSuffixIndex > 0);
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
/*     */   public boolean isTopPrivateDomain() {
/* 367 */     return (this.publicSuffixIndex == 1);
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
/*     */   
/*     */   public InternetDomainName topPrivateDomain() {
/* 390 */     if (isTopPrivateDomain()) {
/* 391 */       return this;
/*     */     }
/* 393 */     Preconditions.checkState(isUnderPublicSuffix(), "Not under a public suffix: %s", this.name);
/* 394 */     return ancestor(this.publicSuffixIndex - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasParent() {
/* 401 */     return (this.parts.size() > 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InternetDomainName parent() {
/* 412 */     Preconditions.checkState(hasParent(), "Domain '%s' has no parent", this.name);
/* 413 */     return ancestor(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InternetDomainName ancestor(int levels) {
/* 424 */     return from(DOT_JOINER.join((Iterable)this.parts.subList(levels, this.parts.size())));
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
/*     */   public InternetDomainName child(String leftParts) {
/* 438 */     return from((String)Preconditions.checkNotNull(leftParts) + "." + this.name);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValid(String name) {
/*     */     try {
/* 464 */       from(name);
/* 465 */       return true;
/* 466 */     } catch (IllegalArgumentException e) {
/* 467 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean matchesWildcardPublicSuffix(String domain) {
/* 475 */     String[] pieces = domain.split("\\.", 2);
/* 476 */     return (pieces.length == 2 && PublicSuffixPatterns.UNDER.containsKey(pieces[1]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 484 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 495 */     if (object == this) {
/* 496 */       return true;
/*     */     }
/*     */     
/* 499 */     if (object instanceof InternetDomainName) {
/* 500 */       InternetDomainName that = (InternetDomainName)object;
/* 501 */       return this.name.equals(that.name);
/*     */     } 
/*     */     
/* 504 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 509 */     return this.name.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\net\InternetDomainName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */