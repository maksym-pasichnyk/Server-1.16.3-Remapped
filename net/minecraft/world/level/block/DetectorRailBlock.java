/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.vehicle.AbstractMinecart;
/*     */ import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class DetectorRailBlock extends BaseRailBlock {
/*  30 */   public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
/*  31 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */ 
/*     */   
/*     */   public DetectorRailBlock(BlockBehaviour.Properties debug1) {
/*  35 */     super(true, debug1);
/*  36 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)SHAPE, (Comparable)RailShape.NORTH_SOUTH));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSignalSource(BlockState debug1) {
/*  41 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void entityInside(BlockState debug1, Level debug2, BlockPos debug3, Entity debug4) {
/*  46 */     if (debug2.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/*  50 */     if (((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  54 */     checkPressed(debug2, debug3, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  59 */     if (!((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  63 */     checkPressed((Level)debug2, debug3, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/*  68 */     return ((Boolean)debug1.getValue((Property)POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDirectSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/*  73 */     if (!((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/*  74 */       return 0;
/*     */     }
/*  76 */     return (debug4 == Direction.UP) ? 15 : 0;
/*     */   }
/*     */   
/*     */   private void checkPressed(Level debug1, BlockPos debug2, BlockState debug3) {
/*  80 */     if (!canSurvive(debug3, (LevelReader)debug1, debug2)) {
/*     */       return;
/*     */     }
/*     */     
/*  84 */     boolean debug4 = ((Boolean)debug3.getValue((Property)POWERED)).booleanValue();
/*  85 */     boolean debug5 = false;
/*     */     
/*  87 */     List<AbstractMinecart> debug6 = getInteractingMinecartOfType(debug1, debug2, AbstractMinecart.class, (Predicate<Entity>)null);
/*  88 */     if (!debug6.isEmpty()) {
/*  89 */       debug5 = true;
/*     */     }
/*     */     
/*  92 */     if (debug5 && !debug4) {
/*  93 */       BlockState debug7 = (BlockState)debug3.setValue((Property)POWERED, Boolean.valueOf(true));
/*  94 */       debug1.setBlock(debug2, debug7, 3);
/*  95 */       updatePowerToConnected(debug1, debug2, debug7, true);
/*  96 */       debug1.updateNeighborsAt(debug2, this);
/*  97 */       debug1.updateNeighborsAt(debug2.below(), this);
/*  98 */       debug1.setBlocksDirty(debug2, debug3, debug7);
/*     */     } 
/*     */     
/* 101 */     if (!debug5 && debug4) {
/* 102 */       BlockState debug7 = (BlockState)debug3.setValue((Property)POWERED, Boolean.valueOf(false));
/* 103 */       debug1.setBlock(debug2, debug7, 3);
/* 104 */       updatePowerToConnected(debug1, debug2, debug7, false);
/* 105 */       debug1.updateNeighborsAt(debug2, this);
/* 106 */       debug1.updateNeighborsAt(debug2.below(), this);
/* 107 */       debug1.setBlocksDirty(debug2, debug3, debug7);
/*     */     } 
/*     */     
/* 110 */     if (debug5) {
/* 111 */       debug1.getBlockTicks().scheduleTick(debug2, this, 20);
/*     */     }
/*     */     
/* 114 */     debug1.updateNeighbourForOutputSignal(debug2, this);
/*     */   }
/*     */   
/*     */   protected void updatePowerToConnected(Level debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 118 */     RailState debug5 = new RailState(debug1, debug2, debug3);
/* 119 */     List<BlockPos> debug6 = debug5.getConnections();
/*     */     
/* 121 */     for (BlockPos debug8 : debug6) {
/* 122 */       BlockState debug9 = debug1.getBlockState(debug8);
/* 123 */       debug9.neighborChanged(debug1, debug8, debug9.getBlock(), debug2, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/* 129 */     if (debug4.is(debug1.getBlock())) {
/*     */       return;
/*     */     }
/*     */     
/* 133 */     checkPressed(debug2, debug3, updateState(debug1, debug2, debug3, debug5));
/*     */   }
/*     */ 
/*     */   
/*     */   public Property<RailShape> getShapeProperty() {
/* 138 */     return (Property<RailShape>)SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/* 143 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/* 148 */     if (((Boolean)debug1.getValue((Property)POWERED)).booleanValue()) {
/* 149 */       List<MinecartCommandBlock> debug4 = getInteractingMinecartOfType(debug2, debug3, MinecartCommandBlock.class, (Predicate<Entity>)null);
/* 150 */       if (!debug4.isEmpty()) {
/* 151 */         return ((MinecartCommandBlock)debug4.get(0)).getCommandBlock().getSuccessCount();
/*     */       }
/*     */       
/* 154 */       List<AbstractMinecart> debug5 = getInteractingMinecartOfType(debug2, debug3, AbstractMinecart.class, EntitySelector.CONTAINER_ENTITY_SELECTOR);
/* 155 */       if (!debug5.isEmpty()) {
/* 156 */         return AbstractContainerMenu.getRedstoneSignalFromContainer((Container)debug5.get(0));
/*     */       }
/*     */     } 
/*     */     
/* 160 */     return 0;
/*     */   }
/*     */   
/*     */   protected <T extends AbstractMinecart> List<T> getInteractingMinecartOfType(Level debug1, BlockPos debug2, Class<T> debug3, @Nullable Predicate<Entity> debug4) {
/* 164 */     return debug1.getEntitiesOfClass(debug3, getSearchBB(debug2), debug4);
/*     */   }
/*     */   
/*     */   private AABB getSearchBB(BlockPos debug1) {
/* 168 */     double debug2 = 0.2D;
/*     */     
/* 170 */     return new AABB(debug1.getX() + 0.2D, debug1.getY(), debug1.getZ() + 0.2D, (debug1.getX() + 1) - 0.2D, (debug1.getY() + 1) - 0.2D, (debug1.getZ() + 1) - 0.2D);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 175 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 177 */         switch ((RailShape)debug1.getValue((Property)SHAPE)) {
/*     */           case LEFT_RIGHT:
/* 179 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_WEST);
/*     */           case FRONT_BACK:
/* 181 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_EAST);
/*     */           case null:
/* 183 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_SOUTH);
/*     */           case null:
/* 185 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_NORTH);
/*     */           case null:
/* 187 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_WEST);
/*     */           case null:
/* 189 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_EAST);
/*     */           case null:
/* 191 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_EAST);
/*     */           case null:
/* 193 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_WEST);
/*     */         } 
/*     */       case FRONT_BACK:
/* 196 */         switch ((RailShape)debug1.getValue((Property)SHAPE)) {
/*     */           case null:
/* 198 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.EAST_WEST);
/*     */           case null:
/* 200 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_SOUTH);
/*     */           case LEFT_RIGHT:
/* 202 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_NORTH);
/*     */           case FRONT_BACK:
/* 204 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_SOUTH);
/*     */           case null:
/* 206 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_WEST);
/*     */           case null:
/* 208 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_EAST);
/*     */           case null:
/* 210 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_EAST);
/*     */           case null:
/* 212 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_EAST);
/*     */           case null:
/* 214 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_WEST);
/*     */           case null:
/* 216 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_WEST);
/*     */         } 
/*     */       case null:
/* 219 */         switch ((RailShape)debug1.getValue((Property)SHAPE)) {
/*     */           case null:
/* 221 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.EAST_WEST);
/*     */           case null:
/* 223 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_SOUTH);
/*     */           case LEFT_RIGHT:
/* 225 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_SOUTH);
/*     */           case FRONT_BACK:
/* 227 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_NORTH);
/*     */           case null:
/* 229 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_EAST);
/*     */           case null:
/* 231 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_WEST);
/*     */           case null:
/* 233 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_WEST);
/*     */           case null:
/* 235 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_WEST);
/*     */           case null:
/* 237 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_EAST);
/*     */           case null:
/* 239 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_EAST);
/*     */         }  break;
/*     */     } 
/* 242 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 248 */     RailShape debug3 = (RailShape)debug1.getValue((Property)SHAPE);
/* 249 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 251 */         switch (debug3) {
/*     */           case null:
/* 253 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_SOUTH);
/*     */           case null:
/* 255 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_NORTH);
/*     */           case null:
/* 257 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_EAST);
/*     */           case null:
/* 259 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_WEST);
/*     */           case null:
/* 261 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_WEST);
/*     */           case null:
/* 263 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_EAST);
/*     */         } 
/*     */         
/*     */         break;
/*     */       
/*     */       case FRONT_BACK:
/* 269 */         switch (debug3) {
/*     */           case LEFT_RIGHT:
/* 271 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_WEST);
/*     */           case FRONT_BACK:
/* 273 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_EAST);
/*     */           case null:
/* 275 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_WEST);
/*     */           case null:
/* 277 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_EAST);
/*     */           case null:
/* 279 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_EAST);
/*     */           case null:
/* 281 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_WEST);
/*     */         } 
/*     */ 
/*     */         
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 289 */     return super.mirror(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 294 */     debug1.add(new Property[] { (Property)SHAPE, (Property)POWERED });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\DetectorRailBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */