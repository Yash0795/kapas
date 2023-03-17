package com.kapas.util;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class AppUtils {

    private AppUtils() {

    }

    public static int generateRandomNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static <T> boolean doesObjectContainField(Class<T> clazz, String fieldName) {
        return Arrays.stream(clazz.getFields())
                .anyMatch(f -> f.getName().equals(fieldName));
    }

    public static <T extends Comparable<? super T>> List<Predicate> getPredicateList(
            List<SearchOperation<T>> searchOperationList, CriteriaBuilder builder) {
        List<Predicate> predicateList = new ArrayList<>();
        for(SearchOperation<T> searchOperation : searchOperationList) {
            Optional<Predicate> optionalPredicate = Optional.ofNullable(getPredicate(searchOperation.getOperation(),
                    searchOperation.getPath(), searchOperation.getValue(), searchOperation.getOtherValue(), builder));
            optionalPredicate.ifPresent(predicateList::add);
        }
        return predicateList;
    }

    private static <T extends Comparable<? super T>> Predicate getPredicate(SearchOperationEnum operation,
                                                                            Path<T> path, T value, T otherValue,
                                                                            CriteriaBuilder builder) {
        String valueString = null;
        Path<String> pathString = null;
        if (SearchOperationEnum.LIKE.equals(operation) || SearchOperationEnum.STARTS_WITH.equals(operation) ||
                SearchOperationEnum.ENDS_WITH.equals(operation) || SearchOperationEnum.CONTAINS.equals(operation)) {
            valueString = (String) value;
            if(StringUtils.isBlank(valueString)) {
                return null;
            }
            pathString = (Path<String>) path;
        }
        switch (operation) {
            case EQUALITY:
                return builder.equal(path, value);
            case NEGATION:
                return builder.notEqual(path, value);
            case GREATER_THAN:
                return builder.greaterThan(path, value);
            case GREATER_THAN_OR_EQUALS:
                return builder.greaterThanOrEqualTo(path, value);
            case LESS_THAN:
                return builder.lessThan(path, value);
            case LESS_THAN_OR_EQUALS:
                return builder.lessThanOrEqualTo(path, value);
            case BETWEEN:
                return builder.between(path, value, otherValue);
            case NULL:
                return builder.isNull(path);
            case NOTNULL:
                return builder.isNotNull(path);
            case IN:
                return path.in(value);
            case LIKE:
                return builder.like(pathString, valueString);
            case STARTS_WITH:
                return builder.like(pathString, valueString + "%");
            case ENDS_WITH:
                return builder.like(pathString, "%" + valueString);
            case CONTAINS:
                return builder.like(pathString, "%" + valueString + "%");
            default:
                return null;
        }
    }
}
