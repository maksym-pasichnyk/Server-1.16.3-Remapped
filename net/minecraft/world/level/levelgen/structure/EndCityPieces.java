/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.util.Tuple;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.decoration.ItemFrame;
/*     */ import net.minecraft.world.entity.monster.Shulker;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
/*     */ import net.minecraft.world.level.levelgen.feature.StructurePieceType;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class EndCityPieces {
/*  30 */   private static final StructurePlaceSettings OVERWRITE = (new StructurePlaceSettings()).setIgnoreEntities(true).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/*  31 */   private static final StructurePlaceSettings INSERT = (new StructurePlaceSettings()).setIgnoreEntities(true).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_AND_AIR);
/*     */   
/*     */   private static EndCityPiece addPiece(StructureManager debug0, EndCityPiece debug1, BlockPos debug2, String debug3, Rotation debug4, boolean debug5) {
/*  34 */     EndCityPiece debug6 = new EndCityPiece(debug0, debug3, debug1.templatePosition, debug4, debug5);
/*  35 */     BlockPos debug7 = debug1.template.calculateConnectedPosition(debug1.placeSettings, debug2, debug6.placeSettings, BlockPos.ZERO);
/*  36 */     debug6.move(debug7.getX(), debug7.getY(), debug7.getZ());
/*     */     
/*  38 */     return debug6;
/*     */   }
/*     */   
/*     */   public static class EndCityPiece extends TemplateStructurePiece {
/*     */     private final String templateName;
/*     */     private final Rotation rotation;
/*     */     private final boolean overwrite;
/*     */     
/*     */     public EndCityPiece(StructureManager debug1, String debug2, BlockPos debug3, Rotation debug4, boolean debug5) {
/*  47 */       super(StructurePieceType.END_CITY_PIECE, 0);
/*     */       
/*  49 */       this.templateName = debug2;
/*  50 */       this.templatePosition = debug3;
/*  51 */       this.rotation = debug4;
/*  52 */       this.overwrite = debug5;
/*     */       
/*  54 */       loadTemplate(debug1);
/*     */     }
/*     */     
/*     */     public EndCityPiece(StructureManager debug1, CompoundTag debug2) {
/*  58 */       super(StructurePieceType.END_CITY_PIECE, debug2);
/*     */       
/*  60 */       this.templateName = debug2.getString("Template");
/*  61 */       this.rotation = Rotation.valueOf(debug2.getString("Rot"));
/*  62 */       this.overwrite = debug2.getBoolean("OW");
/*     */       
/*  64 */       loadTemplate(debug1);
/*     */     }
/*     */     
/*     */     private void loadTemplate(StructureManager debug1) {
/*  68 */       StructureTemplate debug2 = debug1.getOrCreate(new ResourceLocation("end_city/" + this.templateName));
/*  69 */       StructurePlaceSettings debug3 = (this.overwrite ? EndCityPieces.OVERWRITE : EndCityPieces.INSERT).copy().setRotation(this.rotation);
/*     */       
/*  71 */       setup(debug2, this.templatePosition, debug3);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(CompoundTag debug1) {
/*  76 */       super.addAdditionalSaveData(debug1);
/*     */       
/*  78 */       debug1.putString("Template", this.templateName);
/*  79 */       debug1.putString("Rot", this.rotation.name());
/*  80 */       debug1.putBoolean("OW", this.overwrite);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void handleDataMarker(String debug1, BlockPos debug2, ServerLevelAccessor debug3, Random debug4, BoundingBox debug5) {
/*  85 */       if (debug1.startsWith("Chest")) {
/*  86 */         BlockPos debug6 = debug2.below();
/*     */         
/*  88 */         if (debug5.isInside((Vec3i)debug6)) {
/*  89 */           RandomizableContainerBlockEntity.setLootTable((BlockGetter)debug3, debug4, debug6, BuiltInLootTables.END_CITY_TREASURE);
/*     */         }
/*  91 */       } else if (debug1.startsWith("Sentry")) {
/*  92 */         Shulker debug6 = (Shulker)EntityType.SHULKER.create((Level)debug3.getLevel());
/*  93 */         debug6.setPos(debug2.getX() + 0.5D, debug2.getY() + 0.5D, debug2.getZ() + 0.5D);
/*  94 */         debug6.setAttachPosition(debug2);
/*  95 */         debug3.addFreshEntity((Entity)debug6);
/*  96 */       } else if (debug1.startsWith("Elytra")) {
/*  97 */         ItemFrame debug6 = new ItemFrame((Level)debug3.getLevel(), debug2, this.rotation.rotate(Direction.SOUTH));
/*  98 */         debug6.setItem(new ItemStack((ItemLike)Items.ELYTRA), false);
/*  99 */         debug3.addFreshEntity((Entity)debug6);
/*     */       } 
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
/*     */   public static void startHouseTower(StructureManager debug0, BlockPos debug1, Rotation debug2, List<StructurePiece> debug3, Random debug4) {
/* 113 */     FAT_TOWER_GENERATOR.init();
/* 114 */     HOUSE_TOWER_GENERATOR.init();
/* 115 */     TOWER_BRIDGE_GENERATOR.init();
/* 116 */     TOWER_GENERATOR.init();
/*     */     
/* 118 */     EndCityPiece debug5 = addHelper(debug3, new EndCityPiece(debug0, "base_floor", debug1, debug2, true));
/* 119 */     debug5 = addHelper(debug3, addPiece(debug0, debug5, new BlockPos(-1, 0, -1), "second_floor_1", debug2, false));
/* 120 */     debug5 = addHelper(debug3, addPiece(debug0, debug5, new BlockPos(-1, 4, -1), "third_floor_1", debug2, false));
/* 121 */     debug5 = addHelper(debug3, addPiece(debug0, debug5, new BlockPos(-1, 8, -1), "third_roof", debug2, true));
/*     */     
/* 123 */     recursiveChildren(debug0, TOWER_GENERATOR, 1, debug5, null, debug3, debug4);
/*     */   }
/*     */   
/*     */   private static EndCityPiece addHelper(List<StructurePiece> debug0, EndCityPiece debug1) {
/* 127 */     debug0.add(debug1);
/* 128 */     return debug1;
/*     */   }
/*     */   
/*     */   private static boolean recursiveChildren(StructureManager debug0, SectionGenerator debug1, int debug2, EndCityPiece debug3, BlockPos debug4, List<StructurePiece> debug5, Random debug6) {
/* 132 */     if (debug2 > 8) {
/* 133 */       return false;
/*     */     }
/*     */     
/* 136 */     List<StructurePiece> debug7 = Lists.newArrayList();
/* 137 */     if (debug1.generate(debug0, debug2, debug3, debug4, debug7, debug6)) {
/*     */       
/* 139 */       boolean debug8 = false;
/* 140 */       int debug9 = debug6.nextInt();
/* 141 */       for (StructurePiece debug11 : debug7) {
/* 142 */         debug11.genDepth = debug9;
/* 143 */         StructurePiece debug12 = StructurePiece.findCollisionPiece(debug5, debug11.getBoundingBox());
/* 144 */         if (debug12 != null && debug12.genDepth != debug3.genDepth) {
/* 145 */           debug8 = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 149 */       if (!debug8) {
/* 150 */         debug5.addAll(debug7);
/* 151 */         return true;
/*     */       } 
/*     */     } 
/* 154 */     return false;
/*     */   }
/*     */   
/* 157 */   private static final SectionGenerator HOUSE_TOWER_GENERATOR = new SectionGenerator()
/*     */     {
/*     */       public void init() {}
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean generate(StructureManager debug1, int debug2, EndCityPieces.EndCityPiece debug3, BlockPos debug4, List<StructurePiece> debug5, Random debug6) {
/* 164 */         if (debug2 > 8) {
/* 165 */           return false;
/*     */         }
/*     */         
/* 168 */         Rotation debug7 = debug3.placeSettings.getRotation();
/* 169 */         EndCityPieces.EndCityPiece debug8 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug3, debug4, "base_floor", debug7, true));
/*     */         
/* 171 */         int debug9 = debug6.nextInt(3);
/* 172 */         if (debug9 == 0) {
/* 173 */           debug8 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug8, new BlockPos(-1, 4, -1), "base_roof", debug7, true));
/* 174 */         } else if (debug9 == 1) {
/* 175 */           debug8 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug8, new BlockPos(-1, 0, -1), "second_floor_2", debug7, false));
/* 176 */           debug8 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug8, new BlockPos(-1, 8, -1), "second_roof", debug7, false));
/*     */           
/* 178 */           EndCityPieces.recursiveChildren(debug1, EndCityPieces.TOWER_GENERATOR, debug2 + 1, debug8, null, debug5, debug6);
/* 179 */         } else if (debug9 == 2) {
/* 180 */           debug8 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug8, new BlockPos(-1, 0, -1), "second_floor_2", debug7, false));
/* 181 */           debug8 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug8, new BlockPos(-1, 4, -1), "third_floor_2", debug7, false));
/* 182 */           debug8 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug8, new BlockPos(-1, 8, -1), "third_roof", debug7, true));
/*     */           
/* 184 */           EndCityPieces.recursiveChildren(debug1, EndCityPieces.TOWER_GENERATOR, debug2 + 1, debug8, null, debug5, debug6);
/*     */         } 
/* 186 */         return true;
/*     */       }
/*     */     };
/*     */   
/* 190 */   private static final List<Tuple<Rotation, BlockPos>> TOWER_BRIDGES = Lists.newArrayList((Object[])new Tuple[] { new Tuple(Rotation.NONE, new BlockPos(1, -1, 0)), new Tuple(Rotation.CLOCKWISE_90, new BlockPos(6, -1, 1)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 5)), new Tuple(Rotation.CLOCKWISE_180, new BlockPos(5, -1, 6)) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 197 */   private static final SectionGenerator TOWER_GENERATOR = new SectionGenerator()
/*     */     {
/*     */       public void init() {}
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean generate(StructureManager debug1, int debug2, EndCityPieces.EndCityPiece debug3, BlockPos debug4, List<StructurePiece> debug5, Random debug6) {
/* 204 */         Rotation debug7 = debug3.placeSettings.getRotation();
/* 205 */         EndCityPieces.EndCityPiece debug8 = debug3;
/* 206 */         debug8 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug8, new BlockPos(3 + debug6.nextInt(2), -3, 3 + debug6.nextInt(2)), "tower_base", debug7, true));
/* 207 */         debug8 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug8, new BlockPos(0, 7, 0), "tower_piece", debug7, true));
/*     */         
/* 209 */         EndCityPieces.EndCityPiece debug9 = (debug6.nextInt(3) == 0) ? debug8 : null;
/*     */         
/* 211 */         int debug10 = 1 + debug6.nextInt(3);
/* 212 */         for (int debug11 = 0; debug11 < debug10; debug11++) {
/* 213 */           debug8 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug8, new BlockPos(0, 4, 0), "tower_piece", debug7, true));
/* 214 */           if (debug11 < debug10 - 1 && debug6.nextBoolean()) {
/* 215 */             debug9 = debug8;
/*     */           }
/*     */         } 
/*     */         
/* 219 */         if (debug9 != null) {
/* 220 */           for (Tuple<Rotation, BlockPos> debug12 : (Iterable<Tuple<Rotation, BlockPos>>)EndCityPieces.TOWER_BRIDGES) {
/* 221 */             if (debug6.nextBoolean()) {
/*     */               
/* 223 */               EndCityPieces.EndCityPiece debug13 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug9, (BlockPos)debug12.getB(), "bridge_end", debug7.getRotated((Rotation)debug12.getA()), true));
/* 224 */               EndCityPieces.recursiveChildren(debug1, EndCityPieces.TOWER_BRIDGE_GENERATOR, debug2 + 1, debug13, null, debug5, debug6);
/*     */             } 
/*     */           } 
/*     */           
/* 228 */           debug8 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug8, new BlockPos(-1, 4, -1), "tower_top", debug7, true));
/*     */         }
/* 230 */         else if (debug2 == 7) {
/* 231 */           debug8 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug8, new BlockPos(-1, 4, -1), "tower_top", debug7, true));
/*     */         } else {
/* 233 */           return EndCityPieces.recursiveChildren(debug1, EndCityPieces.FAT_TOWER_GENERATOR, debug2 + 1, debug8, null, debug5, debug6);
/*     */         } 
/*     */         
/* 236 */         return true;
/*     */       }
/*     */     };
/*     */   
/* 240 */   private static final SectionGenerator TOWER_BRIDGE_GENERATOR = new SectionGenerator()
/*     */     {
/*     */       public boolean shipCreated;
/*     */       
/*     */       public void init() {
/* 245 */         this.shipCreated = false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean generate(StructureManager debug1, int debug2, EndCityPieces.EndCityPiece debug3, BlockPos debug4, List<StructurePiece> debug5, Random debug6) {
/* 250 */         Rotation debug7 = debug3.placeSettings.getRotation();
/* 251 */         int debug8 = debug6.nextInt(4) + 1;
/*     */         
/* 253 */         EndCityPieces.EndCityPiece debug9 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug3, new BlockPos(0, 0, -4), "bridge_piece", debug7, true));
/* 254 */         debug9.genDepth = -1;
/* 255 */         int debug10 = 0;
/* 256 */         for (int debug11 = 0; debug11 < debug8; debug11++) {
/* 257 */           if (debug6.nextBoolean()) {
/* 258 */             debug9 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug9, new BlockPos(0, debug10, -4), "bridge_piece", debug7, true));
/* 259 */             debug10 = 0;
/*     */           } else {
/* 261 */             if (debug6.nextBoolean()) {
/* 262 */               debug9 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug9, new BlockPos(0, debug10, -4), "bridge_steep_stairs", debug7, true));
/*     */             } else {
/* 264 */               debug9 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug9, new BlockPos(0, debug10, -8), "bridge_gentle_stairs", debug7, true));
/*     */             } 
/* 266 */             debug10 = 4;
/*     */           } 
/*     */         } 
/*     */         
/* 270 */         if (this.shipCreated || debug6.nextInt(10 - debug2) != 0) {
/* 271 */           if (!EndCityPieces.recursiveChildren(debug1, EndCityPieces.HOUSE_TOWER_GENERATOR, debug2 + 1, debug9, new BlockPos(-3, debug10 + 1, -11), debug5, debug6)) {
/* 272 */             return false;
/*     */           }
/*     */         } else {
/*     */           
/* 276 */           EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug9, new BlockPos(-8 + debug6.nextInt(8), debug10, -70 + debug6.nextInt(10)), "ship", debug7, true));
/* 277 */           this.shipCreated = true;
/*     */         } 
/*     */ 
/*     */         
/* 281 */         debug9 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug9, new BlockPos(4, debug10, 0), "bridge_end", debug7.getRotated(Rotation.CLOCKWISE_180), true));
/* 282 */         debug9.genDepth = -1;
/*     */         
/* 284 */         return true;
/*     */       }
/*     */     };
/*     */   
/* 288 */   private static final List<Tuple<Rotation, BlockPos>> FAT_TOWER_BRIDGES = Lists.newArrayList((Object[])new Tuple[] { new Tuple(Rotation.NONE, new BlockPos(4, -1, 0)), new Tuple(Rotation.CLOCKWISE_90, new BlockPos(12, -1, 4)), new Tuple(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, -1, 8)), new Tuple(Rotation.CLOCKWISE_180, new BlockPos(8, -1, 12)) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 295 */   private static final SectionGenerator FAT_TOWER_GENERATOR = new SectionGenerator()
/*     */     {
/*     */       public void init() {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean generate(StructureManager debug1, int debug2, EndCityPieces.EndCityPiece debug3, BlockPos debug4, List<StructurePiece> debug5, Random debug6) {
/* 303 */         Rotation debug8 = debug3.placeSettings.getRotation();
/*     */         
/* 305 */         EndCityPieces.EndCityPiece debug7 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug3, new BlockPos(-3, 4, -3), "fat_tower_base", debug8, true));
/* 306 */         debug7 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug7, new BlockPos(0, 4, 0), "fat_tower_middle", debug8, true));
/* 307 */         for (int debug9 = 0; debug9 < 2 && 
/* 308 */           debug6.nextInt(3) != 0; debug9++) {
/*     */ 
/*     */           
/* 311 */           debug7 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug7, new BlockPos(0, 8, 0), "fat_tower_middle", debug8, true));
/*     */           
/* 313 */           for (Tuple<Rotation, BlockPos> debug11 : (Iterable<Tuple<Rotation, BlockPos>>)EndCityPieces.FAT_TOWER_BRIDGES) {
/* 314 */             if (debug6.nextBoolean()) {
/*     */               
/* 316 */               EndCityPieces.EndCityPiece debug12 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug7, (BlockPos)debug11.getB(), "bridge_end", debug8.getRotated((Rotation)debug11.getA()), true));
/* 317 */               EndCityPieces.recursiveChildren(debug1, EndCityPieces.TOWER_BRIDGE_GENERATOR, debug2 + 1, debug12, null, debug5, debug6);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 322 */         debug7 = EndCityPieces.addHelper(debug5, EndCityPieces.addPiece(debug1, debug7, new BlockPos(-2, 8, -2), "fat_tower_top", debug8, true));
/* 323 */         return true;
/*     */       }
/*     */     };
/*     */   
/*     */   static interface SectionGenerator {
/*     */     void init();
/*     */     
/*     */     boolean generate(StructureManager param1StructureManager, int param1Int, EndCityPieces.EndCityPiece param1EndCityPiece, BlockPos param1BlockPos, List<StructurePiece> param1List, Random param1Random);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\EndCityPieces.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */