/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class EntityUUIDFix extends AbstractUUIDFix {
/*  14 */   private static final Set<String> ABSTRACT_HORSES = Sets.newHashSet();
/*  15 */   private static final Set<String> TAMEABLE_ANIMALS = Sets.newHashSet();
/*  16 */   private static final Set<String> ANIMALS = Sets.newHashSet();
/*  17 */   private static final Set<String> MOBS = Sets.newHashSet();
/*  18 */   private static final Set<String> LIVING_ENTITIES = Sets.newHashSet();
/*  19 */   private static final Set<String> PROJECTILES = Sets.newHashSet();
/*     */   
/*     */   static {
/*  22 */     ABSTRACT_HORSES.add("minecraft:donkey");
/*  23 */     ABSTRACT_HORSES.add("minecraft:horse");
/*  24 */     ABSTRACT_HORSES.add("minecraft:llama");
/*  25 */     ABSTRACT_HORSES.add("minecraft:mule");
/*  26 */     ABSTRACT_HORSES.add("minecraft:skeleton_horse");
/*  27 */     ABSTRACT_HORSES.add("minecraft:trader_llama");
/*  28 */     ABSTRACT_HORSES.add("minecraft:zombie_horse");
/*  29 */     TAMEABLE_ANIMALS.add("minecraft:cat");
/*  30 */     TAMEABLE_ANIMALS.add("minecraft:parrot");
/*  31 */     TAMEABLE_ANIMALS.add("minecraft:wolf");
/*  32 */     ANIMALS.add("minecraft:bee");
/*  33 */     ANIMALS.add("minecraft:chicken");
/*  34 */     ANIMALS.add("minecraft:cow");
/*  35 */     ANIMALS.add("minecraft:fox");
/*  36 */     ANIMALS.add("minecraft:mooshroom");
/*  37 */     ANIMALS.add("minecraft:ocelot");
/*  38 */     ANIMALS.add("minecraft:panda");
/*  39 */     ANIMALS.add("minecraft:pig");
/*  40 */     ANIMALS.add("minecraft:polar_bear");
/*  41 */     ANIMALS.add("minecraft:rabbit");
/*  42 */     ANIMALS.add("minecraft:sheep");
/*  43 */     ANIMALS.add("minecraft:turtle");
/*  44 */     ANIMALS.add("minecraft:hoglin");
/*  45 */     MOBS.add("minecraft:bat");
/*  46 */     MOBS.add("minecraft:blaze");
/*  47 */     MOBS.add("minecraft:cave_spider");
/*  48 */     MOBS.add("minecraft:cod");
/*  49 */     MOBS.add("minecraft:creeper");
/*  50 */     MOBS.add("minecraft:dolphin");
/*  51 */     MOBS.add("minecraft:drowned");
/*  52 */     MOBS.add("minecraft:elder_guardian");
/*  53 */     MOBS.add("minecraft:ender_dragon");
/*  54 */     MOBS.add("minecraft:enderman");
/*  55 */     MOBS.add("minecraft:endermite");
/*  56 */     MOBS.add("minecraft:evoker");
/*  57 */     MOBS.add("minecraft:ghast");
/*  58 */     MOBS.add("minecraft:giant");
/*  59 */     MOBS.add("minecraft:guardian");
/*  60 */     MOBS.add("minecraft:husk");
/*  61 */     MOBS.add("minecraft:illusioner");
/*  62 */     MOBS.add("minecraft:magma_cube");
/*  63 */     MOBS.add("minecraft:pufferfish");
/*  64 */     MOBS.add("minecraft:zombified_piglin");
/*  65 */     MOBS.add("minecraft:salmon");
/*  66 */     MOBS.add("minecraft:shulker");
/*  67 */     MOBS.add("minecraft:silverfish");
/*  68 */     MOBS.add("minecraft:skeleton");
/*  69 */     MOBS.add("minecraft:slime");
/*  70 */     MOBS.add("minecraft:snow_golem");
/*  71 */     MOBS.add("minecraft:spider");
/*  72 */     MOBS.add("minecraft:squid");
/*  73 */     MOBS.add("minecraft:stray");
/*  74 */     MOBS.add("minecraft:tropical_fish");
/*  75 */     MOBS.add("minecraft:vex");
/*  76 */     MOBS.add("minecraft:villager");
/*  77 */     MOBS.add("minecraft:iron_golem");
/*  78 */     MOBS.add("minecraft:vindicator");
/*  79 */     MOBS.add("minecraft:pillager");
/*  80 */     MOBS.add("minecraft:wandering_trader");
/*  81 */     MOBS.add("minecraft:witch");
/*  82 */     MOBS.add("minecraft:wither");
/*  83 */     MOBS.add("minecraft:wither_skeleton");
/*  84 */     MOBS.add("minecraft:zombie");
/*  85 */     MOBS.add("minecraft:zombie_villager");
/*  86 */     MOBS.add("minecraft:phantom");
/*  87 */     MOBS.add("minecraft:ravager");
/*  88 */     MOBS.add("minecraft:piglin");
/*  89 */     LIVING_ENTITIES.add("minecraft:armor_stand");
/*  90 */     PROJECTILES.add("minecraft:arrow");
/*  91 */     PROJECTILES.add("minecraft:dragon_fireball");
/*  92 */     PROJECTILES.add("minecraft:firework_rocket");
/*  93 */     PROJECTILES.add("minecraft:fireball");
/*  94 */     PROJECTILES.add("minecraft:llama_spit");
/*  95 */     PROJECTILES.add("minecraft:small_fireball");
/*  96 */     PROJECTILES.add("minecraft:snowball");
/*  97 */     PROJECTILES.add("minecraft:spectral_arrow");
/*  98 */     PROJECTILES.add("minecraft:egg");
/*  99 */     PROJECTILES.add("minecraft:ender_pearl");
/* 100 */     PROJECTILES.add("minecraft:experience_bottle");
/* 101 */     PROJECTILES.add("minecraft:potion");
/* 102 */     PROJECTILES.add("minecraft:trident");
/* 103 */     PROJECTILES.add("minecraft:wither_skull");
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityUUIDFix(Schema debug1) {
/* 108 */     super(debug1, References.ENTITY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/* 113 */     return fixTypeEverywhereTyped("EntityUUIDFixes", getInputSchema().getType(this.typeReference), debug1 -> {
/*     */           debug1 = debug1.update(DSL.remainderFinder(), EntityUUIDFix::updateEntityUUID);
/*     */           for (String debug3 : ABSTRACT_HORSES) {
/*     */             debug1 = updateNamedChoice(debug1, debug3, EntityUUIDFix::updateAnimalOwner);
/*     */           }
/*     */           for (String debug3 : TAMEABLE_ANIMALS) {
/*     */             debug1 = updateNamedChoice(debug1, debug3, EntityUUIDFix::updateAnimalOwner);
/*     */           }
/*     */           for (String debug3 : ANIMALS) {
/*     */             debug1 = updateNamedChoice(debug1, debug3, EntityUUIDFix::updateAnimal);
/*     */           }
/*     */           for (String debug3 : MOBS) {
/*     */             debug1 = updateNamedChoice(debug1, debug3, EntityUUIDFix::updateMob);
/*     */           }
/*     */           for (String debug3 : LIVING_ENTITIES) {
/*     */             debug1 = updateNamedChoice(debug1, debug3, EntityUUIDFix::updateLivingEntity);
/*     */           }
/*     */           for (String debug3 : PROJECTILES) {
/*     */             debug1 = updateNamedChoice(debug1, debug3, EntityUUIDFix::updateProjectile);
/*     */           }
/*     */           debug1 = updateNamedChoice(debug1, "minecraft:bee", EntityUUIDFix::updateHurtBy);
/*     */           debug1 = updateNamedChoice(debug1, "minecraft:zombified_piglin", EntityUUIDFix::updateHurtBy);
/*     */           debug1 = updateNamedChoice(debug1, "minecraft:fox", EntityUUIDFix::updateFox);
/*     */           debug1 = updateNamedChoice(debug1, "minecraft:item", EntityUUIDFix::updateItem);
/*     */           debug1 = updateNamedChoice(debug1, "minecraft:shulker_bullet", EntityUUIDFix::updateShulkerBullet);
/*     */           debug1 = updateNamedChoice(debug1, "minecraft:area_effect_cloud", EntityUUIDFix::updateAreaEffectCloud);
/*     */           debug1 = updateNamedChoice(debug1, "minecraft:zombie_villager", EntityUUIDFix::updateZombieVillager);
/*     */           debug1 = updateNamedChoice(debug1, "minecraft:evoker_fangs", EntityUUIDFix::updateEvokerFangs);
/*     */           return updateNamedChoice(debug1, "minecraft:piglin", EntityUUIDFix::updatePiglin);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static Dynamic<?> updatePiglin(Dynamic<?> debug0) {
/* 147 */     return debug0.update("Brain", debug0 -> debug0.update("memories", ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> updateEvokerFangs(Dynamic<?> debug0) {
/* 158 */     return replaceUUIDLeastMost(debug0, "OwnerUUID", "Owner").orElse(debug0);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateZombieVillager(Dynamic<?> debug0) {
/* 162 */     return replaceUUIDLeastMost(debug0, "ConversionPlayer", "ConversionPlayer").orElse(debug0);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateAreaEffectCloud(Dynamic<?> debug0) {
/* 166 */     return replaceUUIDLeastMost(debug0, "OwnerUUID", "Owner").orElse(debug0);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateShulkerBullet(Dynamic<?> debug0) {
/* 170 */     debug0 = replaceUUIDMLTag(debug0, "Owner", "Owner").orElse(debug0);
/* 171 */     return replaceUUIDMLTag(debug0, "Target", "Target").orElse(debug0);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateItem(Dynamic<?> debug0) {
/* 175 */     debug0 = replaceUUIDMLTag(debug0, "Owner", "Owner").orElse(debug0);
/* 176 */     return replaceUUIDMLTag(debug0, "Thrower", "Thrower").orElse(debug0);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateFox(Dynamic<?> debug0) {
/* 180 */     Optional<Dynamic<?>> debug1 = debug0.get("TrustedUUIDs").result().map(debug1 -> debug0.createList(debug1.asStream().map(())));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 188 */     return (Dynamic)DataFixUtils.orElse(debug1.map(debug1 -> debug0.remove("TrustedUUIDs").set("Trusted", debug1)), debug0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Dynamic<?> updateHurtBy(Dynamic<?> debug0) {
/* 194 */     return replaceUUIDString(debug0, "HurtBy", "HurtBy").orElse(debug0);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateAnimalOwner(Dynamic<?> debug0) {
/* 198 */     Dynamic<?> debug1 = updateAnimal(debug0);
/* 199 */     return replaceUUIDString(debug1, "OwnerUUID", "Owner").orElse(debug1);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateAnimal(Dynamic<?> debug0) {
/* 203 */     Dynamic<?> debug1 = updateMob(debug0);
/* 204 */     return replaceUUIDLeastMost(debug1, "LoveCause", "LoveCause").orElse(debug1);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> updateMob(Dynamic<?> debug0) {
/* 208 */     return updateLivingEntity(debug0).update("Leash", debug0 -> (Dynamic)replaceUUIDLeastMost(debug0, "UUID", "UUID").orElse(debug0));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Dynamic<?> updateLivingEntity(Dynamic<?> debug0) {
/* 214 */     return debug0.update("Attributes", debug1 -> debug0.createList(debug1.asStream().map(())));
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
/*     */   private static Dynamic<?> updateProjectile(Dynamic<?> debug0) {
/* 226 */     return (Dynamic)DataFixUtils.orElse(debug0.get("OwnerUUID").result().map(debug1 -> debug0.remove("OwnerUUID").set("Owner", debug1)), debug0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Dynamic<?> updateEntityUUID(Dynamic<?> debug0) {
/* 232 */     return replaceUUIDLeastMost(debug0, "UUID", "UUID").orElse(debug0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityUUIDFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */