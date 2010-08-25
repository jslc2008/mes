package com.qcadoo.mes.core.data.internal.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import com.qcadoo.mes.core.data.api.DataAccessService;
import com.qcadoo.mes.core.data.beans.Entity;
import com.qcadoo.mes.core.data.definition.FieldTypeFactory;
import com.qcadoo.mes.core.data.definition.LookupedFieldType;
import com.qcadoo.mes.core.data.search.Order;
import com.qcadoo.mes.core.data.search.ResultSet;
import com.qcadoo.mes.core.data.search.SearchCriteriaBuilder;

public class BelongsToFieldType implements LookupedFieldType {

    private final String entityName;

    private final String[] eagerLoadingFieldNames;

    private final DataAccessService dataAccessService;

    private final String lookupFieldName;

    public BelongsToFieldType(final String entityName, final String lookupFieldName, final String[] eagerLoadingFieldNames,
            final DataAccessService dataAccessService) {
        this.entityName = entityName;
        this.lookupFieldName = lookupFieldName;
        this.eagerLoadingFieldNames = eagerLoadingFieldNames;
        this.dataAccessService = dataAccessService;
    }

    @Override
    public int getNumericType() {
        return FieldTypeFactory.NUMERIC_TYPE_BELONGS_TO;
    }

    @Override
    public boolean isSearchable() {
        return true;
    }

    @Override
    public boolean isOrderable() {
        return false;
    }

    @Override
    public boolean isAggregable() {
        return false;
    }

    @Override
    public boolean isValidType(Object value) {
        return true;
    }

    @Override
    public Map<Long, String> lookup(String prefix) {
        ResultSet resultSet = dataAccessService.find(entityName,
                SearchCriteriaBuilder.forEntity(entityName).orderBy(Order.asc(lookupFieldName)).build());
        Map<Long, String> possibleValues = new LinkedHashMap<Long, String>();

        for (Entity entity : resultSet.getResults()) {
            possibleValues.put(entity.getId(), (String) entity.getField(lookupFieldName));
        }

        return possibleValues;
    }

    public String getEntityName() {
        return entityName;
    }

    public String[] getEagerLoadingFieldNames() {
        return eagerLoadingFieldNames;
    }

}
