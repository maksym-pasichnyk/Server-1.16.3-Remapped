/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import net.minecraft.world.entity.ambient.Bat;
/*     */ import net.minecraft.world.entity.animal.Bee;
/*     */ import net.minecraft.world.entity.animal.Cat;
/*     */ import net.minecraft.world.entity.animal.Chicken;
/*     */ import net.minecraft.world.entity.animal.Cod;
/*     */ import net.minecraft.world.entity.animal.Cow;
/*     */ import net.minecraft.world.entity.animal.Dolphin;
/*     */ import net.minecraft.world.entity.animal.Fox;
/*     */ import net.minecraft.world.entity.animal.IronGolem;
/*     */ import net.minecraft.world.entity.animal.MushroomCow;
/*     */ import net.minecraft.world.entity.animal.Ocelot;
/*     */ import net.minecraft.world.entity.animal.Panda;
/*     */ import net.minecraft.world.entity.animal.Parrot;
/*     */ import net.minecraft.world.entity.animal.Pig;
/*     */ import net.minecraft.world.entity.animal.PolarBear;
/*     */ import net.minecraft.world.entity.animal.Pufferfish;
/*     */ import net.minecraft.world.entity.animal.Rabbit;
/*     */ import net.minecraft.world.entity.animal.Salmon;
/*     */ import net.minecraft.world.entity.animal.Sheep;
/*     */ import net.minecraft.world.entity.animal.SnowGolem;
/*     */ import net.minecraft.world.entity.animal.Squid;
/*     */ import net.minecraft.world.entity.animal.TropicalFish;
/*     */ import net.minecraft.world.entity.animal.Turtle;
/*     */ import net.minecraft.world.entity.animal.Wolf;
/*     */ import net.minecraft.world.entity.animal.horse.Donkey;
/*     */ import net.minecraft.world.entity.animal.horse.Horse;
/*     */ import net.minecraft.world.entity.animal.horse.Llama;
/*     */ import net.minecraft.world.entity.animal.horse.Mule;
/*     */ import net.minecraft.world.entity.animal.horse.SkeletonHorse;
/*     */ import net.minecraft.world.entity.animal.horse.TraderLlama;
/*     */ import net.minecraft.world.entity.animal.horse.ZombieHorse;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.boss.wither.WitherBoss;
/*     */ import net.minecraft.world.entity.decoration.ArmorStand;
/*     */ import net.minecraft.world.entity.decoration.ItemFrame;
/*     */ import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
/*     */ import net.minecraft.world.entity.decoration.Painting;
/*     */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.item.PrimedTnt;
/*     */ import net.minecraft.world.entity.monster.Blaze;
/*     */ import net.minecraft.world.entity.monster.CaveSpider;
/*     */ import net.minecraft.world.entity.monster.Creeper;
/*     */ import net.minecraft.world.entity.monster.Drowned;
/*     */ import net.minecraft.world.entity.monster.ElderGuardian;
/*     */ import net.minecraft.world.entity.monster.EnderMan;
/*     */ import net.minecraft.world.entity.monster.Endermite;
/*     */ import net.minecraft.world.entity.monster.Evoker;
/*     */ import net.minecraft.world.entity.monster.Ghast;
/*     */ import net.minecraft.world.entity.monster.Giant;
/*     */ import net.minecraft.world.entity.monster.Guardian;
/*     */ import net.minecraft.world.entity.monster.Husk;
/*     */ import net.minecraft.world.entity.monster.Illusioner;
/*     */ import net.minecraft.world.entity.monster.MagmaCube;
/*     */ import net.minecraft.world.entity.monster.Phantom;
/*     */ import net.minecraft.world.entity.monster.Pillager;
/*     */ import net.minecraft.world.entity.monster.Ravager;
/*     */ import net.minecraft.world.entity.monster.Shulker;
/*     */ import net.minecraft.world.entity.monster.Silverfish;
/*     */ import net.minecraft.world.entity.monster.Skeleton;
/*     */ import net.minecraft.world.entity.monster.Slime;
/*     */ import net.minecraft.world.entity.monster.Spider;
/*     */ import net.minecraft.world.entity.monster.Stray;
/*     */ import net.minecraft.world.entity.monster.Strider;
/*     */ import net.minecraft.world.entity.monster.Vex;
/*     */ import net.minecraft.world.entity.monster.Vindicator;
/*     */ import net.minecraft.world.entity.monster.Witch;
/*     */ import net.minecraft.world.entity.monster.WitherSkeleton;
/*     */ import net.minecraft.world.entity.monster.Zoglin;
/*     */ import net.minecraft.world.entity.monster.Zombie;
/*     */ import net.minecraft.world.entity.monster.ZombieVillager;
/*     */ import net.minecraft.world.entity.monster.ZombifiedPiglin;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.monster.piglin.Piglin;
/*     */ import net.minecraft.world.entity.monster.piglin.PiglinBrute;
/*     */ import net.minecraft.world.entity.npc.Villager;
/*     */ import net.minecraft.world.entity.npc.WanderingTrader;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Arrow;
/*     */ import net.minecraft.world.entity.projectile.DragonFireball;
/*     */ import net.minecraft.world.entity.projectile.EvokerFangs;
/*     */ import net.minecraft.world.entity.projectile.EyeOfEnder;
/*     */ import net.minecraft.world.entity.projectile.FireworkRocketEntity;
/*     */ import net.minecraft.world.entity.projectile.FishingHook;
/*     */ import net.minecraft.world.entity.projectile.LargeFireball;
/*     */ import net.minecraft.world.entity.projectile.LlamaSpit;
/*     */ import net.minecraft.world.entity.projectile.ShulkerBullet;
/*     */ import net.minecraft.world.entity.projectile.SmallFireball;
/*     */ import net.minecraft.world.entity.projectile.Snowball;
/*     */ import net.minecraft.world.entity.projectile.SpectralArrow;
/*     */ import net.minecraft.world.entity.projectile.ThrownEgg;
/*     */ import net.minecraft.world.entity.projectile.ThrownEnderpearl;
/*     */ import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
/*     */ import net.minecraft.world.entity.projectile.ThrownPotion;
/*     */ import net.minecraft.world.entity.projectile.ThrownTrident;
/*     */ import net.minecraft.world.entity.projectile.WitherSkull;
/*     */ import net.minecraft.world.entity.vehicle.Boat;
/*     */ import net.minecraft.world.entity.vehicle.Minecart;
/*     */ import net.minecraft.world.entity.vehicle.MinecartChest;
/*     */ import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
/*     */ import net.minecraft.world.entity.vehicle.MinecartFurnace;
/*     */ import net.minecraft.world.entity.vehicle.MinecartHopper;
/*     */ import net.minecraft.world.entity.vehicle.MinecartSpawner;
/*     */ import net.minecraft.world.entity.vehicle.MinecartTNT;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class EntityType<T extends Entity> {
/* 144 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   private static <T extends Entity> EntityType<T> register(String debug0, Builder<T> debug1) {
/* 148 */     return (EntityType<T>)Registry.register((Registry)Registry.ENTITY_TYPE, debug0, debug1.build(debug0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   public static final EntityType<AreaEffectCloud> AREA_EFFECT_CLOUD = register("area_effect_cloud", Builder.<AreaEffectCloud>of(AreaEffectCloud::new, MobCategory.MISC).fireImmune().sized(6.0F, 0.5F).clientTrackingRange(10).updateInterval(2147483647));
/* 155 */   public static final EntityType<ArmorStand> ARMOR_STAND = register("armor_stand", Builder.<ArmorStand>of(ArmorStand::new, MobCategory.MISC).sized(0.5F, 1.975F).clientTrackingRange(10));
/* 156 */   public static final EntityType<Arrow> ARROW = register("arrow", Builder.<Arrow>of(Arrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
/* 157 */   public static final EntityType<Bat> BAT = register("bat", Builder.<Bat>of(Bat::new, MobCategory.AMBIENT).sized(0.5F, 0.9F).clientTrackingRange(5));
/* 158 */   public static final EntityType<Bee> BEE = register("bee", Builder.<Bee>of(Bee::new, MobCategory.CREATURE).sized(0.7F, 0.6F).clientTrackingRange(8));
/* 159 */   public static final EntityType<Blaze> BLAZE = register("blaze", Builder.<Blaze>of(Blaze::new, MobCategory.MONSTER).fireImmune().sized(0.6F, 1.8F).clientTrackingRange(8));
/* 160 */   public static final EntityType<Boat> BOAT = register("boat", Builder.<Boat>of(Boat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10));
/* 161 */   public static final EntityType<Cat> CAT = register("cat", Builder.<Cat>of(Cat::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(8));
/* 162 */   public static final EntityType<CaveSpider> CAVE_SPIDER = register("cave_spider", Builder.<CaveSpider>of(CaveSpider::new, MobCategory.MONSTER).sized(0.7F, 0.5F).clientTrackingRange(8));
/* 163 */   public static final EntityType<Chicken> CHICKEN = register("chicken", Builder.<Chicken>of(Chicken::new, MobCategory.CREATURE).sized(0.4F, 0.7F).clientTrackingRange(10));
/* 164 */   public static final EntityType<Cod> COD = register("cod", Builder.<Cod>of(Cod::new, MobCategory.WATER_AMBIENT).sized(0.5F, 0.3F).clientTrackingRange(4));
/* 165 */   public static final EntityType<Cow> COW = register("cow", Builder.<Cow>of(Cow::new, MobCategory.CREATURE).sized(0.9F, 1.4F).clientTrackingRange(10));
/* 166 */   public static final EntityType<Creeper> CREEPER = register("creeper", Builder.<Creeper>of(Creeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).clientTrackingRange(8));
/* 167 */   public static final EntityType<Dolphin> DOLPHIN = register("dolphin", Builder.<Dolphin>of(Dolphin::new, MobCategory.WATER_CREATURE).sized(0.9F, 0.6F));
/* 168 */   public static final EntityType<Donkey> DONKEY = register("donkey", Builder.<Donkey>of(Donkey::new, MobCategory.CREATURE).sized(1.3964844F, 1.5F).clientTrackingRange(10));
/* 169 */   public static final EntityType<DragonFireball> DRAGON_FIREBALL = register("dragon_fireball", Builder.<DragonFireball>of(DragonFireball::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10));
/* 170 */   public static final EntityType<Drowned> DROWNED = register("drowned", Builder.<Drowned>of(Drowned::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
/* 171 */   public static final EntityType<ElderGuardian> ELDER_GUARDIAN = register("elder_guardian", Builder.<ElderGuardian>of(ElderGuardian::new, MobCategory.MONSTER).sized(1.9975F, 1.9975F).clientTrackingRange(10));
/* 172 */   public static final EntityType<EndCrystal> END_CRYSTAL = register("end_crystal", Builder.<EndCrystal>of(EndCrystal::new, MobCategory.MISC).sized(2.0F, 2.0F).clientTrackingRange(16).updateInterval(2147483647));
/* 173 */   public static final EntityType<EnderDragon> ENDER_DRAGON = register("ender_dragon", Builder.<EnderDragon>of(EnderDragon::new, MobCategory.MONSTER).fireImmune().sized(16.0F, 8.0F).clientTrackingRange(10));
/* 174 */   public static final EntityType<EnderMan> ENDERMAN = register("enderman", Builder.<EnderMan>of(EnderMan::new, MobCategory.MONSTER).sized(0.6F, 2.9F).clientTrackingRange(8));
/* 175 */   public static final EntityType<Endermite> ENDERMITE = register("endermite", Builder.<Endermite>of(Endermite::new, MobCategory.MONSTER).sized(0.4F, 0.3F).clientTrackingRange(8));
/* 176 */   public static final EntityType<Evoker> EVOKER = register("evoker", Builder.<Evoker>of(Evoker::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
/* 177 */   public static final EntityType<EvokerFangs> EVOKER_FANGS = register("evoker_fangs", Builder.<EvokerFangs>of(EvokerFangs::new, MobCategory.MISC).sized(0.5F, 0.8F).clientTrackingRange(6).updateInterval(2));
/* 178 */   public static final EntityType<ExperienceOrb> EXPERIENCE_ORB = register("experience_orb", Builder.<ExperienceOrb>of(ExperienceOrb::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(6).updateInterval(20));
/* 179 */   public static final EntityType<EyeOfEnder> EYE_OF_ENDER = register("eye_of_ender", Builder.<EyeOfEnder>of(EyeOfEnder::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(4));
/* 180 */   public static final EntityType<FallingBlockEntity> FALLING_BLOCK = register("falling_block", Builder.<FallingBlockEntity>of(FallingBlockEntity::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20));
/* 181 */   public static final EntityType<FireworkRocketEntity> FIREWORK_ROCKET = register("firework_rocket", Builder.<FireworkRocketEntity>of(FireworkRocketEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
/* 182 */   public static final EntityType<Fox> FOX = register("fox", Builder.<Fox>of(Fox::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(8).immuneTo(new Block[] { Blocks.SWEET_BERRY_BUSH }));
/* 183 */   public static final EntityType<Ghast> GHAST = register("ghast", Builder.<Ghast>of(Ghast::new, MobCategory.MONSTER).fireImmune().sized(4.0F, 4.0F).clientTrackingRange(10));
/* 184 */   public static final EntityType<Giant> GIANT = register("giant", Builder.<Giant>of(Giant::new, MobCategory.MONSTER).sized(3.6F, 12.0F).clientTrackingRange(10));
/* 185 */   public static final EntityType<Guardian> GUARDIAN = register("guardian", Builder.<Guardian>of(Guardian::new, MobCategory.MONSTER).sized(0.85F, 0.85F).clientTrackingRange(8));
/* 186 */   public static final EntityType<Hoglin> HOGLIN = register("hoglin", Builder.<Hoglin>of(Hoglin::new, MobCategory.MONSTER).sized(1.3964844F, 1.4F).clientTrackingRange(8));
/* 187 */   public static final EntityType<Horse> HORSE = register("horse", Builder.<Horse>of(Horse::new, MobCategory.CREATURE).sized(1.3964844F, 1.6F).clientTrackingRange(10));
/* 188 */   public static final EntityType<Husk> HUSK = register("husk", Builder.<Husk>of(Husk::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
/* 189 */   public static final EntityType<Illusioner> ILLUSIONER = register("illusioner", Builder.<Illusioner>of(Illusioner::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
/* 190 */   public static final EntityType<IronGolem> IRON_GOLEM = register("iron_golem", Builder.<IronGolem>of(IronGolem::new, MobCategory.MISC).sized(1.4F, 2.7F).clientTrackingRange(10));
/* 191 */   public static final EntityType<ItemEntity> ITEM = register("item", Builder.<ItemEntity>of(ItemEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(20));
/* 192 */   public static final EntityType<ItemFrame> ITEM_FRAME = register("item_frame", Builder.<ItemFrame>of(ItemFrame::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(2147483647));
/* 193 */   public static final EntityType<LargeFireball> FIREBALL = register("fireball", Builder.<LargeFireball>of(LargeFireball::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10));
/* 194 */   public static final EntityType<LeashFenceKnotEntity> LEASH_KNOT = register("leash_knot", Builder.<LeashFenceKnotEntity>of(LeashFenceKnotEntity::new, MobCategory.MISC).noSave().sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(2147483647));
/* 195 */   public static final EntityType<LightningBolt> LIGHTNING_BOLT = register("lightning_bolt", Builder.<LightningBolt>of(LightningBolt::new, MobCategory.MISC).noSave().sized(0.0F, 0.0F).clientTrackingRange(16).updateInterval(2147483647));
/* 196 */   public static final EntityType<Llama> LLAMA = register("llama", Builder.<Llama>of(Llama::new, MobCategory.CREATURE).sized(0.9F, 1.87F).clientTrackingRange(10));
/* 197 */   public static final EntityType<LlamaSpit> LLAMA_SPIT = register("llama_spit", Builder.<LlamaSpit>of(LlamaSpit::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
/* 198 */   public static final EntityType<MagmaCube> MAGMA_CUBE = register("magma_cube", Builder.<MagmaCube>of(MagmaCube::new, MobCategory.MONSTER).fireImmune().sized(2.04F, 2.04F).clientTrackingRange(8));
/* 199 */   public static final EntityType<Minecart> MINECART = register("minecart", Builder.<Minecart>of(Minecart::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8));
/* 200 */   public static final EntityType<MinecartChest> CHEST_MINECART = register("chest_minecart", Builder.<MinecartChest>of(MinecartChest::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8));
/* 201 */   public static final EntityType<MinecartCommandBlock> COMMAND_BLOCK_MINECART = register("command_block_minecart", Builder.<MinecartCommandBlock>of(MinecartCommandBlock::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8));
/* 202 */   public static final EntityType<MinecartFurnace> FURNACE_MINECART = register("furnace_minecart", Builder.<MinecartFurnace>of(MinecartFurnace::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8));
/* 203 */   public static final EntityType<MinecartHopper> HOPPER_MINECART = register("hopper_minecart", Builder.<MinecartHopper>of(MinecartHopper::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8));
/* 204 */   public static final EntityType<MinecartSpawner> SPAWNER_MINECART = register("spawner_minecart", Builder.<MinecartSpawner>of(MinecartSpawner::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8));
/* 205 */   public static final EntityType<MinecartTNT> TNT_MINECART = register("tnt_minecart", Builder.<MinecartTNT>of(MinecartTNT::new, MobCategory.MISC).sized(0.98F, 0.7F).clientTrackingRange(8));
/* 206 */   public static final EntityType<Mule> MULE = register("mule", Builder.<Mule>of(Mule::new, MobCategory.CREATURE).sized(1.3964844F, 1.6F).clientTrackingRange(8));
/* 207 */   public static final EntityType<MushroomCow> MOOSHROOM = register("mooshroom", Builder.<MushroomCow>of(MushroomCow::new, MobCategory.CREATURE).sized(0.9F, 1.4F).clientTrackingRange(10));
/* 208 */   public static final EntityType<Ocelot> OCELOT = register("ocelot", Builder.<Ocelot>of(Ocelot::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(10));
/* 209 */   public static final EntityType<Painting> PAINTING = register("painting", Builder.<Painting>of(Painting::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(2147483647));
/* 210 */   public static final EntityType<Panda> PANDA = register("panda", Builder.<Panda>of(Panda::new, MobCategory.CREATURE).sized(1.3F, 1.25F).clientTrackingRange(10));
/* 211 */   public static final EntityType<Parrot> PARROT = register("parrot", Builder.<Parrot>of(Parrot::new, MobCategory.CREATURE).sized(0.5F, 0.9F).clientTrackingRange(8));
/* 212 */   public static final EntityType<Phantom> PHANTOM = register("phantom", Builder.<Phantom>of(Phantom::new, MobCategory.MONSTER).sized(0.9F, 0.5F).clientTrackingRange(8));
/* 213 */   public static final EntityType<Pig> PIG = register("pig", Builder.<Pig>of(Pig::new, MobCategory.CREATURE).sized(0.9F, 0.9F).clientTrackingRange(10));
/* 214 */   public static final EntityType<Piglin> PIGLIN = register("piglin", Builder.<Piglin>of(Piglin::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
/* 215 */   public static final EntityType<PiglinBrute> PIGLIN_BRUTE = register("piglin_brute", Builder.<PiglinBrute>of(PiglinBrute::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
/* 216 */   public static final EntityType<Pillager> PILLAGER = register("pillager", Builder.<Pillager>of(Pillager::new, MobCategory.MONSTER).canSpawnFarFromPlayer().sized(0.6F, 1.95F).clientTrackingRange(8));
/* 217 */   public static final EntityType<PolarBear> POLAR_BEAR = register("polar_bear", Builder.<PolarBear>of(PolarBear::new, MobCategory.CREATURE).sized(1.4F, 1.4F).clientTrackingRange(10));
/* 218 */   public static final EntityType<PrimedTnt> TNT = register("tnt", Builder.<PrimedTnt>of(PrimedTnt::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(10));
/* 219 */   public static final EntityType<Pufferfish> PUFFERFISH = register("pufferfish", Builder.<Pufferfish>of(Pufferfish::new, MobCategory.WATER_AMBIENT).sized(0.7F, 0.7F).clientTrackingRange(4));
/* 220 */   public static final EntityType<Rabbit> RABBIT = register("rabbit", Builder.<Rabbit>of(Rabbit::new, MobCategory.CREATURE).sized(0.4F, 0.5F).clientTrackingRange(8));
/* 221 */   public static final EntityType<Ravager> RAVAGER = register("ravager", Builder.<Ravager>of(Ravager::new, MobCategory.MONSTER).sized(1.95F, 2.2F).clientTrackingRange(10));
/* 222 */   public static final EntityType<Salmon> SALMON = register("salmon", Builder.<Salmon>of(Salmon::new, MobCategory.WATER_AMBIENT).sized(0.7F, 0.4F).clientTrackingRange(4));
/* 223 */   public static final EntityType<Sheep> SHEEP = register("sheep", Builder.<Sheep>of(Sheep::new, MobCategory.CREATURE).sized(0.9F, 1.3F).clientTrackingRange(10));
/* 224 */   public static final EntityType<Shulker> SHULKER = register("shulker", Builder.<Shulker>of(Shulker::new, MobCategory.MONSTER).fireImmune().canSpawnFarFromPlayer().sized(1.0F, 1.0F).clientTrackingRange(10));
/* 225 */   public static final EntityType<ShulkerBullet> SHULKER_BULLET = register("shulker_bullet", Builder.<ShulkerBullet>of(ShulkerBullet::new, MobCategory.MISC).sized(0.3125F, 0.3125F).clientTrackingRange(8));
/* 226 */   public static final EntityType<Silverfish> SILVERFISH = register("silverfish", Builder.<Silverfish>of(Silverfish::new, MobCategory.MONSTER).sized(0.4F, 0.3F).clientTrackingRange(8));
/* 227 */   public static final EntityType<Skeleton> SKELETON = register("skeleton", Builder.<Skeleton>of(Skeleton::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8));
/* 228 */   public static final EntityType<SkeletonHorse> SKELETON_HORSE = register("skeleton_horse", Builder.<SkeletonHorse>of(SkeletonHorse::new, MobCategory.CREATURE).sized(1.3964844F, 1.6F).clientTrackingRange(10));
/* 229 */   public static final EntityType<Slime> SLIME = register("slime", Builder.<Slime>of(Slime::new, MobCategory.MONSTER).sized(2.04F, 2.04F).clientTrackingRange(10));
/* 230 */   public static final EntityType<SmallFireball> SMALL_FIREBALL = register("small_fireball", Builder.<SmallFireball>of(SmallFireball::new, MobCategory.MISC).sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10));
/* 231 */   public static final EntityType<SnowGolem> SNOW_GOLEM = register("snow_golem", Builder.<SnowGolem>of(SnowGolem::new, MobCategory.MISC).sized(0.7F, 1.9F).clientTrackingRange(8));
/* 232 */   public static final EntityType<Snowball> SNOWBALL = register("snowball", Builder.<Snowball>of(Snowball::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
/* 233 */   public static final EntityType<SpectralArrow> SPECTRAL_ARROW = register("spectral_arrow", Builder.<SpectralArrow>of(SpectralArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
/* 234 */   public static final EntityType<Spider> SPIDER = register("spider", Builder.<Spider>of(Spider::new, MobCategory.MONSTER).sized(1.4F, 0.9F).clientTrackingRange(8));
/* 235 */   public static final EntityType<Squid> SQUID = register("squid", Builder.<Squid>of(Squid::new, MobCategory.WATER_CREATURE).sized(0.8F, 0.8F).clientTrackingRange(8));
/* 236 */   public static final EntityType<Stray> STRAY = register("stray", Builder.<Stray>of(Stray::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8));
/* 237 */   public static final EntityType<Strider> STRIDER = register("strider", Builder.<Strider>of(Strider::new, MobCategory.CREATURE).fireImmune().sized(0.9F, 1.7F).clientTrackingRange(10));
/* 238 */   public static final EntityType<ThrownEgg> EGG = register("egg", Builder.<ThrownEgg>of(ThrownEgg::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
/* 239 */   public static final EntityType<ThrownEnderpearl> ENDER_PEARL = register("ender_pearl", Builder.<ThrownEnderpearl>of(ThrownEnderpearl::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
/* 240 */   public static final EntityType<ThrownExperienceBottle> EXPERIENCE_BOTTLE = register("experience_bottle", Builder.<ThrownExperienceBottle>of(ThrownExperienceBottle::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
/* 241 */   public static final EntityType<ThrownPotion> POTION = register("potion", Builder.<ThrownPotion>of(ThrownPotion::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
/* 242 */   public static final EntityType<ThrownTrident> TRIDENT = register("trident", Builder.<ThrownTrident>of(ThrownTrident::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
/* 243 */   public static final EntityType<TraderLlama> TRADER_LLAMA = register("trader_llama", Builder.<TraderLlama>of(TraderLlama::new, MobCategory.CREATURE).sized(0.9F, 1.87F).clientTrackingRange(10));
/* 244 */   public static final EntityType<TropicalFish> TROPICAL_FISH = register("tropical_fish", Builder.<TropicalFish>of(TropicalFish::new, MobCategory.WATER_AMBIENT).sized(0.5F, 0.4F).clientTrackingRange(4));
/* 245 */   public static final EntityType<Turtle> TURTLE = register("turtle", Builder.<Turtle>of(Turtle::new, MobCategory.CREATURE).sized(1.2F, 0.4F).clientTrackingRange(10));
/* 246 */   public static final EntityType<Vex> VEX = register("vex", Builder.<Vex>of(Vex::new, MobCategory.MONSTER).fireImmune().sized(0.4F, 0.8F).clientTrackingRange(8));
/* 247 */   public static final EntityType<Villager> VILLAGER = register("villager", Builder.<Villager>of(Villager::new, MobCategory.MISC).sized(0.6F, 1.95F).clientTrackingRange(10));
/* 248 */   public static final EntityType<Vindicator> VINDICATOR = register("vindicator", Builder.<Vindicator>of(Vindicator::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
/* 249 */   public static final EntityType<WanderingTrader> WANDERING_TRADER = register("wandering_trader", Builder.<WanderingTrader>of(WanderingTrader::new, MobCategory.CREATURE).sized(0.6F, 1.95F).clientTrackingRange(10));
/* 250 */   public static final EntityType<Witch> WITCH = register("witch", Builder.<Witch>of(Witch::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
/* 251 */   public static final EntityType<WitherBoss> WITHER = register("wither", Builder.<WitherBoss>of(WitherBoss::new, MobCategory.MONSTER).fireImmune().immuneTo(new Block[] { Blocks.WITHER_ROSE }).sized(0.9F, 3.5F).clientTrackingRange(10));
/* 252 */   public static final EntityType<WitherSkeleton> WITHER_SKELETON = register("wither_skeleton", Builder.<WitherSkeleton>of(WitherSkeleton::new, MobCategory.MONSTER).fireImmune().immuneTo(new Block[] { Blocks.WITHER_ROSE }).sized(0.7F, 2.4F).clientTrackingRange(8));
/* 253 */   public static final EntityType<WitherSkull> WITHER_SKULL = register("wither_skull", Builder.<WitherSkull>of(WitherSkull::new, MobCategory.MISC).sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10));
/* 254 */   public static final EntityType<Wolf> WOLF = register("wolf", Builder.<Wolf>of(Wolf::new, MobCategory.CREATURE).sized(0.6F, 0.85F).clientTrackingRange(10));
/* 255 */   public static final EntityType<Zoglin> ZOGLIN = register("zoglin", Builder.<Zoglin>of(Zoglin::new, MobCategory.MONSTER).fireImmune().sized(1.3964844F, 1.4F).clientTrackingRange(8));
/* 256 */   public static final EntityType<Zombie> ZOMBIE = register("zombie", Builder.<Zombie>of(Zombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
/* 257 */   public static final EntityType<ZombieHorse> ZOMBIE_HORSE = register("zombie_horse", Builder.<ZombieHorse>of(ZombieHorse::new, MobCategory.CREATURE).sized(1.3964844F, 1.6F).clientTrackingRange(10));
/* 258 */   public static final EntityType<ZombieVillager> ZOMBIE_VILLAGER = register("zombie_villager", Builder.<ZombieVillager>of(ZombieVillager::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8));
/* 259 */   public static final EntityType<ZombifiedPiglin> ZOMBIFIED_PIGLIN = register("zombified_piglin", Builder.<ZombifiedPiglin>of(ZombifiedPiglin::new, MobCategory.MONSTER).fireImmune().sized(0.6F, 1.95F).clientTrackingRange(8));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 268 */   public static final EntityType<Player> PLAYER = register("player", Builder.<Player>createNothing(MobCategory.MISC).noSave().noSummon().sized(0.6F, 1.8F).clientTrackingRange(32).updateInterval(2));
/* 269 */   public static final EntityType<FishingHook> FISHING_BOBBER = register("fishing_bobber", Builder.<FishingHook>createNothing(MobCategory.MISC).noSave().noSummon().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(5));
/*     */   private final EntityFactory<T> factory; private final MobCategory category; private final ImmutableSet<Block> immuneTo; private final boolean serialize; private final boolean summon; private final boolean fireImmune;
/*     */   
/* 272 */   public static ResourceLocation getKey(EntityType<?> debug0) { return Registry.ENTITY_TYPE.getKey(debug0); } private final boolean canSpawnFarFromPlayer; private final int clientTrackingRange; private final int updateInterval; @Nullable
/*     */   private String descriptionId; @Nullable
/*     */   private Component description; @Nullable
/*     */   private ResourceLocation lootTable; private final EntityDimensions dimensions; public static Optional<EntityType<?>> byString(String debug0) {
/* 276 */     return Registry.ENTITY_TYPE.getOptional(ResourceLocation.tryParse(debug0));
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
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityType(EntityFactory<T> debug1, MobCategory debug2, boolean debug3, boolean debug4, boolean debug5, boolean debug6, ImmutableSet<Block> debug7, EntityDimensions debug8, int debug9, int debug10) {
/* 297 */     this.factory = debug1;
/* 298 */     this.category = debug2;
/* 299 */     this.canSpawnFarFromPlayer = debug6;
/* 300 */     this.serialize = debug3;
/* 301 */     this.summon = debug4;
/* 302 */     this.fireImmune = debug5;
/* 303 */     this.immuneTo = debug7;
/* 304 */     this.dimensions = debug8;
/* 305 */     this.clientTrackingRange = debug9;
/* 306 */     this.updateInterval = debug10;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Entity spawn(ServerLevel debug1, @Nullable ItemStack debug2, @Nullable Player debug3, BlockPos debug4, MobSpawnType debug5, boolean debug6, boolean debug7) {
/* 311 */     return (Entity)spawn(debug1, (debug2 == null) ? null : debug2
/*     */         
/* 313 */         .getTag(), (debug2 != null && debug2
/* 314 */         .hasCustomHoverName()) ? debug2.getHoverName() : null, debug3, debug4, debug5, debug6, debug7);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T spawn(ServerLevel debug1, @Nullable CompoundTag debug2, @Nullable Component debug3, @Nullable Player debug4, BlockPos debug5, MobSpawnType debug6, boolean debug7, boolean debug8) {
/* 320 */     T debug9 = create(debug1, debug2, debug3, debug4, debug5, debug6, debug7, debug8);
/* 321 */     if (debug9 != null) {
/* 322 */       debug1.addFreshEntityWithPassengers((Entity)debug9);
/*     */     }
/* 324 */     return debug9;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T create(ServerLevel debug1, @Nullable CompoundTag debug2, @Nullable Component debug3, @Nullable Player debug4, BlockPos debug5, MobSpawnType debug6, boolean debug7, boolean debug8) {
/*     */     double debug10;
/* 332 */     T debug9 = create((Level)debug1);
/*     */     
/* 334 */     if (debug9 == null) {
/* 335 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 339 */     if (debug7) {
/* 340 */       debug9.setPos(debug5.getX() + 0.5D, (debug5.getY() + 1), debug5.getZ() + 0.5D);
/*     */       
/* 342 */       debug10 = getYOffset((LevelReader)debug1, debug5, debug8, debug9.getBoundingBox());
/*     */     } else {
/* 344 */       debug10 = 0.0D;
/*     */     } 
/*     */     
/* 347 */     debug9.moveTo(debug5.getX() + 0.5D, debug5.getY() + debug10, debug5.getZ() + 0.5D, Mth.wrapDegrees(debug1.random.nextFloat() * 360.0F), 0.0F);
/*     */ 
/*     */     
/* 350 */     if (debug9 instanceof Mob) {
/* 351 */       Mob debug12 = (Mob)debug9;
/*     */       
/* 353 */       debug12.yHeadRot = debug12.yRot;
/* 354 */       debug12.yBodyRot = debug12.yRot;
/*     */       
/* 356 */       debug12.finalizeSpawn((ServerLevelAccessor)debug1, debug1.getCurrentDifficultyAt(debug12.blockPosition()), debug6, (SpawnGroupData)null, debug2);
/* 357 */       debug12.playAmbientSound();
/*     */     } 
/*     */     
/* 360 */     if (debug3 != null && debug9 instanceof LivingEntity) {
/* 361 */       debug9.setCustomName(debug3);
/*     */     }
/*     */     
/* 364 */     updateCustomEntityTag((Level)debug1, debug4, (Entity)debug9, debug2);
/*     */     
/* 366 */     return debug9;
/*     */   }
/*     */   
/*     */   protected static double getYOffset(LevelReader debug0, BlockPos debug1, boolean debug2, AABB debug3) {
/* 370 */     AABB debug4 = new AABB(debug1);
/* 371 */     if (debug2) {
/* 372 */       debug4 = debug4.expandTowards(0.0D, -1.0D, 0.0D);
/*     */     }
/* 374 */     Stream<VoxelShape> debug5 = debug0.getCollisions(null, debug4, debug0 -> true);
/*     */     
/* 376 */     return 1.0D + Shapes.collide(Direction.Axis.Y, debug3, debug5, debug2 ? -2.0D : -1.0D);
/*     */   }
/*     */   
/*     */   public static void updateCustomEntityTag(Level debug0, @Nullable Player debug1, @Nullable Entity debug2, @Nullable CompoundTag debug3) {
/* 380 */     if (debug3 == null || !debug3.contains("EntityTag", 10)) {
/*     */       return;
/*     */     }
/*     */     
/* 384 */     MinecraftServer debug4 = debug0.getServer();
/* 385 */     if (debug4 == null || debug2 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 389 */     if (!debug0.isClientSide && debug2.onlyOpCanSetNbt() && (debug1 == null || !debug4.getPlayerList().isOp(debug1.getGameProfile()))) {
/*     */       return;
/*     */     }
/*     */     
/* 393 */     CompoundTag debug5 = debug2.saveWithoutId(new CompoundTag());
/* 394 */     UUID debug6 = debug2.getUUID();
/* 395 */     debug5.merge(debug3.getCompound("EntityTag"));
/* 396 */     debug2.setUUID(debug6);
/* 397 */     debug2.load(debug5);
/*     */   }
/*     */   
/*     */   public boolean canSerialize() {
/* 401 */     return this.serialize;
/*     */   }
/*     */   
/*     */   public boolean canSummon() {
/* 405 */     return this.summon;
/*     */   }
/*     */   
/*     */   public boolean fireImmune() {
/* 409 */     return this.fireImmune;
/*     */   }
/*     */   
/*     */   public boolean canSpawnFarFromPlayer() {
/* 413 */     return this.canSpawnFarFromPlayer;
/*     */   }
/*     */   
/*     */   public MobCategory getCategory() {
/* 417 */     return this.category;
/*     */   }
/*     */   
/*     */   public String getDescriptionId() {
/* 421 */     if (this.descriptionId == null) {
/* 422 */       this.descriptionId = Util.makeDescriptionId("entity", Registry.ENTITY_TYPE.getKey(this));
/*     */     }
/* 424 */     return this.descriptionId;
/*     */   }
/*     */   
/*     */   public Component getDescription() {
/* 428 */     if (this.description == null) {
/* 429 */       this.description = (Component)new TranslatableComponent(getDescriptionId());
/*     */     }
/* 431 */     return this.description;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 436 */     return getDescriptionId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceLocation getDefaultLootTable() {
/* 445 */     if (this.lootTable == null) {
/* 446 */       ResourceLocation debug1 = Registry.ENTITY_TYPE.getKey(this);
/*     */       
/* 448 */       this.lootTable = new ResourceLocation(debug1.getNamespace(), "entities/" + debug1.getPath());
/*     */     } 
/* 450 */     return this.lootTable;
/*     */   }
/*     */   
/*     */   public float getWidth() {
/* 454 */     return this.dimensions.width;
/*     */   }
/*     */   
/*     */   public float getHeight() {
/* 458 */     return this.dimensions.height;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public T create(Level debug1) {
/* 463 */     return this.factory.create(this, debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Optional<Entity> create(CompoundTag debug0, Level debug1) {
/* 472 */     return Util.ifElse(by(debug0).map(debug1 -> debug1.create(debug0)), debug1 -> debug1.load(debug0), () -> LOGGER.warn("Skipping Entity with id {}", debug0.getString("id")));
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
/*     */   public AABB getAABB(double debug1, double debug3, double debug5) {
/* 485 */     float debug7 = getWidth() / 2.0F;
/* 486 */     return new AABB(debug1 - debug7, debug3, debug5 - debug7, debug1 + debug7, debug3 + 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 491 */         getHeight(), debug5 + debug7);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBlockDangerous(BlockState debug1) {
/* 497 */     if (this.immuneTo.contains(debug1.getBlock())) {
/* 498 */       return false;
/*     */     }
/* 500 */     if (!this.fireImmune && (debug1.is((Tag)BlockTags.FIRE) || debug1.is(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(debug1) || debug1.is(Blocks.LAVA))) {
/* 501 */       return true;
/*     */     }
/* 503 */     return (debug1.is(Blocks.WITHER_ROSE) || debug1.is(Blocks.SWEET_BERRY_BUSH) || debug1.is(Blocks.CACTUS));
/*     */   }
/*     */   
/*     */   public EntityDimensions getDimensions() {
/* 507 */     return this.dimensions;
/*     */   }
/*     */   
/*     */   public static Optional<EntityType<?>> by(CompoundTag debug0) {
/* 511 */     return Registry.ENTITY_TYPE.getOptional(new ResourceLocation(debug0.getString("id")));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Entity loadEntityRecursive(CompoundTag debug0, Level debug1, Function<Entity, Entity> debug2) {
/* 516 */     return loadStaticEntity(debug0, debug1)
/* 517 */       .<Entity>map(debug2)
/* 518 */       .map(debug3 -> {
/*     */           if (debug0.contains("Passengers", 9)) {
/*     */             ListTag debug4 = debug0.getList("Passengers", 10);
/*     */             
/*     */             for (int debug5 = 0; debug5 < debug4.size(); debug5++) {
/*     */               Entity debug6 = loadEntityRecursive(debug4.getCompound(debug5), debug1, debug2);
/*     */               if (debug6 != null) {
/*     */                 debug6.startRiding(debug3, true);
/*     */               }
/*     */             } 
/*     */           } 
/*     */           return debug3;
/* 530 */         }).orElse(null);
/*     */   }
/*     */   
/*     */   private static Optional<Entity> loadStaticEntity(CompoundTag debug0, Level debug1) {
/*     */     try {
/* 535 */       return create(debug0, debug1);
/* 536 */     } catch (RuntimeException debug2) {
/* 537 */       LOGGER.warn("Exception loading entity: ", debug2);
/* 538 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int clientTrackingRange() {
/* 543 */     return this.clientTrackingRange;
/*     */   }
/*     */   
/*     */   public int updateInterval() {
/* 547 */     return this.updateInterval;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean trackDeltas() {
/* 552 */     return (this != PLAYER && this != LLAMA_SPIT && this != WITHER && this != BAT && this != ITEM_FRAME && this != LEASH_KNOT && this != PAINTING && this != END_CRYSTAL && this != EVOKER_FANGS);
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
/*     */   public boolean is(Tag<EntityType<?>> debug1) {
/* 565 */     return debug1.contains(this);
/*     */   }
/*     */   public static interface EntityFactory<T extends Entity> {
/*     */     T create(EntityType<T> param1EntityType, Level param1Level); }
/*     */   public static class Builder<T extends Entity> { private final EntityType.EntityFactory<T> factory;
/*     */     private final MobCategory category;
/* 571 */     private ImmutableSet<Block> immuneTo = ImmutableSet.of();
/*     */     private boolean serialize = true;
/*     */     private boolean summon = true;
/*     */     private boolean fireImmune;
/*     */     private boolean canSpawnFarFromPlayer;
/* 576 */     private int clientTrackingRange = 5;
/* 577 */     private int updateInterval = 3;
/* 578 */     private EntityDimensions dimensions = EntityDimensions.scalable(0.6F, 1.8F);
/*     */     
/*     */     private Builder(EntityType.EntityFactory<T> debug1, MobCategory debug2) {
/* 581 */       this.factory = debug1;
/* 582 */       this.category = debug2;
/* 583 */       this.canSpawnFarFromPlayer = (debug2 == MobCategory.CREATURE || debug2 == MobCategory.MISC);
/*     */     }
/*     */     
/*     */     public static <T extends Entity> Builder<T> of(EntityType.EntityFactory<T> debug0, MobCategory debug1) {
/* 587 */       return new Builder<>(debug0, debug1);
/*     */     }
/*     */     
/*     */     public static <T extends Entity> Builder<T> createNothing(MobCategory debug0) {
/* 591 */       return new Builder<>((debug0, debug1) -> null, debug0);
/*     */     }
/*     */     
/*     */     public Builder<T> sized(float debug1, float debug2) {
/* 595 */       this.dimensions = EntityDimensions.scalable(debug1, debug2);
/* 596 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> noSummon() {
/* 600 */       this.summon = false;
/* 601 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> noSave() {
/* 605 */       this.serialize = false;
/* 606 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> fireImmune() {
/* 610 */       this.fireImmune = true;
/* 611 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> immuneTo(Block... debug1) {
/* 615 */       this.immuneTo = ImmutableSet.copyOf((Object[])debug1);
/* 616 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> canSpawnFarFromPlayer() {
/* 620 */       this.canSpawnFarFromPlayer = true;
/* 621 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> clientTrackingRange(int debug1) {
/* 625 */       this.clientTrackingRange = debug1;
/* 626 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> updateInterval(int debug1) {
/* 630 */       this.updateInterval = debug1;
/* 631 */       return this;
/*     */     }
/*     */     
/*     */     public EntityType<T> build(String debug1) {
/* 635 */       if (this.serialize) {
/* 636 */         Util.fetchChoiceType(References.ENTITY_TREE, debug1);
/*     */       }
/* 638 */       return new EntityType<>(this.factory, this.category, this.serialize, this.summon, this.fireImmune, this.canSpawnFarFromPlayer, this.immuneTo, this.dimensions, this.clientTrackingRange, this.updateInterval);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\EntityType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */