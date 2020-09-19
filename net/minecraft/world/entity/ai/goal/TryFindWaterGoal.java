/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ 
/*    */ public class TryFindWaterGoal
/*    */   extends Goal {
/*    */   public TryFindWaterGoal(PathfinderMob debug1) {
/* 12 */     this.mob = debug1;
/*    */   }
/*    */   private final PathfinderMob mob;
/*    */   
/*    */   public boolean canUse() {
/* 17 */     return (this.mob.isOnGround() && !this.mob.level.getFluidState(this.mob.blockPosition()).is((Tag)FluidTags.WATER));
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 22 */     BlockPos debug1 = null;
/*    */     
/* 24 */     Iterable<BlockPos> debug2 = BlockPos.betweenClosed(
/* 25 */         Mth.floor(this.mob.getX() - 2.0D), 
/* 26 */         Mth.floor(this.mob.getY() - 2.0D), 
/* 27 */         Mth.floor(this.mob.getZ() - 2.0D), 
/* 28 */         Mth.floor(this.mob.getX() + 2.0D), 
/* 29 */         Mth.floor(this.mob.getY()), 
/* 30 */         Mth.floor(this.mob.getZ() + 2.0D));
/*    */ 
/*    */     
/* 33 */     for (BlockPos debug4 : debug2) {
/* 34 */       if (this.mob.level.getFluidState(debug4).is((Tag)FluidTags.WATER)) {
/* 35 */         debug1 = debug4;
/*    */         
/*    */         break;
/*    */       } 
/*    */     } 
/* 40 */     if (debug1 != null)
/* 41 */       this.mob.getMoveControl().setWantedPosition(debug1.getX(), debug1.getY(), debug1.getZ(), 1.0D); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\TryFindWaterGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */