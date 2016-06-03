SRCDIR = ./src
CLASSDIR = ./build
STUFF = test seq-boruvka noether
NOETHERSRC = ./src/noether/Noether.java ./src/noether/NoetherThread.java ./src/noether/WorkItem.java ./src/noether/WorkList.java
TESTSRC = ./src/test/Test.java ./src/test/TestWorkItem.java ./src/test/TestWorkList.java
GRAPHSRC = ./src/graph/Edge.java ./src/graph/NoetherGraph.java ./src/graph/NoetherNode.java
CONCURRENTBORUVKA = ./src/concurrentboruvka/ConBoruvkaMain.java ./src/concurrentboruvka/BoruvkaWorkList.java ./src/concurrentboruvka/BoruvkaWorkItem.java
CONCURRENTGRAPH = ./src/concurrentgraph/ConcurrentGraph.java ./src/concurrentgraph/EdgeComparator.java ./src/concurrentgraph/Edge.java ./src/concurrentgraph/Node.java
SEQBORUVKA = ./src/sequentialboruvka/SeqBoruvkaMain.java ./src/sequentialboruvka/SequentialGraph.java ./src/sequentialboruvka/SeqEdgeComparator.java ./src/sequentialboruvka/SeqEdge.java ./src/sequentialboruvka/SeqNode.java
FASTCONCURRENTGRAPH = ./src/fastconcurrentgraph/ConcurrentGraph.java ./src/fastconcurrentgraph/EdgeComparator.java ./src/fastconcurrentgraph/Edge.java ./src/fastconcurrentgraph/Node.java
FASTCONCURRENTBORUVKA = ./src/fastconboruvka/ConBoruvkaMain.java ./src/fastconboruvka/BoruvkaWorkList.java ./src/fastconboruvka/BoruvkaWorkItem.java ./src/fastconboruvka/PhaseOneItem.java

fastconboruvka : ${FASTCONCURRENTGRAPH} ${NOETHERSRC} ${CONCURRENTBORUVKA}
	javac -d build/ ${FASTCONCURRENTBORUVKA} ${FASTCONCURRENTGRAPH} ${NOETHERSRC}

seqboruvka : ${SEQBORUVKA}
	javac -d build/ ${SEQBORUVKA}

concurrentgraph : ${CONCURRENTGRAPH}
	javac -d build/ ${CONCURRENTGRAPH}

concurrentgraphtest : ./src/test/ConcurrentGraphTest.java ${CONCURRENTGRAPH}
	javac -d build/ ./src/test/ConcurrentGraphTest.java ${CONCURRENTGRAPH}

test : ${NOETHERSRC} ${TESTSRC}
	javac -d build/ ${TESTSRC} ${NOETHERSRC}

graph : ${GRAPHSRC}
	javac -d build/ ${GRAPHSRC}

graphtest : ./src/test/GraphTest.java ${GRAPHSRC}
	javac -d build/ ./src/test/GraphTest.java ${GRAPHSRC}

conboruvka : ${CONCURRENTBORUVKA} ${NOETHERSRC} ${CONCURRENTGRAPH}
	javac -d build/ ${CONCURRENTBORUVKA} ${NOETHERSRC} ${CONCURRENTGRAPH}
