package com.petcare.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchRequest
{

    List<String> fieldSearch;

    String valueSearch;

}
