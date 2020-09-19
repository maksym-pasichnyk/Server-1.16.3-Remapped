/*     */ package net.minecraft.world.level.portal;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.BlockUtil;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.TicketType;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.NetherPortalBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PortalForcer
/*     */ {
/*     */   private final ServerLevel level;
/*     */   
/*     */   public PortalForcer(ServerLevel debug1) {
/*  46 */     this.level = debug1;
/*     */   }
/*     */   
/*     */   public Optional<BlockUtil.FoundRectangle> findPortalAround(BlockPos debug1, boolean debug2) {
/*  50 */     PoiManager debug3 = this.level.getPoiManager();
/*  51 */     int debug4 = debug2 ? 16 : 128;
/*  52 */     debug3.ensureLoadedAndValid((LevelReader)this.level, debug1, debug4);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  57 */     Optional<PoiRecord> debug5 = debug3.getInSquare(debug0 -> (debug0 == PoiType.NETHER_PORTAL), debug1, debug4, PoiManager.Occupancy.ANY).sorted(Comparator.<PoiRecord>comparingDouble(debug1 -> debug1.getPos().distSqr((Vec3i)debug0)).thenComparingInt(debug0 -> debug0.getPos().getY())).filter(debug1 -> this.level.getBlockState(debug1.getPos()).hasProperty((Property)BlockStateProperties.HORIZONTAL_AXIS)).findFirst();
/*     */     
/*  59 */     return debug5.map(debug1 -> {
/*     */           BlockPos debug2 = debug1.getPos();
/*     */           this.level.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(debug2), 3, debug2);
/*     */           BlockState debug3 = this.level.getBlockState(debug2);
/*     */           return BlockUtil.getLargestRectangleAround(debug2, (Direction.Axis)debug3.getValue((Property)BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, ());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<BlockUtil.FoundRectangle> createPortal(BlockPos debug1, Direction.Axis debug2) {
/*  70 */     Direction debug3 = Direction.get(Direction.AxisDirection.POSITIVE, debug2);
/*     */     
/*  72 */     double debug4 = -1.0D;
/*  73 */     BlockPos debug6 = null;
/*  74 */     double debug7 = -1.0D;
/*  75 */     BlockPos debug9 = null;
/*     */     
/*  77 */     WorldBorder debug10 = this.level.getWorldBorder();
/*  78 */     int debug11 = this.level.getHeight() - 1;
/*     */     
/*  80 */     BlockPos.MutableBlockPos debug12 = debug1.mutable();
/*  81 */     for (BlockPos.MutableBlockPos mutableBlockPos : BlockPos.spiralAround(debug1, 16, Direction.EAST, Direction.SOUTH)) {
/*  82 */       int debug15 = Math.min(debug11, this.level.getHeight(Heightmap.Types.MOTION_BLOCKING, mutableBlockPos.getX(), mutableBlockPos.getZ()));
/*     */ 
/*     */       
/*  85 */       int debug16 = 1;
/*  86 */       if (!debug10.isWithinBounds((BlockPos)mutableBlockPos) || !debug10.isWithinBounds((BlockPos)mutableBlockPos.move(debug3, 1))) {
/*     */         continue;
/*     */       }
/*  89 */       mutableBlockPos.move(debug3.getOpposite(), 1);
/*     */       
/*  91 */       for (int debug17 = debug15; debug17 >= 0; debug17--) {
/*  92 */         mutableBlockPos.setY(debug17);
/*  93 */         if (this.level.isEmptyBlock((BlockPos)mutableBlockPos)) {
/*     */ 
/*     */ 
/*     */           
/*  97 */           int debug18 = debug17;
/*     */           
/*  99 */           while (debug17 > 0 && this.level.isEmptyBlock((BlockPos)mutableBlockPos.move(Direction.DOWN))) {
/* 100 */             debug17--;
/*     */           }
/*     */ 
/*     */           
/* 104 */           if (debug17 + 4 <= debug11) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 109 */             int debug19 = debug18 - debug17;
/* 110 */             if (debug19 <= 0 || debug19 >= 3) {
/*     */ 
/*     */ 
/*     */               
/* 114 */               mutableBlockPos.setY(debug17);
/*     */               
/* 116 */               if (canHostFrame((BlockPos)mutableBlockPos, debug12, debug3, 0)) {
/*     */                 
/* 118 */                 double debug20 = debug1.distSqr((Vec3i)mutableBlockPos);
/*     */ 
/*     */                 
/* 121 */                 if (canHostFrame((BlockPos)mutableBlockPos, debug12, debug3, -1) && 
/* 122 */                   canHostFrame((BlockPos)mutableBlockPos, debug12, debug3, 1))
/*     */                 {
/*     */                   
/* 125 */                   if (debug4 == -1.0D || debug4 > debug20) {
/* 126 */                     debug4 = debug20;
/* 127 */                     debug6 = mutableBlockPos.immutable();
/*     */                   } 
/*     */                 }
/*     */ 
/*     */                 
/* 132 */                 if (debug4 == -1.0D && (debug7 == -1.0D || debug7 > debug20)) {
/* 133 */                   debug7 = debug20;
/* 134 */                   debug9 = mutableBlockPos.immutable();
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 141 */     }  if (debug4 == -1.0D && debug7 != -1.0D) {
/* 142 */       debug6 = debug9;
/* 143 */       debug4 = debug7;
/*     */     } 
/*     */     
/* 146 */     if (debug4 == -1.0D) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 152 */       debug6 = (new BlockPos(debug1.getX(), Mth.clamp(debug1.getY(), 70, this.level.getHeight() - 10), debug1.getZ())).immutable();
/* 153 */       Direction direction = debug3.getClockWise();
/*     */ 
/*     */       
/* 156 */       if (!debug10.isWithinBounds(debug6))
/*     */       {
/* 158 */         return Optional.empty();
/*     */       }
/*     */ 
/*     */       
/* 162 */       for (int j = -1; j < 2; j++) {
/* 163 */         for (int debug15 = 0; debug15 < 2; debug15++) {
/*     */           
/* 165 */           for (int debug16 = -1; debug16 < 3; debug16++) {
/*     */             
/* 167 */             BlockState debug17 = (debug16 < 0) ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.AIR.defaultBlockState();
/*     */             
/* 169 */             debug12.setWithOffset((Vec3i)debug6, debug15 * debug3
/*     */                 
/* 171 */                 .getStepX() + j * direction.getStepX(), debug16, debug15 * debug3
/*     */                 
/* 173 */                 .getStepZ() + j * direction.getStepZ());
/*     */             
/* 175 */             this.level.setBlockAndUpdate((BlockPos)debug12, debug17);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 182 */     for (int i = -1; i < 3; i++) {
/* 183 */       for (int j = -1; j < 4; j++) {
/*     */         
/* 185 */         if (i == -1 || i == 2 || j == -1 || j == 3) {
/* 186 */           debug12.setWithOffset((Vec3i)debug6, i * debug3
/*     */               
/* 188 */               .getStepX(), j, i * debug3
/*     */               
/* 190 */               .getStepZ());
/*     */           
/* 192 */           this.level.setBlock((BlockPos)debug12, Blocks.OBSIDIAN.defaultBlockState(), 3);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 198 */     BlockState debug13 = (BlockState)Blocks.NETHER_PORTAL.defaultBlockState().setValue((Property)NetherPortalBlock.AXIS, (Comparable)debug2);
/*     */     
/* 200 */     for (int debug14 = 0; debug14 < 2; debug14++) {
/* 201 */       for (int debug15 = 0; debug15 < 3; debug15++) {
/* 202 */         debug12.setWithOffset((Vec3i)debug6, debug14 * debug3
/*     */             
/* 204 */             .getStepX(), debug15, debug14 * debug3
/*     */             
/* 206 */             .getStepZ());
/*     */         
/* 208 */         this.level.setBlock((BlockPos)debug12, debug13, 18);
/*     */       } 
/*     */     } 
/*     */     
/* 212 */     return Optional.of(new BlockUtil.FoundRectangle(debug6.immutable(), 2, 3));
/*     */   }
/*     */   
/*     */   private boolean canHostFrame(BlockPos debug1, BlockPos.MutableBlockPos debug2, Direction debug3, int debug4) {
/* 216 */     Direction debug5 = debug3.getClockWise();
/*     */     
/* 218 */     for (int debug6 = -1; debug6 < 3; debug6++) {
/* 219 */       for (int debug7 = -1; debug7 < 4; debug7++) {
/* 220 */         debug2.setWithOffset((Vec3i)debug1, debug3
/*     */             
/* 222 */             .getStepX() * debug6 + debug5.getStepX() * debug4, debug7, debug3
/*     */             
/* 224 */             .getStepZ() * debug6 + debug5.getStepZ() * debug4);
/*     */ 
/*     */         
/* 227 */         if (debug7 < 0 && !this.level.getBlockState((BlockPos)debug2).getMaterial().isSolid()) {
/* 228 */           return false;
/*     */         }
/* 230 */         if (debug7 >= 0 && !this.level.isEmptyBlock((BlockPos)debug2)) {
/* 231 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 236 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\portal\PortalForcer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */