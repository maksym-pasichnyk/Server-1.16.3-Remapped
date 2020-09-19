/*    */ package net.minecraft.util.datafix;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public enum DataFixTypes {
/*  7 */   LEVEL(References.LEVEL),
/*  8 */   PLAYER(References.PLAYER),
/*  9 */   CHUNK(References.CHUNK),
/* 10 */   HOTBAR(References.HOTBAR),
/* 11 */   OPTIONS(References.OPTIONS),
/* 12 */   STRUCTURE(References.STRUCTURE),
/* 13 */   STATS(References.STATS),
/* 14 */   SAVED_DATA(References.SAVED_DATA),
/* 15 */   ADVANCEMENTS(References.ADVANCEMENTS),
/* 16 */   POI_CHUNK(References.POI_CHUNK),
/* 17 */   WORLD_GEN_SETTINGS(References.WORLD_GEN_SETTINGS);
/*    */   
/*    */   private final DSL.TypeReference type;
/*    */ 
/*    */   
/*    */   DataFixTypes(DSL.TypeReference debug3) {
/* 23 */     this.type = debug3;
/*    */   }
/*    */   
/*    */   public DSL.TypeReference getType() {
/* 27 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\DataFixTypes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */