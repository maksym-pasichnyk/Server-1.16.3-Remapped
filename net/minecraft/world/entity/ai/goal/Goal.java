/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ 
/*    */ public abstract class Goal {
/*  6 */   private final EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);
/*    */   
/*    */   public abstract boolean canUse();
/*    */   
/*    */   public boolean canContinueToUse() {
/* 11 */     return canUse();
/*    */   }
/*    */   
/*    */   public boolean isInterruptable() {
/* 15 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {}
/*    */ 
/*    */   
/*    */   public void stop() {}
/*    */ 
/*    */   
/*    */   public void tick() {}
/*    */   
/*    */   public void setFlags(EnumSet<Flag> debug1) {
/* 28 */     this.flags.clear();
/* 29 */     this.flags.addAll(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 34 */     return getClass().getSimpleName();
/*    */   }
/*    */   
/*    */   public EnumSet<Flag> getFlags() {
/* 38 */     return this.flags;
/*    */   }
/*    */   
/*    */   public enum Flag {
/* 42 */     MOVE,
/* 43 */     LOOK,
/* 44 */     JUMP,
/* 45 */     TARGET;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\Goal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */