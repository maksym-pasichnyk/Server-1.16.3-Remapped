/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ public class EndGatewayFeature extends Feature<EndGatewayConfiguration> {
/*    */   public EndGatewayFeature(Codec<EndGatewayConfiguration> debug1) {
/* 16 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, EndGatewayConfiguration debug5) {
/* 21 */     for (BlockPos debug7 : BlockPos.betweenClosed(debug4.offset(-1, -2, -1), debug4.offset(1, 2, 1))) {
/* 22 */       boolean debug8 = (debug7.getX() == debug4.getX());
/* 23 */       boolean debug9 = (debug7.getY() == debug4.getY());
/* 24 */       boolean debug10 = (debug7.getZ() == debug4.getZ());
/* 25 */       boolean debug11 = (Math.abs(debug7.getY() - debug4.getY()) == 2);
/*    */       
/* 27 */       if (debug8 && debug9 && debug10) {
/* 28 */         BlockPos debug12 = debug7.immutable();
/* 29 */         setBlock((LevelWriter)debug1, debug12, Blocks.END_GATEWAY.defaultBlockState());
/* 30 */         debug5.getExit().ifPresent(debug3 -> {
/*    */               BlockEntity debug4 = debug0.getBlockEntity(debug1); if (debug4 instanceof TheEndGatewayBlockEntity) {
/*    */                 TheEndGatewayBlockEntity debug5 = (TheEndGatewayBlockEntity)debug4; debug5.setExitPosition(debug3, debug2.isExitExact());
/*    */                 debug4.setChanged();
/*    */               } 
/*    */             });
/*    */         continue;
/*    */       } 
/* 38 */       if (debug9) {
/* 39 */         setBlock((LevelWriter)debug1, debug7, Blocks.AIR.defaultBlockState()); continue;
/* 40 */       }  if (debug11 && debug8 && debug10) {
/* 41 */         setBlock((LevelWriter)debug1, debug7, Blocks.BEDROCK.defaultBlockState()); continue;
/* 42 */       }  if ((!debug8 && !debug10) || debug11) {
/* 43 */         setBlock((LevelWriter)debug1, debug7, Blocks.AIR.defaultBlockState()); continue;
/*    */       } 
/* 45 */       setBlock((LevelWriter)debug1, debug7, Blocks.BEDROCK.defaultBlockState());
/*    */     } 
/*    */     
/* 48 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\EndGatewayFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */