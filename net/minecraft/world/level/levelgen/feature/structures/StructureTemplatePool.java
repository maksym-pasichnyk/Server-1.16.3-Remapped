/*     */ package net.minecraft.world.level.levelgen.feature.structures;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrays;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.GravityProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class StructureTemplatePool {
/*  32 */   private static final Logger LOGGER = LogManager.getLogger(); public static final Codec<StructureTemplatePool> DIRECT_CODEC;
/*     */   static {
/*  34 */     DIRECT_CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)ResourceLocation.CODEC.fieldOf("name").forGetter(StructureTemplatePool::getName), (App)ResourceLocation.CODEC.fieldOf("fallback").forGetter(StructureTemplatePool::getFallback), (App)Codec.mapPair(StructurePoolElement.CODEC.fieldOf("element"), Codec.INT.fieldOf("weight")).codec().listOf().promotePartial(Util.prefix("Pool element: ", LOGGER::error)).fieldOf("elements").forGetter(())).apply((Applicative)debug0, StructureTemplatePool::new));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final ResourceLocation name;
/*     */   
/*     */   private final List<Pair<StructurePoolElement, Integer>> rawTemplates;
/*     */   
/*  43 */   public static final Codec<Supplier<StructureTemplatePool>> CODEC = (Codec<Supplier<StructureTemplatePool>>)RegistryFileCodec.create(Registry.TEMPLATE_POOL_REGISTRY, DIRECT_CODEC); private final List<StructurePoolElement> templates;
/*     */   private final ResourceLocation fallback;
/*     */   
/*  46 */   public enum Projection implements StringRepresentable { TERRAIN_MATCHING("terrain_matching", 
/*     */       
/*  48 */       ImmutableList.of(new GravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -1))),
/*     */     
/*  50 */     RIGID("rigid", 
/*     */       
/*  52 */       ImmutableList.of());
/*     */ 
/*     */     
/*  55 */     public static final Codec<Projection> CODEC = StringRepresentable.fromEnum(Projection::values, Projection::byName); private static final Map<String, Projection> BY_NAME;
/*     */     static {
/*  57 */       BY_NAME = (Map<String, Projection>)Arrays.<Projection>stream(values()).collect(Collectors.toMap(Projection::getName, debug0 -> debug0));
/*     */     }
/*     */     private final String name; private final ImmutableList<StructureProcessor> processors;
/*     */     
/*     */     Projection(String debug3, ImmutableList<StructureProcessor> debug4) {
/*  62 */       this.name = debug3;
/*  63 */       this.processors = debug4;
/*     */     }
/*     */     
/*     */     public String getName() {
/*  67 */       return this.name;
/*     */     }
/*     */     
/*     */     public static Projection byName(String debug0) {
/*  71 */       return BY_NAME.get(debug0);
/*     */     }
/*     */     
/*     */     public ImmutableList<StructureProcessor> getProcessors() {
/*  75 */       return this.processors;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/*  80 */       return this.name;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   private int maxSize = Integer.MIN_VALUE;
/*     */   
/*     */   public StructureTemplatePool(ResourceLocation debug1, ResourceLocation debug2, List<Pair<StructurePoolElement, Integer>> debug3) {
/*  91 */     this.name = debug1;
/*  92 */     this.rawTemplates = debug3;
/*  93 */     this.templates = Lists.newArrayList();
/*  94 */     for (Pair<StructurePoolElement, Integer> debug5 : debug3) {
/*  95 */       StructurePoolElement debug6 = (StructurePoolElement)debug5.getFirst();
/*  96 */       for (int debug7 = 0; debug7 < ((Integer)debug5.getSecond()).intValue(); debug7++) {
/*  97 */         this.templates.add(debug6);
/*     */       }
/*     */     } 
/*     */     
/* 101 */     this.fallback = debug2;
/*     */   }
/*     */   
/*     */   public StructureTemplatePool(ResourceLocation debug1, ResourceLocation debug2, List<Pair<Function<Projection, ? extends StructurePoolElement>, Integer>> debug3, Projection debug4) {
/* 105 */     this.name = debug1;
/* 106 */     this.rawTemplates = Lists.newArrayList();
/* 107 */     this.templates = Lists.newArrayList();
/* 108 */     for (Pair<Function<Projection, ? extends StructurePoolElement>, Integer> debug6 : debug3) {
/* 109 */       StructurePoolElement debug7 = ((Function<Projection, StructurePoolElement>)debug6.getFirst()).apply(debug4);
/* 110 */       this.rawTemplates.add(Pair.of(debug7, debug6.getSecond()));
/* 111 */       for (int debug8 = 0; debug8 < ((Integer)debug6.getSecond()).intValue(); debug8++) {
/* 112 */         this.templates.add(debug7);
/*     */       }
/*     */     } 
/*     */     
/* 116 */     this.fallback = debug2;
/*     */   }
/*     */   
/*     */   public int getMaxSize(StructureManager debug1) {
/* 120 */     if (this.maxSize == Integer.MIN_VALUE) {
/* 121 */       this.maxSize = this.templates.stream().mapToInt(debug1 -> debug1.getBoundingBox(debug0, BlockPos.ZERO, Rotation.NONE).getYSpan()).max().orElse(0);
/*     */     }
/* 123 */     return this.maxSize;
/*     */   }
/*     */   
/*     */   public ResourceLocation getFallback() {
/* 127 */     return this.fallback;
/*     */   }
/*     */   
/*     */   public StructurePoolElement getRandomTemplate(Random debug1) {
/* 131 */     return this.templates.get(debug1.nextInt(this.templates.size()));
/*     */   }
/*     */   
/*     */   public List<StructurePoolElement> getShuffledTemplates(Random debug1) {
/* 135 */     return (List<StructurePoolElement>)ImmutableList.copyOf(ObjectArrays.shuffle(this.templates.toArray((Object[])new StructurePoolElement[0]), debug1));
/*     */   }
/*     */   
/*     */   public ResourceLocation getName() {
/* 139 */     return this.name;
/*     */   }
/*     */   
/*     */   public int size() {
/* 143 */     return this.templates.size();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\structures\StructureTemplatePool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */