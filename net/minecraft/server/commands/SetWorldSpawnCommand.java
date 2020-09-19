/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.AngleArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ 
/*    */ public class SetWorldSpawnCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 18 */     debug0.register(
/* 19 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("setworldspawn")
/* 20 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 21 */         .executes(debug0 -> setSpawn((CommandSourceStack)debug0.getSource(), new BlockPos(((CommandSourceStack)debug0.getSource()).getPosition()), 0.0F)))
/* 22 */         .then((
/* 23 */           (RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)BlockPosArgument.blockPos())
/* 24 */           .executes(debug0 -> setSpawn((CommandSourceStack)debug0.getSource(), BlockPosArgument.getOrLoadBlockPos(debug0, "pos"), 0.0F)))
/* 25 */           .then(
/* 26 */             Commands.argument("angle", (ArgumentType)AngleArgument.angle())
/* 27 */             .executes(debug0 -> setSpawn((CommandSourceStack)debug0.getSource(), BlockPosArgument.getOrLoadBlockPos(debug0, "pos"), AngleArgument.getAngle(debug0, "angle"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int setSpawn(CommandSourceStack debug0, BlockPos debug1, float debug2) {
/* 34 */     debug0.getLevel().setDefaultSpawnPos(debug1, debug2);
/* 35 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.setworldspawn.success", new Object[] { Integer.valueOf(debug1.getX()), Integer.valueOf(debug1.getY()), Integer.valueOf(debug1.getZ()), Float.valueOf(debug2) }), true);
/* 36 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\SetWorldSpawnCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */