package be.unipartners.escqrs.cqrsquiz.projections;

import be.unipartners.escqrs.cqrsquiz.queries.QueryObject;

public interface Projection {
    Answer query(QueryObject query);
}
