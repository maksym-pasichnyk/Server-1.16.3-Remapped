/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.util.RandomPos;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.npc.Villager;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class GolemRandomStrollInVillageGoal
/*     */   extends RandomStrollGoal
/*     */ {
/*     */   public GolemRandomStrollInVillageGoal(PathfinderMob debug1, double debug2) {
/*  25 */     super(debug1, debug2, 240, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Vec3 getPosition() {
/*     */     Vec3 debug1;
/*  32 */     float debug2 = this.mob.level.random.nextFloat();
/*  33 */     if (this.mob.level.random.nextFloat() < 0.3F) {
/*  34 */       return getPositionTowardsAnywhere();
/*     */     }
/*     */     
/*  37 */     if (debug2 < 0.7F) {
/*  38 */       debug1 = getPositionTowardsVillagerWhoWantsGolem();
/*  39 */       if (debug1 == null) {
/*  40 */         debug1 = getPositionTowardsPoi();
/*     */       }
/*     */     } else {
/*  43 */       debug1 = getPositionTowardsPoi();
/*  44 */       if (debug1 == null) {
/*  45 */         debug1 = getPositionTowardsVillagerWhoWantsGolem();
/*     */       }
/*     */     } 
/*     */     
/*  49 */     return (debug1 == null) ? getPositionTowardsAnywhere() : debug1;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Vec3 getPositionTowardsAnywhere() {
/*  54 */     return RandomPos.getLandPos(this.mob, 10, 7);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Vec3 getPositionTowardsVillagerWhoWantsGolem() {
/*  59 */     ServerLevel debug1 = (ServerLevel)this.mob.level;
/*  60 */     List<Villager> debug2 = debug1.getEntities(EntityType.VILLAGER, this.mob.getBoundingBox().inflate(32.0D), this::doesVillagerWantGolem);
/*  61 */     if (debug2.isEmpty()) {
/*  62 */       return null;
/*     */     }
/*  64 */     Villager debug3 = debug2.get(this.mob.level.random.nextInt(debug2.size()));
/*  65 */     Vec3 debug4 = debug3.position();
/*  66 */     return RandomPos.getLandPosTowards(this.mob, 10, 7, debug4);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Vec3 getPositionTowardsPoi() {
/*  71 */     SectionPos debug1 = getRandomVillageSection();
/*  72 */     if (debug1 == null) {
/*  73 */       return null;
/*     */     }
/*     */     
/*  76 */     BlockPos debug2 = getRandomPoiWithinSection(debug1);
/*  77 */     if (debug2 == null)
/*     */     {
/*  79 */       return null;
/*     */     }
/*     */     
/*  82 */     return RandomPos.getLandPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf((Vec3i)debug2));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private SectionPos getRandomVillageSection() {
/*  87 */     ServerLevel debug1 = (ServerLevel)this.mob.level;
/*     */ 
/*     */ 
/*     */     
/*  91 */     List<SectionPos> debug2 = (List<SectionPos>)SectionPos.cube(SectionPos.of((Entity)this.mob), 2).filter(debug1 -> (debug0.sectionsToVillage(debug1) == 0)).collect(Collectors.toList());
/*     */     
/*  93 */     if (debug2.isEmpty()) {
/*  94 */       return null;
/*     */     }
/*  96 */     return debug2.get(debug1.random.nextInt(debug2.size()));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private BlockPos getRandomPoiWithinSection(SectionPos debug1) {
/* 102 */     ServerLevel debug2 = (ServerLevel)this.mob.level;
/* 103 */     PoiManager debug3 = debug2.getPoiManager();
/*     */ 
/*     */     
/* 106 */     List<BlockPos> debug4 = (List<BlockPos>)debug3.getInRange(debug0 -> true, debug1.center(), 8, PoiManager.Occupancy.IS_OCCUPIED).map(PoiRecord::getPos).collect(Collectors.toList());
/*     */     
/* 108 */     if (debug4.isEmpty()) {
/* 109 */       return null;
/*     */     }
/* 111 */     return debug4.get(debug2.random.nextInt(debug4.size()));
/*     */   }
/*     */   
/*     */   private boolean doesVillagerWantGolem(Villager debug1) {
/* 115 */     return debug1.wantsToSpawnGolem(this.mob.level.getGameTime());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\GolemRandomStrollInVillageGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */