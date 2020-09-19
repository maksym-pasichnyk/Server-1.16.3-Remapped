/*     */ package net.minecraft.server.packs.repository;
/*     */ 
/*     */ import com.google.common.base.Functions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ 
/*     */ public class PackRepository implements AutoCloseable {
/*  21 */   private Map<String, Pack> available = (Map<String, Pack>)ImmutableMap.of(); private final Set<RepositorySource> sources;
/*  22 */   private List<Pack> selected = (List<Pack>)ImmutableList.of();
/*     */   private final Pack.PackConstructor constructor;
/*     */   
/*     */   public PackRepository(Pack.PackConstructor debug1, RepositorySource... debug2) {
/*  26 */     this.constructor = debug1;
/*  27 */     this.sources = (Set<RepositorySource>)ImmutableSet.copyOf((Object[])debug2);
/*     */   }
/*     */   
/*     */   public PackRepository(RepositorySource... debug1) {
/*  31 */     this(Pack::new, debug1);
/*     */   }
/*     */   
/*     */   public void reload() {
/*  35 */     List<String> debug1 = (List<String>)this.selected.stream().map(Pack::getId).collect(ImmutableList.toImmutableList());
/*  36 */     close();
/*  37 */     this.available = discoverAvailable();
/*  38 */     this.selected = rebuildSelected(debug1);
/*     */   }
/*     */   
/*     */   private Map<String, Pack> discoverAvailable() {
/*  42 */     Map<String, Pack> debug1 = Maps.newTreeMap();
/*  43 */     for (RepositorySource debug3 : this.sources) {
/*  44 */       debug3.loadPacks(debug1 -> (Pack)debug0.put(debug1.getId(), debug1), this.constructor);
/*     */     }
/*  46 */     return (Map<String, Pack>)ImmutableMap.copyOf(debug1);
/*     */   }
/*     */   
/*     */   public void setSelected(Collection<String> debug1) {
/*  50 */     this.selected = rebuildSelected(debug1);
/*     */   }
/*     */   
/*     */   private List<Pack> rebuildSelected(Collection<String> debug1) {
/*  54 */     List<Pack> debug2 = getAvailablePacks(debug1).collect((Collector)Collectors.toList());
/*     */     
/*  56 */     for (Pack debug4 : this.available.values()) {
/*     */       
/*  58 */       if (debug4.isRequired() && !debug2.contains(debug4)) {
/*  59 */         debug4.getDefaultPosition().insert(debug2, debug4, (Function<Pack, Pack>)Functions.identity(), false);
/*     */       }
/*     */     } 
/*  62 */     return (List<Pack>)ImmutableList.copyOf(debug2);
/*     */   }
/*     */   
/*     */   private Stream<Pack> getAvailablePacks(Collection<String> debug1) {
/*  66 */     return debug1.stream().map(this.available::get).filter(Objects::nonNull);
/*     */   }
/*     */   
/*     */   public Collection<String> getAvailableIds() {
/*  70 */     return this.available.keySet();
/*     */   }
/*     */   
/*     */   public Collection<Pack> getAvailablePacks() {
/*  74 */     return this.available.values();
/*     */   }
/*     */   
/*     */   public Collection<String> getSelectedIds() {
/*  78 */     return (Collection<String>)this.selected.stream().map(Pack::getId).collect(ImmutableSet.toImmutableSet());
/*     */   }
/*     */   
/*     */   public Collection<Pack> getSelectedPacks() {
/*  82 */     return this.selected;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Pack getPack(String debug1) {
/*  87 */     return this.available.get(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  92 */     this.available.values().forEach(Pack::close);
/*     */   }
/*     */   
/*     */   public boolean isAvailable(String debug1) {
/*  96 */     return this.available.containsKey(debug1);
/*     */   }
/*     */   
/*     */   public List<PackResources> openAllSelected() {
/* 100 */     return (List<PackResources>)this.selected.stream().map(Pack::open).collect(ImmutableList.toImmutableList());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\repository\PackRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */