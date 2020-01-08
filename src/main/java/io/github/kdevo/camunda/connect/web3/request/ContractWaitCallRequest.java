package io.github.kdevo.camunda.connect.web3.request;

import io.github.kdevo.camunda.connect.web3.Web3Connector;
import io.github.kdevo.camunda.connect.web3.request.base.AbstractConnectorRequest;
import io.github.kdevo.camunda.connect.web3.request.base.ContractRequest;
import io.github.kdevo.camunda.connect.web3.request.parameters.Parameter;

import java.util.List;
import java.util.Map;

public class ContractWaitCallRequest extends AbstractContractCallRequest<ContractWaitCallRequest> {
    public interface WaitCondition {
        boolean check(List<Object> outputs, long runs);
    }

    public ContractWaitCallRequest(Web3Connector connector, Map<String, Object> requestParameters) {
        super(connector, requestParameters);
    }

    public ContractWaitCallRequest waitCondition(String groovyScriptCondition) {
        setRequestParameter(Parameter.WAIT_CONDITION.toString(), groovyScriptCondition);
        return this;
    }

    public ContractWaitCallRequest waitCondition(WaitCondition waitCondition) {
        setRequestParameter(Parameter.WAIT_CONDITION.toString(), waitCondition);
        return this;
    }
}
