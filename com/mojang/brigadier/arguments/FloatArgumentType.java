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
/*    */ public class FloatArgumentType
/*    */   implements ArgumentType<Float>
/*    */ {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0", "1.2", ".5", "-1", "-.5", "-1234.56" });
/*    */   
/*    */   private final float minimum;
/*    */   private final float maximum;
/*    */   
/*    */   private FloatArgumentType(float minimum, float maximum) {
/* 20 */     this.minimum = minimum;
/* 21 */     this.maximum = maximum;
/*    */   }
/*    */   
/*    */   public static FloatArgumentType floatArg() {
/* 25 */     return floatArg(-3.4028235E38F);
/*    */   }
/*    */   
/*    */   public static FloatArgumentType floatArg(float min) {
/* 29 */     return floatArg(min, Float.MAX_VALUE);
/*    */   }
/*    */   
/*    */   public static FloatArgumentType floatArg(float min, float max) {
/* 33 */     return new FloatArgumentType(min, max);
/*    */   }
/*    */   
/*    */   public static float getFloat(CommandContext<?> context, String name) {
/* 37 */     return ((Float)context.getArgument(name, Float.class)).floatValue();
/*    */   }
/*    */   
/*    */   public float getMinimum() {
/* 41 */     return this.minimum;
/*    */   }
/*    */   
/*    */   public float getMaximum() {
/* 45 */     return this.maximum;
/*    */   }
/*    */ 
/*    */   
/*    */   public Float parse(StringReader reader) throws CommandSyntaxException {
/* 50 */     int start = reader.getCursor();
/* 51 */     float result = reader.readFloat();
/* 52 */     if (result < this.minimum) {
/* 53 */       reader.setCursor(start);
/* 54 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.floatTooLow().createWithContext(reader, Float.valueOf(result), Float.valueOf(this.minimum));
/*    */     } 
/* 56 */     if (result > this.maximum) {
/* 57 */       reader.setCursor(start);
/* 58 */       throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.floatTooHigh().createWithContext(reader, Float.valueOf(result), Float.valueOf(this.maximum));
/*    */     } 
/* 60 */     return Float.valueOf(result);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 65 */     if (this == o) return true; 
/* 66 */     if (!(o instanceof FloatArgumentType)) return false;
/*    */     
/* 68 */     FloatArgumentType that = (FloatArgumentType)o;
/* 69 */     return (this.maximum == that.maximum && this.minimum == that.minimum);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 74 */     return (int)(31.0F * this.minimum + this.maximum);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     if (this.minimum == -3.4028235E38F && this.maximum == Float.MAX_VALUE)
/* 80 */       return "float()"; 
/* 81 */     if (this.maximum == Float.MAX_VALUE) {
/* 82 */       return "float(" + this.minimum + ")";
/*    */     }
/* 84 */     return "float(" + this.minimum + ", " + this.maximum + ")";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 90 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\brigadier\arguments\FloatArgumentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */