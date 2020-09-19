/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.ParticleArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParticleCommand
/*    */ {
/* 30 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.particle.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 33 */     debug0.register(
/* 34 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("particle")
/* 35 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 36 */         .then((
/* 37 */           (RequiredArgumentBuilder)Commands.argument("name", (ArgumentType)ParticleArgument.particle())
/* 38 */           .executes(debug0 -> sendParticles((CommandSourceStack)debug0.getSource(), ParticleArgument.getParticle(debug0, "name"), ((CommandSourceStack)debug0.getSource()).getPosition(), Vec3.ZERO, 0.0F, 0, false, ((CommandSourceStack)debug0.getSource()).getServer().getPlayerList().getPlayers())))
/* 39 */           .then((
/* 40 */             (RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)Vec3Argument.vec3())
/* 41 */             .executes(debug0 -> sendParticles((CommandSourceStack)debug0.getSource(), ParticleArgument.getParticle(debug0, "name"), Vec3Argument.getVec3(debug0, "pos"), Vec3.ZERO, 0.0F, 0, false, ((CommandSourceStack)debug0.getSource()).getServer().getPlayerList().getPlayers())))
/* 42 */             .then(
/* 43 */               Commands.argument("delta", (ArgumentType)Vec3Argument.vec3(false))
/* 44 */               .then(
/* 45 */                 Commands.argument("speed", (ArgumentType)FloatArgumentType.floatArg(0.0F))
/* 46 */                 .then((
/* 47 */                   (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(0))
/* 48 */                   .executes(debug0 -> sendParticles((CommandSourceStack)debug0.getSource(), ParticleArgument.getParticle(debug0, "name"), Vec3Argument.getVec3(debug0, "pos"), Vec3Argument.getVec3(debug0, "delta"), FloatArgumentType.getFloat(debug0, "speed"), IntegerArgumentType.getInteger(debug0, "count"), false, ((CommandSourceStack)debug0.getSource()).getServer().getPlayerList().getPlayers())))
/* 49 */                   .then((
/* 50 */                     (LiteralArgumentBuilder)Commands.literal("force")
/* 51 */                     .executes(debug0 -> sendParticles((CommandSourceStack)debug0.getSource(), ParticleArgument.getParticle(debug0, "name"), Vec3Argument.getVec3(debug0, "pos"), Vec3Argument.getVec3(debug0, "delta"), FloatArgumentType.getFloat(debug0, "speed"), IntegerArgumentType.getInteger(debug0, "count"), true, ((CommandSourceStack)debug0.getSource()).getServer().getPlayerList().getPlayers())))
/* 52 */                     .then(
/* 53 */                       Commands.argument("viewers", (ArgumentType)EntityArgument.players())
/* 54 */                       .executes(debug0 -> sendParticles((CommandSourceStack)debug0.getSource(), ParticleArgument.getParticle(debug0, "name"), Vec3Argument.getVec3(debug0, "pos"), Vec3Argument.getVec3(debug0, "delta"), FloatArgumentType.getFloat(debug0, "speed"), IntegerArgumentType.getInteger(debug0, "count"), true, EntityArgument.getPlayers(debug0, "viewers"))))))
/*    */ 
/*    */                   
/* 57 */                   .then((
/* 58 */                     (LiteralArgumentBuilder)Commands.literal("normal")
/* 59 */                     .executes(debug0 -> sendParticles((CommandSourceStack)debug0.getSource(), ParticleArgument.getParticle(debug0, "name"), Vec3Argument.getVec3(debug0, "pos"), Vec3Argument.getVec3(debug0, "delta"), FloatArgumentType.getFloat(debug0, "speed"), IntegerArgumentType.getInteger(debug0, "count"), false, ((CommandSourceStack)debug0.getSource()).getServer().getPlayerList().getPlayers())))
/* 60 */                     .then(
/* 61 */                       Commands.argument("viewers", (ArgumentType)EntityArgument.players())
/* 62 */                       .executes(debug0 -> sendParticles((CommandSourceStack)debug0.getSource(), ParticleArgument.getParticle(debug0, "name"), Vec3Argument.getVec3(debug0, "pos"), Vec3Argument.getVec3(debug0, "delta"), FloatArgumentType.getFloat(debug0, "speed"), IntegerArgumentType.getInteger(debug0, "count"), false, EntityArgument.getPlayers(debug0, "viewers")))))))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int sendParticles(CommandSourceStack debug0, ParticleOptions debug1, Vec3 debug2, Vec3 debug3, float debug4, int debug5, boolean debug6, Collection<ServerPlayer> debug7) throws CommandSyntaxException {
/* 74 */     int debug8 = 0;
/*    */     
/* 76 */     for (ServerPlayer debug10 : debug7) {
/* 77 */       if (debug0.getLevel().sendParticles(debug10, debug1, debug6, debug2.x, debug2.y, debug2.z, debug5, debug3.x, debug3.y, debug3.z, debug4)) {
/* 78 */         debug8++;
/*    */       }
/*    */     } 
/*    */     
/* 82 */     if (debug8 == 0) {
/* 83 */       throw ERROR_FAILED.create();
/*    */     }
/*    */     
/* 86 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.particle.success", new Object[] { Registry.PARTICLE_TYPE.getKey(debug1.getType()).toString() }), true);
/*    */     
/* 88 */     return debug8;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\ParticleCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */