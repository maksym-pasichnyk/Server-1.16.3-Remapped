/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.Random;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class FungusBlock extends BushBlock implements BonemealableBlock {
/* 18 */   protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 9.0D, 12.0D);
/*    */   
/*    */   private final Supplier<ConfiguredFeature<HugeFungusConfiguration, ?>> feature;
/*    */ 
/*    */   
/*    */   protected FungusBlock(BlockBehaviour.Properties debug1, Supplier<ConfiguredFeature<HugeFungusConfiguration, ?>> debug2) {
/* 24 */     super(debug1);
/* 25 */     this.feature = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 30 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 35 */     return (debug1.is((Tag)BlockTags.NYLIUM) || debug1.is(Blocks.MYCELIUM) || debug1.is(Blocks.SOUL_SOIL) || super.mayPlaceOn(debug1, debug2, debug3));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 40 */     Block debug5 = ((HugeFungusConfiguration)((ConfiguredFeature)this.feature.get()).config).validBaseState.getBlock();
/* 41 */     Block debug6 = debug1.getBlockState(debug2.below()).getBlock();
/*    */     
/* 43 */     return (debug6 == debug5);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 48 */     return (debug2.nextFloat() < 0.4D);
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 53 */     ((ConfiguredFeature)this.feature.get()).place((WorldGenLevel)debug1, debug1.getChunkSource().getGenerator(), debug2, debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\FungusBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */