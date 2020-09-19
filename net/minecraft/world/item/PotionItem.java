/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResultHolder;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.alchemy.Potion;
/*     */ import net.minecraft.world.item.alchemy.PotionUtils;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PotionItem
/*     */   extends Item
/*     */ {
/*     */   public PotionItem(Item.Properties debug1) {
/*  26 */     super(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getDefaultInstance() {
/*  31 */     return PotionUtils.setPotion(super.getDefaultInstance(), Potions.WATER);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack finishUsingItem(ItemStack debug1, Level debug2, LivingEntity debug3) {
/*  36 */     Player debug4 = (debug3 instanceof Player) ? (Player)debug3 : null;
/*     */     
/*  38 */     if (debug4 instanceof ServerPlayer) {
/*  39 */       CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)debug4, debug1);
/*     */     }
/*  41 */     if (!debug2.isClientSide) {
/*  42 */       List<MobEffectInstance> debug5 = PotionUtils.getMobEffects(debug1);
/*  43 */       for (MobEffectInstance debug7 : debug5) {
/*  44 */         if (debug7.getEffect().isInstantenous()) {
/*  45 */           debug7.getEffect().applyInstantenousEffect((Entity)debug4, (Entity)debug4, debug3, debug7.getAmplifier(), 1.0D); continue;
/*     */         } 
/*  47 */         debug3.addEffect(new MobEffectInstance(debug7));
/*     */       } 
/*     */     } 
/*     */     
/*  51 */     if (debug4 != null) {
/*  52 */       debug4.awardStat(Stats.ITEM_USED.get(this));
/*  53 */       if (!debug4.abilities.instabuild) {
/*  54 */         debug1.shrink(1);
/*     */       }
/*     */     } 
/*     */     
/*  58 */     if (debug4 == null || !debug4.abilities.instabuild) {
/*  59 */       if (debug1.isEmpty())
/*  60 */         return new ItemStack(Items.GLASS_BOTTLE); 
/*  61 */       if (debug4 != null) {
/*  62 */         debug4.inventory.add(new ItemStack(Items.GLASS_BOTTLE));
/*     */       }
/*     */     } 
/*     */     
/*  66 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUseDuration(ItemStack debug1) {
/*  71 */     return 32;
/*     */   }
/*     */ 
/*     */   
/*     */   public UseAnim getUseAnimation(ItemStack debug1) {
/*  76 */     return UseAnim.DRINK;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/*  81 */     return ItemUtils.useDrink(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescriptionId(ItemStack debug1) {
/*  86 */     return PotionUtils.getPotion(debug1).getName(getDescriptionId() + ".effect.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFoil(ItemStack debug1) {
/*  96 */     return (super.isFoil(debug1) || !PotionUtils.getMobEffects(debug1).isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillItemCategory(CreativeModeTab debug1, NonNullList<ItemStack> debug2) {
/* 101 */     if (allowdedIn(debug1))
/* 102 */       for (Potion debug4 : Registry.POTION) {
/* 103 */         if (debug4 != Potions.EMPTY)
/* 104 */           debug2.add(PotionUtils.setPotion(new ItemStack(this), debug4)); 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\PotionItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */