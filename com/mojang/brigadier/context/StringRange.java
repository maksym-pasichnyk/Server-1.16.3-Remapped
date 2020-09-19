/*    */ package com.mojang.brigadier.context;
/*    */ 
/*    */ import com.mojang.brigadier.ImmutableStringReader;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StringRange
/*    */ {
/*    */   private final int start;
/*    */   private final int end;
/*    */   
/*    */   public StringRange(int start, int end) {
/* 15 */     this.start = start;
/* 16 */     this.end = end;
/*    */   }
/*    */   
/*    */   public static StringRange at(int pos) {
/* 20 */     return new StringRange(pos, pos);
/*    */   }
/*    */   
/*    */   public static StringRange between(int start, int end) {
/* 24 */     return new StringRange(start, end);
/*    */   }
/*    */   
/*    */   public static StringRange encompassing(StringRange a, StringRange b) {
/* 28 */     return new StringRange(Math.min(a.getStart(), b.getStart()), Math.max(a.getEnd(), b.getEnd()));
/*    */   }
/*    */   
/*    */   public int getStart() {
/* 32 */     return this.start;
/*    */   }
/*    */   
/*    */   public int getEnd() {
/* 36 */     return this.end;
/*    */   }
/*    */   
/*    */   public String get(ImmutableStringReader reader) {
/* 40 */     return reader.getString().substring(this.start, this.end);
/*    */   }
/*    */   
/*    */   public String get(String string) {
/* 44 */     return string.substring(this.start, this.end);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 48 */     return (this.start == this.end);
/*    */   }
/*    */   
/*    */   public int getLength() {
/* 52 */     return this.end - this.start;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 57 */     if (this == o) {
/* 58 */       return true;
/*    */     }
/* 60 */     if (!(o instanceof StringRange)) {
/* 61 */       return false;
/*    */     }
/* 63 */     StringRange that = (StringRange)o;
/* 64 */     return (this.start == that.start && this.end == that.end);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 69 */     return Objects.hash(new Object[] { Integer.valueOf(this.start), Integer.valueOf(this.end) });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 74 */     return "StringRange{start=" + this.start + ", end=" + this.end + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\context\StringRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */