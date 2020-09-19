/*     */ package net.minecraft.core.particles;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Registry;
/*     */ 
/*     */ public class ParticleTypes
/*     */ {
/*   9 */   public static final SimpleParticleType AMBIENT_ENTITY_EFFECT = register("ambient_entity_effect", false);
/*  10 */   public static final SimpleParticleType ANGRY_VILLAGER = register("angry_villager", false);
/*  11 */   public static final SimpleParticleType BARRIER = register("barrier", false);
/*  12 */   public static final ParticleType<BlockParticleOption> BLOCK = register("block", BlockParticleOption.DESERIALIZER, BlockParticleOption::codec);
/*  13 */   public static final SimpleParticleType BUBBLE = register("bubble", false);
/*  14 */   public static final SimpleParticleType CLOUD = register("cloud", false);
/*  15 */   public static final SimpleParticleType CRIT = register("crit", false);
/*  16 */   public static final SimpleParticleType DAMAGE_INDICATOR = register("damage_indicator", true);
/*  17 */   public static final SimpleParticleType DRAGON_BREATH = register("dragon_breath", false);
/*  18 */   public static final SimpleParticleType DRIPPING_LAVA = register("dripping_lava", false);
/*  19 */   public static final SimpleParticleType FALLING_LAVA = register("falling_lava", false);
/*  20 */   public static final SimpleParticleType LANDING_LAVA = register("landing_lava", false);
/*  21 */   public static final SimpleParticleType DRIPPING_WATER = register("dripping_water", false);
/*  22 */   public static final SimpleParticleType FALLING_WATER = register("falling_water", false);
/*  23 */   public static final ParticleType<DustParticleOptions> DUST = register("dust", DustParticleOptions.DESERIALIZER, debug0 -> DustParticleOptions.CODEC);
/*  24 */   public static final SimpleParticleType EFFECT = register("effect", false);
/*  25 */   public static final SimpleParticleType ELDER_GUARDIAN = register("elder_guardian", true);
/*  26 */   public static final SimpleParticleType ENCHANTED_HIT = register("enchanted_hit", false);
/*  27 */   public static final SimpleParticleType ENCHANT = register("enchant", false);
/*  28 */   public static final SimpleParticleType END_ROD = register("end_rod", false);
/*  29 */   public static final SimpleParticleType ENTITY_EFFECT = register("entity_effect", false);
/*  30 */   public static final SimpleParticleType EXPLOSION_EMITTER = register("explosion_emitter", true);
/*  31 */   public static final SimpleParticleType EXPLOSION = register("explosion", true);
/*  32 */   public static final ParticleType<BlockParticleOption> FALLING_DUST = register("falling_dust", BlockParticleOption.DESERIALIZER, BlockParticleOption::codec);
/*  33 */   public static final SimpleParticleType FIREWORK = register("firework", false);
/*  34 */   public static final SimpleParticleType FISHING = register("fishing", false);
/*  35 */   public static final SimpleParticleType FLAME = register("flame", false);
/*  36 */   public static final SimpleParticleType SOUL_FIRE_FLAME = register("soul_fire_flame", false);
/*  37 */   public static final SimpleParticleType SOUL = register("soul", false);
/*  38 */   public static final SimpleParticleType FLASH = register("flash", false);
/*  39 */   public static final SimpleParticleType HAPPY_VILLAGER = register("happy_villager", false);
/*  40 */   public static final SimpleParticleType COMPOSTER = register("composter", false);
/*  41 */   public static final SimpleParticleType HEART = register("heart", false);
/*  42 */   public static final SimpleParticleType INSTANT_EFFECT = register("instant_effect", false);
/*  43 */   public static final ParticleType<ItemParticleOption> ITEM = register("item", ItemParticleOption.DESERIALIZER, ItemParticleOption::codec);
/*     */   
/*  45 */   public static final SimpleParticleType ITEM_SLIME = register("item_slime", false);
/*  46 */   public static final SimpleParticleType ITEM_SNOWBALL = register("item_snowball", false);
/*  47 */   public static final SimpleParticleType LARGE_SMOKE = register("large_smoke", false);
/*  48 */   public static final SimpleParticleType LAVA = register("lava", false);
/*  49 */   public static final SimpleParticleType MYCELIUM = register("mycelium", false);
/*  50 */   public static final SimpleParticleType NOTE = register("note", false);
/*  51 */   public static final SimpleParticleType POOF = register("poof", true);
/*  52 */   public static final SimpleParticleType PORTAL = register("portal", false);
/*  53 */   public static final SimpleParticleType RAIN = register("rain", false);
/*  54 */   public static final SimpleParticleType SMOKE = register("smoke", false);
/*  55 */   public static final SimpleParticleType SNEEZE = register("sneeze", false);
/*  56 */   public static final SimpleParticleType SPIT = register("spit", true);
/*  57 */   public static final SimpleParticleType SQUID_INK = register("squid_ink", true);
/*  58 */   public static final SimpleParticleType SWEEP_ATTACK = register("sweep_attack", true);
/*  59 */   public static final SimpleParticleType TOTEM_OF_UNDYING = register("totem_of_undying", false);
/*     */   
/*  61 */   public static final SimpleParticleType UNDERWATER = register("underwater", false);
/*  62 */   public static final SimpleParticleType SPLASH = register("splash", false);
/*  63 */   public static final SimpleParticleType WITCH = register("witch", false);
/*  64 */   public static final SimpleParticleType BUBBLE_POP = register("bubble_pop", false);
/*  65 */   public static final SimpleParticleType CURRENT_DOWN = register("current_down", false);
/*  66 */   public static final SimpleParticleType BUBBLE_COLUMN_UP = register("bubble_column_up", false);
/*  67 */   public static final SimpleParticleType NAUTILUS = register("nautilus", false);
/*  68 */   public static final SimpleParticleType DOLPHIN = register("dolphin", false);
/*     */   
/*  70 */   public static final SimpleParticleType CAMPFIRE_COSY_SMOKE = register("campfire_cosy_smoke", true);
/*  71 */   public static final SimpleParticleType CAMPFIRE_SIGNAL_SMOKE = register("campfire_signal_smoke", true);
/*     */   
/*  73 */   public static final SimpleParticleType DRIPPING_HONEY = register("dripping_honey", false);
/*  74 */   public static final SimpleParticleType FALLING_HONEY = register("falling_honey", false);
/*  75 */   public static final SimpleParticleType LANDING_HONEY = register("landing_honey", false);
/*  76 */   public static final SimpleParticleType FALLING_NECTAR = register("falling_nectar", false);
/*     */   
/*  78 */   public static final SimpleParticleType ASH = register("ash", false);
/*  79 */   public static final SimpleParticleType CRIMSON_SPORE = register("crimson_spore", false);
/*  80 */   public static final SimpleParticleType WARPED_SPORE = register("warped_spore", false);
/*  81 */   public static final SimpleParticleType DRIPPING_OBSIDIAN_TEAR = register("dripping_obsidian_tear", false);
/*  82 */   public static final SimpleParticleType FALLING_OBSIDIAN_TEAR = register("falling_obsidian_tear", false);
/*  83 */   public static final SimpleParticleType LANDING_OBSIDIAN_TEAR = register("landing_obsidian_tear", false);
/*     */   
/*  85 */   public static final SimpleParticleType REVERSE_PORTAL = register("reverse_portal", false);
/*     */   
/*  87 */   public static final SimpleParticleType WHITE_ASH = register("white_ash", false);
/*     */   
/*     */   private static SimpleParticleType register(String debug0, boolean debug1) {
/*  90 */     return (SimpleParticleType)Registry.register(Registry.PARTICLE_TYPE, debug0, new SimpleParticleType(debug1));
/*     */   }
/*     */   
/*     */   private static <T extends ParticleOptions> ParticleType<T> register(String debug0, ParticleOptions.Deserializer<T> debug1, final Function<ParticleType<T>, Codec<T>> codec) {
/*  94 */     return (ParticleType<T>)Registry.register(Registry.PARTICLE_TYPE, debug0, new ParticleType<T>(false, debug1)
/*     */         {
/*     */           public Codec<T> codec() {
/*  97 */             return codec.apply(this);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/* 102 */   public static final Codec<ParticleOptions> CODEC = Registry.PARTICLE_TYPE.dispatch("type", ParticleOptions::getType, ParticleType::codec);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\core\particles\ParticleTypes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */