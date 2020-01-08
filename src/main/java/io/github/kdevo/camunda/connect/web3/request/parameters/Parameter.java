package io.github.kdevo.camunda.connect.web3.request.parameters;

import java.util.*;

public enum Parameter {
    ENDPOINT(
            "endpoint",
            "http://localhost:7545",
            Objects::nonNull,
            "URL (usually a combination of protocol, host and port) that is used to access an Ethereum Client."
    ),

    TYPE(
            "type",
            ParameterType.CONTRACT_CALL.getName(),
            (val -> ParameterType.isExistingName(val.toString())),
            "The request type. It determines the behavior of the web3-connector."
    ),

    CONTRACT_ADDRESS(
            "contractAddress",
            null,
            (val -> {
                if (val == null) {
                    return false;
                }
                return val.toString().length() >= 20;
            }),
            "Ethereum address of the Smart Contract to use.",
            Arrays.asList(ParameterType.CONTRACT_CALL, ParameterType.CONTRACT_CALL_WAIT, ParameterType.CONTRACT_EVENT_WATCH)
    ),

    FUNCTION(
            "function",
            null,
            Objects::nonNull,
            "Contract function to call in Groovy script format. " +
                    "This is needed to dynamically link input/output parameter types. ",
            Arrays.asList(ParameterType.CONTRACT_CALL, ParameterType.CONTRACT_CALL_WAIT)
    ),

    WAIT_CONDITION(
            "waitCondition",
            null,
            Objects::nonNull,
            "Wait condition is a function in Groovy script format that " +
                    "accepts one argument (the 'outputs' list of the contract call) " +
                    "and returns either *true* (i.e.  \"continue waiting for correct result\") " +
                    "or *false* (i.e. \"stop waiting and continue process invocation\").",
            Collections.singletonList(ParameterType.CONTRACT_CALL_WAIT)
    ),

    EVENT_FILTER(
            "event",
            null,
            Objects::nonNull,
            "",
            Collections.singletonList(ParameterType.CONTRACT_EVENT_WATCH)
    );

    private String name;
    private Object defaultValue;
    private ParameterValidator validator;
    private String helpText;
    private Set<ParameterType> possibleTypes;


    Parameter(String name, Object defaultValue, ParameterValidator validator, String helpText) {
        this.name = name;
        this.validator = validator;
        this.defaultValue = defaultValue;
        this.helpText = helpText;
    }

    Parameter(String name, String defaultValue,
              ParameterValidator validator,
              String helpText, Collection<ParameterType> types) {
        this(name, defaultValue, validator, helpText);
        this.possibleTypes = new HashSet<>(types);
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isValueValid(Object value) {
        return validator == null || validator.isValid(value);
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public String getHelpText() {
        return helpText;
    }

    public Set<ParameterType> getPossibleTypes() {
        return possibleTypes;
    }

    public static Optional<Parameter> getByName(String name) {
        for (Parameter p : values()) {
            if (p.name.equalsIgnoreCase(name)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }
}
