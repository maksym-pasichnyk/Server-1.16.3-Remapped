/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.IntRange;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class CopyMemoryWithExpiry<E extends Mob, T>
/*    */   extends Behavior<E> {
/*    */   private final Predicate<E> predicate;
/*    */   private final MemoryModuleType<? extends T> sourceMemory;
/*    */   
/*    */   public CopyMemoryWithExpiry(Predicate<E> debug1, MemoryModuleType<? extends T> debug2, MemoryModuleType<T> debug3, IntRange debug4) {
/* 20 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(debug2, MemoryStatus.VALUE_PRESENT, debug3, MemoryStatus.VALUE_ABSENT));
/*    */ 
/*    */ 
/*    */     
/* 24 */     this.predicate = debug1;
/* 25 */     this.sourceMemory = debug2;
/* 26 */     this.targetMemory = debug3;
/* 27 */     this.durationOfCopy = debug4;
/*    */   }
/*    */   private final MemoryModuleType<T> targetMemory; private final IntRange durationOfCopy;
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 32 */     return this.predicate.test(debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 37 */     Brain<?> debug5 = debug2.getBrain();
/* 38 */     debug5.setMemoryWithExpiry(this.targetMemory, debug5.getMemory(this.sourceMemory).get(), this.durationOfCopy.randomValue(debug1.random));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\CopyMemoryWithExpiry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */