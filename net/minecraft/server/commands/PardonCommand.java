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
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.players.UserBanList;
/*    */ 
/*    */ public class PardonCommand {
/* 22 */   private static final SimpleCommandExceptionType ERROR_NOT_BANNED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.pardon.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 25 */     debug0.register(
/* 26 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("pardon")
/* 27 */         .requires(debug0 -> debug0.hasPermission(3)))
/* 28 */         .then(
/* 29 */           Commands.argument("targets", (ArgumentType)GameProfileArgument.gameProfile())
/* 30 */           .suggests((debug0, debug1) -> SharedSuggestionProvider.suggest(((CommandSourceStack)debug0.getSource()).getServer().getPlayerList().getBans().getUserList(), debug1))
/* 31 */           .executes(debug0 -> pardonPlayers((CommandSourceStack)debug0.getSource(), GameProfileArgument.getGameProfiles(debug0, "targets")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int pardonPlayers(CommandSourceStack debug0, Collection<GameProfile> debug1) throws CommandSyntaxException {
/* 37 */     UserBanList debug2 = debug0.getServer().getPlayerList().getBans();
/* 38 */     int debug3 = 0;
/*    */     
/* 40 */     for (GameProfile debug5 : debug1) {
/* 41 */       if (debug2.isBanned(debug5)) {
/* 42 */         debug2.remove(debug5);
/* 43 */         debug3++;
/* 44 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.pardon.success", new Object[] { ComponentUtils.getDisplayName(debug5) }), true);
/*    */       } 
/*    */     } 
/*    */     
/* 48 */     if (debug3 == 0) {
/* 49 */       throw ERROR_NOT_BANNED.create();
/*    */     }
/*    */     
/* 52 */     return debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\PardonCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */