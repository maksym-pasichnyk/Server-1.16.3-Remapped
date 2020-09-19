/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.TextComponent;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.InteractionResultHolder;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LecternBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WrittenBookItem
/*     */   extends Item
/*     */ {
/*     */   public WrittenBookItem(Item.Properties debug1) {
/*  43 */     super(debug1);
/*     */   }
/*     */   
/*     */   public static boolean makeSureTagIsValid(@Nullable CompoundTag debug0) {
/*  47 */     if (!WritableBookItem.makeSureTagIsValid(debug0)) {
/*  48 */       return false;
/*     */     }
/*     */     
/*  51 */     if (!debug0.contains("title", 8)) {
/*  52 */       return false;
/*     */     }
/*  54 */     String debug1 = debug0.getString("title");
/*  55 */     if (debug1.length() > 32) {
/*  56 */       return false;
/*     */     }
/*     */     
/*  59 */     return debug0.contains("author", 8);
/*     */   }
/*     */   
/*     */   public static int getGeneration(ItemStack debug0) {
/*  63 */     return debug0.getTag().getInt("generation");
/*     */   }
/*     */   
/*     */   public static int getPageCount(ItemStack debug0) {
/*  67 */     CompoundTag debug1 = debug0.getTag();
/*  68 */     return (debug1 != null) ? debug1.getList("pages", 8).size() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getName(ItemStack debug1) {
/*  73 */     if (debug1.hasTag()) {
/*  74 */       CompoundTag debug2 = debug1.getTag();
/*     */       
/*  76 */       String debug3 = debug2.getString("title");
/*  77 */       if (!StringUtil.isNullOrEmpty(debug3)) {
/*  78 */         return (Component)new TextComponent(debug3);
/*     */       }
/*     */     } 
/*  81 */     return super.getName(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext debug1) {
/* 100 */     Level debug2 = debug1.getLevel();
/* 101 */     BlockPos debug3 = debug1.getClickedPos();
/* 102 */     BlockState debug4 = debug2.getBlockState(debug3);
/*     */     
/* 104 */     if (debug4.is(Blocks.LECTERN)) {
/* 105 */       return LecternBlock.tryPlaceBook(debug2, debug3, debug4, debug1.getItemInHand()) ? InteractionResult.sidedSuccess(debug2.isClientSide) : InteractionResult.PASS;
/*     */     }
/*     */     
/* 108 */     return InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResultHolder<ItemStack> use(Level debug1, Player debug2, InteractionHand debug3) {
/* 113 */     ItemStack debug4 = debug2.getItemInHand(debug3);
/* 114 */     debug2.openItemGui(debug4, debug3);
/* 115 */     debug2.awardStat(Stats.ITEM_USED.get(this));
/* 116 */     return InteractionResultHolder.sidedSuccess(debug4, debug1.isClientSide());
/*     */   }
/*     */   
/*     */   public static boolean resolveBookComponents(ItemStack debug0, @Nullable CommandSourceStack debug1, @Nullable Player debug2) {
/* 120 */     CompoundTag debug3 = debug0.getTag();
/* 121 */     if (debug3 == null || debug3.getBoolean("resolved")) {
/* 122 */       return false;
/*     */     }
/* 124 */     debug3.putBoolean("resolved", true);
/* 125 */     if (!makeSureTagIsValid(debug3)) {
/* 126 */       return false;
/*     */     }
/* 128 */     ListTag debug4 = debug3.getList("pages", 8);
/* 129 */     for (int debug5 = 0; debug5 < debug4.size(); debug5++) {
/* 130 */       TextComponent textComponent; String debug6 = debug4.getString(debug5);
/*     */       
/*     */       try {
/* 133 */         MutableComponent mutableComponent = Component.Serializer.fromJsonLenient(debug6);
/* 134 */         mutableComponent = ComponentUtils.updateForEntity(debug1, (Component)mutableComponent, (Entity)debug2, 0);
/* 135 */       } catch (Exception debug8) {
/* 136 */         textComponent = new TextComponent(debug6);
/*     */       } 
/* 138 */       debug4.set(debug5, (Tag)StringTag.valueOf(Component.Serializer.toJson((Component)textComponent)));
/*     */     } 
/* 140 */     debug3.put("pages", (Tag)debug4);
/* 141 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFoil(ItemStack debug1) {
/* 146 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\WrittenBookItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */