/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.RandomPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class RandomSwimmingGoal extends RandomStrollGoal {
/*    */   public RandomSwimmingGoal(PathfinderMob debug1, double debug2, int debug4) {
/* 13 */     super(debug1, debug2, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Vec3 getPosition() {
/* 19 */     Vec3 debug1 = RandomPos.getPos(this.mob, 10, 7);
/* 20 */     int debug2 = 0;
/* 21 */     while (debug1 != null && !this.mob.level.getBlockState(new BlockPos(debug1)).isPathfindable((BlockGetter)this.mob.level, new BlockPos(debug1), PathComputationType.WATER) && debug2++ < 10) {
/* 22 */       debug1 = RandomPos.getPos(this.mob, 10, 7);
/*    */     }
/* 24 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\RandomSwimmingGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */