/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.util.RandomPos;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*    */ import net.minecraft.world.entity.npc.Villager;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class GoToClosestVillage extends Behavior<Villager> {
/*    */   private final float speedModifier;
/*    */   
/*    */   public GoToClosestVillage(float debug1, int debug2) {
/* 20 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
/*    */ 
/*    */     
/* 23 */     this.speedModifier = debug1;
/* 24 */     this.closeEnoughDistance = debug2;
/*    */   }
/*    */   private final int closeEnoughDistance;
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, Villager debug2) {
/* 29 */     return !debug1.isVillage(debug2.blockPosition());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/* 34 */     PoiManager debug5 = debug1.getPoiManager();
/* 35 */     int debug6 = debug5.sectionsToVillage(SectionPos.of(debug2.blockPosition()));
/*    */     
/* 37 */     Vec3 debug7 = null;
/* 38 */     for (int debug8 = 0; debug8 < 5; debug8++) {
/* 39 */       Vec3 debug9 = RandomPos.getLandPos((PathfinderMob)debug2, 15, 7, debug1 -> -debug0.sectionsToVillage(SectionPos.of(debug1)));
/* 40 */       if (debug9 != null) {
/*    */ 
/*    */ 
/*    */         
/* 44 */         int debug10 = debug5.sectionsToVillage(SectionPos.of(new BlockPos(debug9)));
/* 45 */         if (debug10 < debug6) {
/* 46 */           debug7 = debug9; break;
/*    */         } 
/* 48 */         if (debug10 == debug6) {
/* 49 */           debug7 = debug9;
/*    */         }
/*    */       } 
/*    */     } 
/* 53 */     if (debug7 != null)
/* 54 */       debug2.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(debug7, this.speedModifier, this.closeEnoughDistance)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\GoToClosestVillage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */