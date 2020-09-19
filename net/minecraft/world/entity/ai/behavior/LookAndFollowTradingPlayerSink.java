/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.npc.Villager;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class LookAndFollowTradingPlayerSink extends Behavior<Villager> {
/*    */   public LookAndFollowTradingPlayerSink(float debug1) {
/* 16 */     super(
/* 17 */         (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), 2147483647);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 23 */     this.speedModifier = debug1;
/*    */   }
/*    */   private final float speedModifier;
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, Villager debug2) {
/* 28 */     Player debug3 = debug2.getTradingPlayer();
/*    */     
/* 30 */     return (debug2.isAlive() && debug3 != null && 
/*    */       
/* 32 */       !debug2.isInWater() && !debug2.hurtMarked && debug2
/*    */       
/* 34 */       .distanceToSqr((Entity)debug3) <= 16.0D && debug3.containerMenu != null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel debug1, Villager debug2, long debug3) {
/* 41 */     return checkExtraStartConditions(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/* 46 */     followPlayer(debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel debug1, Villager debug2, long debug3) {
/* 51 */     Brain<?> debug5 = debug2.getBrain();
/* 52 */     debug5.eraseMemory(MemoryModuleType.WALK_TARGET);
/* 53 */     debug5.eraseMemory(MemoryModuleType.LOOK_TARGET);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel debug1, Villager debug2, long debug3) {
/* 58 */     followPlayer(debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean timedOut(long debug1) {
/* 63 */     return false;
/*    */   }
/*    */   
/*    */   private void followPlayer(Villager debug1) {
/* 67 */     Brain<?> debug2 = debug1.getBrain();
/* 68 */     debug2.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker((Entity)debug1.getTradingPlayer(), false), this.speedModifier, 2));
/* 69 */     debug2.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)debug1.getTradingPlayer(), true));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\LookAndFollowTradingPlayerSink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */