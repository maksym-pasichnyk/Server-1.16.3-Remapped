/*     */ package net.minecraft.world.item.trading;
/*     */ 
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class MerchantOffer
/*     */ {
/*     */   private final ItemStack baseCostA;
/*     */   private final ItemStack costB;
/*     */   private final ItemStack result;
/*     */   private int uses;
/*     */   private final int maxUses;
/*     */   private boolean rewardExp = true;
/*     */   private int specialPriceDiff;
/*     */   private int demand;
/*     */   private float priceMultiplier;
/*  20 */   private int xp = 1;
/*     */   
/*     */   public MerchantOffer(CompoundTag debug1) {
/*  23 */     this.baseCostA = ItemStack.of(debug1.getCompound("buy"));
/*  24 */     this.costB = ItemStack.of(debug1.getCompound("buyB"));
/*     */     
/*  26 */     this.result = ItemStack.of(debug1.getCompound("sell"));
/*     */     
/*  28 */     this.uses = debug1.getInt("uses");
/*  29 */     if (debug1.contains("maxUses", 99)) {
/*  30 */       this.maxUses = debug1.getInt("maxUses");
/*     */     } else {
/*  32 */       this.maxUses = 4;
/*     */     } 
/*     */     
/*  35 */     if (debug1.contains("rewardExp", 1)) {
/*  36 */       this.rewardExp = debug1.getBoolean("rewardExp");
/*     */     }
/*     */     
/*  39 */     if (debug1.contains("xp", 3)) {
/*  40 */       this.xp = debug1.getInt("xp");
/*     */     }
/*     */     
/*  43 */     if (debug1.contains("priceMultiplier", 5)) {
/*  44 */       this.priceMultiplier = debug1.getFloat("priceMultiplier");
/*     */     }
/*     */     
/*  47 */     this.specialPriceDiff = debug1.getInt("specialPrice");
/*  48 */     this.demand = debug1.getInt("demand");
/*     */   }
/*     */   
/*     */   public MerchantOffer(ItemStack debug1, ItemStack debug2, int debug3, int debug4, float debug5) {
/*  52 */     this(debug1, ItemStack.EMPTY, debug2, debug3, debug4, debug5);
/*     */   }
/*     */   
/*     */   public MerchantOffer(ItemStack debug1, ItemStack debug2, ItemStack debug3, int debug4, int debug5, float debug6) {
/*  56 */     this(debug1, debug2, debug3, 0, debug4, debug5, debug6);
/*     */   }
/*     */   
/*     */   public MerchantOffer(ItemStack debug1, ItemStack debug2, ItemStack debug3, int debug4, int debug5, int debug6, float debug7) {
/*  60 */     this(debug1, debug2, debug3, debug4, debug5, debug6, debug7, 0);
/*     */   }
/*     */   
/*     */   public MerchantOffer(ItemStack debug1, ItemStack debug2, ItemStack debug3, int debug4, int debug5, int debug6, float debug7, int debug8) {
/*  64 */     this.baseCostA = debug1;
/*  65 */     this.costB = debug2;
/*  66 */     this.result = debug3;
/*  67 */     this.uses = debug4;
/*  68 */     this.maxUses = debug5;
/*  69 */     this.xp = debug6;
/*  70 */     this.priceMultiplier = debug7;
/*  71 */     this.demand = debug8;
/*     */   }
/*     */   
/*     */   public ItemStack getBaseCostA() {
/*  75 */     return this.baseCostA;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getCostA() {
/*  80 */     int debug1 = this.baseCostA.getCount();
/*  81 */     ItemStack debug2 = this.baseCostA.copy();
/*     */ 
/*     */     
/*  84 */     int debug3 = Math.max(0, Mth.floor((debug1 * this.demand) * this.priceMultiplier));
/*     */     
/*  86 */     debug2.setCount(Mth.clamp(debug1 + debug3 + this.specialPriceDiff, 1, this.baseCostA.getItem().getMaxStackSize()));
/*  87 */     return debug2;
/*     */   }
/*     */   
/*     */   public ItemStack getCostB() {
/*  91 */     return this.costB;
/*     */   }
/*     */   
/*     */   public ItemStack getResult() {
/*  95 */     return this.result;
/*     */   }
/*     */   
/*     */   public void updateDemand() {
/*  99 */     this.demand = this.demand + this.uses - this.maxUses - this.uses;
/*     */   }
/*     */   
/*     */   public ItemStack assemble() {
/* 103 */     return this.result.copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUses() {
/* 114 */     return this.uses;
/*     */   }
/*     */   
/*     */   public void resetUses() {
/* 118 */     this.uses = 0;
/*     */   }
/*     */   
/*     */   public int getMaxUses() {
/* 122 */     return this.maxUses;
/*     */   }
/*     */   
/*     */   public void increaseUses() {
/* 126 */     this.uses++;
/*     */   }
/*     */   
/*     */   public int getDemand() {
/* 130 */     return this.demand;
/*     */   }
/*     */   
/*     */   public void addToSpecialPriceDiff(int debug1) {
/* 134 */     this.specialPriceDiff += debug1;
/*     */   }
/*     */   
/*     */   public void resetSpecialPriceDiff() {
/* 138 */     this.specialPriceDiff = 0;
/*     */   }
/*     */   
/*     */   public int getSpecialPriceDiff() {
/* 142 */     return this.specialPriceDiff;
/*     */   }
/*     */   
/*     */   public void setSpecialPriceDiff(int debug1) {
/* 146 */     this.specialPriceDiff = debug1;
/*     */   }
/*     */   
/*     */   public float getPriceMultiplier() {
/* 150 */     return this.priceMultiplier;
/*     */   }
/*     */   
/*     */   public int getXp() {
/* 154 */     return this.xp;
/*     */   }
/*     */   
/*     */   public boolean isOutOfStock() {
/* 158 */     return (this.uses >= this.maxUses);
/*     */   }
/*     */   
/*     */   public void setToOutOfStock() {
/* 162 */     this.uses = this.maxUses;
/*     */   }
/*     */   
/*     */   public boolean needsRestock() {
/* 166 */     return (this.uses > 0);
/*     */   }
/*     */   
/*     */   public boolean shouldRewardExp() {
/* 170 */     return this.rewardExp;
/*     */   }
/*     */   
/*     */   public CompoundTag createTag() {
/* 174 */     CompoundTag debug1 = new CompoundTag();
/* 175 */     debug1.put("buy", (Tag)this.baseCostA.save(new CompoundTag()));
/* 176 */     debug1.put("sell", (Tag)this.result.save(new CompoundTag()));
/* 177 */     debug1.put("buyB", (Tag)this.costB.save(new CompoundTag()));
/* 178 */     debug1.putInt("uses", this.uses);
/* 179 */     debug1.putInt("maxUses", this.maxUses);
/* 180 */     debug1.putBoolean("rewardExp", this.rewardExp);
/* 181 */     debug1.putInt("xp", this.xp);
/* 182 */     debug1.putFloat("priceMultiplier", this.priceMultiplier);
/* 183 */     debug1.putInt("specialPrice", this.specialPriceDiff);
/* 184 */     debug1.putInt("demand", this.demand);
/* 185 */     return debug1;
/*     */   }
/*     */   
/*     */   public boolean satisfiedBy(ItemStack debug1, ItemStack debug2) {
/* 189 */     return (isRequiredItem(debug1, getCostA()) && debug1.getCount() >= getCostA().getCount() && 
/* 190 */       isRequiredItem(debug2, this.costB) && debug2.getCount() >= this.costB.getCount());
/*     */   }
/*     */   
/*     */   private boolean isRequiredItem(ItemStack debug1, ItemStack debug2) {
/* 194 */     if (debug2.isEmpty() && debug1.isEmpty()) {
/* 195 */       return true;
/*     */     }
/*     */     
/* 198 */     ItemStack debug3 = debug1.copy();
/* 199 */     if (debug3.getItem().canBeDepleted()) {
/* 200 */       debug3.setDamageValue(debug3.getDamageValue());
/*     */     }
/* 202 */     return (ItemStack.isSame(debug3, debug2) && (!debug2.hasTag() || (debug3.hasTag() && NbtUtils.compareNbt((Tag)debug2.getTag(), (Tag)debug3.getTag(), false))));
/*     */   }
/*     */   
/*     */   public boolean take(ItemStack debug1, ItemStack debug2) {
/* 206 */     if (!satisfiedBy(debug1, debug2)) {
/* 207 */       return false;
/*     */     }
/*     */     
/* 210 */     debug1.shrink(getCostA().getCount());
/* 211 */     if (!getCostB().isEmpty()) {
/* 212 */       debug2.shrink(getCostB().getCount());
/*     */     }
/* 214 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\trading\MerchantOffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */