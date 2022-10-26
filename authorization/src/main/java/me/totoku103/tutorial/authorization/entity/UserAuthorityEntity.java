package me.totoku103.tutorial.authorization.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tb_web_user_author")
@Getter
@Setter
public class UserAuthorityEntity {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idx;
    private String userId;
    private String level;
    private String authority;
    private boolean authBill;
    private boolean authOperation;
    private boolean authUser;
    private boolean authManage;
}
