/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class KillCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 19 */     debug0.register(
/* 20 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("kill")
/* 21 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 22 */         .executes(debug0 -> kill((CommandSourceStack)debug0.getSource(), (Collection<? extends Entity>)ImmutableList.of(((CommandSourceStack)debug0.getSource()).getEntityOrException()))))
/* 23 */         .then(
/* 24 */           Commands.argument("targets", (ArgumentType)EntityArgument.entities())
/* 25 */           .executes(debug0 -> kill((CommandSourceStack)debug0.getSource(), EntityArgument.getEntities(debug0, "targets")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int kill(CommandSourceStack debug0, Collection<? extends Entity> debug1) {
/* 31 */     for (Entity debug3 : debug1) {
/* 32 */       debug3.kill();
/*    */     }
/*    */     
/* 35 */     if (debug1.size() == 1) {
/* 36 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.kill.success.single", new Object[] { ((Entity)debug1.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 38 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.kill.success.multiple", new Object[] { Integer.valueOf(debug1.size()) }), true);
/*    */     } 
/*    */     
/* 41 */     return debug1.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\KillCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */