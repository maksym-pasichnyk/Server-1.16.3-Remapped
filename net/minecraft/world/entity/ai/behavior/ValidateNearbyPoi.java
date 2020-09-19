/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.network.protocol.game.DebugPackets;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.level.block.BedBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class ValidateNearbyPoi
/*    */   extends Behavior<LivingEntity>
/*    */ {
/*    */   private final MemoryModuleType<GlobalPos> memoryType;
/*    */   private final Predicate<PoiType> poiPredicate;
/*    */   
/*    */   public ValidateNearbyPoi(PoiType debug1, MemoryModuleType<GlobalPos> debug2) {
/* 29 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(debug2, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */     
/* 33 */     this.poiPredicate = debug1.getPredicate();
/* 34 */     this.memoryType = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, LivingEntity debug2) {
/* 39 */     GlobalPos debug3 = debug2.getBrain().getMemory(this.memoryType).get();
/* 40 */     return (debug1.dimension() == debug3.dimension() && debug3
/* 41 */       .pos().closerThan((Position)debug2.position(), 16.0D));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 46 */     Brain<?> debug5 = debug2.getBrain();
/* 47 */     GlobalPos debug6 = debug5.getMemory(this.memoryType).get();
/* 48 */     BlockPos debug7 = debug6.pos();
/* 49 */     ServerLevel debug8 = debug1.getServer().getLevel(debug6.dimension());
/* 50 */     if (debug8 == null || poiDoesntExist(debug8, debug7)) {
/* 51 */       debug5.eraseMemory(this.memoryType);
/* 52 */     } else if (bedIsOccupied(debug8, debug7, debug2)) {
/* 53 */       debug5.eraseMemory(this.memoryType);
/* 54 */       debug1.getPoiManager().release(debug7);
/* 55 */       DebugPackets.sendPoiTicketCountPacket(debug1, debug7);
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean bedIsOccupied(ServerLevel debug1, BlockPos debug2, LivingEntity debug3) {
/* 60 */     BlockState debug4 = debug1.getBlockState(debug2);
/* 61 */     return (debug4.getBlock().is((Tag)BlockTags.BEDS) && ((Boolean)debug4.getValue((Property)BedBlock.OCCUPIED)).booleanValue() && !debug3.isSleeping());
/*    */   }
/*    */   
/*    */   private boolean poiDoesntExist(ServerLevel debug1, BlockPos debug2) {
/* 65 */     return !debug1.getPoiManager().exists(debug2, this.poiPredicate);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\ValidateNearbyPoi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */