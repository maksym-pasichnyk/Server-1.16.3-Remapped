package net.minecraft.util.profiling;

import it.unimi.dsi.fastutil.objects.Object2LongMap;

public interface ProfilerPathEntry {
  long getDuration();
  
  long getCount();
  
  Object2LongMap<String> getCounters();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\profiling\ProfilerPathEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */