package io.github.kdevo.camunda.connect.web3.request.base;

import io.github.kdevo.camunda.connect.web3.Web3Connector;
import io.github.kdevo.camunda.connect.web3.request.parameters.Parameter;
import io.github.kdevo.camunda.connect.web3.request.parameters.ParameterType;
import io.github.kdevo.camunda.connect.web3.response.ConnectorResponse;
import org.camunda.connect.spi.ConnectorRequest;

import java.util.Optional;

import static io.github.kdevo.camunda.connect.web3.Web3Connector.logger;

public abstract class AbstractConnectorRequest extends org.camunda.connect.impl.AbstractConnectorRequest<ConnectorResponse> implements ConnectorRequest<ConnectorResponse> {
    public AbstractConnectorRequest(Web3Connector connector) {
        super(connector);
    }

    public Object getRequestParameter(Parameter param) {
        Object value = getRequestParameter(param.toString());
        return value == null ? param.getDefaultValue() : value;
    }

    public boolean checkParameterValidity() {
        boolean allValid = true;
        Optional<ParameterType> type = ParameterType.getByName(getRequestParameter(Parameter.TYPE).toString());
        if (!type.isPresent()) {
            allValid = false;
        }
        for (Parameter p : Parameter.values()) {
            if (p.getPossibleTypes() == null || p.getPossibleTypes().contains(type.get())) {
                Object value = getRequestParameter(p);
                if (p.isValueValid(value)) {
                    logger.info(String.format("VALID: Got parameter \"%s\" with value \"%s\".", p, value));
                } else {
                    String defaultValueText = p.getDefaultValue() == null ?
                            "[No default value - is null]" : String.format(" [Default: %s]", p.getDefaultValue());
                    logger.severe(String.format("The parameter \"%s\" has an invalid value \"%s\"! Double-check it. " +
                            "HELP: %s", p, value, p.getHelpText() + ' ' + defaultValueText));
                    allValid = false;
                }
            }
        }
        return allValid;
    }

    @Override
    protected boolean isRequestValid() {
        return checkParameterValidity();
    }
}
