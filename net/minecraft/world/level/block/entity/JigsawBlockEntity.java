/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.JigsawBlock;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.SinglePoolElement;
/*     */ import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ 
/*     */ public class JigsawBlockEntity extends BlockEntity {
/*     */   public enum JointType implements StringRepresentable {
/*  33 */     ROLLABLE("rollable"),
/*  34 */     ALIGNED("aligned");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     JointType(String debug3) {
/*  39 */       this.name = debug3;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/*  44 */       return this.name;
/*     */     }
/*     */     
/*     */     public static Optional<JointType> byName(String debug0) {
/*  48 */       return Arrays.<JointType>stream(values()).filter(debug1 -> debug1.getSerializedName().equals(debug0)).findFirst();
/*     */     }
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
/*  63 */   private ResourceLocation name = new ResourceLocation("empty");
/*  64 */   private ResourceLocation target = new ResourceLocation("empty");
/*  65 */   private ResourceLocation pool = new ResourceLocation("empty");
/*  66 */   private JointType joint = JointType.ROLLABLE;
/*  67 */   private String finalState = "minecraft:air";
/*     */   
/*     */   public JigsawBlockEntity(BlockEntityType<?> debug1) {
/*  70 */     super(debug1);
/*     */   }
/*     */   
/*     */   public JigsawBlockEntity() {
/*  74 */     this(BlockEntityType.JIGSAW);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(ResourceLocation debug1) {
/*  98 */     this.name = debug1;
/*     */   }
/*     */   
/*     */   public void setTarget(ResourceLocation debug1) {
/* 102 */     this.target = debug1;
/*     */   }
/*     */   
/*     */   public void setPool(ResourceLocation debug1) {
/* 106 */     this.pool = debug1;
/*     */   }
/*     */   
/*     */   public void setFinalState(String debug1) {
/* 110 */     this.finalState = debug1;
/*     */   }
/*     */   
/*     */   public void setJoint(JointType debug1) {
/* 114 */     this.joint = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/* 119 */     super.save(debug1);
/* 120 */     debug1.putString("name", this.name.toString());
/* 121 */     debug1.putString("target", this.target.toString());
/* 122 */     debug1.putString("pool", this.pool.toString());
/* 123 */     debug1.putString("final_state", this.finalState);
/* 124 */     debug1.putString("joint", this.joint.getSerializedName());
/* 125 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/* 130 */     super.load(debug1, debug2);
/* 131 */     this.name = new ResourceLocation(debug2.getString("name"));
/* 132 */     this.target = new ResourceLocation(debug2.getString("target"));
/* 133 */     this.pool = new ResourceLocation(debug2.getString("pool"));
/* 134 */     this.finalState = debug2.getString("final_state");
/* 135 */     this
/* 136 */       .joint = JointType.byName(debug2.getString("joint")).orElseGet(() -> JigsawBlock.getFrontFacing(debug0).getAxis().isHorizontal() ? JointType.ALIGNED : JointType.ROLLABLE);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 142 */     return new ClientboundBlockEntityDataPacket(this.worldPosition, 12, getUpdateTag());
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag() {
/* 147 */     return save(new CompoundTag());
/*     */   }
/*     */   
/*     */   public void generate(ServerLevel debug1, int debug2, boolean debug3) {
/* 151 */     ChunkGenerator debug4 = debug1.getChunkSource().getGenerator();
/* 152 */     StructureManager debug5 = debug1.getStructureManager();
/* 153 */     StructureFeatureManager debug6 = debug1.structureFeatureManager();
/* 154 */     Random debug7 = debug1.getRandom();
/* 155 */     BlockPos debug8 = getBlockPos();
/*     */     
/* 157 */     List<PoolElementStructurePiece> debug9 = Lists.newArrayList();
/*     */     
/* 159 */     StructureTemplate debug10 = new StructureTemplate();
/* 160 */     debug10.fillFromWorld((Level)debug1, debug8, new BlockPos(1, 1, 1), false, null);
/*     */     
/* 162 */     SinglePoolElement singlePoolElement = new SinglePoolElement(debug10);
/* 163 */     PoolElementStructurePiece debug12 = new PoolElementStructurePiece(debug5, (StructurePoolElement)singlePoolElement, debug8, 1, Rotation.NONE, new BoundingBox((Vec3i)debug8, (Vec3i)debug8));
/*     */     
/* 165 */     JigsawPlacement.addPieces(debug1.registryAccess(), debug12, debug2, PoolElementStructurePiece::new, debug4, debug5, debug9, debug7);
/*     */     
/* 167 */     for (PoolElementStructurePiece debug14 : debug9)
/* 168 */       debug14.place((WorldGenLevel)debug1, debug6, debug4, debug7, BoundingBox.infinite(), debug8, debug3); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\JigsawBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */