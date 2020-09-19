/*     */ package net.minecraft.world.level.block;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class NoteBlock extends Block {
/*  24 */   public static final EnumProperty<NoteBlockInstrument> INSTRUMENT = BlockStateProperties.NOTEBLOCK_INSTRUMENT;
/*  25 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*  26 */   public static final IntegerProperty NOTE = BlockStateProperties.NOTE;
/*     */   
/*     */   public NoteBlock(BlockBehaviour.Properties debug1) {
/*  29 */     super(debug1);
/*  30 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)INSTRUMENT, (Comparable)NoteBlockInstrument.HARP)).setValue((Property)NOTE, Integer.valueOf(0))).setValue((Property)POWERED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/*  35 */     return (BlockState)defaultBlockState().setValue((Property)INSTRUMENT, (Comparable)NoteBlockInstrument.byState(debug1.getLevel().getBlockState(debug1.getClickedPos().below())));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  40 */     if (debug2 == Direction.DOWN) {
/*  41 */       return (BlockState)debug1.setValue((Property)INSTRUMENT, (Comparable)NoteBlockInstrument.byState(debug3));
/*     */     }
/*  43 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/*  48 */     boolean debug7 = debug2.hasNeighborSignal(debug3);
/*     */     
/*  50 */     if (debug7 != ((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/*  51 */       if (debug7) {
/*  52 */         playNote(debug2, debug3);
/*     */       }
/*  54 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(debug7)), 3);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void playNote(Level debug1, BlockPos debug2) {
/*  59 */     if (debug1.getBlockState(debug2.above()).isAir()) {
/*  60 */       debug1.blockEvent(debug2, this, 0, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  66 */     if (debug2.isClientSide) {
/*  67 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  70 */     debug1 = (BlockState)debug1.cycle((Property)NOTE);
/*  71 */     debug2.setBlock(debug3, debug1, 3);
/*  72 */     playNote(debug2, debug3);
/*  73 */     debug4.awardStat(Stats.TUNE_NOTEBLOCK);
/*     */     
/*  75 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   public void attack(BlockState debug1, Level debug2, BlockPos debug3, Player debug4) {
/*  80 */     if (debug2.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/*  84 */     playNote(debug2, debug3);
/*  85 */     debug4.awardStat(Stats.PLAY_NOTEBLOCK);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean triggerEvent(BlockState debug1, Level debug2, BlockPos debug3, int debug4, int debug5) {
/*  90 */     int debug6 = ((Integer)debug1.getValue((Property)NOTE)).intValue();
/*  91 */     float debug7 = (float)Math.pow(2.0D, (debug6 - 12) / 12.0D);
/*     */     
/*  93 */     debug2.playSound(null, debug3, ((NoteBlockInstrument)debug1.getValue((Property)INSTRUMENT)).getSoundEvent(), SoundSource.RECORDS, 3.0F, debug7);
/*  94 */     debug2.addParticle((ParticleOptions)ParticleTypes.NOTE, debug3.getX() + 0.5D, debug3.getY() + 1.2D, debug3.getZ() + 0.5D, debug6 / 24.0D, 0.0D, 0.0D);
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 100 */     debug1.add(new Property[] { (Property)INSTRUMENT, (Property)POWERED, (Property)NOTE });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\NoteBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */