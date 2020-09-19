/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.RandomPos;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WaterAvoidingRandomFlyingGoal extends WaterAvoidingRandomStrollGoal {
/*    */   public WaterAvoidingRandomFlyingGoal(PathfinderMob debug1, double debug2) {
/* 17 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Vec3 getPosition() {
/* 23 */     Vec3 debug1 = null;
/* 24 */     if (this.mob.isInWater()) {
/* 25 */       debug1 = RandomPos.getLandPos(this.mob, 15, 15);
/*    */     }
/* 27 */     if (this.mob.getRandom().nextFloat() >= this.probability) {
/* 28 */       debug1 = getTreePos();
/*    */     }
/* 30 */     return (debug1 == null) ? super.getPosition() : debug1;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   private Vec3 getTreePos() {
/* 35 */     BlockPos debug1 = this.mob.blockPosition();
/*    */     
/* 37 */     BlockPos.MutableBlockPos debug2 = new BlockPos.MutableBlockPos();
/* 38 */     BlockPos.MutableBlockPos debug3 = new BlockPos.MutableBlockPos();
/* 39 */     Iterable<BlockPos> debug4 = BlockPos.betweenClosed(
/* 40 */         Mth.floor(this.mob.getX() - 3.0D), 
/* 41 */         Mth.floor(this.mob.getY() - 6.0D), 
/* 42 */         Mth.floor(this.mob.getZ() - 3.0D), 
/* 43 */         Mth.floor(this.mob.getX() + 3.0D), 
/* 44 */         Mth.floor(this.mob.getY() + 6.0D), 
/* 45 */         Mth.floor(this.mob.getZ() + 3.0D));
/*    */ 
/*    */     
/* 48 */     for (BlockPos debug6 : debug4) {
/* 49 */       if (debug1.equals(debug6)) {
/*    */         continue;
/*    */       }
/*    */       
/* 53 */       Block debug7 = this.mob.level.getBlockState((BlockPos)debug3.setWithOffset((Vec3i)debug6, Direction.DOWN)).getBlock();
/* 54 */       boolean debug8 = (debug7 instanceof net.minecraft.world.level.block.LeavesBlock || debug7.is((Tag)BlockTags.LOGS));
/* 55 */       if (debug8 && this.mob.level.isEmptyBlock(debug6) && this.mob.level.isEmptyBlock((BlockPos)debug2.setWithOffset((Vec3i)debug6, Direction.UP))) {
/* 56 */         return Vec3.atBottomCenterOf((Vec3i)debug6);
/*    */       }
/*    */     } 
/*    */     
/* 60 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\WaterAvoidingRandomFlyingGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */