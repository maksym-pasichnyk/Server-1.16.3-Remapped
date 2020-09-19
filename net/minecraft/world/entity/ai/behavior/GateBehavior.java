/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GateBehavior<E extends LivingEntity>
/*     */   extends Behavior<E>
/*     */ {
/*     */   private final Set<MemoryModuleType<?>> exitErasedMemories;
/*     */   private final OrderPolicy orderPolicy;
/*     */   private final RunningPolicy runningPolicy;
/*  25 */   private final WeightedList<Behavior<? super E>> behaviors = new WeightedList<>();
/*     */   
/*     */   public GateBehavior(Map<MemoryModuleType<?>, MemoryStatus> debug1, Set<MemoryModuleType<?>> debug2, OrderPolicy debug3, RunningPolicy debug4, List<Pair<Behavior<? super E>, Integer>> debug5) {
/*  28 */     super(debug1);
/*  29 */     this.exitErasedMemories = debug2;
/*  30 */     this.orderPolicy = debug3;
/*  31 */     this.runningPolicy = debug4;
/*  32 */     debug5.forEach(debug1 -> this.behaviors.add(debug1.getFirst(), ((Integer)debug1.getSecond()).intValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel debug1, E debug2, long debug3) {
/*  38 */     return this.behaviors.stream()
/*  39 */       .filter(debug0 -> (debug0.getStatus() == Behavior.Status.RUNNING))
/*  40 */       .anyMatch(debug4 -> debug4.canStillUse(debug0, debug1, debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean timedOut(long debug1) {
/*  46 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/*  51 */     this.orderPolicy.apply(this.behaviors);
/*     */     
/*  53 */     this.runningPolicy.apply(this.behaviors, debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel debug1, E debug2, long debug3) {
/*  59 */     this.behaviors.stream()
/*  60 */       .filter(debug0 -> (debug0.getStatus() == Behavior.Status.RUNNING))
/*  61 */       .forEach(debug4 -> debug4.tickOrStop(debug0, debug1, debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel debug1, E debug2, long debug3) {
/*  67 */     this.behaviors.stream()
/*  68 */       .filter(debug0 -> (debug0.getStatus() == Behavior.Status.RUNNING))
/*  69 */       .forEach(debug4 -> debug4.doStop(debug0, debug1, debug2));
/*     */     
/*  71 */     this.exitErasedMemories.forEach(debug2.getBrain()::eraseMemory);
/*     */   }
/*     */   
/*     */   enum OrderPolicy {
/*  75 */     ORDERED((String)(debug0 -> { 
/*  76 */       })), SHUFFLED((String)WeightedList::shuffle);
/*     */     
/*     */     private final Consumer<WeightedList<?>> consumer;
/*     */ 
/*     */     
/*     */     OrderPolicy(Consumer<WeightedList<?>> debug3) {
/*  82 */       this.consumer = debug3;
/*     */     }
/*     */     
/*     */     public void apply(WeightedList<?> debug1) {
/*  86 */       this.consumer.accept(debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   enum RunningPolicy {
/*  91 */     RUN_ONE
/*     */     {
/*     */       public <E extends LivingEntity> void apply(WeightedList<Behavior<? super E>> debug1, ServerLevel debug2, E debug3, long debug4) {
/*  94 */         debug1.stream()
/*  95 */           .filter(debug0 -> (debug0.getStatus() == Behavior.Status.STOPPED))
/*  96 */           .filter(debug4 -> debug4.tryStart(debug0, debug1, debug2))
/*  97 */           .findFirst();
/*     */       }
/*     */     },
/* 100 */     TRY_ALL
/*     */     {
/*     */       public <E extends LivingEntity> void apply(WeightedList<Behavior<? super E>> debug1, ServerLevel debug2, E debug3, long debug4) {
/* 103 */         debug1.stream()
/* 104 */           .filter(debug0 -> (debug0.getStatus() == Behavior.Status.STOPPED))
/* 105 */           .forEach(debug4 -> debug4.tryStart(debug0, debug1, debug2));
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract <E extends LivingEntity> void apply(WeightedList<Behavior<? super E>> param1WeightedList, ServerLevel param1ServerLevel, E param1E, long param1Long);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 117 */     Set<? extends Behavior<? super E>> debug1 = (Set<? extends Behavior<? super E>>)this.behaviors.stream().filter(debug0 -> (debug0.getStatus() == Behavior.Status.RUNNING)).collect(Collectors.toSet());
/*     */     
/* 119 */     return "(" + getClass().getSimpleName() + "): " + debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\GateBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */