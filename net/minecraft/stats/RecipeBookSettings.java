/*     */ package net.minecraft.stats;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.world.inventory.RecipeBookType;
/*     */ 
/*     */ public final class RecipeBookSettings {
/*  14 */   private static final Map<RecipeBookType, Pair<String, String>> TAG_FIELDS = (Map<RecipeBookType, Pair<String, String>>)ImmutableMap.of(RecipeBookType.CRAFTING, 
/*  15 */       Pair.of("isGuiOpen", "isFilteringCraftable"), RecipeBookType.FURNACE, 
/*  16 */       Pair.of("isFurnaceGuiOpen", "isFurnaceFilteringCraftable"), RecipeBookType.BLAST_FURNACE, 
/*  17 */       Pair.of("isBlastingFurnaceGuiOpen", "isBlastingFurnaceFilteringCraftable"), RecipeBookType.SMOKER, 
/*  18 */       Pair.of("isSmokerGuiOpen", "isSmokerFilteringCraftable"));
/*     */   private final Map<RecipeBookType, TypeSettings> states;
/*     */   
/*     */   static final class TypeSettings {
/*     */     private boolean open;
/*     */     private boolean filtering;
/*     */     
/*     */     public TypeSettings(boolean debug1, boolean debug2) {
/*  26 */       this.open = debug1;
/*  27 */       this.filtering = debug2;
/*     */     }
/*     */     
/*     */     public TypeSettings copy() {
/*  31 */       return new TypeSettings(this.open, this.filtering);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object debug1) {
/*  36 */       if (this == debug1) {
/*  37 */         return true;
/*     */       }
/*     */       
/*  40 */       if (debug1 instanceof TypeSettings) {
/*  41 */         TypeSettings debug2 = (TypeSettings)debug1;
/*  42 */         return (this.open == debug2.open && this.filtering == debug2.filtering);
/*     */       } 
/*  44 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  49 */       int debug1 = this.open ? 1 : 0;
/*  50 */       debug1 = 31 * debug1 + (this.filtering ? 1 : 0);
/*  51 */       return debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  56 */       return "[open=" + this.open + ", filtering=" + this.filtering + ']';
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private RecipeBookSettings(Map<RecipeBookType, TypeSettings> debug1) {
/*  63 */     this.states = debug1;
/*     */   }
/*     */   
/*     */   public RecipeBookSettings() {
/*  67 */     this((Map<RecipeBookType, TypeSettings>)Util.make(Maps.newEnumMap(RecipeBookType.class), debug0 -> {
/*     */             for (RecipeBookType debug4 : RecipeBookType.values()) {
/*     */               debug0.put(debug4, new TypeSettings(false, false));
/*     */             }
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOpen(RecipeBookType debug1, boolean debug2) {
/*  79 */     (this.states.get(debug1)).open = debug2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFiltering(RecipeBookType debug1, boolean debug2) {
/*  87 */     (this.states.get(debug1)).filtering = debug2;
/*     */   }
/*     */   
/*     */   public static RecipeBookSettings read(FriendlyByteBuf debug0) {
/*  91 */     Map<RecipeBookType, TypeSettings> debug1 = Maps.newEnumMap(RecipeBookType.class);
/*  92 */     for (RecipeBookType debug5 : RecipeBookType.values()) {
/*  93 */       boolean debug6 = debug0.readBoolean();
/*  94 */       boolean debug7 = debug0.readBoolean();
/*  95 */       debug1.put(debug5, new TypeSettings(debug6, debug7));
/*     */     } 
/*  97 */     return new RecipeBookSettings(debug1);
/*     */   }
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) {
/* 101 */     for (RecipeBookType debug5 : RecipeBookType.values()) {
/* 102 */       TypeSettings debug6 = this.states.get(debug5);
/* 103 */       if (debug6 == null) {
/* 104 */         debug1.writeBoolean(false);
/* 105 */         debug1.writeBoolean(false);
/*     */       } else {
/* 107 */         debug1.writeBoolean(debug6.open);
/* 108 */         debug1.writeBoolean(debug6.filtering);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static RecipeBookSettings read(CompoundTag debug0) {
/* 114 */     Map<RecipeBookType, TypeSettings> debug1 = Maps.newEnumMap(RecipeBookType.class);
/* 115 */     TAG_FIELDS.forEach((debug2, debug3) -> {
/*     */           boolean debug4 = debug0.getBoolean((String)debug3.getFirst());
/*     */           boolean debug5 = debug0.getBoolean((String)debug3.getSecond());
/*     */           debug1.put(debug2, new TypeSettings(debug4, debug5));
/*     */         });
/* 120 */     return new RecipeBookSettings(debug1);
/*     */   }
/*     */   
/*     */   public void write(CompoundTag debug1) {
/* 124 */     TAG_FIELDS.forEach((debug2, debug3) -> {
/*     */           TypeSettings debug4 = this.states.get(debug2);
/*     */           debug1.putBoolean((String)debug3.getFirst(), debug4.open);
/*     */           debug1.putBoolean((String)debug3.getSecond(), debug4.filtering);
/*     */         });
/*     */   }
/*     */   
/*     */   public RecipeBookSettings copy() {
/* 132 */     Map<RecipeBookType, TypeSettings> debug1 = Maps.newEnumMap(RecipeBookType.class);
/* 133 */     for (RecipeBookType debug5 : RecipeBookType.values()) {
/* 134 */       TypeSettings debug6 = this.states.get(debug5);
/* 135 */       debug1.put(debug5, debug6.copy());
/*     */     } 
/* 137 */     return new RecipeBookSettings(debug1);
/*     */   }
/*     */   
/*     */   public void replaceFrom(RecipeBookSettings debug1) {
/* 141 */     this.states.clear();
/* 142 */     for (RecipeBookType debug5 : RecipeBookType.values()) {
/* 143 */       TypeSettings debug6 = debug1.states.get(debug5);
/* 144 */       this.states.put(debug5, debug6.copy());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 150 */     return (this == debug1 || (debug1 instanceof RecipeBookSettings && this.states.equals(((RecipeBookSettings)debug1).states)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 155 */     return this.states.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\stats\RecipeBookSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */