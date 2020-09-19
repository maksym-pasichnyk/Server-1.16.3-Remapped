package org.apache.logging.log4j.core.config.builder.api;

public interface FilterableComponentBuilder<T extends ComponentBuilder<T>> extends ComponentBuilder<T> {
  T add(FilterComponentBuilder paramFilterComponentBuilder);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\logging\log4j\core\config\builder\api\FilterableComponentBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */