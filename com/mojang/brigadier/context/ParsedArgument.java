/*    */ package com.mojang.brigadier.context;
/*    */ 
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParsedArgument<S, T>
/*    */ {
/*    */   private final StringRange range;
/*    */   private final T result;
/*    */   
/*    */   public ParsedArgument(int start, int end, T result) {
/* 13 */     this.range = StringRange.between(start, end);
/* 14 */     this.result = result;
/*    */   }
/*    */   
/*    */   public StringRange getRange() {
/* 18 */     return this.range;
/*    */   }
/*    */   
/*    */   public T getResult() {
/* 22 */     return this.result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 27 */     if (this == o) {
/* 28 */       return true;
/*    */     }
/* 30 */     if (!(o instanceof ParsedArgument)) {
/* 31 */       return false;
/*    */     }
/* 33 */     ParsedArgument<?, ?> that = (ParsedArgument<?, ?>)o;
/* 34 */     return (Objects.equals(this.range, that.range) && Objects.equals(this.result, that.result));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 39 */     return Objects.hash(new Object[] { this.range, this.result });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\context\ParsedArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */