package com.example.springboot.sandbox.infrastructure.entry.cl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent {
    Device device;
    Os os;
}
