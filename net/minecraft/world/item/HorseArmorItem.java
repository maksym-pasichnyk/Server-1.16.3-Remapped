/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HorseArmorItem
/*    */   extends Item
/*    */ {
/*    */   private final int protection;
/*    */   private final String texture;
/*    */   
/*    */   public HorseArmorItem(int debug1, String debug2, Item.Properties debug3) {
/* 12 */     super(debug3);
/* 13 */     this.protection = debug1;
/* 14 */     this.texture = "textures/entity/horse/armor/horse_armor_" + debug2 + ".png";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getProtection() {
/* 22 */     return this.protection;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\HorseArmorItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */