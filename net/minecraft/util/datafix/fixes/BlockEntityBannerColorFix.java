/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class BlockEntityBannerColorFix extends NamedEntityFix {
/*    */   public BlockEntityBannerColorFix(Schema debug1, boolean debug2) {
/* 11 */     super(debug1, debug2, "BlockEntityBannerColorFix", References.BLOCK_ENTITY, "minecraft:banner");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> debug1) {
/* 15 */     debug1 = debug1.update("Base", debug0 -> debug0.createInt(15 - debug0.asInt(0)));
/*    */     
/* 17 */     debug1 = debug1.update("Patterns", debug0 -> (Dynamic)DataFixUtils.orElse(debug0.asStreamOpt().map(()).map(debug0::createList).result(), debug0));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 23 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 28 */     return debug1.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BlockEntityBannerColorFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */