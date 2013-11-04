import difflib.*

apiaryExpected = new File(basedir, "ApiaryExpected.md")
assert apiaryExpected.exists()

println '\n Expected: '
println apiaryExpected.getText()

apiaryResult = new File(basedir, "target/generated/apiary/Apiary.md")
assert apiaryResult.exists()

println '\n Result: '
println apiaryResult.getText()

Patch patch = DiffUtils.diff(apiaryExpected.readLines(), apiaryResult.readLines())

println patch.getDeltas().size()

patch.getDeltas().each {
    println it
}

assert patch.getDeltas().size() == 0
assert apiaryExpected.getText().equals(apiaryResult.getText())