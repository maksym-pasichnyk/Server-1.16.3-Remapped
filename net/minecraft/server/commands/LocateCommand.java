/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Map;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.network.chat.HoverEvent;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*    */ 
/*    */ public class LocateCommand {
/* 24 */   private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.locate.failed"));
/*    */ 
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> debug0) {
/* 28 */     LiteralArgumentBuilder<CommandSourceStack> debug1 = (LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("locate").requires(debug0 -> debug0.hasPermission(2));
/*    */     
/* 30 */     for (Map.Entry<String, StructureFeature<?>> debug3 : (Iterable<Map.Entry<String, StructureFeature<?>>>)StructureFeature.STRUCTURES_REGISTRY.entrySet()) {
/* 31 */       debug1 = (LiteralArgumentBuilder<CommandSourceStack>)debug1.then(Commands.literal(debug3.getKey()).executes(debug1 -> locate((CommandSourceStack)debug1.getSource(), (StructureFeature)debug0.getValue())));
/*    */     }
/*    */     
/* 34 */     debug0.register(debug1);
/*    */   }
/*    */   
/*    */   private static int locate(CommandSourceStack debug0, StructureFeature<?> debug1) throws CommandSyntaxException {
/* 38 */     BlockPos debug2 = new BlockPos(debug0.getPosition());
/* 39 */     BlockPos debug3 = debug0.getLevel().findNearestMapFeature(debug1, debug2, 100, false);
/* 40 */     if (debug3 == null) {
/* 41 */       throw ERROR_FAILED.create();
/*    */     }
/*    */     
/* 44 */     return showLocateResult(debug0, debug1.getFeatureName(), debug2, debug3, "commands.locate.success");
/*    */   }
/*    */   
/*    */   public static int showLocateResult(CommandSourceStack debug0, String debug1, BlockPos debug2, BlockPos debug3, String debug4) {
/* 48 */     int debug5 = Mth.floor(dist(debug2.getX(), debug2.getZ(), debug3.getX(), debug3.getZ()));
/* 49 */     MutableComponent mutableComponent = ComponentUtils.wrapInSquareBrackets((Component)new TranslatableComponent("chat.coordinates", new Object[] { Integer.valueOf(debug3.getX()), "~", Integer.valueOf(debug3.getZ()) })).withStyle(debug1 -> debug1.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp @s " + debug0.getX() + " ~ " + debug0.getZ())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("chat.coordinates.tooltip"))));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 54 */     debug0.sendSuccess((Component)new TranslatableComponent(debug4, new Object[] { debug1, mutableComponent, Integer.valueOf(debug5) }), false);
/*    */     
/* 56 */     return debug5;
/*    */   }
/*    */   
/*    */   private static float dist(int debug0, int debug1, int debug2, int debug3) {
/* 60 */     int debug4 = debug2 - debug0;
/* 61 */     int debug5 = debug3 - debug1;
/* 62 */     return Mth.sqrt((debug4 * debug4 + debug5 * debug5));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\commands\LocateCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */