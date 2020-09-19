/*    */ package net.minecraft.world.entity.ai.navigation;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WallClimberNavigation
/*    */   extends GroundPathNavigation
/*    */ {
/*    */   private BlockPos pathToPosition;
/*    */   
/*    */   public WallClimberNavigation(Mob debug1, Level debug2) {
/* 21 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Path createPath(BlockPos debug1, int debug2) {
/* 26 */     this.pathToPosition = debug1;
/* 27 */     return super.createPath(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Path createPath(Entity debug1, int debug2) {
/* 32 */     this.pathToPosition = debug1.blockPosition();
/* 33 */     return super.createPath(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean moveTo(Entity debug1, double debug2) {
/* 38 */     Path debug4 = createPath(debug1, 0);
/* 39 */     if (debug4 != null) {
/* 40 */       return moveTo(debug4, debug2);
/*    */     }
/* 42 */     this.pathToPosition = debug1.blockPosition();
/* 43 */     this.speedModifier = debug2;
/* 44 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 50 */     if (isDone()) {
/* 51 */       if (this.pathToPosition != null)
/*    */       {
/* 53 */         if (this.pathToPosition.closerThan((Position)this.mob.position(), this.mob.getBbWidth()) || (this.mob.getY() > this.pathToPosition.getY() && (new BlockPos(this.pathToPosition.getX(), this.mob.getY(), this.pathToPosition.getZ())).closerThan((Position)this.mob.position(), this.mob.getBbWidth()))) {
/* 54 */           this.pathToPosition = null;
/*    */         } else {
/* 56 */           this.mob.getMoveControl().setWantedPosition(this.pathToPosition.getX(), this.pathToPosition.getY(), this.pathToPosition.getZ(), this.speedModifier);
/*    */         } 
/*    */       }
/*    */       return;
/*    */     } 
/* 61 */     super.tick();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\navigation\WallClimberNavigation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */