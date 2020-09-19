/*     */ package net.minecraft.world.level.block;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class DoublePlantBlock extends BushBlock {
/*  23 */   public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
/*     */   
/*     */   public DoublePlantBlock(BlockBehaviour.Properties debug1) {
/*  26 */     super(debug1);
/*     */     
/*  28 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HALF, (Comparable)DoubleBlockHalf.LOWER));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  33 */     DoubleBlockHalf debug7 = (DoubleBlockHalf)debug1.getValue((Property)HALF);
/*  34 */     if (debug2.getAxis() == Direction.Axis.Y) if (((debug7 == DoubleBlockHalf.LOWER) ? true : false) == ((debug2 == Direction.UP) ? true : false) && (
/*  35 */         !debug3.is(this) || debug3.getValue((Property)HALF) == debug7)) {
/*  36 */         return Blocks.AIR.defaultBlockState();
/*     */       }
/*     */ 
/*     */     
/*  40 */     if (debug7 == DoubleBlockHalf.LOWER && debug2 == Direction.DOWN && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/*  41 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  44 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  50 */     BlockPos debug2 = debug1.getClickedPos();
/*  51 */     if (debug2.getY() < 255 && debug1.getLevel().getBlockState(debug2.above()).canBeReplaced(debug1)) {
/*  52 */       return super.getStateForPlacement(debug1);
/*     */     }
/*     */     
/*  55 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, LivingEntity debug4, ItemStack debug5) {
/*  60 */     debug1.setBlock(debug2.above(), (BlockState)defaultBlockState().setValue((Property)HALF, (Comparable)DoubleBlockHalf.UPPER), 3);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  66 */     if (debug1.getValue((Property)HALF) == DoubleBlockHalf.UPPER) {
/*  67 */       BlockState debug4 = debug2.getBlockState(debug3.below());
/*  68 */       return (debug4.is(this) && debug4.getValue((Property)HALF) == DoubleBlockHalf.LOWER);
/*     */     } 
/*     */     
/*  71 */     return super.canSurvive(debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   public void placeAt(LevelAccessor debug1, BlockPos debug2, int debug3) {
/*  75 */     debug1.setBlock(debug2, (BlockState)defaultBlockState().setValue((Property)HALF, (Comparable)DoubleBlockHalf.LOWER), debug3);
/*  76 */     debug1.setBlock(debug2.above(), (BlockState)defaultBlockState().setValue((Property)HALF, (Comparable)DoubleBlockHalf.UPPER), debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerWillDestroy(Level debug1, BlockPos debug2, BlockState debug3, Player debug4) {
/*  81 */     if (!debug1.isClientSide) {
/*  82 */       if (debug4.isCreative()) {
/*  83 */         preventCreativeDropFromBottomPart(debug1, debug2, debug3, debug4);
/*     */       } else {
/*     */         
/*  86 */         dropResources(debug3, debug1, debug2, null, (Entity)debug4, debug4.getMainHandItem());
/*     */       } 
/*     */     }
/*     */     
/*  90 */     super.playerWillDestroy(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void playerDestroy(Level debug1, Player debug2, BlockPos debug3, BlockState debug4, @Nullable BlockEntity debug5, ItemStack debug6) {
/*  96 */     super.playerDestroy(debug1, debug2, debug3, Blocks.AIR.defaultBlockState(), debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void preventCreativeDropFromBottomPart(Level debug0, BlockPos debug1, BlockState debug2, Player debug3) {
/* 101 */     DoubleBlockHalf debug4 = (DoubleBlockHalf)debug2.getValue((Property)HALF);
/* 102 */     if (debug4 == DoubleBlockHalf.UPPER) {
/* 103 */       BlockPos debug5 = debug1.below();
/* 104 */       BlockState debug6 = debug0.getBlockState(debug5);
/* 105 */       if (debug6.getBlock() == debug2.getBlock() && debug6.getValue((Property)HALF) == DoubleBlockHalf.LOWER) {
/*     */         
/* 107 */         debug0.setBlock(debug5, Blocks.AIR.defaultBlockState(), 35);
/* 108 */         debug0.levelEvent(debug3, 2001, debug5, Block.getId(debug6));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 115 */     debug1.add(new Property[] { (Property)HALF });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockBehaviour.OffsetType getOffsetType() {
/* 120 */     return BlockBehaviour.OffsetType.XZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\DoublePlantBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */