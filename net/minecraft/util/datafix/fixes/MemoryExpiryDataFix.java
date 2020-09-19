/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
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
/*    */ public class MemoryExpiryDataFix
/*    */   extends NamedEntityFix
/*    */ {
/*    */   public MemoryExpiryDataFix(Schema debug1, String debug2) {
/* 31 */     super(debug1, false, "Memory expiry data fix (" + debug2 + ")", References.ENTITY, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> debug1) {
/* 36 */     return debug1.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> debug1) {
/* 40 */     return debug1.update("Brain", this::updateBrain);
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateBrain(Dynamic<?> debug1) {
/* 44 */     return debug1.update("memories", this::updateMemories);
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateMemories(Dynamic<?> debug1) {
/* 48 */     return debug1.updateMapValues(this::updateMemoryEntry);
/*    */   }
/*    */   
/*    */   private Pair<Dynamic<?>, Dynamic<?>> updateMemoryEntry(Pair<Dynamic<?>, Dynamic<?>> debug1) {
/* 52 */     return debug1.mapSecond(this::wrapMemoryValue);
/*    */   }
/*    */   
/*    */   private Dynamic<?> wrapMemoryValue(Dynamic<?> debug1) {
/* 56 */     return debug1.createMap((Map)ImmutableMap.of(debug1
/* 57 */           .createString("value"), debug1));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\MemoryExpiryDataFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */