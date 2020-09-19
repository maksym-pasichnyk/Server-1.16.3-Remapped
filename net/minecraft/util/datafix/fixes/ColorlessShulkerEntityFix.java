/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class ColorlessShulkerEntityFix extends NamedEntityFix {
/*    */   public ColorlessShulkerEntityFix(Schema debug1, boolean debug2) {
/*  9 */     super(debug1, debug2, "Colorless shulker entity fix", References.ENTITY, "minecraft:shulker");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 14 */     return debug1.update(DSL.remainderFinder(), debug0 -> (debug0.get("Color").asInt(0) == 10) ? debug0.set("Color", debug0.createByte((byte)16)) : debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\ColorlessShulkerEntityFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */