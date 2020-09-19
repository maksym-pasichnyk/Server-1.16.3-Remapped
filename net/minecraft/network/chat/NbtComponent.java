/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.critereon.NbtPredicate;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Coordinates;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelectorParser;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public abstract class NbtComponent
/*     */   extends BaseComponent implements ContextAwareComponent {
/*  29 */   private static final Logger LOGGER = LogManager.getLogger(); protected final boolean interpreting;
/*     */   protected final String nbtPathPattern;
/*     */   @Nullable
/*     */   protected final NbtPathArgument.NbtPath compiledNbtPath;
/*     */   
/*     */   public static class EntityNbtComponent extends NbtComponent { private final String selectorPattern;
/*     */     @Nullable
/*     */     private final EntitySelector compiledSelector;
/*     */     
/*     */     public EntityNbtComponent(String debug1, boolean debug2, String debug3) {
/*  39 */       super(debug1, debug2);
/*  40 */       this.selectorPattern = debug3;
/*  41 */       this.compiledSelector = compileSelector(debug3);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private static EntitySelector compileSelector(String debug0) {
/*     */       try {
/*  47 */         EntitySelectorParser debug1 = new EntitySelectorParser(new StringReader(debug0));
/*  48 */         return debug1.parse();
/*  49 */       } catch (CommandSyntaxException debug1) {
/*  50 */         return null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private EntityNbtComponent(String debug1, @Nullable NbtPathArgument.NbtPath debug2, boolean debug3, String debug4, @Nullable EntitySelector debug5) {
/*  55 */       super(debug1, debug2, debug3);
/*  56 */       this.selectorPattern = debug4;
/*  57 */       this.compiledSelector = debug5;
/*     */     }
/*     */     
/*     */     public String getSelector() {
/*  61 */       return this.selectorPattern;
/*     */     }
/*     */ 
/*     */     
/*     */     public EntityNbtComponent plainCopy() {
/*  66 */       return new EntityNbtComponent(this.nbtPathPattern, this.compiledNbtPath, this.interpreting, this.selectorPattern, this.compiledSelector);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Stream<CompoundTag> getData(CommandSourceStack debug1) throws CommandSyntaxException {
/*  71 */       if (this.compiledSelector != null) {
/*  72 */         List<? extends Entity> debug2 = this.compiledSelector.findEntities(debug1);
/*  73 */         return debug2.stream().map(NbtPredicate::getEntityTagToCompare);
/*     */       } 
/*     */       
/*  76 */       return Stream.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object debug1) {
/*  81 */       if (this == debug1) {
/*  82 */         return true;
/*     */       }
/*     */       
/*  85 */       if (debug1 instanceof EntityNbtComponent) {
/*  86 */         EntityNbtComponent debug2 = (EntityNbtComponent)debug1;
/*  87 */         return (Objects.equals(this.selectorPattern, debug2.selectorPattern) && 
/*  88 */           Objects.equals(this.nbtPathPattern, debug2.nbtPathPattern) && super
/*  89 */           .equals(debug1));
/*     */       } 
/*     */       
/*  92 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  97 */       return "EntityNbtComponent{selector='" + this.selectorPattern + '\'' + "path='" + this.nbtPathPattern + '\'' + ", siblings=" + this.siblings + ", style=" + 
/*     */ 
/*     */ 
/*     */         
/* 101 */         getStyle() + '}';
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class BlockNbtComponent
/*     */     extends NbtComponent
/*     */   {
/*     */     private final String posPattern;
/*     */     @Nullable
/*     */     private final Coordinates compiledPos;
/*     */     
/*     */     public BlockNbtComponent(String debug1, boolean debug2, String debug3) {
/* 113 */       super(debug1, debug2);
/* 114 */       this.posPattern = debug3;
/* 115 */       this.compiledPos = compilePos(this.posPattern);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private Coordinates compilePos(String debug1) {
/*     */       try {
/* 121 */         return BlockPosArgument.blockPos().parse(new StringReader(debug1));
/* 122 */       } catch (CommandSyntaxException debug2) {
/* 123 */         return null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private BlockNbtComponent(String debug1, @Nullable NbtPathArgument.NbtPath debug2, boolean debug3, String debug4, @Nullable Coordinates debug5) {
/* 128 */       super(debug1, debug2, debug3);
/* 129 */       this.posPattern = debug4;
/* 130 */       this.compiledPos = debug5;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public String getPos() {
/* 135 */       return this.posPattern;
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockNbtComponent plainCopy() {
/* 140 */       return new BlockNbtComponent(this.nbtPathPattern, this.compiledNbtPath, this.interpreting, this.posPattern, this.compiledPos);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Stream<CompoundTag> getData(CommandSourceStack debug1) {
/* 145 */       if (this.compiledPos != null) {
/* 146 */         ServerLevel debug2 = debug1.getLevel();
/* 147 */         BlockPos debug3 = this.compiledPos.getBlockPos(debug1);
/* 148 */         if (debug2.isLoaded(debug3)) {
/* 149 */           BlockEntity debug4 = debug2.getBlockEntity(debug3);
/*     */           
/* 151 */           if (debug4 != null) {
/* 152 */             return Stream.of(debug4.save(new CompoundTag()));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 157 */       return Stream.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object debug1) {
/* 162 */       if (this == debug1) {
/* 163 */         return true;
/*     */       }
/*     */       
/* 166 */       if (debug1 instanceof BlockNbtComponent) {
/* 167 */         BlockNbtComponent debug2 = (BlockNbtComponent)debug1;
/* 168 */         return (Objects.equals(this.posPattern, debug2.posPattern) && 
/* 169 */           Objects.equals(this.nbtPathPattern, debug2.nbtPathPattern) && super
/* 170 */           .equals(debug1));
/*     */       } 
/*     */       
/* 173 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 178 */       return "BlockPosArgument{pos='" + this.posPattern + '\'' + "path='" + this.nbtPathPattern + '\'' + ", siblings=" + this.siblings + ", style=" + 
/*     */ 
/*     */ 
/*     */         
/* 182 */         getStyle() + '}';
/*     */     }
/*     */   }
/*     */   
/*     */   public static class StorageNbtComponent
/*     */     extends NbtComponent {
/*     */     private final ResourceLocation id;
/*     */     
/*     */     public StorageNbtComponent(String debug1, boolean debug2, ResourceLocation debug3) {
/* 191 */       super(debug1, debug2);
/* 192 */       this.id = debug3;
/*     */     }
/*     */     
/*     */     public StorageNbtComponent(String debug1, @Nullable NbtPathArgument.NbtPath debug2, boolean debug3, ResourceLocation debug4) {
/* 196 */       super(debug1, debug2, debug3);
/* 197 */       this.id = debug4;
/*     */     }
/*     */     
/*     */     public ResourceLocation getId() {
/* 201 */       return this.id;
/*     */     }
/*     */ 
/*     */     
/*     */     public StorageNbtComponent plainCopy() {
/* 206 */       return new StorageNbtComponent(this.nbtPathPattern, this.compiledNbtPath, this.interpreting, this.id);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Stream<CompoundTag> getData(CommandSourceStack debug1) {
/* 211 */       CompoundTag debug2 = debug1.getServer().getCommandStorage().get(this.id);
/* 212 */       return Stream.of(debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object debug1) {
/* 217 */       if (this == debug1) {
/* 218 */         return true;
/*     */       }
/*     */       
/* 221 */       if (debug1 instanceof StorageNbtComponent) {
/* 222 */         StorageNbtComponent debug2 = (StorageNbtComponent)debug1;
/* 223 */         return (Objects.equals(this.id, debug2.id) && 
/* 224 */           Objects.equals(this.nbtPathPattern, debug2.nbtPathPattern) && super
/* 225 */           .equals(debug1));
/*     */       } 
/*     */       
/* 228 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 233 */       return "StorageNbtComponent{id='" + this.id + '\'' + "path='" + this.nbtPathPattern + '\'' + ", siblings=" + this.siblings + ", style=" + 
/*     */ 
/*     */ 
/*     */         
/* 237 */         getStyle() + '}';
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
/*     */   @Nullable
/*     */   private static NbtPathArgument.NbtPath compileNbtPath(String debug0) {
/*     */     try {
/* 252 */       return (new NbtPathArgument()).parse(new StringReader(debug0));
/* 253 */     } catch (CommandSyntaxException debug1) {
/* 254 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public NbtComponent(String debug1, boolean debug2) {
/* 259 */     this(debug1, compileNbtPath(debug1), debug2);
/*     */   }
/*     */   
/*     */   protected NbtComponent(String debug1, @Nullable NbtPathArgument.NbtPath debug2, boolean debug3) {
/* 263 */     this.nbtPathPattern = debug1;
/* 264 */     this.compiledNbtPath = debug2;
/* 265 */     this.interpreting = debug3;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNbtPath() {
/* 271 */     return this.nbtPathPattern;
/*     */   }
/*     */   
/*     */   public boolean isInterpreting() {
/* 275 */     return this.interpreting;
/*     */   }
/*     */ 
/*     */   
/*     */   public MutableComponent resolve(@Nullable CommandSourceStack debug1, @Nullable Entity debug2, int debug3) throws CommandSyntaxException {
/* 280 */     if (debug1 == null || this.compiledNbtPath == null) {
/* 281 */       return new TextComponent("");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 291 */     Stream<String> debug4 = getData(debug1).flatMap(debug1 -> { try { return this.compiledNbtPath.get((Tag)debug1).stream(); } catch (CommandSyntaxException debug2) { return Stream.empty(); }  }).map(Tag::getAsString);
/*     */     
/* 293 */     if (this.interpreting) {
/* 294 */       return debug4.<MutableComponent>flatMap(debug3 -> {
/*     */             try {
/*     */               MutableComponent debug4 = Component.Serializer.fromJson(debug3);
/*     */               return Stream.of(ComponentUtils.updateForEntity(debug0, debug4, debug1, debug2));
/* 298 */             } catch (Exception debug4) {
/*     */               LOGGER.warn("Failed to parse component: " + debug3, debug4);
/*     */               
/*     */               return Stream.of(new MutableComponent[0]);
/*     */             } 
/* 303 */           }).reduce((debug0, debug1) -> debug0.append(", ").append(debug1))
/* 304 */         .orElse(new TextComponent(""));
/*     */     }
/* 306 */     return new TextComponent(Joiner.on(", ").join(debug4.iterator()));
/*     */   }
/*     */   
/*     */   protected abstract Stream<CompoundTag> getData(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\network\chat\NbtComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */