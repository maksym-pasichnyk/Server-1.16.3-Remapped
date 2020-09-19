/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ResourceLocationException;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundRecipePacket;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeManager;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ServerRecipeBook
/*     */   extends RecipeBook {
/*  26 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int addRecipes(Collection<Recipe<?>> debug1, ServerPlayer debug2) {
/*  32 */     List<ResourceLocation> debug3 = Lists.newArrayList();
/*  33 */     int debug4 = 0;
/*     */     
/*  35 */     for (Recipe<?> debug6 : debug1) {
/*  36 */       ResourceLocation debug7 = debug6.getId();
/*  37 */       if (!this.known.contains(debug7) && !debug6.isSpecial()) {
/*  38 */         add(debug7);
/*  39 */         addHighlight(debug7);
/*  40 */         debug3.add(debug7);
/*  41 */         CriteriaTriggers.RECIPE_UNLOCKED.trigger(debug2, debug6);
/*  42 */         debug4++;
/*     */       } 
/*     */     } 
/*     */     
/*  46 */     sendRecipes(ClientboundRecipePacket.State.ADD, debug2, debug3);
/*  47 */     return debug4;
/*     */   }
/*     */   
/*     */   public int removeRecipes(Collection<Recipe<?>> debug1, ServerPlayer debug2) {
/*  51 */     List<ResourceLocation> debug3 = Lists.newArrayList();
/*  52 */     int debug4 = 0;
/*     */     
/*  54 */     for (Recipe<?> debug6 : debug1) {
/*  55 */       ResourceLocation debug7 = debug6.getId();
/*  56 */       if (this.known.contains(debug7)) {
/*  57 */         remove(debug7);
/*  58 */         debug3.add(debug7);
/*  59 */         debug4++;
/*     */       } 
/*     */     } 
/*     */     
/*  63 */     sendRecipes(ClientboundRecipePacket.State.REMOVE, debug2, debug3);
/*  64 */     return debug4;
/*     */   }
/*     */   
/*     */   private void sendRecipes(ClientboundRecipePacket.State debug1, ServerPlayer debug2, List<ResourceLocation> debug3) {
/*  68 */     debug2.connection.send((Packet)new ClientboundRecipePacket(debug1, debug3, Collections.emptyList(), getBookSettings()));
/*     */   }
/*     */   
/*     */   public CompoundTag toNbt() {
/*  72 */     CompoundTag debug1 = new CompoundTag();
/*     */     
/*  74 */     getBookSettings().write(debug1);
/*     */     
/*  76 */     ListTag debug2 = new ListTag();
/*  77 */     for (ResourceLocation debug4 : this.known) {
/*  78 */       debug2.add(StringTag.valueOf(debug4.toString()));
/*     */     }
/*  80 */     debug1.put("recipes", (Tag)debug2);
/*     */     
/*  82 */     ListTag debug3 = new ListTag();
/*  83 */     for (ResourceLocation debug5 : this.highlight) {
/*  84 */       debug3.add(StringTag.valueOf(debug5.toString()));
/*     */     }
/*  86 */     debug1.put("toBeDisplayed", (Tag)debug3);
/*     */     
/*  88 */     return debug1;
/*     */   }
/*     */   
/*     */   public void fromNbt(CompoundTag debug1, RecipeManager debug2) {
/*  92 */     setBookSettings(RecipeBookSettings.read(debug1));
/*     */     
/*  94 */     ListTag debug3 = debug1.getList("recipes", 8);
/*  95 */     loadRecipes(debug3, this::add, debug2);
/*     */     
/*  97 */     ListTag debug4 = debug1.getList("toBeDisplayed", 8);
/*  98 */     loadRecipes(debug4, this::addHighlight, debug2);
/*     */   }
/*     */   
/*     */   private void loadRecipes(ListTag debug1, Consumer<Recipe<?>> debug2, RecipeManager debug3) {
/* 102 */     for (int debug4 = 0; debug4 < debug1.size(); debug4++) {
/* 103 */       String debug5 = debug1.getString(debug4);
/*     */       try {
/* 105 */         ResourceLocation debug6 = new ResourceLocation(debug5);
/* 106 */         Optional<? extends Recipe<?>> debug7 = debug3.byKey(debug6);
/* 107 */         if (!debug7.isPresent())
/* 108 */         { LOGGER.error("Tried to load unrecognized recipe: {} removed now.", debug6); }
/*     */         else
/*     */         
/* 111 */         { debug2.accept(debug7.get()); } 
/* 112 */       } catch (ResourceLocationException debug6) {
/* 113 */         LOGGER.error("Tried to load improperly formatted recipe: {} removed now.", debug5);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void sendInitialRecipeBook(ServerPlayer debug1) {
/* 119 */     debug1.connection.send((Packet)new ClientboundRecipePacket(ClientboundRecipePacket.State.INIT, this.known, this.highlight, getBookSettings()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\stats\ServerRecipeBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */