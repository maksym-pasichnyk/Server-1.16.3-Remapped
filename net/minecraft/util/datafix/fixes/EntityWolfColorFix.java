/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityWolfColorFix extends NamedEntityFix {
/*    */   public EntityWolfColorFix(Schema debug1, boolean debug2) {
/* 10 */     super(debug1, debug2, "EntityWolfColorFix", References.ENTITY, "minecraft:wolf");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> debug1) {
/* 14 */     return debug1.update("CollarColor", debug0 -> debug0.createByte((byte)(15 - debug0.asInt(0))));
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 19 */     return debug1.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityWolfColorFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */