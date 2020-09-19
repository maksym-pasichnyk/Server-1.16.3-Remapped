/*     */ package net.minecraft.world.entity.ai.village.poi;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.entity.npc.VillagerProfession;
/*     */ import net.minecraft.world.level.block.BedBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BedPart;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class PoiType {
/*  26 */   private static final Supplier<Set<PoiType>> ALL_JOB_POI_TYPES = (Supplier<Set<PoiType>>)Suppliers.memoize(() -> (Set)Registry.VILLAGER_PROFESSION.stream().map(VillagerProfession::getJobPoiType).collect(Collectors.toSet())); static {
/*  27 */     ALL_JOBS = (debug0 -> ((Set)ALL_JOB_POI_TYPES.get()).contains(debug0));
/*     */   }
/*     */   
/*     */   public static final Predicate<PoiType> ALL_JOBS;
/*     */   public static final Predicate<PoiType> ALL = debug0 -> true;
/*     */   private static final Set<BlockState> BEDS;
/*     */   
/*     */   static {
/*  35 */     BEDS = (Set<BlockState>)ImmutableList.of(Blocks.RED_BED, Blocks.BLACK_BED, Blocks.BLUE_BED, Blocks.BROWN_BED, Blocks.CYAN_BED, Blocks.GRAY_BED, Blocks.GREEN_BED, Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_GRAY_BED, Blocks.LIME_BED, Blocks.MAGENTA_BED, Blocks.ORANGE_BED, (Object[])new Block[] { Blocks.PINK_BED, Blocks.PURPLE_BED, Blocks.WHITE_BED, Blocks.YELLOW_BED }).stream().flatMap(debug0 -> debug0.getStateDefinition().getPossibleStates().stream()).filter(debug0 -> (debug0.getValue((Property)BedBlock.PART) == BedPart.HEAD)).collect(ImmutableSet.toImmutableSet());
/*     */   }
/*  37 */   private static final Map<BlockState, PoiType> TYPE_BY_STATE = Maps.newHashMap();
/*     */ 
/*     */   
/*  40 */   public static final PoiType UNEMPLOYED = register("unemployed", (Set<BlockState>)ImmutableSet.of(), 1, ALL_JOBS, 1);
/*  41 */   public static final PoiType ARMORER = register("armorer", getBlockStates(Blocks.BLAST_FURNACE), 1, 1);
/*  42 */   public static final PoiType BUTCHER = register("butcher", getBlockStates(Blocks.SMOKER), 1, 1);
/*  43 */   public static final PoiType CARTOGRAPHER = register("cartographer", getBlockStates(Blocks.CARTOGRAPHY_TABLE), 1, 1);
/*  44 */   public static final PoiType CLERIC = register("cleric", getBlockStates(Blocks.BREWING_STAND), 1, 1);
/*  45 */   public static final PoiType FARMER = register("farmer", getBlockStates(Blocks.COMPOSTER), 1, 1);
/*  46 */   public static final PoiType FISHERMAN = register("fisherman", getBlockStates(Blocks.BARREL), 1, 1);
/*  47 */   public static final PoiType FLETCHER = register("fletcher", getBlockStates(Blocks.FLETCHING_TABLE), 1, 1);
/*  48 */   public static final PoiType LEATHERWORKER = register("leatherworker", getBlockStates(Blocks.CAULDRON), 1, 1);
/*  49 */   public static final PoiType LIBRARIAN = register("librarian", getBlockStates(Blocks.LECTERN), 1, 1);
/*  50 */   public static final PoiType MASON = register("mason", getBlockStates(Blocks.STONECUTTER), 1, 1);
/*  51 */   public static final PoiType NITWIT = register("nitwit", (Set<BlockState>)ImmutableSet.of(), 1, 1);
/*  52 */   public static final PoiType SHEPHERD = register("shepherd", getBlockStates(Blocks.LOOM), 1, 1);
/*  53 */   public static final PoiType TOOLSMITH = register("toolsmith", getBlockStates(Blocks.SMITHING_TABLE), 1, 1);
/*  54 */   public static final PoiType WEAPONSMITH = register("weaponsmith", getBlockStates(Blocks.GRINDSTONE), 1, 1);
/*  55 */   public static final PoiType HOME = register("home", BEDS, 1, 1);
/*  56 */   public static final PoiType MEETING = register("meeting", getBlockStates(Blocks.BELL), 32, 6);
/*  57 */   public static final PoiType BEEHIVE = register("beehive", getBlockStates(Blocks.BEEHIVE), 0, 1);
/*  58 */   public static final PoiType BEE_NEST = register("bee_nest", getBlockStates(Blocks.BEE_NEST), 0, 1);
/*  59 */   public static final PoiType NETHER_PORTAL = register("nether_portal", getBlockStates(Blocks.NETHER_PORTAL), 0, 1);
/*  60 */   public static final PoiType LODESTONE = register("lodestone", getBlockStates(Blocks.LODESTONE), 0, 1);
/*     */   
/*  62 */   protected static final Set<BlockState> ALL_STATES = (Set<BlockState>)new ObjectOpenHashSet(TYPE_BY_STATE.keySet());
/*     */   
/*     */   private final String name;
/*     */   private final Set<BlockState> matchingStates;
/*     */   private final int maxTickets;
/*     */   private final Predicate<PoiType> predicate;
/*     */   private final int validRange;
/*     */   
/*     */   private static Set<BlockState> getBlockStates(Block debug0) {
/*  71 */     return (Set<BlockState>)ImmutableSet.copyOf((Collection)debug0.getStateDefinition().getPossibleStates());
/*     */   }
/*     */   
/*     */   private PoiType(String debug1, Set<BlockState> debug2, int debug3, Predicate<PoiType> debug4, int debug5) {
/*  75 */     this.name = debug1;
/*  76 */     this.matchingStates = (Set<BlockState>)ImmutableSet.copyOf(debug2);
/*  77 */     this.maxTickets = debug3;
/*  78 */     this.predicate = debug4;
/*  79 */     this.validRange = debug5;
/*     */   }
/*     */   
/*     */   private PoiType(String debug1, Set<BlockState> debug2, int debug3, int debug4) {
/*  83 */     this.name = debug1;
/*  84 */     this.matchingStates = (Set<BlockState>)ImmutableSet.copyOf(debug2);
/*  85 */     this.maxTickets = debug3;
/*  86 */     this.predicate = (debug1 -> (debug1 == this));
/*  87 */     this.validRange = debug4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxTickets() {
/*  95 */     return this.maxTickets;
/*     */   }
/*     */   
/*     */   public Predicate<PoiType> getPredicate() {
/*  99 */     return this.predicate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValidRange() {
/* 107 */     return this.validRange;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 112 */     return this.name;
/*     */   }
/*     */   
/*     */   private static PoiType register(String debug0, Set<BlockState> debug1, int debug2, int debug3) {
/* 116 */     return registerBlockStates((PoiType)Registry.register((Registry)Registry.POINT_OF_INTEREST_TYPE, new ResourceLocation(debug0), new PoiType(debug0, debug1, debug2, debug3)));
/*     */   }
/*     */   
/*     */   private static PoiType register(String debug0, Set<BlockState> debug1, int debug2, Predicate<PoiType> debug3, int debug4) {
/* 120 */     return registerBlockStates((PoiType)Registry.register((Registry)Registry.POINT_OF_INTEREST_TYPE, new ResourceLocation(debug0), new PoiType(debug0, debug1, debug2, debug3, debug4)));
/*     */   }
/*     */   
/*     */   private static PoiType registerBlockStates(PoiType debug0) {
/* 124 */     debug0.matchingStates.forEach(debug1 -> {
/*     */           PoiType debug2 = TYPE_BY_STATE.put(debug1, debug0);
/*     */           
/*     */           if (debug2 != null) {
/*     */             throw (IllegalStateException)Util.pauseInIde(new IllegalStateException(String.format("%s is defined in too many tags", new Object[] { debug1 })));
/*     */           }
/*     */         });
/* 131 */     return debug0;
/*     */   }
/*     */   
/*     */   public static Optional<PoiType> forState(BlockState debug0) {
/* 135 */     return Optional.ofNullable(TYPE_BY_STATE.get(debug0));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\village\poi\PoiType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */