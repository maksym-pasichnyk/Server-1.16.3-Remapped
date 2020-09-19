/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.Sheep;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class DyeItem
/*    */   extends Item {
/* 13 */   private static final Map<DyeColor, DyeItem> ITEM_BY_COLOR = Maps.newEnumMap(DyeColor.class);
/*    */   
/*    */   private final DyeColor dyeColor;
/*    */   
/*    */   public DyeItem(DyeColor debug1, Item.Properties debug2) {
/* 18 */     super(debug2);
/* 19 */     this.dyeColor = debug1;
/* 20 */     ITEM_BY_COLOR.put(debug1, this);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult interactLivingEntity(ItemStack debug1, Player debug2, LivingEntity debug3, InteractionHand debug4) {
/* 25 */     if (debug3 instanceof Sheep) {
/* 26 */       Sheep debug5 = (Sheep)debug3;
/* 27 */       if (debug5.isAlive() && !debug5.isSheared() && debug5.getColor() != this.dyeColor) {
/* 28 */         if (!debug2.level.isClientSide) {
/* 29 */           debug5.setColor(this.dyeColor);
/* 30 */           debug1.shrink(1);
/*    */         } 
/* 32 */         return InteractionResult.sidedSuccess(debug2.level.isClientSide);
/*    */       } 
/*    */     } 
/* 35 */     return InteractionResult.PASS;
/*    */   }
/*    */   
/*    */   public DyeColor getDyeColor() {
/* 39 */     return this.dyeColor;
/*    */   }
/*    */   
/*    */   public static DyeItem byColor(DyeColor debug0) {
/* 43 */     return ITEM_BY_COLOR.get(debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\DyeItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */