/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*    */ import net.minecraft.world.entity.ai.util.RandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class MoveBackToVillageGoal
/*    */   extends RandomStrollGoal
/*    */ {
/*    */   public MoveBackToVillageGoal(PathfinderMob debug1, double debug2, boolean debug4) {
/* 18 */     super(debug1, debug2, 10, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 23 */     ServerLevel debug1 = (ServerLevel)this.mob.level;
/* 24 */     BlockPos debug2 = this.mob.blockPosition();
/*    */     
/* 26 */     if (debug1.isVillage(debug2)) {
/* 27 */       return false;
/*    */     }
/*    */     
/* 30 */     return super.canUse();
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Vec3 getPosition() {
/* 36 */     ServerLevel debug1 = (ServerLevel)this.mob.level;
/* 37 */     BlockPos debug2 = this.mob.blockPosition();
/*    */     
/* 39 */     SectionPos debug3 = SectionPos.of(debug2);
/* 40 */     SectionPos debug4 = BehaviorUtils.findSectionClosestToVillage(debug1, debug3, 2);
/*    */     
/* 42 */     if (debug4 != debug3) {
/* 43 */       return RandomPos.getPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf((Vec3i)debug4.center()));
/*    */     }
/*    */     
/* 46 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\MoveBackToVillageGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */