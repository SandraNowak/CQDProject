package com.cqdTaskProcessing.CQD.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Result {
    private int position;
    private int typos;
}
