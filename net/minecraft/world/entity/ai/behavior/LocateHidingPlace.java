/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ 
/*    */ public class LocateHidingPlace
/*    */   extends Behavior<LivingEntity> {
/*    */   private final float speedModifier;
/* 21 */   private Optional<BlockPos> currentPos = Optional.empty(); private final int radius; private final int closeEnoughDist;
/*    */   
/*    */   public LocateHidingPlace(int debug1, float debug2, int debug3) {
/* 24 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.HOME, MemoryStatus.REGISTERED, MemoryModuleType.HIDING_PLACE, MemoryStatus.REGISTERED));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 30 */     this.radius = debug1;
/* 31 */     this.speedModifier = debug2;
/* 32 */     this.closeEnoughDist = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, LivingEntity debug2) {
/* 37 */     Optional<BlockPos> debug3 = debug1.getPoiManager().find(debug0 -> (debug0 == PoiType.HOME), debug0 -> true, debug2.blockPosition(), this.closeEnoughDist + 1, PoiManager.Occupancy.ANY);
/*    */     
/* 39 */     if (debug3.isPresent() && ((BlockPos)debug3.get()).closerThan((Position)debug2.position(), this.closeEnoughDist)) {
/* 40 */       this.currentPos = debug3;
/*    */     } else {
/* 42 */       this.currentPos = Optional.empty();
/*    */     } 
/*    */     
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 50 */     Brain<?> debug5 = debug2.getBrain();
/*    */     
/* 52 */     Optional<BlockPos> debug6 = this.currentPos;
/*    */     
/* 54 */     if (!debug6.isPresent()) {
/* 55 */       debug6 = debug1.getPoiManager().getRandom(debug0 -> (debug0 == PoiType.HOME), debug0 -> true, PoiManager.Occupancy.ANY, debug2.blockPosition(), this.radius, debug2.getRandom());
/* 56 */       if (!debug6.isPresent()) {
/* 57 */         Optional<GlobalPos> debug7 = debug5.getMemory(MemoryModuleType.HOME);
/* 58 */         if (debug7.isPresent()) {
/* 59 */           debug6 = Optional.of(((GlobalPos)debug7.get()).pos());
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 64 */     if (debug6.isPresent()) {
/* 65 */       debug5.eraseMemory(MemoryModuleType.PATH);
/* 66 */       debug5.eraseMemory(MemoryModuleType.LOOK_TARGET);
/* 67 */       debug5.eraseMemory(MemoryModuleType.BREED_TARGET);
/* 68 */       debug5.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
/* 69 */       debug5.setMemory(MemoryModuleType.HIDING_PLACE, GlobalPos.of(debug1.dimension(), debug6.get()));
/* 70 */       if (!((BlockPos)debug6.get()).closerThan((Position)debug2.position(), this.closeEnoughDist))
/* 71 */         debug5.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(debug6.get(), this.speedModifier, this.closeEnoughDist)); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\LocateHidingPlace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */