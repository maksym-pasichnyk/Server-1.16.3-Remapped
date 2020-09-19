/*    */ package net.minecraft.commands.arguments.blocks;
/*    */ 
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlockInput
/*    */   implements Predicate<BlockInWorld>
/*    */ {
/*    */   private final BlockState state;
/*    */   
/*    */   public BlockInput(BlockState debug1, Set<Property<?>> debug2, @Nullable CompoundTag debug3) {
/* 24 */     this.state = debug1;
/* 25 */     this.properties = debug2;
/* 26 */     this.tag = debug3;
/*    */   } private final Set<Property<?>> properties; @Nullable
/*    */   private final CompoundTag tag;
/*    */   public BlockState getState() {
/* 30 */     return this.state;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(BlockInWorld debug1) {
/* 44 */     BlockState debug2 = debug1.getState();
/*    */     
/* 46 */     if (!debug2.is(this.state.getBlock())) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     for (Property<?> debug4 : this.properties) {
/* 51 */       if (debug2.getValue(debug4) != this.state.getValue(debug4)) {
/* 52 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 56 */     if (this.tag != null) {
/* 57 */       BlockEntity debug3 = debug1.getEntity();
/* 58 */       return (debug3 != null && NbtUtils.compareNbt((Tag)this.tag, (Tag)debug3.save(new CompoundTag()), true));
/*    */     } 
/*    */     
/* 61 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean place(ServerLevel debug1, BlockPos debug2, int debug3) {
/* 69 */     BlockState debug4 = Block.updateFromNeighbourShapes(this.state, (LevelAccessor)debug1, debug2);
/* 70 */     if (debug4.isAir()) {
/* 71 */       debug4 = this.state;
/*    */     }
/* 73 */     if (!debug1.setBlock(debug2, debug4, debug3)) {
/* 74 */       return false;
/*    */     }
/*    */     
/* 77 */     if (this.tag != null) {
/* 78 */       BlockEntity debug5 = debug1.getBlockEntity(debug2);
/* 79 */       if (debug5 != null) {
/* 80 */         CompoundTag debug6 = this.tag.copy();
/* 81 */         debug6.putInt("x", debug2.getX());
/* 82 */         debug6.putInt("y", debug2.getY());
/* 83 */         debug6.putInt("z", debug2.getZ());
/* 84 */         debug5.load(debug4, debug6);
/*    */       } 
/*    */     } 
/*    */     
/* 88 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\blocks\BlockInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */