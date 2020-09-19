/*    */ package net.minecraft.commands.arguments;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
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
/*    */ import net.minecraft.server.ServerScoreboard;
/*    */ import net.minecraft.world.scores.Objective;
/*    */ 
/*    */ public class ObjectiveArgument implements ArgumentType<String> {
/*    */   private static final DynamicCommandExceptionType ERROR_OBJECTIVE_NOT_FOUND;
/*    */   private static final DynamicCommandExceptionType ERROR_OBJECTIVE_READ_ONLY;
/* 21 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "*", "012" }); public static final DynamicCommandExceptionType ERROR_OBJECTIVE_NAME_TOO_LONG; static {
/* 22 */     ERROR_OBJECTIVE_NOT_FOUND = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("arguments.objective.notFound", new Object[] { debug0 }));
/* 23 */     ERROR_OBJECTIVE_READ_ONLY = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("arguments.objective.readonly", new Object[] { debug0 }));
/* 24 */     ERROR_OBJECTIVE_NAME_TOO_LONG = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.scoreboard.objectives.add.longName", new Object[] { debug0 }));
/*    */   }
/*    */   public static ObjectiveArgument objective() {
/* 27 */     return new ObjectiveArgument();
/*    */   }
/*    */   
/*    */   public static Objective getObjective(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 31 */     String debug2 = (String)debug0.getArgument(debug1, String.class);
/* 32 */     ServerScoreboard serverScoreboard = ((CommandSourceStack)debug0.getSource()).getServer().getScoreboard();
/* 33 */     Objective debug4 = serverScoreboard.getObjective(debug2);
/* 34 */     if (debug4 == null) {
/* 35 */       throw ERROR_OBJECTIVE_NOT_FOUND.create(debug2);
/*    */     }
/* 37 */     return debug4;
/*    */   }
/*    */   
/*    */   public static Objective getWritableObjective(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 41 */     Objective debug2 = getObjective(debug0, debug1);
/* 42 */     if (debug2.getCriteria().isReadOnly()) {
/* 43 */       throw ERROR_OBJECTIVE_READ_ONLY.create(debug2.getName());
/*    */     }
/* 45 */     return debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String parse(StringReader debug1) throws CommandSyntaxException {
/* 50 */     String debug2 = debug1.readUnquotedString();
/* 51 */     if (debug2.length() > 16) {
/* 52 */       throw ERROR_OBJECTIVE_NAME_TOO_LONG.create(Integer.valueOf(16));
/*    */     }
/* 54 */     return debug2;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 60 */     if (debug1.getSource() instanceof CommandSourceStack)
/* 61 */       return SharedSuggestionProvider.suggest(((CommandSourceStack)debug1.getSource()).getServer().getScoreboard().getObjectiveNames(), debug2); 
/* 62 */     if (debug1.getSource() instanceof SharedSuggestionProvider) {
/* 63 */       SharedSuggestionProvider debug3 = (SharedSuggestionProvider)debug1.getSource();
/* 64 */       return debug3.customSuggestion(debug1, debug2);
/*    */     } 
/* 66 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 72 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\ObjectiveArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */