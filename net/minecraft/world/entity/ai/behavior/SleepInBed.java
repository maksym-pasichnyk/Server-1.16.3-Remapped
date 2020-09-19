/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ import net.minecraft.world.level.block.BedBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class SleepInBed
/*    */   extends Behavior<LivingEntity> {
/*    */   public SleepInBed() {
/* 24 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LAST_WOKEN, MemoryStatus.REGISTERED));
/*    */   }
/*    */ 
/*    */   
/*    */   private long nextOkStartTime;
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, LivingEntity debug2) {
/* 32 */     if (debug2.isPassenger()) {
/* 33 */       return false;
/*    */     }
/* 35 */     Brain<?> debug3 = debug2.getBrain();
/*    */     
/* 37 */     GlobalPos debug4 = debug3.getMemory(MemoryModuleType.HOME).get();
/* 38 */     if (debug1.dimension() != debug4.dimension()) {
/* 39 */       return false;
/*    */     }
/*    */     
/* 42 */     Optional<Long> debug5 = debug3.getMemory(MemoryModuleType.LAST_WOKEN);
/* 43 */     if (debug5.isPresent()) {
/* 44 */       long l = debug1.getGameTime() - ((Long)debug5.get()).longValue();
/* 45 */       if (l > 0L && l < 100L)
/*    */       {
/* 47 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 51 */     BlockState debug6 = debug1.getBlockState(debug4.pos());
/* 52 */     return (debug4.pos().closerThan((Position)debug2.position(), 2.0D) && debug6.getBlock().is((Tag)BlockTags.BEDS) && !((Boolean)debug6.getValue((Property)BedBlock.OCCUPIED)).booleanValue());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 57 */     Optional<GlobalPos> debug5 = debug2.getBrain().getMemory(MemoryModuleType.HOME);
/*    */     
/* 59 */     if (!debug5.isPresent()) {
/* 60 */       return false;
/*    */     }
/*    */     
/* 63 */     BlockPos debug6 = ((GlobalPos)debug5.get()).pos();
/* 64 */     return (debug2.getBrain().isActive(Activity.REST) && debug2.getY() > debug6.getY() + 0.4D && debug6.closerThan((Position)debug2.position(), 1.14D));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 69 */     if (debug3 > this.nextOkStartTime) {
/* 70 */       InteractWithDoor.closeDoorsThatIHaveOpenedOrPassedThrough(debug1, debug2, null, null);
/* 71 */       debug2.startSleeping(((GlobalPos)debug2.getBrain().getMemory(MemoryModuleType.HOME).get()).pos());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean timedOut(long debug1) {
/* 77 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 82 */     if (debug2.isSleeping()) {
/* 83 */       debug2.stopSleeping();
/* 84 */       this.nextOkStartTime = debug3 + 40L;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SleepInBed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */