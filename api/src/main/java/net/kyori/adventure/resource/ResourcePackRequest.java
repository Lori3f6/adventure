/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2017-2023 KyoriPowered
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
package net.kyori.adventure.resource;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.builder.AbstractBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.examination.Examinable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a resource pack request that can be sent to players.
 *
 * @see Audience#sendResourcePack(ResourcePackRequest, ResourcePackRequest...)
 * @since 4.15.0
 */
public interface ResourcePackRequest extends Examinable, ResourcePackRequestLike {
  /**
   * Creates a resource pack request.
   *
   * @param id the id
   * @param uri the uri
   * @param hash the sha-1 hash
   * @param required whether the resource pack is required or not
   * @return the resource pack request
   * @since 4.15.0
   */
  static @NotNull ResourcePackRequest resourcePackRequest(final @NotNull UUID id, final @NotNull URI uri, final @NotNull String hash, final boolean required) {
    return resourcePackRequest(id, uri, hash, required, null);
  }

  /**
   * Creates a resource pack request.
   *
   * @param id the id
   * @param uri the uri
   * @param hash the sha-1 hash
   * @param required whether the resource pack is required or not
   * @param prompt the prompt
   * @return the resource pack request
   * @since 4.15.0
   */
  static @NotNull ResourcePackRequest resourcePackRequest(final @NotNull UUID id, final @NotNull URI uri, final @NotNull String hash, final boolean required, final @Nullable Component prompt) {
    return new ResourcePackRequestImpl(id, uri, hash, required, prompt);
  }

  /**
   * Create a new builder that will create a {@link ResourcePackRequest}.
   *
   * @return a builder
   * @since 4.15.0
   */
  static @NotNull Builder resourcePackRequest() {
    return new ResourcePackRequestImpl.BuilderImpl();
  }

  /**
   * Gets the id.
   *
   * @return the id
   * @since 4.15.0
   */
  @NotNull UUID id();

  /**
   * Gets the uri.
   *
   * @return the uri
   * @since 4.15.0
   */
  @NotNull URI uri();

  /**
   * Gets the hash.
   *
   * @return the hash
   * @since 4.15.0
   */
  @NotNull String hash();

  /**
   * Gets whether the resource pack is required
   * or not.
   *
   * @return True if the resource pack is required,
   *     false otherwise
   * @since 4.15.0
   */
  boolean required();

  /**
   * Gets the prompt.
   *
   * @return the prompt
   * @since 4.15.0
   */
  @Nullable Component prompt();

  @Override
  default @NotNull ResourcePackRequest asResourcePackRequest() {
    return this;
  }

  /**
   * A builder for resource pack requests.
   *
   * @since 4.15.0
   */
  interface Builder extends AbstractBuilder<ResourcePackRequest>, ResourcePackRequestLike {
    /**
     * Sets the id.
     *
     * @param id the id
     * @return this builder
     * @since 4.15.0
     */
    @Contract("_ -> this")
    @NotNull Builder id(final @NotNull UUID id);

    /**
     * Sets the uri.
     *
     * <p>If no UUID has been provided, setting a URL will set the ID to one based on the URL.</p>
     *
     * <p>This parameter is required.</p>
     *
     * @param uri the uri
     * @return this builder
     * @since 4.15.0
     */
    @Contract("_ -> this")
    @NotNull Builder uri(final @NotNull URI uri);

    /**
     * Sets the hash.
     *
     * @param hash the hash
     * @return this builder
     * @since 4.15.0
     */
    @Contract("_ -> this")
    @NotNull Builder hash(final @NotNull String hash);

    /**
     * Sets whether the resource pack is required or not.
     *
     * @param required whether the resource pack is required or not
     * @return this builder
     * @since 4.15.0
     */
    @Contract("_ -> this")
    @NotNull Builder required(final boolean required);

    /**
     * Sets the prompt.
     *
     * @param prompt the prompt
     * @return this builder
     * @since 4.15.0
     */
    @Contract("_ -> this")
    @NotNull Builder prompt(final @Nullable Component prompt);

    /**
     * Builds.
     *
     * @return a new resource pack request
     * @since 4.15.0
     */
    @Override
    @NotNull ResourcePackRequest build();

    /**
     * Builds, computing a hash based on the provided URL.
     *
     * <p>The hash computation will perform a network request asynchronously, exposing the built request via the returned future.</p>
     *
     * @return a future providing the new resource pack request
     * @since 4.15.0
     */
    default @NotNull CompletableFuture<ResourcePackRequest> computeHashAndBuild() {
      return this.computeHashAndBuild(ForkJoinPool.commonPool());
    }

    /**
     * Builds, computing a hash based on the provided URL.
     *
     * <p>The hash computation will perform a network request asynchronously, exposing the built request via the returned future.</p>
     *
     * @param executor the executor to perform the hash computation on
     * @return a future providing the new resource pack request
     * @since 4.15.0
     */
    @NotNull CompletableFuture<ResourcePackRequest> computeHashAndBuild(final @NotNull Executor executor);

    @Override
    default @NotNull ResourcePackRequest asResourcePackRequest() {
      return this.build();
    }
  }
}
