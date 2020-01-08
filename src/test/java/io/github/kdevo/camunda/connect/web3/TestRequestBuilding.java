package io.github.kdevo.camunda.connect.web3;

import io.github.kdevo.camunda.connect.web3.request.base.ConnectorRequest;
import io.github.kdevo.camunda.connect.web3.request.parameters.ParameterType;
import io.github.kdevo.camunda.connect.web3.response.ConnectorResponse;
import org.junit.Assert;
import org.junit.Test;

public class TestRequestBuilding {
    @Test
    public void shouldHaveCorrectRequestType() {
        ConnectorRequest request;

        request = new Web3Connector(Web3Connector.ID).createRequest();
        Assert.assertTrue(request.hasType(ParameterType.CONTRACT_CALL));

        request = new Web3Connector(Web3Connector.ID).createRequest().asContractWaitCall();
        Assert.assertTrue(request.hasType(ParameterType.CONTRACT_CALL_WAIT));

        request = new Web3Connector(Web3Connector.ID).createRequest().asContractEventWatch();
        Assert.assertTrue(request.hasType(ParameterType.CONTRACT_EVENT_WATCH));
    }

    @Test
    public void shouldFailOnValidation() {
        ConnectorResponse response;

        try {
            response = new Web3Connector(Web3Connector.ID).createRequest().execute();
        } catch (RuntimeException e) {
            return;
        }

        Assert.fail("Runtime exception should have been thrown. Response: " + response);
    }

}
