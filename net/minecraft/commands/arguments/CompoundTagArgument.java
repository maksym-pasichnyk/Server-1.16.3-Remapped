/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ 
/*    */ public class CompoundTagArgument
/*    */   implements ArgumentType<CompoundTag> {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "{}", "{foo=bar}" });
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CompoundTagArgument compoundTag() {
/* 20 */     return new CompoundTagArgument();
/*    */   }
/*    */   
/*    */   public static <S> CompoundTag getCompoundTag(CommandContext<S> debug0, String debug1) {
/* 24 */     return (CompoundTag)debug0.getArgument(debug1, CompoundTag.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag parse(StringReader debug1) throws CommandSyntaxException {
/* 29 */     return (new TagParser(debug1)).readStruct();
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 34 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\CompoundTagArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */