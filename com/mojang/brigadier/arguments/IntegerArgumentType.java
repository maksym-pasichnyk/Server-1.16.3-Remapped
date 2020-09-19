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
/*    */ public class IntegerArgumentType
/*    */   implements ArgumentType<Integer>
/*    */ {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0", "123", "-123" });
/*    */   
/*    */   private final int minimum;
/*    */   private final int maximum;
/*    */   
/*    */   private IntegerArgumentType(int minimum, int maximum) {
/* 20 */     this.minimum = minimum;
/* 21 */     this.maximum = maximum;
/*    */   }
/*    */   
/*    */   public static IntegerArgumentType integer() {
/* 25 */     return integer(-2147483648);
/*    */   }
/*    */   
/*    */   public static IntegerArgumentType integer(int min) {
/* 29 */     return integer(min, 2147483647);
/*    */   }
/*    */   
/*    */   public static IntegerArgumentType integer(int min, int max) {
/* 33 */     return new IntegerArgumentType(min, max);
/*    */   }
/*    */   
/*    */   public static int getInteger(CommandContext<?> context, String name) {
/* 37 */     return ((Integer)context.getArgument(name, int.class)).intValue();
/*    */   }
/*    */   
/*    */   public int getMinimum() {
/* 41 */     return this.minimum;
/*    */   }
/*    */   
/*    */   public int getMaximum() {
/* 45 */     return this.maximum;
/*    */   }
/*    */ 
/*    */   
/*    */   public Integer parse(StringReader reader) throws CommandSyntaxException {
/* 50 */     int start = reader.getCursor();
/* 51 */     int result = reader.readInt();
/* 52 */     if (result < this.minimum) {
/* 53 */       reader.setCursor(start);
/* 54 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooLow().createWithContext(reader, Integer.valueOf(result), Integer.valueOf(this.minimum));
/*    */     } 
/* 56 */     if (result > this.maximum) {
/* 57 */       reader.setCursor(start);
/* 58 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooHigh().createWithContext(reader, Integer.valueOf(result), Integer.valueOf(this.maximum));
/*    */     } 
/* 60 */     return Integer.valueOf(result);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 65 */     if (this == o) return true; 
/* 66 */     if (!(o instanceof IntegerArgumentType)) return false;
/*    */     
/* 68 */     IntegerArgumentType that = (IntegerArgumentType)o;
/* 69 */     return (this.maximum == that.maximum && this.minimum == that.minimum);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 74 */     return 31 * this.minimum + this.maximum;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     if (this.minimum == Integer.MIN_VALUE && this.maximum == Integer.MAX_VALUE)
/* 80 */       return "integer()"; 
/* 81 */     if (this.maximum == Integer.MAX_VALUE) {
/* 82 */       return "integer(" + this.minimum + ")";
/*    */     }
/* 84 */     return "integer(" + this.minimum + ", " + this.maximum + ")";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 90 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\arguments\IntegerArgumentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */