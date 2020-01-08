package io.github.kdevo.camunda.connect.web3.request;

import io.github.kdevo.camunda.connect.web3.Web3Connector;
import io.github.kdevo.camunda.connect.web3.request.base.ContractRequest;
import io.github.kdevo.camunda.connect.web3.request.parameters.Parameter;
import org.web3j.abi.datatypes.Event;

import java.util.Map;

public class ContractEventWatchRequest extends ContractRequest<ContractEventWatchRequest> {
    public ContractEventWatchRequest(Web3Connector connector, Map<String, Object> requestParameters) {
        super(connector, requestParameters);
    }

    public ContractEventWatchRequest event(String groovyScriptEvent) {
        setRequestParameter(Parameter.EVENT_FILTER.toString(), groovyScriptEvent);
        return this;
    }

    public ContractEventWatchRequest event(Event event) {
        setRequestParameter(Parameter.EVENT_FILTER.toString(), event);
        return this;
    }
}
