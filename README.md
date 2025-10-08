# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
## Link for Phase 2 Diagram
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+iMykoKp+h-Ds0KPMB4lUEiMAIEJ4oYoJwkZKoAGkm+hi7jS+4MkyU76XOnl3kuwowGKEpujKcplu8SqYCqwYagAkmgVAmkg676TA0AwGpOw3gFDpJr6zpdjAPZ9tu7kWf6zLTJe0BIAAXigHBRjGcaFFphXwMgqYwOmACMBE5qoebzNBRYlvU4opcg66Hhw5hICaqgNvReW8oFFlWeFW5uYKw78vUh5yCgz7xOel7Xgdi6VMuAZrgGl5aPIFXtjppYOeKTkAZg70gdUun6YZWwmeh8LJj12EwLh+GjEDxqXlR6B0U2njeH4-heCg6AxHEiRYzjDm+FgomCqB9QNNIEb8RG7QRt0PRyaoCnDBRSMdQD8KVO9Lxs4hyO-WZGFbS61lCcTF3wfzaCucV+23odRgoNwx6XpLlHS-5G0FbdwUZDMEA0I9L67XLXU8wJ4unt9CCAUL4NFRJYHDaN9RkEg9hoM1mWnppnOYZDYA4XhWYu4ybse1767EyjDFo8x-gouu-jYOKGr8WiMAAOJKhopNVaWDRZ7TDP2EqrOI9LYPafbvOV0hpmwsLjuWaLyA5DnObq+zsudvL+X0jAjJgGd3ea+tC4CkF9ShU+l1bnF6owElM1pQjeowJASET-u-2dt2vb9ntXXk5KtV6vVTUtdGKCxgp1ddaJaZOINoxhyVE3QPUPjn-El-NbHHem0W5WTOq+M2VIFaD2HoGBAnc1AYi1pPe8wUyDUwjHAdoMAABUIU0gtAALLZ1zkAgqIsSrwIzjkV65ta7ELmFQsANs7ZNwdh2GoLwy45gpi4XhnQ-ZOwDiUIO0MQ6jC4WoHhfDAHxwxhwAA7G4JwKAnAxAjMEOAXEABs8AJyGHgTAIogcyacwpq0Dopdy6-3ZlmCRAA5JUAjEzczoSMPmSE1j2McYLVhJj94wGOuieBY8PFDyVA4uYvdW7uUgQPUcQ8mSj3cegJBu9p4hXFHPE2L1F6JWSqldcFFN5V2ulPchB9yrHwLq6X+-9r5tXvp1CGwjn6vxGO-eon9Sw-wonUwBpS97RPqGA02fcPLa0HiGDgwSJFXSgVPXW9QEp2LQWkLBXtNj0JQOAsZH46FwD0fA5hPivyDN0hIhK0h6j9XCMETx4TvFNMqE-URsMRgXKuTAG5dywlzAiSgGRTEMaWGVjZTZsQPaJBBX2CAmyABSEBxRbJiMkUAaojHCL8RwxoTRmQyR6BIiuUskJZmwAgYAIKoBwAgDZKA9y5iXKcc3B4rC67EvQKpcllLqW0vpSgS5tRvmN1OeUmAAArRFaBgnJLQHyy5nKKWUB5dAKJg5YkTPiTAtWMrUmBUWRkiUIycmqjyavQpl5inbwGSfYqFSj5m2qWfXpUBGrNVarfdqD9mm9QGqHfkrsYBdO-rUl1V9+nzMGaA+eL1j7qsnt-bAz0UAzKVPK7KXKlU0ugGsPypCbpCldImk6WydnRLenQhF4ojn-ltic8yjr2n+vDjAStUdfmGGcvuAAZofSwTK2EvJhn63MzbW3ewkTATtI4e19j7Y2OOQKAheApbjSFYBl3ykQMGWAwBsBksIHkAohj84t0klTGmdMGbGAfi41l3Vt08j0AYRBdbmVuSOtwPAL7Y0yHmR+7dsCX3Wv1dIbw2BjQShRIEwwJoECb10NwDQVTb1fnqFuvAxy-o2qdi8Dpv75BPsMCAT9Sd+1CN6kOt+TaSqPv0ERkjAD52YCAA
