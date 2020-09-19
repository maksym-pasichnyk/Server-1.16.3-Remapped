/*    */ package net.minecraft.world.level.levelgen.flat;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class FlatLayerInfo {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Codec.intRange(0, 256).fieldOf("height").forGetter(FlatLayerInfo::getHeight), (App)Registry.BLOCK.fieldOf("block").orElse(Blocks.AIR).forGetter(())).apply((Applicative)debug0, FlatLayerInfo::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<FlatLayerInfo> CODEC;
/*    */   private final BlockState blockState;
/*    */   private final int height;
/*    */   private int start;
/*    */   
/*    */   public FlatLayerInfo(int debug1, Block debug2) {
/* 22 */     this.height = debug1;
/* 23 */     this.blockState = debug2.defaultBlockState();
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 27 */     return this.height;
/*    */   }
/*    */   
/*    */   public BlockState getBlockState() {
/* 31 */     return this.blockState;
/*    */   }
/*    */   
/*    */   public int getStart() {
/* 35 */     return this.start;
/*    */   }
/*    */   
/*    */   public void setStart(int debug1) {
/* 39 */     this.start = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 44 */     return ((this.height != 1) ? (this.height + "*") : "") + Registry.BLOCK.getKey(this.blockState.getBlock());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\flat\FlatLayerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */