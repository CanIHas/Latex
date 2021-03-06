package can.i.has.grex.experiments.example.model.discretization

import can.i.has.grex.experiments.example.model.DataSet

import groovy.transform.Canonical

@Canonical
class RangeDiscretizer extends AbstractDiscretizer {

    int rangeNumber

    @Override
    List<Number> getCuts(int attrIdx, DataSet dataSet) {
        assert dataSet.scheme.isNumericalAttribute(attrIdx)

        List<Number> vals = dataSet.getAttributeSnapshot(attrIdx).sort()
        List<Number> out = []

        Number rangeSpan = (vals.last() - vals.first()) / rangeNumber
        Number current = vals.first()
        (rangeNumber-1).times {
            current += rangeSpan
            out.add(current)
        }
        out
    }
}
