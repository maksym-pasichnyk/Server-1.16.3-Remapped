/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.HoverEvent;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.scores.PlayerTeam;
/*    */ 
/*    */ public class TeamMsgCommand {
/* 25 */   private static final Style SUGGEST_STYLE = Style.EMPTY
/* 26 */     .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("chat.type.team.hover")))
/* 27 */     .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/teammsg "));
/*    */   
/* 29 */   private static final SimpleCommandExceptionType ERROR_NOT_ON_TEAM = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.teammsg.failed.noteam"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 32 */     LiteralCommandNode<CommandSourceStack> debug1 = debug0.register(
/* 33 */         (LiteralArgumentBuilder)Commands.literal("teammsg")
/* 34 */         .then(
/* 35 */           Commands.argument("message", (ArgumentType)MessageArgument.message())
/* 36 */           .executes(debug0 -> sendMessage((CommandSourceStack)debug0.getSource(), MessageArgument.getMessage(debug0, "message")))));
/*    */ 
/*    */     
/* 39 */     debug0.register((LiteralArgumentBuilder)Commands.literal("tm").redirect((CommandNode)debug1));
/*    */   }
/*    */   
/*    */   private static int sendMessage(CommandSourceStack debug0, Component debug1) throws CommandSyntaxException {
/* 43 */     Entity debug2 = debug0.getEntityOrException();
/* 44 */     PlayerTeam debug3 = (PlayerTeam)debug2.getTeam();
/* 45 */     if (debug3 == null) {
/* 46 */       throw ERROR_NOT_ON_TEAM.create();
/*    */     }
/*    */     
/* 49 */     MutableComponent mutableComponent = debug3.getFormattedDisplayName().withStyle(SUGGEST_STYLE);
/*    */     
/* 51 */     List<ServerPlayer> debug5 = debug0.getServer().getPlayerList().getPlayers();
/* 52 */     for (ServerPlayer debug7 : debug5) {
/* 53 */       if (debug7 == debug2) {
/* 54 */         debug7.sendMessage((Component)new TranslatableComponent("chat.type.team.sent", new Object[] { mutableComponent, debug0.getDisplayName(), debug1 }), debug2.getUUID()); continue;
/* 55 */       }  if (debug7.getTeam() == debug3) {
/* 56 */         debug7.sendMessage((Component)new TranslatableComponent("chat.type.team.text", new Object[] { mutableComponent, debug0.getDisplayName(), debug1 }), debug2.getUUID());
/*    */       }
/*    */     } 
/*    */     
/* 60 */     return debug5.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\TeamMsgCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */