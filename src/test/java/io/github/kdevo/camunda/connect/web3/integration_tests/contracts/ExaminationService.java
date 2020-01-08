package io.github.kdevo.camunda.connect.web3.integration_tests.contracts;

import io.github.kdevo.camunda.connect.web3.Web3Connector;
import io.github.kdevo.camunda.connect.web3.integration_tests.Setup;
import io.github.kdevo.camunda.connect.web3.response.ConnectorResponse;
import org.junit.Assert;
import org.junit.Test;

public class ExaminationService {
    @Test
    public void shouldNotBeApproved() {
        ConnectorResponse response = new Web3Connector(Web3Connector.ID).createRequest()
                .endpoint("http://192.168.42.42:7545")
                .asContractCall()
                .contractAddress(Setup.Contract.EXAMINATION_SERVICE.getAddress())
                .function(String.format("new Function(\"isStudentApprovedForBachelorThesis\", [new Address(\"%s\")], [new TypeReference<Bool>() {}])", Setup.FHAChainMember.STUDENT2.getAddress()))
                .execute();

        Assert.assertFalse((Boolean) response.getOutputs().get(0));
    }

    @Test
    public void shouldBeApproved() {
        ConnectorResponse response = new Web3Connector(Web3Connector.ID).createRequest()
                .endpoint("http://192.168.42.42:7545")
                .asContractCall()
                .contractAddress(Setup.Contract.EXAMINATION_SERVICE.getAddress())
                .function(String.format("new Function(\"isStudentApprovedForBachelorThesis\", [new Address(\"%s\")], [new TypeReference<Bool>() {}])", Setup.FHAChainMember.STUDENT1.getAddress()))
                .execute();

        Assert.assertTrue((Boolean) response.getOutputs().get(0));
    }
}
