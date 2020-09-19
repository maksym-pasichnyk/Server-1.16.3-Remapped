/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Arrays;
/*    */ import java.util.Map;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Collectors;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.OceanRuinConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ public class OceanRuinFeature extends StructureFeature<OceanRuinConfiguration> {
/*    */   public OceanRuinFeature(Codec<OceanRuinConfiguration> debug1) {
/* 21 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureFeature.StructureStartFactory<OceanRuinConfiguration> getStartFactory() {
/* 26 */     return OceanRuinStart::new;
/*    */   }
/*    */   
/*    */   public static class OceanRuinStart extends StructureStart<OceanRuinConfiguration> {
/*    */     public OceanRuinStart(StructureFeature<OceanRuinConfiguration> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/* 31 */       super(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */     }
/*    */ 
/*    */     
/*    */     public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, OceanRuinConfiguration debug7) {
/* 36 */       int debug8 = debug4 * 16;
/* 37 */       int debug9 = debug5 * 16;
/*    */       
/* 39 */       BlockPos debug10 = new BlockPos(debug8, 90, debug9);
/* 40 */       Rotation debug11 = Rotation.getRandom((Random)this.random);
/* 41 */       OceanRuinPieces.addPieces(debug3, debug10, debug11, this.pieces, (Random)this.random, debug7);
/* 42 */       calculateBoundingBox();
/*    */     }
/*    */   }
/*    */   
/*    */   public enum Type implements StringRepresentable {
/* 47 */     WARM("warm"),
/* 48 */     COLD("cold");
/*    */ 
/*    */     
/* 51 */     public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values, Type::byName); private static final Map<String, Type> BY_NAME;
/*    */     static {
/* 53 */       BY_NAME = (Map<String, Type>)Arrays.<Type>stream(values()).collect(Collectors.toMap(Type::getName, debug0 -> debug0));
/*    */     }
/*    */     private final String name;
/*    */     Type(String debug3) {
/* 57 */       this.name = debug3;
/*    */     }
/*    */     
/*    */     public String getName() {
/* 61 */       return this.name;
/*    */     }
/*    */     
/*    */     @Nullable
/*    */     public static Type byName(String debug0) {
/* 66 */       return BY_NAME.get(debug0);
/*    */     }
/*    */ 
/*    */     
/*    */     public String getSerializedName() {
/* 71 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\OceanRuinFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */