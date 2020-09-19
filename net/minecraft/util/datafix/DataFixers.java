/*     */ package net.minecraft.util.datafix;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.datafixers.DataFixerBuilder;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.util.datafix.fixes.AddNewChoices;
/*     */ import net.minecraft.util.datafix.fixes.AdvancementsFix;
/*     */ import net.minecraft.util.datafix.fixes.AdvancementsRenameFix;
/*     */ import net.minecraft.util.datafix.fixes.AttributesRename;
/*     */ import net.minecraft.util.datafix.fixes.BedBlockEntityInjecter;
/*     */ import net.minecraft.util.datafix.fixes.BedItemColorFix;
/*     */ import net.minecraft.util.datafix.fixes.BeehivePoiRenameFix;
/*     */ import net.minecraft.util.datafix.fixes.BiomeFix;
/*     */ import net.minecraft.util.datafix.fixes.BitStorageAlignFix;
/*     */ import net.minecraft.util.datafix.fixes.BlockEntityBannerColorFix;
/*     */ import net.minecraft.util.datafix.fixes.BlockEntityBlockStateFix;
/*     */ import net.minecraft.util.datafix.fixes.BlockEntityCustomNameToComponentFix;
/*     */ import net.minecraft.util.datafix.fixes.BlockEntityIdFix;
/*     */ import net.minecraft.util.datafix.fixes.BlockEntityJukeboxFix;
/*     */ import net.minecraft.util.datafix.fixes.BlockEntityKeepPacked;
/*     */ import net.minecraft.util.datafix.fixes.BlockEntityShulkerBoxColorFix;
/*     */ import net.minecraft.util.datafix.fixes.BlockEntitySignTextStrictJsonFix;
/*     */ import net.minecraft.util.datafix.fixes.BlockEntityUUIDFix;
/*     */ import net.minecraft.util.datafix.fixes.BlockNameFlatteningFix;
/*     */ import net.minecraft.util.datafix.fixes.BlockRenameFix;
/*     */ import net.minecraft.util.datafix.fixes.BlockStateStructureTemplateFix;
/*     */ import net.minecraft.util.datafix.fixes.CatTypeFix;
/*     */ import net.minecraft.util.datafix.fixes.ChunkBiomeFix;
/*     */ import net.minecraft.util.datafix.fixes.ChunkLightRemoveFix;
/*     */ import net.minecraft.util.datafix.fixes.ChunkPalettedStorageFix;
/*     */ import net.minecraft.util.datafix.fixes.ChunkStatusFix;
/*     */ import net.minecraft.util.datafix.fixes.ChunkStatusFix2;
/*     */ import net.minecraft.util.datafix.fixes.ChunkStructuresTemplateRenameFix;
/*     */ import net.minecraft.util.datafix.fixes.ChunkToProtochunkFix;
/*     */ import net.minecraft.util.datafix.fixes.ColorlessShulkerEntityFix;
/*     */ import net.minecraft.util.datafix.fixes.DyeItemRenameFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityArmorStandSilentFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityBlockStateFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityCatSplitFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityCodSalmonFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityCustomNameToComponentFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityElderGuardianSplitFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityEquipmentToArmorAndHandFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityHealthFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityHorseSaddleFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityHorseSplitFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityIdFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityItemFrameDirectionFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityMinecartIdentifiersFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityPaintingItemFrameDirectionFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityPaintingMotiveFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityProjectileOwnerFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityPufferfishRenameFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityRavagerRenameFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityRedundantChanceTagsFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityRidingToPassengersFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityShulkerColorFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityShulkerRotationFix;
/*     */ import net.minecraft.util.datafix.fixes.EntitySkeletonSplitFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityStringUuidFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityTheRenameningFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityTippedArrowFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityUUIDFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityWolfColorFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityZombieSplitFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityZombieVillagerTypeFix;
/*     */ import net.minecraft.util.datafix.fixes.EntityZombifiedPiglinRenameFix;
/*     */ import net.minecraft.util.datafix.fixes.ForcePoiRebuild;
/*     */ import net.minecraft.util.datafix.fixes.FurnaceRecipeFix;
/*     */ import net.minecraft.util.datafix.fixes.GossipUUIDFix;
/*     */ import net.minecraft.util.datafix.fixes.HeightmapRenamingFix;
/*     */ import net.minecraft.util.datafix.fixes.IglooMetadataRemovalFix;
/*     */ import net.minecraft.util.datafix.fixes.ItemBannerColorFix;
/*     */ import net.minecraft.util.datafix.fixes.ItemCustomNameToComponentFix;
/*     */ import net.minecraft.util.datafix.fixes.ItemIdFix;
/*     */ import net.minecraft.util.datafix.fixes.ItemLoreFix;
/*     */ import net.minecraft.util.datafix.fixes.ItemPotionFix;
/*     */ import net.minecraft.util.datafix.fixes.ItemRenameFix;
/*     */ import net.minecraft.util.datafix.fixes.ItemShulkerBoxColorFix;
/*     */ import net.minecraft.util.datafix.fixes.ItemSpawnEggFix;
/*     */ import net.minecraft.util.datafix.fixes.ItemStackEnchantmentNamesFix;
/*     */ import net.minecraft.util.datafix.fixes.ItemStackMapIdFix;
/*     */ import net.minecraft.util.datafix.fixes.ItemStackSpawnEggFix;
/*     */ import net.minecraft.util.datafix.fixes.ItemStackTheFlatteningFix;
/*     */ import net.minecraft.util.datafix.fixes.ItemStackUUIDFix;
/*     */ import net.minecraft.util.datafix.fixes.ItemWaterPotionFix;
/*     */ import net.minecraft.util.datafix.fixes.ItemWrittenBookPagesStrictJsonFix;
/*     */ import net.minecraft.util.datafix.fixes.JigsawPropertiesFix;
/*     */ import net.minecraft.util.datafix.fixes.JigsawRotationFix;
/*     */ import net.minecraft.util.datafix.fixes.LeavesFix;
/*     */ import net.minecraft.util.datafix.fixes.LevelDataGeneratorOptionsFix;
/*     */ import net.minecraft.util.datafix.fixes.LevelFlatGeneratorInfoFix;
/*     */ import net.minecraft.util.datafix.fixes.LevelUUIDFix;
/*     */ import net.minecraft.util.datafix.fixes.MapIdFix;
/*     */ import net.minecraft.util.datafix.fixes.MemoryExpiryDataFix;
/*     */ import net.minecraft.util.datafix.fixes.MissingDimensionFix;
/*     */ import net.minecraft.util.datafix.fixes.MobSpawnerEntityIdentifiersFix;
/*     */ import net.minecraft.util.datafix.fixes.NamedEntityFix;
/*     */ import net.minecraft.util.datafix.fixes.NewVillageFix;
/*     */ import net.minecraft.util.datafix.fixes.ObjectiveDisplayNameFix;
/*     */ import net.minecraft.util.datafix.fixes.ObjectiveRenderTypeFix;
/*     */ import net.minecraft.util.datafix.fixes.OminousBannerBlockEntityRenameFix;
/*     */ import net.minecraft.util.datafix.fixes.OminousBannerRenameFix;
/*     */ import net.minecraft.util.datafix.fixes.OptionsAddTextBackgroundFix;
/*     */ import net.minecraft.util.datafix.fixes.OptionsForceVBOFix;
/*     */ import net.minecraft.util.datafix.fixes.OptionsKeyLwjgl3Fix;
/*     */ import net.minecraft.util.datafix.fixes.OptionsKeyTranslationFix;
/*     */ import net.minecraft.util.datafix.fixes.OptionsLowerCaseLanguageFix;
/*     */ import net.minecraft.util.datafix.fixes.OptionsRenameFieldFix;
/*     */ import net.minecraft.util.datafix.fixes.PlayerUUIDFix;
/*     */ import net.minecraft.util.datafix.fixes.RecipesFix;
/*     */ import net.minecraft.util.datafix.fixes.RecipesRenameFix;
/*     */ import net.minecraft.util.datafix.fixes.RecipesRenameningFix;
/*     */ import net.minecraft.util.datafix.fixes.RedstoneWireConnectionsFix;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import net.minecraft.util.datafix.fixes.RemoveGolemGossipFix;
/*     */ import net.minecraft.util.datafix.fixes.RenameBiomesFix;
/*     */ import net.minecraft.util.datafix.fixes.RenamedCoralFansFix;
/*     */ import net.minecraft.util.datafix.fixes.RenamedCoralFix;
/*     */ import net.minecraft.util.datafix.fixes.ReorganizePoi;
/*     */ import net.minecraft.util.datafix.fixes.SavedDataUUIDFix;
/*     */ import net.minecraft.util.datafix.fixes.SavedDataVillageCropFix;
/*     */ import net.minecraft.util.datafix.fixes.StatsCounterFix;
/*     */ import net.minecraft.util.datafix.fixes.StriderGravityFix;
/*     */ import net.minecraft.util.datafix.fixes.StructureReferenceCountFix;
/*     */ import net.minecraft.util.datafix.fixes.SwimStatsRenameFix;
/*     */ import net.minecraft.util.datafix.fixes.TeamDisplayNameFix;
/*     */ import net.minecraft.util.datafix.fixes.TrappedChestBlockEntityFix;
/*     */ import net.minecraft.util.datafix.fixes.VillagerDataFix;
/*     */ import net.minecraft.util.datafix.fixes.VillagerFollowRangeFix;
/*     */ import net.minecraft.util.datafix.fixes.VillagerRebuildLevelAndXpFix;
/*     */ import net.minecraft.util.datafix.fixes.VillagerTradeFix;
/*     */ import net.minecraft.util.datafix.fixes.WallPropertyFix;
/*     */ import net.minecraft.util.datafix.fixes.WorldGenSettingsFix;
/*     */ import net.minecraft.util.datafix.fixes.WriteAndReadFix;
/*     */ import net.minecraft.util.datafix.fixes.ZombieVillagerRebuildXpFix;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataFixers
/*     */ {
/* 200 */   private static final BiFunction<Integer, Schema, Schema> SAME = Schema::new;
/* 201 */   private static final BiFunction<Integer, Schema, Schema> SAME_NAMESPACED = NamespacedSchema::new;
/*     */   
/* 203 */   private static final DataFixer DATA_FIXER = createFixerUpper();
/*     */   
/*     */   private static DataFixer createFixerUpper() {
/* 206 */     DataFixerBuilder debug0 = new DataFixerBuilder(SharedConstants.getCurrentVersion().getWorldVersion());
/* 207 */     addFixers(debug0);
/* 208 */     return debug0.build(Util.bootstrapExecutor());
/*     */   }
/*     */   
/*     */   public static DataFixer getDataFixer() {
/* 212 */     return DATA_FIXER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addFixers(DataFixerBuilder debug0) {
/* 220 */     Schema debug1 = debug0.addSchema(99, net.minecraft.util.datafix.schemas.V99::new);
/*     */ 
/*     */     
/* 223 */     Schema debug2 = debug0.addSchema(100, net.minecraft.util.datafix.schemas.V100::new);
/* 224 */     debug0.addFixer((DataFix)new EntityEquipmentToArmorAndHandFix(debug2, true));
/*     */     
/* 226 */     Schema debug3 = debug0.addSchema(101, SAME);
/* 227 */     debug0.addFixer((DataFix)new BlockEntitySignTextStrictJsonFix(debug3, false));
/*     */     
/* 229 */     Schema debug4 = debug0.addSchema(102, net.minecraft.util.datafix.schemas.V102::new);
/* 230 */     debug0.addFixer((DataFix)new ItemIdFix(debug4, true));
/* 231 */     debug0.addFixer((DataFix)new ItemPotionFix(debug4, false));
/*     */     
/* 233 */     Schema debug5 = debug0.addSchema(105, SAME);
/* 234 */     debug0.addFixer((DataFix)new ItemSpawnEggFix(debug5, true));
/*     */ 
/*     */     
/* 237 */     Schema debug6 = debug0.addSchema(106, net.minecraft.util.datafix.schemas.V106::new);
/* 238 */     debug0.addFixer((DataFix)new MobSpawnerEntityIdentifiersFix(debug6, true));
/*     */     
/* 240 */     Schema debug7 = debug0.addSchema(107, net.minecraft.util.datafix.schemas.V107::new);
/* 241 */     debug0.addFixer((DataFix)new EntityMinecartIdentifiersFix(debug7, true));
/*     */     
/* 243 */     Schema debug8 = debug0.addSchema(108, SAME);
/* 244 */     debug0.addFixer((DataFix)new EntityStringUuidFix(debug8, true));
/*     */     
/* 246 */     Schema debug9 = debug0.addSchema(109, SAME);
/* 247 */     debug0.addFixer((DataFix)new EntityHealthFix(debug9, true));
/*     */     
/* 249 */     Schema debug10 = debug0.addSchema(110, SAME);
/* 250 */     debug0.addFixer((DataFix)new EntityHorseSaddleFix(debug10, true));
/*     */     
/* 252 */     Schema debug11 = debug0.addSchema(111, SAME);
/* 253 */     debug0.addFixer((DataFix)new EntityPaintingItemFrameDirectionFix(debug11, true));
/*     */     
/* 255 */     Schema debug12 = debug0.addSchema(113, SAME);
/* 256 */     debug0.addFixer((DataFix)new EntityRedundantChanceTagsFix(debug12, true));
/*     */     
/* 258 */     Schema debug13 = debug0.addSchema(135, net.minecraft.util.datafix.schemas.V135::new);
/* 259 */     debug0.addFixer((DataFix)new EntityRidingToPassengersFix(debug13, true));
/*     */     
/* 261 */     Schema debug14 = debug0.addSchema(143, net.minecraft.util.datafix.schemas.V143::new);
/* 262 */     debug0.addFixer((DataFix)new EntityTippedArrowFix(debug14, true));
/*     */     
/* 264 */     Schema debug15 = debug0.addSchema(147, SAME);
/* 265 */     debug0.addFixer((DataFix)new EntityArmorStandSilentFix(debug15, true));
/*     */     
/* 267 */     Schema debug16 = debug0.addSchema(165, SAME);
/* 268 */     debug0.addFixer((DataFix)new ItemWrittenBookPagesStrictJsonFix(debug16, true));
/*     */ 
/*     */     
/* 271 */     Schema debug17 = debug0.addSchema(501, net.minecraft.util.datafix.schemas.V501::new);
/* 272 */     debug0.addFixer((DataFix)new AddNewChoices(debug17, "Add 1.10 entities fix", References.ENTITY));
/*     */     
/* 274 */     Schema debug18 = debug0.addSchema(502, SAME);
/* 275 */     debug0.addFixer(ItemRenameFix.create(debug18, "cooked_fished item renamer", debug0 -> Objects.equals(NamespacedSchema.ensureNamespaced(debug0), "minecraft:cooked_fished") ? "minecraft:cooked_fish" : debug0));
/*     */ 
/*     */     
/* 278 */     debug0.addFixer((DataFix)new EntityZombieVillagerTypeFix(debug18, false));
/*     */     
/* 280 */     Schema debug19 = debug0.addSchema(505, SAME);
/* 281 */     debug0.addFixer((DataFix)new OptionsForceVBOFix(debug19, false));
/*     */ 
/*     */     
/* 284 */     Schema debug20 = debug0.addSchema(700, net.minecraft.util.datafix.schemas.V700::new);
/* 285 */     debug0.addFixer((DataFix)new EntityElderGuardianSplitFix(debug20, true));
/*     */     
/* 287 */     Schema debug21 = debug0.addSchema(701, net.minecraft.util.datafix.schemas.V701::new);
/* 288 */     debug0.addFixer((DataFix)new EntitySkeletonSplitFix(debug21, true));
/*     */     
/* 290 */     Schema debug22 = debug0.addSchema(702, net.minecraft.util.datafix.schemas.V702::new);
/* 291 */     debug0.addFixer((DataFix)new EntityZombieSplitFix(debug22, true));
/*     */     
/* 293 */     Schema debug23 = debug0.addSchema(703, net.minecraft.util.datafix.schemas.V703::new);
/* 294 */     debug0.addFixer((DataFix)new EntityHorseSplitFix(debug23, true));
/*     */ 
/*     */     
/* 297 */     Schema debug24 = debug0.addSchema(704, net.minecraft.util.datafix.schemas.V704::new);
/* 298 */     debug0.addFixer((DataFix)new BlockEntityIdFix(debug24, true));
/*     */     
/* 300 */     Schema debug25 = debug0.addSchema(705, net.minecraft.util.datafix.schemas.V705::new);
/* 301 */     debug0.addFixer((DataFix)new EntityIdFix(debug25, true));
/*     */     
/* 303 */     Schema debug26 = debug0.addSchema(804, SAME_NAMESPACED);
/* 304 */     debug0.addFixer((DataFix)new ItemBannerColorFix(debug26, true));
/*     */     
/* 306 */     Schema debug27 = debug0.addSchema(806, SAME_NAMESPACED);
/* 307 */     debug0.addFixer((DataFix)new ItemWaterPotionFix(debug27, false));
/*     */ 
/*     */     
/* 310 */     Schema debug28 = debug0.addSchema(808, net.minecraft.util.datafix.schemas.V808::new);
/* 311 */     debug0.addFixer((DataFix)new AddNewChoices(debug28, "added shulker box", References.BLOCK_ENTITY));
/*     */     
/* 313 */     Schema debug29 = debug0.addSchema(808, 1, SAME_NAMESPACED);
/* 314 */     debug0.addFixer((DataFix)new EntityShulkerColorFix(debug29, false));
/*     */     
/* 316 */     Schema debug30 = debug0.addSchema(813, SAME_NAMESPACED);
/* 317 */     debug0.addFixer((DataFix)new ItemShulkerBoxColorFix(debug30, false));
/* 318 */     debug0.addFixer((DataFix)new BlockEntityShulkerBoxColorFix(debug30, false));
/*     */     
/* 320 */     Schema debug31 = debug0.addSchema(816, SAME_NAMESPACED);
/* 321 */     debug0.addFixer((DataFix)new OptionsLowerCaseLanguageFix(debug31, false));
/*     */ 
/*     */     
/* 324 */     Schema debug32 = debug0.addSchema(820, SAME_NAMESPACED);
/* 325 */     debug0.addFixer(ItemRenameFix.create(debug32, "totem item renamer", createRenamer("minecraft:totem", "minecraft:totem_of_undying")));
/*     */ 
/*     */     
/* 328 */     Schema debug33 = debug0.addSchema(1022, net.minecraft.util.datafix.schemas.V1022::new);
/* 329 */     debug0.addFixer((DataFix)new WriteAndReadFix(debug33, "added shoulder entities to players", References.PLAYER));
/*     */     
/* 331 */     Schema debug34 = debug0.addSchema(1125, net.minecraft.util.datafix.schemas.V1125::new);
/* 332 */     debug0.addFixer((DataFix)new BedBlockEntityInjecter(debug34, true));
/* 333 */     debug0.addFixer((DataFix)new BedItemColorFix(debug34, false));
/*     */ 
/*     */     
/* 336 */     Schema debug35 = debug0.addSchema(1344, SAME_NAMESPACED);
/* 337 */     debug0.addFixer((DataFix)new OptionsKeyLwjgl3Fix(debug35, false));
/*     */     
/* 339 */     Schema debug36 = debug0.addSchema(1446, SAME_NAMESPACED);
/* 340 */     debug0.addFixer((DataFix)new OptionsKeyTranslationFix(debug36, false));
/*     */     
/* 342 */     Schema debug37 = debug0.addSchema(1450, SAME_NAMESPACED);
/* 343 */     debug0.addFixer((DataFix)new BlockStateStructureTemplateFix(debug37, false));
/*     */     
/* 345 */     Schema debug38 = debug0.addSchema(1451, net.minecraft.util.datafix.schemas.V1451::new);
/* 346 */     debug0.addFixer((DataFix)new AddNewChoices(debug38, "AddTrappedChestFix", References.BLOCK_ENTITY));
/*     */     
/* 348 */     Schema debug39 = debug0.addSchema(1451, 1, net.minecraft.util.datafix.schemas.V1451_1::new);
/* 349 */     debug0.addFixer((DataFix)new ChunkPalettedStorageFix(debug39, true));
/*     */ 
/*     */     
/* 352 */     Schema debug40 = debug0.addSchema(1451, 2, net.minecraft.util.datafix.schemas.V1451_2::new);
/* 353 */     debug0.addFixer((DataFix)new BlockEntityBlockStateFix(debug40, true));
/*     */     
/* 355 */     Schema debug41 = debug0.addSchema(1451, 3, net.minecraft.util.datafix.schemas.V1451_3::new);
/* 356 */     debug0.addFixer((DataFix)new EntityBlockStateFix(debug41, true));
/* 357 */     debug0.addFixer((DataFix)new ItemStackMapIdFix(debug41, false));
/*     */     
/* 359 */     Schema debug42 = debug0.addSchema(1451, 4, net.minecraft.util.datafix.schemas.V1451_4::new);
/* 360 */     debug0.addFixer((DataFix)new BlockNameFlatteningFix(debug42, true));
/* 361 */     debug0.addFixer((DataFix)new ItemStackTheFlatteningFix(debug42, false));
/*     */ 
/*     */     
/* 364 */     Schema debug43 = debug0.addSchema(1451, 5, net.minecraft.util.datafix.schemas.V1451_5::new);
/* 365 */     debug0.addFixer((DataFix)new AddNewChoices(debug43, "RemoveNoteBlockFlowerPotFix", References.BLOCK_ENTITY));
/* 366 */     debug0.addFixer((DataFix)new ItemStackSpawnEggFix(debug43, false));
/* 367 */     debug0.addFixer((DataFix)new EntityWolfColorFix(debug43, false));
/* 368 */     debug0.addFixer((DataFix)new BlockEntityBannerColorFix(debug43, false));
/* 369 */     debug0.addFixer((DataFix)new LevelFlatGeneratorInfoFix(debug43, false));
/*     */     
/* 371 */     Schema debug44 = debug0.addSchema(1451, 6, net.minecraft.util.datafix.schemas.V1451_6::new);
/* 372 */     debug0.addFixer((DataFix)new StatsCounterFix(debug44, true));
/* 373 */     debug0.addFixer((DataFix)new BlockEntityJukeboxFix(debug44, false));
/*     */     
/* 375 */     Schema debug45 = debug0.addSchema(1451, 7, net.minecraft.util.datafix.schemas.V1451_7::new);
/* 376 */     debug0.addFixer((DataFix)new SavedDataVillageCropFix(debug45, true));
/*     */     
/* 378 */     Schema debug46 = debug0.addSchema(1451, 7, SAME_NAMESPACED);
/* 379 */     debug0.addFixer((DataFix)new VillagerTradeFix(debug46, false));
/*     */     
/* 381 */     Schema debug47 = debug0.addSchema(1456, SAME_NAMESPACED);
/* 382 */     debug0.addFixer((DataFix)new EntityItemFrameDirectionFix(debug47, false));
/*     */     
/* 384 */     Schema debug48 = debug0.addSchema(1458, SAME_NAMESPACED);
/* 385 */     debug0.addFixer((DataFix)new EntityCustomNameToComponentFix(debug48, false));
/* 386 */     debug0.addFixer((DataFix)new ItemCustomNameToComponentFix(debug48, false));
/* 387 */     debug0.addFixer((DataFix)new BlockEntityCustomNameToComponentFix(debug48, false));
/*     */     
/* 389 */     Schema debug49 = debug0.addSchema(1460, net.minecraft.util.datafix.schemas.V1460::new);
/* 390 */     debug0.addFixer((DataFix)new EntityPaintingMotiveFix(debug49, false));
/*     */     
/* 392 */     Schema debug50 = debug0.addSchema(1466, net.minecraft.util.datafix.schemas.V1466::new);
/* 393 */     debug0.addFixer((DataFix)new ChunkToProtochunkFix(debug50, true));
/*     */ 
/*     */     
/* 396 */     Schema debug51 = debug0.addSchema(1470, net.minecraft.util.datafix.schemas.V1470::new);
/* 397 */     debug0.addFixer((DataFix)new AddNewChoices(debug51, "Add 1.13 entities fix", References.ENTITY));
/*     */     
/* 399 */     Schema debug52 = debug0.addSchema(1474, SAME_NAMESPACED);
/* 400 */     debug0.addFixer((DataFix)new ColorlessShulkerEntityFix(debug52, false));
/* 401 */     debug0.addFixer(BlockRenameFix.create(debug52, "Colorless shulker block fixer", debug0 -> Objects.equals(NamespacedSchema.ensureNamespaced(debug0), "minecraft:purple_shulker_box") ? "minecraft:shulker_box" : debug0));
/* 402 */     debug0.addFixer(ItemRenameFix.create(debug52, "Colorless shulker item fixer", debug0 -> Objects.equals(NamespacedSchema.ensureNamespaced(debug0), "minecraft:purple_shulker_box") ? "minecraft:shulker_box" : debug0));
/*     */     
/* 404 */     Schema debug53 = debug0.addSchema(1475, SAME_NAMESPACED);
/* 405 */     debug0.addFixer(BlockRenameFix.create(debug53, "Flowing fixer", createRenamer(
/* 406 */             (Map<String, String>)ImmutableMap.of("minecraft:flowing_water", "minecraft:water", "minecraft:flowing_lava", "minecraft:lava"))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 412 */     Schema debug54 = debug0.addSchema(1480, SAME_NAMESPACED);
/* 413 */     debug0.addFixer(BlockRenameFix.create(debug54, "Rename coral blocks", createRenamer(RenamedCoralFix.RENAMED_IDS)));
/* 414 */     debug0.addFixer(ItemRenameFix.create(debug54, "Rename coral items", createRenamer(RenamedCoralFix.RENAMED_IDS)));
/*     */     
/* 416 */     Schema debug55 = debug0.addSchema(1481, net.minecraft.util.datafix.schemas.V1481::new);
/* 417 */     debug0.addFixer((DataFix)new AddNewChoices(debug55, "Add conduit", References.BLOCK_ENTITY));
/*     */     
/* 419 */     Schema debug56 = debug0.addSchema(1483, net.minecraft.util.datafix.schemas.V1483::new);
/* 420 */     debug0.addFixer((DataFix)new EntityPufferfishRenameFix(debug56, true));
/* 421 */     debug0.addFixer(ItemRenameFix.create(debug56, "Rename pufferfish egg item", createRenamer(EntityPufferfishRenameFix.RENAMED_IDS)));
/*     */     
/* 423 */     Schema debug57 = debug0.addSchema(1484, SAME_NAMESPACED);
/* 424 */     debug0.addFixer(ItemRenameFix.create(debug57, "Rename seagrass items", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:sea_grass", "minecraft:seagrass", "minecraft:tall_sea_grass", "minecraft:tall_seagrass"))));
/*     */ 
/*     */ 
/*     */     
/* 428 */     debug0.addFixer(BlockRenameFix.create(debug57, "Rename seagrass blocks", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:sea_grass", "minecraft:seagrass", "minecraft:tall_sea_grass", "minecraft:tall_seagrass"))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 434 */     debug0.addFixer((DataFix)new HeightmapRenamingFix(debug57, false));
/*     */ 
/*     */     
/* 437 */     Schema debug58 = debug0.addSchema(1486, net.minecraft.util.datafix.schemas.V1486::new);
/* 438 */     debug0.addFixer((DataFix)new EntityCodSalmonFix(debug58, true));
/* 439 */     debug0.addFixer(ItemRenameFix.create(debug58, "Rename cod/salmon egg items", createRenamer(EntityCodSalmonFix.RENAMED_EGG_IDS)));
/*     */     
/* 441 */     Schema debug59 = debug0.addSchema(1487, SAME_NAMESPACED);
/* 442 */     debug0.addFixer(ItemRenameFix.create(debug59, "Rename prismarine_brick(s)_* blocks", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:prismarine_bricks_slab", "minecraft:prismarine_brick_slab", "minecraft:prismarine_bricks_stairs", "minecraft:prismarine_brick_stairs"))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 447 */     debug0.addFixer(BlockRenameFix.create(debug59, "Rename prismarine_brick(s)_* items", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:prismarine_bricks_slab", "minecraft:prismarine_brick_slab", "minecraft:prismarine_bricks_stairs", "minecraft:prismarine_brick_stairs"))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 453 */     Schema debug60 = debug0.addSchema(1488, SAME_NAMESPACED);
/* 454 */     debug0.addFixer(BlockRenameFix.create(debug60, "Rename kelp/kelptop", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:kelp_top", "minecraft:kelp", "minecraft:kelp", "minecraft:kelp_plant"))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 459 */     debug0.addFixer(ItemRenameFix.create(debug60, "Rename kelptop", createRenamer("minecraft:kelp_top", "minecraft:kelp")));
/* 460 */     debug0.addFixer((DataFix)new NamedEntityFix(debug60, false, "Command block block entity custom name fix", References.BLOCK_ENTITY, "minecraft:command_block")
/*     */         {
/*     */           protected Typed<?> fix(Typed<?> debug1) {
/* 463 */             return debug1.update(DSL.remainderFinder(), EntityCustomNameToComponentFix::fixTagCustomName);
/*     */           }
/*     */         });
/* 466 */     debug0.addFixer((DataFix)new NamedEntityFix(debug60, false, "Command block minecart custom name fix", References.ENTITY, "minecraft:commandblock_minecart")
/*     */         {
/*     */           protected Typed<?> fix(Typed<?> debug1) {
/* 469 */             return debug1.update(DSL.remainderFinder(), EntityCustomNameToComponentFix::fixTagCustomName);
/*     */           }
/*     */         });
/* 472 */     debug0.addFixer((DataFix)new IglooMetadataRemovalFix(debug60, false));
/*     */     
/* 474 */     Schema debug61 = debug0.addSchema(1490, SAME_NAMESPACED);
/* 475 */     debug0.addFixer(BlockRenameFix.create(debug61, "Rename melon_block", createRenamer("minecraft:melon_block", "minecraft:melon")));
/* 476 */     debug0.addFixer(ItemRenameFix.create(debug61, "Rename melon_block/melon/speckled_melon", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:melon_block", "minecraft:melon", "minecraft:melon", "minecraft:melon_slice", "minecraft:speckled_melon", "minecraft:glistering_melon_slice"))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 483 */     Schema debug62 = debug0.addSchema(1492, SAME_NAMESPACED);
/* 484 */     debug0.addFixer((DataFix)new ChunkStructuresTemplateRenameFix(debug62, false));
/*     */     
/* 486 */     Schema debug63 = debug0.addSchema(1494, SAME_NAMESPACED);
/* 487 */     debug0.addFixer((DataFix)new ItemStackEnchantmentNamesFix(debug63, false));
/*     */     
/* 489 */     Schema debug64 = debug0.addSchema(1496, SAME_NAMESPACED);
/* 490 */     debug0.addFixer((DataFix)new LeavesFix(debug64, false));
/*     */     
/* 492 */     Schema debug65 = debug0.addSchema(1500, SAME_NAMESPACED);
/* 493 */     debug0.addFixer((DataFix)new BlockEntityKeepPacked(debug65, false));
/*     */     
/* 495 */     Schema debug66 = debug0.addSchema(1501, SAME_NAMESPACED);
/* 496 */     debug0.addFixer((DataFix)new AdvancementsFix(debug66, false));
/*     */     
/* 498 */     Schema debug67 = debug0.addSchema(1502, SAME_NAMESPACED);
/* 499 */     debug0.addFixer((DataFix)new RecipesFix(debug67, false));
/*     */     
/* 501 */     Schema debug68 = debug0.addSchema(1506, SAME_NAMESPACED);
/* 502 */     debug0.addFixer((DataFix)new LevelDataGeneratorOptionsFix(debug68, false));
/*     */     
/* 504 */     Schema debug69 = debug0.addSchema(1510, net.minecraft.util.datafix.schemas.V1510::new);
/* 505 */     debug0.addFixer(BlockRenameFix.create(debug69, "Block renamening fix", createRenamer(EntityTheRenameningFix.RENAMED_BLOCKS)));
/* 506 */     debug0.addFixer(ItemRenameFix.create(debug69, "Item renamening fix", createRenamer(EntityTheRenameningFix.RENAMED_ITEMS)));
/* 507 */     debug0.addFixer((DataFix)new RecipesRenameningFix(debug69, false));
/* 508 */     debug0.addFixer((DataFix)new EntityTheRenameningFix(debug69, true));
/* 509 */     debug0.addFixer((DataFix)new SwimStatsRenameFix(debug69, false));
/*     */     
/* 511 */     Schema debug70 = debug0.addSchema(1514, SAME_NAMESPACED);
/* 512 */     debug0.addFixer((DataFix)new ObjectiveDisplayNameFix(debug70, false));
/* 513 */     debug0.addFixer((DataFix)new TeamDisplayNameFix(debug70, false));
/* 514 */     debug0.addFixer((DataFix)new ObjectiveRenderTypeFix(debug70, false));
/*     */     
/* 516 */     Schema debug71 = debug0.addSchema(1515, SAME_NAMESPACED);
/* 517 */     debug0.addFixer(BlockRenameFix.create(debug71, "Rename coral fan blocks", createRenamer(RenamedCoralFansFix.RENAMED_IDS)));
/*     */     
/* 519 */     Schema debug72 = debug0.addSchema(1624, SAME_NAMESPACED);
/* 520 */     debug0.addFixer((DataFix)new TrappedChestBlockEntityFix(debug72, false));
/*     */     
/* 522 */     Schema debug73 = debug0.addSchema(1800, net.minecraft.util.datafix.schemas.V1800::new);
/* 523 */     debug0.addFixer((DataFix)new AddNewChoices(debug73, "Added 1.14 mobs fix", References.ENTITY));
/* 524 */     debug0.addFixer(ItemRenameFix.create(debug73, "Rename dye items", createRenamer(DyeItemRenameFix.RENAMED_IDS)));
/*     */     
/* 526 */     Schema debug74 = debug0.addSchema(1801, net.minecraft.util.datafix.schemas.V1801::new);
/* 527 */     debug0.addFixer((DataFix)new AddNewChoices(debug74, "Added Illager Beast", References.ENTITY));
/*     */     
/* 529 */     Schema debug75 = debug0.addSchema(1802, SAME_NAMESPACED);
/* 530 */     debug0.addFixer(BlockRenameFix.create(debug75, "Rename sign blocks & stone slabs", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:stone_slab", "minecraft:smooth_stone_slab", "minecraft:sign", "minecraft:oak_sign", "minecraft:wall_sign", "minecraft:oak_wall_sign"))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 536 */     debug0.addFixer(ItemRenameFix.create(debug75, "Rename sign item & stone slabs", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:stone_slab", "minecraft:smooth_stone_slab", "minecraft:sign", "minecraft:oak_sign"))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 542 */     Schema debug76 = debug0.addSchema(1803, SAME_NAMESPACED);
/* 543 */     debug0.addFixer((DataFix)new ItemLoreFix(debug76, false));
/*     */     
/* 545 */     Schema debug77 = debug0.addSchema(1904, net.minecraft.util.datafix.schemas.V1904::new);
/* 546 */     debug0.addFixer((DataFix)new AddNewChoices(debug77, "Added Cats", References.ENTITY));
/* 547 */     debug0.addFixer((DataFix)new EntityCatSplitFix(debug77, false));
/*     */     
/* 549 */     Schema debug78 = debug0.addSchema(1905, SAME_NAMESPACED);
/* 550 */     debug0.addFixer((DataFix)new ChunkStatusFix(debug78, false));
/*     */     
/* 552 */     Schema debug79 = debug0.addSchema(1906, net.minecraft.util.datafix.schemas.V1906::new);
/* 553 */     debug0.addFixer((DataFix)new AddNewChoices(debug79, "Add POI Blocks", References.BLOCK_ENTITY));
/*     */     
/* 555 */     Schema debug80 = debug0.addSchema(1909, net.minecraft.util.datafix.schemas.V1909::new);
/* 556 */     debug0.addFixer((DataFix)new AddNewChoices(debug80, "Add jigsaw", References.BLOCK_ENTITY));
/*     */     
/* 558 */     Schema debug81 = debug0.addSchema(1911, SAME_NAMESPACED);
/* 559 */     debug0.addFixer((DataFix)new ChunkStatusFix2(debug81, false));
/*     */     
/* 561 */     Schema debug82 = debug0.addSchema(1917, SAME_NAMESPACED);
/* 562 */     debug0.addFixer((DataFix)new CatTypeFix(debug82, false));
/*     */     
/* 564 */     Schema debug83 = debug0.addSchema(1918, SAME_NAMESPACED);
/* 565 */     debug0.addFixer((DataFix)new VillagerDataFix(debug83, "minecraft:villager"));
/* 566 */     debug0.addFixer((DataFix)new VillagerDataFix(debug83, "minecraft:zombie_villager"));
/*     */     
/* 568 */     Schema debug84 = debug0.addSchema(1920, net.minecraft.util.datafix.schemas.V1920::new);
/* 569 */     debug0.addFixer((DataFix)new NewVillageFix(debug84, false));
/* 570 */     debug0.addFixer((DataFix)new AddNewChoices(debug84, "Add campfire", References.BLOCK_ENTITY));
/*     */     
/* 572 */     Schema debug85 = debug0.addSchema(1925, SAME_NAMESPACED);
/* 573 */     debug0.addFixer((DataFix)new MapIdFix(debug85, false));
/*     */     
/* 575 */     Schema debug86 = debug0.addSchema(1928, net.minecraft.util.datafix.schemas.V1928::new);
/* 576 */     debug0.addFixer((DataFix)new EntityRavagerRenameFix(debug86, true));
/* 577 */     debug0.addFixer(ItemRenameFix.create(debug86, "Rename ravager egg item", createRenamer(EntityRavagerRenameFix.RENAMED_IDS)));
/*     */     
/* 579 */     Schema debug87 = debug0.addSchema(1929, net.minecraft.util.datafix.schemas.V1929::new);
/* 580 */     debug0.addFixer((DataFix)new AddNewChoices(debug87, "Add Wandering Trader and Trader Llama", References.ENTITY));
/*     */     
/* 582 */     Schema debug88 = debug0.addSchema(1931, net.minecraft.util.datafix.schemas.V1931::new);
/* 583 */     debug0.addFixer((DataFix)new AddNewChoices(debug88, "Added Fox", References.ENTITY));
/*     */     
/* 585 */     Schema debug89 = debug0.addSchema(1936, SAME_NAMESPACED);
/* 586 */     debug0.addFixer((DataFix)new OptionsAddTextBackgroundFix(debug89, false));
/*     */     
/* 588 */     Schema debug90 = debug0.addSchema(1946, SAME_NAMESPACED);
/* 589 */     debug0.addFixer((DataFix)new ReorganizePoi(debug90, false));
/*     */     
/* 591 */     Schema debug91 = debug0.addSchema(1948, SAME_NAMESPACED);
/* 592 */     debug0.addFixer((DataFix)new OminousBannerRenameFix(debug91, false));
/*     */     
/* 594 */     Schema debug92 = debug0.addSchema(1953, SAME_NAMESPACED);
/* 595 */     debug0.addFixer((DataFix)new OminousBannerBlockEntityRenameFix(debug92, false));
/*     */     
/* 597 */     Schema debug93 = debug0.addSchema(1955, SAME_NAMESPACED);
/* 598 */     debug0.addFixer((DataFix)new VillagerRebuildLevelAndXpFix(debug93, false));
/* 599 */     debug0.addFixer((DataFix)new ZombieVillagerRebuildXpFix(debug93, false));
/*     */     
/* 601 */     Schema debug94 = debug0.addSchema(1961, SAME_NAMESPACED);
/* 602 */     debug0.addFixer((DataFix)new ChunkLightRemoveFix(debug94, false));
/*     */     
/* 604 */     Schema debug95 = debug0.addSchema(1963, SAME_NAMESPACED);
/* 605 */     debug0.addFixer((DataFix)new RemoveGolemGossipFix(debug95, false));
/*     */     
/* 607 */     Schema debug96 = debug0.addSchema(2100, net.minecraft.util.datafix.schemas.V2100::new);
/* 608 */     debug0.addFixer((DataFix)new AddNewChoices(debug96, "Added Bee and Bee Stinger", References.ENTITY));
/* 609 */     debug0.addFixer((DataFix)new AddNewChoices(debug96, "Add beehive", References.BLOCK_ENTITY));
/* 610 */     debug0.addFixer((DataFix)new RecipesRenameFix(debug96, false, "Rename sugar recipe", createRenamer("minecraft:sugar", "sugar_from_sugar_cane")));
/* 611 */     debug0.addFixer((DataFix)new AdvancementsRenameFix(debug96, false, "Rename sugar recipe advancement", createRenamer("minecraft:recipes/misc/sugar", "minecraft:recipes/misc/sugar_from_sugar_cane")));
/*     */     
/* 613 */     Schema debug97 = debug0.addSchema(2202, SAME_NAMESPACED);
/* 614 */     debug0.addFixer((DataFix)new ChunkBiomeFix(debug97, false));
/*     */     
/* 616 */     Schema debug98 = debug0.addSchema(2209, SAME_NAMESPACED);
/* 617 */     debug0.addFixer(ItemRenameFix.create(debug98, "Rename bee_hive item to beehive", createRenamer("minecraft:bee_hive", "minecraft:beehive")));
/* 618 */     debug0.addFixer((DataFix)new BeehivePoiRenameFix(debug98));
/* 619 */     debug0.addFixer(BlockRenameFix.create(debug98, "Rename bee_hive block to beehive", createRenamer("minecraft:bee_hive", "minecraft:beehive")));
/*     */     
/* 621 */     Schema debug99 = debug0.addSchema(2211, SAME_NAMESPACED);
/* 622 */     debug0.addFixer((DataFix)new StructureReferenceCountFix(debug99, false));
/*     */     
/* 624 */     Schema debug100 = debug0.addSchema(2218, SAME_NAMESPACED);
/* 625 */     debug0.addFixer((DataFix)new ForcePoiRebuild(debug100, false));
/*     */     
/* 627 */     Schema debug101 = debug0.addSchema(2501, net.minecraft.util.datafix.schemas.V2501::new);
/* 628 */     debug0.addFixer((DataFix)new FurnaceRecipeFix(debug101, true));
/*     */     
/* 630 */     Schema debug102 = debug0.addSchema(2502, net.minecraft.util.datafix.schemas.V2502::new);
/* 631 */     debug0.addFixer((DataFix)new AddNewChoices(debug102, "Added Hoglin", References.ENTITY));
/*     */     
/* 633 */     Schema debug103 = debug0.addSchema(2503, SAME_NAMESPACED);
/* 634 */     debug0.addFixer((DataFix)new WallPropertyFix(debug103, false));
/* 635 */     debug0.addFixer((DataFix)new AdvancementsRenameFix(debug103, false, "Composter category change", createRenamer("minecraft:recipes/misc/composter", "minecraft:recipes/decorations/composter")));
/*     */     
/* 637 */     Schema debug104 = debug0.addSchema(2505, net.minecraft.util.datafix.schemas.V2505::new);
/* 638 */     debug0.addFixer((DataFix)new AddNewChoices(debug104, "Added Piglin", References.ENTITY));
/* 639 */     debug0.addFixer((DataFix)new MemoryExpiryDataFix(debug104, "minecraft:villager"));
/*     */     
/* 641 */     Schema debug105 = debug0.addSchema(2508, SAME_NAMESPACED);
/* 642 */     debug0.addFixer(ItemRenameFix.create(debug105, "Renamed fungi items to fungus", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:warped_fungi", "minecraft:warped_fungus", "minecraft:crimson_fungi", "minecraft:crimson_fungus"))));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 647 */     debug0.addFixer(BlockRenameFix.create(debug105, "Renamed fungi blocks to fungus", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:warped_fungi", "minecraft:warped_fungus", "minecraft:crimson_fungi", "minecraft:crimson_fungus"))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 653 */     Schema debug106 = debug0.addSchema(2509, net.minecraft.util.datafix.schemas.V2509::new);
/* 654 */     debug0.addFixer((DataFix)new EntityZombifiedPiglinRenameFix(debug106));
/* 655 */     debug0.addFixer(ItemRenameFix.create(debug106, "Rename zombie pigman egg item", createRenamer(EntityZombifiedPiglinRenameFix.RENAMED_IDS)));
/*     */     
/* 657 */     Schema debug107 = debug0.addSchema(2511, SAME_NAMESPACED);
/* 658 */     debug0.addFixer((DataFix)new EntityProjectileOwnerFix(debug107));
/*     */     
/* 660 */     Schema debug108 = debug0.addSchema(2514, SAME_NAMESPACED);
/* 661 */     debug0.addFixer((DataFix)new EntityUUIDFix(debug108));
/* 662 */     debug0.addFixer((DataFix)new BlockEntityUUIDFix(debug108));
/* 663 */     debug0.addFixer((DataFix)new PlayerUUIDFix(debug108));
/* 664 */     debug0.addFixer((DataFix)new LevelUUIDFix(debug108));
/* 665 */     debug0.addFixer((DataFix)new SavedDataUUIDFix(debug108));
/* 666 */     debug0.addFixer((DataFix)new ItemStackUUIDFix(debug108));
/*     */     
/* 668 */     Schema debug109 = debug0.addSchema(2516, SAME_NAMESPACED);
/* 669 */     debug0.addFixer((DataFix)new GossipUUIDFix(debug109, "minecraft:villager"));
/* 670 */     debug0.addFixer((DataFix)new GossipUUIDFix(debug109, "minecraft:zombie_villager"));
/*     */     
/* 672 */     Schema debug110 = debug0.addSchema(2518, SAME_NAMESPACED);
/* 673 */     debug0.addFixer((DataFix)new JigsawPropertiesFix(debug110, false));
/* 674 */     debug0.addFixer((DataFix)new JigsawRotationFix(debug110, false));
/*     */     
/* 676 */     Schema debug111 = debug0.addSchema(2519, net.minecraft.util.datafix.schemas.V2519::new);
/* 677 */     debug0.addFixer((DataFix)new AddNewChoices(debug111, "Added Strider", References.ENTITY));
/*     */     
/* 679 */     Schema debug112 = debug0.addSchema(2522, net.minecraft.util.datafix.schemas.V2522::new);
/* 680 */     debug0.addFixer((DataFix)new AddNewChoices(debug112, "Added Zoglin", References.ENTITY));
/*     */     
/* 682 */     Schema debug113 = debug0.addSchema(2523, SAME_NAMESPACED);
/* 683 */     debug0.addFixer((DataFix)new AttributesRename(debug113));
/*     */     
/* 685 */     Schema debug114 = debug0.addSchema(2527, SAME_NAMESPACED);
/* 686 */     debug0.addFixer((DataFix)new BitStorageAlignFix(debug114));
/*     */     
/* 688 */     Schema debug115 = debug0.addSchema(2528, SAME_NAMESPACED);
/* 689 */     debug0.addFixer(ItemRenameFix.create(debug115, "Rename soul fire torch and soul fire lantern", 
/* 690 */           createRenamer((Map<String, String>)ImmutableMap.of("minecraft:soul_fire_torch", "minecraft:soul_torch", "minecraft:soul_fire_lantern", "minecraft:soul_lantern"))));
/*     */ 
/*     */ 
/*     */     
/* 694 */     debug0.addFixer(BlockRenameFix.create(debug115, "Rename soul fire torch and soul fire lantern", createRenamer((Map<String, String>)ImmutableMap.of("minecraft:soul_fire_torch", "minecraft:soul_torch", "minecraft:soul_fire_wall_torch", "minecraft:soul_wall_torch", "minecraft:soul_fire_lantern", "minecraft:soul_lantern"))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 701 */     Schema debug116 = debug0.addSchema(2529, SAME_NAMESPACED);
/* 702 */     debug0.addFixer((DataFix)new StriderGravityFix(debug116, false));
/*     */     
/* 704 */     Schema debug117 = debug0.addSchema(2531, SAME_NAMESPACED);
/* 705 */     debug0.addFixer((DataFix)new RedstoneWireConnectionsFix(debug117));
/*     */     
/* 707 */     Schema debug118 = debug0.addSchema(2533, SAME_NAMESPACED);
/* 708 */     debug0.addFixer((DataFix)new VillagerFollowRangeFix(debug118));
/*     */     
/* 710 */     Schema debug119 = debug0.addSchema(2535, SAME_NAMESPACED);
/* 711 */     debug0.addFixer((DataFix)new EntityShulkerRotationFix(debug119));
/*     */     
/* 713 */     Schema debug120 = debug0.addSchema(2550, SAME_NAMESPACED);
/* 714 */     debug0.addFixer((DataFix)new WorldGenSettingsFix(debug120));
/*     */     
/* 716 */     Schema debug121 = debug0.addSchema(2551, net.minecraft.util.datafix.schemas.V2551::new);
/* 717 */     debug0.addFixer((DataFix)new WriteAndReadFix(debug121, "add types to WorldGenData", References.WORLD_GEN_SETTINGS));
/*     */     
/* 719 */     Schema debug122 = debug0.addSchema(2552, SAME_NAMESPACED);
/* 720 */     debug0.addFixer((DataFix)new RenameBiomesFix(debug122, false, "Nether biome rename", (Map)ImmutableMap.of("minecraft:nether", "minecraft:nether_wastes")));
/*     */ 
/*     */     
/* 723 */     Schema debug123 = debug0.addSchema(2553, SAME_NAMESPACED);
/* 724 */     debug0.addFixer((DataFix)new BiomeFix(debug123, false));
/*     */     
/* 726 */     Schema debug124 = debug0.addSchema(2558, SAME_NAMESPACED);
/* 727 */     debug0.addFixer((DataFix)new MissingDimensionFix(debug124, false));
/* 728 */     debug0.addFixer((DataFix)new OptionsRenameFieldFix(debug124, false, "Rename swapHands setting", "key_key.swapHands", "key_key.swapOffhand"));
/*     */     
/* 730 */     Schema debug125 = debug0.addSchema(2568, net.minecraft.util.datafix.schemas.V2568::new);
/* 731 */     debug0.addFixer((DataFix)new AddNewChoices(debug125, "Added Piglin Brute", References.ENTITY));
/*     */   }
/*     */   
/*     */   private static UnaryOperator<String> createRenamer(Map<String, String> debug0) {
/* 735 */     return debug1 -> (String)debug0.getOrDefault(debug1, debug1);
/*     */   }
/*     */   
/*     */   private static UnaryOperator<String> createRenamer(String debug0, String debug1) {
/* 739 */     return debug2 -> Objects.equals(debug2, debug0) ? debug1 : debug2;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\datafix\DataFixers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */