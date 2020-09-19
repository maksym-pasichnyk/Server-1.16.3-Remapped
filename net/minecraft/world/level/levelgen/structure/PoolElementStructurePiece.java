/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.EmptyPoolElement;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.JigsawJunction;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class PoolElementStructurePiece extends StructurePiece {
/*  27 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   protected final StructurePoolElement element;
/*     */   protected BlockPos position;
/*     */   private final int groundLevelDelta;
/*     */   protected final Rotation rotation;
/*  33 */   private final List<JigsawJunction> junctions = Lists.newArrayList();
/*     */   private final StructureManager structureManager;
/*     */   
/*     */   public PoolElementStructurePiece(StructureManager debug1, StructurePoolElement debug2, BlockPos debug3, int debug4, Rotation debug5, BoundingBox debug6) {
/*  37 */     super(StructurePieceType.JIGSAW, 0);
/*  38 */     this.structureManager = debug1;
/*  39 */     this.element = debug2;
/*  40 */     this.position = debug3;
/*  41 */     this.groundLevelDelta = debug4;
/*  42 */     this.rotation = debug5;
/*  43 */     this.boundingBox = debug6;
/*     */   }
/*     */   
/*     */   public PoolElementStructurePiece(StructureManager debug1, CompoundTag debug2) {
/*  47 */     super(StructurePieceType.JIGSAW, debug2);
/*  48 */     this.structureManager = debug1;
/*  49 */     this.position = new BlockPos(debug2.getInt("PosX"), debug2.getInt("PosY"), debug2.getInt("PosZ"));
/*  50 */     this.groundLevelDelta = debug2.getInt("ground_level_delta");
/*  51 */     this.element = (StructurePoolElement)StructurePoolElement.CODEC.parse((DynamicOps)NbtOps.INSTANCE, debug2.getCompound("pool_element")).resultOrPartial(LOGGER::error).orElse(EmptyPoolElement.INSTANCE);
/*  52 */     this.rotation = Rotation.valueOf(debug2.getString("rotation"));
/*  53 */     this.boundingBox = this.element.getBoundingBox(debug1, this.position, this.rotation);
/*     */     
/*  55 */     ListTag debug3 = debug2.getList("junctions", 10);
/*  56 */     this.junctions.clear();
/*  57 */     debug3.forEach(debug1 -> this.junctions.add(JigsawJunction.deserialize(new Dynamic((DynamicOps)NbtOps.INSTANCE, debug1))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/*  62 */     debug1.putInt("PosX", this.position.getX());
/*  63 */     debug1.putInt("PosY", this.position.getY());
/*  64 */     debug1.putInt("PosZ", this.position.getZ());
/*  65 */     debug1.putInt("ground_level_delta", this.groundLevelDelta);
/*  66 */     StructurePoolElement.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, this.element).resultOrPartial(LOGGER::error).ifPresent(debug1 -> debug0.put("pool_element", debug1));
/*     */ 
/*     */     
/*  69 */     debug1.putString("rotation", this.rotation.name());
/*  70 */     ListTag debug2 = new ListTag();
/*  71 */     for (JigsawJunction debug4 : this.junctions) {
/*  72 */       debug2.add(debug4.serialize((DynamicOps)NbtOps.INSTANCE).getValue());
/*     */     }
/*  74 */     debug1.put("junctions", (Tag)debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  79 */     return place(debug1, debug2, debug3, debug4, debug5, debug7, false);
/*     */   }
/*     */   
/*     */   public boolean place(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, BlockPos debug6, boolean debug7) {
/*  83 */     return this.element.place(this.structureManager, debug1, debug2, debug3, this.position, debug6, this.rotation, debug5, debug4, debug7);
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(int debug1, int debug2, int debug3) {
/*  88 */     super.move(debug1, debug2, debug3);
/*  89 */     this.position = this.position.offset(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public Rotation getRotation() {
/*  94 */     return this.rotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  99 */     return String.format("<%s | %s | %s | %s>", new Object[] { getClass().getSimpleName(), this.position, this.rotation, this.element });
/*     */   }
/*     */   
/*     */   public StructurePoolElement getElement() {
/* 103 */     return this.element;
/*     */   }
/*     */   
/*     */   public BlockPos getPosition() {
/* 107 */     return this.position;
/*     */   }
/*     */   
/*     */   public int getGroundLevelDelta() {
/* 111 */     return this.groundLevelDelta;
/*     */   }
/*     */   
/*     */   public void addJunction(JigsawJunction debug1) {
/* 115 */     this.junctions.add(debug1);
/*     */   }
/*     */   
/*     */   public List<JigsawJunction> getJunctions() {
/* 119 */     return this.junctions;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\PoolElementStructurePiece.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */