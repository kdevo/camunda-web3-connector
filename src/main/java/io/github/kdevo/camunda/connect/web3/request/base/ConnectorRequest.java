package io.github.kdevo.camunda.connect.web3.request.base;

import io.github.kdevo.camunda.connect.web3.Web3Connector;
import io.github.kdevo.camunda.connect.web3.request.ContractCallRequest;
import io.github.kdevo.camunda.connect.web3.request.ContractEventWatchRequest;
import io.github.kdevo.camunda.connect.web3.request.ContractWaitCallRequest;
import io.github.kdevo.camunda.connect.web3.request.parameters.Parameter;
import io.github.kdevo.camunda.connect.web3.request.parameters.ParameterType;
import io.github.kdevo.camunda.connect.web3.response.ConnectorResponse;

public class ConnectorRequest extends AbstractConnectorRequest implements org.camunda.connect.spi.ConnectorRequest<ConnectorResponse> {
    public ConnectorRequest(Web3Connector connector) {
        super(connector);
    }

    public ConnectorRequest endpoint(String endpoint) {
        setRequestParameter(Parameter.ENDPOINT.toString(), endpoint);
        return this;
    }

    public String getEndpoint() {
        return (String) getRequestParameter(Parameter.ENDPOINT);
    }

    public ContractCallRequest asContractCall() {
        requestParameters.put(Parameter.TYPE.toString(), ParameterType.CONTRACT_CALL);
        return new ContractCallRequest((Web3Connector) connector, requestParameters);
    }

    public ContractWaitCallRequest asContractWaitCall() {
        requestParameters.put(Parameter.TYPE.toString(), ParameterType.CONTRACT_CALL_WAIT);
        return new ContractWaitCallRequest((Web3Connector) connector, requestParameters);
    }

    public ContractEventWatchRequest asContractEventWatch() {
        requestParameters.put(Parameter.TYPE.toString(), ParameterType.CONTRACT_EVENT_WATCH);
        return new ContractEventWatchRequest((Web3Connector) connector, requestParameters);
    }

    public boolean hasType(ParameterType checkType) {
        return getRequestParameter(Parameter.TYPE).toString().equalsIgnoreCase(checkType.getName());
    }
}
