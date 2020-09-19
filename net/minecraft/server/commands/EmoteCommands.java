/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.ChatType;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class EmoteCommands {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 17 */     debug0.register(
/* 18 */         (LiteralArgumentBuilder)Commands.literal("me")
/* 19 */         .then(
/* 20 */           Commands.argument("action", (ArgumentType)StringArgumentType.greedyString()).executes(debug0 -> {
/*    */               TranslatableComponent debug1 = new TranslatableComponent("chat.type.emote", new Object[] { ((CommandSourceStack)debug0.getSource()).getDisplayName(), StringArgumentType.getString(debug0, "action") });
/*    */               Entity debug2 = ((CommandSourceStack)debug0.getSource()).getEntity();
/*    */               if (debug2 != null) {
/*    */                 ((CommandSourceStack)debug0.getSource()).getServer().getPlayerList().broadcastMessage((Component)debug1, ChatType.CHAT, debug2.getUUID());
/*    */               } else {
/*    */                 ((CommandSourceStack)debug0.getSource()).getServer().getPlayerList().broadcastMessage((Component)debug1, ChatType.SYSTEM, Util.NIL_UUID);
/*    */               } 
/*    */               return 1;
/*    */             })));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\EmoteCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */