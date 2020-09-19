package net.minecraft.world.level.chunk;

import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;

public interface FeatureAccess {
  @Nullable
  StructureStart<?> getStartForFeature(StructureFeature<?> paramStructureFeature);
  
  void setStartForFeature(StructureFeature<?> paramStructureFeature, StructureStart<?> paramStructureStart);
  
  LongSet getReferencesForFeature(StructureFeature<?> paramStructureFeature);
  
  void addReferenceForFeature(StructureFeature<?> paramStructureFeature, long paramLong);
  
  Map<StructureFeature<?>, LongSet> getAllReferences();
  
  void setAllReferences(Map<StructureFeature<?>, LongSet> paramMap);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\FeatureAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */