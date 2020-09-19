/*     */ package net.minecraft.world.level.block;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ 
/*     */ public class RailBlock extends BaseRailBlock {
/*  13 */   public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE;
/*     */   
/*     */   protected RailBlock(BlockBehaviour.Properties debug1) {
/*  16 */     super(false, debug1);
/*  17 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)SHAPE, (Comparable)RailShape.NORTH_SOUTH));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateState(BlockState debug1, Level debug2, BlockPos debug3, Block debug4) {
/*  22 */     if (debug4.defaultBlockState().isSignalSource() && (
/*  23 */       new RailState(debug2, debug3, debug1)).countPotentialConnections() == 3) {
/*  24 */       updateDir(debug2, debug3, debug1, false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Property<RailShape> getShapeProperty() {
/*  31 */     return (Property<RailShape>)SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/*  36 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/*  38 */         switch ((RailShape)debug1.getValue((Property)SHAPE)) {
/*     */           case LEFT_RIGHT:
/*  40 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_WEST);
/*     */           case FRONT_BACK:
/*  42 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_EAST);
/*     */           case null:
/*  44 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_SOUTH);
/*     */           case null:
/*  46 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_NORTH);
/*     */           case null:
/*  48 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_WEST);
/*     */           case null:
/*  50 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_EAST);
/*     */           case null:
/*  52 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_EAST);
/*     */           case null:
/*  54 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_WEST);
/*     */         } 
/*     */       case FRONT_BACK:
/*  57 */         switch ((RailShape)debug1.getValue((Property)SHAPE)) {
/*     */           case null:
/*  59 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.EAST_WEST);
/*     */           case null:
/*  61 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_SOUTH);
/*     */           case LEFT_RIGHT:
/*  63 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_NORTH);
/*     */           case FRONT_BACK:
/*  65 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_SOUTH);
/*     */           case null:
/*  67 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_WEST);
/*     */           case null:
/*  69 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_EAST);
/*     */           case null:
/*  71 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_EAST);
/*     */           case null:
/*  73 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_EAST);
/*     */           case null:
/*  75 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_WEST);
/*     */           case null:
/*  77 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_WEST);
/*     */         } 
/*     */       case null:
/*  80 */         switch ((RailShape)debug1.getValue((Property)SHAPE)) {
/*     */           case null:
/*  82 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.EAST_WEST);
/*     */           case null:
/*  84 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_SOUTH);
/*     */           case LEFT_RIGHT:
/*  86 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_SOUTH);
/*     */           case FRONT_BACK:
/*  88 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_NORTH);
/*     */           case null:
/*  90 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_EAST);
/*     */           case null:
/*  92 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_WEST);
/*     */           case null:
/*  94 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_WEST);
/*     */           case null:
/*  96 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_WEST);
/*     */           case null:
/*  98 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_EAST);
/*     */           case null:
/* 100 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_EAST);
/*     */         }  break;
/*     */     } 
/* 103 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 109 */     RailShape debug3 = (RailShape)debug1.getValue((Property)SHAPE);
/* 110 */     switch (debug2) {
/*     */       case LEFT_RIGHT:
/* 112 */         switch (debug3) {
/*     */           case null:
/* 114 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_SOUTH);
/*     */           case null:
/* 116 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_NORTH);
/*     */           case null:
/* 118 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_EAST);
/*     */           case null:
/* 120 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_WEST);
/*     */           case null:
/* 122 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_WEST);
/*     */           case null:
/* 124 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_EAST);
/*     */         } 
/*     */         
/*     */         break;
/*     */       
/*     */       case FRONT_BACK:
/* 130 */         switch (debug3) {
/*     */           case LEFT_RIGHT:
/* 132 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_WEST);
/*     */           case FRONT_BACK:
/* 134 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.ASCENDING_EAST);
/*     */           case null:
/* 136 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_WEST);
/*     */           case null:
/* 138 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.SOUTH_EAST);
/*     */           case null:
/* 140 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_EAST);
/*     */           case null:
/* 142 */             return (BlockState)debug1.setValue((Property)SHAPE, (Comparable)RailShape.NORTH_WEST);
/*     */         } 
/*     */ 
/*     */         
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 150 */     return super.mirror(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 155 */     debug1.add(new Property[] { (Property)SHAPE });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\RailBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */