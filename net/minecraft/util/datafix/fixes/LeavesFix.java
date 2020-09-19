/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.List;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntIterator;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.util.datafix.PackedBitStorage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LeavesFix
/*     */   extends DataFix
/*     */ {
/*  45 */   private static final int[][] DIRECTIONS = new int[][] { { -1, 0, 0 }, { 1, 0, 0 }, { 0, -1, 0 }, { 0, 1, 0 }, { 0, 0, -1 }, { 0, 0, 1 } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Object2IntMap<String> LEAVES;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  58 */     LEAVES = (Object2IntMap<String>)DataFixUtils.make(new Object2IntOpenHashMap(), debug0 -> {
/*     */           debug0.put("minecraft:acacia_leaves", 0);
/*     */           debug0.put("minecraft:birch_leaves", 1);
/*     */           debug0.put("minecraft:dark_oak_leaves", 2);
/*     */           debug0.put("minecraft:jungle_leaves", 3);
/*     */           debug0.put("minecraft:oak_leaves", 4);
/*     */           debug0.put("minecraft:spruce_leaves", 5);
/*     */         });
/*     */   }
/*  67 */   private static final Set<String> LOGS = (Set<String>)ImmutableSet.of("minecraft:acacia_bark", "minecraft:birch_bark", "minecraft:dark_oak_bark", "minecraft:jungle_bark", "minecraft:oak_bark", "minecraft:spruce_bark", (Object[])new String[] { "minecraft:acacia_log", "minecraft:birch_log", "minecraft:dark_oak_log", "minecraft:jungle_log", "minecraft:oak_log", "minecraft:spruce_log", "minecraft:stripped_acacia_log", "minecraft:stripped_birch_log", "minecraft:stripped_dark_oak_log", "minecraft:stripped_jungle_log", "minecraft:stripped_oak_log", "minecraft:stripped_spruce_log" });
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
/*     */   public LeavesFix(Schema debug1, boolean debug2) {
/*  89 */     super(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  94 */     Type<?> debug1 = getInputSchema().getType(References.CHUNK);
/*     */     
/*  96 */     OpticFinder<?> debug2 = debug1.findField("Level");
/*  97 */     OpticFinder<?> debug3 = debug2.type().findField("Sections");
/*  98 */     Type<?> debug4 = debug3.type();
/*  99 */     if (!(debug4 instanceof List.ListType)) {
/* 100 */       throw new IllegalStateException("Expecting sections to be a list.");
/*     */     }
/* 102 */     Type<?> debug5 = ((List.ListType)debug4).getElement();
/* 103 */     OpticFinder<?> debug6 = DSL.typeFinder(debug5);
/*     */     
/* 105 */     return fixTypeEverywhereTyped("Leaves fix", debug1, debug4 -> debug4.updateTyped(debug1, ()));
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
/*     */   public static abstract class Section
/*     */   {
/* 193 */     private final Type<Pair<String, Dynamic<?>>> blockStateType = DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType());
/* 194 */     protected final OpticFinder<List<Pair<String, Dynamic<?>>>> paletteFinder = DSL.fieldFinder("Palette", (Type)DSL.list(this.blockStateType));
/*     */     
/*     */     protected final List<Dynamic<?>> palette;
/*     */     protected final int index;
/*     */     @Nullable
/*     */     protected PackedBitStorage storage;
/*     */     
/*     */     public Section(Typed<?> debug1, Schema debug2) {
/* 202 */       if (!Objects.equals(debug2.getType(References.BLOCK_STATE), this.blockStateType)) {
/* 203 */         throw new IllegalStateException("Block state type is not what was expected.");
/*     */       }
/*     */       
/* 206 */       Optional<List<Pair<String, Dynamic<?>>>> debug3 = debug1.getOptional(this.paletteFinder);
/*     */       
/* 208 */       this.palette = (List<Dynamic<?>>)debug3.map(debug0 -> (List)debug0.stream().map(Pair::getSecond).collect(Collectors.toList())).orElse(ImmutableList.of());
/*     */       
/* 210 */       Dynamic<?> debug4 = (Dynamic)debug1.get(DSL.remainderFinder());
/* 211 */       this.index = debug4.get("Y").asInt(0);
/*     */       
/* 213 */       readStorage(debug4);
/*     */     }
/*     */     
/*     */     protected void readStorage(Dynamic<?> debug1) {
/* 217 */       if (skippable()) {
/* 218 */         this.storage = null;
/*     */       } else {
/* 220 */         long[] debug2 = debug1.get("BlockStates").asLongStream().toArray();
/* 221 */         int debug3 = Math.max(4, DataFixUtils.ceillog2(this.palette.size()));
/* 222 */         this.storage = new PackedBitStorage(debug3, 4096, debug2);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Typed<?> write(Typed<?> debug1) {
/* 227 */       if (isSkippable()) {
/* 228 */         return debug1;
/*     */       }
/* 230 */       return debug1
/* 231 */         .update(DSL.remainderFinder(), debug1 -> debug1.set("BlockStates", debug1.createLongList(Arrays.stream(this.storage.getRaw()))))
/* 232 */         .set(this.paletteFinder, this.palette.stream().map(debug0 -> Pair.of(References.BLOCK_STATE.typeName(), debug0)).collect(Collectors.toList()));
/*     */     }
/*     */     
/*     */     public boolean isSkippable() {
/* 236 */       return (this.storage == null);
/*     */     }
/*     */     
/*     */     public int getBlock(int debug1) {
/* 240 */       return this.storage.get(debug1);
/*     */     }
/*     */     
/*     */     protected int getStateId(String debug1, boolean debug2, int debug3) {
/* 244 */       return LeavesFix.LEAVES.get(debug1).intValue() << 5 | (debug2 ? 16 : 0) | debug3;
/*     */     }
/*     */     
/*     */     int getIndex() {
/* 248 */       return this.index;
/*     */     }
/*     */ 
/*     */     
/*     */     protected abstract boolean skippable();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class LeavesSection
/*     */     extends Section
/*     */   {
/*     */     @Nullable
/*     */     private IntSet leaveIds;
/*     */     
/*     */     @Nullable
/*     */     private IntSet logIds;
/*     */     @Nullable
/*     */     private Int2IntMap stateToIdMap;
/*     */     
/*     */     public LeavesSection(Typed<?> debug1, Schema debug2) {
/* 268 */       super(debug1, debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean skippable() {
/* 273 */       this.leaveIds = (IntSet)new IntOpenHashSet();
/* 274 */       this.logIds = (IntSet)new IntOpenHashSet();
/* 275 */       this.stateToIdMap = (Int2IntMap)new Int2IntOpenHashMap();
/*     */       
/* 277 */       for (int debug1 = 0; debug1 < this.palette.size(); debug1++) {
/* 278 */         Dynamic<?> debug2 = this.palette.get(debug1);
/* 279 */         String debug3 = debug2.get("Name").asString("");
/* 280 */         if (LeavesFix.LEAVES.containsKey(debug3)) {
/* 281 */           boolean debug4 = Objects.equals(debug2.get("Properties").get("decayable").asString(""), "false");
/* 282 */           this.leaveIds.add(debug1);
/* 283 */           this.stateToIdMap.put(getStateId(debug3, debug4, 7), debug1);
/* 284 */           this.palette.set(debug1, makeLeafTag(debug2, debug3, debug4, 7));
/*     */         } 
/* 286 */         if (LeavesFix.LOGS.contains(debug3)) {
/* 287 */           this.logIds.add(debug1);
/*     */         }
/*     */       } 
/*     */       
/* 291 */       return (this.leaveIds.isEmpty() && this.logIds.isEmpty());
/*     */     }
/*     */     
/*     */     private Dynamic<?> makeLeafTag(Dynamic<?> debug1, String debug2, boolean debug3, int debug4) {
/* 295 */       Dynamic<?> debug5 = debug1.emptyMap();
/* 296 */       debug5 = debug5.set("persistent", debug5.createString(debug3 ? "true" : "false"));
/* 297 */       debug5 = debug5.set("distance", debug5.createString(Integer.toString(debug4)));
/*     */       
/* 299 */       Dynamic<?> debug6 = debug1.emptyMap();
/* 300 */       debug6 = debug6.set("Properties", debug5);
/* 301 */       debug6 = debug6.set("Name", debug6.createString(debug2));
/* 302 */       return debug6;
/*     */     }
/*     */     
/*     */     public boolean isLog(int debug1) {
/* 306 */       return this.logIds.contains(debug1);
/*     */     }
/*     */     
/*     */     public boolean isLeaf(int debug1) {
/* 310 */       return this.leaveIds.contains(debug1);
/*     */     }
/*     */     
/*     */     private int getDistance(int debug1) {
/* 314 */       if (isLog(debug1)) {
/* 315 */         return 0;
/*     */       }
/* 317 */       return Integer.parseInt(((Dynamic)this.palette.get(debug1)).get("Properties").get("distance").asString(""));
/*     */     }
/*     */     
/*     */     private void setDistance(int debug1, int debug2, int debug3) {
/* 321 */       Dynamic<?> debug4 = this.palette.get(debug2);
/* 322 */       String debug5 = debug4.get("Name").asString("");
/* 323 */       boolean debug6 = Objects.equals(debug4.get("Properties").get("persistent").asString(""), "true");
/* 324 */       int debug7 = getStateId(debug5, debug6, debug3);
/*     */       
/* 326 */       if (!this.stateToIdMap.containsKey(debug7)) {
/* 327 */         int i = this.palette.size();
/* 328 */         this.leaveIds.add(i);
/* 329 */         this.stateToIdMap.put(debug7, i);
/* 330 */         this.palette.add(makeLeafTag(debug4, debug5, debug6, debug3));
/*     */       } 
/*     */       
/* 333 */       int debug8 = this.stateToIdMap.get(debug7);
/* 334 */       if (1 << this.storage.getBits() <= debug8) {
/* 335 */         PackedBitStorage debug9 = new PackedBitStorage(this.storage.getBits() + 1, 4096);
/* 336 */         for (int debug10 = 0; debug10 < 4096; debug10++) {
/* 337 */           debug9.set(debug10, this.storage.get(debug10));
/*     */         }
/* 339 */         this.storage = debug9;
/*     */       } 
/* 341 */       this.storage.set(debug1, debug8);
/*     */     }
/*     */   }
/*     */   
/*     */   public static int getIndex(int debug0, int debug1, int debug2) {
/* 346 */     return debug1 << 8 | debug2 << 4 | debug0;
/*     */   }
/*     */   
/*     */   private int getX(int debug1) {
/* 350 */     return debug1 & 0xF;
/*     */   }
/*     */   
/*     */   private int getY(int debug1) {
/* 354 */     return debug1 >> 8 & 0xFF;
/*     */   }
/*     */   
/*     */   private int getZ(int debug1) {
/* 358 */     return debug1 >> 4 & 0xF;
/*     */   }
/*     */   
/*     */   public static int getSideMask(boolean debug0, boolean debug1, boolean debug2, boolean debug3) {
/* 362 */     int debug4 = 0;
/* 363 */     if (debug2) {
/* 364 */       if (debug1) {
/* 365 */         debug4 |= 0x2;
/* 366 */       } else if (debug0) {
/* 367 */         debug4 |= 0x80;
/*     */       } else {
/* 369 */         debug4 |= 0x1;
/*     */       } 
/* 371 */     } else if (debug3) {
/* 372 */       if (debug0) {
/* 373 */         debug4 |= 0x20;
/* 374 */       } else if (debug1) {
/* 375 */         debug4 |= 0x8;
/*     */       } else {
/* 377 */         debug4 |= 0x10;
/*     */       } 
/* 379 */     } else if (debug1) {
/* 380 */       debug4 |= 0x4;
/* 381 */     } else if (debug0) {
/* 382 */       debug4 |= 0x40;
/*     */     } 
/* 384 */     return debug4;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\LeavesFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */