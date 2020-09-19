/*    */ package net.minecraft.world.entity.boss.enderdragon.phases;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*    */ 
/*    */ public class EnderDragonPhase<T extends DragonPhaseInstance>
/*    */ {
/*  9 */   private static EnderDragonPhase<?>[] phases = (EnderDragonPhase<?>[])new EnderDragonPhase[0];
/* 10 */   public static final EnderDragonPhase<DragonHoldingPatternPhase> HOLDING_PATTERN = create(DragonHoldingPatternPhase.class, "HoldingPattern");
/* 11 */   public static final EnderDragonPhase<DragonStrafePlayerPhase> STRAFE_PLAYER = create(DragonStrafePlayerPhase.class, "StrafePlayer");
/* 12 */   public static final EnderDragonPhase<DragonLandingApproachPhase> LANDING_APPROACH = create(DragonLandingApproachPhase.class, "LandingApproach");
/* 13 */   public static final EnderDragonPhase<DragonLandingPhase> LANDING = create(DragonLandingPhase.class, "Landing");
/* 14 */   public static final EnderDragonPhase<DragonTakeoffPhase> TAKEOFF = create(DragonTakeoffPhase.class, "Takeoff");
/* 15 */   public static final EnderDragonPhase<DragonSittingFlamingPhase> SITTING_FLAMING = create(DragonSittingFlamingPhase.class, "SittingFlaming");
/* 16 */   public static final EnderDragonPhase<DragonSittingScanningPhase> SITTING_SCANNING = create(DragonSittingScanningPhase.class, "SittingScanning");
/* 17 */   public static final EnderDragonPhase<DragonSittingAttackingPhase> SITTING_ATTACKING = create(DragonSittingAttackingPhase.class, "SittingAttacking");
/* 18 */   public static final EnderDragonPhase<DragonChargePlayerPhase> CHARGING_PLAYER = create(DragonChargePlayerPhase.class, "ChargingPlayer");
/* 19 */   public static final EnderDragonPhase<DragonDeathPhase> DYING = create(DragonDeathPhase.class, "Dying");
/* 20 */   public static final EnderDragonPhase<DragonHoverPhase> HOVERING = create(DragonHoverPhase.class, "Hover");
/*    */   
/*    */   private final Class<? extends DragonPhaseInstance> instanceClass;
/*    */   private final int id;
/*    */   private final String name;
/*    */   
/*    */   private EnderDragonPhase(int debug1, Class<? extends DragonPhaseInstance> debug2, String debug3) {
/* 27 */     this.id = debug1;
/* 28 */     this.instanceClass = debug2;
/* 29 */     this.name = debug3;
/*    */   }
/*    */   
/*    */   public DragonPhaseInstance createInstance(EnderDragon debug1) {
/*    */     try {
/* 34 */       Constructor<? extends DragonPhaseInstance> debug2 = getConstructor();
/* 35 */       return debug2.newInstance(new Object[] { debug1 });
/* 36 */     } catch (Exception debug2) {
/* 37 */       throw new Error(debug2);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected Constructor<? extends DragonPhaseInstance> getConstructor() throws NoSuchMethodException {
/* 42 */     return this.instanceClass.getConstructor(new Class[] { EnderDragon.class });
/*    */   }
/*    */   
/*    */   public int getId() {
/* 46 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return this.name + " (#" + this.id + ")";
/*    */   }
/*    */   
/*    */   public static EnderDragonPhase<?> getById(int debug0) {
/* 55 */     if (debug0 < 0 || debug0 >= phases.length) {
/* 56 */       return HOLDING_PATTERN;
/*    */     }
/* 58 */     return phases[debug0];
/*    */   }
/*    */   
/*    */   public static int getCount() {
/* 62 */     return phases.length;
/*    */   }
/*    */   
/*    */   private static <T extends DragonPhaseInstance> EnderDragonPhase<T> create(Class<T> debug0, String debug1) {
/* 66 */     EnderDragonPhase<T> debug2 = new EnderDragonPhase<>(phases.length, debug0, debug1);
/* 67 */     phases = (EnderDragonPhase<?>[])Arrays.<EnderDragonPhase>copyOf((EnderDragonPhase[])phases, phases.length + 1);
/* 68 */     phases[debug2.getId()] = debug2;
/* 69 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\boss\enderdragon\phases\EnderDragonPhase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */