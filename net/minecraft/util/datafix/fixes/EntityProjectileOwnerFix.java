/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import java.util.Arrays;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ 
/*     */ public class EntityProjectileOwnerFix
/*     */   extends DataFix
/*     */ {
/*     */   public EntityProjectileOwnerFix(Schema debug1) {
/*  19 */     super(debug1, false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  24 */     Schema debug1 = getInputSchema();
/*  25 */     return fixTypeEverywhereTyped("EntityProjectileOwner", debug1.getType(References.ENTITY), this::updateProjectiles);
/*     */   }
/*     */   
/*     */   private Typed<?> updateProjectiles(Typed<?> debug1) {
/*  29 */     debug1 = updateEntity(debug1, "minecraft:egg", this::updateOwnerThrowable);
/*  30 */     debug1 = updateEntity(debug1, "minecraft:ender_pearl", this::updateOwnerThrowable);
/*  31 */     debug1 = updateEntity(debug1, "minecraft:experience_bottle", this::updateOwnerThrowable);
/*  32 */     debug1 = updateEntity(debug1, "minecraft:snowball", this::updateOwnerThrowable);
/*  33 */     debug1 = updateEntity(debug1, "minecraft:potion", this::updateOwnerThrowable);
/*  34 */     debug1 = updateEntity(debug1, "minecraft:potion", this::updateItemPotion);
/*  35 */     debug1 = updateEntity(debug1, "minecraft:llama_spit", this::updateOwnerLlamaSpit);
/*  36 */     debug1 = updateEntity(debug1, "minecraft:arrow", this::updateOwnerArrow);
/*  37 */     debug1 = updateEntity(debug1, "minecraft:spectral_arrow", this::updateOwnerArrow);
/*  38 */     debug1 = updateEntity(debug1, "minecraft:trident", this::updateOwnerArrow);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  48 */     return debug1;
/*     */   }
/*     */   
/*     */   private Dynamic<?> updateOwnerArrow(Dynamic<?> debug1) {
/*  52 */     long debug2 = debug1.get("OwnerUUIDMost").asLong(0L);
/*  53 */     long debug4 = debug1.get("OwnerUUIDLeast").asLong(0L);
/*     */     
/*  55 */     return setUUID(debug1, debug2, debug4).remove("OwnerUUIDMost").remove("OwnerUUIDLeast");
/*     */   }
/*     */   
/*     */   private Dynamic<?> updateOwnerLlamaSpit(Dynamic<?> debug1) {
/*  59 */     OptionalDynamic<?> debug2 = debug1.get("Owner");
/*  60 */     long debug3 = debug2.get("OwnerUUIDMost").asLong(0L);
/*  61 */     long debug5 = debug2.get("OwnerUUIDLeast").asLong(0L);
/*     */     
/*  63 */     return setUUID(debug1, debug3, debug5).remove("Owner");
/*     */   }
/*     */   
/*     */   private Dynamic<?> updateItemPotion(Dynamic<?> debug1) {
/*  67 */     OptionalDynamic<?> debug2 = debug1.get("Potion");
/*  68 */     return debug1.set("Item", debug2.orElseEmptyMap()).remove("Potion");
/*     */   }
/*     */   
/*     */   private Dynamic<?> updateOwnerThrowable(Dynamic<?> debug1) {
/*  72 */     String debug2 = "owner";
/*  73 */     OptionalDynamic<?> debug3 = debug1.get("owner");
/*  74 */     long debug4 = debug3.get("M").asLong(0L);
/*  75 */     long debug6 = debug3.get("L").asLong(0L);
/*     */     
/*  77 */     return setUUID(debug1, debug4, debug6).remove("owner");
/*     */   }
/*     */   
/*     */   private Dynamic<?> setUUID(Dynamic<?> debug1, long debug2, long debug4) {
/*  81 */     String debug6 = "OwnerUUID";
/*  82 */     if (debug2 != 0L && debug4 != 0L) {
/*  83 */       return debug1.set("OwnerUUID", debug1.createIntList(Arrays.stream(createUUIDArray(debug2, debug4))));
/*     */     }
/*  85 */     return debug1;
/*     */   }
/*     */   
/*     */   private static int[] createUUIDArray(long debug0, long debug2) {
/*  89 */     return new int[] { (int)(debug0 >> 32L), (int)debug0, (int)(debug2 >> 32L), (int)debug2 };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Typed<?> updateEntity(Typed<?> debug1, String debug2, Function<Dynamic<?>, Dynamic<?>> debug3) {
/*  98 */     Type<?> debug4 = getInputSchema().getChoiceType(References.ENTITY, debug2);
/*  99 */     Type<?> debug5 = getOutputSchema().getChoiceType(References.ENTITY, debug2);
/* 100 */     return debug1.updateTyped(DSL.namedChoice(debug2, debug4), debug5, debug1 -> debug1.update(DSL.remainderFinder(), debug0));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityProjectileOwnerFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */