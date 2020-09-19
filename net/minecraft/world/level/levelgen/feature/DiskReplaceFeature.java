/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ public class DiskReplaceFeature extends BaseDiskFeature {
/*    */   public DiskReplaceFeature(Codec<DiskConfiguration> debug1) {
/* 14 */     super(debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, DiskConfiguration debug5) {
/* 20 */     if (!debug1.getFluidState(debug4).is((Tag)FluidTags.WATER)) {
/* 21 */       return false;
/*    */     }
/*    */     
/* 24 */     return super.place(debug1, debug2, debug3, debug4, debug5);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\DiskReplaceFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */