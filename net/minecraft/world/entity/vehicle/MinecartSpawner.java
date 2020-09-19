/*    */ package net.minecraft.world.entity.vehicle;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.level.BaseSpawner;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MinecartSpawner extends AbstractMinecart {
/* 12 */   private final BaseSpawner spawner = new BaseSpawner()
/*    */     {
/*    */       public void broadcastEvent(int debug1) {
/* 15 */         MinecartSpawner.this.level.broadcastEntityEvent(MinecartSpawner.this, (byte)debug1);
/*    */       }
/*    */ 
/*    */       
/*    */       public Level getLevel() {
/* 20 */         return MinecartSpawner.this.level;
/*    */       }
/*    */ 
/*    */       
/*    */       public BlockPos getPos() {
/* 25 */         return MinecartSpawner.this.blockPosition();
/*    */       }
/*    */     };
/*    */   
/*    */   public MinecartSpawner(EntityType<? extends MinecartSpawner> debug1, Level debug2) {
/* 30 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   public MinecartSpawner(Level debug1, double debug2, double debug4, double debug6) {
/* 34 */     super(EntityType.SPAWNER_MINECART, debug1, debug2, debug4, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractMinecart.Type getMinecartType() {
/* 39 */     return AbstractMinecart.Type.SPAWNER;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getDefaultDisplayBlockState() {
/* 44 */     return Blocks.SPAWNER.defaultBlockState();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readAdditionalSaveData(CompoundTag debug1) {
/* 49 */     super.readAdditionalSaveData(debug1);
/* 50 */     this.spawner.load(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(CompoundTag debug1) {
/* 55 */     super.addAdditionalSaveData(debug1);
/* 56 */     this.spawner.save(debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 66 */     super.tick();
/* 67 */     this.spawner.tick();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean onlyOpCanSetNbt() {
/* 76 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\vehicle\MinecartSpawner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */