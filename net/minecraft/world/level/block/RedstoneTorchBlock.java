/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.WeakHashMap;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class RedstoneTorchBlock extends TorchBlock {
/*  22 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*     */ 
/*     */   
/*  25 */   private static final Map<BlockGetter, List<Toggle>> RECENT_TOGGLES = new WeakHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RedstoneTorchBlock(BlockBehaviour.Properties debug1) {
/*  33 */     super(debug1, (ParticleOptions)DustParticleOptions.REDSTONE);
/*  34 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)LIT, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlace(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  39 */     for (Direction debug9 : Direction.values()) {
/*  40 */       debug2.updateNeighborsAt(debug3.relative(debug9), this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRemove(BlockState debug1, Level debug2, BlockPos debug3, BlockState debug4, boolean debug5) {
/*  46 */     if (debug5) {
/*     */       return;
/*     */     }
/*     */     
/*  50 */     for (Direction debug9 : Direction.values()) {
/*  51 */       debug2.updateNeighborsAt(debug3.relative(debug9), this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/*  57 */     if (((Boolean)debug1.getValue((Property)LIT)).booleanValue() && Direction.UP != debug4) {
/*  58 */       return 15;
/*     */     }
/*     */     
/*  61 */     return 0;
/*     */   }
/*     */   
/*     */   protected boolean hasNeighborSignal(Level debug1, BlockPos debug2, BlockState debug3) {
/*  65 */     return debug1.hasSignal(debug2.below(), Direction.DOWN);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  70 */     boolean debug5 = hasNeighborSignal((Level)debug2, debug3, debug1);
/*     */     
/*  72 */     List<Toggle> debug6 = RECENT_TOGGLES.get(debug2);
/*  73 */     while (debug6 != null && !debug6.isEmpty() && debug2.getGameTime() - (debug6.get(0)).when > 60L) {
/*  74 */       debug6.remove(0);
/*     */     }
/*     */     
/*  77 */     if (((Boolean)debug1.getValue((Property)LIT)).booleanValue()) {
/*  78 */       if (debug5) {
/*  79 */         debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)LIT, Boolean.valueOf(false)), 3);
/*     */         
/*  81 */         if (isToggledTooFrequently((Level)debug2, debug3, true)) {
/*  82 */           debug2.levelEvent(1502, debug3, 0);
/*  83 */           debug2.getBlockTicks().scheduleTick(debug3, debug2.getBlockState(debug3).getBlock(), 160);
/*     */         }
/*     */       
/*     */       } 
/*  87 */     } else if (!debug5 && !isToggledTooFrequently((Level)debug2, debug3, false)) {
/*  88 */       debug2.setBlock(debug3, (BlockState)debug1.setValue((Property)LIT, Boolean.valueOf(true)), 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/*  95 */     if (((Boolean)debug1.getValue((Property)LIT)).booleanValue() == hasNeighborSignal(debug2, debug3, debug1) && !debug2.getBlockTicks().willTickThisTick(debug3, this)) {
/*  96 */       debug2.getBlockTicks().scheduleTick(debug3, this, 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDirectSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 102 */     if (debug4 == Direction.DOWN) {
/* 103 */       return debug1.getSignal(debug2, debug3, debug4);
/*     */     }
/* 105 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSignalSource(BlockState debug1) {
/* 110 */     return true;
/*     */   }
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
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 128 */     debug1.add(new Property[] { (Property)LIT });
/*     */   }
/*     */   
/*     */   public static class Toggle {
/*     */     private final BlockPos pos;
/*     */     private final long when;
/*     */     
/*     */     public Toggle(BlockPos debug1, long debug2) {
/* 136 */       this.pos = debug1;
/* 137 */       this.when = debug2;
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isToggledTooFrequently(Level debug0, BlockPos debug1, boolean debug2) {
/* 142 */     List<Toggle> debug3 = RECENT_TOGGLES.computeIfAbsent(debug0, debug0 -> Lists.newArrayList());
/*     */     
/* 144 */     if (debug2) {
/* 145 */       debug3.add(new Toggle(debug1.immutable(), debug0.getGameTime()));
/*     */     }
/*     */     
/* 148 */     int debug4 = 0;
/* 149 */     for (int debug5 = 0; debug5 < debug3.size(); debug5++) {
/* 150 */       Toggle debug6 = debug3.get(debug5);
/*     */       
/* 152 */       debug4++;
/* 153 */       if (debug6.pos.equals(debug1) && debug4 >= 8) {
/* 154 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 158 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\RedstoneTorchBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */