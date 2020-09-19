/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.BlockItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FlowerPotBlock extends Block {
/*  25 */   private static final Map<Block, Block> POTTED_BY_CONTENT = Maps.newHashMap();
/*     */ 
/*     */   
/*  28 */   protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);
/*     */   
/*     */   private final Block content;
/*     */   
/*     */   public FlowerPotBlock(Block debug1, BlockBehaviour.Properties debug2) {
/*  33 */     super(debug2);
/*  34 */     this.content = debug1;
/*     */     
/*  36 */     POTTED_BY_CONTENT.put(debug1, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/*  41 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/*  46 */     return RenderShape.MODEL;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/*  51 */     ItemStack debug7 = debug4.getItemInHand(debug5);
/*     */     
/*  53 */     Item debug8 = debug7.getItem();
/*  54 */     Block debug9 = (debug8 instanceof BlockItem) ? POTTED_BY_CONTENT.getOrDefault(((BlockItem)debug8).getBlock(), Blocks.AIR) : Blocks.AIR;
/*  55 */     boolean debug10 = (debug9 == Blocks.AIR);
/*  56 */     boolean debug11 = (this.content == Blocks.AIR);
/*     */     
/*  58 */     if (debug10 != debug11) {
/*  59 */       if (debug11) {
/*  60 */         debug2.setBlock(debug3, debug9.defaultBlockState(), 3);
/*  61 */         debug4.awardStat(Stats.POT_FLOWER);
/*     */         
/*  63 */         if (!debug4.abilities.instabuild) {
/*  64 */           debug7.shrink(1);
/*     */         }
/*     */       } else {
/*  67 */         ItemStack debug12 = new ItemStack(this.content);
/*  68 */         if (debug7.isEmpty()) {
/*  69 */           debug4.setItemInHand(debug5, debug12);
/*  70 */         } else if (!debug4.addItem(debug12)) {
/*  71 */           debug4.drop(debug12, false);
/*     */         } 
/*  73 */         debug2.setBlock(debug3, Blocks.FLOWER_POT.defaultBlockState(), 3);
/*     */       } 
/*  75 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/*     */     
/*  78 */     return InteractionResult.CONSUME;
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
/*     */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/*  91 */     if (debug2 == Direction.DOWN && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/*  92 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  95 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*     */   }
/*     */   
/*     */   public Block getContent() {
/*  99 */     return this.content;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 104 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\FlowerPotBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */