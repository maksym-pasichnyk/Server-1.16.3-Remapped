/*     */ package com.mojang.brigadier.suggestion;
/*     */ 
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.context.StringRange;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Suggestion
/*     */   implements Comparable<Suggestion>
/*     */ {
/*     */   private final StringRange range;
/*     */   private final String text;
/*     */   private final Message tooltip;
/*     */   
/*     */   public Suggestion(StringRange range, String text) {
/*  17 */     this(range, text, null);
/*     */   }
/*     */   
/*     */   public Suggestion(StringRange range, String text, Message tooltip) {
/*  21 */     this.range = range;
/*  22 */     this.text = text;
/*  23 */     this.tooltip = tooltip;
/*     */   }
/*     */   
/*     */   public StringRange getRange() {
/*  27 */     return this.range;
/*     */   }
/*     */   
/*     */   public String getText() {
/*  31 */     return this.text;
/*     */   }
/*     */   
/*     */   public Message getTooltip() {
/*  35 */     return this.tooltip;
/*     */   }
/*     */   
/*     */   public String apply(String input) {
/*  39 */     if (this.range.getStart() == 0 && this.range.getEnd() == input.length()) {
/*  40 */       return this.text;
/*     */     }
/*  42 */     StringBuilder result = new StringBuilder();
/*  43 */     if (this.range.getStart() > 0) {
/*  44 */       result.append(input.substring(0, this.range.getStart()));
/*     */     }
/*  46 */     result.append(this.text);
/*  47 */     if (this.range.getEnd() < input.length()) {
/*  48 */       result.append(input.substring(this.range.getEnd()));
/*     */     }
/*  50 */     return result.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  55 */     if (this == o) {
/*  56 */       return true;
/*     */     }
/*  58 */     if (!(o instanceof Suggestion)) {
/*  59 */       return false;
/*     */     }
/*  61 */     Suggestion that = (Suggestion)o;
/*  62 */     return (Objects.equals(this.range, that.range) && Objects.equals(this.text, that.text) && Objects.equals(this.tooltip, that.tooltip));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  67 */     return Objects.hash(new Object[] { this.range, this.text, this.tooltip });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  72 */     return "Suggestion{range=" + this.range + ", text='" + this.text + '\'' + ", tooltip='" + this.tooltip + '\'' + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Suggestion o) {
/*  81 */     return this.text.compareTo(o.text);
/*     */   }
/*     */   
/*     */   public int compareToIgnoreCase(Suggestion b) {
/*  85 */     return this.text.compareToIgnoreCase(b.text);
/*     */   }
/*     */   
/*     */   public Suggestion expand(String command, StringRange range) {
/*  89 */     if (range.equals(this.range)) {
/*  90 */       return this;
/*     */     }
/*  92 */     StringBuilder result = new StringBuilder();
/*  93 */     if (range.getStart() < this.range.getStart()) {
/*  94 */       result.append(command.substring(range.getStart(), this.range.getStart()));
/*     */     }
/*  96 */     result.append(this.text);
/*  97 */     if (range.getEnd() > this.range.getEnd()) {
/*  98 */       result.append(command.substring(this.range.getEnd(), range.getEnd()));
/*     */     }
/* 100 */     return new Suggestion(range, result.toString(), this.tooltip);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\suggestion\Suggestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */