/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntListIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
/*     */ import net.minecraft.util.datafix.PackedBitStorage;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChunkPalettedStorageFix
/*     */   extends DataFix
/*     */ {
/*     */   public ChunkPalettedStorageFix(Schema debug1, boolean debug2) {
/*  43 */     super(debug1, debug2);
/*     */   }
/*     */   
/*  46 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  48 */   private static final BitSet VIRTUAL = new BitSet(256);
/*  49 */   private static final BitSet FIX = new BitSet(256);
/*  50 */   private static final Dynamic<?> PUMPKIN = BlockStateData.parse("{Name:'minecraft:pumpkin'}");
/*  51 */   private static final Dynamic<?> SNOWY_PODZOL = BlockStateData.parse("{Name:'minecraft:podzol',Properties:{snowy:'true'}}");
/*  52 */   private static final Dynamic<?> SNOWY_GRASS = BlockStateData.parse("{Name:'minecraft:grass_block',Properties:{snowy:'true'}}");
/*  53 */   private static final Dynamic<?> SNOWY_MYCELIUM = BlockStateData.parse("{Name:'minecraft:mycelium',Properties:{snowy:'true'}}");
/*  54 */   private static final Dynamic<?> UPPER_SUNFLOWER = BlockStateData.parse("{Name:'minecraft:sunflower',Properties:{half:'upper'}}");
/*  55 */   private static final Dynamic<?> UPPER_LILAC = BlockStateData.parse("{Name:'minecraft:lilac',Properties:{half:'upper'}}");
/*  56 */   private static final Dynamic<?> UPPER_TALL_GRASS = BlockStateData.parse("{Name:'minecraft:tall_grass',Properties:{half:'upper'}}");
/*  57 */   private static final Dynamic<?> UPPER_LARGE_FERN = BlockStateData.parse("{Name:'minecraft:large_fern',Properties:{half:'upper'}}");
/*  58 */   private static final Dynamic<?> UPPER_ROSE_BUSH = BlockStateData.parse("{Name:'minecraft:rose_bush',Properties:{half:'upper'}}");
/*  59 */   private static final Dynamic<?> UPPER_PEONY = BlockStateData.parse("{Name:'minecraft:peony',Properties:{half:'upper'}}");
/*     */   private static final Map<String, Dynamic<?>> FLOWER_POT_MAP; private static final Map<String, Dynamic<?>> SKULL_MAP; private static final Map<String, Dynamic<?>> DOOR_MAP; private static final Map<String, Dynamic<?>> NOTE_BLOCK_MAP; private static final Int2ObjectMap<String> DYE_COLOR_MAP; private static final Map<String, Dynamic<?>> BED_BLOCK_MAP; private static final Map<String, Dynamic<?>> BANNER_BLOCK_MAP; private static void mapSkull(Map<String, Dynamic<?>> debug0, int debug1, String debug2, String debug3) { debug0.put(debug1 + "north", BlockStateData.parse("{Name:'minecraft:" + debug2 + "_wall_" + debug3 + "',Properties:{facing:'north'}}")); debug0.put(debug1 + "east", BlockStateData.parse("{Name:'minecraft:" + debug2 + "_wall_" + debug3 + "',Properties:{facing:'east'}}")); debug0.put(debug1 + "south", BlockStateData.parse("{Name:'minecraft:" + debug2 + "_wall_" + debug3 + "',Properties:{facing:'south'}}")); debug0.put(debug1 + "west", BlockStateData.parse("{Name:'minecraft:" + debug2 + "_wall_" + debug3 + "',Properties:{facing:'west'}}")); for (int debug4 = 0; debug4 < 16; debug4++)
/*  61 */       debug0.put(debug1 + "" + debug4, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_" + debug3 + "',Properties:{rotation:'" + debug4 + "'}}"));  } static { FLOWER_POT_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), debug0 -> {
/*     */           debug0.put("minecraft:air0", BlockStateData.parse("{Name:'minecraft:flower_pot'}"));
/*     */           
/*     */           debug0.put("minecraft:red_flower0", BlockStateData.parse("{Name:'minecraft:potted_poppy'}"));
/*     */           debug0.put("minecraft:red_flower1", BlockStateData.parse("{Name:'minecraft:potted_blue_orchid'}"));
/*     */           debug0.put("minecraft:red_flower2", BlockStateData.parse("{Name:'minecraft:potted_allium'}"));
/*     */           debug0.put("minecraft:red_flower3", BlockStateData.parse("{Name:'minecraft:potted_azure_bluet'}"));
/*     */           debug0.put("minecraft:red_flower4", BlockStateData.parse("{Name:'minecraft:potted_red_tulip'}"));
/*     */           debug0.put("minecraft:red_flower5", BlockStateData.parse("{Name:'minecraft:potted_orange_tulip'}"));
/*     */           debug0.put("minecraft:red_flower6", BlockStateData.parse("{Name:'minecraft:potted_white_tulip'}"));
/*     */           debug0.put("minecraft:red_flower7", BlockStateData.parse("{Name:'minecraft:potted_pink_tulip'}"));
/*     */           debug0.put("minecraft:red_flower8", BlockStateData.parse("{Name:'minecraft:potted_oxeye_daisy'}"));
/*     */           debug0.put("minecraft:yellow_flower0", BlockStateData.parse("{Name:'minecraft:potted_dandelion'}"));
/*     */           debug0.put("minecraft:sapling0", BlockStateData.parse("{Name:'minecraft:potted_oak_sapling'}"));
/*     */           debug0.put("minecraft:sapling1", BlockStateData.parse("{Name:'minecraft:potted_spruce_sapling'}"));
/*     */           debug0.put("minecraft:sapling2", BlockStateData.parse("{Name:'minecraft:potted_birch_sapling'}"));
/*     */           debug0.put("minecraft:sapling3", BlockStateData.parse("{Name:'minecraft:potted_jungle_sapling'}"));
/*     */           debug0.put("minecraft:sapling4", BlockStateData.parse("{Name:'minecraft:potted_acacia_sapling'}"));
/*     */           debug0.put("minecraft:sapling5", BlockStateData.parse("{Name:'minecraft:potted_dark_oak_sapling'}"));
/*     */           debug0.put("minecraft:red_mushroom0", BlockStateData.parse("{Name:'minecraft:potted_red_mushroom'}"));
/*     */           debug0.put("minecraft:brown_mushroom0", BlockStateData.parse("{Name:'minecraft:potted_brown_mushroom'}"));
/*     */           debug0.put("minecraft:deadbush0", BlockStateData.parse("{Name:'minecraft:potted_dead_bush'}"));
/*     */           debug0.put("minecraft:tallgrass2", BlockStateData.parse("{Name:'minecraft:potted_fern'}"));
/*     */           debug0.put("minecraft:cactus0", BlockStateData.getTag(2240));
/*     */         });
/*  86 */     SKULL_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), debug0 -> {
/*     */           mapSkull(debug0, 0, "skeleton", "skull");
/*     */ 
/*     */           
/*     */           mapSkull(debug0, 1, "wither_skeleton", "skull");
/*     */ 
/*     */           
/*     */           mapSkull(debug0, 2, "zombie", "head");
/*     */ 
/*     */           
/*     */           mapSkull(debug0, 3, "player", "head");
/*     */ 
/*     */           
/*     */           mapSkull(debug0, 4, "creeper", "head");
/*     */ 
/*     */           
/*     */           mapSkull(debug0, 5, "dragon", "head");
/*     */         });
/*     */     
/* 105 */     DOOR_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), debug0 -> {
/*     */           mapDoor(debug0, "oak_door", 1024);
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
/*     */           mapDoor(debug0, "iron_door", 1136);
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
/*     */           mapDoor(debug0, "spruce_door", 3088);
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
/*     */           mapDoor(debug0, "birch_door", 3104);
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
/*     */           mapDoor(debug0, "jungle_door", 3120);
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
/*     */           mapDoor(debug0, "acacia_door", 3136);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           mapDoor(debug0, "dark_oak_door", 3152);
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     NOTE_BLOCK_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), debug0 -> {
/*     */           for (int debug1 = 0; debug1 < 26; debug1++) {
/*     */             debug0.put("true" + debug1, BlockStateData.parse("{Name:'minecraft:note_block',Properties:{powered:'true',note:'" + debug1 + "'}}"));
/*     */             
/*     */             debug0.put("false" + debug1, BlockStateData.parse("{Name:'minecraft:note_block',Properties:{powered:'false',note:'" + debug1 + "'}}"));
/*     */           } 
/*     */         });
/* 189 */     DYE_COLOR_MAP = (Int2ObjectMap<String>)DataFixUtils.make(new Int2ObjectOpenHashMap(), debug0 -> {
/*     */           debug0.put(0, "white");
/*     */           
/*     */           debug0.put(1, "orange");
/*     */           debug0.put(2, "magenta");
/*     */           debug0.put(3, "light_blue");
/*     */           debug0.put(4, "yellow");
/*     */           debug0.put(5, "lime");
/*     */           debug0.put(6, "pink");
/*     */           debug0.put(7, "gray");
/*     */           debug0.put(8, "light_gray");
/*     */           debug0.put(9, "cyan");
/*     */           debug0.put(10, "purple");
/*     */           debug0.put(11, "blue");
/*     */           debug0.put(12, "brown");
/*     */           debug0.put(13, "green");
/*     */           debug0.put(14, "red");
/*     */           debug0.put(15, "black");
/*     */         });
/* 208 */     BED_BLOCK_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), debug0 -> {
/*     */           ObjectIterator<Int2ObjectMap.Entry<String>> objectIterator = DYE_COLOR_MAP.int2ObjectEntrySet().iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           while (objectIterator.hasNext()) {
/*     */             Int2ObjectMap.Entry<String> debug2 = objectIterator.next();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             if (!Objects.equals(debug2.getValue(), "red")) {
/*     */               addBeds(debug0, debug2.getIntKey(), (String)debug2.getValue());
/*     */             }
/*     */           } 
/*     */         });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     BANNER_BLOCK_MAP = (Map<String, Dynamic<?>>)DataFixUtils.make(Maps.newHashMap(), debug0 -> {
/*     */           ObjectIterator<Int2ObjectMap.Entry<String>> objectIterator = DYE_COLOR_MAP.int2ObjectEntrySet().iterator();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           while (objectIterator.hasNext()) {
/*     */             Int2ObjectMap.Entry<String> debug2 = objectIterator.next();
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             if (!Objects.equals(debug2.getValue(), "white")) {
/*     */               addBanners(debug0, 15 - debug2.getIntKey(), (String)debug2.getValue());
/*     */             }
/*     */           } 
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 251 */     FIX.set(2);
/* 252 */     FIX.set(3);
/* 253 */     FIX.set(110);
/*     */     
/* 255 */     FIX.set(140);
/* 256 */     FIX.set(144);
/*     */     
/* 258 */     FIX.set(25);
/*     */     
/* 260 */     FIX.set(86);
/*     */ 
/*     */     
/* 263 */     FIX.set(26);
/* 264 */     FIX.set(176);
/* 265 */     FIX.set(177);
/*     */     
/* 267 */     FIX.set(175);
/*     */     
/* 269 */     FIX.set(64);
/* 270 */     FIX.set(71);
/* 271 */     FIX.set(193);
/* 272 */     FIX.set(194);
/* 273 */     FIX.set(195);
/* 274 */     FIX.set(196);
/* 275 */     FIX.set(197);
/*     */     
/* 277 */     VIRTUAL.set(54);
/* 278 */     VIRTUAL.set(146);
/*     */     
/* 280 */     VIRTUAL.set(25);
/*     */     
/* 282 */     VIRTUAL.set(26);
/*     */     
/* 284 */     VIRTUAL.set(51);
/*     */     
/* 286 */     VIRTUAL.set(53);
/* 287 */     VIRTUAL.set(67);
/* 288 */     VIRTUAL.set(108);
/* 289 */     VIRTUAL.set(109);
/* 290 */     VIRTUAL.set(114);
/* 291 */     VIRTUAL.set(128);
/* 292 */     VIRTUAL.set(134);
/* 293 */     VIRTUAL.set(135);
/* 294 */     VIRTUAL.set(136);
/* 295 */     VIRTUAL.set(156);
/* 296 */     VIRTUAL.set(163);
/* 297 */     VIRTUAL.set(164);
/* 298 */     VIRTUAL.set(180);
/* 299 */     VIRTUAL.set(203);
/*     */     
/* 301 */     VIRTUAL.set(55);
/*     */     
/* 303 */     VIRTUAL.set(85);
/* 304 */     VIRTUAL.set(113);
/* 305 */     VIRTUAL.set(188);
/* 306 */     VIRTUAL.set(189);
/* 307 */     VIRTUAL.set(190);
/* 308 */     VIRTUAL.set(191);
/* 309 */     VIRTUAL.set(192);
/*     */     
/* 311 */     VIRTUAL.set(93);
/* 312 */     VIRTUAL.set(94);
/*     */     
/* 314 */     VIRTUAL.set(101);
/* 315 */     VIRTUAL.set(102);
/* 316 */     VIRTUAL.set(160);
/*     */     
/* 318 */     VIRTUAL.set(106);
/*     */ 
/*     */     
/* 321 */     VIRTUAL.set(107);
/* 322 */     VIRTUAL.set(183);
/* 323 */     VIRTUAL.set(184);
/* 324 */     VIRTUAL.set(185);
/* 325 */     VIRTUAL.set(186);
/* 326 */     VIRTUAL.set(187);
/*     */     
/* 328 */     VIRTUAL.set(132);
/* 329 */     VIRTUAL.set(139);
/*     */     
/* 331 */     VIRTUAL.set(199); }
/*     */   private static void mapDoor(Map<String, Dynamic<?>> debug0, String debug1, int debug2) { debug0.put("minecraft:" + debug1 + "eastlowerleftfalsefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'east',half:'lower',hinge:'left',open:'false',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "eastlowerleftfalsetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'east',half:'lower',hinge:'left',open:'false',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "eastlowerlefttruefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'east',half:'lower',hinge:'left',open:'true',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "eastlowerlefttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'east',half:'lower',hinge:'left',open:'true',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "eastlowerrightfalsefalse", BlockStateData.getTag(debug2)); debug0.put("minecraft:" + debug1 + "eastlowerrightfalsetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'east',half:'lower',hinge:'right',open:'false',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "eastlowerrighttruefalse", BlockStateData.getTag(debug2 + 4)); debug0.put("minecraft:" + debug1 + "eastlowerrighttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'east',half:'lower',hinge:'right',open:'true',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "eastupperleftfalsefalse", BlockStateData.getTag(debug2 + 8)); debug0.put("minecraft:" + debug1 + "eastupperleftfalsetrue", BlockStateData.getTag(debug2 + 10)); debug0.put("minecraft:" + debug1 + "eastupperlefttruefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'east',half:'upper',hinge:'left',open:'true',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "eastupperlefttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'east',half:'upper',hinge:'left',open:'true',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "eastupperrightfalsefalse", BlockStateData.getTag(debug2 + 9)); debug0.put("minecraft:" + debug1 + "eastupperrightfalsetrue", BlockStateData.getTag(debug2 + 11)); debug0.put("minecraft:" + debug1 + "eastupperrighttruefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'east',half:'upper',hinge:'right',open:'true',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "eastupperrighttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'east',half:'upper',hinge:'right',open:'true',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "northlowerleftfalsefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'north',half:'lower',hinge:'left',open:'false',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "northlowerleftfalsetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'north',half:'lower',hinge:'left',open:'false',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "northlowerlefttruefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'north',half:'lower',hinge:'left',open:'true',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "northlowerlefttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'north',half:'lower',hinge:'left',open:'true',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "northlowerrightfalsefalse", BlockStateData.getTag(debug2 + 3)); debug0.put("minecraft:" + debug1 + "northlowerrightfalsetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'north',half:'lower',hinge:'right',open:'false',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "northlowerrighttruefalse", BlockStateData.getTag(debug2 + 7)); debug0.put("minecraft:" + debug1 + "northlowerrighttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'north',half:'lower',hinge:'right',open:'true',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "northupperleftfalsefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'north',half:'upper',hinge:'left',open:'false',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "northupperleftfalsetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'north',half:'upper',hinge:'left',open:'false',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "northupperlefttruefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'north',half:'upper',hinge:'left',open:'true',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "northupperlefttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'north',half:'upper',hinge:'left',open:'true',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "northupperrightfalsefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'north',half:'upper',hinge:'right',open:'false',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "northupperrightfalsetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'north',half:'upper',hinge:'right',open:'false',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "northupperrighttruefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'north',half:'upper',hinge:'right',open:'true',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "northupperrighttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'north',half:'upper',hinge:'right',open:'true',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "southlowerleftfalsefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'south',half:'lower',hinge:'left',open:'false',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "southlowerleftfalsetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'south',half:'lower',hinge:'left',open:'false',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "southlowerlefttruefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'south',half:'lower',hinge:'left',open:'true',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "southlowerlefttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'south',half:'lower',hinge:'left',open:'true',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "southlowerrightfalsefalse", BlockStateData.getTag(debug2 + 1)); debug0.put("minecraft:" + debug1 + "southlowerrightfalsetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'south',half:'lower',hinge:'right',open:'false',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "southlowerrighttruefalse", BlockStateData.getTag(debug2 + 5)); debug0.put("minecraft:" + debug1 + "southlowerrighttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'south',half:'lower',hinge:'right',open:'true',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "southupperleftfalsefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'south',half:'upper',hinge:'left',open:'false',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "southupperleftfalsetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'south',half:'upper',hinge:'left',open:'false',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "southupperlefttruefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'south',half:'upper',hinge:'left',open:'true',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "southupperlefttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'south',half:'upper',hinge:'left',open:'true',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "southupperrightfalsefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'south',half:'upper',hinge:'right',open:'false',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "southupperrightfalsetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'south',half:'upper',hinge:'right',open:'false',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "southupperrighttruefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'south',half:'upper',hinge:'right',open:'true',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "southupperrighttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'south',half:'upper',hinge:'right',open:'true',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "westlowerleftfalsefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'west',half:'lower',hinge:'left',open:'false',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "westlowerleftfalsetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'west',half:'lower',hinge:'left',open:'false',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "westlowerlefttruefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'west',half:'lower',hinge:'left',open:'true',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "westlowerlefttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'west',half:'lower',hinge:'left',open:'true',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "westlowerrightfalsefalse", BlockStateData.getTag(debug2 + 2)); debug0.put("minecraft:" + debug1 + "westlowerrightfalsetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'west',half:'lower',hinge:'right',open:'false',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "westlowerrighttruefalse", BlockStateData.getTag(debug2 + 6)); debug0.put("minecraft:" + debug1 + "westlowerrighttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'west',half:'lower',hinge:'right',open:'true',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "westupperleftfalsefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'west',half:'upper',hinge:'left',open:'false',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "westupperleftfalsetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'west',half:'upper',hinge:'left',open:'false',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "westupperlefttruefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'west',half:'upper',hinge:'left',open:'true',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "westupperlefttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'west',half:'upper',hinge:'left',open:'true',powered:'true'}}")); debug0.put("minecraft:" + debug1 + "westupperrightfalsefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'west',half:'upper',hinge:'right',open:'false',powered:'false'}}")); debug0.put("minecraft:" + debug1 + "westupperrightfalsetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'west',half:'upper',hinge:'right',open:'false',powered:'true'}}"));
/*     */     debug0.put("minecraft:" + debug1 + "westupperrighttruefalse", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'west',half:'upper',hinge:'right',open:'true',powered:'false'}}"));
/* 334 */     debug0.put("minecraft:" + debug1 + "westupperrighttruetrue", BlockStateData.parse("{Name:'minecraft:" + debug1 + "',Properties:{facing:'west',half:'upper',hinge:'right',open:'true',powered:'true'}}")); } private static final Dynamic<?> AIR = BlockStateData.getTag(0); private static void addBeds(Map<String, Dynamic<?>> debug0, int debug1, String debug2) { debug0.put("southfalsefoot" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_bed',Properties:{facing:'south',occupied:'false',part:'foot'}}")); debug0.put("westfalsefoot" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_bed',Properties:{facing:'west',occupied:'false',part:'foot'}}")); debug0.put("northfalsefoot" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_bed',Properties:{facing:'north',occupied:'false',part:'foot'}}")); debug0.put("eastfalsefoot" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_bed',Properties:{facing:'east',occupied:'false',part:'foot'}}")); debug0.put("southfalsehead" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_bed',Properties:{facing:'south',occupied:'false',part:'head'}}")); debug0.put("westfalsehead" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_bed',Properties:{facing:'west',occupied:'false',part:'head'}}")); debug0.put("northfalsehead" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_bed',Properties:{facing:'north',occupied:'false',part:'head'}}")); debug0.put("eastfalsehead" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_bed',Properties:{facing:'east',occupied:'false',part:'head'}}")); debug0.put("southtruehead" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_bed',Properties:{facing:'south',occupied:'true',part:'head'}}")); debug0.put("westtruehead" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_bed',Properties:{facing:'west',occupied:'true',part:'head'}}")); debug0.put("northtruehead" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_bed',Properties:{facing:'north',occupied:'true',part:'head'}}")); debug0.put("easttruehead" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_bed',Properties:{facing:'east',occupied:'true',part:'head'}}")); }
/*     */   private static void addBanners(Map<String, Dynamic<?>> debug0, int debug1, String debug2) { for (int debug3 = 0; debug3 < 16; debug3++)
/*     */       debug0.put("" + debug3 + "_" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_banner',Properties:{rotation:'" + debug3 + "'}}"));  debug0.put("north_" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_wall_banner',Properties:{facing:'north'}}")); debug0.put("south_" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_wall_banner',Properties:{facing:'south'}}")); debug0.put("west_" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_wall_banner',Properties:{facing:'west'}}"));
/*     */     debug0.put("east_" + debug1, BlockStateData.parse("{Name:'minecraft:" + debug2 + "_wall_banner',Properties:{facing:'east'}}")); }
/* 338 */   public static String getName(Dynamic<?> debug0) { return debug0.get("Name").asString(""); }
/*     */ 
/*     */   
/*     */   public static String getProperty(Dynamic<?> debug0, String debug1) {
/* 342 */     return debug0.get("Properties").get(debug1).asString("");
/*     */   }
/*     */   
/*     */   public static int idFor(CrudeIncrementalIntIdentityHashBiMap<Dynamic<?>> debug0, Dynamic<?> debug1) {
/* 346 */     int debug2 = debug0.getId(debug1);
/* 347 */     if (debug2 == -1) {
/* 348 */       debug2 = debug0.add(debug1);
/*     */     }
/* 350 */     return debug2;
/*     */   }
/*     */   
/*     */   private Dynamic<?> fix(Dynamic<?> debug1) {
/* 354 */     Optional<? extends Dynamic<?>> debug2 = debug1.get("Level").result();
/* 355 */     if (debug2.isPresent() && ((Dynamic)debug2.get()).get("Sections").asStreamOpt().result().isPresent()) {
/* 356 */       return debug1.set("Level", (new UpgradeChunk(debug2.get())).write());
/*     */     }
/* 358 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 363 */     Type<?> debug1 = getInputSchema().getType(References.CHUNK);
/* 364 */     Type<?> debug2 = getOutputSchema().getType(References.CHUNK);
/* 365 */     return writeFixAndRead("ChunkPalettedStorageFix", debug1, debug2, this::fix);
/*     */   }
/*     */   
/*     */   static class Section {
/* 369 */     private final CrudeIncrementalIntIdentityHashBiMap<Dynamic<?>> palette = new CrudeIncrementalIntIdentityHashBiMap(32);
/*     */     
/*     */     private final List<Dynamic<?>> listTag;
/*     */     private final Dynamic<?> section;
/*     */     private final boolean hasData;
/* 374 */     private final Int2ObjectMap<IntList> toFix = (Int2ObjectMap<IntList>)new Int2ObjectLinkedOpenHashMap();
/*     */     
/* 376 */     private final IntList update = (IntList)new IntArrayList();
/*     */     public final int y;
/* 378 */     private final Set<Dynamic<?>> seen = Sets.newIdentityHashSet();
/* 379 */     private final int[] buffer = new int[4096];
/*     */     
/*     */     public Section(Dynamic<?> debug1) {
/* 382 */       this.listTag = Lists.newArrayList();
/* 383 */       this.section = debug1;
/* 384 */       this.y = debug1.get("Y").asInt(0);
/* 385 */       this.hasData = debug1.get("Blocks").result().isPresent();
/*     */     }
/*     */     
/*     */     public Dynamic<?> getBlock(int debug1) {
/* 389 */       if (debug1 < 0 || debug1 > 4095) {
/* 390 */         return ChunkPalettedStorageFix.AIR;
/*     */       }
/*     */       
/* 393 */       Dynamic<?> debug2 = (Dynamic)this.palette.byId(this.buffer[debug1]);
/* 394 */       return (debug2 == null) ? ChunkPalettedStorageFix.AIR : debug2;
/*     */     }
/*     */     
/*     */     public void setBlock(int debug1, Dynamic<?> debug2) {
/* 398 */       if (this.seen.add(debug2)) {
/* 399 */         this.listTag.add("%%FILTER_ME%%".equals(ChunkPalettedStorageFix.getName(debug2)) ? ChunkPalettedStorageFix.AIR : debug2);
/*     */       }
/* 401 */       this.buffer[debug1] = ChunkPalettedStorageFix.idFor(this.palette, debug2);
/*     */     }
/*     */     
/*     */     public int upgrade(int debug1) {
/* 405 */       if (!this.hasData) {
/* 406 */         return debug1;
/*     */       }
/* 408 */       ByteBuffer debug2 = this.section.get("Blocks").asByteBufferOpt().result().get();
/* 409 */       ChunkPalettedStorageFix.DataLayer debug3 = this.section.get("Data").asByteBufferOpt().map(debug0 -> new ChunkPalettedStorageFix.DataLayer(DataFixUtils.toArray(debug0))).result().orElseGet(DataLayer::new);
/* 410 */       ChunkPalettedStorageFix.DataLayer debug4 = this.section.get("Add").asByteBufferOpt().map(debug0 -> new ChunkPalettedStorageFix.DataLayer(DataFixUtils.toArray(debug0))).result().orElseGet(DataLayer::new);
/*     */       
/* 412 */       this.seen.add(ChunkPalettedStorageFix.AIR);
/* 413 */       ChunkPalettedStorageFix.idFor(this.palette, ChunkPalettedStorageFix.AIR);
/* 414 */       this.listTag.add(ChunkPalettedStorageFix.AIR);
/*     */       
/* 416 */       for (int debug5 = 0; debug5 < 4096; debug5++) {
/* 417 */         int debug6 = debug5 & 0xF;
/* 418 */         int debug7 = debug5 >> 8 & 0xF;
/* 419 */         int debug8 = debug5 >> 4 & 0xF;
/* 420 */         int debug9 = debug4.get(debug6, debug7, debug8) << 12 | (debug2.get(debug5) & 0xFF) << 4 | debug3.get(debug6, debug7, debug8);
/*     */         
/* 422 */         if (ChunkPalettedStorageFix.FIX.get(debug9 >> 4)) {
/* 423 */           addFix(debug9 >> 4, debug5);
/*     */         }
/* 425 */         if (ChunkPalettedStorageFix.VIRTUAL.get(debug9 >> 4)) {
/*     */           
/* 427 */           int debug10 = ChunkPalettedStorageFix.getSideMask((debug6 == 0), (debug6 == 15), (debug8 == 0), (debug8 == 15));
/* 428 */           if (debug10 == 0) {
/*     */             
/* 430 */             this.update.add(debug5);
/*     */           } else {
/* 432 */             debug1 |= debug10;
/*     */           } 
/*     */         } 
/*     */         
/* 436 */         setBlock(debug5, BlockStateData.getTag(debug9));
/*     */       } 
/*     */       
/* 439 */       return debug1;
/*     */     }
/*     */     private void addFix(int debug1, int debug2) {
/*     */       IntArrayList intArrayList;
/* 443 */       IntList debug3 = (IntList)this.toFix.get(debug1);
/* 444 */       if (debug3 == null) {
/* 445 */         intArrayList = new IntArrayList();
/* 446 */         this.toFix.put(debug1, intArrayList);
/*     */       } 
/* 448 */       intArrayList.add(debug2);
/*     */     }
/*     */     
/*     */     public Dynamic<?> write() {
/* 452 */       Dynamic<?> debug1 = this.section;
/* 453 */       if (!this.hasData) {
/* 454 */         return debug1;
/*     */       }
/* 456 */       debug1 = debug1.set("Palette", debug1.createList(this.listTag.stream()));
/*     */       
/* 458 */       int debug2 = Math.max(4, DataFixUtils.ceillog2(this.seen.size()));
/* 459 */       PackedBitStorage debug3 = new PackedBitStorage(debug2, 4096);
/* 460 */       for (int debug4 = 0; debug4 < this.buffer.length; debug4++) {
/* 461 */         debug3.set(debug4, this.buffer[debug4]);
/*     */       }
/*     */       
/* 464 */       debug1 = debug1.set("BlockStates", debug1.createLongList(Arrays.stream(debug3.getRaw())));
/*     */       
/* 466 */       debug1 = debug1.remove("Blocks");
/* 467 */       debug1 = debug1.remove("Data");
/* 468 */       debug1 = debug1.remove("Add");
/*     */       
/* 470 */       return debug1;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class UpgradeChunk
/*     */   {
/*     */     private int sides;
/* 477 */     private final ChunkPalettedStorageFix.Section[] sections = new ChunkPalettedStorageFix.Section[16];
/*     */     
/*     */     private final Dynamic<?> level;
/*     */     private final int x;
/*     */     private final int z;
/* 482 */     private final Int2ObjectMap<Dynamic<?>> blockEntities = (Int2ObjectMap<Dynamic<?>>)new Int2ObjectLinkedOpenHashMap(16);
/*     */     
/*     */     public UpgradeChunk(Dynamic<?> debug1) {
/* 485 */       this.level = debug1;
/* 486 */       this.x = debug1.get("xPos").asInt(0) << 4;
/* 487 */       this.z = debug1.get("zPos").asInt(0) << 4;
/*     */       
/* 489 */       debug1.get("TileEntities").asStreamOpt().result().ifPresent(debug1 -> debug1.forEach(()));
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
/* 502 */       boolean debug2 = debug1.get("convertedFromAlphaFormat").asBoolean(false);
/*     */       
/* 504 */       debug1.get("Sections").asStreamOpt().result().ifPresent(debug1 -> debug1.forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 512 */       for (ChunkPalettedStorageFix.Section debug6 : this.sections) {
/* 513 */         if (debug6 != null)
/*     */         {
/*     */ 
/*     */           
/* 517 */           for (ObjectIterator<Map.Entry<Integer, IntList>> objectIterator = debug6.toFix.entrySet().iterator(); objectIterator.hasNext(); ) { IntListIterator<Integer> intListIterator; Map.Entry<Integer, IntList> debug8 = objectIterator.next();
/* 518 */             int debug9 = debug6.y << 12;
/* 519 */             switch (((Integer)debug8.getKey()).intValue()) {
/*     */               case 2:
/* 521 */                 for (intListIterator = ((IntList)debug8.getValue()).iterator(); intListIterator.hasNext(); ) { int debug11 = ((Integer)intListIterator.next()).intValue();
/* 522 */                   debug11 |= debug9;
/*     */                   
/* 524 */                   Dynamic<?> debug12 = getBlock(debug11);
/* 525 */                   if ("minecraft:grass_block".equals(ChunkPalettedStorageFix.getName(debug12))) {
/* 526 */                     String debug13 = ChunkPalettedStorageFix.getName(getBlock(relative(debug11, ChunkPalettedStorageFix.Direction.UP)));
/* 527 */                     if ("minecraft:snow".equals(debug13) || "minecraft:snow_layer".equals(debug13)) {
/* 528 */                       setBlock(debug11, ChunkPalettedStorageFix.SNOWY_GRASS);
/*     */                     }
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 3:
/* 535 */                 for (intListIterator = ((IntList)debug8.getValue()).iterator(); intListIterator.hasNext(); ) { int debug11 = ((Integer)intListIterator.next()).intValue();
/* 536 */                   debug11 |= debug9;
/*     */                   
/* 538 */                   Dynamic<?> debug12 = getBlock(debug11);
/* 539 */                   if ("minecraft:podzol".equals(ChunkPalettedStorageFix.getName(debug12))) {
/* 540 */                     String debug13 = ChunkPalettedStorageFix.getName(getBlock(relative(debug11, ChunkPalettedStorageFix.Direction.UP)));
/* 541 */                     if ("minecraft:snow".equals(debug13) || "minecraft:snow_layer".equals(debug13)) {
/* 542 */                       setBlock(debug11, ChunkPalettedStorageFix.SNOWY_PODZOL);
/*     */                     }
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 110:
/* 549 */                 for (intListIterator = ((IntList)debug8.getValue()).iterator(); intListIterator.hasNext(); ) { int debug11 = ((Integer)intListIterator.next()).intValue();
/* 550 */                   debug11 |= debug9;
/*     */                   
/* 552 */                   Dynamic<?> debug12 = getBlock(debug11);
/* 553 */                   if ("minecraft:mycelium".equals(ChunkPalettedStorageFix.getName(debug12))) {
/* 554 */                     String debug13 = ChunkPalettedStorageFix.getName(getBlock(relative(debug11, ChunkPalettedStorageFix.Direction.UP)));
/* 555 */                     if ("minecraft:snow".equals(debug13) || "minecraft:snow_layer".equals(debug13)) {
/* 556 */                       setBlock(debug11, ChunkPalettedStorageFix.SNOWY_MYCELIUM);
/*     */                     }
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 25:
/* 563 */                 for (intListIterator = ((IntList)debug8.getValue()).iterator(); intListIterator.hasNext(); ) { int debug11 = ((Integer)intListIterator.next()).intValue();
/* 564 */                   debug11 |= debug9;
/* 565 */                   Dynamic<?> debug12 = removeBlockEntity(debug11);
/* 566 */                   if (debug12 != null) {
/* 567 */                     String debug13 = Boolean.toString(debug12.get("powered").asBoolean(false)) + (byte)Math.min(Math.max(debug12.get("note").asInt(0), 0), 24);
/* 568 */                     setBlock(debug11, (Dynamic)ChunkPalettedStorageFix.NOTE_BLOCK_MAP.getOrDefault(debug13, ChunkPalettedStorageFix.NOTE_BLOCK_MAP.get("false0")));
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 26:
/* 574 */                 for (intListIterator = ((IntList)debug8.getValue()).iterator(); intListIterator.hasNext(); ) { int debug11 = ((Integer)intListIterator.next()).intValue();
/* 575 */                   debug11 |= debug9;
/* 576 */                   Dynamic<?> debug12 = getBlockEntity(debug11);
/* 577 */                   Dynamic<?> debug13 = getBlock(debug11);
/* 578 */                   if (debug12 != null) {
/* 579 */                     int debug14 = debug12.get("color").asInt(0);
/* 580 */                     if (debug14 != 14 && debug14 >= 0 && debug14 < 16) {
/* 581 */                       String debug15 = ChunkPalettedStorageFix.getProperty(debug13, "facing") + ChunkPalettedStorageFix.getProperty(debug13, "occupied") + ChunkPalettedStorageFix.getProperty(debug13, "part") + debug14;
/* 582 */                       if (ChunkPalettedStorageFix.BED_BLOCK_MAP.containsKey(debug15)) {
/* 583 */                         setBlock(debug11, (Dynamic)ChunkPalettedStorageFix.BED_BLOCK_MAP.get(debug15));
/*     */                       }
/*     */                     } 
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 176:
/*     */               case 177:
/* 592 */                 for (intListIterator = ((IntList)debug8.getValue()).iterator(); intListIterator.hasNext(); ) { int debug11 = ((Integer)intListIterator.next()).intValue();
/* 593 */                   debug11 |= debug9;
/* 594 */                   Dynamic<?> debug12 = getBlockEntity(debug11);
/* 595 */                   Dynamic<?> debug13 = getBlock(debug11);
/* 596 */                   if (debug12 != null) {
/* 597 */                     int debug14 = debug12.get("Base").asInt(0);
/* 598 */                     if (debug14 != 15 && debug14 >= 0 && debug14 < 16) {
/* 599 */                       String debug15 = ChunkPalettedStorageFix.getProperty(debug13, (((Integer)debug8.getKey()).intValue() == 176) ? "rotation" : "facing") + "_" + debug14;
/* 600 */                       if (ChunkPalettedStorageFix.BANNER_BLOCK_MAP.containsKey(debug15)) {
/* 601 */                         setBlock(debug11, (Dynamic)ChunkPalettedStorageFix.BANNER_BLOCK_MAP.get(debug15));
/*     */                       }
/*     */                     } 
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 86:
/* 609 */                 for (intListIterator = ((IntList)debug8.getValue()).iterator(); intListIterator.hasNext(); ) { int debug11 = ((Integer)intListIterator.next()).intValue();
/* 610 */                   debug11 |= debug9;
/*     */                   
/* 612 */                   Dynamic<?> debug12 = getBlock(debug11);
/* 613 */                   if ("minecraft:carved_pumpkin".equals(ChunkPalettedStorageFix.getName(debug12))) {
/* 614 */                     String debug13 = ChunkPalettedStorageFix.getName(getBlock(relative(debug11, ChunkPalettedStorageFix.Direction.DOWN)));
/* 615 */                     if ("minecraft:grass_block".equals(debug13) || "minecraft:dirt".equals(debug13)) {
/* 616 */                       setBlock(debug11, ChunkPalettedStorageFix.PUMPKIN);
/*     */                     }
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 140:
/* 623 */                 for (intListIterator = ((IntList)debug8.getValue()).iterator(); intListIterator.hasNext(); ) { int debug11 = ((Integer)intListIterator.next()).intValue();
/* 624 */                   debug11 |= debug9;
/* 625 */                   Dynamic<?> debug12 = removeBlockEntity(debug11);
/* 626 */                   if (debug12 != null) {
/* 627 */                     String debug13 = debug12.get("Item").asString("") + debug12.get("Data").asInt(0);
/* 628 */                     setBlock(debug11, (Dynamic)ChunkPalettedStorageFix.FLOWER_POT_MAP.getOrDefault(debug13, ChunkPalettedStorageFix.FLOWER_POT_MAP.get("minecraft:air0")));
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 144:
/* 634 */                 for (intListIterator = ((IntList)debug8.getValue()).iterator(); intListIterator.hasNext(); ) { int debug11 = ((Integer)intListIterator.next()).intValue();
/* 635 */                   debug11 |= debug9;
/* 636 */                   Dynamic<?> debug12 = getBlockEntity(debug11);
/* 637 */                   if (debug12 != null) {
/* 638 */                     String debug15, debug13 = String.valueOf(debug12.get("SkullType").asInt(0));
/* 639 */                     String debug14 = ChunkPalettedStorageFix.getProperty(getBlock(debug11), "facing");
/*     */                     
/* 641 */                     if ("up".equals(debug14) || "down".equals(debug14)) {
/* 642 */                       debug15 = debug13 + String.valueOf(debug12.get("Rot").asInt(0));
/*     */                     } else {
/* 644 */                       debug15 = debug13 + debug14;
/*     */                     } 
/*     */                     
/* 647 */                     debug12.remove("SkullType");
/* 648 */                     debug12.remove("facing");
/* 649 */                     debug12.remove("Rot");
/*     */                     
/* 651 */                     setBlock(debug11, (Dynamic)ChunkPalettedStorageFix.SKULL_MAP.getOrDefault(debug15, ChunkPalettedStorageFix.SKULL_MAP.get("0north")));
/*     */                   }  }
/*     */               
/*     */               
/*     */               case 64:
/*     */               case 71:
/*     */               case 193:
/*     */               case 194:
/*     */               case 195:
/*     */               case 196:
/*     */               case 197:
/* 662 */                 for (intListIterator = ((IntList)debug8.getValue()).iterator(); intListIterator.hasNext(); ) { int debug11 = ((Integer)intListIterator.next()).intValue();
/* 663 */                   debug11 |= debug9;
/*     */                   
/* 665 */                   Dynamic<?> debug12 = getBlock(debug11);
/* 666 */                   if (ChunkPalettedStorageFix.getName(debug12).endsWith("_door")) {
/* 667 */                     Dynamic<?> debug13 = getBlock(debug11);
/* 668 */                     if ("lower".equals(ChunkPalettedStorageFix.getProperty(debug13, "half"))) {
/* 669 */                       int debug14 = relative(debug11, ChunkPalettedStorageFix.Direction.UP);
/* 670 */                       Dynamic<?> debug15 = getBlock(debug14);
/* 671 */                       String debug16 = ChunkPalettedStorageFix.getName(debug13);
/* 672 */                       if (debug16.equals(ChunkPalettedStorageFix.getName(debug15))) {
/* 673 */                         String debug17 = ChunkPalettedStorageFix.getProperty(debug13, "facing");
/* 674 */                         String debug18 = ChunkPalettedStorageFix.getProperty(debug13, "open");
/* 675 */                         String debug19 = debug2 ? "left" : ChunkPalettedStorageFix.getProperty(debug15, "hinge");
/* 676 */                         String debug20 = debug2 ? "false" : ChunkPalettedStorageFix.getProperty(debug15, "powered");
/* 677 */                         setBlock(debug11, (Dynamic)ChunkPalettedStorageFix.DOOR_MAP.get(debug16 + debug17 + "lower" + debug19 + debug18 + debug20));
/* 678 */                         setBlock(debug14, (Dynamic)ChunkPalettedStorageFix.DOOR_MAP.get(debug16 + debug17 + "upper" + debug19 + debug18 + debug20));
/*     */                       } 
/*     */                     } 
/*     */                   }  }
/*     */               
/*     */ 
/*     */               
/*     */               case 175:
/* 686 */                 for (intListIterator = ((IntList)debug8.getValue()).iterator(); intListIterator.hasNext(); ) { int debug11 = ((Integer)intListIterator.next()).intValue();
/* 687 */                   debug11 |= debug9;
/*     */                   
/* 689 */                   Dynamic<?> debug12 = getBlock(debug11);
/* 690 */                   if ("upper".equals(ChunkPalettedStorageFix.getProperty(debug12, "half"))) {
/* 691 */                     Dynamic<?> debug13 = getBlock(relative(debug11, ChunkPalettedStorageFix.Direction.DOWN));
/* 692 */                     String debug14 = ChunkPalettedStorageFix.getName(debug13);
/* 693 */                     if ("minecraft:sunflower".equals(debug14)) {
/* 694 */                       setBlock(debug11, ChunkPalettedStorageFix.UPPER_SUNFLOWER); continue;
/* 695 */                     }  if ("minecraft:lilac".equals(debug14)) {
/* 696 */                       setBlock(debug11, ChunkPalettedStorageFix.UPPER_LILAC); continue;
/* 697 */                     }  if ("minecraft:tall_grass".equals(debug14)) {
/* 698 */                       setBlock(debug11, ChunkPalettedStorageFix.UPPER_TALL_GRASS); continue;
/* 699 */                     }  if ("minecraft:large_fern".equals(debug14)) {
/* 700 */                       setBlock(debug11, ChunkPalettedStorageFix.UPPER_LARGE_FERN); continue;
/* 701 */                     }  if ("minecraft:rose_bush".equals(debug14)) {
/* 702 */                       setBlock(debug11, ChunkPalettedStorageFix.UPPER_ROSE_BUSH); continue;
/* 703 */                     }  if ("minecraft:peony".equals(debug14)) {
/* 704 */                       setBlock(debug11, ChunkPalettedStorageFix.UPPER_PEONY);
/*     */                     }
/*     */                   }  }
/*     */               
/*     */             }  }
/*     */         
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private Dynamic<?> getBlockEntity(int debug1) {
/* 717 */       return (Dynamic)this.blockEntities.get(debug1);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private Dynamic<?> removeBlockEntity(int debug1) {
/* 722 */       return (Dynamic)this.blockEntities.remove(debug1); } public static int relative(int debug0, ChunkPalettedStorageFix.Direction debug1) {
/*     */       int debug2;
/*     */       int debug3;
/*     */       int debug4;
/* 726 */       switch (debug1.getAxis()) {
/*     */         case X:
/* 728 */           debug2 = (debug0 & 0xF) + debug1.getAxisDirection().getStep();
/* 729 */           return (debug2 < 0 || debug2 > 15) ? -1 : (debug0 & 0xFFFFFFF0 | debug2);
/*     */         case Y:
/* 731 */           debug3 = (debug0 >> 8) + debug1.getAxisDirection().getStep();
/* 732 */           return (debug3 < 0 || debug3 > 255) ? -1 : (debug0 & 0xFF | debug3 << 8);
/*     */         case Z:
/* 734 */           debug4 = (debug0 >> 4 & 0xF) + debug1.getAxisDirection().getStep();
/* 735 */           return (debug4 < 0 || debug4 > 15) ? -1 : (debug0 & 0xFFFFFF0F | debug4 << 4);
/*     */       } 
/* 737 */       return -1;
/*     */     }
/*     */     
/*     */     private void setBlock(int debug1, Dynamic<?> debug2) {
/* 741 */       if (debug1 < 0 || debug1 > 65535) {
/*     */         return;
/*     */       }
/*     */       
/* 745 */       ChunkPalettedStorageFix.Section debug3 = getSection(debug1);
/*     */       
/* 747 */       if (debug3 == null) {
/*     */         return;
/*     */       }
/*     */       
/* 751 */       debug3.setBlock(debug1 & 0xFFF, debug2);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private ChunkPalettedStorageFix.Section getSection(int debug1) {
/* 756 */       int debug2 = debug1 >> 12;
/* 757 */       return (debug2 < this.sections.length) ? this.sections[debug2] : null;
/*     */     }
/*     */     
/*     */     public Dynamic<?> getBlock(int debug1) {
/* 761 */       if (debug1 < 0 || debug1 > 65535) {
/* 762 */         return ChunkPalettedStorageFix.AIR;
/*     */       }
/*     */       
/* 765 */       ChunkPalettedStorageFix.Section debug2 = getSection(debug1);
/*     */       
/* 767 */       if (debug2 == null) {
/* 768 */         return ChunkPalettedStorageFix.AIR;
/*     */       }
/*     */       
/* 771 */       return debug2.getBlock(debug1 & 0xFFF);
/*     */     }
/*     */     
/*     */     public Dynamic<?> write() {
/* 775 */       Dynamic<?> debug1 = this.level;
/* 776 */       if (this.blockEntities.isEmpty()) {
/* 777 */         debug1 = debug1.remove("TileEntities");
/*     */       } else {
/* 779 */         debug1 = debug1.set("TileEntities", debug1.createList(this.blockEntities.values().stream()));
/*     */       } 
/*     */       
/* 782 */       Dynamic<?> debug2 = debug1.emptyMap();
/* 783 */       List<Dynamic<?>> debug3 = Lists.newArrayList();
/* 784 */       for (ChunkPalettedStorageFix.Section debug7 : this.sections) {
/* 785 */         if (debug7 != null) {
/* 786 */           debug3.add(debug7.write());
/* 787 */           debug2 = debug2.set(String.valueOf(debug7.y), debug2.createIntList(Arrays.stream(debug7.update.toIntArray())));
/*     */         } 
/*     */       } 
/*     */       
/* 791 */       Dynamic<?> debug4 = debug1.emptyMap();
/* 792 */       debug4 = debug4.set("Sides", debug4.createByte((byte)this.sides));
/* 793 */       debug4 = debug4.set("Indices", debug2);
/* 794 */       return debug1.set("UpgradeData", debug4).set("Sections", debug4.createList(debug3.stream()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class DataLayer
/*     */   {
/*     */     private final byte[] data;
/*     */ 
/*     */     
/*     */     public DataLayer() {
/* 805 */       this.data = new byte[2048];
/*     */     }
/*     */     
/*     */     public DataLayer(byte[] debug1) {
/* 809 */       this.data = debug1;
/*     */       
/* 811 */       if (debug1.length != 2048) {
/* 812 */         throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + debug1.length);
/*     */       }
/*     */     }
/*     */     
/*     */     public int get(int debug1, int debug2, int debug3) {
/* 817 */       int debug4 = getPosition(debug2 << 8 | debug3 << 4 | debug1);
/*     */       
/* 819 */       if (isFirst(debug2 << 8 | debug3 << 4 | debug1)) {
/* 820 */         return this.data[debug4] & 0xF;
/*     */       }
/* 822 */       return this.data[debug4] >> 4 & 0xF;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean isFirst(int debug1) {
/* 827 */       return ((debug1 & 0x1) == 0);
/*     */     }
/*     */     
/*     */     private int getPosition(int debug1) {
/* 831 */       return debug1 >> 1;
/*     */     }
/*     */   }
/*     */   
/*     */   public static int getSideMask(boolean debug0, boolean debug1, boolean debug2, boolean debug3) {
/* 836 */     int debug4 = 0;
/* 837 */     if (debug2) {
/* 838 */       if (debug1) {
/* 839 */         debug4 |= 0x2;
/* 840 */       } else if (debug0) {
/* 841 */         debug4 |= 0x80;
/*     */       } else {
/* 843 */         debug4 |= 0x1;
/*     */       } 
/* 845 */     } else if (debug3) {
/* 846 */       if (debug0) {
/* 847 */         debug4 |= 0x20;
/* 848 */       } else if (debug1) {
/* 849 */         debug4 |= 0x8;
/*     */       } else {
/* 851 */         debug4 |= 0x10;
/*     */       } 
/* 853 */     } else if (debug1) {
/* 854 */       debug4 |= 0x4;
/* 855 */     } else if (debug0) {
/* 856 */       debug4 |= 0x40;
/*     */     } 
/* 858 */     return debug4;
/*     */   }
/*     */   
/*     */   public enum Direction {
/* 862 */     DOWN((String)AxisDirection.NEGATIVE, Axis.Y),
/* 863 */     UP((String)AxisDirection.POSITIVE, Axis.Y),
/* 864 */     NORTH((String)AxisDirection.NEGATIVE, Axis.Z),
/* 865 */     SOUTH((String)AxisDirection.POSITIVE, Axis.Z),
/* 866 */     WEST((String)AxisDirection.NEGATIVE, Axis.X),
/* 867 */     EAST((String)AxisDirection.POSITIVE, Axis.X);
/*     */     
/*     */     private final Axis axis;
/*     */     
/*     */     private final AxisDirection axisDirection;
/*     */     
/*     */     Direction(AxisDirection debug3, Axis debug4) {
/* 874 */       this.axis = debug4;
/* 875 */       this.axisDirection = debug3;
/*     */     }
/*     */     
/*     */     public AxisDirection getAxisDirection() {
/* 879 */       return this.axisDirection;
/*     */     }
/*     */     
/*     */     public Axis getAxis() {
/* 883 */       return this.axis;
/*     */     }
/*     */     
/*     */     public enum Axis {
/* 887 */       X,
/* 888 */       Y,
/* 889 */       Z;
/*     */     }
/*     */     
/*     */     public enum AxisDirection {
/* 893 */       POSITIVE(1),
/* 894 */       NEGATIVE(-1);
/*     */       
/*     */       private final int step;
/*     */ 
/*     */       
/*     */       AxisDirection(int debug3) {
/* 900 */         this.step = debug3;
/*     */       }
/*     */       
/*     */       public int getStep() {
/* 904 */         return this.step;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ChunkPalettedStorageFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */