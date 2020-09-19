/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ 
/*     */ public class RailState
/*     */ {
/*     */   private final Level level;
/*     */   private final BlockPos pos;
/*     */   private final BaseRailBlock block;
/*     */   private BlockState state;
/*     */   private final boolean isStraight;
/*  19 */   private final List<BlockPos> connections = Lists.newArrayList();
/*     */   
/*     */   public RailState(Level debug1, BlockPos debug2, BlockState debug3) {
/*  22 */     this.level = debug1;
/*  23 */     this.pos = debug2;
/*  24 */     this.state = debug3;
/*  25 */     this.block = (BaseRailBlock)debug3.getBlock();
/*  26 */     RailShape debug4 = (RailShape)debug3.getValue(this.block.getShapeProperty());
/*  27 */     this.isStraight = this.block.isStraight();
/*  28 */     updateConnections(debug4);
/*     */   }
/*     */   
/*     */   public List<BlockPos> getConnections() {
/*  32 */     return this.connections;
/*     */   }
/*     */   
/*     */   private void updateConnections(RailShape debug1) {
/*  36 */     this.connections.clear();
/*  37 */     switch (debug1) {
/*     */       case NORTH_SOUTH:
/*  39 */         this.connections.add(this.pos.north());
/*  40 */         this.connections.add(this.pos.south());
/*     */         break;
/*     */       case EAST_WEST:
/*  43 */         this.connections.add(this.pos.west());
/*  44 */         this.connections.add(this.pos.east());
/*     */         break;
/*     */       case ASCENDING_EAST:
/*  47 */         this.connections.add(this.pos.west());
/*  48 */         this.connections.add(this.pos.east().above());
/*     */         break;
/*     */       case ASCENDING_WEST:
/*  51 */         this.connections.add(this.pos.west().above());
/*  52 */         this.connections.add(this.pos.east());
/*     */         break;
/*     */       case ASCENDING_NORTH:
/*  55 */         this.connections.add(this.pos.north().above());
/*  56 */         this.connections.add(this.pos.south());
/*     */         break;
/*     */       case ASCENDING_SOUTH:
/*  59 */         this.connections.add(this.pos.north());
/*  60 */         this.connections.add(this.pos.south().above());
/*     */         break;
/*     */       case SOUTH_EAST:
/*  63 */         this.connections.add(this.pos.east());
/*  64 */         this.connections.add(this.pos.south());
/*     */         break;
/*     */       case SOUTH_WEST:
/*  67 */         this.connections.add(this.pos.west());
/*  68 */         this.connections.add(this.pos.south());
/*     */         break;
/*     */       case NORTH_WEST:
/*  71 */         this.connections.add(this.pos.west());
/*  72 */         this.connections.add(this.pos.north());
/*     */         break;
/*     */       case NORTH_EAST:
/*  75 */         this.connections.add(this.pos.east());
/*  76 */         this.connections.add(this.pos.north());
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeSoftConnections() {
/*  82 */     for (int debug1 = 0; debug1 < this.connections.size(); debug1++) {
/*  83 */       RailState debug2 = getRail(this.connections.get(debug1));
/*  84 */       if (debug2 == null || !debug2.connectsTo(this)) {
/*  85 */         this.connections.remove(debug1--);
/*     */       } else {
/*  87 */         this.connections.set(debug1, debug2.pos);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasRail(BlockPos debug1) {
/*  93 */     return (BaseRailBlock.isRail(this.level, debug1) || BaseRailBlock.isRail(this.level, debug1.above()) || BaseRailBlock.isRail(this.level, debug1.below()));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private RailState getRail(BlockPos debug1) {
/*  98 */     BlockPos debug2 = debug1;
/*  99 */     BlockState debug3 = this.level.getBlockState(debug2);
/* 100 */     if (BaseRailBlock.isRail(debug3)) {
/* 101 */       return new RailState(this.level, debug2, debug3);
/*     */     }
/*     */     
/* 104 */     debug2 = debug1.above();
/* 105 */     debug3 = this.level.getBlockState(debug2);
/* 106 */     if (BaseRailBlock.isRail(debug3)) {
/* 107 */       return new RailState(this.level, debug2, debug3);
/*     */     }
/*     */     
/* 110 */     debug2 = debug1.below();
/* 111 */     debug3 = this.level.getBlockState(debug2);
/* 112 */     if (BaseRailBlock.isRail(debug3)) {
/* 113 */       return new RailState(this.level, debug2, debug3);
/*     */     }
/*     */     
/* 116 */     return null;
/*     */   }
/*     */   
/*     */   private boolean connectsTo(RailState debug1) {
/* 120 */     return hasConnection(debug1.pos);
/*     */   }
/*     */   
/*     */   private boolean hasConnection(BlockPos debug1) {
/* 124 */     for (int debug2 = 0; debug2 < this.connections.size(); debug2++) {
/* 125 */       BlockPos debug3 = this.connections.get(debug2);
/* 126 */       if (debug3.getX() == debug1.getX() && debug3.getZ() == debug1.getZ()) {
/* 127 */         return true;
/*     */       }
/*     */     } 
/* 130 */     return false;
/*     */   }
/*     */   
/*     */   protected int countPotentialConnections() {
/* 134 */     int debug1 = 0;
/*     */     
/* 136 */     for (Direction debug3 : Direction.Plane.HORIZONTAL) {
/* 137 */       if (hasRail(this.pos.relative(debug3))) {
/* 138 */         debug1++;
/*     */       }
/*     */     } 
/*     */     
/* 142 */     return debug1;
/*     */   }
/*     */   
/*     */   private boolean canConnectTo(RailState debug1) {
/* 146 */     return (connectsTo(debug1) || this.connections.size() != 2);
/*     */   }
/*     */   
/*     */   private void connectTo(RailState debug1) {
/* 150 */     this.connections.add(debug1.pos);
/*     */     
/* 152 */     BlockPos debug2 = this.pos.north();
/* 153 */     BlockPos debug3 = this.pos.south();
/* 154 */     BlockPos debug4 = this.pos.west();
/* 155 */     BlockPos debug5 = this.pos.east();
/*     */     
/* 157 */     boolean debug6 = hasConnection(debug2);
/* 158 */     boolean debug7 = hasConnection(debug3);
/* 159 */     boolean debug8 = hasConnection(debug4);
/* 160 */     boolean debug9 = hasConnection(debug5);
/*     */     
/* 162 */     RailShape debug10 = null;
/*     */     
/* 164 */     if (debug6 || debug7) {
/* 165 */       debug10 = RailShape.NORTH_SOUTH;
/*     */     }
/* 167 */     if (debug8 || debug9) {
/* 168 */       debug10 = RailShape.EAST_WEST;
/*     */     }
/* 170 */     if (!this.isStraight) {
/* 171 */       if (debug7 && debug9 && !debug6 && !debug8) {
/* 172 */         debug10 = RailShape.SOUTH_EAST;
/*     */       }
/* 174 */       if (debug7 && debug8 && !debug6 && !debug9) {
/* 175 */         debug10 = RailShape.SOUTH_WEST;
/*     */       }
/* 177 */       if (debug6 && debug8 && !debug7 && !debug9) {
/* 178 */         debug10 = RailShape.NORTH_WEST;
/*     */       }
/* 180 */       if (debug6 && debug9 && !debug7 && !debug8) {
/* 181 */         debug10 = RailShape.NORTH_EAST;
/*     */       }
/*     */     } 
/* 184 */     if (debug10 == RailShape.NORTH_SOUTH) {
/* 185 */       if (BaseRailBlock.isRail(this.level, debug2.above())) {
/* 186 */         debug10 = RailShape.ASCENDING_NORTH;
/*     */       }
/* 188 */       if (BaseRailBlock.isRail(this.level, debug3.above())) {
/* 189 */         debug10 = RailShape.ASCENDING_SOUTH;
/*     */       }
/*     */     } 
/* 192 */     if (debug10 == RailShape.EAST_WEST) {
/* 193 */       if (BaseRailBlock.isRail(this.level, debug5.above())) {
/* 194 */         debug10 = RailShape.ASCENDING_EAST;
/*     */       }
/* 196 */       if (BaseRailBlock.isRail(this.level, debug4.above())) {
/* 197 */         debug10 = RailShape.ASCENDING_WEST;
/*     */       }
/*     */     } 
/*     */     
/* 201 */     if (debug10 == null) {
/* 202 */       debug10 = RailShape.NORTH_SOUTH;
/*     */     }
/*     */     
/* 205 */     this.state = (BlockState)this.state.setValue(this.block.getShapeProperty(), (Comparable)debug10);
/* 206 */     this.level.setBlock(this.pos, this.state, 3);
/*     */   }
/*     */   
/*     */   private boolean hasNeighborRail(BlockPos debug1) {
/* 210 */     RailState debug2 = getRail(debug1);
/* 211 */     if (debug2 == null) {
/* 212 */       return false;
/*     */     }
/*     */     
/* 215 */     debug2.removeSoftConnections();
/* 216 */     return debug2.canConnectTo(this);
/*     */   }
/*     */   
/*     */   public RailState place(boolean debug1, boolean debug2, RailShape debug3) {
/* 220 */     BlockPos debug4 = this.pos.north();
/* 221 */     BlockPos debug5 = this.pos.south();
/* 222 */     BlockPos debug6 = this.pos.west();
/* 223 */     BlockPos debug7 = this.pos.east();
/*     */     
/* 225 */     boolean debug8 = hasNeighborRail(debug4);
/* 226 */     boolean debug9 = hasNeighborRail(debug5);
/* 227 */     boolean debug10 = hasNeighborRail(debug6);
/* 228 */     boolean debug11 = hasNeighborRail(debug7);
/*     */     
/* 230 */     RailShape debug12 = null;
/*     */     
/* 232 */     boolean debug13 = (debug8 || debug9);
/* 233 */     boolean debug14 = (debug10 || debug11);
/* 234 */     if (debug13 && !debug14) {
/* 235 */       debug12 = RailShape.NORTH_SOUTH;
/*     */     }
/* 237 */     if (debug14 && !debug13) {
/* 238 */       debug12 = RailShape.EAST_WEST;
/*     */     }
/*     */     
/* 241 */     boolean debug15 = (debug9 && debug11);
/* 242 */     boolean debug16 = (debug9 && debug10);
/* 243 */     boolean debug17 = (debug8 && debug11);
/* 244 */     boolean debug18 = (debug8 && debug10);
/*     */     
/* 246 */     if (!this.isStraight) {
/* 247 */       if (debug15 && !debug8 && !debug10) {
/* 248 */         debug12 = RailShape.SOUTH_EAST;
/*     */       }
/* 250 */       if (debug16 && !debug8 && !debug11) {
/* 251 */         debug12 = RailShape.SOUTH_WEST;
/*     */       }
/* 253 */       if (debug18 && !debug9 && !debug11) {
/* 254 */         debug12 = RailShape.NORTH_WEST;
/*     */       }
/* 256 */       if (debug17 && !debug9 && !debug10) {
/* 257 */         debug12 = RailShape.NORTH_EAST;
/*     */       }
/*     */     } 
/* 260 */     if (debug12 == null) {
/* 261 */       if (debug13 && debug14) {
/* 262 */         debug12 = debug3;
/* 263 */       } else if (debug13) {
/* 264 */         debug12 = RailShape.NORTH_SOUTH;
/* 265 */       } else if (debug14) {
/* 266 */         debug12 = RailShape.EAST_WEST;
/*     */       } 
/*     */       
/* 269 */       if (!this.isStraight) {
/* 270 */         if (debug1) {
/* 271 */           if (debug15) {
/* 272 */             debug12 = RailShape.SOUTH_EAST;
/*     */           }
/* 274 */           if (debug16) {
/* 275 */             debug12 = RailShape.SOUTH_WEST;
/*     */           }
/* 277 */           if (debug17) {
/* 278 */             debug12 = RailShape.NORTH_EAST;
/*     */           }
/* 280 */           if (debug18) {
/* 281 */             debug12 = RailShape.NORTH_WEST;
/*     */           }
/*     */         } else {
/* 284 */           if (debug18) {
/* 285 */             debug12 = RailShape.NORTH_WEST;
/*     */           }
/* 287 */           if (debug17) {
/* 288 */             debug12 = RailShape.NORTH_EAST;
/*     */           }
/* 290 */           if (debug16) {
/* 291 */             debug12 = RailShape.SOUTH_WEST;
/*     */           }
/* 293 */           if (debug15) {
/* 294 */             debug12 = RailShape.SOUTH_EAST;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 300 */     if (debug12 == RailShape.NORTH_SOUTH) {
/* 301 */       if (BaseRailBlock.isRail(this.level, debug4.above())) {
/* 302 */         debug12 = RailShape.ASCENDING_NORTH;
/*     */       }
/* 304 */       if (BaseRailBlock.isRail(this.level, debug5.above())) {
/* 305 */         debug12 = RailShape.ASCENDING_SOUTH;
/*     */       }
/*     */     } 
/* 308 */     if (debug12 == RailShape.EAST_WEST) {
/* 309 */       if (BaseRailBlock.isRail(this.level, debug7.above())) {
/* 310 */         debug12 = RailShape.ASCENDING_EAST;
/*     */       }
/* 312 */       if (BaseRailBlock.isRail(this.level, debug6.above())) {
/* 313 */         debug12 = RailShape.ASCENDING_WEST;
/*     */       }
/*     */     } 
/*     */     
/* 317 */     if (debug12 == null) {
/* 318 */       debug12 = debug3;
/*     */     }
/*     */     
/* 321 */     updateConnections(debug12);
/* 322 */     this.state = (BlockState)this.state.setValue(this.block.getShapeProperty(), (Comparable)debug12);
/*     */     
/* 324 */     if (debug2 || this.level.getBlockState(this.pos) != this.state) {
/* 325 */       this.level.setBlock(this.pos, this.state, 3);
/*     */       
/* 327 */       for (int debug19 = 0; debug19 < this.connections.size(); debug19++) {
/* 328 */         RailState debug20 = getRail(this.connections.get(debug19));
/* 329 */         if (debug20 != null) {
/*     */ 
/*     */           
/* 332 */           debug20.removeSoftConnections();
/*     */           
/* 334 */           if (debug20.canConnectTo(this)) {
/* 335 */             debug20.connectTo(this);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 340 */     return this;
/*     */   }
/*     */   
/*     */   public BlockState getState() {
/* 344 */     return this.state;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\RailState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */