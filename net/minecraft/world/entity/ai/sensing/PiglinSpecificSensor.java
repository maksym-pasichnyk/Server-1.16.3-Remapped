/*     */ package net.minecraft.world.entity.ai.sensing;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
/*     */ import net.minecraft.world.entity.monster.piglin.Piglin;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinAi;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PiglinSpecificSensor
/*     */   extends Sensor<LivingEntity>
/*     */ {
/*     */   public Set<MemoryModuleType<?>> requires() {
/*  36 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, (Object[])new MemoryModuleType[] { MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_REPELLENT });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doTick(ServerLevel debug1, LivingEntity debug2) {
/*  57 */     Brain<?> debug3 = debug2.getBrain();
/*     */     
/*  59 */     debug3.setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(debug1, debug2));
/*     */     
/*  61 */     Optional<Mob> debug4 = Optional.empty();
/*  62 */     Optional<Hoglin> debug5 = Optional.empty();
/*  63 */     Optional<Hoglin> debug6 = Optional.empty();
/*  64 */     Optional<Piglin> debug7 = Optional.empty();
/*  65 */     Optional<LivingEntity> debug8 = Optional.empty();
/*  66 */     Optional<Player> debug9 = Optional.empty();
/*  67 */     Optional<Player> debug10 = Optional.empty();
/*  68 */     int debug11 = 0;
/*     */     
/*  70 */     List<AbstractPiglin> debug12 = Lists.newArrayList();
/*  71 */     List<AbstractPiglin> debug13 = Lists.newArrayList();
/*     */ 
/*     */     
/*  74 */     List<LivingEntity> debug14 = (List<LivingEntity>)debug3.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(ImmutableList.of());
/*  75 */     for (LivingEntity debug16 : debug14) {
/*     */       
/*  77 */       if (debug16 instanceof Hoglin) {
/*  78 */         Hoglin debug17 = (Hoglin)debug16;
/*  79 */         if (debug17.isBaby() && !debug6.isPresent()) {
/*  80 */           debug6 = Optional.of(debug17); continue;
/*  81 */         }  if (debug17.isAdult()) {
/*  82 */           debug11++;
/*  83 */           if (!debug5.isPresent() && debug17.canBeHunted())
/*  84 */             debug5 = Optional.of(debug17); 
/*     */         } 
/*     */         continue;
/*     */       } 
/*  88 */       if (debug16 instanceof net.minecraft.world.entity.monster.piglin.PiglinBrute) {
/*  89 */         debug12.add(debug16); continue;
/*     */       } 
/*  91 */       if (debug16 instanceof Piglin) {
/*  92 */         Piglin debug17 = (Piglin)debug16;
/*  93 */         if (debug17.isBaby() && !debug7.isPresent()) {
/*  94 */           debug7 = Optional.of(debug17); continue;
/*  95 */         }  if (debug17.isAdult())
/*  96 */           debug12.add(debug17); 
/*     */         continue;
/*     */       } 
/*  99 */       if (debug16 instanceof Player) {
/* 100 */         Player debug17 = (Player)debug16;
/* 101 */         if (!debug9.isPresent() && EntitySelector.ATTACK_ALLOWED.test(debug16) && !PiglinAi.isWearingGold((LivingEntity)debug17)) {
/* 102 */           debug9 = Optional.of(debug17);
/*     */         }
/* 104 */         if (!debug10.isPresent() && !debug17.isSpectator() && PiglinAi.isPlayerHoldingLovedItem((LivingEntity)debug17))
/* 105 */           debug10 = Optional.of(debug17); 
/*     */         continue;
/*     */       } 
/* 108 */       if (!debug4.isPresent() && (debug16 instanceof net.minecraft.world.entity.monster.WitherSkeleton || debug16 instanceof net.minecraft.world.entity.boss.wither.WitherBoss)) {
/* 109 */         debug4 = Optional.of((Mob)debug16); continue;
/*     */       } 
/* 111 */       if (!debug8.isPresent() && PiglinAi.isZombified(debug16.getType())) {
/* 112 */         debug8 = Optional.of(debug16);
/*     */       }
/*     */     } 
/*     */     
/* 116 */     List<LivingEntity> debug15 = (List<LivingEntity>)debug3.getMemory(MemoryModuleType.LIVING_ENTITIES).orElse(ImmutableList.of());
/* 117 */     for (LivingEntity debug17 : debug15) {
/* 118 */       if (debug17 instanceof AbstractPiglin && ((AbstractPiglin)debug17).isAdult()) {
/* 119 */         debug13.add((AbstractPiglin)debug17);
/*     */       }
/*     */     } 
/*     */     
/* 123 */     debug3.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, debug4);
/* 124 */     debug3.setMemory(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, debug5);
/* 125 */     debug3.setMemory(MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, debug6);
/* 126 */     debug3.setMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, debug8);
/* 127 */     debug3.setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, debug9);
/* 128 */     debug3.setMemory(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, debug10);
/* 129 */     debug3.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, debug13);
/* 130 */     debug3.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, debug12);
/* 131 */     debug3.setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, Integer.valueOf(debug12.size()));
/* 132 */     debug3.setMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, Integer.valueOf(debug11));
/*     */   }
/*     */   
/*     */   private static Optional<BlockPos> findNearestRepellent(ServerLevel debug0, LivingEntity debug1) {
/* 136 */     return BlockPos.findClosestMatch(debug1
/* 137 */         .blockPosition(), 8, 4, debug1 -> isValidRepellent(debug0, debug1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isValidRepellent(ServerLevel debug0, BlockPos debug1) {
/* 145 */     BlockState debug2 = debug0.getBlockState(debug1);
/* 146 */     boolean debug3 = debug2.is((Tag)BlockTags.PIGLIN_REPELLENTS);
/* 147 */     if (debug3 && debug2.is(Blocks.SOUL_CAMPFIRE)) {
/* 148 */       return CampfireBlock.isLitCampfire(debug2);
/*     */     }
/* 150 */     return debug3;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\PiglinSpecificSensor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */