package com.example.generator.process;

import com.example.generator.entity.ClientInfoEntity;
import com.example.generator.entity.SendLimitConfigEntity;
import com.example.generator.repository.ClientInfoRepository;
import com.example.generator.repository.SendLimitConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class SendLimitProcess {

    private final ClientInfoRepository clientInfoRepository;
    private final SendLimitConfigRepository sendLimitConfigRepository;
    private final String[] CHANNEL_TYPES = {"AT", "FT", "SM", "LM", "MM", "IM", "PS"};

    public void process() {
        List<String> agentLists = getDummeyAgentLists();
        save(agentLists);
    }

    private List<String> getAgentLists() {
        return clientInfoRepository
                .findAll()
                .stream()
                .map(d -> d.getAgentId())
                .collect(Collectors.toList());
    }

    private List<String> getDummeyAgentLists() {
        return IntStream
                .rangeClosed(1, 1000)
                .mapToObj(d -> String.format("demoComp-%04d", d))
                .collect(Collectors.toList());
    }

    private void save(List<String> agentIds) {
        agentIds.forEach(agentId -> {
            List<SendLimitConfigEntity> group = new ArrayList<>();
            for (int i = 0; i < CHANNEL_TYPES.length; i++) {
                String channelType = CHANNEL_TYPES[i];
                SendLimitConfigEntity sendLimitConfig = getSendLimitConfig(agentId, channelType);
                group.add(sendLimitConfig);
            }
            sendLimitConfigRepository.saveAll(group);
        });
    }

    public SendLimitConfigEntity getSendLimitConfig(String agentId, String channelType) {
        SendLimitConfigEntity sendLimitConfigEntity = new SendLimitConfigEntity();
        sendLimitConfigEntity.setAgentId(agentId);
        sendLimitConfigEntity.setChannelType(channelType);
        sendLimitConfigEntity.setActive(false);
        sendLimitConfigEntity.setDailyLimitCount(1000);
        sendLimitConfigEntity.setMonthlyLimitCount(10000);
        sendLimitConfigEntity.setCreatedBy("tester");
        sendLimitConfigEntity.setLastModifiedBy("tester");
        return sendLimitConfigEntity;
    }
}
