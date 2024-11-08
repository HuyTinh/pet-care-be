package com.petcare.utils;

import com.petcare.entity.Pet;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PetSpecification
{


    public static String[] LIST_SEARCH = {
            "id",
            "customerName",
            "petName",
            "species",
            "age",
            "weight"
    };

    public static Specification<Pet> findPetByAny(List<String> fieldSearch, String valueSearch) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            String[] nameSplit = valueSearch.trim().split(" ");
            String firstName = null;
            String lastName = null;
            if(nameSplit.length > 1) {
                firstName = nameSplit[nameSplit.length - 1];
//                lastName = nameSplit[nameSplit.length - 1];
                lastName = String.join(" ", Arrays.copyOfRange(nameSplit, 0, nameSplit.length - 1));
//                firstName = String.join(" ", Arrays.copyOfRange(nameSplit, 0, nameSplit.length - 1));
                System.out.println("fistname: " + firstName);
                System.out.println("lastName: " + lastName);
            }
            else{
                firstName = nameSplit[0];
            }

            for (String field : fieldSearch) {
                if(field.equals("customerName"))
                {
                    Join<Object, Object> ownerJoin = root.join("owner");
                    Predicate firstnamePredicate = criteriaBuilder.like(
                            criteriaBuilder.lower(ownerJoin.get("firstName")),
                            "%" + firstName + "%"
                    );
                    Predicate lastnamePredicate = criteriaBuilder.like(
                            criteriaBuilder.lower(ownerJoin.get("lastName")),
                            "%" + lastName + "%"
                    );
                    predicates.add(criteriaBuilder.or(firstnamePredicate, lastnamePredicate));                }
                else if(field.equals("petName"))
                {
                    predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + valueSearch + "%"));
                }
                else if(field.equals("species"))
                {
                    Join<Object, Object> speciesJoin = root.join("species");
                    predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + valueSearch + "%"));
                }
                else{
                    predicates.add(criteriaBuilder.like(root.get(field).as(String.class), "%" + valueSearch + "%"));
                }
            }

            return predicates.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean isValidField(String field) {
        for (String validField : LIST_SEARCH) {
            if (validField.equals(field)) {
                return true;
            }
        }
        return false;
    }
}
