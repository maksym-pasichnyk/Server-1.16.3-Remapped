/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.templates.List;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class VillagerRebuildLevelAndXpFix
/*    */   extends DataFix
/*    */ {
/* 18 */   private static final int[] LEVEL_XP_THRESHOLDS = new int[] { 0, 10, 50, 100, 150 };
/*    */   
/*    */   public static int getMinXpPerLevel(int debug0) {
/* 21 */     return LEVEL_XP_THRESHOLDS[Mth.clamp(debug0 - 1, 0, LEVEL_XP_THRESHOLDS.length - 1)];
/*    */   }
/*    */   
/*    */   public VillagerRebuildLevelAndXpFix(Schema debug1, boolean debug2) {
/* 25 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 30 */     Type<?> debug1 = getInputSchema().getChoiceType(References.ENTITY, "minecraft:villager");
/* 31 */     OpticFinder<?> debug2 = DSL.namedChoice("minecraft:villager", debug1);
/*    */     
/* 33 */     OpticFinder<?> debug3 = debug1.findField("Offers");
/* 34 */     Type<?> debug4 = debug3.type();
/* 35 */     OpticFinder<?> debug5 = debug4.findField("Recipes");
/* 36 */     List.ListType<?> debug6 = (List.ListType)debug5.type();
/* 37 */     OpticFinder<?> debug7 = debug6.getElement().finder();
/*    */     
/* 39 */     return fixTypeEverywhereTyped("Villager level and xp rebuild", getInputSchema().getType(References.ENTITY), debug5 -> debug5.updateTyped(debug0, debug1, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Typed<?> addLevel(Typed<?> debug0, int debug1) {
/* 72 */     return debug0.update(DSL.remainderFinder(), debug1 -> debug1.update("VillagerData", ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Typed<?> addXpFromLevel(Typed<?> debug0, int debug1) {
/* 79 */     int debug2 = getMinXpPerLevel(debug1);
/* 80 */     return debug0.update(DSL.remainderFinder(), debug1 -> debug1.set("Xp", debug1.createInt(debug0)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\VillagerRebuildLevelAndXpFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */