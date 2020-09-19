/*     */ package net.minecraft.world.level.block.state.properties;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Collection;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ 
/*     */ public abstract class Property<T extends Comparable<T>>
/*     */ {
/*     */   private final Class<T> clazz;
/*     */   private final String name;
/*     */   
/*     */   protected Property(String debug1, Class<T> debug2) {
/*  17 */     this.codec = Codec.STRING.comapFlatMap(debug1 -> (DataResult)getValue(debug1).<DataResult>map(DataResult::success).orElseGet(()), this::getName);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  22 */     this.valueCodec = this.codec.xmap(this::value, Value::value);
/*     */ 
/*     */     
/*  25 */     this.clazz = debug2;
/*  26 */     this.name = debug1;
/*     */   }
/*     */   private Integer hashCode; private final Codec<T> codec; private final Codec<Value<T>> valueCodec;
/*     */   public Value<T> value(T debug1) {
/*  30 */     return new Value<>(this, (Comparable)debug1);
/*     */   }
/*     */   
/*     */   public Value<T> value(StateHolder<?, ?> debug1) {
/*  34 */     return new Value<>(this, debug1.getValue(this));
/*     */   }
/*     */   
/*     */   public Stream<Value<T>> getAllValues() {
/*  38 */     return getPossibleValues().stream().map(this::value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Codec<Value<T>> valueCodec() {
/*  46 */     return this.valueCodec;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  50 */     return this.name;
/*     */   }
/*     */   
/*     */   public Class<T> getValueClass() {
/*  54 */     return this.clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  65 */     return MoreObjects.toStringHelper(this)
/*  66 */       .add("name", this.name)
/*  67 */       .add("clazz", this.clazz)
/*  68 */       .add("values", getPossibleValues())
/*  69 */       .toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/*  74 */     if (this == debug1) {
/*  75 */       return true;
/*     */     }
/*     */     
/*  78 */     if (debug1 instanceof Property) {
/*  79 */       Property<?> debug2 = (Property)debug1;
/*     */       
/*  81 */       return (this.clazz.equals(debug2.clazz) && this.name.equals(debug2.name));
/*     */     } 
/*     */     
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/*  89 */     if (this.hashCode == null) {
/*  90 */       this.hashCode = Integer.valueOf(generateHashCode());
/*     */     }
/*  92 */     return this.hashCode.intValue();
/*     */   }
/*     */   
/*     */   public int generateHashCode() {
/*  96 */     return 31 * this.clazz.hashCode() + this.name.hashCode();
/*     */   }
/*     */   
/*     */   public abstract Collection<T> getPossibleValues();
/*     */   
/*     */   public abstract String getName(T paramT);
/*     */   
/*     */   public abstract Optional<T> getValue(String paramString);
/*     */   
/*     */   public static final class Value<T extends Comparable<T>> { private final Property<T> property;
/*     */     private final T value;
/*     */     
/*     */     private Value(Property<T> debug1, T debug2) {
/* 109 */       if (!debug1.getPossibleValues().contains(debug2)) {
/* 110 */         throw new IllegalArgumentException("Value " + debug2 + " does not belong to property " + debug1);
/*     */       }
/* 112 */       this.property = debug1;
/* 113 */       this.value = debug2;
/*     */     }
/*     */     
/*     */     public Property<T> getProperty() {
/* 117 */       return this.property;
/*     */     }
/*     */     
/*     */     public T value() {
/* 121 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 126 */       return this.property.getName() + "=" + this.property.getName(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object debug1) {
/* 131 */       if (this == debug1) {
/* 132 */         return true;
/*     */       }
/* 134 */       if (!(debug1 instanceof Value)) {
/* 135 */         return false;
/*     */       }
/*     */       
/* 138 */       Value<?> debug2 = (Value)debug1;
/* 139 */       return (this.property == debug2.property && this.value.equals(debug2.value));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 144 */       int debug1 = this.property.hashCode();
/* 145 */       debug1 = 31 * debug1 + this.value.hashCode();
/* 146 */       return debug1;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\Property.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */