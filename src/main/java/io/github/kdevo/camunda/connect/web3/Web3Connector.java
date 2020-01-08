package io.github.kdevo.camunda.connect.web3;

import groovy.lang.GroovyShell;
import io.github.kdevo.camunda.connect.web3.request.ContractWaitCallRequest;
import io.github.kdevo.camunda.connect.web3.request.parameters.ParameterType;
import io.github.kdevo.camunda.connect.web3.request.base.ConnectorRequest;
import io.github.kdevo.camunda.connect.web3.request.parameters.Parameter;
import io.github.kdevo.camunda.connect.web3.response.ConnectorResponse;
import io.github.kdevo.camunda.connect.web3.web3j_extensions.EventFilter;
import org.camunda.connect.impl.AbstractConnector;
import org.codehaus.groovy.control.CompilationFailedException;
import org.web3j.abi.*;
import org.web3j.abi.datatypes.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

public class Web3Connector extends AbstractConnector<ConnectorRequest, ConnectorResponse> {
    public static final String ID = "web3-connector";

    public static final Logger logger = Logger.getLogger(Web3Connector.class.getName());

    private Web3j web3;

    public Web3Connector(String connectorId) {
        super(connectorId);
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public ConnectorRequest createRequest() {
        return new ConnectorRequest(this);
    }

    @Override
    public ConnectorResponse execute(ConnectorRequest request) throws RuntimeException {
        logger.info(String.format("+ Execution of %s started +", ID));

        web3 = Web3j.build(new HttpService(request.getEndpoint()));

        try {
            Web3ClientVersion version = web3.web3ClientVersion().send();
            logger.info("Web3 version: " + version.getWeb3ClientVersion());
            EthBlock.Block latestBlock = web3.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock();
            logger.info("Latest block: " + latestBlock.getNumber());
        } catch (IOException e) {
            logger.severe("Unable to get basic information: " + e.getLocalizedMessage());
            e.printStackTrace();
            return null;
        }

        try {
            if (request.hasType(ParameterType.CONTRACT_CALL) || request.hasType(ParameterType.CONTRACT_CALL_WAIT)) {
                return handleContractCallLikeType(request);
            } else if (request.hasType(ParameterType.CONTRACT_EVENT_WATCH)) {
                return handleContractEventWatchType(request);
            }

            assert false; // Should never happen
            return null;

        } catch (IOException e) {
            logger.severe("The connector request failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            logger.severe("Interrupted while performing request.");
            e.printStackTrace();
            return null;
        }
    }

    private ConnectorResponse handleContractEventWatchType(ConnectorRequest request) throws IOException {
        String contractAddress = (String) request.getRequestParameter(Parameter.CONTRACT_ADDRESS);
        EventFilter eventFilter = initParameterValue(request, Parameter.EVENT_FILTER, "DynamicEventInitializer");

        Log l = waitForEvent(contractAddress, eventFilter);
        EventValues values = Contract.staticExtractEventParameters(eventFilter, l);
        List<Type> result = values.getIndexedValues();
        result.addAll(values.getNonIndexedValues());

        return new ConnectorResponse(result);
    }

    private ConnectorResponse handleContractCallLikeType(ConnectorRequest request) throws IOException, InterruptedException {
        Function function = initParameterValue(request, Parameter.FUNCTION, "DynamicFunctionInitializer");

        String contractAddress = (String) request.getRequestParameter(Parameter.CONTRACT_ADDRESS);

        if (request.hasType(ParameterType.CONTRACT_CALL)) {

            return new ConnectorResponse(execContractFunc(contractAddress, function));
        } else if (request.hasType(ParameterType.CONTRACT_CALL_WAIT)) {
            GroovyShell shell = new GroovyShell();

            final long waitTime = 2500;
            Object waitCondition = request.getRequestParameter(Parameter.WAIT_CONDITION);
            List<Type> result;
            boolean wait;
            long runs = 1;
            do {
                result = execContractFunc(contractAddress, function);
                List<Object> outputs = ConnectorResponse.convertTypeToValueList(result);
                try {
                    if (waitCondition instanceof String) {
                        shell.setVariable("outputs", outputs);
                        shell.setVariable("runs", runs);
                        wait = (boolean) shell.evaluate((String) waitCondition);

                    } else {
                        wait = ((ContractWaitCallRequest.WaitCondition)
                                request.getRequestParameter(Parameter.WAIT_CONDITION)).check(outputs, runs);
                    }
                } catch (Exception e) {
                    logger.severe("Aborting! Exception occurred while invoking the wait condition: " + e.getMessage());
                    e.printStackTrace();
                    break;
                }

                if (wait) {
                    logger.info(String.format("Wait condition is (still) true. Waiting %d ms...", waitTime));
                    Thread.sleep(2500);
                    runs++;
                }
            } while (wait);
            return new ConnectorResponse(result, runs);
        } else {
            assert false;
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T initParameterValue(ConnectorRequest request, Parameter parameter, String scriptName) throws IOException {
        Object obj = request.getRequestParameter(parameter);
        if (obj instanceof String) {
            String functionAsGroovyScript = (String) obj;
            logger.info(String.format("Got %s as a string (will be initialized via Groovy): \n%s",
                    parameter, functionAsGroovyScript));
            try {
                return initDynamicallyViaGroovy(scriptName, functionAsGroovyScript, false);
            } catch (CompilationFailedException | IOException e) {
                logger.severe("Initialization via Groovy failed: " + e.getMessage());
                throw e;
            }
        } else {
            return (T) obj;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T initDynamicallyViaGroovy(String scriptName, String initializationScriptPart, boolean enableSecurity) throws IOException {
        final String groovyScriptResourcePath = String.format("/groovy/%s.groovy", scriptName);
        GroovyShell shell = new GroovyShell();
        shell.setVariable("injectedEnableAstSecurity", enableSecurity);
        shell.setVariable("injectedFunctionString", initializationScriptPart);
        try {
            URI groovy = Web3Connector.class.getResource(groovyScriptResourcePath).toURI();
            return (T) shell.evaluate(groovy);
        } catch (URISyntaxException e) {
            logger.severe("The following exception should never happen: " + e);
            logger.severe(String.format("Ensure that '%s' is accessible as a resource!", groovyScriptResourcePath));
            return null;
        }
    }

    private List<Type> execContractFunc(String contractAddress, Function function) throws IOException {
        logger.info(String.format("Running contract function '%s'...", function.getName()));

        String result = web3.ethCall(Transaction.createEthCallTransaction(null,
                contractAddress, FunctionEncoder.encode(function)),
                DefaultBlockParameterName.LATEST)
                .send().getValue();
        return FunctionReturnDecoder.decode(result, function.getOutputParameters());
    }

    private Log waitForEvent(String contractAddress, EventFilter event) {
        // Due to a bug in Web3j (not verified by code review - TODO: Add reference to StackOverflow),
        // the address prefix "0x" needs to be trimmed:
        if (contractAddress.startsWith("0x")) {
            contractAddress = contractAddress.substring(2);
        }
        EthFilter filter = new EthFilter(DefaultBlockParameterName.LATEST, DefaultBlockParameterName.LATEST, contractAddress);
        filter.addSingleTopic(EventEncoder.encode(event));
        for (String topic : event.getEncodedTopics()) {
            filter.addSingleTopic(topic);
        }

        logger.info(String.format("Waiting for event '%s'...", event.getName()));

        return web3.ethLogObservable(filter).toBlocking().first();
    }
}