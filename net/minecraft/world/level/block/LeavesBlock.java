/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LeavesBlock
/*     */   extends Block {
/*  25 */   public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;
/*  26 */   public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
/*     */ 
/*     */ 
/*     */   
/*     */   public LeavesBlock(BlockBehaviour.Properties debug1) {
/*  31 */     super(debug1);
/*  32 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)DISTANCE, Integer.valueOf(7))).setValue((Property)PERSISTENT, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getBlockSupportShape(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  37 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRandomlyTicking(BlockState debug1) {
/*  42 */     return (((Integer)debug1.getValue((Property)DISTANCE)).intValue() == 7 && !((Boolean)debug1.getValue((Property)PERSISTENT)).booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  47 */     if (!((Boolean)debug1.getValue((Property)PERSISTENT)).booleanValue() && ((Integer)debug1.getValue((Property)DISTANCE)).intValue() == 7) {
/*  48 */       dropResources(debug1, (Level)debug2, debug3);
/*  49 */       debug2.removeBlock(debug3, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  55 */     debug2.setBlock(debug3, updateDistance(debug1, (LevelAccessor)debug2, debug3), 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLightBlock(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/*  60 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  65 */     int debug7 = getDistanceAt(debug3) + 1;
/*  66 */     if (debug7 != 1 || ((Integer)debug1.getValue((Property)DISTANCE)).intValue() != debug7) {
/*  67 */       debug4.getBlockTicks().scheduleTick(debug5, this, 1);
/*     */     }
/*  69 */     return debug1;
/*     */   }
/*     */   
/*     */   private static BlockState updateDistance(BlockState debug0, LevelAccessor debug1, BlockPos debug2) {
/*  73 */     int debug3 = 7;
/*  74 */     BlockPos.MutableBlockPos debug4 = new BlockPos.MutableBlockPos();
/*  75 */     for (Direction debug8 : Direction.values()) {
/*  76 */       debug4.setWithOffset((Vec3i)debug2, debug8);
/*  77 */       debug3 = Math.min(debug3, getDistanceAt(debug1.getBlockState((BlockPos)debug4)) + 1);
/*  78 */       if (debug3 == 1) {
/*     */         break;
/*     */       }
/*     */     } 
/*  82 */     return (BlockState)debug0.setValue((Property)DISTANCE, Integer.valueOf(debug3));
/*     */   }
/*     */   
/*     */   private static int getDistanceAt(BlockState debug0) {
/*  86 */     if (BlockTags.LOGS.contains(debug0.getBlock())) {
/*  87 */       return 0;
/*     */     }
/*  89 */     if (debug0.getBlock() instanceof LeavesBlock) {
/*  90 */       return ((Integer)debug0.getValue((Property)DISTANCE)).intValue();
/*     */     }
/*  92 */     return 7;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 120 */     debug1.add(new Property[] { (Property)DISTANCE, (Property)PERSISTENT });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 125 */     return updateDistance((BlockState)defaultBlockState().setValue((Property)PERSISTENT, Boolean.valueOf(true)), (LevelAccessor)debug1.getLevel(), debug1.getClickedPos());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\LeavesBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */