package me.totoku103.tutorial.gatewayjwt.model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TokenUser {
    private String id;
    private Set<String> scope;
}
