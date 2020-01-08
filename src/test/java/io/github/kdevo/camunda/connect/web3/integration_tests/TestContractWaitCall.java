package io.github.kdevo.camunda.connect.web3.integration_tests;

import io.github.kdevo.camunda.connect.web3.Web3Connector;
import io.github.kdevo.camunda.connect.web3.response.ConnectorResponse;
import org.junit.Assert;
import org.junit.Test;

public class TestContractWaitCall {

    @Test
    public void shouldRunOnce() {
        ConnectorResponse response = new Web3Connector(Web3Connector.ID).createRequest()
                .endpoint(Setup.ENDPOINT)
                .asContractWaitCall()
                .contractAddress(Setup.Contract.MEMBER_STORAGE.getAddress())
                .function("new Function(\"getId\",\n" +
                        "[new Address(\"0x23090a27F02716dB82bd3034b9fA85A15aE4222C\")],\n" +
                        "[new TypeReference<Uint256>() {}])")
                .waitCondition((outputs, runs) -> {
                    System.out.println(outputs);
                    return "303".equals(outputs.get(0));
                })
                .execute();
    }

    @Test
    public void shouldRunOnceViaString() {
        ConnectorResponse response = new Web3Connector(Web3Connector.ID).createRequest()
                .endpoint(Setup.ENDPOINT)
                .asContractWaitCall()
                .contractAddress(Setup.Contract.MEMBER_STORAGE.getAddress())
                .function("new Function(\"getId\",\n" +
                        "[new Address(\"0x23090a27F02716dB82bd3034b9fA85A15aE4222C\")],\n" +
                        "[new TypeReference<Uint256>() {}])")
                .waitCondition("println(outputs); return '303'.equals(outputs.get(0))")
                .execute();
    }

    @Test
    public void shouldRunTwoTimes() {
        ConnectorResponse response = new Web3Connector(Web3Connector.ID).createRequest()
                .endpoint(Setup.ENDPOINT)
                .asContractWaitCall()
                .contractAddress(Setup.Contract.MEMBER_STORAGE.getAddress())
                .function("new Function(\"getId\",\n" +
                        "[new Address(\"0x23090a27F02716dB82bd3034b9fA85A15aE4222C\")],\n" +
                        "[new TypeReference<Uint256>() {}])")
                .waitCondition((outputs, runs) -> {
                    System.out.println(outputs);
                    return runs == 1;
                })
                .execute();
        Assert.assertEquals(new Long(2), response.getRuns());
    }
}
