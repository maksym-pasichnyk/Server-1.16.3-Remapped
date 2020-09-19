/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RunIf<E extends LivingEntity>
/*    */   extends Behavior<E>
/*    */ {
/*    */   private final Predicate<E> predicate;
/*    */   private final Behavior<? super E> wrappedBehavior;
/*    */   private final boolean checkWhileRunningAlso;
/*    */   
/*    */   public RunIf(Map<MemoryModuleType<?>, MemoryStatus> debug1, Predicate<E> debug2, Behavior<? super E> debug3, boolean debug4) {
/* 26 */     super(mergeMaps(debug1, debug3.entryCondition));
/* 27 */     this.predicate = debug2;
/* 28 */     this.wrappedBehavior = debug3;
/* 29 */     this.checkWhileRunningAlso = debug4;
/*    */   }
/*    */   
/*    */   private static Map<MemoryModuleType<?>, MemoryStatus> mergeMaps(Map<MemoryModuleType<?>, MemoryStatus> debug0, Map<MemoryModuleType<?>, MemoryStatus> debug1) {
/* 33 */     Map<MemoryModuleType<?>, MemoryStatus> debug2 = Maps.newHashMap();
/* 34 */     debug2.putAll(debug0);
/* 35 */     debug2.putAll(debug1);
/* 36 */     return debug2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RunIf(Predicate<E> debug1, Behavior<? super E> debug2) {
/* 44 */     this((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(), debug1, debug2, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 53 */     return (this.predicate.test(debug2) && this.wrappedBehavior.checkExtraStartConditions(debug1, debug2));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel debug1, E debug2, long debug3) {
/* 58 */     return (this.checkWhileRunningAlso && this.predicate.test(debug2) && this.wrappedBehavior.canStillUse(debug1, debug2, debug3));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean timedOut(long debug1) {
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 69 */     this.wrappedBehavior.start(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel debug1, E debug2, long debug3) {
/* 74 */     this.wrappedBehavior.tick(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel debug1, E debug2, long debug3) {
/* 79 */     this.wrappedBehavior.stop(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 84 */     return "RunIf: " + this.wrappedBehavior;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\RunIf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */