/*    */ package net.minecraft.world.level.levelgen.feature.treedecorators;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class TreeDecoratorType<P extends TreeDecorator> {
/*  7 */   public static final TreeDecoratorType<TrunkVineDecorator> TRUNK_VINE = register("trunk_vine", TrunkVineDecorator.CODEC);
/*  8 */   public static final TreeDecoratorType<LeaveVineDecorator> LEAVE_VINE = register("leave_vine", LeaveVineDecorator.CODEC);
/*  9 */   public static final TreeDecoratorType<CocoaDecorator> COCOA = register("cocoa", CocoaDecorator.CODEC);
/* 10 */   public static final TreeDecoratorType<BeehiveDecorator> BEEHIVE = register("beehive", BeehiveDecorator.CODEC);
/* 11 */   public static final TreeDecoratorType<AlterGroundDecorator> ALTER_GROUND = register("alter_ground", AlterGroundDecorator.CODEC);
/*    */   
/*    */   private static <P extends TreeDecorator> TreeDecoratorType<P> register(String debug0, Codec<P> debug1) {
/* 14 */     return (TreeDecoratorType<P>)Registry.register(Registry.TREE_DECORATOR_TYPES, debug0, new TreeDecoratorType<>(debug1));
/*    */   }
/*    */   
/*    */   private final Codec<P> codec;
/*    */   
/*    */   private TreeDecoratorType(Codec<P> debug1) {
/* 20 */     this.codec = debug1;
/*    */   }
/*    */   
/*    */   public Codec<P> codec() {
/* 24 */     return this.codec;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\treedecorators\TreeDecoratorType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */