/*     */ package net.minecraft.world.entity.ai.village.poi;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.util.Supplier;
/*     */ 
/*     */ public class PoiSection {
/*  27 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Codec<PoiSection> codec(Runnable debug0) {
/*  34 */     return RecordCodecBuilder.create(debug1 -> debug1.group((App)RecordCodecBuilder.point(debug0), (App)Codec.BOOL.optionalFieldOf("Valid", Boolean.valueOf(false)).forGetter(()), (App)PoiRecord.codec(debug0).listOf().fieldOf("Records").forGetter(())).apply((Applicative)debug1, PoiSection::new))
/*     */ 
/*     */ 
/*     */       
/*  38 */       .orElseGet(Util.prefix("Failed to read POI section: ", LOGGER::error), () -> new PoiSection(debug0, false, (List<PoiRecord>)ImmutableList.of()));
/*     */   }
/*     */   
/*  41 */   private final Short2ObjectMap<PoiRecord> records = (Short2ObjectMap<PoiRecord>)new Short2ObjectOpenHashMap();
/*  42 */   private final Map<PoiType, Set<PoiRecord>> byType = Maps.newHashMap();
/*     */   private final Runnable setDirty;
/*     */   private boolean isValid;
/*     */   
/*     */   public PoiSection(Runnable debug1) {
/*  47 */     this(debug1, true, (List<PoiRecord>)ImmutableList.of());
/*     */   }
/*     */   
/*     */   private PoiSection(Runnable debug1, boolean debug2, List<PoiRecord> debug3) {
/*  51 */     this.setDirty = debug1;
/*  52 */     this.isValid = debug2;
/*  53 */     debug3.forEach(this::add);
/*     */   }
/*     */   
/*     */   public Stream<PoiRecord> getRecords(Predicate<PoiType> debug1, PoiManager.Occupancy debug2) {
/*  57 */     return this.byType.entrySet()
/*  58 */       .stream()
/*  59 */       .filter(debug1 -> debug0.test(debug1.getKey()))
/*  60 */       .flatMap(debug0 -> ((Set)debug0.getValue()).stream())
/*  61 */       .filter(debug2.getTest());
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(BlockPos debug1, PoiType debug2) {
/*  66 */     if (add(new PoiRecord(debug1, debug2, this.setDirty))) {
/*  67 */       LOGGER.debug("Added POI of type {} @ {}", new Supplier[] { () -> debug0, () -> debug0 });
/*  68 */       this.setDirty.run();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean add(PoiRecord debug1) {
/*  73 */     BlockPos debug2 = debug1.getPos();
/*  74 */     PoiType debug3 = debug1.getPoiType();
/*  75 */     short debug4 = SectionPos.sectionRelativePos(debug2);
/*  76 */     PoiRecord debug5 = (PoiRecord)this.records.get(debug4);
/*     */     
/*  78 */     if (debug5 != null) {
/*  79 */       if (debug3.equals(debug5.getPoiType())) {
/*  80 */         return false;
/*     */       }
/*  82 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("POI data mismatch: already registered at " + debug2));
/*     */     } 
/*     */ 
/*     */     
/*  86 */     this.records.put(debug4, debug1);
/*  87 */     ((Set<PoiRecord>)this.byType.computeIfAbsent(debug3, debug0 -> Sets.newHashSet())).add(debug1);
/*  88 */     return true;
/*     */   }
/*     */   
/*     */   public void remove(BlockPos debug1) {
/*  92 */     PoiRecord debug2 = (PoiRecord)this.records.remove(SectionPos.sectionRelativePos(debug1));
/*  93 */     if (debug2 == null) {
/*  94 */       LOGGER.error("POI data mismatch: never registered at " + debug1);
/*     */       return;
/*     */     } 
/*  97 */     ((Set)this.byType.get(debug2.getPoiType())).remove(debug2);
/*     */     
/*  99 */     LOGGER.debug("Removed POI of type {} @ {}", new Supplier[] { debug2::getPoiType, debug2::getPos });
/* 100 */     this.setDirty.run();
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
/*     */   public boolean release(BlockPos debug1) {
/* 112 */     PoiRecord debug2 = (PoiRecord)this.records.get(SectionPos.sectionRelativePos(debug1));
/* 113 */     if (debug2 == null) {
/* 114 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("POI never registered at " + debug1));
/*     */     }
/* 116 */     boolean debug3 = debug2.releaseTicket();
/* 117 */     this.setDirty.run();
/* 118 */     return debug3;
/*     */   }
/*     */   
/*     */   public boolean exists(BlockPos debug1, Predicate<PoiType> debug2) {
/* 122 */     short debug3 = SectionPos.sectionRelativePos(debug1);
/* 123 */     PoiRecord debug4 = (PoiRecord)this.records.get(debug3);
/* 124 */     return (debug4 != null && debug2.test(debug4.getPoiType()));
/*     */   }
/*     */   
/*     */   public Optional<PoiType> getType(BlockPos debug1) {
/* 128 */     short debug2 = SectionPos.sectionRelativePos(debug1);
/* 129 */     PoiRecord debug3 = (PoiRecord)this.records.get(debug2);
/* 130 */     return (debug3 != null) ? Optional.<PoiType>of(debug3.getPoiType()) : Optional.<PoiType>empty();
/*     */   }
/*     */   
/*     */   public void refresh(Consumer<BiConsumer<BlockPos, PoiType>> debug1) {
/* 134 */     if (!this.isValid) {
/* 135 */       Short2ObjectOpenHashMap short2ObjectOpenHashMap = new Short2ObjectOpenHashMap(this.records);
/* 136 */       clear();
/* 137 */       debug1.accept((debug2, debug3) -> {
/*     */             short debug4 = SectionPos.sectionRelativePos(debug2);
/*     */             PoiRecord debug5 = (PoiRecord)debug1.computeIfAbsent(debug4, ());
/*     */             add(debug5);
/*     */           });
/* 142 */       this.isValid = true;
/* 143 */       this.setDirty.run();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void clear() {
/* 148 */     this.records.clear();
/* 149 */     this.byType.clear();
/*     */   }
/*     */   
/*     */   boolean isValid() {
/* 153 */     return this.isValid;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\village\poi\PoiSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */