/*    */ package net.minecraft.world.level.levelgen.feature.structures;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ 
/*    */ public class LegacySinglePoolElement extends SinglePoolElement {
/*    */   static {
/* 21 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)templateCodec(), (App)processorsCodec(), (App)projectionCodec()).apply((Applicative)debug0, LegacySinglePoolElement::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<LegacySinglePoolElement> CODEC;
/*    */   
/*    */   protected LegacySinglePoolElement(Either<ResourceLocation, StructureTemplate> debug1, Supplier<StructureProcessorList> debug2, StructureTemplatePool.Projection debug3) {
/* 28 */     super(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructurePlaceSettings getSettings(Rotation debug1, BoundingBox debug2, boolean debug3) {
/* 33 */     StructurePlaceSettings debug4 = super.getSettings(debug1, debug2, debug3);
/* 34 */     debug4.popProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/* 35 */     debug4.addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/* 36 */     return debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   public StructurePoolElementType<?> getType() {
/* 41 */     return StructurePoolElementType.LEGACY;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 46 */     return "LegacySingle[" + this.template + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\structures\LegacySinglePoolElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */