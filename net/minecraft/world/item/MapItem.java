/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.LinkedHashMultiset;
/*     */ import com.google.common.collect.Multiset;
/*     */ import com.google.common.collect.Multisets;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.MaterialColor;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MapItem
/*     */   extends ComplexItem
/*     */ {
/*     */   public MapItem(Item.Properties debug1) {
/*  46 */     super(debug1);
/*     */   }
/*     */   
/*     */   public static ItemStack create(Level debug0, int debug1, int debug2, byte debug3, boolean debug4, boolean debug5) {
/*  50 */     ItemStack debug6 = new ItemStack(Items.FILLED_MAP);
/*     */     
/*  52 */     createAndStoreSavedData(debug6, debug0, debug1, debug2, debug3, debug4, debug5, debug0.dimension());
/*     */     
/*  54 */     return debug6;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static MapItemSavedData getSavedData(ItemStack debug0, Level debug1) {
/*  59 */     return debug1.getMapData(makeKey(getMapId(debug0)));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static MapItemSavedData getOrCreateSavedData(ItemStack debug0, Level debug1) {
/*  64 */     MapItemSavedData debug2 = getSavedData(debug0, debug1);
/*     */     
/*  66 */     if (debug2 == null && debug1 instanceof ServerLevel) {
/*  67 */       debug2 = createAndStoreSavedData(debug0, debug1, debug1.getLevelData().getXSpawn(), debug1.getLevelData().getZSpawn(), 3, false, false, debug1.dimension());
/*     */     }
/*     */     
/*  70 */     return debug2;
/*     */   }
/*     */   
/*     */   public static int getMapId(ItemStack debug0) {
/*  74 */     CompoundTag debug1 = debug0.getTag();
/*  75 */     return (debug1 != null && debug1.contains("map", 99)) ? debug1.getInt("map") : 0;
/*     */   }
/*     */   
/*     */   private static MapItemSavedData createAndStoreSavedData(ItemStack debug0, Level debug1, int debug2, int debug3, int debug4, boolean debug5, boolean debug6, ResourceKey<Level> debug7) {
/*  79 */     int debug8 = debug1.getFreeMapId();
/*     */     
/*  81 */     MapItemSavedData debug9 = new MapItemSavedData(makeKey(debug8));
/*     */     
/*  83 */     debug9.setProperties(debug2, debug3, debug4, debug5, debug6, debug7);
/*     */     
/*  85 */     debug1.setMapData(debug9);
/*     */     
/*  87 */     debug0.getOrCreateTag().putInt("map", debug8);
/*  88 */     return debug9;
/*     */   }
/*     */   
/*     */   public static String makeKey(int debug0) {
/*  92 */     return "map_" + debug0;
/*     */   }
/*     */   
/*     */   public void update(Level debug1, Entity debug2, MapItemSavedData debug3) {
/*  96 */     if (debug1.dimension() != debug3.dimension || !(debug2 instanceof Player)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 101 */     int debug4 = 1 << debug3.scale;
/* 102 */     int debug5 = debug3.x;
/* 103 */     int debug6 = debug3.z;
/*     */     
/* 105 */     int debug7 = Mth.floor(debug2.getX() - debug5) / debug4 + 64;
/* 106 */     int debug8 = Mth.floor(debug2.getZ() - debug6) / debug4 + 64;
/* 107 */     int debug9 = 128 / debug4;
/*     */     
/* 109 */     if (debug1.dimensionType().hasCeiling()) {
/* 110 */       debug9 /= 2;
/*     */     }
/*     */     
/* 113 */     MapItemSavedData.HoldingPlayer debug10 = debug3.getHoldingPlayer((Player)debug2);
/* 114 */     debug10.step++;
/*     */     
/* 116 */     boolean debug11 = false;
/* 117 */     for (int debug12 = debug7 - debug9 + 1; debug12 < debug7 + debug9; debug12++) {
/* 118 */       if ((debug12 & 0xF) == (debug10.step & 0xF) || debug11) {
/*     */ 
/*     */ 
/*     */         
/* 122 */         debug11 = false;
/* 123 */         double debug13 = 0.0D;
/* 124 */         for (int debug15 = debug8 - debug9 - 1; debug15 < debug8 + debug9; debug15++) {
/* 125 */           if (debug12 >= 0 && debug15 >= -1 && debug12 < 128 && debug15 < 128) {
/*     */ 
/*     */ 
/*     */             
/* 129 */             int debug16 = debug12 - debug7;
/* 130 */             int debug17 = debug15 - debug8;
/*     */             
/* 132 */             boolean debug18 = (debug16 * debug16 + debug17 * debug17 > (debug9 - 2) * (debug9 - 2));
/*     */             
/* 134 */             int debug19 = (debug5 / debug4 + debug12 - 64) * debug4;
/* 135 */             int debug20 = (debug6 / debug4 + debug15 - 64) * debug4;
/*     */             
/* 137 */             LinkedHashMultiset linkedHashMultiset = LinkedHashMultiset.create();
/*     */             
/* 139 */             LevelChunk debug22 = debug1.getChunkAt(new BlockPos(debug19, 0, debug20));
/* 140 */             if (!debug22.isEmpty()) {
/*     */ 
/*     */               
/* 143 */               ChunkPos debug23 = debug22.getPos();
/* 144 */               int debug24 = debug19 & 0xF;
/* 145 */               int debug25 = debug20 & 0xF;
/* 146 */               int debug26 = 0;
/*     */               
/* 148 */               double debug27 = 0.0D;
/* 149 */               if (debug1.dimensionType().hasCeiling()) {
/* 150 */                 int i = debug19 + debug20 * 231871;
/* 151 */                 i = i * i * 31287121 + i * 11;
/*     */                 
/* 153 */                 if ((i >> 20 & 0x1) == 0) {
/* 154 */                   linkedHashMultiset.add(Blocks.DIRT.defaultBlockState().getMapColor((BlockGetter)debug1, BlockPos.ZERO), 10);
/*     */                 } else {
/* 156 */                   linkedHashMultiset.add(Blocks.STONE.defaultBlockState().getMapColor((BlockGetter)debug1, BlockPos.ZERO), 100);
/*     */                 } 
/*     */                 
/* 159 */                 debug27 = 100.0D;
/*     */               } else {
/* 161 */                 BlockPos.MutableBlockPos mutableBlockPos1 = new BlockPos.MutableBlockPos();
/* 162 */                 BlockPos.MutableBlockPos debug30 = new BlockPos.MutableBlockPos();
/* 163 */                 for (int i = 0; i < debug4; i++) {
/* 164 */                   for (int j = 0; j < debug4; j++) {
/* 165 */                     BlockState debug34; int debug33 = debug22.getHeight(Heightmap.Types.WORLD_SURFACE, i + debug24, j + debug25) + 1;
/*     */                     
/* 167 */                     if (debug33 > 1) {
/*     */                       do {
/* 169 */                         debug33--;
/* 170 */                         mutableBlockPos1.set(debug23.getMinBlockX() + i + debug24, debug33, debug23.getMinBlockZ() + j + debug25);
/* 171 */                         debug34 = debug22.getBlockState((BlockPos)mutableBlockPos1);
/* 172 */                       } while (debug34.getMapColor((BlockGetter)debug1, (BlockPos)mutableBlockPos1) == MaterialColor.NONE && debug33 > 0);
/*     */                       
/* 174 */                       if (debug33 > 0 && !debug34.getFluidState().isEmpty()) {
/* 175 */                         BlockState debug36; int debug35 = debug33 - 1;
/*     */                         
/* 177 */                         debug30.set((Vec3i)mutableBlockPos1);
/*     */                         do {
/* 179 */                           debug30.setY(debug35--);
/* 180 */                           debug36 = debug22.getBlockState((BlockPos)debug30);
/* 181 */                           debug26++;
/* 182 */                         } while (debug35 > 0 && !debug36.getFluidState().isEmpty());
/*     */                         
/* 184 */                         debug34 = getCorrectStateForFluidBlock(debug1, debug34, (BlockPos)mutableBlockPos1);
/*     */                       } 
/*     */                     } else {
/* 187 */                       debug34 = Blocks.BEDROCK.defaultBlockState();
/*     */                     } 
/*     */                     
/* 190 */                     debug3.checkBanners((BlockGetter)debug1, debug23.getMinBlockX() + i + debug24, debug23.getMinBlockZ() + j + debug25);
/*     */                     
/* 192 */                     debug27 += debug33 / (debug4 * debug4);
/*     */                     
/* 194 */                     linkedHashMultiset.add(debug34.getMapColor((BlockGetter)debug1, (BlockPos)mutableBlockPos1));
/*     */                   } 
/*     */                 } 
/*     */               } 
/* 198 */               debug26 /= debug4 * debug4;
/*     */               
/* 200 */               double debug29 = (debug27 - debug13) * 4.0D / (debug4 + 4) + ((debug12 + debug15 & 0x1) - 0.5D) * 0.4D;
/* 201 */               int debug31 = 1;
/* 202 */               if (debug29 > 0.6D) {
/* 203 */                 debug31 = 2;
/*     */               }
/* 205 */               if (debug29 < -0.6D) {
/* 206 */                 debug31 = 0;
/*     */               }
/*     */               
/* 209 */               MaterialColor debug32 = (MaterialColor)Iterables.getFirst((Iterable)Multisets.copyHighestCountFirst((Multiset)linkedHashMultiset), MaterialColor.NONE);
/*     */               
/* 211 */               if (debug32 == MaterialColor.WATER) {
/* 212 */                 debug29 = debug26 * 0.1D + (debug12 + debug15 & 0x1) * 0.2D;
/* 213 */                 debug31 = 1;
/* 214 */                 if (debug29 < 0.5D) {
/* 215 */                   debug31 = 2;
/*     */                 }
/* 217 */                 if (debug29 > 0.9D) {
/* 218 */                   debug31 = 0;
/*     */                 }
/*     */               } 
/*     */               
/* 222 */               debug13 = debug27;
/*     */               
/* 224 */               if (debug15 >= 0)
/*     */               {
/*     */                 
/* 227 */                 if (debug16 * debug16 + debug17 * debug17 < debug9 * debug9)
/*     */                 {
/*     */                   
/* 230 */                   if (!debug18 || (debug12 + debug15 & 0x1) != 0) {
/*     */ 
/*     */                     
/* 233 */                     byte debug33 = debug3.colors[debug12 + debug15 * 128];
/* 234 */                     byte debug34 = (byte)(debug32.id * 4 + debug31);
/* 235 */                     if (debug33 != debug34) {
/* 236 */                       debug3.colors[debug12 + debug15 * 128] = debug34;
/* 237 */                       debug3.setDirty(debug12, debug15);
/* 238 */                       debug11 = true;
/*     */                     } 
/*     */                   }  }  } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 245 */     }  } private BlockState getCorrectStateForFluidBlock(Level debug1, BlockState debug2, BlockPos debug3) { FluidState debug4 = debug2.getFluidState();
/* 246 */     if (!debug4.isEmpty() && !debug2.isFaceSturdy((BlockGetter)debug1, debug3, Direction.UP)) {
/* 247 */       return debug4.createLegacyBlock();
/*     */     }
/*     */     
/* 250 */     return debug2; }
/*     */ 
/*     */   
/*     */   private static boolean isLand(Biome[] debug0, int debug1, int debug2, int debug3) {
/* 254 */     return (debug0[debug2 * debug1 + debug3 * debug1 * 128 * debug1].getDepth() >= 0.0F);
/*     */   }
/*     */   
/*     */   public static void renderBiomePreviewMap(ServerLevel debug0, ItemStack debug1) {
/* 258 */     MapItemSavedData debug2 = getOrCreateSavedData(debug1, (Level)debug0);
/* 259 */     if (debug2 == null) {
/*     */       return;
/*     */     }
/* 262 */     if (debug0.dimension() != debug2.dimension) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 267 */     int debug3 = 1 << debug2.scale;
/* 268 */     int debug4 = debug2.x;
/* 269 */     int debug5 = debug2.z;
/*     */     
/* 271 */     Biome[] debug6 = new Biome[128 * debug3 * 128 * debug3];
/*     */     int debug7;
/* 273 */     for (debug7 = 0; debug7 < 128 * debug3; debug7++) {
/* 274 */       for (int debug8 = 0; debug8 < 128 * debug3; debug8++) {
/* 275 */         debug6[debug7 * 128 * debug3 + debug8] = debug0.getBiome(new BlockPos((debug4 / debug3 - 64) * debug3 + debug8, 0, (debug5 / debug3 - 64) * debug3 + debug7));
/*     */       }
/*     */     } 
/* 278 */     for (debug7 = 0; debug7 < 128; debug7++) {
/* 279 */       for (int debug8 = 0; debug8 < 128; debug8++) {
/*     */         
/* 281 */         if (debug7 > 0 && debug8 > 0 && debug7 < 127 && debug8 < 127) {
/* 282 */           Biome debug9 = debug6[debug7 * debug3 + debug8 * debug3 * 128 * debug3];
/*     */           
/* 284 */           int debug10 = 8;
/* 285 */           if (isLand(debug6, debug3, debug7 - 1, debug8 - 1)) {
/* 286 */             debug10--;
/*     */           }
/* 288 */           if (isLand(debug6, debug3, debug7 - 1, debug8 + 1)) {
/* 289 */             debug10--;
/*     */           }
/* 291 */           if (isLand(debug6, debug3, debug7 - 1, debug8)) {
/* 292 */             debug10--;
/*     */           }
/* 294 */           if (isLand(debug6, debug3, debug7 + 1, debug8 - 1)) {
/* 295 */             debug10--;
/*     */           }
/* 297 */           if (isLand(debug6, debug3, debug7 + 1, debug8 + 1)) {
/* 298 */             debug10--;
/*     */           }
/* 300 */           if (isLand(debug6, debug3, debug7 + 1, debug8)) {
/* 301 */             debug10--;
/*     */           }
/* 303 */           if (isLand(debug6, debug3, debug7, debug8 - 1)) {
/* 304 */             debug10--;
/*     */           }
/* 306 */           if (isLand(debug6, debug3, debug7, debug8 + 1)) {
/* 307 */             debug10--;
/*     */           }
/*     */           
/* 310 */           int debug11 = 3;
/* 311 */           MaterialColor debug12 = MaterialColor.NONE;
/* 312 */           if (debug9.getDepth() < 0.0F) {
/* 313 */             debug12 = MaterialColor.COLOR_ORANGE;
/* 314 */             if (debug10 > 7 && debug8 % 2 == 0) {
/* 315 */               debug11 = (debug7 + (int)(Mth.sin(debug8 + 0.0F) * 7.0F)) / 8 % 5;
/* 316 */               if (debug11 == 3) {
/* 317 */                 debug11 = 1;
/* 318 */               } else if (debug11 == 4) {
/* 319 */                 debug11 = 0;
/*     */               } 
/* 321 */             } else if (debug10 > 7) {
/* 322 */               debug12 = MaterialColor.NONE;
/* 323 */             } else if (debug10 > 5) {
/* 324 */               debug11 = 1;
/* 325 */             } else if (debug10 > 3) {
/* 326 */               debug11 = 0;
/* 327 */             } else if (debug10 > 1) {
/* 328 */               debug11 = 0;
/*     */             } 
/* 330 */           } else if (debug10 > 0) {
/* 331 */             debug12 = MaterialColor.COLOR_BROWN;
/* 332 */             if (debug10 > 3) {
/* 333 */               debug11 = 1;
/*     */             } else {
/* 335 */               debug11 = 3;
/*     */             } 
/*     */           } 
/*     */           
/* 339 */           if (debug12 != MaterialColor.NONE) {
/* 340 */             debug2.colors[debug7 + debug8 * 128] = (byte)(debug12.id * 4 + debug11);
/* 341 */             debug2.setDirty(debug7, debug8);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void inventoryTick(ItemStack debug1, Level debug2, Entity debug3, int debug4, boolean debug5) {
/* 350 */     if (debug2.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/* 354 */     MapItemSavedData debug6 = getOrCreateSavedData(debug1, debug2);
/* 355 */     if (debug6 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 359 */     if (debug3 instanceof Player) {
/* 360 */       Player debug7 = (Player)debug3;
/* 361 */       debug6.tickCarriedBy(debug7, debug1);
/*     */     } 
/*     */     
/* 364 */     if (!debug6.locked && (debug5 || (debug3 instanceof Player && ((Player)debug3).getOffhandItem() == debug1))) {
/* 365 */       update(debug2, debug3, debug6);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Packet<?> getUpdatePacket(ItemStack debug1, Level debug2, Player debug3) {
/* 372 */     return getOrCreateSavedData(debug1, debug2).getUpdatePacket(debug1, (BlockGetter)debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCraftedBy(ItemStack debug1, Level debug2, Player debug3) {
/* 377 */     CompoundTag debug4 = debug1.getTag();
/* 378 */     if (debug4 != null && debug4.contains("map_scale_direction", 99)) {
/* 379 */       scaleMap(debug1, debug2, debug4.getInt("map_scale_direction"));
/* 380 */       debug4.remove("map_scale_direction");
/* 381 */     } else if (debug4 != null && debug4.contains("map_to_lock", 1) && debug4.getBoolean("map_to_lock")) {
/* 382 */       lockMap(debug2, debug1);
/* 383 */       debug4.remove("map_to_lock");
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static void scaleMap(ItemStack debug0, Level debug1, int debug2) {
/* 388 */     MapItemSavedData debug3 = getOrCreateSavedData(debug0, debug1);
/*     */     
/* 390 */     if (debug3 != null) {
/* 391 */       createAndStoreSavedData(debug0, debug1, debug3.x, debug3.z, Mth.clamp(debug3.scale + debug2, 0, 4), debug3.trackingPosition, debug3.unlimitedTracking, debug3.dimension);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void lockMap(Level debug0, ItemStack debug1) {
/* 396 */     MapItemSavedData debug2 = getOrCreateSavedData(debug1, debug0);
/* 397 */     if (debug2 != null) {
/* 398 */       MapItemSavedData debug3 = createAndStoreSavedData(debug1, debug0, 0, 0, debug2.scale, debug2.trackingPosition, debug2.unlimitedTracking, debug2.dimension);
/* 399 */       debug3.lockData(debug2);
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext debug1) {
/* 433 */     BlockState debug2 = debug1.getLevel().getBlockState(debug1.getClickedPos());
/* 434 */     if (debug2.is((Tag)BlockTags.BANNERS)) {
/* 435 */       if (!(debug1.getLevel()).isClientSide) {
/* 436 */         MapItemSavedData debug3 = getOrCreateSavedData(debug1.getItemInHand(), debug1.getLevel());
/* 437 */         debug3.toggleBanner((LevelAccessor)debug1.getLevel(), debug1.getClickedPos());
/*     */       } 
/* 439 */       return InteractionResult.sidedSuccess((debug1.getLevel()).isClientSide);
/*     */     } 
/* 441 */     return super.useOn(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\MapItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */