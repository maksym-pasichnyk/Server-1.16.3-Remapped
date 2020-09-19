/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*     */ import net.minecraft.world.level.block.state.properties.StructureMode;
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
/*     */ public class ServerboundSetStructureBlockPacket
/*     */   implements Packet<ServerGamePacketListener>
/*     */ {
/*     */   private BlockPos pos;
/*     */   private StructureBlockEntity.UpdateType updateType;
/*     */   private StructureMode mode;
/*     */   private String name;
/*     */   private BlockPos offset;
/*     */   private BlockPos size;
/*     */   private Mirror mirror;
/*     */   private Rotation rotation;
/*     */   private String data;
/*     */   private boolean ignoreEntities;
/*     */   private boolean showAir;
/*     */   private boolean showBoundingBox;
/*     */   private float integrity;
/*     */   private long seed;
/*     */   
/*     */   public void read(FriendlyByteBuf debug1) throws IOException {
/*  56 */     this.pos = debug1.readBlockPos();
/*  57 */     this.updateType = (StructureBlockEntity.UpdateType)debug1.readEnum(StructureBlockEntity.UpdateType.class);
/*  58 */     this.mode = (StructureMode)debug1.readEnum(StructureMode.class);
/*  59 */     this.name = debug1.readUtf(32767);
/*  60 */     int debug2 = 48;
/*  61 */     this.offset = new BlockPos(Mth.clamp(debug1.readByte(), -48, 48), Mth.clamp(debug1.readByte(), -48, 48), Mth.clamp(debug1.readByte(), -48, 48));
/*  62 */     int debug3 = 48;
/*  63 */     this.size = new BlockPos(Mth.clamp(debug1.readByte(), 0, 48), Mth.clamp(debug1.readByte(), 0, 48), Mth.clamp(debug1.readByte(), 0, 48));
/*  64 */     this.mirror = (Mirror)debug1.readEnum(Mirror.class);
/*  65 */     this.rotation = (Rotation)debug1.readEnum(Rotation.class);
/*  66 */     this.data = debug1.readUtf(12);
/*  67 */     this.integrity = Mth.clamp(debug1.readFloat(), 0.0F, 1.0F);
/*  68 */     this.seed = debug1.readVarLong();
/*  69 */     int debug4 = debug1.readByte();
/*  70 */     this.ignoreEntities = ((debug4 & 0x1) != 0);
/*  71 */     this.showAir = ((debug4 & 0x2) != 0);
/*  72 */     this.showBoundingBox = ((debug4 & 0x4) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf debug1) throws IOException {
/*  77 */     debug1.writeBlockPos(this.pos);
/*  78 */     debug1.writeEnum((Enum)this.updateType);
/*  79 */     debug1.writeEnum((Enum)this.mode);
/*  80 */     debug1.writeUtf(this.name);
/*  81 */     debug1.writeByte(this.offset.getX());
/*  82 */     debug1.writeByte(this.offset.getY());
/*  83 */     debug1.writeByte(this.offset.getZ());
/*  84 */     debug1.writeByte(this.size.getX());
/*  85 */     debug1.writeByte(this.size.getY());
/*  86 */     debug1.writeByte(this.size.getZ());
/*  87 */     debug1.writeEnum((Enum)this.mirror);
/*  88 */     debug1.writeEnum((Enum)this.rotation);
/*  89 */     debug1.writeUtf(this.data);
/*  90 */     debug1.writeFloat(this.integrity);
/*  91 */     debug1.writeVarLong(this.seed);
/*     */     
/*  93 */     int debug2 = 0;
/*  94 */     if (this.ignoreEntities) {
/*  95 */       debug2 |= 0x1;
/*     */     }
/*  97 */     if (this.showAir) {
/*  98 */       debug2 |= 0x2;
/*     */     }
/* 100 */     if (this.showBoundingBox) {
/* 101 */       debug2 |= 0x4;
/*     */     }
/* 103 */     debug1.writeByte(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ServerGamePacketListener debug1) {
/* 108 */     debug1.handleSetStructureBlock(this);
/*     */   }
/*     */   
/*     */   public BlockPos getPos() {
/* 112 */     return this.pos;
/*     */   }
/*     */   
/*     */   public StructureBlockEntity.UpdateType getUpdateType() {
/* 116 */     return this.updateType;
/*     */   }
/*     */   
/*     */   public StructureMode getMode() {
/* 120 */     return this.mode;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 124 */     return this.name;
/*     */   }
/*     */   
/*     */   public BlockPos getOffset() {
/* 128 */     return this.offset;
/*     */   }
/*     */   
/*     */   public BlockPos getSize() {
/* 132 */     return this.size;
/*     */   }
/*     */   
/*     */   public Mirror getMirror() {
/* 136 */     return this.mirror;
/*     */   }
/*     */   
/*     */   public Rotation getRotation() {
/* 140 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public String getData() {
/* 144 */     return this.data;
/*     */   }
/*     */   
/*     */   public boolean isIgnoreEntities() {
/* 148 */     return this.ignoreEntities;
/*     */   }
/*     */   
/*     */   public boolean isShowAir() {
/* 152 */     return this.showAir;
/*     */   }
/*     */   
/*     */   public boolean isShowBoundingBox() {
/* 156 */     return this.showBoundingBox;
/*     */   }
/*     */   
/*     */   public float getIntegrity() {
/* 160 */     return this.integrity;
/*     */   }
/*     */   
/*     */   public long getSeed() {
/* 164 */     return this.seed;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSetStructureBlockPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */