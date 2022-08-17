package com.datastax.tutorials.config;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.context.DriverContext;
import com.datastax.oss.driver.api.core.cql.AsyncResultSet;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Statement;
import com.datastax.oss.driver.api.core.metadata.Metadata;
import com.datastax.oss.driver.api.core.metrics.Metrics;
import com.datastax.oss.driver.api.core.session.Request;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapSetter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class OtelCqlSession implements CqlSession {
    private static final TextMapSetter<Map<String, ByteBuffer>> contextSetter = (carrier, key, value) -> {
        if (carrier != null) {
            carrier.put(key, ByteBuffer.wrap(value.getBytes(StandardCharsets.UTF_8)));
        }
    };

    private final CqlSession delegate;

    public OtelCqlSession(CqlSession delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    @NonNull
    public String getName() {
        return delegate.getName();
    }

    @Override
    @NonNull
    public Metadata getMetadata() {
        return delegate.getMetadata();
    }

    @Override
    public boolean isSchemaMetadataEnabled() {
        return delegate.isSchemaMetadataEnabled();
    }

    @Override
    @NonNull
    public CompletionStage<Metadata> setSchemaMetadataEnabled(@Nullable Boolean aBoolean) {
        return delegate.setSchemaMetadataEnabled(aBoolean);
    }

    @Override
    @NonNull
    public CompletionStage<Metadata> refreshSchemaAsync() {
        return delegate.refreshSchemaAsync();
    }

    @Override
    @NonNull
    public CompletionStage<Boolean> checkSchemaAgreementAsync() {
        return delegate.checkSchemaAgreementAsync();
    }

    @Override
    @NonNull
    public DriverContext getContext() {
        return delegate.getContext();
    }

    @Override
    @NonNull
    public Optional<CqlIdentifier> getKeyspace() {
        return delegate.getKeyspace();
    }

    @Override
    @NonNull
    public Optional<Metrics> getMetrics() {
        return delegate.getMetrics();
    }

    @Override
    @Nullable
    public <RequestT extends Request, ResultT> ResultT execute(@NonNull RequestT requestT, @NonNull GenericType<ResultT> genericType) {
        return delegate.execute(requestT, genericType);
    }

    @Override
    @NonNull
    public ResultSet execute(@NonNull Statement<?> statement) {
        Map<String, ByteBuffer> payload = new HashMap<>();
        W3CTraceContextPropagator.getInstance().inject(Context.current(), payload, contextSetter);
        Statement<?> injected = statement.setCustomPayload(payload);
        return delegate.execute(injected);
    }

    @Override
    @NonNull
    public CompletionStage<AsyncResultSet> executeAsync(@NonNull Statement<?> statement) {
        Map<String, ByteBuffer> payload = new HashMap<>();
        W3CTraceContextPropagator.getInstance().inject(Context.current(), payload, contextSetter);
        Statement<?> injected = statement.setCustomPayload(payload);
        return delegate.executeAsync(injected);
    }

    @Override
    @NonNull
    public ReactiveResultSet executeReactive(@NonNull Statement<?> statement) {
        Map<String, ByteBuffer> payload = new HashMap<>();
        W3CTraceContextPropagator.getInstance().inject(Context.current(), payload, contextSetter);
        Statement<?> injected = statement.setCustomPayload(payload);
        return delegate.executeReactive(injected);
    }

    @Override
    @NonNull
    public CompletionStage<Void> closeFuture() {
        return delegate.closeFuture();
    }

    @Override
    @NonNull
    public CompletionStage<Void> closeAsync() {
        return delegate.closeAsync();
    }

    @Override
    @NonNull
    public CompletionStage<Void> forceCloseAsync() {
        return delegate.forceCloseAsync();
    }
}
