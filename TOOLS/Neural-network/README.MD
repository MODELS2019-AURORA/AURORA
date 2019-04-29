
================================

bGrams

runner.run('bGrams/CO2/Round1', 6895, 10, 9, 300, 3.0, 0.75)
runner.run('bGrams/CO3/Round1', 3554, 10, 9, 300, 3.0, 0.75)
runner.run('bGrams/CO4/Round1', 2665, 10, 9, 300, 3.0, 0.75)
runner.run('bGrams/CO6/Round1', 1747, 10, 9, 300, 3.0, 0.75)
runner.run('bGrams/CO8/Round1', 1174, 10, 9, 300, 3.0, 0.75)

=== Re-run to compute Precision/Recall

runner.rerun('bGrams/CO2/Round1', 6895, 10, 9, 20, 3.0, 0.75)
runner.rerun('bGrams/CO4/Round1', 2665, 10, 9, 20, 3.0, 0.75)
runner.rerun('bGrams/CO6/Round1', 1747, 10, 9, 20, 3.0, 0.75)
runner.rerun('bGrams/CO8/Round1', 1174, 10, 9, 20, 3.0, 0.75)

================================

nGrams

runner.run('nGrams/CO2/Round1', 10837, 10, 9, 300, 3.0, 0.75)
runner.run('nGrams/CO4/Round1', 4166, 10, 9, 300, 3.0, 0.75)
runner.run('nGrams/CO6/Round1', 2749, 10, 9, 300, 3.0, 0.75)
runner.run('nGrams/CO8/Round1', 1733, 10, 9, 300, 3.0, 0.75)
runner.run('nGrams/CO9/Round1', 1409, 10, 9, 300, 3.0, 0.75)

=== Re-run to compute Precision/Recall

runner.rerun('nGrams/CO2/Round1', 10837, 10, 9, 30, 3.0, 0.75)
runner.rerun('nGrams/CO4/Round1', 4166, 10, 9, 20, 3.0, 0.75)
runner.rerun('nGrams/CO6/Round1', 2749, 10, 9, 30, 3.0, 0.75)
runner.rerun('nGrams/CO8/Round1', 1733, 10, 9, 40, 3.0, 0.75)

================================

No Structure (Uni-gram)

runner.run('NoStructure/CO2/Round1', 4356, 10, 9, 300, 3.0, 0.75)
runner.run('NoStructure/CO4/Round1', 2412, 10, 9, 300, 3.0, 0.75)
runner.run('NoStructure/CO6/Round1', 1715, 10, 9, 300, 3.0, 0.75)
runner.run('NoStructure/CO8/Round1', 1240, 10, 9, 300, 3.0, 0.75)
runner.run('NoStructure/CO9/Round1', 1012, 10, 9, 300, 3.0, 0.75)


=== Re-run to compute Precision/Recall


runner.rerun('NoStructure/CO2/Round1', 4356, 10, 9, 300, 3.0, 0.75)
runner.rerun('NoStructure/CO4/Round1', 2412, 10, 9, 300, 3.0, 0.75)
runner.rerun('NoStructure/CO6/Round1', 1715, 10, 9, 300, 3.0, 0.75)
runner.rerun('NoStructure/CO8/Round1', 1240, 10, 9, 300, 3.0, 0.75)



================================


