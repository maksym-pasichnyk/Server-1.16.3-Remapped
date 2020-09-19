/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
/*    */ 
/*    */ public class JigsawConfiguration implements FeatureConfiguration {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(JigsawConfiguration::startPool), (App)Codec.intRange(0, 7).fieldOf("size").forGetter(JigsawConfiguration::maxDepth)).apply((Applicative)debug0, JigsawConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<JigsawConfiguration> CODEC;
/*    */   private final Supplier<StructureTemplatePool> startPool;
/*    */   private final int maxDepth;
/*    */   
/*    */   public JigsawConfiguration(Supplier<StructureTemplatePool> debug1, int debug2) {
/* 19 */     this.startPool = debug1;
/* 20 */     this.maxDepth = debug2;
/*    */   }
/*    */   
/*    */   public int maxDepth() {
/* 24 */     return this.maxDepth;
/*    */   }
/*    */   
/*    */   public Supplier<StructureTemplatePool> startPool() {
/* 28 */     return this.startPool;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\JigsawConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */