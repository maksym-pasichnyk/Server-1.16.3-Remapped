/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelSimulatedReader;
/*    */ import net.minecraft.world.level.LevelWriter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.VineBlock;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public class TrunkVineDecorator extends TreeDecorator {
/*    */   protected TreeDecoratorType<?> type() {
/* 17 */     return TreeDecoratorType.TRUNK_VINE;
/*    */   }
/*    */   
/* 20 */   public static final Codec<TrunkVineDecorator> CODEC = Codec.unit(() -> INSTANCE);
/*    */   
/* 22 */   public static final TrunkVineDecorator INSTANCE = new TrunkVineDecorator();
/*    */ 
/*    */   
/*    */   public void place(WorldGenLevel debug1, Random debug2, List<BlockPos> debug3, List<BlockPos> debug4, Set<BlockPos> debug5, BoundingBox debug6) {
/* 26 */     debug3.forEach(debug5 -> {
/*    */           if (debug1.nextInt(3) > 0) {
/*    */             BlockPos debug6 = debug5.west();
/*    */             if (Feature.isAir((LevelSimulatedReader)debug2, debug6))
/*    */               placeVine((LevelWriter)debug2, debug6, VineBlock.EAST, debug3, debug4); 
/*    */           } 
/*    */           if (debug1.nextInt(3) > 0) {
/*    */             BlockPos debug6 = debug5.east();
/*    */             if (Feature.isAir((LevelSimulatedReader)debug2, debug6))
/*    */               placeVine((LevelWriter)debug2, debug6, VineBlock.WEST, debug3, debug4); 
/*    */           } 
/*    */           if (debug1.nextInt(3) > 0) {
/*    */             BlockPos debug6 = debug5.north();
/*    */             if (Feature.isAir((LevelSimulatedReader)debug2, debug6))
/*    */               placeVine((LevelWriter)debug2, debug6, VineBlock.SOUTH, debug3, debug4); 
/*    */           } 
/*    */           if (debug1.nextInt(3) > 0) {
/*    */             BlockPos debug6 = debug5.south();
/*    */             if (Feature.isAir((LevelSimulatedReader)debug2, debug6))
/*    */               placeVine((LevelWriter)debug2, debug6, VineBlock.NORTH, debug3, debug4); 
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\TrunkVineDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */