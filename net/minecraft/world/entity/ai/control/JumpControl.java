/*    */ package net.minecraft.world.entity.ai.control;
/*    */ 
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class JumpControl {
/*    */   private final Mob mob;
/*    */   protected boolean jump;
/*    */   
/*    */   public JumpControl(Mob debug1) {
/* 10 */     this.mob = debug1;
/*    */   }
/*    */   
/*    */   public void jump() {
/* 14 */     this.jump = true;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 18 */     this.mob.setJumping(this.jump);
/* 19 */     this.jump = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\control\JumpControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */