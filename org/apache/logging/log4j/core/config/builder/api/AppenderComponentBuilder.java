package org.apache.logging.log4j.core.config.builder.api;

public interface AppenderComponentBuilder extends FilterableComponentBuilder<AppenderComponentBuilder> {
  AppenderComponentBuilder add(LayoutComponentBuilder paramLayoutComponentBuilder);
  
  String getName();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\builder\api\AppenderComponentBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */