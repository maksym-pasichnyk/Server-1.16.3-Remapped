/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.BellBlock;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RingBell extends Behavior<LivingEntity> {
/*    */   public RingBell() {
/* 19 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, LivingEntity debug2) {
/* 24 */     return (debug1.random.nextFloat() > 0.95F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 29 */     Brain<?> debug5 = debug2.getBrain();
/* 30 */     BlockPos debug6 = ((GlobalPos)debug5.getMemory(MemoryModuleType.MEETING_POINT).get()).pos();
/*    */     
/* 32 */     if (debug6.closerThan((Vec3i)debug2.blockPosition(), 3.0D)) {
/* 33 */       BlockState debug7 = debug1.getBlockState(debug6);
/* 34 */       if (debug7.is(Blocks.BELL)) {
/* 35 */         BellBlock debug8 = (BellBlock)debug7.getBlock();
/* 36 */         debug8.attemptToRing((Level)debug1, debug6, null);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\RingBell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */