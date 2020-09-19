/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.HashMap;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ 
/*    */ public class EntityPaintingMotiveFix extends NamedEntityFix {
/*    */   public EntityPaintingMotiveFix(Schema debug1, boolean debug2) {
/* 17 */     super(debug1, debug2, "EntityPaintingMotiveFix", References.ENTITY, "minecraft:painting");
/*    */   } private static final Map<String, String> MAP;
/*    */   static {
/* 20 */     MAP = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), debug0 -> {
/*    */           debug0.put("donkeykong", "donkey_kong");
/*    */           debug0.put("burningskull", "burning_skull");
/*    */           debug0.put("skullandroses", "skull_and_roses");
/*    */         });
/*    */   }
/*    */   public Dynamic<?> fixTag(Dynamic<?> debug1) {
/* 27 */     Optional<String> debug2 = debug1.get("Motive").asString().result();
/* 28 */     if (debug2.isPresent()) {
/* 29 */       String debug3 = ((String)debug2.get()).toLowerCase(Locale.ROOT);
/* 30 */       return debug1.set("Motive", debug1.createString((new ResourceLocation(MAP.getOrDefault(debug3, debug3))).toString()));
/*    */     } 
/* 32 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 37 */     return debug1.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityPaintingMotiveFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */