/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.animal.Cat;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ 
/*    */ public class CatLieOnBedGoal extends MoveToBlockGoal {
/*    */   private final Cat cat;
/*    */   
/*    */   public CatLieOnBedGoal(Cat debug1, double debug2, int debug4) {
/* 15 */     super((PathfinderMob)debug1, debug2, debug4, 6);
/* 16 */     this.cat = debug1;
/* 17 */     this.verticalSearchStart = -2;
/* 18 */     setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 23 */     return (this.cat.isTame() && !this.cat.isOrderedToSit() && !this.cat.isLying() && super.canUse());
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 28 */     super.start();
/* 29 */     this.cat.setInSittingPose(false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int nextStartTick(PathfinderMob debug1) {
/* 34 */     return 40;
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 39 */     super.stop();
/* 40 */     this.cat.setLying(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 45 */     super.tick();
/*    */     
/* 47 */     this.cat.setInSittingPose(false);
/* 48 */     if (!isReachedTarget()) {
/* 49 */       this.cat.setLying(false);
/* 50 */     } else if (!this.cat.isLying()) {
/* 51 */       this.cat.setLying(true);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isValidTarget(LevelReader debug1, BlockPos debug2) {
/* 57 */     return (debug1.isEmptyBlock(debug2.above()) && debug1.getBlockState(debug2).getBlock().is((Tag)BlockTags.BEDS));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\CatLieOnBedGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */