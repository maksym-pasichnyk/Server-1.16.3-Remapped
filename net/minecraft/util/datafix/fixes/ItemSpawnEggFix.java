/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ public class ItemSpawnEggFix
/*     */   extends DataFix {
/*     */   public ItemSpawnEggFix(Schema debug1, boolean debug2) {
/*  21 */     super(debug1, debug2);
/*     */   } private static final String[] ID_TO_ENTITY;
/*     */   static {
/*  24 */     ID_TO_ENTITY = (String[])DataFixUtils.make(new String[256], debug0 -> {
/*     */           debug0[1] = "Item";
/*     */           debug0[2] = "XPOrb";
/*     */           debug0[7] = "ThrownEgg";
/*     */           debug0[8] = "LeashKnot";
/*     */           debug0[9] = "Painting";
/*     */           debug0[10] = "Arrow";
/*     */           debug0[11] = "Snowball";
/*     */           debug0[12] = "Fireball";
/*     */           debug0[13] = "SmallFireball";
/*     */           debug0[14] = "ThrownEnderpearl";
/*     */           debug0[15] = "EyeOfEnderSignal";
/*     */           debug0[16] = "ThrownPotion";
/*     */           debug0[17] = "ThrownExpBottle";
/*     */           debug0[18] = "ItemFrame";
/*     */           debug0[19] = "WitherSkull";
/*     */           debug0[20] = "PrimedTnt";
/*     */           debug0[21] = "FallingSand";
/*     */           debug0[22] = "FireworksRocketEntity";
/*     */           debug0[23] = "TippedArrow";
/*     */           debug0[24] = "SpectralArrow";
/*     */           debug0[25] = "ShulkerBullet";
/*     */           debug0[26] = "DragonFireball";
/*     */           debug0[30] = "ArmorStand";
/*     */           debug0[41] = "Boat";
/*     */           debug0[42] = "MinecartRideable";
/*     */           debug0[43] = "MinecartChest";
/*     */           debug0[44] = "MinecartFurnace";
/*     */           debug0[45] = "MinecartTNT";
/*     */           debug0[46] = "MinecartHopper";
/*     */           debug0[47] = "MinecartSpawner";
/*     */           debug0[40] = "MinecartCommandBlock";
/*     */           debug0[48] = "Mob";
/*     */           debug0[49] = "Monster";
/*     */           debug0[50] = "Creeper";
/*     */           debug0[51] = "Skeleton";
/*     */           debug0[52] = "Spider";
/*     */           debug0[53] = "Giant";
/*     */           debug0[54] = "Zombie";
/*     */           debug0[55] = "Slime";
/*     */           debug0[56] = "Ghast";
/*     */           debug0[57] = "PigZombie";
/*     */           debug0[58] = "Enderman";
/*     */           debug0[59] = "CaveSpider";
/*     */           debug0[60] = "Silverfish";
/*     */           debug0[61] = "Blaze";
/*     */           debug0[62] = "LavaSlime";
/*     */           debug0[63] = "EnderDragon";
/*     */           debug0[64] = "WitherBoss";
/*     */           debug0[65] = "Bat";
/*     */           debug0[66] = "Witch";
/*     */           debug0[67] = "Endermite";
/*     */           debug0[68] = "Guardian";
/*     */           debug0[69] = "Shulker";
/*     */           debug0[90] = "Pig";
/*     */           debug0[91] = "Sheep";
/*     */           debug0[92] = "Cow";
/*     */           debug0[93] = "Chicken";
/*     */           debug0[94] = "Squid";
/*     */           debug0[95] = "Wolf";
/*     */           debug0[96] = "MushroomCow";
/*     */           debug0[97] = "SnowMan";
/*     */           debug0[98] = "Ozelot";
/*     */           debug0[99] = "VillagerGolem";
/*     */           debug0[100] = "EntityHorse";
/*     */           debug0[101] = "Rabbit";
/*     */           debug0[120] = "Villager";
/*     */           debug0[200] = "EnderCrystal";
/*     */         });
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
/*     */   public TypeRewriteRule makeRule() {
/* 107 */     Schema debug1 = getInputSchema();
/* 108 */     Type<?> debug2 = debug1.getType(References.ITEM_STACK);
/*     */     
/* 110 */     OpticFinder<Pair<String, String>> debug3 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 111 */     OpticFinder<String> debug4 = DSL.fieldFinder("id", DSL.string());
/* 112 */     OpticFinder<?> debug5 = debug2.findField("tag");
/* 113 */     OpticFinder<?> debug6 = debug5.type().findField("EntityTag");
/* 114 */     OpticFinder<?> debug7 = DSL.typeFinder(debug1.getTypeRaw(References.ENTITY));
/*     */     
/* 116 */     Type<?> debug8 = getOutputSchema().getTypeRaw(References.ENTITY);
/*     */     
/* 118 */     return fixTypeEverywhereTyped("ItemSpawnEggFix", debug2, debug6 -> {
/*     */           Optional<Pair<String, String>> debug7 = debug6.getOptional(debug0);
/*     */           if (debug7.isPresent() && Objects.equals(((Pair)debug7.get()).getSecond(), "minecraft:spawn_egg")) {
/*     */             Dynamic<?> debug8 = (Dynamic)debug6.get(DSL.remainderFinder());
/*     */             short debug9 = debug8.get("Damage").asShort((short)0);
/*     */             Optional<? extends Typed<?>> debug10 = debug6.getOptionalTyped(debug1);
/*     */             Optional<? extends Typed<?>> debug11 = debug10.flatMap(());
/*     */             Optional<? extends Typed<?>> debug12 = debug11.flatMap(());
/*     */             Optional<String> debug13 = debug12.flatMap(());
/*     */             Typed<?> debug14 = debug6;
/*     */             String debug15 = ID_TO_ENTITY[debug9 & 0xFF];
/*     */             if (debug15 != null && (!debug13.isPresent() || !Objects.equals(debug13.get(), debug15))) {
/*     */               Typed<?> debug16 = debug6.getOrCreateTyped(debug1);
/*     */               Typed<?> debug17 = debug16.getOrCreateTyped(debug2);
/*     */               Typed<?> debug18 = debug17.getOrCreateTyped(debug3);
/*     */               Dynamic<?> debug19 = debug8;
/*     */               Typed<?> debug20 = (Typed)((Pair)debug18.write().flatMap(()).result().orElseThrow(())).getFirst();
/*     */               debug14 = debug14.set(debug1, debug16.set(debug2, debug17.set(debug3, debug20)));
/*     */             } 
/*     */             if (debug9 != 0) {
/*     */               debug8 = debug8.set("Damage", debug8.createShort((short)0));
/*     */               debug14 = debug14.set(DSL.remainderFinder(), debug8);
/*     */             } 
/*     */             return debug14;
/*     */           } 
/*     */           return debug6;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemSpawnEggFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */