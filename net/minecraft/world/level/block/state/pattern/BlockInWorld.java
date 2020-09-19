/*    */ package net.minecraft.world.level.block.state.pattern;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockInWorld
/*    */ {
/*    */   private final LevelReader level;
/*    */   private final BlockPos pos;
/*    */   private final boolean loadChunks;
/*    */   private BlockState state;
/*    */   private BlockEntity entity;
/*    */   private boolean cachedEntity;
/*    */   
/*    */   public BlockInWorld(LevelReader debug1, BlockPos debug2, boolean debug3) {
/* 20 */     this.level = debug1;
/* 21 */     this.pos = debug2.immutable();
/* 22 */     this.loadChunks = debug3;
/*    */   }
/*    */   
/*    */   public BlockState getState() {
/* 26 */     if (this.state == null && (this.loadChunks || this.level.hasChunkAt(this.pos))) {
/* 27 */       this.state = this.level.getBlockState(this.pos);
/*    */     }
/*    */     
/* 30 */     return this.state;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public BlockEntity getEntity() {
/* 35 */     if (this.entity == null && !this.cachedEntity) {
/* 36 */       this.entity = this.level.getBlockEntity(this.pos);
/* 37 */       this.cachedEntity = true;
/*    */     } 
/*    */     
/* 40 */     return this.entity;
/*    */   }
/*    */   
/*    */   public LevelReader getLevel() {
/* 44 */     return this.level;
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 48 */     return this.pos;
/*    */   }
/*    */   
/*    */   public static Predicate<BlockInWorld> hasState(Predicate<BlockState> debug0) {
/* 52 */     return debug1 -> (debug1 != null && debug0.test(debug1.getState()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\pattern\BlockInWorld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */