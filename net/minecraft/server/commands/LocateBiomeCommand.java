/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.ResourceLocationArgument;
/*    */ import net.minecraft.commands.synchronization.SuggestionProviders;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ public class LocateBiomeCommand {
/*    */   static {
/* 21 */     ERROR_INVALID_BIOME = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.locatebiome.invalid", new Object[] { debug0 }));
/* 22 */     ERROR_BIOME_NOT_FOUND = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("commands.locatebiome.notFound", new Object[] { debug0 }));
/*    */   }
/*    */   public static final DynamicCommandExceptionType ERROR_INVALID_BIOME;
/*    */   private static final DynamicCommandExceptionType ERROR_BIOME_NOT_FOUND;
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 28 */     debug0.register(
/* 29 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("locatebiome")
/* 30 */         .requires(debug0 -> debug0.hasPermission(2)))
/* 31 */         .then(
/* 32 */           Commands.argument("biome", (ArgumentType)ResourceLocationArgument.id())
/* 33 */           .suggests(SuggestionProviders.AVAILABLE_BIOMES)
/* 34 */           .executes(debug0 -> locateBiome((CommandSourceStack)debug0.getSource(), (ResourceLocation)debug0.getArgument("biome", ResourceLocation.class)))));
/*    */   }
/*    */ 
/*    */   
/*    */   private static int locateBiome(CommandSourceStack debug0, ResourceLocation debug1) throws CommandSyntaxException {
/* 39 */     Biome debug2 = (Biome)debug0.getServer().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getOptional(debug1).orElseThrow(() -> ERROR_INVALID_BIOME.create(debug0));
/*    */     
/* 41 */     BlockPos debug3 = new BlockPos(debug0.getPosition());
/* 42 */     BlockPos debug4 = debug0.getLevel().findNearestBiome(debug2, debug3, 6400, 8);
/* 43 */     String debug5 = debug1.toString();
/* 44 */     if (debug4 == null) {
/* 45 */       throw ERROR_BIOME_NOT_FOUND.create(debug5);
/*    */     }
/* 47 */     return LocateCommand.showLocateResult(debug0, debug5, debug3, debug4, "commands.locatebiome.success");
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\LocateBiomeCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */