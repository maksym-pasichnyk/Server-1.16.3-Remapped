/*      */ package net.minecraft.data.models;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.gson.JsonElement;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.function.UnaryOperator;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.IntStream;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.FrontAndTop;
/*      */ import net.minecraft.data.models.blockstates.BlockStateGenerator;
/*      */ import net.minecraft.data.models.blockstates.Condition;
/*      */ import net.minecraft.data.models.blockstates.MultiPartGenerator;
/*      */ import net.minecraft.data.models.blockstates.MultiVariantGenerator;
/*      */ import net.minecraft.data.models.blockstates.PropertyDispatch;
/*      */ import net.minecraft.data.models.blockstates.Variant;
/*      */ import net.minecraft.data.models.blockstates.VariantProperties;
/*      */ import net.minecraft.data.models.model.DelegatedModel;
/*      */ import net.minecraft.data.models.model.ModelLocationUtils;
/*      */ import net.minecraft.data.models.model.ModelTemplate;
/*      */ import net.minecraft.data.models.model.ModelTemplates;
/*      */ import net.minecraft.data.models.model.TextureMapping;
/*      */ import net.minecraft.data.models.model.TextureSlot;
/*      */ import net.minecraft.data.models.model.TexturedModel;
/*      */ import net.minecraft.resources.ResourceLocation;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.SpawnEggItem;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*      */ import net.minecraft.world.level.block.state.properties.BambooLeaves;
/*      */ import net.minecraft.world.level.block.state.properties.BellAttachType;
/*      */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*      */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*      */ import net.minecraft.world.level.block.state.properties.ComparatorMode;
/*      */ import net.minecraft.world.level.block.state.properties.DoorHingeSide;
/*      */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*      */ import net.minecraft.world.level.block.state.properties.Half;
/*      */ import net.minecraft.world.level.block.state.properties.PistonType;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.block.state.properties.RailShape;
/*      */ import net.minecraft.world.level.block.state.properties.RedstoneSide;
/*      */ import net.minecraft.world.level.block.state.properties.SlabType;
/*      */ import net.minecraft.world.level.block.state.properties.StairsShape;
/*      */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*      */ import net.minecraft.world.level.block.state.properties.WallSide;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BlockModelGenerators
/*      */ {
/*      */   private final Consumer<BlockStateGenerator> blockStateOutput;
/*      */   private final BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;
/*      */   private final Consumer<Item> skippedAutoModelsOutput;
/*      */   
/*      */   public BlockModelGenerators(Consumer<BlockStateGenerator> debug1, BiConsumer<ResourceLocation, Supplier<JsonElement>> debug2, Consumer<Item> debug3) {
/*   82 */     this.blockStateOutput = debug1;
/*   83 */     this.modelOutput = debug2;
/*   84 */     this.skippedAutoModelsOutput = debug3;
/*      */   }
/*      */   
/*      */   private void skipAutoItemBlock(Block debug1) {
/*   88 */     this.skippedAutoModelsOutput.accept(debug1.asItem());
/*      */   }
/*      */   
/*      */   private void delegateItemModel(Block debug1, ResourceLocation debug2) {
/*   92 */     this.modelOutput.accept(ModelLocationUtils.getModelLocation(debug1.asItem()), new DelegatedModel(debug2));
/*      */   }
/*      */   
/*      */   private void delegateItemModel(Item debug1, ResourceLocation debug2) {
/*   96 */     this.modelOutput.accept(ModelLocationUtils.getModelLocation(debug1), new DelegatedModel(debug2));
/*      */   }
/*      */   
/*      */   private void createSimpleFlatItemModel(Item debug1) {
/*  100 */     ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(debug1), TextureMapping.layer0(debug1), this.modelOutput);
/*      */   }
/*      */   
/*      */   private void createSimpleFlatItemModel(Block debug1) {
/*  104 */     Item debug2 = debug1.asItem();
/*  105 */     if (debug2 != Items.AIR) {
/*  106 */       ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(debug2), TextureMapping.layer0(debug1), this.modelOutput);
/*      */     }
/*      */   }
/*      */   
/*      */   private void createSimpleFlatItemModel(Block debug1, String debug2) {
/*  111 */     Item debug3 = debug1.asItem();
/*  112 */     ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(debug3), TextureMapping.layer0(TextureMapping.getBlockTexture(debug1, debug2)), this.modelOutput);
/*      */   }
/*      */   
/*      */   private static PropertyDispatch createHorizontalFacingDispatch() {
/*  116 */     return (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.HORIZONTAL_FACING)
/*  117 */       .select((Comparable)Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  118 */       .select((Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  119 */       .select((Comparable)Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*  120 */       .select((Comparable)Direction.NORTH, Variant.variant());
/*      */   }
/*      */   
/*      */   private static PropertyDispatch createHorizontalFacingDispatchAlt() {
/*  124 */     return (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.HORIZONTAL_FACING)
/*  125 */       .select((Comparable)Direction.SOUTH, Variant.variant())
/*  126 */       .select((Comparable)Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  127 */       .select((Comparable)Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  128 */       .select((Comparable)Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
/*      */   }
/*      */   
/*      */   private static PropertyDispatch createTorchHorizontalDispatch() {
/*  132 */     return (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.HORIZONTAL_FACING)
/*  133 */       .select((Comparable)Direction.EAST, Variant.variant())
/*  134 */       .select((Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  135 */       .select((Comparable)Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  136 */       .select((Comparable)Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
/*      */   }
/*      */   
/*      */   private static PropertyDispatch createFacingDispatch() {
/*  140 */     return (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.FACING)
/*  141 */       .select((Comparable)Direction.DOWN, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
/*  142 */       .select((Comparable)Direction.UP, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R270))
/*  143 */       .select((Comparable)Direction.NORTH, Variant.variant())
/*  144 */       .select((Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  145 */       .select((Comparable)Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*  146 */       .select((Comparable)Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
/*      */   }
/*      */   
/*      */   private static MultiVariantGenerator createRotatedVariant(Block debug0, ResourceLocation debug1) {
/*  150 */     return MultiVariantGenerator.multiVariant(debug0, createRotatedVariants(debug1));
/*      */   }
/*      */   
/*      */   private static Variant[] createRotatedVariants(ResourceLocation debug0) {
/*  154 */     return new Variant[] { Variant.variant().with(VariantProperties.MODEL, debug0), 
/*  155 */         Variant.variant().with(VariantProperties.MODEL, debug0).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90), 
/*  156 */         Variant.variant().with(VariantProperties.MODEL, debug0).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180), 
/*  157 */         Variant.variant().with(VariantProperties.MODEL, debug0).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270) };
/*      */   }
/*      */   
/*      */   private static MultiVariantGenerator createRotatedVariant(Block debug0, ResourceLocation debug1, ResourceLocation debug2) {
/*  161 */     return MultiVariantGenerator.multiVariant(debug0, new Variant[] {
/*  162 */           Variant.variant().with(VariantProperties.MODEL, debug1), 
/*  163 */           Variant.variant().with(VariantProperties.MODEL, debug2), 
/*  164 */           Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180), 
/*  165 */           Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)
/*      */         });
/*      */   }
/*      */   
/*      */   private static PropertyDispatch createBooleanModelDispatch(BooleanProperty debug0, ResourceLocation debug1, ResourceLocation debug2) {
/*  170 */     return (PropertyDispatch)PropertyDispatch.property((Property)debug0)
/*  171 */       .select(Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug1))
/*  172 */       .select(Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug2));
/*      */   }
/*      */   
/*      */   private void createRotatedMirroredVariantBlock(Block debug1) {
/*  176 */     ResourceLocation debug2 = TexturedModel.CUBE.create(debug1, this.modelOutput);
/*  177 */     ResourceLocation debug3 = TexturedModel.CUBE_MIRRORED.create(debug1, this.modelOutput);
/*  178 */     this.blockStateOutput.accept(createRotatedVariant(debug1, debug2, debug3));
/*      */   }
/*      */   
/*      */   private void createRotatedVariantBlock(Block debug1) {
/*  182 */     ResourceLocation debug2 = TexturedModel.CUBE.create(debug1, this.modelOutput);
/*  183 */     this.blockStateOutput.accept(createRotatedVariant(debug1, debug2));
/*      */   }
/*      */   
/*      */   private static BlockStateGenerator createButton(Block debug0, ResourceLocation debug1, ResourceLocation debug2) {
/*  187 */     return (BlockStateGenerator)MultiVariantGenerator.multiVariant(debug0)
/*  188 */       .with(
/*  189 */         (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.POWERED)
/*  190 */         .select(Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug1))
/*  191 */         .select(Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug2)))
/*      */       
/*  193 */       .with(
/*  194 */         (PropertyDispatch)PropertyDispatch.properties((Property)BlockStateProperties.ATTACH_FACE, (Property)BlockStateProperties.HORIZONTAL_FACING)
/*  195 */         .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  196 */         .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*  197 */         .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  198 */         .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.NORTH, Variant.variant())
/*      */         
/*  200 */         .select((Comparable)AttachFace.WALL, (Comparable)Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  201 */         .select((Comparable)AttachFace.WALL, (Comparable)Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  202 */         .select((Comparable)AttachFace.WALL, (Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  203 */         .select((Comparable)AttachFace.WALL, (Comparable)Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*      */         
/*  205 */         .select((Comparable)AttachFace.CEILING, (Comparable)Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
/*  206 */         .select((Comparable)AttachFace.CEILING, (Comparable)Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
/*  207 */         .select((Comparable)AttachFace.CEILING, (Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
/*  208 */         .select((Comparable)AttachFace.CEILING, (Comparable)Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180)));
/*      */   }
/*      */ 
/*      */   
/*      */   private static PropertyDispatch.C4<Direction, DoubleBlockHalf, DoorHingeSide, Boolean> configureDoorHalf(PropertyDispatch.C4<Direction, DoubleBlockHalf, DoorHingeSide, Boolean> debug0, DoubleBlockHalf debug1, ResourceLocation debug2, ResourceLocation debug3) {
/*  213 */     return debug0
/*  214 */       .select((Comparable)Direction.EAST, (Comparable)debug1, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug2))
/*  215 */       .select((Comparable)Direction.SOUTH, (Comparable)debug1, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  216 */       .select((Comparable)Direction.WEST, (Comparable)debug1, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  217 */       .select((Comparable)Direction.NORTH, (Comparable)debug1, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */       
/*  219 */       .select((Comparable)Direction.EAST, (Comparable)debug1, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug3))
/*  220 */       .select((Comparable)Direction.SOUTH, (Comparable)debug1, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  221 */       .select((Comparable)Direction.WEST, (Comparable)debug1, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  222 */       .select((Comparable)Direction.NORTH, (Comparable)debug1, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */       
/*  224 */       .select((Comparable)Direction.EAST, (Comparable)debug1, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  225 */       .select((Comparable)Direction.SOUTH, (Comparable)debug1, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  226 */       .select((Comparable)Direction.WEST, (Comparable)debug1, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*  227 */       .select((Comparable)Direction.NORTH, (Comparable)debug1, (Comparable)DoorHingeSide.LEFT, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3))
/*      */       
/*  229 */       .select((Comparable)Direction.EAST, (Comparable)debug1, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*  230 */       .select((Comparable)Direction.SOUTH, (Comparable)debug1, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug2))
/*  231 */       .select((Comparable)Direction.WEST, (Comparable)debug1, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  232 */       .select((Comparable)Direction.NORTH, (Comparable)debug1, (Comparable)DoorHingeSide.RIGHT, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180));
/*      */   }
/*      */   
/*      */   private static BlockStateGenerator createDoor(Block debug0, ResourceLocation debug1, ResourceLocation debug2, ResourceLocation debug3, ResourceLocation debug4) {
/*  236 */     return (BlockStateGenerator)MultiVariantGenerator.multiVariant(debug0)
/*  237 */       .with(
/*  238 */         (PropertyDispatch)configureDoorHalf(
/*  239 */           configureDoorHalf(PropertyDispatch.properties((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.DOUBLE_BLOCK_HALF, (Property)BlockStateProperties.DOOR_HINGE, (Property)BlockStateProperties.OPEN), DoubleBlockHalf.LOWER, debug1, debug2), DoubleBlockHalf.UPPER, debug3, debug4));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static BlockStateGenerator createFence(Block debug0, ResourceLocation debug1, ResourceLocation debug2) {
/*  246 */     return (BlockStateGenerator)MultiPartGenerator.multiPart(debug0)
/*  247 */       .with(Variant.variant().with(VariantProperties.MODEL, debug1))
/*  248 */       .with((Condition)Condition.condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  249 */       .with((Condition)Condition.condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  250 */       .with((Condition)Condition.condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  251 */       .with((Condition)Condition.condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)));
/*      */   }
/*      */   
/*      */   private static BlockStateGenerator createWall(Block debug0, ResourceLocation debug1, ResourceLocation debug2, ResourceLocation debug3) {
/*  255 */     return (BlockStateGenerator)MultiPartGenerator.multiPart(debug0)
/*  256 */       .with((Condition)Condition.condition().term((Property)BlockStateProperties.UP, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug1))
/*      */       
/*  258 */       .with((Condition)Condition.condition().term((Property)BlockStateProperties.NORTH_WALL, (Comparable)WallSide.LOW), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  259 */       .with((Condition)Condition.condition().term((Property)BlockStateProperties.EAST_WALL, (Comparable)WallSide.LOW), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  260 */       .with((Condition)Condition.condition().term((Property)BlockStateProperties.SOUTH_WALL, (Comparable)WallSide.LOW), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  261 */       .with((Condition)Condition.condition().term((Property)BlockStateProperties.WEST_WALL, (Comparable)WallSide.LOW), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*      */       
/*  263 */       .with((Condition)Condition.condition().term((Property)BlockStateProperties.NORTH_WALL, (Comparable)WallSide.TALL), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  264 */       .with((Condition)Condition.condition().term((Property)BlockStateProperties.EAST_WALL, (Comparable)WallSide.TALL), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  265 */       .with((Condition)Condition.condition().term((Property)BlockStateProperties.SOUTH_WALL, (Comparable)WallSide.TALL), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  266 */       .with((Condition)Condition.condition().term((Property)BlockStateProperties.WEST_WALL, (Comparable)WallSide.TALL), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)));
/*      */   }
/*      */ 
/*      */   
/*      */   private static BlockStateGenerator createFenceGate(Block debug0, ResourceLocation debug1, ResourceLocation debug2, ResourceLocation debug3, ResourceLocation debug4) {
/*  271 */     return (BlockStateGenerator)MultiVariantGenerator.multiVariant(debug0, Variant.variant().with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  272 */       .with(createHorizontalFacingDispatchAlt())
/*  273 */       .with(
/*  274 */         (PropertyDispatch)PropertyDispatch.properties((Property)BlockStateProperties.IN_WALL, (Property)BlockStateProperties.OPEN)
/*  275 */         .select(Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug2))
/*  276 */         .select(Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug4))
/*  277 */         .select(Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug1))
/*  278 */         .select(Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3)));
/*      */   }
/*      */ 
/*      */   
/*      */   private static BlockStateGenerator createStairs(Block debug0, ResourceLocation debug1, ResourceLocation debug2, ResourceLocation debug3) {
/*  283 */     return (BlockStateGenerator)MultiVariantGenerator.multiVariant(debug0)
/*  284 */       .with(
/*  285 */         (PropertyDispatch)PropertyDispatch.properties((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.HALF, (Property)BlockStateProperties.STAIRS_SHAPE)
/*  286 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, debug2))
/*  287 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  288 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  289 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  290 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug3))
/*  291 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  292 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  293 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  294 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  295 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  296 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug3))
/*  297 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  298 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug1))
/*  299 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  300 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  301 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  302 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  303 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  304 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug1))
/*  305 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, (Comparable)StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  306 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, (Comparable)StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  307 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, (Comparable)StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  308 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, (Comparable)StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  309 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, (Comparable)StairsShape.STRAIGHT, Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  310 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  311 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  312 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  313 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  314 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  315 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  316 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  317 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, (Comparable)StairsShape.OUTER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  318 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  319 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  320 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  321 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_RIGHT, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  322 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  323 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  324 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*  325 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, (Comparable)StairsShape.INNER_LEFT, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static BlockStateGenerator createOrientableTrapdoor(Block debug0, ResourceLocation debug1, ResourceLocation debug2, ResourceLocation debug3) {
/*  331 */     return (BlockStateGenerator)MultiVariantGenerator.multiVariant(debug0)
/*  332 */       .with(
/*  333 */         (PropertyDispatch)PropertyDispatch.properties((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.HALF, (Property)BlockStateProperties.OPEN)
/*  334 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug2))
/*  335 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  336 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  337 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*  338 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug1))
/*  339 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  340 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  341 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*  342 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3))
/*  343 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  344 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  345 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*  346 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  347 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0))
/*  348 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*  349 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)));
/*      */   }
/*      */ 
/*      */   
/*      */   private static BlockStateGenerator createTrapdoor(Block debug0, ResourceLocation debug1, ResourceLocation debug2, ResourceLocation debug3) {
/*  354 */     return (BlockStateGenerator)MultiVariantGenerator.multiVariant(debug0)
/*  355 */       .with(
/*  356 */         (PropertyDispatch)PropertyDispatch.properties((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.HALF, (Property)BlockStateProperties.OPEN)
/*  357 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug2))
/*  358 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug2))
/*  359 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug2))
/*  360 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug2))
/*  361 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug1))
/*  362 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug1))
/*  363 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug1))
/*  364 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug1))
/*  365 */         .select((Comparable)Direction.NORTH, (Comparable)Half.BOTTOM, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3))
/*  366 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.BOTTOM, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  367 */         .select((Comparable)Direction.EAST, (Comparable)Half.BOTTOM, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  368 */         .select((Comparable)Direction.WEST, (Comparable)Half.BOTTOM, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*  369 */         .select((Comparable)Direction.NORTH, (Comparable)Half.TOP, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3))
/*  370 */         .select((Comparable)Direction.SOUTH, (Comparable)Half.TOP, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  371 */         .select((Comparable)Direction.EAST, (Comparable)Half.TOP, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  372 */         .select((Comparable)Direction.WEST, (Comparable)Half.TOP, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
/*      */   }
/*      */ 
/*      */   
/*      */   private static MultiVariantGenerator createSimpleBlock(Block debug0, ResourceLocation debug1) {
/*  377 */     return MultiVariantGenerator.multiVariant(debug0, Variant.variant().with(VariantProperties.MODEL, debug1));
/*      */   }
/*      */   
/*      */   private static PropertyDispatch createRotatedPillar() {
/*  381 */     return (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.AXIS)
/*  382 */       .select((Comparable)Direction.Axis.Y, Variant.variant())
/*  383 */       .select((Comparable)Direction.Axis.Z, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
/*  384 */       .select((Comparable)Direction.Axis.X, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
/*      */   }
/*      */   
/*      */   private static BlockStateGenerator createAxisAlignedPillarBlock(Block debug0, ResourceLocation debug1) {
/*  388 */     return (BlockStateGenerator)MultiVariantGenerator.multiVariant(debug0, Variant.variant().with(VariantProperties.MODEL, debug1)).with(createRotatedPillar());
/*      */   }
/*      */   
/*      */   private void createAxisAlignedPillarBlockCustomModel(Block debug1, ResourceLocation debug2) {
/*  392 */     this.blockStateOutput.accept(createAxisAlignedPillarBlock(debug1, debug2));
/*      */   }
/*      */   
/*      */   private void createAxisAlignedPillarBlock(Block debug1, TexturedModel.Provider debug2) {
/*  396 */     ResourceLocation debug3 = debug2.create(debug1, this.modelOutput);
/*  397 */     this.blockStateOutput.accept(createAxisAlignedPillarBlock(debug1, debug3));
/*      */   }
/*      */   
/*      */   private void createHorizontallyRotatedBlock(Block debug1, TexturedModel.Provider debug2) {
/*  401 */     ResourceLocation debug3 = debug2.create(debug1, this.modelOutput);
/*  402 */     this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(debug1, Variant.variant().with(VariantProperties.MODEL, debug3)).with(createHorizontalFacingDispatch()));
/*      */   }
/*      */   
/*      */   private static BlockStateGenerator createRotatedPillarWithHorizontalVariant(Block debug0, ResourceLocation debug1, ResourceLocation debug2) {
/*  406 */     return (BlockStateGenerator)MultiVariantGenerator.multiVariant(debug0)
/*  407 */       .with(
/*  408 */         (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.AXIS)
/*  409 */         .select((Comparable)Direction.Axis.Y, Variant.variant().with(VariantProperties.MODEL, debug1))
/*  410 */         .select((Comparable)Direction.Axis.Z, Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
/*  411 */         .select((Comparable)Direction.Axis.X, Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createRotatedPillarWithHorizontalVariant(Block debug1, TexturedModel.Provider debug2, TexturedModel.Provider debug3) {
/*  416 */     ResourceLocation debug4 = debug2.create(debug1, this.modelOutput);
/*  417 */     ResourceLocation debug5 = debug3.create(debug1, this.modelOutput);
/*  418 */     this.blockStateOutput.accept(createRotatedPillarWithHorizontalVariant(debug1, debug4, debug5));
/*      */   }
/*      */   
/*      */   private ResourceLocation createSuffixedVariant(Block debug1, String debug2, ModelTemplate debug3, Function<ResourceLocation, TextureMapping> debug4) {
/*  422 */     return debug3.createWithSuffix(debug1, debug2, debug4.apply(TextureMapping.getBlockTexture(debug1, debug2)), this.modelOutput);
/*      */   }
/*      */   
/*      */   private static BlockStateGenerator createPressurePlate(Block debug0, ResourceLocation debug1, ResourceLocation debug2) {
/*  426 */     return (BlockStateGenerator)MultiVariantGenerator.multiVariant(debug0)
/*  427 */       .with(createBooleanModelDispatch(BlockStateProperties.POWERED, debug2, debug1));
/*      */   }
/*      */   
/*      */   private static BlockStateGenerator createSlab(Block debug0, ResourceLocation debug1, ResourceLocation debug2, ResourceLocation debug3) {
/*  431 */     return (BlockStateGenerator)MultiVariantGenerator.multiVariant(debug0)
/*  432 */       .with(
/*  433 */         (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.SLAB_TYPE)
/*  434 */         .select((Comparable)SlabType.BOTTOM, Variant.variant().with(VariantProperties.MODEL, debug1))
/*  435 */         .select((Comparable)SlabType.TOP, Variant.variant().with(VariantProperties.MODEL, debug2))
/*  436 */         .select((Comparable)SlabType.DOUBLE, Variant.variant().with(VariantProperties.MODEL, debug3)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createTrivialCube(Block debug1) {
/*  441 */     createTrivialBlock(debug1, TexturedModel.CUBE);
/*      */   }
/*      */   
/*      */   private void createTrivialBlock(Block debug1, TexturedModel.Provider debug2) {
/*  445 */     this.blockStateOutput.accept(createSimpleBlock(debug1, debug2.create(debug1, this.modelOutput)));
/*      */   }
/*      */   
/*      */   private void createTrivialBlock(Block debug1, TextureMapping debug2, ModelTemplate debug3) {
/*  449 */     ResourceLocation debug4 = debug3.create(debug1, debug2, this.modelOutput);
/*  450 */     this.blockStateOutput.accept(createSimpleBlock(debug1, debug4));
/*      */   }
/*      */   
/*      */   class BlockFamilyProvider
/*      */   {
/*      */     private final TextureMapping mapping;
/*      */     @Nullable
/*      */     private ResourceLocation fullBlock;
/*      */     
/*      */     public BlockFamilyProvider(TextureMapping debug2) {
/*  460 */       this.mapping = debug2;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider fullBlock(Block debug1, ModelTemplate debug2) {
/*  464 */       this.fullBlock = debug2.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  465 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(debug1, this.fullBlock));
/*  466 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider fullBlock(Function<TextureMapping, ResourceLocation> debug1) {
/*  470 */       this.fullBlock = debug1.apply(this.mapping);
/*  471 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider button(Block debug1) {
/*  475 */       ResourceLocation debug2 = ModelTemplates.BUTTON.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  476 */       ResourceLocation debug3 = ModelTemplates.BUTTON_PRESSED.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  477 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createButton(debug1, debug2, debug3));
/*      */       
/*  479 */       ResourceLocation debug4 = ModelTemplates.BUTTON_INVENTORY.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  480 */       BlockModelGenerators.this.delegateItemModel(debug1, debug4);
/*  481 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider wall(Block debug1) {
/*  485 */       ResourceLocation debug2 = ModelTemplates.WALL_POST.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  486 */       ResourceLocation debug3 = ModelTemplates.WALL_LOW_SIDE.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  487 */       ResourceLocation debug4 = ModelTemplates.WALL_TALL_SIDE.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  488 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createWall(debug1, debug2, debug3, debug4));
/*      */       
/*  490 */       ResourceLocation debug5 = ModelTemplates.WALL_INVENTORY.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  491 */       BlockModelGenerators.this.delegateItemModel(debug1, debug5);
/*  492 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider fence(Block debug1) {
/*  496 */       ResourceLocation debug2 = ModelTemplates.FENCE_POST.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  497 */       ResourceLocation debug3 = ModelTemplates.FENCE_SIDE.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  498 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createFence(debug1, debug2, debug3));
/*      */       
/*  500 */       ResourceLocation debug4 = ModelTemplates.FENCE_INVENTORY.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  501 */       BlockModelGenerators.this.delegateItemModel(debug1, debug4);
/*  502 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider fenceGate(Block debug1) {
/*  506 */       ResourceLocation debug2 = ModelTemplates.FENCE_GATE_OPEN.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  507 */       ResourceLocation debug3 = ModelTemplates.FENCE_GATE_CLOSED.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  508 */       ResourceLocation debug4 = ModelTemplates.FENCE_GATE_WALL_OPEN.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  509 */       ResourceLocation debug5 = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  510 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createFenceGate(debug1, debug2, debug3, debug4, debug5));
/*  511 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider pressurePlate(Block debug1) {
/*  515 */       ResourceLocation debug2 = ModelTemplates.PRESSURE_PLATE_UP.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  516 */       ResourceLocation debug3 = ModelTemplates.PRESSURE_PLATE_DOWN.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  517 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createPressurePlate(debug1, debug2, debug3));
/*  518 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider sign(Block debug1, Block debug2) {
/*  522 */       ResourceLocation debug3 = ModelTemplates.PARTICLE_ONLY.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  523 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(debug1, debug3));
/*  524 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(debug2, debug3));
/*  525 */       BlockModelGenerators.this.createSimpleFlatItemModel(debug1.asItem());
/*  526 */       BlockModelGenerators.this.skipAutoItemBlock(debug2);
/*  527 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider slab(Block debug1) {
/*  531 */       if (this.fullBlock == null) {
/*  532 */         throw new IllegalStateException("Full block not generated yet");
/*      */       }
/*  534 */       ResourceLocation debug2 = ModelTemplates.SLAB_BOTTOM.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  535 */       ResourceLocation debug3 = ModelTemplates.SLAB_TOP.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  536 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSlab(debug1, debug2, debug3, this.fullBlock));
/*  537 */       return this;
/*      */     }
/*      */     
/*      */     public BlockFamilyProvider stairs(Block debug1) {
/*  541 */       ResourceLocation debug2 = ModelTemplates.STAIRS_INNER.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  542 */       ResourceLocation debug3 = ModelTemplates.STAIRS_STRAIGHT.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*  543 */       ResourceLocation debug4 = ModelTemplates.STAIRS_OUTER.create(debug1, this.mapping, BlockModelGenerators.this.modelOutput);
/*      */       
/*  545 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createStairs(debug1, debug2, debug3, debug4));
/*  546 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private BlockFamilyProvider family(Block debug1, TexturedModel debug2) {
/*  552 */     return (new BlockFamilyProvider(debug2.getMapping())).fullBlock(debug1, debug2.getTemplate());
/*      */   }
/*      */   
/*      */   private BlockFamilyProvider family(Block debug1, TexturedModel.Provider debug2) {
/*  556 */     TexturedModel debug3 = debug2.get(debug1);
/*  557 */     return (new BlockFamilyProvider(debug3.getMapping())).fullBlock(debug1, debug3.getTemplate());
/*      */   }
/*      */   
/*      */   private BlockFamilyProvider family(Block debug1) {
/*  561 */     return family(debug1, TexturedModel.CUBE);
/*      */   }
/*      */   
/*      */   private BlockFamilyProvider family(TextureMapping debug1) {
/*  565 */     return new BlockFamilyProvider(debug1);
/*      */   }
/*      */   
/*      */   private void createDoor(Block debug1) {
/*  569 */     TextureMapping debug2 = TextureMapping.door(debug1);
/*  570 */     ResourceLocation debug3 = ModelTemplates.DOOR_BOTTOM.create(debug1, debug2, this.modelOutput);
/*  571 */     ResourceLocation debug4 = ModelTemplates.DOOR_BOTTOM_HINGE.create(debug1, debug2, this.modelOutput);
/*  572 */     ResourceLocation debug5 = ModelTemplates.DOOR_TOP.create(debug1, debug2, this.modelOutput);
/*  573 */     ResourceLocation debug6 = ModelTemplates.DOOR_TOP_HINGE.create(debug1, debug2, this.modelOutput);
/*      */     
/*  575 */     createSimpleFlatItemModel(debug1.asItem());
/*  576 */     this.blockStateOutput.accept(createDoor(debug1, debug3, debug4, debug5, debug6));
/*      */   }
/*      */   
/*      */   private void createOrientableTrapdoor(Block debug1) {
/*  580 */     TextureMapping debug2 = TextureMapping.defaultTexture(debug1);
/*  581 */     ResourceLocation debug3 = ModelTemplates.ORIENTABLE_TRAPDOOR_TOP.create(debug1, debug2, this.modelOutput);
/*  582 */     ResourceLocation debug4 = ModelTemplates.ORIENTABLE_TRAPDOOR_BOTTOM.create(debug1, debug2, this.modelOutput);
/*  583 */     ResourceLocation debug5 = ModelTemplates.ORIENTABLE_TRAPDOOR_OPEN.create(debug1, debug2, this.modelOutput);
/*      */     
/*  585 */     this.blockStateOutput.accept(createOrientableTrapdoor(debug1, debug3, debug4, debug5));
/*  586 */     delegateItemModel(debug1, debug4);
/*      */   }
/*      */   
/*      */   private void createTrapdoor(Block debug1) {
/*  590 */     TextureMapping debug2 = TextureMapping.defaultTexture(debug1);
/*  591 */     ResourceLocation debug3 = ModelTemplates.TRAPDOOR_TOP.create(debug1, debug2, this.modelOutput);
/*  592 */     ResourceLocation debug4 = ModelTemplates.TRAPDOOR_BOTTOM.create(debug1, debug2, this.modelOutput);
/*  593 */     ResourceLocation debug5 = ModelTemplates.TRAPDOOR_OPEN.create(debug1, debug2, this.modelOutput);
/*      */     
/*  595 */     this.blockStateOutput.accept(createTrapdoor(debug1, debug3, debug4, debug5));
/*  596 */     delegateItemModel(debug1, debug4);
/*      */   }
/*      */   
/*      */   class WoodProvider
/*      */   {
/*      */     private final TextureMapping logMapping;
/*      */     
/*      */     public WoodProvider(TextureMapping debug2) {
/*  604 */       this.logMapping = debug2;
/*      */     }
/*      */     
/*      */     public WoodProvider wood(Block debug1) {
/*  608 */       TextureMapping debug2 = this.logMapping.copyAndUpdate(TextureSlot.END, this.logMapping.get(TextureSlot.SIDE));
/*  609 */       ResourceLocation debug3 = ModelTemplates.CUBE_COLUMN.create(debug1, debug2, BlockModelGenerators.this.modelOutput);
/*  610 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createAxisAlignedPillarBlock(debug1, debug3));
/*  611 */       return this;
/*      */     }
/*      */     
/*      */     public WoodProvider log(Block debug1) {
/*  615 */       ResourceLocation debug2 = ModelTemplates.CUBE_COLUMN.create(debug1, this.logMapping, BlockModelGenerators.this.modelOutput);
/*  616 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createAxisAlignedPillarBlock(debug1, debug2));
/*  617 */       return this;
/*      */     }
/*      */     
/*      */     public WoodProvider logWithHorizontal(Block debug1) {
/*  621 */       ResourceLocation debug2 = ModelTemplates.CUBE_COLUMN.create(debug1, this.logMapping, BlockModelGenerators.this.modelOutput);
/*  622 */       ResourceLocation debug3 = ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(debug1, this.logMapping, BlockModelGenerators.this.modelOutput);
/*  623 */       BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createRotatedPillarWithHorizontalVariant(debug1, debug2, debug3));
/*  624 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private WoodProvider woodProvider(Block debug1) {
/*  630 */     return new WoodProvider(TextureMapping.logColumn(debug1));
/*      */   }
/*      */   
/*      */   private void createNonTemplateModelBlock(Block debug1) {
/*  634 */     createNonTemplateModelBlock(debug1, debug1);
/*      */   }
/*      */   
/*      */   private void createNonTemplateModelBlock(Block debug1, Block debug2) {
/*  638 */     this.blockStateOutput.accept(createSimpleBlock(debug1, ModelLocationUtils.getModelLocation(debug2)));
/*      */   }
/*      */   
/*      */   enum TintState {
/*  642 */     TINTED, NOT_TINTED;
/*      */     
/*      */     public ModelTemplate getCross() {
/*  645 */       return (this == TINTED) ? ModelTemplates.TINTED_CROSS : ModelTemplates.CROSS;
/*      */     }
/*      */     
/*      */     public ModelTemplate getCrossPot() {
/*  649 */       return (this == TINTED) ? ModelTemplates.TINTED_FLOWER_POT_CROSS : ModelTemplates.FLOWER_POT_CROSS;
/*      */     }
/*      */   }
/*      */   
/*      */   private void createCrossBlockWithDefaultItem(Block debug1, TintState debug2) {
/*  654 */     createSimpleFlatItemModel(debug1);
/*  655 */     createCrossBlock(debug1, debug2);
/*      */   }
/*      */   
/*      */   private void createCrossBlockWithDefaultItem(Block debug1, TintState debug2, TextureMapping debug3) {
/*  659 */     createSimpleFlatItemModel(debug1);
/*  660 */     createCrossBlock(debug1, debug2, debug3);
/*      */   }
/*      */   
/*      */   private void createCrossBlock(Block debug1, TintState debug2) {
/*  664 */     TextureMapping debug3 = TextureMapping.cross(debug1);
/*  665 */     createCrossBlock(debug1, debug2, debug3);
/*      */   }
/*      */   
/*      */   private void createCrossBlock(Block debug1, TintState debug2, TextureMapping debug3) {
/*  669 */     ResourceLocation debug4 = debug2.getCross().create(debug1, debug3, this.modelOutput);
/*  670 */     this.blockStateOutput.accept(createSimpleBlock(debug1, debug4));
/*      */   }
/*      */   
/*      */   private void createPlant(Block debug1, Block debug2, TintState debug3) {
/*  674 */     createCrossBlockWithDefaultItem(debug1, debug3);
/*      */     
/*  676 */     TextureMapping debug4 = TextureMapping.plant(debug1);
/*  677 */     ResourceLocation debug5 = debug3.getCrossPot().create(debug2, debug4, this.modelOutput);
/*  678 */     this.blockStateOutput.accept(createSimpleBlock(debug2, debug5));
/*      */   }
/*      */   
/*      */   private void createCoralFans(Block debug1, Block debug2) {
/*  682 */     TexturedModel debug3 = TexturedModel.CORAL_FAN.get(debug1);
/*      */     
/*  684 */     ResourceLocation debug4 = debug3.create(debug1, this.modelOutput);
/*  685 */     this.blockStateOutput.accept(createSimpleBlock(debug1, debug4));
/*      */     
/*  687 */     ResourceLocation debug5 = ModelTemplates.CORAL_WALL_FAN.create(debug2, debug3.getMapping(), this.modelOutput);
/*  688 */     this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(debug2, Variant.variant().with(VariantProperties.MODEL, debug5)).with(createHorizontalFacingDispatch()));
/*      */     
/*  690 */     createSimpleFlatItemModel(debug1);
/*      */   }
/*      */   
/*      */   private void createStems(Block debug1, Block debug2) {
/*  694 */     createSimpleFlatItemModel(debug1.asItem());
/*  695 */     TextureMapping debug3 = TextureMapping.stem(debug1);
/*  696 */     TextureMapping debug4 = TextureMapping.attachedStem(debug1, debug2);
/*      */     
/*  698 */     ResourceLocation debug5 = ModelTemplates.ATTACHED_STEM.create(debug2, debug4, this.modelOutput);
/*  699 */     this.blockStateOutput.accept(
/*  700 */         MultiVariantGenerator.multiVariant(debug2, Variant.variant()
/*  701 */           .with(VariantProperties.MODEL, debug5))
/*  702 */         .with((PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.HORIZONTAL_FACING)
/*  703 */           .select((Comparable)Direction.WEST, Variant.variant())
/*  704 */           .select((Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*  705 */           .select((Comparable)Direction.NORTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  706 */           .select((Comparable)Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))));
/*      */ 
/*      */     
/*  709 */     this.blockStateOutput.accept(
/*  710 */         MultiVariantGenerator.multiVariant(debug1)
/*  711 */         .with(
/*  712 */           PropertyDispatch.property((Property)BlockStateProperties.AGE_7).generate(debug3 -> Variant.variant().with(VariantProperties.MODEL, ModelTemplates.STEMS[debug3.intValue()].create(debug1, debug2, this.modelOutput)))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createCoral(Block debug1, Block debug2, Block debug3, Block debug4, Block debug5, Block debug6, Block debug7, Block debug8) {
/*  718 */     createCrossBlockWithDefaultItem(debug1, TintState.NOT_TINTED);
/*  719 */     createCrossBlockWithDefaultItem(debug2, TintState.NOT_TINTED);
/*      */     
/*  721 */     createTrivialCube(debug3);
/*  722 */     createTrivialCube(debug4);
/*      */     
/*  724 */     createCoralFans(debug5, debug7);
/*  725 */     createCoralFans(debug6, debug8);
/*      */   }
/*      */   
/*      */   private void createDoublePlant(Block debug1, TintState debug2) {
/*  729 */     createSimpleFlatItemModel(debug1, "_top");
/*  730 */     ResourceLocation debug3 = createSuffixedVariant(debug1, "_top", debug2.getCross(), TextureMapping::cross);
/*  731 */     ResourceLocation debug4 = createSuffixedVariant(debug1, "_bottom", debug2.getCross(), TextureMapping::cross);
/*  732 */     createDoubleBlock(debug1, debug3, debug4);
/*      */   }
/*      */   
/*      */   private void createSunflower() {
/*  736 */     createSimpleFlatItemModel(Blocks.SUNFLOWER, "_front");
/*  737 */     ResourceLocation debug1 = ModelLocationUtils.getModelLocation(Blocks.SUNFLOWER, "_top");
/*  738 */     ResourceLocation debug2 = createSuffixedVariant(Blocks.SUNFLOWER, "_bottom", TintState.NOT_TINTED.getCross(), TextureMapping::cross);
/*  739 */     createDoubleBlock(Blocks.SUNFLOWER, debug1, debug2);
/*      */   }
/*      */   
/*      */   private void createTallSeagrass() {
/*  743 */     ResourceLocation debug1 = createSuffixedVariant(Blocks.TALL_SEAGRASS, "_top", ModelTemplates.SEAGRASS, TextureMapping::defaultTexture);
/*  744 */     ResourceLocation debug2 = createSuffixedVariant(Blocks.TALL_SEAGRASS, "_bottom", ModelTemplates.SEAGRASS, TextureMapping::defaultTexture);
/*  745 */     createDoubleBlock(Blocks.TALL_SEAGRASS, debug1, debug2);
/*      */   }
/*      */   
/*      */   private void createDoubleBlock(Block debug1, ResourceLocation debug2, ResourceLocation debug3) {
/*  749 */     this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(debug1)
/*  750 */         .with(
/*  751 */           (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.DOUBLE_BLOCK_HALF)
/*  752 */           .select((Comparable)DoubleBlockHalf.LOWER, Variant.variant().with(VariantProperties.MODEL, debug3))
/*  753 */           .select((Comparable)DoubleBlockHalf.UPPER, Variant.variant().with(VariantProperties.MODEL, debug2))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createPassiveRail(Block debug1) {
/*  759 */     TextureMapping debug2 = TextureMapping.rail(debug1);
/*  760 */     TextureMapping debug3 = TextureMapping.rail(TextureMapping.getBlockTexture(debug1, "_corner"));
/*      */     
/*  762 */     ResourceLocation debug4 = ModelTemplates.RAIL_FLAT.create(debug1, debug2, this.modelOutput);
/*  763 */     ResourceLocation debug5 = ModelTemplates.RAIL_CURVED.create(debug1, debug3, this.modelOutput);
/*  764 */     ResourceLocation debug6 = ModelTemplates.RAIL_RAISED_NE.create(debug1, debug2, this.modelOutput);
/*  765 */     ResourceLocation debug7 = ModelTemplates.RAIL_RAISED_SW.create(debug1, debug2, this.modelOutput);
/*      */     
/*  767 */     createSimpleFlatItemModel(debug1);
/*  768 */     this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(debug1)
/*  769 */         .with(
/*  770 */           (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.RAIL_SHAPE)
/*  771 */           .select((Comparable)RailShape.NORTH_SOUTH, Variant.variant().with(VariantProperties.MODEL, debug4))
/*  772 */           .select((Comparable)RailShape.EAST_WEST, Variant.variant().with(VariantProperties.MODEL, debug4).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*      */           
/*  774 */           .select((Comparable)RailShape.ASCENDING_EAST, Variant.variant().with(VariantProperties.MODEL, debug6).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  775 */           .select((Comparable)RailShape.ASCENDING_WEST, Variant.variant().with(VariantProperties.MODEL, debug7).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  776 */           .select((Comparable)RailShape.ASCENDING_NORTH, Variant.variant().with(VariantProperties.MODEL, debug6))
/*  777 */           .select((Comparable)RailShape.ASCENDING_SOUTH, Variant.variant().with(VariantProperties.MODEL, debug7))
/*      */           
/*  779 */           .select((Comparable)RailShape.SOUTH_EAST, Variant.variant().with(VariantProperties.MODEL, debug5))
/*  780 */           .select((Comparable)RailShape.SOUTH_WEST, Variant.variant().with(VariantProperties.MODEL, debug5).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  781 */           .select((Comparable)RailShape.NORTH_WEST, Variant.variant().with(VariantProperties.MODEL, debug5).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  782 */           .select((Comparable)RailShape.NORTH_EAST, Variant.variant().with(VariantProperties.MODEL, debug5).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createActiveRail(Block debug1) {
/*  788 */     ResourceLocation debug2 = createSuffixedVariant(debug1, "", ModelTemplates.RAIL_FLAT, TextureMapping::rail);
/*  789 */     ResourceLocation debug3 = createSuffixedVariant(debug1, "", ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail);
/*  790 */     ResourceLocation debug4 = createSuffixedVariant(debug1, "", ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail);
/*      */     
/*  792 */     ResourceLocation debug5 = createSuffixedVariant(debug1, "_on", ModelTemplates.RAIL_FLAT, TextureMapping::rail);
/*  793 */     ResourceLocation debug6 = createSuffixedVariant(debug1, "_on", ModelTemplates.RAIL_RAISED_NE, TextureMapping::rail);
/*  794 */     ResourceLocation debug7 = createSuffixedVariant(debug1, "_on", ModelTemplates.RAIL_RAISED_SW, TextureMapping::rail);
/*      */ 
/*      */ 
/*      */     
/*  798 */     PropertyDispatch debug8 = PropertyDispatch.properties((Property)BlockStateProperties.POWERED, (Property)BlockStateProperties.RAIL_SHAPE_STRAIGHT).generate((debug6, debug7) -> {
/*      */           switch (debug7) {
/*      */             case NORTH_SOUTH:
/*      */               return Variant.variant().with(VariantProperties.MODEL, debug6.booleanValue() ? debug0 : debug1);
/*      */             
/*      */             case EAST_WEST:
/*      */               return Variant.variant().with(VariantProperties.MODEL, debug6.booleanValue() ? debug0 : debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
/*      */             
/*      */             case ASCENDING_EAST:
/*      */               return Variant.variant().with(VariantProperties.MODEL, debug6.booleanValue() ? debug2 : debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
/*      */             case ASCENDING_WEST:
/*      */               return Variant.variant().with(VariantProperties.MODEL, debug6.booleanValue() ? debug4 : debug5).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
/*      */             case ASCENDING_NORTH:
/*      */               return Variant.variant().with(VariantProperties.MODEL, debug6.booleanValue() ? debug2 : debug3);
/*      */             case ASCENDING_SOUTH:
/*      */               return Variant.variant().with(VariantProperties.MODEL, debug6.booleanValue() ? debug4 : debug5);
/*      */           } 
/*      */           throw new UnsupportedOperationException("Fix you generator!");
/*      */         });
/*  817 */     createSimpleFlatItemModel(debug1);
/*  818 */     this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(debug1).with(debug8));
/*      */   }
/*      */   
/*      */   class BlockEntityModelGenerator {
/*      */     private final ResourceLocation baseModel;
/*      */     
/*      */     public BlockEntityModelGenerator(ResourceLocation debug2, Block debug3) {
/*  825 */       this.baseModel = ModelTemplates.PARTICLE_ONLY.create(debug2, TextureMapping.particle(debug3), BlockModelGenerators.this.modelOutput);
/*      */     }
/*      */     
/*      */     public BlockEntityModelGenerator create(Block... debug1) {
/*  829 */       for (Block debug5 : debug1) {
/*  830 */         BlockModelGenerators.this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(debug5, this.baseModel));
/*      */       }
/*  832 */       return this;
/*      */     }
/*      */     
/*      */     public BlockEntityModelGenerator createWithoutBlockItem(Block... debug1) {
/*  836 */       for (Block debug5 : debug1) {
/*  837 */         BlockModelGenerators.this.skipAutoItemBlock(debug5);
/*      */       }
/*  839 */       return create(debug1);
/*      */     }
/*      */     
/*      */     public BlockEntityModelGenerator createWithCustomBlockItemModel(ModelTemplate debug1, Block... debug2) {
/*  843 */       for (Block debug6 : debug2) {
/*  844 */         debug1.create(ModelLocationUtils.getModelLocation(debug6.asItem()), TextureMapping.particle(debug6), BlockModelGenerators.this.modelOutput);
/*      */       }
/*  846 */       return create(debug2);
/*      */     }
/*      */   }
/*      */   
/*      */   private BlockEntityModelGenerator blockEntityModels(ResourceLocation debug1, Block debug2) {
/*  851 */     return new BlockEntityModelGenerator(debug1, debug2);
/*      */   }
/*      */   
/*      */   private BlockEntityModelGenerator blockEntityModels(Block debug1, Block debug2) {
/*  855 */     return new BlockEntityModelGenerator(ModelLocationUtils.getModelLocation(debug1), debug2);
/*      */   }
/*      */   
/*      */   private void createAirLikeBlock(Block debug1, Item debug2) {
/*  859 */     ResourceLocation debug3 = ModelTemplates.PARTICLE_ONLY.create(debug1, TextureMapping.particleFromItem(debug2), this.modelOutput);
/*  860 */     this.blockStateOutput.accept(createSimpleBlock(debug1, debug3));
/*      */   }
/*      */   
/*      */   private void createAirLikeBlock(Block debug1, ResourceLocation debug2) {
/*  864 */     ResourceLocation debug3 = ModelTemplates.PARTICLE_ONLY.create(debug1, TextureMapping.particle(debug2), this.modelOutput);
/*  865 */     this.blockStateOutput.accept(createSimpleBlock(debug1, debug3));
/*      */   }
/*      */   
/*      */   private void createWoolBlocks(Block debug1, Block debug2) {
/*  869 */     createTrivialBlock(debug1, TexturedModel.CUBE);
/*      */ 
/*      */     
/*  872 */     ResourceLocation debug3 = TexturedModel.CARPET.get(debug1).create(debug2, this.modelOutput);
/*  873 */     this.blockStateOutput.accept(createSimpleBlock(debug2, debug3));
/*      */   }
/*      */   
/*      */   private void createColoredBlockWithRandomRotations(TexturedModel.Provider debug1, Block... debug2) {
/*  877 */     for (Block debug6 : debug2) {
/*  878 */       ResourceLocation debug7 = debug1.create(debug6, this.modelOutput);
/*  879 */       this.blockStateOutput.accept(createRotatedVariant(debug6, debug7));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void createColoredBlockWithStateRotations(TexturedModel.Provider debug1, Block... debug2) {
/*  884 */     for (Block debug6 : debug2) {
/*  885 */       ResourceLocation debug7 = debug1.create(debug6, this.modelOutput);
/*  886 */       this.blockStateOutput.accept(
/*  887 */           MultiVariantGenerator.multiVariant(debug6, Variant.variant().with(VariantProperties.MODEL, debug7))
/*  888 */           .with(createHorizontalFacingDispatchAlt()));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void createGlassBlocks(Block debug1, Block debug2) {
/*  894 */     createTrivialCube(debug1);
/*      */     
/*  896 */     TextureMapping debug3 = TextureMapping.pane(debug1, debug2);
/*  897 */     ResourceLocation debug4 = ModelTemplates.STAINED_GLASS_PANE_POST.create(debug2, debug3, this.modelOutput);
/*  898 */     ResourceLocation debug5 = ModelTemplates.STAINED_GLASS_PANE_SIDE.create(debug2, debug3, this.modelOutput);
/*  899 */     ResourceLocation debug6 = ModelTemplates.STAINED_GLASS_PANE_SIDE_ALT.create(debug2, debug3, this.modelOutput);
/*  900 */     ResourceLocation debug7 = ModelTemplates.STAINED_GLASS_PANE_NOSIDE.create(debug2, debug3, this.modelOutput);
/*  901 */     ResourceLocation debug8 = ModelTemplates.STAINED_GLASS_PANE_NOSIDE_ALT.create(debug2, debug3, this.modelOutput);
/*      */     
/*  903 */     Item debug9 = debug2.asItem();
/*  904 */     ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(debug9), TextureMapping.layer0(debug1), this.modelOutput);
/*      */     
/*  906 */     this.blockStateOutput.accept(
/*  907 */         MultiPartGenerator.multiPart(debug2)
/*  908 */         .with(Variant.variant().with(VariantProperties.MODEL, debug4))
/*  909 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug5))
/*  910 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug5).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  911 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug6))
/*  912 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug6).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*      */         
/*  914 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false)), Variant.variant().with(VariantProperties.MODEL, debug7))
/*  915 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(false)), Variant.variant().with(VariantProperties.MODEL, debug8))
/*  916 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false)), Variant.variant().with(VariantProperties.MODEL, debug8).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*  917 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)), Variant.variant().with(VariantProperties.MODEL, debug7).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createCommandBlock(Block debug1) {
/*  922 */     TextureMapping debug2 = TextureMapping.commandBlock(debug1);
/*      */     
/*  924 */     ResourceLocation debug3 = ModelTemplates.COMMAND_BLOCK.create(debug1, debug2, this.modelOutput);
/*  925 */     ResourceLocation debug4 = createSuffixedVariant(debug1, "_conditional", ModelTemplates.COMMAND_BLOCK, debug1 -> debug0.copyAndUpdate(TextureSlot.SIDE, debug1));
/*      */     
/*  927 */     this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(debug1)
/*  928 */         .with(createBooleanModelDispatch(BlockStateProperties.CONDITIONAL, debug4, debug3))
/*  929 */         .with(createFacingDispatch()));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createAnvil(Block debug1) {
/*  934 */     ResourceLocation debug2 = TexturedModel.ANVIL.create(debug1, this.modelOutput);
/*  935 */     this.blockStateOutput.accept(createSimpleBlock(debug1, debug2).with(createHorizontalFacingDispatchAlt()));
/*      */   }
/*      */   
/*      */   private List<Variant> createBambooModels(int debug1) {
/*  939 */     String debug2 = "_age" + debug1;
/*  940 */     return (List<Variant>)IntStream.range(1, 5)
/*  941 */       .mapToObj(debug1 -> Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.BAMBOO, debug1 + debug0)))
/*  942 */       .collect(Collectors.toList());
/*      */   }
/*      */   
/*      */   private void createBamboo() {
/*  946 */     skipAutoItemBlock(Blocks.BAMBOO);
/*  947 */     this.blockStateOutput.accept(
/*  948 */         MultiPartGenerator.multiPart(Blocks.BAMBOO)
/*  949 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.AGE_1, Integer.valueOf(0)), createBambooModels(0))
/*  950 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.AGE_1, Integer.valueOf(1)), createBambooModels(1))
/*  951 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.BAMBOO_LEAVES, (Comparable)BambooLeaves.SMALL), 
/*  952 */           Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.BAMBOO, "_small_leaves")))
/*      */         
/*  954 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.BAMBOO_LEAVES, (Comparable)BambooLeaves.LARGE), 
/*  955 */           Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.BAMBOO, "_large_leaves"))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private PropertyDispatch createColumnWithFacing() {
/*  961 */     return (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.FACING)
/*  962 */       .select((Comparable)Direction.DOWN, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
/*  963 */       .select((Comparable)Direction.UP, Variant.variant())
/*  964 */       .select((Comparable)Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
/*  965 */       .select((Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*  966 */       .select((Comparable)Direction.WEST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*  967 */       .select((Comparable)Direction.EAST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
/*      */   }
/*      */   
/*      */   private void createBarrel() {
/*  971 */     ResourceLocation debug1 = TextureMapping.getBlockTexture(Blocks.BARREL, "_top_open");
/*      */     
/*  973 */     this.blockStateOutput.accept(
/*  974 */         MultiVariantGenerator.multiVariant(Blocks.BARREL)
/*  975 */         .with(createColumnWithFacing())
/*  976 */         .with((PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.OPEN)
/*  977 */           .select(Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, TexturedModel.CUBE_TOP_BOTTOM.create(Blocks.BARREL, this.modelOutput)))
/*  978 */           .select(Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, TexturedModel.CUBE_TOP_BOTTOM.get(Blocks.BARREL).updateTextures(debug1 -> debug1.put(TextureSlot.TOP, debug0)).createWithSuffix(Blocks.BARREL, "_open", this.modelOutput)))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T extends Comparable<T>> PropertyDispatch createEmptyOrFullDispatch(Property<T> debug0, T debug1, ResourceLocation debug2, ResourceLocation debug3) {
/*  984 */     Variant debug4 = Variant.variant().with(VariantProperties.MODEL, debug2);
/*  985 */     Variant debug5 = Variant.variant().with(VariantProperties.MODEL, debug3);
/*      */     
/*  987 */     return PropertyDispatch.property(debug0).generate(debug3 -> {
/*      */           boolean debug4 = (debug3.compareTo(debug0) >= 0);
/*      */           return debug4 ? debug1 : debug2;
/*      */         });
/*      */   }
/*      */   
/*      */   private void createBeeNest(Block debug1, Function<Block, TextureMapping> debug2) {
/*  994 */     TextureMapping debug3 = ((TextureMapping)debug2.apply(debug1)).copyForced(TextureSlot.SIDE, TextureSlot.PARTICLE);
/*  995 */     TextureMapping debug4 = debug3.copyAndUpdate(TextureSlot.FRONT, TextureMapping.getBlockTexture(debug1, "_front_honey"));
/*      */     
/*  997 */     ResourceLocation debug5 = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.create(debug1, debug3, this.modelOutput);
/*  998 */     ResourceLocation debug6 = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.createWithSuffix(debug1, "_honey", debug4, this.modelOutput);
/*      */     
/* 1000 */     this.blockStateOutput.accept(
/* 1001 */         MultiVariantGenerator.multiVariant(debug1)
/* 1002 */         .with(createHorizontalFacingDispatch())
/* 1003 */         .with(createEmptyOrFullDispatch((Property<Integer>)BlockStateProperties.LEVEL_HONEY, Integer.valueOf(5), debug6, debug5)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createCropBlock(Block debug1, Property<Integer> debug2, int... debug3) {
/* 1008 */     if (debug2.getPossibleValues().size() != debug3.length) {
/* 1009 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/* 1012 */     Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();
/*      */     
/* 1014 */     PropertyDispatch debug5 = PropertyDispatch.property(debug2).generate(debug4 -> {
/*      */           int debug5 = debug1[debug4.intValue()];
/*      */           
/*      */           ResourceLocation debug6 = (ResourceLocation)debug2.computeIfAbsent(debug5, ());
/*      */           return Variant.variant().with(VariantProperties.MODEL, debug6);
/*      */         });
/* 1020 */     createSimpleFlatItemModel(debug1.asItem());
/* 1021 */     this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(debug1).with(debug5));
/*      */   }
/*      */   
/*      */   private void createBell() {
/* 1025 */     ResourceLocation debug1 = ModelLocationUtils.getModelLocation(Blocks.BELL, "_floor");
/* 1026 */     ResourceLocation debug2 = ModelLocationUtils.getModelLocation(Blocks.BELL, "_ceiling");
/* 1027 */     ResourceLocation debug3 = ModelLocationUtils.getModelLocation(Blocks.BELL, "_wall");
/* 1028 */     ResourceLocation debug4 = ModelLocationUtils.getModelLocation(Blocks.BELL, "_between_walls");
/*      */     
/* 1030 */     createSimpleFlatItemModel(Items.BELL);
/* 1031 */     this.blockStateOutput.accept(
/* 1032 */         MultiVariantGenerator.multiVariant(Blocks.BELL)
/* 1033 */         .with(
/* 1034 */           (PropertyDispatch)PropertyDispatch.properties((Property)BlockStateProperties.HORIZONTAL_FACING, (Property)BlockStateProperties.BELL_ATTACHMENT)
/* 1035 */           .select((Comparable)Direction.NORTH, (Comparable)BellAttachType.FLOOR, Variant.variant().with(VariantProperties.MODEL, debug1))
/* 1036 */           .select((Comparable)Direction.SOUTH, (Comparable)BellAttachType.FLOOR, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 1037 */           .select((Comparable)Direction.EAST, (Comparable)BellAttachType.FLOOR, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 1038 */           .select((Comparable)Direction.WEST, (Comparable)BellAttachType.FLOOR, Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */           
/* 1040 */           .select((Comparable)Direction.NORTH, (Comparable)BellAttachType.CEILING, Variant.variant().with(VariantProperties.MODEL, debug2))
/* 1041 */           .select((Comparable)Direction.SOUTH, (Comparable)BellAttachType.CEILING, Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 1042 */           .select((Comparable)Direction.EAST, (Comparable)BellAttachType.CEILING, Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 1043 */           .select((Comparable)Direction.WEST, (Comparable)BellAttachType.CEILING, Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */           
/* 1045 */           .select((Comparable)Direction.NORTH, (Comparable)BellAttachType.SINGLE_WALL, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/* 1046 */           .select((Comparable)Direction.SOUTH, (Comparable)BellAttachType.SINGLE_WALL, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 1047 */           .select((Comparable)Direction.EAST, (Comparable)BellAttachType.SINGLE_WALL, Variant.variant().with(VariantProperties.MODEL, debug3))
/* 1048 */           .select((Comparable)Direction.WEST, (Comparable)BellAttachType.SINGLE_WALL, Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*      */           
/* 1050 */           .select((Comparable)Direction.SOUTH, (Comparable)BellAttachType.DOUBLE_WALL, Variant.variant().with(VariantProperties.MODEL, debug4).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 1051 */           .select((Comparable)Direction.NORTH, (Comparable)BellAttachType.DOUBLE_WALL, Variant.variant().with(VariantProperties.MODEL, debug4).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/* 1052 */           .select((Comparable)Direction.EAST, (Comparable)BellAttachType.DOUBLE_WALL, Variant.variant().with(VariantProperties.MODEL, debug4))
/* 1053 */           .select((Comparable)Direction.WEST, (Comparable)BellAttachType.DOUBLE_WALL, Variant.variant().with(VariantProperties.MODEL, debug4).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createGrindstone() {
/* 1059 */     this.blockStateOutput.accept(
/* 1060 */         MultiVariantGenerator.multiVariant(Blocks.GRINDSTONE, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.GRINDSTONE)))
/* 1061 */         .with(
/* 1062 */           (PropertyDispatch)PropertyDispatch.properties((Property)BlockStateProperties.ATTACH_FACE, (Property)BlockStateProperties.HORIZONTAL_FACING)
/* 1063 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.NORTH, Variant.variant())
/* 1064 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 1065 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 1066 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */           
/* 1068 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
/* 1069 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.EAST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 1070 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 1071 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.WEST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */           
/* 1073 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
/* 1074 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.WEST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 1075 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 1076 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.EAST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createFurnace(Block debug1, TexturedModel.Provider debug2) {
/* 1082 */     ResourceLocation debug3 = debug2.create(debug1, this.modelOutput);
/*      */     
/* 1084 */     ResourceLocation debug4 = TextureMapping.getBlockTexture(debug1, "_front_on");
/* 1085 */     ResourceLocation debug5 = debug2.get(debug1).updateTextures(debug1 -> debug1.put(TextureSlot.FRONT, debug0)).createWithSuffix(debug1, "_on", this.modelOutput);
/*      */     
/* 1087 */     this.blockStateOutput.accept(
/* 1088 */         MultiVariantGenerator.multiVariant(debug1)
/* 1089 */         .with(createBooleanModelDispatch(BlockStateProperties.LIT, debug5, debug3))
/* 1090 */         .with(createHorizontalFacingDispatch()));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createCampfires(Block... debug1) {
/* 1095 */     ResourceLocation debug2 = ModelLocationUtils.decorateBlockModelLocation("campfire_off");
/*      */     
/* 1097 */     for (Block debug6 : debug1) {
/* 1098 */       ResourceLocation debug7 = ModelTemplates.CAMPFIRE.create(debug6, TextureMapping.campfire(debug6), this.modelOutput);
/*      */       
/* 1100 */       createSimpleFlatItemModel(debug6.asItem());
/* 1101 */       this.blockStateOutput.accept(
/* 1102 */           MultiVariantGenerator.multiVariant(debug6)
/* 1103 */           .with(createBooleanModelDispatch(BlockStateProperties.LIT, debug7, debug2))
/* 1104 */           .with(createHorizontalFacingDispatchAlt()));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void createBookshelf() {
/* 1110 */     TextureMapping debug1 = TextureMapping.column(TextureMapping.getBlockTexture(Blocks.BOOKSHELF), TextureMapping.getBlockTexture(Blocks.OAK_PLANKS));
/* 1111 */     ResourceLocation debug2 = ModelTemplates.CUBE_COLUMN.create(Blocks.BOOKSHELF, debug1, this.modelOutput);
/* 1112 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.BOOKSHELF, debug2));
/*      */   }
/*      */   
/*      */   private void createRedstoneWire() {
/* 1116 */     createSimpleFlatItemModel(Items.REDSTONE);
/* 1117 */     this.blockStateOutput.accept(
/* 1118 */         MultiPartGenerator.multiPart(Blocks.REDSTONE_WIRE)
/* 1119 */         .with(
/* 1120 */           Condition.or(new Condition[] {
/* 1121 */               (Condition)Condition.condition()
/* 1122 */               .term((Property)BlockStateProperties.NORTH_REDSTONE, (Comparable)RedstoneSide.NONE)
/* 1123 */               .term((Property)BlockStateProperties.EAST_REDSTONE, (Comparable)RedstoneSide.NONE)
/* 1124 */               .term((Property)BlockStateProperties.SOUTH_REDSTONE, (Comparable)RedstoneSide.NONE)
/* 1125 */               .term((Property)BlockStateProperties.WEST_REDSTONE, (Comparable)RedstoneSide.NONE), 
/* 1126 */               (Condition)Condition.condition()
/* 1127 */               .term((Property)BlockStateProperties.NORTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1128 */                 }).term((Property)BlockStateProperties.EAST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1129 */                 }), (Condition)Condition.condition()
/* 1130 */               .term((Property)BlockStateProperties.EAST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1131 */                 }).term((Property)BlockStateProperties.SOUTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1132 */                 }), (Condition)Condition.condition()
/* 1133 */               .term((Property)BlockStateProperties.SOUTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1134 */                 }).term((Property)BlockStateProperties.WEST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1135 */                 }), (Condition)Condition.condition()
/* 1136 */               .term((Property)BlockStateProperties.WEST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1137 */                 }).term((Property)BlockStateProperties.NORTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/*      */                 })
/* 1139 */             }), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_dot")))
/*      */         
/* 1141 */         .with(
/* 1142 */           (Condition)Condition.condition()
/* 1143 */           .term((Property)BlockStateProperties.NORTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1144 */             }), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side0")))
/*      */         
/* 1146 */         .with(
/* 1147 */           (Condition)Condition.condition()
/* 1148 */           .term((Property)BlockStateProperties.SOUTH_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1149 */             }), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side_alt0")))
/*      */         
/* 1151 */         .with(
/* 1152 */           (Condition)Condition.condition()
/* 1153 */           .term((Property)BlockStateProperties.EAST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1154 */             }), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side_alt1")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */         
/* 1156 */         .with(
/* 1157 */           (Condition)Condition.condition()
/* 1158 */           .term((Property)BlockStateProperties.WEST_REDSTONE, (Comparable)RedstoneSide.SIDE, (Comparable[])new RedstoneSide[] { RedstoneSide.UP
/* 1159 */             }), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_side1")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */         
/* 1161 */         .with(
/* 1162 */           (Condition)Condition.condition().term((Property)BlockStateProperties.NORTH_REDSTONE, (Comparable)RedstoneSide.UP), 
/* 1163 */           Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up")))
/*      */         
/* 1165 */         .with(
/* 1166 */           (Condition)Condition.condition().term((Property)BlockStateProperties.EAST_REDSTONE, (Comparable)RedstoneSide.UP), 
/* 1167 */           Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*      */         
/* 1169 */         .with(
/* 1170 */           (Condition)Condition.condition().term((Property)BlockStateProperties.SOUTH_REDSTONE, (Comparable)RedstoneSide.UP), 
/* 1171 */           Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/*      */         
/* 1173 */         .with(
/* 1174 */           (Condition)Condition.condition().term((Property)BlockStateProperties.WEST_REDSTONE, (Comparable)RedstoneSide.UP), 
/* 1175 */           Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.decorateBlockModelLocation("redstone_dust_up")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createComparator() {
/* 1181 */     createSimpleFlatItemModel(Items.COMPARATOR);
/* 1182 */     this.blockStateOutput.accept(
/* 1183 */         MultiVariantGenerator.multiVariant(Blocks.COMPARATOR)
/* 1184 */         .with(createHorizontalFacingDispatchAlt())
/* 1185 */         .with(
/* 1186 */           (PropertyDispatch)PropertyDispatch.properties((Property)BlockStateProperties.MODE_COMPARATOR, (Property)BlockStateProperties.POWERED)
/* 1187 */           .select((Comparable)ComparatorMode.COMPARE, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.COMPARATOR)))
/* 1188 */           .select((Comparable)ComparatorMode.COMPARE, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.COMPARATOR, "_on")))
/* 1189 */           .select((Comparable)ComparatorMode.SUBTRACT, Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.COMPARATOR, "_subtract")))
/* 1190 */           .select((Comparable)ComparatorMode.SUBTRACT, Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.COMPARATOR, "_on_subtract")))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createSmoothStoneSlab() {
/* 1196 */     TextureMapping debug1 = TextureMapping.cube(Blocks.SMOOTH_STONE);
/* 1197 */     TextureMapping debug2 = TextureMapping.column(
/* 1198 */         TextureMapping.getBlockTexture(Blocks.SMOOTH_STONE_SLAB, "_side"), debug1
/* 1199 */         .get(TextureSlot.TOP));
/*      */     
/* 1201 */     ResourceLocation debug3 = ModelTemplates.SLAB_BOTTOM.create(Blocks.SMOOTH_STONE_SLAB, debug2, this.modelOutput);
/* 1202 */     ResourceLocation debug4 = ModelTemplates.SLAB_TOP.create(Blocks.SMOOTH_STONE_SLAB, debug2, this.modelOutput);
/* 1203 */     ResourceLocation debug5 = ModelTemplates.CUBE_COLUMN.createWithOverride(Blocks.SMOOTH_STONE_SLAB, "_double", debug2, this.modelOutput);
/* 1204 */     this.blockStateOutput.accept(createSlab(Blocks.SMOOTH_STONE_SLAB, debug3, debug4, debug5));
/* 1205 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.SMOOTH_STONE, ModelTemplates.CUBE_ALL.create(Blocks.SMOOTH_STONE, debug1, this.modelOutput)));
/*      */   }
/*      */   
/*      */   private void createBrewingStand() {
/* 1209 */     createSimpleFlatItemModel(Items.BREWING_STAND);
/* 1210 */     this.blockStateOutput.accept(
/* 1211 */         MultiPartGenerator.multiPart(Blocks.BREWING_STAND)
/* 1212 */         .with(Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.BREWING_STAND)))
/* 1213 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.HAS_BOTTLE_0, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_bottle0")))
/* 1214 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.HAS_BOTTLE_1, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_bottle1")))
/* 1215 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.HAS_BOTTLE_2, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_bottle2")))
/*      */         
/* 1217 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.HAS_BOTTLE_0, Boolean.valueOf(false)), Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_empty0")))
/* 1218 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.HAS_BOTTLE_1, Boolean.valueOf(false)), Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_empty1")))
/* 1219 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.HAS_BOTTLE_2, Boolean.valueOf(false)), Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.BREWING_STAND, "_empty2"))));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createMushroomBlock(Block debug1) {
/* 1224 */     ResourceLocation debug2 = ModelTemplates.SINGLE_FACE.create(debug1, TextureMapping.defaultTexture(debug1), this.modelOutput);
/* 1225 */     ResourceLocation debug3 = ModelLocationUtils.decorateBlockModelLocation("mushroom_block_inside");
/*      */     
/* 1227 */     this.blockStateOutput.accept(
/* 1228 */         MultiPartGenerator.multiPart(debug1)
/* 1229 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug2))
/* 1230 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/* 1231 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/* 1232 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/* 1233 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.UP, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/* 1234 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.DOWN, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*      */         
/* 1236 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false)), Variant.variant().with(VariantProperties.MODEL, debug3))
/* 1237 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(false)), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(false)))
/* 1238 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false)), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(false)))
/* 1239 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(false)))
/* 1240 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.UP, Boolean.valueOf(false)), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(false)))
/* 1241 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.DOWN, Boolean.valueOf(false)), Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(false))));
/*      */ 
/*      */     
/* 1244 */     delegateItemModel(debug1, TexturedModel.CUBE.createWithSuffix(debug1, "_inventory", this.modelOutput));
/*      */   }
/*      */   
/*      */   private void createCakeBlock() {
/* 1248 */     createSimpleFlatItemModel(Items.CAKE);
/* 1249 */     this.blockStateOutput.accept(
/* 1250 */         MultiVariantGenerator.multiVariant(Blocks.CAKE)
/* 1251 */         .with(
/* 1252 */           (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.BITES)
/* 1253 */           .select(Integer.valueOf(0), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE)))
/* 1254 */           .select(Integer.valueOf(1), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice1")))
/* 1255 */           .select(Integer.valueOf(2), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice2")))
/* 1256 */           .select(Integer.valueOf(3), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice3")))
/* 1257 */           .select(Integer.valueOf(4), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice4")))
/* 1258 */           .select(Integer.valueOf(5), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice5")))
/* 1259 */           .select(Integer.valueOf(6), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice6")))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createCartographyTable() {
/* 1272 */     TextureMapping debug1 = (new TextureMapping()).put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side3")).put(TextureSlot.DOWN, TextureMapping.getBlockTexture(Blocks.DARK_OAK_PLANKS)).put(TextureSlot.UP, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_top")).put(TextureSlot.NORTH, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side3")).put(TextureSlot.EAST, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side3")).put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side1")).put(TextureSlot.WEST, TextureMapping.getBlockTexture(Blocks.CARTOGRAPHY_TABLE, "_side2"));
/*      */     
/* 1274 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.CARTOGRAPHY_TABLE, ModelTemplates.CUBE.create(Blocks.CARTOGRAPHY_TABLE, debug1, this.modelOutput)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createSmithingTable() {
/* 1285 */     TextureMapping debug1 = (new TextureMapping()).put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_front")).put(TextureSlot.DOWN, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_bottom")).put(TextureSlot.UP, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_top")).put(TextureSlot.NORTH, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_front")).put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_front")).put(TextureSlot.EAST, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_side")).put(TextureSlot.WEST, TextureMapping.getBlockTexture(Blocks.SMITHING_TABLE, "_side"));
/*      */     
/* 1287 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.SMITHING_TABLE, ModelTemplates.CUBE.create(Blocks.SMITHING_TABLE, debug1, this.modelOutput)));
/*      */   }
/*      */   
/*      */   private void createCraftingTableLike(Block debug1, Block debug2, BiFunction<Block, Block, TextureMapping> debug3) {
/* 1291 */     TextureMapping debug4 = debug3.apply(debug1, debug2);
/* 1292 */     this.blockStateOutput.accept(createSimpleBlock(debug1, ModelTemplates.CUBE.create(debug1, debug4, this.modelOutput)));
/*      */   }
/*      */   
/*      */   private void createPumpkins() {
/* 1296 */     TextureMapping debug1 = TextureMapping.column(Blocks.PUMPKIN);
/*      */     
/* 1298 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.PUMPKIN, ModelLocationUtils.getModelLocation(Blocks.PUMPKIN)));
/*      */     
/* 1300 */     createPumpkinVariant(Blocks.CARVED_PUMPKIN, debug1);
/* 1301 */     createPumpkinVariant(Blocks.JACK_O_LANTERN, debug1);
/*      */   }
/*      */   
/*      */   private void createPumpkinVariant(Block debug1, TextureMapping debug2) {
/* 1305 */     ResourceLocation debug3 = ModelTemplates.CUBE_ORIENTABLE.create(debug1, debug2.copyAndUpdate(TextureSlot.FRONT, TextureMapping.getBlockTexture(debug1)), this.modelOutput);
/* 1306 */     this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(debug1, Variant.variant().with(VariantProperties.MODEL, debug3)).with(createHorizontalFacingDispatch()));
/*      */   }
/*      */   
/*      */   private void createCauldron() {
/* 1310 */     createSimpleFlatItemModel(Items.CAULDRON);
/* 1311 */     this.blockStateOutput.accept(
/* 1312 */         MultiVariantGenerator.multiVariant(Blocks.CAULDRON)
/* 1313 */         .with(
/* 1314 */           (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.LEVEL_CAULDRON)
/* 1315 */           .select(Integer.valueOf(0), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAULDRON)))
/* 1316 */           .select(Integer.valueOf(1), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAULDRON, "_level1")))
/* 1317 */           .select(Integer.valueOf(2), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAULDRON, "_level2")))
/* 1318 */           .select(Integer.valueOf(3), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAULDRON, "_level3")))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createChiseledSandsone(Block debug1, Block debug2) {
/* 1326 */     TextureMapping debug3 = (new TextureMapping()).put(TextureSlot.END, TextureMapping.getBlockTexture(debug2, "_top")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(debug1));
/*      */     
/* 1328 */     createTrivialBlock(debug1, debug3, ModelTemplates.CUBE_COLUMN);
/*      */   }
/*      */   
/*      */   private void createChorusFlower() {
/* 1332 */     TextureMapping debug1 = TextureMapping.defaultTexture(Blocks.CHORUS_FLOWER);
/* 1333 */     ResourceLocation debug2 = ModelTemplates.CHORUS_FLOWER.create(Blocks.CHORUS_FLOWER, debug1, this.modelOutput);
/* 1334 */     ResourceLocation debug3 = createSuffixedVariant(Blocks.CHORUS_FLOWER, "_dead", ModelTemplates.CHORUS_FLOWER, debug1 -> debug0.copyAndUpdate(TextureSlot.TEXTURE, debug1));
/*      */     
/* 1336 */     this.blockStateOutput.accept(
/* 1337 */         MultiVariantGenerator.multiVariant(Blocks.CHORUS_FLOWER)
/* 1338 */         .with(createEmptyOrFullDispatch((Property<Integer>)BlockStateProperties.AGE_5, Integer.valueOf(5), debug3, debug2)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createDispenserBlock(Block debug1) {
/* 1346 */     TextureMapping debug2 = (new TextureMapping()).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.FURNACE, "_top")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.FURNACE, "_side")).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(debug1, "_front"));
/*      */ 
/*      */ 
/*      */     
/* 1350 */     TextureMapping debug3 = (new TextureMapping()).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.FURNACE, "_top")).put(TextureSlot.FRONT, TextureMapping.getBlockTexture(debug1, "_front_vertical"));
/*      */     
/* 1352 */     ResourceLocation debug4 = ModelTemplates.CUBE_ORIENTABLE.create(debug1, debug2, this.modelOutput);
/* 1353 */     ResourceLocation debug5 = ModelTemplates.CUBE_ORIENTABLE_VERTICAL.create(debug1, debug3, this.modelOutput);
/*      */     
/* 1355 */     this.blockStateOutput.accept(
/* 1356 */         MultiVariantGenerator.multiVariant(debug1)
/* 1357 */         .with(
/* 1358 */           (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.FACING)
/* 1359 */           .select((Comparable)Direction.DOWN, Variant.variant().with(VariantProperties.MODEL, debug5).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
/* 1360 */           .select((Comparable)Direction.UP, Variant.variant().with(VariantProperties.MODEL, debug5))
/*      */           
/* 1362 */           .select((Comparable)Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, debug4))
/* 1363 */           .select((Comparable)Direction.EAST, Variant.variant().with(VariantProperties.MODEL, debug4).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 1364 */           .select((Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, debug4).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 1365 */           .select((Comparable)Direction.WEST, Variant.variant().with(VariantProperties.MODEL, debug4).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createEndPortalFrame() {
/* 1372 */     ResourceLocation debug1 = ModelLocationUtils.getModelLocation(Blocks.END_PORTAL_FRAME);
/* 1373 */     ResourceLocation debug2 = ModelLocationUtils.getModelLocation(Blocks.END_PORTAL_FRAME, "_filled");
/*      */     
/* 1375 */     this.blockStateOutput.accept(
/* 1376 */         MultiVariantGenerator.multiVariant(Blocks.END_PORTAL_FRAME)
/* 1377 */         .with(
/* 1378 */           (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.EYE)
/* 1379 */           .select(Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, debug1))
/* 1380 */           .select(Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, debug2)))
/*      */         
/* 1382 */         .with(
/* 1383 */           createHorizontalFacingDispatchAlt()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createChorusPlant() {
/* 1389 */     ResourceLocation debug1 = ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_side");
/* 1390 */     ResourceLocation debug2 = ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside");
/* 1391 */     ResourceLocation debug3 = ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside1");
/* 1392 */     ResourceLocation debug4 = ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside2");
/* 1393 */     ResourceLocation debug5 = ModelLocationUtils.getModelLocation(Blocks.CHORUS_PLANT, "_noside3");
/*      */     
/* 1395 */     this.blockStateOutput.accept(
/* 1396 */         MultiPartGenerator.multiPart(Blocks.CHORUS_PLANT)
/* 1397 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug1))
/* 1398 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/* 1399 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/* 1400 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/* 1401 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.UP, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/* 1402 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.DOWN, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)))
/*      */         
/* 1404 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false)), new Variant[] {
/* 1405 */             Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.WEIGHT, Integer.valueOf(2)), 
/* 1406 */             Variant.variant().with(VariantProperties.MODEL, debug3), 
/* 1407 */             Variant.variant().with(VariantProperties.MODEL, debug4), 
/* 1408 */             Variant.variant().with(VariantProperties.MODEL, debug5)
/*      */ 
/*      */           
/* 1411 */           }).with((Condition)Condition.condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(false)), new Variant[] {
/* 1412 */             Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)), 
/* 1413 */             Variant.variant().with(VariantProperties.MODEL, debug4).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)), 
/* 1414 */             Variant.variant().with(VariantProperties.MODEL, debug5).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)), 
/* 1415 */             Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.WEIGHT, Integer.valueOf(2)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true))
/*      */ 
/*      */           
/* 1418 */           }).with((Condition)Condition.condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false)), new Variant[] {
/* 1419 */             Variant.variant().with(VariantProperties.MODEL, debug4).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)), 
/* 1420 */             Variant.variant().with(VariantProperties.MODEL, debug5).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)), 
/* 1421 */             Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.WEIGHT, Integer.valueOf(2)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)), 
/* 1422 */             Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, Boolean.valueOf(true))
/*      */ 
/*      */           
/* 1425 */           }).with((Condition)Condition.condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)), new Variant[] {
/* 1426 */             Variant.variant().with(VariantProperties.MODEL, debug5).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)), 
/* 1427 */             Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.WEIGHT, Integer.valueOf(2)).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)), 
/* 1428 */             Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)), 
/* 1429 */             Variant.variant().with(VariantProperties.MODEL, debug4).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true))
/*      */ 
/*      */           
/* 1432 */           }).with((Condition)Condition.condition().term((Property)BlockStateProperties.UP, Boolean.valueOf(false)), new Variant[] {
/* 1433 */             Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.WEIGHT, Integer.valueOf(2)).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)), 
/* 1434 */             Variant.variant().with(VariantProperties.MODEL, debug5).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)), 
/* 1435 */             Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)), 
/* 1436 */             Variant.variant().with(VariantProperties.MODEL, debug4).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, Boolean.valueOf(true))
/*      */ 
/*      */           
/* 1439 */           }).with((Condition)Condition.condition().term((Property)BlockStateProperties.DOWN, Boolean.valueOf(false)), new Variant[] {
/* 1440 */             Variant.variant().with(VariantProperties.MODEL, debug5).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)), 
/* 1441 */             Variant.variant().with(VariantProperties.MODEL, debug4).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)), 
/* 1442 */             Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true)), 
/* 1443 */             Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.WEIGHT, Integer.valueOf(2)).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, Boolean.valueOf(true))
/*      */           }));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createComposter() {
/* 1449 */     this.blockStateOutput.accept(MultiPartGenerator.multiPart(Blocks.COMPOSTER)
/* 1450 */         .with(Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER)))
/* 1451 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(1)), Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents1")))
/* 1452 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(2)), Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents2")))
/* 1453 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(3)), Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents3")))
/* 1454 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(4)), Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents4")))
/* 1455 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(5)), Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents5")))
/* 1456 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(6)), Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents6")))
/* 1457 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(7)), Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents7")))
/* 1458 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.LEVEL_COMPOSTER, Integer.valueOf(8)), Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.COMPOSTER, "_contents_ready"))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createNyliumBlock(Block debug1) {
/* 1467 */     TextureMapping debug2 = (new TextureMapping()).put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.NETHERRACK)).put(TextureSlot.TOP, TextureMapping.getBlockTexture(debug1)).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(debug1, "_side"));
/*      */     
/* 1469 */     this.blockStateOutput.accept(createSimpleBlock(debug1, ModelTemplates.CUBE_BOTTOM_TOP.create(debug1, debug2, this.modelOutput)));
/*      */   }
/*      */   
/*      */   private void createDaylightDetector() {
/* 1473 */     ResourceLocation debug1 = TextureMapping.getBlockTexture(Blocks.DAYLIGHT_DETECTOR, "_side");
/* 1474 */     TextureMapping debug2 = (new TextureMapping()).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.DAYLIGHT_DETECTOR, "_top")).put(TextureSlot.SIDE, debug1);
/* 1475 */     TextureMapping debug3 = (new TextureMapping()).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.DAYLIGHT_DETECTOR, "_inverted_top")).put(TextureSlot.SIDE, debug1);
/*      */     
/* 1477 */     this.blockStateOutput.accept(
/* 1478 */         MultiVariantGenerator.multiVariant(Blocks.DAYLIGHT_DETECTOR)
/* 1479 */         .with(
/* 1480 */           (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.INVERTED)
/* 1481 */           .select(Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelTemplates.DAYLIGHT_DETECTOR.create(Blocks.DAYLIGHT_DETECTOR, debug2, this.modelOutput)))
/* 1482 */           .select(Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelTemplates.DAYLIGHT_DETECTOR.create(ModelLocationUtils.getModelLocation(Blocks.DAYLIGHT_DETECTOR, "_inverted"), debug3, this.modelOutput)))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createRotatableColumn(Block debug1) {
/* 1488 */     this.blockStateOutput.accept(
/* 1489 */         MultiVariantGenerator.multiVariant(debug1, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(debug1)))
/* 1490 */         .with(createColumnWithFacing()));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createFarmland() {
/* 1495 */     TextureMapping debug1 = (new TextureMapping()).put(TextureSlot.DIRT, TextureMapping.getBlockTexture(Blocks.DIRT)).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.FARMLAND));
/* 1496 */     TextureMapping debug2 = (new TextureMapping()).put(TextureSlot.DIRT, TextureMapping.getBlockTexture(Blocks.DIRT)).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.FARMLAND, "_moist"));
/*      */     
/* 1498 */     ResourceLocation debug3 = ModelTemplates.FARMLAND.create(Blocks.FARMLAND, debug1, this.modelOutput);
/* 1499 */     ResourceLocation debug4 = ModelTemplates.FARMLAND.create(TextureMapping.getBlockTexture(Blocks.FARMLAND, "_moist"), debug2, this.modelOutput);
/*      */     
/* 1501 */     this.blockStateOutput.accept(
/* 1502 */         MultiVariantGenerator.multiVariant(Blocks.FARMLAND)
/* 1503 */         .with(createEmptyOrFullDispatch((Property<Integer>)BlockStateProperties.MOISTURE, Integer.valueOf(7), debug4, debug3)));
/*      */   }
/*      */ 
/*      */   
/*      */   private List<ResourceLocation> createFloorFireModels(Block debug1) {
/* 1508 */     ResourceLocation debug2 = ModelTemplates.FIRE_FLOOR.create(ModelLocationUtils.getModelLocation(debug1, "_floor0"), TextureMapping.fire0(debug1), this.modelOutput);
/* 1509 */     ResourceLocation debug3 = ModelTemplates.FIRE_FLOOR.create(ModelLocationUtils.getModelLocation(debug1, "_floor1"), TextureMapping.fire1(debug1), this.modelOutput);
/*      */     
/* 1511 */     return (List<ResourceLocation>)ImmutableList.of(debug2, debug3);
/*      */   }
/*      */   
/*      */   private List<ResourceLocation> createSideFireModels(Block debug1) {
/* 1515 */     ResourceLocation debug2 = ModelTemplates.FIRE_SIDE.create(ModelLocationUtils.getModelLocation(debug1, "_side0"), TextureMapping.fire0(debug1), this.modelOutput);
/* 1516 */     ResourceLocation debug3 = ModelTemplates.FIRE_SIDE.create(ModelLocationUtils.getModelLocation(debug1, "_side1"), TextureMapping.fire1(debug1), this.modelOutput);
/*      */     
/* 1518 */     ResourceLocation debug4 = ModelTemplates.FIRE_SIDE_ALT.create(ModelLocationUtils.getModelLocation(debug1, "_side_alt0"), TextureMapping.fire0(debug1), this.modelOutput);
/* 1519 */     ResourceLocation debug5 = ModelTemplates.FIRE_SIDE_ALT.create(ModelLocationUtils.getModelLocation(debug1, "_side_alt1"), TextureMapping.fire1(debug1), this.modelOutput);
/*      */     
/* 1521 */     return (List<ResourceLocation>)ImmutableList.of(debug2, debug3, debug4, debug5);
/*      */   }
/*      */   
/*      */   private List<ResourceLocation> createTopFireModels(Block debug1) {
/* 1525 */     ResourceLocation debug2 = ModelTemplates.FIRE_UP.create(ModelLocationUtils.getModelLocation(debug1, "_up0"), TextureMapping.fire0(debug1), this.modelOutput);
/* 1526 */     ResourceLocation debug3 = ModelTemplates.FIRE_UP.create(ModelLocationUtils.getModelLocation(debug1, "_up1"), TextureMapping.fire1(debug1), this.modelOutput);
/*      */     
/* 1528 */     ResourceLocation debug4 = ModelTemplates.FIRE_UP_ALT.create(ModelLocationUtils.getModelLocation(debug1, "_up_alt0"), TextureMapping.fire0(debug1), this.modelOutput);
/* 1529 */     ResourceLocation debug5 = ModelTemplates.FIRE_UP_ALT.create(ModelLocationUtils.getModelLocation(debug1, "_up_alt1"), TextureMapping.fire1(debug1), this.modelOutput);
/*      */     
/* 1531 */     return (List<ResourceLocation>)ImmutableList.of(debug2, debug3, debug4, debug5);
/*      */   }
/*      */   
/*      */   private static List<Variant> wrapModels(List<ResourceLocation> debug0, UnaryOperator<Variant> debug1) {
/* 1535 */     return (List<Variant>)debug0.stream().map(debug0 -> Variant.variant().with(VariantProperties.MODEL, debug0)).map(debug1).collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createFire() {
/* 1544 */     Condition.TerminalCondition terminalCondition = Condition.condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false)).term((Property)BlockStateProperties.EAST, Boolean.valueOf(false)).term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false)).term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)).term((Property)BlockStateProperties.UP, Boolean.valueOf(false));
/* 1545 */     List<ResourceLocation> debug2 = createFloorFireModels(Blocks.FIRE);
/* 1546 */     List<ResourceLocation> debug3 = createSideFireModels(Blocks.FIRE);
/* 1547 */     List<ResourceLocation> debug4 = createTopFireModels(Blocks.FIRE);
/*      */     
/* 1549 */     this.blockStateOutput.accept(
/* 1550 */         MultiPartGenerator.multiPart(Blocks.FIRE)
/* 1551 */         .with((Condition)terminalCondition, 
/*      */           
/* 1553 */           wrapModels(debug2, debug0 -> debug0))
/*      */         
/* 1555 */         .with(
/* 1556 */           Condition.or(new Condition[] { (Condition)Condition.condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(true)), (Condition)terminalCondition
/* 1557 */             }), wrapModels(debug3, debug0 -> debug0))
/*      */         
/* 1559 */         .with(
/* 1560 */           Condition.or(new Condition[] { (Condition)Condition.condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(true)), (Condition)terminalCondition
/* 1561 */             }), wrapModels(debug3, debug0 -> debug0.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)))
/*      */         
/* 1563 */         .with(
/* 1564 */           Condition.or(new Condition[] { (Condition)Condition.condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(true)), (Condition)terminalCondition
/* 1565 */             }), wrapModels(debug3, debug0 -> debug0.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)))
/*      */         
/* 1567 */         .with(
/* 1568 */           Condition.or(new Condition[] { (Condition)Condition.condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(true)), (Condition)terminalCondition
/* 1569 */             }), wrapModels(debug3, debug0 -> debug0.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)))
/*      */         
/* 1571 */         .with(
/* 1572 */           (Condition)Condition.condition().term((Property)BlockStateProperties.UP, Boolean.valueOf(true)), 
/* 1573 */           wrapModels(debug4, debug0 -> debug0)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createSoulFire() {
/* 1579 */     List<ResourceLocation> debug1 = createFloorFireModels(Blocks.SOUL_FIRE);
/* 1580 */     List<ResourceLocation> debug2 = createSideFireModels(Blocks.SOUL_FIRE);
/*      */     
/* 1582 */     this.blockStateOutput.accept(
/* 1583 */         MultiPartGenerator.multiPart(Blocks.SOUL_FIRE)
/* 1584 */         .with(wrapModels(debug1, debug0 -> debug0))
/* 1585 */         .with(wrapModels(debug2, debug0 -> debug0))
/* 1586 */         .with(wrapModels(debug2, debug0 -> debug0.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)))
/* 1587 */         .with(wrapModels(debug2, debug0 -> debug0.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)))
/* 1588 */         .with(wrapModels(debug2, debug0 -> debug0.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createLantern(Block debug1) {
/* 1593 */     ResourceLocation debug2 = TexturedModel.LANTERN.create(debug1, this.modelOutput);
/* 1594 */     ResourceLocation debug3 = TexturedModel.HANGING_LANTERN.create(debug1, this.modelOutput);
/*      */     
/* 1596 */     createSimpleFlatItemModel(debug1.asItem());
/* 1597 */     this.blockStateOutput.accept(
/* 1598 */         MultiVariantGenerator.multiVariant(debug1)
/* 1599 */         .with(createBooleanModelDispatch(BlockStateProperties.HANGING, debug3, debug2)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createFrostedIce() {
/* 1604 */     this.blockStateOutput.accept(
/* 1605 */         MultiVariantGenerator.multiVariant(Blocks.FROSTED_ICE)
/* 1606 */         .with(
/* 1607 */           (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.AGE_3)
/* 1608 */           .select(Integer.valueOf(0), Variant.variant().with(VariantProperties.MODEL, createSuffixedVariant(Blocks.FROSTED_ICE, "_0", ModelTemplates.CUBE_ALL, TextureMapping::cube)))
/* 1609 */           .select(Integer.valueOf(1), Variant.variant().with(VariantProperties.MODEL, createSuffixedVariant(Blocks.FROSTED_ICE, "_1", ModelTemplates.CUBE_ALL, TextureMapping::cube)))
/* 1610 */           .select(Integer.valueOf(2), Variant.variant().with(VariantProperties.MODEL, createSuffixedVariant(Blocks.FROSTED_ICE, "_2", ModelTemplates.CUBE_ALL, TextureMapping::cube)))
/* 1611 */           .select(Integer.valueOf(3), Variant.variant().with(VariantProperties.MODEL, createSuffixedVariant(Blocks.FROSTED_ICE, "_3", ModelTemplates.CUBE_ALL, TextureMapping::cube)))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createGrassBlocks() {
/* 1617 */     ResourceLocation debug1 = TextureMapping.getBlockTexture(Blocks.DIRT);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1622 */     TextureMapping debug2 = (new TextureMapping()).put(TextureSlot.BOTTOM, debug1).copyForced(TextureSlot.BOTTOM, TextureSlot.PARTICLE).put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_top")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_snow"));
/*      */     
/* 1624 */     Variant debug3 = Variant.variant().with(VariantProperties.MODEL, ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.GRASS_BLOCK, "_snow", debug2, this.modelOutput));
/*      */     
/* 1626 */     createGrassLikeBlock(Blocks.GRASS_BLOCK, ModelLocationUtils.getModelLocation(Blocks.GRASS_BLOCK), debug3);
/*      */     
/* 1628 */     ResourceLocation debug4 = TexturedModel.CUBE_TOP_BOTTOM.get(Blocks.MYCELIUM).updateTextures(debug1 -> debug1.put(TextureSlot.BOTTOM, debug0)).create(Blocks.MYCELIUM, this.modelOutput);
/* 1629 */     createGrassLikeBlock(Blocks.MYCELIUM, debug4, debug3);
/*      */     
/* 1631 */     ResourceLocation debug5 = TexturedModel.CUBE_TOP_BOTTOM.get(Blocks.PODZOL).updateTextures(debug1 -> debug1.put(TextureSlot.BOTTOM, debug0)).create(Blocks.PODZOL, this.modelOutput);
/* 1632 */     createGrassLikeBlock(Blocks.PODZOL, debug5, debug3);
/*      */   }
/*      */   
/*      */   private void createGrassLikeBlock(Block debug1, ResourceLocation debug2, Variant debug3) {
/* 1636 */     List<Variant> debug4 = Arrays.asList(createRotatedVariants(debug2));
/* 1637 */     this.blockStateOutput.accept(
/* 1638 */         MultiVariantGenerator.multiVariant(debug1)
/* 1639 */         .with(
/* 1640 */           (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.SNOWY)
/* 1641 */           .select(Boolean.valueOf(true), debug3)
/* 1642 */           .select(Boolean.valueOf(false), debug4)));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createCocoa() {
/* 1648 */     createSimpleFlatItemModel(Items.COCOA_BEANS);
/* 1649 */     this.blockStateOutput.accept(
/* 1650 */         MultiVariantGenerator.multiVariant(Blocks.COCOA)
/* 1651 */         .with(
/* 1652 */           (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.AGE_2)
/* 1653 */           .select(Integer.valueOf(0), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.COCOA, "_stage0")))
/* 1654 */           .select(Integer.valueOf(1), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.COCOA, "_stage1")))
/* 1655 */           .select(Integer.valueOf(2), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.COCOA, "_stage2"))))
/*      */         
/* 1657 */         .with(createHorizontalFacingDispatchAlt()));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createGrassPath() {
/* 1662 */     this.blockStateOutput.accept(createRotatedVariant(Blocks.GRASS_PATH, ModelLocationUtils.getModelLocation(Blocks.GRASS_PATH)));
/*      */   }
/*      */   
/*      */   private void createWeightedPressurePlate(Block debug1, Block debug2) {
/* 1666 */     TextureMapping debug3 = TextureMapping.defaultTexture(debug2);
/* 1667 */     ResourceLocation debug4 = ModelTemplates.PRESSURE_PLATE_UP.create(debug1, debug3, this.modelOutput);
/* 1668 */     ResourceLocation debug5 = ModelTemplates.PRESSURE_PLATE_DOWN.create(debug1, debug3, this.modelOutput);
/*      */     
/* 1670 */     this.blockStateOutput.accept(
/* 1671 */         MultiVariantGenerator.multiVariant(debug1)
/* 1672 */         .with(createEmptyOrFullDispatch((Property<Integer>)BlockStateProperties.POWER, Integer.valueOf(1), debug5, debug4)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createHopper() {
/* 1677 */     ResourceLocation debug1 = ModelLocationUtils.getModelLocation(Blocks.HOPPER);
/* 1678 */     ResourceLocation debug2 = ModelLocationUtils.getModelLocation(Blocks.HOPPER, "_side");
/*      */     
/* 1680 */     createSimpleFlatItemModel(Items.HOPPER);
/* 1681 */     this.blockStateOutput.accept(
/* 1682 */         MultiVariantGenerator.multiVariant(Blocks.HOPPER)
/* 1683 */         .with(
/* 1684 */           (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.FACING_HOPPER)
/* 1685 */           .select((Comparable)Direction.DOWN, Variant.variant().with(VariantProperties.MODEL, debug1))
/* 1686 */           .select((Comparable)Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, debug2))
/* 1687 */           .select((Comparable)Direction.EAST, Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 1688 */           .select((Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 1689 */           .select((Comparable)Direction.WEST, Variant.variant().with(VariantProperties.MODEL, debug2).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void copyModel(Block debug1, Block debug2) {
/* 1696 */     ResourceLocation debug3 = ModelLocationUtils.getModelLocation(debug1);
/* 1697 */     this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(debug2, Variant.variant().with(VariantProperties.MODEL, debug3)));
/* 1698 */     delegateItemModel(debug2, debug3);
/*      */   }
/*      */   
/*      */   private void createIronBars() {
/* 1702 */     ResourceLocation debug1 = ModelLocationUtils.getModelLocation(Blocks.IRON_BARS, "_post_ends");
/* 1703 */     ResourceLocation debug2 = ModelLocationUtils.getModelLocation(Blocks.IRON_BARS, "_post");
/* 1704 */     ResourceLocation debug3 = ModelLocationUtils.getModelLocation(Blocks.IRON_BARS, "_cap");
/* 1705 */     ResourceLocation debug4 = ModelLocationUtils.getModelLocation(Blocks.IRON_BARS, "_cap_alt");
/* 1706 */     ResourceLocation debug5 = ModelLocationUtils.getModelLocation(Blocks.IRON_BARS, "_side");
/* 1707 */     ResourceLocation debug6 = ModelLocationUtils.getModelLocation(Blocks.IRON_BARS, "_side_alt");
/*      */     
/* 1709 */     this.blockStateOutput.accept(
/* 1710 */         MultiPartGenerator.multiPart(Blocks.IRON_BARS)
/* 1711 */         .with(Variant.variant().with(VariantProperties.MODEL, debug1))
/* 1712 */         .with(
/* 1713 */           (Condition)Condition.condition()
/* 1714 */           .term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false))
/* 1715 */           .term((Property)BlockStateProperties.EAST, Boolean.valueOf(false))
/* 1716 */           .term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false))
/* 1717 */           .term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)), 
/* 1718 */           Variant.variant().with(VariantProperties.MODEL, debug2))
/*      */         
/* 1720 */         .with(
/* 1721 */           (Condition)Condition.condition()
/* 1722 */           .term((Property)BlockStateProperties.NORTH, Boolean.valueOf(true))
/* 1723 */           .term((Property)BlockStateProperties.EAST, Boolean.valueOf(false))
/* 1724 */           .term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false))
/* 1725 */           .term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)), 
/* 1726 */           Variant.variant().with(VariantProperties.MODEL, debug3))
/*      */         
/* 1728 */         .with(
/* 1729 */           (Condition)Condition.condition()
/* 1730 */           .term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false))
/* 1731 */           .term((Property)BlockStateProperties.EAST, Boolean.valueOf(true))
/* 1732 */           .term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false))
/* 1733 */           .term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)), 
/* 1734 */           Variant.variant().with(VariantProperties.MODEL, debug3).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*      */         
/* 1736 */         .with(
/* 1737 */           (Condition)Condition.condition()
/* 1738 */           .term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false))
/* 1739 */           .term((Property)BlockStateProperties.EAST, Boolean.valueOf(false))
/* 1740 */           .term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(true))
/* 1741 */           .term((Property)BlockStateProperties.WEST, Boolean.valueOf(false)), 
/* 1742 */           Variant.variant().with(VariantProperties.MODEL, debug4))
/*      */         
/* 1744 */         .with(
/* 1745 */           (Condition)Condition.condition()
/* 1746 */           .term((Property)BlockStateProperties.NORTH, Boolean.valueOf(false))
/* 1747 */           .term((Property)BlockStateProperties.EAST, Boolean.valueOf(false))
/* 1748 */           .term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(false))
/* 1749 */           .term((Property)BlockStateProperties.WEST, Boolean.valueOf(true)), 
/* 1750 */           Variant.variant().with(VariantProperties.MODEL, debug4).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*      */         
/* 1752 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.NORTH, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug5))
/* 1753 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.EAST, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug5).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 1754 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.SOUTH, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug6))
/* 1755 */         .with((Condition)Condition.condition().term((Property)BlockStateProperties.WEST, Boolean.valueOf(true)), Variant.variant().with(VariantProperties.MODEL, debug6).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)));
/*      */     
/* 1757 */     createSimpleFlatItemModel(Blocks.IRON_BARS);
/*      */   }
/*      */   
/*      */   private void createNonTemplateHorizontalBlock(Block debug1) {
/* 1761 */     this.blockStateOutput.accept(
/* 1762 */         MultiVariantGenerator.multiVariant(debug1, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(debug1)))
/* 1763 */         .with(createHorizontalFacingDispatch()));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createLever() {
/* 1768 */     ResourceLocation debug1 = ModelLocationUtils.getModelLocation(Blocks.LEVER);
/* 1769 */     ResourceLocation debug2 = ModelLocationUtils.getModelLocation(Blocks.LEVER, "_on");
/*      */     
/* 1771 */     createSimpleFlatItemModel(Blocks.LEVER);
/* 1772 */     this.blockStateOutput.accept(
/* 1773 */         MultiVariantGenerator.multiVariant(Blocks.LEVER)
/* 1774 */         .with(
/* 1775 */           createBooleanModelDispatch(BlockStateProperties.POWERED, debug1, debug2))
/*      */         
/* 1777 */         .with(
/* 1778 */           (PropertyDispatch)PropertyDispatch.properties((Property)BlockStateProperties.ATTACH_FACE, (Property)BlockStateProperties.HORIZONTAL_FACING)
/* 1779 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 1780 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.EAST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/* 1781 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
/* 1782 */           .select((Comparable)AttachFace.CEILING, (Comparable)Direction.WEST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*      */           
/* 1784 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.NORTH, Variant.variant())
/* 1785 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.EAST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 1786 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 1787 */           .select((Comparable)AttachFace.FLOOR, (Comparable)Direction.WEST, Variant.variant().with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */           
/* 1789 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
/* 1790 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.EAST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 1791 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 1792 */           .select((Comparable)AttachFace.WALL, (Comparable)Direction.WEST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createLilyPad() {
/* 1798 */     createSimpleFlatItemModel(Blocks.LILY_PAD);
/* 1799 */     this.blockStateOutput.accept(createRotatedVariant(Blocks.LILY_PAD, ModelLocationUtils.getModelLocation(Blocks.LILY_PAD)));
/*      */   }
/*      */   
/*      */   private void createNetherPortalBlock() {
/* 1803 */     this.blockStateOutput.accept(
/* 1804 */         MultiVariantGenerator.multiVariant(Blocks.NETHER_PORTAL)
/* 1805 */         .with(
/* 1806 */           (PropertyDispatch)PropertyDispatch.property((Property)BlockStateProperties.HORIZONTAL_AXIS)
/* 1807 */           .select((Comparable)Direction.Axis.X, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.NETHER_PORTAL, "_ns")))
/* 1808 */           .select((Comparable)Direction.Axis.Z, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.NETHER_PORTAL, "_ew")))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createNetherrack() {
/* 1814 */     ResourceLocation debug1 = TexturedModel.CUBE.create(Blocks.NETHERRACK, this.modelOutput);
/* 1815 */     this.blockStateOutput.accept(
/* 1816 */         MultiVariantGenerator.multiVariant(Blocks.NETHERRACK, new Variant[] { 
/* 1817 */             Variant.variant().with(VariantProperties.MODEL, debug1), 
/* 1818 */             Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90), 
/* 1819 */             Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180), 
/* 1820 */             Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270), 
/*      */             
/* 1822 */             Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90), 
/* 1823 */             Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90), 
/* 1824 */             Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180), 
/* 1825 */             Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270), 
/*      */             
/* 1827 */             Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180), 
/* 1828 */             Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90), 
/* 1829 */             Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180), 
/* 1830 */             Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270), 
/*      */             
/* 1832 */             Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270), 
/* 1833 */             Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R90), 
/* 1834 */             Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R180), 
/* 1835 */             Variant.variant().with(VariantProperties.MODEL, debug1).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.X_ROT, VariantProperties.Rotation.R270) }));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createObserver() {
/* 1842 */     ResourceLocation debug1 = ModelLocationUtils.getModelLocation(Blocks.OBSERVER);
/* 1843 */     ResourceLocation debug2 = ModelLocationUtils.getModelLocation(Blocks.OBSERVER, "_on");
/*      */     
/* 1845 */     this.blockStateOutput.accept(
/* 1846 */         MultiVariantGenerator.multiVariant(Blocks.OBSERVER)
/* 1847 */         .with(createBooleanModelDispatch(BlockStateProperties.POWERED, debug2, debug1))
/* 1848 */         .with(createFacingDispatch()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createPistons() {
/* 1855 */     TextureMapping debug1 = (new TextureMapping()).put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.PISTON, "_bottom")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.PISTON, "_side"));
/*      */     
/* 1857 */     ResourceLocation debug2 = TextureMapping.getBlockTexture(Blocks.PISTON, "_top_sticky");
/* 1858 */     ResourceLocation debug3 = TextureMapping.getBlockTexture(Blocks.PISTON, "_top");
/*      */     
/* 1860 */     TextureMapping debug4 = debug1.copyAndUpdate(TextureSlot.PLATFORM, debug2);
/* 1861 */     TextureMapping debug5 = debug1.copyAndUpdate(TextureSlot.PLATFORM, debug3);
/*      */     
/* 1863 */     ResourceLocation debug6 = ModelLocationUtils.getModelLocation(Blocks.PISTON, "_base");
/*      */     
/* 1865 */     createPistonVariant(Blocks.PISTON, debug6, debug5);
/* 1866 */     createPistonVariant(Blocks.STICKY_PISTON, debug6, debug4);
/*      */     
/* 1868 */     ResourceLocation debug7 = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.PISTON, "_inventory", debug1.copyAndUpdate(TextureSlot.TOP, debug3), this.modelOutput);
/* 1869 */     ResourceLocation debug8 = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.STICKY_PISTON, "_inventory", debug1.copyAndUpdate(TextureSlot.TOP, debug2), this.modelOutput);
/*      */     
/* 1871 */     delegateItemModel(Blocks.PISTON, debug7);
/* 1872 */     delegateItemModel(Blocks.STICKY_PISTON, debug8);
/*      */   }
/*      */   
/*      */   private void createPistonVariant(Block debug1, ResourceLocation debug2, TextureMapping debug3) {
/* 1876 */     ResourceLocation debug4 = ModelTemplates.PISTON.create(debug1, debug3, this.modelOutput);
/* 1877 */     this.blockStateOutput.accept(
/* 1878 */         MultiVariantGenerator.multiVariant(debug1)
/* 1879 */         .with(createBooleanModelDispatch(BlockStateProperties.EXTENDED, debug2, debug4))
/* 1880 */         .with(createFacingDispatch()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createPistonHeads() {
/* 1887 */     TextureMapping debug1 = (new TextureMapping()).put(TextureSlot.UNSTICKY, TextureMapping.getBlockTexture(Blocks.PISTON, "_top")).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.PISTON, "_side"));
/*      */     
/* 1889 */     TextureMapping debug2 = debug1.copyAndUpdate(TextureSlot.PLATFORM, TextureMapping.getBlockTexture(Blocks.PISTON, "_top_sticky"));
/* 1890 */     TextureMapping debug3 = debug1.copyAndUpdate(TextureSlot.PLATFORM, TextureMapping.getBlockTexture(Blocks.PISTON, "_top"));
/*      */     
/* 1892 */     this.blockStateOutput.accept(
/* 1893 */         MultiVariantGenerator.multiVariant(Blocks.PISTON_HEAD)
/* 1894 */         .with(
/* 1895 */           (PropertyDispatch)PropertyDispatch.properties((Property)BlockStateProperties.SHORT, (Property)BlockStateProperties.PISTON_TYPE)
/* 1896 */           .select(Boolean.valueOf(false), (Comparable)PistonType.DEFAULT, Variant.variant().with(VariantProperties.MODEL, ModelTemplates.PISTON_HEAD.createWithSuffix(Blocks.PISTON, "_head", debug3, this.modelOutput)))
/* 1897 */           .select(Boolean.valueOf(false), (Comparable)PistonType.STICKY, Variant.variant().with(VariantProperties.MODEL, ModelTemplates.PISTON_HEAD.createWithSuffix(Blocks.PISTON, "_head_sticky", debug2, this.modelOutput)))
/* 1898 */           .select(Boolean.valueOf(true), (Comparable)PistonType.DEFAULT, Variant.variant().with(VariantProperties.MODEL, ModelTemplates.PISTON_HEAD_SHORT.createWithSuffix(Blocks.PISTON, "_head_short", debug3, this.modelOutput)))
/* 1899 */           .select(Boolean.valueOf(true), (Comparable)PistonType.STICKY, Variant.variant().with(VariantProperties.MODEL, ModelTemplates.PISTON_HEAD_SHORT.createWithSuffix(Blocks.PISTON, "_head_short_sticky", debug2, this.modelOutput))))
/*      */         
/* 1901 */         .with(
/* 1902 */           createFacingDispatch()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createScaffolding() {
/* 1908 */     ResourceLocation debug1 = ModelLocationUtils.getModelLocation(Blocks.SCAFFOLDING, "_stable");
/* 1909 */     ResourceLocation debug2 = ModelLocationUtils.getModelLocation(Blocks.SCAFFOLDING, "_unstable");
/* 1910 */     delegateItemModel(Blocks.SCAFFOLDING, debug1);
/* 1911 */     this.blockStateOutput.accept(
/* 1912 */         MultiVariantGenerator.multiVariant(Blocks.SCAFFOLDING)
/* 1913 */         .with(createBooleanModelDispatch(BlockStateProperties.BOTTOM, debug2, debug1)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createRedstoneLamp() {
/* 1918 */     ResourceLocation debug1 = TexturedModel.CUBE.create(Blocks.REDSTONE_LAMP, this.modelOutput);
/* 1919 */     ResourceLocation debug2 = createSuffixedVariant(Blocks.REDSTONE_LAMP, "_on", ModelTemplates.CUBE_ALL, TextureMapping::cube);
/*      */     
/* 1921 */     this.blockStateOutput.accept(
/* 1922 */         MultiVariantGenerator.multiVariant(Blocks.REDSTONE_LAMP)
/* 1923 */         .with(createBooleanModelDispatch(BlockStateProperties.LIT, debug2, debug1)));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createNormalTorch(Block debug1, Block debug2) {
/* 1928 */     TextureMapping debug3 = TextureMapping.torch(debug1);
/*      */     
/* 1930 */     this.blockStateOutput.accept(createSimpleBlock(debug1, ModelTemplates.TORCH.create(debug1, debug3, this.modelOutput)));
/*      */     
/* 1932 */     this.blockStateOutput.accept(
/* 1933 */         MultiVariantGenerator.multiVariant(debug2, Variant.variant().with(VariantProperties.MODEL, ModelTemplates.WALL_TORCH.create(debug2, debug3, this.modelOutput)))
/* 1934 */         .with(createTorchHorizontalDispatch()));
/*      */     
/* 1936 */     createSimpleFlatItemModel(debug1);
/* 1937 */     skipAutoItemBlock(debug2);
/*      */   }
/*      */   
/*      */   private void createRedstoneTorch() {
/* 1941 */     TextureMapping debug1 = TextureMapping.torch(Blocks.REDSTONE_TORCH);
/* 1942 */     TextureMapping debug2 = TextureMapping.torch(TextureMapping.getBlockTexture(Blocks.REDSTONE_TORCH, "_off"));
/*      */     
/* 1944 */     ResourceLocation debug3 = ModelTemplates.TORCH.create(Blocks.REDSTONE_TORCH, debug1, this.modelOutput);
/* 1945 */     ResourceLocation debug4 = ModelTemplates.TORCH.createWithSuffix(Blocks.REDSTONE_TORCH, "_off", debug2, this.modelOutput);
/*      */     
/* 1947 */     this.blockStateOutput.accept(
/* 1948 */         MultiVariantGenerator.multiVariant(Blocks.REDSTONE_TORCH)
/* 1949 */         .with(createBooleanModelDispatch(BlockStateProperties.LIT, debug3, debug4)));
/*      */ 
/*      */     
/* 1952 */     ResourceLocation debug5 = ModelTemplates.WALL_TORCH.create(Blocks.REDSTONE_WALL_TORCH, debug1, this.modelOutput);
/* 1953 */     ResourceLocation debug6 = ModelTemplates.WALL_TORCH.createWithSuffix(Blocks.REDSTONE_WALL_TORCH, "_off", debug2, this.modelOutput);
/*      */     
/* 1955 */     this.blockStateOutput.accept(
/* 1956 */         MultiVariantGenerator.multiVariant(Blocks.REDSTONE_WALL_TORCH)
/* 1957 */         .with(createBooleanModelDispatch(BlockStateProperties.LIT, debug5, debug6))
/* 1958 */         .with(createTorchHorizontalDispatch()));
/*      */     
/* 1960 */     createSimpleFlatItemModel(Blocks.REDSTONE_TORCH);
/* 1961 */     skipAutoItemBlock(Blocks.REDSTONE_WALL_TORCH);
/*      */   }
/*      */   
/*      */   private void createRepeater() {
/* 1965 */     createSimpleFlatItemModel(Items.REPEATER);
/* 1966 */     this.blockStateOutput.accept(
/* 1967 */         MultiVariantGenerator.multiVariant(Blocks.REPEATER)
/* 1968 */         .with(
/* 1969 */           PropertyDispatch.properties((Property)BlockStateProperties.DELAY, (Property)BlockStateProperties.LOCKED, (Property)BlockStateProperties.POWERED)
/* 1970 */           .generate((debug0, debug1, debug2) -> {
/*      */               StringBuilder debug3 = new StringBuilder();
/*      */               
/*      */               debug3.append('_').append(debug0).append("tick");
/*      */               
/*      */               if (debug2.booleanValue()) {
/*      */                 debug3.append("_on");
/*      */               }
/*      */               if (debug1.booleanValue()) {
/*      */                 debug3.append("_locked");
/*      */               }
/*      */               return Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.REPEATER, debug3.toString()));
/* 1982 */             })).with(createHorizontalFacingDispatchAlt()));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createSeaPickle() {
/* 1987 */     createSimpleFlatItemModel(Items.SEA_PICKLE);
/*      */     
/* 1989 */     this.blockStateOutput.accept(
/* 1990 */         MultiVariantGenerator.multiVariant(Blocks.SEA_PICKLE)
/* 1991 */         .with(
/* 1992 */           (PropertyDispatch)PropertyDispatch.properties((Property)BlockStateProperties.PICKLES, (Property)BlockStateProperties.WATERLOGGED)
/* 1993 */           .select(Integer.valueOf(1), Boolean.valueOf(false), Arrays.asList(createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("dead_sea_pickle"))))
/* 1994 */           .select(Integer.valueOf(2), Boolean.valueOf(false), Arrays.asList(createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("two_dead_sea_pickles"))))
/* 1995 */           .select(Integer.valueOf(3), Boolean.valueOf(false), Arrays.asList(createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("three_dead_sea_pickles"))))
/* 1996 */           .select(Integer.valueOf(4), Boolean.valueOf(false), Arrays.asList(createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("four_dead_sea_pickles"))))
/*      */           
/* 1998 */           .select(Integer.valueOf(1), Boolean.valueOf(true), Arrays.asList(createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("sea_pickle"))))
/* 1999 */           .select(Integer.valueOf(2), Boolean.valueOf(true), Arrays.asList(createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("two_sea_pickles"))))
/* 2000 */           .select(Integer.valueOf(3), Boolean.valueOf(true), Arrays.asList(createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("three_sea_pickles"))))
/* 2001 */           .select(Integer.valueOf(4), Boolean.valueOf(true), Arrays.asList(createRotatedVariants(ModelLocationUtils.decorateBlockModelLocation("four_sea_pickles"))))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createSnowBlocks() {
/* 2008 */     TextureMapping debug1 = TextureMapping.cube(Blocks.SNOW);
/* 2009 */     ResourceLocation debug2 = ModelTemplates.CUBE_ALL.create(Blocks.SNOW_BLOCK, debug1, this.modelOutput);
/*      */     
/* 2011 */     this.blockStateOutput.accept(
/* 2012 */         MultiVariantGenerator.multiVariant(Blocks.SNOW)
/* 2013 */         .with(
/* 2014 */           PropertyDispatch.property((Property)BlockStateProperties.LAYERS)
/* 2015 */           .generate(debug1 -> Variant.variant().with(VariantProperties.MODEL, (debug1.intValue() < 8) ? ModelLocationUtils.getModelLocation(Blocks.SNOW, "_height" + (debug1.intValue() * 2)) : debug0))));
/*      */ 
/*      */ 
/*      */     
/* 2019 */     delegateItemModel(Blocks.SNOW, ModelLocationUtils.getModelLocation(Blocks.SNOW, "_height2"));
/* 2020 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.SNOW_BLOCK, debug2));
/*      */   }
/*      */   
/*      */   private void createStonecutter() {
/* 2024 */     this.blockStateOutput.accept(
/* 2025 */         MultiVariantGenerator.multiVariant(Blocks.STONECUTTER, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.STONECUTTER)))
/* 2026 */         .with(createHorizontalFacingDispatch()));
/*      */   }
/*      */ 
/*      */   
/*      */   private void createStructureBlock() {
/* 2031 */     ResourceLocation debug1 = TexturedModel.CUBE.create(Blocks.STRUCTURE_BLOCK, this.modelOutput);
/* 2032 */     delegateItemModel(Blocks.STRUCTURE_BLOCK, debug1);
/*      */     
/* 2034 */     this.blockStateOutput.accept(
/* 2035 */         MultiVariantGenerator.multiVariant(Blocks.STRUCTURE_BLOCK)
/* 2036 */         .with(
/* 2037 */           PropertyDispatch.property((Property)BlockStateProperties.STRUCTUREBLOCK_MODE)
/* 2038 */           .generate(debug1 -> Variant.variant().with(VariantProperties.MODEL, createSuffixedVariant(Blocks.STRUCTURE_BLOCK, "_" + debug1.getSerializedName(), ModelTemplates.CUBE_ALL, TextureMapping::cube)))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createSweetBerryBush() {
/* 2045 */     createSimpleFlatItemModel(Items.SWEET_BERRIES);
/* 2046 */     this.blockStateOutput.accept(
/* 2047 */         MultiVariantGenerator.multiVariant(Blocks.SWEET_BERRY_BUSH)
/* 2048 */         .with(
/* 2049 */           PropertyDispatch.property((Property)BlockStateProperties.AGE_3)
/* 2050 */           .generate(debug1 -> Variant.variant().with(VariantProperties.MODEL, createSuffixedVariant(Blocks.SWEET_BERRY_BUSH, "_stage" + debug1, ModelTemplates.CROSS, TextureMapping::cross)))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createTripwire() {
/* 2058 */     createSimpleFlatItemModel(Items.STRING);
/* 2059 */     this.blockStateOutput.accept(
/* 2060 */         MultiVariantGenerator.multiVariant(Blocks.TRIPWIRE)
/* 2061 */         .with(
/* 2062 */           (PropertyDispatch)PropertyDispatch.properties((Property)BlockStateProperties.ATTACHED, (Property)BlockStateProperties.EAST, (Property)BlockStateProperties.NORTH, (Property)BlockStateProperties.SOUTH, (Property)BlockStateProperties.WEST)
/*      */           
/* 2064 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ns")))
/*      */ 
/*      */           
/* 2067 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 2068 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n")))
/* 2069 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 2070 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_n")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */ 
/*      */           
/* 2073 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne")))
/* 2074 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 2075 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 2076 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ne")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/* 2077 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ns")))
/* 2078 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_ns")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*      */ 
/*      */           
/* 2081 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse")))
/* 2082 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 2083 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 2084 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nse")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */ 
/*      */           
/* 2087 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_nsew")))
/*      */ 
/*      */           
/* 2090 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ns")))
/*      */ 
/*      */           
/* 2093 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n")))
/* 2094 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 2095 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 2096 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_n")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */ 
/*      */           
/* 2099 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne")))
/* 2100 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 2101 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 2102 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ne")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/* 2103 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ns")))
/* 2104 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_ns")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*      */ 
/*      */           
/* 2107 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse")))
/* 2108 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 2109 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 2110 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nse")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */ 
/*      */           
/* 2113 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.TRIPWIRE, "_attached_nsew")))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createTripwireHook() {
/* 2119 */     createSimpleFlatItemModel(Blocks.TRIPWIRE_HOOK);
/* 2120 */     this.blockStateOutput.accept(
/* 2121 */         MultiVariantGenerator.multiVariant(Blocks.TRIPWIRE_HOOK)
/* 2122 */         .with(
/* 2123 */           PropertyDispatch.properties((Property)BlockStateProperties.ATTACHED, (Property)BlockStateProperties.POWERED)
/* 2124 */           .generate((debug0, debug1) -> Variant.variant().with(VariantProperties.MODEL, TextureMapping.getBlockTexture(Blocks.TRIPWIRE_HOOK, (debug0.booleanValue() ? "_attached" : "") + (debug1.booleanValue() ? "_on" : "")))))
/*      */         
/* 2126 */         .with(createHorizontalFacingDispatch()));
/*      */   }
/*      */ 
/*      */   
/*      */   private ResourceLocation createTurtleEggModel(int debug1, String debug2, TextureMapping debug3) {
/* 2131 */     switch (debug1) {
/*      */       case 1:
/* 2133 */         return ModelTemplates.TURTLE_EGG.create(ModelLocationUtils.decorateBlockModelLocation(debug2 + "turtle_egg"), debug3, this.modelOutput);
/*      */       case 2:
/* 2135 */         return ModelTemplates.TWO_TURTLE_EGGS.create(ModelLocationUtils.decorateBlockModelLocation("two_" + debug2 + "turtle_eggs"), debug3, this.modelOutput);
/*      */       case 3:
/* 2137 */         return ModelTemplates.THREE_TURTLE_EGGS.create(ModelLocationUtils.decorateBlockModelLocation("three_" + debug2 + "turtle_eggs"), debug3, this.modelOutput);
/*      */       case 4:
/* 2139 */         return ModelTemplates.FOUR_TURTLE_EGGS.create(ModelLocationUtils.decorateBlockModelLocation("four_" + debug2 + "turtle_eggs"), debug3, this.modelOutput);
/*      */     } 
/* 2141 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   
/*      */   private ResourceLocation createTurtleEggModel(Integer debug1, Integer debug2) {
/* 2146 */     switch (debug2.intValue()) {
/*      */       case 0:
/* 2148 */         return createTurtleEggModel(debug1.intValue(), "", TextureMapping.cube(TextureMapping.getBlockTexture(Blocks.TURTLE_EGG)));
/*      */       case 1:
/* 2150 */         return createTurtleEggModel(debug1.intValue(), "slightly_cracked_", TextureMapping.cube(TextureMapping.getBlockTexture(Blocks.TURTLE_EGG, "_slightly_cracked")));
/*      */       case 2:
/* 2152 */         return createTurtleEggModel(debug1.intValue(), "very_cracked_", TextureMapping.cube(TextureMapping.getBlockTexture(Blocks.TURTLE_EGG, "_very_cracked")));
/*      */     } 
/* 2154 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   
/*      */   private void createTurtleEgg() {
/* 2159 */     createSimpleFlatItemModel(Items.TURTLE_EGG);
/* 2160 */     this.blockStateOutput.accept(
/* 2161 */         MultiVariantGenerator.multiVariant(Blocks.TURTLE_EGG)
/* 2162 */         .with(
/* 2163 */           PropertyDispatch.properties((Property)BlockStateProperties.EGGS, (Property)BlockStateProperties.HATCH)
/* 2164 */           .generateList((debug1, debug2) -> Arrays.asList(createRotatedVariants(createTurtleEggModel(debug1, debug2))))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createVine() {
/* 2170 */     createSimpleFlatItemModel(Blocks.VINE);
/* 2171 */     this.blockStateOutput.accept(
/* 2172 */         MultiVariantGenerator.multiVariant(Blocks.VINE)
/* 2173 */         .with(
/* 2174 */           (PropertyDispatch)PropertyDispatch.properties((Property)BlockStateProperties.EAST, (Property)BlockStateProperties.NORTH, (Property)BlockStateProperties.SOUTH, (Property)BlockStateProperties.UP, (Property)BlockStateProperties.WEST)
/* 2175 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1")))
/*      */           
/* 2177 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1")))
/* 2178 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 2179 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 2180 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */           
/* 2182 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2")))
/* 2183 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 2184 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 2185 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/* 2186 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2_opposite")))
/* 2187 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2_opposite")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*      */           
/* 2189 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3")))
/* 2190 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 2191 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 2192 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */           
/* 2194 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_4")))
/*      */           
/* 2196 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_u")))
/*      */           
/* 2198 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1u")))
/* 2199 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1u")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 2200 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1u")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 2201 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_1u")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */           
/* 2203 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2u")))
/* 2204 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2u")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 2205 */           .select(Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2u")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 2206 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2u")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/* 2207 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2u_opposite")))
/* 2208 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_2u_opposite")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/*      */           
/* 2210 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3u")))
/* 2211 */           .select(Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3u")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
/* 2212 */           .select(Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3u")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
/* 2213 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_3u")).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
/*      */           
/* 2215 */           .select(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.VINE, "_4u")))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createMagmaBlock() {
/* 2221 */     this.blockStateOutput.accept(createSimpleBlock(Blocks.MAGMA_BLOCK, ModelTemplates.CUBE_ALL.create(Blocks.MAGMA_BLOCK, TextureMapping.cube(ModelLocationUtils.decorateBlockModelLocation("magma")), this.modelOutput)));
/*      */   }
/*      */   
/*      */   private void createShulkerBox(Block debug1) {
/* 2225 */     createTrivialBlock(debug1, TexturedModel.PARTICLE_ONLY);
/* 2226 */     ModelTemplates.SHULKER_BOX_INVENTORY.create(ModelLocationUtils.getModelLocation(debug1.asItem()), TextureMapping.particle(debug1), this.modelOutput);
/*      */   }
/*      */   
/*      */   private void createGrowingPlant(Block debug1, Block debug2, TintState debug3) {
/* 2230 */     createCrossBlock(debug1, debug3);
/* 2231 */     createCrossBlock(debug2, debug3);
/*      */   }
/*      */   
/*      */   private void createBedItem(Block debug1, Block debug2) {
/* 2235 */     ModelTemplates.BED_INVENTORY.create(ModelLocationUtils.getModelLocation(debug1.asItem()), TextureMapping.particle(debug2), this.modelOutput);
/*      */   }
/*      */   
/*      */   private void createInfestedStone() {
/* 2239 */     ResourceLocation debug1 = ModelLocationUtils.getModelLocation(Blocks.STONE);
/* 2240 */     ResourceLocation debug2 = ModelLocationUtils.getModelLocation(Blocks.STONE, "_mirrored");
/* 2241 */     this.blockStateOutput.accept(createRotatedVariant(Blocks.INFESTED_STONE, debug1, debug2));
/* 2242 */     delegateItemModel(Blocks.INFESTED_STONE, debug1);
/*      */   }
/*      */   
/*      */   private void createNetherRoots(Block debug1, Block debug2) {
/* 2246 */     createCrossBlockWithDefaultItem(debug1, TintState.NOT_TINTED);
/* 2247 */     TextureMapping debug3 = TextureMapping.plant(TextureMapping.getBlockTexture(debug1, "_pot"));
/* 2248 */     ResourceLocation debug4 = TintState.NOT_TINTED.getCrossPot().create(debug2, debug3, this.modelOutput);
/* 2249 */     this.blockStateOutput.accept(createSimpleBlock(debug2, debug4));
/*      */   }
/*      */   
/*      */   private void createRespawnAnchor() {
/* 2253 */     ResourceLocation debug1 = TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, "_bottom");
/* 2254 */     ResourceLocation debug2 = TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, "_top_off");
/* 2255 */     ResourceLocation debug3 = TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, "_top");
/* 2256 */     ResourceLocation[] debug4 = new ResourceLocation[5];
/* 2257 */     for (int debug5 = 0; debug5 < 5; debug5++) {
/*      */ 
/*      */ 
/*      */       
/* 2261 */       TextureMapping debug6 = (new TextureMapping()).put(TextureSlot.BOTTOM, debug1).put(TextureSlot.TOP, (debug5 == 0) ? debug2 : debug3).put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.RESPAWN_ANCHOR, "_side" + debug5));
/* 2262 */       debug4[debug5] = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(Blocks.RESPAWN_ANCHOR, "_" + debug5, debug6, this.modelOutput);
/*      */     } 
/*      */     
/* 2265 */     this.blockStateOutput.accept(
/* 2266 */         MultiVariantGenerator.multiVariant(Blocks.RESPAWN_ANCHOR)
/* 2267 */         .with(
/* 2268 */           PropertyDispatch.property((Property)BlockStateProperties.RESPAWN_ANCHOR_CHARGES)
/* 2269 */           .generate(debug1 -> Variant.variant().with(VariantProperties.MODEL, debug0[debug1.intValue()]))));
/*      */ 
/*      */     
/* 2272 */     delegateItemModel(Items.RESPAWN_ANCHOR, debug4[0]);
/*      */   }
/*      */   
/*      */   private Variant applyRotation(FrontAndTop debug1, Variant debug2) {
/* 2276 */     switch (debug1) {
/*      */       case NORTH_SOUTH:
/* 2278 */         return debug2.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90);
/*      */       case EAST_WEST:
/* 2280 */         return debug2.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180);
/*      */       case ASCENDING_EAST:
/* 2282 */         return debug2.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
/*      */       case ASCENDING_WEST:
/* 2284 */         return debug2.with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
/*      */       case ASCENDING_NORTH:
/* 2286 */         return debug2.with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180);
/*      */       case ASCENDING_SOUTH:
/* 2288 */         return debug2.with(VariantProperties.X_ROT, VariantProperties.Rotation.R270);
/*      */       case null:
/* 2290 */         return debug2.with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
/*      */       case null:
/* 2292 */         return debug2.with(VariantProperties.X_ROT, VariantProperties.Rotation.R270).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
/*      */       
/*      */       case null:
/* 2295 */         return debug2;
/*      */       case null:
/* 2297 */         return debug2.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180);
/*      */       case null:
/* 2299 */         return debug2.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270);
/*      */       case null:
/* 2301 */         return debug2.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
/*      */     } 
/* 2303 */     throw new UnsupportedOperationException("Rotation " + debug1 + " can't be expressed with existing x and y values");
/*      */   }
/*      */ 
/*      */   
/*      */   private void createJigsaw() {
/* 2308 */     ResourceLocation debug1 = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_top");
/* 2309 */     ResourceLocation debug2 = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_bottom");
/* 2310 */     ResourceLocation debug3 = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_side");
/* 2311 */     ResourceLocation debug4 = TextureMapping.getBlockTexture(Blocks.JIGSAW, "_lock");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2322 */     TextureMapping debug5 = (new TextureMapping()).put(TextureSlot.DOWN, debug3).put(TextureSlot.WEST, debug3).put(TextureSlot.EAST, debug3).put(TextureSlot.PARTICLE, debug1).put(TextureSlot.NORTH, debug1).put(TextureSlot.SOUTH, debug2).put(TextureSlot.UP, debug4);
/*      */     
/* 2324 */     ResourceLocation debug6 = ModelTemplates.CUBE_DIRECTIONAL.create(Blocks.JIGSAW, debug5, this.modelOutput);
/* 2325 */     this.blockStateOutput.accept(
/* 2326 */         MultiVariantGenerator.multiVariant(Blocks.JIGSAW, Variant.variant().with(VariantProperties.MODEL, debug6))
/* 2327 */         .with(
/* 2328 */           PropertyDispatch.property((Property)BlockStateProperties.ORIENTATION)
/* 2329 */           .generate(debug1 -> applyRotation(debug1, Variant.variant()))));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void run() {
/* 2335 */     createNonTemplateModelBlock(Blocks.AIR);
/* 2336 */     createNonTemplateModelBlock(Blocks.CAVE_AIR, Blocks.AIR);
/* 2337 */     createNonTemplateModelBlock(Blocks.VOID_AIR, Blocks.AIR);
/* 2338 */     createNonTemplateModelBlock(Blocks.BEACON);
/* 2339 */     createNonTemplateModelBlock(Blocks.CACTUS);
/* 2340 */     createNonTemplateModelBlock(Blocks.BUBBLE_COLUMN, Blocks.WATER);
/* 2341 */     createNonTemplateModelBlock(Blocks.DRAGON_EGG);
/* 2342 */     createNonTemplateModelBlock(Blocks.DRIED_KELP_BLOCK);
/* 2343 */     createNonTemplateModelBlock(Blocks.ENCHANTING_TABLE);
/* 2344 */     createNonTemplateModelBlock(Blocks.FLOWER_POT);
/* 2345 */     createSimpleFlatItemModel(Items.FLOWER_POT);
/* 2346 */     createNonTemplateModelBlock(Blocks.HONEY_BLOCK);
/* 2347 */     createNonTemplateModelBlock(Blocks.WATER);
/* 2348 */     createNonTemplateModelBlock(Blocks.LAVA);
/* 2349 */     createNonTemplateModelBlock(Blocks.SLIME_BLOCK);
/* 2350 */     createSimpleFlatItemModel(Items.CHAIN);
/*      */     
/* 2352 */     createNonTemplateModelBlock(Blocks.POTTED_BAMBOO);
/* 2353 */     createNonTemplateModelBlock(Blocks.POTTED_CACTUS);
/*      */     
/* 2355 */     createAirLikeBlock(Blocks.BARRIER, Items.BARRIER);
/* 2356 */     createSimpleFlatItemModel(Items.BARRIER);
/* 2357 */     createAirLikeBlock(Blocks.STRUCTURE_VOID, Items.STRUCTURE_VOID);
/* 2358 */     createSimpleFlatItemModel(Items.STRUCTURE_VOID);
/* 2359 */     createAirLikeBlock(Blocks.MOVING_PISTON, TextureMapping.getBlockTexture(Blocks.PISTON, "_side"));
/*      */     
/* 2361 */     createTrivialBlock(Blocks.COAL_ORE, TexturedModel.CUBE);
/* 2362 */     createTrivialBlock(Blocks.COAL_BLOCK, TexturedModel.CUBE);
/* 2363 */     createTrivialBlock(Blocks.DIAMOND_ORE, TexturedModel.CUBE);
/* 2364 */     createTrivialBlock(Blocks.DIAMOND_BLOCK, TexturedModel.CUBE);
/* 2365 */     createTrivialBlock(Blocks.EMERALD_ORE, TexturedModel.CUBE);
/* 2366 */     createTrivialBlock(Blocks.EMERALD_BLOCK, TexturedModel.CUBE);
/* 2367 */     createTrivialBlock(Blocks.GOLD_ORE, TexturedModel.CUBE);
/* 2368 */     createTrivialBlock(Blocks.NETHER_GOLD_ORE, TexturedModel.CUBE);
/* 2369 */     createTrivialBlock(Blocks.GOLD_BLOCK, TexturedModel.CUBE);
/* 2370 */     createTrivialBlock(Blocks.IRON_ORE, TexturedModel.CUBE);
/* 2371 */     createTrivialBlock(Blocks.IRON_BLOCK, TexturedModel.CUBE);
/* 2372 */     createTrivialBlock(Blocks.ANCIENT_DEBRIS, TexturedModel.COLUMN);
/* 2373 */     createTrivialBlock(Blocks.NETHERITE_BLOCK, TexturedModel.CUBE);
/* 2374 */     createTrivialBlock(Blocks.LAPIS_ORE, TexturedModel.CUBE);
/* 2375 */     createTrivialBlock(Blocks.LAPIS_BLOCK, TexturedModel.CUBE);
/* 2376 */     createTrivialBlock(Blocks.NETHER_QUARTZ_ORE, TexturedModel.CUBE);
/* 2377 */     createTrivialBlock(Blocks.REDSTONE_ORE, TexturedModel.CUBE);
/* 2378 */     createTrivialBlock(Blocks.REDSTONE_BLOCK, TexturedModel.CUBE);
/* 2379 */     createTrivialBlock(Blocks.GILDED_BLACKSTONE, TexturedModel.CUBE);
/*      */     
/* 2381 */     createTrivialBlock(Blocks.BLUE_ICE, TexturedModel.CUBE);
/* 2382 */     createTrivialBlock(Blocks.CHISELED_NETHER_BRICKS, TexturedModel.CUBE);
/* 2383 */     createTrivialBlock(Blocks.CLAY, TexturedModel.CUBE);
/* 2384 */     createTrivialBlock(Blocks.COARSE_DIRT, TexturedModel.CUBE);
/* 2385 */     createTrivialBlock(Blocks.CRACKED_NETHER_BRICKS, TexturedModel.CUBE);
/* 2386 */     createTrivialBlock(Blocks.CRACKED_STONE_BRICKS, TexturedModel.CUBE);
/* 2387 */     createTrivialBlock(Blocks.CRYING_OBSIDIAN, TexturedModel.CUBE);
/* 2388 */     createTrivialBlock(Blocks.END_STONE, TexturedModel.CUBE);
/* 2389 */     createTrivialBlock(Blocks.GLOWSTONE, TexturedModel.CUBE);
/* 2390 */     createTrivialBlock(Blocks.GRAVEL, TexturedModel.CUBE);
/* 2391 */     createTrivialBlock(Blocks.HONEYCOMB_BLOCK, TexturedModel.CUBE);
/* 2392 */     createTrivialBlock(Blocks.ICE, TexturedModel.CUBE);
/* 2393 */     createTrivialBlock(Blocks.JUKEBOX, TexturedModel.CUBE_TOP);
/* 2394 */     createTrivialBlock(Blocks.LODESTONE, TexturedModel.COLUMN);
/* 2395 */     createTrivialBlock(Blocks.MELON, TexturedModel.COLUMN);
/* 2396 */     createTrivialBlock(Blocks.NETHER_WART_BLOCK, TexturedModel.CUBE);
/* 2397 */     createTrivialBlock(Blocks.NOTE_BLOCK, TexturedModel.CUBE);
/* 2398 */     createTrivialBlock(Blocks.PACKED_ICE, TexturedModel.CUBE);
/* 2399 */     createTrivialBlock(Blocks.OBSIDIAN, TexturedModel.CUBE);
/* 2400 */     createTrivialBlock(Blocks.QUARTZ_BRICKS, TexturedModel.CUBE);
/* 2401 */     createTrivialBlock(Blocks.SEA_LANTERN, TexturedModel.CUBE);
/* 2402 */     createTrivialBlock(Blocks.SHROOMLIGHT, TexturedModel.CUBE);
/* 2403 */     createTrivialBlock(Blocks.SOUL_SAND, TexturedModel.CUBE);
/* 2404 */     createTrivialBlock(Blocks.SOUL_SOIL, TexturedModel.CUBE);
/* 2405 */     createTrivialBlock(Blocks.SPAWNER, TexturedModel.CUBE);
/* 2406 */     createTrivialBlock(Blocks.SPONGE, TexturedModel.CUBE);
/* 2407 */     createTrivialBlock(Blocks.SEAGRASS, TexturedModel.SEAGRASS);
/* 2408 */     createSimpleFlatItemModel(Items.SEAGRASS);
/* 2409 */     createTrivialBlock(Blocks.TNT, TexturedModel.CUBE_TOP_BOTTOM);
/* 2410 */     createTrivialBlock(Blocks.TARGET, TexturedModel.COLUMN);
/* 2411 */     createTrivialBlock(Blocks.WARPED_WART_BLOCK, TexturedModel.CUBE);
/* 2412 */     createTrivialBlock(Blocks.WET_SPONGE, TexturedModel.CUBE);
/* 2413 */     createTrivialBlock(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, TexturedModel.CUBE);
/* 2414 */     createTrivialBlock(Blocks.CHISELED_QUARTZ_BLOCK, TexturedModel.COLUMN.updateTexture(debug0 -> debug0.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CHISELED_QUARTZ_BLOCK))));
/* 2415 */     createTrivialBlock(Blocks.CHISELED_STONE_BRICKS, TexturedModel.CUBE);
/* 2416 */     createChiseledSandsone(Blocks.CHISELED_SANDSTONE, Blocks.SANDSTONE);
/* 2417 */     createChiseledSandsone(Blocks.CHISELED_RED_SANDSTONE, Blocks.RED_SANDSTONE);
/* 2418 */     createTrivialBlock(Blocks.CHISELED_POLISHED_BLACKSTONE, TexturedModel.CUBE);
/*      */     
/* 2420 */     createWeightedPressurePlate(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.GOLD_BLOCK);
/* 2421 */     createWeightedPressurePlate(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.IRON_BLOCK);
/*      */     
/* 2423 */     createBookshelf();
/* 2424 */     createBrewingStand();
/* 2425 */     createCakeBlock();
/* 2426 */     createCampfires(new Block[] { Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE });
/* 2427 */     createCartographyTable();
/* 2428 */     createCauldron();
/* 2429 */     createChorusFlower();
/* 2430 */     createChorusPlant();
/* 2431 */     createComposter();
/* 2432 */     createDaylightDetector();
/* 2433 */     createEndPortalFrame();
/* 2434 */     createRotatableColumn(Blocks.END_ROD);
/* 2435 */     createFarmland();
/* 2436 */     createFire();
/* 2437 */     createSoulFire();
/* 2438 */     createFrostedIce();
/* 2439 */     createGrassBlocks();
/* 2440 */     createCocoa();
/* 2441 */     createGrassPath();
/* 2442 */     createGrindstone();
/* 2443 */     createHopper();
/* 2444 */     createIronBars();
/* 2445 */     createLever();
/* 2446 */     createLilyPad();
/* 2447 */     createNetherPortalBlock();
/* 2448 */     createNetherrack();
/* 2449 */     createObserver();
/* 2450 */     createPistons();
/* 2451 */     createPistonHeads();
/* 2452 */     createScaffolding();
/* 2453 */     createRedstoneTorch();
/* 2454 */     createRedstoneLamp();
/* 2455 */     createRepeater();
/* 2456 */     createSeaPickle();
/* 2457 */     createSmithingTable();
/* 2458 */     createSnowBlocks();
/* 2459 */     createStonecutter();
/* 2460 */     createStructureBlock();
/* 2461 */     createSweetBerryBush();
/* 2462 */     createTripwire();
/* 2463 */     createTripwireHook();
/* 2464 */     createTurtleEgg();
/* 2465 */     createVine();
/* 2466 */     createMagmaBlock();
/* 2467 */     createJigsaw();
/*      */     
/* 2469 */     createNonTemplateHorizontalBlock(Blocks.LADDER);
/* 2470 */     createSimpleFlatItemModel(Blocks.LADDER);
/* 2471 */     createNonTemplateHorizontalBlock(Blocks.LECTERN);
/*      */     
/* 2473 */     createNormalTorch(Blocks.TORCH, Blocks.WALL_TORCH);
/* 2474 */     createNormalTorch(Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH);
/*      */     
/* 2476 */     createCraftingTableLike(Blocks.CRAFTING_TABLE, Blocks.OAK_PLANKS, TextureMapping::craftingTable);
/* 2477 */     createCraftingTableLike(Blocks.FLETCHING_TABLE, Blocks.BIRCH_PLANKS, TextureMapping::fletchingTable);
/*      */     
/* 2479 */     createNyliumBlock(Blocks.CRIMSON_NYLIUM);
/* 2480 */     createNyliumBlock(Blocks.WARPED_NYLIUM);
/*      */     
/* 2482 */     createDispenserBlock(Blocks.DISPENSER);
/* 2483 */     createDispenserBlock(Blocks.DROPPER);
/*      */     
/* 2485 */     createLantern(Blocks.LANTERN);
/* 2486 */     createLantern(Blocks.SOUL_LANTERN);
/*      */     
/* 2488 */     createAxisAlignedPillarBlockCustomModel(Blocks.CHAIN, ModelLocationUtils.getModelLocation(Blocks.CHAIN));
/* 2489 */     createAxisAlignedPillarBlock(Blocks.BASALT, TexturedModel.COLUMN);
/* 2490 */     createAxisAlignedPillarBlock(Blocks.POLISHED_BASALT, TexturedModel.COLUMN);
/* 2491 */     createAxisAlignedPillarBlock(Blocks.BONE_BLOCK, TexturedModel.COLUMN);
/* 2492 */     createRotatedVariantBlock(Blocks.DIRT);
/* 2493 */     createRotatedVariantBlock(Blocks.SAND);
/* 2494 */     createRotatedVariantBlock(Blocks.RED_SAND);
/* 2495 */     createRotatedMirroredVariantBlock(Blocks.BEDROCK);
/*      */     
/* 2497 */     createRotatedPillarWithHorizontalVariant(Blocks.HAY_BLOCK, TexturedModel.COLUMN, TexturedModel.COLUMN_HORIZONTAL);
/* 2498 */     createRotatedPillarWithHorizontalVariant(Blocks.PURPUR_PILLAR, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);
/* 2499 */     createRotatedPillarWithHorizontalVariant(Blocks.QUARTZ_PILLAR, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);
/*      */     
/* 2501 */     createHorizontallyRotatedBlock(Blocks.LOOM, TexturedModel.ORIENTABLE);
/*      */     
/* 2503 */     createPumpkins();
/* 2504 */     createBeeNest(Blocks.BEE_NEST, TextureMapping::orientableCube);
/* 2505 */     createBeeNest(Blocks.BEEHIVE, TextureMapping::orientableCubeSameEnds);
/*      */ 
/*      */     
/* 2508 */     createCropBlock(Blocks.BEETROOTS, (Property<Integer>)BlockStateProperties.AGE_3, new int[] { 0, 1, 2, 3 });
/* 2509 */     createCropBlock(Blocks.CARROTS, (Property<Integer>)BlockStateProperties.AGE_7, new int[] { 0, 0, 1, 1, 2, 2, 2, 3 });
/* 2510 */     createCropBlock(Blocks.NETHER_WART, (Property<Integer>)BlockStateProperties.AGE_3, new int[] { 0, 1, 1, 2 });
/* 2511 */     createCropBlock(Blocks.POTATOES, (Property<Integer>)BlockStateProperties.AGE_7, new int[] { 0, 0, 1, 1, 2, 2, 2, 3 });
/* 2512 */     createCropBlock(Blocks.WHEAT, (Property<Integer>)BlockStateProperties.AGE_7, new int[] { 0, 1, 2, 3, 4, 5, 6, 7 });
/*      */     
/* 2514 */     blockEntityModels(ModelLocationUtils.decorateBlockModelLocation("banner"), Blocks.OAK_PLANKS)
/* 2515 */       .createWithCustomBlockItemModel(ModelTemplates.BANNER_INVENTORY, new Block[] {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           Blocks.WHITE_BANNER, Blocks.ORANGE_BANNER, Blocks.MAGENTA_BANNER, Blocks.LIGHT_BLUE_BANNER, Blocks.YELLOW_BANNER, Blocks.LIME_BANNER, Blocks.PINK_BANNER, Blocks.GRAY_BANNER, Blocks.LIGHT_GRAY_BANNER, Blocks.CYAN_BANNER,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           Blocks.PURPLE_BANNER, Blocks.BLUE_BANNER, Blocks.BROWN_BANNER, Blocks.GREEN_BANNER, Blocks.RED_BANNER, Blocks.BLACK_BANNER
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2533 */         }).createWithoutBlockItem(new Block[] { 
/*      */           Blocks.WHITE_WALL_BANNER, Blocks.ORANGE_WALL_BANNER, Blocks.MAGENTA_WALL_BANNER, Blocks.LIGHT_BLUE_WALL_BANNER, Blocks.YELLOW_WALL_BANNER, Blocks.LIME_WALL_BANNER, Blocks.PINK_WALL_BANNER, Blocks.GRAY_WALL_BANNER, Blocks.LIGHT_GRAY_WALL_BANNER, Blocks.CYAN_WALL_BANNER, 
/*      */           Blocks.PURPLE_WALL_BANNER, Blocks.BLUE_WALL_BANNER, Blocks.BROWN_WALL_BANNER, Blocks.GREEN_WALL_BANNER, Blocks.RED_WALL_BANNER, Blocks.BLACK_WALL_BANNER });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2552 */     blockEntityModels(ModelLocationUtils.decorateBlockModelLocation("bed"), Blocks.OAK_PLANKS)
/* 2553 */       .createWithoutBlockItem(new Block[] { 
/*      */           Blocks.WHITE_BED, Blocks.ORANGE_BED, Blocks.MAGENTA_BED, Blocks.LIGHT_BLUE_BED, Blocks.YELLOW_BED, Blocks.LIME_BED, Blocks.PINK_BED, Blocks.GRAY_BED, Blocks.LIGHT_GRAY_BED, Blocks.CYAN_BED, 
/*      */           Blocks.PURPLE_BED, Blocks.BLUE_BED, Blocks.BROWN_BED, Blocks.GREEN_BED, Blocks.RED_BED, Blocks.BLACK_BED });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2572 */     createBedItem(Blocks.WHITE_BED, Blocks.WHITE_WOOL);
/* 2573 */     createBedItem(Blocks.ORANGE_BED, Blocks.ORANGE_WOOL);
/* 2574 */     createBedItem(Blocks.MAGENTA_BED, Blocks.MAGENTA_WOOL);
/* 2575 */     createBedItem(Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_BLUE_WOOL);
/* 2576 */     createBedItem(Blocks.YELLOW_BED, Blocks.YELLOW_WOOL);
/* 2577 */     createBedItem(Blocks.LIME_BED, Blocks.LIME_WOOL);
/* 2578 */     createBedItem(Blocks.PINK_BED, Blocks.PINK_WOOL);
/* 2579 */     createBedItem(Blocks.GRAY_BED, Blocks.GRAY_WOOL);
/* 2580 */     createBedItem(Blocks.LIGHT_GRAY_BED, Blocks.LIGHT_GRAY_WOOL);
/* 2581 */     createBedItem(Blocks.CYAN_BED, Blocks.CYAN_WOOL);
/* 2582 */     createBedItem(Blocks.PURPLE_BED, Blocks.PURPLE_WOOL);
/* 2583 */     createBedItem(Blocks.BLUE_BED, Blocks.BLUE_WOOL);
/* 2584 */     createBedItem(Blocks.BROWN_BED, Blocks.BROWN_WOOL);
/* 2585 */     createBedItem(Blocks.GREEN_BED, Blocks.GREEN_WOOL);
/* 2586 */     createBedItem(Blocks.RED_BED, Blocks.RED_WOOL);
/* 2587 */     createBedItem(Blocks.BLACK_BED, Blocks.BLACK_WOOL);
/*      */     
/* 2589 */     blockEntityModels(ModelLocationUtils.decorateBlockModelLocation("skull"), Blocks.SOUL_SAND)
/* 2590 */       .createWithCustomBlockItemModel(ModelTemplates.SKULL_INVENTORY, new Block[] {
/*      */ 
/*      */           
/*      */           Blocks.CREEPER_HEAD, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL
/*      */ 
/*      */ 
/*      */         
/* 2597 */         }).create(new Block[] {
/*      */           
/*      */           Blocks.DRAGON_HEAD
/* 2600 */         }).createWithoutBlockItem(new Block[] { Blocks.CREEPER_WALL_HEAD, Blocks.DRAGON_WALL_HEAD, Blocks.PLAYER_WALL_HEAD, Blocks.ZOMBIE_WALL_HEAD, Blocks.SKELETON_WALL_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2609 */     createShulkerBox(Blocks.SHULKER_BOX);
/* 2610 */     createShulkerBox(Blocks.WHITE_SHULKER_BOX);
/* 2611 */     createShulkerBox(Blocks.ORANGE_SHULKER_BOX);
/* 2612 */     createShulkerBox(Blocks.MAGENTA_SHULKER_BOX);
/* 2613 */     createShulkerBox(Blocks.LIGHT_BLUE_SHULKER_BOX);
/* 2614 */     createShulkerBox(Blocks.YELLOW_SHULKER_BOX);
/* 2615 */     createShulkerBox(Blocks.LIME_SHULKER_BOX);
/* 2616 */     createShulkerBox(Blocks.PINK_SHULKER_BOX);
/* 2617 */     createShulkerBox(Blocks.GRAY_SHULKER_BOX);
/* 2618 */     createShulkerBox(Blocks.LIGHT_GRAY_SHULKER_BOX);
/* 2619 */     createShulkerBox(Blocks.CYAN_SHULKER_BOX);
/* 2620 */     createShulkerBox(Blocks.PURPLE_SHULKER_BOX);
/* 2621 */     createShulkerBox(Blocks.BLUE_SHULKER_BOX);
/* 2622 */     createShulkerBox(Blocks.BROWN_SHULKER_BOX);
/* 2623 */     createShulkerBox(Blocks.GREEN_SHULKER_BOX);
/* 2624 */     createShulkerBox(Blocks.RED_SHULKER_BOX);
/* 2625 */     createShulkerBox(Blocks.BLACK_SHULKER_BOX);
/*      */     
/* 2627 */     createTrivialBlock(Blocks.CONDUIT, TexturedModel.PARTICLE_ONLY);
/* 2628 */     skipAutoItemBlock(Blocks.CONDUIT);
/*      */     
/* 2630 */     blockEntityModels(ModelLocationUtils.decorateBlockModelLocation("chest"), Blocks.OAK_PLANKS)
/* 2631 */       .createWithoutBlockItem(new Block[] { Blocks.CHEST, Blocks.TRAPPED_CHEST });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2636 */     blockEntityModels(ModelLocationUtils.decorateBlockModelLocation("ender_chest"), Blocks.OBSIDIAN)
/* 2637 */       .createWithoutBlockItem(new Block[] { Blocks.ENDER_CHEST });
/*      */ 
/*      */ 
/*      */     
/* 2641 */     blockEntityModels(Blocks.END_PORTAL, Blocks.OBSIDIAN)
/* 2642 */       .create(new Block[] { Blocks.END_PORTAL, Blocks.END_GATEWAY });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2647 */     createTrivialCube(Blocks.WHITE_CONCRETE);
/* 2648 */     createTrivialCube(Blocks.ORANGE_CONCRETE);
/* 2649 */     createTrivialCube(Blocks.MAGENTA_CONCRETE);
/* 2650 */     createTrivialCube(Blocks.LIGHT_BLUE_CONCRETE);
/* 2651 */     createTrivialCube(Blocks.YELLOW_CONCRETE);
/* 2652 */     createTrivialCube(Blocks.LIME_CONCRETE);
/* 2653 */     createTrivialCube(Blocks.PINK_CONCRETE);
/* 2654 */     createTrivialCube(Blocks.GRAY_CONCRETE);
/* 2655 */     createTrivialCube(Blocks.LIGHT_GRAY_CONCRETE);
/* 2656 */     createTrivialCube(Blocks.CYAN_CONCRETE);
/* 2657 */     createTrivialCube(Blocks.PURPLE_CONCRETE);
/* 2658 */     createTrivialCube(Blocks.BLUE_CONCRETE);
/* 2659 */     createTrivialCube(Blocks.BROWN_CONCRETE);
/* 2660 */     createTrivialCube(Blocks.GREEN_CONCRETE);
/* 2661 */     createTrivialCube(Blocks.RED_CONCRETE);
/* 2662 */     createTrivialCube(Blocks.BLACK_CONCRETE);
/*      */     
/* 2664 */     createColoredBlockWithRandomRotations(TexturedModel.CUBE, new Block[] { Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER, Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER, Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER, Blocks.BLACK_CONCRETE_POWDER });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2683 */     createTrivialCube(Blocks.TERRACOTTA);
/* 2684 */     createTrivialCube(Blocks.WHITE_TERRACOTTA);
/* 2685 */     createTrivialCube(Blocks.ORANGE_TERRACOTTA);
/* 2686 */     createTrivialCube(Blocks.MAGENTA_TERRACOTTA);
/* 2687 */     createTrivialCube(Blocks.LIGHT_BLUE_TERRACOTTA);
/* 2688 */     createTrivialCube(Blocks.YELLOW_TERRACOTTA);
/* 2689 */     createTrivialCube(Blocks.LIME_TERRACOTTA);
/* 2690 */     createTrivialCube(Blocks.PINK_TERRACOTTA);
/* 2691 */     createTrivialCube(Blocks.GRAY_TERRACOTTA);
/* 2692 */     createTrivialCube(Blocks.LIGHT_GRAY_TERRACOTTA);
/* 2693 */     createTrivialCube(Blocks.CYAN_TERRACOTTA);
/* 2694 */     createTrivialCube(Blocks.PURPLE_TERRACOTTA);
/* 2695 */     createTrivialCube(Blocks.BLUE_TERRACOTTA);
/* 2696 */     createTrivialCube(Blocks.BROWN_TERRACOTTA);
/* 2697 */     createTrivialCube(Blocks.GREEN_TERRACOTTA);
/* 2698 */     createTrivialCube(Blocks.RED_TERRACOTTA);
/* 2699 */     createTrivialCube(Blocks.BLACK_TERRACOTTA);
/*      */     
/* 2701 */     createGlassBlocks(Blocks.GLASS, Blocks.GLASS_PANE);
/* 2702 */     createGlassBlocks(Blocks.WHITE_STAINED_GLASS, Blocks.WHITE_STAINED_GLASS_PANE);
/* 2703 */     createGlassBlocks(Blocks.ORANGE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS_PANE);
/* 2704 */     createGlassBlocks(Blocks.MAGENTA_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS_PANE);
/* 2705 */     createGlassBlocks(Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE);
/* 2706 */     createGlassBlocks(Blocks.YELLOW_STAINED_GLASS, Blocks.YELLOW_STAINED_GLASS_PANE);
/* 2707 */     createGlassBlocks(Blocks.LIME_STAINED_GLASS, Blocks.LIME_STAINED_GLASS_PANE);
/* 2708 */     createGlassBlocks(Blocks.PINK_STAINED_GLASS, Blocks.PINK_STAINED_GLASS_PANE);
/* 2709 */     createGlassBlocks(Blocks.GRAY_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS_PANE);
/* 2710 */     createGlassBlocks(Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE);
/* 2711 */     createGlassBlocks(Blocks.CYAN_STAINED_GLASS, Blocks.CYAN_STAINED_GLASS_PANE);
/* 2712 */     createGlassBlocks(Blocks.PURPLE_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS_PANE);
/* 2713 */     createGlassBlocks(Blocks.BLUE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS_PANE);
/* 2714 */     createGlassBlocks(Blocks.BROWN_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS_PANE);
/* 2715 */     createGlassBlocks(Blocks.GREEN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS_PANE);
/* 2716 */     createGlassBlocks(Blocks.RED_STAINED_GLASS, Blocks.RED_STAINED_GLASS_PANE);
/* 2717 */     createGlassBlocks(Blocks.BLACK_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS_PANE);
/*      */     
/* 2719 */     createColoredBlockWithStateRotations(TexturedModel.GLAZED_TERRACOTTA, new Block[] { Blocks.WHITE_GLAZED_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA, Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA, Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2738 */     createWoolBlocks(Blocks.WHITE_WOOL, Blocks.WHITE_CARPET);
/* 2739 */     createWoolBlocks(Blocks.ORANGE_WOOL, Blocks.ORANGE_CARPET);
/* 2740 */     createWoolBlocks(Blocks.MAGENTA_WOOL, Blocks.MAGENTA_CARPET);
/* 2741 */     createWoolBlocks(Blocks.LIGHT_BLUE_WOOL, Blocks.LIGHT_BLUE_CARPET);
/* 2742 */     createWoolBlocks(Blocks.YELLOW_WOOL, Blocks.YELLOW_CARPET);
/* 2743 */     createWoolBlocks(Blocks.LIME_WOOL, Blocks.LIME_CARPET);
/* 2744 */     createWoolBlocks(Blocks.PINK_WOOL, Blocks.PINK_CARPET);
/* 2745 */     createWoolBlocks(Blocks.GRAY_WOOL, Blocks.GRAY_CARPET);
/* 2746 */     createWoolBlocks(Blocks.LIGHT_GRAY_WOOL, Blocks.LIGHT_GRAY_CARPET);
/* 2747 */     createWoolBlocks(Blocks.CYAN_WOOL, Blocks.CYAN_CARPET);
/* 2748 */     createWoolBlocks(Blocks.PURPLE_WOOL, Blocks.PURPLE_CARPET);
/* 2749 */     createWoolBlocks(Blocks.BLUE_WOOL, Blocks.BLUE_CARPET);
/* 2750 */     createWoolBlocks(Blocks.BROWN_WOOL, Blocks.BROWN_CARPET);
/* 2751 */     createWoolBlocks(Blocks.GREEN_WOOL, Blocks.GREEN_CARPET);
/* 2752 */     createWoolBlocks(Blocks.RED_WOOL, Blocks.RED_CARPET);
/* 2753 */     createWoolBlocks(Blocks.BLACK_WOOL, Blocks.BLACK_CARPET);
/*      */     
/* 2755 */     createPlant(Blocks.FERN, Blocks.POTTED_FERN, TintState.TINTED);
/* 2756 */     createPlant(Blocks.DANDELION, Blocks.POTTED_DANDELION, TintState.NOT_TINTED);
/* 2757 */     createPlant(Blocks.POPPY, Blocks.POTTED_POPPY, TintState.NOT_TINTED);
/* 2758 */     createPlant(Blocks.BLUE_ORCHID, Blocks.POTTED_BLUE_ORCHID, TintState.NOT_TINTED);
/* 2759 */     createPlant(Blocks.ALLIUM, Blocks.POTTED_ALLIUM, TintState.NOT_TINTED);
/* 2760 */     createPlant(Blocks.AZURE_BLUET, Blocks.POTTED_AZURE_BLUET, TintState.NOT_TINTED);
/* 2761 */     createPlant(Blocks.RED_TULIP, Blocks.POTTED_RED_TULIP, TintState.NOT_TINTED);
/* 2762 */     createPlant(Blocks.ORANGE_TULIP, Blocks.POTTED_ORANGE_TULIP, TintState.NOT_TINTED);
/* 2763 */     createPlant(Blocks.WHITE_TULIP, Blocks.POTTED_WHITE_TULIP, TintState.NOT_TINTED);
/* 2764 */     createPlant(Blocks.PINK_TULIP, Blocks.POTTED_PINK_TULIP, TintState.NOT_TINTED);
/* 2765 */     createPlant(Blocks.OXEYE_DAISY, Blocks.POTTED_OXEYE_DAISY, TintState.NOT_TINTED);
/* 2766 */     createPlant(Blocks.CORNFLOWER, Blocks.POTTED_CORNFLOWER, TintState.NOT_TINTED);
/* 2767 */     createPlant(Blocks.LILY_OF_THE_VALLEY, Blocks.POTTED_LILY_OF_THE_VALLEY, TintState.NOT_TINTED);
/* 2768 */     createPlant(Blocks.WITHER_ROSE, Blocks.POTTED_WITHER_ROSE, TintState.NOT_TINTED);
/* 2769 */     createPlant(Blocks.RED_MUSHROOM, Blocks.POTTED_RED_MUSHROOM, TintState.NOT_TINTED);
/* 2770 */     createPlant(Blocks.BROWN_MUSHROOM, Blocks.POTTED_BROWN_MUSHROOM, TintState.NOT_TINTED);
/* 2771 */     createPlant(Blocks.DEAD_BUSH, Blocks.POTTED_DEAD_BUSH, TintState.NOT_TINTED);
/*      */     
/* 2773 */     createMushroomBlock(Blocks.BROWN_MUSHROOM_BLOCK);
/* 2774 */     createMushroomBlock(Blocks.RED_MUSHROOM_BLOCK);
/* 2775 */     createMushroomBlock(Blocks.MUSHROOM_STEM);
/*      */     
/* 2777 */     createCrossBlockWithDefaultItem(Blocks.GRASS, TintState.TINTED);
/* 2778 */     createCrossBlock(Blocks.SUGAR_CANE, TintState.TINTED);
/* 2779 */     createSimpleFlatItemModel(Items.SUGAR_CANE);
/* 2780 */     createGrowingPlant(Blocks.KELP, Blocks.KELP_PLANT, TintState.TINTED);
/* 2781 */     createSimpleFlatItemModel(Items.KELP);
/* 2782 */     skipAutoItemBlock(Blocks.KELP_PLANT);
/* 2783 */     createGrowingPlant(Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT, TintState.NOT_TINTED);
/* 2784 */     createGrowingPlant(Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT, TintState.NOT_TINTED);
/* 2785 */     createSimpleFlatItemModel(Blocks.WEEPING_VINES, "_plant");
/* 2786 */     skipAutoItemBlock(Blocks.WEEPING_VINES_PLANT);
/* 2787 */     createSimpleFlatItemModel(Blocks.TWISTING_VINES, "_plant");
/* 2788 */     skipAutoItemBlock(Blocks.TWISTING_VINES_PLANT);
/* 2789 */     createCrossBlockWithDefaultItem(Blocks.BAMBOO_SAPLING, TintState.TINTED, TextureMapping.cross(TextureMapping.getBlockTexture(Blocks.BAMBOO, "_stage0")));
/* 2790 */     createBamboo();
/*      */     
/* 2792 */     createCrossBlockWithDefaultItem(Blocks.COBWEB, TintState.NOT_TINTED);
/*      */     
/* 2794 */     createDoublePlant(Blocks.LILAC, TintState.NOT_TINTED);
/* 2795 */     createDoublePlant(Blocks.ROSE_BUSH, TintState.NOT_TINTED);
/* 2796 */     createDoublePlant(Blocks.PEONY, TintState.NOT_TINTED);
/* 2797 */     createDoublePlant(Blocks.TALL_GRASS, TintState.TINTED);
/* 2798 */     createDoublePlant(Blocks.LARGE_FERN, TintState.TINTED);
/*      */     
/* 2800 */     createSunflower();
/* 2801 */     createTallSeagrass();
/*      */     
/* 2803 */     createCoral(Blocks.TUBE_CORAL, Blocks.DEAD_TUBE_CORAL, Blocks.TUBE_CORAL_BLOCK, Blocks.DEAD_TUBE_CORAL_BLOCK, Blocks.TUBE_CORAL_FAN, Blocks.DEAD_TUBE_CORAL_FAN, Blocks.TUBE_CORAL_WALL_FAN, Blocks.DEAD_TUBE_CORAL_WALL_FAN);
/* 2804 */     createCoral(Blocks.BRAIN_CORAL, Blocks.DEAD_BRAIN_CORAL, Blocks.BRAIN_CORAL_BLOCK, Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.BRAIN_CORAL_FAN, Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.BRAIN_CORAL_WALL_FAN, Blocks.DEAD_BRAIN_CORAL_WALL_FAN);
/* 2805 */     createCoral(Blocks.BUBBLE_CORAL, Blocks.DEAD_BUBBLE_CORAL, Blocks.BUBBLE_CORAL_BLOCK, Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.BUBBLE_CORAL_FAN, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.BUBBLE_CORAL_WALL_FAN, Blocks.DEAD_BUBBLE_CORAL_WALL_FAN);
/* 2806 */     createCoral(Blocks.FIRE_CORAL, Blocks.DEAD_FIRE_CORAL, Blocks.FIRE_CORAL_BLOCK, Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.FIRE_CORAL_FAN, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.FIRE_CORAL_WALL_FAN, Blocks.DEAD_FIRE_CORAL_WALL_FAN);
/* 2807 */     createCoral(Blocks.HORN_CORAL, Blocks.DEAD_HORN_CORAL, Blocks.HORN_CORAL_BLOCK, Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.HORN_CORAL_FAN, Blocks.DEAD_HORN_CORAL_FAN, Blocks.HORN_CORAL_WALL_FAN, Blocks.DEAD_HORN_CORAL_WALL_FAN);
/*      */     
/* 2809 */     createStems(Blocks.MELON_STEM, Blocks.ATTACHED_MELON_STEM);
/* 2810 */     createStems(Blocks.PUMPKIN_STEM, Blocks.ATTACHED_PUMPKIN_STEM);
/*      */     
/* 2812 */     family(Blocks.ACACIA_PLANKS)
/* 2813 */       .button(Blocks.ACACIA_BUTTON)
/* 2814 */       .fence(Blocks.ACACIA_FENCE)
/* 2815 */       .fenceGate(Blocks.ACACIA_FENCE_GATE)
/* 2816 */       .pressurePlate(Blocks.ACACIA_PRESSURE_PLATE)
/* 2817 */       .sign(Blocks.ACACIA_SIGN, Blocks.ACACIA_WALL_SIGN)
/* 2818 */       .slab(Blocks.ACACIA_SLAB)
/* 2819 */       .stairs(Blocks.ACACIA_STAIRS);
/* 2820 */     createDoor(Blocks.ACACIA_DOOR);
/* 2821 */     createOrientableTrapdoor(Blocks.ACACIA_TRAPDOOR);
/* 2822 */     woodProvider(Blocks.ACACIA_LOG).logWithHorizontal(Blocks.ACACIA_LOG).wood(Blocks.ACACIA_WOOD);
/* 2823 */     woodProvider(Blocks.STRIPPED_ACACIA_LOG).logWithHorizontal(Blocks.STRIPPED_ACACIA_LOG).wood(Blocks.STRIPPED_ACACIA_WOOD);
/* 2824 */     createPlant(Blocks.ACACIA_SAPLING, Blocks.POTTED_ACACIA_SAPLING, TintState.NOT_TINTED);
/* 2825 */     createTrivialBlock(Blocks.ACACIA_LEAVES, TexturedModel.LEAVES);
/*      */     
/* 2827 */     family(Blocks.BIRCH_PLANKS)
/* 2828 */       .button(Blocks.BIRCH_BUTTON)
/* 2829 */       .fence(Blocks.BIRCH_FENCE)
/* 2830 */       .fenceGate(Blocks.BIRCH_FENCE_GATE)
/* 2831 */       .pressurePlate(Blocks.BIRCH_PRESSURE_PLATE)
/* 2832 */       .sign(Blocks.BIRCH_SIGN, Blocks.BIRCH_WALL_SIGN)
/* 2833 */       .slab(Blocks.BIRCH_SLAB)
/* 2834 */       .stairs(Blocks.BIRCH_STAIRS);
/* 2835 */     createDoor(Blocks.BIRCH_DOOR);
/* 2836 */     createOrientableTrapdoor(Blocks.BIRCH_TRAPDOOR);
/* 2837 */     woodProvider(Blocks.BIRCH_LOG).logWithHorizontal(Blocks.BIRCH_LOG).wood(Blocks.BIRCH_WOOD);
/* 2838 */     woodProvider(Blocks.STRIPPED_BIRCH_LOG).logWithHorizontal(Blocks.STRIPPED_BIRCH_LOG).wood(Blocks.STRIPPED_BIRCH_WOOD);
/* 2839 */     createPlant(Blocks.BIRCH_SAPLING, Blocks.POTTED_BIRCH_SAPLING, TintState.NOT_TINTED);
/* 2840 */     createTrivialBlock(Blocks.BIRCH_LEAVES, TexturedModel.LEAVES);
/*      */     
/* 2842 */     family(Blocks.OAK_PLANKS)
/* 2843 */       .button(Blocks.OAK_BUTTON)
/* 2844 */       .fence(Blocks.OAK_FENCE)
/* 2845 */       .fenceGate(Blocks.OAK_FENCE_GATE)
/* 2846 */       .pressurePlate(Blocks.OAK_PRESSURE_PLATE)
/* 2847 */       .sign(Blocks.OAK_SIGN, Blocks.OAK_WALL_SIGN)
/* 2848 */       .slab(Blocks.OAK_SLAB)
/* 2849 */       .slab(Blocks.PETRIFIED_OAK_SLAB)
/* 2850 */       .stairs(Blocks.OAK_STAIRS);
/* 2851 */     createDoor(Blocks.OAK_DOOR);
/* 2852 */     createTrapdoor(Blocks.OAK_TRAPDOOR);
/* 2853 */     woodProvider(Blocks.OAK_LOG).logWithHorizontal(Blocks.OAK_LOG).wood(Blocks.OAK_WOOD);
/* 2854 */     woodProvider(Blocks.STRIPPED_OAK_LOG).logWithHorizontal(Blocks.STRIPPED_OAK_LOG).wood(Blocks.STRIPPED_OAK_WOOD);
/* 2855 */     createPlant(Blocks.OAK_SAPLING, Blocks.POTTED_OAK_SAPLING, TintState.NOT_TINTED);
/* 2856 */     createTrivialBlock(Blocks.OAK_LEAVES, TexturedModel.LEAVES);
/*      */     
/* 2858 */     family(Blocks.SPRUCE_PLANKS)
/* 2859 */       .button(Blocks.SPRUCE_BUTTON)
/* 2860 */       .fence(Blocks.SPRUCE_FENCE)
/* 2861 */       .fenceGate(Blocks.SPRUCE_FENCE_GATE)
/* 2862 */       .pressurePlate(Blocks.SPRUCE_PRESSURE_PLATE)
/* 2863 */       .sign(Blocks.SPRUCE_SIGN, Blocks.SPRUCE_WALL_SIGN)
/* 2864 */       .slab(Blocks.SPRUCE_SLAB)
/* 2865 */       .stairs(Blocks.SPRUCE_STAIRS);
/* 2866 */     createDoor(Blocks.SPRUCE_DOOR);
/* 2867 */     createOrientableTrapdoor(Blocks.SPRUCE_TRAPDOOR);
/* 2868 */     woodProvider(Blocks.SPRUCE_LOG).logWithHorizontal(Blocks.SPRUCE_LOG).wood(Blocks.SPRUCE_WOOD);
/* 2869 */     woodProvider(Blocks.STRIPPED_SPRUCE_LOG).logWithHorizontal(Blocks.STRIPPED_SPRUCE_LOG).wood(Blocks.STRIPPED_SPRUCE_WOOD);
/* 2870 */     createPlant(Blocks.SPRUCE_SAPLING, Blocks.POTTED_SPRUCE_SAPLING, TintState.NOT_TINTED);
/* 2871 */     createTrivialBlock(Blocks.SPRUCE_LEAVES, TexturedModel.LEAVES);
/*      */     
/* 2873 */     family(Blocks.DARK_OAK_PLANKS)
/* 2874 */       .button(Blocks.DARK_OAK_BUTTON)
/* 2875 */       .fence(Blocks.DARK_OAK_FENCE)
/* 2876 */       .fenceGate(Blocks.DARK_OAK_FENCE_GATE)
/* 2877 */       .pressurePlate(Blocks.DARK_OAK_PRESSURE_PLATE)
/* 2878 */       .sign(Blocks.DARK_OAK_SIGN, Blocks.DARK_OAK_WALL_SIGN)
/* 2879 */       .slab(Blocks.DARK_OAK_SLAB)
/* 2880 */       .stairs(Blocks.DARK_OAK_STAIRS);
/* 2881 */     createDoor(Blocks.DARK_OAK_DOOR);
/* 2882 */     createTrapdoor(Blocks.DARK_OAK_TRAPDOOR);
/* 2883 */     woodProvider(Blocks.DARK_OAK_LOG).logWithHorizontal(Blocks.DARK_OAK_LOG).wood(Blocks.DARK_OAK_WOOD);
/* 2884 */     woodProvider(Blocks.STRIPPED_DARK_OAK_LOG).logWithHorizontal(Blocks.STRIPPED_DARK_OAK_LOG).wood(Blocks.STRIPPED_DARK_OAK_WOOD);
/* 2885 */     createPlant(Blocks.DARK_OAK_SAPLING, Blocks.POTTED_DARK_OAK_SAPLING, TintState.NOT_TINTED);
/* 2886 */     createTrivialBlock(Blocks.DARK_OAK_LEAVES, TexturedModel.LEAVES);
/*      */     
/* 2888 */     family(Blocks.JUNGLE_PLANKS)
/* 2889 */       .button(Blocks.JUNGLE_BUTTON)
/* 2890 */       .fence(Blocks.JUNGLE_FENCE)
/* 2891 */       .fenceGate(Blocks.JUNGLE_FENCE_GATE)
/* 2892 */       .pressurePlate(Blocks.JUNGLE_PRESSURE_PLATE)
/* 2893 */       .sign(Blocks.JUNGLE_SIGN, Blocks.JUNGLE_WALL_SIGN)
/* 2894 */       .slab(Blocks.JUNGLE_SLAB)
/* 2895 */       .stairs(Blocks.JUNGLE_STAIRS);
/* 2896 */     createDoor(Blocks.JUNGLE_DOOR);
/* 2897 */     createOrientableTrapdoor(Blocks.JUNGLE_TRAPDOOR);
/* 2898 */     woodProvider(Blocks.JUNGLE_LOG).logWithHorizontal(Blocks.JUNGLE_LOG).wood(Blocks.JUNGLE_WOOD);
/* 2899 */     woodProvider(Blocks.STRIPPED_JUNGLE_LOG).logWithHorizontal(Blocks.STRIPPED_JUNGLE_LOG).wood(Blocks.STRIPPED_JUNGLE_WOOD);
/* 2900 */     createPlant(Blocks.JUNGLE_SAPLING, Blocks.POTTED_JUNGLE_SAPLING, TintState.NOT_TINTED);
/* 2901 */     createTrivialBlock(Blocks.JUNGLE_LEAVES, TexturedModel.LEAVES);
/*      */     
/* 2903 */     family(Blocks.CRIMSON_PLANKS)
/* 2904 */       .button(Blocks.CRIMSON_BUTTON)
/* 2905 */       .fence(Blocks.CRIMSON_FENCE)
/* 2906 */       .fenceGate(Blocks.CRIMSON_FENCE_GATE)
/* 2907 */       .pressurePlate(Blocks.CRIMSON_PRESSURE_PLATE)
/* 2908 */       .sign(Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN)
/* 2909 */       .slab(Blocks.CRIMSON_SLAB)
/* 2910 */       .stairs(Blocks.CRIMSON_STAIRS);
/* 2911 */     createDoor(Blocks.CRIMSON_DOOR);
/* 2912 */     createOrientableTrapdoor(Blocks.CRIMSON_TRAPDOOR);
/* 2913 */     woodProvider(Blocks.CRIMSON_STEM).log(Blocks.CRIMSON_STEM).wood(Blocks.CRIMSON_HYPHAE);
/* 2914 */     woodProvider(Blocks.STRIPPED_CRIMSON_STEM).log(Blocks.STRIPPED_CRIMSON_STEM).wood(Blocks.STRIPPED_CRIMSON_HYPHAE);
/* 2915 */     createPlant(Blocks.CRIMSON_FUNGUS, Blocks.POTTED_CRIMSON_FUNGUS, TintState.NOT_TINTED);
/* 2916 */     createNetherRoots(Blocks.CRIMSON_ROOTS, Blocks.POTTED_CRIMSON_ROOTS);
/*      */     
/* 2918 */     family(Blocks.WARPED_PLANKS)
/* 2919 */       .button(Blocks.WARPED_BUTTON)
/* 2920 */       .fence(Blocks.WARPED_FENCE)
/* 2921 */       .fenceGate(Blocks.WARPED_FENCE_GATE)
/* 2922 */       .pressurePlate(Blocks.WARPED_PRESSURE_PLATE)
/* 2923 */       .sign(Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN)
/* 2924 */       .slab(Blocks.WARPED_SLAB)
/* 2925 */       .stairs(Blocks.WARPED_STAIRS);
/* 2926 */     createDoor(Blocks.WARPED_DOOR);
/* 2927 */     createOrientableTrapdoor(Blocks.WARPED_TRAPDOOR);
/* 2928 */     woodProvider(Blocks.WARPED_STEM).log(Blocks.WARPED_STEM).wood(Blocks.WARPED_HYPHAE);
/* 2929 */     woodProvider(Blocks.STRIPPED_WARPED_STEM).log(Blocks.STRIPPED_WARPED_STEM).wood(Blocks.STRIPPED_WARPED_HYPHAE);
/* 2930 */     createPlant(Blocks.WARPED_FUNGUS, Blocks.POTTED_WARPED_FUNGUS, TintState.NOT_TINTED);
/* 2931 */     createNetherRoots(Blocks.WARPED_ROOTS, Blocks.POTTED_WARPED_ROOTS);
/*      */     
/* 2933 */     createCrossBlock(Blocks.NETHER_SPROUTS, TintState.NOT_TINTED);
/* 2934 */     createSimpleFlatItemModel(Items.NETHER_SPROUTS);
/*      */     
/* 2936 */     family(TextureMapping.cube(Blocks.STONE))
/* 2937 */       .fullBlock(debug1 -> {
/*      */           ResourceLocation debug2 = ModelTemplates.CUBE_ALL.create(Blocks.STONE, debug1, this.modelOutput);
/*      */           
/*      */           ResourceLocation debug3 = ModelTemplates.CUBE_MIRRORED_ALL.create(Blocks.STONE, debug1, this.modelOutput);
/*      */           this.blockStateOutput.accept(createRotatedVariant(Blocks.STONE, debug2, debug3));
/*      */           return debug2;
/* 2943 */         }).slab(Blocks.STONE_SLAB)
/* 2944 */       .pressurePlate(Blocks.STONE_PRESSURE_PLATE)
/* 2945 */       .button(Blocks.STONE_BUTTON)
/* 2946 */       .stairs(Blocks.STONE_STAIRS);
/*      */     
/* 2948 */     createDoor(Blocks.IRON_DOOR);
/* 2949 */     createTrapdoor(Blocks.IRON_TRAPDOOR);
/*      */     
/* 2951 */     family(Blocks.STONE_BRICKS)
/* 2952 */       .wall(Blocks.STONE_BRICK_WALL)
/* 2953 */       .stairs(Blocks.STONE_BRICK_STAIRS)
/* 2954 */       .slab(Blocks.STONE_BRICK_SLAB);
/*      */     
/* 2956 */     family(Blocks.MOSSY_STONE_BRICKS)
/* 2957 */       .wall(Blocks.MOSSY_STONE_BRICK_WALL)
/* 2958 */       .stairs(Blocks.MOSSY_STONE_BRICK_STAIRS)
/* 2959 */       .slab(Blocks.MOSSY_STONE_BRICK_SLAB);
/*      */     
/* 2961 */     family(Blocks.COBBLESTONE)
/* 2962 */       .wall(Blocks.COBBLESTONE_WALL)
/* 2963 */       .stairs(Blocks.COBBLESTONE_STAIRS)
/* 2964 */       .slab(Blocks.COBBLESTONE_SLAB);
/*      */     
/* 2966 */     family(Blocks.MOSSY_COBBLESTONE)
/* 2967 */       .wall(Blocks.MOSSY_COBBLESTONE_WALL)
/* 2968 */       .stairs(Blocks.MOSSY_COBBLESTONE_STAIRS)
/* 2969 */       .slab(Blocks.MOSSY_COBBLESTONE_SLAB);
/*      */     
/* 2971 */     family(Blocks.PRISMARINE)
/* 2972 */       .wall(Blocks.PRISMARINE_WALL)
/* 2973 */       .stairs(Blocks.PRISMARINE_STAIRS)
/* 2974 */       .slab(Blocks.PRISMARINE_SLAB);
/*      */     
/* 2976 */     family(Blocks.PRISMARINE_BRICKS)
/* 2977 */       .stairs(Blocks.PRISMARINE_BRICK_STAIRS)
/* 2978 */       .slab(Blocks.PRISMARINE_BRICK_SLAB);
/*      */     
/* 2980 */     family(Blocks.DARK_PRISMARINE)
/* 2981 */       .stairs(Blocks.DARK_PRISMARINE_STAIRS)
/* 2982 */       .slab(Blocks.DARK_PRISMARINE_SLAB);
/*      */     
/* 2984 */     family(Blocks.SANDSTONE, TexturedModel.TOP_BOTTOM_WITH_WALL)
/* 2985 */       .wall(Blocks.SANDSTONE_WALL)
/* 2986 */       .stairs(Blocks.SANDSTONE_STAIRS)
/* 2987 */       .slab(Blocks.SANDSTONE_SLAB);
/*      */ 
/*      */     
/* 2990 */     family(Blocks.SMOOTH_SANDSTONE, TexturedModel.createAllSame(TextureMapping.getBlockTexture(Blocks.SANDSTONE, "_top")))
/* 2991 */       .slab(Blocks.SMOOTH_SANDSTONE_SLAB)
/* 2992 */       .stairs(Blocks.SMOOTH_SANDSTONE_STAIRS);
/*      */     
/* 2994 */     family(Blocks.CUT_SANDSTONE, TexturedModel.COLUMN.get(Blocks.SANDSTONE).updateTextures(debug0 -> debug0.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CUT_SANDSTONE))))
/* 2995 */       .slab(Blocks.CUT_SANDSTONE_SLAB);
/*      */     
/* 2997 */     family(Blocks.RED_SANDSTONE, TexturedModel.TOP_BOTTOM_WITH_WALL)
/* 2998 */       .wall(Blocks.RED_SANDSTONE_WALL)
/* 2999 */       .stairs(Blocks.RED_SANDSTONE_STAIRS)
/* 3000 */       .slab(Blocks.RED_SANDSTONE_SLAB);
/*      */     
/* 3002 */     family(Blocks.SMOOTH_RED_SANDSTONE, TexturedModel.createAllSame(TextureMapping.getBlockTexture(Blocks.RED_SANDSTONE, "_top")))
/* 3003 */       .slab(Blocks.SMOOTH_RED_SANDSTONE_SLAB)
/* 3004 */       .stairs(Blocks.SMOOTH_RED_SANDSTONE_STAIRS);
/*      */     
/* 3006 */     family(Blocks.CUT_RED_SANDSTONE, TexturedModel.COLUMN.get(Blocks.RED_SANDSTONE).updateTextures(debug0 -> debug0.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(Blocks.CUT_RED_SANDSTONE))))
/* 3007 */       .slab(Blocks.CUT_RED_SANDSTONE_SLAB);
/*      */     
/* 3009 */     family(Blocks.BRICKS)
/* 3010 */       .wall(Blocks.BRICK_WALL)
/* 3011 */       .stairs(Blocks.BRICK_STAIRS)
/* 3012 */       .slab(Blocks.BRICK_SLAB);
/*      */     
/* 3014 */     family(Blocks.NETHER_BRICKS)
/* 3015 */       .fence(Blocks.NETHER_BRICK_FENCE)
/* 3016 */       .wall(Blocks.NETHER_BRICK_WALL)
/* 3017 */       .stairs(Blocks.NETHER_BRICK_STAIRS)
/* 3018 */       .slab(Blocks.NETHER_BRICK_SLAB);
/*      */     
/* 3020 */     family(Blocks.PURPUR_BLOCK)
/* 3021 */       .stairs(Blocks.PURPUR_STAIRS)
/* 3022 */       .slab(Blocks.PURPUR_SLAB);
/*      */     
/* 3024 */     family(Blocks.DIORITE)
/* 3025 */       .wall(Blocks.DIORITE_WALL)
/* 3026 */       .stairs(Blocks.DIORITE_STAIRS)
/* 3027 */       .slab(Blocks.DIORITE_SLAB);
/*      */     
/* 3029 */     family(Blocks.POLISHED_DIORITE)
/* 3030 */       .stairs(Blocks.POLISHED_DIORITE_STAIRS)
/* 3031 */       .slab(Blocks.POLISHED_DIORITE_SLAB);
/*      */     
/* 3033 */     family(Blocks.GRANITE)
/* 3034 */       .wall(Blocks.GRANITE_WALL)
/* 3035 */       .stairs(Blocks.GRANITE_STAIRS)
/* 3036 */       .slab(Blocks.GRANITE_SLAB);
/*      */     
/* 3038 */     family(Blocks.POLISHED_GRANITE)
/* 3039 */       .stairs(Blocks.POLISHED_GRANITE_STAIRS)
/* 3040 */       .slab(Blocks.POLISHED_GRANITE_SLAB);
/*      */     
/* 3042 */     family(Blocks.ANDESITE)
/* 3043 */       .wall(Blocks.ANDESITE_WALL)
/* 3044 */       .stairs(Blocks.ANDESITE_STAIRS)
/* 3045 */       .slab(Blocks.ANDESITE_SLAB);
/*      */     
/* 3047 */     family(Blocks.POLISHED_ANDESITE)
/* 3048 */       .stairs(Blocks.POLISHED_ANDESITE_STAIRS)
/* 3049 */       .slab(Blocks.POLISHED_ANDESITE_SLAB);
/*      */     
/* 3051 */     family(Blocks.END_STONE_BRICKS)
/* 3052 */       .wall(Blocks.END_STONE_BRICK_WALL)
/* 3053 */       .stairs(Blocks.END_STONE_BRICK_STAIRS)
/* 3054 */       .slab(Blocks.END_STONE_BRICK_SLAB);
/*      */     
/* 3056 */     family(Blocks.QUARTZ_BLOCK, TexturedModel.COLUMN)
/* 3057 */       .stairs(Blocks.QUARTZ_STAIRS)
/* 3058 */       .slab(Blocks.QUARTZ_SLAB);
/*      */     
/* 3060 */     family(Blocks.SMOOTH_QUARTZ, TexturedModel.createAllSame(TextureMapping.getBlockTexture(Blocks.QUARTZ_BLOCK, "_bottom")))
/* 3061 */       .stairs(Blocks.SMOOTH_QUARTZ_STAIRS)
/* 3062 */       .slab(Blocks.SMOOTH_QUARTZ_SLAB);
/*      */     
/* 3064 */     family(Blocks.RED_NETHER_BRICKS)
/* 3065 */       .slab(Blocks.RED_NETHER_BRICK_SLAB)
/* 3066 */       .stairs(Blocks.RED_NETHER_BRICK_STAIRS)
/* 3067 */       .wall(Blocks.RED_NETHER_BRICK_WALL);
/*      */     
/* 3069 */     family(Blocks.BLACKSTONE, TexturedModel.COLUMN_WITH_WALL)
/* 3070 */       .wall(Blocks.BLACKSTONE_WALL)
/* 3071 */       .stairs(Blocks.BLACKSTONE_STAIRS)
/* 3072 */       .slab(Blocks.BLACKSTONE_SLAB);
/*      */     
/* 3074 */     family(Blocks.POLISHED_BLACKSTONE_BRICKS)
/* 3075 */       .wall(Blocks.POLISHED_BLACKSTONE_BRICK_WALL)
/* 3076 */       .stairs(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS)
/* 3077 */       .slab(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
/*      */     
/* 3079 */     family(Blocks.POLISHED_BLACKSTONE)
/* 3080 */       .wall(Blocks.POLISHED_BLACKSTONE_WALL)
/* 3081 */       .pressurePlate(Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE)
/* 3082 */       .button(Blocks.POLISHED_BLACKSTONE_BUTTON)
/* 3083 */       .stairs(Blocks.POLISHED_BLACKSTONE_STAIRS)
/* 3084 */       .slab(Blocks.POLISHED_BLACKSTONE_SLAB);
/*      */     
/* 3086 */     createSmoothStoneSlab();
/*      */     
/* 3088 */     createPassiveRail(Blocks.RAIL);
/* 3089 */     createActiveRail(Blocks.POWERED_RAIL);
/* 3090 */     createActiveRail(Blocks.DETECTOR_RAIL);
/* 3091 */     createActiveRail(Blocks.ACTIVATOR_RAIL);
/*      */     
/* 3093 */     createComparator();
/*      */     
/* 3095 */     createCommandBlock(Blocks.COMMAND_BLOCK);
/* 3096 */     createCommandBlock(Blocks.REPEATING_COMMAND_BLOCK);
/* 3097 */     createCommandBlock(Blocks.CHAIN_COMMAND_BLOCK);
/*      */     
/* 3099 */     createAnvil(Blocks.ANVIL);
/* 3100 */     createAnvil(Blocks.CHIPPED_ANVIL);
/* 3101 */     createAnvil(Blocks.DAMAGED_ANVIL);
/*      */     
/* 3103 */     createBarrel();
/* 3104 */     createBell();
/*      */     
/* 3106 */     createFurnace(Blocks.FURNACE, TexturedModel.ORIENTABLE_ONLY_TOP);
/* 3107 */     createFurnace(Blocks.BLAST_FURNACE, TexturedModel.ORIENTABLE_ONLY_TOP);
/* 3108 */     createFurnace(Blocks.SMOKER, TexturedModel.ORIENTABLE);
/*      */     
/* 3110 */     createRedstoneWire();
/*      */     
/* 3112 */     createRespawnAnchor();
/*      */     
/* 3114 */     copyModel(Blocks.CHISELED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS);
/* 3115 */     copyModel(Blocks.COBBLESTONE, Blocks.INFESTED_COBBLESTONE);
/* 3116 */     copyModel(Blocks.CRACKED_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS);
/* 3117 */     copyModel(Blocks.MOSSY_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS);
/* 3118 */     createInfestedStone();
/* 3119 */     copyModel(Blocks.STONE_BRICKS, Blocks.INFESTED_STONE_BRICKS);
/*      */     
/* 3121 */     SpawnEggItem.eggs().forEach(debug1 -> delegateItemModel((Item)debug1, ModelLocationUtils.decorateItemModelLocation("template_spawn_egg")));
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\BlockModelGenerators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */