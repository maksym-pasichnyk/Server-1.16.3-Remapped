/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class V100
/*     */   extends Schema
/*     */ {
/*     */   public V100(int debug1, Schema debug2) {
/*  21 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   protected static TypeTemplate equipment(Schema debug0) {
/*  25 */     return DSL.optionalFields("ArmorItems", 
/*  26 */         DSL.list(References.ITEM_STACK.in(debug0)), "HandItems", 
/*  27 */         DSL.list(References.ITEM_STACK.in(debug0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void registerMob(Schema debug0, Map<String, Supplier<TypeTemplate>> debug1, String debug2) {
/*  32 */     debug0.register(debug1, debug2, () -> equipment(debug0));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema debug1) {
/*  37 */     Map<String, Supplier<TypeTemplate>> debug2 = super.registerEntities(debug1);
/*     */ 
/*     */     
/*  40 */     registerMob(debug1, debug2, "ArmorStand");
/*  41 */     registerMob(debug1, debug2, "Creeper");
/*  42 */     registerMob(debug1, debug2, "Skeleton");
/*  43 */     registerMob(debug1, debug2, "Spider");
/*  44 */     registerMob(debug1, debug2, "Giant");
/*  45 */     registerMob(debug1, debug2, "Zombie");
/*  46 */     registerMob(debug1, debug2, "Slime");
/*  47 */     registerMob(debug1, debug2, "Ghast");
/*  48 */     registerMob(debug1, debug2, "PigZombie");
/*  49 */     debug1.register(debug2, "Enderman", debug1 -> DSL.optionalFields("carried", References.BLOCK_NAME.in(debug0), equipment(debug0)));
/*     */ 
/*     */ 
/*     */     
/*  53 */     registerMob(debug1, debug2, "CaveSpider");
/*  54 */     registerMob(debug1, debug2, "Silverfish");
/*  55 */     registerMob(debug1, debug2, "Blaze");
/*  56 */     registerMob(debug1, debug2, "LavaSlime");
/*  57 */     registerMob(debug1, debug2, "EnderDragon");
/*  58 */     registerMob(debug1, debug2, "WitherBoss");
/*  59 */     registerMob(debug1, debug2, "Bat");
/*  60 */     registerMob(debug1, debug2, "Witch");
/*  61 */     registerMob(debug1, debug2, "Endermite");
/*  62 */     registerMob(debug1, debug2, "Guardian");
/*  63 */     registerMob(debug1, debug2, "Pig");
/*  64 */     registerMob(debug1, debug2, "Sheep");
/*  65 */     registerMob(debug1, debug2, "Cow");
/*  66 */     registerMob(debug1, debug2, "Chicken");
/*  67 */     registerMob(debug1, debug2, "Squid");
/*  68 */     registerMob(debug1, debug2, "Wolf");
/*  69 */     registerMob(debug1, debug2, "MushroomCow");
/*  70 */     registerMob(debug1, debug2, "SnowMan");
/*  71 */     registerMob(debug1, debug2, "Ozelot");
/*  72 */     registerMob(debug1, debug2, "VillagerGolem");
/*  73 */     debug1.register(debug2, "EntityHorse", debug1 -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(debug0)), "ArmorItem", References.ITEM_STACK.in(debug0), "SaddleItem", References.ITEM_STACK.in(debug0), equipment(debug0)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     registerMob(debug1, debug2, "Rabbit");
/*  80 */     debug1.register(debug2, "Villager", debug1 -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(debug0)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(debug0), "buyB", References.ITEM_STACK.in(debug0), "sell", References.ITEM_STACK.in(debug0)))), equipment(debug0)));
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
/*  93 */     registerMob(debug1, debug2, "Shulker");
/*     */ 
/*     */     
/*  96 */     debug1.registerSimple(debug2, "AreaEffectCloud");
/*  97 */     debug1.registerSimple(debug2, "ShulkerBullet");
/*     */     
/*  99 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void registerTypes(Schema debug1, Map<String, Supplier<TypeTemplate>> debug2, Map<String, Supplier<TypeTemplate>> debug3) {
/* 104 */     super.registerTypes(debug1, debug2, debug3);
/*     */     
/* 106 */     debug1.registerType(false, References.STRUCTURE, () -> DSL.optionalFields("entities", DSL.list(DSL.optionalFields("nbt", References.ENTITY_TREE.in(debug0))), "blocks", DSL.list(DSL.optionalFields("nbt", References.BLOCK_ENTITY.in(debug0))), "palette", DSL.list(References.BLOCK_STATE.in(debug0))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     debug1.registerType(false, References.BLOCK_STATE, DSL::remainder);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V100.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */