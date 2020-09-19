/*     */ package net.minecraft.world.item;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.context.UseOnContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class CompassItem extends Item implements Vanishable {
/*  24 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompassItem(Item.Properties debug1) {
/*  31 */     super(debug1);
/*     */   }
/*     */   
/*     */   public static boolean isLodestoneCompass(ItemStack debug0) {
/*  35 */     CompoundTag debug1 = debug0.getTag();
/*  36 */     return (debug1 != null && (debug1.contains("LodestoneDimension") || debug1.contains("LodestonePos")));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFoil(ItemStack debug1) {
/*  41 */     return (isLodestoneCompass(debug1) || super.isFoil(debug1));
/*     */   }
/*     */   
/*     */   public static Optional<ResourceKey<Level>> getLodestoneDimension(CompoundTag debug0) {
/*  45 */     return Level.RESOURCE_KEY_CODEC.parse((DynamicOps)NbtOps.INSTANCE, debug0.get("LodestoneDimension")).result();
/*     */   }
/*     */ 
/*     */   
/*     */   public void inventoryTick(ItemStack debug1, Level debug2, Entity debug3, int debug4, boolean debug5) {
/*  50 */     if (debug2.isClientSide) {
/*     */       return;
/*     */     }
/*     */     
/*  54 */     if (isLodestoneCompass(debug1)) {
/*  55 */       CompoundTag debug6 = debug1.getOrCreateTag();
/*  56 */       if (debug6.contains("LodestoneTracked") && !debug6.getBoolean("LodestoneTracked")) {
/*     */         return;
/*     */       }
/*     */       
/*  60 */       Optional<ResourceKey<Level>> debug7 = getLodestoneDimension(debug6);
/*  61 */       if (debug7.isPresent() && debug7.get() == debug2.dimension() && debug6.contains("LodestonePos") && 
/*  62 */         !((ServerLevel)debug2).getPoiManager().existsAtPosition(PoiType.LODESTONE, NbtUtils.readBlockPos(debug6.getCompound("LodestonePos")))) {
/*  63 */         debug6.remove("LodestonePos");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InteractionResult useOn(UseOnContext debug1) {
/*  71 */     BlockPos debug2 = debug1.getClickedPos();
/*  72 */     Level debug3 = debug1.getLevel();
/*     */     
/*  74 */     if (debug3.getBlockState(debug2).is(Blocks.LODESTONE)) {
/*  75 */       debug3.playSound(null, debug2, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
/*     */       
/*  77 */       Player debug4 = debug1.getPlayer();
/*  78 */       ItemStack debug5 = debug1.getItemInHand();
/*  79 */       boolean debug6 = (!debug4.abilities.instabuild && debug5.getCount() == 1);
/*     */       
/*  81 */       if (debug6) {
/*  82 */         addLodestoneTags(debug3.dimension(), debug2, debug5.getOrCreateTag());
/*     */       } else {
/*  84 */         ItemStack debug7 = new ItemStack(Items.COMPASS, 1);
/*  85 */         CompoundTag debug8 = debug5.hasTag() ? debug5.getTag().copy() : new CompoundTag();
/*  86 */         debug7.setTag(debug8);
/*  87 */         if (!debug4.abilities.instabuild) {
/*  88 */           debug5.shrink(1);
/*     */         }
/*  90 */         addLodestoneTags(debug3.dimension(), debug2, debug8);
/*  91 */         if (!debug4.inventory.add(debug7)) {
/*  92 */           debug4.drop(debug7, false);
/*     */         }
/*     */       } 
/*     */       
/*  96 */       return InteractionResult.sidedSuccess(debug3.isClientSide);
/*     */     } 
/*  98 */     return super.useOn(debug1);
/*     */   }
/*     */   
/*     */   private void addLodestoneTags(ResourceKey<Level> debug1, BlockPos debug2, CompoundTag debug3) {
/* 102 */     debug3.put("LodestonePos", (Tag)NbtUtils.writeBlockPos(debug2));
/* 103 */     Level.RESOURCE_KEY_CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, debug1).resultOrPartial(LOGGER::error).ifPresent(debug1 -> debug0.put("LodestoneDimension", debug1));
/* 104 */     debug3.putBoolean("LodestoneTracked", true);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDescriptionId(ItemStack debug1) {
/* 109 */     return isLodestoneCompass(debug1) ? "item.minecraft.lodestone_compass" : super.getDescriptionId(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\item\CompassItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */