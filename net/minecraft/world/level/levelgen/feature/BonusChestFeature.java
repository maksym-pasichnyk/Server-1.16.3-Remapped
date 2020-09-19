/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.stream.Collector;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.IntStream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class BonusChestFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public BonusChestFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 25 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/* 30 */     ChunkPos debug6 = new ChunkPos(debug4);
/* 31 */     List<Integer> debug7 = IntStream.rangeClosed(debug6.getMinBlockX(), debug6.getMaxBlockX()).boxed().collect((Collector)Collectors.toList());
/* 32 */     Collections.shuffle(debug7, debug3);
/* 33 */     List<Integer> debug8 = IntStream.rangeClosed(debug6.getMinBlockZ(), debug6.getMaxBlockZ()).boxed().collect((Collector)Collectors.toList());
/* 34 */     Collections.shuffle(debug8, debug3);
/* 35 */     BlockPos.MutableBlockPos debug9 = new BlockPos.MutableBlockPos();
/*    */     
/* 37 */     for (Integer debug11 : debug7) {
/* 38 */       for (Integer debug13 : debug8) {
/* 39 */         debug9.set(debug11.intValue(), 0, debug13.intValue());
/* 40 */         BlockPos debug14 = debug1.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (BlockPos)debug9);
/*    */         
/* 42 */         if (debug1.isEmptyBlock(debug14) || debug1.getBlockState(debug14).getCollisionShape((BlockGetter)debug1, debug14).isEmpty()) {
/* 43 */           debug1.setBlock(debug14, Blocks.CHEST.defaultBlockState(), 2);
/*    */           
/* 45 */           RandomizableContainerBlockEntity.setLootTable((BlockGetter)debug1, debug3, debug14, BuiltInLootTables.SPAWN_BONUS_CHEST);
/*    */           
/* 47 */           BlockState debug15 = Blocks.TORCH.defaultBlockState();
/*    */           
/* 49 */           for (Direction debug17 : Direction.Plane.HORIZONTAL) {
/* 50 */             BlockPos debug18 = debug14.relative(debug17);
/* 51 */             if (debug15.canSurvive((LevelReader)debug1, debug18)) {
/* 52 */               debug1.setBlock(debug18, debug15, 2);
/*    */             }
/*    */           } 
/* 55 */           return true;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 60 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BonusChestFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */