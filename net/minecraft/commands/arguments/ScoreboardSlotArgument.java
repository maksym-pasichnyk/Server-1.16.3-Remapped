/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.scores.Scoreboard;
/*    */ 
/*    */ public class ScoreboardSlotArgument implements ArgumentType<Integer> {
/* 20 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "sidebar", "foo.bar" }); static {
/* 21 */     ERROR_INVALID_VALUE = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.scoreboardDisplaySlot.invalid", new Object[] { debug0 }));
/*    */   }
/*    */   
/*    */   public static final DynamicCommandExceptionType ERROR_INVALID_VALUE;
/*    */   
/*    */   public static ScoreboardSlotArgument displaySlot() {
/* 27 */     return new ScoreboardSlotArgument();
/*    */   }
/*    */   
/*    */   public static int getDisplaySlot(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 31 */     return ((Integer)debug0.getArgument(debug1, Integer.class)).intValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Integer parse(StringReader debug1) throws CommandSyntaxException {
/* 36 */     String debug2 = debug1.readUnquotedString();
/* 37 */     int debug3 = Scoreboard.getDisplaySlotByName(debug2);
/* 38 */     if (debug3 == -1) {
/* 39 */       throw ERROR_INVALID_VALUE.create(debug2);
/*    */     }
/* 41 */     return Integer.valueOf(debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 46 */     return SharedSuggestionProvider.suggest(Scoreboard.getDisplaySlotNames(), debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 51 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\ScoreboardSlotArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */