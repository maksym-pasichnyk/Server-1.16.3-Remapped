/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.world.Nameable;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.AbstractBannerBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BannerBlockEntity
/*     */   extends BlockEntity
/*     */   implements Nameable
/*     */ {
/*     */   @Nullable
/*     */   private Component name;
/*     */   @Nullable
/*  32 */   private DyeColor baseColor = DyeColor.WHITE;
/*     */   
/*     */   @Nullable
/*     */   private ListTag itemPatterns;
/*     */   
/*     */   private boolean receivedData;
/*     */   
/*     */   @Nullable
/*     */   private List<Pair<BannerPattern, DyeColor>> patterns;
/*     */   
/*     */   public BannerBlockEntity() {
/*  43 */     super(BlockEntityType.BANNER);
/*     */   }
/*     */   
/*     */   public BannerBlockEntity(DyeColor debug1) {
/*  47 */     this();
/*  48 */     this.baseColor = debug1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Component getName() {
/*  71 */     if (this.name != null) {
/*  72 */       return this.name;
/*     */     }
/*  74 */     return (Component)new TranslatableComponent("block.minecraft.banner");
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Component getCustomName() {
/*  80 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setCustomName(Component debug1) {
/*  84 */     this.name = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/*  89 */     super.save(debug1);
/*     */     
/*  91 */     if (this.itemPatterns != null) {
/*  92 */       debug1.put("Patterns", (Tag)this.itemPatterns);
/*     */     }
/*     */     
/*  95 */     if (this.name != null) {
/*  96 */       debug1.putString("CustomName", Component.Serializer.toJson(this.name));
/*     */     }
/*     */     
/*  99 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/* 104 */     super.load(debug1, debug2);
/*     */     
/* 106 */     if (debug2.contains("CustomName", 8)) {
/* 107 */       this.name = (Component)Component.Serializer.fromJson(debug2.getString("CustomName"));
/*     */     }
/*     */     
/* 110 */     if (hasLevel()) {
/* 111 */       this.baseColor = ((AbstractBannerBlock)getBlockState().getBlock()).getColor();
/*     */     } else {
/* 113 */       this.baseColor = null;
/*     */     } 
/* 115 */     this.itemPatterns = debug2.getList("Patterns", 10);
/*     */     
/* 117 */     this.patterns = null;
/* 118 */     this.receivedData = true;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 124 */     return new ClientboundBlockEntityDataPacket(this.worldPosition, 6, getUpdateTag());
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag() {
/* 129 */     return save(new CompoundTag());
/*     */   }
/*     */   
/*     */   public static int getPatternCount(ItemStack debug0) {
/* 133 */     CompoundTag debug1 = debug0.getTagElement("BlockEntityTag");
/* 134 */     if (debug1 != null && debug1.contains("Patterns")) {
/* 135 */       return debug1.getList("Patterns", 10).size();
/*     */     }
/* 137 */     return 0;
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
/*     */   public static void removeLastPattern(ItemStack debug0) {
/* 167 */     CompoundTag debug1 = debug0.getTagElement("BlockEntityTag");
/* 168 */     if (debug1 == null || !debug1.contains("Patterns", 9)) {
/*     */       return;
/*     */     }
/*     */     
/* 172 */     ListTag debug2 = debug1.getList("Patterns", 10);
/* 173 */     if (debug2.isEmpty()) {
/*     */       return;
/*     */     }
/* 176 */     debug2.remove(debug2.size() - 1);
/*     */     
/* 178 */     if (debug2.isEmpty()) {
/* 179 */       debug0.removeTagKey("BlockEntityTag");
/*     */     }
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
/*     */   public DyeColor getBaseColor(Supplier<BlockState> debug1) {
/* 196 */     if (this.baseColor == null) {
/* 197 */       this.baseColor = ((AbstractBannerBlock)((BlockState)debug1.get()).getBlock()).getColor();
/*     */     }
/* 199 */     return this.baseColor;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\BannerBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */