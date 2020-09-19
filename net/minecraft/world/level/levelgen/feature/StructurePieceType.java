/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface StructurePieceType
/*    */ {
/* 27 */   public static final StructurePieceType MINE_SHAFT_CORRIDOR = setPieceId(net.minecraft.world.level.levelgen.structure.MineShaftPieces.MineShaftCorridor::new, "MSCorridor");
/* 28 */   public static final StructurePieceType MINE_SHAFT_CROSSING = setPieceId(net.minecraft.world.level.levelgen.structure.MineShaftPieces.MineShaftCrossing::new, "MSCrossing");
/* 29 */   public static final StructurePieceType MINE_SHAFT_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.MineShaftPieces.MineShaftRoom::new, "MSRoom");
/* 30 */   public static final StructurePieceType MINE_SHAFT_STAIRS = setPieceId(net.minecraft.world.level.levelgen.structure.MineShaftPieces.MineShaftStairs::new, "MSStairs");
/* 31 */   public static final StructurePieceType NETHER_FORTRESS_BRIDGE_CROSSING = setPieceId(net.minecraft.world.level.levelgen.structure.NetherBridgePieces.BridgeCrossing::new, "NeBCr");
/* 32 */   public static final StructurePieceType NETHER_FORTRESS_BRIDGE_END_FILLER = setPieceId(net.minecraft.world.level.levelgen.structure.NetherBridgePieces.BridgeEndFiller::new, "NeBEF");
/* 33 */   public static final StructurePieceType NETHER_FORTRESS_BRIDGE_STRAIGHT = setPieceId(net.minecraft.world.level.levelgen.structure.NetherBridgePieces.BridgeStraight::new, "NeBS");
/* 34 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS = setPieceId(net.minecraft.world.level.levelgen.structure.NetherBridgePieces.CastleCorridorStairsPiece::new, "NeCCS");
/* 35 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY = setPieceId(net.minecraft.world.level.levelgen.structure.NetherBridgePieces.CastleCorridorTBalconyPiece::new, "NeCTB");
/* 36 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_ENTRANCE = setPieceId(net.minecraft.world.level.levelgen.structure.NetherBridgePieces.CastleEntrance::new, "NeCE");
/* 37 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING = setPieceId(net.minecraft.world.level.levelgen.structure.NetherBridgePieces.CastleSmallCorridorCrossingPiece::new, "NeSCSC");
/* 38 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN = setPieceId(net.minecraft.world.level.levelgen.structure.NetherBridgePieces.CastleSmallCorridorLeftTurnPiece::new, "NeSCLT");
/* 39 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR = setPieceId(net.minecraft.world.level.levelgen.structure.NetherBridgePieces.CastleSmallCorridorPiece::new, "NeSC");
/* 40 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN = setPieceId(net.minecraft.world.level.levelgen.structure.NetherBridgePieces.CastleSmallCorridorRightTurnPiece::new, "NeSCRT");
/* 41 */   public static final StructurePieceType NETHER_FORTRESS_CASTLE_STALK_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.NetherBridgePieces.CastleStalkRoom::new, "NeCSR");
/* 42 */   public static final StructurePieceType NETHER_FORTRESS_MONSTER_THRONE = setPieceId(net.minecraft.world.level.levelgen.structure.NetherBridgePieces.MonsterThrone::new, "NeMT");
/* 43 */   public static final StructurePieceType NETHER_FORTRESS_ROOM_CROSSING = setPieceId(net.minecraft.world.level.levelgen.structure.NetherBridgePieces.RoomCrossing::new, "NeRC");
/* 44 */   public static final StructurePieceType NETHER_FORTRESS_STAIRS_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.NetherBridgePieces.StairsRoom::new, "NeSR");
/* 45 */   public static final StructurePieceType NETHER_FORTRESS_START = setPieceId(net.minecraft.world.level.levelgen.structure.NetherBridgePieces.StartPiece::new, "NeStart");
/* 46 */   public static final StructurePieceType STRONGHOLD_CHEST_CORRIDOR = setPieceId(net.minecraft.world.level.levelgen.structure.StrongholdPieces.ChestCorridor::new, "SHCC");
/* 47 */   public static final StructurePieceType STRONGHOLD_FILLER_CORRIDOR = setPieceId(net.minecraft.world.level.levelgen.structure.StrongholdPieces.FillerCorridor::new, "SHFC");
/* 48 */   public static final StructurePieceType STRONGHOLD_FIVE_CROSSING = setPieceId(net.minecraft.world.level.levelgen.structure.StrongholdPieces.FiveCrossing::new, "SH5C");
/* 49 */   public static final StructurePieceType STRONGHOLD_LEFT_TURN = setPieceId(net.minecraft.world.level.levelgen.structure.StrongholdPieces.LeftTurn::new, "SHLT");
/* 50 */   public static final StructurePieceType STRONGHOLD_LIBRARY = setPieceId(net.minecraft.world.level.levelgen.structure.StrongholdPieces.Library::new, "SHLi");
/* 51 */   public static final StructurePieceType STRONGHOLD_PORTAL_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.StrongholdPieces.PortalRoom::new, "SHPR");
/* 52 */   public static final StructurePieceType STRONGHOLD_PRISON_HALL = setPieceId(net.minecraft.world.level.levelgen.structure.StrongholdPieces.PrisonHall::new, "SHPH");
/* 53 */   public static final StructurePieceType STRONGHOLD_RIGHT_TURN = setPieceId(net.minecraft.world.level.levelgen.structure.StrongholdPieces.RightTurn::new, "SHRT");
/* 54 */   public static final StructurePieceType STRONGHOLD_ROOM_CROSSING = setPieceId(net.minecraft.world.level.levelgen.structure.StrongholdPieces.RoomCrossing::new, "SHRC");
/* 55 */   public static final StructurePieceType STRONGHOLD_STAIRS_DOWN = setPieceId(net.minecraft.world.level.levelgen.structure.StrongholdPieces.StairsDown::new, "SHSD");
/* 56 */   public static final StructurePieceType STRONGHOLD_START = setPieceId(net.minecraft.world.level.levelgen.structure.StrongholdPieces.StartPiece::new, "SHStart");
/* 57 */   public static final StructurePieceType STRONGHOLD_STRAIGHT = setPieceId(net.minecraft.world.level.levelgen.structure.StrongholdPieces.Straight::new, "SHS");
/* 58 */   public static final StructurePieceType STRONGHOLD_STRAIGHT_STAIRS_DOWN = setPieceId(net.minecraft.world.level.levelgen.structure.StrongholdPieces.StraightStairsDown::new, "SHSSD");
/* 59 */   public static final StructurePieceType JUNGLE_PYRAMID_PIECE = setPieceId(net.minecraft.world.level.levelgen.structure.JunglePyramidPiece::new, "TeJP");
/* 60 */   public static final StructurePieceType OCEAN_RUIN = setPieceId(net.minecraft.world.level.levelgen.structure.OceanRuinPieces.OceanRuinPiece::new, "ORP");
/* 61 */   public static final StructurePieceType IGLOO = setPieceId(net.minecraft.world.level.levelgen.structure.IglooPieces.IglooPiece::new, "Iglu");
/* 62 */   public static final StructurePieceType RUINED_PORTAL = setPieceId(net.minecraft.world.level.levelgen.structure.RuinedPortalPiece::new, "RUPO");
/* 63 */   public static final StructurePieceType SWAMPLAND_HUT = setPieceId(net.minecraft.world.level.levelgen.structure.SwamplandHutPiece::new, "TeSH");
/* 64 */   public static final StructurePieceType DESERT_PYRAMID_PIECE = setPieceId(net.minecraft.world.level.levelgen.structure.DesertPyramidPiece::new, "TeDP");
/* 65 */   public static final StructurePieceType OCEAN_MONUMENT_BUILDING = setPieceId(net.minecraft.world.level.levelgen.structure.OceanMonumentPieces.MonumentBuilding::new, "OMB");
/* 66 */   public static final StructurePieceType OCEAN_MONUMENT_CORE_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.OceanMonumentPieces.OceanMonumentCoreRoom::new, "OMCR");
/* 67 */   public static final StructurePieceType OCEAN_MONUMENT_DOUBLE_X_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.OceanMonumentPieces.OceanMonumentDoubleXRoom::new, "OMDXR");
/* 68 */   public static final StructurePieceType OCEAN_MONUMENT_DOUBLE_XY_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.OceanMonumentPieces.OceanMonumentDoubleXYRoom::new, "OMDXYR");
/* 69 */   public static final StructurePieceType OCEAN_MONUMENT_DOUBLE_Y_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.OceanMonumentPieces.OceanMonumentDoubleYRoom::new, "OMDYR");
/* 70 */   public static final StructurePieceType OCEAN_MONUMENT_DOUBLE_YZ_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.OceanMonumentPieces.OceanMonumentDoubleYZRoom::new, "OMDYZR");
/* 71 */   public static final StructurePieceType OCEAN_MONUMENT_DOUBLE_Z_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.OceanMonumentPieces.OceanMonumentDoubleZRoom::new, "OMDZR");
/* 72 */   public static final StructurePieceType OCEAN_MONUMENT_ENTRY_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.OceanMonumentPieces.OceanMonumentEntryRoom::new, "OMEntry");
/* 73 */   public static final StructurePieceType OCEAN_MONUMENT_PENTHOUSE = setPieceId(net.minecraft.world.level.levelgen.structure.OceanMonumentPieces.OceanMonumentPenthouse::new, "OMPenthouse");
/* 74 */   public static final StructurePieceType OCEAN_MONUMENT_SIMPLE_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.OceanMonumentPieces.OceanMonumentSimpleRoom::new, "OMSimple");
/* 75 */   public static final StructurePieceType OCEAN_MONUMENT_SIMPLE_TOP_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.OceanMonumentPieces.OceanMonumentSimpleTopRoom::new, "OMSimpleT");
/* 76 */   public static final StructurePieceType OCEAN_MONUMENT_WING_ROOM = setPieceId(net.minecraft.world.level.levelgen.structure.OceanMonumentPieces.OceanMonumentWingRoom::new, "OMWR");
/* 77 */   public static final StructurePieceType END_CITY_PIECE = setPieceId(net.minecraft.world.level.levelgen.structure.EndCityPieces.EndCityPiece::new, "ECP");
/* 78 */   public static final StructurePieceType WOODLAND_MANSION_PIECE = setPieceId(net.minecraft.world.level.levelgen.structure.WoodlandMansionPieces.WoodlandMansionPiece::new, "WMP");
/* 79 */   public static final StructurePieceType BURIED_TREASURE_PIECE = setPieceId(net.minecraft.world.level.levelgen.structure.BuriedTreasurePieces.BuriedTreasurePiece::new, "BTP");
/* 80 */   public static final StructurePieceType SHIPWRECK_PIECE = setPieceId(net.minecraft.world.level.levelgen.structure.ShipwreckPieces.ShipwreckPiece::new, "Shipwreck");
/* 81 */   public static final StructurePieceType NETHER_FOSSIL = setPieceId(net.minecraft.world.level.levelgen.structure.NetherFossilPieces.NetherFossilPiece::new, "NeFos");
/* 82 */   public static final StructurePieceType JIGSAW = setPieceId(net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece::new, "jigsaw");
/*    */ 
/*    */   
/*    */   StructurePiece load(StructureManager paramStructureManager, CompoundTag paramCompoundTag);
/*    */ 
/*    */   
/*    */   static StructurePieceType setPieceId(StructurePieceType debug0, String debug1) {
/* 89 */     return (StructurePieceType)Registry.register(Registry.STRUCTURE_PIECE, debug1.toLowerCase(Locale.ROOT), debug0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\StructurePieceType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */