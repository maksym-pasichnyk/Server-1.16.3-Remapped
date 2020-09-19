/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.commands.arguments.blocks.BlockStateParser;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureFeatureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public abstract class TemplateStructurePiece
/*     */   extends StructurePiece
/*     */ {
/*  30 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*     */   protected StructureTemplate template;
/*     */   protected StructurePlaceSettings placeSettings;
/*     */   protected BlockPos templatePosition;
/*     */   
/*     */   public TemplateStructurePiece(StructurePieceType debug1, int debug2) {
/*  37 */     super(debug1, debug2);
/*     */   }
/*     */   
/*     */   public TemplateStructurePiece(StructurePieceType debug1, CompoundTag debug2) {
/*  41 */     super(debug1, debug2);
/*  42 */     this.templatePosition = new BlockPos(debug2.getInt("TPX"), debug2.getInt("TPY"), debug2.getInt("TPZ"));
/*     */   }
/*     */   
/*     */   protected void setup(StructureTemplate debug1, BlockPos debug2, StructurePlaceSettings debug3) {
/*  46 */     this.template = debug1;
/*  47 */     setOrientation(Direction.NORTH);
/*  48 */     this.templatePosition = debug2;
/*  49 */     this.placeSettings = debug3;
/*  50 */     this.boundingBox = debug1.getBoundingBox(debug3, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/*  55 */     debug1.putInt("TPX", this.templatePosition.getX());
/*  56 */     debug1.putInt("TPY", this.templatePosition.getY());
/*  57 */     debug1.putInt("TPZ", this.templatePosition.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean postProcess(WorldGenLevel debug1, StructureFeatureManager debug2, ChunkGenerator debug3, Random debug4, BoundingBox debug5, ChunkPos debug6, BlockPos debug7) {
/*  62 */     this.placeSettings.setBoundingBox(debug5);
/*     */     
/*  64 */     this.boundingBox = this.template.getBoundingBox(this.placeSettings, this.templatePosition);
/*  65 */     if (this.template.placeInWorld((ServerLevelAccessor)debug1, this.templatePosition, debug7, this.placeSettings, debug4, 2)) {
/*  66 */       List<StructureTemplate.StructureBlockInfo> debug8 = this.template.filterBlocks(this.templatePosition, this.placeSettings, Blocks.STRUCTURE_BLOCK);
/*  67 */       for (StructureTemplate.StructureBlockInfo debug10 : debug8) {
/*  68 */         if (debug10.nbt == null) {
/*     */           continue;
/*     */         }
/*     */         
/*  72 */         StructureMode debug11 = StructureMode.valueOf(debug10.nbt.getString("mode"));
/*  73 */         if (debug11 != StructureMode.DATA) {
/*     */           continue;
/*     */         }
/*     */         
/*  77 */         handleDataMarker(debug10.nbt.getString("metadata"), debug10.pos, (ServerLevelAccessor)debug1, debug4, debug5);
/*     */       } 
/*     */       
/*  80 */       List<StructureTemplate.StructureBlockInfo> debug9 = this.template.filterBlocks(this.templatePosition, this.placeSettings, Blocks.JIGSAW);
/*  81 */       for (StructureTemplate.StructureBlockInfo debug11 : debug9) {
/*  82 */         if (debug11.nbt == null) {
/*     */           continue;
/*     */         }
/*     */         
/*  86 */         String debug12 = debug11.nbt.getString("final_state");
/*  87 */         BlockStateParser debug13 = new BlockStateParser(new StringReader(debug12), false);
/*  88 */         BlockState debug14 = Blocks.AIR.defaultBlockState();
/*     */         try {
/*  90 */           debug13.parse(true);
/*  91 */           BlockState debug15 = debug13.getState();
/*     */           
/*  93 */           if (debug15 != null) {
/*  94 */             debug14 = debug15;
/*     */           } else {
/*  96 */             LOGGER.error("Error while parsing blockstate {} in jigsaw block @ {}", debug12, debug11.pos);
/*     */           } 
/*  98 */         } catch (CommandSyntaxException debug15) {
/*  99 */           LOGGER.error("Error while parsing blockstate {} in jigsaw block @ {}", debug12, debug11.pos);
/*     */         } 
/*     */         
/* 102 */         debug1.setBlock(debug11.pos, debug14, 3);
/*     */       } 
/*     */     } 
/*     */     
/* 106 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void handleDataMarker(String paramString, BlockPos paramBlockPos, ServerLevelAccessor paramServerLevelAccessor, Random paramRandom, BoundingBox paramBoundingBox);
/*     */   
/*     */   public void move(int debug1, int debug2, int debug3) {
/* 113 */     super.move(debug1, debug2, debug3);
/* 114 */     this.templatePosition = this.templatePosition.offset(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   public Rotation getRotation() {
/* 119 */     return this.placeSettings.getRotation();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\TemplateStructurePiece.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */