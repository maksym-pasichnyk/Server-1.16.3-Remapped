/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2LongMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.network.protocol.game.DebugPackets;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SetClosestHomeAsWalkTarget
/*    */   extends Behavior<LivingEntity>
/*    */ {
/*    */   private final float speedModifier;
/* 31 */   private final Long2LongMap batchCache = (Long2LongMap)new Long2LongOpenHashMap();
/*    */   private int triedCount;
/*    */   private long lastUpdate;
/*    */   
/*    */   public SetClosestHomeAsWalkTarget(float debug1) {
/* 36 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     this.speedModifier = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, LivingEntity debug2) {
/* 46 */     if (debug1.getGameTime() - this.lastUpdate < 20L) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     PathfinderMob debug3 = (PathfinderMob)debug2;
/* 51 */     PoiManager debug4 = debug1.getPoiManager();
/*    */     
/* 53 */     Optional<BlockPos> debug5 = debug4.findClosest(PoiType.HOME.getPredicate(), debug2.blockPosition(), 48, PoiManager.Occupancy.ANY);
/* 54 */     return (debug5.isPresent() && ((BlockPos)debug5.get()).distSqr((Vec3i)debug3.blockPosition()) > 4.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 59 */     this.triedCount = 0;
/* 60 */     this.lastUpdate = debug1.getGameTime() + debug1.getRandom().nextInt(20);
/*    */     
/* 62 */     PathfinderMob debug5 = (PathfinderMob)debug2;
/* 63 */     PoiManager debug6 = debug1.getPoiManager();
/*    */     
/* 65 */     Predicate<BlockPos> debug7 = debug1 -> {
/*    */         long debug2 = debug1.asLong();
/*    */         
/*    */         if (this.batchCache.containsKey(debug2)) {
/*    */           return false;
/*    */         }
/*    */         
/*    */         if (++this.triedCount >= 5) {
/*    */           return false;
/*    */         }
/*    */         
/*    */         this.batchCache.put(debug2, this.lastUpdate + 40L);
/*    */         return true;
/*    */       };
/* 79 */     Stream<BlockPos> debug8 = debug6.findAll(PoiType.HOME.getPredicate(), debug7, debug2.blockPosition(), 48, PoiManager.Occupancy.ANY);
/* 80 */     Path debug9 = debug5.getNavigation().createPath(debug8, PoiType.HOME.getValidRange());
/*    */     
/* 82 */     if (debug9 != null && debug9.canReach()) {
/* 83 */       BlockPos debug10 = debug9.getTarget();
/* 84 */       Optional<PoiType> debug11 = debug6.getType(debug10);
/* 85 */       if (debug11.isPresent()) {
/*    */         
/* 87 */         debug2.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(debug10, this.speedModifier, 1));
/* 88 */         DebugPackets.sendPoiTicketCountPacket(debug1, debug10);
/*    */       } 
/* 90 */     } else if (this.triedCount < 5) {
/* 91 */       this.batchCache.long2LongEntrySet().removeIf(debug1 -> (debug1.getLongValue() < this.lastUpdate));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetClosestHomeAsWalkTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */