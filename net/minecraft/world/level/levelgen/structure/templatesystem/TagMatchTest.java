/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ import java.util.Random;
/*    */ import net.minecraft.tags.SerializationTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class TagMatchTest extends RuleTest {
/*    */   public static final Codec<TagMatchTest> CODEC;
/*    */   
/*    */   static {
/* 12 */     CODEC = Tag.codec(() -> SerializationTags.getInstance().getBlocks()).fieldOf("tag").xmap(TagMatchTest::new, debug0 -> debug0.tag).codec();
/*    */   }
/*    */   private final Tag<Block> tag;
/*    */   
/*    */   public TagMatchTest(Tag<Block> debug1) {
/* 17 */     this.tag = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean test(BlockState debug1, Random debug2) {
/* 22 */     return debug1.is(this.tag);
/*    */   }
/*    */ 
/*    */   
/*    */   protected RuleTestType<?> getType() {
/* 27 */     return RuleTestType.TAG_TEST;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\TagMatchTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */