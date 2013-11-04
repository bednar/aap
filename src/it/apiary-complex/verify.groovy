import difflib.*

apiaryExpected = new File(basedir, "ApiaryExpected.md")
assert apiaryExpected.exists()

apiaryResult = new File(basedir, "target/generated/apiary/Apiary.md")
assert apiaryResult.exists()

Patch patch = DiffUtils.diff(apiaryExpected.readLines(), apiaryResult.readLines())

println patch.getDeltas().size()

patch.getDeltas().each {
    println it
}

assert patch.getDeltas().size() == 0