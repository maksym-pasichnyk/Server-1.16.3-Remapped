/*     */ package io.netty.handler.codec.http.cookie;
/*     */ 
/*     */ import io.netty.util.internal.InternalThreadLocalMap;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClientCookieEncoder
/*     */   extends CookieEncoder
/*     */ {
/*  53 */   public static final ClientCookieEncoder STRICT = new ClientCookieEncoder(true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   public static final ClientCookieEncoder LAX = new ClientCookieEncoder(false);
/*     */   
/*     */   private ClientCookieEncoder(boolean strict) {
/*  62 */     super(strict);
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
/*     */   public String encode(String name, String value) {
/*  75 */     return encode(new DefaultCookie(name, value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String encode(Cookie cookie) {
/*  85 */     StringBuilder buf = CookieUtil.stringBuilder();
/*  86 */     encode(buf, (Cookie)ObjectUtil.checkNotNull(cookie, "cookie"));
/*  87 */     return CookieUtil.stripTrailingSeparator(buf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   private static final Comparator<Cookie> COOKIE_COMPARATOR = new Comparator<Cookie>()
/*     */     {
/*     */       public int compare(Cookie c1, Cookie c2) {
/*  97 */         String path1 = c1.path();
/*  98 */         String path2 = c2.path();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 104 */         int len1 = (path1 == null) ? Integer.MAX_VALUE : path1.length();
/* 105 */         int len2 = (path2 == null) ? Integer.MAX_VALUE : path2.length();
/* 106 */         int diff = len2 - len1;
/* 107 */         if (diff != 0) {
/* 108 */           return diff;
/*     */         }
/*     */ 
/*     */         
/* 112 */         return -1;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String encode(Cookie... cookies) {
/* 124 */     if (((Cookie[])ObjectUtil.checkNotNull(cookies, "cookies")).length == 0) {
/* 125 */       return null;
/*     */     }
/*     */     
/* 128 */     StringBuilder buf = CookieUtil.stringBuilder();
/* 129 */     if (this.strict) {
/* 130 */       if (cookies.length == 1) {
/* 131 */         encode(buf, cookies[0]);
/*     */       } else {
/* 133 */         Cookie[] cookiesSorted = Arrays.<Cookie>copyOf(cookies, cookies.length);
/* 134 */         Arrays.sort(cookiesSorted, COOKIE_COMPARATOR);
/* 135 */         for (Cookie c : cookiesSorted) {
/* 136 */           encode(buf, c);
/*     */         }
/*     */       } 
/*     */     } else {
/* 140 */       for (Cookie c : cookies) {
/* 141 */         encode(buf, c);
/*     */       }
/*     */     } 
/* 144 */     return CookieUtil.stripTrailingSeparatorOrNull(buf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String encode(Collection<? extends Cookie> cookies) {
/* 155 */     if (((Collection)ObjectUtil.checkNotNull(cookies, "cookies")).isEmpty()) {
/* 156 */       return null;
/*     */     }
/*     */     
/* 159 */     StringBuilder buf = CookieUtil.stringBuilder();
/* 160 */     if (this.strict) {
/* 161 */       if (cookies.size() == 1) {
/* 162 */         encode(buf, cookies.iterator().next());
/*     */       } else {
/* 164 */         Cookie[] cookiesSorted = cookies.<Cookie>toArray(new Cookie[cookies.size()]);
/* 165 */         Arrays.sort(cookiesSorted, COOKIE_COMPARATOR);
/* 166 */         for (Cookie c : cookiesSorted) {
/* 167 */           encode(buf, c);
/*     */         }
/*     */       } 
/*     */     } else {
/* 171 */       for (Cookie c : cookies) {
/* 172 */         encode(buf, c);
/*     */       }
/*     */     } 
/* 175 */     return CookieUtil.stripTrailingSeparatorOrNull(buf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String encode(Iterable<? extends Cookie> cookies) {
/* 185 */     Iterator<? extends Cookie> cookiesIt = ((Iterable<? extends Cookie>)ObjectUtil.checkNotNull(cookies, "cookies")).iterator();
/* 186 */     if (!cookiesIt.hasNext()) {
/* 187 */       return null;
/*     */     }
/*     */     
/* 190 */     StringBuilder buf = CookieUtil.stringBuilder();
/* 191 */     if (this.strict) {
/* 192 */       Cookie firstCookie = cookiesIt.next();
/* 193 */       if (!cookiesIt.hasNext()) {
/* 194 */         encode(buf, firstCookie);
/*     */       } else {
/* 196 */         List<Cookie> cookiesList = InternalThreadLocalMap.get().arrayList();
/* 197 */         cookiesList.add(firstCookie);
/* 198 */         while (cookiesIt.hasNext()) {
/* 199 */           cookiesList.add(cookiesIt.next());
/*     */         }
/* 201 */         Cookie[] cookiesSorted = cookiesList.<Cookie>toArray(new Cookie[cookiesList.size()]);
/* 202 */         Arrays.sort(cookiesSorted, COOKIE_COMPARATOR);
/* 203 */         for (Cookie c : cookiesSorted) {
/* 204 */           encode(buf, c);
/*     */         }
/*     */       } 
/*     */     } else {
/* 208 */       while (cookiesIt.hasNext()) {
/* 209 */         encode(buf, cookiesIt.next());
/*     */       }
/*     */     } 
/* 212 */     return CookieUtil.stripTrailingSeparatorOrNull(buf);
/*     */   }
/*     */   
/*     */   private void encode(StringBuilder buf, Cookie c) {
/* 216 */     String name = c.name();
/* 217 */     String value = (c.value() != null) ? c.value() : "";
/*     */     
/* 219 */     validateCookie(name, value);
/*     */     
/* 221 */     if (c.wrap()) {
/* 222 */       CookieUtil.addQuoted(buf, name, value);
/*     */     } else {
/* 224 */       CookieUtil.add(buf, name, value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\cookie\ClientCookieEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */