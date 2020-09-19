/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import it.unimi.dsi.fastutil.longs.Long2LongMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NearestBedSensor
/*    */   extends Sensor<Mob>
/*    */ {
/* 29 */   private final Long2LongMap batchCache = (Long2LongMap)new Long2LongOpenHashMap();
/*    */   private int triedCount;
/*    */   private long lastUpdate;
/*    */   
/*    */   public NearestBedSensor() {
/* 34 */     super(20);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 39 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_BED);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel debug1, Mob debug2) {
/* 44 */     if (!debug2.isBaby()) {
/*    */       return;
/*    */     }
/*    */     
/* 48 */     this.triedCount = 0;
/* 49 */     this.lastUpdate = debug1.getGameTime() + debug1.getRandom().nextInt(20);
/*    */     
/* 51 */     PoiManager debug3 = debug1.getPoiManager();
/*    */     
/* 53 */     Predicate<BlockPos> debug4 = debug1 -> {
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
/* 67 */     Stream<BlockPos> debug5 = debug3.findAll(PoiType.HOME.getPredicate(), debug4, debug2.blockPosition(), 48, PoiManager.Occupancy.ANY);
/* 68 */     Path debug6 = debug2.getNavigation().createPath(debug5, PoiType.HOME.getValidRange());
/*    */     
/* 70 */     if (debug6 != null && debug6.canReach()) {
/* 71 */       BlockPos debug7 = debug6.getTarget();
/* 72 */       Optional<PoiType> debug8 = debug3.getType(debug7);
/* 73 */       if (debug8.isPresent())
/*    */       {
/* 75 */         debug2.getBrain().setMemory(MemoryModuleType.NEAREST_BED, debug7);
/*    */       }
/* 77 */     } else if (this.triedCount < 5) {
/* 78 */       this.batchCache.long2LongEntrySet().removeIf(debug1 -> (debug1.getLongValue() < this.lastUpdate));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\NearestBedSensor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */