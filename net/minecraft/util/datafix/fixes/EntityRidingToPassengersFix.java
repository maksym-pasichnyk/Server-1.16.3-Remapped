/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class EntityRidingToPassengersFix extends DataFix {
/*    */   public EntityRidingToPassengersFix(Schema debug1, boolean debug2) {
/* 22 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 27 */     Schema debug1 = getInputSchema();
/* 28 */     Schema debug2 = getOutputSchema();
/*    */     
/* 30 */     Type<?> debug3 = debug1.getTypeRaw(References.ENTITY_TREE);
/* 31 */     Type<?> debug4 = debug2.getTypeRaw(References.ENTITY_TREE);
/* 32 */     Type<?> debug5 = debug1.getTypeRaw(References.ENTITY);
/*    */     
/* 34 */     return cap(debug1, debug2, debug3, debug4, debug5);
/*    */   }
/*    */   
/*    */   private <OldEntityTree, NewEntityTree, Entity> TypeRewriteRule cap(Schema debug1, Schema debug2, Type<OldEntityTree> debug3, Type<NewEntityTree> debug4, Type<Entity> debug5) {
/* 38 */     Type<Pair<String, Pair<Either<OldEntityTree, Unit>, Entity>>> debug6 = DSL.named(References.ENTITY_TREE.typeName(), DSL.and(
/* 39 */           DSL.optional((Type)DSL.field("Riding", debug3)), debug5));
/*    */ 
/*    */ 
/*    */     
/* 43 */     Type<Pair<String, Pair<Either<List<NewEntityTree>, Unit>, Entity>>> debug7 = DSL.named(References.ENTITY_TREE.typeName(), DSL.and(
/* 44 */           DSL.optional((Type)DSL.field("Passengers", (Type)DSL.list(debug4))), debug5));
/*    */ 
/*    */ 
/*    */     
/* 48 */     Type<?> debug8 = debug1.getType(References.ENTITY_TREE);
/* 49 */     Type<?> debug9 = debug2.getType(References.ENTITY_TREE);
/*    */     
/* 51 */     if (!Objects.equals(debug8, debug6)) {
/* 52 */       throw new IllegalStateException("Old entity type is not what was expected.");
/*    */     }
/*    */     
/* 55 */     if (!debug9.equals(debug7, true, true)) {
/* 56 */       throw new IllegalStateException("New entity type is not what was expected.");
/*    */     }
/*    */     
/* 59 */     OpticFinder<Pair<String, Pair<Either<OldEntityTree, Unit>, Entity>>> debug10 = DSL.typeFinder(debug6);
/* 60 */     OpticFinder<Pair<String, Pair<Either<List<NewEntityTree>, Unit>, Entity>>> debug11 = DSL.typeFinder(debug7);
/* 61 */     OpticFinder<NewEntityTree> debug12 = DSL.typeFinder(debug4);
/*    */     
/* 63 */     Type<?> debug13 = debug1.getType(References.PLAYER);
/* 64 */     Type<?> debug14 = debug2.getType(References.PLAYER);
/*    */     
/* 66 */     return TypeRewriteRule.seq(
/* 67 */         fixTypeEverywhere("EntityRidingToPassengerFix", debug6, debug7, debug5 -> ()), 
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
/* 90 */         writeAndRead("player RootVehicle injecter", debug13, debug14));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\EntityRidingToPassengersFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */