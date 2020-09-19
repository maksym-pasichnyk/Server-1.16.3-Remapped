package net.minecraft.server.packs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;

public interface PackResources extends AutoCloseable {
  InputStream getResource(PackType paramPackType, ResourceLocation paramResourceLocation) throws IOException;
  
  Collection<ResourceLocation> getResources(PackType paramPackType, String paramString1, String paramString2, int paramInt, Predicate<String> paramPredicate);
  
  boolean hasResource(PackType paramPackType, ResourceLocation paramResourceLocation);
  
  Set<String> getNamespaces(PackType paramPackType);
  
  @Nullable
  <T> T getMetadataSection(MetadataSectionSerializer<T> paramMetadataSectionSerializer) throws IOException;
  
  String getName();
  
  void close();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\PackResources.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */