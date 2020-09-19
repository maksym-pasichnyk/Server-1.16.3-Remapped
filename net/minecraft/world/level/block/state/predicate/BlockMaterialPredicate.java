/*    */ package net.minecraft.world.level.block.state.predicate;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Material;
/*    */ 
/*    */ public class BlockMaterialPredicate
/*    */   implements Predicate<BlockState> {
/* 10 */   private static final BlockMaterialPredicate AIR = new BlockMaterialPredicate(Material.AIR)
/*    */     {
/*    */       public boolean test(@Nullable BlockState debug1) {
/* 13 */         return (debug1 != null && debug1.isAir());
/*    */       }
/*    */     };
/*    */   
/*    */   private final Material material;
/*    */   
/*    */   private BlockMaterialPredicate(Material debug1) {
/* 20 */     this.material = debug1;
/*    */   }
/*    */   
/*    */   public static BlockMaterialPredicate forMaterial(Material debug0) {
/* 24 */     return (debug0 == Material.AIR) ? AIR : new BlockMaterialPredicate(debug0);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(@Nullable BlockState debug1) {
/* 29 */     return (debug1 != null && debug1.getMaterial() == this.material);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\predicate\BlockMaterialPredicate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */