/*     */ package net.minecraft.world.entity.ai.attributes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ 
/*     */ public class AttributeInstance
/*     */ {
/*     */   private final Attribute attribute;
/*  23 */   private final Map<AttributeModifier.Operation, Set<AttributeModifier>> modifiersByOperation = Maps.newEnumMap(AttributeModifier.Operation.class);
/*  24 */   private final Map<UUID, AttributeModifier> modifierById = (Map<UUID, AttributeModifier>)new Object2ObjectArrayMap();
/*  25 */   private final Set<AttributeModifier> permanentModifiers = (Set<AttributeModifier>)new ObjectArraySet();
/*     */   private double baseValue;
/*     */   private boolean dirty = true;
/*     */   private double cachedValue;
/*     */   private final Consumer<AttributeInstance> onDirty;
/*     */   
/*     */   public AttributeInstance(Attribute debug1, Consumer<AttributeInstance> debug2) {
/*  32 */     this.attribute = debug1;
/*  33 */     this.onDirty = debug2;
/*  34 */     this.baseValue = debug1.getDefaultValue();
/*     */   }
/*     */   
/*     */   public Attribute getAttribute() {
/*  38 */     return this.attribute;
/*     */   }
/*     */   
/*     */   public double getBaseValue() {
/*  42 */     return this.baseValue;
/*     */   }
/*     */   
/*     */   public void setBaseValue(double debug1) {
/*  46 */     if (debug1 == this.baseValue) {
/*     */       return;
/*     */     }
/*  49 */     this.baseValue = debug1;
/*  50 */     setDirty();
/*     */   }
/*     */   
/*     */   public Set<AttributeModifier> getModifiers(AttributeModifier.Operation debug1) {
/*  54 */     return this.modifiersByOperation.computeIfAbsent(debug1, debug0 -> Sets.newHashSet());
/*     */   }
/*     */   
/*     */   public Set<AttributeModifier> getModifiers() {
/*  58 */     return (Set<AttributeModifier>)ImmutableSet.copyOf(this.modifierById.values());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public AttributeModifier getModifier(UUID debug1) {
/*  63 */     return this.modifierById.get(debug1);
/*     */   }
/*     */   
/*     */   public boolean hasModifier(AttributeModifier debug1) {
/*  67 */     return (this.modifierById.get(debug1.getId()) != null);
/*     */   }
/*     */   
/*     */   private void addModifier(AttributeModifier debug1) {
/*  71 */     AttributeModifier debug2 = this.modifierById.putIfAbsent(debug1.getId(), debug1);
/*  72 */     if (debug2 != null) {
/*  73 */       throw new IllegalArgumentException("Modifier is already applied on this attribute!");
/*     */     }
/*     */     
/*  76 */     getModifiers(debug1.getOperation()).add(debug1);
/*  77 */     setDirty();
/*     */   }
/*     */   
/*     */   public void addTransientModifier(AttributeModifier debug1) {
/*  81 */     addModifier(debug1);
/*     */   }
/*     */   
/*     */   public void addPermanentModifier(AttributeModifier debug1) {
/*  85 */     addModifier(debug1);
/*  86 */     this.permanentModifiers.add(debug1);
/*     */   }
/*     */   
/*     */   protected void setDirty() {
/*  90 */     this.dirty = true;
/*  91 */     this.onDirty.accept(this);
/*     */   }
/*     */   
/*     */   public void removeModifier(AttributeModifier debug1) {
/*  95 */     getModifiers(debug1.getOperation()).remove(debug1);
/*  96 */     this.modifierById.remove(debug1.getId());
/*  97 */     this.permanentModifiers.remove(debug1);
/*  98 */     setDirty();
/*     */   }
/*     */   
/*     */   public void removeModifier(UUID debug1) {
/* 102 */     AttributeModifier debug2 = getModifier(debug1);
/* 103 */     if (debug2 != null) {
/* 104 */       removeModifier(debug2);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean removePermanentModifier(UUID debug1) {
/* 109 */     AttributeModifier debug2 = getModifier(debug1);
/* 110 */     if (debug2 != null && this.permanentModifiers.contains(debug2)) {
/* 111 */       removeModifier(debug2);
/* 112 */       return true;
/*     */     } 
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getValue() {
/* 124 */     if (this.dirty) {
/* 125 */       this.cachedValue = calculateValue();
/* 126 */       this.dirty = false;
/*     */     } 
/*     */     
/* 129 */     return this.cachedValue;
/*     */   }
/*     */   
/*     */   private double calculateValue() {
/* 133 */     double debug1 = getBaseValue();
/*     */     
/* 135 */     for (AttributeModifier debug4 : getModifiersOrEmpty(AttributeModifier.Operation.ADDITION)) {
/* 136 */       debug1 += debug4.getAmount();
/*     */     }
/*     */     
/* 139 */     double debug3 = debug1;
/*     */     
/* 141 */     for (AttributeModifier debug6 : getModifiersOrEmpty(AttributeModifier.Operation.MULTIPLY_BASE)) {
/* 142 */       debug3 += debug1 * debug6.getAmount();
/*     */     }
/*     */     
/* 145 */     for (AttributeModifier debug6 : getModifiersOrEmpty(AttributeModifier.Operation.MULTIPLY_TOTAL)) {
/* 146 */       debug3 *= 1.0D + debug6.getAmount();
/*     */     }
/*     */     
/* 149 */     return this.attribute.sanitizeValue(debug3);
/*     */   }
/*     */   
/*     */   private Collection<AttributeModifier> getModifiersOrEmpty(AttributeModifier.Operation debug1) {
/* 153 */     return this.modifiersByOperation.getOrDefault(debug1, Collections.emptySet());
/*     */   }
/*     */   
/*     */   public void replaceFrom(AttributeInstance debug1) {
/* 157 */     this.baseValue = debug1.baseValue;
/*     */     
/* 159 */     this.modifierById.clear();
/* 160 */     this.modifierById.putAll(debug1.modifierById);
/*     */     
/* 162 */     this.permanentModifiers.clear();
/* 163 */     this.permanentModifiers.addAll(debug1.permanentModifiers);
/*     */     
/* 165 */     this.modifiersByOperation.clear();
/* 166 */     debug1.modifiersByOperation.forEach((debug1, debug2) -> getModifiers(debug1).addAll(debug2));
/*     */ 
/*     */     
/* 169 */     setDirty();
/*     */   }
/*     */   
/*     */   public CompoundTag save() {
/* 173 */     CompoundTag debug1 = new CompoundTag();
/*     */     
/* 175 */     debug1.putString("Name", Registry.ATTRIBUTE.getKey(this.attribute).toString());
/* 176 */     debug1.putDouble("Base", this.baseValue);
/*     */     
/* 178 */     if (!this.permanentModifiers.isEmpty()) {
/* 179 */       ListTag debug2 = new ListTag();
/* 180 */       for (AttributeModifier debug4 : this.permanentModifiers) {
/* 181 */         debug2.add(debug4.save());
/*     */       }
/* 183 */       debug1.put("Modifiers", (Tag)debug2);
/*     */     } 
/* 185 */     return debug1;
/*     */   }
/*     */   
/*     */   public void load(CompoundTag debug1) {
/* 189 */     this.baseValue = debug1.getDouble("Base");
/* 190 */     if (debug1.contains("Modifiers", 9)) {
/* 191 */       ListTag debug2 = debug1.getList("Modifiers", 10);
/*     */       
/* 193 */       for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/* 194 */         AttributeModifier debug4 = AttributeModifier.load(debug2.getCompound(debug3));
/* 195 */         if (debug4 != null) {
/*     */ 
/*     */           
/* 198 */           this.modifierById.put(debug4.getId(), debug4);
/* 199 */           getModifiers(debug4.getOperation()).add(debug4);
/* 200 */           this.permanentModifiers.add(debug4);
/*     */         } 
/*     */       } 
/* 203 */     }  setDirty();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\attributes\AttributeInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */