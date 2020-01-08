package io.github.kdevo.camunda.connect.web3.integration_tests;

import io.github.kdevo.camunda.connect.web3.Web3Connector;
import io.github.kdevo.camunda.connect.web3.response.ConnectorResponse;
import io.github.kdevo.camunda.connect.web3.web3j_extensions.EventFilter;
import org.junit.Assert;
import org.junit.Test;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.Arrays;
import java.util.Collections;

public class TestContractEventWatch {
    @Test
    public void shouldWatchContractEvent() {
        ConnectorResponse response = new Web3Connector(Web3Connector.ID).createRequest()
                .endpoint(Setup.ENDPOINT)
                .asContractEventWatch()
                .contractAddress(Setup.Contract.EXAMINATION_SERVICE.getAddress())
                .event(new EventFilter("OnStudentExamRegistration",
                        Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                        Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}),
                        Collections.singletonList(new Address(Setup.FHAChainMember.STUDENT2.getAddress()))))
                .execute();
        System.out.println(response.getOutputs());
        Assert.assertEquals(response.getOutputs().get(0).toString().toLowerCase(),
                Setup.FHAChainMember.STUDENT2.getAddress().toLowerCase());
    }

    @Test
    public void shouldWatchContractEventViaString() {
        ConnectorResponse response = new Web3Connector(Web3Connector.ID).createRequest()
                .endpoint(Setup.ENDPOINT)
                .asContractEventWatch()
                .contractAddress(Setup.Contract.EXAMINATION_SERVICE.getAddress())
                .event("new EventFilter('OnStudentExamRegistration', " +
                                "[new TypeReference<Address>() {}], [new TypeReference<Uint256>() {}], " +
                        String.format("[new Address(%s)])", Setup.FHAChainMember.STUDENT2.getAddress()))
                .execute();
        System.out.println(response.getOutputs());
        Assert.assertEquals(response.getOutputs().get(0).toString().toLowerCase(),
                Setup.FHAChainMember.STUDENT2.getAddress().toLowerCase());
    }
}
