package com.runonce.model.http;

import lombok.Data;
import java.util.List;

@Data
public class SituationToGuideAddChildrenParam {
    private String pid;
    private List<String> childId;
}