/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Collection;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class IntegerProperty
/*    */   extends Property<Integer> {
/*    */   private final ImmutableSet<Integer> values;
/*    */   
/*    */   protected IntegerProperty(String debug1, int debug2, int debug3) {
/* 14 */     super(debug1, Integer.class);
/* 15 */     if (debug2 < 0) {
/* 16 */       throw new IllegalArgumentException("Min value of " + debug1 + " must be 0 or greater");
/*    */     }
/* 18 */     if (debug3 <= debug2) {
/* 19 */       throw new IllegalArgumentException("Max value of " + debug1 + " must be greater than min (" + debug2 + ")");
/*    */     }
/*    */ 
/*    */     
/* 23 */     Set<Integer> debug4 = Sets.newHashSet();
/* 24 */     for (int debug5 = debug2; debug5 <= debug3; debug5++) {
/* 25 */       debug4.add(Integer.valueOf(debug5));
/*    */     }
/*    */     
/* 28 */     this.values = ImmutableSet.copyOf(debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<Integer> getPossibleValues() {
/* 33 */     return (Collection<Integer>)this.values;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 38 */     if (this == debug1) {
/* 39 */       return true;
/*    */     }
/*    */     
/* 42 */     if (debug1 instanceof IntegerProperty && super.equals(debug1)) {
/* 43 */       IntegerProperty debug2 = (IntegerProperty)debug1;
/*    */       
/* 45 */       return this.values.equals(debug2.values);
/*    */     } 
/*    */     
/* 48 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int generateHashCode() {
/* 53 */     return 31 * super.generateHashCode() + this.values.hashCode();
/*    */   }
/*    */   
/*    */   public static IntegerProperty create(String debug0, int debug1, int debug2) {
/* 57 */     return new IntegerProperty(debug0, debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Integer> getValue(String debug1) {
/*    */     try {
/* 63 */       Integer debug2 = Integer.valueOf(debug1);
/*    */       
/* 65 */       return this.values.contains(debug2) ? Optional.<Integer>of(debug2) : Optional.<Integer>empty();
/* 66 */     } catch (NumberFormatException debug2) {
/* 67 */       return Optional.empty();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName(Integer debug1) {
/* 73 */     return debug1.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\IntegerProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */