package com.codexdrive.electronic.store.helper;

import com.codexdrive.electronic.store.dtos.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    private static final ModelMapper mapper = new ModelMapper();

    public static <U, V> PageableResponse<V> getPageableResponse(
            Page<U> pageData,
            Class<V> type) {

        List<V> dtoList = pageData.getContent()
                .stream()
                .map(object -> mapper.map(object, type))
                .collect(Collectors.toList());

        PageableResponse<V> response = new PageableResponse<>();

        response.setContent(dtoList);
        response.setPageNumber(pageData.getNumber());
        response.setPageSize(pageData.getSize());
        response.setTotalElements(pageData.getTotalElements());
        response.setTotalPages(pageData.getTotalPages());
        response.setLastPage(pageData.isLast());

        return response;
    }
}