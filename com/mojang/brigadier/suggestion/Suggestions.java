/*     */ package com.mojang.brigadier.suggestion;
/*     */ 
/*     */ import com.mojang.brigadier.context.StringRange;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Suggestions
/*     */ {
/*  17 */   private static final Suggestions EMPTY = new Suggestions(StringRange.at(0), new ArrayList<>());
/*     */   
/*     */   private final StringRange range;
/*     */   private final List<Suggestion> suggestions;
/*     */   
/*     */   public Suggestions(StringRange range, List<Suggestion> suggestions) {
/*  23 */     this.range = range;
/*  24 */     this.suggestions = suggestions;
/*     */   }
/*     */   
/*     */   public StringRange getRange() {
/*  28 */     return this.range;
/*     */   }
/*     */   
/*     */   public List<Suggestion> getList() {
/*  32 */     return this.suggestions;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  36 */     return this.suggestions.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  41 */     if (this == o) {
/*  42 */       return true;
/*     */     }
/*  44 */     if (!(o instanceof Suggestions)) {
/*  45 */       return false;
/*     */     }
/*  47 */     Suggestions that = (Suggestions)o;
/*  48 */     return (Objects.equals(this.range, that.range) && 
/*  49 */       Objects.equals(this.suggestions, that.suggestions));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  54 */     return Objects.hash(new Object[] { this.range, this.suggestions });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  59 */     return "Suggestions{range=" + this.range + ", suggestions=" + this.suggestions + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CompletableFuture<Suggestions> empty() {
/*  66 */     return CompletableFuture.completedFuture(EMPTY);
/*     */   }
/*     */   
/*     */   public static Suggestions merge(String command, Collection<Suggestions> input) {
/*  70 */     if (input.isEmpty())
/*  71 */       return EMPTY; 
/*  72 */     if (input.size() == 1) {
/*  73 */       return input.iterator().next();
/*     */     }
/*     */     
/*  76 */     Set<Suggestion> texts = new HashSet<>();
/*  77 */     for (Suggestions suggestions : input) {
/*  78 */       texts.addAll(suggestions.getList());
/*     */     }
/*  80 */     return create(command, texts);
/*     */   }
/*     */   
/*     */   public static Suggestions create(String command, Collection<Suggestion> suggestions) {
/*  84 */     if (suggestions.isEmpty()) {
/*  85 */       return EMPTY;
/*     */     }
/*  87 */     int start = Integer.MAX_VALUE;
/*  88 */     int end = Integer.MIN_VALUE;
/*  89 */     for (Suggestion suggestion : suggestions) {
/*  90 */       start = Math.min(suggestion.getRange().getStart(), start);
/*  91 */       end = Math.max(suggestion.getRange().getEnd(), end);
/*     */     } 
/*  93 */     StringRange range = new StringRange(start, end);
/*  94 */     Set<Suggestion> texts = new HashSet<>();
/*  95 */     for (Suggestion suggestion : suggestions) {
/*  96 */       texts.add(suggestion.expand(command, range));
/*     */     }
/*  98 */     List<Suggestion> sorted = new ArrayList<>(texts);
/*  99 */     sorted.sort((a, b) -> a.compareToIgnoreCase(b));
/* 100 */     return new Suggestions(range, sorted);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\suggestion\Suggestions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */