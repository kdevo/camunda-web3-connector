package io.github.kdevo.camunda.connect.web3.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kdevo.camunda.connect.web3.Web3Connector;
import org.camunda.connect.impl.AbstractConnectorResponse;
import org.web3j.abi.datatypes.Type;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConnectorResponse extends AbstractConnectorResponse {
    private static final ObjectMapper mapper = new ObjectMapper();

    private String typedJsonOutputs;
    private List<Object> outputs;

    private Long runs;

    public enum Parameter {
        OUTPUTS("outputs");

        private String name;

        Parameter(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public ConnectorResponse(List<Type> outputs) throws JsonProcessingException {
        this.outputs = convertTypeToValueList(outputs);
        this.typedJsonOutputs = mapper.writeValueAsString(outputs);
    }

    public ConnectorResponse(List<Type> outputs, long runs) throws JsonProcessingException {
        this.outputs = convertTypeToValueList(outputs);
        this.typedJsonOutputs = mapper.writeValueAsString(outputs);
        this.runs = runs;
    }

    public static List<Object> convertTypeToValueList(List<Type> outputs) {
        List<Object> values = new LinkedList<>();
        outputs.forEach(t -> values.add(t.getValue()));
        return values;
    }

    @Override
    protected void collectResponseParameters(Map<String, Object> responseParameters) {
        responseParameters.put(Parameter.OUTPUTS.toString(), outputs);
        this.responseParameters = responseParameters;
        Web3Connector.logger.info(responseParameters.toString());
    }

    public String getTypedJsonOutputs() {
        return typedJsonOutputs;
    }

    public List<Object> getOutputs() {
        return outputs;
    }

    public Long getRuns() {
        return runs;
    }
}
