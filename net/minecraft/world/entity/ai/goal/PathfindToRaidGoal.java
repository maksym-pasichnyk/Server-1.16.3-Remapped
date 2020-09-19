/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.EnumSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.RandomPos;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ import net.minecraft.world.entity.raid.Raids;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class PathfindToRaidGoal<T extends Raider>
/*    */   extends Goal {
/*    */   private final T mob;
/*    */   
/*    */   public PathfindToRaidGoal(T debug1) {
/* 21 */     this.mob = debug1;
/* 22 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 27 */     return (this.mob.getTarget() == null && 
/* 28 */       !this.mob.isVehicle() && this.mob
/* 29 */       .hasActiveRaid() && 
/* 30 */       !this.mob.getCurrentRaid().isOver() && 
/* 31 */       !((ServerLevel)((Raider)this.mob).level).isVillage(this.mob.blockPosition()));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 36 */     return (this.mob.hasActiveRaid() && 
/* 37 */       !this.mob.getCurrentRaid().isOver() && ((Raider)this.mob).level instanceof ServerLevel && 
/*    */       
/* 39 */       !((ServerLevel)((Raider)this.mob).level).isVillage(this.mob.blockPosition()));
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 44 */     if (this.mob.hasActiveRaid()) {
/* 45 */       Raid debug1 = this.mob.getCurrentRaid();
/* 46 */       if (((Raider)this.mob).tickCount % 20 == 0) {
/* 47 */         recruitNearby(debug1);
/*    */       }
/*    */       
/* 50 */       if (!this.mob.isPathFinding()) {
/* 51 */         Vec3 debug2 = RandomPos.getPosTowards((PathfinderMob)this.mob, 15, 4, Vec3.atBottomCenterOf((Vec3i)debug1.getCenter()));
/* 52 */         if (debug2 != null) {
/* 53 */           this.mob.getNavigation().moveTo(debug2.x, debug2.y, debug2.z, 1.0D);
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void recruitNearby(Raid debug1) {
/* 60 */     if (debug1.isActive()) {
/* 61 */       Set<Raider> debug2 = Sets.newHashSet();
/*    */       
/* 63 */       List<Raider> debug3 = ((Raider)this.mob).level.getEntitiesOfClass(Raider.class, this.mob.getBoundingBox().inflate(16.0D), debug1 -> (!debug1.hasActiveRaid() && Raids.canJoinRaid(debug1, debug0)));
/* 64 */       debug2.addAll(debug3);
/*    */       
/* 66 */       for (Raider debug5 : debug2)
/* 67 */         debug1.joinRaid(debug1.getGroupsSpawned(), debug5, null, true); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\PathfindToRaidGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */