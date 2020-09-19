/*     */ package net.minecraft.world.level.levelgen.feature.configurations;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.blockplacers.BlockPlacer;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ 
/*     */ public class RandomPatchConfiguration implements FeatureConfiguration {
/*     */   static {
/*  17 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(()), (App)BlockPlacer.CODEC.fieldOf("block_placer").forGetter(()), (App)BlockState.CODEC.listOf().fieldOf("whitelist").forGetter(()), (App)BlockState.CODEC.listOf().fieldOf("blacklist").forGetter(()), (App)Codec.INT.fieldOf("tries").orElse(Integer.valueOf(128)).forGetter(()), (App)Codec.INT.fieldOf("xspread").orElse(Integer.valueOf(7)).forGetter(()), (App)Codec.INT.fieldOf("yspread").orElse(Integer.valueOf(3)).forGetter(()), (App)Codec.INT.fieldOf("zspread").orElse(Integer.valueOf(7)).forGetter(()), (App)Codec.BOOL.fieldOf("can_replace").orElse(Boolean.valueOf(false)).forGetter(()), (App)Codec.BOOL.fieldOf("project").orElse(Boolean.valueOf(true)).forGetter(()), (App)Codec.BOOL.fieldOf("need_water").orElse(Boolean.valueOf(false)).forGetter(())).apply((Applicative)debug0, RandomPatchConfiguration::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Codec<RandomPatchConfiguration> CODEC;
/*     */   
/*     */   public final BlockStateProvider stateProvider;
/*     */   
/*     */   public final BlockPlacer blockPlacer;
/*     */   
/*     */   public final Set<Block> whitelist;
/*     */   
/*     */   public final Set<BlockState> blacklist;
/*     */   
/*     */   public final int tries;
/*     */   
/*     */   public final int xspread;
/*     */   
/*     */   public final int yspread;
/*     */   
/*     */   public final int zspread;
/*     */   
/*     */   public final boolean canReplace;
/*     */   public final boolean project;
/*     */   public final boolean needWater;
/*     */   
/*     */   private RandomPatchConfiguration(BlockStateProvider debug1, BlockPlacer debug2, List<BlockState> debug3, List<BlockState> debug4, int debug5, int debug6, int debug7, int debug8, boolean debug9, boolean debug10, boolean debug11) {
/*  44 */     this(debug1, debug2, (Set<Block>)debug3
/*  45 */         .stream().map(BlockBehaviour.BlockStateBase::getBlock).collect(Collectors.toSet()), 
/*  46 */         (Set<BlockState>)ImmutableSet.copyOf(debug4), debug5, debug6, debug7, debug8, debug9, debug10, debug11);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private RandomPatchConfiguration(BlockStateProvider debug1, BlockPlacer debug2, Set<Block> debug3, Set<BlockState> debug4, int debug5, int debug6, int debug7, int debug8, boolean debug9, boolean debug10, boolean debug11) {
/*  52 */     this.stateProvider = debug1;
/*  53 */     this.blockPlacer = debug2;
/*  54 */     this.whitelist = debug3;
/*  55 */     this.blacklist = debug4;
/*  56 */     this.tries = debug5;
/*  57 */     this.xspread = debug6;
/*  58 */     this.yspread = debug7;
/*  59 */     this.zspread = debug8;
/*  60 */     this.canReplace = debug9;
/*  61 */     this.project = debug10;
/*  62 */     this.needWater = debug11;
/*     */   }
/*     */   
/*     */   public static class GrassConfigurationBuilder {
/*     */     private final BlockStateProvider stateProvider;
/*     */     private final BlockPlacer blockPlacer;
/*  68 */     private Set<Block> whitelist = (Set<Block>)ImmutableSet.of();
/*  69 */     private Set<BlockState> blacklist = (Set<BlockState>)ImmutableSet.of();
/*  70 */     private int tries = 64;
/*  71 */     private int xspread = 7;
/*  72 */     private int yspread = 3;
/*  73 */     private int zspread = 7;
/*     */     private boolean canReplace;
/*     */     private boolean project = true;
/*     */     private boolean needWater = false;
/*     */     
/*     */     public GrassConfigurationBuilder(BlockStateProvider debug1, BlockPlacer debug2) {
/*  79 */       this.stateProvider = debug1;
/*  80 */       this.blockPlacer = debug2;
/*     */     }
/*     */     
/*     */     public GrassConfigurationBuilder whitelist(Set<Block> debug1) {
/*  84 */       this.whitelist = debug1;
/*  85 */       return this;
/*     */     }
/*     */     
/*     */     public GrassConfigurationBuilder blacklist(Set<BlockState> debug1) {
/*  89 */       this.blacklist = debug1;
/*  90 */       return this;
/*     */     }
/*     */     
/*     */     public GrassConfigurationBuilder tries(int debug1) {
/*  94 */       this.tries = debug1;
/*  95 */       return this;
/*     */     }
/*     */     
/*     */     public GrassConfigurationBuilder xspread(int debug1) {
/*  99 */       this.xspread = debug1;
/* 100 */       return this;
/*     */     }
/*     */     
/*     */     public GrassConfigurationBuilder yspread(int debug1) {
/* 104 */       this.yspread = debug1;
/* 105 */       return this;
/*     */     }
/*     */     
/*     */     public GrassConfigurationBuilder zspread(int debug1) {
/* 109 */       this.zspread = debug1;
/* 110 */       return this;
/*     */     }
/*     */     
/*     */     public GrassConfigurationBuilder canReplace() {
/* 114 */       this.canReplace = true;
/* 115 */       return this;
/*     */     }
/*     */     
/*     */     public GrassConfigurationBuilder noProjection() {
/* 119 */       this.project = false;
/* 120 */       return this;
/*     */     }
/*     */     
/*     */     public GrassConfigurationBuilder needWater() {
/* 124 */       this.needWater = true;
/* 125 */       return this;
/*     */     }
/*     */     
/*     */     public RandomPatchConfiguration build() {
/* 129 */       return new RandomPatchConfiguration(this.stateProvider, this.blockPlacer, this.whitelist, this.blacklist, this.tries, this.xspread, this.yspread, this.zspread, this.canReplace, this.project, this.needWater);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\RandomPatchConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */