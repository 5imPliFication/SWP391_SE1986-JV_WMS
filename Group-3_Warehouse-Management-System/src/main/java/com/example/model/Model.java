package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model {

    private Long id;

    private String name;

    private boolean isActive;

    private String brandName;

}
