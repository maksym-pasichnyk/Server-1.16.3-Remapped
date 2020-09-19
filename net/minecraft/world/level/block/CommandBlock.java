/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BaseCommandBlock;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DirectionProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class CommandBlock
/*     */   extends BaseEntityBlock {
/*  33 */   private static final Logger LOGGER = LogManager.getLogger();
/*  34 */   public static final DirectionProperty FACING = DirectionalBlock.FACING;
/*  35 */   public static final BooleanProperty CONDITIONAL = BlockStateProperties.CONDITIONAL;
/*     */   
/*     */   public CommandBlock(BlockBehaviour.Properties debug1) {
/*  38 */     super(debug1);
/*  39 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)CONDITIONAL, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/*  44 */     CommandBlockEntity debug2 = new CommandBlockEntity();
/*  45 */     debug2.setAutomatic((this == Blocks.CHAIN_COMMAND_BLOCK));
/*  46 */     return (BlockEntity)debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void neighborChanged(BlockState debug1, Level debug2, BlockPos debug3, Block debug4, BlockPos debug5, boolean debug6) {
/*  51 */     if (debug2.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/*  55 */     BlockEntity debug7 = debug2.getBlockEntity(debug3);
/*  56 */     if (!(debug7 instanceof CommandBlockEntity)) {
/*     */       return;
/*     */     }
/*     */     
/*  60 */     CommandBlockEntity debug8 = (CommandBlockEntity)debug7;
/*  61 */     boolean debug9 = debug2.hasNeighborSignal(debug3);
/*  62 */     boolean debug10 = debug8.isPowered();
/*     */     
/*  64 */     debug8.setPowered(debug9);
/*     */     
/*  66 */     if (debug10 || debug8.isAutomatic() || debug8.getMode() == CommandBlockEntity.Mode.SEQUENCE) {
/*     */       return;
/*     */     }
/*     */     
/*  70 */     if (debug9) {
/*  71 */       debug8.markConditionMet();
/*     */       
/*  73 */       debug2.getBlockTicks().scheduleTick(debug3, this, 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/*  79 */     BlockEntity debug5 = debug2.getBlockEntity(debug3);
/*  80 */     if (debug5 instanceof CommandBlockEntity) {
/*  81 */       CommandBlockEntity debug6 = (CommandBlockEntity)debug5;
/*  82 */       BaseCommandBlock debug7 = debug6.getCommandBlock();
/*  83 */       boolean debug8 = !StringUtil.isNullOrEmpty(debug7.getCommand());
/*  84 */       CommandBlockEntity.Mode debug9 = debug6.getMode();
/*     */       
/*  86 */       boolean debug10 = debug6.wasConditionMet();
/*  87 */       if (debug9 == CommandBlockEntity.Mode.AUTO) {
/*  88 */         debug6.markConditionMet();
/*     */         
/*  90 */         if (debug10) {
/*  91 */           execute(debug1, (Level)debug2, debug3, debug7, debug8);
/*  92 */         } else if (debug6.isConditional()) {
/*  93 */           debug7.setSuccessCount(0);
/*     */         } 
/*     */         
/*  96 */         if (debug6.isPowered() || debug6.isAutomatic()) {
/*  97 */           debug2.getBlockTicks().scheduleTick(debug3, this, 1);
/*     */         }
/*  99 */       } else if (debug9 == CommandBlockEntity.Mode.REDSTONE) {
/* 100 */         if (debug10) {
/* 101 */           execute(debug1, (Level)debug2, debug3, debug7, debug8);
/* 102 */         } else if (debug6.isConditional()) {
/* 103 */           debug7.setSuccessCount(0);
/*     */         } 
/*     */       } 
/*     */       
/* 107 */       debug2.updateNeighbourForOutputSignal(debug3, this);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void execute(BlockState debug1, Level debug2, BlockPos debug3, BaseCommandBlock debug4, boolean debug5) {
/* 112 */     if (debug5) {
/* 113 */       debug4.performCommand(debug2);
/*     */     } else {
/* 115 */       debug4.setSuccessCount(0);
/*     */     } 
/*     */     
/* 118 */     executeChain(debug2, debug3, (Direction)debug1.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 123 */     BlockEntity debug7 = debug2.getBlockEntity(debug3);
/* 124 */     if (debug7 instanceof CommandBlockEntity && debug4.canUseGameMasterBlocks()) {
/* 125 */       debug4.openCommandBlock((CommandBlockEntity)debug7);
/* 126 */       return InteractionResult.sidedSuccess(debug2.isClientSide);
/*     */     } 
/*     */     
/* 129 */     return InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAnalogOutputSignal(BlockState debug1) {
/* 134 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAnalogOutputSignal(BlockState debug1, Level debug2, BlockPos debug3) {
/* 139 */     BlockEntity debug4 = debug2.getBlockEntity(debug3);
/* 140 */     if (debug4 instanceof CommandBlockEntity) {
/* 141 */       return ((CommandBlockEntity)debug4).getCommandBlock().getSuccessCount();
/*     */     }
/* 143 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, LivingEntity debug4, ItemStack debug5) {
/* 148 */     BlockEntity debug6 = debug1.getBlockEntity(debug2);
/* 149 */     if (!(debug6 instanceof CommandBlockEntity)) {
/*     */       return;
/*     */     }
/*     */     
/* 153 */     CommandBlockEntity debug7 = (CommandBlockEntity)debug6;
/* 154 */     BaseCommandBlock debug8 = debug7.getCommandBlock();
/*     */     
/* 156 */     if (debug5.hasCustomHoverName()) {
/* 157 */       debug8.setName(debug5.getHoverName());
/*     */     }
/*     */     
/* 160 */     if (!debug1.isClientSide) {
/* 161 */       if (debug5.getTagElement("BlockEntityTag") == null) {
/* 162 */         debug8.setTrackOutput(debug1.getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK));
/* 163 */         debug7.setAutomatic((this == Blocks.CHAIN_COMMAND_BLOCK));
/*     */       } 
/*     */       
/* 166 */       if (debug7.getMode() == CommandBlockEntity.Mode.SEQUENCE) {
/* 167 */         boolean debug9 = debug1.hasNeighborSignal(debug2);
/* 168 */         debug7.setPowered(debug9);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/* 175 */     return RenderShape.MODEL;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 180 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 185 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 190 */     debug1.add(new Property[] { (Property)FACING, (Property)CONDITIONAL });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 195 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug1.getNearestLookingDirection().getOpposite());
/*     */   }
/*     */   
/*     */   private static void executeChain(Level debug0, BlockPos debug1, Direction debug2) {
/* 199 */     BlockPos.MutableBlockPos debug3 = debug1.mutable();
/*     */     
/* 201 */     GameRules debug4 = debug0.getGameRules();
/* 202 */     int debug5 = debug4.getInt(GameRules.RULE_MAX_COMMAND_CHAIN_LENGTH);
/* 203 */     while (debug5-- > 0) {
/* 204 */       debug3.move(debug2);
/*     */       
/* 206 */       BlockState debug6 = debug0.getBlockState((BlockPos)debug3);
/* 207 */       Block debug7 = debug6.getBlock();
/* 208 */       if (!debug6.is(Blocks.CHAIN_COMMAND_BLOCK)) {
/*     */         break;
/*     */       }
/*     */       
/* 212 */       BlockEntity debug8 = debug0.getBlockEntity((BlockPos)debug3);
/* 213 */       if (!(debug8 instanceof CommandBlockEntity)) {
/*     */         break;
/*     */       }
/*     */       
/* 217 */       CommandBlockEntity debug9 = (CommandBlockEntity)debug8;
/* 218 */       if (debug9.getMode() != CommandBlockEntity.Mode.SEQUENCE) {
/*     */         break;
/*     */       }
/*     */       
/* 222 */       if (debug9.isPowered() || debug9.isAutomatic()) {
/* 223 */         BaseCommandBlock debug10 = debug9.getCommandBlock();
/* 224 */         if (debug9.markConditionMet()) {
/* 225 */           if (debug10.performCommand(debug0)) {
/* 226 */             debug0.updateNeighbourForOutputSignal((BlockPos)debug3, debug7);
/*     */           } else {
/*     */             break;
/*     */           } 
/* 230 */         } else if (debug9.isConditional()) {
/* 231 */           debug10.setSuccessCount(0);
/*     */         } 
/*     */       } 
/*     */       
/* 235 */       debug2 = (Direction)debug6.getValue((Property)FACING);
/*     */     } 
/* 237 */     if (debug5 <= 0) {
/* 238 */       int debug6 = Math.max(debug4.getInt(GameRules.RULE_MAX_COMMAND_CHAIN_LENGTH), 0);
/* 239 */       LOGGER.warn("Command Block chain tried to execute more than {} steps!", Integer.valueOf(debug6));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\CommandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */