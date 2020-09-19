/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.ListTag;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResultHolder;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.RecipeManager;
/*    */ import net.minecraft.world.level.Level;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ 
/*    */ public class KnowledgeBookItem
/*    */   extends Item
/*    */ {
/* 23 */   private static final Logger LOGGER = LogManager.getLogger();
/*    */   
/*    */   public KnowledgeBookItem(Item.Properties debug1) {
/* 26 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 31 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/* 32 */     CompoundTag debug5 = debug4.getTag();
/*    */     
/* 34 */     if (!debug2.abilities.instabuild) {
/* 35 */       debug2.setItemInHand(debug3, ItemStack.EMPTY);
/*    */     }
/*    */     
/* 38 */     if (debug5 == null || !debug5.contains("Recipes", 9)) {
/* 39 */       LOGGER.error("Tag not valid: {}", debug5);
/* 40 */       return InteractionResultHolder.fail(debug4);
/*    */     } 
/*    */     
/* 43 */     if (!debug1.isClientSide) {
/* 44 */       ListTag debug6 = debug5.getList("Recipes", 8);
/* 45 */       List<Recipe<?>> debug7 = Lists.newArrayList();
/*    */       
/* 47 */       RecipeManager debug8 = debug1.getServer().getRecipeManager();
/* 48 */       for (int debug9 = 0; debug9 < debug6.size(); debug9++) {
/* 49 */         String debug10 = debug6.getString(debug9);
/* 50 */         Optional<? extends Recipe<?>> debug11 = debug8.byKey(new ResourceLocation(debug10));
/* 51 */         if (debug11.isPresent()) {
/* 52 */           debug7.add(debug11.get());
/*    */         } else {
/* 54 */           LOGGER.error("Invalid recipe: {}", debug10);
/* 55 */           return InteractionResultHolder.fail(debug4);
/*    */         } 
/*    */       } 
/*    */       
/* 59 */       debug2.awardRecipes(debug7);
/* 60 */       debug2.awardStat(Stats.ITEM_USED.get(this));
/*    */     } 
/*    */     
/* 63 */     return InteractionResultHolder.sidedSuccess(debug4, debug1.isClientSide());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\KnowledgeBookItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */