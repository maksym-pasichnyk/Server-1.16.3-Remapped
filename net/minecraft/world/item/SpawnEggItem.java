/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.InteractionResultHolder;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.BaseSpawner;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SpawnEggItem
/*     */   extends Item
/*     */ {
/*  39 */   private static final Map<EntityType<?>, SpawnEggItem> BY_ID = Maps.newIdentityHashMap();
/*     */   
/*     */   private final int color1;
/*     */   private final int color2;
/*     */   private final EntityType<?> defaultType;
/*     */   
/*     */   public SpawnEggItem(EntityType<?> debug1, int debug2, int debug3, Item.Properties debug4) {
/*  46 */     super(debug4);
/*  47 */     this.defaultType = debug1;
/*  48 */     this.color1 = debug2;
/*  49 */     this.color2 = debug3;
/*     */     
/*  51 */     BY_ID.put(debug1, this);
/*     */   }
/*     */   
/*     */   public InteractionResult useOn(UseOnContext debug1) {
/*     */     BlockPos debug7;
/*  56 */     Level debug2 = debug1.getLevel();
/*  57 */     if (!(debug2 instanceof ServerLevel)) {
/*  58 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/*  61 */     ItemStack debug3 = debug1.getItemInHand();
/*  62 */     BlockPos debug4 = debug1.getClickedPos();
/*  63 */     Direction debug5 = debug1.getClickedFace();
/*     */     
/*  65 */     BlockState debug6 = debug2.getBlockState(debug4);
/*  66 */     if (debug6.is(Blocks.SPAWNER)) {
/*  67 */       BlockEntity blockEntity = debug2.getBlockEntity(debug4);
/*  68 */       if (blockEntity instanceof SpawnerBlockEntity) {
/*  69 */         BaseSpawner baseSpawner = ((SpawnerBlockEntity)blockEntity).getSpawner();
/*  70 */         EntityType<?> debug9 = getType(debug3.getTag());
/*  71 */         baseSpawner.setEntityId(debug9);
/*  72 */         blockEntity.setChanged();
/*  73 */         debug2.sendBlockUpdated(debug4, debug6, debug6, 3);
/*  74 */         debug3.shrink(1);
/*  75 */         return InteractionResult.CONSUME;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  80 */     if (debug6.getCollisionShape((BlockGetter)debug2, debug4).isEmpty()) {
/*  81 */       debug7 = debug4;
/*     */     } else {
/*  83 */       debug7 = debug4.relative(debug5);
/*     */     } 
/*     */     
/*  86 */     EntityType<?> debug8 = getType(debug3.getTag());
/*  87 */     if (debug8.spawn((ServerLevel)debug2, debug3, debug1.getPlayer(), debug7, MobSpawnType.SPAWN_EGG, true, (!Objects.equals(debug4, debug7) && debug5 == Direction.UP)) != null) {
/*  88 */       debug3.shrink(1);
/*     */     }
/*     */     
/*  91 */     return InteractionResult.CONSUME;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/*  96 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/*     */     
/*  98 */     BlockHitResult blockHitResult1 = getPlayerPOVHitResult(debug1, debug2, ClipContext.Fluid.SOURCE_ONLY);
/*  99 */     if (blockHitResult1.getType() != HitResult.Type.BLOCK) {
/* 100 */       return InteractionResultHolder.pass(debug4);
/*     */     }
/*     */     
/* 103 */     if (!(debug1 instanceof ServerLevel)) {
/* 104 */       return InteractionResultHolder.success(debug4);
/*     */     }
/*     */     
/* 107 */     BlockHitResult debug6 = blockHitResult1;
/* 108 */     BlockPos debug7 = debug6.getBlockPos();
/* 109 */     if (!(debug1.getBlockState(debug7).getBlock() instanceof net.minecraft.world.level.block.LiquidBlock)) {
/* 110 */       return InteractionResultHolder.pass(debug4);
/*     */     }
/* 112 */     if (!debug1.mayInteract(debug2, debug7) || !debug2.mayUseItemAt(debug7, debug6.getDirection(), debug4)) {
/* 113 */       return InteractionResultHolder.fail(debug4);
/*     */     }
/* 115 */     EntityType<?> debug8 = getType(debug4.getTag());
/* 116 */     if (debug8.spawn((ServerLevel)debug1, debug4, debug2, debug7, MobSpawnType.SPAWN_EGG, false, false) == null) {
/* 117 */       return InteractionResultHolder.pass(debug4);
/*     */     }
/* 119 */     if (!debug2.abilities.instabuild) {
/* 120 */       debug4.shrink(1);
/*     */     }
/* 122 */     debug2.awardStat(Stats.ITEM_USED.get(this));
/* 123 */     return InteractionResultHolder.consume(debug4);
/*     */   }
/*     */   
/*     */   public boolean spawnsEntity(@Nullable CompoundTag debug1, EntityType<?> debug2) {
/* 127 */     return Objects.equals(getType(debug1), debug2);
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
/*     */   public static Iterable<SpawnEggItem> eggs() {
/* 140 */     return Iterables.unmodifiableIterable(BY_ID.values());
/*     */   }
/*     */   
/*     */   public EntityType<?> getType(@Nullable CompoundTag debug1) {
/* 144 */     if (debug1 != null && 
/* 145 */       debug1.contains("EntityTag", 10)) {
/* 146 */       CompoundTag debug2 = debug1.getCompound("EntityTag");
/* 147 */       if (debug2.contains("id", 8)) {
/* 148 */         return EntityType.byString(debug2.getString("id")).orElse(this.defaultType);
/*     */       }
/*     */     } 
/*     */     
/* 152 */     return this.defaultType;
/*     */   }
/*     */   public Optional<Mob> spawnOffspringFromSpawnEgg(Player debug1, Mob debug2, EntityType<? extends Mob> debug3, ServerLevel debug4, Vec3 debug5, ItemStack debug6) {
/*     */     Mob debug7;
/* 156 */     if (!spawnsEntity(debug6.getTag(), debug3)) {
/* 157 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 161 */     if (debug2 instanceof AgableMob) {
/* 162 */       AgableMob agableMob = ((AgableMob)debug2).getBreedOffspring(debug4, (AgableMob)debug2);
/*     */     } else {
/* 164 */       debug7 = (Mob)debug3.create((Level)debug4);
/*     */     } 
/* 166 */     if (debug7 == null) {
/* 167 */       return Optional.empty();
/*     */     }
/*     */     
/* 170 */     debug7.setBaby(true);
/* 171 */     if (!debug7.isBaby()) {
/* 172 */       return Optional.empty();
/*     */     }
/*     */     
/* 175 */     debug7.moveTo(debug5.x(), debug5.y(), debug5.z(), 0.0F, 0.0F);
/*     */     
/* 177 */     debug4.addFreshEntityWithPassengers((Entity)debug7);
/*     */     
/* 179 */     if (debug6.hasCustomHoverName()) {
/* 180 */       debug7.setCustomName(debug6.getHoverName());
/*     */     }
/* 182 */     if (!debug1.abilities.instabuild) {
/* 183 */       debug6.shrink(1);
/*     */     }
/* 185 */     return Optional.of(debug7);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\SpawnEggItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */