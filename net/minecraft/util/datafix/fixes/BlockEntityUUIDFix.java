/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityUUIDFix extends AbstractUUIDFix {
/*    */   public BlockEntityUUIDFix(Schema debug1) {
/*  9 */     super(debug1, References.BLOCK_ENTITY);
/*    */   }
/*    */ 
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 14 */     return fixTypeEverywhereTyped("BlockEntityUUIDFix", getInputSchema().getType(this.typeReference), debug1 -> {
/*    */           debug1 = updateNamedChoice(debug1, "minecraft:conduit", this::updateConduit);
/*    */           return updateNamedChoice(debug1, "minecraft:skull", this::updateSkull);
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   private Dynamic<?> updateSkull(Dynamic<?> debug1) {
/* 22 */     return debug1.get("Owner").get().map(debug0 -> (Dynamic)replaceUUIDString(debug0, "Id", "Id").orElse(debug0))
/*    */       
/* 24 */       .map(debug1 -> debug0.remove("Owner").set("SkullOwner", debug1))
/*    */       
/* 26 */       .result().orElse(debug1);
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateConduit(Dynamic<?> debug1) {
/* 30 */     return replaceUUIDMLTag(debug1, "target_uuid", "Target").orElse(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityUUIDFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */