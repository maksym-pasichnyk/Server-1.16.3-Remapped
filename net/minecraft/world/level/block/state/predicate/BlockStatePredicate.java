/*    */ package net.minecraft.world.level.block.state.predicate;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlockStatePredicate
/*    */   implements Predicate<BlockState>
/*    */ {
/*    */   public static final Predicate<BlockState> ANY = debug0 -> true;
/*    */   private final StateDefinition<Block, BlockState> definition;
/* 17 */   private final Map<Property<?>, Predicate<Object>> properties = Maps.newHashMap();
/*    */   
/*    */   private BlockStatePredicate(StateDefinition<Block, BlockState> debug1) {
/* 20 */     this.definition = debug1;
/*    */   }
/*    */   
/*    */   public static BlockStatePredicate forBlock(Block debug0) {
/* 24 */     return new BlockStatePredicate(debug0.getStateDefinition());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(@Nullable BlockState debug1) {
/* 33 */     if (debug1 == null || !debug1.getBlock().equals(this.definition.getOwner())) {
/* 34 */       return false;
/*    */     }
/*    */     
/* 37 */     if (this.properties.isEmpty()) {
/* 38 */       return true;
/*    */     }
/*    */     
/* 41 */     for (Map.Entry<Property<?>, Predicate<Object>> debug3 : this.properties.entrySet()) {
/* 42 */       if (!applies(debug1, (Property<Comparable>)debug3.getKey(), debug3.getValue())) {
/* 43 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 47 */     return true;
/*    */   }
/*    */   
/*    */   protected <T extends Comparable<T>> boolean applies(BlockState debug1, Property<T> debug2, Predicate<Object> debug3) {
/* 51 */     Comparable comparable = debug1.getValue(debug2);
/* 52 */     return debug3.test(comparable);
/*    */   }
/*    */   
/*    */   public <V extends Comparable<V>> BlockStatePredicate where(Property<V> debug1, Predicate<Object> debug2) {
/* 56 */     if (!this.definition.getProperties().contains(debug1)) {
/* 57 */       throw new IllegalArgumentException(this.definition + " cannot support property " + debug1);
/*    */     }
/* 59 */     this.properties.put(debug1, debug2);
/* 60 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\predicate\BlockStatePredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */