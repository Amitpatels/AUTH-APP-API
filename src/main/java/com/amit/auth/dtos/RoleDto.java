package com.amit.auth.dtos;

import lombok.*;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RoleDto {
    private UUID id;
    private String name;
}