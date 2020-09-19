/*    */ package net.minecraft.world.entity.ai.attributes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Consumer;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class AttributeSupplier
/*    */ {
/*    */   private final Map<Attribute, AttributeInstance> instances;
/*    */   
/*    */   public AttributeSupplier(Map<Attribute, AttributeInstance> debug1) {
/* 16 */     this.instances = (Map<Attribute, AttributeInstance>)ImmutableMap.copyOf(debug1);
/*    */   }
/*    */   
/*    */   private AttributeInstance getAttributeInstance(Attribute debug1) {
/* 20 */     AttributeInstance debug2 = this.instances.get(debug1);
/* 21 */     if (debug2 == null) {
/* 22 */       throw new IllegalArgumentException("Can't find attribute " + Registry.ATTRIBUTE.getKey(debug1));
/*    */     }
/* 24 */     return debug2;
/*    */   }
/*    */   
/*    */   public double getValue(Attribute debug1) {
/* 28 */     return getAttributeInstance(debug1).getValue();
/*    */   }
/*    */   
/*    */   public double getBaseValue(Attribute debug1) {
/* 32 */     return getAttributeInstance(debug1).getBaseValue();
/*    */   }
/*    */   
/*    */   public double getModifierValue(Attribute debug1, UUID debug2) {
/* 36 */     AttributeModifier debug3 = getAttributeInstance(debug1).getModifier(debug2);
/* 37 */     if (debug3 == null) {
/* 38 */       throw new IllegalArgumentException("Can't find modifier " + debug2 + " on attribute " + Registry.ATTRIBUTE.getKey(debug1));
/*    */     }
/* 40 */     return debug3.getAmount();
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public AttributeInstance createInstance(Consumer<AttributeInstance> debug1, Attribute debug2) {
/* 45 */     AttributeInstance debug3 = this.instances.get(debug2);
/* 46 */     if (debug3 == null) {
/* 47 */       return null;
/*    */     }
/* 49 */     AttributeInstance debug4 = new AttributeInstance(debug2, debug1);
/* 50 */     debug4.replaceFrom(debug3);
/* 51 */     return debug4;
/*    */   }
/*    */   
/*    */   public static Builder builder() {
/* 55 */     return new Builder();
/*    */   }
/*    */   
/*    */   public boolean hasAttribute(Attribute debug1) {
/* 59 */     return this.instances.containsKey(debug1);
/*    */   }
/*    */   
/*    */   public boolean hasModifier(Attribute debug1, UUID debug2) {
/* 63 */     AttributeInstance debug3 = this.instances.get(debug1);
/* 64 */     return (debug3 != null && debug3.getModifier(debug2) != null);
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 68 */     private final Map<Attribute, AttributeInstance> builder = Maps.newHashMap();
/*    */     private boolean instanceFrozen;
/*    */     
/*    */     private AttributeInstance create(Attribute debug1) {
/* 72 */       AttributeInstance debug2 = new AttributeInstance(debug1, debug2 -> {
/*    */             if (this.instanceFrozen) {
/*    */               throw new UnsupportedOperationException("Tried to change value for default attribute instance: " + Registry.ATTRIBUTE.getKey(debug1));
/*    */             }
/*    */           });
/* 77 */       this.builder.put(debug1, debug2);
/* 78 */       return debug2;
/*    */     }
/*    */     
/*    */     public Builder add(Attribute debug1) {
/* 82 */       create(debug1);
/* 83 */       return this;
/*    */     }
/*    */     
/*    */     public Builder add(Attribute debug1, double debug2) {
/* 87 */       AttributeInstance debug4 = create(debug1);
/* 88 */       debug4.setBaseValue(debug2);
/* 89 */       return this;
/*    */     }
/*    */     
/*    */     public AttributeSupplier build() {
/* 93 */       this.instanceFrozen = true;
/* 94 */       return new AttributeSupplier(this.builder);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\attributes\AttributeSupplier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */