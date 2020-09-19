/*     */ package net.minecraft.world.entity.ai.attributes;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class AttributeMap
/*     */ {
/*  22 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  24 */   private final Map<Attribute, AttributeInstance> attributes = Maps.newHashMap();
/*  25 */   private final Set<AttributeInstance> dirtyAttributes = Sets.newHashSet();
/*     */   private final AttributeSupplier supplier;
/*     */   
/*     */   public AttributeMap(AttributeSupplier debug1) {
/*  29 */     this.supplier = debug1;
/*     */   }
/*     */   
/*     */   private void onAttributeModified(AttributeInstance debug1) {
/*  33 */     if (debug1.getAttribute().isClientSyncable()) {
/*  34 */       this.dirtyAttributes.add(debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<AttributeInstance> getDirtyAttributes() {
/*  39 */     return this.dirtyAttributes;
/*     */   }
/*     */   
/*     */   public Collection<AttributeInstance> getSyncableAttributes() {
/*  43 */     return (Collection<AttributeInstance>)this.attributes.values().stream().filter(debug0 -> debug0.getAttribute().isClientSyncable()).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public AttributeInstance getInstance(Attribute debug1) {
/*  48 */     return this.attributes.computeIfAbsent(debug1, debug1 -> this.supplier.createInstance(this::onAttributeModified, debug1));
/*     */   }
/*     */   
/*     */   public boolean hasAttribute(Attribute debug1) {
/*  52 */     return (this.attributes.get(debug1) != null || this.supplier.hasAttribute(debug1));
/*     */   }
/*     */   
/*     */   public boolean hasModifier(Attribute debug1, UUID debug2) {
/*  56 */     AttributeInstance debug3 = this.attributes.get(debug1);
/*  57 */     return (debug3 != null) ? ((debug3.getModifier(debug2) != null)) : this.supplier.hasModifier(debug1, debug2);
/*     */   }
/*     */   
/*     */   public double getValue(Attribute debug1) {
/*  61 */     AttributeInstance debug2 = this.attributes.get(debug1);
/*  62 */     return (debug2 != null) ? debug2.getValue() : this.supplier.getValue(debug1);
/*     */   }
/*     */   
/*     */   public double getBaseValue(Attribute debug1) {
/*  66 */     AttributeInstance debug2 = this.attributes.get(debug1);
/*  67 */     return (debug2 != null) ? debug2.getBaseValue() : this.supplier.getBaseValue(debug1);
/*     */   }
/*     */   
/*     */   public double getModifierValue(Attribute debug1, UUID debug2) {
/*  71 */     AttributeInstance debug3 = this.attributes.get(debug1);
/*  72 */     return (debug3 != null) ? debug3.getModifier(debug2).getAmount() : this.supplier.getModifierValue(debug1, debug2);
/*     */   }
/*     */   
/*     */   public void removeAttributeModifiers(Multimap<Attribute, AttributeModifier> debug1) {
/*  76 */     debug1.asMap().forEach((debug1, debug2) -> {
/*     */           AttributeInstance debug3 = this.attributes.get(debug1);
/*     */           if (debug3 != null) {
/*     */             debug2.forEach(debug3::removeModifier);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void addTransientAttributeModifiers(Multimap<Attribute, AttributeModifier> debug1) {
/*  86 */     debug1.forEach((debug1, debug2) -> {
/*     */           AttributeInstance debug3 = getInstance(debug1);
/*     */           if (debug3 != null) {
/*     */             debug3.removeModifier(debug2);
/*     */             debug3.addTransientModifier(debug2);
/*     */           } 
/*     */         });
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
/*     */   public ListTag save() {
/* 106 */     ListTag debug1 = new ListTag();
/* 107 */     for (AttributeInstance debug3 : this.attributes.values()) {
/* 108 */       debug1.add(debug3.save());
/*     */     }
/* 110 */     return debug1;
/*     */   }
/*     */   
/*     */   public void load(ListTag debug1) {
/* 114 */     for (int debug2 = 0; debug2 < debug1.size(); debug2++) {
/* 115 */       CompoundTag debug3 = debug1.getCompound(debug2);
/* 116 */       String debug4 = debug3.getString("Name");
/* 117 */       Util.ifElse(Registry.ATTRIBUTE.getOptional(ResourceLocation.tryParse(debug4)), debug2 -> {
/*     */             AttributeInstance debug3 = getInstance(debug2);
/*     */             if (debug3 != null)
/*     */               debug3.load(debug1); 
/*     */           }() -> LOGGER.warn("Ignoring unknown attribute '{}'", debug0));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\attributes\AttributeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */