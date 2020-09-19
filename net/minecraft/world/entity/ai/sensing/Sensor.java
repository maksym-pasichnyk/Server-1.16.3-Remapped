/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Sensor<E extends LivingEntity>
/*    */ {
/* 18 */   private static final Random RANDOM = new Random();
/*    */ 
/*    */ 
/*    */   
/* 22 */   private static final TargetingConditions TARGET_CONDITIONS = (new TargetingConditions()).range(16.0D).allowSameTeam().allowNonAttackable();
/* 23 */   private static final TargetingConditions TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING = (new TargetingConditions()).range(16.0D).allowSameTeam().allowNonAttackable().ignoreInvisibilityTesting();
/*    */   
/*    */   private final int scanRate;
/*    */   private long timeToTick;
/*    */   
/*    */   public Sensor(int debug1) {
/* 29 */     this.scanRate = debug1;
/* 30 */     this.timeToTick = RANDOM.nextInt(debug1);
/*    */   }
/*    */   
/*    */   public Sensor() {
/* 34 */     this(20);
/*    */   }
/*    */   
/*    */   public final void tick(ServerLevel debug1, E debug2) {
/* 38 */     if (--this.timeToTick <= 0L) {
/* 39 */       this.timeToTick = this.scanRate;
/* 40 */       doTick(debug1, debug2);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected abstract void doTick(ServerLevel paramServerLevel, E paramE);
/*    */   
/*    */   public abstract Set<MemoryModuleType<?>> requires();
/*    */   
/*    */   protected static boolean isEntityTargetable(LivingEntity debug0, LivingEntity debug1) {
/* 49 */     if (debug0.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, debug1))
/*    */     {
/* 51 */       return TARGET_CONDITIONS_IGNORE_INVISIBILITY_TESTING.test(debug0, debug1);
/*    */     }
/* 53 */     return TARGET_CONDITIONS.test(debug0, debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\Sensor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */