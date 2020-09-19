/*    */ package net.minecraft.world.level.border;
/*    */ 
/*    */ public interface BorderChangeListener {
/*    */   void onBorderSizeSet(WorldBorder paramWorldBorder, double paramDouble);
/*    */   
/*    */   void onBorderSizeLerping(WorldBorder paramWorldBorder, double paramDouble1, double paramDouble2, long paramLong);
/*    */   
/*    */   void onBorderCenterSet(WorldBorder paramWorldBorder, double paramDouble1, double paramDouble2);
/*    */   
/*    */   void onBorderSetWarningTime(WorldBorder paramWorldBorder, int paramInt);
/*    */   
/*    */   void onBorderSetWarningBlocks(WorldBorder paramWorldBorder, int paramInt);
/*    */   
/*    */   void onBorderSetDamagePerBlock(WorldBorder paramWorldBorder, double paramDouble);
/*    */   
/*    */   void onBorderSetDamageSafeZOne(WorldBorder paramWorldBorder, double paramDouble);
/*    */   
/*    */   public static class DelegateBorderChangeListener implements BorderChangeListener {
/*    */     private final WorldBorder worldBorder;
/*    */     
/*    */     public DelegateBorderChangeListener(WorldBorder debug1) {
/* 22 */       this.worldBorder = debug1;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onBorderSizeSet(WorldBorder debug1, double debug2) {
/* 27 */       this.worldBorder.setSize(debug2);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onBorderSizeLerping(WorldBorder debug1, double debug2, double debug4, long debug6) {
/* 32 */       this.worldBorder.lerpSizeBetween(debug2, debug4, debug6);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onBorderCenterSet(WorldBorder debug1, double debug2, double debug4) {
/* 37 */       this.worldBorder.setCenter(debug2, debug4);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onBorderSetWarningTime(WorldBorder debug1, int debug2) {
/* 42 */       this.worldBorder.setWarningTime(debug2);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onBorderSetWarningBlocks(WorldBorder debug1, int debug2) {
/* 47 */       this.worldBorder.setWarningBlocks(debug2);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onBorderSetDamagePerBlock(WorldBorder debug1, double debug2) {
/* 52 */       this.worldBorder.setDamagePerBlock(debug2);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onBorderSetDamageSafeZOne(WorldBorder debug1, double debug2) {
/* 57 */       this.worldBorder.setDamageSafeZone(debug2);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\border\BorderChangeListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */