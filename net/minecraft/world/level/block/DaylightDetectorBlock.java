/*     */ package net.minecraft.world.level.block;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.DaylightDetectorBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class DaylightDetectorBlock extends BaseEntityBlock {
/*  25 */   public static final IntegerProperty POWER = BlockStateProperties.POWER;
/*  26 */   public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
/*     */   
/*  28 */   protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);
/*     */   
/*     */   public DaylightDetectorBlock(BlockBehaviour.Properties debug1) {
/*  31 */     super(debug1);
/*     */     
/*  33 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWER, Integer.valueOf(0))).setValue((Property)INVERTED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  38 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean useShapeForLightOcclusion(BlockState debug1) {
/*  43 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/*  48 */     return ((Integer)debug1.getValue((Property)POWER)).intValue();
/*     */   }
/*     */   
/*     */   public static void updateSignalStrength(BlockState debug0, Level debug1, BlockPos debug2) {
/*  52 */     if (!debug1.dimensionType().hasSkyLight()) {
/*     */       return;
/*     */     }
/*     */     
/*  56 */     int debug3 = debug1.getBrightness(LightLayer.SKY, debug2) - debug1.getSkyDarken();
/*  57 */     float debug4 = debug1.getSunAngle(1.0F);
/*     */     
/*  59 */     boolean debug5 = ((Boolean)debug0.getValue((Property)INVERTED)).booleanValue();
/*  60 */     if (debug5) {
/*  61 */       debug3 = 15 - debug3;
/*  62 */     } else if (debug3 > 0) {
/*     */       
/*  64 */       float debug6 = (debug4 < 3.1415927F) ? 0.0F : 6.2831855F;
/*  65 */       debug4 += (debug6 - debug4) * 0.2F;
/*     */       
/*  67 */       debug3 = Math.round(debug3 * Mth.cos(debug4));
/*     */     } 
/*  69 */     debug3 = Mth.clamp(debug3, 0, 15);
/*     */     
/*  71 */     if (((Integer)debug0.getValue((Property)POWER)).intValue() != debug3) {
/*  72 */       debug1.setBlock(debug2, (BlockState)debug0.setValue((Property)POWER, Integer.valueOf(debug3)), 3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  78 */     if (debug4.mayBuild()) {
/*  79 */       if (debug2.isClientSide) {
/*  80 */         return InteractionResult.SUCCESS;
/*     */       }
/*     */       
/*  83 */       BlockState debug7 = (BlockState)debug1.cycle((Property)INVERTED);
/*  84 */       debug2.setBlock(debug3, debug7, 4);
/*  85 */       updateSignalStrength(debug7, debug2, debug3);
/*     */       
/*  87 */       return InteractionResult.CONSUME;
/*     */     } 
/*  89 */     return super.use(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/*  94 */     return RenderShape.MODEL;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSignalSource(BlockState debug1) {
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 104 */     return (BlockEntity)new DaylightDetectorBlockEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 109 */     debug1.add(new Property[] { (Property)POWER, (Property)INVERTED });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\DaylightDetectorBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */