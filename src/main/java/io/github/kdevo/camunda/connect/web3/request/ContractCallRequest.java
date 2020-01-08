package io.github.kdevo.camunda.connect.web3.request;

import io.github.kdevo.camunda.connect.web3.Web3Connector;
import io.github.kdevo.camunda.connect.web3.request.base.ConnectorRequest;

import java.util.Map;

public class ContractCallRequest extends AbstractContractCallRequest<ContractCallRequest> {
    public ContractCallRequest(Web3Connector connector, Map<String, Object> requestParameters) {
        super(connector, requestParameters);
    }
}
