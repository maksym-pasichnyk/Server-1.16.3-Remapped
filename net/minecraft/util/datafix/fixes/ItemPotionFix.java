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
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ public class ItemPotionFix
/*     */   extends DataFix
/*     */ {
/*     */   private static final String[] POTIONS;
/*     */   
/*     */   public ItemPotionFix(Schema debug1, boolean debug2) {
/*  23 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   static {
/*  27 */     POTIONS = (String[])DataFixUtils.make(new String[128], debug0 -> {
/*     */           debug0[0] = "minecraft:water";
/*     */           debug0[1] = "minecraft:regeneration";
/*     */           debug0[2] = "minecraft:swiftness";
/*     */           debug0[3] = "minecraft:fire_resistance";
/*     */           debug0[4] = "minecraft:poison";
/*     */           debug0[5] = "minecraft:healing";
/*     */           debug0[6] = "minecraft:night_vision";
/*     */           debug0[7] = null;
/*     */           debug0[8] = "minecraft:weakness";
/*     */           debug0[9] = "minecraft:strength";
/*     */           debug0[10] = "minecraft:slowness";
/*     */           debug0[11] = "minecraft:leaping";
/*     */           debug0[12] = "minecraft:harming";
/*     */           debug0[13] = "minecraft:water_breathing";
/*     */           debug0[14] = "minecraft:invisibility";
/*     */           debug0[15] = null;
/*     */           debug0[16] = "minecraft:awkward";
/*     */           debug0[17] = "minecraft:regeneration";
/*     */           debug0[18] = "minecraft:swiftness";
/*     */           debug0[19] = "minecraft:fire_resistance";
/*     */           debug0[20] = "minecraft:poison";
/*     */           debug0[21] = "minecraft:healing";
/*     */           debug0[22] = "minecraft:night_vision";
/*     */           debug0[23] = null;
/*     */           debug0[24] = "minecraft:weakness";
/*     */           debug0[25] = "minecraft:strength";
/*     */           debug0[26] = "minecraft:slowness";
/*     */           debug0[27] = "minecraft:leaping";
/*     */           debug0[28] = "minecraft:harming";
/*     */           debug0[29] = "minecraft:water_breathing";
/*     */           debug0[30] = "minecraft:invisibility";
/*     */           debug0[31] = null;
/*     */           debug0[32] = "minecraft:thick";
/*     */           debug0[33] = "minecraft:strong_regeneration";
/*     */           debug0[34] = "minecraft:strong_swiftness";
/*     */           debug0[35] = "minecraft:fire_resistance";
/*     */           debug0[36] = "minecraft:strong_poison";
/*     */           debug0[37] = "minecraft:strong_healing";
/*     */           debug0[38] = "minecraft:night_vision";
/*     */           debug0[39] = null;
/*     */           debug0[40] = "minecraft:weakness";
/*     */           debug0[41] = "minecraft:strong_strength";
/*     */           debug0[42] = "minecraft:slowness";
/*     */           debug0[43] = "minecraft:strong_leaping";
/*     */           debug0[44] = "minecraft:strong_harming";
/*     */           debug0[45] = "minecraft:water_breathing";
/*     */           debug0[46] = "minecraft:invisibility";
/*     */           debug0[47] = null;
/*     */           debug0[48] = null;
/*     */           debug0[49] = "minecraft:strong_regeneration";
/*     */           debug0[50] = "minecraft:strong_swiftness";
/*     */           debug0[51] = "minecraft:fire_resistance";
/*     */           debug0[52] = "minecraft:strong_poison";
/*     */           debug0[53] = "minecraft:strong_healing";
/*     */           debug0[54] = "minecraft:night_vision";
/*     */           debug0[55] = null;
/*     */           debug0[56] = "minecraft:weakness";
/*     */           debug0[57] = "minecraft:strong_strength";
/*     */           debug0[58] = "minecraft:slowness";
/*     */           debug0[59] = "minecraft:strong_leaping";
/*     */           debug0[60] = "minecraft:strong_harming";
/*     */           debug0[61] = "minecraft:water_breathing";
/*     */           debug0[62] = "minecraft:invisibility";
/*     */           debug0[63] = null;
/*     */           debug0[64] = "minecraft:mundane";
/*     */           debug0[65] = "minecraft:long_regeneration";
/*     */           debug0[66] = "minecraft:long_swiftness";
/*     */           debug0[67] = "minecraft:long_fire_resistance";
/*     */           debug0[68] = "minecraft:long_poison";
/*     */           debug0[69] = "minecraft:healing";
/*     */           debug0[70] = "minecraft:long_night_vision";
/*     */           debug0[71] = null;
/*     */           debug0[72] = "minecraft:long_weakness";
/*     */           debug0[73] = "minecraft:long_strength";
/*     */           debug0[74] = "minecraft:long_slowness";
/*     */           debug0[75] = "minecraft:long_leaping";
/*     */           debug0[76] = "minecraft:harming";
/*     */           debug0[77] = "minecraft:long_water_breathing";
/*     */           debug0[78] = "minecraft:long_invisibility";
/*     */           debug0[79] = null;
/*     */           debug0[80] = "minecraft:awkward";
/*     */           debug0[81] = "minecraft:long_regeneration";
/*     */           debug0[82] = "minecraft:long_swiftness";
/*     */           debug0[83] = "minecraft:long_fire_resistance";
/*     */           debug0[84] = "minecraft:long_poison";
/*     */           debug0[85] = "minecraft:healing";
/*     */           debug0[86] = "minecraft:long_night_vision";
/*     */           debug0[87] = null;
/*     */           debug0[88] = "minecraft:long_weakness";
/*     */           debug0[89] = "minecraft:long_strength";
/*     */           debug0[90] = "minecraft:long_slowness";
/*     */           debug0[91] = "minecraft:long_leaping";
/*     */           debug0[92] = "minecraft:harming";
/*     */           debug0[93] = "minecraft:long_water_breathing";
/*     */           debug0[94] = "minecraft:long_invisibility";
/*     */           debug0[95] = null;
/*     */           debug0[96] = "minecraft:thick";
/*     */           debug0[97] = "minecraft:regeneration";
/*     */           debug0[98] = "minecraft:swiftness";
/*     */           debug0[99] = "minecraft:long_fire_resistance";
/*     */           debug0[100] = "minecraft:poison";
/*     */           debug0[101] = "minecraft:strong_healing";
/*     */           debug0[102] = "minecraft:long_night_vision";
/*     */           debug0[103] = null;
/*     */           debug0[104] = "minecraft:long_weakness";
/*     */           debug0[105] = "minecraft:strength";
/*     */           debug0[106] = "minecraft:long_slowness";
/*     */           debug0[107] = "minecraft:leaping";
/*     */           debug0[108] = "minecraft:strong_harming";
/*     */           debug0[109] = "minecraft:long_water_breathing";
/*     */           debug0[110] = "minecraft:long_invisibility";
/*     */           debug0[111] = null;
/*     */           debug0[112] = null;
/*     */           debug0[113] = "minecraft:regeneration";
/*     */           debug0[114] = "minecraft:swiftness";
/*     */           debug0[115] = "minecraft:long_fire_resistance";
/*     */           debug0[116] = "minecraft:poison";
/*     */           debug0[117] = "minecraft:strong_healing";
/*     */           debug0[118] = "minecraft:long_night_vision";
/*     */           debug0[119] = null;
/*     */           debug0[120] = "minecraft:long_weakness";
/*     */           debug0[121] = "minecraft:strength";
/*     */           debug0[122] = "minecraft:long_slowness";
/*     */           debug0[123] = "minecraft:leaping";
/*     */           debug0[124] = "minecraft:strong_harming";
/*     */           debug0[125] = "minecraft:long_water_breathing";
/*     */           debug0[126] = "minecraft:long_invisibility";
/*     */           debug0[127] = null;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/* 162 */     Type<?> debug1 = getInputSchema().getType(References.ITEM_STACK);
/* 163 */     OpticFinder<Pair<String, String>> debug2 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
/* 164 */     OpticFinder<?> debug3 = debug1.findField("tag");
/*     */     
/* 166 */     return fixTypeEverywhereTyped("ItemPotionFix", debug1, debug2 -> {
/*     */           Optional<Pair<String, String>> debug3 = debug2.getOptional(debug0);
/*     */           if (debug3.isPresent() && Objects.equals(((Pair)debug3.get()).getSecond(), "minecraft:potion")) {
/*     */             Dynamic<?> debug4 = (Dynamic)debug2.get(DSL.remainderFinder());
/*     */             Optional<? extends Typed<?>> debug5 = debug2.getOptionalTyped(debug1);
/*     */             short debug6 = debug4.get("Damage").asShort((short)0);
/*     */             if (debug5.isPresent()) {
/*     */               Typed<?> debug7 = debug2;
/*     */               Dynamic<?> debug8 = (Dynamic)((Typed)debug5.get()).get(DSL.remainderFinder());
/*     */               Optional<String> debug9 = debug8.get("Potion").asString().result();
/*     */               if (!debug9.isPresent()) {
/*     */                 String debug10 = POTIONS[debug6 & 0x7F];
/*     */                 Typed<?> debug11 = ((Typed)debug5.get()).set(DSL.remainderFinder(), debug8.set("Potion", debug8.createString((debug10 == null) ? "minecraft:water" : debug10)));
/*     */                 debug7 = debug7.set(debug1, debug11);
/*     */                 if ((debug6 & 0x4000) == 16384)
/*     */                   debug7 = debug7.set(debug0, Pair.of(References.ITEM_NAME.typeName(), "minecraft:splash_potion")); 
/*     */               } 
/*     */               if (debug6 != 0)
/*     */                 debug4 = debug4.set("Damage", debug4.createShort((short)0)); 
/*     */               return debug7.set(DSL.remainderFinder(), debug4);
/*     */             } 
/*     */           } 
/*     */           return debug2;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ItemPotionFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */