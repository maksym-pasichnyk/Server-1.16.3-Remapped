/*     */ package net.minecraft.world.level.chunk;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*     */ import java.util.EnumSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Direction8;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.HorizontalDirectionalBlock;
/*     */ import net.minecraft.world.level.block.StemBlock;
/*     */ import net.minecraft.world.level.block.StemGrownBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class UpgradeData {
/*  36 */   private static final Logger LOGGER = LogManager.getLogger();
/*  37 */   public static final UpgradeData EMPTY = new UpgradeData();
/*     */   
/*  39 */   private static final Direction8[] DIRECTIONS = Direction8.values();
/*     */   
/*  41 */   private final EnumSet<Direction8> sides = EnumSet.noneOf(Direction8.class);
/*  42 */   private final int[][] index = new int[16][];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UpgradeData(CompoundTag debug1) {
/*  48 */     this();
/*     */     
/*  50 */     if (debug1.contains("Indices", 10)) {
/*  51 */       CompoundTag compoundTag = debug1.getCompound("Indices");
/*  52 */       for (int debug3 = 0; debug3 < this.index.length; debug3++) {
/*  53 */         String debug4 = String.valueOf(debug3);
/*  54 */         if (compoundTag.contains(debug4, 11)) {
/*  55 */           this.index[debug3] = compoundTag.getIntArray(debug4);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  60 */     int debug2 = debug1.getInt("Sides");
/*  61 */     for (Direction8 debug6 : Direction8.values()) {
/*  62 */       if ((debug2 & 1 << debug6.ordinal()) != 0) {
/*  63 */         this.sides.add(debug6);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void upgrade(LevelChunk debug1) {
/*  69 */     upgradeInside(debug1);
/*  70 */     for (Direction8 debug5 : DIRECTIONS) {
/*  71 */       upgradeSides(debug1, debug5);
/*     */     }
/*     */     
/*  74 */     Level debug2 = debug1.getLevel();
/*  75 */     CHUNKY_FIXERS.forEach(debug1 -> debug1.processChunk((LevelAccessor)debug0));
/*     */   }
/*     */   
/*     */   private static void upgradeSides(LevelChunk debug0, Direction8 debug1) {
/*  79 */     Level debug2 = debug0.getLevel();
/*     */     
/*  81 */     if (!(debug0.getUpgradeData()).sides.remove(debug1)) {
/*     */       return;
/*     */     }
/*     */     
/*  85 */     Set<Direction> debug3 = debug1.getDirections();
/*     */     
/*  87 */     int debug4 = 0;
/*  88 */     int debug5 = 15;
/*     */     
/*  90 */     boolean debug6 = debug3.contains(Direction.EAST);
/*  91 */     boolean debug7 = debug3.contains(Direction.WEST);
/*  92 */     boolean debug8 = debug3.contains(Direction.SOUTH);
/*  93 */     boolean debug9 = debug3.contains(Direction.NORTH);
/*  94 */     boolean debug10 = (debug3.size() == 1);
/*     */     
/*  96 */     ChunkPos debug11 = debug0.getPos();
/*  97 */     int debug12 = debug11.getMinBlockX() + ((debug10 && (debug9 || debug8)) ? 1 : (debug7 ? 0 : 15));
/*  98 */     int debug13 = debug11.getMinBlockX() + ((debug10 && (debug9 || debug8)) ? 14 : (debug7 ? 0 : 15));
/*  99 */     int debug14 = debug11.getMinBlockZ() + ((debug10 && (debug6 || debug7)) ? 1 : (debug9 ? 0 : 15));
/* 100 */     int debug15 = debug11.getMinBlockZ() + ((debug10 && (debug6 || debug7)) ? 14 : (debug9 ? 0 : 15));
/*     */     
/* 102 */     Direction[] debug16 = Direction.values();
/* 103 */     BlockPos.MutableBlockPos debug17 = new BlockPos.MutableBlockPos();
/* 104 */     for (BlockPos debug19 : BlockPos.betweenClosed(debug12, 0, debug14, debug13, debug2.getMaxBuildHeight() - 1, debug15)) {
/* 105 */       BlockState debug20 = debug2.getBlockState(debug19);
/* 106 */       BlockState debug21 = debug20;
/*     */       
/* 108 */       for (Direction debug25 : debug16) {
/* 109 */         debug17.setWithOffset((Vec3i)debug19, debug25);
/* 110 */         debug21 = updateState(debug21, debug25, (LevelAccessor)debug2, debug19, (BlockPos)debug17);
/*     */       } 
/*     */       
/* 113 */       Block.updateOrDestroy(debug20, debug21, (LevelAccessor)debug2, debug19, 18);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static BlockState updateState(BlockState debug0, Direction debug1, LevelAccessor debug2, BlockPos debug3, BlockPos debug4) {
/* 118 */     return ((BlockFixer)MAP.getOrDefault(debug0.getBlock(), BlockFixers.DEFAULT)).updateShape(debug0, debug1, debug2.getBlockState(debug4), debug2, debug3, debug4);
/*     */   }
/*     */   
/*     */   private void upgradeInside(LevelChunk debug1) {
/* 122 */     BlockPos.MutableBlockPos debug2 = new BlockPos.MutableBlockPos();
/* 123 */     BlockPos.MutableBlockPos debug3 = new BlockPos.MutableBlockPos();
/*     */     
/* 125 */     ChunkPos debug4 = debug1.getPos();
/* 126 */     Level level = debug1.getLevel(); int debug6;
/* 127 */     for (debug6 = 0; debug6 < 16; debug6++) {
/* 128 */       LevelChunkSection debug7 = debug1.getSections()[debug6];
/* 129 */       int[] debug8 = this.index[debug6];
/* 130 */       this.index[debug6] = null;
/*     */       
/* 132 */       if (debug7 != null && debug8 != null && debug8.length > 0) {
/*     */ 
/*     */ 
/*     */         
/* 136 */         Direction[] debug9 = Direction.values();
/* 137 */         PalettedContainer<BlockState> debug10 = debug7.getStates();
/*     */         
/* 139 */         for (int debug14 : debug8) {
/* 140 */           int debug15 = debug14 & 0xF;
/* 141 */           int debug16 = debug14 >> 8 & 0xF;
/* 142 */           int debug17 = debug14 >> 4 & 0xF;
/*     */           
/* 144 */           debug2.set(debug4.getMinBlockX() + debug15, (debug6 << 4) + debug16, debug4.getMinBlockZ() + debug17);
/*     */           
/* 146 */           BlockState debug18 = debug10.get(debug14);
/* 147 */           BlockState debug19 = debug18;
/*     */           
/* 149 */           for (Direction debug23 : debug9) {
/* 150 */             debug3.setWithOffset((Vec3i)debug2, debug23);
/* 151 */             if (debug2.getX() >> 4 == debug4.x && debug2.getZ() >> 4 == debug4.z)
/*     */             {
/*     */               
/* 154 */               debug19 = updateState(debug19, debug23, (LevelAccessor)level, (BlockPos)debug2, (BlockPos)debug3); } 
/*     */           } 
/* 156 */           Block.updateOrDestroy(debug18, debug19, (LevelAccessor)level, (BlockPos)debug2, 18);
/*     */         } 
/*     */       } 
/* 159 */     }  for (debug6 = 0; debug6 < this.index.length; debug6++) {
/* 160 */       if (this.index[debug6] != null) {
/* 161 */         LOGGER.warn("Discarding update data for section {} for chunk ({} {})", Integer.valueOf(debug6), Integer.valueOf(debug4.x), Integer.valueOf(debug4.z));
/*     */       }
/* 163 */       this.index[debug6] = null;
/*     */     } 
/*     */   }
/*     */   
/* 167 */   private static final Map<Block, BlockFixer> MAP = new IdentityHashMap<>();
/* 168 */   private static final Set<BlockFixer> CHUNKY_FIXERS = Sets.newHashSet();
/*     */   
/*     */   public boolean isEmpty() {
/* 171 */     for (int[] debug4 : this.index) {
/* 172 */       if (debug4 != null) {
/* 173 */         return false;
/*     */       }
/*     */     } 
/* 176 */     return this.sides.isEmpty();
/*     */   }
/*     */   
/*     */   public static interface BlockFixer {
/*     */     BlockState updateShape(BlockState param1BlockState1, Direction param1Direction, BlockState param1BlockState2, LevelAccessor param1LevelAccessor, BlockPos param1BlockPos1, BlockPos param1BlockPos2);
/*     */     
/*     */     default void processChunk(LevelAccessor debug1) {}
/*     */   }
/*     */   
/*     */   enum BlockFixers
/*     */     implements BlockFixer {
/* 187 */     BLACKLIST((String)new Block[] { Blocks.OBSERVER, Blocks.NETHER_PORTAL, Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER, Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL, Blocks.DRAGON_EGG, Blocks.GRAVEL, Blocks.SAND, Blocks.RED_SAND, Blocks.OAK_SIGN, Blocks.SPRUCE_SIGN, Blocks.BIRCH_SIGN, Blocks.ACACIA_SIGN, Blocks.JUNGLE_SIGN, Blocks.DARK_OAK_SIGN, Blocks.OAK_WALL_SIGN, Blocks.SPRUCE_WALL_SIGN, Blocks.BIRCH_WALL_SIGN, Blocks.ACACIA_WALL_SIGN, Blocks.JUNGLE_WALL_SIGN, Blocks.DARK_OAK_WALL_SIGN })
/*     */     {
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
/*     */       public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6)
/*     */       {
/* 230 */         return debug1;
/*     */       }
/*     */     },
/* 233 */     DEFAULT((String)new Block[0])
/*     */     {
/*     */       public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 236 */         return debug1.updateShape(debug2, debug4.getBlockState(debug6), debug4, debug5, debug6);
/*     */       }
/*     */     },
/* 239 */     CHEST((String)new Block[] { Blocks.CHEST, Blocks.TRAPPED_CHEST })
/*     */     {
/*     */       public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 242 */         if (debug3.is(debug1.getBlock()) && debug2.getAxis().isHorizontal() && 
/* 243 */           debug1.getValue((Property)ChestBlock.TYPE) == ChestType.SINGLE && debug3.getValue((Property)ChestBlock.TYPE) == ChestType.SINGLE) {
/* 244 */           Direction debug7 = (Direction)debug1.getValue((Property)ChestBlock.FACING);
/* 245 */           if (debug2.getAxis() != debug7.getAxis() && debug7 == debug3.getValue((Property)ChestBlock.FACING)) {
/* 246 */             ChestType debug8 = (debug2 == debug7.getClockWise()) ? ChestType.LEFT : ChestType.RIGHT;
/* 247 */             debug4.setBlock(debug6, (BlockState)debug3.setValue((Property)ChestBlock.TYPE, (Comparable)debug8.getOpposite()), 18);
/*     */ 
/*     */             
/* 250 */             if (debug7 == Direction.NORTH || debug7 == Direction.EAST) {
/* 251 */               BlockEntity debug9 = debug4.getBlockEntity(debug5);
/* 252 */               BlockEntity debug10 = debug4.getBlockEntity(debug6);
/* 253 */               if (debug9 instanceof ChestBlockEntity && debug10 instanceof ChestBlockEntity) {
/* 254 */                 ChestBlockEntity.swapContents((ChestBlockEntity)debug9, (ChestBlockEntity)debug10);
/*     */               }
/*     */             } 
/*     */             
/* 258 */             return (BlockState)debug1.setValue((Property)ChestBlock.TYPE, (Comparable)debug8);
/*     */           } 
/*     */         } 
/*     */         
/* 262 */         return debug1;
/*     */       }
/*     */     },
/* 265 */     LEAVES(true, new Block[] { Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES }) {
/* 266 */       private final ThreadLocal<List<ObjectSet<BlockPos>>> queue = ThreadLocal.withInitial(() -> Lists.newArrayListWithCapacity(7));
/*     */ 
/*     */       
/*     */       public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 270 */         BlockState debug7 = debug1.updateShape(debug2, debug4.getBlockState(debug6), debug4, debug5, debug6);
/* 271 */         if (debug1 != debug7) {
/* 272 */           int debug8 = ((Integer)debug7.getValue((Property)BlockStateProperties.DISTANCE)).intValue();
/* 273 */           List<ObjectSet<BlockPos>> debug9 = this.queue.get();
/* 274 */           if (debug9.isEmpty()) {
/* 275 */             for (int debug10 = 0; debug10 < 7; debug10++) {
/* 276 */               debug9.add(new ObjectOpenHashSet());
/*     */             }
/*     */           }
/* 279 */           ((ObjectSet)debug9.get(debug8)).add(debug5.immutable());
/*     */         } 
/* 281 */         return debug1;
/*     */       }
/*     */ 
/*     */       
/*     */       public void processChunk(LevelAccessor debug1) {
/* 286 */         BlockPos.MutableBlockPos debug2 = new BlockPos.MutableBlockPos();
/*     */         
/* 288 */         List<ObjectSet<BlockPos>> debug3 = this.queue.get();
/* 289 */         for (int debug4 = 2; debug4 < debug3.size(); debug4++) {
/* 290 */           int debug5 = debug4 - 1;
/* 291 */           ObjectSet<BlockPos> debug6 = debug3.get(debug5);
/* 292 */           ObjectSet<BlockPos> debug7 = debug3.get(debug4);
/*     */           
/* 294 */           for (ObjectIterator<BlockPos> objectIterator = debug6.iterator(); objectIterator.hasNext(); ) { BlockPos debug9 = objectIterator.next();
/* 295 */             BlockState debug10 = debug1.getBlockState(debug9);
/* 296 */             if (((Integer)debug10.getValue((Property)BlockStateProperties.DISTANCE)).intValue() < debug5) {
/*     */               continue;
/*     */             }
/*     */             
/* 300 */             debug1.setBlock(debug9, (BlockState)debug10.setValue((Property)BlockStateProperties.DISTANCE, Integer.valueOf(debug5)), 18);
/*     */             
/* 302 */             if (debug4 != 7) {
/* 303 */               for (Direction debug14 : DIRECTIONS) {
/* 304 */                 debug2.setWithOffset((Vec3i)debug9, debug14);
/* 305 */                 BlockState debug15 = debug1.getBlockState((BlockPos)debug2);
/*     */                 
/* 307 */                 if (debug15.hasProperty((Property)BlockStateProperties.DISTANCE) && ((Integer)debug10.getValue((Property)BlockStateProperties.DISTANCE)).intValue() > debug4) {
/* 308 */                   debug7.add(debug2.immutable());
/*     */                 }
/*     */               } 
/*     */             } }
/*     */         
/*     */         } 
/*     */         
/* 315 */         debug3.clear();
/*     */       }
/*     */     },
/* 318 */     STEM_BLOCK((String)new Block[] { Blocks.MELON_STEM, Blocks.PUMPKIN_STEM })
/*     */     {
/*     */       public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 321 */         if (((Integer)debug1.getValue((Property)StemBlock.AGE)).intValue() == 7) {
/* 322 */           StemGrownBlock debug7 = ((StemBlock)debug1.getBlock()).getFruit();
/* 323 */           if (debug3.is((Block)debug7)) {
/* 324 */             return (BlockState)debug7.getAttachedStem().defaultBlockState().setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)debug2);
/*     */           }
/*     */         } 
/* 327 */         return debug1;
/*     */       }
/*     */     };
/*     */ 
/*     */     
/* 332 */     public static final Direction[] DIRECTIONS = Direction.values();
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     BlockFixers(boolean debug3, Block... debug4) {
/* 339 */       for (Block debug8 : debug4) {
/* 340 */         UpgradeData.MAP.put(debug8, this);
/*     */       }
/* 342 */       if (debug3) {
/* 343 */         UpgradeData.CHUNKY_FIXERS.add(this);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public CompoundTag write() {
/* 349 */     CompoundTag debug1 = new CompoundTag();
/*     */     
/* 351 */     CompoundTag debug2 = new CompoundTag(); int debug3;
/* 352 */     for (debug3 = 0; debug3 < this.index.length; debug3++) {
/* 353 */       String debug4 = String.valueOf(debug3);
/* 354 */       if (this.index[debug3] != null && (this.index[debug3]).length != 0) {
/* 355 */         debug2.putIntArray(debug4, this.index[debug3]);
/*     */       }
/*     */     } 
/* 358 */     if (!debug2.isEmpty()) {
/* 359 */       debug1.put("Indices", (Tag)debug2);
/*     */     }
/*     */     
/* 362 */     debug3 = 0;
/* 363 */     for (Direction8 debug5 : this.sides) {
/* 364 */       debug3 |= 1 << debug5.ordinal();
/*     */     }
/* 366 */     debug1.putByte("Sides", (byte)debug3);
/* 367 */     return debug1;
/*     */   }
/*     */   
/*     */   private UpgradeData() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\UpgradeData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */