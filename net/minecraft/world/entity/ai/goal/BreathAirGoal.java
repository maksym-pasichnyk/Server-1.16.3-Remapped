/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.MoverType;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BreathAirGoal
/*    */   extends Goal {
/*    */   private final PathfinderMob mob;
/*    */   
/*    */   public BreathAirGoal(PathfinderMob debug1) {
/* 20 */     this.mob = debug1;
/* 21 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 26 */     return (this.mob.getAirSupply() < 140);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 31 */     return canUse();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInterruptable() {
/* 36 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 41 */     findAirPosition();
/*    */   }
/*    */   
/*    */   private void findAirPosition() {
/* 45 */     Iterable<BlockPos> debug1 = BlockPos.betweenClosed(
/* 46 */         Mth.floor(this.mob.getX() - 1.0D), 
/* 47 */         Mth.floor(this.mob.getY()), 
/* 48 */         Mth.floor(this.mob.getZ() - 1.0D), 
/* 49 */         Mth.floor(this.mob.getX() + 1.0D), 
/* 50 */         Mth.floor(this.mob.getY() + 8.0D), 
/* 51 */         Mth.floor(this.mob.getZ() + 1.0D));
/*    */ 
/*    */     
/* 54 */     BlockPos debug2 = null;
/* 55 */     for (BlockPos debug4 : debug1) {
/* 56 */       if (givesAir((LevelReader)this.mob.level, debug4)) {
/* 57 */         debug2 = debug4;
/*    */         
/*    */         break;
/*    */       } 
/*    */     } 
/* 62 */     if (debug2 == null) {
/* 63 */       debug2 = new BlockPos(this.mob.getX(), this.mob.getY() + 8.0D, this.mob.getZ());
/*    */     }
/*    */     
/* 66 */     this.mob.getNavigation().moveTo(debug2.getX(), (debug2.getY() + 1), debug2.getZ(), 1.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 71 */     findAirPosition();
/*    */     
/* 73 */     this.mob.moveRelative(0.02F, new Vec3(this.mob.xxa, this.mob.yya, this.mob.zza));
/* 74 */     this.mob.move(MoverType.SELF, this.mob.getDeltaMovement());
/*    */   }
/*    */   
/*    */   private boolean givesAir(LevelReader debug1, BlockPos debug2) {
/* 78 */     BlockState debug3 = debug1.getBlockState(debug2);
/* 79 */     return ((debug1.getFluidState(debug2).isEmpty() || debug3.is(Blocks.BUBBLE_COLUMN)) && debug3.isPathfindable((BlockGetter)debug1, debug2, PathComputationType.LAND));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\BreathAirGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */