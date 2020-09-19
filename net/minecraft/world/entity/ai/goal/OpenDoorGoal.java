/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class OpenDoorGoal extends DoorInteractGoal {
/*    */   private final boolean closeDoor;
/*    */   private int forgetTime;
/*    */   
/*    */   public OpenDoorGoal(Mob debug1, boolean debug2) {
/* 10 */     super(debug1);
/* 11 */     this.mob = debug1;
/* 12 */     this.closeDoor = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 17 */     return (this.closeDoor && this.forgetTime > 0 && super.canContinueToUse());
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 22 */     this.forgetTime = 20;
/* 23 */     setOpen(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 28 */     setOpen(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 33 */     this.forgetTime--;
/* 34 */     super.tick();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\OpenDoorGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */