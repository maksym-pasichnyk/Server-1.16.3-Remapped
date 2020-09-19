/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.npc.Villager;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ComposterBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class WorkAtComposter extends WorkAtPoi {
/*  22 */   private static final List<Item> COMPOSTABLE_ITEMS = (List<Item>)ImmutableList.of(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void useWorkstation(ServerLevel debug1, Villager debug2) {
/*  29 */     Optional<GlobalPos> debug3 = debug2.getBrain().getMemory(MemoryModuleType.JOB_SITE);
/*  30 */     if (!debug3.isPresent()) {
/*     */       return;
/*     */     }
/*  33 */     GlobalPos debug4 = debug3.get();
/*  34 */     BlockState debug5 = debug1.getBlockState(debug4.pos());
/*     */     
/*  36 */     if (debug5.is(Blocks.COMPOSTER)) {
/*  37 */       makeBread(debug2);
/*     */       
/*  39 */       compostItems(debug1, debug2, debug4, debug5);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void compostItems(ServerLevel debug1, Villager debug2, GlobalPos debug3, BlockState debug4) {
/*  45 */     BlockPos debug5 = debug3.pos();
/*  46 */     if (((Integer)debug4.getValue((Property)ComposterBlock.LEVEL)).intValue() == 8) {
/*  47 */       debug4 = ComposterBlock.extractProduce(debug4, (Level)debug1, debug5);
/*     */     }
/*     */ 
/*     */     
/*  51 */     int debug6 = 20;
/*  52 */     int debug7 = 10;
/*     */     
/*  54 */     int[] debug8 = new int[COMPOSTABLE_ITEMS.size()];
/*     */     
/*  56 */     SimpleContainer debug9 = debug2.getInventory();
/*  57 */     int debug10 = debug9.getContainerSize();
/*     */     
/*  59 */     BlockState debug11 = debug4;
/*     */     
/*  61 */     for (int debug12 = debug10 - 1; debug12 >= 0 && debug6 > 0; debug12--) {
/*  62 */       ItemStack debug13 = debug9.getItem(debug12);
/*  63 */       int debug14 = COMPOSTABLE_ITEMS.indexOf(debug13.getItem());
/*  64 */       if (debug14 != -1) {
/*     */ 
/*     */ 
/*     */         
/*  68 */         int debug15 = debug13.getCount();
/*  69 */         int debug16 = debug8[debug14] + debug15;
/*  70 */         debug8[debug14] = debug16;
/*     */         
/*  72 */         int debug17 = Math.min(Math.min(debug16 - 10, debug6), debug15);
/*  73 */         if (debug17 > 0) {
/*  74 */           debug6 -= debug17;
/*  75 */           for (int debug18 = 0; debug18 < debug17; debug18++) {
/*  76 */             debug11 = ComposterBlock.insertItem(debug11, debug1, debug13, debug5);
/*  77 */             if (((Integer)debug11.getValue((Property)ComposterBlock.LEVEL)).intValue() == 7) {
/*  78 */               spawnComposterFillEffects(debug1, debug4, debug5, debug11);
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  85 */     spawnComposterFillEffects(debug1, debug4, debug5, debug11);
/*     */   }
/*     */   
/*     */   private void spawnComposterFillEffects(ServerLevel debug1, BlockState debug2, BlockPos debug3, BlockState debug4) {
/*  89 */     debug1.levelEvent(1500, debug3, (debug4 != debug2) ? 1 : 0);
/*     */   }
/*     */   
/*     */   private void makeBread(Villager debug1) {
/*  93 */     SimpleContainer debug2 = debug1.getInventory();
/*  94 */     if (debug2.countItem(Items.BREAD) > 36) {
/*     */       return;
/*     */     }
/*     */     
/*  98 */     int debug3 = debug2.countItem(Items.WHEAT);
/*  99 */     int debug4 = 3;
/* 100 */     int debug5 = 3;
/* 101 */     int debug6 = Math.min(3, debug3 / 3);
/* 102 */     if (debug6 == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 106 */     int debug7 = debug6 * 3;
/* 107 */     debug2.removeItemType(Items.WHEAT, debug7);
/* 108 */     ItemStack debug8 = debug2.addItem(new ItemStack((ItemLike)Items.BREAD, debug6));
/* 109 */     if (!debug8.isEmpty())
/* 110 */       debug1.spawnAtLocation(debug8, 0.5F); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\WorkAtComposter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */