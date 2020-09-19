/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class BlockEntityType<T extends BlockEntity>
/*     */ {
/*  22 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   @Nullable
/*     */   public static ResourceLocation getKey(BlockEntityType<?> debug0) {
/*  26 */     return Registry.BLOCK_ENTITY_TYPE.getKey(debug0);
/*     */   }
/*     */   
/*  29 */   public static final BlockEntityType<FurnaceBlockEntity> FURNACE = register("furnace", Builder.of(FurnaceBlockEntity::new, new Block[] { Blocks.FURNACE }));
/*  30 */   public static final BlockEntityType<ChestBlockEntity> CHEST = register("chest", Builder.of(ChestBlockEntity::new, new Block[] { Blocks.CHEST }));
/*  31 */   public static final BlockEntityType<TrappedChestBlockEntity> TRAPPED_CHEST = register("trapped_chest", Builder.of(TrappedChestBlockEntity::new, new Block[] { Blocks.TRAPPED_CHEST }));
/*  32 */   public static final BlockEntityType<EnderChestBlockEntity> ENDER_CHEST = register("ender_chest", Builder.of(EnderChestBlockEntity::new, new Block[] { Blocks.ENDER_CHEST }));
/*  33 */   public static final BlockEntityType<JukeboxBlockEntity> JUKEBOX = register("jukebox", Builder.of(JukeboxBlockEntity::new, new Block[] { Blocks.JUKEBOX }));
/*  34 */   public static final BlockEntityType<DispenserBlockEntity> DISPENSER = register("dispenser", Builder.of(DispenserBlockEntity::new, new Block[] { Blocks.DISPENSER }));
/*  35 */   public static final BlockEntityType<DropperBlockEntity> DROPPER = register("dropper", Builder.of(DropperBlockEntity::new, new Block[] { Blocks.DROPPER }));
/*  36 */   public static final BlockEntityType<SignBlockEntity> SIGN = register("sign", Builder.of(SignBlockEntity::new, new Block[] { Blocks.OAK_SIGN, Blocks.SPRUCE_SIGN, Blocks.BIRCH_SIGN, Blocks.ACACIA_SIGN, Blocks.JUNGLE_SIGN, Blocks.DARK_OAK_SIGN, Blocks.OAK_WALL_SIGN, Blocks.SPRUCE_WALL_SIGN, Blocks.BIRCH_WALL_SIGN, Blocks.ACACIA_WALL_SIGN, Blocks.JUNGLE_WALL_SIGN, Blocks.DARK_OAK_WALL_SIGN, Blocks.CRIMSON_SIGN, Blocks.CRIMSON_WALL_SIGN, Blocks.WARPED_SIGN, Blocks.WARPED_WALL_SIGN }));
/*  37 */   public static final BlockEntityType<SpawnerBlockEntity> MOB_SPAWNER = register("mob_spawner", Builder.of(SpawnerBlockEntity::new, new Block[] { Blocks.SPAWNER }));
/*  38 */   public static final BlockEntityType<PistonMovingBlockEntity> PISTON = register("piston", Builder.of(PistonMovingBlockEntity::new, new Block[] { Blocks.MOVING_PISTON }));
/*  39 */   public static final BlockEntityType<BrewingStandBlockEntity> BREWING_STAND = register("brewing_stand", Builder.of(BrewingStandBlockEntity::new, new Block[] { Blocks.BREWING_STAND }));
/*  40 */   public static final BlockEntityType<EnchantmentTableBlockEntity> ENCHANTING_TABLE = register("enchanting_table", Builder.of(EnchantmentTableBlockEntity::new, new Block[] { Blocks.ENCHANTING_TABLE }));
/*  41 */   public static final BlockEntityType<TheEndPortalBlockEntity> END_PORTAL = register("end_portal", Builder.of(TheEndPortalBlockEntity::new, new Block[] { Blocks.END_PORTAL }));
/*  42 */   public static final BlockEntityType<BeaconBlockEntity> BEACON = register("beacon", Builder.of(BeaconBlockEntity::new, new Block[] { Blocks.BEACON }));
/*  43 */   public static final BlockEntityType<SkullBlockEntity> SKULL = register("skull", Builder.of(SkullBlockEntity::new, new Block[] { Blocks.SKELETON_SKULL, Blocks.SKELETON_WALL_SKULL, Blocks.CREEPER_HEAD, Blocks.CREEPER_WALL_HEAD, Blocks.DRAGON_HEAD, Blocks.DRAGON_WALL_HEAD, Blocks.ZOMBIE_HEAD, Blocks.ZOMBIE_WALL_HEAD, Blocks.WITHER_SKELETON_SKULL, Blocks.WITHER_SKELETON_WALL_SKULL, Blocks.PLAYER_HEAD, Blocks.PLAYER_WALL_HEAD }));
/*  44 */   public static final BlockEntityType<DaylightDetectorBlockEntity> DAYLIGHT_DETECTOR = register("daylight_detector", Builder.of(DaylightDetectorBlockEntity::new, new Block[] { Blocks.DAYLIGHT_DETECTOR }));
/*  45 */   public static final BlockEntityType<HopperBlockEntity> HOPPER = register("hopper", Builder.of(HopperBlockEntity::new, new Block[] { Blocks.HOPPER }));
/*  46 */   public static final BlockEntityType<ComparatorBlockEntity> COMPARATOR = register("comparator", Builder.of(ComparatorBlockEntity::new, new Block[] { Blocks.COMPARATOR }));
/*  47 */   public static final BlockEntityType<BannerBlockEntity> BANNER = register("banner", Builder.of(BannerBlockEntity::new, new Block[] { Blocks.WHITE_BANNER, Blocks.ORANGE_BANNER, Blocks.MAGENTA_BANNER, Blocks.LIGHT_BLUE_BANNER, Blocks.YELLOW_BANNER, Blocks.LIME_BANNER, Blocks.PINK_BANNER, Blocks.GRAY_BANNER, Blocks.LIGHT_GRAY_BANNER, Blocks.CYAN_BANNER, Blocks.PURPLE_BANNER, Blocks.BLUE_BANNER, Blocks.BROWN_BANNER, Blocks.GREEN_BANNER, Blocks.RED_BANNER, Blocks.BLACK_BANNER, Blocks.WHITE_WALL_BANNER, Blocks.ORANGE_WALL_BANNER, Blocks.MAGENTA_WALL_BANNER, Blocks.LIGHT_BLUE_WALL_BANNER, Blocks.YELLOW_WALL_BANNER, Blocks.LIME_WALL_BANNER, Blocks.PINK_WALL_BANNER, Blocks.GRAY_WALL_BANNER, Blocks.LIGHT_GRAY_WALL_BANNER, Blocks.CYAN_WALL_BANNER, Blocks.PURPLE_WALL_BANNER, Blocks.BLUE_WALL_BANNER, Blocks.BROWN_WALL_BANNER, Blocks.GREEN_WALL_BANNER, Blocks.RED_WALL_BANNER, Blocks.BLACK_WALL_BANNER }));
/*  48 */   public static final BlockEntityType<StructureBlockEntity> STRUCTURE_BLOCK = register("structure_block", Builder.of(StructureBlockEntity::new, new Block[] { Blocks.STRUCTURE_BLOCK }));
/*  49 */   public static final BlockEntityType<TheEndGatewayBlockEntity> END_GATEWAY = register("end_gateway", Builder.of(TheEndGatewayBlockEntity::new, new Block[] { Blocks.END_GATEWAY }));
/*  50 */   public static final BlockEntityType<CommandBlockEntity> COMMAND_BLOCK = register("command_block", Builder.of(CommandBlockEntity::new, new Block[] { Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.REPEATING_COMMAND_BLOCK }));
/*  51 */   public static final BlockEntityType<ShulkerBoxBlockEntity> SHULKER_BOX = register("shulker_box", Builder.of(ShulkerBoxBlockEntity::new, new Block[] { Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX }));
/*  52 */   public static final BlockEntityType<BedBlockEntity> BED = register("bed", Builder.of(BedBlockEntity::new, new Block[] { Blocks.RED_BED, Blocks.BLACK_BED, Blocks.BLUE_BED, Blocks.BROWN_BED, Blocks.CYAN_BED, Blocks.GRAY_BED, Blocks.GREEN_BED, Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_GRAY_BED, Blocks.LIME_BED, Blocks.MAGENTA_BED, Blocks.ORANGE_BED, Blocks.PINK_BED, Blocks.PURPLE_BED, Blocks.WHITE_BED, Blocks.YELLOW_BED }));
/*  53 */   public static final BlockEntityType<ConduitBlockEntity> CONDUIT = register("conduit", Builder.of(ConduitBlockEntity::new, new Block[] { Blocks.CONDUIT }));
/*  54 */   public static final BlockEntityType<BarrelBlockEntity> BARREL = register("barrel", Builder.of(BarrelBlockEntity::new, new Block[] { Blocks.BARREL }));
/*  55 */   public static final BlockEntityType<SmokerBlockEntity> SMOKER = register("smoker", Builder.of(SmokerBlockEntity::new, new Block[] { Blocks.SMOKER }));
/*  56 */   public static final BlockEntityType<BlastFurnaceBlockEntity> BLAST_FURNACE = register("blast_furnace", Builder.of(BlastFurnaceBlockEntity::new, new Block[] { Blocks.BLAST_FURNACE }));
/*  57 */   public static final BlockEntityType<LecternBlockEntity> LECTERN = register("lectern", Builder.of(LecternBlockEntity::new, new Block[] { Blocks.LECTERN }));
/*  58 */   public static final BlockEntityType<BellBlockEntity> BELL = register("bell", Builder.of(BellBlockEntity::new, new Block[] { Blocks.BELL }));
/*  59 */   public static final BlockEntityType<JigsawBlockEntity> JIGSAW = register("jigsaw", Builder.of(JigsawBlockEntity::new, new Block[] { Blocks.JIGSAW }));
/*  60 */   public static final BlockEntityType<CampfireBlockEntity> CAMPFIRE = register("campfire", Builder.of(CampfireBlockEntity::new, new Block[] { Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE }));
/*  61 */   public static final BlockEntityType<BeehiveBlockEntity> BEEHIVE = register("beehive", Builder.of(BeehiveBlockEntity::new, new Block[] { Blocks.BEE_NEST, Blocks.BEEHIVE })); private final Supplier<? extends T> factory;
/*     */   
/*     */   private static <T extends BlockEntity> BlockEntityType<T> register(String debug0, Builder<T> debug1) {
/*  64 */     if (debug1.validBlocks.isEmpty()) {
/*  65 */       LOGGER.warn("Block entity type {} requires at least one valid block to be defined!", debug0);
/*     */     }
/*  67 */     Type<?> debug2 = Util.fetchChoiceType(References.BLOCK_ENTITY, debug0);
/*  68 */     return (BlockEntityType<T>)Registry.register(Registry.BLOCK_ENTITY_TYPE, debug0, debug1.build(debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   private final Set<Block> validBlocks;
/*     */   private final Type<?> dataType;
/*     */   
/*     */   public BlockEntityType(Supplier<? extends T> debug1, Set<Block> debug2, Type<?> debug3) {
/*  76 */     this.factory = debug1;
/*  77 */     this.validBlocks = debug2;
/*  78 */     this.dataType = debug3;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public T create() {
/*  83 */     return this.factory.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValid(Block debug1) {
/*  90 */     return this.validBlocks.contains(debug1);
/*     */   }
/*     */   
/*     */   public static final class Builder<T extends BlockEntity> {
/*     */     private final Supplier<? extends T> factory;
/*     */     private final Set<Block> validBlocks;
/*     */     
/*     */     private Builder(Supplier<? extends T> debug1, Set<Block> debug2) {
/*  98 */       this.factory = debug1;
/*  99 */       this.validBlocks = debug2;
/*     */     }
/*     */     
/*     */     public static <T extends BlockEntity> Builder<T> of(Supplier<? extends T> debug0, Block... debug1) {
/* 103 */       return new Builder<>(debug0, (Set<Block>)ImmutableSet.copyOf((Object[])debug1));
/*     */     }
/*     */     
/*     */     public BlockEntityType<T> build(Type<?> debug1) {
/* 107 */       return new BlockEntityType<>(this.factory, this.validBlocks, debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T getBlockEntity(BlockGetter debug1, BlockPos debug2) {
/* 114 */     BlockEntity debug3 = debug1.getBlockEntity(debug2);
/* 115 */     if (debug3 == null || debug3.getType() != this) {
/* 116 */       return null;
/*     */     }
/* 118 */     return (T)debug3;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\BlockEntityType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */