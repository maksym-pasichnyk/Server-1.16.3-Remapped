/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class FlowerBlock
/*    */   extends BushBlock {
/* 14 */   protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);
/*    */   private final MobEffect suspiciousStewEffect;
/*    */   private final int effectDuration;
/*    */   
/*    */   public FlowerBlock(MobEffect debug1, int debug2, BlockBehaviour.Properties debug3) {
/* 19 */     super(debug3);
/* 20 */     this.suspiciousStewEffect = debug1;
/* 21 */     if (debug1.isInstantenous()) {
/* 22 */       this.effectDuration = debug2;
/*    */     } else {
/* 24 */       this.effectDuration = debug2 * 20;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 30 */     Vec3 debug5 = debug1.getOffset(debug2, debug3);
/* 31 */     return SHAPE.move(debug5.x, debug5.y, debug5.z);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockBehaviour.OffsetType getOffsetType() {
/* 36 */     return BlockBehaviour.OffsetType.XZ;
/*    */   }
/*    */   
/*    */   public MobEffect getSuspiciousStewEffect() {
/* 40 */     return this.suspiciousStewEffect;
/*    */   }
/*    */   
/*    */   public int getEffectDuration() {
/* 44 */     return this.effectDuration;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\FlowerBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */