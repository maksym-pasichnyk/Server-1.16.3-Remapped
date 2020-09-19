/*    */ package net.minecraft.world.level.pathfinder;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.PathNavigationRegion;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class NodeEvaluator
/*    */ {
/*    */   protected PathNavigationRegion level;
/*    */   protected Mob mob;
/* 19 */   protected final Int2ObjectMap<Node> nodes = (Int2ObjectMap<Node>)new Int2ObjectOpenHashMap();
/*    */   
/*    */   protected int entityWidth;
/*    */   
/*    */   protected int entityHeight;
/*    */   
/*    */   protected int entityDepth;
/*    */   
/*    */   protected boolean canPassDoors;
/*    */   protected boolean canOpenDoors;
/*    */   protected boolean canFloat;
/*    */   
/*    */   public void prepare(PathNavigationRegion debug1, Mob debug2) {
/* 32 */     this.level = debug1;
/* 33 */     this.mob = debug2;
/* 34 */     this.nodes.clear();
/*    */     
/* 36 */     this.entityWidth = Mth.floor(debug2.getBbWidth() + 1.0F);
/* 37 */     this.entityHeight = Mth.floor(debug2.getBbHeight() + 1.0F);
/* 38 */     this.entityDepth = Mth.floor(debug2.getBbWidth() + 1.0F);
/*    */   }
/*    */   
/*    */   public void done() {
/* 42 */     this.level = null;
/* 43 */     this.mob = null;
/*    */   }
/*    */   
/*    */   protected Node getNode(BlockPos debug1) {
/* 47 */     return getNode(debug1.getX(), debug1.getY(), debug1.getZ());
/*    */   }
/*    */   
/*    */   protected Node getNode(int debug1, int debug2, int debug3) {
/* 51 */     return (Node)this.nodes.computeIfAbsent(Node.createHash(debug1, debug2, debug3), debug3 -> new Node(debug0, debug1, debug2));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCanPassDoors(boolean debug1) {
/* 65 */     this.canPassDoors = debug1;
/*    */   }
/*    */   
/*    */   public void setCanOpenDoors(boolean debug1) {
/* 69 */     this.canOpenDoors = debug1;
/*    */   }
/*    */   
/*    */   public void setCanFloat(boolean debug1) {
/* 73 */     this.canFloat = debug1;
/*    */   }
/*    */   
/*    */   public boolean canPassDoors() {
/* 77 */     return this.canPassDoors;
/*    */   }
/*    */   
/*    */   public boolean canOpenDoors() {
/* 81 */     return this.canOpenDoors;
/*    */   }
/*    */   
/*    */   public boolean canFloat() {
/* 85 */     return this.canFloat;
/*    */   }
/*    */   
/*    */   public abstract Node getStart();
/*    */   
/*    */   public abstract Target getGoal(double paramDouble1, double paramDouble2, double paramDouble3);
/*    */   
/*    */   public abstract int getNeighbors(Node[] paramArrayOfNode, Node paramNode);
/*    */   
/*    */   public abstract BlockPathTypes getBlockPathType(BlockGetter paramBlockGetter, int paramInt1, int paramInt2, int paramInt3, Mob paramMob, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean1, boolean paramBoolean2);
/*    */   
/*    */   public abstract BlockPathTypes getBlockPathType(BlockGetter paramBlockGetter, int paramInt1, int paramInt2, int paramInt3);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\pathfinder\NodeEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */