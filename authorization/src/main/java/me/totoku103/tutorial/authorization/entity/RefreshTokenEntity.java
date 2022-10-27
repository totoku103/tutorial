package me.totoku103.tutorial.authorization.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tb_refresh_token")
@Getter
@Setter
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;
    private String tokenId;
    @Column(columnDefinition = "blob")
    private byte[] token;
    @Column(columnDefinition = "blob")
    private byte[] authentication;
}
