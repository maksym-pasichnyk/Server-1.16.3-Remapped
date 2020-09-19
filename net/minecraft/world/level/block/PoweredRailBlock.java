/*     */ package net.minecraft.world.level.block;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ 
/*     */ public class PoweredRailBlock extends BaseRailBlock {
/*  14 */   public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
/*  15 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*     */   
/*     */   protected PoweredRailBlock(BlockBehaviour.Properties debug1) {
/*  18 */     super(true, debug1);
/*  19 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)SHAPE, (Comparable)RailShape.NORTH_SOUTH)).setValue((Property)POWERED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */   protected boolean findPoweredRailSignal(Level debug1, BlockPos debug2, BlockState debug3, boolean debug4, int debug5) {
/*  23 */     if (debug5 >= 8) {
/*  24 */       return false;
/*     */     }
/*     */     
/*  27 */     int debug6 = debug2.getX();
/*  28 */     int debug7 = debug2.getY();
/*  29 */     int debug8 = debug2.getZ();
/*     */     
/*  31 */     boolean debug9 = true;
/*  32 */     RailShape debug10 = (RailShape)debug3.getValue((Property)SHAPE);
/*  33 */     switch (debug10) {
/*     */       case LEFT_RIGHT:
/*  35 */         if (debug4) {
/*  36 */           debug8++; break;
/*     */         } 
/*  38 */         debug8--;
/*     */         break;
/*     */       
/*     */       case FRONT_BACK:
/*  42 */         if (debug4) {
/*  43 */           debug6--; break;
/*     */         } 
/*  45 */         debug6++;
/*     */         break;
/*     */       
/*     */       case null:
/*  49 */         if (debug4) {
/*  50 */           debug6--;
/*     */         } else {
/*  52 */           debug6++;
/*  53 */           debug7++;
/*  54 */           debug9 = false;
/*     */         } 
/*  56 */         debug10 = RailShape.EAST_WEST;
/*     */         break;
/*     */       case null:
/*  59 */         if (debug4) {
/*  60 */           debug6--;
/*  61 */           debug7++;
/*  62 */           debug9 = false;
/*     */         } else {
/*  64 */           debug6++;
/*     */         } 
/*  66 */         debug10 = RailShape.EAST_WEST;
/*     */         break;
/*     */       case null:
/*  69 */         if (debug4) {
/*  70 */           debug8++;
/*     */         } else {
/*  72 */           debug8--;
/*  73 */           debug7++;
/*  74 */           debug9 = false;
/*     */         } 
/*  76 */         debug10 = RailShape.NORTH_SOUTH;
/*     */         break;
/*     */       case null:
/*  79 */         if (debug4) {
/*  80 */           debug8++;
/*  81 */           debug7++;
/*  82 */           debug9 = false;
/*     */         } else {
/*  84 */           debug8--;
/*     */         } 
/*  86 */         debug10 = RailShape.NORTH_SOUTH;
/*     */         break;
/*     */     } 
/*     */     
/*  90 */     if (isSameRailWithPower(debug1, new BlockPos(debug6, debug7, debug8), debug4, debug5, debug10)) {
/*  91 */       return true;
/*     */     }
/*  93 */     if (debug9 && isSameRailWithPower(debug1, new BlockPos(debug6, debug7 - 1, debug8), debug4, debug5, debug10)) {
/*  94 */       return true;
/*     */     }
/*  96 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean isSameRailWithPower(Level debug1, BlockPos debug2, boolean debug3, int debug4, RailShape debug5) {
/* 100 */     BlockState debug6 = debug1.getBlockState(debug2);
/*     */     
/* 102 */     if (!debug6.is(this)) {
/* 103 */       return false;
/*     */     }
/*     */     
/* 106 */     RailShape debug7 = (RailShape)debug6.getValue((Property)SHAPE);
/* 107 */     if (debug5 == RailShape.EAST_WEST && (debug7 == RailShape.NORTH_SOUTH || debug7 == RailShape.ASCENDING_NORTH || debug7 == RailShape.ASCENDING_SOUTH)) {
/* 108 */       return false;
/*     */     }
/* 110 */     if (debug5 == RailShape.NORTH_SOUTH && (debug7 == RailShape.EAST_WEST || debug7 == RailShape.ASCENDING_EAST || debug7 == RailShape.ASCENDING_WEST)) {
/* 111 */       return false;
/*     */     }
/*     */     
/* 114 */     if (((Boolean)debug6.getValue((Property)POWERED)).booleanValue()) {
/* 115 */       if (debug1.hasNeighborSignal(debug2)) {
/* 116 */         return true;
/*     */       }
/* 118 */       return findPoweredRailSignal(debug1, debug2, debug6, debug3, debug4 + 1);
/*     */     } 
/*     */     
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateState(BlockState debug1, Level debug2, BlockPos debug3, Block debug4) {
/* 126 */     boolean debug5 = ((Boolean)debug1.getValue((Property)POWERED)).booleanValue();
/* 127 */     boolean debug6 = (debug2.hasNeighborSignal(debug3) || findPoweredRailSignal(debug2, debug3, debug1, true, 0) || findPoweredRailSignal(debug2, debug3, debug1, false, 0));
/*     */     
/* 129 */     if (debug6 != debug5) {
/* 130 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)POWERED, Boolean.valueOf(debug6)), 3);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 135 */       debug2.updateNeighborsAt(debug3.below(), this);
/* 136 */       if (((RailShape)debug1.getValue((Property)SHAPE)).isAscending()) {
/* 137 */         debug2.updateNeighborsAt(debug3.above(), this);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Property<RailShape> getShapeProperty() {
/* 144 */     return (Property<RailShape>)SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 149 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 151 */         switch ((RailShape)debug1.getValue((Property)SHAPE)) {
/*     */           case null:
/* 153 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_WEST);
/*     */           case null:
/* 155 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_EAST);
/*     */           case null:
/* 157 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_SOUTH);
/*     */           case null:
/* 159 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_NORTH);
/*     */           case null:
/* 161 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_WEST);
/*     */           case null:
/* 163 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_EAST);
/*     */           case null:
/* 165 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_EAST);
/*     */           case null:
/* 167 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_WEST);
/*     */         } 
/*     */       case FRONT_BACK:
/* 170 */         switch ((RailShape)debug1.getValue((Property)SHAPE)) {
/*     */           case LEFT_RIGHT:
/* 172 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.EAST_WEST);
/*     */           case FRONT_BACK:
/* 174 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_SOUTH);
/*     */           case null:
/* 176 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_NORTH);
/*     */           case null:
/* 178 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_SOUTH);
/*     */           case null:
/* 180 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_WEST);
/*     */           case null:
/* 182 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_EAST);
/*     */           case null:
/* 184 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_EAST);
/*     */           case null:
/* 186 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_EAST);
/*     */           case null:
/* 188 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_WEST);
/*     */           case null:
/* 190 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_WEST);
/*     */         } 
/*     */       case null:
/* 193 */         switch ((RailShape)debug1.getValue((Property)SHAPE)) {
/*     */           case LEFT_RIGHT:
/* 195 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.EAST_WEST);
/*     */           case FRONT_BACK:
/* 197 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_SOUTH);
/*     */           case null:
/* 199 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_SOUTH);
/*     */           case null:
/* 201 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_NORTH);
/*     */           case null:
/* 203 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_EAST);
/*     */           case null:
/* 205 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_WEST);
/*     */           case null:
/* 207 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_WEST);
/*     */           case null:
/* 209 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_WEST);
/*     */           case null:
/* 211 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_EAST);
/*     */           case null:
/* 213 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_EAST);
/*     */         }  break;
/*     */     } 
/* 216 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 222 */     RailShape debug3 = (RailShape)debug1.getValue((Property)SHAPE);
/* 223 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 225 */         switch (debug3) {
/*     */           case null:
/* 227 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_SOUTH);
/*     */           case null:
/* 229 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_NORTH);
/*     */           case null:
/* 231 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_EAST);
/*     */           case null:
/* 233 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_WEST);
/*     */           case null:
/* 235 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_WEST);
/*     */           case null:
/* 237 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_EAST);
/*     */         } 
/*     */         
/*     */         break;
/*     */       
/*     */       case FRONT_BACK:
/* 243 */         switch (debug3) {
/*     */           case null:
/* 245 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_WEST);
/*     */           case null:
/* 247 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_EAST);
/*     */           case null:
/* 249 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_WEST);
/*     */           case null:
/* 251 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_EAST);
/*     */           case null:
/* 253 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_EAST);
/*     */           case null:
/* 255 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_WEST);
/*     */         } 
/*     */ 
/*     */         
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 263 */     return super.mirror(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 268 */     debug1.add(new Property[] { (Property)SHAPE, (Property)POWERED });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\PoweredRailBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */