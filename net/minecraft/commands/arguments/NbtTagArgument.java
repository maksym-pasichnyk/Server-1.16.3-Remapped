/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.nbt.TagParser;
/*    */ 
/*    */ public class NbtTagArgument
/*    */   implements ArgumentType<Tag> {
/* 14 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "0", "0b", "0l", "0.0", "\"foo\"", "{foo=bar}", "[0]" });
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static NbtTagArgument nbtTag() {
/* 20 */     return new NbtTagArgument();
/*    */   }
/*    */   
/*    */   public static <S> Tag getNbtTag(CommandContext<S> debug0, String debug1) {
/* 24 */     return (Tag)debug0.getArgument(debug1, Tag.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public Tag parse(StringReader debug1) throws CommandSyntaxException {
/* 29 */     return (new TagParser(debug1)).readValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 34 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\NbtTagArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */