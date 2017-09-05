package com.example.springboot.sandbox.infrastructure.entry.cl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Device {
    String type;
    String version;
}
