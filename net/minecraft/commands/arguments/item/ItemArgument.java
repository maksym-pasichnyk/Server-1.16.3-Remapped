/*    */ package net.minecraft.commands.arguments.item;
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
/*    */ import net.minecraft.tags.ItemTags;
/*    */ 
/*    */ public class ItemArgument
/*    */   implements ArgumentType<ItemInput> {
/* 16 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "stick", "minecraft:stick", "stick{foo=bar}" });
/*    */   
/*    */   public static ItemArgument item() {
/* 19 */     return new ItemArgument();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemInput parse(StringReader debug1) throws CommandSyntaxException {
/* 24 */     ItemParser debug2 = (new ItemParser(debug1, false)).parse();
/*    */     
/* 26 */     return new ItemInput(debug2.getItem(), debug2.getNbt());
/*    */   }
/*    */   
/*    */   public static <S> ItemInput getItem(CommandContext<S> debug0, String debug1) {
/* 30 */     return (ItemInput)debug0.getArgument(debug1, ItemInput.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 35 */     StringReader debug3 = new StringReader(debug2.getInput());
/* 36 */     debug3.setCursor(debug2.getStart());
/* 37 */     ItemParser debug4 = new ItemParser(debug3, false);
/*    */     try {
/* 39 */       debug4.parse();
/* 40 */     } catch (CommandSyntaxException commandSyntaxException) {}
/*    */     
/* 42 */     return debug4.fillSuggestions(debug2, ItemTags.getAllTags());
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 47 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\item\ItemArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */