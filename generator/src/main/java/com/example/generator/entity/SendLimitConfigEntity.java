package com.example.generator.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_send_limit_config"
//        uniqueConstraints = {
//                @UniqueConstraint(name = "tb_send_limit_config_uniqe", columnNames = {"agent_id", "channel_type"})
//        }
        )
@Getter
@Setter
public class SendLimitConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String agentId;
    private String channelType;
    private boolean active;
    private int dailyLimitCount;
    private int monthlyLimitCount;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastModifiedDate;
    private String lastModifiedBy;
}
