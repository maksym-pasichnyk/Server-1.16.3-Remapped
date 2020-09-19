/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.AngleArgument;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class SetSpawnCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 26 */     debug0.register(
/* 27 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spawnpoint")
/* 28 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 29 */         .executes(debug0 -> setSpawn((CommandSourceStack)debug0.getSource(), Collections.singleton(((CommandSourceStack)debug0.getSource()).getPlayerOrException()), new BlockPos(((CommandSourceStack)debug0.getSource()).getPosition()), 0.0F)))
/* 30 */         .then((
/* 31 */           (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 32 */           .executes(debug0 -> setSpawn((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), new BlockPos(((CommandSourceStack)debug0.getSource()).getPosition()), 0.0F)))
/* 33 */           .then((
/* 34 */             (RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/* 35 */             .executes(debug0 -> setSpawn((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), BlockPosArgument.getOrLoadBlockPos(debug0, "pos"), 0.0F)))
/* 36 */             .then(
/* 37 */               Commands.argument("angle", (ArgumentType)AngleArgument.angle())
/* 38 */               .executes(debug0 -> setSpawn((CommandSourceStack)debug0.getSource(), EntityArgument.getPlayers(debug0, "targets"), BlockPosArgument.getOrLoadBlockPos(debug0, "pos"), AngleArgument.getAngle(debug0, "angle")))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int setSpawn(CommandSourceStack debug0, Collection<ServerPlayer> debug1, BlockPos debug2, float debug3) {
/* 46 */     ResourceKey<Level> debug4 = debug0.getLevel().dimension();
/* 47 */     for (ServerPlayer debug6 : debug1) {
/* 48 */       debug6.setRespawnPosition(debug4, debug2, debug3, true, false);
/*    */     }
/*    */     
/* 51 */     String debug5 = debug4.location().toString();
/* 52 */     if (debug1.size() == 1) {
/* 53 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.spawnpoint.success.single", new Object[] { Integer.valueOf(debug2.getX()), Integer.valueOf(debug2.getY()), Integer.valueOf(debug2.getZ()), Float.valueOf(debug3), debug5, ((ServerPlayer)debug1.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 55 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.spawnpoint.success.multiple", new Object[] { Integer.valueOf(debug2.getX()), Integer.valueOf(debug2.getY()), Integer.valueOf(debug2.getZ()), Float.valueOf(debug3), debug5, Integer.valueOf(debug1.size()) }), true);
/*    */     } 
/*    */     
/* 58 */     return debug1.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\SetSpawnCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */