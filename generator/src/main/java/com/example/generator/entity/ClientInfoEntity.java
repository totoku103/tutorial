package com.example.generator.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TB_CLIENT_INFO", indexes = {
        @Index(name = "idx_client_info_01", columnList = "USER_ID"),
        @Index(name = "idx_client_info_02", columnList = "AGENT_ID")})
public class ClientInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "USER_ID", nullable = false)
    private long userId;

    @Column(name = "AGENT_ID", nullable = false, length = 45)
    private String agentId;

    @Column(name = "AGENT_PASSWORD", nullable = false, length = 45)
    private String agentPassword;

    @Column(name = "AGENT_TYPE", length = 10)
    private String agentType;

    @Column(name = "AUTH_KEY", length = 64)
    private String authKey;

    @Column(name = "MASTER_HOST", nullable = false, length = 20)
    private String masterHost;

    @Column(name = "MASTER_SEND_PORT", nullable = false, length = 5)
    private int masterSendPort;

    @Column(name = "MASTER_REPORT_PORT", nullable = false, length = 5)
    private int masterReportPort;

    @Column(name = "MASTER_SENDLINE_COUNT", columnDefinition = "INT(2) DEFAULT 0", nullable = false)
    private int masterSendlineCount = 0;

    @Column(name = "MASTER_REPORTLINE_COUNT", columnDefinition = "INT(2) DEFAULT 0", nullable = false)
    private int masterReportlineCount = 0;

    @Column(name = "SLAVE_HOST", nullable = false, length = 20)
    private String slaveHost;

    @Column(name = "SLAVE_SEND_PORT", nullable = false, length = 5)
    private int slaveSendPort;

    @Column(name = "SLAVE_REPORT_PORT", nullable = false, length = 5)
    private int slaveReportPort;

    @Column(name = "SLAVE_SENDLINE_COUNT", columnDefinition = "INT(2) DEFAULT 0", nullable = false)
    private int slaveSendlineCount = 0;

    @Column(name = "SLAVE_REPORTLINE_COUNT", columnDefinition = "INT(2) DEFAULT 0", nullable = false)
    private int slaveReportlineCount = 0;

    @Column(name = "CREATE_AT", nullable = false, length = 19)
    private String createAt;

    @Column(name = "MODIFIED_AT", nullable = false, length = 19)
    private String modifiedAt;

    @Column(name = "USE_YN", columnDefinition = "VARCHAR(1) DEFAULT 'Y'", nullable = false)
    private String useYn = "Y";

    @Column(name = "ENC_METHOD", columnDefinition = "VARCHAR(10) DEFAULT 'NONE'", nullable = false)
    private String encMethod;

    @Column(name = "ENC_KEY", nullable = false, length = 64)
    private String encKey;

    @Column(name = "REPORT_CONSUMER_COUNT", columnDefinition = "INT(2) DEFAULT 1", nullable = false)
    private int reportConsumerCount;

    @Column(name = "USE_AT_BIZ_MSG", columnDefinition = "CHAR(1) DEFAULT 'N'", nullable = false)
    private String useAtBizMsg = "N";

    @Column(name = "USE_FT_BIZ_MSG", columnDefinition = "CHAR(1) DEFAULT 'N'", nullable = false)
    private String useFtBizMsg = "N";

    @Column(name = "USE_SMS_MT", columnDefinition = "CHAR(1) DEFAULT 'N'", nullable = false)
    private String useSmsMt = "N";

    @Column(name = "USE_LMS_MT", columnDefinition = "CHAR(1) DEFAULT 'N'", nullable = false)
    private String useLmsMt = "N";

    @Column(name = "USE_MMS_MT", columnDefinition = "CHAR(1) DEFAULT 'N'", nullable = false)
    private String useMmsMt = "N";

    @Column(name = "USE_IMS_MT", columnDefinition = "CHAR(1) DEFAULT 'N'", nullable = false)
    private String useImsMt = "N";

    @Column(name = "USE_AT_RESEND", columnDefinition = "CHAR(1) DEFAULT 'N'", nullable = false)
    private String useAtResend = "N";

    @Column(name = "USE_PUSH", columnDefinition = "CHAR(1) DEFAULT 'N'", nullable = false)
    private String usePush = "N";

    @Column(name = "CONNECT_IP", length = 200)
    private String connectIp;

    @Column(name = "USE_MT_NUMBER_CHECK", columnDefinition = "CHAR(1) DEFAULT 'Y'", nullable = false)
    private String useMtNumberCheck = "Y";

    @Column(name = "USE_URACKER", columnDefinition = "CHAR(1) DEFAULT 'N'", nullable = false)
    private String useUracker = "N";

    @Column(name = "AT_SEND_GROUP_ID", length = 11)
    private Integer atSendGroupId = 0;

    @Column(name = "FT_SEND_GROUP_ID", length = 11)
    private Integer ftSendGroupId = 0;

    @Deprecated
    @Column(name = "MT_SEND_GROUP_ID", length = 11)
    private Integer mtSendGroupId = 0;

    @Deprecated
    @Column(name = "RE_SEND_GROUP_ID", length = 11)
    private Integer resendGroupId = 0;

    @Column(name = "SMT_SEND_GROUP_ID", length = 11)
    private Integer smtSendGroupId = 0;

    @Column(name = "LMT_SEND_GROUP_ID", length = 11)
    private Integer lmtSendGroupId = 0;

    @Column(name = "MMT_SEND_GROUP_ID", length = 11)
    private Integer mmtSendGroupId = 0;

    @Column(name = "SMT_RESEND_GROUP_ID", length = 11)
    private Integer smtResendGroupId = 0;

    @Column(name = "LMT_RESEND_GROUP_ID", length = 11)
    private Integer lmtResendGroupId = 0;

    @Column(name = "PUSH_SEND_GROUP_ID", length = 11)
    private Integer pushSendGroupId = 0;

    @Column(name = "REPORT_BROKER_ID", length = 20, nullable = false)
    private long reportBrokerId = 0;

    @Column(name = "AT_SEND_TOPIC_GROUP", length = 11)
    private Integer atSendTopicGroup = 0;

    @Column(name = "FT_SEND_TOPIC_GROUP", length = 11)
    private Integer ftSendTopicGroup = 0;

    @Column(name = "SMT_SEND_TOPIC_GROUP", length = 11)
    private Integer smtSendTopicGroup = 0;

    @Column(name = "LMT_SEND_TOPIC_GROUP", length = 11)
    private Integer lmtSendTopicGroup = 0;

    @Column(name = "MMT_SEND_TOPIC_GROUP", length = 11)
    private Integer mmtSendTopicGroup = 0;

    @Column(name = "IMT_SEND_TOPIC_GROUP", length = 11)
    private Integer imtSendTopicGroup = 0;

    @Column(name = "CMT_SEND_TOPIC_GROUP", length = 11)
    private Integer cmtSendTopicGroup = 0;

    @Column(name = "SMT_RESEND_TOPIC_GROUP", length = 11)
    private Integer smtResendTopicGroup = 0;

    @Column(name = "LMT_RESEND_TOPIC_GROUP", length = 11)
    private Integer lmtResendTopicGroup = 0;

    @Column(name = "MMT_RESEND_TOPIC_GROUP", length = 11)
    private Integer mmtResendTopicGroup = 0;

    @Column(name = "PUSH_SEND_TOPIC_GROUP", length = 11)
    private Integer pushSendTopicGroup = 0;

    @Column(name = "EMAIL_SEND_TOPIC_GROUP", length = 11)
    private Integer emailSendTopicGroup = 0;
}
