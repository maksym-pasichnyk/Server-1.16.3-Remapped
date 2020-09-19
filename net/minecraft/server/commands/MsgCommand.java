/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*    */ import java.util.Collection;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class MsgCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 26 */     LiteralCommandNode<CommandSourceStack> debug1 = debug0.register(
/* 27 */         (LiteralArgumentBuilder)Commands.literal("msg")
/* 28 */         .then(
/* 29 */           Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 30 */           .then(
/* 31 */             Commands.argument("message", (ArgumentType)MessageArgument.message())
/* 32 */             .executes(debug0 -> sendMessage((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), MessageArgument.getMessage(debug0, "message"))))));
/*    */ 
/*    */ 
/*    */     
/* 36 */     debug0.register((LiteralArgumentBuilder)Commands.literal("tell").redirect((CommandNode)debug1));
/* 37 */     debug0.register((LiteralArgumentBuilder)Commands.literal("w").redirect((CommandNode)debug1));
/*    */   }
/*    */   private static int sendMessage(CommandSourceStack debug0, Collection<ServerPlayer> debug1, Component debug2) {
/*    */     Consumer<Component> debug4;
/* 41 */     UUID debug3 = (debug0.getEntity() == null) ? Util.NIL_UUID : debug0.getEntity().getUUID();
/*    */ 
/*    */     
/* 44 */     Entity debug5 = debug0.getEntity();
/* 45 */     if (debug5 instanceof ServerPlayer) {
/* 46 */       ServerPlayer debug6 = (ServerPlayer)debug5;
/* 47 */       debug4 = (debug2 -> debug0.sendMessage((Component)(new TranslatableComponent("commands.message.display.outgoing", new Object[] { debug2, debug1 })).withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC }), debug0.getUUID()));
/*    */     } else {
/* 49 */       debug4 = (debug2 -> debug0.sendSuccess((Component)(new TranslatableComponent("commands.message.display.outgoing", new Object[] { debug2, debug1 })).withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC }), false));
/*    */     } 
/*    */     
/* 52 */     for (ServerPlayer debug7 : debug1) {
/* 53 */       debug4.accept(debug7.getDisplayName());
/* 54 */       debug7.sendMessage((Component)(new TranslatableComponent("commands.message.display.incoming", new Object[] { debug0.getDisplayName(), debug2 })).withStyle(new ChatFormatting[] { ChatFormatting.GRAY, ChatFormatting.ITALIC }), debug3);
/*    */     } 
/*    */     
/* 57 */     return debug1.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\MsgCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */