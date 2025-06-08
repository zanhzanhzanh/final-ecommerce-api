package org.tdtu.ecommerceapi.config;

import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Configuration
@EnableMongoAuditing(
        auditorAwareRef = "auditorAware",
        dateTimeProviderRef = "auditingDateTimeProvider")
public class AuditingConfig {
    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return new SecurityAuditorAware();
    }

    static class SecurityAuditorAware implements AuditorAware<String> {
        @Override
        public Optional<String> getCurrentAuditor() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
                return Optional.of("system");
            }
            return Optional.ofNullable(authentication.getName());
        }
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new MongoOffsetDateTimeWriter(),
                new MongoOffsetDateTimeReader()
        ));
    }

    static class MongoOffsetDateTimeReader implements Converter<Document, OffsetDateTime> {
        @Override
        public OffsetDateTime convert(final Document document) {
            final Date dateTime = document.getDate(MongoOffsetDateTimeWriter.DATE_FIELD);
            final ZoneOffset offset = ZoneOffset.of(document.getString(MongoOffsetDateTimeWriter.OFFSET_FIELD));
            return OffsetDateTime.ofInstant(dateTime.toInstant(), offset);
        }

    }

    static class MongoOffsetDateTimeWriter implements Converter<OffsetDateTime, Document> {
        public static final String OFFSET_DATE_FIELD = "offsetDateTime";
        public static final String DATE_FIELD = "dateTime";
        public static final String OFFSET_FIELD = "offset";

        @Override
        public Document convert(final OffsetDateTime offsetDateTime) {
            final Document document = new Document();
            document.put(OFFSET_DATE_FIELD, offsetDateTime.toString());
            document.put(DATE_FIELD, Date.from(offsetDateTime.toInstant()));
            document.put(OFFSET_FIELD, offsetDateTime.getOffset().toString());
            return document;
        }

    }
}
