/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class AttributesRename extends DataFix {
/* 17 */   private static final Map<String, String> RENAMES = (Map<String, String>)ImmutableMap.builder()
/* 18 */     .put("generic.maxHealth", "generic.max_health")
/* 19 */     .put("Max Health", "generic.max_health")
/*    */     
/* 21 */     .put("zombie.spawnReinforcements", "zombie.spawn_reinforcements")
/* 22 */     .put("Spawn Reinforcements Chance", "zombie.spawn_reinforcements")
/*    */     
/* 24 */     .put("horse.jumpStrength", "horse.jump_strength")
/* 25 */     .put("Jump Strength", "horse.jump_strength")
/*    */     
/* 27 */     .put("generic.followRange", "generic.follow_range")
/* 28 */     .put("Follow Range", "generic.follow_range")
/*    */     
/* 30 */     .put("generic.knockbackResistance", "generic.knockback_resistance")
/* 31 */     .put("Knockback Resistance", "generic.knockback_resistance")
/*    */     
/* 33 */     .put("generic.movementSpeed", "generic.movement_speed")
/* 34 */     .put("Movement Speed", "generic.movement_speed")
/*    */     
/* 36 */     .put("generic.flyingSpeed", "generic.flying_speed")
/* 37 */     .put("Flying Speed", "generic.flying_speed")
/*    */     
/* 39 */     .put("generic.attackDamage", "generic.attack_damage")
/* 40 */     .put("generic.attackKnockback", "generic.attack_knockback")
/* 41 */     .put("generic.attackSpeed", "generic.attack_speed")
/* 42 */     .put("generic.armorToughness", "generic.armor_toughness")
/* 43 */     .build();
/*    */   
/*    */   public AttributesRename(Schema debug1) {
/* 46 */     super(debug1, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 51 */     Type<?> debug1 = getInputSchema().getType(References.ITEM_STACK);
/* 52 */     OpticFinder<?> debug2 = debug1.findField("tag");
/* 53 */     return TypeRewriteRule.seq(
/* 54 */         fixTypeEverywhereTyped("Rename ItemStack Attributes", debug1, debug1 -> debug1.updateTyped(debug0, AttributesRename::fixItemStackTag)), new TypeRewriteRule[] {
/*    */ 
/*    */           
/* 57 */           fixTypeEverywhereTyped("Rename Entity Attributes", getInputSchema().getType(References.ENTITY), AttributesRename::fixEntity), 
/* 58 */           fixTypeEverywhereTyped("Rename Player Attributes", getInputSchema().getType(References.PLAYER), AttributesRename::fixEntity)
/*    */         });
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixName(Dynamic<?> debug0) {
/* 63 */     return (Dynamic)DataFixUtils.orElse(debug0.asString().result().map(debug0 -> (String)RENAMES.getOrDefault(debug0, debug0)).map(debug0::createString), debug0);
/*    */   }
/*    */   
/*    */   private static Typed<?> fixItemStackTag(Typed<?> debug0) {
/* 67 */     return debug0.update(DSL.remainderFinder(), debug0 -> debug0.update("AttributeModifiers", ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Typed<?> fixEntity(Typed<?> debug0) {
/* 75 */     return debug0.update(DSL.remainderFinder(), debug0 -> debug0.update("Attributes", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\AttributesRename.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */