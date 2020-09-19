/*    */ package net.minecraft.world.level.levelgen.feature.structures;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.StructureFeatureManager;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.JigsawBlock;
/*    */ import net.minecraft.world.level.block.Rotation;
/*    */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ 
/*    */ public class FeaturePoolElement extends StructurePoolElement {
/*    */   static {
/* 28 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)ConfiguredFeature.CODEC.fieldOf("feature").forGetter(()), (App)projectionCodec()).apply((Applicative)debug0, FeaturePoolElement::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<FeaturePoolElement> CODEC;
/*    */   private final Supplier<ConfiguredFeature<?, ?>> feature;
/*    */   private final CompoundTag defaultJigsawNBT;
/*    */   
/*    */   protected FeaturePoolElement(Supplier<ConfiguredFeature<?, ?>> debug1, StructureTemplatePool.Projection debug2) {
/* 37 */     super(debug2);
/* 38 */     this.feature = debug1;
/* 39 */     this.defaultJigsawNBT = fillDefaultJigsawNBT();
/*    */   }
/*    */   
/*    */   private CompoundTag fillDefaultJigsawNBT() {
/* 43 */     CompoundTag debug1 = new CompoundTag();
/* 44 */     debug1.putString("name", "minecraft:bottom");
/* 45 */     debug1.putString("final_state", "minecraft:air");
/*    */ 
/*    */     
/* 48 */     debug1.putString("pool", "minecraft:empty");
/* 49 */     debug1.putString("target", "minecraft:empty");
/* 50 */     debug1.putString("joint", JigsawBlockEntity.JointType.ROLLABLE.getSerializedName());
/*    */     
/* 52 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getSize(StructureManager debug1, Rotation debug2) {
/* 57 */     return BlockPos.ZERO;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureManager debug1, BlockPos debug2, Rotation debug3, Random debug4) {
/* 62 */     List<StructureTemplate.StructureBlockInfo> debug5 = Lists.newArrayList();
/* 63 */     debug5.add(new StructureTemplate.StructureBlockInfo(debug2, (BlockState)Blocks.JIGSAW.defaultBlockState().setValue((Property)JigsawBlock.ORIENTATION, (Comparable)FrontAndTop.fromFrontAndTop(Direction.DOWN, Direction.SOUTH)), this.defaultJigsawNBT));
/* 64 */     return debug5;
/*    */   }
/*    */ 
/*    */   
/*    */   public BoundingBox getBoundingBox(StructureManager debug1, BlockPos debug2, Rotation debug3) {
/* 69 */     BlockPos debug4 = getSize(debug1, debug3);
/* 70 */     return new BoundingBox(debug2.getX(), debug2.getY(), debug2.getZ(), debug2.getX() + debug4.getX(), debug2.getY() + debug4.getY(), debug2.getZ() + debug4.getZ());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(StructureManager debug1, WorldGenLevel debug2, StructureFeatureManager debug3, ChunkGenerator debug4, BlockPos debug5, BlockPos debug6, Rotation debug7, BoundingBox debug8, Random debug9, boolean debug10) {
/* 75 */     return ((ConfiguredFeature)this.feature.get()).place(debug2, debug4, debug9, debug5);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructurePoolElementType<?> getType() {
/* 80 */     return StructurePoolElementType.FEATURE;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 85 */     return "Feature[" + Registry.FEATURE.getKey(((ConfiguredFeature)this.feature.get()).feature()) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\structures\FeaturePoolElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */