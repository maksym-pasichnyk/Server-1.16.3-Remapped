/*    */ package net.minecraft.world.level.material;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class EmptyFluid
/*    */   extends Fluid
/*    */ {
/*    */   public Item getBucket() {
/* 19 */     return Items.AIR;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBeReplacedWith(FluidState debug1, BlockGetter debug2, BlockPos debug3, Fluid debug4, Direction debug5) {
/* 24 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getFlow(BlockGetter debug1, BlockPos debug2, FluidState debug3) {
/* 29 */     return Vec3.ZERO;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTickDelay(LevelReader debug1) {
/* 34 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isEmpty() {
/* 39 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getExplosionResistance() {
/* 44 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getHeight(FluidState debug1, BlockGetter debug2, BlockPos debug3) {
/* 49 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getOwnHeight(FluidState debug1) {
/* 54 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState createLegacyBlock(FluidState debug1) {
/* 59 */     return Blocks.AIR.defaultBlockState();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSource(FluidState debug1) {
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAmount(FluidState debug1) {
/* 69 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(FluidState debug1, BlockGetter debug2, BlockPos debug3) {
/* 74 */     return Shapes.empty();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\material\EmptyFluid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */