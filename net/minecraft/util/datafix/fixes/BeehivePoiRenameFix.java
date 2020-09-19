/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ 
/*    */ public class BeehivePoiRenameFix extends PoiTypeRename {
/*    */   public BeehivePoiRenameFix(Schema debug1) {
/*  7 */     super(debug1, false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String rename(String debug1) {
/* 12 */     return debug1.equals("minecraft:bee_hive") ? "minecraft:beehive" : debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\fixes\BeehivePoiRenameFix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */