package can.i.has.grex.experiments.example

import can.i.has.grex.Workspace
import can.i.has.grex.experiments.Experiment
import can.i.has.grex.experiments.config.FullSearchYield
import can.i.has.grex.experiments.example.eval.CrossValidation
import can.i.has.grex.experiments.example.eval.Evaluation
import can.i.has.grex.experiments.example.knn.Distance
import can.i.has.grex.experiments.example.knn.Knn
import can.i.has.grex.experiments.example.knn.ResultInput
import can.i.has.grex.experiments.example.model.DataSet
import can.i.has.grex.experiments.example.model.DataSetResources
import can.i.has.grex.experiments.runner.SingleThreadExperimentRunner
import can.i.has.grex.latex.model.Document
import can.i.has.utils.OrderedMap

new Workspace("./workspace").using {
    def params = [
        "folds",
        "k",
        "dataSet",
        "distance",
        "input"
    ]
    def domains = [
        folds: [5],
        k: [1, 3, 5]
    ]
//    def domains = [
//        folds: [2, 3, 5],
//        k: [1, 3, 5, 7, 11]
//    ]
    def values = [
        dataSet : [
            glass: DataSetResources.glass,
            iris: DataSetResources.iris,
            wine: DataSetResources.wine
        ],
        distance: [
            Manhattan     : { x -> Distance.manhattan() },
            Euclidean     : { x -> Distance.euclidean() },
//            "Minkovsky[3]": { x -> Distance.minkowsky(3) },
//            "Minkovsky[5]": { x -> Distance.minkowsky(5) },
//            Chebyshev     : { x -> Distance.chebyshev() },
        ],
        input   : [
            constant    : ResultInput.constant,
            proportional: ResultInput.proportional,
            weightedWithDistance: ResultInput.weightedWithDistance
        ]
    ]
    println new Document().withContent { content ->
        content.add new Experiment<Evaluation>(
            name: "all_knn",
            configYield: new FullSearchYield<Evaluation>(params, domains, values)
        ).runWith(new SingleThreadExperimentRunner<Evaluation>()) { OrderedMap<String> key, OrderedMap config ->
            DataSet clean = config["dataSet"].cleanSet
            def knn = new Knn(config["input"], config["k"], config["distance"](clean))
            def out = CrossValidation.evaluate(knn, clean, config["folds"])
            return out
        }.table(
            ["weightedPrecision", "weightedRecall", "weightedFMeasure"],
            Experiment.propertyExtractors //move it somewhere where it makes sense
        ).toLaTeX()
    }.render()
}