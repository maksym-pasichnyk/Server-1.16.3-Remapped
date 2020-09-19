/*    */ package net.minecraft.world.level.levelgen.surfacebuilders;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.synth.PerlinNoise;
/*    */ 
/*    */ public abstract class NetherCappedSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
/* 20 */   private ImmutableMap<BlockState, PerlinNoise> floorNoises = ImmutableMap.of(); private long seed;
/* 21 */   private ImmutableMap<BlockState, PerlinNoise> ceilingNoises = ImmutableMap.of();
/*    */   private PerlinNoise patchNoise;
/*    */   
/*    */   public NetherCappedSurfaceBuilder(Codec<SurfaceBuilderBaseConfiguration> debug1) {
/* 25 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void apply(Random debug1, ChunkAccess debug2, Biome debug3, int debug4, int debug5, int debug6, double debug7, BlockState debug9, BlockState debug10, int debug11, long debug12, SurfaceBuilderBaseConfiguration debug14) {
/* 30 */     int debug15 = debug11 + 1;
/* 31 */     int debug16 = debug4 & 0xF;
/* 32 */     int debug17 = debug5 & 0xF;
/*    */     
/* 34 */     int debug18 = (int)(debug7 / 3.0D + 3.0D + debug1.nextDouble() * 0.25D);
/* 35 */     int debug19 = (int)(debug7 / 3.0D + 3.0D + debug1.nextDouble() * 0.25D);
/*    */     
/* 37 */     double debug20 = 0.03125D;
/* 38 */     boolean debug22 = (this.patchNoise.getValue(debug4 * 0.03125D, 109.0D, debug5 * 0.03125D) * 75.0D + debug1.nextDouble() > 0.0D);
/*    */     
/* 40 */     BlockState debug23 = (BlockState)((Map.Entry)this.ceilingNoises.entrySet().stream().max(Comparator.comparing(debug3 -> Double.valueOf(((PerlinNoise)debug3.getValue()).getValue(debug0, debug1, debug2)))).get()).getKey();
/* 41 */     BlockState debug24 = (BlockState)((Map.Entry)this.floorNoises.entrySet().stream().max(Comparator.comparing(debug3 -> Double.valueOf(((PerlinNoise)debug3.getValue()).getValue(debug0, debug1, debug2)))).get()).getKey();
/*    */     
/* 43 */     BlockPos.MutableBlockPos debug25 = new BlockPos.MutableBlockPos();
/* 44 */     BlockState debug26 = debug2.getBlockState((BlockPos)debug25.set(debug16, 128, debug17));
/* 45 */     for (int debug27 = 127; debug27 >= 0; debug27--) {
/* 46 */       debug25.set(debug16, debug27, debug17);
/* 47 */       BlockState debug28 = debug2.getBlockState((BlockPos)debug25);
/*    */ 
/*    */       
/* 50 */       if (debug26.is(debug9.getBlock()) && (debug28.isAir() || debug28 == debug10)) {
/* 51 */         for (int debug29 = 0; debug29 < debug18; ) {
/* 52 */           debug25.move(Direction.UP);
/* 53 */           if (debug2.getBlockState((BlockPos)debug25).is(debug9.getBlock())) {
/* 54 */             debug2.setBlockState((BlockPos)debug25, debug23, false);
/*    */             
/*    */             debug29++;
/*    */           } 
/*    */         } 
/* 59 */         debug25.set(debug16, debug27, debug17);
/*    */       } 
/*    */ 
/*    */       
/* 63 */       if ((debug26.isAir() || debug26 == debug10) && debug28.is(debug9.getBlock())) {
/* 64 */         for (int debug29 = 0; debug29 < debug19 && 
/* 65 */           debug2.getBlockState((BlockPos)debug25).is(debug9.getBlock()); debug29++) {
/* 66 */           if (debug22 && debug27 >= debug15 - 4 && debug27 <= debug15 + 1) {
/* 67 */             debug2.setBlockState((BlockPos)debug25, getPatchBlockState(), false);
/*    */           } else {
/* 69 */             debug2.setBlockState((BlockPos)debug25, debug24, false);
/*    */           } 
/*    */ 
/*    */ 
/*    */           
/* 74 */           debug25.move(Direction.DOWN);
/*    */         } 
/*    */       }
/*    */       
/* 78 */       debug26 = debug28;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void initNoise(long debug1) {
/* 84 */     if (this.seed != debug1 || this.patchNoise == null || this.floorNoises.isEmpty() || this.ceilingNoises.isEmpty()) {
/* 85 */       this.floorNoises = initPerlinNoises(getFloorBlockStates(), debug1);
/* 86 */       this.ceilingNoises = initPerlinNoises(getCeilingBlockStates(), debug1 + this.floorNoises.size());
/*    */       
/* 88 */       this.patchNoise = new PerlinNoise(new WorldgenRandom(debug1 + this.floorNoises.size() + this.ceilingNoises.size()), (List)ImmutableList.of(Integer.valueOf(0)));
/*    */     } 
/* 90 */     this.seed = debug1;
/*    */   }
/*    */   
/*    */   private static ImmutableMap<BlockState, PerlinNoise> initPerlinNoises(ImmutableList<BlockState> debug0, long debug1) {
/* 94 */     ImmutableMap.Builder<BlockState, PerlinNoise> debug3 = new ImmutableMap.Builder();
/* 95 */     for (UnmodifiableIterator<BlockState> unmodifiableIterator = debug0.iterator(); unmodifiableIterator.hasNext(); ) { BlockState debug5 = unmodifiableIterator.next();
/* 96 */       debug3.put(debug5, new PerlinNoise(new WorldgenRandom(debug1), (List)ImmutableList.of(Integer.valueOf(-4))));
/* 97 */       debug1++; }
/*    */     
/* 99 */     return debug3.build();
/*    */   }
/*    */   
/*    */   protected abstract ImmutableList<BlockState> getFloorBlockStates();
/*    */   
/*    */   protected abstract ImmutableList<BlockState> getCeilingBlockStates();
/*    */   
/*    */   protected abstract BlockState getPatchBlockState();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\surfacebuilders\NetherCappedSurfaceBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */