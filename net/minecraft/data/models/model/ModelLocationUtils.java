/*    */ package net.minecraft.data.models.model;
/*    */ 
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ 
/*    */ public class ModelLocationUtils
/*    */ {
/*    */   @Deprecated
/*    */   public static ResourceLocation decorateBlockModelLocation(String debug0) {
/* 12 */     return new ResourceLocation("minecraft", "block/" + debug0);
/*    */   }
/*    */   
/*    */   public static ResourceLocation decorateItemModelLocation(String debug0) {
/* 16 */     return new ResourceLocation("minecraft", "item/" + debug0);
/*    */   }
/*    */   
/*    */   public static ResourceLocation getModelLocation(Block debug0, String debug1) {
/* 20 */     ResourceLocation debug2 = Registry.BLOCK.getKey(debug0);
/* 21 */     return new ResourceLocation(debug2.getNamespace(), "block/" + debug2.getPath() + debug1);
/*    */   }
/*    */   
/*    */   public static ResourceLocation getModelLocation(Block debug0) {
/* 25 */     ResourceLocation debug1 = Registry.BLOCK.getKey(debug0);
/* 26 */     return new ResourceLocation(debug1.getNamespace(), "block/" + debug1.getPath());
/*    */   }
/*    */   
/*    */   public static ResourceLocation getModelLocation(Item debug0) {
/* 30 */     ResourceLocation debug1 = Registry.ITEM.getKey(debug0);
/* 31 */     return new ResourceLocation(debug1.getNamespace(), "item/" + debug1.getPath());
/*    */   }
/*    */   
/*    */   public static ResourceLocation getModelLocation(Item debug0, String debug1) {
/* 35 */     ResourceLocation debug2 = Registry.ITEM.getKey(debug0);
/* 36 */     return new ResourceLocation(debug2.getNamespace(), "item/" + debug2.getPath() + debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\data\models\model\ModelLocationUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */