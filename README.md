## What
All object oriented languages have what's known as an object graph. An object graph
is some set of the available references in a program that have either unidirectional
or bidirectional relationships among themselves. In some languages, like JavaScript,
this graph is quite easy to traverse dynamically. In Java, that's not the case. As
a natural evolution of programming preferences, people frequently prefer to reference
fields at some 'path' which is most commonly delimited with full stops. This library
aims to bring that traversal convenience to Java so that it might be utilized for in-memory
query languages and the like.



## Usage

```java

// given the following (getters / setters omitted for brevity)
public class MyCustomClass {

    private Boolean leafNode;
    private MyOtherClass other = new MyOtherClass();

}

public class MyOtherClass {

    private Integer leafNode;

}

// usage
MyCustomClass instance = new MyCustomClass();
ObjectGraph graph = new ObjectGraph(MyCustomClass.class);


assertNull(graph.resolve("leafNode").value(instance));
instance.setLeafNode(true);
assertTrue(graph.resolve("leafNode").value(instance));
instance.getOther().setLeafNode(4);
assertEquals(4, graph.resolve("other.leafNode").value(instance));

```