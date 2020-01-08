package io.github.kdevo.camunda.connect.web3.request.parameters;

import io.github.kdevo.camunda.connect.web3.request.ContractCallRequest;
import io.github.kdevo.camunda.connect.web3.request.ContractEventWatchRequest;
import io.github.kdevo.camunda.connect.web3.request.ContractWaitCallRequest;

import java.util.Optional;

public enum ParameterType {
    CONTRACT_CALL("contract-call", ContractCallRequest.class),
    CONTRACT_CALL_WAIT("contract-call-wait", ContractWaitCallRequest.class),
    CONTRACT_EVENT_WATCH("contract-event-watch", ContractEventWatchRequest.class);


    private String name;
    private Class cls;


    ParameterType(String name, Class cls) {
        this.name = name;
        this.cls = cls;
    }

    public String getName() {
        return name;
    }

    public static boolean isExistingName(String name) {
        for (ParameterType pt : ParameterType.values()) {
            if (pt.getName().equals(name)) {
                return true;
            }
        }
        return false;

    }

    @Override
    public String toString() {
        return name;
    }

    public static Optional<ParameterType> getByName(String name) {
        for (ParameterType p : values()) {
            if (p.name.equalsIgnoreCase(name)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }
}
