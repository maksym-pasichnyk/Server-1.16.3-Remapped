/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class EntityHealthFix extends DataFix {
/*    */   public EntityHealthFix(Schema debug1, boolean debug2) {
/* 15 */     super(debug1, debug2);
/*    */   }
/*    */   
/* 18 */   private static final Set<String> ENTITIES = Sets.newHashSet((Object[])new String[] { "ArmorStand", "Bat", "Blaze", "CaveSpider", "Chicken", "Cow", "Creeper", "EnderDragon", "Enderman", "Endermite", "EntityHorse", "Ghast", "Giant", "Guardian", "LavaSlime", "MushroomCow", "Ozelot", "Pig", "PigZombie", "Rabbit", "Sheep", "Shulker", "Silverfish", "Skeleton", "Slime", "SnowMan", "Spider", "Squid", "Villager", "VillagerGolem", "Witch", "WitherBoss", "Wolf", "Zombie" });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> debug1) {
/*    */     float debug2;
/* 58 */     Optional<Number> debug3 = debug1.get("HealF").asNumber().result();
/* 59 */     Optional<Number> debug4 = debug1.get("Health").asNumber().result();
/* 60 */     if (debug3.isPresent()) {
/* 61 */       debug2 = ((Number)debug3.get()).floatValue();
/* 62 */       debug1 = debug1.remove("HealF");
/* 63 */     } else if (debug4.isPresent()) {
/* 64 */       debug2 = ((Number)debug4.get()).floatValue();
/*    */     } else {
/* 66 */       return debug1;
/*    */     } 
/* 68 */     return debug1.set("Health", debug1.createFloat(debug2));
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 73 */     return fixTypeEverywhereTyped("EntityHealthFix", getInputSchema().getType(References.ENTITY), debug1 -> debug1.update(DSL.remainderFinder(), this::fixTag));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityHealthFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */