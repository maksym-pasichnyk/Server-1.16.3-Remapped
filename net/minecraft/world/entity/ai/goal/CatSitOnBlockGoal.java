/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.animal.Cat;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.BedBlock;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BedPart;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class CatSitOnBlockGoal extends MoveToBlockGoal {
/*    */   public CatSitOnBlockGoal(Cat debug1, double debug2) {
/* 18 */     super((PathfinderMob)debug1, debug2, 8);
/* 19 */     this.cat = debug1;
/*    */   }
/*    */   private final Cat cat;
/*    */   
/*    */   public boolean canUse() {
/* 24 */     return (this.cat.isTame() && !this.cat.isOrderedToSit() && super.canUse());
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 29 */     super.start();
/* 30 */     this.cat.setInSittingPose(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 35 */     super.stop();
/* 36 */     this.cat.setInSittingPose(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 41 */     super.tick();
/*    */     
/* 43 */     this.cat.setInSittingPose(isReachedTarget());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isValidTarget(LevelReader debug1, BlockPos debug2) {
/* 48 */     if (!debug1.isEmptyBlock(debug2.above())) {
/* 49 */       return false;
/*    */     }
/*    */     
/* 52 */     BlockState debug3 = debug1.getBlockState(debug2);
/*    */ 
/*    */     
/* 55 */     if (debug3.is(Blocks.CHEST))
/* 56 */       return (ChestBlockEntity.getOpenCount((BlockGetter)debug1, debug2) < 1); 
/* 57 */     if (debug3.is(Blocks.FURNACE) && ((Boolean)debug3.getValue((Property)FurnaceBlock.LIT)).booleanValue()) {
/* 58 */       return true;
/*    */     }
/* 60 */     return debug3.is((Tag)BlockTags.BEDS, debug0 -> ((Boolean)debug0.getOptionalValue((Property)BedBlock.PART).map(()).orElse(Boolean.valueOf(true))).booleanValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\CatSitOnBlockGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */