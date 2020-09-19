package net.minecraft.server.packs.resources;

import java.io.Closeable;
import java.io.InputStream;

public interface Resource extends Closeable {
  InputStream getInputStream();
  
  String getSourceName();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\packs\resources\Resource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */