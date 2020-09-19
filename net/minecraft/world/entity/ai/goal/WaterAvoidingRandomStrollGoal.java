/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.util.RandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ public class WaterAvoidingRandomStrollGoal
/*    */   extends RandomStrollGoal
/*    */ {
/*    */   protected final float probability;
/*    */   
/*    */   public WaterAvoidingRandomStrollGoal(PathfinderMob debug1, double debug2) {
/* 15 */     this(debug1, debug2, 0.001F);
/*    */   }
/*    */   
/*    */   public WaterAvoidingRandomStrollGoal(PathfinderMob debug1, double debug2, float debug4) {
/* 19 */     super(debug1, debug2);
/* 20 */     this.probability = debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   protected Vec3 getPosition() {
/* 26 */     if (this.mob.isInWaterOrBubble()) {
/*    */       
/* 28 */       Vec3 debug1 = RandomPos.getLandPos(this.mob, 15, 7);
/* 29 */       return (debug1 == null) ? super.getPosition() : debug1;
/*    */     } 
/* 31 */     if (this.mob.getRandom().nextFloat() >= this.probability) {
/* 32 */       return RandomPos.getLandPos(this.mob, 10, 7);
/*    */     }
/* 34 */     return super.getPosition();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\WaterAvoidingRandomStrollGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */