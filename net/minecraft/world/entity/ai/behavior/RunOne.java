/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RunOne<E extends LivingEntity>
/*    */   extends GateBehavior<E>
/*    */ {
/*    */   public RunOne(List<Pair<Behavior<? super E>, Integer>> debug1) {
/* 19 */     this(
/* 20 */         (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(), debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public RunOne(Map<MemoryModuleType<?>, MemoryStatus> debug1, List<Pair<Behavior<? super E>, Integer>> debug2) {
/* 26 */     super(debug1, 
/*    */         
/* 28 */         (Set<MemoryModuleType<?>>)ImmutableSet.of(), GateBehavior.OrderPolicy.SHUFFLED, GateBehavior.RunningPolicy.RUN_ONE, debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\RunOne.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */