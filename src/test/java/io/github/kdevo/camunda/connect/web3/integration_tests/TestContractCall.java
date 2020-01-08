package io.github.kdevo.camunda.connect.web3.integration_tests;

import io.github.kdevo.camunda.connect.web3.Web3Connector;
import io.github.kdevo.camunda.connect.web3.response.ConnectorResponse;
import org.junit.Assert;
import org.junit.Test;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.Collections;


public class TestContractCall {
    @Test
    public void shouldCallFunctionViaString() {
        ConnectorResponse response = new Web3Connector(Web3Connector.ID).createRequest()
                .endpoint("http://192.168.42.42:7545")
                .asContractCall()
                .contractAddress(Setup.Contract.MEMBER_STORAGE.getAddress())
                .function("new Function(\"getId\",\n" +
                        "[new Address(\"0x23090a27F02716dB82bd3034b9fA85A15aE4222C\")],\n" +
                        "[new TypeReference<Uint256>() {}])")
                .execute();

        System.out.println(response.getTypedJsonOutputs());
    }

    @Test
    public void shouldCallFunctionViaGroovy() {
        new Web3Connector(Web3Connector.ID).createRequest()
                .endpoint("http://192.168.42.42:7545")
                .asContractCall()
                .contractAddress(Setup.Contract.MEMBER_STORAGE.getAddress())
                .function(new Function("getId",
                        Collections.singletonList(new Address("0x23090a27F02716dB82bd3034b9fA85A15aE4222C")),
                        Collections.singletonList(new TypeReference<Uint256>() {})))
                .execute();
    }
}
