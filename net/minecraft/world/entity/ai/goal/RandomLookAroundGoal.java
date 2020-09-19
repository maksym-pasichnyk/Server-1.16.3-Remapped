/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class RandomLookAroundGoal
/*    */   extends Goal {
/*    */   private final Mob mob;
/*    */   private double relX;
/*    */   private double relZ;
/*    */   private int lookTime;
/*    */   
/*    */   public RandomLookAroundGoal(Mob debug1) {
/* 14 */     this.mob = debug1;
/* 15 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 20 */     return (this.mob.getRandom().nextFloat() < 0.02F);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 25 */     return (this.lookTime >= 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 30 */     double debug1 = 6.283185307179586D * this.mob.getRandom().nextDouble();
/* 31 */     this.relX = Math.cos(debug1);
/* 32 */     this.relZ = Math.sin(debug1);
/* 33 */     this.lookTime = 20 + this.mob.getRandom().nextInt(20);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 38 */     this.lookTime--;
/* 39 */     this.mob.getLookControl().setLookAt(this.mob.getX() + this.relX, this.mob.getEyeY(), this.mob.getZ() + this.relZ);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\RandomLookAroundGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */