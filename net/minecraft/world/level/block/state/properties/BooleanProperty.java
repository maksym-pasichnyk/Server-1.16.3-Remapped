/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Collection;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class BooleanProperty
/*    */   extends Property<Boolean> {
/*    */   private final ImmutableSet<Boolean> values;
/*    */   
/*    */   protected BooleanProperty(String debug1) {
/* 12 */     super(debug1, Boolean.class);
/* 13 */     this.values = ImmutableSet.of(Boolean.valueOf(true), Boolean.valueOf(false));
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<Boolean> getPossibleValues() {
/* 18 */     return (Collection<Boolean>)this.values;
/*    */   }
/*    */   
/*    */   public static BooleanProperty create(String debug0) {
/* 22 */     return new BooleanProperty(debug0);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<Boolean> getValue(String debug1) {
/* 27 */     if ("true".equals(debug1) || "false".equals(debug1)) {
/* 28 */       return Optional.of(Boolean.valueOf(debug1));
/*    */     }
/*    */     
/* 31 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName(Boolean debug1) {
/* 36 */     return debug1.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object debug1) {
/* 41 */     if (this == debug1) {
/* 42 */       return true;
/*    */     }
/*    */     
/* 45 */     if (debug1 instanceof BooleanProperty && super.equals(debug1)) {
/* 46 */       BooleanProperty debug2 = (BooleanProperty)debug1;
/*    */       
/* 48 */       return this.values.equals(debug2.values);
/*    */     } 
/*    */     
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int generateHashCode() {
/* 56 */     return 31 * super.generateHashCode() + this.values.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\BooleanProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */