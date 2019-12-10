package be.unipartners.escqrs.cqrsquiz.domain.utils;

import java.util.UUID;

public class AggregateIdGenerator {
    private AggregateIdGenerator() {
    }

    public static String generateAggragateId(UUID id, Class clazz) {
        return clazz.getSimpleName() + "#" + id.toString();
    }
}
