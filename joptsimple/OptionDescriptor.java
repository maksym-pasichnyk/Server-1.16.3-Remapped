package joptsimple;

import java.util.List;

public interface OptionDescriptor {
  List<String> options();
  
  String description();
  
  List<?> defaultValues();
  
  boolean isRequired();
  
  boolean acceptsArguments();
  
  boolean requiresArgument();
  
  String argumentDescription();
  
  String argumentTypeIndicator();
  
  boolean representsNonOptions();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\joptsimple\OptionDescriptor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */