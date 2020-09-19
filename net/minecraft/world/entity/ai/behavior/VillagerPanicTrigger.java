/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.npc.Villager;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ public class VillagerPanicTrigger
/*    */   extends Behavior<Villager> {
/*    */   public VillagerPanicTrigger() {
/* 16 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel debug1, Villager debug2, long debug3) {
/* 21 */     return (isHurt((LivingEntity)debug2) || hasHostile((LivingEntity)debug2));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/* 26 */     if (isHurt((LivingEntity)debug2) || hasHostile((LivingEntity)debug2)) {
/* 27 */       Brain<?> debug5 = debug2.getBrain();
/*    */ 
/*    */       
/* 30 */       if (!debug5.isActive(Activity.PANIC)) {
/* 31 */         debug5.eraseMemory(MemoryModuleType.PATH);
/* 32 */         debug5.eraseMemory(MemoryModuleType.WALK_TARGET);
/* 33 */         debug5.eraseMemory(MemoryModuleType.LOOK_TARGET);
/* 34 */         debug5.eraseMemory(MemoryModuleType.BREED_TARGET);
/* 35 */         debug5.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
/*    */       } 
/* 37 */       debug5.setActiveActivityIfPossible(Activity.PANIC);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel debug1, Villager debug2, long debug3) {
/* 43 */     if (debug3 % 100L == 0L) {
/* 44 */       debug2.spawnGolemIfNeeded(debug1, debug3, 3);
/*    */     }
/*    */   }
/*    */   
/*    */   public static boolean hasHostile(LivingEntity debug0) {
/* 49 */     return debug0.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE);
/*    */   }
/*    */   
/*    */   public static boolean isHurt(LivingEntity debug0) {
/* 53 */     return debug0.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\VillagerPanicTrigger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */