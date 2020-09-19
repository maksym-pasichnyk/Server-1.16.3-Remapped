/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.brigadier.CommandDispatcher;
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
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ 
/*    */ public class OpCommand {
/* 21 */   private static final SimpleCommandExceptionType ERROR_ALREADY_OP = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.op.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 24 */     debug0.register(
/* 25 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("op")
/* 26 */         .requires(debug0 -> debug0.hasPermission(3)))
/* 27 */         .then(
/* 28 */           Commands.argument("targets", (ArgumentType)GameProfileArgument.gameProfile())
/* 29 */           .suggests((debug0, debug1) -> {
/*    */               PlayerList debug2 = ((CommandSourceStack)debug0.getSource()).getServer().getPlayerList();
/*    */               
/*    */               return SharedSuggestionProvider.suggest(debug2.getPlayers().stream().filter(()).map(()), debug1);
/* 33 */             }).executes(debug0 -> opPlayers((CommandSourceStack)debug0.getSource(), GameProfileArgument.getGameProfiles(debug0, "targets")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int opPlayers(CommandSourceStack debug0, Collection<GameProfile> debug1) throws CommandSyntaxException {
/* 39 */     PlayerList debug2 = debug0.getServer().getPlayerList();
/* 40 */     int debug3 = 0;
/*    */     
/* 42 */     for (GameProfile debug5 : debug1) {
/* 43 */       if (!debug2.isOp(debug5)) {
/* 44 */         debug2.op(debug5);
/* 45 */         debug3++;
/* 46 */         debug0.sendSuccess((Component)new TranslatableComponent("commands.op.success", new Object[] { ((GameProfile)debug1.iterator().next()).getName() }), true);
/*    */       } 
/*    */     } 
/*    */     
/* 50 */     if (debug3 == 0) {
/* 51 */       throw ERROR_ALREADY_OP.create();
/*    */     }
/*    */     
/* 54 */     return debug3;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\OpCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */