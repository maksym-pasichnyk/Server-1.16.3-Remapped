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
/*    */ import net.minecraft.server.ServerScoreboard;
/*    */ import net.minecraft.world.scores.PlayerTeam;
/*    */ 
/*    */ public class TeamArgument implements ArgumentType<String> {
/* 21 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "foo", "123" }); static {
/* 22 */     ERROR_TEAM_NOT_FOUND = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("team.notFound", new Object[] { debug0 }));
/*    */   }
/*    */   
/*    */   private static final DynamicCommandExceptionType ERROR_TEAM_NOT_FOUND;
/*    */   
/*    */   public static TeamArgument team() {
/* 28 */     return new TeamArgument();
/*    */   }
/*    */   
/*    */   public static PlayerTeam getTeam(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 32 */     String debug2 = (String)debug0.getArgument(debug1, String.class);
/* 33 */     ServerScoreboard serverScoreboard = ((CommandSourceStack)debug0.getSource()).getServer().getScoreboard();
/* 34 */     PlayerTeam debug4 = serverScoreboard.getPlayerTeam(debug2);
/* 35 */     if (debug4 == null) {
/* 36 */       throw ERROR_TEAM_NOT_FOUND.create(debug2);
/*    */     }
/* 38 */     return debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   public String parse(StringReader debug1) throws CommandSyntaxException {
/* 43 */     return debug1.readUnquotedString();
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 48 */     if (debug1.getSource() instanceof SharedSuggestionProvider) {
/* 49 */       return SharedSuggestionProvider.suggest(((SharedSuggestionProvider)debug1.getSource()).getAllTeams(), debug2);
/*    */     }
/* 51 */     return Suggestions.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 56 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\TeamArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */