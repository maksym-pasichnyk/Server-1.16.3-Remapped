/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class EnderDragonPhaseManager {
/*  8 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   private final EnderDragon dragon;
/* 11 */   private final DragonPhaseInstance[] phases = new DragonPhaseInstance[EnderDragonPhase.getCount()];
/*    */   private DragonPhaseInstance currentPhase;
/*    */   
/*    */   public EnderDragonPhaseManager(EnderDragon debug1) {
/* 15 */     this.dragon = debug1;
/*    */     
/* 17 */     setPhase(EnderDragonPhase.HOVERING);
/*    */   }
/*    */   
/*    */   public void setPhase(EnderDragonPhase<?> debug1) {
/* 21 */     if (this.currentPhase != null && debug1 == this.currentPhase.getPhase()) {
/*    */       return;
/*    */     }
/*    */     
/* 25 */     if (this.currentPhase != null) {
/* 26 */       this.currentPhase.end();
/*    */     }
/*    */     
/* 29 */     this.currentPhase = getPhase(debug1);
/* 30 */     if (!this.dragon.level.isClientSide) {
/* 31 */       this.dragon.getEntityData().set(EnderDragon.DATA_PHASE, Integer.valueOf(debug1.getId()));
/*    */     }
/* 33 */     LOGGER.debug("Dragon is now in phase {} on the {}", debug1, this.dragon.level.isClientSide ? "client" : "server");
/*    */     
/* 35 */     this.currentPhase.begin();
/*    */   }
/*    */   
/*    */   public DragonPhaseInstance getCurrentPhase() {
/* 39 */     return this.currentPhase;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends DragonPhaseInstance> T getPhase(EnderDragonPhase<T> debug1) {
/* 44 */     int debug2 = debug1.getId();
/* 45 */     if (this.phases[debug2] == null) {
/* 46 */       this.phases[debug2] = debug1.createInstance(this.dragon);
/*    */     }
/* 48 */     return (T)this.phases[debug2];
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\EnderDragonPhaseManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */