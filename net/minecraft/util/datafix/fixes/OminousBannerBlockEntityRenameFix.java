/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class OminousBannerBlockEntityRenameFix
/*    */   extends NamedEntityFix {
/*    */   public OminousBannerBlockEntityRenameFix(Schema debug1, boolean debug2) {
/* 12 */     super(debug1, debug2, "OminousBannerBlockEntityRenameFix", References.BLOCK_ENTITY, "minecraft:banner");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 17 */     return debug1.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixTag(Dynamic<?> debug1) {
/* 21 */     Optional<String> debug2 = debug1.get("CustomName").asString().result();
/* 22 */     if (debug2.isPresent()) {
/* 23 */       String debug3 = debug2.get();
/* 24 */       debug3 = debug3.replace("\"translate\":\"block.minecraft.illager_banner\"", "\"translate\":\"block.minecraft.ominous_banner\"");
/* 25 */       return debug1.set("CustomName", debug1.createString(debug3));
/*    */     } 
/* 27 */     return debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\OminousBannerBlockEntityRenameFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */