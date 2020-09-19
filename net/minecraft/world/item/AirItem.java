/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AirItem
/*    */   extends Item
/*    */ {
/*    */   private final Block block;
/*    */   
/*    */   public AirItem(Block debug1, Item.Properties debug2) {
/* 14 */     super(debug2);
/* 15 */     this.block = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescriptionId() {
/* 20 */     return this.block.getDescriptionId();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\AirItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */