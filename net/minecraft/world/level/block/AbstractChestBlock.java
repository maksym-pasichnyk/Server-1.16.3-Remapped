/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractChestBlock<E extends BlockEntity>
/*    */   extends BaseEntityBlock
/*    */ {
/*    */   protected final Supplier<BlockEntityType<? extends E>> blockEntityType;
/*    */   
/*    */   protected AbstractChestBlock(BlockBehaviour.Properties debug1, Supplier<BlockEntityType<? extends E>> debug2) {
/* 16 */     super(debug1);
/* 17 */     this.blockEntityType = debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\AbstractChestBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */