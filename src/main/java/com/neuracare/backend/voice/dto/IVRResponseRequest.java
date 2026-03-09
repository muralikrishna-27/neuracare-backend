package com.neuracare.backend.voice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IVRResponseRequest {

    private String digits;

    private String callSid;
}