/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.FieldFinder;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.CompoundList;
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
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
/*     */ public class MissingDimensionFix
/*     */   extends DataFix
/*     */ {
/*     */   public MissingDimensionFix(Schema debug1, boolean debug2) {
/*  36 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   private static <A> Type<Pair<A, Dynamic<?>>> fields(String debug0, Type<A> debug1) {
/*  40 */     return DSL.and((Type)DSL.field(debug0, debug1), DSL.remainderType());
/*     */   }
/*     */   
/*     */   private static <A> Type<Pair<Either<A, Unit>, Dynamic<?>>> optionalFields(String debug0, Type<A> debug1) {
/*  44 */     return DSL.and(DSL.optional((Type)DSL.field(debug0, debug1)), DSL.remainderType());
/*     */   }
/*     */   
/*     */   private static <A1, A2> Type<Pair<Either<A1, Unit>, Pair<Either<A2, Unit>, Dynamic<?>>>> optionalFields(String debug0, Type<A1> debug1, String debug2, Type<A2> debug3) {
/*  48 */     return DSL.and(
/*  49 */         DSL.optional((Type)DSL.field(debug0, debug1)), 
/*  50 */         DSL.optional((Type)DSL.field(debug2, debug3)), 
/*  51 */         DSL.remainderType());
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypeRewriteRule makeRule() {
/*  56 */     Schema debug1 = getInputSchema();
/*  57 */     TaggedChoice.TaggedChoiceType<String> debug2 = new TaggedChoice.TaggedChoiceType("type", DSL.string(), (Map)ImmutableMap.of("minecraft:debug", 
/*  58 */           DSL.remainderType(), "minecraft:flat", 
/*  59 */           optionalFields("settings", optionalFields("biome", debug1
/*  60 */               .getType(References.BIOME), "layers", 
/*  61 */               (Type<?>)DSL.list(
/*  62 */                 optionalFields("block", debug1
/*  63 */                   .getType(References.BLOCK_NAME))))), "minecraft:noise", 
/*     */ 
/*     */ 
/*     */           
/*  67 */           optionalFields("biome_source", 
/*  68 */             DSL.taggedChoiceType("type", DSL.string(), (Map)ImmutableMap.of("minecraft:fixed", 
/*  69 */                 fields("biome", debug1.getType(References.BIOME)), "minecraft:multi_noise", 
/*  70 */                 DSL.list(fields("biome", debug1.getType(References.BIOME))), "minecraft:checkerboard", 
/*  71 */                 fields("biomes", (Type<?>)DSL.list(debug1.getType(References.BIOME))), "minecraft:vanilla_layered", 
/*  72 */                 DSL.remainderType(), "minecraft:the_end", 
/*  73 */                 DSL.remainderType())), "settings", 
/*     */             
/*  75 */             DSL.or(DSL.string(), optionalFields("default_block", debug1
/*  76 */                 .getType(References.BLOCK_NAME), "default_fluid", debug1
/*  77 */                 .getType(References.BLOCK_NAME))))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  82 */     CompoundList.CompoundListType<String, ?> debug3 = DSL.compoundList(NamespacedSchema.namespacedString(), fields("generator", (Type<String>)debug2));
/*  83 */     Type<?> debug4 = DSL.and((Type)debug3, DSL.remainderType());
/*     */     
/*  85 */     Type<?> debug5 = debug1.getType(References.WORLD_GEN_SETTINGS);
/*     */     
/*  87 */     FieldFinder<?> debug6 = new FieldFinder("dimensions", debug4);
/*  88 */     if (!debug5.findFieldType("dimensions").equals(debug4)) {
/*  89 */       throw new IllegalStateException();
/*     */     }
/*  91 */     OpticFinder<? extends List<? extends Pair<String, ?>>> debug7 = debug3.finder();
/*  92 */     return fixTypeEverywhereTyped("MissingDimensionFix", debug5, debug4 -> debug4.updateTyped((OpticFinder)debug1, ()));
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
/*     */   private <T> Dynamic<T> recreateSettings(Dynamic<T> debug1) {
/* 107 */     long debug2 = debug1.get("seed").asLong(0L);
/* 108 */     return new Dynamic(debug1.getOps(), WorldGenSettingsFix.vanillaLevels(debug1, debug2, WorldGenSettingsFix.defaultOverworld(debug1, debug2), false));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\MissingDimensionFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */