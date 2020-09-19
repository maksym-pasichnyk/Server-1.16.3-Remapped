/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ public class GravityProcessor extends StructureProcessor {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(debug0 -> debug0.group((App)Heightmap.Types.CODEC.fieldOf("heightmap").orElse(Heightmap.Types.WORLD_SURFACE_WG).forGetter(()), (App)Codec.INT.fieldOf("offset").orElse(Integer.valueOf(0)).forGetter(())).apply((Applicative)debug0, GravityProcessor::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<GravityProcessor> CODEC;
/*    */   private final Heightmap.Types heightmap;
/*    */   private final int offset;
/*    */   
/*    */   public GravityProcessor(Heightmap.Types debug1, int debug2) {
/* 22 */     this.heightmap = debug1;
/* 23 */     this.offset = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader debug1, BlockPos debug2, BlockPos debug3, StructureTemplate.StructureBlockInfo debug4, StructureTemplate.StructureBlockInfo debug5, StructurePlaceSettings debug6) {
/*    */     Heightmap.Types debug7;
/* 30 */     if (debug1 instanceof net.minecraft.server.level.ServerLevel) {
/*    */       
/* 32 */       if (this.heightmap == Heightmap.Types.WORLD_SURFACE_WG) {
/* 33 */         debug7 = Heightmap.Types.WORLD_SURFACE;
/* 34 */       } else if (this.heightmap == Heightmap.Types.OCEAN_FLOOR_WG) {
/* 35 */         debug7 = Heightmap.Types.OCEAN_FLOOR;
/*    */       } else {
/* 37 */         debug7 = this.heightmap;
/*    */       } 
/*    */     } else {
/* 40 */       debug7 = this.heightmap;
/*    */     } 
/* 42 */     int debug8 = debug1.getHeight(debug7, debug5.pos.getX(), debug5.pos.getZ()) + this.offset;
/* 43 */     int debug9 = debug4.pos.getY();
/* 44 */     return new StructureTemplate.StructureBlockInfo(new BlockPos(debug5.pos.getX(), debug8 + debug9, debug5.pos.getZ()), debug5.state, debug5.nbt);
/*    */   }
/*    */ 
/*    */   
/*    */   protected StructureProcessorType<?> getType() {
/* 49 */     return StructureProcessorType.GRAVITY;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\GravityProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */