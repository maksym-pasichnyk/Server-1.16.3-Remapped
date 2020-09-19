/*     */ package net.minecraft.world.level.portal;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.BlockUtil;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.NetherPortalBlock;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class PortalShape {
/*     */   private static final BlockBehaviour.StatePredicate FRAME;
/*     */   
/*     */   static {
/*  29 */     FRAME = ((debug0, debug1, debug2) -> debug0.is(Blocks.OBSIDIAN));
/*     */   }
/*     */   private final LevelAccessor level;
/*     */   private final Direction.Axis axis;
/*     */   private final Direction rightDir;
/*     */   private int numPortalBlocks;
/*     */   @Nullable
/*     */   private BlockPos bottomLeft;
/*     */   private int height;
/*     */   private int width;
/*     */   
/*     */   public static Optional<PortalShape> findEmptyPortalShape(LevelAccessor debug0, BlockPos debug1, Direction.Axis debug2) {
/*  41 */     return findPortalShape(debug0, debug1, debug0 -> (debug0.isValid() && debug0.numPortalBlocks == 0), debug2);
/*     */   }
/*     */   
/*     */   public static Optional<PortalShape> findPortalShape(LevelAccessor debug0, BlockPos debug1, Predicate<PortalShape> debug2, Direction.Axis debug3) {
/*  45 */     Optional<PortalShape> debug4 = Optional.<PortalShape>of(new PortalShape(debug0, debug1, debug3)).filter(debug2);
/*  46 */     if (debug4.isPresent()) {
/*  47 */       return debug4;
/*     */     }
/*     */     
/*  50 */     Direction.Axis debug5 = (debug3 == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;
/*  51 */     return Optional.<PortalShape>of(new PortalShape(debug0, debug1, debug5)).filter(debug2);
/*     */   }
/*     */   
/*     */   public PortalShape(LevelAccessor debug1, BlockPos debug2, Direction.Axis debug3) {
/*  55 */     this.level = debug1;
/*  56 */     this.axis = debug3;
/*  57 */     this.rightDir = (debug3 == Direction.Axis.X) ? Direction.WEST : Direction.SOUTH;
/*     */     
/*  59 */     this.bottomLeft = calculateBottomLeft(debug2);
/*  60 */     if (this.bottomLeft == null) {
/*  61 */       this.bottomLeft = debug2;
/*  62 */       this.width = 1;
/*  63 */       this.height = 1;
/*     */     } else {
/*  65 */       this.width = calculateWidth();
/*     */       
/*  67 */       if (this.width > 0) {
/*  68 */         this.height = calculateHeight();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private BlockPos calculateBottomLeft(BlockPos debug1) {
/*  76 */     int debug2 = Math.max(0, debug1.getY() - 21);
/*  77 */     while (debug1.getY() > debug2 && isEmpty(this.level.getBlockState(debug1.below()))) {
/*  78 */       debug1 = debug1.below();
/*     */     }
/*     */     
/*  81 */     Direction debug3 = this.rightDir.getOpposite();
/*  82 */     int debug4 = getDistanceUntilEdgeAboveFrame(debug1, debug3) - 1;
/*  83 */     if (debug4 < 0) {
/*  84 */       return null;
/*     */     }
/*  86 */     return debug1.relative(debug3, debug4);
/*     */   }
/*     */   
/*     */   private int calculateWidth() {
/*  90 */     int debug1 = getDistanceUntilEdgeAboveFrame(this.bottomLeft, this.rightDir);
/*     */     
/*  92 */     if (debug1 < 2 || debug1 > 21) {
/*  93 */       return 0;
/*     */     }
/*     */     
/*  96 */     return debug1;
/*     */   }
/*     */   
/*     */   private int getDistanceUntilEdgeAboveFrame(BlockPos debug1, Direction debug2) {
/* 100 */     BlockPos.MutableBlockPos debug3 = new BlockPos.MutableBlockPos();
/*     */     
/* 102 */     for (int debug4 = 0; debug4 <= 21; debug4++) {
/* 103 */       debug3.set((Vec3i)debug1).move(debug2, debug4);
/*     */       
/* 105 */       BlockState debug5 = this.level.getBlockState((BlockPos)debug3);
/* 106 */       if (!isEmpty(debug5)) {
/* 107 */         if (FRAME.test(debug5, (BlockGetter)this.level, (BlockPos)debug3)) {
/* 108 */           return debug4;
/*     */         }
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/* 114 */       BlockState debug6 = this.level.getBlockState((BlockPos)debug3.move(Direction.DOWN));
/* 115 */       if (!FRAME.test(debug6, (BlockGetter)this.level, (BlockPos)debug3)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 120 */     return 0;
/*     */   }
/*     */   
/*     */   private int calculateHeight() {
/* 124 */     BlockPos.MutableBlockPos debug1 = new BlockPos.MutableBlockPos();
/* 125 */     int debug2 = getDistanceUntilTop(debug1);
/*     */     
/* 127 */     if (debug2 < 3 || debug2 > 21 || !hasTopFrame(debug1, debug2)) {
/* 128 */       return 0;
/*     */     }
/*     */     
/* 131 */     return debug2;
/*     */   }
/*     */   
/*     */   private boolean hasTopFrame(BlockPos.MutableBlockPos debug1, int debug2) {
/* 135 */     for (int debug3 = 0; debug3 < this.width; debug3++) {
/* 136 */       BlockPos.MutableBlockPos debug4 = debug1.set((Vec3i)this.bottomLeft).move(Direction.UP, debug2).move(this.rightDir, debug3);
/* 137 */       if (!FRAME.test(this.level.getBlockState((BlockPos)debug4), (BlockGetter)this.level, (BlockPos)debug4)) {
/* 138 */         return false;
/*     */       }
/*     */     } 
/* 141 */     return true;
/*     */   }
/*     */   
/*     */   private int getDistanceUntilTop(BlockPos.MutableBlockPos debug1) {
/* 145 */     for (int debug2 = 0; debug2 < 21; debug2++) {
/*     */       
/* 147 */       debug1.set((Vec3i)this.bottomLeft).move(Direction.UP, debug2).move(this.rightDir, -1);
/* 148 */       if (!FRAME.test(this.level.getBlockState((BlockPos)debug1), (BlockGetter)this.level, (BlockPos)debug1)) {
/* 149 */         return debug2;
/*     */       }
/*     */ 
/*     */       
/* 153 */       debug1.set((Vec3i)this.bottomLeft).move(Direction.UP, debug2).move(this.rightDir, this.width);
/* 154 */       if (!FRAME.test(this.level.getBlockState((BlockPos)debug1), (BlockGetter)this.level, (BlockPos)debug1)) {
/* 155 */         return debug2;
/*     */       }
/*     */ 
/*     */       
/* 159 */       for (int debug3 = 0; debug3 < this.width; debug3++) {
/* 160 */         debug1.set((Vec3i)this.bottomLeft).move(Direction.UP, debug2).move(this.rightDir, debug3);
/*     */         
/* 162 */         BlockState debug4 = this.level.getBlockState((BlockPos)debug1);
/* 163 */         if (!isEmpty(debug4)) {
/* 164 */           return debug2;
/*     */         }
/*     */         
/* 167 */         if (debug4.is(Blocks.NETHER_PORTAL)) {
/* 168 */           this.numPortalBlocks++;
/*     */         }
/*     */       } 
/*     */     } 
/* 172 */     return 21;
/*     */   }
/*     */   
/*     */   private static boolean isEmpty(BlockState debug0) {
/* 176 */     return (debug0.isAir() || debug0.is((Tag)BlockTags.FIRE) || debug0.is(Blocks.NETHER_PORTAL));
/*     */   }
/*     */   
/*     */   public boolean isValid() {
/* 180 */     return (this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21);
/*     */   }
/*     */   
/*     */   public void createPortalBlocks() {
/* 184 */     BlockState debug1 = (BlockState)Blocks.NETHER_PORTAL.defaultBlockState().setValue((Property)NetherPortalBlock.AXIS, (Comparable)this.axis);
/*     */     
/* 186 */     BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1)).forEach(debug2 -> this.level.setBlock(debug2, debug1, 18));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/* 192 */     return (isValid() && this.numPortalBlocks == this.width * this.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Vec3 getRelativePosition(BlockUtil.FoundRectangle debug0, Direction.Axis debug1, Vec3 debug2, EntityDimensions debug3) {
/* 197 */     double debug9, debug11, debug4 = debug0.axis1Size - debug3.width;
/* 198 */     double debug6 = debug0.axis2Size - debug3.height;
/*     */     
/* 200 */     BlockPos debug8 = debug0.minCorner;
/*     */     
/* 202 */     if (debug4 > 0.0D) {
/* 203 */       float f = debug8.get(debug1) + debug3.width / 2.0F;
/* 204 */       debug9 = Mth.clamp(Mth.inverseLerp(debug2.get(debug1) - f, 0.0D, debug4), 0.0D, 1.0D);
/*     */     } else {
/* 206 */       debug9 = 0.5D;
/*     */     } 
/*     */     
/* 209 */     if (debug6 > 0.0D) {
/* 210 */       Direction.Axis axis = Direction.Axis.Y;
/* 211 */       debug11 = Mth.clamp(Mth.inverseLerp(debug2.get(axis) - debug8.get(axis), 0.0D, debug6), 0.0D, 1.0D);
/*     */     } else {
/* 213 */       debug11 = 0.0D;
/*     */     } 
/*     */     
/* 216 */     Direction.Axis debug13 = (debug1 == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;
/* 217 */     double debug14 = debug2.get(debug13) - debug8.get(debug13) + 0.5D;
/*     */     
/* 219 */     return new Vec3(debug9, debug11, debug14);
/*     */   }
/*     */   
/*     */   public static PortalInfo createPortalInfo(ServerLevel debug0, BlockUtil.FoundRectangle debug1, Direction.Axis debug2, Vec3 debug3, EntityDimensions debug4, Vec3 debug5, float debug6, float debug7) {
/* 223 */     BlockPos debug8 = debug1.minCorner;
/* 224 */     BlockState debug9 = debug0.getBlockState(debug8);
/* 225 */     Direction.Axis debug10 = (Direction.Axis)debug9.getValue((Property)BlockStateProperties.HORIZONTAL_AXIS);
/* 226 */     double debug11 = debug1.axis1Size;
/* 227 */     double debug13 = debug1.axis2Size;
/*     */     
/* 229 */     int debug15 = (debug2 == debug10) ? 0 : 90;
/* 230 */     Vec3 debug16 = (debug2 == debug10) ? debug5 : new Vec3(debug5.z, debug5.y, -debug5.x);
/*     */     
/* 232 */     double debug17 = debug4.width / 2.0D + (debug11 - debug4.width) * debug3.x();
/* 233 */     double debug19 = (debug13 - debug4.height) * debug3.y();
/* 234 */     double debug21 = 0.5D + debug3.z();
/*     */     
/* 236 */     boolean debug23 = (debug10 == Direction.Axis.X);
/*     */ 
/*     */ 
/*     */     
/* 240 */     Vec3 debug24 = new Vec3(debug8.getX() + (debug23 ? debug17 : debug21), debug8.getY() + debug19, debug8.getZ() + (debug23 ? debug21 : debug17));
/*     */ 
/*     */     
/* 243 */     return new PortalInfo(debug24, debug16, debug6 + debug15, debug7);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\portal\PortalShape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */