/*     */ package net.minecraft.world.level.block;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.decoration.ItemFrame;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.TickPriority;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.ComparatorMode;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class ComparatorBlock extends DiodeBlock implements EntityBlock {
/*  31 */   public static final EnumProperty<ComparatorMode> MODE = BlockStateProperties.MODE_COMPARATOR;
/*     */   
/*     */   public ComparatorBlock(BlockBehaviour.Properties debug1) {
/*  34 */     super(debug1);
/*  35 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)POWERED, Boolean.valueOf(false))).setValue((Property)MODE, (Comparable)ComparatorMode.COMPARE));
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDelay(BlockState debug1) {
/*  40 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getOutputSignal(BlockGetter debug1, BlockPos debug2, BlockState debug3) {
/*  45 */     BlockEntity debug4 = debug1.getBlockEntity(debug2);
/*  46 */     if (debug4 instanceof ComparatorBlockEntity) {
/*  47 */       return ((ComparatorBlockEntity)debug4).getOutputSignal();
/*     */     }
/*     */     
/*  50 */     return 0;
/*     */   }
/*     */   
/*     */   private int calculateOutputSignal(Level debug1, BlockPos debug2, BlockState debug3) {
/*  54 */     if (debug3.getValue((Property)MODE) == ComparatorMode.SUBTRACT) {
/*  55 */       return Math.max(getInputSignal(debug1, debug2, debug3) - getAlternateSignal((LevelReader)debug1, debug2, debug3), 0);
/*     */     }
/*     */     
/*  58 */     return getInputSignal(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldTurnOn(Level debug1, BlockPos debug2, BlockState debug3) {
/*  63 */     int debug4 = getInputSignal(debug1, debug2, debug3);
/*  64 */     if (debug4 == 0) {
/*  65 */       return false;
/*     */     }
/*     */     
/*  68 */     int debug5 = getAlternateSignal((LevelReader)debug1, debug2, debug3);
/*  69 */     if (debug4 > debug5) {
/*  70 */       return true;
/*     */     }
/*     */     
/*  73 */     return (debug4 == debug5 && debug3.getValue((Property)MODE) == ComparatorMode.COMPARE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getInputSignal(Level debug1, BlockPos debug2, BlockState debug3) {
/*  78 */     int debug4 = super.getInputSignal(debug1, debug2, debug3);
/*     */     
/*  80 */     Direction debug5 = (Direction)debug3.getValue((Property)FACING);
/*  81 */     BlockPos debug6 = debug2.relative(debug5);
/*  82 */     BlockState debug7 = debug1.getBlockState(debug6);
/*     */     
/*  84 */     if (debug7.hasAnalogOutputSignal()) {
/*  85 */       debug4 = debug7.getAnalogOutputSignal(debug1, debug6);
/*  86 */     } else if (debug4 < 15 && debug7.isRedstoneConductor((BlockGetter)debug1, debug6)) {
/*  87 */       debug6 = debug6.relative(debug5);
/*  88 */       debug7 = debug1.getBlockState(debug6);
/*  89 */       ItemFrame debug8 = getItemFrame(debug1, debug5, debug6);
/*     */       
/*  91 */       int debug9 = Math.max((debug8 == null) ? Integer.MIN_VALUE : debug8
/*  92 */           .getAnalogOutput(), 
/*  93 */           debug7.hasAnalogOutputSignal() ? debug7.getAnalogOutputSignal(debug1, debug6) : Integer.MIN_VALUE);
/*     */ 
/*     */       
/*  96 */       if (debug9 != Integer.MIN_VALUE) {
/*  97 */         debug4 = debug9;
/*     */       }
/*     */     } 
/*     */     
/* 101 */     return debug4;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private ItemFrame getItemFrame(Level debug1, Direction debug2, BlockPos debug3) {
/* 106 */     List<ItemFrame> debug4 = debug1.getEntitiesOfClass(ItemFrame.class, new AABB(debug3.getX(), debug3.getY(), debug3.getZ(), (debug3.getX() + 1), (debug3.getY() + 1), (debug3.getZ() + 1)), debug1 -> (debug1 != null && debug1.getDirection() == debug0));
/*     */     
/* 108 */     if (debug4.size() == 1) {
/* 109 */       return debug4.get(0);
/*     */     }
/*     */     
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 117 */     if (!debug4.abilities.mayBuild) {
/* 118 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 121 */     debug1 = (BlockState)debug1.cycle((Property)MODE);
/* 122 */     float debug7 = (debug1.getValue((Property)MODE) == ComparatorMode.SUBTRACT) ? 0.55F : 0.5F;
/* 123 */     debug2.playSound(debug4, debug3, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 0.3F, debug7);
/*     */     
/* 125 */     debug2.setBlock(debug3, debug1, 2);
/* 126 */     refreshOutputState(debug2, debug3, debug1);
/* 127 */     return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkTickOnNeighbor(Level debug1, BlockPos debug2, BlockState debug3) {
/* 132 */     if (debug1.getBlockTicks().willTickThisTick(debug2, this)) {
/*     */       return;
/*     */     }
/*     */     
/* 136 */     int debug4 = calculateOutputSignal(debug1, debug2, debug3);
/* 137 */     BlockEntity debug5 = debug1.getBlockEntity(debug2);
/* 138 */     int debug6 = (debug5 instanceof ComparatorBlockEntity) ? ((ComparatorBlockEntity)debug5).getOutputSignal() : 0;
/*     */     
/* 140 */     if (debug4 != debug6 || ((Boolean)debug3.getValue((Property)POWERED)).booleanValue() != shouldTurnOn(debug1, debug2, debug3)) {
/*     */       
/* 142 */       TickPriority debug7 = shouldPrioritize((BlockGetter)debug1, debug2, debug3) ? TickPriority.HIGH : TickPriority.NORMAL;
/* 143 */       debug1.getBlockTicks().scheduleTick(debug2, this, 2, debug7);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void refreshOutputState(Level debug1, BlockPos debug2, BlockState debug3) {
/* 148 */     int debug4 = calculateOutputSignal(debug1, debug2, debug3);
/*     */     
/* 150 */     BlockEntity debug5 = debug1.getBlockEntity(debug2);
/* 151 */     int debug6 = 0;
/* 152 */     if (debug5 instanceof ComparatorBlockEntity) {
/* 153 */       ComparatorBlockEntity debug7 = (ComparatorBlockEntity)debug5;
/*     */       
/* 155 */       debug6 = debug7.getOutputSignal();
/* 156 */       debug7.setOutputSignal(debug4);
/*     */     } 
/*     */     
/* 159 */     if (debug6 != debug4 || debug3.getValue((Property)MODE) == ComparatorMode.COMPARE) {
/* 160 */       boolean debug7 = shouldTurnOn(debug1, debug2, debug3);
/* 161 */       boolean debug8 = ((Boolean)debug3.getValue((Property)POWERED)).booleanValue();
/*     */       
/* 163 */       if (debug8 && !debug7) {
/* 164 */         debug1.setBlock(debug2, (BlockState)debug3.setValue((Property)POWERED, Boolean.valueOf(false)), 2);
/* 165 */       } else if (!debug8 && debug7) {
/* 166 */         debug1.setBlock(debug2, (BlockState)debug3.setValue((Property)POWERED, Boolean.valueOf(true)), 2);
/*     */       } 
/*     */       
/* 169 */       updateNeighborsInFront(debug1, debug2, debug3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 175 */     refreshOutputState((Level)debug2, debug3, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean triggerEvent(BlockState debug1, Level debug2, BlockPos debug3, int debug4, int debug5) {
/* 180 */     super.triggerEvent(debug1, debug2, debug3, debug4, debug5);
/*     */     
/* 182 */     BlockEntity debug6 = debug2.getBlockEntity(debug3);
/* 183 */     return (debug6 != null && debug6.triggerEvent(debug4, debug5));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 188 */     return (BlockEntity)new ComparatorBlockEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 193 */     debug1.add(new Property[] { (Property)FACING, (Property)MODE, (Property)POWERED });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\ComparatorBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */