/*    */ package net.minecraft.commands.arguments.blocks;
/*    */ 
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ 
/*    */ public class BlockStateArgument
/*    */   implements ArgumentType<BlockInput> {
/* 17 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "stone", "minecraft:stone", "stone[foo=bar]", "foo{bar=baz}" });
/*    */   
/*    */   public static BlockStateArgument block() {
/* 20 */     return new BlockStateArgument();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockInput parse(StringReader debug1) throws CommandSyntaxException {
/* 25 */     BlockStateParser debug2 = (new BlockStateParser(debug1, false)).parse(true);
/* 26 */     return new BlockInput(debug2.getState(), debug2.getProperties().keySet(), debug2.getNbt());
/*    */   }
/*    */   
/*    */   public static BlockInput getBlock(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 30 */     return (BlockInput)debug0.getArgument(debug1, BlockInput.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 35 */     StringReader debug3 = new StringReader(debug2.getInput());
/* 36 */     debug3.setCursor(debug2.getStart());
/* 37 */     BlockStateParser debug4 = new BlockStateParser(debug3, false);
/*    */     try {
/* 39 */       debug4.parse(true);
/* 40 */     } catch (CommandSyntaxException commandSyntaxException) {}
/*    */     
/* 42 */     return debug4.fillSuggestions(debug2, BlockTags.getAllTags());
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 47 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\blocks\BlockStateArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */