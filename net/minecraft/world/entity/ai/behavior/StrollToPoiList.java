/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.npc.Villager;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StrollToPoiList
/*    */   extends Behavior<Villager>
/*    */ {
/*    */   private final MemoryModuleType<List<GlobalPos>> strollToMemoryType;
/*    */   private final MemoryModuleType<GlobalPos> mustBeCloseToMemoryType;
/*    */   private final float speedModifier;
/*    */   
/*    */   public StrollToPoiList(MemoryModuleType<List<GlobalPos>> debug1, float debug2, int debug3, int debug4, MemoryModuleType<GlobalPos> debug5) {
/* 27 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, debug1, MemoryStatus.VALUE_PRESENT, debug5, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 33 */     this.strollToMemoryType = debug1;
/* 34 */     this.speedModifier = debug2;
/* 35 */     this.closeEnoughDist = debug3;
/* 36 */     this.maxDistanceFromPoi = debug4;
/* 37 */     this.mustBeCloseToMemoryType = debug5;
/*    */   }
/*    */   private final int closeEnoughDist; private final int maxDistanceFromPoi; private long nextOkStartTime; @Nullable
/*    */   private GlobalPos targetPos;
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, Villager debug2) {
/* 42 */     Optional<List<GlobalPos>> debug3 = debug2.getBrain().getMemory(this.strollToMemoryType);
/* 43 */     Optional<GlobalPos> debug4 = debug2.getBrain().getMemory(this.mustBeCloseToMemoryType);
/* 44 */     if (debug3.isPresent() && debug4.isPresent()) {
/* 45 */       List<GlobalPos> debug5 = debug3.get();
/* 46 */       if (!debug5.isEmpty()) {
/* 47 */         this.targetPos = debug5.get(debug1.getRandom().nextInt(debug5.size()));
/* 48 */         return (this.targetPos != null && debug1.dimension() == this.targetPos.dimension() && ((GlobalPos)debug4.get()).pos().closerThan((Position)debug2.position(), this.maxDistanceFromPoi));
/*    */       } 
/*    */     } 
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/* 56 */     if (debug3 > this.nextOkStartTime && this.targetPos != null) {
/* 57 */       debug2.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(this.targetPos.pos(), this.speedModifier, this.closeEnoughDist));
/* 58 */       this.nextOkStartTime = debug3 + 100L;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StrollToPoiList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */