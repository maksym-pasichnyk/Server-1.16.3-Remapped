/*     */ package net.minecraft.world.level.material;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public final class FluidState
/*     */   extends StateHolder<Fluid, FluidState>
/*     */ {
/*  23 */   public static final Codec<FluidState> CODEC = codec((Codec)Registry.FLUID, Fluid::defaultFluidState).stable();
/*     */ 
/*     */ 
/*     */   
/*     */   public FluidState(Fluid debug1, ImmutableMap<Property<?>, Comparable<?>> debug2, MapCodec<FluidState> debug3) {
/*  28 */     super(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Fluid getType() {
/*  35 */     return (Fluid)this.owner;
/*     */   }
/*     */   
/*     */   public boolean isSource() {
/*  39 */     return getType().isSource(this);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  43 */     return getType().isEmpty();
/*     */   }
/*     */   
/*     */   public float getHeight(BlockGetter debug1, BlockPos debug2) {
/*  47 */     return getType().getHeight(this, debug1, debug2);
/*     */   }
/*     */   
/*     */   public float getOwnHeight() {
/*  51 */     return getType().getOwnHeight(this);
/*     */   }
/*     */   
/*     */   public int getAmount() {
/*  55 */     return getType().getAmount(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick(Level debug1, BlockPos debug2) {
/*  72 */     getType().tick(debug1, debug2, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRandomlyTicking() {
/*  80 */     return getType().isRandomlyTicking();
/*     */   }
/*     */   
/*     */   public void randomTick(Level debug1, BlockPos debug2, Random debug3) {
/*  84 */     getType().randomTick(debug1, debug2, this, debug3);
/*     */   }
/*     */   
/*     */   public Vec3 getFlow(BlockGetter debug1, BlockPos debug2) {
/*  88 */     return getType().getFlow(debug1, debug2, this);
/*     */   }
/*     */   
/*     */   public BlockState createLegacyBlock() {
/*  92 */     return getType().createLegacyBlock(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is(Tag<Fluid> debug1) {
/* 101 */     return getType().is(debug1);
/*     */   }
/*     */   
/*     */   public float getExplosionResistance() {
/* 105 */     return getType().getExplosionResistance();
/*     */   }
/*     */   
/*     */   public boolean canBeReplacedWith(BlockGetter debug1, BlockPos debug2, Fluid debug3, Direction debug4) {
/* 109 */     return getType().canBeReplacedWith(this, debug1, debug2, debug3, debug4);
/*     */   }
/*     */   
/*     */   public VoxelShape getShape(BlockGetter debug1, BlockPos debug2) {
/* 113 */     return getType().getShape(this, debug1, debug2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\material\FluidState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */