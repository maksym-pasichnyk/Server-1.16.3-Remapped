/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public class DirectionProperty
/*    */   extends EnumProperty<Direction>
/*    */ {
/*    */   protected DirectionProperty(String debug1, Collection<Direction> debug2) {
/* 14 */     super(debug1, Direction.class, debug2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DirectionProperty create(String debug0, Predicate<Direction> debug1) {
/* 22 */     return create(debug0, (Collection<Direction>)Arrays.<Direction>stream(Direction.values()).filter(debug1).collect(Collectors.toList()));
/*    */   }
/*    */   
/*    */   public static DirectionProperty create(String debug0, Direction... debug1) {
/* 26 */     return create(debug0, Lists.newArrayList((Object[])debug1));
/*    */   }
/*    */   
/*    */   public static DirectionProperty create(String debug0, Collection<Direction> debug1) {
/* 30 */     return new DirectionProperty(debug0, debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\DirectionProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */