/*   */ package net.minecraft.world.entity.ai.goal;
/*   */ 
/*   */ import java.util.EnumSet;
/*   */ 
/*   */ public abstract class JumpGoal extends Goal {
/*   */   public JumpGoal() {
/* 7 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
/*   */   }
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\JumpGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */