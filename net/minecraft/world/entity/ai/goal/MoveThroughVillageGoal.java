/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*     */ import net.minecraft.world.entity.ai.util.RandomPos;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class MoveThroughVillageGoal
/*     */   extends Goal {
/*     */   protected final PathfinderMob mob;
/*     */   private final double speedModifier;
/*  29 */   private final List<BlockPos> visited = Lists.newArrayList(); private Path path; private BlockPos poiPos; private final boolean onlyAtNight;
/*     */   private final int distanceToPoi;
/*     */   private final BooleanSupplier canDealWithDoors;
/*     */   
/*     */   public MoveThroughVillageGoal(PathfinderMob debug1, double debug2, boolean debug4, int debug5, BooleanSupplier debug6) {
/*  34 */     this.mob = debug1;
/*  35 */     this.speedModifier = debug2;
/*  36 */     this.onlyAtNight = debug4;
/*  37 */     this.distanceToPoi = debug5;
/*  38 */     this.canDealWithDoors = debug6;
/*  39 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     
/*  41 */     if (!GoalUtils.hasGroundPathNavigation((Mob)debug1)) {
/*  42 */       throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  48 */     if (!GoalUtils.hasGroundPathNavigation((Mob)this.mob)) {
/*  49 */       return false;
/*     */     }
/*  51 */     updateVisited();
/*     */     
/*  53 */     if (this.onlyAtNight && this.mob.level.isDay()) {
/*  54 */       return false;
/*     */     }
/*     */     
/*  57 */     ServerLevel debug1 = (ServerLevel)this.mob.level;
/*  58 */     BlockPos debug2 = this.mob.blockPosition();
/*     */     
/*  60 */     if (!debug1.isCloseToVillage(debug2, 6)) {
/*  61 */       return false;
/*     */     }
/*     */     
/*  64 */     Vec3 debug3 = RandomPos.getLandPos(this.mob, 15, 7, debug3 -> {
/*     */           if (!debug1.isVillage(debug3)) {
/*     */             return Double.NEGATIVE_INFINITY;
/*     */           }
/*     */           
/*     */           Optional<BlockPos> debug4 = debug1.getPoiManager().find(PoiType.ALL, this::hasNotVisited, debug3, 10, PoiManager.Occupancy.IS_OCCUPIED);
/*     */           
/*     */           return !debug4.isPresent() ? Double.NEGATIVE_INFINITY : -((BlockPos)debug4.get()).distSqr((Vec3i)debug2);
/*     */         });
/*     */     
/*  74 */     if (debug3 == null) {
/*  75 */       return false;
/*     */     }
/*  77 */     Optional<BlockPos> debug4 = debug1.getPoiManager().find(PoiType.ALL, this::hasNotVisited, new BlockPos(debug3), 10, PoiManager.Occupancy.IS_OCCUPIED);
/*  78 */     if (!debug4.isPresent()) {
/*  79 */       return false;
/*     */     }
/*  81 */     this.poiPos = ((BlockPos)debug4.get()).immutable();
/*     */     
/*  83 */     GroundPathNavigation debug5 = (GroundPathNavigation)this.mob.getNavigation();
/*  84 */     boolean debug6 = debug5.canOpenDoors();
/*  85 */     debug5.setCanOpenDoors(this.canDealWithDoors.getAsBoolean());
/*  86 */     this.path = debug5.createPath(this.poiPos, 0);
/*  87 */     debug5.setCanOpenDoors(debug6);
/*  88 */     if (this.path == null) {
/*  89 */       Vec3 vec3 = RandomPos.getPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf((Vec3i)this.poiPos));
/*  90 */       if (vec3 == null) {
/*  91 */         return false;
/*     */       }
/*  93 */       debug5.setCanOpenDoors(this.canDealWithDoors.getAsBoolean());
/*  94 */       this.path = this.mob.getNavigation().createPath(vec3.x, vec3.y, vec3.z, 0);
/*  95 */       debug5.setCanOpenDoors(debug6);
/*  96 */       if (this.path == null) {
/*  97 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 101 */     for (int debug7 = 0; debug7 < this.path.getNodeCount(); debug7++) {
/* 102 */       Node debug8 = this.path.getNode(debug7);
/* 103 */       BlockPos debug9 = new BlockPos(debug8.x, debug8.y + 1, debug8.z);
/* 104 */       if (DoorBlock.isWoodenDoor(this.mob.level, debug9)) {
/*     */         
/* 106 */         this.path = this.mob.getNavigation().createPath(debug8.x, debug8.y, debug8.z, 0);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 111 */     return (this.path != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/* 116 */     if (this.mob.getNavigation().isDone()) {
/* 117 */       return false;
/*     */     }
/* 119 */     return !this.poiPos.closerThan((Position)this.mob.position(), (this.mob.getBbWidth() + this.distanceToPoi));
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 124 */     this.mob.getNavigation().moveTo(this.path, this.speedModifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 129 */     if (this.mob.getNavigation().isDone() || this.poiPos.closerThan((Position)this.mob.position(), this.distanceToPoi)) {
/* 130 */       this.visited.add(this.poiPos);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasNotVisited(BlockPos debug1) {
/* 135 */     for (BlockPos debug3 : this.visited) {
/* 136 */       if (Objects.equals(debug1, debug3)) {
/* 137 */         return false;
/*     */       }
/*     */     } 
/* 140 */     return true;
/*     */   }
/*     */   
/*     */   private void updateVisited() {
/* 144 */     if (this.visited.size() > 15)
/* 145 */       this.visited.remove(0); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\MoveThroughVillageGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */