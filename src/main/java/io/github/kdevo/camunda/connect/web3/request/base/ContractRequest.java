package io.github.kdevo.camunda.connect.web3.request.base;

import io.github.kdevo.camunda.connect.web3.Web3Connector;
import io.github.kdevo.camunda.connect.web3.request.parameters.Parameter;
import org.web3j.abi.datatypes.Function;

import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class ContractRequest<T extends ConnectorRequest> extends ConnectorRequest {
    public ContractRequest(Web3Connector connector, Map<String, Object> requestParameters) {
        super(connector);
        setRequestParameters(requestParameters);
    }

    public T contractAddress(String address) {
        setRequestParameter(Parameter.CONTRACT_ADDRESS.toString(), address);
        return (T) this;
    }

    public String getContractAddress() {
        return (String) getRequestParameter(Parameter.CONTRACT_ADDRESS);
    }

    public Object getFunction() {
        return getRequestParameter(Parameter.FUNCTION);
    }
}
