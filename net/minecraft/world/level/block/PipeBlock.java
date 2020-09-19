/*    */ package net.minecraft.world.level.block;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.EnumMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class PipeBlock extends Block {
/* 18 */   private static final Direction[] DIRECTIONS = Direction.values();
/*    */   
/* 20 */   public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
/* 21 */   public static final BooleanProperty EAST = BlockStateProperties.EAST;
/* 22 */   public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
/* 23 */   public static final BooleanProperty WEST = BlockStateProperties.WEST;
/* 24 */   public static final BooleanProperty UP = BlockStateProperties.UP;
/* 25 */   public static final BooleanProperty DOWN = BlockStateProperties.DOWN; public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
/*    */   static {
/* 27 */     PROPERTY_BY_DIRECTION = (Map<Direction, BooleanProperty>)Util.make(Maps.newEnumMap(Direction.class), debug0 -> {
/*    */           debug0.put(Direction.NORTH, NORTH);
/*    */           debug0.put(Direction.EAST, EAST);
/*    */           debug0.put(Direction.SOUTH, SOUTH);
/*    */           debug0.put(Direction.WEST, WEST);
/*    */           debug0.put(Direction.UP, UP);
/*    */           debug0.put(Direction.DOWN, DOWN);
/*    */         });
/*    */   }
/*    */   protected final VoxelShape[] shapeByIndex;
/*    */   
/*    */   protected PipeBlock(float debug1, BlockBehaviour.Properties debug2) {
/* 39 */     super(debug2);
/*    */     
/* 41 */     this.shapeByIndex = makeShapes(debug1);
/*    */   }
/*    */   
/*    */   private VoxelShape[] makeShapes(float debug1) {
/* 45 */     float debug2 = 0.5F - debug1;
/* 46 */     float debug3 = 0.5F + debug1;
/*    */     
/* 48 */     VoxelShape debug4 = Block.box((debug2 * 16.0F), (debug2 * 16.0F), (debug2 * 16.0F), (debug3 * 16.0F), (debug3 * 16.0F), (debug3 * 16.0F));
/*    */     
/* 50 */     VoxelShape[] debug5 = new VoxelShape[DIRECTIONS.length];
/*    */     
/* 52 */     for (int i = 0; i < DIRECTIONS.length; i++) {
/* 53 */       Direction direction = DIRECTIONS[i];
/* 54 */       debug5[i] = Shapes.box(0.5D + 
/* 55 */           Math.min(-debug1, direction.getStepX() * 0.5D), 0.5D + 
/* 56 */           Math.min(-debug1, direction.getStepY() * 0.5D), 0.5D + 
/* 57 */           Math.min(-debug1, direction.getStepZ() * 0.5D), 0.5D + 
/* 58 */           Math.max(debug1, direction.getStepX() * 0.5D), 0.5D + 
/* 59 */           Math.max(debug1, direction.getStepY() * 0.5D), 0.5D + 
/* 60 */           Math.max(debug1, direction.getStepZ() * 0.5D));
/*    */     } 
/*    */ 
/*    */     
/* 64 */     VoxelShape[] debug6 = new VoxelShape[64];
/* 65 */     for (int debug7 = 0; debug7 < 64; debug7++) {
/* 66 */       VoxelShape debug8 = debug4;
/* 67 */       for (int debug9 = 0; debug9 < DIRECTIONS.length; debug9++) {
/* 68 */         if ((debug7 & 1 << debug9) != 0) {
/* 69 */           debug8 = Shapes.or(debug8, debug5[debug9]);
/*    */         }
/*    */       } 
/* 72 */       debug6[debug7] = debug8;
/*    */     } 
/* 74 */     return debug6;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean propagatesSkylightDown(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 79 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 84 */     return this.shapeByIndex[getAABBIndex(debug1)];
/*    */   }
/*    */   
/*    */   protected int getAABBIndex(BlockState debug1) {
/* 88 */     int debug2 = 0;
/* 89 */     for (int debug3 = 0; debug3 < DIRECTIONS.length; debug3++) {
/* 90 */       if (((Boolean)debug1.getValue((Property)PROPERTY_BY_DIRECTION.get(DIRECTIONS[debug3]))).booleanValue()) {
/* 91 */         debug2 |= 1 << debug3;
/*    */       }
/*    */     } 
/* 94 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\PipeBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */