/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.sql.parser.relation.segment.select.projection.engine;

import org.apache.shardingsphere.sql.parser.core.constant.AggregationType;
import org.apache.shardingsphere.sql.parser.relation.metadata.RelationMetas;
import org.apache.shardingsphere.sql.parser.relation.segment.select.projection.Projection;
import org.apache.shardingsphere.sql.parser.relation.segment.select.projection.impl.AggregationDistinctProjection;
import org.apache.shardingsphere.sql.parser.relation.segment.select.projection.impl.AggregationProjection;
import org.apache.shardingsphere.sql.parser.relation.segment.select.projection.impl.ColumnProjection;
import org.apache.shardingsphere.sql.parser.relation.segment.select.projection.impl.ExpressionProjection;
import org.apache.shardingsphere.sql.parser.relation.segment.select.projection.impl.ShorthandProjection;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.column.ColumnSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.item.AggregationDistinctProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.item.AggregationProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.item.ColumnProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.item.ExpressionProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.item.ShorthandProjectionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.generic.AliasSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.generic.OwnerSegment;
import org.apache.shardingsphere.sql.parser.sql.value.identifier.IdentifierValue;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public final class ProjectionEngineTest {
    
    @Test
    public void assertCreateProjectionWhenProjectionSegmentNotMatched() {
        assertFalse(new ProjectionEngine(mock(RelationMetas.class)).createProjection(null, Collections.emptyList(), null).isPresent());
    }
    
    @Test
    public void assertCreateProjectionWhenProjectionSegmentInstanceOfShorthandProjectionSegment() {
        ShorthandProjectionSegment shorthandProjectionSegment = new ShorthandProjectionSegment(0, 0);
        shorthandProjectionSegment.setOwner(new OwnerSegment(0, 0, new IdentifierValue("tbl")));
        Optional<Projection> actual = new ProjectionEngine(mock(RelationMetas.class)).createProjection(null, Collections.emptyList(), shorthandProjectionSegment);
        assertTrue(actual.isPresent());
        assertThat(actual.get(), instanceOf(ShorthandProjection.class));
    }
    
    @Test
    public void assertCreateProjectionWhenProjectionSegmentInstanceOfColumnProjectionSegment() {
        ColumnProjectionSegment columnProjectionSegment = new ColumnProjectionSegment(new ColumnSegment(0, 10, new IdentifierValue("name")));
        columnProjectionSegment.setAlias(new AliasSegment(0, 0, new IdentifierValue("alias")));
        Optional<Projection> actual = new ProjectionEngine(mock(RelationMetas.class)).createProjection(null, Collections.emptyList(), columnProjectionSegment);
        assertTrue(actual.isPresent());
        assertThat(actual.get(), instanceOf(ColumnProjection.class));
    }

    @Test
    public void assertCreateProjectionWhenProjectionSegmentInstanceOfExpressionProjectionSegment() {
        ExpressionProjectionSegment expressionProjectionSegment = new ExpressionProjectionSegment(0, 10, "text");
        Optional<Projection> actual = new ProjectionEngine(mock(RelationMetas.class)).createProjection(null, Collections.emptyList(), expressionProjectionSegment);
        assertTrue(actual.isPresent());
        assertThat(actual.get(), instanceOf(ExpressionProjection.class));
    }
    
    @Test
    public void assertCreateProjectionWhenProjectionSegmentInstanceOfAggregationDistinctProjectionSegment() {
        AggregationDistinctProjectionSegment aggregationDistinctProjectionSegment = new AggregationDistinctProjectionSegment(0, 10, AggregationType.COUNT, 0, "distinctExpression");
        Optional<Projection> actual = new ProjectionEngine(mock(RelationMetas.class)).createProjection("select count(1) from table_1", Collections.emptyList(), aggregationDistinctProjectionSegment);
        assertTrue(actual.isPresent());
        assertThat(actual.get(), instanceOf(AggregationDistinctProjection.class));
    }
    
    @Test
    public void assertCreateProjectionWhenProjectionSegmentInstanceOfAggregationProjectionSegment() {
        AggregationProjectionSegment aggregationProjectionSegment = new AggregationProjectionSegment(0, 10, AggregationType.COUNT, 0);
        Optional<Projection> actual = new ProjectionEngine(mock(RelationMetas.class)).createProjection("select count(1) from table_1", Collections.emptyList(), aggregationProjectionSegment);
        assertTrue(actual.isPresent());
        assertThat(actual.get(), instanceOf(AggregationProjection.class));
    }
    
    @Test
    public void assertCreateProjectionWhenProjectionSegmentInstanceOfAggregationDistinctProjectionSegmentAndAggregationTypeIsAvg() {
        AggregationDistinctProjectionSegment aggregationDistinctProjectionSegment = new AggregationDistinctProjectionSegment(0, 10, AggregationType.AVG, 0, "distinctExpression");
        Optional<Projection> actual = new ProjectionEngine(mock(RelationMetas.class)).createProjection("select count(1) from table_1", Collections.emptyList(), aggregationDistinctProjectionSegment);
        assertTrue(actual.isPresent());
        assertThat(actual.get(), instanceOf(AggregationDistinctProjection.class));
    }
    
    @Test
    public void assertCreateProjectionWhenProjectionSegmentInstanceOfAggregationProjectionSegmentAndAggregationTypeIsAvg() {
        AggregationProjectionSegment aggregationProjectionSegment = new AggregationProjectionSegment(0, 10, AggregationType.AVG, 0);
        Optional<Projection> actual = new ProjectionEngine(mock(RelationMetas.class)).createProjection("select count(1) from table_1", Collections.emptyList(), aggregationProjectionSegment);
        assertTrue(actual.isPresent());
        assertThat(actual.get(), instanceOf(AggregationProjection.class));
    }
    
}