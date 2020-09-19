/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Arrays;
/*    */ import java.util.Map;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.BiomeSource;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.MineshaftConfiguration;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.MineShaftPieces;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ public class MineshaftFeature extends StructureFeature<MineshaftConfiguration> {
/*    */   public MineshaftFeature(Codec<MineshaftConfiguration> debug1) {
/* 24 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isFeatureChunk(ChunkGenerator debug1, BiomeSource debug2, long debug3, WorldgenRandom debug5, int debug6, int debug7, Biome debug8, ChunkPos debug9, MineshaftConfiguration debug10) {
/* 29 */     debug5.setLargeFeatureSeed(debug3, debug6, debug7);
/*    */     
/* 31 */     double debug11 = debug10.probability;
/* 32 */     return (debug5.nextDouble() < debug11);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructureFeature.StructureStartFactory<MineshaftConfiguration> getStartFactory() {
/* 37 */     return MineShaftStart::new;
/*    */   }
/*    */   
/*    */   public enum Type implements StringRepresentable {
/* 41 */     NORMAL("normal"),
/* 42 */     MESA("mesa");
/*    */ 
/*    */     
/* 45 */     public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values, Type::byName); private static final Map<String, Type> BY_NAME; static {
/* 46 */       BY_NAME = (Map<String, Type>)Arrays.<Type>stream(values()).collect(Collectors.toMap(Type::getName, debug0 -> debug0));
/*    */     }
/*    */     private final String name;
/*    */     Type(String debug3) {
/* 50 */       this.name = debug3;
/*    */     }
/*    */     
/*    */     public String getName() {
/* 54 */       return this.name;
/*    */     }
/*    */     
/*    */     private static Type byName(String debug0) {
/* 58 */       return BY_NAME.get(debug0);
/*    */     }
/*    */     
/*    */     public static Type byId(int debug0) {
/* 62 */       if (debug0 < 0 || debug0 >= (values()).length) {
/* 63 */         return NORMAL;
/*    */       }
/* 65 */       return values()[debug0];
/*    */     }
/*    */ 
/*    */     
/*    */     public String getSerializedName() {
/* 70 */       return this.name;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class MineShaftStart extends StructureStart<MineshaftConfiguration> {
/*    */     public MineShaftStart(StructureFeature<MineshaftConfiguration> debug1, int debug2, int debug3, BoundingBox debug4, int debug5, long debug6) {
/* 76 */       super(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */     }
/*    */ 
/*    */     
/*    */     public void generatePieces(RegistryAccess debug1, ChunkGenerator debug2, StructureManager debug3, int debug4, int debug5, Biome debug6, MineshaftConfiguration debug7) {
/* 81 */       MineShaftPieces.MineShaftRoom debug8 = new MineShaftPieces.MineShaftRoom(0, (Random)this.random, (debug4 << 4) + 2, (debug5 << 4) + 2, debug7.type);
/* 82 */       this.pieces.add(debug8);
/* 83 */       debug8.addChildren((StructurePiece)debug8, this.pieces, (Random)this.random);
/*    */       
/* 85 */       calculateBoundingBox();
/* 86 */       if (debug7.type == MineshaftFeature.Type.MESA) {
/*    */         
/* 88 */         int debug9 = -5;
/* 89 */         int debug10 = debug2.getSeaLevel() - this.boundingBox.y1 + this.boundingBox.getYSpan() / 2 - -5;
/*    */ 
/*    */         
/* 92 */         this.boundingBox.move(0, debug10, 0);
/* 93 */         for (StructurePiece debug12 : this.pieces) {
/* 94 */           debug12.move(0, debug10, 0);
/*    */         }
/*    */       } else {
/* 97 */         moveBelowSeaLevel(debug2.getSeaLevel(), (Random)this.random, 10);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\MineshaftFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */