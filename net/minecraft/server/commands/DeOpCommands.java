/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.commands.arguments.GameProfileArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ 
/*    */ public class DeOpCommands {
/* 21 */   private static final SimpleCommandExceptionType ERROR_NOT_OP = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.deop.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 24 */     debug0.register(
/* 25 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("deop")
/* 26 */         .requires(debug0 -> debug0.hasPermission(3)))
/* 27 */         .then(
/* 28 */           Commands.argument("targets", (ArgumentType)GameProfileArgument.gameProfile())
/* 29 */           .suggests((debug0, debug1) -> SharedSuggestionProvider.suggest(((CommandSourceStack)debug0.getSource()).getServer().getPlayerList().getOpNames(), debug1))
/* 30 */           .executes(debug0 -> deopPlayers((CommandSourceStack)debug0.getSource(), GameProfileArgument.getGameProfiles(debug0, "targets")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int deopPlayers(CommandSourceStack debug0, Collection<GameProfile> debug1) throws CommandSyntaxException {
/* 36 */     PlayerList debug2 = debug0.getServer().getPlayerList();
/* 37 */     int debug3 = 0;
/*    */     
/* 39 */     for (GameProfile debug5 : debug1) {
/* 40 */       if (debug2.isOp(debug5)) {
/* 41 */         debug2.deop(debug5);
/* 42 */         debug3++;
/* 43 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.deop.success", new Object[] { ((GameProfile)debug1.iterator().next()).getName() }), true);
/*    */       } 
/*    */     } 
/*    */     
/* 47 */     if (debug3 == 0) {
/* 48 */       throw ERROR_NOT_OP.create();
/*    */     }
/*    */     
/* 51 */     debug0.getServer().kickUnlistedPlayers(debug0);
/* 52 */     return debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\DeOpCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */