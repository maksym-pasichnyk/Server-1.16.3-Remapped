/*    */ package com.mojang.brigadier.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LongArgumentType
/*    */   implements ArgumentType<Long>
/*    */ {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0", "123", "-123" });
/*    */   
/*    */   private final long minimum;
/*    */   private final long maximum;
/*    */   
/*    */   private LongArgumentType(long minimum, long maximum) {
/* 20 */     this.minimum = minimum;
/* 21 */     this.maximum = maximum;
/*    */   }
/*    */   
/*    */   public static LongArgumentType longArg() {
/* 25 */     return longArg(Long.MIN_VALUE);
/*    */   }
/*    */   
/*    */   public static LongArgumentType longArg(long min) {
/* 29 */     return longArg(min, Long.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public static LongArgumentType longArg(long min, long max) {
/* 33 */     return new LongArgumentType(min, max);
/*    */   }
/*    */   
/*    */   public static long getLong(CommandContext<?> context, String name) {
/* 37 */     return ((Long)context.getArgument(name, long.class)).longValue();
/*    */   }
/*    */   
/*    */   public long getMinimum() {
/* 41 */     return this.minimum;
/*    */   }
/*    */   
/*    */   public long getMaximum() {
/* 45 */     return this.maximum;
/*    */   }
/*    */ 
/*    */   
/*    */   public Long parse(StringReader reader) throws CommandSyntaxException {
/* 50 */     int start = reader.getCursor();
/* 51 */     long result = reader.readLong();
/* 52 */     if (result < this.minimum) {
/* 53 */       reader.setCursor(start);
/* 54 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.longTooLow().createWithContext(reader, Long.valueOf(result), Long.valueOf(this.minimum));
/*    */     } 
/* 56 */     if (result > this.maximum) {
/* 57 */       reader.setCursor(start);
/* 58 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.longTooHigh().createWithContext(reader, Long.valueOf(result), Long.valueOf(this.maximum));
/*    */     } 
/* 60 */     return Long.valueOf(result);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 65 */     if (this == o) return true; 
/* 66 */     if (!(o instanceof LongArgumentType)) return false;
/*    */     
/* 68 */     LongArgumentType that = (LongArgumentType)o;
/* 69 */     return (this.maximum == that.maximum && this.minimum == that.minimum);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 74 */     return 31 * Long.hashCode(this.minimum) + Long.hashCode(this.maximum);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     if (this.minimum == Long.MIN_VALUE && this.maximum == Long.MAX_VALUE)
/* 80 */       return "longArg()"; 
/* 81 */     if (this.maximum == Long.MAX_VALUE) {
/* 82 */       return "longArg(" + this.minimum + ")";
/*    */     }
/* 84 */     return "longArg(" + this.minimum + ", " + this.maximum + ")";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 90 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\arguments\LongArgumentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */