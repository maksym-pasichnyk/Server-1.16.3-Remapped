/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
/*    */ import net.minecraft.world.entity.ai.behavior.PositionTracker;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class WalkTarget
/*    */ {
/*    */   private final PositionTracker target;
/*    */   private final float speedModifier;
/*    */   private final int closeEnoughDist;
/*    */   
/*    */   public WalkTarget(BlockPos debug1, float debug2, int debug3) {
/* 16 */     this((PositionTracker)new BlockPosTracker(debug1), debug2, debug3);
/*    */   }
/*    */   
/*    */   public WalkTarget(Vec3 debug1, float debug2, int debug3) {
/* 20 */     this((PositionTracker)new BlockPosTracker(new BlockPos(debug1)), debug2, debug3);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WalkTarget(PositionTracker debug1, float debug2, int debug3) {
/* 28 */     this.target = debug1;
/* 29 */     this.speedModifier = debug2;
/* 30 */     this.closeEnoughDist = debug3;
/*    */   }
/*    */   
/*    */   public PositionTracker getTarget() {
/* 34 */     return this.target;
/*    */   }
/*    */   
/*    */   public float getSpeedModifier() {
/* 38 */     return this.speedModifier;
/*    */   }
/*    */   
/*    */   public int getCloseEnoughDist() {
/* 42 */     return this.closeEnoughDist;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\memory\WalkTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */