/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V1451_4
/*    */   extends NamespacedSchema
/*    */ {
/*    */   public V1451_4(int debug1, Schema debug2) {
/* 14 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void registerTypes(Schema debug1, Map<String, Supplier<TypeTemplate>> debug2, Map<String, Supplier<TypeTemplate>> debug3) {
/* 19 */     super.registerTypes(debug1, debug2, debug3);
/*    */     
/* 21 */     debug1.registerType(false, References.BLOCK_NAME, () -> DSL.constType(namespacedString()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\schemas\V1451_4.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */