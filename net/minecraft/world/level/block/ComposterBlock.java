/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2FloatMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.WorldlyContainerHolder;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ComposterBlock
/*     */   extends Block
/*     */   implements WorldlyContainerHolder {
/*  43 */   public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_COMPOSTER;
/*     */   
/*  45 */   public static final Object2FloatMap<ItemLike> COMPOSTABLES = (Object2FloatMap<ItemLike>)new Object2FloatOpenHashMap();
/*     */   
/*     */   public static void bootStrap() {
/*  48 */     COMPOSTABLES.defaultReturnValue(-1.0F);
/*     */     
/*  50 */     float debug0 = 0.3F;
/*  51 */     float debug1 = 0.5F;
/*  52 */     float debug2 = 0.65F;
/*  53 */     float debug3 = 0.85F;
/*  54 */     float debug4 = 1.0F;
/*     */     
/*  56 */     add(0.3F, (ItemLike)Items.JUNGLE_LEAVES);
/*  57 */     add(0.3F, (ItemLike)Items.OAK_LEAVES);
/*  58 */     add(0.3F, (ItemLike)Items.SPRUCE_LEAVES);
/*  59 */     add(0.3F, (ItemLike)Items.DARK_OAK_LEAVES);
/*  60 */     add(0.3F, (ItemLike)Items.ACACIA_LEAVES);
/*  61 */     add(0.3F, (ItemLike)Items.BIRCH_LEAVES);
/*  62 */     add(0.3F, (ItemLike)Items.OAK_SAPLING);
/*  63 */     add(0.3F, (ItemLike)Items.SPRUCE_SAPLING);
/*  64 */     add(0.3F, (ItemLike)Items.BIRCH_SAPLING);
/*  65 */     add(0.3F, (ItemLike)Items.JUNGLE_SAPLING);
/*  66 */     add(0.3F, (ItemLike)Items.ACACIA_SAPLING);
/*  67 */     add(0.3F, (ItemLike)Items.DARK_OAK_SAPLING);
/*  68 */     add(0.3F, (ItemLike)Items.BEETROOT_SEEDS);
/*  69 */     add(0.3F, (ItemLike)Items.DRIED_KELP);
/*  70 */     add(0.3F, (ItemLike)Items.GRASS);
/*  71 */     add(0.3F, (ItemLike)Items.KELP);
/*  72 */     add(0.3F, (ItemLike)Items.MELON_SEEDS);
/*  73 */     add(0.3F, (ItemLike)Items.PUMPKIN_SEEDS);
/*  74 */     add(0.3F, (ItemLike)Items.SEAGRASS);
/*  75 */     add(0.3F, (ItemLike)Items.SWEET_BERRIES);
/*  76 */     add(0.3F, (ItemLike)Items.WHEAT_SEEDS);
/*     */     
/*  78 */     add(0.5F, (ItemLike)Items.DRIED_KELP_BLOCK);
/*  79 */     add(0.5F, (ItemLike)Items.TALL_GRASS);
/*     */     
/*  81 */     add(0.5F, (ItemLike)Items.CACTUS);
/*  82 */     add(0.5F, (ItemLike)Items.SUGAR_CANE);
/*  83 */     add(0.5F, (ItemLike)Items.VINE);
/*  84 */     add(0.5F, (ItemLike)Items.NETHER_SPROUTS);
/*  85 */     add(0.5F, (ItemLike)Items.WEEPING_VINES);
/*  86 */     add(0.5F, (ItemLike)Items.TWISTING_VINES);
/*     */     
/*  88 */     add(0.5F, (ItemLike)Items.MELON_SLICE);
/*     */     
/*  90 */     add(0.65F, (ItemLike)Items.SEA_PICKLE);
/*  91 */     add(0.65F, (ItemLike)Items.LILY_PAD);
/*     */     
/*  93 */     add(0.65F, (ItemLike)Items.PUMPKIN);
/*  94 */     add(0.65F, (ItemLike)Items.CARVED_PUMPKIN);
/*  95 */     add(0.65F, (ItemLike)Items.MELON);
/*  96 */     add(0.65F, (ItemLike)Items.APPLE);
/*  97 */     add(0.65F, (ItemLike)Items.BEETROOT);
/*  98 */     add(0.65F, (ItemLike)Items.CARROT);
/*  99 */     add(0.65F, (ItemLike)Items.COCOA_BEANS);
/* 100 */     add(0.65F, (ItemLike)Items.POTATO);
/* 101 */     add(0.65F, (ItemLike)Items.WHEAT);
/*     */     
/* 103 */     add(0.65F, (ItemLike)Items.BROWN_MUSHROOM);
/* 104 */     add(0.65F, (ItemLike)Items.RED_MUSHROOM);
/* 105 */     add(0.65F, (ItemLike)Items.MUSHROOM_STEM);
/* 106 */     add(0.65F, (ItemLike)Items.CRIMSON_FUNGUS);
/* 107 */     add(0.65F, (ItemLike)Items.WARPED_FUNGUS);
/* 108 */     add(0.65F, (ItemLike)Items.NETHER_WART);
/* 109 */     add(0.65F, (ItemLike)Items.CRIMSON_ROOTS);
/* 110 */     add(0.65F, (ItemLike)Items.WARPED_ROOTS);
/* 111 */     add(0.65F, (ItemLike)Items.SHROOMLIGHT);
/*     */     
/* 113 */     add(0.65F, (ItemLike)Items.DANDELION);
/* 114 */     add(0.65F, (ItemLike)Items.POPPY);
/* 115 */     add(0.65F, (ItemLike)Items.BLUE_ORCHID);
/* 116 */     add(0.65F, (ItemLike)Items.ALLIUM);
/* 117 */     add(0.65F, (ItemLike)Items.AZURE_BLUET);
/* 118 */     add(0.65F, (ItemLike)Items.RED_TULIP);
/* 119 */     add(0.65F, (ItemLike)Items.ORANGE_TULIP);
/* 120 */     add(0.65F, (ItemLike)Items.WHITE_TULIP);
/* 121 */     add(0.65F, (ItemLike)Items.PINK_TULIP);
/* 122 */     add(0.65F, (ItemLike)Items.OXEYE_DAISY);
/* 123 */     add(0.65F, (ItemLike)Items.CORNFLOWER);
/* 124 */     add(0.65F, (ItemLike)Items.LILY_OF_THE_VALLEY);
/* 125 */     add(0.65F, (ItemLike)Items.WITHER_ROSE);
/* 126 */     add(0.65F, (ItemLike)Items.FERN);
/* 127 */     add(0.65F, (ItemLike)Items.SUNFLOWER);
/* 128 */     add(0.65F, (ItemLike)Items.LILAC);
/* 129 */     add(0.65F, (ItemLike)Items.ROSE_BUSH);
/* 130 */     add(0.65F, (ItemLike)Items.PEONY);
/* 131 */     add(0.65F, (ItemLike)Items.LARGE_FERN);
/*     */     
/* 133 */     add(0.85F, (ItemLike)Items.HAY_BLOCK);
/* 134 */     add(0.85F, (ItemLike)Items.BROWN_MUSHROOM_BLOCK);
/* 135 */     add(0.85F, (ItemLike)Items.RED_MUSHROOM_BLOCK);
/* 136 */     add(0.85F, (ItemLike)Items.NETHER_WART_BLOCK);
/* 137 */     add(0.85F, (ItemLike)Items.WARPED_WART_BLOCK);
/*     */     
/* 139 */     add(0.85F, (ItemLike)Items.BREAD);
/* 140 */     add(0.85F, (ItemLike)Items.BAKED_POTATO);
/* 141 */     add(0.85F, (ItemLike)Items.COOKIE);
/*     */     
/* 143 */     add(1.0F, (ItemLike)Items.CAKE);
/* 144 */     add(1.0F, (ItemLike)Items.PUMPKIN_PIE);
/*     */   }
/*     */   
/*     */   private static void add(float debug0, ItemLike debug1) {
/* 148 */     COMPOSTABLES.put(debug1.asItem(), debug0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 153 */   private static final VoxelShape OUTER_SHAPE = Shapes.block();
/*     */   static {
/* 155 */     SHAPES = (VoxelShape[])Util.make(new VoxelShape[9], debug0 -> {
/*     */           for (int debug1 = 0; debug1 < 8; debug1++)
/*     */             debug0[debug1] = Shapes.join(OUTER_SHAPE, Block.box(2.0D, Math.max(2, 1 + debug1 * 2), 2.0D, 14.0D, 16.0D, 14.0D), BooleanOp.ONLY_FIRST); 
/*     */           debug0[8] = debug0[7];
/*     */         });
/*     */   }
/*     */   private static final VoxelShape[] SHAPES;
/*     */   public ComposterBlock(BlockBehaviour.Properties debug1) {
/* 163 */     super(debug1);
/* 164 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LEVEL, Integer.valueOf(0)));
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
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 193 */     return SHAPES[((Integer)debug1.getValue((Property)LEVEL)).intValue()];
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getInteractionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 198 */     return OUTER_SHAPE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 204 */     return SHAPES[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 209 */     if (((Integer)debug1.getValue((Property)LEVEL)).intValue() == 7) {
/* 210 */       debug2.getBlockTicks().scheduleTick(debug3, debug1.getBlock(), 20);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 216 */     int debug7 = ((Integer)debug1.getValue((Property)LEVEL)).intValue();
/*     */     
/* 218 */     ItemStack debug8 = debug4.getItemInHand(debug5);
/* 219 */     if (debug7 < 8 && COMPOSTABLES.containsKey(debug8.getItem())) {
/* 220 */       if (debug7 < 7 && !debug2.isClientSide) {
/* 221 */         BlockState debug9 = addItem(debug1, (LevelAccessor)debug2, debug3, debug8);
/* 222 */         debug2.levelEvent(1500, debug3, (debug1 != debug9) ? 1 : 0);
/*     */         
/* 224 */         if (!debug4.abilities.instabuild) {
/* 225 */           debug8.shrink(1);
/*     */         }
/*     */       } 
/*     */       
/* 229 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/* 230 */     }  if (debug7 == 8) {
/* 231 */       extractProduce(debug1, debug2, debug3);
/* 232 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/*     */     
/* 235 */     return InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public static BlockState insertItem(BlockState debug0, ServerLevel debug1, ItemStack debug2, BlockPos debug3) {
/* 239 */     int debug4 = ((Integer)debug0.getValue((Property)LEVEL)).intValue();
/*     */     
/* 241 */     if (debug4 < 7 && COMPOSTABLES.containsKey(debug2.getItem())) {
/* 242 */       BlockState debug5 = addItem(debug0, (LevelAccessor)debug1, debug3, debug2);
/* 243 */       debug2.shrink(1);
/* 244 */       return debug5;
/*     */     } 
/*     */     
/* 247 */     return debug0;
/*     */   }
/*     */   
/*     */   public static BlockState extractProduce(BlockState debug0, Level debug1, BlockPos debug2) {
/* 251 */     if (!debug1.isClientSide) {
/* 252 */       float f = 0.7F;
/* 253 */       double debug4 = (debug1.random.nextFloat() * 0.7F) + 0.15000000596046448D;
/* 254 */       double debug6 = (debug1.random.nextFloat() * 0.7F) + 0.06000000238418579D + 0.6D;
/* 255 */       double debug8 = (debug1.random.nextFloat() * 0.7F) + 0.15000000596046448D;
/*     */       
/* 257 */       ItemEntity debug10 = new ItemEntity(debug1, debug2.getX() + debug4, debug2.getY() + debug6, debug2.getZ() + debug8, new ItemStack((ItemLike)Items.BONE_MEAL));
/* 258 */       debug10.setDefaultPickUpDelay();
/* 259 */       debug1.addFreshEntity((Entity)debug10);
/*     */     } 
/*     */     
/* 262 */     BlockState debug3 = empty(debug0, (LevelAccessor)debug1, debug2);
/* 263 */     debug1.playSound(null, debug2, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 264 */     return debug3;
/*     */   }
/*     */   
/*     */   private static BlockState empty(BlockState debug0, LevelAccessor debug1, BlockPos debug2) {
/* 268 */     BlockState debug3 = (BlockState)debug0.setValue((Property)LEVEL, Integer.valueOf(0));
/* 269 */     debug1.setBlock(debug2, debug3, 3);
/* 270 */     return debug3;
/*     */   }
/*     */   
/*     */   private static BlockState addItem(BlockState debug0, LevelAccessor debug1, BlockPos debug2, ItemStack debug3) {
/* 274 */     int debug4 = ((Integer)debug0.getValue((Property)LEVEL)).intValue();
/* 275 */     float debug5 = COMPOSTABLES.getFloat(debug3.getItem());
/* 276 */     if ((debug4 == 0 && debug5 > 0.0F) || debug1.getRandom().nextDouble() < debug5) {
/* 277 */       int debug6 = debug4 + 1;
/* 278 */       BlockState debug7 = (BlockState)debug0.setValue((Property)LEVEL, Integer.valueOf(debug6));
/* 279 */       debug1.setBlock(debug2, debug7, 3);
/*     */       
/* 281 */       if (debug6 == 7) {
/* 282 */         debug1.getBlockTicks().scheduleTick(debug2, debug0.getBlock(), 20);
/*     */       }
/*     */       
/* 285 */       return debug7;
/*     */     } 
/* 287 */     return debug0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 292 */     if (((Integer)debug1.getValue((Property)LEVEL)).intValue() == 7) {
/* 293 */       debug2.setBlock(debug3, (BlockState)debug1.cycle((Property)LEVEL), 3);
/* 294 */       debug2.playSound(null, debug3, SoundEvents.COMPOSTER_READY, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/* 300 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/* 305 */     return ((Integer)debug1.getValue((Property)LEVEL)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 310 */     debug1.add(new Property[] { (Property)LEVEL });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 315 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldlyContainer getContainer(BlockState debug1, LevelAccessor debug2, BlockPos debug3) {
/* 320 */     int debug4 = ((Integer)debug1.getValue((Property)LEVEL)).intValue();
/* 321 */     if (debug4 == 8) {
/* 322 */       return new OutputContainer(debug1, debug2, debug3, new ItemStack((ItemLike)Items.BONE_MEAL));
/*     */     }
/*     */     
/* 325 */     if (debug4 < 7) {
/* 326 */       return new InputContainer(debug1, debug2, debug3);
/*     */     }
/*     */     
/* 329 */     return new EmptyContainer();
/*     */   }
/*     */   
/*     */   static class EmptyContainer extends SimpleContainer implements WorldlyContainer {
/*     */     public EmptyContainer() {
/* 334 */       super(0);
/*     */     }
/*     */ 
/*     */     
/*     */     public int[] getSlotsForFace(Direction debug1) {
/* 339 */       return new int[0];
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canPlaceItemThroughFace(int debug1, ItemStack debug2, @Nullable Direction debug3) {
/* 344 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canTakeItemThroughFace(int debug1, ItemStack debug2, Direction debug3) {
/* 349 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   static class OutputContainer extends SimpleContainer implements WorldlyContainer {
/*     */     private final BlockState state;
/*     */     private final LevelAccessor level;
/*     */     private final BlockPos pos;
/*     */     private boolean changed;
/*     */     
/*     */     public OutputContainer(BlockState debug1, LevelAccessor debug2, BlockPos debug3, ItemStack debug4) {
/* 360 */       super(new ItemStack[] { debug4 });
/* 361 */       this.state = debug1;
/* 362 */       this.level = debug2;
/* 363 */       this.pos = debug3;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMaxStackSize() {
/* 368 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int[] getSlotsForFace(Direction debug1) {
/* 373 */       (new int[1])[0] = 0; return (debug1 == Direction.DOWN) ? new int[1] : new int[0];
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canPlaceItemThroughFace(int debug1, ItemStack debug2, @Nullable Direction debug3) {
/* 378 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canTakeItemThroughFace(int debug1, ItemStack debug2, Direction debug3) {
/* 383 */       return (!this.changed && debug3 == Direction.DOWN && debug2.getItem() == Items.BONE_MEAL);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setChanged() {
/* 388 */       ComposterBlock.empty(this.state, this.level, this.pos);
/* 389 */       this.changed = true;
/*     */     }
/*     */   }
/*     */   
/*     */   static class InputContainer extends SimpleContainer implements WorldlyContainer {
/*     */     private final BlockState state;
/*     */     private final LevelAccessor level;
/*     */     private final BlockPos pos;
/*     */     private boolean changed;
/*     */     
/*     */     public InputContainer(BlockState debug1, LevelAccessor debug2, BlockPos debug3) {
/* 400 */       super(1);
/* 401 */       this.state = debug1;
/* 402 */       this.level = debug2;
/* 403 */       this.pos = debug3;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMaxStackSize() {
/* 408 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int[] getSlotsForFace(Direction debug1) {
/* 413 */       (new int[1])[0] = 0; return (debug1 == Direction.UP) ? new int[1] : new int[0];
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canPlaceItemThroughFace(int debug1, ItemStack debug2, @Nullable Direction debug3) {
/* 418 */       return (!this.changed && debug3 == Direction.UP && ComposterBlock.COMPOSTABLES.containsKey(debug2.getItem()));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canTakeItemThroughFace(int debug1, ItemStack debug2, Direction debug3) {
/* 423 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setChanged() {
/* 428 */       ItemStack debug1 = getItem(0);
/* 429 */       if (!debug1.isEmpty()) {
/* 430 */         this.changed = true;
/* 431 */         BlockState debug2 = ComposterBlock.addItem(this.state, this.level, this.pos, debug1);
/* 432 */         this.level.levelEvent(1500, this.pos, (debug2 != this.state) ? 1 : 0);
/* 433 */         removeItemNoUpdate(0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\ComposterBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */