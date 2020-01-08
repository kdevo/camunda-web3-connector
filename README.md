# Web3-Connector for Camunda BPM

This is a custom connector implementation for Camunda BPM that supports making Smart Contract calls to via *web3j* 
without actually writing any Java code.

It is currently very basic and part of the **[FHAChain](https://kdevo.github.io/#bachelor-thesis) Proof-of-Concept**.

> :warning: Warning: This project was part of my [Bachelor Thesis](https://kdevo.github.io/#bachelor-thesis) and therefore last been tested **more than 2 years ago**. There is no guarantee that it still works. Still, feel free to open issue(s) as I might consider to re-support it depending on current interest.

## How to integrate

1. Build the Uber-JAR (with all dependencies) using `mvn clean install`.
2. Copy `target/web3-connector.jar` to `${CATALINA_HOME}/lib` directory.
3. Restart Camunda BPM so that it automatically recognizes the new connector.

> :bulb: For concrete usage examples [refer to page 87](https://kdevo.github.io/docs/20181406-KaiDinghofer-BachelorThesis-German.pdf) of my Bachelor Thesis.

## References

Custom connectors are described in the [Camunda documentation](https://docs.camunda.org/manual/7.8/reference/connect/extending-connect/#custom-connector).
Though very minimal, this documentation provides a good starting point on how to implement an own connector.

Internally, Java's [Service Loader mechanism](https://docs.oracle.com/javase/tutorial/ext/basics/spi.html) is used.
