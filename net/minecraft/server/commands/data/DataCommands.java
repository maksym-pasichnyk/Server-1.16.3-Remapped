/*     */ package net.minecraft.server.commands.data;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.CompoundTagArgument;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.commands.arguments.NbtTagArgument;
/*     */ import net.minecraft.nbt.CollectionTag;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NumericTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataCommands
/*     */ {
/*  45 */   private static final SimpleCommandExceptionType ERROR_MERGE_UNCHANGED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.data.merge.failed")); private static final DynamicCommandExceptionType ERROR_GET_NOT_NUMBER; private static final DynamicCommandExceptionType ERROR_GET_NON_EXISTENT; static {
/*  46 */     ERROR_GET_NOT_NUMBER = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.data.get.invalid", new Object[] { debug0 }));
/*  47 */     ERROR_GET_NON_EXISTENT = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.data.get.unknown", new Object[] { debug0 }));
/*  48 */   } private static final SimpleCommandExceptionType ERROR_MULTIPLE_TAGS = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.data.get.multiple")); private static final DynamicCommandExceptionType ERROR_EXPECTED_LIST; static {
/*  49 */     ERROR_EXPECTED_LIST = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.data.modify.expected_list", new Object[] { debug0 }));
/*  50 */     ERROR_EXPECTED_OBJECT = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.data.modify.expected_object", new Object[] { debug0 }));
/*  51 */     ERROR_INVALID_INDEX = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.data.modify.invalid_index", new Object[] { debug0 }));
/*     */   }
/*  53 */   private static final DynamicCommandExceptionType ERROR_EXPECTED_OBJECT; private static final DynamicCommandExceptionType ERROR_INVALID_INDEX; public static final List<Function<String, DataProvider>> ALL_PROVIDERS = (List<Function<String, DataProvider>>)ImmutableList.of(EntityDataAccessor.PROVIDER, BlockDataAccessor.PROVIDER, StorageDataAccessor.PROVIDER); public static final List<DataProvider> TARGET_PROVIDERS; public static final List<DataProvider> SOURCE_PROVIDERS;
/*     */   static {
/*  55 */     TARGET_PROVIDERS = (List<DataProvider>)ALL_PROVIDERS.stream().map(debug0 -> (DataProvider)debug0.apply("target")).collect(ImmutableList.toImmutableList());
/*  56 */     SOURCE_PROVIDERS = (List<DataProvider>)ALL_PROVIDERS.stream().map(debug0 -> (DataProvider)debug0.apply("source")).collect(ImmutableList.toImmutableList());
/*     */   }
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  59 */     LiteralArgumentBuilder<CommandSourceStack> debug1 = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("data").requires(debug0 -> debug0.hasPermission(2));
/*     */     
/*  61 */     for (DataProvider debug3 : TARGET_PROVIDERS) {
/*  62 */       ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)debug1
/*  63 */         .then(debug3
/*  64 */           .wrap((ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("merge"), debug1 -> debug1.then(Commands.argument("nbt", (ArgumentType)CompoundTagArgument.compoundTag()).executes(())))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  71 */         .then(debug3
/*  72 */           .wrap((ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("get"), debug1 -> debug1.executes(()).then(((RequiredArgumentBuilder)Commands.argument("path", (ArgumentType)NbtPathArgument.nbtPath()).executes(())).then(Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg()).executes(()))))))
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
/*  84 */         .then(debug3
/*  85 */           .wrap((ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("remove"), debug1 -> debug1.then(Commands.argument("path", (ArgumentType)NbtPathArgument.nbtPath()).executes(())))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  92 */         .then(decorateModification((debug0, debug1) -> debug0.then(Commands.literal("insert").then(Commands.argument("index", (ArgumentType)IntegerArgumentType.integer()).then(debug1.create(())))).then(Commands.literal("prepend").then(debug1.create(()))).then(Commands.literal("append").then(debug1.create(()))).then(Commands.literal("set").then(debug1.create(()))).then(Commands.literal("merge").then(debug1.create(())))));
/*     */     }
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
/* 159 */     debug0.register(debug1);
/*     */   }
/*     */   
/*     */   private static int insertAtIndex(int debug0, CompoundTag debug1, NbtPathArgument.NbtPath debug2, List<Tag> debug3) throws CommandSyntaxException {
/* 163 */     Collection<Tag> debug4 = debug2.getOrCreate((Tag)debug1, net.minecraft.nbt.ListTag::new);
/*     */     
/* 165 */     int debug5 = 0;
/* 166 */     for (Tag debug7 : debug4) {
/* 167 */       if (!(debug7 instanceof CollectionTag)) {
/* 168 */         throw ERROR_EXPECTED_LIST.create(debug7);
/*     */       }
/*     */       
/* 171 */       boolean debug8 = false;
/* 172 */       CollectionTag<?> debug9 = (CollectionTag)debug7;
/* 173 */       int debug10 = (debug0 < 0) ? (debug9.size() + debug0 + 1) : debug0;
/* 174 */       for (Tag debug12 : debug3) {
/*     */         try {
/* 176 */           if (debug9.addTag(debug10, debug12.copy())) {
/* 177 */             debug10++;
/* 178 */             debug8 = true;
/*     */           } 
/* 180 */         } catch (IndexOutOfBoundsException debug13) {
/* 181 */           throw ERROR_INVALID_INDEX.create(Integer.valueOf(debug10));
/*     */         } 
/*     */       } 
/* 184 */       debug5 += debug8 ? 1 : 0;
/*     */     } 
/*     */     
/* 187 */     return debug5;
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
/*     */   private static ArgumentBuilder<CommandSourceStack, ?> decorateModification(BiConsumer<ArgumentBuilder<CommandSourceStack, ?>, DataManipulatorDecorator> debug0) {
/* 199 */     LiteralArgumentBuilder<CommandSourceStack> debug1 = Commands.literal("modify");
/*     */     
/* 201 */     for (Iterator<DataProvider> iterator = TARGET_PROVIDERS.iterator(); iterator.hasNext(); ) { DataProvider debug3 = iterator.next();
/* 202 */       debug3.wrap((ArgumentBuilder)debug1, debug2 -> {
/*     */             RequiredArgumentBuilder requiredArgumentBuilder = Commands.argument("targetPath", (ArgumentType)NbtPathArgument.nbtPath());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             for (DataProvider debug5 : SOURCE_PROVIDERS) {
/*     */               debug0.accept(requiredArgumentBuilder, ());
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             debug0.accept(requiredArgumentBuilder, ());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             return debug2.then((ArgumentBuilder)requiredArgumentBuilder);
/*     */           }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 241 */     return (ArgumentBuilder)debug1;
/*     */   }
/*     */   
/*     */   private static int manipulateData(CommandContext<CommandSourceStack> debug0, DataProvider debug1, DataManipulator debug2, List<Tag> debug3) throws CommandSyntaxException {
/* 245 */     DataAccessor debug4 = debug1.access(debug0);
/* 246 */     NbtPathArgument.NbtPath debug5 = NbtPathArgument.getPath(debug0, "targetPath");
/*     */     
/* 248 */     CompoundTag debug6 = debug4.getData();
/*     */     
/* 250 */     int debug7 = debug2.modify(debug0, debug6, debug5, debug3);
/*     */     
/* 252 */     if (debug7 == 0) {
/* 253 */       throw ERROR_MERGE_UNCHANGED.create();
/*     */     }
/*     */     
/* 256 */     debug4.setData(debug6);
/* 257 */     ((CommandSourceStack)debug0.getSource()).sendSuccess(debug4.getModifiedSuccess(), true);
/*     */     
/* 259 */     return debug7;
/*     */   }
/*     */   
/*     */   private static int removeData(CommandSourceStack debug0, DataAccessor debug1, NbtPathArgument.NbtPath debug2) throws CommandSyntaxException {
/* 263 */     CompoundTag debug3 = debug1.getData();
/*     */     
/* 265 */     int debug4 = debug2.remove((Tag)debug3);
/*     */     
/* 267 */     if (debug4 == 0) {
/* 268 */       throw ERROR_MERGE_UNCHANGED.create();
/*     */     }
/*     */     
/* 271 */     debug1.setData(debug3);
/* 272 */     debug0.sendSuccess(debug1.getModifiedSuccess(), true);
/* 273 */     return debug4;
/*     */   }
/*     */   
/*     */   private static Tag getSingleTag(NbtPathArgument.NbtPath debug0, DataAccessor debug1) throws CommandSyntaxException {
/* 277 */     Collection<Tag> debug2 = debug0.get((Tag)debug1.getData());
/* 278 */     Iterator<Tag> debug3 = debug2.iterator();
/* 279 */     Tag debug4 = debug3.next();
/* 280 */     if (debug3.hasNext()) {
/* 281 */       throw ERROR_MULTIPLE_TAGS.create();
/*     */     }
/*     */     
/* 284 */     return debug4;
/*     */   }
/*     */   private static int getData(CommandSourceStack debug0, DataAccessor debug1, NbtPathArgument.NbtPath debug2) throws CommandSyntaxException {
/*     */     int debug4;
/* 288 */     Tag debug3 = getSingleTag(debug2, debug1);
/*     */     
/* 290 */     if (debug3 instanceof NumericTag) {
/* 291 */       debug4 = Mth.floor(((NumericTag)debug3).getAsDouble());
/* 292 */     } else if (debug3 instanceof CollectionTag) {
/* 293 */       debug4 = ((CollectionTag)debug3).size();
/* 294 */     } else if (debug3 instanceof CompoundTag) {
/* 295 */       debug4 = ((CompoundTag)debug3).size();
/* 296 */     } else if (debug3 instanceof net.minecraft.nbt.StringTag) {
/* 297 */       debug4 = debug3.getAsString().length();
/*     */     } else {
/* 299 */       throw ERROR_GET_NON_EXISTENT.create(debug2.toString());
/*     */     } 
/* 301 */     debug0.sendSuccess(debug1.getPrintSuccess(debug3), false);
/* 302 */     return debug4;
/*     */   }
/*     */   
/*     */   private static int getNumeric(CommandSourceStack debug0, DataAccessor debug1, NbtPathArgument.NbtPath debug2, double debug3) throws CommandSyntaxException {
/* 306 */     Tag debug5 = getSingleTag(debug2, debug1);
/* 307 */     if (!(debug5 instanceof NumericTag)) {
/* 308 */       throw ERROR_GET_NOT_NUMBER.create(debug2.toString());
/*     */     }
/* 310 */     int debug6 = Mth.floor(((NumericTag)debug5).getAsDouble() * debug3);
/* 311 */     debug0.sendSuccess(debug1.getPrintSuccess(debug2, debug3, debug6), false);
/* 312 */     return debug6;
/*     */   }
/*     */   
/*     */   private static int getData(CommandSourceStack debug0, DataAccessor debug1) throws CommandSyntaxException {
/* 316 */     debug0.sendSuccess(debug1.getPrintSuccess((Tag)debug1.getData()), false);
/* 317 */     return 1;
/*     */   }
/*     */   
/*     */   private static int mergeData(CommandSourceStack debug0, DataAccessor debug1, CompoundTag debug2) throws CommandSyntaxException {
/* 321 */     CompoundTag debug3 = debug1.getData();
/* 322 */     CompoundTag debug4 = debug3.copy().merge(debug2);
/*     */     
/* 324 */     if (debug3.equals(debug4)) {
/* 325 */       throw ERROR_MERGE_UNCHANGED.create();
/*     */     }
/*     */     
/* 328 */     debug1.setData(debug4);
/*     */     
/* 330 */     debug0.sendSuccess(debug1.getModifiedSuccess(), true);
/* 331 */     return 1;
/*     */   }
/*     */   
/*     */   public static interface DataProvider {
/*     */     DataAccessor access(CommandContext<CommandSourceStack> param1CommandContext) throws CommandSyntaxException;
/*     */     
/*     */     ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> param1ArgumentBuilder, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> param1Function);
/*     */   }
/*     */   
/*     */   static interface DataManipulatorDecorator {
/*     */     ArgumentBuilder<CommandSourceStack, ?> create(DataCommands.DataManipulator param1DataManipulator);
/*     */   }
/*     */   
/*     */   static interface DataManipulator {
/*     */     int modify(CommandContext<CommandSourceStack> param1CommandContext, CompoundTag param1CompoundTag, NbtPathArgument.NbtPath param1NbtPath, List<Tag> param1List) throws CommandSyntaxException;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\data\DataCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */