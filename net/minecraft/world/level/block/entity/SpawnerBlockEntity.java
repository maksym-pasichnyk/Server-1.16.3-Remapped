/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*    */ import net.minecraft.world.level.BaseSpawner;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.SpawnData;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SpawnerBlockEntity
/*    */   extends BlockEntity
/*    */   implements TickableBlockEntity {
/* 16 */   private final BaseSpawner spawner = new BaseSpawner()
/*    */     {
/*    */       public void broadcastEvent(int debug1) {
/* 19 */         SpawnerBlockEntity.this.level.blockEvent(SpawnerBlockEntity.this.worldPosition, Blocks.SPAWNER, debug1, 0);
/*    */       }
/*    */ 
/*    */       
/*    */       public Level getLevel() {
/* 24 */         return SpawnerBlockEntity.this.level;
/*    */       }
/*    */ 
/*    */       
/*    */       public BlockPos getPos() {
/* 29 */         return SpawnerBlockEntity.this.worldPosition;
/*    */       }
/*    */ 
/*    */       
/*    */       public void setNextSpawnData(SpawnData debug1) {
/* 34 */         super.setNextSpawnData(debug1);
/* 35 */         if (getLevel() != null) {
/* 36 */           BlockState debug2 = getLevel().getBlockState(getPos());
/* 37 */           getLevel().sendBlockUpdated(SpawnerBlockEntity.this.worldPosition, debug2, debug2, 4);
/*    */         } 
/*    */       }
/*    */     };
/*    */   
/*    */   public SpawnerBlockEntity() {
/* 43 */     super(BlockEntityType.MOB_SPAWNER);
/*    */   }
/*    */ 
/*    */   
/*    */   public void load(BlockState debug1, CompoundTag debug2) {
/* 48 */     super.load(debug1, debug2);
/* 49 */     this.spawner.load(debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag save(CompoundTag debug1) {
/* 54 */     super.save(debug1);
/* 55 */     this.spawner.save(debug1);
/*    */     
/* 57 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 62 */     this.spawner.tick();
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 68 */     return new ClientboundBlockEntityDataPacket(this.worldPosition, 1, getUpdateTag());
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag getUpdateTag() {
/* 73 */     CompoundTag debug1 = save(new CompoundTag());
/* 74 */     debug1.remove("SpawnPotentials");
/* 75 */     return debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean triggerEvent(int debug1, int debug2) {
/* 80 */     if (this.spawner.onEventTriggered(debug1)) {
/* 81 */       return true;
/*    */     }
/* 83 */     return super.triggerEvent(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onlyOpCanSetNbt() {
/* 88 */     return true;
/*    */   }
/*    */   
/*    */   public BaseSpawner getSpawner() {
/* 92 */     return this.spawner;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\SpawnerBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */