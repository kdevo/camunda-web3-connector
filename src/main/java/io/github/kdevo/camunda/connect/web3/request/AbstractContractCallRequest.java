package io.github.kdevo.camunda.connect.web3.request;

import io.github.kdevo.camunda.connect.web3.Web3Connector;
import io.github.kdevo.camunda.connect.web3.request.base.ConnectorRequest;
import io.github.kdevo.camunda.connect.web3.request.base.ContractRequest;
import io.github.kdevo.camunda.connect.web3.request.parameters.Parameter;
import org.web3j.abi.datatypes.Function;

import java.util.Map;

@SuppressWarnings("unchecked")
public abstract  class AbstractContractCallRequest<T extends ConnectorRequest> extends ContractRequest<T> {
    public AbstractContractCallRequest(Web3Connector connector, Map<String, Object> requestParameters) {
        super(connector, requestParameters);
    }

    public T function(String groovyScriptFunction) {
        setRequestParameter(Parameter.FUNCTION.toString(), groovyScriptFunction);
        return (T) this;
    }

    public T function(Function function) {
        setRequestParameter(Parameter.FUNCTION.toString(), function);
        return (T) this;
    }
}
