/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.advancements.critereon.MinMaxBounds;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ 
/*    */ public interface RangeArgument<T extends MinMaxBounds<?>>
/*    */   extends ArgumentType<T> {
/*    */   public static class Ints implements RangeArgument<MinMaxBounds.Ints> {
/* 15 */     private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0..5", "0", "-5", "-100..", "..100" });
/*    */     
/*    */     public static MinMaxBounds.Ints getRange(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 18 */       return (MinMaxBounds.Ints)debug0.getArgument(debug1, MinMaxBounds.Ints.class);
/*    */     }
/*    */ 
/*    */     
/*    */     public MinMaxBounds.Ints parse(StringReader debug1) throws CommandSyntaxException {
/* 23 */       return MinMaxBounds.Ints.fromReader(debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public Collection<String> getExamples() {
/* 28 */       return EXAMPLES;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Floats implements RangeArgument<MinMaxBounds.Floats> {
/* 33 */     private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0..5.2", "0", "-5.4", "-100.76..", "..100" });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public MinMaxBounds.Floats parse(StringReader debug1) throws CommandSyntaxException {
/* 41 */       return MinMaxBounds.Floats.fromReader(debug1);
/*    */     }
/*    */ 
/*    */     
/*    */     public Collection<String> getExamples() {
/* 46 */       return EXAMPLES;
/*    */     }
/*    */   }
/*    */   
/*    */   static Ints intRange() {
/* 51 */     return new Ints();
/*    */   }
/*    */   
/*    */   static Floats floatRange() {
/* 55 */     return new Floats();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\RangeArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */