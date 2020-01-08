package io.github.kdevo.camunda.connect.web3;

import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorProvider;

public class Web3ConnectorProvider implements ConnectorProvider {
    @Override
    public String getConnectorId() {
        return Web3Connector.ID;
    }

    @Override
    public Connector<?> createConnectorInstance() {
        return new Web3Connector(Web3Connector.ID);
    }
}
