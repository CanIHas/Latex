package can.i.has.grex.experiments.runner

import can.i.has.grex.experiments.Result
import can.i.has.utils.OrderedMap

import java.util.concurrent.Callable


class SingleThreadExperimentRunner<R extends Result> implements ExperimentRunner<R> {
    @Override
    void runWithCallback(OrderedMap<String> key, Callable<R> callable, Closure<Void> callback) {
        callback.call(callable.call())
    }

    @Override
    void join() {

    }

    @Override
    void close() {

    }
}
