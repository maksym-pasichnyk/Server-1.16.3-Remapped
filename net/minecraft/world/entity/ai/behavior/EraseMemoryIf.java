/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class EraseMemoryIf<E extends LivingEntity>
/*    */   extends Behavior<E> {
/*    */   private final Predicate<E> predicate;
/*    */   private final MemoryModuleType<?> memoryType;
/*    */   
/*    */   public EraseMemoryIf(Predicate<E> debug1, MemoryModuleType<?> debug2) {
/* 17 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(debug2, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */     
/* 20 */     this.predicate = debug1;
/* 21 */     this.memoryType = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 26 */     return this.predicate.test(debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 31 */     debug2.getBrain().eraseMemory(this.memoryType);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\EraseMemoryIf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */