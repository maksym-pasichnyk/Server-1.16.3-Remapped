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
/*    */ public class DoubleArgumentType
/*    */   implements ArgumentType<Double>
/*    */ {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0", "1.2", ".5", "-1", "-.5", "-1234.56" });
/*    */   
/*    */   private final double minimum;
/*    */   private final double maximum;
/*    */   
/*    */   private DoubleArgumentType(double minimum, double maximum) {
/* 20 */     this.minimum = minimum;
/* 21 */     this.maximum = maximum;
/*    */   }
/*    */   
/*    */   public static DoubleArgumentType doubleArg() {
/* 25 */     return doubleArg(-1.7976931348623157E308D);
/*    */   }
/*    */   
/*    */   public static DoubleArgumentType doubleArg(double min) {
/* 29 */     return doubleArg(min, Double.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public static DoubleArgumentType doubleArg(double min, double max) {
/* 33 */     return new DoubleArgumentType(min, max);
/*    */   }
/*    */   
/*    */   public static double getDouble(CommandContext<?> context, String name) {
/* 37 */     return ((Double)context.getArgument(name, Double.class)).doubleValue();
/*    */   }
/*    */   
/*    */   public double getMinimum() {
/* 41 */     return this.minimum;
/*    */   }
/*    */   
/*    */   public double getMaximum() {
/* 45 */     return this.maximum;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double parse(StringReader reader) throws CommandSyntaxException {
/* 50 */     int start = reader.getCursor();
/* 51 */     double result = reader.readDouble();
/* 52 */     if (result < this.minimum) {
/* 53 */       reader.setCursor(start);
/* 54 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.doubleTooLow().createWithContext(reader, Double.valueOf(result), Double.valueOf(this.minimum));
/*    */     } 
/* 56 */     if (result > this.maximum) {
/* 57 */       reader.setCursor(start);
/* 58 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.doubleTooHigh().createWithContext(reader, Double.valueOf(result), Double.valueOf(this.maximum));
/*    */     } 
/* 60 */     return Double.valueOf(result);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 65 */     if (this == o) return true; 
/* 66 */     if (!(o instanceof DoubleArgumentType)) return false;
/*    */     
/* 68 */     DoubleArgumentType that = (DoubleArgumentType)o;
/* 69 */     return (this.maximum == that.maximum && this.minimum == that.minimum);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 74 */     return (int)(31.0D * this.minimum + this.maximum);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     if (this.minimum == -1.7976931348623157E308D && this.maximum == Double.MAX_VALUE)
/* 80 */       return "double()"; 
/* 81 */     if (this.maximum == Double.MAX_VALUE) {
/* 82 */       return "double(" + this.minimum + ")";
/*    */     }
/* 84 */     return "double(" + this.minimum + ", " + this.maximum + ")";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 90 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\arguments\DoubleArgumentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */