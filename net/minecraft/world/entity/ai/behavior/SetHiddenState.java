/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SetHiddenState
/*    */   extends Behavior<LivingEntity>
/*    */ {
/*    */   private final int closeEnoughDist;
/*    */   private final int stayHiddenTicks;
/*    */   private int ticksHidden;
/*    */   
/*    */   public SetHiddenState(int debug1, int debug2) {
/* 29 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.HIDING_PLACE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.HEARD_BELL_TIME, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 34 */     this.stayHiddenTicks = debug1 * 20;
/* 35 */     this.ticksHidden = 0;
/* 36 */     this.closeEnoughDist = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 41 */     Brain<?> debug5 = debug2.getBrain();
/* 42 */     Optional<Long> debug6 = debug5.getMemory(MemoryModuleType.HEARD_BELL_TIME);
/* 43 */     boolean debug7 = (((Long)debug6.get()).longValue() + 300L <= debug3);
/*    */     
/* 45 */     if (this.ticksHidden > this.stayHiddenTicks || debug7) {
/* 46 */       debug5.eraseMemory(MemoryModuleType.HEARD_BELL_TIME);
/* 47 */       debug5.eraseMemory(MemoryModuleType.HIDING_PLACE);
/* 48 */       debug5.updateActivityFromSchedule(debug1.getDayTime(), debug1.getGameTime());
/* 49 */       this.ticksHidden = 0;
/*    */       
/*    */       return;
/*    */     } 
/* 53 */     BlockPos debug8 = ((GlobalPos)debug5.getMemory(MemoryModuleType.HIDING_PLACE).get()).pos();
/* 54 */     if (debug8.closerThan((Vec3i)debug2.blockPosition(), this.closeEnoughDist))
/* 55 */       this.ticksHidden++; 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetHiddenState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */