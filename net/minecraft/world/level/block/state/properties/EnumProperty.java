/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import com.google.common.base.Predicates;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public class EnumProperty<T extends Enum<T> & StringRepresentable>
/*    */   extends Property<T> {
/*    */   private final ImmutableSet<T> values;
/* 18 */   private final Map<String, T> names = Maps.newHashMap();
/*    */   
/*    */   protected EnumProperty(String debug1, Class<T> debug2, Collection<T> debug3) {
/* 21 */     super(debug1, debug2);
/* 22 */     this.values = ImmutableSet.copyOf(debug3);
/*    */     
/* 24 */     for (Enum enum_ : debug3) {
/* 25 */       String debug6 = ((StringRepresentable)enum_).getSerializedName();
/* 26 */       if (this.names.containsKey(debug6)) {
/* 27 */         throw new IllegalArgumentException("Multiple values have the same name '" + debug6 + "'");
/*    */       }
/* 29 */       this.names.put(debug6, (T)enum_);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<T> getPossibleValues() {
/* 35 */     return (Collection<T>)this.values;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<T> getValue(String debug1) {
/* 40 */     return Optional.ofNullable(this.names.get(debug1));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName(T debug1) {
/* 45 */     return ((StringRepresentable)debug1).getSerializedName();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 50 */     if (this == debug1) {
/* 51 */       return true;
/*    */     }
/*    */     
/* 54 */     if (debug1 instanceof EnumProperty && super.equals(debug1)) {
/* 55 */       EnumProperty<?> debug2 = (EnumProperty)debug1;
/* 56 */       return (this.values.equals(debug2.values) && this.names.equals(debug2.names));
/*    */     } 
/*    */     
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int generateHashCode() {
/* 64 */     int debug1 = super.generateHashCode();
/* 65 */     debug1 = 31 * debug1 + this.values.hashCode();
/* 66 */     debug1 = 31 * debug1 + this.names.hashCode();
/* 67 */     return debug1;
/*    */   }
/*    */   
/*    */   public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String debug0, Class<T> debug1) {
/* 71 */     return create(debug0, debug1, (Predicate<T>)Predicates.alwaysTrue());
/*    */   }
/*    */   
/*    */   public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String debug0, Class<T> debug1, Predicate<T> debug2) {
/* 75 */     return create(debug0, debug1, (Collection<T>)Arrays.<T>stream(debug1.getEnumConstants()).filter(debug2).collect(Collectors.toList()));
/*    */   }
/*    */   
/*    */   public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String debug0, Class<T> debug1, T... debug2) {
/* 79 */     return create(debug0, debug1, Lists.newArrayList((Object[])debug2));
/*    */   }
/*    */   
/*    */   public static <T extends Enum<T> & StringRepresentable> EnumProperty<T> create(String debug0, Class<T> debug1, Collection<T> debug2) {
/* 83 */     return new EnumProperty<>(debug0, debug1, debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\EnumProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */