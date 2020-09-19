/*     */ package net.minecraft.world.entity.player;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntCollection;
/*     */ import it.unimi.dsi.fastutil.ints.IntIterator;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntListIterator;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ 
/*     */ public class StackedContents {
/*  23 */   public final Int2IntMap contents = (Int2IntMap)new Int2IntOpenHashMap();
/*     */   
/*     */   public void accountSimpleStack(ItemStack debug1) {
/*  26 */     if (!debug1.isDamaged() && !debug1.isEnchanted() && !debug1.hasCustomHoverName()) {
/*  27 */       accountStack(debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void accountStack(ItemStack debug1) {
/*  32 */     accountStack(debug1, 64);
/*     */   }
/*     */   
/*     */   public void accountStack(ItemStack debug1, int debug2) {
/*  36 */     if (!debug1.isEmpty()) {
/*  37 */       int debug3 = getStackingIndex(debug1);
/*  38 */       int debug4 = Math.min(debug2, debug1.getCount());
/*  39 */       put(debug3, debug4);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getStackingIndex(ItemStack debug0) {
/*  44 */     return Registry.ITEM.getId(debug0.getItem());
/*     */   }
/*     */   
/*     */   private boolean has(int debug1) {
/*  48 */     return (this.contents.get(debug1) > 0);
/*     */   }
/*     */   
/*     */   private int take(int debug1, int debug2) {
/*  52 */     int debug3 = this.contents.get(debug1);
/*  53 */     if (debug3 >= debug2) {
/*  54 */       this.contents.put(debug1, debug3 - debug2);
/*  55 */       return debug1;
/*     */     } 
/*     */     
/*  58 */     return 0;
/*     */   }
/*     */   
/*     */   private void put(int debug1, int debug2) {
/*  62 */     this.contents.put(debug1, this.contents.get(debug1) + debug2);
/*     */   }
/*     */   
/*     */   public boolean canCraft(Recipe<?> debug1, @Nullable IntList debug2) {
/*  66 */     return canCraft(debug1, debug2, 1);
/*     */   }
/*     */   
/*     */   public boolean canCraft(Recipe<?> debug1, @Nullable IntList debug2, int debug3) {
/*  70 */     return (new RecipePicker(debug1)).tryPick(debug3, debug2);
/*     */   }
/*     */   
/*     */   public int getBiggestCraftableStack(Recipe<?> debug1, @Nullable IntList debug2) {
/*  74 */     return getBiggestCraftableStack(debug1, 2147483647, debug2);
/*     */   }
/*     */   
/*     */   public int getBiggestCraftableStack(Recipe<?> debug1, int debug2, @Nullable IntList debug3) {
/*  78 */     return (new RecipePicker(debug1)).tryPickAll(debug2, debug3);
/*     */   }
/*     */   
/*     */   public static ItemStack fromStackingIndex(int debug0) {
/*  82 */     if (debug0 == 0) {
/*  83 */       return ItemStack.EMPTY;
/*     */     }
/*  85 */     return new ItemStack((ItemLike)Item.byId(debug0));
/*     */   }
/*     */   
/*     */   public void clear() {
/*  89 */     this.contents.clear();
/*     */   }
/*     */   
/*     */   class RecipePicker {
/*     */     private final Recipe<?> recipe;
/*  94 */     private final List<Ingredient> ingredients = Lists.newArrayList();
/*     */     private final int ingredientCount;
/*     */     private final int[] items;
/*     */     private final int itemCount;
/*     */     private final BitSet data;
/*  99 */     private final IntList path = (IntList)new IntArrayList();
/*     */     
/*     */     public RecipePicker(Recipe<?> debug2) {
/* 102 */       this.recipe = debug2;
/* 103 */       this.ingredients.addAll((Collection<? extends Ingredient>)debug2.getIngredients());
/* 104 */       this.ingredients.removeIf(Ingredient::isEmpty);
/*     */       
/* 106 */       this.ingredientCount = this.ingredients.size();
/* 107 */       this.items = getUniqueAvailableIngredientItems();
/* 108 */       this.itemCount = this.items.length;
/*     */ 
/*     */       
/* 111 */       this.data = new BitSet(this.ingredientCount + this.itemCount + this.ingredientCount + this.ingredientCount * this.itemCount);
/* 112 */       for (int debug3 = 0; debug3 < this.ingredients.size(); debug3++) {
/* 113 */         IntList debug4 = ((Ingredient)this.ingredients.get(debug3)).getStackingIds();
/* 114 */         for (int debug5 = 0; debug5 < this.itemCount; debug5++) {
/* 115 */           if (debug4.contains(this.items[debug5])) {
/* 116 */             this.data.set(getIndex(true, debug5, debug3));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean tryPick(int debug1, @Nullable IntList debug2) {
/* 123 */       if (debug1 <= 0) {
/* 124 */         return true;
/*     */       }
/*     */       
/* 127 */       int debug3 = 0;
/* 128 */       while (dfs(debug1)) {
/* 129 */         StackedContents.this.take(this.items[this.path.getInt(0)], debug1);
/*     */         
/* 131 */         int i = this.path.size() - 1;
/* 132 */         setSatisfied(this.path.getInt(i));
/*     */         
/* 134 */         for (int j = 0; j < i; j++) {
/* 135 */           toggleResidual(((j & 0x1) == 0), this.path.get(j).intValue(), this.path.get(j + 1).intValue());
/*     */         }
/*     */         
/* 138 */         this.path.clear();
/* 139 */         this.data.clear(0, this.ingredientCount + this.itemCount);
/*     */         
/* 141 */         debug3++;
/*     */       } 
/*     */       
/* 144 */       boolean debug4 = (debug3 == this.ingredientCount);
/* 145 */       boolean debug5 = (debug4 && debug2 != null);
/* 146 */       if (debug5) {
/* 147 */         debug2.clear();
/*     */       }
/*     */ 
/*     */       
/* 151 */       this.data.clear(0, this.ingredientCount + this.itemCount + this.ingredientCount);
/*     */       
/* 153 */       int debug6 = 0;
/* 154 */       NonNullList<Ingredient> nonNullList = this.recipe.getIngredients();
/* 155 */       for (int debug8 = 0; debug8 < nonNullList.size(); debug8++) {
/* 156 */         if (debug5 && ((Ingredient)nonNullList.get(debug8)).isEmpty()) {
/* 157 */           debug2.add(0);
/*     */         } else {
/* 159 */           for (int debug9 = 0; debug9 < this.itemCount; debug9++) {
/* 160 */             if (hasResidual(false, debug6, debug9)) {
/* 161 */               toggleResidual(true, debug9, debug6);
/* 162 */               StackedContents.this.put(this.items[debug9], debug1);
/*     */               
/* 164 */               if (debug5) {
/* 165 */                 debug2.add(this.items[debug9]);
/*     */               }
/*     */             } 
/*     */           } 
/* 169 */           debug6++;
/*     */         } 
/*     */       } 
/*     */       
/* 173 */       return debug4;
/*     */     }
/*     */     
/*     */     private int[] getUniqueAvailableIngredientItems() {
/* 177 */       IntAVLTreeSet intAVLTreeSet = new IntAVLTreeSet();
/* 178 */       for (Ingredient debug3 : this.ingredients) {
/* 179 */         intAVLTreeSet.addAll((IntCollection)debug3.getStackingIds());
/*     */       }
/*     */       
/* 182 */       IntIterator debug2 = intAVLTreeSet.iterator();
/* 183 */       while (debug2.hasNext()) {
/* 184 */         if (!StackedContents.this.has(debug2.nextInt())) {
/* 185 */           debug2.remove();
/*     */         }
/*     */       } 
/* 188 */       return intAVLTreeSet.toIntArray();
/*     */     }
/*     */     
/*     */     private boolean dfs(int debug1) {
/* 192 */       int debug2 = this.itemCount;
/* 193 */       for (int debug3 = 0; debug3 < debug2; debug3++) {
/* 194 */         if (StackedContents.this.contents.get(this.items[debug3]) >= debug1) {
/* 195 */           visit(false, debug3);
/*     */           
/* 197 */           while (!this.path.isEmpty()) {
/* 198 */             int debug4 = this.path.size();
/* 199 */             boolean debug5 = ((debug4 & 0x1) == 1);
/*     */             
/* 201 */             int debug6 = this.path.getInt(debug4 - 1);
/* 202 */             if (!debug5 && !isSatisfied(debug6)) {
/*     */               break;
/*     */             }
/*     */             
/* 206 */             int debug7 = debug5 ? this.ingredientCount : debug2; int debug8;
/* 207 */             for (debug8 = 0; debug8 < debug7; debug8++) {
/* 208 */               if (!hasVisited(debug5, debug8) && hasConnection(debug5, debug6, debug8) && hasResidual(debug5, debug6, debug8)) {
/* 209 */                 visit(debug5, debug8);
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/* 214 */             debug8 = this.path.size();
/* 215 */             if (debug8 == debug4) {
/* 216 */               this.path.removeInt(debug8 - 1);
/*     */             }
/*     */           } 
/* 219 */           if (!this.path.isEmpty()) {
/* 220 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 225 */       return false;
/*     */     }
/*     */     
/*     */     private boolean isSatisfied(int debug1) {
/* 229 */       return this.data.get(getSatisfiedIndex(debug1));
/*     */     }
/*     */     
/*     */     private void setSatisfied(int debug1) {
/* 233 */       this.data.set(getSatisfiedIndex(debug1));
/*     */     }
/*     */     
/*     */     private int getSatisfiedIndex(int debug1) {
/* 237 */       return this.ingredientCount + this.itemCount + debug1;
/*     */     }
/*     */     
/*     */     private boolean hasConnection(boolean debug1, int debug2, int debug3) {
/* 241 */       return this.data.get(getIndex(debug1, debug2, debug3));
/*     */     }
/*     */     
/*     */     private boolean hasResidual(boolean debug1, int debug2, int debug3) {
/* 245 */       return (debug1 != this.data.get(1 + getIndex(debug1, debug2, debug3)));
/*     */     }
/*     */     
/*     */     private void toggleResidual(boolean debug1, int debug2, int debug3) {
/* 249 */       this.data.flip(1 + getIndex(debug1, debug2, debug3));
/*     */     }
/*     */     
/*     */     private int getIndex(boolean debug1, int debug2, int debug3) {
/* 253 */       int debug4 = debug1 ? (debug2 * this.ingredientCount + debug3) : (debug3 * this.ingredientCount + debug2);
/* 254 */       return this.ingredientCount + this.itemCount + this.ingredientCount + 2 * debug4;
/*     */     }
/*     */     
/*     */     private void visit(boolean debug1, int debug2) {
/* 258 */       this.data.set(getVisitedIndex(debug1, debug2));
/* 259 */       this.path.add(debug2);
/*     */     }
/*     */     
/*     */     private boolean hasVisited(boolean debug1, int debug2) {
/* 263 */       return this.data.get(getVisitedIndex(debug1, debug2));
/*     */     }
/*     */     
/*     */     private int getVisitedIndex(boolean debug1, int debug2) {
/* 267 */       return (debug1 ? 0 : this.ingredientCount) + debug2;
/*     */     }
/*     */     
/*     */     public int tryPickAll(int debug1, @Nullable IntList debug2) {
/* 271 */       int debug5, debug3 = 0;
/* 272 */       int debug4 = Math.min(debug1, getMinIngredientCount()) + 1;
/*     */ 
/*     */       
/*     */       while (true) {
/* 276 */         debug5 = (debug3 + debug4) / 2;
/*     */         
/* 278 */         if (tryPick(debug5, null)) {
/* 279 */           if (debug4 - debug3 <= 1) {
/*     */             break;
/*     */           }
/* 282 */           debug3 = debug5; continue;
/*     */         } 
/* 284 */         debug4 = debug5;
/*     */       } 
/*     */ 
/*     */       
/* 288 */       if (debug5 > 0) {
/* 289 */         tryPick(debug5, debug2);
/*     */       }
/*     */       
/* 292 */       return debug5;
/*     */     }
/*     */     
/*     */     private int getMinIngredientCount() {
/* 296 */       int debug1 = Integer.MAX_VALUE;
/* 297 */       for (Ingredient debug3 : this.ingredients) {
/* 298 */         int debug4 = 0;
/* 299 */         for (IntListIterator<Integer> intListIterator = debug3.getStackingIds().iterator(); intListIterator.hasNext(); ) { int debug6 = ((Integer)intListIterator.next()).intValue();
/* 300 */           debug4 = Math.max(debug4, StackedContents.this.contents.get(debug6)); }
/*     */         
/* 302 */         if (debug1 > 0) {
/* 303 */           debug1 = Math.min(debug1, debug4);
/*     */         }
/*     */       } 
/* 306 */       return debug1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\player\StackedContents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */