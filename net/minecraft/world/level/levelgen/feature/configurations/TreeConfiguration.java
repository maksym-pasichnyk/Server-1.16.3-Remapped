/*     */ package net.minecraft.world.level.levelgen.feature.configurations;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize;
/*     */ import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*     */ import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
/*     */ 
/*     */ public class TreeConfiguration implements FeatureConfiguration {
/*     */   static {
/*  16 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(()), (App)BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter(()), (App)FoliagePlacer.CODEC.fieldOf("foliage_placer").forGetter(()), (App)TrunkPlacer.CODEC.fieldOf("trunk_placer").forGetter(()), (App)FeatureSize.CODEC.fieldOf("minimum_size").forGetter(()), (App)TreeDecorator.CODEC.listOf().fieldOf("decorators").forGetter(()), (App)Codec.INT.fieldOf("max_water_depth").orElse(Integer.valueOf(0)).forGetter(()), (App)Codec.BOOL.fieldOf("ignore_vines").orElse(Boolean.valueOf(false)).forGetter(()), (App)Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(())).apply((Applicative)debug0, TreeConfiguration::new));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Codec<TreeConfiguration> CODEC;
/*     */ 
/*     */   
/*     */   public final BlockStateProvider trunkProvider;
/*     */ 
/*     */   
/*     */   public final BlockStateProvider leavesProvider;
/*     */ 
/*     */   
/*     */   public final List<TreeDecorator> decorators;
/*     */ 
/*     */   
/*     */   public transient boolean fromSapling;
/*     */   
/*     */   public final FoliagePlacer foliagePlacer;
/*     */   
/*     */   public final TrunkPlacer trunkPlacer;
/*     */   
/*     */   public final FeatureSize minimumSize;
/*     */   
/*     */   public final int maxWaterDepth;
/*     */   
/*     */   public final boolean ignoreVines;
/*     */   
/*     */   public final Heightmap.Types heightmap;
/*     */ 
/*     */   
/*     */   protected TreeConfiguration(BlockStateProvider debug1, BlockStateProvider debug2, FoliagePlacer debug3, TrunkPlacer debug4, FeatureSize debug5, List<TreeDecorator> debug6, int debug7, boolean debug8, Heightmap.Types debug9) {
/*  49 */     this.trunkProvider = debug1;
/*  50 */     this.leavesProvider = debug2;
/*  51 */     this.decorators = debug6;
/*  52 */     this.foliagePlacer = debug3;
/*  53 */     this.minimumSize = debug5;
/*  54 */     this.trunkPlacer = debug4;
/*  55 */     this.maxWaterDepth = debug7;
/*  56 */     this.ignoreVines = debug8;
/*  57 */     this.heightmap = debug9;
/*     */   }
/*     */   
/*     */   public void setFromSapling() {
/*  61 */     this.fromSapling = true;
/*     */   }
/*     */   
/*     */   public TreeConfiguration withDecorators(List<TreeDecorator> debug1) {
/*  65 */     return new TreeConfiguration(this.trunkProvider, this.leavesProvider, this.foliagePlacer, this.trunkPlacer, this.minimumSize, debug1, this.maxWaterDepth, this.ignoreVines, this.heightmap);
/*     */   }
/*     */   
/*     */   public static class TreeConfigurationBuilder {
/*     */     public final BlockStateProvider trunkProvider;
/*     */     public final BlockStateProvider leavesProvider;
/*     */     private final FoliagePlacer foliagePlacer;
/*     */     private final TrunkPlacer trunkPlacer;
/*     */     private final FeatureSize minimumSize;
/*  74 */     private List<TreeDecorator> decorators = (List<TreeDecorator>)ImmutableList.of();
/*     */     private int maxWaterDepth;
/*     */     private boolean ignoreVines;
/*  77 */     private Heightmap.Types heightmap = Heightmap.Types.OCEAN_FLOOR;
/*     */     
/*     */     public TreeConfigurationBuilder(BlockStateProvider debug1, BlockStateProvider debug2, FoliagePlacer debug3, TrunkPlacer debug4, FeatureSize debug5) {
/*  80 */       this.trunkProvider = debug1;
/*  81 */       this.leavesProvider = debug2;
/*  82 */       this.foliagePlacer = debug3;
/*  83 */       this.trunkPlacer = debug4;
/*  84 */       this.minimumSize = debug5;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder decorators(List<TreeDecorator> debug1) {
/*  88 */       this.decorators = debug1;
/*  89 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder maxWaterDepth(int debug1) {
/*  93 */       this.maxWaterDepth = debug1;
/*  94 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder ignoreVines() {
/*  98 */       this.ignoreVines = true;
/*  99 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfigurationBuilder heightmap(Heightmap.Types debug1) {
/* 103 */       this.heightmap = debug1;
/* 104 */       return this;
/*     */     }
/*     */     
/*     */     public TreeConfiguration build() {
/* 108 */       return new TreeConfiguration(this.trunkProvider, this.leavesProvider, this.foliagePlacer, this.trunkPlacer, this.minimumSize, this.decorators, this.maxWaterDepth, this.ignoreVines, this.heightmap);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\TreeConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */