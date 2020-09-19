/*      */ package net.minecraft.server.network;
/*      */ 
/*      */ import com.google.common.primitives.Doubles;
/*      */ import com.google.common.primitives.Floats;
/*      */ import com.mojang.brigadier.ParseResults;
/*      */ import com.mojang.brigadier.StringReader;
/*      */ import com.mojang.brigadier.suggestion.Suggestions;
/*      */ import io.netty.util.concurrent.Future;
/*      */ import io.netty.util.concurrent.GenericFutureListener;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ShortMap;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ShortOpenHashMap;
/*      */ import java.util.Collections;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.stream.Stream;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.Util;
/*      */ import net.minecraft.advancements.Advancement;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.commands.CommandSourceStack;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.NonNullList;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.ListTag;
/*      */ import net.minecraft.nbt.StringTag;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.Connection;
/*      */ import net.minecraft.network.PacketListener;
/*      */ import net.minecraft.network.chat.ChatType;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.TextComponent;
/*      */ import net.minecraft.network.chat.TranslatableComponent;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.PacketUtils;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundChatPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerAckPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundKeepAlivePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundMoveVehiclePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTagQueryPacket;
/*      */ import net.minecraft.network.protocol.game.ServerGamePacketListener;
/*      */ import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundBlockEntityTagQuery;
/*      */ import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundChatPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundContainerAckPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundEntityTagQuery;
/*      */ import net.minecraft.network.protocol.game.ServerboundInteractPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundJigsawGeneratePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundKeepAlivePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlaceRecipePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerAbilitiesPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundRecipeBookSeenRecipePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundResourcePackPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetCommandBlockPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetJigsawBlockPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundSwingPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
/*      */ import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
/*      */ import net.minecraft.resources.ResourceLocation;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.util.StringUtil;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.MoverType;
/*      */ import net.minecraft.world.entity.PlayerRideableJumping;
/*      */ import net.minecraft.world.entity.animal.horse.AbstractHorse;
/*      */ import net.minecraft.world.entity.player.ChatVisiblity;
/*      */ import net.minecraft.world.entity.player.Inventory;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.vehicle.Boat;
/*      */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*      */ import net.minecraft.world.inventory.AnvilMenu;
/*      */ import net.minecraft.world.inventory.BeaconMenu;
/*      */ import net.minecraft.world.inventory.MerchantMenu;
/*      */ import net.minecraft.world.inventory.RecipeBookMenu;
/*      */ import net.minecraft.world.inventory.Slot;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.WritableBookItem;
/*      */ import net.minecraft.world.item.crafting.Recipe;
/*      */ import net.minecraft.world.level.BaseCommandBlock;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.GameRules;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.CommandBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.BooleanOp;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ public class ServerGamePacketListenerImpl
/*      */   implements ServerGamePacketListener
/*      */ {
/*  156 */   private static final Logger LOGGER = LogManager.getLogger();
/*      */   
/*      */   public final Connection connection;
/*      */   
/*      */   private final MinecraftServer server;
/*      */   
/*      */   public ServerPlayer player;
/*      */   
/*      */   private int tickCount;
/*      */   private long keepAliveTime;
/*      */   private boolean keepAlivePending;
/*      */   private long keepAliveChallenge;
/*      */   private int chatSpamTickCount;
/*      */   private int dropSpamTickCount;
/*  170 */   private final Int2ShortMap expectedAcks = (Int2ShortMap)new Int2ShortOpenHashMap();
/*      */   private double firstGoodX;
/*      */   private double firstGoodY;
/*      */   private double firstGoodZ;
/*      */   private double lastGoodX;
/*      */   private double lastGoodY;
/*      */   private double lastGoodZ;
/*      */   private Entity lastVehicle;
/*      */   private double vehicleFirstGoodX;
/*      */   private double vehicleFirstGoodY;
/*      */   private double vehicleFirstGoodZ;
/*      */   private double vehicleLastGoodX;
/*      */   private double vehicleLastGoodY;
/*      */   private double vehicleLastGoodZ;
/*      */   private Vec3 awaitingPositionFromClient;
/*      */   private int awaitingTeleport;
/*      */   private int awaitingTeleportTime;
/*      */   private boolean clientIsFloating;
/*      */   private int aboveGroundTickCount;
/*      */   private boolean clientVehicleIsFloating;
/*      */   private int aboveGroundVehicleTickCount;
/*      */   private int receivedMovePacketCount;
/*      */   private int knownMovePacketCount;
/*      */   
/*      */   public ServerGamePacketListenerImpl(MinecraftServer debug1, Connection debug2, ServerPlayer debug3) {
/*  195 */     this.server = debug1;
/*  196 */     this.connection = debug2;
/*  197 */     debug2.setListener((PacketListener)this);
/*  198 */     this.player = debug3;
/*  199 */     debug3.connection = this;
/*      */   }
/*      */   
/*      */   public void tick() {
/*  203 */     resetPosition();
/*  204 */     this.player.xo = this.player.getX();
/*  205 */     this.player.yo = this.player.getY();
/*  206 */     this.player.zo = this.player.getZ();
/*  207 */     this.player.doTick();
/*  208 */     this.player.absMoveTo(this.firstGoodX, this.firstGoodY, this.firstGoodZ, this.player.yRot, this.player.xRot);
/*  209 */     this.tickCount++;
/*  210 */     this.knownMovePacketCount = this.receivedMovePacketCount;
/*      */     
/*  212 */     if (this.clientIsFloating && !this.player.isSleeping()) {
/*  213 */       if (++this.aboveGroundTickCount > 80) {
/*  214 */         LOGGER.warn("{} was kicked for floating too long!", this.player.getName().getString());
/*  215 */         disconnect((Component)new TranslatableComponent("multiplayer.disconnect.flying"));
/*      */         return;
/*      */       } 
/*      */     } else {
/*  219 */       this.clientIsFloating = false;
/*  220 */       this.aboveGroundTickCount = 0;
/*      */     } 
/*      */     
/*  223 */     this.lastVehicle = this.player.getRootVehicle();
/*  224 */     if (this.lastVehicle == this.player || this.lastVehicle.getControllingPassenger() != this.player) {
/*  225 */       this.lastVehicle = null;
/*  226 */       this.clientVehicleIsFloating = false;
/*  227 */       this.aboveGroundVehicleTickCount = 0;
/*      */     } else {
/*  229 */       this.vehicleFirstGoodX = this.lastVehicle.getX();
/*  230 */       this.vehicleFirstGoodY = this.lastVehicle.getY();
/*  231 */       this.vehicleFirstGoodZ = this.lastVehicle.getZ();
/*  232 */       this.vehicleLastGoodX = this.lastVehicle.getX();
/*  233 */       this.vehicleLastGoodY = this.lastVehicle.getY();
/*  234 */       this.vehicleLastGoodZ = this.lastVehicle.getZ();
/*  235 */       if (this.clientVehicleIsFloating && this.player.getRootVehicle().getControllingPassenger() == this.player) {
/*  236 */         if (++this.aboveGroundVehicleTickCount > 80) {
/*  237 */           LOGGER.warn("{} was kicked for floating a vehicle too long!", this.player.getName().getString());
/*  238 */           disconnect((Component)new TranslatableComponent("multiplayer.disconnect.flying"));
/*      */           return;
/*      */         } 
/*      */       } else {
/*  242 */         this.clientVehicleIsFloating = false;
/*  243 */         this.aboveGroundVehicleTickCount = 0;
/*      */       } 
/*      */     } 
/*      */     
/*  247 */     this.server.getProfiler().push("keepAlive");
/*  248 */     long debug1 = Util.getMillis();
/*  249 */     if (debug1 - this.keepAliveTime >= 15000L) {
/*  250 */       if (this.keepAlivePending) {
/*  251 */         disconnect((Component)new TranslatableComponent("disconnect.timeout"));
/*      */       } else {
/*  253 */         this.keepAlivePending = true;
/*  254 */         this.keepAliveTime = debug1;
/*  255 */         this.keepAliveChallenge = debug1;
/*  256 */         send((Packet<?>)new ClientboundKeepAlivePacket(this.keepAliveChallenge));
/*      */       } 
/*      */     }
/*  259 */     this.server.getProfiler().pop();
/*      */     
/*  261 */     if (this.chatSpamTickCount > 0) {
/*  262 */       this.chatSpamTickCount--;
/*      */     }
/*  264 */     if (this.dropSpamTickCount > 0) {
/*  265 */       this.dropSpamTickCount--;
/*      */     }
/*      */     
/*  268 */     if (this.player.getLastActionTime() > 0L && this.server.getPlayerIdleTimeout() > 0 && Util.getMillis() - this.player.getLastActionTime() > (this.server.getPlayerIdleTimeout() * 1000 * 60)) {
/*  269 */       disconnect((Component)new TranslatableComponent("multiplayer.disconnect.idling"));
/*      */     }
/*      */   }
/*      */   
/*      */   public void resetPosition() {
/*  274 */     this.firstGoodX = this.player.getX();
/*  275 */     this.firstGoodY = this.player.getY();
/*  276 */     this.firstGoodZ = this.player.getZ();
/*  277 */     this.lastGoodX = this.player.getX();
/*  278 */     this.lastGoodY = this.player.getY();
/*  279 */     this.lastGoodZ = this.player.getZ();
/*      */   }
/*      */ 
/*      */   
/*      */   public Connection getConnection() {
/*  284 */     return this.connection;
/*      */   }
/*      */   
/*      */   private boolean isSingleplayerOwner() {
/*  288 */     return this.server.isSingleplayerOwner(this.player.getGameProfile());
/*      */   }
/*      */   
/*      */   public void disconnect(Component debug1) {
/*  292 */     this.connection.send((Packet)new ClientboundDisconnectPacket(debug1), debug2 -> this.connection.disconnect(debug1));
/*  293 */     this.connection.setReadOnly();
/*  294 */     this.server.executeBlocking(this.connection::handleDisconnection);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePlayerInput(ServerboundPlayerInputPacket debug1) {
/*  299 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  300 */     this.player.setPlayerInput(debug1.getXxa(), debug1.getZza(), debug1.isJumping(), debug1.isShiftKeyDown());
/*      */   }
/*      */   
/*      */   private static boolean containsInvalidValues(ServerboundMovePlayerPacket debug0) {
/*  304 */     if (!Doubles.isFinite(debug0.getX(0.0D)) || !Doubles.isFinite(debug0.getY(0.0D)) || !Doubles.isFinite(debug0.getZ(0.0D)) || !Floats.isFinite(debug0.getXRot(0.0F)) || !Floats.isFinite(debug0.getYRot(0.0F))) {
/*  305 */       return true;
/*      */     }
/*  307 */     if (Math.abs(debug0.getX(0.0D)) > 3.0E7D || Math.abs(debug0.getY(0.0D)) > 3.0E7D || Math.abs(debug0.getZ(0.0D)) > 3.0E7D) {
/*  308 */       return true;
/*      */     }
/*  310 */     return false;
/*      */   }
/*      */   
/*      */   private static boolean containsInvalidValues(ServerboundMoveVehiclePacket debug0) {
/*  314 */     return (!Doubles.isFinite(debug0.getX()) || !Doubles.isFinite(debug0.getY()) || !Doubles.isFinite(debug0.getZ()) || !Floats.isFinite(debug0.getXRot()) || !Floats.isFinite(debug0.getYRot()));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleMoveVehicle(ServerboundMoveVehiclePacket debug1) {
/*  319 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  320 */     if (containsInvalidValues(debug1)) {
/*  321 */       disconnect((Component)new TranslatableComponent("multiplayer.disconnect.invalid_vehicle_movement"));
/*      */       
/*      */       return;
/*      */     } 
/*  325 */     Entity debug2 = this.player.getRootVehicle();
/*  326 */     if (debug2 != this.player && debug2.getControllingPassenger() == this.player && debug2 == this.lastVehicle) {
/*  327 */       ServerLevel debug3 = this.player.getLevel();
/*  328 */       double debug4 = debug2.getX();
/*  329 */       double debug6 = debug2.getY();
/*  330 */       double debug8 = debug2.getZ();
/*      */       
/*  332 */       double debug10 = debug1.getX();
/*  333 */       double debug12 = debug1.getY();
/*  334 */       double debug14 = debug1.getZ();
/*      */       
/*  336 */       float debug16 = debug1.getYRot();
/*  337 */       float debug17 = debug1.getXRot();
/*      */       
/*  339 */       double debug18 = debug10 - this.vehicleFirstGoodX;
/*  340 */       double debug20 = debug12 - this.vehicleFirstGoodY;
/*  341 */       double debug22 = debug14 - this.vehicleFirstGoodZ;
/*      */       
/*  343 */       double debug24 = debug2.getDeltaMovement().lengthSqr();
/*  344 */       double debug26 = debug18 * debug18 + debug20 * debug20 + debug22 * debug22;
/*      */       
/*  346 */       if (debug26 - debug24 > 100.0D && !isSingleplayerOwner()) {
/*  347 */         LOGGER.warn("{} (vehicle of {}) moved too quickly! {},{},{}", debug2.getName().getString(), this.player.getName().getString(), Double.valueOf(debug18), Double.valueOf(debug20), Double.valueOf(debug22));
/*  348 */         this.connection.send((Packet)new ClientboundMoveVehiclePacket(debug2));
/*      */         
/*      */         return;
/*      */       } 
/*  352 */       boolean debug28 = debug3.noCollision(debug2, debug2.getBoundingBox().deflate(0.0625D));
/*      */ 
/*      */       
/*  355 */       debug18 = debug10 - this.vehicleLastGoodX;
/*  356 */       debug20 = debug12 - this.vehicleLastGoodY - 1.0E-6D;
/*  357 */       debug22 = debug14 - this.vehicleLastGoodZ;
/*      */       
/*  359 */       debug2.move(MoverType.PLAYER, new Vec3(debug18, debug20, debug22));
/*      */       
/*  361 */       double debug29 = debug20;
/*      */       
/*  363 */       debug18 = debug10 - debug2.getX();
/*  364 */       debug20 = debug12 - debug2.getY();
/*  365 */       if (debug20 > -0.5D || debug20 < 0.5D) {
/*  366 */         debug20 = 0.0D;
/*      */       }
/*  368 */       debug22 = debug14 - debug2.getZ();
/*  369 */       debug26 = debug18 * debug18 + debug20 * debug20 + debug22 * debug22;
/*  370 */       boolean debug31 = false;
/*  371 */       if (debug26 > 0.0625D) {
/*  372 */         debug31 = true;
/*  373 */         LOGGER.warn("{} (vehicle of {}) moved wrongly! {}", debug2.getName().getString(), this.player.getName().getString(), Double.valueOf(Math.sqrt(debug26)));
/*      */       } 
/*  375 */       debug2.absMoveTo(debug10, debug12, debug14, debug16, debug17);
/*      */       
/*  377 */       boolean debug32 = debug3.noCollision(debug2, debug2.getBoundingBox().deflate(0.0625D));
/*  378 */       if (debug28 && (debug31 || !debug32)) {
/*  379 */         debug2.absMoveTo(debug4, debug6, debug8, debug16, debug17);
/*  380 */         this.connection.send((Packet)new ClientboundMoveVehiclePacket(debug2));
/*      */         
/*      */         return;
/*      */       } 
/*  384 */       this.player.getLevel().getChunkSource().move(this.player);
/*  385 */       this.player.checkMovementStatistics(this.player.getX() - debug4, this.player.getY() - debug6, this.player.getZ() - debug8);
/*  386 */       this.clientVehicleIsFloating = (debug29 >= -0.03125D && !this.server.isFlightAllowed() && noBlocksAround(debug2));
/*      */       
/*  388 */       this.vehicleLastGoodX = debug2.getX();
/*  389 */       this.vehicleLastGoodY = debug2.getY();
/*  390 */       this.vehicleLastGoodZ = debug2.getZ();
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean noBlocksAround(Entity debug1) {
/*  395 */     return debug1.level.getBlockStates(debug1.getBoundingBox().inflate(0.0625D).expandTowards(0.0D, -0.55D, 0.0D)).allMatch(BlockBehaviour.BlockStateBase::isAir);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket debug1) {
/*  400 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  401 */     if (debug1.getId() == this.awaitingTeleport) {
/*  402 */       this.player.absMoveTo(this.awaitingPositionFromClient.x, this.awaitingPositionFromClient.y, this.awaitingPositionFromClient.z, this.player.yRot, this.player.xRot);
/*      */       
/*  404 */       this.lastGoodX = this.awaitingPositionFromClient.x;
/*  405 */       this.lastGoodY = this.awaitingPositionFromClient.y;
/*  406 */       this.lastGoodZ = this.awaitingPositionFromClient.z;
/*  407 */       if (this.player.isChangingDimension()) {
/*  408 */         this.player.hasChangedDimension();
/*      */       }
/*      */       
/*  411 */       this.awaitingPositionFromClient = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket debug1) {
/*  417 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  418 */     this.server.getRecipeManager().byKey(debug1.getRecipe()).ifPresent(this.player.getRecipeBook()::removeHighlight);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleRecipeBookChangeSettingsPacket(ServerboundRecipeBookChangeSettingsPacket debug1) {
/*  423 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  424 */     this.player.getRecipeBook().setBookSetting(debug1.getBookType(), debug1.isOpen(), debug1.isFiltering());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSeenAdvancements(ServerboundSeenAdvancementsPacket debug1) {
/*  429 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  430 */     if (debug1.getAction() == ServerboundSeenAdvancementsPacket.Action.OPENED_TAB) {
/*  431 */       ResourceLocation debug2 = debug1.getTab();
/*  432 */       Advancement debug3 = this.server.getAdvancements().getAdvancement(debug2);
/*  433 */       if (debug3 != null) {
/*  434 */         this.player.getAdvancements().setSelectedTab(debug3);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket debug1) {
/*  441 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  442 */     StringReader debug2 = new StringReader(debug1.getCommand());
/*  443 */     if (debug2.canRead() && debug2.peek() == '/') {
/*  444 */       debug2.skip();
/*      */     }
/*  446 */     ParseResults<CommandSourceStack> debug3 = this.server.getCommands().getDispatcher().parse(debug2, this.player.createCommandSourceStack());
/*  447 */     this.server.getCommands().getDispatcher().getCompletionSuggestions(debug3).thenAccept(debug2 -> this.connection.send((Packet)new ClientboundCommandSuggestionsPacket(debug1.getId(), debug2)));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetCommandBlock(ServerboundSetCommandBlockPacket debug1) {
/*  452 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  453 */     if (!this.server.isCommandBlockEnabled()) {
/*  454 */       this.player.sendMessage((Component)new TranslatableComponent("advMode.notEnabled"), Util.NIL_UUID);
/*      */       return;
/*      */     } 
/*  457 */     if (!this.player.canUseGameMasterBlocks()) {
/*  458 */       this.player.sendMessage((Component)new TranslatableComponent("advMode.notAllowed"), Util.NIL_UUID);
/*      */       return;
/*      */     } 
/*  461 */     BaseCommandBlock debug2 = null;
/*  462 */     CommandBlockEntity debug3 = null;
/*  463 */     BlockPos debug4 = debug1.getPos();
/*  464 */     BlockEntity debug5 = this.player.level.getBlockEntity(debug4);
/*  465 */     if (debug5 instanceof CommandBlockEntity) {
/*  466 */       debug3 = (CommandBlockEntity)debug5;
/*  467 */       debug2 = debug3.getCommandBlock();
/*      */     } 
/*      */     
/*  470 */     String debug6 = debug1.getCommand();
/*  471 */     boolean debug7 = debug1.isTrackOutput();
/*      */     
/*  473 */     if (debug2 != null) {
/*  474 */       BlockState debug10; CommandBlockEntity.Mode debug8 = debug3.getMode();
/*      */       
/*  476 */       Direction debug9 = (Direction)this.player.level.getBlockState(debug4).getValue((Property)CommandBlock.FACING);
/*      */       
/*  478 */       switch (debug1.getMode()) {
/*      */         case PERFORM_RESPAWN:
/*  480 */           debug10 = Blocks.CHAIN_COMMAND_BLOCK.defaultBlockState();
/*  481 */           this.player.level.setBlock(debug4, (BlockState)((BlockState)debug10.setValue((Property)CommandBlock.FACING, (Comparable)debug9)).setValue((Property)CommandBlock.CONDITIONAL, Boolean.valueOf(debug1.isConditional())), 2);
/*      */           break;
/*      */         case REQUEST_STATS:
/*  484 */           debug10 = Blocks.REPEATING_COMMAND_BLOCK.defaultBlockState();
/*  485 */           this.player.level.setBlock(debug4, (BlockState)((BlockState)debug10.setValue((Property)CommandBlock.FACING, (Comparable)debug9)).setValue((Property)CommandBlock.CONDITIONAL, Boolean.valueOf(debug1.isConditional())), 2);
/*      */           break;
/*      */         
/*      */         default:
/*  489 */           debug10 = Blocks.COMMAND_BLOCK.defaultBlockState();
/*  490 */           this.player.level.setBlock(debug4, (BlockState)((BlockState)debug10.setValue((Property)CommandBlock.FACING, (Comparable)debug9)).setValue((Property)CommandBlock.CONDITIONAL, Boolean.valueOf(debug1.isConditional())), 2);
/*      */           break;
/*      */       } 
/*  493 */       debug5.clearRemoved();
/*  494 */       this.player.level.setBlockEntity(debug4, debug5);
/*      */       
/*  496 */       debug2.setCommand(debug6);
/*  497 */       debug2.setTrackOutput(debug7);
/*  498 */       if (!debug7) {
/*  499 */         debug2.setLastOutput(null);
/*      */       }
/*  501 */       debug3.setAutomatic(debug1.isAutomatic());
/*  502 */       if (debug8 != debug1.getMode()) {
/*  503 */         debug3.onModeSwitch();
/*      */       }
/*  505 */       debug2.onUpdated();
/*  506 */       if (!StringUtil.isNullOrEmpty(debug6)) {
/*  507 */         this.player.sendMessage((Component)new TranslatableComponent("advMode.setCommand.success", new Object[] { debug6 }), Util.NIL_UUID);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetCommandMinecart(ServerboundSetCommandMinecartPacket debug1) {
/*  514 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  515 */     if (!this.server.isCommandBlockEnabled()) {
/*  516 */       this.player.sendMessage((Component)new TranslatableComponent("advMode.notEnabled"), Util.NIL_UUID);
/*      */       return;
/*      */     } 
/*  519 */     if (!this.player.canUseGameMasterBlocks()) {
/*  520 */       this.player.sendMessage((Component)new TranslatableComponent("advMode.notAllowed"), Util.NIL_UUID);
/*      */       return;
/*      */     } 
/*  523 */     BaseCommandBlock debug2 = debug1.getCommandBlock(this.player.level);
/*      */     
/*  525 */     if (debug2 != null) {
/*  526 */       debug2.setCommand(debug1.getCommand());
/*  527 */       debug2.setTrackOutput(debug1.isTrackOutput());
/*  528 */       if (!debug1.isTrackOutput()) {
/*  529 */         debug2.setLastOutput(null);
/*      */       }
/*  531 */       debug2.onUpdated();
/*  532 */       this.player.sendMessage((Component)new TranslatableComponent("advMode.setCommand.success", new Object[] { debug1.getCommand() }), Util.NIL_UUID);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePickItem(ServerboundPickItemPacket debug1) {
/*  538 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  539 */     this.player.inventory.pickSlot(debug1.getSlot());
/*  540 */     this.player.connection.send((Packet<?>)new ClientboundContainerSetSlotPacket(-2, this.player.inventory.selected, this.player.inventory.getItem(this.player.inventory.selected)));
/*  541 */     this.player.connection.send((Packet<?>)new ClientboundContainerSetSlotPacket(-2, debug1.getSlot(), this.player.inventory.getItem(debug1.getSlot())));
/*  542 */     this.player.connection.send((Packet<?>)new ClientboundSetCarriedItemPacket(this.player.inventory.selected));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleRenameItem(ServerboundRenameItemPacket debug1) {
/*  547 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  548 */     if (this.player.containerMenu instanceof AnvilMenu) {
/*  549 */       AnvilMenu debug2 = (AnvilMenu)this.player.containerMenu;
/*  550 */       String debug3 = SharedConstants.filterText(debug1.getName());
/*  551 */       if (debug3.length() <= 35) {
/*  552 */         debug2.setItemName(debug3);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetBeaconPacket(ServerboundSetBeaconPacket debug1) {
/*  559 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  560 */     if (this.player.containerMenu instanceof BeaconMenu) {
/*  561 */       ((BeaconMenu)this.player.containerMenu).updateEffects(debug1.getPrimary(), debug1.getSecondary());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetStructureBlock(ServerboundSetStructureBlockPacket debug1) {
/*  567 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  568 */     if (!this.player.canUseGameMasterBlocks()) {
/*      */       return;
/*      */     }
/*  571 */     BlockPos debug2 = debug1.getPos();
/*  572 */     BlockState debug3 = this.player.level.getBlockState(debug2);
/*  573 */     BlockEntity debug4 = this.player.level.getBlockEntity(debug2);
/*  574 */     if (debug4 instanceof StructureBlockEntity) {
/*  575 */       StructureBlockEntity debug5 = (StructureBlockEntity)debug4;
/*      */       
/*  577 */       debug5.setMode(debug1.getMode());
/*  578 */       debug5.setStructureName(debug1.getName());
/*  579 */       debug5.setStructurePos(debug1.getOffset());
/*  580 */       debug5.setStructureSize(debug1.getSize());
/*  581 */       debug5.setMirror(debug1.getMirror());
/*  582 */       debug5.setRotation(debug1.getRotation());
/*  583 */       debug5.setMetaData(debug1.getData());
/*  584 */       debug5.setIgnoreEntities(debug1.isIgnoreEntities());
/*  585 */       debug5.setShowAir(debug1.isShowAir());
/*  586 */       debug5.setShowBoundingBox(debug1.isShowBoundingBox());
/*  587 */       debug5.setIntegrity(debug1.getIntegrity());
/*  588 */       debug5.setSeed(debug1.getSeed());
/*      */       
/*  590 */       if (debug5.hasStructureName()) {
/*  591 */         String debug6 = debug5.getStructureName();
/*  592 */         if (debug1.getUpdateType() == StructureBlockEntity.UpdateType.SAVE_AREA) {
/*  593 */           if (debug5.saveStructure()) {
/*  594 */             this.player.displayClientMessage((Component)new TranslatableComponent("structure_block.save_success", new Object[] { debug6 }), false);
/*      */           } else {
/*  596 */             this.player.displayClientMessage((Component)new TranslatableComponent("structure_block.save_failure", new Object[] { debug6 }), false);
/*      */           } 
/*  598 */         } else if (debug1.getUpdateType() == StructureBlockEntity.UpdateType.LOAD_AREA) {
/*  599 */           if (!debug5.isStructureLoadable()) {
/*  600 */             this.player.displayClientMessage((Component)new TranslatableComponent("structure_block.load_not_found", new Object[] { debug6 }), false);
/*  601 */           } else if (debug5.loadStructure(this.player.getLevel())) {
/*  602 */             this.player.displayClientMessage((Component)new TranslatableComponent("structure_block.load_success", new Object[] { debug6 }), false);
/*      */           } else {
/*  604 */             this.player.displayClientMessage((Component)new TranslatableComponent("structure_block.load_prepare", new Object[] { debug6 }), false);
/*      */           } 
/*  606 */         } else if (debug1.getUpdateType() == StructureBlockEntity.UpdateType.SCAN_AREA) {
/*  607 */           if (debug5.detectSize()) {
/*  608 */             this.player.displayClientMessage((Component)new TranslatableComponent("structure_block.size_success", new Object[] { debug6 }), false);
/*      */           } else {
/*  610 */             this.player.displayClientMessage((Component)new TranslatableComponent("structure_block.size_failure"), false);
/*      */           } 
/*      */         } 
/*      */       } else {
/*  614 */         this.player.displayClientMessage((Component)new TranslatableComponent("structure_block.invalid_structure_name", new Object[] { debug1.getName() }), false);
/*      */       } 
/*      */       
/*  617 */       debug5.setChanged();
/*  618 */       this.player.level.sendBlockUpdated(debug2, debug3, debug3, 3);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetJigsawBlock(ServerboundSetJigsawBlockPacket debug1) {
/*  624 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  625 */     if (!this.player.canUseGameMasterBlocks()) {
/*      */       return;
/*      */     }
/*  628 */     BlockPos debug2 = debug1.getPos();
/*  629 */     BlockState debug3 = this.player.level.getBlockState(debug2);
/*  630 */     BlockEntity debug4 = this.player.level.getBlockEntity(debug2);
/*  631 */     if (debug4 instanceof JigsawBlockEntity) {
/*  632 */       JigsawBlockEntity debug5 = (JigsawBlockEntity)debug4;
/*  633 */       debug5.setName(debug1.getName());
/*  634 */       debug5.setTarget(debug1.getTarget());
/*  635 */       debug5.setPool(debug1.getPool());
/*  636 */       debug5.setFinalState(debug1.getFinalState());
/*  637 */       debug5.setJoint(debug1.getJoint());
/*  638 */       debug5.setChanged();
/*  639 */       this.player.level.sendBlockUpdated(debug2, debug3, debug3, 3);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleJigsawGenerate(ServerboundJigsawGeneratePacket debug1) {
/*  645 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  646 */     if (!this.player.canUseGameMasterBlocks()) {
/*      */       return;
/*      */     }
/*  649 */     BlockPos debug2 = debug1.getPos();
/*  650 */     BlockEntity debug3 = this.player.level.getBlockEntity(debug2);
/*  651 */     if (debug3 instanceof JigsawBlockEntity) {
/*  652 */       JigsawBlockEntity debug4 = (JigsawBlockEntity)debug3;
/*  653 */       debug4.generate(this.player.getLevel(), debug1.levels(), debug1.keepJigsaws());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSelectTrade(ServerboundSelectTradePacket debug1) {
/*  659 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  660 */     int debug2 = debug1.getItem();
/*  661 */     AbstractContainerMenu debug3 = this.player.containerMenu;
/*  662 */     if (debug3 instanceof MerchantMenu) {
/*  663 */       MerchantMenu debug4 = (MerchantMenu)debug3;
/*  664 */       debug4.setSelectionHint(debug2);
/*  665 */       debug4.tryMoveItems(debug2);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEditBook(ServerboundEditBookPacket debug1) {
/*  671 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  672 */     ItemStack debug2 = debug1.getBook();
/*  673 */     if (debug2.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/*  677 */     if (!WritableBookItem.makeSureTagIsValid(debug2.getTag())) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  683 */     ItemStack debug3 = this.player.getItemInHand(debug1.getHand());
/*  684 */     if (debug2.getItem() == Items.WRITABLE_BOOK && debug3.getItem() == Items.WRITABLE_BOOK) {
/*  685 */       if (debug1.isSigning()) {
/*  686 */         ItemStack debug4 = new ItemStack((ItemLike)Items.WRITTEN_BOOK);
/*  687 */         CompoundTag debug5 = debug3.getTag();
/*  688 */         if (debug5 != null) {
/*  689 */           debug4.setTag(debug5.copy());
/*      */         }
/*  691 */         debug4.addTagElement("author", (Tag)StringTag.valueOf(this.player.getName().getString()));
/*  692 */         debug4.addTagElement("title", (Tag)StringTag.valueOf(debug2.getTag().getString("title")));
/*  693 */         ListTag debug6 = debug2.getTag().getList("pages", 8);
/*  694 */         for (int debug7 = 0; debug7 < debug6.size(); debug7++) {
/*  695 */           String debug8 = debug6.getString(debug7);
/*  696 */           TextComponent textComponent = new TextComponent(debug8);
/*  697 */           debug8 = Component.Serializer.toJson((Component)textComponent);
/*  698 */           debug6.set(debug7, (Tag)StringTag.valueOf(debug8));
/*      */         } 
/*  700 */         debug4.addTagElement("pages", (Tag)debug6);
/*  701 */         this.player.setItemInHand(debug1.getHand(), debug4);
/*      */       } else {
/*  703 */         debug3.addTagElement("pages", (Tag)debug2.getTag().getList("pages", 8));
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleEntityTagQuery(ServerboundEntityTagQuery debug1) {
/*  710 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*      */     
/*  712 */     if (!this.player.hasPermissions(2)) {
/*      */       return;
/*      */     }
/*      */     
/*  716 */     Entity debug2 = this.player.getLevel().getEntity(debug1.getEntityId());
/*  717 */     if (debug2 != null) {
/*  718 */       CompoundTag debug3 = debug2.saveWithoutId(new CompoundTag());
/*  719 */       this.player.connection.send((Packet<?>)new ClientboundTagQueryPacket(debug1.getTransactionId(), debug3));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleBlockEntityTagQuery(ServerboundBlockEntityTagQuery debug1) {
/*  725 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*      */     
/*  727 */     if (!this.player.hasPermissions(2)) {
/*      */       return;
/*      */     }
/*      */     
/*  731 */     BlockEntity debug2 = this.player.getLevel().getBlockEntity(debug1.getPos());
/*  732 */     CompoundTag debug3 = (debug2 != null) ? debug2.save(new CompoundTag()) : null;
/*  733 */     this.player.connection.send((Packet<?>)new ClientboundTagQueryPacket(debug1.getTransactionId(), debug3));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleMovePlayer(ServerboundMovePlayerPacket debug1) {
/*  738 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  739 */     if (containsInvalidValues(debug1)) {
/*  740 */       disconnect((Component)new TranslatableComponent("multiplayer.disconnect.invalid_player_movement"));
/*      */       return;
/*      */     } 
/*  743 */     ServerLevel debug2 = this.player.getLevel();
/*      */     
/*  745 */     if (this.player.wonGame) {
/*      */       return;
/*      */     }
/*      */     
/*  749 */     if (this.tickCount == 0) {
/*  750 */       resetPosition();
/*      */     }
/*      */     
/*  753 */     if (this.awaitingPositionFromClient != null) {
/*      */       
/*  755 */       if (this.tickCount - this.awaitingTeleportTime > 20) {
/*  756 */         this.awaitingTeleportTime = this.tickCount;
/*  757 */         teleport(this.awaitingPositionFromClient.x, this.awaitingPositionFromClient.y, this.awaitingPositionFromClient.z, this.player.yRot, this.player.xRot);
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/*  762 */     this.awaitingTeleportTime = this.tickCount;
/*  763 */     if (this.player.isPassenger()) {
/*  764 */       this.player.absMoveTo(this.player.getX(), this.player.getY(), this.player.getZ(), debug1.getYRot(this.player.yRot), debug1.getXRot(this.player.xRot));
/*  765 */       this.player.getLevel().getChunkSource().move(this.player);
/*      */       
/*      */       return;
/*      */     } 
/*  769 */     double debug3 = this.player.getX();
/*  770 */     double debug5 = this.player.getY();
/*  771 */     double debug7 = this.player.getZ();
/*      */     
/*  773 */     double debug9 = this.player.getY();
/*      */     
/*  775 */     double debug11 = debug1.getX(this.player.getX());
/*  776 */     double debug13 = debug1.getY(this.player.getY());
/*  777 */     double debug15 = debug1.getZ(this.player.getZ());
/*      */     
/*  779 */     float debug17 = debug1.getYRot(this.player.yRot);
/*  780 */     float debug18 = debug1.getXRot(this.player.xRot);
/*      */     
/*  782 */     double debug19 = debug11 - this.firstGoodX;
/*  783 */     double debug21 = debug13 - this.firstGoodY;
/*  784 */     double debug23 = debug15 - this.firstGoodZ;
/*      */     
/*  786 */     double debug25 = this.player.getDeltaMovement().lengthSqr();
/*  787 */     double debug27 = debug19 * debug19 + debug21 * debug21 + debug23 * debug23;
/*      */     
/*  789 */     if (this.player.isSleeping()) {
/*  790 */       if (debug27 > 1.0D) {
/*  791 */         teleport(this.player.getX(), this.player.getY(), this.player.getZ(), debug1.getYRot(this.player.yRot), debug1.getXRot(this.player.xRot));
/*      */       }
/*      */       
/*      */       return;
/*      */     } 
/*  796 */     this.receivedMovePacketCount++;
/*  797 */     int debug29 = this.receivedMovePacketCount - this.knownMovePacketCount;
/*  798 */     if (debug29 > 5) {
/*  799 */       LOGGER.debug("{} is sending move packets too frequently ({} packets since last tick)", this.player.getName().getString(), Integer.valueOf(debug29));
/*  800 */       debug29 = 1;
/*      */     } 
/*      */     
/*  803 */     if (!this.player.isChangingDimension() && (
/*  804 */       !this.player.getLevel().getGameRules().getBoolean(GameRules.RULE_DISABLE_ELYTRA_MOVEMENT_CHECK) || !this.player.isFallFlying())) {
/*      */ 
/*      */       
/*  807 */       float f = this.player.isFallFlying() ? 300.0F : 100.0F;
/*  808 */       if (debug27 - debug25 > (f * debug29) && !isSingleplayerOwner()) {
/*  809 */         LOGGER.warn("{} moved too quickly! {},{},{}", this.player.getName().getString(), Double.valueOf(debug19), Double.valueOf(debug21), Double.valueOf(debug23));
/*  810 */         teleport(this.player.getX(), this.player.getY(), this.player.getZ(), this.player.yRot, this.player.xRot);
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/*      */     
/*  816 */     AABB debug30 = this.player.getBoundingBox();
/*      */     
/*  818 */     debug19 = debug11 - this.lastGoodX;
/*  819 */     debug21 = debug13 - this.lastGoodY;
/*  820 */     debug23 = debug15 - this.lastGoodZ;
/*      */     
/*  822 */     boolean debug31 = (debug21 > 0.0D);
/*      */     
/*  824 */     if (this.player.isOnGround() && !debug1.isOnGround() && debug31)
/*      */     {
/*  826 */       this.player.jumpFromGround();
/*      */     }
/*      */     
/*  829 */     this.player.move(MoverType.PLAYER, new Vec3(debug19, debug21, debug23));
/*      */     
/*  831 */     double debug32 = debug21;
/*      */     
/*  833 */     debug19 = debug11 - this.player.getX();
/*  834 */     debug21 = debug13 - this.player.getY();
/*  835 */     if (debug21 > -0.5D || debug21 < 0.5D) {
/*  836 */       debug21 = 0.0D;
/*      */     }
/*  838 */     debug23 = debug15 - this.player.getZ();
/*  839 */     debug27 = debug19 * debug19 + debug21 * debug21 + debug23 * debug23;
/*  840 */     boolean debug34 = false;
/*  841 */     if (!this.player.isChangingDimension() && debug27 > 0.0625D && !this.player.isSleeping() && !this.player.gameMode.isCreative() && this.player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {
/*  842 */       debug34 = true;
/*  843 */       LOGGER.warn("{} moved wrongly!", this.player.getName().getString());
/*      */     } 
/*      */     
/*  846 */     this.player.absMoveTo(debug11, debug13, debug15, debug17, debug18);
/*      */     
/*  848 */     if (!this.player.noPhysics && !this.player.isSleeping() && ((
/*  849 */       debug34 && debug2.noCollision((Entity)this.player, debug30)) || isPlayerCollidingWithAnythingNew((LevelReader)debug2, debug30))) {
/*  850 */       teleport(debug3, debug5, debug7, debug17, debug18);
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  855 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  861 */       .clientIsFloating = (debug32 >= -0.03125D && this.player.gameMode.getGameModeForPlayer() != GameType.SPECTATOR && !this.server.isFlightAllowed() && !this.player.abilities.mayfly && !this.player.hasEffect(MobEffects.LEVITATION) && !this.player.isFallFlying() && noBlocksAround((Entity)this.player));
/*      */     
/*  863 */     this.player.getLevel().getChunkSource().move(this.player);
/*  864 */     this.player.doCheckFallDamage(this.player.getY() - debug9, debug1.isOnGround());
/*  865 */     this.player.setOnGround(debug1.isOnGround());
/*      */ 
/*      */     
/*  868 */     if (debug31) {
/*  869 */       this.player.fallDistance = 0.0F;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  874 */     this.player.checkMovementStatistics(this.player.getX() - debug3, this.player.getY() - debug5, this.player.getZ() - debug7);
/*      */     
/*  876 */     this.lastGoodX = this.player.getX();
/*  877 */     this.lastGoodY = this.player.getY();
/*  878 */     this.lastGoodZ = this.player.getZ();
/*      */   }
/*      */   
/*      */   private boolean isPlayerCollidingWithAnythingNew(LevelReader debug1, AABB debug2) {
/*  882 */     Stream<VoxelShape> debug3 = debug1.getCollisions((Entity)this.player, this.player.getBoundingBox().deflate(9.999999747378752E-6D), debug0 -> true);
/*  883 */     VoxelShape debug4 = Shapes.create(debug2.deflate(9.999999747378752E-6D));
/*  884 */     return debug3.anyMatch(debug1 -> !Shapes.joinIsNotEmpty(debug1, debug0, BooleanOp.AND));
/*      */   }
/*      */   
/*      */   public void teleport(double debug1, double debug3, double debug5, float debug7, float debug8) {
/*  888 */     teleport(debug1, debug3, debug5, debug7, debug8, Collections.emptySet());
/*      */   }
/*      */   
/*      */   public void teleport(double debug1, double debug3, double debug5, float debug7, float debug8, Set<ClientboundPlayerPositionPacket.RelativeArgument> debug9) {
/*  892 */     double debug10 = debug9.contains(ClientboundPlayerPositionPacket.RelativeArgument.X) ? this.player.getX() : 0.0D;
/*  893 */     double debug12 = debug9.contains(ClientboundPlayerPositionPacket.RelativeArgument.Y) ? this.player.getY() : 0.0D;
/*  894 */     double debug14 = debug9.contains(ClientboundPlayerPositionPacket.RelativeArgument.Z) ? this.player.getZ() : 0.0D;
/*  895 */     float debug16 = debug9.contains(ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT) ? this.player.yRot : 0.0F;
/*  896 */     float debug17 = debug9.contains(ClientboundPlayerPositionPacket.RelativeArgument.X_ROT) ? this.player.xRot : 0.0F;
/*  897 */     this.awaitingPositionFromClient = new Vec3(debug1, debug3, debug5);
/*      */     
/*  899 */     if (++this.awaitingTeleport == Integer.MAX_VALUE) {
/*  900 */       this.awaitingTeleport = 0;
/*      */     }
/*  902 */     this.awaitingTeleportTime = this.tickCount;
/*  903 */     this.player.absMoveTo(debug1, debug3, debug5, debug7, debug8);
/*  904 */     this.player.connection.send((Packet<?>)new ClientboundPlayerPositionPacket(debug1 - debug10, debug3 - debug12, debug5 - debug14, debug7 - debug16, debug8 - debug17, debug9, this.awaitingTeleport));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePlayerAction(ServerboundPlayerActionPacket debug1) {
/*  909 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*  910 */     BlockPos debug2 = debug1.getPos();
/*  911 */     this.player.resetLastActionTime();
/*      */     
/*  913 */     ServerboundPlayerActionPacket.Action debug3 = debug1.getAction();
/*      */     
/*  915 */     switch (debug3) {
/*      */       case PERFORM_RESPAWN:
/*  917 */         if (!this.player.isSpectator()) {
/*  918 */           ItemStack debug4 = this.player.getItemInHand(InteractionHand.OFF_HAND);
/*  919 */           this.player.setItemInHand(InteractionHand.OFF_HAND, this.player.getItemInHand(InteractionHand.MAIN_HAND));
/*  920 */           this.player.setItemInHand(InteractionHand.MAIN_HAND, debug4);
/*  921 */           this.player.stopUsingItem();
/*      */         } 
/*      */         return;
/*      */       case REQUEST_STATS:
/*  925 */         if (!this.player.isSpectator()) {
/*  926 */           this.player.drop(false);
/*      */         }
/*      */         return;
/*      */       case null:
/*  930 */         if (!this.player.isSpectator()) {
/*  931 */           this.player.drop(true);
/*      */         }
/*      */         return;
/*      */       case null:
/*  935 */         this.player.releaseUsingItem();
/*      */         return;
/*      */       case null:
/*      */       case null:
/*      */       case null:
/*  940 */         this.player.gameMode.handleBlockBreakAction(debug2, debug3, debug1.getDirection(), this.server.getMaxBuildHeight());
/*      */         return;
/*      */     } 
/*  943 */     throw new IllegalArgumentException("Invalid player action");
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean wasBlockPlacementAttempt(ServerPlayer debug0, ItemStack debug1) {
/*  948 */     if (debug1.isEmpty()) {
/*  949 */       return false;
/*      */     }
/*      */     
/*  952 */     Item debug2 = debug1.getItem();
/*  953 */     return ((debug2 instanceof net.minecraft.world.item.BlockItem || debug2 instanceof net.minecraft.world.item.BucketItem) && !debug0.getCooldowns().isOnCooldown(debug2));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleUseItemOn(ServerboundUseItemOnPacket debug1) {
/*  958 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*      */     
/*  960 */     ServerLevel debug2 = this.player.getLevel();
/*  961 */     InteractionHand debug3 = debug1.getHand();
/*  962 */     ItemStack debug4 = this.player.getItemInHand(debug3);
/*      */     
/*  964 */     BlockHitResult debug5 = debug1.getHitResult();
/*  965 */     BlockPos debug6 = debug5.getBlockPos();
/*  966 */     Direction debug7 = debug5.getDirection();
/*  967 */     this.player.resetLastActionTime();
/*      */     
/*  969 */     if (debug6.getY() < this.server.getMaxBuildHeight()) {
/*  970 */       if (this.awaitingPositionFromClient == null && this.player.distanceToSqr(debug6.getX() + 0.5D, debug6.getY() + 0.5D, debug6.getZ() + 0.5D) < 64.0D && 
/*  971 */         debug2.mayInteract((Player)this.player, debug6)) {
/*  972 */         InteractionResult debug8 = this.player.gameMode.useItemOn(this.player, (Level)debug2, debug4, debug3, debug5);
/*      */         
/*  974 */         if (debug7 == Direction.UP && !debug8.consumesAction() && debug6.getY() >= this.server.getMaxBuildHeight() - 1 && wasBlockPlacementAttempt(this.player, debug4)) {
/*      */           
/*  976 */           MutableComponent mutableComponent = (new TranslatableComponent("build.tooHigh", new Object[] { Integer.valueOf(this.server.getMaxBuildHeight()) })).withStyle(ChatFormatting.RED);
/*  977 */           this.player.connection.send((Packet<?>)new ClientboundChatPacket((Component)mutableComponent, ChatType.GAME_INFO, Util.NIL_UUID));
/*  978 */         } else if (debug8.shouldSwing()) {
/*  979 */           this.player.swing(debug3, true);
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  984 */       MutableComponent mutableComponent = (new TranslatableComponent("build.tooHigh", new Object[] { Integer.valueOf(this.server.getMaxBuildHeight()) })).withStyle(ChatFormatting.RED);
/*  985 */       this.player.connection.send((Packet<?>)new ClientboundChatPacket((Component)mutableComponent, ChatType.GAME_INFO, Util.NIL_UUID));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  991 */     this.player.connection.send((Packet<?>)new ClientboundBlockUpdatePacket((BlockGetter)debug2, debug6));
/*  992 */     this.player.connection.send((Packet<?>)new ClientboundBlockUpdatePacket((BlockGetter)debug2, debug6.relative(debug7)));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleUseItem(ServerboundUseItemPacket debug1) {
/*  997 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*      */     
/*  999 */     ServerLevel debug2 = this.player.getLevel();
/* 1000 */     InteractionHand debug3 = debug1.getHand();
/* 1001 */     ItemStack debug4 = this.player.getItemInHand(debug3);
/* 1002 */     this.player.resetLastActionTime();
/*      */     
/* 1004 */     if (debug4.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/* 1008 */     InteractionResult debug5 = this.player.gameMode.useItem(this.player, (Level)debug2, debug4, debug3);
/* 1009 */     if (debug5.shouldSwing()) {
/* 1010 */       this.player.swing(debug3, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleTeleportToEntityPacket(ServerboundTeleportToEntityPacket debug1) {
/* 1016 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1017 */     if (this.player.isSpectator()) {
/* 1018 */       for (ServerLevel debug3 : this.server.getAllLevels()) {
/* 1019 */         Entity debug4 = debug1.getEntity(debug3);
/*      */         
/* 1021 */         if (debug4 != null) {
/* 1022 */           this.player.teleportTo(debug3, debug4.getX(), debug4.getY(), debug4.getZ(), debug4.yRot, debug4.xRot);
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleResourcePackResponse(ServerboundResourcePackPacket debug1) {}
/*      */ 
/*      */   
/*      */   public void handlePaddleBoat(ServerboundPaddleBoatPacket debug1) {
/* 1035 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1036 */     Entity debug2 = this.player.getVehicle();
/* 1037 */     if (debug2 instanceof Boat) {
/* 1038 */       ((Boat)debug2).setPaddleState(debug1.getLeft(), debug1.getRight());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onDisconnect(Component debug1) {
/* 1044 */     LOGGER.info("{} lost connection: {}", this.player.getName().getString(), debug1.getString());
/* 1045 */     this.server.invalidateStatus();
/* 1046 */     this.server.getPlayerList().broadcastMessage((Component)(new TranslatableComponent("multiplayer.player.left", new Object[] { this.player.getDisplayName() })).withStyle(ChatFormatting.YELLOW), ChatType.SYSTEM, Util.NIL_UUID);
/* 1047 */     this.player.disconnect();
/* 1048 */     this.server.getPlayerList().remove(this.player);
/*      */     
/* 1050 */     if (isSingleplayerOwner()) {
/* 1051 */       LOGGER.info("Stopping singleplayer server as player logged out");
/* 1052 */       this.server.halt(false);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void send(Packet<?> debug1) {
/* 1057 */     send(debug1, null);
/*      */   }
/*      */   
/*      */   public void send(Packet<?> debug1, @Nullable GenericFutureListener<? extends Future<? super Void>> debug2) {
/* 1061 */     if (debug1 instanceof ClientboundChatPacket) {
/* 1062 */       ClientboundChatPacket debug3 = (ClientboundChatPacket)debug1;
/* 1063 */       ChatVisiblity debug4 = this.player.getChatVisibility();
/*      */       
/* 1065 */       if (debug4 == ChatVisiblity.HIDDEN && debug3.getType() != ChatType.GAME_INFO) {
/*      */         return;
/*      */       }
/* 1068 */       if (debug4 == ChatVisiblity.SYSTEM && !debug3.isSystem()) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */     
/*      */     try {
/* 1074 */       this.connection.send(debug1, debug2);
/* 1075 */     } catch (Throwable debug3) {
/* 1076 */       CrashReport debug4 = CrashReport.forThrowable(debug3, "Sending packet");
/* 1077 */       CrashReportCategory debug5 = debug4.addCategory("Packet being sent");
/*      */       
/* 1079 */       debug5.setDetail("Packet class", () -> debug0.getClass().getCanonicalName());
/*      */       
/* 1081 */       throw new ReportedException(debug4);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetCarriedItem(ServerboundSetCarriedItemPacket debug1) {
/* 1087 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1088 */     if (debug1.getSlot() < 0 || debug1.getSlot() >= Inventory.getSelectionSize()) {
/* 1089 */       LOGGER.warn("{} tried to set an invalid carried item", this.player.getName().getString());
/*      */       return;
/*      */     } 
/* 1092 */     if (this.player.inventory.selected != debug1.getSlot() && this.player.getUsedItemHand() == InteractionHand.MAIN_HAND) {
/* 1093 */       this.player.stopUsingItem();
/*      */     }
/* 1095 */     this.player.inventory.selected = debug1.getSlot();
/* 1096 */     this.player.resetLastActionTime();
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleChat(ServerboundChatPacket debug1) {
/* 1101 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1102 */     if (this.player.getChatVisibility() == ChatVisiblity.HIDDEN) {
/* 1103 */       send((Packet<?>)new ClientboundChatPacket((Component)(new TranslatableComponent("chat.cannotSend")).withStyle(ChatFormatting.RED), ChatType.SYSTEM, Util.NIL_UUID));
/*      */       return;
/*      */     } 
/* 1106 */     this.player.resetLastActionTime();
/*      */     
/* 1108 */     String debug2 = StringUtils.normalizeSpace(debug1.getMessage());
/* 1109 */     for (int debug3 = 0; debug3 < debug2.length(); debug3++) {
/* 1110 */       if (!SharedConstants.isAllowedChatCharacter(debug2.charAt(debug3))) {
/* 1111 */         disconnect((Component)new TranslatableComponent("multiplayer.disconnect.illegal_characters"));
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/* 1116 */     if (debug2.startsWith("/")) {
/* 1117 */       handleCommand(debug2);
/*      */     } else {
/* 1119 */       TranslatableComponent translatableComponent = new TranslatableComponent("chat.type.text", new Object[] { this.player.getDisplayName(), debug2 });
/* 1120 */       this.server.getPlayerList().broadcastMessage((Component)translatableComponent, ChatType.CHAT, this.player.getUUID());
/*      */     } 
/*      */     
/* 1123 */     this.chatSpamTickCount += 20;
/* 1124 */     if (this.chatSpamTickCount > 200 && !this.server.getPlayerList().isOp(this.player.getGameProfile())) {
/* 1125 */       disconnect((Component)new TranslatableComponent("disconnect.spam"));
/*      */     }
/*      */   }
/*      */   
/*      */   private void handleCommand(String debug1) {
/* 1130 */     this.server.getCommands().performCommand(this.player.createCommandSourceStack(), debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleAnimate(ServerboundSwingPacket debug1) {
/* 1135 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1136 */     this.player.resetLastActionTime();
/* 1137 */     this.player.swing(debug1.getHand());
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePlayerCommand(ServerboundPlayerCommandPacket debug1) {
/* 1142 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1143 */     this.player.resetLastActionTime();
/* 1144 */     switch (debug1.getAction()) {
/*      */       case PERFORM_RESPAWN:
/* 1146 */         this.player.setShiftKeyDown(true);
/*      */         return;
/*      */       case REQUEST_STATS:
/* 1149 */         this.player.setShiftKeyDown(false);
/*      */         return;
/*      */       case null:
/* 1152 */         this.player.setSprinting(true);
/*      */         return;
/*      */       case null:
/* 1155 */         this.player.setSprinting(false);
/*      */         return;
/*      */       case null:
/* 1158 */         if (this.player.isSleeping()) {
/* 1159 */           this.player.stopSleepInBed(false, true);
/* 1160 */           this.awaitingPositionFromClient = this.player.position();
/*      */         } 
/*      */         return;
/*      */       case null:
/* 1164 */         if (this.player.getVehicle() instanceof PlayerRideableJumping) {
/* 1165 */           PlayerRideableJumping debug2 = (PlayerRideableJumping)this.player.getVehicle();
/* 1166 */           int debug3 = debug1.getData();
/* 1167 */           if (debug2.canJump() && debug3 > 0) {
/* 1168 */             debug2.handleStartJump(debug3);
/*      */           }
/*      */         } 
/*      */         return;
/*      */       case null:
/* 1173 */         if (this.player.getVehicle() instanceof PlayerRideableJumping) {
/* 1174 */           PlayerRideableJumping debug2 = (PlayerRideableJumping)this.player.getVehicle();
/* 1175 */           debug2.handleStopJump();
/*      */         } 
/*      */         return;
/*      */       
/*      */       case null:
/* 1180 */         if (this.player.getVehicle() instanceof AbstractHorse) {
/* 1181 */           ((AbstractHorse)this.player.getVehicle()).openInventory((Player)this.player);
/*      */         }
/*      */         return;
/*      */       case null:
/* 1185 */         if (!this.player.tryToStartFallFlying())
/*      */         {
/* 1187 */           this.player.stopFallFlying();
/*      */         }
/*      */         return;
/*      */     } 
/* 1191 */     throw new IllegalArgumentException("Invalid client command!");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleInteract(ServerboundInteractPacket debug1) {
/* 1207 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1208 */     ServerLevel debug2 = this.player.getLevel();
/* 1209 */     Entity debug3 = debug1.getTarget((Level)debug2);
/* 1210 */     this.player.resetLastActionTime();
/*      */     
/* 1212 */     this.player.setShiftKeyDown(debug1.isUsingSecondaryAction());
/*      */     
/* 1214 */     if (debug3 != null) {
/* 1215 */       double debug4 = 36.0D;
/*      */       
/* 1217 */       if (this.player.distanceToSqr(debug3) < 36.0D) {
/*      */         
/* 1219 */         InteractionHand debug6 = debug1.getHand();
/* 1220 */         ItemStack debug7 = (debug6 != null) ? this.player.getItemInHand(debug6).copy() : ItemStack.EMPTY;
/* 1221 */         Optional<InteractionResult> debug8 = Optional.empty();
/* 1222 */         if (debug1.getAction() == ServerboundInteractPacket.Action.INTERACT) {
/* 1223 */           debug8 = Optional.of(this.player.interactOn(debug3, debug6));
/* 1224 */         } else if (debug1.getAction() == ServerboundInteractPacket.Action.INTERACT_AT) {
/* 1225 */           debug8 = Optional.of(debug3.interactAt((Player)this.player, debug1.getLocation(), debug6));
/* 1226 */         } else if (debug1.getAction() == ServerboundInteractPacket.Action.ATTACK) {
/* 1227 */           if (debug3 instanceof net.minecraft.world.entity.item.ItemEntity || debug3 instanceof net.minecraft.world.entity.ExperienceOrb || debug3 instanceof net.minecraft.world.entity.projectile.AbstractArrow || debug3 == this.player) {
/* 1228 */             disconnect((Component)new TranslatableComponent("multiplayer.disconnect.invalid_entity_attacked"));
/* 1229 */             LOGGER.warn("Player {} tried to attack an invalid entity", this.player.getName().getString());
/*      */             
/*      */             return;
/*      */           } 
/* 1233 */           this.player.attack(debug3);
/*      */         } 
/*      */         
/* 1236 */         if (debug8.isPresent() && ((InteractionResult)debug8.get()).consumesAction()) {
/* 1237 */           CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY.trigger(this.player, debug7, debug3);
/* 1238 */           if (((InteractionResult)debug8.get()).shouldSwing()) {
/* 1239 */             this.player.swing(debug6, true);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleClientCommand(ServerboundClientCommandPacket debug1) {
/* 1248 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1249 */     this.player.resetLastActionTime();
/* 1250 */     ServerboundClientCommandPacket.Action debug2 = debug1.getAction();
/* 1251 */     switch (debug2) {
/*      */       case PERFORM_RESPAWN:
/* 1253 */         if (this.player.wonGame) {
/* 1254 */           this.player.wonGame = false;
/* 1255 */           this.player = this.server.getPlayerList().respawn(this.player, true);
/* 1256 */           CriteriaTriggers.CHANGED_DIMENSION.trigger(this.player, Level.END, Level.OVERWORLD); break;
/*      */         } 
/* 1258 */         if (this.player.getHealth() > 0.0F) {
/*      */           return;
/*      */         }
/* 1261 */         this.player = this.server.getPlayerList().respawn(this.player, false);
/* 1262 */         if (this.server.isHardcore()) {
/* 1263 */           this.player.setGameMode(GameType.SPECTATOR);
/* 1264 */           ((GameRules.BooleanValue)this.player.getLevel().getGameRules().getRule(GameRules.RULE_SPECTATORSGENERATECHUNKS)).set(false, this.server);
/*      */         } 
/*      */         break;
/*      */       
/*      */       case REQUEST_STATS:
/* 1269 */         this.player.getStats().sendStats(this.player);
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleContainerClose(ServerboundContainerClosePacket debug1) {
/* 1276 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1277 */     this.player.doCloseContainer();
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleContainerClick(ServerboundContainerClickPacket debug1) {
/* 1282 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1283 */     this.player.resetLastActionTime();
/* 1284 */     if (this.player.containerMenu.containerId == debug1.getContainerId() && this.player.containerMenu.isSynched((Player)this.player)) {
/* 1285 */       if (this.player.isSpectator()) {
/* 1286 */         NonNullList<ItemStack> debug2 = NonNullList.create();
/* 1287 */         for (int debug3 = 0; debug3 < this.player.containerMenu.slots.size(); debug3++) {
/* 1288 */           debug2.add(((Slot)this.player.containerMenu.slots.get(debug3)).getItem());
/*      */         }
/* 1290 */         this.player.refreshContainer(this.player.containerMenu, debug2);
/*      */       } else {
/* 1292 */         ItemStack debug2 = this.player.containerMenu.clicked(debug1.getSlotNum(), debug1.getButtonNum(), debug1.getClickType(), (Player)this.player);
/*      */         
/* 1294 */         if (ItemStack.matches(debug1.getItem(), debug2)) {
/*      */           
/* 1296 */           this.player.connection.send((Packet<?>)new ClientboundContainerAckPacket(debug1.getContainerId(), debug1.getUid(), true));
/* 1297 */           this.player.ignoreSlotUpdateHack = true;
/* 1298 */           this.player.containerMenu.broadcastChanges();
/* 1299 */           this.player.broadcastCarriedItem();
/* 1300 */           this.player.ignoreSlotUpdateHack = false;
/*      */         } else {
/*      */           
/* 1303 */           this.expectedAcks.put(this.player.containerMenu.containerId, debug1.getUid());
/* 1304 */           this.player.connection.send((Packet<?>)new ClientboundContainerAckPacket(debug1.getContainerId(), debug1.getUid(), false));
/* 1305 */           this.player.containerMenu.setSynched((Player)this.player, false);
/*      */           
/* 1307 */           NonNullList<ItemStack> debug3 = NonNullList.create();
/* 1308 */           for (int debug4 = 0; debug4 < this.player.containerMenu.slots.size(); debug4++) {
/* 1309 */             ItemStack debug5 = ((Slot)this.player.containerMenu.slots.get(debug4)).getItem();
/* 1310 */             debug3.add(debug5.isEmpty() ? ItemStack.EMPTY : debug5);
/*      */           } 
/* 1312 */           this.player.refreshContainer(this.player.containerMenu, debug3);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handlePlaceRecipe(ServerboundPlaceRecipePacket debug1) {
/* 1320 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1321 */     this.player.resetLastActionTime();
/*      */     
/* 1323 */     if (this.player.isSpectator() || this.player.containerMenu.containerId != debug1.getContainerId() || !this.player.containerMenu.isSynched((Player)this.player) || !(this.player.containerMenu instanceof RecipeBookMenu)) {
/*      */       return;
/*      */     }
/*      */     
/* 1327 */     this.server.getRecipeManager().byKey(debug1.getRecipe()).ifPresent(debug2 -> ((RecipeBookMenu)this.player.containerMenu).handlePlacement(debug1.isShiftDown(), debug2, this.player));
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleContainerButtonClick(ServerboundContainerButtonClickPacket debug1) {
/* 1332 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1333 */     this.player.resetLastActionTime();
/* 1334 */     if (this.player.containerMenu.containerId == debug1.getContainerId() && this.player.containerMenu.isSynched((Player)this.player) && !this.player.isSpectator()) {
/* 1335 */       this.player.containerMenu.clickMenuButton((Player)this.player, debug1.getButtonId());
/* 1336 */       this.player.containerMenu.broadcastChanges();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket debug1) {
/* 1342 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1343 */     if (this.player.gameMode.isCreative()) {
/* 1344 */       boolean debug2 = (debug1.getSlotNum() < 0);
/* 1345 */       ItemStack debug3 = debug1.getItem();
/*      */       
/* 1347 */       CompoundTag debug4 = debug3.getTagElement("BlockEntityTag");
/* 1348 */       if (!debug3.isEmpty() && debug4 != null && 
/* 1349 */         debug4.contains("x") && debug4.contains("y") && debug4.contains("z")) {
/* 1350 */         BlockPos blockPos = new BlockPos(debug4.getInt("x"), debug4.getInt("y"), debug4.getInt("z"));
/* 1351 */         BlockEntity blockEntity = this.player.level.getBlockEntity(blockPos);
/* 1352 */         if (blockEntity != null) {
/* 1353 */           CompoundTag debug7 = blockEntity.save(new CompoundTag());
/* 1354 */           debug7.remove("x");
/* 1355 */           debug7.remove("y");
/* 1356 */           debug7.remove("z");
/* 1357 */           debug3.addTagElement("BlockEntityTag", (Tag)debug7);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1362 */       boolean debug5 = (debug1.getSlotNum() >= 1 && debug1.getSlotNum() <= 45);
/* 1363 */       boolean debug6 = (debug3.isEmpty() || (debug3.getDamageValue() >= 0 && debug3.getCount() <= 64 && !debug3.isEmpty()));
/*      */       
/* 1365 */       if (debug5 && debug6) {
/* 1366 */         if (debug3.isEmpty()) {
/* 1367 */           this.player.inventoryMenu.setItem(debug1.getSlotNum(), ItemStack.EMPTY);
/*      */         } else {
/* 1369 */           this.player.inventoryMenu.setItem(debug1.getSlotNum(), debug3);
/*      */         } 
/* 1371 */         this.player.inventoryMenu.setSynched((Player)this.player, true);
/* 1372 */         this.player.inventoryMenu.broadcastChanges();
/* 1373 */       } else if (debug2 && debug6 && 
/* 1374 */         this.dropSpamTickCount < 200) {
/* 1375 */         this.dropSpamTickCount += 20;
/*      */         
/* 1377 */         this.player.drop(debug3, true);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleContainerAck(ServerboundContainerAckPacket debug1) {
/* 1385 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1386 */     int debug2 = this.player.containerMenu.containerId;
/* 1387 */     if (debug2 == debug1.getContainerId() && this.expectedAcks.getOrDefault(debug2, (short)(debug1.getUid() + 1)) == debug1.getUid() && !this.player.containerMenu.isSynched((Player)this.player) && !this.player.isSpectator()) {
/* 1388 */       this.player.containerMenu.setSynched((Player)this.player, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleSignUpdate(ServerboundSignUpdatePacket debug1) {
/* 1394 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1395 */     this.player.resetLastActionTime();
/* 1396 */     ServerLevel debug2 = this.player.getLevel();
/* 1397 */     BlockPos debug3 = debug1.getPos();
/* 1398 */     if (debug2.hasChunkAt(debug3)) {
/* 1399 */       BlockState debug4 = debug2.getBlockState(debug3);
/* 1400 */       BlockEntity debug5 = debug2.getBlockEntity(debug3);
/*      */       
/* 1402 */       if (!(debug5 instanceof SignBlockEntity)) {
/*      */         return;
/*      */       }
/*      */       
/* 1406 */       SignBlockEntity debug6 = (SignBlockEntity)debug5;
/* 1407 */       if (!debug6.isEditable() || debug6.getPlayerWhoMayEdit() != this.player) {
/* 1408 */         LOGGER.warn("Player {} just tried to change non-editable sign", this.player.getName().getString());
/*      */         
/*      */         return;
/*      */       } 
/* 1412 */       String[] debug7 = debug1.getLines();
/* 1413 */       for (int debug8 = 0; debug8 < debug7.length; debug8++) {
/* 1414 */         debug6.setMessage(debug8, (Component)new TextComponent(ChatFormatting.stripFormatting(debug7[debug8])));
/*      */       }
/*      */       
/* 1417 */       debug6.setChanged();
/* 1418 */       debug2.sendBlockUpdated(debug3, debug4, debug4, 3);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleKeepAlive(ServerboundKeepAlivePacket debug1) {
/* 1424 */     if (this.keepAlivePending && debug1.getId() == this.keepAliveChallenge) {
/* 1425 */       int debug2 = (int)(Util.getMillis() - this.keepAliveTime);
/* 1426 */       this.player.latency = (this.player.latency * 3 + debug2) / 4;
/* 1427 */       this.keepAlivePending = false;
/*      */     }
/* 1429 */     else if (!isSingleplayerOwner()) {
/* 1430 */       disconnect((Component)new TranslatableComponent("disconnect.timeout"));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket debug1) {
/* 1437 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1438 */     this.player.abilities.flying = (debug1.isFlying() && this.player.abilities.mayfly);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleClientInformation(ServerboundClientInformationPacket debug1) {
/* 1443 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/* 1444 */     this.player.updateOptions(debug1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleCustomPayload(ServerboundCustomPayloadPacket debug1) {}
/*      */ 
/*      */   
/*      */   public void handleChangeDifficulty(ServerboundChangeDifficultyPacket debug1) {
/* 1453 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*      */     
/* 1455 */     if (!this.player.hasPermissions(2) && !isSingleplayerOwner()) {
/*      */       return;
/*      */     }
/*      */     
/* 1459 */     this.server.setDifficulty(debug1.getDifficulty(), false);
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleLockDifficulty(ServerboundLockDifficultyPacket debug1) {
/* 1464 */     PacketUtils.ensureRunningOnSameThread((Packet)debug1, (PacketListener)this, this.player.getLevel());
/*      */     
/* 1466 */     if (!this.player.hasPermissions(2) && !isSingleplayerOwner()) {
/*      */       return;
/*      */     }
/*      */     
/* 1470 */     this.server.setDifficultyLocked(debug1.isLocked());
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\network\ServerGamePacketListenerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */