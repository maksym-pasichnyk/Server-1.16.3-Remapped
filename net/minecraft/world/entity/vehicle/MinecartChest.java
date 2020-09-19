/*    */ package net.minecraft.world.entity.vehicle;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.inventory.ChestMenu;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.ChestBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class MinecartChest extends AbstractMinecartContainer {
/*    */   public MinecartChest(EntityType<? extends MinecartChest> debug1, Level debug2) {
/* 17 */     super(debug1, debug2);
/*    */   }
/*    */   
/*    */   public MinecartChest(Level debug1, double debug2, double debug4, double debug6) {
/* 21 */     super(EntityType.CHEST_MINECART, debug2, debug4, debug6, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void destroy(DamageSource debug1) {
/* 26 */     super.destroy(debug1);
/*    */     
/* 28 */     if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
/* 29 */       spawnAtLocation((ItemLike)Blocks.CHEST);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int getContainerSize() {
/* 35 */     return 27;
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractMinecart.Type getMinecartType() {
/* 40 */     return AbstractMinecart.Type.CHEST;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getDefaultDisplayBlockState() {
/* 45 */     return (BlockState)Blocks.CHEST.defaultBlockState().setValue((Property)ChestBlock.FACING, (Comparable)Direction.NORTH);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getDefaultDisplayOffset() {
/* 50 */     return 8;
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractContainerMenu createMenu(int debug1, Inventory debug2) {
/* 55 */     return (AbstractContainerMenu)ChestMenu.threeRows(debug1, debug2, this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\vehicle\MinecartChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */