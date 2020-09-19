/*     */ package net.minecraft.world.level.block;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.SimpleMenuProvider;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerLevelAccess;
/*     */ import net.minecraft.world.inventory.GrindstoneMenu;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class GrindstoneBlock extends FaceAttachedHorizontalDirectionalBlock {
/*  29 */   public static final VoxelShape FLOOR_NORTH_SOUTH_LEFT_POST = Block.box(2.0D, 0.0D, 6.0D, 4.0D, 7.0D, 10.0D);
/*  30 */   public static final VoxelShape FLOOR_NORTH_SOUTH_RIGHT_POST = Block.box(12.0D, 0.0D, 6.0D, 14.0D, 7.0D, 10.0D);
/*  31 */   public static final VoxelShape FLOOR_NORTH_SOUTH_LEFT_PIVOT = Block.box(2.0D, 7.0D, 5.0D, 4.0D, 13.0D, 11.0D);
/*  32 */   public static final VoxelShape FLOOR_NORTH_SOUTH_RIGHT_PIVOT = Block.box(12.0D, 7.0D, 5.0D, 14.0D, 13.0D, 11.0D);
/*  33 */   public static final VoxelShape FLOOR_NORTH_SOUTH_LEFT_LEG = Shapes.or(FLOOR_NORTH_SOUTH_LEFT_POST, FLOOR_NORTH_SOUTH_LEFT_PIVOT);
/*  34 */   public static final VoxelShape FLOOR_NORTH_SOUTH_RIGHT_LEG = Shapes.or(FLOOR_NORTH_SOUTH_RIGHT_POST, FLOOR_NORTH_SOUTH_RIGHT_PIVOT);
/*  35 */   public static final VoxelShape FLOOR_NORTH_SOUTH_ALL_LEGS = Shapes.or(FLOOR_NORTH_SOUTH_LEFT_LEG, FLOOR_NORTH_SOUTH_RIGHT_LEG);
/*  36 */   public static final VoxelShape FLOOR_NORTH_SOUTH_GRINDSTONE = Shapes.or(FLOOR_NORTH_SOUTH_ALL_LEGS, Block.box(4.0D, 4.0D, 2.0D, 12.0D, 16.0D, 14.0D));
/*     */   
/*  38 */   public static final VoxelShape FLOOR_EAST_WEST_LEFT_POST = Block.box(6.0D, 0.0D, 2.0D, 10.0D, 7.0D, 4.0D);
/*  39 */   public static final VoxelShape FLOOR_EAST_WEST_RIGHT_POST = Block.box(6.0D, 0.0D, 12.0D, 10.0D, 7.0D, 14.0D);
/*  40 */   public static final VoxelShape FLOOR_EAST_WEST_LEFT_PIVOT = Block.box(5.0D, 7.0D, 2.0D, 11.0D, 13.0D, 4.0D);
/*  41 */   public static final VoxelShape FLOOR_EAST_WEST_RIGHT_PIVOT = Block.box(5.0D, 7.0D, 12.0D, 11.0D, 13.0D, 14.0D);
/*  42 */   public static final VoxelShape FLOOR_EAST_WEST_LEFT_LEG = Shapes.or(FLOOR_EAST_WEST_LEFT_POST, FLOOR_EAST_WEST_LEFT_PIVOT);
/*  43 */   public static final VoxelShape FLOOR_EAST_WEST_RIGHT_LEG = Shapes.or(FLOOR_EAST_WEST_RIGHT_POST, FLOOR_EAST_WEST_RIGHT_PIVOT);
/*  44 */   public static final VoxelShape FLOOR_EAST_WEST_ALL_LEGS = Shapes.or(FLOOR_EAST_WEST_LEFT_LEG, FLOOR_EAST_WEST_RIGHT_LEG);
/*  45 */   public static final VoxelShape FLOOR_EAST_WEST_GRINDSTONE = Shapes.or(FLOOR_EAST_WEST_ALL_LEGS, Block.box(2.0D, 4.0D, 4.0D, 14.0D, 16.0D, 12.0D));
/*     */   
/*  47 */   public static final VoxelShape WALL_SOUTH_LEFT_POST = Block.box(2.0D, 6.0D, 0.0D, 4.0D, 10.0D, 7.0D);
/*  48 */   public static final VoxelShape WALL_SOUTH_RIGHT_POST = Block.box(12.0D, 6.0D, 0.0D, 14.0D, 10.0D, 7.0D);
/*  49 */   public static final VoxelShape WALL_SOUTH_LEFT_PIVOT = Block.box(2.0D, 5.0D, 7.0D, 4.0D, 11.0D, 13.0D);
/*  50 */   public static final VoxelShape WALL_SOUTH_RIGHT_PIVOT = Block.box(12.0D, 5.0D, 7.0D, 14.0D, 11.0D, 13.0D);
/*  51 */   public static final VoxelShape WALL_SOUTH_LEFT_LEG = Shapes.or(WALL_SOUTH_LEFT_POST, WALL_SOUTH_LEFT_PIVOT);
/*  52 */   public static final VoxelShape WALL_SOUTH_RIGHT_LEG = Shapes.or(WALL_SOUTH_RIGHT_POST, WALL_SOUTH_RIGHT_PIVOT);
/*  53 */   public static final VoxelShape WALL_SOUTH_ALL_LEGS = Shapes.or(WALL_SOUTH_LEFT_LEG, WALL_SOUTH_RIGHT_LEG);
/*  54 */   public static final VoxelShape WALL_SOUTH_GRINDSTONE = Shapes.or(WALL_SOUTH_ALL_LEGS, Block.box(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 16.0D));
/*     */   
/*  56 */   public static final VoxelShape WALL_NORTH_LEFT_POST = Block.box(2.0D, 6.0D, 7.0D, 4.0D, 10.0D, 16.0D);
/*  57 */   public static final VoxelShape WALL_NORTH_RIGHT_POST = Block.box(12.0D, 6.0D, 7.0D, 14.0D, 10.0D, 16.0D);
/*  58 */   public static final VoxelShape WALL_NORTH_LEFT_PIVOT = Block.box(2.0D, 5.0D, 3.0D, 4.0D, 11.0D, 9.0D);
/*  59 */   public static final VoxelShape WALL_NORTH_RIGHT_PIVOT = Block.box(12.0D, 5.0D, 3.0D, 14.0D, 11.0D, 9.0D);
/*  60 */   public static final VoxelShape WALL_NORTH_LEFT_LEG = Shapes.or(WALL_NORTH_LEFT_POST, WALL_NORTH_LEFT_PIVOT);
/*  61 */   public static final VoxelShape WALL_NORTH_RIGHT_LEG = Shapes.or(WALL_NORTH_RIGHT_POST, WALL_NORTH_RIGHT_PIVOT);
/*  62 */   public static final VoxelShape WALL_NORTH_ALL_LEGS = Shapes.or(WALL_NORTH_LEFT_LEG, WALL_NORTH_RIGHT_LEG);
/*  63 */   public static final VoxelShape WALL_NORTH_GRINDSTONE = Shapes.or(WALL_NORTH_ALL_LEGS, Block.box(4.0D, 2.0D, 0.0D, 12.0D, 14.0D, 12.0D));
/*     */   
/*  65 */   public static final VoxelShape WALL_WEST_LEFT_POST = Block.box(7.0D, 6.0D, 2.0D, 16.0D, 10.0D, 4.0D);
/*  66 */   public static final VoxelShape WALL_WEST_RIGHT_POST = Block.box(7.0D, 6.0D, 12.0D, 16.0D, 10.0D, 14.0D);
/*  67 */   public static final VoxelShape WALL_WEST_LEFT_PIVOT = Block.box(3.0D, 5.0D, 2.0D, 9.0D, 11.0D, 4.0D);
/*  68 */   public static final VoxelShape WALL_WEST_RIGHT_PIVOT = Block.box(3.0D, 5.0D, 12.0D, 9.0D, 11.0D, 14.0D);
/*  69 */   public static final VoxelShape WALL_WEST_LEFT_LEG = Shapes.or(WALL_WEST_LEFT_POST, WALL_WEST_LEFT_PIVOT);
/*  70 */   public static final VoxelShape WALL_WEST_RIGHT_LEG = Shapes.or(WALL_WEST_RIGHT_POST, WALL_WEST_RIGHT_PIVOT);
/*  71 */   public static final VoxelShape WALL_WEST_ALL_LEGS = Shapes.or(WALL_WEST_LEFT_LEG, WALL_WEST_RIGHT_LEG);
/*  72 */   public static final VoxelShape WALL_WEST_GRINDSTONE = Shapes.or(WALL_WEST_ALL_LEGS, Block.box(0.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D));
/*     */   
/*  74 */   public static final VoxelShape WALL_EAST_LEFT_POST = Block.box(0.0D, 6.0D, 2.0D, 9.0D, 10.0D, 4.0D);
/*  75 */   public static final VoxelShape WALL_EAST_RIGHT_POST = Block.box(0.0D, 6.0D, 12.0D, 9.0D, 10.0D, 14.0D);
/*  76 */   public static final VoxelShape WALL_EAST_LEFT_PIVOT = Block.box(7.0D, 5.0D, 2.0D, 13.0D, 11.0D, 4.0D);
/*  77 */   public static final VoxelShape WALL_EAST_RIGHT_PIVOT = Block.box(7.0D, 5.0D, 12.0D, 13.0D, 11.0D, 14.0D);
/*  78 */   public static final VoxelShape WALL_EAST_LEFT_LEG = Shapes.or(WALL_EAST_LEFT_POST, WALL_EAST_LEFT_PIVOT);
/*  79 */   public static final VoxelShape WALL_EAST_RIGHT_LEG = Shapes.or(WALL_EAST_RIGHT_POST, WALL_EAST_RIGHT_PIVOT);
/*  80 */   public static final VoxelShape WALL_EAST_ALL_LEGS = Shapes.or(WALL_EAST_LEFT_LEG, WALL_EAST_RIGHT_LEG);
/*  81 */   public static final VoxelShape WALL_EAST_GRINDSTONE = Shapes.or(WALL_EAST_ALL_LEGS, Block.box(4.0D, 2.0D, 4.0D, 16.0D, 14.0D, 12.0D));
/*     */   
/*  83 */   public static final VoxelShape CEILING_NORTH_SOUTH_LEFT_POST = Block.box(2.0D, 9.0D, 6.0D, 4.0D, 16.0D, 10.0D);
/*  84 */   public static final VoxelShape CEILING_NORTH_SOUTH_RIGHT_POST = Block.box(12.0D, 9.0D, 6.0D, 14.0D, 16.0D, 10.0D);
/*  85 */   public static final VoxelShape CEILING_NORTH_SOUTH_LEFT_PIVOT = Block.box(2.0D, 3.0D, 5.0D, 4.0D, 9.0D, 11.0D);
/*  86 */   public static final VoxelShape CEILING_NORTH_SOUTH_RIGHT_PIVOT = Block.box(12.0D, 3.0D, 5.0D, 14.0D, 9.0D, 11.0D);
/*  87 */   public static final VoxelShape CEILING_NORTH_SOUTH_LEFT_LEG = Shapes.or(CEILING_NORTH_SOUTH_LEFT_POST, CEILING_NORTH_SOUTH_LEFT_PIVOT);
/*  88 */   public static final VoxelShape CEILING_NORTH_SOUTH_RIGHT_LEG = Shapes.or(CEILING_NORTH_SOUTH_RIGHT_POST, CEILING_NORTH_SOUTH_RIGHT_PIVOT);
/*  89 */   public static final VoxelShape CEILING_NORTH_SOUTH_ALL_LEGS = Shapes.or(CEILING_NORTH_SOUTH_LEFT_LEG, CEILING_NORTH_SOUTH_RIGHT_LEG);
/*  90 */   public static final VoxelShape CEILING_NORTH_SOUTH_GRINDSTONE = Shapes.or(CEILING_NORTH_SOUTH_ALL_LEGS, Block.box(4.0D, 0.0D, 2.0D, 12.0D, 12.0D, 14.0D));
/*     */   
/*  92 */   public static final VoxelShape CEILING_EAST_WEST_LEFT_POST = Block.box(6.0D, 9.0D, 2.0D, 10.0D, 16.0D, 4.0D);
/*  93 */   public static final VoxelShape CEILING_EAST_WEST_RIGHT_POST = Block.box(6.0D, 9.0D, 12.0D, 10.0D, 16.0D, 14.0D);
/*  94 */   public static final VoxelShape CEILING_EAST_WEST_LEFT_PIVOT = Block.box(5.0D, 3.0D, 2.0D, 11.0D, 9.0D, 4.0D);
/*  95 */   public static final VoxelShape CEILING_EAST_WEST_RIGHT_PIVOT = Block.box(5.0D, 3.0D, 12.0D, 11.0D, 9.0D, 14.0D);
/*  96 */   public static final VoxelShape CEILING_EAST_WEST_LEFT_LEG = Shapes.or(CEILING_EAST_WEST_LEFT_POST, CEILING_EAST_WEST_LEFT_PIVOT);
/*  97 */   public static final VoxelShape CEILING_EAST_WEST_RIGHT_LEG = Shapes.or(CEILING_EAST_WEST_RIGHT_POST, CEILING_EAST_WEST_RIGHT_PIVOT);
/*  98 */   public static final VoxelShape CEILING_EAST_WEST_ALL_LEGS = Shapes.or(CEILING_EAST_WEST_LEFT_LEG, CEILING_EAST_WEST_RIGHT_LEG);
/*  99 */   public static final VoxelShape CEILING_EAST_WEST_GRINDSTONE = Shapes.or(CEILING_EAST_WEST_ALL_LEGS, Block.box(2.0D, 0.0D, 4.0D, 14.0D, 12.0D, 12.0D));
/*     */   
/* 101 */   private static final Component CONTAINER_TITLE = (Component)new TranslatableComponent("container.grindstone_title");
/*     */   
/*     */   protected GrindstoneBlock(BlockBehaviour.Properties debug1) {
/* 104 */     super(debug1);
/* 105 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)FACE, (Comparable)AttachFace.WALL));
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderShape getRenderShape(BlockState debug1) {
/* 110 */     return RenderShape.MODEL;
/*     */   }
/*     */   
/*     */   private VoxelShape getVoxelShape(BlockState debug1) {
/* 114 */     Direction debug2 = (Direction)debug1.getValue((Property)FACING);
/* 115 */     switch ((AttachFace)debug1.getValue((Property)FACE)) {
/*     */       case FLOOR:
/* 117 */         if (debug2 == Direction.NORTH || debug2 == Direction.SOUTH) {
/* 118 */           return FLOOR_NORTH_SOUTH_GRINDSTONE;
/*     */         }
/* 120 */         return FLOOR_EAST_WEST_GRINDSTONE;
/*     */ 
/*     */       
/*     */       case WALL:
/* 124 */         if (debug2 == Direction.NORTH)
/* 125 */           return WALL_NORTH_GRINDSTONE; 
/* 126 */         if (debug2 == Direction.SOUTH)
/* 127 */           return WALL_SOUTH_GRINDSTONE; 
/* 128 */         if (debug2 == Direction.EAST) {
/* 129 */           return WALL_EAST_GRINDSTONE;
/*     */         }
/* 131 */         return WALL_WEST_GRINDSTONE;
/*     */ 
/*     */       
/*     */       case CEILING:
/* 135 */         if (debug2 == Direction.NORTH || debug2 == Direction.SOUTH) {
/* 136 */           return CEILING_NORTH_SOUTH_GRINDSTONE;
/*     */         }
/* 138 */         return CEILING_EAST_WEST_GRINDSTONE;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 143 */     return FLOOR_EAST_WEST_GRINDSTONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 148 */     return getVoxelShape(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 153 */     return getVoxelShape(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 158 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult use(BlockState debug1, Level debug2, BlockPos debug3, Player debug4, InteractionHand debug5, BlockHitResult debug6) {
/* 163 */     if (debug2.isClientSide) {
/* 164 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/* 167 */     debug4.openMenu(debug1.getMenuProvider(debug2, debug3));
/* 168 */     debug4.awardStat(Stats.INTERACT_WITH_GRINDSTONE);
/* 169 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   public MenuProvider getMenuProvider(BlockState debug1, Level debug2, BlockPos debug3) {
/* 174 */     return (MenuProvider)new SimpleMenuProvider((debug2, debug3, debug4) -> new GrindstoneMenu(debug2, debug3, ContainerLevelAccess.create(debug0, debug1)), CONTAINER_TITLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 179 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 184 */     return debug1.rotate(debug2.getRotation((Direction)debug1.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 189 */     debug1.add(new Property[] { (Property)FACING, (Property)FACE });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 194 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\GrindstoneBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */