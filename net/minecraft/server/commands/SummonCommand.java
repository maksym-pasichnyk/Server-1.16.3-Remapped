/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.CompoundTagArgument;
/*    */ import net.minecraft.commands.arguments.EntitySummonArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*    */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.MobSpawnType;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class SummonCommand {
/* 31 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.summon.failed"));
/* 32 */   private static final SimpleCommandExceptionType ERROR_DUPLICATE_UUID = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.summon.failed.uuid"));
/* 33 */   private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.summon.invalidPosition"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 36 */     debug0.register(
/* 37 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("summon")
/* 38 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 39 */         .then((
/* 40 */           (RequiredArgumentBuilder)Commands.argument("entity", (ArgumentType)EntitySummonArgument.id())
/* 41 */           .suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
/* 42 */           .executes(debug0 -> spawnEntity((CommandSourceStack)debug0.getSource(), EntitySummonArgument.getSummonableEntity(debug0, "entity"), ((CommandSourceStack)debug0.getSource()).getPosition(), new CompoundTag(), true)))
/* 43 */           .then((
/* 44 */             (RequiredArgumentBuilder)Commands.argument("pos", (ArgumentType)Vec3Argument.vec3())
/* 45 */             .executes(debug0 -> spawnEntity((CommandSourceStack)debug0.getSource(), EntitySummonArgument.getSummonableEntity(debug0, "entity"), Vec3Argument.getVec3(debug0, "pos"), new CompoundTag(), true)))
/* 46 */             .then(
/* 47 */               Commands.argument("nbt", (ArgumentType)CompoundTagArgument.compoundTag())
/* 48 */               .executes(debug0 -> spawnEntity((CommandSourceStack)debug0.getSource(), EntitySummonArgument.getSummonableEntity(debug0, "entity"), Vec3Argument.getVec3(debug0, "pos"), CompoundTagArgument.getCompoundTag(debug0, "nbt"), false))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int spawnEntity(CommandSourceStack debug0, ResourceLocation debug1, Vec3 debug2, CompoundTag debug3, boolean debug4) throws CommandSyntaxException {
/* 56 */     BlockPos debug5 = new BlockPos(debug2);
/* 57 */     if (!Level.isInSpawnableBounds(debug5)) {
/* 58 */       throw INVALID_POSITION.create();
/*    */     }
/*    */     
/* 61 */     CompoundTag debug6 = debug3.copy();
/* 62 */     debug6.putString("id", debug1.toString());
/*    */     
/* 64 */     ServerLevel debug7 = debug0.getLevel();
/* 65 */     Entity debug8 = EntityType.loadEntityRecursive(debug6, (Level)debug7, debug1 -> {
/*    */           debug1.moveTo(debug0.x, debug0.y, debug0.z, debug1.yRot, debug1.xRot);
/*    */           return debug1;
/*    */         });
/* 69 */     if (debug8 == null) {
/* 70 */       throw ERROR_FAILED.create();
/*    */     }
/*    */     
/* 73 */     if (debug4 && debug8 instanceof Mob) {
/* 74 */       ((Mob)debug8).finalizeSpawn((ServerLevelAccessor)debug0.getLevel(), debug0.getLevel().getCurrentDifficultyAt(debug8.blockPosition()), MobSpawnType.COMMAND, null, null);
/*    */     }
/*    */     
/* 77 */     if (!debug7.tryAddFreshEntityWithPassengers(debug8)) {
/* 78 */       throw ERROR_DUPLICATE_UUID.create();
/*    */     }
/*    */     
/* 81 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.summon.success", new Object[] { debug8.getDisplayName() }), true);
/* 82 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\SummonCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */