/*     */ package net.minecraft.world.entity.ai.village.poi;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.io.File;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.SectionTracker;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.storage.SectionStorage;
/*     */ 
/*     */ 
/*     */ public class PoiManager
/*     */   extends SectionStorage<PoiSection>
/*     */ {
/*     */   private final DistanceTracker distanceTracker;
/*  41 */   private final LongSet loadedChunks = (LongSet)new LongOpenHashSet();
/*     */   
/*     */   public PoiManager(File debug1, DataFixer debug2, boolean debug3) {
/*  44 */     super(debug1, PoiSection::codec, PoiSection::new, debug2, DataFixTypes.POI_CHUNK, debug3);
/*  45 */     this.distanceTracker = new DistanceTracker();
/*     */   }
/*     */   
/*     */   public void add(BlockPos debug1, PoiType debug2) {
/*  49 */     ((PoiSection)getOrCreate(SectionPos.of(debug1).asLong())).add(debug1, debug2);
/*     */   }
/*     */   
/*     */   public void remove(BlockPos debug1) {
/*  53 */     ((PoiSection)getOrCreate(SectionPos.of(debug1).asLong())).remove(debug1);
/*     */   }
/*     */   
/*     */   public long getCountInRange(Predicate<PoiType> debug1, BlockPos debug2, int debug3, Occupancy debug4) {
/*  57 */     return getInRange(debug1, debug2, debug3, debug4).count();
/*     */   }
/*     */   
/*     */   public boolean existsAtPosition(PoiType debug1, BlockPos debug2) {
/*  61 */     Optional<PoiType> debug3 = ((PoiSection)getOrCreate(SectionPos.of(debug2).asLong())).getType(debug2);
/*  62 */     return (debug3.isPresent() && ((PoiType)debug3.get()).equals(debug1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stream<PoiRecord> getInSquare(Predicate<PoiType> debug1, BlockPos debug2, int debug3, Occupancy debug4) {
/*  69 */     int debug5 = Math.floorDiv(debug3, 16) + 1;
/*     */     
/*  71 */     return ChunkPos.rangeClosed(new ChunkPos(debug2), debug5).flatMap(debug3 -> getInChunk(debug1, debug3, debug2))
/*  72 */       .filter(debug2 -> {
/*     */           BlockPos debug3 = debug2.getPos();
/*  74 */           return (Math.abs(debug3.getX() - debug0.getX()) <= debug1 && Math.abs(debug3.getZ() - debug0.getZ()) <= debug1);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<PoiRecord> getInRange(Predicate<PoiType> debug1, BlockPos debug2, int debug3, Occupancy debug4) {
/*  80 */     int debug5 = debug3 * debug3;
/*  81 */     return getInSquare(debug1, debug2, debug3, debug4).filter(debug2 -> (debug2.getPos().distSqr((Vec3i)debug0) <= debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<PoiRecord> getInChunk(Predicate<PoiType> debug1, ChunkPos debug2, Occupancy debug3) {
/*  86 */     return IntStream.range(0, 16).boxed()
/*  87 */       .map(debug2 -> getOrLoad(SectionPos.of(debug1, debug2.intValue()).asLong()))
/*  88 */       .filter(Optional::isPresent)
/*  89 */       .flatMap(debug2 -> ((PoiSection)debug2.get()).getRecords(debug0, debug1));
/*     */   }
/*     */   
/*     */   public Stream<BlockPos> findAll(Predicate<PoiType> debug1, Predicate<BlockPos> debug2, BlockPos debug3, int debug4, Occupancy debug5) {
/*  93 */     return getInRange(debug1, debug3, debug4, debug5)
/*  94 */       .<BlockPos>map(PoiRecord::getPos)
/*  95 */       .filter(debug2);
/*     */   }
/*     */   
/*     */   public Stream<BlockPos> findAllClosestFirst(Predicate<PoiType> debug1, Predicate<BlockPos> debug2, BlockPos debug3, int debug4, Occupancy debug5) {
/*  99 */     return findAll(debug1, debug2, debug3, debug4, debug5)
/* 100 */       .sorted(Comparator.comparingDouble(debug1 -> debug1.distSqr((Vec3i)debug0)));
/*     */   }
/*     */   
/*     */   public Optional<BlockPos> find(Predicate<PoiType> debug1, Predicate<BlockPos> debug2, BlockPos debug3, int debug4, Occupancy debug5) {
/* 104 */     return findAll(debug1, debug2, debug3, debug4, debug5).findFirst();
/*     */   }
/*     */   
/*     */   public Optional<BlockPos> findClosest(Predicate<PoiType> debug1, BlockPos debug2, int debug3, Occupancy debug4) {
/* 108 */     return getInRange(debug1, debug2, debug3, debug4)
/* 109 */       .<BlockPos>map(PoiRecord::getPos)
/* 110 */       .min(Comparator.comparingDouble(debug1 -> debug1.distSqr((Vec3i)debug0)));
/*     */   }
/*     */   
/*     */   public Optional<BlockPos> take(Predicate<PoiType> debug1, Predicate<BlockPos> debug2, BlockPos debug3, int debug4) {
/* 114 */     return getInRange(debug1, debug3, debug4, Occupancy.HAS_SPACE)
/* 115 */       .filter(debug1 -> debug0.test(debug1.getPos()))
/* 116 */       .findFirst()
/* 117 */       .map(debug0 -> {
/*     */           debug0.acquireTicket();
/*     */           return debug0.getPos();
/*     */         });
/*     */   }
/*     */   
/*     */   public Optional<BlockPos> getRandom(Predicate<PoiType> debug1, Predicate<BlockPos> debug2, Occupancy debug3, BlockPos debug4, int debug5, Random debug6) {
/* 124 */     List<PoiRecord> debug7 = getInRange(debug1, debug4, debug5, debug3).collect((Collector)Collectors.toList());
/* 125 */     Collections.shuffle(debug7, debug6);
/* 126 */     return debug7.stream().filter(debug1 -> debug0.test(debug1.getPos())).findFirst().map(PoiRecord::getPos);
/*     */   }
/*     */   
/*     */   public boolean release(BlockPos debug1) {
/* 130 */     return ((PoiSection)getOrCreate(SectionPos.of(debug1).asLong())).release(debug1);
/*     */   }
/*     */   
/*     */   public boolean exists(BlockPos debug1, Predicate<PoiType> debug2) {
/* 134 */     return ((Boolean)getOrLoad(SectionPos.of(debug1).asLong()).map(debug2 -> Boolean.valueOf(debug2.exists(debug0, debug1))).orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */   
/*     */   public Optional<PoiType> getType(BlockPos debug1) {
/* 138 */     PoiSection debug2 = (PoiSection)getOrCreate(SectionPos.of(debug1).asLong());
/* 139 */     return debug2.getType(debug1);
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
/*     */   public int sectionsToVillage(SectionPos debug1) {
/* 153 */     this.distanceTracker.runAllUpdates();
/* 154 */     return this.distanceTracker.getLevel(debug1.asLong());
/*     */   }
/*     */   
/*     */   private boolean isVillageCenter(long debug1) {
/* 158 */     Optional<PoiSection> debug3 = get(debug1);
/* 159 */     if (debug3 == null) {
/* 160 */       return false;
/*     */     }
/*     */     
/* 163 */     return ((Boolean)debug3.<Boolean>map(debug0 -> Boolean.valueOf((debug0.getRecords(PoiType.ALL, Occupancy.IS_OCCUPIED).count() > 0L))).orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BooleanSupplier debug1) {
/* 168 */     super.tick(debug1);
/* 169 */     this.distanceTracker.runAllUpdates();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setDirty(long debug1) {
/* 174 */     super.setDirty(debug1);
/* 175 */     this.distanceTracker.update(debug1, this.distanceTracker.getLevelFromSource(debug1), false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onSectionLoad(long debug1) {
/* 180 */     this.distanceTracker.update(debug1, this.distanceTracker.getLevelFromSource(debug1), false);
/*     */   }
/*     */   
/*     */   public void checkConsistencyWithBlocks(ChunkPos debug1, LevelChunkSection debug2) {
/* 184 */     SectionPos debug3 = SectionPos.of(debug1, debug2.bottomBlockY() >> 4);
/* 185 */     Util.ifElse(getOrLoad(debug3.asLong()), debug3 -> debug3.refresh(()), () -> {
/*     */           if (mayHavePoi(debug1)) {
/*     */             PoiSection debug3 = (PoiSection)getOrCreate(debug2.asLong());
/*     */             updateFromSection(debug1, debug2, debug3::add);
/*     */           } 
/*     */         });
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
/*     */   private static boolean mayHavePoi(LevelChunkSection debug0) {
/* 203 */     return debug0.maybeHas(PoiType.ALL_STATES::contains);
/*     */   }
/*     */   
/*     */   private void updateFromSection(LevelChunkSection debug1, SectionPos debug2, BiConsumer<BlockPos, PoiType> debug3) {
/* 207 */     debug2.blocksInside().forEach(debug2 -> {
/*     */           BlockState debug3 = debug0.getBlockState(SectionPos.sectionRelative(debug2.getX()), SectionPos.sectionRelative(debug2.getY()), SectionPos.sectionRelative(debug2.getZ()));
/*     */           PoiType.forState(debug3).ifPresent(());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureLoadedAndValid(LevelReader debug1, BlockPos debug2, int debug3) {
/* 218 */     SectionPos.aroundChunk(new ChunkPos(debug2), Math.floorDiv(debug3, 16))
/* 219 */       .map(debug1 -> Pair.of(debug1, getOrLoad(debug1.asLong())))
/* 220 */       .filter(debug0 -> !((Boolean)((Optional)debug0.getSecond()).map(PoiSection::isValid).orElse(Boolean.valueOf(false))).booleanValue())
/* 221 */       .map(debug0 -> ((SectionPos)debug0.getFirst()).chunk())
/* 222 */       .filter(debug1 -> this.loadedChunks.add(debug1.toLong()))
/* 223 */       .forEach(debug1 -> debug0.getChunk(debug1.x, debug1.z, ChunkStatus.EMPTY));
/*     */   }
/*     */   
/*     */   public enum Occupancy {
/* 227 */     HAS_SPACE((String)PoiRecord::hasSpace),
/* 228 */     IS_OCCUPIED((String)PoiRecord::isOccupied),
/* 229 */     ANY((String)(debug0 -> true));
/*     */     
/*     */     private final Predicate<? super PoiRecord> test;
/*     */     
/*     */     Occupancy(Predicate<? super PoiRecord> debug3) {
/* 234 */       this.test = debug3;
/*     */     }
/*     */     
/*     */     public Predicate<? super PoiRecord> getTest() {
/* 238 */       return this.test;
/*     */     }
/*     */   }
/*     */   
/*     */   final class DistanceTracker extends SectionTracker {
/*     */     private final Long2ByteMap levels;
/*     */     
/*     */     protected DistanceTracker() {
/* 246 */       super(7, 16, 256);
/* 247 */       this.levels = (Long2ByteMap)new Long2ByteOpenHashMap();
/* 248 */       this.levels.defaultReturnValue((byte)7);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getLevelFromSource(long debug1) {
/* 253 */       return PoiManager.this.isVillageCenter(debug1) ? 0 : 7;
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getLevel(long debug1) {
/* 258 */       return this.levels.get(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void setLevel(long debug1, int debug3) {
/* 263 */       if (debug3 > 6) {
/* 264 */         this.levels.remove(debug1);
/*     */       } else {
/* 266 */         this.levels.put(debug1, (byte)debug3);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void runAllUpdates() {
/* 271 */       runUpdates(2147483647);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\village\poi\PoiManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */