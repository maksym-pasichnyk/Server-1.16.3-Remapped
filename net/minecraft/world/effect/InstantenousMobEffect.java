/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ public class InstantenousMobEffect extends MobEffect {
/*    */   public InstantenousMobEffect(MobEffectCategory debug1, int debug2) {
/*  5 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInstantenous() {
/* 10 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDurationEffectTick(int debug1, int debug2) {
/* 15 */     return (debug1 >= 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\effect\InstantenousMobEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */