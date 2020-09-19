/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*    */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*    */ import net.minecraft.world.level.block.DoorBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.pathfinder.Node;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ public abstract class DoorInteractGoal extends Goal {
/* 14 */   protected BlockPos doorPos = BlockPos.ZERO; protected Mob mob;
/*    */   protected boolean hasDoor;
/*    */   private boolean passed;
/*    */   private float doorOpenDirX;
/*    */   private float doorOpenDirZ;
/*    */   
/*    */   public DoorInteractGoal(Mob debug1) {
/* 21 */     this.mob = debug1;
/* 22 */     if (!GoalUtils.hasGroundPathNavigation(debug1)) {
/* 23 */       throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
/*    */     }
/*    */   }
/*    */   
/*    */   protected boolean isOpen() {
/* 28 */     if (!this.hasDoor) {
/* 29 */       return false;
/*    */     }
/* 31 */     BlockState debug1 = this.mob.level.getBlockState(this.doorPos);
/* 32 */     if (!(debug1.getBlock() instanceof DoorBlock)) {
/* 33 */       this.hasDoor = false;
/* 34 */       return false;
/*    */     } 
/* 36 */     return ((Boolean)debug1.getValue((Property)DoorBlock.OPEN)).booleanValue();
/*    */   }
/*    */   
/*    */   protected void setOpen(boolean debug1) {
/* 40 */     if (this.hasDoor) {
/* 41 */       BlockState debug2 = this.mob.level.getBlockState(this.doorPos);
/* 42 */       if (debug2.getBlock() instanceof DoorBlock) {
/* 43 */         ((DoorBlock)debug2.getBlock()).setOpen(this.mob.level, debug2, this.doorPos, debug1);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 50 */     if (!GoalUtils.hasGroundPathNavigation(this.mob)) {
/* 51 */       return false;
/*    */     }
/* 53 */     if (!this.mob.horizontalCollision) {
/* 54 */       return false;
/*    */     }
/* 56 */     GroundPathNavigation debug1 = (GroundPathNavigation)this.mob.getNavigation();
/* 57 */     Path debug2 = debug1.getPath();
/* 58 */     if (debug2 == null || debug2.isDone() || !debug1.canOpenDoors()) {
/* 59 */       return false;
/*    */     }
/*    */     
/* 62 */     for (int debug3 = 0; debug3 < Math.min(debug2.getNextNodeIndex() + 2, debug2.getNodeCount()); debug3++) {
/* 63 */       Node debug4 = debug2.getNode(debug3);
/* 64 */       this.doorPos = new BlockPos(debug4.x, debug4.y + 1, debug4.z);
/* 65 */       if (this.mob.distanceToSqr(this.doorPos.getX(), this.mob.getY(), this.doorPos.getZ()) <= 2.25D) {
/*    */ 
/*    */         
/* 68 */         this.hasDoor = DoorBlock.isWoodenDoor(this.mob.level, this.doorPos);
/* 69 */         if (this.hasDoor) {
/* 70 */           return true;
/*    */         }
/*    */       } 
/*    */     } 
/* 74 */     this.doorPos = this.mob.blockPosition().above();
/* 75 */     this.hasDoor = DoorBlock.isWoodenDoor(this.mob.level, this.doorPos);
/* 76 */     return this.hasDoor;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 81 */     return !this.passed;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 86 */     this.passed = false;
/* 87 */     this.doorOpenDirX = (float)(this.doorPos.getX() + 0.5D - this.mob.getX());
/* 88 */     this.doorOpenDirZ = (float)(this.doorPos.getZ() + 0.5D - this.mob.getZ());
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 93 */     float debug1 = (float)(this.doorPos.getX() + 0.5D - this.mob.getX());
/* 94 */     float debug2 = (float)(this.doorPos.getZ() + 0.5D - this.mob.getZ());
/* 95 */     float debug3 = this.doorOpenDirX * debug1 + this.doorOpenDirZ * debug2;
/* 96 */     if (debug3 < 0.0F)
/* 97 */       this.passed = true; 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\DoorInteractGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */