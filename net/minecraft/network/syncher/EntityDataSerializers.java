/*     */ package net.minecraft.network.syncher;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.Rotations;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleType;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.npc.VillagerData;
/*     */ import net.minecraft.world.entity.npc.VillagerProfession;
/*     */ import net.minecraft.world.entity.npc.VillagerType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class EntityDataSerializers {
/*  25 */   private static final CrudeIncrementalIntIdentityHashBiMap<EntityDataSerializer<?>> SERIALIZERS = new CrudeIncrementalIntIdentityHashBiMap(16);
/*     */   
/*  27 */   public static final EntityDataSerializer<Byte> BYTE = new EntityDataSerializer<Byte>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, Byte debug2) {
/*  30 */         debug1.writeByte(debug2.byteValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public Byte read(FriendlyByteBuf debug1) {
/*  35 */         return Byte.valueOf(debug1.readByte());
/*     */       }
/*     */ 
/*     */       
/*     */       public Byte copy(Byte debug1) {
/*  40 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/*  44 */   public static final EntityDataSerializer<Integer> INT = new EntityDataSerializer<Integer>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, Integer debug2) {
/*  47 */         debug1.writeVarInt(debug2.intValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public Integer read(FriendlyByteBuf debug1) {
/*  52 */         return Integer.valueOf(debug1.readVarInt());
/*     */       }
/*     */ 
/*     */       
/*     */       public Integer copy(Integer debug1) {
/*  57 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/*  61 */   public static final EntityDataSerializer<Float> FLOAT = new EntityDataSerializer<Float>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, Float debug2) {
/*  64 */         debug1.writeFloat(debug2.floatValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public Float read(FriendlyByteBuf debug1) {
/*  69 */         return Float.valueOf(debug1.readFloat());
/*     */       }
/*     */ 
/*     */       
/*     */       public Float copy(Float debug1) {
/*  74 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/*  78 */   public static final EntityDataSerializer<String> STRING = new EntityDataSerializer<String>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, String debug2) {
/*  81 */         debug1.writeUtf(debug2);
/*     */       }
/*     */ 
/*     */       
/*     */       public String read(FriendlyByteBuf debug1) {
/*  86 */         return debug1.readUtf(32767);
/*     */       }
/*     */ 
/*     */       
/*     */       public String copy(String debug1) {
/*  91 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/*  95 */   public static final EntityDataSerializer<Component> COMPONENT = new EntityDataSerializer<Component>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, Component debug2) {
/*  98 */         debug1.writeComponent(debug2);
/*     */       }
/*     */ 
/*     */       
/*     */       public Component read(FriendlyByteBuf debug1) {
/* 103 */         return debug1.readComponent();
/*     */       }
/*     */ 
/*     */       
/*     */       public Component copy(Component debug1) {
/* 108 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/* 112 */   public static final EntityDataSerializer<Optional<Component>> OPTIONAL_COMPONENT = new EntityDataSerializer<Optional<Component>>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, Optional<Component> debug2) {
/* 115 */         if (debug2.isPresent()) {
/* 116 */           debug1.writeBoolean(true);
/* 117 */           debug1.writeComponent(debug2.get());
/*     */         } else {
/* 119 */           debug1.writeBoolean(false);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<Component> read(FriendlyByteBuf debug1) {
/* 125 */         return debug1.readBoolean() ? Optional.<Component>of(debug1.readComponent()) : Optional.<Component>empty();
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<Component> copy(Optional<Component> debug1) {
/* 130 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/* 134 */   public static final EntityDataSerializer<ItemStack> ITEM_STACK = new EntityDataSerializer<ItemStack>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, ItemStack debug2) {
/* 137 */         debug1.writeItem(debug2);
/*     */       }
/*     */ 
/*     */       
/*     */       public ItemStack read(FriendlyByteBuf debug1) {
/* 142 */         return debug1.readItem();
/*     */       }
/*     */ 
/*     */       
/*     */       public ItemStack copy(ItemStack debug1) {
/* 147 */         return debug1.copy();
/*     */       }
/*     */     };
/*     */   
/* 151 */   public static final EntityDataSerializer<Optional<BlockState>> BLOCK_STATE = new EntityDataSerializer<Optional<BlockState>>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, Optional<BlockState> debug2) {
/* 154 */         if (debug2.isPresent()) {
/* 155 */           debug1.writeVarInt(Block.getId(debug2.get()));
/*     */         } else {
/* 157 */           debug1.writeVarInt(0);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<BlockState> read(FriendlyByteBuf debug1) {
/* 163 */         int debug2 = debug1.readVarInt();
/* 164 */         if (debug2 == 0) {
/* 165 */           return Optional.empty();
/*     */         }
/* 167 */         return Optional.of(Block.stateById(debug2));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public Optional<BlockState> copy(Optional<BlockState> debug1) {
/* 173 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/* 177 */   public static final EntityDataSerializer<Boolean> BOOLEAN = new EntityDataSerializer<Boolean>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, Boolean debug2) {
/* 180 */         debug1.writeBoolean(debug2.booleanValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public Boolean read(FriendlyByteBuf debug1) {
/* 185 */         return Boolean.valueOf(debug1.readBoolean());
/*     */       }
/*     */ 
/*     */       
/*     */       public Boolean copy(Boolean debug1) {
/* 190 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/* 194 */   public static final EntityDataSerializer<ParticleOptions> PARTICLE = new EntityDataSerializer<ParticleOptions>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, ParticleOptions debug2) {
/* 197 */         debug1.writeVarInt(Registry.PARTICLE_TYPE.getId(debug2.getType()));
/* 198 */         debug2.writeToNetwork(debug1);
/*     */       }
/*     */ 
/*     */       
/*     */       public ParticleOptions read(FriendlyByteBuf debug1) {
/* 203 */         return readParticle(debug1, (ParticleType<ParticleOptions>)Registry.PARTICLE_TYPE.byId(debug1.readVarInt()));
/*     */       }
/*     */       
/*     */       private <T extends ParticleOptions> T readParticle(FriendlyByteBuf debug1, ParticleType<T> debug2) {
/* 207 */         return (T)debug2.getDeserializer().fromNetwork(debug2, debug1);
/*     */       }
/*     */ 
/*     */       
/*     */       public ParticleOptions copy(ParticleOptions debug1) {
/* 212 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/* 216 */   public static final EntityDataSerializer<Rotations> ROTATIONS = new EntityDataSerializer<Rotations>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, Rotations debug2) {
/* 219 */         debug1.writeFloat(debug2.getX());
/* 220 */         debug1.writeFloat(debug2.getY());
/* 221 */         debug1.writeFloat(debug2.getZ());
/*     */       }
/*     */ 
/*     */       
/*     */       public Rotations read(FriendlyByteBuf debug1) {
/* 226 */         return new Rotations(debug1.readFloat(), debug1.readFloat(), debug1.readFloat());
/*     */       }
/*     */ 
/*     */       
/*     */       public Rotations copy(Rotations debug1) {
/* 231 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/* 235 */   public static final EntityDataSerializer<BlockPos> BLOCK_POS = new EntityDataSerializer<BlockPos>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, BlockPos debug2) {
/* 238 */         debug1.writeBlockPos(debug2);
/*     */       }
/*     */ 
/*     */       
/*     */       public BlockPos read(FriendlyByteBuf debug1) {
/* 243 */         return debug1.readBlockPos();
/*     */       }
/*     */ 
/*     */       
/*     */       public BlockPos copy(BlockPos debug1) {
/* 248 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/* 252 */   public static final EntityDataSerializer<Optional<BlockPos>> OPTIONAL_BLOCK_POS = new EntityDataSerializer<Optional<BlockPos>>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, Optional<BlockPos> debug2) {
/* 255 */         debug1.writeBoolean(debug2.isPresent());
/* 256 */         if (debug2.isPresent()) {
/* 257 */           debug1.writeBlockPos(debug2.get());
/*     */         }
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<BlockPos> read(FriendlyByteBuf debug1) {
/* 263 */         if (!debug1.readBoolean()) {
/* 264 */           return Optional.empty();
/*     */         }
/* 266 */         return Optional.of(debug1.readBlockPos());
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<BlockPos> copy(Optional<BlockPos> debug1) {
/* 271 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/* 275 */   public static final EntityDataSerializer<Direction> DIRECTION = new EntityDataSerializer<Direction>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, Direction debug2) {
/* 278 */         debug1.writeEnum((Enum)debug2);
/*     */       }
/*     */ 
/*     */       
/*     */       public Direction read(FriendlyByteBuf debug1) {
/* 283 */         return (Direction)debug1.readEnum(Direction.class);
/*     */       }
/*     */ 
/*     */       
/*     */       public Direction copy(Direction debug1) {
/* 288 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/* 292 */   public static final EntityDataSerializer<Optional<UUID>> OPTIONAL_UUID = new EntityDataSerializer<Optional<UUID>>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, Optional<UUID> debug2) {
/* 295 */         debug1.writeBoolean(debug2.isPresent());
/* 296 */         if (debug2.isPresent()) {
/* 297 */           debug1.writeUUID(debug2.get());
/*     */         }
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<UUID> read(FriendlyByteBuf debug1) {
/* 303 */         if (!debug1.readBoolean()) {
/* 304 */           return Optional.empty();
/*     */         }
/* 306 */         return Optional.of(debug1.readUUID());
/*     */       }
/*     */ 
/*     */       
/*     */       public Optional<UUID> copy(Optional<UUID> debug1) {
/* 311 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/* 315 */   public static final EntityDataSerializer<CompoundTag> COMPOUND_TAG = new EntityDataSerializer<CompoundTag>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, CompoundTag debug2) {
/* 318 */         debug1.writeNbt(debug2);
/*     */       }
/*     */ 
/*     */       
/*     */       public CompoundTag read(FriendlyByteBuf debug1) {
/* 323 */         return debug1.readNbt();
/*     */       }
/*     */ 
/*     */       
/*     */       public CompoundTag copy(CompoundTag debug1) {
/* 328 */         return debug1.copy();
/*     */       }
/*     */     };
/*     */   
/* 332 */   public static final EntityDataSerializer<VillagerData> VILLAGER_DATA = new EntityDataSerializer<VillagerData>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, VillagerData debug2) {
/* 335 */         debug1.writeVarInt(Registry.VILLAGER_TYPE.getId(debug2.getType()));
/* 336 */         debug1.writeVarInt(Registry.VILLAGER_PROFESSION.getId(debug2.getProfession()));
/* 337 */         debug1.writeVarInt(debug2.getLevel());
/*     */       }
/*     */ 
/*     */       
/*     */       public VillagerData read(FriendlyByteBuf debug1) {
/* 342 */         return new VillagerData((VillagerType)Registry.VILLAGER_TYPE
/* 343 */             .byId(debug1.readVarInt()), (VillagerProfession)Registry.VILLAGER_PROFESSION
/* 344 */             .byId(debug1.readVarInt()), debug1
/* 345 */             .readVarInt());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public VillagerData copy(VillagerData debug1) {
/* 351 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/* 355 */   public static final EntityDataSerializer<OptionalInt> OPTIONAL_UNSIGNED_INT = new EntityDataSerializer<OptionalInt>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, OptionalInt debug2) {
/* 358 */         debug1.writeVarInt(debug2.orElse(-1) + 1);
/*     */       }
/*     */ 
/*     */       
/*     */       public OptionalInt read(FriendlyByteBuf debug1) {
/* 363 */         int debug2 = debug1.readVarInt();
/* 364 */         return (debug2 == 0) ? OptionalInt.empty() : OptionalInt.of(debug2 - 1);
/*     */       }
/*     */ 
/*     */       
/*     */       public OptionalInt copy(OptionalInt debug1) {
/* 369 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/* 373 */   public static final EntityDataSerializer<Pose> POSE = new EntityDataSerializer<Pose>()
/*     */     {
/*     */       public void write(FriendlyByteBuf debug1, Pose debug2) {
/* 376 */         debug1.writeEnum((Enum)debug2);
/*     */       }
/*     */ 
/*     */       
/*     */       public Pose read(FriendlyByteBuf debug1) {
/* 381 */         return (Pose)debug1.readEnum(Pose.class);
/*     */       }
/*     */ 
/*     */       
/*     */       public Pose copy(Pose debug1) {
/* 386 */         return debug1;
/*     */       }
/*     */     };
/*     */   
/*     */   static {
/* 391 */     registerSerializer(BYTE);
/* 392 */     registerSerializer(INT);
/* 393 */     registerSerializer(FLOAT);
/* 394 */     registerSerializer(STRING);
/* 395 */     registerSerializer(COMPONENT);
/* 396 */     registerSerializer(OPTIONAL_COMPONENT);
/* 397 */     registerSerializer(ITEM_STACK);
/* 398 */     registerSerializer(BOOLEAN);
/* 399 */     registerSerializer(ROTATIONS);
/* 400 */     registerSerializer(BLOCK_POS);
/* 401 */     registerSerializer(OPTIONAL_BLOCK_POS);
/* 402 */     registerSerializer(DIRECTION);
/* 403 */     registerSerializer(OPTIONAL_UUID);
/* 404 */     registerSerializer(BLOCK_STATE);
/* 405 */     registerSerializer(COMPOUND_TAG);
/* 406 */     registerSerializer(PARTICLE);
/* 407 */     registerSerializer(VILLAGER_DATA);
/* 408 */     registerSerializer(OPTIONAL_UNSIGNED_INT);
/* 409 */     registerSerializer(POSE);
/*     */   }
/*     */   
/*     */   public static void registerSerializer(EntityDataSerializer<?> debug0) {
/* 413 */     SERIALIZERS.add(debug0);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static EntityDataSerializer<?> getSerializer(int debug0) {
/* 418 */     return (EntityDataSerializer)SERIALIZERS.byId(debug0);
/*     */   }
/*     */   
/*     */   public static int getSerializedId(EntityDataSerializer<?> debug0) {
/* 422 */     return SERIALIZERS.getId(debug0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\syncher\EntityDataSerializers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */