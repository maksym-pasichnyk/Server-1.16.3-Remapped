/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WallTorchBlock
/*     */   extends TorchBlock {
/*  25 */   public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
/*     */ 
/*     */   
/*  28 */   private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap((Map)ImmutableMap.of(Direction.NORTH, 
/*  29 */         Block.box(5.5D, 3.0D, 11.0D, 10.5D, 13.0D, 16.0D), Direction.SOUTH, 
/*  30 */         Block.box(5.5D, 3.0D, 0.0D, 10.5D, 13.0D, 5.0D), Direction.WEST, 
/*  31 */         Block.box(11.0D, 3.0D, 5.5D, 16.0D, 13.0D, 10.5D), Direction.EAST, 
/*  32 */         Block.box(0.0D, 3.0D, 5.5D, 5.0D, 13.0D, 10.5D)));
/*     */ 
/*     */   
/*     */   protected WallTorchBlock(BlockBehaviour.Properties debug1, ParticleOptions debug2) {
/*  36 */     super(debug1, debug2);
/*  37 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescriptionId() {
/*  42 */     return asItem().getDescriptionId();
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  47 */     return getShape(debug1);
/*     */   }
/*     */   
/*     */   public static VoxelShape getShape(BlockState debug0) {
/*  51 */     return AABBS.get(debug0.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/*  56 */     Direction debug4 = (Direction)debug1.getValue((Property)FACING);
/*  57 */     BlockPos debug5 = debug3.relative(debug4.getOpposite());
/*  58 */     BlockState debug6 = debug2.getBlockState(debug5);
/*     */     
/*  60 */     return debug6.isFaceSturdy((BlockGetter)debug2, debug5, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  66 */     BlockState debug2 = defaultBlockState();
/*     */     
/*  68 */     Level level = debug1.getLevel();
/*  69 */     BlockPos debug4 = debug1.getClickedPos();
/*     */     
/*  71 */     Direction[] debug5 = debug1.getNearestLookingDirections();
/*  72 */     for (Direction debug9 : debug5) {
/*  73 */       if (debug9.getAxis().isHorizontal()) {
/*     */ 
/*     */ 
/*     */         
/*  77 */         Direction debug10 = debug9.getOpposite();
/*     */         
/*  79 */         debug2 = (BlockState)debug2.setValue((Property)FACING, (Comparable)debug10);
/*  80 */         if (debug2.canSurvive((LevelReader)level, debug4)) {
/*  81 */           return debug2;
/*     */         }
/*     */       } 
/*     */     } 
/*  85 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  90 */     if (debug2.getOpposite() == debug1.getValue((Property)FACING) && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/*  91 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  93 */     return debug1;
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
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 112 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 117 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 122 */     debug1.add(new Property[] { (Property)FACING });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\WallTorchBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */