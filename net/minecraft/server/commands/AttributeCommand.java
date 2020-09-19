/*     */ package net.minecraft.server.commands;
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*     */ import com.mojang.brigadier.arguments.StringArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
/*     */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*     */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.SharedSuggestionProvider;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.ResourceLocationArgument;
/*     */ import net.minecraft.commands.arguments.UuidArgument;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeMap;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ 
/*     */ public class AttributeCommand {
/*     */   private static final SuggestionProvider<CommandSourceStack> AVAILABLE_ATTRIBUTES;
/*     */   private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY;
/*     */   
/*     */   static {
/*  38 */     AVAILABLE_ATTRIBUTES = ((debug0, debug1) -> SharedSuggestionProvider.suggestResource(Registry.ATTRIBUTE.keySet(), debug1));
/*     */     
/*  40 */     ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.attribute.failed.entity", new Object[] { debug0 }));
/*  41 */     ERROR_NO_SUCH_ATTRIBUTE = new Dynamic2CommandExceptionType((debug0, debug1) -> new TranslatableComponent("commands.attribute.failed.no_attribute", new Object[] { debug0, debug1 }));
/*  42 */     ERROR_NO_SUCH_MODIFIER = new Dynamic3CommandExceptionType((debug0, debug1, debug2) -> new TranslatableComponent("commands.attribute.failed.no_modifier", new Object[] { debug1, debug0, debug2 }));
/*  43 */     ERROR_MODIFIER_ALREADY_PRESENT = new Dynamic3CommandExceptionType((debug0, debug1, debug2) -> new TranslatableComponent("commands.attribute.failed.modifier_already_present", new Object[] { debug2, debug1, debug0 }));
/*     */   } private static final Dynamic2CommandExceptionType ERROR_NO_SUCH_ATTRIBUTE; private static final Dynamic3CommandExceptionType ERROR_NO_SUCH_MODIFIER; private static final Dynamic3CommandExceptionType ERROR_MODIFIER_ALREADY_PRESENT;
/*     */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/*  46 */     debug0.register(
/*  47 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("attribute")
/*  48 */         .requires(debug0 -> debug0.hasPermission(2)))
/*  49 */         .then(
/*  50 */           Commands.argument("target", (ArgumentType)EntityArgument.entity())
/*  51 */           .then((
/*  52 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("attribute", (ArgumentType)ResourceLocationArgument.id())
/*  53 */             .suggests(AVAILABLE_ATTRIBUTES)
/*  54 */             .then((
/*  55 */               (LiteralArgumentBuilder)Commands.literal("get")
/*  56 */               .executes(debug0 -> getAttributeValue((CommandSourceStack)debug0.getSource(), EntityArgument.getEntity(debug0, "target"), ResourceLocationArgument.getAttribute(debug0, "attribute"), 1.0D)))
/*  57 */               .then(
/*  58 */                 Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg())
/*  59 */                 .executes(debug0 -> getAttributeValue((CommandSourceStack)debug0.getSource(), EntityArgument.getEntity(debug0, "target"), ResourceLocationArgument.getAttribute(debug0, "attribute"), DoubleArgumentType.getDouble(debug0, "scale"))))))
/*     */ 
/*     */             
/*  62 */             .then((
/*  63 */               (LiteralArgumentBuilder)Commands.literal("base")
/*  64 */               .then(
/*  65 */                 Commands.literal("set")
/*  66 */                 .then(
/*  67 */                   Commands.argument("value", (ArgumentType)DoubleArgumentType.doubleArg())
/*  68 */                   .executes(debug0 -> setAttributeBase((CommandSourceStack)debug0.getSource(), EntityArgument.getEntity(debug0, "target"), ResourceLocationArgument.getAttribute(debug0, "attribute"), DoubleArgumentType.getDouble(debug0, "value"))))))
/*     */ 
/*     */               
/*  71 */               .then((
/*  72 */                 (LiteralArgumentBuilder)Commands.literal("get")
/*  73 */                 .executes(debug0 -> getAttributeBase((CommandSourceStack)debug0.getSource(), EntityArgument.getEntity(debug0, "target"), ResourceLocationArgument.getAttribute(debug0, "attribute"), 1.0D)))
/*  74 */                 .then(
/*  75 */                   Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg())
/*  76 */                   .executes(debug0 -> getAttributeBase((CommandSourceStack)debug0.getSource(), EntityArgument.getEntity(debug0, "target"), ResourceLocationArgument.getAttribute(debug0, "attribute"), DoubleArgumentType.getDouble(debug0, "scale")))))))
/*     */ 
/*     */ 
/*     */             
/*  80 */             .then((
/*  81 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("modifier")
/*  82 */               .then(
/*  83 */                 Commands.literal("add")
/*  84 */                 .then(
/*  85 */                   Commands.argument("uuid", (ArgumentType)UuidArgument.uuid())
/*  86 */                   .then(
/*  87 */                     Commands.argument("name", (ArgumentType)StringArgumentType.string())
/*  88 */                     .then((
/*  89 */                       (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("value", (ArgumentType)DoubleArgumentType.doubleArg())
/*  90 */                       .then(
/*  91 */                         Commands.literal("add")
/*  92 */                         .executes(debug0 -> addModifier((CommandSourceStack)debug0.getSource(), EntityArgument.getEntity(debug0, "target"), ResourceLocationArgument.getAttribute(debug0, "attribute"), UuidArgument.getUuid(debug0, "uuid"), StringArgumentType.getString(debug0, "name"), DoubleArgumentType.getDouble(debug0, "value"), AttributeModifier.Operation.ADDITION))))
/*     */                       
/*  94 */                       .then(
/*  95 */                         Commands.literal("multiply")
/*  96 */                         .executes(debug0 -> addModifier((CommandSourceStack)debug0.getSource(), EntityArgument.getEntity(debug0, "target"), ResourceLocationArgument.getAttribute(debug0, "attribute"), UuidArgument.getUuid(debug0, "uuid"), StringArgumentType.getString(debug0, "name"), DoubleArgumentType.getDouble(debug0, "value"), AttributeModifier.Operation.MULTIPLY_TOTAL))))
/*     */                       
/*  98 */                       .then(
/*  99 */                         Commands.literal("multiply_base")
/* 100 */                         .executes(debug0 -> addModifier((CommandSourceStack)debug0.getSource(), EntityArgument.getEntity(debug0, "target"), ResourceLocationArgument.getAttribute(debug0, "attribute"), UuidArgument.getUuid(debug0, "uuid"), StringArgumentType.getString(debug0, "name"), DoubleArgumentType.getDouble(debug0, "value"), AttributeModifier.Operation.MULTIPLY_BASE))))))))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 106 */               .then(
/* 107 */                 Commands.literal("remove")
/* 108 */                 .then(Commands.argument("uuid", (ArgumentType)UuidArgument.uuid())
/* 109 */                   .executes(debug0 -> removeModifier((CommandSourceStack)debug0.getSource(), EntityArgument.getEntity(debug0, "target"), ResourceLocationArgument.getAttribute(debug0, "attribute"), UuidArgument.getUuid(debug0, "uuid"))))))
/*     */ 
/*     */               
/* 112 */               .then(
/* 113 */                 Commands.literal("value")
/* 114 */                 .then(
/* 115 */                   Commands.literal("get")
/* 116 */                   .then((
/* 117 */                     (RequiredArgumentBuilder)Commands.argument("uuid", (ArgumentType)UuidArgument.uuid())
/* 118 */                     .executes(debug0 -> getAttributeModifier((CommandSourceStack)debug0.getSource(), EntityArgument.getEntity(debug0, "target"), ResourceLocationArgument.getAttribute(debug0, "attribute"), UuidArgument.getUuid(debug0, "uuid"), 1.0D)))
/* 119 */                     .then(
/* 120 */                       Commands.argument("scale", (ArgumentType)DoubleArgumentType.doubleArg())
/* 121 */                       .executes(debug0 -> getAttributeModifier((CommandSourceStack)debug0.getSource(), EntityArgument.getEntity(debug0, "target"), ResourceLocationArgument.getAttribute(debug0, "attribute"), UuidArgument.getUuid(debug0, "uuid"), DoubleArgumentType.getDouble(debug0, "scale")))))))))));
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
/*     */   private static AttributeInstance getAttributeInstance(Entity debug0, Attribute debug1) throws CommandSyntaxException {
/* 133 */     AttributeInstance debug2 = getLivingEntity(debug0).getAttributes().getInstance(debug1);
/* 134 */     if (debug2 == null) {
/* 135 */       throw ERROR_NO_SUCH_ATTRIBUTE.create(debug0.getName(), new TranslatableComponent(debug1.getDescriptionId()));
/*     */     }
/* 137 */     return debug2;
/*     */   }
/*     */   
/*     */   private static LivingEntity getLivingEntity(Entity debug0) throws CommandSyntaxException {
/* 141 */     if (!(debug0 instanceof LivingEntity)) {
/* 142 */       throw ERROR_NOT_LIVING_ENTITY.create(debug0.getName());
/*     */     }
/* 144 */     return (LivingEntity)debug0;
/*     */   }
/*     */   
/*     */   private static LivingEntity getEntityWithAttribute(Entity debug0, Attribute debug1) throws CommandSyntaxException {
/* 148 */     LivingEntity debug2 = getLivingEntity(debug0);
/* 149 */     if (!debug2.getAttributes().hasAttribute(debug1)) {
/* 150 */       throw ERROR_NO_SUCH_ATTRIBUTE.create(debug0.getName(), new TranslatableComponent(debug1.getDescriptionId()));
/*     */     }
/* 152 */     return debug2;
/*     */   }
/*     */   
/*     */   private static int getAttributeValue(CommandSourceStack debug0, Entity debug1, Attribute debug2, double debug3) throws CommandSyntaxException {
/* 156 */     LivingEntity debug5 = getEntityWithAttribute(debug1, debug2);
/* 157 */     double debug6 = debug5.getAttributeValue(debug2);
/* 158 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.attribute.value.get.success", new Object[] { new TranslatableComponent(debug2.getDescriptionId()), debug1.getName(), Double.valueOf(debug6) }), false);
/* 159 */     return (int)(debug6 * debug3);
/*     */   }
/*     */   
/*     */   private static int getAttributeBase(CommandSourceStack debug0, Entity debug1, Attribute debug2, double debug3) throws CommandSyntaxException {
/* 163 */     LivingEntity debug5 = getEntityWithAttribute(debug1, debug2);
/* 164 */     double debug6 = debug5.getAttributeBaseValue(debug2);
/* 165 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.attribute.base_value.get.success", new Object[] { new TranslatableComponent(debug2.getDescriptionId()), debug1.getName(), Double.valueOf(debug6) }), false);
/* 166 */     return (int)(debug6 * debug3);
/*     */   }
/*     */   
/*     */   private static int getAttributeModifier(CommandSourceStack debug0, Entity debug1, Attribute debug2, UUID debug3, double debug4) throws CommandSyntaxException {
/* 170 */     LivingEntity debug6 = getEntityWithAttribute(debug1, debug2);
/*     */     
/* 172 */     AttributeMap debug7 = debug6.getAttributes();
/*     */     
/* 174 */     if (!debug7.hasModifier(debug2, debug3)) {
/* 175 */       throw ERROR_NO_SUCH_MODIFIER.create(debug1.getName(), new TranslatableComponent(debug2.getDescriptionId()), debug3);
/*     */     }
/*     */     
/* 178 */     double debug8 = debug7.getModifierValue(debug2, debug3);
/* 179 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.attribute.modifier.value.get.success", new Object[] { debug3, new TranslatableComponent(debug2.getDescriptionId()), debug1.getName(), Double.valueOf(debug8) }), false);
/* 180 */     return (int)(debug8 * debug4);
/*     */   }
/*     */   
/*     */   private static int setAttributeBase(CommandSourceStack debug0, Entity debug1, Attribute debug2, double debug3) throws CommandSyntaxException {
/* 184 */     getAttributeInstance(debug1, debug2).setBaseValue(debug3);
/* 185 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.attribute.base_value.set.success", new Object[] { new TranslatableComponent(debug2.getDescriptionId()), debug1.getName(), Double.valueOf(debug3) }), false);
/* 186 */     return 1;
/*     */   }
/*     */   
/*     */   private static int addModifier(CommandSourceStack debug0, Entity debug1, Attribute debug2, UUID debug3, String debug4, double debug5, AttributeModifier.Operation debug7) throws CommandSyntaxException {
/* 190 */     AttributeInstance debug8 = getAttributeInstance(debug1, debug2);
/* 191 */     AttributeModifier debug9 = new AttributeModifier(debug3, debug4, debug5, debug7);
/* 192 */     if (debug8.hasModifier(debug9)) {
/* 193 */       throw ERROR_MODIFIER_ALREADY_PRESENT.create(debug1.getName(), new TranslatableComponent(debug2.getDescriptionId()), debug3);
/*     */     }
/* 195 */     debug8.addPermanentModifier(debug9);
/* 196 */     debug0.sendSuccess((Component)new TranslatableComponent("commands.attribute.modifier.add.success", new Object[] { debug3, new TranslatableComponent(debug2.getDescriptionId()), debug1.getName() }), false);
/* 197 */     return 1;
/*     */   }
/*     */   
/*     */   private static int removeModifier(CommandSourceStack debug0, Entity debug1, Attribute debug2, UUID debug3) throws CommandSyntaxException {
/* 201 */     AttributeInstance debug4 = getAttributeInstance(debug1, debug2);
/* 202 */     if (debug4.removePermanentModifier(debug3)) {
/* 203 */       debug0.sendSuccess((Component)new TranslatableComponent("commands.attribute.modifier.remove.success", new Object[] { debug3, new TranslatableComponent(debug2.getDescriptionId()), debug1.getName() }), false);
/* 204 */       return 1;
/*     */     } 
/* 206 */     throw ERROR_NO_SUCH_MODIFIER.create(debug1.getName(), new TranslatableComponent(debug2.getDescriptionId()), debug3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\AttributeCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */