/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ @GwtCompatible
/*     */ public class Joiner
/*     */ {
/*     */   private final String separator;
/*     */   
/*     */   public static Joiner on(String separator) {
/*  67 */     return new Joiner(separator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Joiner on(char separator) {
/*  74 */     return new Joiner(String.valueOf(separator));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Joiner(String separator) {
/*  80 */     this.separator = Preconditions.<String>checkNotNull(separator);
/*     */   }
/*     */   
/*     */   private Joiner(Joiner prototype) {
/*  84 */     this.separator = prototype.separator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <A extends Appendable> A appendTo(A appendable, Iterable<?> parts) throws IOException {
/*  93 */     return appendTo(appendable, parts.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
/* 104 */     Preconditions.checkNotNull(appendable);
/* 105 */     if (parts.hasNext()) {
/* 106 */       appendable.append(toString(parts.next()));
/* 107 */       while (parts.hasNext()) {
/* 108 */         appendable.append(this.separator);
/* 109 */         appendable.append(toString(parts.next()));
/*     */       } 
/*     */     } 
/* 112 */     return appendable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final <A extends Appendable> A appendTo(A appendable, Object[] parts) throws IOException {
/* 121 */     return appendTo(appendable, Arrays.asList(parts));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final <A extends Appendable> A appendTo(A appendable, @Nullable Object first, @Nullable Object second, Object... rest) throws IOException {
/* 131 */     return appendTo(appendable, iterable(first, second, rest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final StringBuilder appendTo(StringBuilder builder, Iterable<?> parts) {
/* 141 */     return appendTo(builder, parts.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final StringBuilder appendTo(StringBuilder builder, Iterator<?> parts) {
/*     */     try {
/* 154 */       appendTo(builder, parts);
/* 155 */     } catch (IOException impossible) {
/* 156 */       throw new AssertionError(impossible);
/*     */     } 
/* 158 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final StringBuilder appendTo(StringBuilder builder, Object[] parts) {
/* 168 */     return appendTo(builder, Arrays.asList(parts));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final StringBuilder appendTo(StringBuilder builder, @Nullable Object first, @Nullable Object second, Object... rest) {
/* 179 */     return appendTo(builder, iterable(first, second, rest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(Iterable<?> parts) {
/* 187 */     return join(parts.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(Iterator<?> parts) {
/* 197 */     return appendTo(new StringBuilder(), parts).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(Object[] parts) {
/* 205 */     return join(Arrays.asList(parts));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String join(@Nullable Object first, @Nullable Object second, Object... rest) {
/* 213 */     return join(iterable(first, second, rest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Joiner useForNull(final String nullText) {
/* 221 */     Preconditions.checkNotNull(nullText);
/* 222 */     return new Joiner(this)
/*     */       {
/*     */         CharSequence toString(@Nullable Object part) {
/* 225 */           return (part == null) ? nullText : Joiner.this.toString(part);
/*     */         }
/*     */ 
/*     */         
/*     */         public Joiner useForNull(String nullText) {
/* 230 */           throw new UnsupportedOperationException("already specified useForNull");
/*     */         }
/*     */ 
/*     */         
/*     */         public Joiner skipNulls() {
/* 235 */           throw new UnsupportedOperationException("already specified useForNull");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Joiner skipNulls() {
/* 245 */     return new Joiner(this)
/*     */       {
/*     */         public <A extends Appendable> A appendTo(A appendable, Iterator<?> parts) throws IOException {
/* 248 */           Preconditions.checkNotNull(appendable, "appendable");
/* 249 */           Preconditions.checkNotNull(parts, "parts");
/* 250 */           while (parts.hasNext()) {
/* 251 */             Object part = parts.next();
/* 252 */             if (part != null) {
/* 253 */               appendable.append(Joiner.this.toString(part));
/*     */               break;
/*     */             } 
/*     */           } 
/* 257 */           while (parts.hasNext()) {
/* 258 */             Object part = parts.next();
/* 259 */             if (part != null) {
/* 260 */               appendable.append(Joiner.this.separator);
/* 261 */               appendable.append(Joiner.this.toString(part));
/*     */             } 
/*     */           } 
/* 264 */           return appendable;
/*     */         }
/*     */ 
/*     */         
/*     */         public Joiner useForNull(String nullText) {
/* 269 */           throw new UnsupportedOperationException("already specified skipNulls");
/*     */         }
/*     */ 
/*     */         
/*     */         public Joiner.MapJoiner withKeyValueSeparator(String kvs) {
/* 274 */           throw new UnsupportedOperationException("can't use .skipNulls() with maps");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapJoiner withKeyValueSeparator(char keyValueSeparator) {
/* 286 */     return withKeyValueSeparator(String.valueOf(keyValueSeparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapJoiner withKeyValueSeparator(String keyValueSeparator) {
/* 294 */     return new MapJoiner(this, keyValueSeparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class MapJoiner
/*     */   {
/*     */     private final Joiner joiner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String keyValueSeparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private MapJoiner(Joiner joiner, String keyValueSeparator) {
/* 320 */       this.joiner = joiner;
/* 321 */       this.keyValueSeparator = Preconditions.<String>checkNotNull(keyValueSeparator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public <A extends Appendable> A appendTo(A appendable, Map<?, ?> map) throws IOException {
/* 330 */       return appendTo(appendable, map.entrySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public StringBuilder appendTo(StringBuilder builder, Map<?, ?> map) {
/* 340 */       return appendTo(builder, map.entrySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String join(Map<?, ?> map) {
/* 348 */       return join(map.entrySet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     @CanIgnoreReturnValue
/*     */     public <A extends Appendable> A appendTo(A appendable, Iterable<? extends Map.Entry<?, ?>> entries) throws IOException {
/* 361 */       return appendTo(appendable, entries.iterator());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     @CanIgnoreReturnValue
/*     */     public <A extends Appendable> A appendTo(A appendable, Iterator<? extends Map.Entry<?, ?>> parts) throws IOException {
/* 374 */       Preconditions.checkNotNull(appendable);
/* 375 */       if (parts.hasNext()) {
/* 376 */         Map.Entry<?, ?> entry = parts.next();
/* 377 */         appendable.append(this.joiner.toString(entry.getKey()));
/* 378 */         appendable.append(this.keyValueSeparator);
/* 379 */         appendable.append(this.joiner.toString(entry.getValue()));
/* 380 */         while (parts.hasNext()) {
/* 381 */           appendable.append(this.joiner.separator);
/* 382 */           Map.Entry<?, ?> e = parts.next();
/* 383 */           appendable.append(this.joiner.toString(e.getKey()));
/* 384 */           appendable.append(this.keyValueSeparator);
/* 385 */           appendable.append(this.joiner.toString(e.getValue()));
/*     */         } 
/*     */       } 
/* 388 */       return appendable;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     @CanIgnoreReturnValue
/*     */     public StringBuilder appendTo(StringBuilder builder, Iterable<? extends Map.Entry<?, ?>> entries) {
/* 401 */       return appendTo(builder, entries.iterator());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     @CanIgnoreReturnValue
/*     */     public StringBuilder appendTo(StringBuilder builder, Iterator<? extends Map.Entry<?, ?>> entries) {
/*     */       try {
/* 415 */         appendTo(builder, entries);
/* 416 */       } catch (IOException impossible) {
/* 417 */         throw new AssertionError(impossible);
/*     */       } 
/* 419 */       return builder;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     public String join(Iterable<? extends Map.Entry<?, ?>> entries) {
/* 430 */       return join(entries.iterator());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Beta
/*     */     public String join(Iterator<? extends Map.Entry<?, ?>> entries) {
/* 441 */       return appendTo(new StringBuilder(), entries).toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MapJoiner useForNull(String nullText) {
/* 449 */       return new MapJoiner(this.joiner.useForNull(nullText), this.keyValueSeparator);
/*     */     }
/*     */   }
/*     */   
/*     */   CharSequence toString(Object part) {
/* 454 */     Preconditions.checkNotNull(part);
/* 455 */     return (part instanceof CharSequence) ? (CharSequence)part : part.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static Iterable<Object> iterable(final Object first, final Object second, final Object[] rest) {
/* 460 */     Preconditions.checkNotNull(rest);
/* 461 */     return new AbstractList()
/*     */       {
/*     */         public int size() {
/* 464 */           return rest.length + 2;
/*     */         }
/*     */ 
/*     */         
/*     */         public Object get(int index) {
/* 469 */           switch (index) {
/*     */             case 0:
/* 471 */               return first;
/*     */             case 1:
/* 473 */               return second;
/*     */           } 
/* 475 */           return rest[index - 2];
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\Joiner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */