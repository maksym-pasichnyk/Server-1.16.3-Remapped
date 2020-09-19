/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.minecraft.MinecraftSessionService;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.players.GameProfileCache;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SkullBlockEntity
/*     */   extends BlockEntity
/*     */   implements TickableBlockEntity
/*     */ {
/*     */   @Nullable
/*     */   private static GameProfileCache profileCache;
/*     */   @Nullable
/*     */   private static MinecraftSessionService sessionService;
/*     */   @Nullable
/*     */   private GameProfile owner;
/*     */   private int mouthTickCount;
/*     */   private boolean isMovingMouth;
/*     */   
/*     */   public SkullBlockEntity() {
/*  33 */     super(BlockEntityType.SKULL);
/*     */   }
/*     */   
/*     */   public static void setProfileCache(GameProfileCache debug0) {
/*  37 */     profileCache = debug0;
/*     */   }
/*     */   
/*     */   public static void setSessionService(MinecraftSessionService debug0) {
/*  41 */     sessionService = debug0;
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag save(CompoundTag debug1) {
/*  46 */     super.save(debug1);
/*     */     
/*  48 */     if (this.owner != null) {
/*  49 */       CompoundTag debug2 = new CompoundTag();
/*  50 */       NbtUtils.writeGameProfile(debug2, this.owner);
/*  51 */       debug1.put("SkullOwner", (Tag)debug2);
/*     */     } 
/*     */     
/*  54 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(BlockState debug1, CompoundTag debug2) {
/*  59 */     super.load(debug1, debug2);
/*     */     
/*  61 */     if (debug2.contains("SkullOwner", 10)) {
/*  62 */       setOwner(NbtUtils.readGameProfile(debug2.getCompound("SkullOwner")));
/*  63 */     } else if (debug2.contains("ExtraType", 8)) {
/*  64 */       String debug3 = debug2.getString("ExtraType");
/*  65 */       if (!StringUtil.isNullOrEmpty(debug3)) {
/*  66 */         setOwner(new GameProfile(null, debug3));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  73 */     BlockState debug1 = getBlockState();
/*  74 */     if (debug1.is(Blocks.DRAGON_HEAD) || debug1.is(Blocks.DRAGON_WALL_HEAD)) {
/*  75 */       if (this.level.hasNeighborSignal(this.worldPosition)) {
/*  76 */         this.isMovingMouth = true;
/*  77 */         this.mouthTickCount++;
/*     */       } else {
/*  79 */         this.isMovingMouth = false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/*  99 */     return new ClientboundBlockEntityDataPacket(this.worldPosition, 4, getUpdateTag());
/*     */   }
/*     */ 
/*     */   
/*     */   public CompoundTag getUpdateTag() {
/* 104 */     return save(new CompoundTag());
/*     */   }
/*     */   
/*     */   public void setOwner(@Nullable GameProfile debug1) {
/* 108 */     this.owner = debug1;
/* 109 */     updateOwnerProfile();
/*     */   }
/*     */   
/*     */   private void updateOwnerProfile() {
/* 113 */     this.owner = updateGameprofile(this.owner);
/* 114 */     setChanged();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static GameProfile updateGameprofile(@Nullable GameProfile debug0) {
/* 119 */     if (debug0 == null || StringUtil.isNullOrEmpty(debug0.getName()))
/* 120 */       return debug0; 
/* 121 */     if (debug0.isComplete() && debug0.getProperties().containsKey("textures")) {
/* 122 */       return debug0;
/*     */     }
/*     */     
/* 125 */     if (profileCache == null || sessionService == null) {
/* 126 */       return debug0;
/*     */     }
/*     */ 
/*     */     
/* 130 */     GameProfile debug1 = profileCache.get(debug0.getName());
/* 131 */     if (debug1 == null) {
/* 132 */       return debug0;
/*     */     }
/*     */ 
/*     */     
/* 136 */     Property debug2 = (Property)Iterables.getFirst(debug1.getProperties().get("textures"), null);
/* 137 */     if (debug2 == null) {
/* 138 */       debug1 = sessionService.fillProfileProperties(debug1, true);
/*     */     }
/* 140 */     return debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\entity\SkullBlockEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */