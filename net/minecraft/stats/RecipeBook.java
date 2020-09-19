/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.world.inventory.RecipeBookType;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ 
/*     */ 
/*     */ public class RecipeBook
/*     */ {
/*  13 */   protected final Set<ResourceLocation> known = Sets.newHashSet();
/*  14 */   protected final Set<ResourceLocation> highlight = Sets.newHashSet();
/*     */   
/*  16 */   private final RecipeBookSettings bookSettings = new RecipeBookSettings();
/*     */   
/*     */   public void copyOverData(RecipeBook debug1) {
/*  19 */     this.known.clear();
/*  20 */     this.highlight.clear();
/*     */     
/*  22 */     this.bookSettings.replaceFrom(debug1.bookSettings);
/*     */     
/*  24 */     this.known.addAll(debug1.known);
/*  25 */     this.highlight.addAll(debug1.highlight);
/*     */   }
/*     */   
/*     */   public void add(Recipe<?> debug1) {
/*  29 */     if (!debug1.isSpecial()) {
/*  30 */       add(debug1.getId());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void add(ResourceLocation debug1) {
/*  35 */     this.known.add(debug1);
/*     */   }
/*     */   
/*     */   public boolean contains(@Nullable Recipe<?> debug1) {
/*  39 */     if (debug1 == null) {
/*  40 */       return false;
/*     */     }
/*  42 */     return this.known.contains(debug1.getId());
/*     */   }
/*     */   
/*     */   public boolean contains(ResourceLocation debug1) {
/*  46 */     return this.known.contains(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void remove(ResourceLocation debug1) {
/*  54 */     this.known.remove(debug1);
/*  55 */     this.highlight.remove(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeHighlight(Recipe<?> debug1) {
/*  63 */     this.highlight.remove(debug1.getId());
/*     */   }
/*     */   
/*     */   public void addHighlight(Recipe<?> debug1) {
/*  67 */     addHighlight(debug1.getId());
/*     */   }
/*     */   
/*     */   protected void addHighlight(ResourceLocation debug1) {
/*  71 */     this.highlight.add(debug1);
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
/*     */   public void setBookSettings(RecipeBookSettings debug1) {
/*  95 */     this.bookSettings.replaceFrom(debug1);
/*     */   }
/*     */   
/*     */   public RecipeBookSettings getBookSettings() {
/*  99 */     return this.bookSettings.copy();
/*     */   }
/*     */   
/*     */   public void setBookSetting(RecipeBookType debug1, boolean debug2, boolean debug3) {
/* 103 */     this.bookSettings.setOpen(debug1, debug2);
/* 104 */     this.bookSettings.setFiltering(debug1, debug3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\stats\RecipeBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */