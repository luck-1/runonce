package com.runonce.model.test;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ImageJsonBean implements Serializable {
    private List<String> imgs;
}
