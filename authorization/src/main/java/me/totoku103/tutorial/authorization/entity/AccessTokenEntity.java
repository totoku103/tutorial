package me.totoku103.tutorial.authorization.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "oauth_access_token")
@Getter
@Setter
public class AccessTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;
    private String tokenId;
    @Column(columnDefinition = "blob")
    private String token;
    private String authenticationId;
    private String userName;
    private String clientId;
    @Column(columnDefinition = "blob")
    private String authentication;
    private String refreshToken;


}
