/*    */ package net.minecraft.commands.arguments;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.StringReader;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.Suggestions;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.BiFunction;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.Util;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.TranslatableComponent;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EntityAnchorArgument implements ArgumentType<EntityAnchorArgument.Anchor> {
/* 26 */   private static final Collection<String> EXAMPLES = Arrays.asList(new String[] { "eyes", "feet" }); private static final DynamicCommandExceptionType ERROR_INVALID; static {
/* 27 */     ERROR_INVALID = new DynamicCommandExceptionType(debug0 -> new TranslatableComponent("argument.anchor.invalid", new Object[] { debug0 }));
/*    */   }
/*    */   public static Anchor getAnchor(CommandContext<CommandSourceStack> debug0, String debug1) {
/* 30 */     return (Anchor)debug0.getArgument(debug1, Anchor.class);
/*    */   }
/*    */   
/*    */   public static EntityAnchorArgument anchor() {
/* 34 */     return new EntityAnchorArgument();
/*    */   }
/*    */ 
/*    */   
/*    */   public Anchor parse(StringReader debug1) throws CommandSyntaxException {
/* 39 */     int debug2 = debug1.getCursor();
/* 40 */     String debug3 = debug1.readUnquotedString();
/* 41 */     Anchor debug4 = Anchor.getByName(debug3);
/* 42 */     if (debug4 == null) {
/* 43 */       debug1.setCursor(debug2);
/* 44 */       throw ERROR_INVALID.createWithContext(debug1, debug3);
/*    */     } 
/* 46 */     return debug4;
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> debug1, SuggestionsBuilder debug2) {
/* 51 */     return SharedSuggestionProvider.suggest(Anchor.BY_NAME.keySet(), debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<String> getExamples() {
/* 56 */     return EXAMPLES;
/*    */   }
/*    */   public enum Anchor { FEET, EYES; private static final Map<String, Anchor> BY_NAME;
/*    */     static {
/* 60 */       FEET = new Anchor("FEET", 0, "feet", (debug0, debug1) -> debug0);
/* 61 */       EYES = new Anchor("EYES", 1, "eyes", (debug0, debug1) -> new Vec3(debug0.x, debug0.y + debug1.getEyeHeight(), debug0.z));
/*    */     } private final String name; private final BiFunction<Vec3, Entity, Vec3> transform;
/*    */     static {
/* 64 */       BY_NAME = (Map<String, Anchor>)Util.make(Maps.newHashMap(), debug0 -> {
/*    */             for (Anchor debug4 : values()) {
/*    */               debug0.put(debug4.name, debug4);
/*    */             }
/*    */           });
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     Anchor(String debug3, BiFunction<Vec3, Entity, Vec3> debug4) {
/* 74 */       this.name = debug3;
/* 75 */       this.transform = debug4;
/*    */     }
/*    */     
/*    */     @Nullable
/*    */     public static Anchor getByName(String debug0) {
/* 80 */       return BY_NAME.get(debug0);
/*    */     }
/*    */     
/*    */     public Vec3 apply(Entity debug1) {
/* 84 */       return this.transform.apply(debug1.position(), debug1);
/*    */     }
/*    */     
/*    */     public Vec3 apply(CommandSourceStack debug1) {
/* 88 */       Entity debug2 = debug1.getEntity();
/* 89 */       if (debug2 == null) {
/* 90 */         return debug1.getPosition();
/*    */       }
/* 92 */       return this.transform.apply(debug1.getPosition(), debug2);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\commands\arguments\EntityAnchorArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */