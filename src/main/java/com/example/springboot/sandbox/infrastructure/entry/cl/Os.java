package com.example.springboot.sandbox.infrastructure.entry.cl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Os {
    String type;
    String version;
}
