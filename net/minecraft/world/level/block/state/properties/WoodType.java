/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ public class WoodType
/*    */ {
/*  9 */   private static final Set<WoodType> VALUES = (Set<WoodType>)new ObjectArraySet();
/*    */   
/* 11 */   public static final WoodType OAK = register(new WoodType("oak"));
/* 12 */   public static final WoodType SPRUCE = register(new WoodType("spruce"));
/* 13 */   public static final WoodType BIRCH = register(new WoodType("birch"));
/* 14 */   public static final WoodType ACACIA = register(new WoodType("acacia"));
/* 15 */   public static final WoodType JUNGLE = register(new WoodType("jungle"));
/* 16 */   public static final WoodType DARK_OAK = register(new WoodType("dark_oak"));
/* 17 */   public static final WoodType CRIMSON = register(new WoodType("crimson"));
/* 18 */   public static final WoodType WARPED = register(new WoodType("warped"));
/*    */   
/*    */   private final String name;
/*    */   
/*    */   protected WoodType(String debug1) {
/* 23 */     this.name = debug1;
/*    */   }
/*    */   
/*    */   private static WoodType register(WoodType debug0) {
/* 27 */     VALUES.add(debug0);
/* 28 */     return debug0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\state\properties\WoodType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */