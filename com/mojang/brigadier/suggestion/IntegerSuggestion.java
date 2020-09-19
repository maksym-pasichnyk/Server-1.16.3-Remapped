/*    */ package com.mojang.brigadier.suggestion;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.context.StringRange;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IntegerSuggestion
/*    */   extends Suggestion
/*    */ {
/*    */   private int value;
/*    */   
/*    */   public IntegerSuggestion(StringRange range, int value) {
/* 15 */     this(range, value, (Message)null);
/*    */   }
/*    */   
/*    */   public IntegerSuggestion(StringRange range, int value, Message tooltip) {
/* 19 */     super(range, Integer.toString(value), tooltip);
/* 20 */     this.value = value;
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 24 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 29 */     if (this == o) {
/* 30 */       return true;
/*    */     }
/* 32 */     if (!(o instanceof IntegerSuggestion)) {
/* 33 */       return false;
/*    */     }
/* 35 */     IntegerSuggestion that = (IntegerSuggestion)o;
/* 36 */     return (this.value == that.value && super.equals(o));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 41 */     return Objects.hash(new Object[] { Integer.valueOf(super.hashCode()), Integer.valueOf(this.value) });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 46 */     return "IntegerSuggestion{value=" + this.value + ", range=" + 
/*    */       
/* 48 */       getRange() + ", text='" + 
/* 49 */       getText() + '\'' + ", tooltip='" + 
/* 50 */       getTooltip() + '\'' + '}';
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compareTo(Suggestion o) {
/* 56 */     if (o instanceof IntegerSuggestion) {
/* 57 */       return Integer.compare(this.value, ((IntegerSuggestion)o).value);
/*    */     }
/* 59 */     return super.compareTo(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareToIgnoreCase(Suggestion b) {
/* 64 */     return compareTo(b);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\suggestion\IntegerSuggestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */