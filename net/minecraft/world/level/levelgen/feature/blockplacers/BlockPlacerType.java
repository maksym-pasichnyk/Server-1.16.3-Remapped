/*    */ package net.minecraft.world.level.levelgen.feature.blockplacers;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class BlockPlacerType<P extends BlockPlacer> {
/*  7 */   public static final BlockPlacerType<SimpleBlockPlacer> SIMPLE_BLOCK_PLACER = register("simple_block_placer", SimpleBlockPlacer.CODEC);
/*  8 */   public static final BlockPlacerType<DoublePlantPlacer> DOUBLE_PLANT_PLACER = register("double_plant_placer", DoublePlantPlacer.CODEC);
/*  9 */   public static final BlockPlacerType<ColumnPlacer> COLUMN_PLACER = register("column_placer", ColumnPlacer.CODEC);
/*    */   
/*    */   private static <P extends BlockPlacer> BlockPlacerType<P> register(String debug0, Codec<P> debug1) {
/* 12 */     return (BlockPlacerType<P>)Registry.register(Registry.BLOCK_PLACER_TYPES, debug0, new BlockPlacerType<>(debug1));
/*    */   }
/*    */   
/*    */   private final Codec<P> codec;
/*    */   
/*    */   private BlockPlacerType(Codec<P> debug1) {
/* 18 */     this.codec = debug1;
/*    */   }
/*    */   
/*    */   public Codec<P> codec() {
/* 22 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\blockplacers\BlockPlacerType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */