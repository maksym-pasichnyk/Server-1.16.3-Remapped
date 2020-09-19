/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LevelChunkSection
/*     */ {
/*  18 */   private static final Palette<BlockState> GLOBAL_BLOCKSTATE_PALETTE = new GlobalPalette<>(Block.BLOCK_STATE_REGISTRY, Blocks.AIR.defaultBlockState());
/*     */   
/*     */   private final int bottomBlockY;
/*     */   private short nonEmptyBlockCount;
/*     */   private short tickingBlockCount;
/*     */   private short tickingFluidCount;
/*     */   private final PalettedContainer<BlockState> states;
/*     */   
/*     */   public LevelChunkSection(int debug1) {
/*  27 */     this(debug1, (short)0, (short)0, (short)0);
/*     */   }
/*     */   
/*     */   public LevelChunkSection(int debug1, short debug2, short debug3, short debug4) {
/*  31 */     this.bottomBlockY = debug1;
/*  32 */     this.nonEmptyBlockCount = debug2;
/*  33 */     this.tickingBlockCount = debug3;
/*  34 */     this.tickingFluidCount = debug4;
/*  35 */     this.states = new PalettedContainer<>(GLOBAL_BLOCKSTATE_PALETTE, Block.BLOCK_STATE_REGISTRY, NbtUtils::readBlockState, NbtUtils::writeBlockState, Blocks.AIR.defaultBlockState());
/*     */   }
/*     */   
/*     */   public BlockState getBlockState(int debug1, int debug2, int debug3) {
/*  39 */     return this.states.get(debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   public FluidState getFluidState(int debug1, int debug2, int debug3) {
/*  43 */     return ((BlockState)this.states.get(debug1, debug2, debug3)).getFluidState();
/*     */   }
/*     */   
/*     */   public void acquire() {
/*  47 */     this.states.acquire();
/*     */   }
/*     */   
/*     */   public void release() {
/*  51 */     this.states.release();
/*     */   }
/*     */   
/*     */   public BlockState setBlockState(int debug1, int debug2, int debug3, BlockState debug4) {
/*  55 */     return setBlockState(debug1, debug2, debug3, debug4, true);
/*     */   }
/*     */   
/*     */   public BlockState setBlockState(int debug1, int debug2, int debug3, BlockState debug4, boolean debug5) {
/*     */     BlockState debug6;
/*  60 */     if (debug5) {
/*  61 */       debug6 = this.states.getAndSet(debug1, debug2, debug3, debug4);
/*     */     } else {
/*  63 */       debug6 = this.states.getAndSetUnchecked(debug1, debug2, debug3, debug4);
/*     */     } 
/*  65 */     FluidState debug7 = debug6.getFluidState();
/*  66 */     FluidState debug8 = debug4.getFluidState();
/*     */     
/*  68 */     if (!debug6.isAir()) {
/*  69 */       this.nonEmptyBlockCount = (short)(this.nonEmptyBlockCount - 1);
/*  70 */       if (debug6.isRandomlyTicking()) {
/*  71 */         this.tickingBlockCount = (short)(this.tickingBlockCount - 1);
/*     */       }
/*     */     } 
/*     */     
/*  75 */     if (!debug7.isEmpty()) {
/*  76 */       this.tickingFluidCount = (short)(this.tickingFluidCount - 1);
/*     */     }
/*     */     
/*  79 */     if (!debug4.isAir()) {
/*  80 */       this.nonEmptyBlockCount = (short)(this.nonEmptyBlockCount + 1);
/*  81 */       if (debug4.isRandomlyTicking()) {
/*  82 */         this.tickingBlockCount = (short)(this.tickingBlockCount + 1);
/*     */       }
/*     */     } 
/*     */     
/*  86 */     if (!debug8.isEmpty()) {
/*  87 */       this.tickingFluidCount = (short)(this.tickingFluidCount + 1);
/*     */     }
/*     */     
/*  90 */     return debug6;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  94 */     return (this.nonEmptyBlockCount == 0);
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(@Nullable LevelChunkSection debug0) {
/*  98 */     return (debug0 == LevelChunk.EMPTY_SECTION || debug0.isEmpty());
/*     */   }
/*     */   
/*     */   public boolean isRandomlyTicking() {
/* 102 */     return (isRandomlyTickingBlocks() || isRandomlyTickingFluids());
/*     */   }
/*     */   
/*     */   public boolean isRandomlyTickingBlocks() {
/* 106 */     return (this.tickingBlockCount > 0);
/*     */   }
/*     */   
/*     */   public boolean isRandomlyTickingFluids() {
/* 110 */     return (this.tickingFluidCount > 0);
/*     */   }
/*     */   
/*     */   public int bottomBlockY() {
/* 114 */     return this.bottomBlockY;
/*     */   }
/*     */   
/*     */   public void recalcBlockCounts() {
/* 118 */     this.nonEmptyBlockCount = 0;
/* 119 */     this.tickingBlockCount = 0;
/* 120 */     this.tickingFluidCount = 0;
/*     */     
/* 122 */     this.states.count((debug1, debug2) -> {
/*     */           FluidState debug3 = debug1.getFluidState();
/*     */           if (!debug1.isAir()) {
/*     */             this.nonEmptyBlockCount = (short)(this.nonEmptyBlockCount + debug2);
/*     */             if (debug1.isRandomlyTicking()) {
/*     */               this.tickingBlockCount = (short)(this.tickingBlockCount + debug2);
/*     */             }
/*     */           } 
/*     */           if (!debug3.isEmpty()) {
/*     */             this.nonEmptyBlockCount = (short)(this.nonEmptyBlockCount + debug2);
/*     */             if (debug3.isRandomlyTicking()) {
/*     */               this.tickingFluidCount = (short)(this.tickingFluidCount + debug2);
/*     */             }
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public PalettedContainer<BlockState> getStates() {
/* 141 */     return this.states;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) {
/* 150 */     debug1.writeShort(this.nonEmptyBlockCount);
/* 151 */     this.states.write(debug1);
/*     */   }
/*     */   
/*     */   public int getSerializedSize() {
/* 155 */     return 2 + this.states.getSerializedSize();
/*     */   }
/*     */   
/*     */   public boolean maybeHas(Predicate<BlockState> debug1) {
/* 159 */     return this.states.maybeHas(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\LevelChunkSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */