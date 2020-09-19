/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.regex.Matcher;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.players.IpBanList;
/*    */ 
/*    */ public class PardonIpCommand {
/* 20 */   private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.pardonip.invalid"));
/* 21 */   private static final SimpleCommandExceptionType ERROR_NOT_BANNED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.pardonip.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 24 */     debug0.register(
/* 25 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("pardon-ip")
/* 26 */         .requires(debug0 -> debug0.hasPermission(3)))
/* 27 */         .then(
/* 28 */           Commands.argument("target", (ArgumentType)StringArgumentType.word())
/* 29 */           .suggests((debug0, debug1) -> SharedSuggestionProvider.suggest(((CommandSourceStack)debug0.getSource()).getServer().getPlayerList().getIpBans().getUserList(), debug1))
/* 30 */           .executes(debug0 -> unban((CommandSourceStack)debug0.getSource(), StringArgumentType.getString(debug0, "target")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int unban(CommandSourceStack debug0, String debug1) throws CommandSyntaxException {
/* 36 */     Matcher debug2 = BanIpCommands.IP_ADDRESS_PATTERN.matcher(debug1);
/* 37 */     if (!debug2.matches()) {
/* 38 */       throw ERROR_INVALID.create();
/*    */     }
/*    */     
/* 41 */     IpBanList debug3 = debug0.getServer().getPlayerList().getIpBans();
/* 42 */     if (!debug3.isBanned(debug1)) {
/* 43 */       throw ERROR_NOT_BANNED.create();
/*    */     }
/*    */     
/* 46 */     debug3.remove(debug1);
/* 47 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.pardonip.success", new Object[] { debug1 }), true);
/* 48 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\PardonIpCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */