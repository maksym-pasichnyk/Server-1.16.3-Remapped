/*     */ package net.minecraft.world.entity.ai.gossip;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.SerializableUUID;
/*     */ 
/*     */ 
/*     */ public class GossipContainer
/*     */ {
/*     */   static class GossipEntry
/*     */   {
/*     */     public final UUID target;
/*     */     public final GossipType type;
/*     */     public final int value;
/*     */     
/*     */     public GossipEntry(UUID debug1, GossipType debug2, int debug3) {
/*  43 */       this.target = debug1;
/*  44 */       this.type = debug2;
/*  45 */       this.value = debug3;
/*     */     }
/*     */     
/*     */     public int weightedValue() {
/*  49 */       return this.value * this.type.weight;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  54 */       return "GossipEntry{target=" + this.target + ", type=" + this.type + ", value=" + this.value + '}';
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> Dynamic<T> store(DynamicOps<T> debug1) {
/*  62 */       return new Dynamic(debug1, debug1.createMap((Map)ImmutableMap.of(debug1
/*  63 */               .createString("Target"), SerializableUUID.CODEC.encodeStart(debug1, this.target).result().orElseThrow(RuntimeException::new), debug1
/*  64 */               .createString("Type"), debug1.createString(this.type.id), debug1
/*  65 */               .createString("Value"), debug1.createInt(this.value))));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public static DataResult<GossipEntry> load(Dynamic<?> debug0) {
/*  71 */       return DataResult.unbox(DataResult.instance().group((App)debug0
/*  72 */             .get("Target").read((Decoder)SerializableUUID.CODEC), (App)debug0
/*  73 */             .get("Type").asString().map(GossipType::byId), (App)debug0
/*  74 */             .get("Value").asNumber().map(Number::intValue))
/*  75 */           .apply((Applicative)DataResult.instance(), GossipEntry::new));
/*     */     }
/*     */   }
/*     */   
/*     */   static class EntityGossips {
/*  80 */     private final Object2IntMap<GossipType> entries = (Object2IntMap<GossipType>)new Object2IntOpenHashMap();
/*     */     
/*     */     public int weightedValue(Predicate<GossipType> debug1) {
/*  83 */       return this.entries.object2IntEntrySet()
/*  84 */         .stream()
/*  85 */         .filter(debug1 -> debug0.test(debug1.getKey()))
/*  86 */         .mapToInt(debug0 -> debug0.getIntValue() * ((GossipType)debug0.getKey()).weight)
/*  87 */         .sum();
/*     */     }
/*     */     
/*     */     public Stream<GossipContainer.GossipEntry> unpack(UUID debug1) {
/*  91 */       return this.entries.object2IntEntrySet().stream().map(debug1 -> new GossipContainer.GossipEntry(debug0, (GossipType)debug1.getKey(), debug1.getIntValue()));
/*     */     }
/*     */     
/*     */     public void decay() {
/*  95 */       ObjectIterator<Object2IntMap.Entry<GossipType>> debug1 = this.entries.object2IntEntrySet().iterator();
/*  96 */       while (debug1.hasNext()) {
/*  97 */         Object2IntMap.Entry<GossipType> debug2 = (Object2IntMap.Entry<GossipType>)debug1.next();
/*  98 */         int debug3 = debug2.getIntValue() - ((GossipType)debug2.getKey()).decayPerDay;
/*  99 */         if (debug3 < 2) {
/* 100 */           debug1.remove(); continue;
/*     */         } 
/* 102 */         debug2.setValue(debug3);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 108 */       return this.entries.isEmpty();
/*     */     }
/*     */     
/*     */     public void makeSureValueIsntTooLowOrTooHigh(GossipType debug1) {
/* 112 */       int debug2 = this.entries.getInt(debug1);
/* 113 */       if (debug2 > debug1.max) {
/* 114 */         this.entries.put(debug1, debug1.max);
/*     */       }
/* 116 */       if (debug2 < 2) {
/* 117 */         remove(debug1);
/*     */       }
/*     */     }
/*     */     
/*     */     public void remove(GossipType debug1) {
/* 122 */       this.entries.removeInt(debug1);
/*     */     }
/*     */     
/*     */     private EntityGossips() {} }
/* 126 */   private final Map<UUID, EntityGossips> gossips = Maps.newHashMap();
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
/*     */   public void decay() {
/* 139 */     Iterator<EntityGossips> debug1 = this.gossips.values().iterator();
/* 140 */     while (debug1.hasNext()) {
/* 141 */       EntityGossips debug2 = debug1.next();
/* 142 */       debug2.decay();
/* 143 */       if (debug2.isEmpty())
/*     */       {
/* 145 */         debug1.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private Stream<GossipEntry> unpack() {
/* 151 */     return this.gossips.entrySet().stream().flatMap(debug0 -> ((EntityGossips)debug0.getValue()).unpack((UUID)debug0.getKey()));
/*     */   }
/*     */   
/*     */   private Collection<GossipEntry> selectGossipsForTransfer(Random debug1, int debug2) {
/* 155 */     List<GossipEntry> debug3 = unpack().collect((Collector)Collectors.toList());
/* 156 */     if (debug3.isEmpty()) {
/* 157 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 160 */     int[] debug4 = new int[debug3.size()];
/* 161 */     int debug5 = 0;
/* 162 */     for (int i = 0; i < debug3.size(); i++) {
/* 163 */       GossipEntry gossipEntry = debug3.get(i);
/* 164 */       debug5 += Math.abs(gossipEntry.weightedValue());
/* 165 */       debug4[i] = debug5 - 1;
/*     */     } 
/*     */     
/* 168 */     Set<GossipEntry> debug6 = Sets.newIdentityHashSet();
/* 169 */     for (int debug7 = 0; debug7 < debug2; debug7++) {
/* 170 */       int debug8 = debug1.nextInt(debug5);
/* 171 */       int debug9 = Arrays.binarySearch(debug4, debug8);
/* 172 */       debug6.add(debug3.get((debug9 < 0) ? (-debug9 - 1) : debug9));
/*     */     } 
/* 174 */     return debug6;
/*     */   }
/*     */   
/*     */   private EntityGossips getOrCreate(UUID debug1) {
/* 178 */     return this.gossips.computeIfAbsent(debug1, debug0 -> new EntityGossips());
/*     */   }
/*     */   
/*     */   public void transferFrom(GossipContainer debug1, Random debug2, int debug3) {
/* 182 */     Collection<GossipEntry> debug4 = debug1.selectGossipsForTransfer(debug2, debug3);
/*     */     
/* 184 */     debug4.forEach(debug1 -> {
/*     */           int debug2 = debug1.value - debug1.type.decayPerTransfer;
/*     */           if (debug2 >= 2) {
/*     */             (getOrCreate(debug1.target)).entries.mergeInt(debug1.type, debug2, GossipContainer::mergeValuesForTransfer);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getReputation(UUID debug1, Predicate<GossipType> debug2) {
/* 197 */     EntityGossips debug3 = this.gossips.get(debug1);
/* 198 */     return (debug3 != null) ? debug3.weightedValue(debug2) : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(UUID debug1, GossipType debug2, int debug3) {
/* 209 */     EntityGossips debug4 = getOrCreate(debug1);
/* 210 */     debug4.entries.mergeInt(debug2, debug3, (debug2, debug3) -> Integer.valueOf(mergeValuesForAddition(debug1, debug2.intValue(), debug3.intValue())));
/* 211 */     debug4.makeSureValueIsntTooLowOrTooHigh(debug2);
/* 212 */     if (debug4.isEmpty()) {
/* 213 */       this.gossips.remove(debug1);
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
/*     */   public <T> Dynamic<T> store(DynamicOps<T> debug1) {
/* 247 */     return new Dynamic(debug1, debug1.createList(unpack().map(debug1 -> debug1.store(debug0)).map(Dynamic::getValue)));
/*     */   }
/*     */   
/*     */   public void update(Dynamic<?> debug1) {
/* 251 */     debug1.asStream()
/* 252 */       .map(GossipEntry::load)
/* 253 */       .flatMap(debug0 -> Util.toStream(debug0.result()))
/* 254 */       .forEach(debug1 -> (getOrCreate(debug1.target)).entries.put(debug1.type, debug1.value));
/*     */   }
/*     */   
/*     */   private static int mergeValuesForTransfer(int debug0, int debug1) {
/* 258 */     return Math.max(debug0, debug1);
/*     */   }
/*     */   
/*     */   private int mergeValuesForAddition(GossipType debug1, int debug2, int debug3) {
/* 262 */     int debug4 = debug2 + debug3;
/* 263 */     return (debug4 > debug1.max) ? Math.max(debug1.max, debug2) : debug4;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\gossip\GossipContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */