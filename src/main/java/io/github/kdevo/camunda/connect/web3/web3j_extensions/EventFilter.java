package io.github.kdevo.camunda.connect.web3.web3j_extensions;

import org.web3j.abi.TypeEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class EventFilter extends Event {
    private static final boolean add0xPrefixToTypes = true;

    private List<String> encodedTopics = new LinkedList<>();

    public EventFilter(String name, List<TypeReference<?>> indexedParameters, List<TypeReference<?>> nonIndexedParameters,
                       List<Type> topics) {
        super(name, indexedParameters, nonIndexedParameters);
        for (Type t : topics) {
            encodedTopics.add((add0xPrefixToTypes ? "0x" : "") + TypeEncoder.encode(t));
        }
    }


    public EventFilter(String name, List<TypeReference<?>> indexedParameters, List<TypeReference<?>> nonIndexedParameters) {
        this(name, indexedParameters, nonIndexedParameters, Collections.emptyList());
    }

    public EventFilter(String name, List<TypeReference<?>> indexedParameters) {
        this(name, indexedParameters, Collections.emptyList(), Collections.emptyList());
    }

    public EventFilter(String name) {
        this(name, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    public List<String> getEncodedTopics() {
        return encodedTopics;
    }
}
