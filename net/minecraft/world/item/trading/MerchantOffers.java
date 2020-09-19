/*     */ package net.minecraft.world.item.trading;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class MerchantOffers
/*     */   extends ArrayList<MerchantOffer>
/*     */ {
/*     */   public MerchantOffers() {}
/*     */   
/*     */   public MerchantOffers(CompoundTag debug1) {
/*  17 */     ListTag debug2 = debug1.getList("Recipes", 10);
/*     */     
/*  19 */     for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/*  20 */       add(new MerchantOffer(debug2.getCompound(debug3)));
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public MerchantOffer getRecipeFor(ItemStack debug1, ItemStack debug2, int debug3) {
/*  26 */     if (debug3 > 0 && debug3 < size()) {
/*     */       
/*  28 */       MerchantOffer merchantOffer = get(debug3);
/*  29 */       if (merchantOffer.satisfiedBy(debug1, debug2)) {
/*  30 */         return merchantOffer;
/*     */       }
/*  32 */       return null;
/*     */     } 
/*     */     
/*  35 */     for (int debug4 = 0; debug4 < size(); debug4++) {
/*  36 */       MerchantOffer debug5 = get(debug4);
/*  37 */       if (debug5.satisfiedBy(debug1, debug2)) {
/*  38 */         return debug5;
/*     */       }
/*     */     } 
/*  41 */     return null;
/*     */   }
/*     */   
/*     */   public void writeToStream(FriendlyByteBuf debug1) {
/*  45 */     debug1.writeByte((byte)(size() & 0xFF));
/*  46 */     for (int debug2 = 0; debug2 < size(); debug2++) {
/*  47 */       MerchantOffer debug3 = get(debug2);
/*  48 */       debug1.writeItem(debug3.getBaseCostA());
/*  49 */       debug1.writeItem(debug3.getResult());
/*     */       
/*  51 */       ItemStack debug4 = debug3.getCostB();
/*  52 */       debug1.writeBoolean(!debug4.isEmpty());
/*  53 */       if (!debug4.isEmpty()) {
/*  54 */         debug1.writeItem(debug4);
/*     */       }
/*  56 */       debug1.writeBoolean(debug3.isOutOfStock());
/*  57 */       debug1.writeInt(debug3.getUses());
/*  58 */       debug1.writeInt(debug3.getMaxUses());
/*  59 */       debug1.writeInt(debug3.getXp());
/*  60 */       debug1.writeInt(debug3.getSpecialPriceDiff());
/*  61 */       debug1.writeFloat(debug3.getPriceMultiplier());
/*  62 */       debug1.writeInt(debug3.getDemand());
/*     */     } 
/*     */   }
/*     */   
/*     */   public static MerchantOffers createFromStream(FriendlyByteBuf debug0) {
/*  67 */     MerchantOffers debug1 = new MerchantOffers();
/*     */     
/*  69 */     int debug2 = debug0.readByte() & 0xFF;
/*  70 */     for (int debug3 = 0; debug3 < debug2; debug3++) {
/*  71 */       ItemStack debug4 = debug0.readItem();
/*  72 */       ItemStack debug5 = debug0.readItem();
/*     */       
/*  74 */       ItemStack debug6 = ItemStack.EMPTY;
/*  75 */       if (debug0.readBoolean()) {
/*  76 */         debug6 = debug0.readItem();
/*     */       }
/*  78 */       boolean debug7 = debug0.readBoolean();
/*  79 */       int debug8 = debug0.readInt();
/*  80 */       int debug9 = debug0.readInt();
/*  81 */       int debug10 = debug0.readInt();
/*  82 */       int debug11 = debug0.readInt();
/*  83 */       float debug12 = debug0.readFloat();
/*  84 */       int debug13 = debug0.readInt();
/*     */       
/*  86 */       MerchantOffer debug14 = new MerchantOffer(debug4, debug6, debug5, debug8, debug9, debug10, debug12, debug13);
/*  87 */       if (debug7) {
/*  88 */         debug14.setToOutOfStock();
/*     */       }
/*  90 */       debug14.setSpecialPriceDiff(debug11);
/*     */       
/*  92 */       debug1.add(debug14);
/*     */     } 
/*  94 */     return debug1;
/*     */   }
/*     */   
/*     */   public CompoundTag createTag() {
/*  98 */     CompoundTag debug1 = new CompoundTag();
/*     */     
/* 100 */     ListTag debug2 = new ListTag();
/* 101 */     for (int debug3 = 0; debug3 < size(); debug3++) {
/* 102 */       MerchantOffer debug4 = get(debug3);
/* 103 */       debug2.add(debug4.createTag());
/*     */     } 
/* 105 */     debug1.put("Recipes", (Tag)debug2);
/* 106 */     return debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\trading\MerchantOffers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */