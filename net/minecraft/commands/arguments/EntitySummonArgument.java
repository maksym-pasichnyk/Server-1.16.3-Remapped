/*    */ package net.minecraft.commands.arguments;
/*    */ 
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.resources.ResourceLocation;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ 
/*    */ public class EntitySummonArgument implements ArgumentType<ResourceLocation> {
/* 18 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "minecraft:pig", "cow" }); static {
/* 19 */     ERROR_UNKNOWN_ENTITY = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("entity.notFound", new Object[] { debug0 }));
/*    */   }
/*    */   
/*    */   public static final DynamicCommandExceptionType ERROR_UNKNOWN_ENTITY;
/*    */   
/*    */   public static EntitySummonArgument id() {
/* 25 */     return new EntitySummonArgument();
/*    */   }
/*    */   
/*    */   public static ResourceLocation getSummonableEntity(CommandContext<CommandSourceStack> debug0, String debug1) throws CommandSyntaxException {
/* 29 */     return verifyCanSummon((ResourceLocation)debug0.getArgument(debug1, ResourceLocation.class));
/*    */   }
/*    */   
/*    */   private static ResourceLocation verifyCanSummon(ResourceLocation debug0) throws CommandSyntaxException {
/* 33 */     Registry.ENTITY_TYPE.getOptional(debug0).filter(EntityType::canSummon).orElseThrow(() -> ERROR_UNKNOWN_ENTITY.create(debug0));
/* 34 */     return debug0;
/*    */   }
/*    */ 
/*    */   
/*    */   public ResourceLocation parse(StringReader debug1) throws CommandSyntaxException {
/* 39 */     return verifyCanSummon(ResourceLocation.read(debug1));
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 44 */     return EXAMPLES;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\EntitySummonArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */