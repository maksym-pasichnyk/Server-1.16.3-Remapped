/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BlockPosTracker implements PositionTracker {
/*    */   private final BlockPos blockPos;
/*    */   
/*    */   public BlockPosTracker(BlockPos debug1) {
/* 12 */     this.blockPos = debug1;
/* 13 */     this.centerPosition = Vec3.atCenterOf((Vec3i)debug1);
/*    */   }
/*    */   private final Vec3 centerPosition;
/*    */   
/*    */   public Vec3 currentPosition() {
/* 18 */     return this.centerPosition;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos currentBlockPosition() {
/* 23 */     return this.blockPos;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isVisibleBy(LivingEntity debug1) {
/* 28 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 33 */     return "BlockPosTracker{blockPos=" + this.blockPos + ", centerPosition=" + this.centerPosition + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\BlockPosTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */