package me.totoku103.tutorial.authorizationold.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tb_web_admin_user")
@Getter
@Setter
public class WebAdminUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userLogin;
    private String password;
}
