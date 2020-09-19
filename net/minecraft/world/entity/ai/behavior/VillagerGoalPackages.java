/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.npc.Villager;
/*     */ import net.minecraft.world.entity.npc.VillagerProfession;
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
/*     */ public class VillagerGoalPackages
/*     */ {
/*     */   public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getCorePackage(VillagerProfession debug0, float debug1) {
/*  35 */     return ImmutableList.of(
/*  36 */         Pair.of(Integer.valueOf(0), new Swim(0.8F)), 
/*  37 */         Pair.of(Integer.valueOf(0), new InteractWithDoor()), 
/*  38 */         Pair.of(Integer.valueOf(0), new LookAtTargetSink(45, 90)), 
/*  39 */         Pair.of(Integer.valueOf(0), new VillagerPanicTrigger()), 
/*  40 */         Pair.of(Integer.valueOf(0), new WakeUp()), 
/*  41 */         Pair.of(Integer.valueOf(0), new ReactToBell()), 
/*  42 */         Pair.of(Integer.valueOf(0), new SetRaidStatus()), 
/*  43 */         Pair.of(Integer.valueOf(0), new ValidateNearbyPoi(debug0.getJobPoiType(), MemoryModuleType.JOB_SITE)), 
/*  44 */         Pair.of(Integer.valueOf(0), new ValidateNearbyPoi(debug0.getJobPoiType(), MemoryModuleType.POTENTIAL_JOB_SITE)), 
/*  45 */         Pair.of(Integer.valueOf(1), new MoveToTargetSink()), 
/*  46 */         Pair.of(Integer.valueOf(2), new PoiCompetitorScan(debug0)), 
/*  47 */         Pair.of(Integer.valueOf(3), new LookAndFollowTradingPlayerSink(debug1)), (Object[])new Pair[] {
/*  48 */           Pair.of(Integer.valueOf(5), new GoToWantedItem<>(debug1, false, 4)), 
/*     */           
/*  50 */           Pair.of(Integer.valueOf(6), new AcquirePoi(debug0.getJobPoiType(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty())), 
/*  51 */           Pair.of(Integer.valueOf(7), new GoToPotentialJobSite(debug1)), 
/*  52 */           Pair.of(Integer.valueOf(8), new YieldJobSite(debug1)), 
/*  53 */           Pair.of(Integer.valueOf(10), new AcquirePoi(PoiType.HOME, MemoryModuleType.HOME, false, Optional.of(Byte.valueOf((byte)14)))), 
/*  54 */           Pair.of(Integer.valueOf(10), new AcquirePoi(PoiType.MEETING, MemoryModuleType.MEETING_POINT, true, Optional.of(Byte.valueOf((byte)14)))), 
/*  55 */           Pair.of(Integer.valueOf(10), new AssignProfessionFromJobSite()), 
/*  56 */           Pair.of(Integer.valueOf(10), new ResetProfession())
/*     */         });
/*     */   }
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getWorkPackage(VillagerProfession debug0, float debug1) {
/*     */     WorkAtPoi debug2;
/*  62 */     if (debug0 == VillagerProfession.FARMER) {
/*  63 */       debug2 = new WorkAtComposter();
/*     */     } else {
/*  65 */       debug2 = new WorkAtPoi();
/*     */     } 
/*     */     
/*  68 */     return ImmutableList.of(
/*  69 */         getMinimalLookBehavior(), 
/*  70 */         Pair.of(Integer.valueOf(5), new RunOne<>((List<Pair<Behavior<? super LivingEntity>, Integer>>)ImmutableList.of(
/*  71 */               Pair.of(debug2, Integer.valueOf(7)), 
/*  72 */               Pair.of(new StrollAroundPoi(MemoryModuleType.JOB_SITE, 0.4F, 4), Integer.valueOf(2)), 
/*  73 */               Pair.of(new StrollToPoi(MemoryModuleType.JOB_SITE, 0.4F, 1, 10), Integer.valueOf(5)), 
/*  74 */               Pair.of(new StrollToPoiList(MemoryModuleType.SECONDARY_JOB_SITE, debug1, 1, 6, MemoryModuleType.JOB_SITE), Integer.valueOf(5)), 
/*  75 */               Pair.of(new HarvestFarmland(), Integer.valueOf((debug0 == VillagerProfession.FARMER) ? 2 : 5)), 
/*  76 */               Pair.of(new UseBonemeal(), Integer.valueOf((debug0 == VillagerProfession.FARMER) ? 4 : 7))))), 
/*     */         
/*  78 */         Pair.of(Integer.valueOf(10), new ShowTradesToPlayer(400, 1600)), 
/*  79 */         Pair.of(Integer.valueOf(10), new SetLookAndInteract(EntityType.PLAYER, 4)), 
/*  80 */         Pair.of(Integer.valueOf(2), new SetWalkTargetFromBlockMemory(MemoryModuleType.JOB_SITE, debug1, 9, 100, 1200)), 
/*  81 */         Pair.of(Integer.valueOf(3), new GiveGiftToHero(100)), 
/*  82 */         Pair.of(Integer.valueOf(99), new UpdateActivityFromSchedule()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getPlayPackage(float debug0) {
/*  87 */     return ImmutableList.of(
/*  88 */         Pair.of(Integer.valueOf(0), new MoveToTargetSink(80, 120)), 
/*  89 */         getFullLookBehavior(), 
/*  90 */         Pair.of(Integer.valueOf(5), new PlayTagWithOtherKids()), 
/*  91 */         Pair.of(Integer.valueOf(5), new RunOne<>(
/*  92 */             (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryStatus.VALUE_ABSENT), 
/*     */ 
/*     */ 
/*     */             
/*  96 */             (List<Pair<Behavior<? super LivingEntity>, Integer>>)ImmutableList.of(
/*  97 */               Pair.of(InteractWith.of(EntityType.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, debug0, 2), Integer.valueOf(2)), 
/*  98 */               Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, debug0, 2), Integer.valueOf(1)), 
/*  99 */               Pair.of(new VillageBoundRandomStroll(debug0), Integer.valueOf(1)), 
/* 100 */               Pair.of(new SetWalkTargetFromLookTarget(debug0, 2), Integer.valueOf(1)), 
/* 101 */               Pair.of(new JumpOnBed(debug0), Integer.valueOf(2)), 
/* 102 */               Pair.of(new DoNothing(20, 40), Integer.valueOf(2))))), 
/*     */ 
/*     */         
/* 105 */         Pair.of(Integer.valueOf(99), new UpdateActivityFromSchedule()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getRestPackage(VillagerProfession debug0, float debug1) {
/* 110 */     return ImmutableList.of(
/* 111 */         Pair.of(Integer.valueOf(2), new SetWalkTargetFromBlockMemory(MemoryModuleType.HOME, debug1, 1, 150, 1200)), 
/* 112 */         Pair.of(Integer.valueOf(3), new ValidateNearbyPoi(PoiType.HOME, MemoryModuleType.HOME)), 
/* 113 */         Pair.of(Integer.valueOf(3), new SleepInBed()), 
/* 114 */         Pair.of(Integer.valueOf(5), new RunOne<>(
/* 115 */             (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT), 
/*     */ 
/*     */ 
/*     */             
/* 119 */             (List<Pair<Behavior<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 120 */               Pair.of(new SetClosestHomeAsWalkTarget(debug1), Integer.valueOf(1)), 
/* 121 */               Pair.of(new InsideBrownianWalk(debug1), Integer.valueOf(4)), 
/* 122 */               Pair.of(new GoToClosestVillage(debug1, 4), Integer.valueOf(2)), 
/* 123 */               Pair.of(new DoNothing(20, 40), Integer.valueOf(2))))), 
/*     */ 
/*     */         
/* 126 */         getMinimalLookBehavior(), 
/* 127 */         Pair.of(Integer.valueOf(99), new UpdateActivityFromSchedule()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getMeetPackage(VillagerProfession debug0, float debug1) {
/* 132 */     return ImmutableList.of(
/* 133 */         Pair.of(Integer.valueOf(2), new RunOne<>((List<Pair<Behavior<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 134 */               Pair.of(new StrollAroundPoi(MemoryModuleType.MEETING_POINT, 0.4F, 40), Integer.valueOf(2)), 
/* 135 */               Pair.of(new SocializeAtBell(), Integer.valueOf(2))))), 
/*     */         
/* 137 */         Pair.of(Integer.valueOf(10), new ShowTradesToPlayer(400, 1600)), 
/* 138 */         Pair.of(Integer.valueOf(10), new SetLookAndInteract(EntityType.PLAYER, 4)), 
/* 139 */         Pair.of(Integer.valueOf(2), new SetWalkTargetFromBlockMemory(MemoryModuleType.MEETING_POINT, debug1, 6, 100, 200)), 
/* 140 */         Pair.of(Integer.valueOf(3), new GiveGiftToHero(100)), 
/* 141 */         Pair.of(Integer.valueOf(3), new ValidateNearbyPoi(PoiType.MEETING, MemoryModuleType.MEETING_POINT)), 
/* 142 */         Pair.of(Integer.valueOf(3), new GateBehavior<>(
/* 143 */             (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(), 
/* 144 */             (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, 
/*     */ 
/*     */             
/* 147 */             (List<Pair<Behavior<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 148 */               Pair.of(new TradeWithVillager(), Integer.valueOf(1))))), 
/*     */ 
/*     */         
/* 151 */         getFullLookBehavior(), 
/* 152 */         Pair.of(Integer.valueOf(99), new UpdateActivityFromSchedule()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getIdlePackage(VillagerProfession debug0, float debug1) {
/* 157 */     return ImmutableList.of(
/* 158 */         Pair.of(Integer.valueOf(2), new RunOne<>((List<Pair<Behavior<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 159 */               Pair.of(InteractWith.of(EntityType.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, debug1, 2), Integer.valueOf(2)), 
/* 160 */               Pair.of(new InteractWith<>(EntityType.VILLAGER, 8, AgableMob::canBreed, AgableMob::canBreed, MemoryModuleType.BREED_TARGET, debug1, 2), Integer.valueOf(1)), 
/* 161 */               Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, debug1, 2), Integer.valueOf(1)), 
/* 162 */               Pair.of(new VillageBoundRandomStroll(debug1), Integer.valueOf(1)), 
/* 163 */               Pair.of(new SetWalkTargetFromLookTarget(debug1, 2), Integer.valueOf(1)), 
/* 164 */               Pair.of(new JumpOnBed(debug1), Integer.valueOf(1)), 
/* 165 */               Pair.of(new DoNothing(30, 60), Integer.valueOf(1))))), 
/*     */         
/* 167 */         Pair.of(Integer.valueOf(3), new GiveGiftToHero(100)), 
/* 168 */         Pair.of(Integer.valueOf(3), new SetLookAndInteract(EntityType.PLAYER, 4)), 
/* 169 */         Pair.of(Integer.valueOf(3), new ShowTradesToPlayer(400, 1600)), 
/* 170 */         Pair.of(Integer.valueOf(3), new GateBehavior<>(
/* 171 */             (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(), 
/* 172 */             (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, 
/*     */ 
/*     */             
/* 175 */             (List<Pair<Behavior<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 176 */               Pair.of(new TradeWithVillager(), Integer.valueOf(1))))), 
/*     */ 
/*     */         
/* 179 */         Pair.of(Integer.valueOf(3), new GateBehavior<>(
/* 180 */             (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(), 
/* 181 */             (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.BREED_TARGET), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, 
/*     */ 
/*     */             
/* 184 */             (List<Pair<Behavior<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 185 */               Pair.of(new VillagerMakeLove(), Integer.valueOf(1))))), 
/*     */ 
/*     */         
/* 188 */         getFullLookBehavior(), 
/* 189 */         Pair.of(Integer.valueOf(99), new UpdateActivityFromSchedule()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getPanicPackage(VillagerProfession debug0, float debug1) {
/* 194 */     float debug2 = debug1 * 1.5F;
/*     */     
/* 196 */     return ImmutableList.of(
/* 197 */         Pair.of(Integer.valueOf(0), new VillagerCalmDown()), 
/* 198 */         Pair.of(Integer.valueOf(1), SetWalkTargetAwayFrom.entity(MemoryModuleType.NEAREST_HOSTILE, debug2, 6, false)), 
/* 199 */         Pair.of(Integer.valueOf(1), SetWalkTargetAwayFrom.entity(MemoryModuleType.HURT_BY_ENTITY, debug2, 6, false)), 
/* 200 */         Pair.of(Integer.valueOf(3), new VillageBoundRandomStroll(debug2, 2, 2)), 
/* 201 */         getMinimalLookBehavior());
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getPreRaidPackage(VillagerProfession debug0, float debug1) {
/* 206 */     return ImmutableList.of(
/* 207 */         Pair.of(Integer.valueOf(0), new RingBell()), 
/* 208 */         Pair.of(Integer.valueOf(0), new RunOne<>((List<Pair<Behavior<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 209 */               Pair.of(new SetWalkTargetFromBlockMemory(MemoryModuleType.MEETING_POINT, debug1 * 1.5F, 2, 150, 200), Integer.valueOf(6)), 
/* 210 */               Pair.of(new VillageBoundRandomStroll(debug1 * 1.5F), Integer.valueOf(2))))), 
/*     */         
/* 212 */         getMinimalLookBehavior(), 
/* 213 */         Pair.of(Integer.valueOf(99), new ResetRaidStatus()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getRaidPackage(VillagerProfession debug0, float debug1) {
/* 218 */     return ImmutableList.of(
/* 219 */         Pair.of(Integer.valueOf(0), new RunOne<>((List<Pair<Behavior<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 220 */               Pair.of(new GoOutsideToCelebrate(debug1), Integer.valueOf(5)), 
/* 221 */               Pair.of(new VictoryStroll(debug1 * 1.1F), Integer.valueOf(2))))), 
/*     */         
/* 223 */         Pair.of(Integer.valueOf(0), new CelebrateVillagersSurvivedRaid(600, 600)), 
/* 224 */         Pair.of(Integer.valueOf(2), new LocateHidingPlaceDuringRaid(24, debug1 * 1.4F)), 
/* 225 */         getMinimalLookBehavior(), 
/* 226 */         Pair.of(Integer.valueOf(99), new ResetRaidStatus()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getHidePackage(VillagerProfession debug0, float debug1) {
/* 231 */     int debug2 = 2;
/* 232 */     return ImmutableList.of(
/* 233 */         Pair.of(Integer.valueOf(0), new SetHiddenState(15, 3)), 
/* 234 */         Pair.of(Integer.valueOf(1), new LocateHidingPlace(32, debug1 * 1.25F, 2)), 
/* 235 */         getMinimalLookBehavior());
/*     */   }
/*     */ 
/*     */   
/*     */   private static Pair<Integer, Behavior<LivingEntity>> getFullLookBehavior() {
/* 240 */     return Pair.of(Integer.valueOf(5), new RunOne<>((List<Pair<Behavior<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 241 */             Pair.of(new SetEntityLookTarget(EntityType.CAT, 8.0F), Integer.valueOf(8)), 
/* 242 */             Pair.of(new SetEntityLookTarget(EntityType.VILLAGER, 8.0F), Integer.valueOf(2)), 
/* 243 */             Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), Integer.valueOf(2)), 
/* 244 */             Pair.of(new SetEntityLookTarget(MobCategory.CREATURE, 8.0F), Integer.valueOf(1)), 
/* 245 */             Pair.of(new SetEntityLookTarget(MobCategory.WATER_CREATURE, 8.0F), Integer.valueOf(1)), 
/* 246 */             Pair.of(new SetEntityLookTarget(MobCategory.WATER_AMBIENT, 8.0F), Integer.valueOf(1)), 
/* 247 */             Pair.of(new SetEntityLookTarget(MobCategory.MONSTER, 8.0F), Integer.valueOf(1)), 
/* 248 */             Pair.of(new DoNothing(30, 60), Integer.valueOf(2)))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static Pair<Integer, Behavior<LivingEntity>> getMinimalLookBehavior() {
/* 253 */     return Pair.of(Integer.valueOf(5), new RunOne<>((List<Pair<Behavior<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 254 */             Pair.of(new SetEntityLookTarget(EntityType.VILLAGER, 8.0F), Integer.valueOf(2)), 
/* 255 */             Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), Integer.valueOf(2)), 
/* 256 */             Pair.of(new DoNothing(30, 60), Integer.valueOf(8)))));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\VillagerGoalPackages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */