/*
 * This file is part of text, licensed under the MIT License.
 *
 * Copyright (c) 2017 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.text;

import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A text component.
 */
public interface Component {

  /**
   * An empty, unmodifiable, list of components.
   */
  List<Component> EMPTY_COMPONENT_LIST = Collections.emptyList();

  /**
   * Gets the unmodifiable list of children.
   *
   * @return the unmodifiable list of children
   */
  @Nonnull
  List<Component> children();

  /**
   * Checks if this component contains a component.
   *
   * @param that the other component
   * @return {@code true} if this component contains the provided
   *     component, {@code false} otherwise
   */
  default boolean contains(@Nonnull final Component that) {
    if(this == that) return true;
    for(final Component child : this.children()) {
      if(child.contains(that)) return true;
    }
    if(this.hoverEvent() != null) {
      final Component hover = this.hoverEvent().value();
      if(that == hover) return true;
      for(final Component child : hover.children()) {
        if(child.contains(that)) return true;
      }
    }
    return false;
  }

  /**
   * Prevents a cycle between this component and the provided component.
   *
   * @param that the other component
   */
  default void detectCycle(@Nonnull final Component that) {
    if(that.contains(this)) {
      throw new IllegalStateException("Component cycle detected between " + this + " and " + that);
    }
  }

  /**
   * Appends a component to this component.
   *
   * @param component the component to append
   * @return a component with the component added
   */
  @Nonnull
  Component append(@Nonnull final Component component);

  /**
   * Creates a component.
   *
   * @return a component
   */
  @Nonnull
  Component copy();

  /**
   * Gets the color of this component.
   *
   * @return the color of this component
   */
  @Nullable
  TextColor color();

  /**
   * Sets the color of this component.
   *
   * @param color the color
   * @return a component
   */
  @Nonnull
  Component color(@Nullable final TextColor color);

  /**
   * Tests if this component has a decoration.
   *
   * @param decoration the decoration
   * @return {@code true} if this component has the decoration, {@code false} if this
   *     component does not have the decoration
   */
  default boolean hasDecoration(@Nonnull final TextDecoration decoration) {
    return this.decoration(decoration) == TextDecoration.State.TRUE;
  }

  /**
   * Gets the state of a decoration on this component.
   *
   * @param decoration the decoration
   * @return {@link TextDecoration.State#TRUE} if this component has the decoration,
   *     {@link TextDecoration.State#FALSE} if this component does not have the decoration,
   *     and {@link TextDecoration.State#NOT_SET} if not set
   */
  @Nonnull
  TextDecoration.State decoration(@Nonnull final TextDecoration decoration);

  /**
   * Sets the state of a decoration on this component.
   *
   * @param decoration the decoration
   * @param flag {@code true} if this component should have the decoration, {@code false} if
   *     this component should not have the decoration
   * @return a component
   */
  @Nonnull
  default Component decoration(@Nonnull final TextDecoration decoration, final boolean flag) {
    return this.decoration(decoration, TextDecoration.State.byBoolean(flag));
  }

  /**
   * Sets the value of a decoration on this component.
   *
   * @param decoration the decoration
   * @param state {@link TextDecoration.State#TRUE} if this component should have the
   *     decoration, {@link TextDecoration.State#FALSE} if this component should not
   *     have the decoration, and {@link TextDecoration.State#NOT_SET} if the decoration
   *     should not have a set value
   * @return a component
   */
  @Nonnull
  Component decoration(@Nonnull final TextDecoration decoration, @Nonnull final TextDecoration.State state);

  /**
   * Gets a set of decorations this component has.
   *
   * @return a set of decorations this component has
   */
  @Nonnull
  default Set<TextDecoration> decorations() {
    return this.decorations(Collections.emptySet());
  }

  /**
   * Gets a set of decorations this component has.
   *
   * @param defaultValues a set of default values
   * @return a set of decorations this component has
   */
  @Nonnull
  default Set<TextDecoration> decorations(@Nonnull final Set<TextDecoration> defaultValues) {
    final Set<TextDecoration> decorations = EnumSet.noneOf(TextDecoration.class);
    for(final TextDecoration decoration : TextDecoration.values()) {
      final TextDecoration.State value = this.decoration(decoration);
      if(value == TextDecoration.State.TRUE || (value == TextDecoration.State.NOT_SET && defaultValues.contains(decoration))) {
        decorations.add(decoration);
      }
    }
    return decorations;
  }

  /**
   * Gets the click event of this component.
   *
   * @return the click event
   */
  @Nullable
  ClickEvent clickEvent();

  /**
   * Sets the click event of this component.
   *
   * @param event the click event
   * @return a component
   */
  @Nonnull
  Component clickEvent(@Nullable final ClickEvent event);

  /**
   * Gets the hover event of this component.
   *
   * @return the hover event
   */
  @Nullable
  HoverEvent hoverEvent();

  /**
   * Sets the hover event of this component.
   *
   * @param event the hover event
   * @return a component
   */
  @Nonnull
  Component hoverEvent(@Nullable final HoverEvent event);

  /**
   * Gets the string to be inserted when this component is shift-clicked.
   *
   * @return the insertion string
   */
  @Nullable
  String insertion();

  /**
   * Sets the string to be inserted when this component is shift-clicked.
   *
   * @param insertion the insertion string
   * @return a component
   */
  @Nonnull
  Component insertion(@Nullable final String insertion);

  /**
   * Merges styling from another component into this component.
   *
   * @param that the other component
   * @return a component
   */
  @Nonnull
  Component mergeStyle(@Nonnull final Component that);

  /**
   * Merges the color from another component into this component.
   *
   * @param that the other component
   * @return a component
   */
  @Nonnull
  Component mergeColor(@Nonnull final Component that);

  /**
   * Merges the decorations from another component into this component.
   *
   * @param that the other component
   * @return a component
   */
  @Nonnull
  Component mergeDecorations(@Nonnull final Component that);

  /**
   * Merges the events from another component into this component.
   *
   * @param that the other component
   * @return a component
   */
  @Nonnull
  Component mergeEvents(@Nonnull final Component that);

  /**
   * Resets all styling on this component.
   *
   * @return a component
   */
  @Nonnull
  Component resetStyle();

  /**
   * Tests if this component has any styling.
   *
   * @return {@code true} if this component has any styling, {@code false} if this
   *     component does not have any styling
   */
  boolean hasStyling();

  /**
   * Creates a text component with the content of {@link String#valueOf(boolean)}.
   *
   * @param value the boolean value
   * @return the component
   */
  @Nonnull
  static Component of(final boolean value) {
    return TextComponent.of(String.valueOf(value));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(char)}.
   *
   * @param value the char value
   * @return the component
   */
  @Nonnull
  static Component of(final char value) {
    return TextComponent.of(String.valueOf(value));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(double)}.
   *
   * @param value the double value
   * @return the component
   */
  @Nonnull
  static Component of(final double value) {
    return TextComponent.of(String.valueOf(value));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(float)}.
   *
   * @param value the float value
   * @return the component
   */
  @Nonnull
  static Component of(final float value) {
    return TextComponent.of(String.valueOf(value));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(int)}.
   *
   * @param value the int value
   * @return the component
   */
  @Nonnull
  static Component of(final int value) {
    return TextComponent.of(String.valueOf(value));
  }

  /**
   * Creates a text component with the content of {@link String#valueOf(long)}.
   *
   * @param value the long value
   * @return the component
   */
  @Nonnull
  static Component of(final long value) {
    return TextComponent.of(String.valueOf(value));
  }
}
