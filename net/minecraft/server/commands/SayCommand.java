/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.ChatType;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class SayCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 19 */     debug0.register(
/* 20 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("say")
/* 21 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 22 */         .then(
/* 23 */           Commands.argument("message", (ArgumentType)MessageArgument.message())
/* 24 */           .executes(debug0 -> {
/*    */               Component debug1 = MessageArgument.getMessage(debug0, "message");
/*    */               TranslatableComponent debug2 = new TranslatableComponent("chat.type.announcement", new Object[] { ((CommandSourceStack)debug0.getSource()).getDisplayName(), debug1 });
/*    */               Entity debug3 = ((CommandSourceStack)debug0.getSource()).getEntity();
/*    */               if (debug3 != null) {
/*    */                 ((CommandSourceStack)debug0.getSource()).getServer().getPlayerList().broadcastMessage((Component)debug2, ChatType.CHAT, debug3.getUUID());
/*    */               } else {
/*    */                 ((CommandSourceStack)debug0.getSource()).getServer().getPlayerList().broadcastMessage((Component)debug2, ChatType.SYSTEM, Util.NIL_UUID);
/*    */               } 
/*    */               return 1;
/*    */             })));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\SayCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */