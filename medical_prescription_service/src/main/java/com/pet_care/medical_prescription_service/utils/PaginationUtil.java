package com.pet_care.medical_prescription_service.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PaginationUtil {
    public static <T> Page<T> convertListToPage(List<T> list, Pageable pageable) {
        int totalElements = list.size();
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min(start + pageable.getPageSize(), totalElements);

        List<T> pagedList = list.subList(start, end);
        return new PageImpl<>(pagedList, pageable, totalElements);
    }
}